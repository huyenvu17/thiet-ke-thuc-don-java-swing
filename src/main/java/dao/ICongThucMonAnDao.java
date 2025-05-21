package dao;

import entity.CongThucMonAnEntity;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface ICongThucMonAnDao {
    List<CongThucMonAnEntity> getAllCongThucMonAn();
    List<CongThucMonAnEntity> getCongThucByMonAnId(int monAnId);
    int addCongThucMonAn(CongThucMonAnEntity entity);
    CongThucMonAnEntity getCongThucMonAnById(int id);
    boolean updateCongThucMonAn(CongThucMonAnEntity entity);
    boolean deleteCongThucMonAn(int id);
    boolean deleteCongThucByMonAnId(int monAnId);
} 