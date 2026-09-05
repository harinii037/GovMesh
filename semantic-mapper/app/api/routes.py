from fastapi import APIRouter, HTTPException

from app.models.schemas import (
    SchemaMapRequest,
    SchemaMapResponse
)

from app.services.llm_mapper import LLMMapper


router = APIRouter()

llm_mapper = LLMMapper()


@router.post("/semantic-map", response_model=SchemaMapResponse)
def semantic_map(request: SchemaMapRequest):

    try:

        suggestions = llm_mapper.find_mappings(
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