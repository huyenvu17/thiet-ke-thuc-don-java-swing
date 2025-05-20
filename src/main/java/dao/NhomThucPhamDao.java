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
    
    public static NhomThucPhamDao getInstance() {
        if (instance == null) {
            instance = new NhomThucPhamDao();
        }
        return instance;
    }
    
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
        String sql = "SELECT * FROM vw_nhomthucpham ORDER BY id";
        try (DatabaseConnection provider = new DatabaseConnection()) {
            ResultSet rs = provider.executeQuery(sql);
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
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * FROM nhomthucpham WHERE id = ");
        sqlBuilder.append(id);
        
        try (DatabaseConnection provider = new DatabaseConnection()) {
            ResultSet rs = provider.executeQuery(sqlBuilder.toString());
            if (rs.next()) {
                entity = mapResultSetToNhomThucPham(rs);
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
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("INSERT INTO nhomthucpham");
        sqlBuilder.append(" (ten_nhom, mo_ta)");
        sqlBuilder.append(" VALUES (");
        sqlBuilder.append("'").append(entity.tenNhom()).append("'");
        sqlBuilder.append(", '").append(entity.moTa()).append("'");
        sqlBuilder.append(")");
        
        int newId = -1;
        String sql = "SELECT LAST_INSERT_ID()";
        try (DatabaseConnection provider = new DatabaseConnection()) {
            int affectedRows = provider.executeUpdate(sqlBuilder.toString());
            
            if (affectedRows > 0) {
                ResultSet rs = provider.executeQuery(sql);
                if (rs.next()) {
                    newId = rs.getInt(1);
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
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("UPDATE nhomthucpham SET ");
        sqlBuilder.append("ten_nhom = '").append(entity.tenNhom()).append("'");
        sqlBuilder.append(", mo_ta = '").append(entity.moTa()).append("'");
        sqlBuilder.append(" WHERE id = ").append(entity.id());
        
        boolean success = false;
        
        try (DatabaseConnection provider = new DatabaseConnection()) {
            int affectedRows = provider.executeUpdate(sqlBuilder.toString());
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
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("DELETE FROM nhomthucpham WHERE id = ");
        sqlBuilder.append(id);
        
        boolean success = false;
        
        try (DatabaseConnection provider = new DatabaseConnection()) {
            int affectedRows = provider.executeUpdate(sqlBuilder.toString());
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
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT COUNT(*) AS so_luong FROM nguyenlieu WHERE nhom_thuc_pham_id = ");
        sqlBuilder.append(nhomThucPhamId);
        
        int soLuong = 0;
        
        try (DatabaseConnection provider = new DatabaseConnection()) {
            ResultSet rs = provider.executeQuery(sqlBuilder.toString());
            if (rs.next()) {
                soLuong = rs.getInt("so_luong");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy số lượng nguyên liệu thuộc nhóm: " + e.getMessage());
            e.printStackTrace();
        }
        
        return soLuong;
    }
} 