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
 *
 * @author ADMIN
 */
public class MonAnDao implements IMonAnDao {
    
    private static MonAnDao instance;
    
    public static synchronized MonAnDao getInstance() {
        if (MonAnDao.instance == null) {
            MonAnDao.instance = new MonAnDao();
        }
        return instance;
    }

    private MonAnDao() {
    }

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