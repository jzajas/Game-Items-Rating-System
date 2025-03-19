package com.jzajas.RatingSystem.Services;

import com.jzajas.RatingSystem.DTO.Input.UserRegistrationDTO;
import com.jzajas.RatingSystem.Entities.User;
import com.jzajas.RatingSystem.Mappers.DTOMapper;
import com.jzajas.RatingSystem.Repositories.UserRepository;
import com.jzajas.RatingSystem.Services.Implementations.AuthServiceImpl;
import com.jzajas.RatingSystem.Services.Implementations.EmailServiceImpl;
import com.jzajas.RatingSystem.Services.Implementations.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private AuthServiceImpl authServiceImpl;

    @Mock
    private EmailServiceImpl emailServiceImpl;

    @Mock
    private DTOMapper mapper;

    @Test
    void userCanBeFoundByEmail() {
        String email = "test.example@email.com";
        User user = new User();
        user.setEmail(email);

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> optionalUser = userRepository.findByEmail(email);
        assertThat(optionalUser).isPresent();
        assertThat(optionalUser.get().getEmail()).isEqualTo(email);
    }

    @Test
    void mapperProperlyMapsDTOtoUser() {
        UserRegistrationDTO dto = new UserRegistrationDTO("John", "Doe",
                "password", "john@doe@example.com");

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password");
        user.setEmail("john@doe@example.com");

        Mockito.when(mapper.convertFromUserRegistrationDTOtoUser(dto)).thenReturn(user);

        User mappedUser = mapper.convertFromUserRegistrationDTOtoUser(dto);

        assertEquals(mappedUser.getFirstName(), dto.getFirstName());
        assertEquals(mappedUser.getLastName(), dto.getLastName());
        assertEquals(mappedUser.getEmail(), dto.getEmail());
        assertEquals(mappedUser.getPassword(), dto.getPassword());
    }

    @Test
    void passwordEncodingWorksProperly() {
        User user = new User();
        String password = "password";
        String encodedPassword = "encodedPassword";

        Mockito.when(encoder.encode(password)).thenReturn(encodedPassword);
        user.setPassword(encoder.encode(password));

        assertEquals(user.getPassword(), encodedPassword);
    }

    @Test
    void userCreationWorksProperly() {
        String email = "john@doe@example.com";
        String password = "password";
        String encodedPassword = "encodedPassword";
        UserRegistrationDTO dto = new UserRegistrationDTO("John", "Doe", password, email);
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());


        Mockito.when(encoder.encode(password)).thenReturn(encodedPassword);
        Mockito.when(mapper.convertFromUserRegistrationDTOtoUser(dto)).thenReturn(user);
        Mockito.when(authServiceImpl.createAndSaveCreationToken(user.getEmail())).thenReturn("token");

        userServiceImpl.createNewUser(dto);

        Mockito.verify(userRepository).save(user);
        Mockito.verify(encoder).encode(password);
        Mockito.verify(authServiceImpl).createAndSaveCreationToken(email);
        Mockito.verify(emailServiceImpl).sendVerificationEmail(email, "token");
        assertEquals(encodedPassword, user.getPassword());
    }
}
