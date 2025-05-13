package entity;

/**
 * Entity representing a menu detail item (chi tiết thực đơn) in the database.
 * Implemented as a record for immutability and automatic implementation of common methods.
 */
public record ChiTietThucDonEntity(
    int id,
    int thucDonId,
    int ngay,
    String buoi, // 'sang', 'trua', 'xe'
    int monAnId,
    // Additional fields for display purposes
    String tenMon
) {
    /**
     * Constructor with only the database fields (no display fields)
     */
    public ChiTietThucDonEntity(int id, int thucDonId, int ngay, String buoi, int monAnId) {
        this(id, thucDonId, ngay, buoi, monAnId, null);
    }
} 