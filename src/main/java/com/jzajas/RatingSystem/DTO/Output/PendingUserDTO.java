package com.jzajas.RatingSystem.DTO.Output;

import com.jzajas.RatingSystem.Entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PendingUserDTO {

    private String firstName;
    private String lastName;
    private String email;
    private Date createdAt;
    private Role role;
}
