package com.jzajas.RatingSystem.Security;

import com.jzajas.RatingSystem.Entities.Status;
import com.jzajas.RatingSystem.Entities.User;
import com.jzajas.RatingSystem.Repositories.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final UserRepository userRepository;


    public CustomAuthenticationEntryPoint(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException
    {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String email = extractUsername(request);
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            response.getWriter().write("Missing or incorrect credentials. Please provide correct username and password");
        } else if (userRepository.isAdministrator(email)){
            response.getWriter().write("Administrator cannot perform this action");
        } else if (user.get().getStatus() == Status.PENDING_EMAIL) {
            response.getWriter().write("Account is not approved. Please check your email");
        } else if (user.get().getStatus() == Status.DECLINED) {
            response.getWriter().write("Account is declined.");
        }
    }

    private String extractUsername(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null) {
            String encodedCredentials = authHeader.substring("Basic ".length());
            byte[] bytes = Base64.getDecoder().decode(encodedCredentials);
            String credentials = new String(bytes, StandardCharsets.UTF_8);
            String[] parts = credentials.split(":", 2);
            return parts[0];
        }
        return null;
    }
}
