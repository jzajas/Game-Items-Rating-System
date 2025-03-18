package com.jzajas.RatingSystem.Repositories;

import com.jzajas.RatingSystem.Entities.AnonymousUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AnonymousUserDetailsRepository extends JpaRepository<AnonymousUserDetails, Long> {
}
