package com.govmesh.backend.mapping;

import java.util.List;

public record SemanticMapResponse(List<MappingSuggestion> suggestions) {
}