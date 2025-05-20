package dao;

import entity.ThucDonEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho bảng thực đơn
 */
public class ThucDonDao implements IThucDonDao {
    
    private static ThucDonDao instance;
    
    /**
     * Get singleton instance of ThucDonDao
     * @return ThucDonDao instance
     */
    public static synchronized ThucDonDao getInstance() {
        if (ThucDonDao.instance == null) {
            ThucDonDao.instance = new ThucDonDao();
        }
        return instance;
    }
    
    /**
     * Private constructor for Singleton pattern
     */
    private ThucDonDao() {
    }
    
    /**
     * Get all menus from the database
     * @return List of ThucDonEntity objects
     */
    @Override
    public List<ThucDonEntity> getAllThucDon() {
        List<ThucDonEntity> thucDonList = new ArrayList<>();
        String query = "SELECT id, ten_thuc_don, so_ngay FROM thucdon";
        
        try (Connection conn = new DatabaseConnection().connection;
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                ThucDonEntity thucDon = new ThucDonEntity(
                        rs.getInt("id"),
                        rs.getString("ten_thuc_don"),
                        rs.getInt("so_ngay")
                );
                thucDonList.add(thucDon);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving ThucDon list: " + e.getMessage());
            e.printStackTrace();
        }
        
        return thucDonList;
    }
    
    /**
     * Add a new menu to the database
     * @param thucDon ThucDonEntity to add
     * @return ID of the newly inserted menu, or -1 if failed
     */
    @Override
    public int addThucDon(ThucDonEntity thucDon) {
        String query = "INSERT INTO thucdon (ten_thuc_don, so_ngay) VALUES (?, ?)";
        int newId = -1;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, thucDon.tenThucDon());
            pstmt.setInt(2, thucDon.soNgay());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating ThucDon failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newId = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding ThucDon: " + e.getMessage());
            e.printStackTrace();
        }
        
        return newId;
    }
    
    /**
     * Get a menu by ID
     * @param id ID of the menu to retrieve
     * @return ThucDonEntity if found, null otherwise
     */
    @Override
    public ThucDonEntity getThucDonById(int id) {
        String query = "SELECT id, ten_thuc_don, so_ngay FROM thucdon WHERE id = ?";
        ThucDonEntity thucDon = null;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    thucDon = new ThucDonEntity(
                            rs.getInt("id"),
                            rs.getString("ten_thuc_don"),
                            rs.getInt("so_ngay")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving ThucDon by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return thucDon;
    }
    
    /**
     * Update an existing menu in the database
     * @param thucDon ThucDonEntity to update
     * @return true if successful, false otherwise
     */
    @Override
    public boolean updateThucDon(ThucDonEntity thucDon) {
        String query = "UPDATE thucdon SET ten_thuc_don = ?, so_ngay = ? WHERE id = ?";
        boolean success = false;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, thucDon.tenThucDon());
            pstmt.setInt(2, thucDon.soNgay());
            pstmt.setInt(3, thucDon.id());
            
            int affectedRows = pstmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Error updating ThucDon: " + e.getMessage());
            e.printStackTrace();
        }
        
        return success;
    }
    
    /**
     * Delete a menu from the database by ID
     * @param id ID of the menu to delete
     * @return true if successful, false otherwise
     */
    @Override
    public boolean deleteThucDon(int id) {
        String query = "DELETE FROM thucdon WHERE id = ?";
        boolean success = false;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Error deleting ThucDon: " + e.getMessage());
            e.printStackTrace();
        }
        
        return success;
    }
} 