package com.hospital.controller;

import com.hospital.service.CrudService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/** CRUD controller that accepts and returns DTOs rather than exposing persistence entities. */
public abstract class AbstractCrudController<T, D> {
    protected final CrudService<T> service;
    private final Class<T> entityType;
    private final Class<D> dtoType;

    protected AbstractCrudController(CrudService<T> service, Class<T> entityType, Class<D> dtoType) {
        this.service = service;
        this.entityType = entityType;
        this.dtoType = dtoType;
    }

    protected T toEntity(D dto) {
        T entity = BeanUtils.instantiateClass(entityType);
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    protected D toDto(T entity) {
        D dto = BeanUtils.instantiateClass(dtoType);
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public D create(@Valid @RequestBody D dto) { return toDto(service.create(toEntity(dto))); }


    @GetMapping public List<D> all() { return service.findAll().stream().map(this::toDto).toList(); }

    @GetMapping("/{id}") public D one(@PathVariable Integer id) { return toDto(service.findById(id)); }


    @PutMapping("/{id}") public D update(@PathVariable Integer id, @Valid @RequestBody D dto) { return toDto(service.update(id, toEntity(dto))); }


    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Integer id) { service.delete(id); }
}
