package com.govmesh.governance;

public record PolicyDecision(
        boolean allowed,
        String reason
) {
}
