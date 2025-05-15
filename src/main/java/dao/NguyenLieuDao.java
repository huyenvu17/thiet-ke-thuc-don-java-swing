package dao;

import entity.NguyenLieuEntity;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for NguyenLieuEntity
 * Handles database operations for ingredients
 */
public class NguyenLieuDao implements INguyenLieuDao {
    
    private static NguyenLieuDao instance;
    
    public static NguyenLieuDao getInstance() {
        if (NguyenLieuDao.instance == null) {
            NguyenLieuDao.instance = new NguyenLieuDao();
        }
        return instance;
    }
    
    /**
     * Private constructor for Singleton pattern
     */
    private NguyenLieuDao() {
    }
    
    /**
     * Map a database result set to a NguyenLieuEntity object
     */
    private NguyenLieuEntity mapResultSetToNguyenLieu(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String tenNguyenLieu = rs.getString("ten_nguyen_lieu");
        Double khoiLuong = rs.getDouble("khoi_luong");
        BigDecimal donGia = rs.getBigDecimal("don_gia");
        int nhomThucPhamId = 0;
        
        try {
            nhomThucPhamId = rs.getInt("nhom_thuc_pham_id");
        } catch (SQLException e) {
            // Cột không tồn tại trong kết quả - giữ giá trị mặc định là 0
        }
        
        return new NguyenLieuEntity(id, tenNguyenLieu, khoiLuong, donGia, nhomThucPhamId);
    }

    @Override
    public List<NguyenLieuEntity> getAllNguyenLieu() {
        List<NguyenLieuEntity> dsNguyenLieu = new ArrayList<>();
        
        try (Connection conn = new DatabaseConnection().connection;
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM vw_nguyenlieu_theotennhom")) {
            
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
        String sql = "INSERT INTO nguyenlieu (ten_nguyen_lieu, khoi_luong, don_gia) VALUES (?, ?, ?)";
        int newId = -1;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, entity.tenNguyenLieu());
            stmt.setDouble(2, entity.khoiLuong());
            stmt.setBigDecimal(3, entity.donGia());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating NguyenLieu failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newId = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding nguyen lieu: " + e.getMessage());
            e.printStackTrace();
        }
        
        return newId;
    }
    
    /**
     * Get a NguyenLieuEntity by ID
     */
    public NguyenLieuEntity getById(int id) {
        NguyenLieuEntity entity = null;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM nguyenlieu WHERE id = ?")) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    entity = mapResultSetToNguyenLieu(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving nguyen lieu by id: " + e.getMessage());
            e.printStackTrace();
        }
        
        return entity;
    }
    
    /**
     * Update an existing NguyenLieuEntity
     */
    public boolean updateNguyenLieu(NguyenLieuEntity entity) {
        String sql = "UPDATE nguyenlieu SET ten_nguyen_lieu = ?, khoi_luong = ?, don_gia = ? WHERE id = ?";
        boolean success = false;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, entity.tenNguyenLieu());
            stmt.setDouble(2, entity.khoiLuong());
            stmt.setBigDecimal(3, entity.donGia());
            stmt.setInt(4, entity.id());
            
            int affectedRows = stmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Error updating nguyen lieu: " + e.getMessage());
            e.printStackTrace();
        }
        
        return success;
    }
    
    /**
     * Delete a NguyenLieuEntity by ID
     */
    public boolean deleteNguyenLieu(int id) {
        String sql = "DELETE FROM nguyenlieu WHERE id = ?";
        boolean success = false;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Error deleting nguyen lieu: " + e.getMessage());
            e.printStackTrace();
        }
        
        return success;
    }
} 