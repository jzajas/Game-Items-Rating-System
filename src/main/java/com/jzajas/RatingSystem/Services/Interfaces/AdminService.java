package com.jzajas.RatingSystem.Services.Interfaces;

import com.jzajas.RatingSystem.DTO.Input.UserRegistrationDTO;
import com.jzajas.RatingSystem.DTO.Output.PendingCommentDTO;
import com.jzajas.RatingSystem.DTO.Output.PendingUserDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AdminService {

    @Transactional
    void createAdmin(UserRegistrationDTO dto);

    @Transactional(readOnly = true)
    List<PendingUserDTO> getAllPendingUsers();

    @Transactional(readOnly = true)
    List<PendingCommentDTO> getAllPendingComments();

    @Transactional
    void approveUser(Long id);

    @Transactional
    void declineUser(Long id);

    @Transactional
    void approveComment(Long id);

    @Transactional
    void declineComment(Long id);
}
