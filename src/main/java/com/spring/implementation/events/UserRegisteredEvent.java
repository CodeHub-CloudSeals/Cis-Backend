package com.spring.implementation.events;



public class UserRegisteredEvent implements NotificationEvent  {
    private final String email;
    private final String name;
    private final Integer id;

    public UserRegisteredEvent(String email, String name,Integer id) {
        this.email = email;
        this.name  = name;
        this.id=id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
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
