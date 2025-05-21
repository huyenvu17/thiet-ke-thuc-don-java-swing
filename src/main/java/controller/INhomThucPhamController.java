package controller;

import dto.NhomThucPhamDTO;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface INhomThucPhamController {

    List<NhomThucPhamDTO> getAllNhomThucPham();
    boolean addNhomThucPham(NhomThucPhamDTO nhomThucPhamDto);
    boolean updateNhomThucPham(NhomThucPhamDTO nhomThucPhamDto);
    boolean deleteNhomThucPham(int id);
    int getSoLuongNguyenLieu(int nhomThucPhamId);
} 