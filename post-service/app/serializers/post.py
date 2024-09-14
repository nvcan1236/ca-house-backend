from app.configs.databases import image_collection, comment_collection, react_collection
from app.utils import get_jwt_claim, JWTClaim


async def decode_post(doc, token=None) -> dict:
    images_docs = await image_collection.find({
        "post_id": doc['_id']
    }).to_list(length=10)
    images = decode_images(images_docs)
    comment_count = await comment_collection.count_documents({"post_id": doc['_id']})
    react_count = await react_collection.count_documents({"post_id": doc['_id']})
    liked = None
    if token:
        create_by = get_jwt_claim(token, JWTClaim.SUBJECT)
        liked_doc = await react_collection.find_one({"post_id": doc['_id'], "user_id": create_by})
        liked = liked_doc['type'] if liked_doc else None
    if doc:
        return {
            "content": doc['content'],
            "id": doc['_id'],
            "type": doc['type'],
            "create_by": doc["create_by"],
            "images": images,
            "comment_count": comment_count,
            "react_count": react_count,
            "liked": liked,
            "create_at": doc["create_at"]
        }
    return {}


async def decode_posts(docs, token=None) -> list:
    return [await decode_post(doc, token) for doc in docs]


def decode_image(doc) -> dict:
    if doc:
        return {
            "id": doc['_id'],
            "url": doc['url'],
        }


def decode_images(docs) -> list:
    return [decode_image(doc) for doc in docs]


def decode_comment(doc) -> dict:
    if doc:
        return {
            "id": doc['_id'],
            "create_at": doc['create_at'],
            "post_id": doc['post_id'],
            "user_id": doc['user_id'],
            "content": doc['content'],
            "reply_to": doc['reply_to'],
        }


def decode_comments(docs) -> list:
    return [decode_comment(doc) for doc in docs]
