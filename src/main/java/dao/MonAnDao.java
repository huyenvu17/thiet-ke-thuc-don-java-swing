package dao;

import entity.MonAnEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for MonAn entity
 */
public class MonAnDao implements IMonAnDao {
    
    private static MonAnDao instance;
    
    public static synchronized MonAnDao getInstance() {
        if (MonAnDao.instance == null) {
            MonAnDao.instance = new MonAnDao();
        }
        return instance;
    }
    
    /**
     * Private constructor for Singleton pattern
     */
    private MonAnDao() {
    }
    
    /**
     * Get all dishes from the database
     * @return List of MonAnEntity objects
     */
    @Override
    public List<MonAnEntity> getAllMonAn() {
        List<MonAnEntity> monAnList = new ArrayList<>();
        String query = "SELECT id, ten_mon, loai_mon, cach_che_bien FROM monan";
        
        try (Connection conn = new DatabaseConnection().connection;
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                MonAnEntity monAn = new MonAnEntity(
                        rs.getInt("id"),
                        rs.getString("ten_mon"),
                        rs.getString("loai_mon"),
                        rs.getString("cach_che_bien")
                );
                monAnList.add(monAn);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving MonAn list: " + e.getMessage());
            e.printStackTrace();
        }
        
        return monAnList;
    }
    
    /**
     * Add a new dish to the database
     * @param monAn MonAnEntity to add
     * @return ID of the newly inserted dish, or -1 if failed
     */
    @Override
    public int addMonAn(MonAnEntity monAn) {
        String query = "INSERT INTO monan (ten_mon, loai_mon, cach_che_bien) VALUES (?, ?, ?)";
        int newId = -1;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, monAn.tenMon());
            pstmt.setString(2, monAn.loaiMon());
            pstmt.setString(3, monAn.cachCheBien());
            
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
            System.err.println("Error adding MonAn: " + e.getMessage());
            e.printStackTrace();
        }
        
        return newId;
    }
    
    /**
     * Update an existing dish in the database
     * @param monAn MonAnEntity to update
     * @return true if successful, false otherwise
     */
    @Override
    public boolean updateMonAn(MonAnEntity monAn) {
        String query = "UPDATE monan SET ten_mon = ?, loai_mon = ?, cach_che_bien = ? WHERE id = ?";
        boolean success = false;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, monAn.tenMon());
            pstmt.setString(2, monAn.loaiMon());
            pstmt.setString(3, monAn.cachCheBien());
            pstmt.setInt(4, monAn.id());
            
            int affectedRows = pstmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Error updating MonAn: " + e.getMessage());
            e.printStackTrace();
        }
        
        return success;
    }
    
    /**
     * Delete a dish from the database by ID
     * @param id ID of the dish to delete
     * @return true if successful, false otherwise
     */
    @Override
    public boolean deleteMonAn(int id) {
        String query = "DELETE FROM monan WHERE id = ?";
        boolean success = false;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Error deleting MonAn: " + e.getMessage());
            e.printStackTrace();
        }
        
        return success;
    }
    
    /**
     * Get a dish by ID
     * @param id ID of the dish to retrieve
     * @return MonAnEntity if found, null otherwise
     */
    @Override
    public MonAnEntity getMonAnById(int id) {
        String query = "SELECT id, ten_mon, loai_mon, cach_che_bien FROM monan WHERE id = ?";
        MonAnEntity entity = null;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    entity = new MonAnEntity(
                        rs.getInt("id"),
                        rs.getString("ten_mon"),
                        rs.getString("loai_mon"),
                        rs.getString("cach_che_bien")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving MonAn by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return entity;
    }
} 