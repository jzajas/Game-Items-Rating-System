package com.jzajas.RatingSystem.Services.Implementations;

import com.jzajas.RatingSystem.AOP.LogExecutionTime;
import com.jzajas.RatingSystem.Exceptions.MessageSendingException;
import com.jzajas.RatingSystem.Services.Interfaces.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final String RESET_EMAIL_BODY = "Your password reset code is: [[code]]" +
            "\n\nThis code will expire in [[expirationTime]] minutes." +
            "\n If this action was not initiated by you";
    private static final String VERIFICATION_EMAIL_BODY = "<p>To confirm your email address, please click the link below:</p>" +
            "<a href='[[link]][[verification]]'>Confirm Email</a>" +
            "<p>This link will expire in 24 hours.</p>" +
            "<p>Note: After confirming your email, an administrator will review and approve your account and only after" +
            " that you will be able to use your account.</p>";
    private static String CONFIRMATION_LINK;
    private final JavaMailSender mailSender;

    @Value("${sending.email}")
    private String sendingEmail;


    @Override
    @LogExecutionTime
    public void sendVerificationEmail(String receiverEmail, String verificationCode) {
        try {
            MimeMessage message = createMimeMessage(
                    receiverEmail,
                    "Email Confirmation",
                    VERIFICATION_EMAIL_BODY
                            .replace("[[verification]]", verificationCode)
                            .replace("[[link]]", CONFIRMATION_LINK)
            );
            mailSender.send(message);
        } catch (MessagingException me) {
            throw new MessageSendingException("Error occurred while sending verification email to: " + receiverEmail);
        }
    }

    @Override
    @LogExecutionTime
    public void sendResetEmail(String receiverEmail, String code, Long expirationTime) {
        try {
            MimeMessage message = createMimeMessage(
                    receiverEmail,
                    "Password Reset",
                    RESET_EMAIL_BODY
                            .replace("[[code]]", code)
                            .replace("[[expirationTime]]", expirationTime.toString())
            );
            mailSender.send(message);
        } catch (MessagingException me) {
            throw new MessageSendingException("Error occurred while sending reset email to: " + receiverEmail);
        }
    }

    private MimeMessage createMimeMessage(String receiverEmail, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(sendingEmail);
        helper.setTo(receiverEmail);
        helper.setSubject(subject);
        helper.setText(body, true);

        return message;
    }

    @Value("${app.base-url:http://localhost:8080}/auth/confirm?token=")
    public void setConfirmationLink(String url) {
        CONFIRMATION_LINK = url;
    }
}