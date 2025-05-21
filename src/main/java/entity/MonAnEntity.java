package entity;

/**
 *
 * @author ADMIN
 */
public record MonAnEntity(
    int id,
    String tenMon,
    String loaiMon, // 'sang', 'trua', 'xe'
    String cachCheBien
) {
} 