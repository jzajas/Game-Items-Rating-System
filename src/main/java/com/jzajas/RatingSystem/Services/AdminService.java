package com.jzajas.RatingSystem.Services;

import com.jzajas.RatingSystem.DTO.UserRegistrationDTO;
import com.jzajas.RatingSystem.Entities.User;
import com.jzajas.RatingSystem.Exceptions.EmailAlreadyInUseException;
import com.jzajas.RatingSystem.Mappers.DTOMapper;
import com.jzajas.RatingSystem.Repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final DTOMapper mapper;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;


    public AdminService(PasswordEncoder encoder, DTOMapper mapper, UserRepository userRepository) {
        this.encoder = encoder;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    public void createAdmin(UserRegistrationDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) throw new EmailAlreadyInUseException(dto.getEmail());

        User user = mapper.convertFromUserRegistrationDTOtoAdmin(dto);
        user.setPassword(encoder.encode(user.getPassword()));

        userRepository.save(user);
    }
}
