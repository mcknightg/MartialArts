package com.bluntsoftware.lib.jpa.util;

import org.springframework.hateoas.ResourceSupport;

/**
 * Created by Alexander Mcknight on 7/4/2015.
 *
 */
public class EntityResource<T> extends ResourceSupport {

    T entity;

    public EntityResource(T entity) {
        this.entity = entity;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }
}
