from fastapi import APIRouter, HTTPException

from app.models.schemas import (
    SchemaMapRequest,
    SchemaMapResponse
)

from app.services.similarity_service import SimilarityService


router = APIRouter()

similarity_service = SimilarityService()


@router.post("/semantic-map", response_model=SchemaMapResponse)
def semantic_map(request: SchemaMapRequest):

    try:
        suggestions = similarity_service.find_mappings(
            request.sourceSchema,
            request.targetSchema
        )

        return {
            "suggestions": suggestions
        }

    except Exception as e:
        raise HTTPException(
            status_code=500,
            detail=str(e)
        )