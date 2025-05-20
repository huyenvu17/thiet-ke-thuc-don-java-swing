package dao;

import entity.CongThucMonAnEntity;
import entity.NguyenLieuEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

/**
 * Data Access Object for CongThucMonAn entity
 */
public class CongThucMonAnDao implements ICongThucMonAnDao {
    
    private static CongThucMonAnDao instance;
    
    public static synchronized CongThucMonAnDao getInstance() {
        if (CongThucMonAnDao.instance == null) {
            CongThucMonAnDao.instance = new CongThucMonAnDao();
        }
        return instance;
    }
    
    /**
     * Private constructor for Singleton pattern
     */
    private CongThucMonAnDao() {
    }
    
    /**
     * Get all recipe ingredients from the database
     * @return List of CongThucMonAnEntity objects
     */
    @Override
    public List<CongThucMonAnEntity> getAllCongThucMonAn() {
        List<CongThucMonAnEntity> congThucList = new ArrayList<>();
        String query = "SELECT ct.id, ct.mon_an_id, ct.nguyen_lieu_id, ct.khoi_luong, " +
                       "m.ten_mon, nl.ten_nguyen_lieu " +
                       "FROM congthucmonan ct " +
                       "JOIN monan m ON ct.mon_an_id = m.id " +
                       "JOIN nguyenlieu nl ON ct.nguyen_lieu_id = nl.id";
        
        try {
            Connection conn = new DatabaseConnection().connection;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                CongThucMonAnEntity congThuc = new CongThucMonAnEntity(
                        rs.getInt("id"),
                        rs.getInt("mon_an_id"),
                        rs.getInt("nguyen_lieu_id"),
                        rs.getBigDecimal("khoi_luong"),
                        rs.getString("ten_mon"),
                        rs.getString("ten_nguyen_lieu"),
                        "kg"  // Đơn vị cố định là kg
                );
                congThucList.add(congThuc);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving CongThucMonAn list: " + e.getMessage());
            e.printStackTrace();
        }
        
        return congThucList;
    }
    
    /**
     * Get all recipe ingredients for a specific dish
     * @param monAnId The ID of the dish
     * @return List of CongThucMonAnEntity objects
     */
    @Override
    public List<CongThucMonAnEntity> getCongThucByMonAnId(int monAnId) {
        List<CongThucMonAnEntity> congThucList = new ArrayList<>();
        String query = "SELECT ct.id, ct.mon_an_id, ct.nguyen_lieu_id, ct.khoi_luong, " +
                       "m.ten_mon, nl.ten_nguyen_lieu " +
                       "FROM congthucmonan ct " +
                       "JOIN monan m ON ct.mon_an_id = m.id " +
                       "JOIN nguyenlieu nl ON ct.nguyen_lieu_id = nl.id " +
                       "WHERE ct.mon_an_id = ?";
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, monAnId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    CongThucMonAnEntity congThuc = new CongThucMonAnEntity(
                            rs.getInt("id"),
                            rs.getInt("mon_an_id"),
                            rs.getInt("nguyen_lieu_id"),
                            rs.getBigDecimal("khoi_luong"),
                            rs.getString("ten_mon"),
                            rs.getString("ten_nguyen_lieu"),
                            "kg"  // Đơn vị cố định là kg
                    );
                    congThucList.add(congThuc);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving CongThucMonAn by monAnId: " + e.getMessage());
            e.printStackTrace();
        }
        
        return congThucList;
    }
    
    /**
     * Add a new recipe ingredient to the database
     * @param congThuc CongThucMonAnEntity to add
     * @return ID of the newly inserted recipe ingredient, or -1 if failed
     */
    @Override
    public int addCongThucMonAn(CongThucMonAnEntity congThuc) {
        String query = "INSERT INTO congthucmonan (mon_an_id, nguyen_lieu_id, khoi_luong) VALUES (?, ?, ?)";
        int newId = -1;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, congThuc.monAnId());
            pstmt.setInt(2, congThuc.nguyenLieuId());
            pstmt.setBigDecimal(3, congThuc.khoiLuong());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                return -1;
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newId = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding CongThucMonAn: " + e.getMessage());
            e.printStackTrace();
        }
        
        return newId;
    }
    
    /**
     * Update an existing recipe ingredient in the database
     * @param congThuc CongThucMonAnEntity to update
     * @return true if successful, false otherwise
     */
    @Override
    public boolean updateCongThucMonAn(CongThucMonAnEntity congThuc) {
        String query = "UPDATE congthucmonan SET mon_an_id = ?, nguyen_lieu_id = ?, khoi_luong = ? WHERE id = ?";
        boolean success = false;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, congThuc.monAnId());
            pstmt.setInt(2, congThuc.nguyenLieuId());
            pstmt.setBigDecimal(3, congThuc.khoiLuong());
            pstmt.setInt(4, congThuc.id());
            
            int affectedRows = pstmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Error updating CongThucMonAn: " + e.getMessage());
            e.printStackTrace();
        }
        
        return success;
    }
    
    /**
     * Delete a recipe ingredient from the database by ID
     * @param id ID of the recipe ingredient to delete
     * @return true if successful, false otherwise
     */
    @Override
    public boolean deleteCongThucMonAn(int id) {
        String query = "DELETE FROM congthucmonan WHERE id = ?";
        boolean success = false;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Error deleting CongThucMonAn: " + e.getMessage());
            e.printStackTrace();
        }
        
        return success;
    }
    
    /**
     * Delete all recipe ingredients for a specific dish
     * @param monAnId The ID of the dish
     * @return true if successful, false otherwise
     */
    @Override
    public boolean deleteCongThucByMonAnId(int monAnId) {
        String query = "DELETE FROM congthucmonan WHERE mon_an_id = ?";
        boolean success = false;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, monAnId);
            
            int affectedRows = pstmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Error deleting CongThucMonAn by monAnId: " + e.getMessage());
            e.printStackTrace();
        }
        
        return success;
    }
    
    /**
     * Get a recipe ingredient by ID
     * @param id ID of the recipe ingredient to retrieve
     * @return CongThucMonAnEntity if found, null otherwise
     */
    @Override
    public CongThucMonAnEntity getCongThucMonAnById(int id) {
        String query = "SELECT ct.id, ct.mon_an_id, ct.nguyen_lieu_id, ct.khoi_luong, " +
                       "m.ten_mon, nl.ten_nguyen_lieu " +
                       "FROM congthucmonan ct " +
                       "JOIN monan m ON ct.mon_an_id = m.id " +
                       "JOIN nguyenlieu nl ON ct.nguyen_lieu_id = nl.id " +
                       "WHERE ct.id = ?";
        CongThucMonAnEntity entity = null;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    entity = new CongThucMonAnEntity(
                        rs.getInt("id"),
                        rs.getInt("mon_an_id"),
                        rs.getInt("nguyen_lieu_id"),
                        rs.getBigDecimal("khoi_luong"),
                        rs.getString("ten_mon"),
                        rs.getString("ten_nguyen_lieu"),
                        "kg"  // Đơn vị cố định là kg
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving CongThucMonAn by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return entity;
    }
} 