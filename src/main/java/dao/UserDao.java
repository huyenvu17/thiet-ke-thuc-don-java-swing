package dao;

import entity.UserEntity;
import service.AuthService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    /**
     * Tìm người dùng theo tên đăng nhập
     */
    public UserEntity findByUsername(String username) {
        String sql = "SELECT * FROM user WHERE username = ?";
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Thêm người dùng mới
     */
    public boolean addUser(UserEntity userEntity) {
        String sql = "INSERT INTO user (username, password, full_name, email, phone, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userEntity.getUsername());
            
            // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
            String hashedPassword = AuthService.hashPassword(userEntity.getPassword());
            ps.setString(2, hashedPassword);
            
            ps.setString(3, userEntity.getFullName());
            ps.setString(4, userEntity.getEmail());
            ps.setString(5, userEntity.getPhone());
            ps.setString(6, userEntity.getRole());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xác thực người dùng
     */
    public UserEntity login(String username, String password) {
        // Tìm người dùng theo tên đăng nhập
        UserEntity userEntity = findByUsername(username);
        if (userEntity != null) {
            // Kiểm tra mật khẩu
            if (AuthService.checkPassword(password, userEntity.getPassword())) {
                return userEntity;
            }
        }
        return null;
    }

    /**
     * Cập nhật thông tin người dùng
     */
    public boolean updateUser(UserEntity userEntity) {
        // Lấy thông tin người dùng hiện tại
        UserEntity existingUserEntity = findById(userEntity.getId());
        if (existingUserEntity == null) {
            return false;
        }
        
        // Kiểm tra xem mật khẩu đã thay đổi chưa
        boolean passwordChanged = !userEntity.getPassword().equals(existingUserEntity.getPassword());
        
        String sql = "UPDATE user SET full_name=?, email=?, phone=?, password=?, role=? WHERE id=?";
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userEntity.getFullName());
            ps.setString(2, userEntity.getEmail());
            ps.setString(3, userEntity.getPhone());
            
            // Nếu mật khẩu thay đổi, mã hóa mật khẩu mới
            if (passwordChanged) {
                ps.setString(4, AuthService.hashPassword(userEntity.getPassword()));
            } else {
                ps.setString(4, userEntity.getPassword()); // Giữ mật khẩu cũ
            }
            
            ps.setString(5, userEntity.getRole());
            ps.setInt(6, userEntity.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Tìm người dùng theo ID
     */
    public UserEntity findById(int id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Xóa người dùng
     */
    public boolean deleteUser(int id) {
        String sql = "DELETE FROM user WHERE id=?";
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy danh sách tất cả người dùng
     */
    public List<UserEntity> getAllUsers() {
        List<UserEntity> list = new ArrayList<>();
        String sql = "SELECT * FROM user";
        try (Connection conn = new DatabaseConnection().connection;
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Chuyển đổi ResultSet thành đối tượng User
     */
    private UserEntity mapResultSetToUser(ResultSet rs) throws SQLException {
        return new UserEntity(
            rs.getInt("id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("full_name"),
            rs.getString("email"),
            rs.getString("phone"),
            rs.getString("role")
        );
    }
} 