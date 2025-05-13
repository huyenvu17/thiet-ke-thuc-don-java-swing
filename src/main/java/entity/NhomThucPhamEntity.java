package entity;

/**
 * Entity đại diện cho nhóm thực phẩm trong cơ sở dữ liệu.
 * Sử dụng Java Record để đảm bảo tính bất biến và tự động cài đặt các phương thức thông dụng.
 */
public record NhomThucPhamEntity(
    int id,
    String tenNhom,
    String moTa
) {
    // Java Record tự động cung cấp:
    // - Constructor với tất cả các trường
    // - Các accessor method (trùng tên với các trường)
    // - Cài đặt equals và hashCode
    // - Cài đặt toString
    
    /**
     * Ghi đè toString để hiển thị tên nhóm trong JComboBox
     */
    @Override
    public String toString() {
        return tenNhom;
    }
} 