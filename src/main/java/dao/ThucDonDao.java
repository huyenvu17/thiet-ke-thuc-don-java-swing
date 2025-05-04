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
 * Data Access Object for ThucDonEntity
 * Handles database operations for menus
 */
public class ThucDonDao implements IThucDonDao {
    
    private static ThucDonDao instance;
    
    public static ThucDonDao getInstance() {
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
     * Map a database result set to a ThucDonEntity object
     */
    private ThucDonEntity mapResultSetToThucDon(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String tenThucDon = rs.getString("ten_thuc_don");
        int soNgay = rs.getInt("so_ngay");
        
        return new ThucDonEntity(id, tenThucDon, soNgay);
    }

    @Override
    public List<ThucDonEntity> getAllThucDon() {
        List<ThucDonEntity> thucDons = new ArrayList<>();
        
        try (Connection conn = new DatabaseConnection().connection;
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM thucdon")) {
            
            while (rs.next()) {
                thucDons.add(mapResultSetToThucDon(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all thuc don: " + e.getMessage());
            e.printStackTrace();
        }
        
        return thucDons;
    }
    
    @Override
    public ThucDonEntity getById(int id) {
        ThucDonEntity entity = null;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM thucdon WHERE id = ?")) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    entity = mapResultSetToThucDon(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving thuc don by id: " + e.getMessage());
            e.printStackTrace();
        }
        
        return entity;
    }

    @Override
    public int addThucDon(ThucDonEntity thucDon) {
        String sql = "INSERT INTO thucdon (ten_thuc_don, so_ngay) VALUES (?, ?)";
        int newId = -1;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, thucDon.tenThucDon());
            stmt.setInt(2, thucDon.soNgay());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating ThucDon failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newId = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding thuc don: " + e.getMessage());
            e.printStackTrace();
        }
        
        return newId;
    }

    @Override
    public boolean updateThucDon(ThucDonEntity thucDon) {
        String sql = "UPDATE thucdon SET ten_thuc_don = ?, so_ngay = ? WHERE id = ?";
        boolean success = false;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, thucDon.tenThucDon());
            stmt.setInt(2, thucDon.soNgay());
            stmt.setInt(3, thucDon.id());
            
            int affectedRows = stmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Error updating thuc don: " + e.getMessage());
            e.printStackTrace();
        }
        
        return success;
    }

    @Override
    public boolean deleteThucDon(int id) {
        String sql = "DELETE FROM thucdon WHERE id = ?";
        boolean success = false;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Error deleting thuc don: " + e.getMessage());
            e.printStackTrace();
        }
        
        return success;
    }
} 