package dao;

import entity.NguyenLieuEntity;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class NguyenLieuDao implements INguyenLieuDao {
    
    private static NguyenLieuDao instance;
    
    public static NguyenLieuDao getInstance() {
        if (NguyenLieuDao.instance == null) {
            NguyenLieuDao.instance = new NguyenLieuDao();
        }
        return instance;
    }

    private NguyenLieuDao() {
    }

    private NguyenLieuEntity mapResultSetToNguyenLieu(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String tenNguyenLieu = rs.getString("ten_nguyen_lieu");
        Double khoiLuong = rs.getDouble("khoi_luong");
        Double donGia = rs.getDouble("don_gia");
        int nhomThucPhamId = 0;
        
        try {
            nhomThucPhamId = rs.getInt("nhom_thuc_pham_id");
        } catch (SQLException e) {
        }
        
        return new NguyenLieuEntity(id, tenNguyenLieu, khoiLuong, donGia, nhomThucPhamId);
    }

    @Override
    public List<NguyenLieuEntity> getAllNguyenLieu() {
        List<NguyenLieuEntity> dsNguyenLieu = new ArrayList<>();
        String sql = "SELECT * FROM vw_nguyenlieu_theotennhom";
        
        try (DatabaseConnection provider = new DatabaseConnection()) {
            ResultSet rs = provider.executeQuery(sql);
            while (rs.next()) {
                dsNguyenLieu.add(mapResultSetToNguyenLieu(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all nguyen lieu: " + e.getMessage());
            e.printStackTrace();
        }
        
        return dsNguyenLieu;
    }

    @Override
    public int addNguyenLieu(NguyenLieuEntity entity) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("INSERT INTO nguyenlieu");
        sqlBuilder.append(" (ten_nguyen_lieu, khoi_luong, don_gia, nhom_thuc_pham_id)");
        sqlBuilder.append(" VALUES (");
        sqlBuilder.append("'").append(entity.tenNguyenLieu().replace("'", "''")).append("'");
        sqlBuilder.append(", ").append(entity.khoiLuong());
        sqlBuilder.append(", ").append(entity.donGia());
        sqlBuilder.append(", ").append(entity.nhomThucPhamId());
        sqlBuilder.append(")");
        
        int newId = -1;
        
        try (DatabaseConnection provider = new DatabaseConnection()) {
            int affectedRows = provider.executeUpdate(sqlBuilder.toString());
            
            if (affectedRows > 0) {
                ResultSet rs = provider.executeQuery("SELECT LAST_INSERT_ID()");
                if (rs.next()) {
                    newId = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding nguyen lieu: " + e.getMessage());
            e.printStackTrace();
        }
        
        return newId;
    }

    public NguyenLieuEntity getById(int id) {
        NguyenLieuEntity entity = null;
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * FROM nguyenlieu WHERE id = ");
        sqlBuilder.append(id);
        
        try (DatabaseConnection provider = new DatabaseConnection()) {
            ResultSet rs = provider.executeQuery(sqlBuilder.toString());
            if (rs.next()) {
                entity = mapResultSetToNguyenLieu(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving nguyen lieu by id: " + e.getMessage());
            e.printStackTrace();
        }
        
        return entity;
    }

    public boolean updateNguyenLieu(NguyenLieuEntity entity) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("UPDATE nguyenlieu SET ");
        sqlBuilder.append("ten_nguyen_lieu = '").append(entity.tenNguyenLieu()).append("'");
        sqlBuilder.append(", khoi_luong = ").append(entity.khoiLuong());
        sqlBuilder.append(", don_gia = ").append(entity.donGia());
        sqlBuilder.append(" WHERE id = ").append(entity.id());
        
        boolean success = false;
        
        try (DatabaseConnection provider = new DatabaseConnection()) {
            int affectedRows = provider.executeUpdate(sqlBuilder.toString());
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Error updating nguyen lieu: " + e.getMessage());
            e.printStackTrace();
        }
        
        return success;
    }

    public boolean deleteNguyenLieu(int id) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("DELETE FROM nguyenlieu WHERE id = ");
        sqlBuilder.append(id);
        
        boolean success = false;
        
        try (DatabaseConnection provider = new DatabaseConnection()) {
            int affectedRows = provider.executeUpdate(sqlBuilder.toString());
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Error deleting nguyen lieu: " + e.getMessage());
            e.printStackTrace();
        }
        
        return success;
    }
} 