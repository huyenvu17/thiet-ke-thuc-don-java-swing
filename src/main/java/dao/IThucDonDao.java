package dao;

import entity.ThucDonEntity;
import java.util.List;

/**
 * Interface for ThucDonDao operations
 */
public interface IThucDonDao {
    List<ThucDonEntity> getAllThucDon();
    int addThucDon(ThucDonEntity entity);
    boolean updateThucDon(ThucDonEntity entity);
    boolean deleteThucDon(int id);
    ThucDonEntity getById(int id);
} 