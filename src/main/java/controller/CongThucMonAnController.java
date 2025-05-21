package controller;

import dao.ICongThucMonAnDao;
import dao.CongThucMonAnDao;
import dao.IMonAnDao;
import dao.MonAnDao;
import dao.INguyenLieuDao;
import dao.NguyenLieuDao;
import doanthietkethucdon.BHException;
import dto.CongThucMonAnDTO;
import dto.MonAnDTO;
import dto.NguyenLieuDTO;
import entity.MonAnEntity;
import entity.NguyenLieuEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.CongThucMonAn;
import model.MonAn;
import model.NguyenLieu;
import service.CongThucMonAnService;
import service.MonAnService;
import service.NguyenLieuService;

/**
 *
 * @author ADMIN
 */
public class CongThucMonAnController implements ICongThucMonAnController {
    private static CongThucMonAnController instance;
    private CongThucMonAnService congThucMonAnService;
    private MonAnService monAnService;
    private NguyenLieuService nguyenLieuService;
    
    public static CongThucMonAnController getInstance() {
        if (CongThucMonAnController.instance == null) {
            CongThucMonAnController.instance = new CongThucMonAnController();
        }
        return instance;
    }

    private CongThucMonAnController() {
        congThucMonAnService = CongThucMonAnService.getInstance();
        monAnService = MonAnService.getInstance();
        nguyenLieuService = NguyenLieuService.getInstance();
        
        ICongThucMonAnDao congThucMonAnDao = CongThucMonAnDao.getInstance();
        congThucMonAnService.setCongThucMonAnDaoService(congThucMonAnDao);
        
        IMonAnDao monAnDao = MonAnDao.getInstance();
        monAnService.setMonAnDaoService(monAnDao);
        
        INguyenLieuDao nguyenLieuDao = NguyenLieuDao.getInstance();
        nguyenLieuService.setNguyenLieuDaoService(nguyenLieuDao);
        
        congThucMonAnService.loadAllCongThucMonAnFromDatabase();
        monAnService.loadAllMonAnFromDatabase();
        nguyenLieuService.loadAllNguyenLieuFromDatabase();
    }

    @Override
    public List<CongThucMonAnDTO> getAllCongThucMonAn() {
        congThucMonAnService.loadAllCongThucMonAnFromDatabase();
        
        List<CongThucMonAnDTO> dtoList = new ArrayList<>();
        for (CongThucMonAn model : congThucMonAnService.getDanhSachCongThuc()) {
            dtoList.add(toDtoWithNames(model));
        }
        return dtoList;
    }
    
    @Override
    public List<CongThucMonAnDTO> getCongThucByMonAnId(int monAnId) {
        List<CongThucMonAn> modelList = congThucMonAnService.getCongThucByMonAnId(monAnId);
        List<CongThucMonAnDTO> dtoList = new ArrayList<>();
        
        for (CongThucMonAn model : modelList) {
            dtoList.add(toDtoWithNames(model));
        }
        
        return dtoList;
    }
    
    @Override
    public CongThucMonAnDTO getCongThucMonAnById(int id) {
        CongThucMonAn model = congThucMonAnService.getCongThucMonAnById(id);
        if (model != null) {
            return toDtoWithNames(model);
        }
        return null;
    }

    @Override
    public boolean addCongThucMonAn(CongThucMonAnDTO congThucDto) {
        try {
            boolean result = congThucMonAnService.addCongThucMonAn(toModel(congThucDto));
            if (result) {
                congThucMonAnService.loadAllCongThucMonAnFromDatabase();
            }
            return result;
        } catch (BHException ex) {
            Logger.getLogger(CongThucMonAnService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    @Override
    public boolean updateCongThucMonAn(CongThucMonAnDTO congThucDto) {
        try {
            boolean result = congThucMonAnService.updateCongThucMonAn(toModel(congThucDto));
            if (result) {
                congThucMonAnService.loadAllCongThucMonAnFromDatabase();
            }
            return result;
        } catch (BHException ex) {
            Logger.getLogger(CongThucMonAnService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    @Override
    public boolean deleteCongThucMonAn(int id) {
        boolean result = congThucMonAnService.deleteCongThucMonAn(id);
        if (result) {
            congThucMonAnService.loadAllCongThucMonAnFromDatabase();
        }
        return result;
    }
    
    @Override
    public boolean deleteCongThucByMonAnId(int monAnId) {
        boolean result = congThucMonAnService.deleteCongThucByMonAnId(monAnId);
        if (result) {
            congThucMonAnService.loadAllCongThucMonAnFromDatabase();
        }
        return result;
    }
    
    @Override
    public List<MonAnDTO> getAllMonAn() {
        monAnService.loadAllMonAnFromDatabase();
        
        List<MonAnDTO> dtoList = new ArrayList<>();
        for (MonAn model : monAnService.getDanhSachMonAn()) {
            dtoList.add(monAnToDto(model));
        }
        return dtoList;
    }
    
    @Override
    public List<NguyenLieuDTO> getAllNguyenLieu() {
        nguyenLieuService.loadAllNguyenLieuFromDatabase();
        
        List<NguyenLieuDTO> dtoList = new ArrayList<>();
        for (NguyenLieu model : nguyenLieuService.getDanhSachNguyenLieu()) {
            dtoList.add(nguyenLieuToDto(model));
        }
        return dtoList;
    }
    
    private CongThucMonAn toModel(CongThucMonAnDTO dto) throws BHException {
        return new CongThucMonAn(
                dto.getId(),
                dto.getMonAnId(),
                dto.getNguyenLieuId(),
                dto.getKhoiLuong());
    }

    private CongThucMonAnDTO toDto(CongThucMonAn model) {
        return new CongThucMonAnDTO(
                model.getId(),
                model.getMonAnId(),
                model.getNguyenLieuId(),
                model.getKhoiLuong());
    }
    
    private CongThucMonAnDTO toDtoWithNames(CongThucMonAn model) {
        String tenMonAn = "";
        String tenNguyenLieu = "";
        
        MonAnEntity monAnEntity = MonAnDao.getInstance().getMonAnById(model.getMonAnId());
        if (monAnEntity != null) {
            tenMonAn = monAnEntity.tenMon();
        }
        
        NguyenLieuEntity nguyenLieuEntity = NguyenLieuDao.getInstance().getById(model.getNguyenLieuId());
        if (nguyenLieuEntity != null) {
            tenNguyenLieu = nguyenLieuEntity.tenNguyenLieu();
        }
        
        return new CongThucMonAnDTO(
                model.getId(),
                model.getMonAnId(),
                tenMonAn,
                model.getNguyenLieuId(),
                tenNguyenLieu,
                model.getKhoiLuong());
    }
    
    private MonAnDTO monAnToDto(MonAn model) {
        return new MonAnDTO(
                model.getId(),
                model.getTenMon(),
                model.getLoaiMon(),
                model.getCachCheBien());
    }
    
    private NguyenLieuDTO nguyenLieuToDto(NguyenLieu model) {
        return new NguyenLieuDTO(
                model.getId(),
                model.getTenNguyenLieu(),
                model.getKhoiLuong(),
                model.getDonGia());
    }
} 