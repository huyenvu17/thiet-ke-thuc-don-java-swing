package entity;

/**
 * Entity class:  NguyenLieuEntity
 */
public record NguyenLieuEntity(
        int id,
        String tenNguyenLieu,
        Double khoiLuong,
        Double donGia,
        int nhomThucPhamId
        ) {
}
