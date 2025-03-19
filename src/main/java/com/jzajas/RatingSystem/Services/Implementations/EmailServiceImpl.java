package com.jzajas.RatingSystem.Services.Implementations;

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

    private static final String CONFIRMATION_LINK = "http://localhost:8080/auth/confirm?token=";

    private static final String HTML_CONTENT = "<p>To confirm your email address, please click the link below:</p>" +
            "<a href='" + CONFIRMATION_LINK + "[[verification]]'>Confirm Email</a>" +
            "<p>This link will expire in 24 hours.</p>" +
            "<p>Note: After confirming your email, an administrator will review and approve your account and only after" +
            " that you will be able to use your account.</p>";

    private static final String RESET_EMAIL_BODY = "Your password reset code is: [[code]]" +
            "\n\nThis code will expire in [[expirationTime]] minutes." +
            "\n If this action was not initiated by you";

    private final JavaMailSender mailSender;

    @Value("${sending.email}")
    private String sendingEmail;


    @Override
    public void sendVerificationEmail(String receiverEmail, String verificationCode) {
        try {
            MimeMessage message = createMimeMessage(
                    receiverEmail,
                    "Email Confirmation",
                    HTML_CONTENT.replace("[[verification]]", verificationCode)
            );
            mailSender.send(message);
        } catch (MessagingException me) {
            throw new MessageSendingException("Error occurred while sending verification email to: " + receiverEmail);
        }
    }

    @Override
    public void sendResetEmail(String receiverEmail, String code, Long expirationTime) {
        String body = "Your password reset code is: " + code +
                "\n\nThis code will expire in " + expirationTime + " minutes." +
                "\n If this action was not initiated by you";
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
}