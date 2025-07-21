package com.spring.implementation.component;
import com.spring.implementation.events.NotificationEvent;
import com.spring.implementation.service.EmailService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class GenericNotificationListener {

    private final EmailService emailService;

    public GenericNotificationListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async("emailExecutor")
    @EventListener
    public void handleNotification(NotificationEvent event) {
        emailService
            .sendEmail(event.getEmail(), event.getSubject(), event.getBody())
            .thenAccept(result -> System.out.println("Result: " + result));
    }
}
