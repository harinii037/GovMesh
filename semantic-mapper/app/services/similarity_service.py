import re
from sentence_transformers import SentenceTransformer
from sklearn.metrics.pairwise import cosine_similarity


class SimilarityService:

    def __init__(self):
        print("Loading Sentence Transformer model...")
        self.model = SentenceTransformer("all-MiniLM-L6-v2")
        print("Model loaded successfully.")

    def normalize_field(self, field_name: str) -> str:
        # Convert snake_case to spaces
        field_name = field_name.replace("_", " ")

        # Convert camelCase to separate words
        field_name = re.sub(r"([a-z])([A-Z])", r"\1 \2", field_name)

        # Remove extra spaces
        field_name = re.sub(r"\s+", " ", field_name).strip()

        return field_name

    def find_mappings(self, source_schema: dict, target_schema: dict):

        source_fields = list(source_schema.keys())
        target_fields = list(target_schema.keys())

        if not source_fields or not target_fields:
            return []

        normalized_sources = [
            self.normalize_field(field)
            for field in source_fields
        ]

        normalized_targets = [
            self.normalize_field(field)
            for field in target_fields
        ]

        source_embeddings = self.model.encode(normalized_sources)
        target_embeddings = self.model.encode(normalized_targets)

        suggestions = []

        for i, source_field in enumerate(source_fields):

            similarities = cosine_similarity(
                [source_embeddings[i]],
                target_embeddings
            )[0]

            best_index = similarities.argmax()
            best_score = float(similarities[best_index])

            suggestions.append({
                "source": source_field,
                "target": target_fields[best_index],
                "confidence": round(best_score, 4)
            })

        return suggestions