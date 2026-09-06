package com.govmesh.backend.mapping;

public record MappingSuggestion(String source, String target, double confidence, MappingStatus status) {
}