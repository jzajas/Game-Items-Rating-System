package com.jzajas.RatingSystem.Controllers;

import com.jzajas.RatingSystem.DTO.Input.UserRegistrationDTO;
import com.jzajas.RatingSystem.DTO.Output.PendingCommentDTO;
import com.jzajas.RatingSystem.DTO.Output.PendingUserDTO;
import com.jzajas.RatingSystem.Entities.Comment;
import com.jzajas.RatingSystem.Services.AdminService;
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

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerAdmin(@Valid @RequestBody UserRegistrationDTO dto) {
        adminService.createAdmin(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/pending/users")
    public ResponseEntity<List<PendingUserDTO>> getPendingUsers() {
        List<PendingUserDTO> allPendingUsers = adminService.getAllPendingUsers();
        return new ResponseEntity<>(allPendingUsers, HttpStatus.OK);
    }

    @GetMapping("/pending/comments")
    public ResponseEntity<List<PendingCommentDTO>> getPendingComments() {
        List<PendingCommentDTO> allPendingComments = adminService.getAllPendingComments();
        return new ResponseEntity<>(allPendingComments, HttpStatus.OK);
    }

    @PutMapping("/approve/user/{id}")
    public ResponseEntity<Void> approveUser(@PathVariable Long id) {
        adminService.approveUser(id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/decline/user/{id}")
    public ResponseEntity<Void> declineUser(@PathVariable Long id) {
        adminService.declineUser(id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/approve/comment/{id}")
    public ResponseEntity<Void> approveComment(@PathVariable Long id) {
        adminService.approveComment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/decline/comment/{id}")
    public ResponseEntity<Void> declineComment(@PathVariable Long id) {
        adminService.declineComment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
