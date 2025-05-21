package entity;

/**
 *
 * @author ADMIN
 */
public record NhomThucPhamEntity(
    int id,
    String tenNhom,
    String moTa
) {
    /**
     * Ghi đè toString để hiển thị tên nhóm trong JComboBox
     */
    @Override
    public String toString() {
        return tenNhom;
    }
} 