package dao;

import entity.ChiTietThucDonEntity;
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
public class ChiTietThucDonDao implements IChiTietThucDonDao {
    
    private static ChiTietThucDonDao instance;

    public static synchronized ChiTietThucDonDao getInstance() {
        if (ChiTietThucDonDao.instance == null) {
            ChiTietThucDonDao.instance = new ChiTietThucDonDao();
        }
        return instance;
    }

    private ChiTietThucDonDao() {
    }

    @Override
    public List<ChiTietThucDonEntity> getAllChiTietThucDon() {
        List<ChiTietThucDonEntity> chiTietList = new ArrayList<>();
        String query = "SELECT ct.id, ct.thuc_don_id, ct.ngay, ct.buoi, ct.mon_an_id, m.ten_mon " +
                       "FROM chitietthucdon ct " +
                       "JOIN monan m ON ct.mon_an_id = m.id";
        
        try (Connection conn = new DatabaseConnection().connection;
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                ChiTietThucDonEntity chiTiet = new ChiTietThucDonEntity(
                        rs.getInt("id"),
                        rs.getInt("thuc_don_id"),
                        rs.getInt("ngay"),
                        rs.getString("buoi"),
                        rs.getInt("mon_an_id"),
                        rs.getString("ten_mon")
                );
                chiTietList.add(chiTiet);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving ChiTietThucDon list: " + e.getMessage());
            e.printStackTrace();
        }
        
        return chiTietList;
    }

    @Override
    public List<ChiTietThucDonEntity> getByThucDonId(int thucDonId) {
        List<ChiTietThucDonEntity> chiTietList = new ArrayList<>();
        String query = "SELECT ct.id, ct.thuc_don_id, ct.ngay, ct.buoi, ct.mon_an_id, m.ten_mon " +
                       "FROM chitietthucdon ct " +
                       "JOIN monan m ON ct.mon_an_id = m.id " +
                       "WHERE ct.thuc_don_id = ?";
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, thucDonId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ChiTietThucDonEntity chiTiet = new ChiTietThucDonEntity(
                            rs.getInt("id"),
                            rs.getInt("thuc_don_id"),
                            rs.getInt("ngay"),
                            rs.getString("buoi"),
                            rs.getInt("mon_an_id"),
                            rs.getString("ten_mon")
                    );
                    chiTietList.add(chiTiet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving ChiTietThucDon by thucDonId: " + e.getMessage());
            e.printStackTrace();
        }
        
        return chiTietList;
    }

    @Override
    public int addChiTietThucDon(ChiTietThucDonEntity chiTiet) {
        String query = "INSERT INTO chitietthucdon (thuc_don_id, ngay, buoi, mon_an_id) VALUES (?, ?, ?, ?)";
        int newId = -1;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, chiTiet.thucDonId());
            pstmt.setInt(2, chiTiet.ngay());
            pstmt.setString(3, chiTiet.buoi());
            pstmt.setInt(4, chiTiet.monAnId());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating ChiTietThucDon failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newId = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding ChiTietThucDon: " + e.getMessage());
            e.printStackTrace();
        }
        
        return newId;
    }

    @Override
    public ChiTietThucDonEntity getChiTietThucDonById(int id) {
        String query = "SELECT ct.id, ct.thuc_don_id, ct.ngay, ct.buoi, ct.mon_an_id, m.ten_mon " +
                       "FROM chitietthucdon ct " +
                       "JOIN monan m ON ct.mon_an_id = m.id " +
                       "WHERE ct.id = ?";
        ChiTietThucDonEntity chiTiet = null;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    chiTiet = new ChiTietThucDonEntity(
                            rs.getInt("id"),
                            rs.getInt("thuc_don_id"),
                            rs.getInt("ngay"),
                            rs.getString("buoi"),
                            rs.getInt("mon_an_id"),
                            rs.getString("ten_mon")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving ChiTietThucDon by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return chiTiet;
    }

    @Override
    public boolean updateChiTietThucDon(ChiTietThucDonEntity chiTiet) {
        String query = "UPDATE chitietthucdon SET thuc_don_id = ?, ngay = ?, buoi = ?, mon_an_id = ? WHERE id = ?";
        boolean success = false;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, chiTiet.thucDonId());
            pstmt.setInt(2, chiTiet.ngay());
            pstmt.setString(3, chiTiet.buoi());
            pstmt.setInt(4, chiTiet.monAnId());
            pstmt.setInt(5, chiTiet.id());
            
            int affectedRows = pstmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Error updating ChiTietThucDon: " + e.getMessage());
            e.printStackTrace();
        }
        
        return success;
    }

    @Override
    public boolean deleteChiTietThucDon(int id) {
        String query = "DELETE FROM chitietthucdon WHERE id = ?";
        boolean success = false;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Error deleting ChiTietThucDon: " + e.getMessage());
            e.printStackTrace();
        }
        
        return success;
    }

    @Override
    public boolean deleteByThucDonId(int thucDonId) {
        String query = "DELETE FROM chitietthucdon WHERE thuc_don_id = ?";
        boolean success = false;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, thucDonId);
            
            int affectedRows = pstmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Error deleting ChiTietThucDon by thucDonId: " + e.getMessage());
            e.printStackTrace();
        }
        
        return success;
    }
} 