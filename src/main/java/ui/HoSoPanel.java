package ui;

import dao.UserDao;
import entity.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel thông tin hồ sơ người dùng
 */
public class HoSoPanel extends JPanel {
    
    private JTextField tfHoTen, tfEmail, tfSoDienThoai, tfUsername, tfRole;
    private JPasswordField tfPassword, tfConfirmPassword;
    private JButton btnCapNhat, btnDoiMatKhau;
    private JTable userTable;
    private DefaultTableModel userModel;
    private UserDao userDao = new UserDao();
    private User currentUser;
    
    public HoSoPanel(User user) {
        this.currentUser = user;
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(5, 5, 5, 5));
        JLabel titleLabel = new JLabel("THÔNG TIN HỒ SƠ", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Kiểm tra nếu người dùng là null
        if (currentUser == null) {
            JLabel messageLabel = new JLabel("Vui lòng đăng nhập để xem thông tin hồ sơ", JLabel.CENTER);
            messageLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
            add(messageLabel, BorderLayout.CENTER);
            return;
        }

        if (currentUser.getRole().equals("admin")) {
            // Admin: hiển thị bảng user
            userModel = new DefaultTableModel(new String[]{"ID", "Username", "Họ tên", "Email", "SĐT", "Role"}, 0) {
                @Override public boolean isCellEditable(int row, int col) { return false; }
            };
            userTable = new JTable(userModel);
            JScrollPane scrollPane = new JScrollPane(userTable);
            loadUserTable();
            add(scrollPane, BorderLayout.CENTER);
            // Thêm nút thao tác (thêm/sửa/xóa)
            JPanel buttonPanel = new JPanel();
            JButton btnThem = new JButton("Thêm user");
            JButton btnSua = new JButton("Sửa user");
            JButton btnXoa = new JButton("Xóa user");
            buttonPanel.add(btnThem); buttonPanel.add(btnSua); buttonPanel.add(btnXoa);
            add(buttonPanel, BorderLayout.SOUTH);
            // TODO: Thêm sự kiện cho các nút này
        } else {
            // User thường: chỉ xem/sửa thông tin cá nhân
            JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
            formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            tfUsername = new JTextField(currentUser.getUsername());
            tfUsername.setEditable(false);
            tfHoTen = new JTextField(currentUser.getFullName());
            tfEmail = new JTextField(currentUser.getEmail());
            tfSoDienThoai = new JTextField(currentUser.getPhone());
            tfRole = new JTextField(currentUser.getRole());
            tfRole.setEditable(false);
            tfPassword = new JPasswordField();
            tfConfirmPassword = new JPasswordField();
            formPanel.add(new JLabel("Tên đăng nhập:"));
            formPanel.add(tfUsername);
            formPanel.add(new JLabel("Họ và tên:"));
            formPanel.add(tfHoTen);
            formPanel.add(new JLabel("Email:"));
            formPanel.add(tfEmail);
            formPanel.add(new JLabel("Số điện thoại:"));
            formPanel.add(tfSoDienThoai);
            formPanel.add(new JLabel("Quyền:"));
            formPanel.add(tfRole);
            formPanel.add(new JLabel("Mật khẩu mới (để trống nếu không đổi):"));
            formPanel.add(tfPassword);
            formPanel.add(new JLabel("Xác nhận mật khẩu mới:"));
            formPanel.add(tfConfirmPassword);
            btnCapNhat = new JButton("Cập nhật thông tin");
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(btnCapNhat);
            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.add(formPanel, BorderLayout.NORTH);
            centerPanel.add(buttonPanel, BorderLayout.CENTER);
            add(centerPanel, BorderLayout.CENTER);
            btnCapNhat.addActionListener(e -> handleUpdateProfile());
        }
    }

    public void updateUser(User user) {
        this.currentUser = user;
        removeAll();
        initComponents();
        revalidate();
        repaint();
    }

    private void loadUserTable() {
        userModel.setRowCount(0);
        List<User> users = userDao.getAllUsers();
        for (User u : users) {
            userModel.addRow(new Object[]{u.getId(), u.getUsername(), u.getFullName(), u.getEmail(), u.getPhone(), u.getRole()});
        }
    }

    private void handleUpdateProfile() {
        // Cập nhật thông tin cơ bản
        currentUser.setFullName(tfHoTen.getText().trim());
        currentUser.setEmail(tfEmail.getText().trim());
        currentUser.setPhone(tfSoDienThoai.getText().trim());
        
        // Kiểm tra mật khẩu mới
        String newPassword = new String(tfPassword.getPassword());
        String confirmPassword = new String(tfConfirmPassword.getPassword());
        
        if (!newPassword.isEmpty()) {
            // Người dùng muốn đổi mật khẩu
            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, 
                    "Mật khẩu xác nhận không khớp với mật khẩu mới!", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Cập nhật mật khẩu mới
            currentUser.setPassword(newPassword);
        }
        
        // Cập nhật thông tin người dùng
        if (userDao.updateUser(currentUser)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công!");
            // Xóa các trường mật khẩu
            tfPassword.setText("");
            tfConfirmPassword.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
} 