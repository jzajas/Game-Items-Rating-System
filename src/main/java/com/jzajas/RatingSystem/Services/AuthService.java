package com.jzajas.RatingSystem.Services;

import com.jzajas.RatingSystem.Entities.Status;
import com.jzajas.RatingSystem.Exceptions.AccountNotApprovedException;
import com.jzajas.RatingSystem.Repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;


    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void isAccountApproved(Status status) {

          if (status == Status.PENDING_EMAIL|| status == Status.DECLINED) throw new AccountNotApprovedException();
    }
}
