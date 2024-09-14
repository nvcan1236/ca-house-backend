from enum import Enum
from typing import Optional

from pydantic import BaseModel

from app.models.post import MyBaseModel


class Interaction(MyBaseModel):
    post_id: Optional[str]
    user_id: Optional[str]


class Comment(Interaction):
    content: str
    reply_to: Optional[str] = None


class CommentUpdate(BaseModel):
    content: str


class ReactType(str, Enum):
    LIKE = "LIKE"
    LOVE = "LOVE"
    DISLIKE = "DISLIKE"
    HAHA = "HAHA"
    SAD = "SAD"
    ANGRY = "ANGRY"


class Reaction(Interaction):
    type: ReactType

