import os
import json
from dotenv import load_dotenv
from google import genai
from pydantic import BaseModel

load_dotenv()


class LLMSuggestion(BaseModel):
    source: str
    target: str | None
    confidence: float
    reason: str


class LLMMappingResponse(BaseModel):
    suggestions: list[LLMSuggestion]


class LLMMapper:

    def __init__(self):
        api_key = os.getenv("GEMINI_API_KEY")

        if not api_key:
            raise ValueError("GEMINI_API_KEY not found in .env")

        self.client = genai.Client(api_key=api_key)

    def find_mappings(self, source_schema: dict, target_schema: dict):

        prompt = f"""
You are the semantic schema mapping engine for GovMesh.

SOURCE SCHEMA:
{json.dumps(source_schema, indent=2)}

TARGET SCHEMA:
{json.dumps(target_schema, indent=2)}

Your task is to identify the best semantic target field
for every source field.

Rules:

1. Understand the meaning of fields rather than relying only
   on exact names.

2. Handle:
   - snake_case
   - camelCase
   - abbreviations
   - synonyms
   - different naming conventions

3. Consider the data type.

4. A source field should only map to a target field when the
   semantic relationship is reasonable.

5. If there is no suitable target, return null as the target.

6. Do NOT use a hardcoded mapping table.

7. Confidence must be a number between 0 and 1.

8. Confidence represents how strongly you believe the mapping
   is semantically correct.

9. Return exactly one result for every source field.

10. Only use target fields that actually exist in the target schema.

11. Provide a short reason for every mapping.
"""

        interaction = self.client.interactions.create(
            model="gemini-3.6-flash",
            input=prompt,
            response_format={
                "type": "text",
                "mime_type": "application/json",
                "schema": LLMMappingResponse.model_json_schema()
            }
        )

        result = LLMMappingResponse.model_validate_json(
            interaction.output_text
        )

        valid_suggestions = []

        for suggestion in result.suggestions:

            source = suggestion.source
            target = suggestion.target

            # Source validation
            if source not in source_schema:
                continue

            # Confidence normalization
            confidence = max(
                0.0,
                min(1.0, suggestion.confidence)
            )

            # No suitable target
            if target is None:
                status = "REJECT"

                valid_suggestions.append({
                    "source": source,
                    "target": None,
                    "confidence": round(confidence, 4),
                    "status": status
                })

                continue

            # Target validation
            if target not in target_schema:
                status = "REJECT"
                target = None

            elif confidence >= 0.85:
                status = "AUTO_MATCH"

            elif confidence >= 0.60:
                status = "REVIEW"

            else:
                status = "REJECT"

            valid_suggestions.append({
                "source": source,
                "target": target,
                "confidence": round(confidence, 4),
                "status": status
            })

        return valid_suggestions