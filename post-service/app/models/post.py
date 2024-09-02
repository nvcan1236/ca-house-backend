import datetime
import uuid
from enum import Enum
from typing import Optional

from pydantic import BaseModel, Field


class PostType(str, Enum):
    REVIEW = "REVIEW",
    PASS_ROOM = "PASS_ROOM"
    FIND_ROOM = "FIND_ROOM"


class MyBaseModel(BaseModel):
    id: str = Field(default_factory=uuid.uuid4, alias="_id")
    create_at: str = Field(default_factory=datetime.datetime.utcnow)


class PostCreate(BaseModel):
    content: str
    type: PostType


class PostUpdate(BaseModel):
    content: str
    type: PostType


class Post(PostCreate, MyBaseModel):
    create_by: str


class Image(BaseModel):
    post_id: str
    id: str = Field(default_factory=uuid.uuid4, alias="_id")
    url: str
