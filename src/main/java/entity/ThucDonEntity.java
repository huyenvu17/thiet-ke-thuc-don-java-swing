package entity;

/**
 * Entity class: ThucDonEntity
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