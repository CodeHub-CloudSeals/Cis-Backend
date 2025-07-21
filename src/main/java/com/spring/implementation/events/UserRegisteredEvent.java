package com.spring.implementation.events;

import org.springframework.context.ApplicationEvent;

public class UserRegisteredEvent implements NotificationEvent  {
    private final String email;
    private final String name;

    public UserRegisteredEvent(String email, String name) {
        this.email = email;
        this.name  = name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getSubject() {
        return "Welcome, " + name + "!";
    }

    @Override
    public String getBody() {
        return "Hi " + name + ",\n\nThank you for registering with us.";
    }
}
