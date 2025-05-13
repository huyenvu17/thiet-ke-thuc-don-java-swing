package dao;

import entity.NhomThucPhamEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho bảng nhóm thực phẩm
 */
public class NhomThucPhamDao implements INhomThucPhamDao {
    
    private static NhomThucPhamDao instance;
    
    /**
     * Singleton pattern - Lấy instance duy nhất
     */
    public static NhomThucPhamDao getInstance() {
        if (instance == null) {
            instance = new NhomThucPhamDao();
        }
        return instance;
    }
    
    /**
     * Constructor riêng tư - theo mẫu Singleton
     */
    private NhomThucPhamDao() {
    }
    
    /**
     * Map ResultSet thành đối tượng NhomThucPhamEntity
     */
    private NhomThucPhamEntity mapResultSetToNhomThucPham(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String tenNhom = rs.getString("ten_nhom");
        String moTa = rs.getString("mo_ta");
        
        return new NhomThucPhamEntity(id, tenNhom, moTa);
    }
    
    /**
     * Lấy danh sách tất cả nhóm thực phẩm
     */
    @Override
    public List<NhomThucPhamEntity> getAllNhomThucPham() {
        List<NhomThucPhamEntity> list = new ArrayList<>();
        
        try (Connection conn = new DatabaseConnection().connection;
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM vw_nhomthucpham ORDER BY id")) {
            
            while (rs.next()) {
                list.add(mapResultSetToNhomThucPham(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách nhóm thực phẩm: " + e.getMessage());
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * Lấy nhóm thực phẩm theo ID
     */
    @Override
    public NhomThucPhamEntity getById(int id) {
        NhomThucPhamEntity entity = null;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM nhomthucpham WHERE id = ?")) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    entity = mapResultSetToNhomThucPham(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy nhóm thực phẩm theo ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return entity;
    }
    
    /**
     * Lưu nhóm thực phẩm mới
     */
    @Override
    public int addNhomThucPham(NhomThucPhamEntity entity) {
        String sql = "INSERT INTO nhomthucpham (ten_nhom, mo_ta) VALUES (?, ?)";
        int newId = -1;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, entity.tenNhom());
            stmt.setString(2, entity.moTa());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Thêm nhóm thực phẩm thất bại, không có dòng nào được thêm.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newId = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm nhóm thực phẩm: " + e.getMessage());
            e.printStackTrace();
        }
        
        return newId;
    }
    
    /**
     * Cập nhật nhóm thực phẩm
     */
    @Override
    public boolean updateNhomThucPham(NhomThucPhamEntity entity) {
        String sql = "UPDATE nhomthucpham SET ten_nhom = ?, mo_ta = ? WHERE id = ?";
        boolean success = false;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, entity.tenNhom());
            stmt.setString(2, entity.moTa());
            stmt.setInt(3, entity.id());
            
            int affectedRows = stmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật nhóm thực phẩm: " + e.getMessage());
            e.printStackTrace();
        }
        
        return success;
    }
    
    /**
     * Xóa nhóm thực phẩm
     */
    @Override
    public boolean deleteNhomThucPham(int id) {
        String sql = "DELETE FROM nhomthucpham WHERE id = ?";
        boolean success = false;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa nhóm thực phẩm: " + e.getMessage());
            e.printStackTrace();
        }
        
        return success;
    }
    
    /**
     * Lấy số lượng nguyên liệu thuộc nhóm thực phẩm
     */
    @Override
    public int getSoLuongNguyenLieu(int nhomThucPhamId) {
        int soLuong = 0;
        
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) AS so_luong FROM nguyenlieu WHERE nhom_thuc_pham_id = ?")) {
            
            stmt.setInt(1, nhomThucPhamId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    soLuong = rs.getInt("so_luong");
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy số lượng nguyên liệu thuộc nhóm: " + e.getMessage());
            e.printStackTrace();
        }
        
        return soLuong;
    }
    
    /**
     * Phương thức tiện ích để thực hiện gọi save theo ý muốn của panel
     */
    public int save(NhomThucPhamEntity entity) {
        return addNhomThucPham(entity);
    }
    
    /**
     * Phương thức tiện ích để thực hiện gọi update theo ý muốn của panel
     */
    public boolean update(NhomThucPhamEntity entity) {
        return updateNhomThucPham(entity);
    }
    
    /**
     * Phương thức tiện ích để thực hiện gọi delete theo ý muốn của panel
     */
    public boolean delete(int id) {
        return deleteNhomThucPham(id);
    }
} 