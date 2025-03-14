package com.jzajas.RatingSystem.Controllers;

import com.jzajas.RatingSystem.DTO.UserRegistrationDTO;
import com.jzajas.RatingSystem.Entities.User;
import com.jzajas.RatingSystem.Services.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("isAdmin")
@RestController("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PutMapping("/register")
    public ResponseEntity<Void> registerAdmin(@Valid @RequestBody UserRegistrationDTO dto) {
        adminService.createAdmin(dto);

        return new ResponseEntity<>(HttpStatus.CREATED);
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


    @GetMapping("/pending/users")
    public ResponseEntity<List<User>> getPendingUsers() {
        List<User> allPendingUsers = adminService.getAllPendingUsers();
        return new ResponseEntity<>(allPendingUsers, HttpStatus.OK);
    }

    @GetMapping("/pending/comments")
    public ResponseEntity<Void> getPendingComments() {

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
