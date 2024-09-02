import base64
import json
from enum import Enum

from fastapi import HTTPException, status, Depends, UploadFile
from fastapi.security import OAuth2PasswordBearer
from pydantic import BaseModel
import httpx

from app.configs.kafka import producer

oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")


class NotificationEvent(BaseModel):
    chanel: str
    recipient: str
    subject: str
    body: str


def decode_jwt_payload(token: str):
    payload_encoded = token.split('.')[1]

    payload_encoded += '=' * (-len(payload_encoded) % 4)

    payload_decoded = base64.urlsafe_b64decode(payload_encoded).decode('utf-8')

    payload = json.loads(payload_decoded)

    return payload


class JWTClaim(str, Enum):
    ISSUER = "iss"
    SUBJECT = "sub"
    AUDIENCE = "aud"
    EXPIRATION = "exp"
    NOT_BEFORE = "nbf"
    ISSUED_AT = "iat"
    JWT_ID = "jti"
    SCOPE = "scope"
    ROLE = "role"


def get_jwt_claim(token: str | None, claim: JWTClaim):
    payload = decode_jwt_payload(token)
    scope = payload.get(claim, None)
    return scope


def require_admin_scope(token: str = Depends(oauth2_scheme)):
    scope = get_jwt_claim(token, JWTClaim.SCOPE)
    if "ROLE_ADMIN" not in scope.split(" "):
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail="Not enough permissions"
        )


def push_notification():
    notification = NotificationEvent(
        chanel="EMAIL",
        recipient="canh@yopmail.com",
        subject="Welcome to CaHouse",
        body="Bạn vừa đăng ký tài khoản tại ca-house với username: example_user"
    )
    message = notification.json()
    producer.produce('notification-delivery', key="notification", value=message)
    producer.flush()


async def upload_images(files: list[UploadFile]):
    url = "http://localhost:8081/file/upload"

    files_data = [("images", (file.filename, file.file, "image/jpeg")) for file in files]
    data = {"category": "POST_IMAGE"}

    with httpx.Client(timeout=httpx.Timeout(30.0)) as client:
        response = client.post(url, files=files_data, data=data)

    if response.status_code == 200:
        return response.json()
    else:
        return None
