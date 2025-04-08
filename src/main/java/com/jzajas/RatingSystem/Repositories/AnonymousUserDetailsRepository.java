package com.jzajas.RatingSystem.Repositories;

import com.jzajas.RatingSystem.Entities.AnonymousUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AnonymousUserDetailsRepository extends JpaRepository<AnonymousUserDetails, Long> {
}
