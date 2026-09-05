package com.govmesh.backend.governance;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PolicySeeder {
    @Bean
    CommandLineRunner seedPolicies(ConsentPolicyRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new ConsentPolicy(
                        "employment", "welfare", "employment-data", true));

                repository.save(new ConsentPolicy(
                        "welfare", "employment", "employment-data", false));

                System.out.println("M5: consent policies seeded.");
            }
        };
    }
}
