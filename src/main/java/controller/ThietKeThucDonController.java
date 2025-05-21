package controller;

import dao.MonAnDao;
import dao.NguyenLieuDao;
import dao.NhomThucPhamDao;
import dto.ThietKeThucDonDTO;
import dto.NguyenLieuDTO;
import dto.NhomThucPhamDTO;
import dto.MonAnDTO;
import entity.MonAnEntity;
import entity.NguyenLieuEntity;
import entity.NhomThucPhamEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import service.ThucDonService;

/**
 *
 * @author ADMIN
 */
public class ThietKeThucDonController implements IThietKeThucDonController {
    private static ThietKeThucDonController instance;
    private final ThucDonService thucDonService;
    private final MonAnDao monAnDao;
    private final NguyenLieuDao nguyenLieuDao;
    private final NhomThucPhamDao nhomThucPhamDao;
    
    public static ThietKeThucDonController getInstance() {
        if (ThietKeThucDonController.instance == null) {
            ThietKeThucDonController.instance = new ThietKeThucDonController();
        }
        return instance;
    }

    private ThietKeThucDonController() {
        thucDonService = ThucDonService.getInstance();
        monAnDao = MonAnDao.getInstance();
        nguyenLieuDao = NguyenLieuDao.getInstance();
        nhomThucPhamDao = NhomThucPhamDao.getInstance();
    }
    
    @Override
    public int generateThucDon(ThietKeThucDonDTO thietKeDto) {
        return thucDonService.generateThucDon(
                thietKeDto.getTenThucDon(),
                thietKeDto.getSoNgay(),
                thietKeDto.getBudgetMap(),
                thietKeDto.getSelectedNguyenLieuIds());
    }
    
    @Override
    public Map<String, Object> getThucDonWithDetails(int thucDonId) {
        return thucDonService.getThucDonWithDetails(thucDonId);
    }
    
    @Override
    public List<NhomThucPhamDTO> getAllNhomThucPham() {
        List<NhomThucPhamEntity> entityList = nhomThucPhamDao.getAllNhomThucPham();
        List<NhomThucPhamDTO> dtoList = new ArrayList<>();
        
        for (NhomThucPhamEntity entity : entityList) {
            dtoList.add(toDto(entity));
        }
        
        return dtoList;
    }
    
    @Override
    public List<NguyenLieuDTO> getAllNguyenLieu() {
        List<NguyenLieuEntity> entityList = nguyenLieuDao.getAllNguyenLieu();
        List<NguyenLieuDTO> dtoList = new ArrayList<>();
        
        for (NguyenLieuEntity entity : entityList) {
            dtoList.add(toDto(entity));
        }
        
        return dtoList;
    }
    
    @Override
    public List<NguyenLieuDTO> getNguyenLieuByNhomThucPhamId(int nhomThucPhamId) {
        List<NguyenLieuEntity> allNguyenLieu = nguyenLieuDao.getAllNguyenLieu();
        List<NguyenLieuEntity> filteredList = allNguyenLieu.stream()
                .filter(nl -> nl.nhomThucPhamId() == nhomThucPhamId)
                .collect(Collectors.toList());

        List<NguyenLieuDTO> dtoList = new ArrayList<>();
        for (NguyenLieuEntity entity : filteredList) {
            dtoList.add(toDto(entity));
        }
        
        return dtoList;
    }
    
    @Override
    public Map<String, Boolean> checkMonAnAvailability() {
        List<MonAnEntity> monAnList = monAnDao.getAllMonAn();
        
        Map<String, Boolean> availability = new HashMap<>();
        availability.put("sang", monAnList.stream().anyMatch(m -> "sang".equals(m.loaiMon())));
        availability.put("trua", monAnList.stream().anyMatch(m -> "trua".equals(m.loaiMon())));
        availability.put("xe", monAnList.stream().anyMatch(m -> "xe".equals(m.loaiMon())));
        
        return availability;
    }
    
    private NhomThucPhamDTO toDto(NhomThucPhamEntity entity) {
        return new NhomThucPhamDTO(
                entity.id(),
                entity.tenNhom(),
                entity.moTa());
    }
    
    private NguyenLieuDTO toDto(NguyenLieuEntity entity) {
        String tenNhomThucPham = "";
        NhomThucPhamEntity nhomThucPham = nhomThucPhamDao.getById(entity.nhomThucPhamId());
        if (nhomThucPham != null) {
            tenNhomThucPham = nhomThucPham.tenNhom();
        }
        
        return new NguyenLieuDTO(
                entity.id(),
                entity.tenNguyenLieu(),
                entity.khoiLuong(),
                entity.donGia(),
                entity.nhomThucPhamId(),
                tenNhomThucPham);
    }
} 