from fastapi import FastAPI

from app.api.routes import router


app = FastAPI(
    title="GovMesh Semantic Mapper",
    description="AI-powered semantic schema mapping service",
    version="1.0.0"
)


app.include_router(router)


@app.get("/health")
def health():
    return {
        "status": "UP"
    }