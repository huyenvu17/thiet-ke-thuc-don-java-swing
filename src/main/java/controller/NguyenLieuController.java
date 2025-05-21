/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.INguyenLieuDao;
import dao.NguyenLieuDao;
import dao.NhomThucPhamDao;
import doanthietkethucdon.BHException;
import dto.NguyenLieuDTO;
import entity.NhomThucPhamEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.NguyenLieu;
import service.NguyenLieuService;

/**
 *
 * @author ADMIN
 */
public class NguyenLieuController implements INguyenLieuController{
    private static NguyenLieuController instance;
    private NguyenLieuService nguyenLieuService;
    
    public static NguyenLieuController getInstance()
    {
        if(NguyenLieuController.instance == null)
        {
            NguyenLieuController.instance = new NguyenLieuController();
        }
        return instance;
    }
    
    /**
     * Don't let anyone instantiate this class.
     */
    private NguyenLieuController() {
        nguyenLieuService = NguyenLieuService.getInstance();
        INguyenLieuDao nguyenLieuDao = NguyenLieuDao.getInstance();
        nguyenLieuService.setNguyenLieuDaoService(nguyenLieuDao);
        nguyenLieuService.loadAllNguyenLieuFromDatabase();
    }

    @Override
    public List<NguyenLieuDTO> getAllNguyenLieu() {
        nguyenLieuService.loadAllNguyenLieuFromDatabase();
        
        List<NguyenLieuDTO> nlDtoList = new ArrayList();
        for (NguyenLieu model : nguyenLieuService.getDanhSachNguyenLieu()) {
            nlDtoList.add(toDto(model));
        }
        return nlDtoList;
    }

    @Override
    public boolean addNguyenLieu(NguyenLieuDTO nlDto) {
        try {
            boolean result = nguyenLieuService.addNguyenLieu(toModel(nlDto));
            if (result) {
                nguyenLieuService.loadAllNguyenLieuFromDatabase();
            }
            return result;
        } catch (BHException ex) {
            Logger.getLogger(NguyenLieuService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    @Override
    public boolean updateNguyenLieu(NguyenLieuDTO nlDto) {
        try {
            boolean result = nguyenLieuService.updateNguyenLieu(toModel(nlDto));
            if (result) {
                nguyenLieuService.loadAllNguyenLieuFromDatabase();
            }
            return result;
        } catch (BHException ex) {
            Logger.getLogger(NguyenLieuService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    @Override
    public boolean deleteNguyenLieu(int id) {
        boolean result = nguyenLieuService.deleteNguyenLieu(id);
        if (result) {
            nguyenLieuService.loadAllNguyenLieuFromDatabase();
        }
        return result;
    }
    
    private NguyenLieu toModel(NguyenLieuDTO dto) throws BHException {
        return new NguyenLieu(
                dto.getId(),
                dto.getTenNguyenLieu(),
                dto.getKhoiLuong(),
                dto.getDonGia(),
                dto.getNhomThucPhamId());
    }

    private NguyenLieuDTO toDto(NguyenLieu model) {
        String tenNhom = "";
        if (model.getNhomThucPhamId() > 0) {
            try {
                NhomThucPhamEntity nhomEntity = 
                    NhomThucPhamDao.getInstance().getById(model.getNhomThucPhamId());
                if (nhomEntity != null) {
                    tenNhom = nhomEntity.tenNhom();
                }
            } catch (Exception ex) {
            }
        }
        
        return new NguyenLieuDTO(
                model.getId(),
                model.getTenNguyenLieu(),
                model.getKhoiLuong(),
                model.getDonGia(),
                model.getNhomThucPhamId(),
                tenNhom);
    }
}
