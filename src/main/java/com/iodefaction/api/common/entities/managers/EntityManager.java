package com.iodefaction.api.common.entities.managers;

import com.iodefaction.api.common.entities.Entity;

import java.util.Collection;

public interface EntityManager<E extends Entity> {
    E getByKey(String key);
    Collection<E> getAll();
    void send(E e);
    void delete(String key);
    E create(String key);
    boolean has(String key);
    E newInstance(String key);
}
