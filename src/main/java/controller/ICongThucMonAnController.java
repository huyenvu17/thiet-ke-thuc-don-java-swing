package controller;

import dto.CongThucMonAnDTO;
import dto.MonAnDTO;
import dto.NguyenLieuDTO;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface ICongThucMonAnController {

    List<CongThucMonAnDTO> getAllCongThucMonAn();
    List<CongThucMonAnDTO> getCongThucByMonAnId(int monAnId);
    CongThucMonAnDTO getCongThucMonAnById(int id);
    boolean addCongThucMonAn(CongThucMonAnDTO congThucDto);
    boolean updateCongThucMonAn(CongThucMonAnDTO congThucDto);
    boolean deleteCongThucMonAn(int id);
    boolean deleteCongThucByMonAnId(int monAnId);
    List<MonAnDTO> getAllMonAn();
    List<NguyenLieuDTO> getAllNguyenLieu();
} 