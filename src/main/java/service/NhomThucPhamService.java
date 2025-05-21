package service;

import dao.INhomThucPhamDao;
import doanthietkethucdon.BHException;
import entity.NhomThucPhamEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.NhomThucPham;

/**
 *
 * @author ADMIN
 */
public class NhomThucPhamService {
    private static NhomThucPhamService instance;

    public static NhomThucPhamService getInstance() {
        if (NhomThucPhamService.instance == null) {
            NhomThucPhamService.instance = new NhomThucPhamService();
        }
        return instance;
    }

    private final List<NhomThucPham> dsNhomThucPham;
    private INhomThucPhamDao nhomThucPhamDao;

    /**
     * Don't let anyone instantiate this class directly.
     */
    private NhomThucPhamService() {
        this.dsNhomThucPham = new ArrayList<>();
    }

    public List<NhomThucPham> getDanhSachNhomThucPham() {
        return dsNhomThucPham;
    }

    public void setNhomThucPhamDaoService(INhomThucPhamDao nhomThucPhamDao) {
        this.nhomThucPhamDao = nhomThucPhamDao;
    }

    public void loadAllNhomThucPhamFromDatabase() {
        this.dsNhomThucPham.clear();
        List<NhomThucPhamEntity> entityList = this.nhomThucPhamDao.getAllNhomThucPham();
        for (NhomThucPhamEntity entity : entityList) {
            try {
                this.dsNhomThucPham.add(toModel(entity));
            } catch (BHException ex) {
                Logger.getLogger(NhomThucPhamService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean addNhomThucPham(NhomThucPham model) {
        return this.nhomThucPhamDao.addNhomThucPham(toEntity(model)) > 0;
    }

    public boolean updateNhomThucPham(NhomThucPham model) {
        return this.nhomThucPhamDao.updateNhomThucPham(toEntity(model));
    }

    public boolean deleteNhomThucPham(int id) {
        return this.nhomThucPhamDao.deleteNhomThucPham(id);
    }
    
    public int getSoLuongNguyenLieu(int nhomThucPhamId) {
        return this.nhomThucPhamDao.getSoLuongNguyenLieu(nhomThucPhamId);
    }

    public NhomThucPham toModel(NhomThucPhamEntity entity) throws BHException {
        return new NhomThucPham(
                entity.id(),
                entity.tenNhom(),
                entity.moTa());
    }

    public NhomThucPhamEntity toEntity(NhomThucPham model) {
        return new NhomThucPhamEntity(
                model.getId(),
                model.getTenNhom(),
                model.getMoTa());
    }
} 