package entity;

/**
 * Entity representing a dish (món ăn) in the database.
 * Implemented as a record for immutability and automatic implementation of common methods.
 */
public record MonAnEntity(
    int id,
    String tenMon,
    String loaiMon, // 'sang', 'trua', 'xe'
    String cachCheBien
) {
    // Using record, we automatically get:
    // - Constructor with all fields
    // - Accessor methods (named the same as the fields)
    // - equals and hashCode implementations
    // - toString implementation
} 