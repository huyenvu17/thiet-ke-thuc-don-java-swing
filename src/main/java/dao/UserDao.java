package dao;

import entity.User;
import service.AuthService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    /**
     * Tìm người dùng theo tên đăng nhập
     */
    public User findByUsername(String username) {
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
    public boolean addUser(User user) {
        String sql = "INSERT INTO user (username, password, full_name, email, phone, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            
            // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
            String hashedPassword = AuthService.hashPassword(user.getPassword());
            ps.setString(2, hashedPassword);
            
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPhone());
            ps.setString(6, user.getRole());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xác thực người dùng
     */
    public User login(String username, String password) {
        // Tìm người dùng theo tên đăng nhập
        User user = findByUsername(username);
        if (user != null) {
            // Kiểm tra mật khẩu
            if (AuthService.checkPassword(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    /**
     * Cập nhật thông tin người dùng
     */
    public boolean updateUser(User user) {
        // Lấy thông tin người dùng hiện tại
        User existingUser = findById(user.getId());
        if (existingUser == null) {
            return false;
        }
        
        // Kiểm tra xem mật khẩu đã thay đổi chưa
        boolean passwordChanged = !user.getPassword().equals(existingUser.getPassword());
        
        String sql = "UPDATE user SET full_name=?, email=?, phone=?, password=?, role=? WHERE id=?";
        try (Connection conn = new DatabaseConnection().connection;
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            
            // Nếu mật khẩu thay đổi, mã hóa mật khẩu mới
            if (passwordChanged) {
                ps.setString(4, AuthService.hashPassword(user.getPassword()));
            } else {
                ps.setString(4, user.getPassword()); // Giữ mật khẩu cũ
            }
            
            ps.setString(5, user.getRole());
            ps.setInt(6, user.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Tìm người dùng theo ID
     */
    public User findById(int id) {
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
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
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
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        return new User(
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