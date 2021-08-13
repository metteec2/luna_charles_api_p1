package com.revature.registration.repositories;

/**
 * This interface defines CRUD (create, read, update, delete) repositories. Creation is ensured by the save method,
 * reading by findById, updating by update, and deletion by deleteById
 * @param <T>
 */
public interface CrudRepository<T> {
    T findById(String id);
    T save(T newResource);
    boolean update(T updatedResource,String field,String newValue);
    boolean deleteById(String id);
}
