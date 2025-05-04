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
 * Data Access Object for MonAnEntity
 * Handles database operations for dishes
 */
public class MonAnDao implements IMonAnDao {
    
    private static MonAnDao instance;
    
    public static MonAnDao getInstance() {
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
     * Map a database result set to a MonAnEntity object
     */
    private MonAnEntity mapResultSetToMonAn(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String tenMon = rs.getString("ten_mon");
        String loaiMon = rs.getString("loai_mon");
        String cachCheBien = rs.getString("cach_che_bien");
        
        return new MonAnEntity(id, tenMon, loaiMon, cachCheBien);
    }

    @Override
    public List<MonAnEntity> getAllMonAn() {
        List<MonAnEntity> monAns = new ArrayList<>();
        
        try (Connection conn = new DatabaseConnection().connection;
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM monan")) {
            
            while (rs.next()) {
                monAns.add(mapResultSetToMonAn(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all mon an: " + e.getMessage());
            e.printStackTrace();
        }
        
        return monAns;
    }
    
    @Override
    public MonAnEntity getById(int id) {
        MonAnEntity entity = null;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM monan WHERE id = ?")) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    entity = mapResultSetToMonAn(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving mon an by id: " + e.getMessage());
            e.printStackTrace();
        }
        
        return entity;
    }

    @Override
    public int addMonAn(MonAnEntity monAn) {
        String sql = "INSERT INTO monan (ten_mon, loai_mon, cach_che_bien) VALUES (?, ?, ?)";
        int newId = -1;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, monAn.tenMon());
            stmt.setString(2, monAn.loaiMon());
            stmt.setString(3, monAn.cachCheBien());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating MonAn failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newId = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding mon an: " + e.getMessage());
            e.printStackTrace();
        }
        
        return newId;
    }

    @Override
    public boolean updateMonAn(MonAnEntity monAn) {
        String sql = "UPDATE monan SET ten_mon = ?, loai_mon = ?, cach_che_bien = ? WHERE id = ?";
        boolean success = false;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, monAn.tenMon());
            stmt.setString(2, monAn.loaiMon());
            stmt.setString(3, monAn.cachCheBien());
            stmt.setInt(4, monAn.id());
            
            int affectedRows = stmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Error updating mon an: " + e.getMessage());
            e.printStackTrace();
        }
        
        return success;
    }

    @Override
    public boolean deleteMonAn(int id) {
        String sql = "DELETE FROM monan WHERE id = ?";
        boolean success = false;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Error deleting mon an: " + e.getMessage());
            e.printStackTrace();
        }
        
        return success;
    }
} 