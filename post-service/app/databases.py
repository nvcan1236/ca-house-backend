import motor.motor_asyncio

MONGO_DETAILS = "mongodb://localhost:27017/"

client = motor.motor_asyncio.AsyncIOMotorClient(MONGO_DETAILS)
database = client.get_database("ca_house_post")
student_collection = database.get_collection("post")