import motor.motor_asyncio
from pymongo import ASCENDING

MONGO_DETAILS = "mongodb://localhost:27017/"

client = motor.motor_asyncio.AsyncIOMotorClient(MONGO_DETAILS)
database = client.get_database("ca_house_post")

post_collection = database.get_collection("post")
comment_collection = database.get_collection("comment")
react_collection = database.get_collection("react")
image_collection = database.get_collection("image")

react_collection.create_index([("post_id", ASCENDING), ("user_id", ASCENDING)], unique=True)