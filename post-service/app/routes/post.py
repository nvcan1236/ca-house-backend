from fastapi import Request, Body, status, APIRouter, Depends, HTTPException
from fastapi.encoders import jsonable_encoder
from fastapi.responses import JSONResponse
from pydantic import BaseModel

from app.kafka import producer
from app.models.post import Post
from app.models.response import ApiResponse
from app.utils import require_admin_scope

router = APIRouter()


class NotificationEvent(BaseModel):
    chanel: str
    recipient: str
    subject: str
    body: str


@router.get("/all")
async def get_all_posts(request: Request, _=Depends(require_admin_scope)):
    try:
        notification = NotificationEvent(
            chanel="EMAIL",
            recipient="canh@yopmail.com",
            subject="Welcome to CaHouse",
            body="Bạn vừa đăng ký tài khoản tại ca-house với username: example_user"
        )
        message = notification.json()
        producer.produce('notification-delivery', key="notification", value=message)
        producer.flush()
        posts = []
        for doc in await request.app.mongodb["post"].find().to_list(length=10):
            posts.append(doc)
        return JSONResponse(status_code=status.HTTP_201_CREATED, content=ApiResponse(1000, posts, "OK"))
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Failed to send message: {str(e)}"
        )


@router.post("/")
async def create_post(request: Request, post: Post = Body()):
    post = jsonable_encoder(post)
    new_post = await request.app.mongodb['post'].insert_one(post)

    created_post = await request.app.mongodb["post"].find_one(
        {"_id": new_post.inserted_id}
    )

    return JSONResponse(status_code=status.HTTP_201_CREATED, content=ApiResponse(1000, created_post, "Created"))
