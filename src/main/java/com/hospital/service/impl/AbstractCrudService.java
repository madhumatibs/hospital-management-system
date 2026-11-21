package com.hospital.service.impl;

import com.hospital.exception.ResourceNotFoundException;
import com.hospital.service.CrudService;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import java.lang.reflect.Field;
import java.util.List;

public abstract class AbstractCrudService<T> implements CrudService<T> {
    protected final JpaRepository<T, Integer> repository;

    protected AbstractCrudService(JpaRepository<T, Integer> repository) { this.repository = repository; }

    @Override public T create(T entity) { return repository.save(entity); }
    @Override public List<T> findAll() { return repository.findAll(); }

    @Override public T findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found: " + id));
    }

    @Override public T update(Integer id, T entity) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Resource not found: " + id);
        setEntityId(entity, id);
        return repository.save(entity);
    }

    @Override public void delete(Integer id) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Resource not found: " + id);
        repository.deleteById(id);
    }

    private void setEntityId(T entity, Integer id) {
        Class<?> type = entity.getClass();
        while (type != null && type != Object.class) {
            for (Field field : type.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    try { field.setAccessible(true); field.set(entity, id); return; }
                    catch (ReflectiveOperationException ex) { throw new IllegalStateException("Unable to set entity ID", ex); }
                }
            }
            type = type.getSuperclass();
        }
        throw new IllegalStateException("No @Id field found for entity " + entity.getClass().getSimpleName());
    }
}
