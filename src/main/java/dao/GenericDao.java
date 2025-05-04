package dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Generic interface for data access operations
 * @param <T> the entity type
 */
public interface GenericDao<T> {
    
    /**
     * Get an entity by its ID
     * @param id the ID of the entity
     * @return the entity, or null if not found
     * @throws SQLException if a database error occurs
     */
    T getById(int id) throws SQLException;
    
    /**
     * Get all entities of this type
     * @return a list of all entities
     * @throws SQLException if a database error occurs
     */
    List<T> getAll() throws SQLException;
    
    /**
     * Insert a new entity
     * @param entity the entity to insert
     * @return the ID of the newly inserted entity
     * @throws SQLException if a database error occurs
     */
    int insert(T entity) throws SQLException;
    
    /**
     * Update an existing entity
     * @param entity the entity to update
     * @return true if the update was successful
     * @throws SQLException if a database error occurs
     */
    boolean update(T entity) throws SQLException;
    
    /**
     * Delete an entity by its ID
     * @param id the ID of the entity to delete
     * @return true if the deletion was successful
     * @throws SQLException if a database error occurs
     */
    boolean delete(int id) throws SQLException;
} 