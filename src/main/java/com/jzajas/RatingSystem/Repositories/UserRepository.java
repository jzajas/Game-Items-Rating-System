package com.jzajas.RatingSystem.Repositories;

import com.jzajas.RatingSystem.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
