package dao;

import entity.MonAnEntity;
import java.util.List;

/**
 * Interface for MonAn Data Access Object
 */
public interface IMonAnDao {
    List<MonAnEntity> getAllMonAn();
    int addMonAn(MonAnEntity entity);
    MonAnEntity getMonAnById(int id);
    boolean updateMonAn(MonAnEntity entity);
    boolean deleteMonAn(int id);
} 