package dao;

import entity.MonAnEntity;
import java.util.List;

/**
 * Interface for MonAnDao operations
 */
public interface IMonAnDao {
    List<MonAnEntity> getAllMonAn();
    int addMonAn(MonAnEntity entity);
    boolean updateMonAn(MonAnEntity entity);
    boolean deleteMonAn(int id);
    MonAnEntity getById(int id);
} 