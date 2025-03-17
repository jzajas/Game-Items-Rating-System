package com.jzajas.RatingSystem.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final String CONFIRMATION_LINK = "http://localhost:8080/auth/confirm?token=";

    private static final String HTML_CONTENT = "<p>To confirm your email address, please click the link below:</p>" +
            "<a href='" + CONFIRMATION_LINK + "[[verification]]'>Confirm Email</a>" +
            "<p>This link will expire in 24 hours.</p>" +
            "<p>Note: After confirming your email, an administrator will review and approve your account and only after" +
            " that you will be able to use your account.</p>";

    private final JavaMailSender mailSender;

    @Value("${sending.email}")
    private String sendingEmail;


    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String receiverEmail, String verificationCode) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sendingEmail);
            helper.setTo(receiverEmail);
            helper.setSubject("Email Confirmation");
            helper.setText(HTML_CONTENT.replace("[[verification]]", verificationCode), true);
        } catch (MessagingException me) {
//            TODO messaging exception handling
            me.printStackTrace();
        }
        mailSender.send(message);
    }

    public void sendResetEmail(String receiverEmail, String body) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sendingEmail);
            helper.setTo(receiverEmail);
            helper.setSubject("Password Reset");
            helper.setText(body, true);
        } catch (MessagingException me) {
//            TODO messaging exception handling
            me.printStackTrace();
        }
        mailSender.send(message);
    }
}
