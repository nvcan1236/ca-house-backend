from typing import Optional

from bson import SON
from fastapi import status, APIRouter, Depends, UploadFile, File, HTTPException
from fastapi.encoders import jsonable_encoder
from fastapi.responses import JSONResponse
from fastapi.security import OAuth2PasswordBearer

from app.configs.databases import post_collection, image_collection
from app.configs.openai import suggest_post_content, Requirement
from app.models.post import Post, PostUpdate, PostCreate, Image
from app.models.response import ApiResponse
from app.serializers.post import decode_post, decode_posts
from app.utils import get_jwt_claim, JWTClaim, upload_images, check_owner_permission

router = APIRouter()
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token", auto_error=False)


async def get_optional_token(token: Optional[str] = Depends(oauth2_scheme)):
    return token


@router.get("/")
async def get_all_posts(token: str = Depends(get_optional_token)
                        , offset: int = 0
                        , limit: int = 10
                        , search: str = ""):
    query = {}
    if search:
        query = {"content": {
            "$regex": search,
            "$options": "i"
        }}

    docs = await post_collection.find(query).sort("create_at", -1).skip(offset).limit(limit).to_list(length=10)
    if not token:
        token = None
    posts = await decode_posts(docs, token)
    return JSONResponse(status_code=status.HTTP_200_OK, content=ApiResponse(1000, posts, "OK"))


@router.get("/user/{user_id}")
async def get_post_by_user(token: str = Depends(get_optional_token),
                           offset: int = 0,
                           limit: int = 10,
                           user_id: str = ""
                           ):
    query = {"create_by": user_id}
    docs = await post_collection.find(query).sort("create_at", -1).skip(offset).limit(limit).to_list(length=10)
    if not token:
        token = None
    posts = await decode_posts(docs, token)
    return JSONResponse(status_code=status.HTTP_200_OK, content=ApiResponse(1000, posts, "OK"))


@router.get("/{post_id}")
async def get_post_by_id(post_id: str):
    doc = await post_collection.find_one(
        {"_id": post_id}
    )
    post = await decode_post(doc)
    return JSONResponse(status_code=status.HTTP_200_OK, content=ApiResponse(1000, post, message=None))


@router.post("/")
async def create_post(post_create: PostCreate
                      , token: str = Depends(oauth2_scheme)):
    post: Post = Post(**post_create.dict(), create_by=get_jwt_claim(token, JWTClaim.SUBJECT))
    post = jsonable_encoder(post)
    new_post = await post_collection.insert_one(post)
    created_post = await post_collection.find_one(
        {"_id": new_post.inserted_id}
    )
    created_post = await decode_post(created_post)
    return JSONResponse(status_code=status.HTTP_201_CREATED,
                        content=ApiResponse(1000, created_post, "Created"))


@router.patch("/{post_id}")
async def update_post(post_id: str, post: PostUpdate, token: str = Depends(oauth2_scheme)):
    doc = post.model_dump(exclude_unset=True, exclude_none=True)
    post_collection.find_one_and_update(
        {"_id": post_id},
        {"$set": doc}
    )
    updated_post = await post_collection.find_one(
        {"_id": post_id}
    )
    check_owner_permission(token, updated_post['create_by'])
    return JSONResponse(status_code=status.HTTP_200_OK, content=ApiResponse(1000, updated_post, "Created"))


@router.delete("/{post_id}")
async def delete_post(post_id: str):
    post_collection.find_one_and_delete(
        {"_id": post_id}
    )
    return JSONResponse(status_code=status.HTTP_204_NO_CONTENT
                        , content=ApiResponse(1000
                                              , None, "Deleted"))


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
            image_collection.insert_one(jsonable_encoder(image))
        return JSONResponse(status_code=status.HTTP_200_OK,
                            content=ApiResponse(1000, None, "Upload successfully"))

    return JSONResponse(status_code=status.HTTP_200_OK,
                        content=ApiResponse(1000, None, "Upload fail"))


@router.post("/suggest/")
def suggest_content(requirement: Requirement):
    content = suggest_post_content(requirement).data
    return JSONResponse(status_code=status.HTTP_200_OK,
                        content=ApiResponse(1000, jsonable_encoder(content), "OK"))


@router.get("/stat/")
async def get_post_statistics():
    try:
        pipeline_period = [
            {
                "$addFields": {
                    "create_date": {
                        "$toDate": "$create_at"
                    }
                }
            },
            {
                "$group": {
                    "_id": {
                        "month": {"$month": "$create_date"},
                        "year": {"$year": "$create_date"}
                    },
                    "count": {"$sum": 1}
                }
            },
            {
                "$sort": {
                    "_id.year": 1,
                    "_id.month": 1
                }
            }
        ]

        stats_period = []
        async for stat in post_collection.aggregate(pipeline_period):
            stats_period.append({
                "period": f'{stat["_id"]["month"]}/{stat["_id"]["year"]}',
                "count": stat["count"]
            })

        pipeline_type = [
            {
                "$group": {
                    "_id": "$type",  # Group by the type field
                    "count": {"$sum": 1}
                }
            },
            {
                "$sort": {
                    "count": -1  # Sort by count in descending order
                }
            }
        ]

        stats_type = []
        async for stat in post_collection.aggregate(pipeline_type):
            stats_type.append({
                "type": stat["_id"],
                "count": stat["count"]
            })

        return JSONResponse(status_code=status.HTTP_200_OK,
                            content=ApiResponse(1000,
                                                {"byPeriod": stats_period, "byType": stats_type},
                                                None))

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"An error occurred: {str(e)}")
