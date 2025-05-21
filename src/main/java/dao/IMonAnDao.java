package dao;

import entity.MonAnEntity;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface IMonAnDao {
    List<MonAnEntity> getAllMonAn();
    int addMonAn(MonAnEntity entity);
    MonAnEntity getMonAnById(int id);
    boolean updateMonAn(MonAnEntity entity);
    boolean deleteMonAn(int id);
} 