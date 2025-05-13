package dao;

import entity.ChiTietThucDonEntity;
import java.util.List;

/**
 * Interface for ChiTietThucDon Data Access Object
 */
public interface IChiTietThucDonDao {
    List<ChiTietThucDonEntity> getAllChiTietThucDon();
    List<ChiTietThucDonEntity> getByThucDonId(int thucDonId);
    int addChiTietThucDon(ChiTietThucDonEntity entity);
    ChiTietThucDonEntity getChiTietThucDonById(int id);
    boolean updateChiTietThucDon(ChiTietThucDonEntity entity);
    boolean deleteChiTietThucDon(int id);
    boolean deleteByThucDonId(int thucDonId);
} 