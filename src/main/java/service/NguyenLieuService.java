/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.INguyenLieuDao;
import doanthietkethucdon.BHException;
import entity.NguyenLieuEntity;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.NguyenLieu;

/**
 *
 * @author ADMIN
 */
public class NguyenLieuService {
    private static NguyenLieuService instance;

    public static NguyenLieuService getInstance() {
        if (NguyenLieuService.instance == null) {
            NguyenLieuService.instance = new NguyenLieuService();
        }
        return instance;
    }

    private final List<NguyenLieu> dsNguyenLieu;
    private INguyenLieuDao nguyenLieuDao;

    /**
     * Don't let anyone instantiate this class.
     */
    private NguyenLieuService() {
        this.dsNguyenLieu = new ArrayList();
    }

    public List<NguyenLieu> getDanhSachNguyenLieu() {
        return dsNguyenLieu;
    }

    public void setNguyenLieuDaoService(INguyenLieuDao dsNguyenLieu) {
        this.nguyenLieuDao = dsNguyenLieu;
    }

    public void loadAllNguyenLieuFromDatabase() {
        this.dsNguyenLieu.clear();
        List<NguyenLieuEntity> entityList = this.nguyenLieuDao.getAllNguyenLieu();
        for (NguyenLieuEntity entity : entityList) {
            try {
                this.dsNguyenLieu.add(toModel(entity));
            } catch (BHException ex) {
                Logger.getLogger(NguyenLieuService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean addNguyenLieu(NguyenLieu model) {
        return this.nguyenLieuDao.addNguyenLieu(toEntity(model)) > 0;
    }

    public boolean updateNguyenLieu(NguyenLieu model) {
        return this.nguyenLieuDao.updateNguyenLieu(toEntity(model));
    }

    public boolean deleteNguyenLieu(int id) {
        return this.nguyenLieuDao.deleteNguyenLieu(id);
    }

    public NguyenLieu toModel(NguyenLieuEntity entity) throws BHException {
        return new NguyenLieu(
                entity.id(),
                entity.tenNguyenLieu(),
                entity.donViTinh(),
                entity.donGia(),
                entity.nhomThucPhamId());
    }

    public NguyenLieuEntity toEntity(NguyenLieu model) {
        return new NguyenLieuEntity(
                model.getId(),
                model.getTenNguyenLieu(),
                model.getDonViTinh(),
                model.getDonGia(),
                model.getNhomThucPhamId());
    }
}
