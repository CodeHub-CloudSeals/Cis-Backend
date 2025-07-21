package com.spring.implementation.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

@Service
public class EmailService {

  private final JavaMailSender mailSender;
  public EmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public CompletableFuture<String> sendEmail(
      String to, String subject, String text) {

    return CompletableFuture.supplyAsync(() -> {
      try {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);
        mailSender.send(msg);
        return "Email sent to " + to;
      } catch (Exception ex) {
        return "Failed to send email to " + to + ": " + ex.getMessage();
      }
    });
  }
}
