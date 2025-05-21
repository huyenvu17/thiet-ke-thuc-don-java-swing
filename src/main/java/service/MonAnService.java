package service;

import dao.IMonAnDao;
import doanthietkethucdon.BHException;
import entity.MonAnEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.MonAn;

/**
 *
 * @author ADMIN
 */
public class MonAnService {
    private static MonAnService instance;

    public static MonAnService getInstance() {
        if (MonAnService.instance == null) {
            MonAnService.instance = new MonAnService();
        }
        return instance;
    }

    private final List<MonAn> danhSachMonAn;
    private IMonAnDao monAnDao;

    /**
     * Don't let anyone instantiate this class directly.
     */
    private MonAnService() {
        this.danhSachMonAn = new ArrayList<>();
    }

    public List<MonAn> getDanhSachMonAn() {
        return danhSachMonAn;
    }

    public void setMonAnDaoService(IMonAnDao monAnDao) {
        this.monAnDao = monAnDao;
    }

    public void loadAllMonAnFromDatabase() {
        this.danhSachMonAn.clear();
        List<MonAnEntity> entityList = this.monAnDao.getAllMonAn();
        for (MonAnEntity entity : entityList) {
            try {
                this.danhSachMonAn.add(toModel(entity));
            } catch (BHException ex) {
                Logger.getLogger(MonAnService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean addMonAn(MonAn model) {
        return this.monAnDao.addMonAn(toEntity(model)) > 0;
    }
    
    public MonAn getMonAnById(int id) {
        MonAnEntity entity = this.monAnDao.getMonAnById(id);
        if (entity != null) {
            try {
                return toModel(entity);
            } catch (BHException ex) {
                Logger.getLogger(MonAnService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public boolean updateMonAn(MonAn model) {
        return this.monAnDao.updateMonAn(toEntity(model));
    }

    public boolean deleteMonAn(int id) {
        return this.monAnDao.deleteMonAn(id);
    }

    public MonAn toModel(MonAnEntity entity) throws BHException {
        return new MonAn(
                entity.id(),
                entity.tenMon(),
                entity.loaiMon(),
                entity.cachCheBien());
    }

    public MonAnEntity toEntity(MonAn model) {
        return new MonAnEntity(
                model.getId(),
                model.getTenMon(),
                model.getLoaiMon(),
                model.getCachCheBien());
    }
} 