package com.spring.implementation.component;
import com.spring.implementation.events.UserRegisteredEvent;
import com.spring.implementation.service.EmailService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RegistrationListener {

  private final EmailService emailService;

  public RegistrationListener(EmailService emailService) {
    this.emailService = emailService;
  }

  @Async("emailExecutor")
  @EventListener
  public void onUserRegistered(UserRegisteredEvent event) {
    String subject = "Welcome, " + event.getSubject()+ "!";
    String body    = "Hi " + event.getEmail() + ",\n\nThank you for registering.";
    emailService.sendEmail(event.getEmail(), subject, body)
                .thenAccept(result -> {
                  // optionally log success or failure
                  System.out.println(result);
                });
  }
 }
