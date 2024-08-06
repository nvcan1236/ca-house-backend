package com.nvc.user_service.configuration;

import com.nvc.user_service.entity.Role;
import com.nvc.user_service.entity.User;
import com.nvc.user_service.repository.RoleRepository;
import com.nvc.user_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository,
                                        RoleRepository roleRepository) {
        return args -> {
            if(roleRepository.findById("ADMIN").isEmpty()) {
                Role roleAdmin = new Role();
                roleAdmin.setName("ADMIN");
                roleAdmin.setDescription("Full quyền");

                roleRepository.save(roleAdmin);
            }

            if (userRepository.findByUsername("admin").isEmpty()) {
                User user = new User();
                user.setUsername("admin");
                user.setPassword(passwordEncoder.encode("admin"));
                user.setRoles(Set.of(roleRepository.findById("ADMIN").orElseThrow()));
                userRepository.save(user);

                log.warn("An admin account has been created with username 'admin' and default password 'admin'." +
                        " So please change the password!!");
            }
        };
    }
}
