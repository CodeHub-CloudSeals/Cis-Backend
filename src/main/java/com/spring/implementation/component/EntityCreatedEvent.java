package com.spring.implementation.component;

import org.springframework.context.ApplicationEvent;

public class EntityCreatedEvent<T> extends ApplicationEvent {
    private final T entity;

    public EntityCreatedEvent(T entity) {
        super(entity);
        this.entity = entity;
    }

    public T getEntity() {
        return entity;
    }
}