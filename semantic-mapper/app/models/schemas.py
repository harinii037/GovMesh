from pydantic import BaseModel, field_validator
from typing import Dict, List, Optional


class SchemaMapRequest(BaseModel):
    sourceSchema: Dict
    targetSchema: Dict

    @field_validator("sourceSchema", "targetSchema")
    @classmethod
    def validate_schema(cls, value):
        if not isinstance(value, dict):
            raise ValueError("Schema must be an object")

        if not value:
            raise ValueError("Schema cannot be empty")

        for field_name, field_type in value.items():

            if not isinstance(field_name, str) or not field_name.strip():
                raise ValueError("Field names must be non-empty strings")

            if not isinstance(field_type, str):
                raise ValueError(
                    f"Data type for field '{field_name}' must be a string"
                )

        return value


class MappingSuggestion(BaseModel):
    source: str
    target: Optional[str]
    confidence: float
    status: str


class SchemaMapResponse(BaseModel):
    suggestions: List[MappingSuggestion]