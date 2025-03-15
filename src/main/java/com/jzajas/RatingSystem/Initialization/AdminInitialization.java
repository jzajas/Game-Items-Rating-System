package com.jzajas.RatingSystem.Initialization;

import com.jzajas.RatingSystem.Entities.Role;
import com.jzajas.RatingSystem.Entities.Status;
import com.jzajas.RatingSystem.Entities.User;
import com.jzajas.RatingSystem.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitialization implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Value("${admin.first_name}")
    private String adminFirstName;

    @Value("${admin.last_name}")
    private String adminLastName;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.email}")
    private String adminEmail;


    public AdminInitialization(PasswordEncoder encoder, UserRepository userRepository) {
        this.encoder = encoder;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = new User();

            admin.setFirstName(adminFirstName);
            admin.setLastName(adminLastName);
            admin.setPassword(encoder.encode(adminPassword));
            admin.setEmail(adminEmail);
            admin.setRole(Role.ADMINISTRATOR);
            admin.setStatus(Status.APPROVED);

            userRepository.save(admin);
        }
    }
}
