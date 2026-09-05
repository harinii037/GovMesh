package com.govmesh.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class DepartmentAuthorizationService {

    public boolean canActForSourceDepartment(Authentication authentication,
                                             String sourceDept) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String role = authentication.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .findFirst()
                .orElse("");

        if ("ROLE_ADMIN".equals(role)) {
            return true;
        }

        if ("ROLE_EMPLOYMENT_OFFICER".equals(role)) {
            return "employment".equalsIgnoreCase(sourceDept);
        }

        if ("ROLE_WELFARE_OFFICER".equals(role)) {
            return "welfare".equalsIgnoreCase(sourceDept);
        }

        return false;
    }
}
