package com.govmesh.governance;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/test/policies")
public class PolicyTestController {
    private final ConsentPolicyRepository repository;

    public PolicyTestController(ConsentPolicyRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<ConsentPolicy> getPolicies() {
        return repository.findAll();
    }
}
