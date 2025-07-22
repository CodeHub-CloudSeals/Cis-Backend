package com.spring.implementation.component;
import com.spring.implementation.events.UserRegisteredEvent;
import com.spring.implementation.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RegistrationListener {

  private final EmailService emailService;

  public RegistrationListener(EmailService emailService) {
    this.emailService = emailService;
  }

  @Async("emailExecutor")
  @EventListener
  public void onUserRegistered(UserRegisteredEvent event) {
    String subject = "Welcome, " + event.getSubject()+ "!";

    String redirectUrl = "http://34.45.198.251:5173/activeUser?id="+ event.getId();

      String body = "Hi " + event.getName() + ",\n\n" +
              "Thank you for registering with Cloud seals.\n\n" +
              "Please activate your account by clicking the link below:\n" +
              redirectUrl + "\n\n" +
              "Best Regards,\nCloud seals admin Team";

      emailService.sendEmail(event.getEmail(), subject, body)
                .thenAccept(result -> {
                  // optionally log success or failure
                    log.info("email result  status: {}", result);
                });
  }
 }
