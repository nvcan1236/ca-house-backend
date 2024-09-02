from fastapi import status, APIRouter, Depends, HTTPException, Body, UploadFile, File
from fastapi.encoders import jsonable_encoder
from fastapi.responses import JSONResponse

from app.configs.databases import post_collection, image_collection
from app.models.post import Post, PostUpdate, PostCreate, Image
from app.models.response import ApiResponse
from app.serializers.post import decode_post, decode_posts
from app.utils import require_admin_scope, oauth2_scheme, get_jwt_claim, JWTClaim, upload_images

router = APIRouter()


@router.get("/")
async def get_all_posts(_=Depends(require_admin_scope), offset: int = 0, limit: int = 10, search: str = ""):
    try:
        query = {}
        if search:
            query = {"content": {
                "$regex": search,
                "$options": "i"
            }}

        docs = await post_collection.find(query).skip(offset).limit(limit).to_list(length=10)
        posts = decode_posts(docs)
        return JSONResponse(status_code=status.HTTP_200_OK, content=ApiResponse(1000, posts, "OK"))

    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Failed to send message: {str(e)}"
        )


@router.get("/{post_id}")
async def get_post_by_id(post_id: str):
    doc = await post_collection.find_one(
        {"_id": post_id}
    )
    post = decode_post(doc)
    return JSONResponse(status_code=status.HTTP_200_OK, content=ApiResponse(1000, post, message=None))


@router.post("/")
async def create_post(post_create: PostCreate = Body(), token: str = Depends(oauth2_scheme)):
    post: Post = Post(**post_create.dict(), create_by=get_jwt_claim(token, JWTClaim.SUBJECT))
    post = jsonable_encoder(post)
    new_post = await post_collection.insert_one(post)

    created_post = await post_collection.find_one(
        {"_id": new_post.inserted_id}
    )

    return JSONResponse(status_code=status.HTTP_201_CREATED, content=ApiResponse(1000, created_post, "Created"))


@router.patch("/{post_id}")
async def update_post(post_id: str, post: PostUpdate):
    doc = post.model_dump(exclude_unset=True)
    post_collection.find_one_and_update(
        {"_id": post_id},
        {"$set": doc}
    )
    return JSONResponse(status_code=status.HTTP_200_OK, content=ApiResponse(1000, None, "Created"))


@router.delete("/{post_id}")
async def delete_post(post_id: str):
    post_collection.find_one_and_delete(
        {"_id": post_id}
    )
    return JSONResponse(status_code=status.HTTP_200_OK, content=ApiResponse(1000, None, "Deleted"))


@router.post("/{post_id}/images/")
async def upload_image(post_id: str, images: list[UploadFile] = File(...)):
    post = post_collection.find_one({"_id": post_id})
    if not post:
        return JSONResponse(status_code=status.HTTP_404_NOT_FOUND,
                            content=ApiResponse(1000, None, "Post not found"))

    urls = await upload_images(images)
    if urls:
        for url in urls:
            image: Image = Image(url=url, post_id=post_id)
            var = image_collection.insert_one(jsonable_encoder(image))
        return JSONResponse(status_code=status.HTTP_200_OK,
                            content=ApiResponse(1000, None, "Upload successfully"))

    return JSONResponse(status_code=status.HTTP_200_OK,
                        content=ApiResponse(1000, None, "Upload fail"))
