package com.jzajas.RatingSystem.Controllers;

import com.jzajas.RatingSystem.DTO.Input.UserRegistrationDTO;
import com.jzajas.RatingSystem.DTO.Output.PendingCommentDTO;
import com.jzajas.RatingSystem.DTO.Output.PendingUserDTO;
import com.jzajas.RatingSystem.Services.Implementations.AdminServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('ADMINISTRATOR')")
@RestController()
@RequestMapping("/admin")
public class AdminController {

    private final AdminServiceImpl adminServiceImpl;

    public AdminController(AdminServiceImpl adminServiceImpl) {
        this.adminServiceImpl = adminServiceImpl;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerAdmin(@Valid @RequestBody UserRegistrationDTO dto) {
        adminServiceImpl.createAdmin(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/pending/users")
    public ResponseEntity<List<PendingUserDTO>> getPendingUsers() {
        List<PendingUserDTO> allPendingUsers = adminServiceImpl.getAllPendingUsers();
        return new ResponseEntity<>(allPendingUsers, HttpStatus.OK);
    }

    @GetMapping("/pending/comments")
    public ResponseEntity<List<PendingCommentDTO>> getPendingComments() {
        List<PendingCommentDTO> allPendingComments = adminServiceImpl.getAllPendingComments();
        return new ResponseEntity<>(allPendingComments, HttpStatus.OK);
    }

    @PutMapping("/approve/user/{id}")
    public ResponseEntity<Void> approveUser(@PathVariable Long id) {
        adminServiceImpl.approveUser(id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/decline/user/{id}")
    public ResponseEntity<Void> declineUser(@PathVariable Long id) {
        adminServiceImpl.declineUser(id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/approve/comment/{id}")
    public ResponseEntity<Void> approveComment(@PathVariable Long id) {
        adminServiceImpl.approveComment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/decline/comment/{id}")
    public ResponseEntity<Void> declineComment(@PathVariable Long id) {
        adminServiceImpl.declineComment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
