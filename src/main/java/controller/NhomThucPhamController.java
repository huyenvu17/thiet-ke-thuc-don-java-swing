package controller;

import dao.INhomThucPhamDao;
import dao.NhomThucPhamDao;
import doanthietkethucdon.BHException;
import dto.NhomThucPhamDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.NhomThucPham;
import service.NhomThucPhamService;

/**
 *
 * @author ADMIN
 */
public class NhomThucPhamController implements INhomThucPhamController {
    private static NhomThucPhamController instance;
    private NhomThucPhamService nhomThucPhamService;
    
    public static NhomThucPhamController getInstance() {
        if (NhomThucPhamController.instance == null) {
            NhomThucPhamController.instance = new NhomThucPhamController();
        }
        return instance;
    }

    private NhomThucPhamController() {
        nhomThucPhamService = NhomThucPhamService.getInstance();
        INhomThucPhamDao nhomThucPhamDao = NhomThucPhamDao.getInstance();
        nhomThucPhamService.setNhomThucPhamDaoService(nhomThucPhamDao);
        nhomThucPhamService.loadAllNhomThucPhamFromDatabase();
    }

    @Override
    public List<NhomThucPhamDTO> getAllNhomThucPham() {
        nhomThucPhamService.loadAllNhomThucPhamFromDatabase();
        
        List<NhomThucPhamDTO> nhomDtoList = new ArrayList<>();
        for (NhomThucPham model : nhomThucPhamService.getDanhSachNhomThucPham()) {
            NhomThucPhamDTO dto = toDto(model);
            dto.setSoLuongNguyenLieu(getSoLuongNguyenLieu(model.getId()));
            nhomDtoList.add(dto);
        }
        return nhomDtoList;
    }

    @Override
    public boolean addNhomThucPham(NhomThucPhamDTO nhomThucPhamDto) {
        try {
            boolean result = nhomThucPhamService.addNhomThucPham(toModel(nhomThucPhamDto));
            if (result) {
                nhomThucPhamService.loadAllNhomThucPhamFromDatabase();
            }
            return result;
        } catch (BHException ex) {
            Logger.getLogger(NhomThucPhamService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    @Override
    public boolean updateNhomThucPham(NhomThucPhamDTO nhomThucPhamDto) {
        try {
            boolean result = nhomThucPhamService.updateNhomThucPham(toModel(nhomThucPhamDto));
            if (result) {
                nhomThucPhamService.loadAllNhomThucPhamFromDatabase();
            }
            return result;
        } catch (BHException ex) {
            Logger.getLogger(NhomThucPhamService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    @Override
    public boolean deleteNhomThucPham(int id) {
        boolean result = nhomThucPhamService.deleteNhomThucPham(id);
        if (result) {
            nhomThucPhamService.loadAllNhomThucPhamFromDatabase();
        }
        return result;
    }
    
    @Override
    public int getSoLuongNguyenLieu(int nhomThucPhamId) {
        return nhomThucPhamService.getSoLuongNguyenLieu(nhomThucPhamId);
    }
    
    private NhomThucPham toModel(NhomThucPhamDTO dto) throws BHException {
        return new NhomThucPham(
                dto.getId(),
                dto.getTenNhom(),
                dto.getMoTa());
    }

    private NhomThucPhamDTO toDto(NhomThucPham model) {
        return new NhomThucPhamDTO(
                model.getId(),
                model.getTenNhom(),
                model.getMoTa());
    }
} 