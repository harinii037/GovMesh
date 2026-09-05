from pydantic import BaseModel
from typing import Dict, List, Optional


class SchemaMapRequest(BaseModel):
    sourceSchema: Dict
    targetSchema: Dict


class MappingSuggestion(BaseModel):
    source: str
    target: Optional[str]
    confidence: float
    status: str


class SchemaMapResponse(BaseModel):
    suggestions: List[MappingSuggestion]