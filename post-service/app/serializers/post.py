def decode_post(doc) -> dict:
    if doc:
        return {
            "content": doc['content'],
            "id": doc['_id'],
            "type": doc['type']
        }
    return {}


def decode_posts(docs) -> list:
    return [decode_post(doc) for doc in docs]


def decode_comment(doc) -> dict:
    if doc:
        return {
            "id": doc['_id'],
            "content": doc['content'],
            "create_at": doc['create_at'],
        }


def decode_comments(docs) -> list:
    return [decode_comment(doc) for doc in docs]
