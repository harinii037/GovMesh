package com.govmesh.backend.governance;

public record PolicyDecision(
        boolean allowed,
        String reason
) {
}
