package dao;

import entity.ThucDonEntity;
import java.util.List;

/**
 * Interface for ThucDon Data Access Object
 */
public interface IThucDonDao {
    List<ThucDonEntity> getAllThucDon();
    int addThucDon(ThucDonEntity entity);
    ThucDonEntity getThucDonById(int id);
    boolean updateThucDon(ThucDonEntity entity);
    boolean deleteThucDon(int id);
} 