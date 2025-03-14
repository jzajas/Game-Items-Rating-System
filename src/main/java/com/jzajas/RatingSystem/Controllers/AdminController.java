package com.jzajas.RatingSystem.Controllers;

import com.jzajas.RatingSystem.DTO.UserRegistrationDTO;
import com.jzajas.RatingSystem.Services.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PreAuthorize("isAdmin")
    public ResponseEntity<Void> registerAdmin(@Valid @RequestBody UserRegistrationDTO dto) {
        adminService.createAdmin(dto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PreAuthorize("isAdmin")
    @GetMapping("/pending/users")
    public ResponseEntity<Void> getPendingUsers() {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAdmin")
    @GetMapping("/pending/users")
    public ResponseEntity<Void> getPendingComments() {

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
