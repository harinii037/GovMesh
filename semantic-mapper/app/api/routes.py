from fastapi import APIRouter, HTTPException
from pydantic import ValidationError

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

    except ValueError as e:

        raise HTTPException(
            status_code=400,
            detail=str(e)
        )

    except Exception as e:

        raise HTTPException(
            status_code=500,
            detail="Internal server error"
        )