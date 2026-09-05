from fastapi import FastAPI, Request
from fastapi.exceptions import RequestValidationError
from fastapi.responses import JSONResponse

from app.api.routes import router


app = FastAPI(
    title="GovMesh Semantic Mapper",
    description="AI-powered semantic schema mapping service",
    version="1.0.0"
)


@app.exception_handler(RequestValidationError)
async def validation_exception_handler(
    request: Request,
    exc: RequestValidationError
):
    return JSONResponse(
        status_code=400,
        content={
            "detail": "Invalid request schema",
            "errors": exc.errors()
        }
    )


app.include_router(router)


@app.get("/health")
def health():
    return {
        "status": "UP"
    }