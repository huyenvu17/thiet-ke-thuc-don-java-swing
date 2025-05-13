package entity;

/**
 * Entity representing a menu (thực đơn) in the database.
 * Implemented as a record for immutability and automatic implementation of common methods.
 */
public record ThucDonEntity(
    int id,
    String tenThucDon,
    int soNgay
) {
    // Using record, we automatically get:
    // - Constructor with all fields
    // - Accessor methods (named the same as the fields)
    // - equals and hashCode implementations
    // - toString implementation
} 