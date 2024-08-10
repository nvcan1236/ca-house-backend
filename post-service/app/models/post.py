import uuid

from pydantic import BaseModel, Field


class Post(BaseModel):
    id: str = Field(default_factory=uuid.uuid4, alias="_id")
    content: str = Field(...)

    class Config:
        json_schema_extra = {
            "example": {
                "content": "Post content"
            }
        }
