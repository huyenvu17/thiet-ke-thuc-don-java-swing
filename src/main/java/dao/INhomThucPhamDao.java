package dao;

import entity.NhomThucPhamEntity;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface INhomThucPhamDao {
    List<NhomThucPhamEntity> getAllNhomThucPham();
    int addNhomThucPham(NhomThucPhamEntity entity);
    NhomThucPhamEntity getById(int id);
    boolean updateNhomThucPham(NhomThucPhamEntity entity);
    boolean deleteNhomThucPham(int id);
    int getSoLuongNguyenLieu(int nhomThucPhamId);
} 