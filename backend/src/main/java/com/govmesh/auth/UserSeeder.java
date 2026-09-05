package com.govmesh.auth;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserSeeder {
    @Bean
    CommandLineRunner seedUsers(UserRepository repository, PasswordEncoder encoder) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new User("emp1", encoder.encode("emp123"), Role.EMPLOYMENT_OFFICER));
                repository.save(new User("wel1", encoder.encode("wel123"), Role.WELFARE_OFFICER));
                repository.save(new User("admin", encoder.encode("admin123"), Role.ADMIN));
                System.out.println("M5: seeded emp1, wel1 and admin.");
            }
        };
    }
}
