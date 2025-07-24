package com.spring.implementation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setSenderValue() throws Exception {
        // Remove MockitoAnnotations.openMocks(this); — not needed with @ExtendWith

        // Inject dummy sender value using reflection
        Field senderField = EmailService.class.getDeclaredField("sender");
        senderField.setAccessible(true);
        senderField.set(emailService, "noreply@cloudseals.com");
    }

    @Test
    public void shouldSendEmailSuccessfully() throws Exception {
        String to = "mahesh@cloudseals.com";
        String subject = "Welcome!";
        String body = "Your registration is successful.";

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        CompletableFuture<String> result = emailService.sendEmail(to, subject, body);

        assertEquals("Email sent to " + to, result.get());
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    public void shouldHandleEmailSendFailure() throws Exception {
        String to = "invalid@cloudseals.com";
        String subject = "Oops!";
        String body = "Something went wrong.";

        doThrow(new RuntimeException("SMTP server not available"))
            .when(mailSender).send(any(SimpleMailMessage.class));

        CompletableFuture<String> result = emailService.sendEmail(to, subject, body);

        assertTrue(result.get().contains("Failed to send email to " + to));
        assertTrue(result.get().contains("SMTP server not available"));
    }
}