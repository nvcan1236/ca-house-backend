from typing import Optional

from fastapi import APIRouter, Depends
from fastapi import Query
from starlette import status
from starlette.responses import JSONResponse

from app.configs.databases import react_collection
from app.models.comment import ReactType
from app.models.response import ApiResponse
from app.utils import get_jwt_claim, oauth2_scheme, JWTClaim

router = APIRouter()


@router.post("/{post_id}/react")
async def react(post_id: str, token: str = Depends(oauth2_scheme), type: Optional[ReactType] = Query(None)):
    user_id = get_jwt_claim(token=token, claim=JWTClaim.SUBJECT)
    reaction = await react_collection.find_one({
        "post_id": post_id,
        "user_id": user_id,
    })
    print(type, reaction)

    if reaction:
        if type:
            react_collection.update_one({
                "post_id": post_id,
                "user_id": user_id,
            }, {
                "$set": {
                    "type": type
                }
            })
        else:
            react_collection.delete_one({
                "post_id": post_id,
                "user_id": user_id,
            })
    else:
        react_collection.insert_one({
            "post_id": post_id,
            "user_id": user_id,
            "type": type
        })

    return JSONResponse(status_code=status.HTTP_200_OK,
                        content=ApiResponse(1000, None, None))
