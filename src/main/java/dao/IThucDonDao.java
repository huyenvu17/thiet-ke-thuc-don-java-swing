package dao;

import entity.ThucDonEntity;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface IThucDonDao {
    List<ThucDonEntity> getAllThucDon();
    int addThucDon(ThucDonEntity entity);
    ThucDonEntity getThucDonById(int id);
    boolean updateThucDon(ThucDonEntity entity);
    boolean deleteThucDon(int id);
} 