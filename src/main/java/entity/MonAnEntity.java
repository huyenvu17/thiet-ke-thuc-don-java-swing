package entity;

/**
 * Entity record representing a MonAn (Dish) in the system
 */
public record MonAnEntity(
    int id,
    String tenMon,
    String loaiMon, // 'sang', 'trua', 'xe'
    String cachCheBien
) {} 