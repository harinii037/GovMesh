from pydantic import BaseModel
from typing import Dict, List


class SchemaMapRequest(BaseModel):
    sourceSchema: Dict
    targetSchema: Dict


class MappingSuggestion(BaseModel):
    source: str
    target: str
    confidence: float


class SchemaMapResponse(BaseModel):
    suggestions: List[MappingSuggestion]