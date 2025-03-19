package com.jzajas.RatingSystem.Services.Interfaces;

import com.jzajas.RatingSystem.DTO.Input.UserRegistrationDTO;
import com.jzajas.RatingSystem.DTO.Output.UserDTO;
import com.jzajas.RatingSystem.DTO.Output.UserScoreDTO;
import com.jzajas.RatingSystem.Entities.Comment;
import com.jzajas.RatingSystem.Entities.GameCategory;
import com.jzajas.RatingSystem.Entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {

    @Transactional
    void createNewUser(UserRegistrationDTO dto);

    @Transactional(readOnly = true)
    UserDTO findUserDTOById(Long id);

    @Transactional(readOnly = true)
    User findUserByEmail(String email);

    @Transactional(readOnly = true)
    List<Comment> getAllCommentsForUserById(Long id);

    @Transactional(readOnly = true)
    List<UserScoreDTO> getTopSellers(int display, GameCategory category, double from, double to);

    @Transactional
    void deleteUser(Authentication authentication);

    @Transactional(readOnly = true)
    double calculateUserScore(Long id);
}
