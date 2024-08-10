import base64
import json
from enum import Enum

from fastapi import HTTPException, status, Depends
from fastapi.security import OAuth2PasswordBearer

oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")


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
