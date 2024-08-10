from fastapi import Request, Body, status, APIRouter
from fastapi.encoders import jsonable_encoder
from fastapi.responses import JSONResponse

from app.models.post import Post
from app.models.response import ApiResponse

router = APIRouter()


@router.get("/", response_description="List all posts")
async def get_all_posts(request: Request):
    posts = []
    for doc in await request.app.mongodb["post"].find().to_list(length=10):
        posts.append(doc)
    return JSONResponse(status_code=status.HTTP_201_CREATED, content=ApiResponse(1000, posts, "OK"))


@router.post("/", response_description="Create new post")
async def create_post(request: Request, post: Post = Body()):
    post = jsonable_encoder(post)
    new_post = await request.app.mongodb['post'].insert_one(post)

    created_post = await request.app.mongodb["post"].find_one(
        {"_id": new_post.inserted_id}
    )

    return JSONResponse(status_code=status.HTTP_201_CREATED, content=ApiResponse(1000, created_post, "Created"))
