package com.hospital.service;
import java.util.List;
public interface CrudService<T> { T create(T entity); List<T> findAll(); T findById(Integer id); T update(Integer id,T entity); void delete(Integer id); }
