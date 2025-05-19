package entity;

/**
 * Entity class: ChiTietThucDonEntity
 */
public record ChiTietThucDonEntity(
    int id,
    int thucDonId,
    int ngay,
    String buoi, // 'sang', 'trua', 'xe'
    int monAnId,
    String tenMon
) {
    /**
     * Constructor with only the database fields (no display fields)
     */
    public ChiTietThucDonEntity(int id, int thucDonId, int ngay, String buoi, int monAnId) {
        this(id, thucDonId, ngay, buoi, monAnId, null);
    }
} 