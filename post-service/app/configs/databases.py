import motor.motor_asyncio
from pymongo import ASCENDING
import urllib.parse

username = urllib.parse.quote_plus("root")
password = urllib.parse.quote_plus("Moca@1236")

# MONGO_DETAILS = f"mongodb://{username}:{password}@localhost:27018/ca_house_post?authSource=admin"
MONGO_DETAILS = f"mongodb://{username}:{password}@mongodb_post_chat:27017/ca_house_post?authSource=admin"

client = motor.motor_asyncio.AsyncIOMotorClient(MONGO_DETAILS)
database = client.get_database("ca_house_post")

post_collection = database.get_collection("post")
comment_collection = database.get_collection("comment")
react_collection = database.get_collection("react")
image_collection = database.get_collection("image")

react_collection.create_index([("post_id", ASCENDING), ("user_id", ASCENDING)], unique=True)