package entity;

/**
 * Entity class: MonAnEntity
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