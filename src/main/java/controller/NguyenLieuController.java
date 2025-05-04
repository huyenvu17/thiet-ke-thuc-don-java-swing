/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import doanthietkethucdon.BHException;
import dto.NguyenLieuDTO;
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
    }

    @Override
    public List<NguyenLieuDTO> getAllKhachHang() {
        List<NguyenLieuDTO> nlDtoList = new ArrayList();
        for (NguyenLieu model : NguyenLieuService.getInstance().getDanhSachNguyenLieu()) {
            nlDtoList.add(toDto(model));
        }
        return nlDtoList;
    }

    @Override
    public boolean addNguyenLieu(NguyenLieuDTO nlDto) {
        try {
            return NguyenLieuService.getInstance().addNguyenLieu(toModel(nlDto));
        } catch (BHException ex) {
            Logger.getLogger(NguyenLieuService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    private NguyenLieu toModel(NguyenLieuDTO dto) throws BHException {
        return new NguyenLieu(
                dto.getId(),
                dto.getTenNguyenLieu(),
                dto.getDonViTinh(),
                dto.getDonGia());
    }

    private NguyenLieuDTO toDto(NguyenLieu model) {
        return new NguyenLieuDTO(
                model.getId(),
                model.getTenNguyenLieu(),
                model.getDonViTinh(),
                model.getDonGia());
    }
}
