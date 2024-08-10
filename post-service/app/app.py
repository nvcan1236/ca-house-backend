from fastapi import FastAPI

from app.databases import client, database
from app.routes.post import router as post_router

app = FastAPI()


@app.get("/")
def read_root():
    return {"Hello": "World"}


@app.on_event("startup")
async def startup_db_client():
    app.mongodb_client = client
    app.mongodb = database


@app.on_event("shutdown")
async def shutdown_db_client():
    app.mongodb_client.close()


app.include_router(post_router, tags=["POST"], prefix='/post')
