package service;

import dao.ICongThucMonAnDao;
import doanthietkethucdon.BHException;
import entity.CongThucMonAnEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.CongThucMonAn;

/**
 *
 * @author ADMIN
 */
public class CongThucMonAnService {
    private static CongThucMonAnService instance;

    public static CongThucMonAnService getInstance() {
        if (CongThucMonAnService.instance == null) {
            CongThucMonAnService.instance = new CongThucMonAnService();
        }
        return instance;
    }

    private final List<CongThucMonAn> danhSachCongThuc;
    private ICongThucMonAnDao congThucMonAnDao;

    /**
     * Don't let anyone instantiate this class directly.
     */
    private CongThucMonAnService() {
        this.danhSachCongThuc = new ArrayList<>();
    }

    public List<CongThucMonAn> getDanhSachCongThuc() {
        return danhSachCongThuc;
    }

    public void setCongThucMonAnDaoService(ICongThucMonAnDao congThucMonAnDao) {
        this.congThucMonAnDao = congThucMonAnDao;
    }

    public void loadAllCongThucMonAnFromDatabase() {
        this.danhSachCongThuc.clear();
        List<CongThucMonAnEntity> entityList = this.congThucMonAnDao.getAllCongThucMonAn();
        for (CongThucMonAnEntity entity : entityList) {
            try {
                this.danhSachCongThuc.add(toModel(entity));
            } catch (BHException ex) {
                Logger.getLogger(CongThucMonAnService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public List<CongThucMonAn> getCongThucByMonAnId(int monAnId) {
        List<CongThucMonAn> result = new ArrayList<>();
        List<CongThucMonAnEntity> entityList = this.congThucMonAnDao.getCongThucByMonAnId(monAnId);
        for (CongThucMonAnEntity entity : entityList) {
            try {
                result.add(toModel(entity));
            } catch (BHException ex) {
                Logger.getLogger(CongThucMonAnService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public boolean addCongThucMonAn(CongThucMonAn model) {
        return this.congThucMonAnDao.addCongThucMonAn(toEntity(model)) > 0;
    }
    
    public CongThucMonAn getCongThucMonAnById(int id) {
        CongThucMonAnEntity entity = this.congThucMonAnDao.getCongThucMonAnById(id);
        if (entity != null) {
            try {
                return toModel(entity);
            } catch (BHException ex) {
                Logger.getLogger(CongThucMonAnService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public boolean updateCongThucMonAn(CongThucMonAn model) {
        return this.congThucMonAnDao.updateCongThucMonAn(toEntity(model));
    }

    public boolean deleteCongThucMonAn(int id) {
        return this.congThucMonAnDao.deleteCongThucMonAn(id);
    }
    
    public boolean deleteCongThucByMonAnId(int monAnId) {
        return this.congThucMonAnDao.deleteCongThucByMonAnId(monAnId);
    }

    public CongThucMonAn toModel(CongThucMonAnEntity entity) throws BHException {
        return new CongThucMonAn(
                entity.id(),
                entity.monAnId(),
                entity.nguyenLieuId(),
                entity.khoiLuong());
    }

    public CongThucMonAnEntity toEntity(CongThucMonAn model) {
        return new CongThucMonAnEntity(
                model.getId(),
                model.getMonAnId(),
                model.getNguyenLieuId(),
                model.getKhoiLuong());
    }
} 