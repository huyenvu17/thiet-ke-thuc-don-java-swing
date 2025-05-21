package controller;

import dto.ThietKeThucDonDTO;
import dto.NguyenLieuDTO;
import dto.NhomThucPhamDTO;
import dto.MonAnDTO;
import entity.ChiTietThucDonEntity;
import entity.ThucDonEntity;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public interface IThietKeThucDonController {

    int generateThucDon(ThietKeThucDonDTO thietKeDto);
    Map<String, Object> getThucDonWithDetails(int thucDonId);
    List<NhomThucPhamDTO> getAllNhomThucPham();
    List<NguyenLieuDTO> getAllNguyenLieu();
    List<NguyenLieuDTO> getNguyenLieuByNhomThucPhamId(int nhomThucPhamId);
    Map<String, Boolean> checkMonAnAvailability();
} 