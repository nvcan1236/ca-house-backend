from fastapi import status, APIRouter, Depends
from fastapi.encoders import jsonable_encoder
from fastapi.responses import JSONResponse

from app.configs.databases import post_collection, comment_collection
from app.models.comment import Comment, CommentUpdate
from app.models.response import ApiResponse
from app.serializers.post import decode_comments, decode_comment
from app.utils import get_jwt_claim, JWTClaim, oauth2_scheme

router = APIRouter()


@router.post("/{post_id}/comment")
async def add_comment(comment: CommentUpdate, post_id: str, token: str = Depends(oauth2_scheme)):
    user_id = get_jwt_claim(token=token, claim=JWTClaim.SUBJECT)
    comment_create = Comment(**comment.dict(), post_id=post_id, user_id=user_id)
    doc = jsonable_encoder(comment_create)
    new_comment = await comment_collection.insert_one(doc)
    inserted_comment = await comment_collection.find_one({"_id": new_comment.inserted_id})

    return JSONResponse(status_code=status.HTTP_201_CREATED,
                        content=ApiResponse(1000, decode_comment(inserted_comment), None))


@router.get("/{post_id}/comment")
async def get_comment(post_id: str):
    post = post_collection.find_one({"_id": post_id})
    if not post:
        return JSONResponse(status_code=status.HTTP_404_NOT_FOUND,
                            content=ApiResponse(1000, None, "Post not found"))

    docs = await comment_collection.find({"post_id": post_id}).sort("created_at", 1).to_list(length=10)
    return JSONResponse(status_code=status.HTTP_200_OK,
                        content=ApiResponse(1000, decode_comments(docs), None))


@router.patch("/{post_id}/comment/{comment_id}")
async def update_comment(comment: CommentUpdate, post_id: str, comment_id: str):
    post = post_collection.find_one({"_id": post_id})
    if not post:
        return JSONResponse(status_code=status.HTTP_404_NOT_FOUND,
                            content=ApiResponse(1000, None, "Post not found"))

    doc = comment.model_dump(exclude_unset=True)
    comment = await comment_collection.find_one_and_update({"_id": comment_id}, {"$set": doc})
    if not comment:
        return JSONResponse(status_code=status.HTTP_404_NOT_FOUND,
                            content=ApiResponse(1000, None, "Comment not found"))
    return JSONResponse(status_code=status.HTTP_200_OK,
                        content=ApiResponse(1000, comment, None))


@router.delete("/{post_id}/comment/{comment_id}")
async def delete_comment(post_id: str, comment_id: str):
    post = post_collection.find_one({"_id": post_id})
    if not post:
        return JSONResponse(status_code=status.HTTP_404_NOT_FOUND,
                            content=ApiResponse(1000, None, "Post not found"))

    comment = await comment_collection.find_one_and_delete({"_id": comment_id})
    if not comment:
        return JSONResponse(status_code=status.HTTP_404_NOT_FOUND,
                            content=ApiResponse(1000, None, "Comment not found"))
    return JSONResponse(status_code=status.HTTP_204_NO_CONTENT,
                        content=ApiResponse(1000, None, "Comment deleted"))
