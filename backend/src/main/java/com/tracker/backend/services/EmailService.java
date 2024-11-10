package com.tracker.backend.services;

import org.springframework.stereotype.Service;

import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.tracker.backend.exceptions.ServiceException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EmailService {
    private JavaMailSender mailSender;
    private TemplateEngine templateEngine;

    public Mono<String> sendverificationEmail(String email, String username, String otp) throws ServiceException {
        // Prepare the HTML content with Thymeleaf
        Context context = new Context();
        context.setVariable("otp", otp);
        context.setVariable("username", username);
        String htmlContent = templateEngine.process("email-template.html", context);

        // Send email
        try {
            sendEmail(email, "Email verification: One Time Password!", htmlContent);
            return Mono.just("Email sent successfully");
        } catch (MessagingException e) {
            return Mono.error(new ServiceException("Error sending email", HttpStatus.INTERNAL_SERVER_ERROR));
        }

    }

    public void sendEmail(String to, String subject, String content) throws MessagingException {
        // Prepare message using a Spring helper
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("abhi@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        
        // Send email
        mailSender.send(message);
    }

}
