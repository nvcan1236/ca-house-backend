from fastapi import FastAPI, HTTPException
from fastapi import Request
from fastapi.responses import JSONResponse

from app.configs.databases import client, database
from app.models.response import ApiResponse
from app.routes.post import router as post_router
from app.routes.comment import router as comment_router
from app.routes.react import router as react_router

app = FastAPI(root_path="/post")

app.include_router(comment_router)
app.include_router(react_router)
app.include_router(post_router)


@app.on_event("startup")
async def startup_db_client():
    app.mongodb_client = client
    app.mongodb = database


@app.on_event("shutdown")
async def shutdown_db_client():
    app.mongodb_client.close()


# exception handler
@app.exception_handler(HTTPException)
async def http_exception(request: Request, exc: HTTPException):
    return JSONResponse(content=ApiResponse(code=400, message=exc.detail, result=None),
                        status_code=exc.status_code)
