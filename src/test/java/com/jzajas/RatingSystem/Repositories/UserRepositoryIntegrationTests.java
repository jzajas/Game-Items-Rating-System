package com.jzajas.RatingSystem.Repositories;


import com.jzajas.RatingSystem.Entities.Role;
import com.jzajas.RatingSystem.Entities.Status;
import com.jzajas.RatingSystem.Entities.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.Optional;

import static org.assertj.core.api.Assertions.*;



@DataJpaTest
public class UserRepositoryIntegrationTests {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void findByEmailReturnsUserIfUserExists() {
        String email = "present.user@example.com";
        User user = generateExampleUser(email);

        userRepository.save(user);
        Optional<User> result = userRepository.findByEmail(email);

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(email);
    }

    @Test
    void findByEmailReturnsNothingWhenUserNotExists() {
        String email = "nonexistent@example.com";

        Optional<User> result = userRepository.findByEmail(email);

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void isAdministratorReturnsTrueForAdmin() {
        String email = "admin@example.com";
        User user = generateExampleUser(email);
        user.setRole(Role.ADMINISTRATOR);

        userRepository.save(user);
        boolean result = userRepository.isAdministrator(email);

        assertThat(result).isTrue();
    }

    @Test
    void isAdministratorReturnsFalseForSeller() {
        String email = "nonadmin@example.com";
        User user = generateExampleUser(email);

        userRepository.save(user);
        boolean result = userRepository.isAdministrator(email);

        assertThat(result).isFalse();
    }

    @Test
    void findUserWithApprovedStatusReturnsUserWhenStatusApproved() {
        String email = "approved.user@example.com";
        User user = generateExampleUser(email);
        user.setStatus(Status.APPROVED);
        user.setRole(Role.SELLER);

        User saved = userRepository.save(user);
        Optional<User> result = userRepository.findUserWithApprovedStatus(saved.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(email);
    }

    @Test
    void findUserWithApprovedStatusReturnsNothingForUnapprovedUsers() {
        String email = "unapproved.user@example.com";
        User user = generateExampleUser(email);

        User saved = userRepository.save(user);
        Optional<User> result = userRepository.findUserWithApprovedStatus(saved.getId());

        assertThat(result).isEmpty();
    }

    private User generateExampleUser(String email) {
        User user = new User();

        user.setFirstName("Example");
        user.setLastName("User");
        user.setPassword("password");
        user.setEmail(email);

        return user;
    }
}
