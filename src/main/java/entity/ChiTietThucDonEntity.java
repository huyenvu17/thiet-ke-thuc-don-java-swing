package entity;

/**
 *
 * @author ADMIN
 */
public record ChiTietThucDonEntity(
    int id,
    int thucDonId,
    int ngay,
    String buoi, // 'sang', 'trua', 'xe'
    int monAnId,
    String tenMon
) {
    public ChiTietThucDonEntity(int id, int thucDonId, int ngay, String buoi, int monAnId) {
        this(id, thucDonId, ngay, buoi, monAnId, null);
    }
} 