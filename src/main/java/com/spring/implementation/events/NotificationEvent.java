package com.spring.implementation.events;

public interface NotificationEvent {
    String getEmail();
    String getSubject();
    String getBody();
}
