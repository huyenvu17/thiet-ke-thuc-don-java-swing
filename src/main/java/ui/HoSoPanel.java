package ui;

import dao.UserDao;
import entity.UserEntity;
import service.AuthService;

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
    private JPasswordField tfPassword, tfConfirmPassword, tfCurrentPassword;
    private JButton btnCapNhat, btnDoiMatKhau;
    private JTable userTable;
    private DefaultTableModel userModel;
    private UserDao userDao = new UserDao();
    private UserEntity currentUserEntity;
    
    public HoSoPanel(UserEntity userEntity) {
        this.currentUserEntity = userEntity;
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(5, 5, 5, 5));
        JLabel titleLabel = new JLabel("THÔNG TIN HỒ SƠ", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Kiểm tra nếu người dùng là null
        if (currentUserEntity == null) {
            JLabel messageLabel = new JLabel("Vui lòng đăng nhập để xem thông tin hồ sơ", JLabel.CENTER);
            messageLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
            add(messageLabel, BorderLayout.CENTER);
            return;
        }

        if (currentUserEntity.getRole().equals("admin")) {
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
            // User thường: tách làm 2 phần - thông tin cá nhân và đổi mật khẩu
            JPanel mainPanel = new JPanel(new GridLayout(1, 2, 15, 0));
            mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            
            // PHẦN 1: FORM THÔNG TIN CÁ NHÂN (BÊN TRÁI)
            JPanel infoPanel = new JPanel(new BorderLayout(0, 10));
            infoPanel.setBorder(BorderFactory.createTitledBorder("Thông tin cá nhân"));
            
            JPanel formInfoPanel = new JPanel();
            formInfoPanel.setLayout(new BoxLayout(formInfoPanel, BoxLayout.Y_AXIS));
            formInfoPanel.setBorder(new EmptyBorder(15, 10, 15, 10));
            
            tfUsername = new JTextField(currentUserEntity.getUsername());
            tfUsername.setEditable(false);
            tfUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
            
            tfHoTen = new JTextField(currentUserEntity.getFullName());
            tfHoTen.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
            
            tfEmail = new JTextField(currentUserEntity.getEmail());
            tfEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
            
            tfSoDienThoai = new JTextField(currentUserEntity.getPhone());
            tfSoDienThoai.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
            
            // Tạo label với căn lề trái
            JLabel lblUsername = new JLabel("Tên đăng nhập:");
            lblUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel lblHoTen = new JLabel("Họ và tên:");
            lblHoTen.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel lblEmail = new JLabel("Email:");
            lblEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel lblSoDienThoai = new JLabel("Số điện thoại:");
            lblSoDienThoai.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            // Đặt căn lề trái cho các text field
            tfUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
            tfHoTen.setAlignmentX(Component.LEFT_ALIGNMENT);
            tfEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
            tfSoDienThoai.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            formInfoPanel.add(lblUsername);
            formInfoPanel.add(Box.createVerticalStrut(5));
            formInfoPanel.add(tfUsername);
            formInfoPanel.add(Box.createVerticalStrut(10));
            
            formInfoPanel.add(lblHoTen);
            formInfoPanel.add(Box.createVerticalStrut(5));
            formInfoPanel.add(tfHoTen);
            formInfoPanel.add(Box.createVerticalStrut(10));
            
            formInfoPanel.add(lblEmail);
            formInfoPanel.add(Box.createVerticalStrut(5));
            formInfoPanel.add(tfEmail);
            formInfoPanel.add(Box.createVerticalStrut(10));
            
            formInfoPanel.add(lblSoDienThoai);
            formInfoPanel.add(Box.createVerticalStrut(5));
            formInfoPanel.add(tfSoDienThoai);
            
            JPanel infoButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            btnCapNhat = new JButton("Cập nhật thông tin");
            btnCapNhat.setPreferredSize(new Dimension(150, 30));
            infoButtonPanel.add(btnCapNhat);
            
            infoPanel.add(formInfoPanel, BorderLayout.CENTER);
            infoPanel.add(infoButtonPanel, BorderLayout.SOUTH);
            
            // PHẦN 2: FORM ĐỔI MẬT KHẨU (BÊN PHẢI)
            JPanel passwordPanel = new JPanel(new BorderLayout(0, 10));
            passwordPanel.setBorder(BorderFactory.createTitledBorder("Đổi mật khẩu"));
            
            JPanel formPasswordPanel = new JPanel();
            formPasswordPanel.setLayout(new BoxLayout(formPasswordPanel, BoxLayout.Y_AXIS));
            formPasswordPanel.setBorder(new EmptyBorder(15, 10, 15, 10));
            
            // Thêm trường mật khẩu cũ
            tfCurrentPassword = new JPasswordField();
            tfCurrentPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
            
            tfPassword = new JPasswordField();
            tfPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
            
            tfConfirmPassword = new JPasswordField();
            tfConfirmPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
            
            // Tạo label với căn lề trái
            JLabel lblCurrentPassword = new JLabel("Mật khẩu hiện tại:");
            lblCurrentPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel lblNewPassword = new JLabel("Mật khẩu mới:");
            lblNewPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel lblConfirmPassword = new JLabel("Xác nhận mật khẩu:");
            lblConfirmPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            // Đặt căn lề trái cho các password field
            tfCurrentPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
            tfPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
            tfConfirmPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            formPasswordPanel.add(lblCurrentPassword);
            formPasswordPanel.add(Box.createVerticalStrut(5));
            formPasswordPanel.add(tfCurrentPassword);
            formPasswordPanel.add(Box.createVerticalStrut(10));
            
            formPasswordPanel.add(lblNewPassword);
            formPasswordPanel.add(Box.createVerticalStrut(5));
            formPasswordPanel.add(tfPassword);
            formPasswordPanel.add(Box.createVerticalStrut(10));
            
            formPasswordPanel.add(lblConfirmPassword);
            formPasswordPanel.add(Box.createVerticalStrut(5));
            formPasswordPanel.add(tfConfirmPassword);
            
            JPanel passwordButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            btnDoiMatKhau = new JButton("Đổi mật khẩu");
            btnDoiMatKhau.setPreferredSize(new Dimension(150, 30));
            passwordButtonPanel.add(btnDoiMatKhau);
            
            passwordPanel.add(formPasswordPanel, BorderLayout.CENTER);
            passwordPanel.add(passwordButtonPanel, BorderLayout.SOUTH);
            
            // Thêm cả hai panel vào main panel
            mainPanel.add(infoPanel);
            mainPanel.add(passwordPanel);
            
            // Thêm main panel vào center của HoSoPanel
            add(mainPanel, BorderLayout.CENTER);
            
            // Thêm action listeners cho các button
            btnCapNhat.addActionListener(e -> handleUpdateProfile());
            btnDoiMatKhau.addActionListener(e -> handleChangePassword());
        }
    }

    public void updateUser(UserEntity userEntity) {
        this.currentUserEntity = userEntity;
        removeAll();
        initComponents();
        revalidate();
        repaint();
    }

    private void loadUserTable() {
        userModel.setRowCount(0);
        List<UserEntity> userEntities = userDao.getAllUsers();
        for (UserEntity u : userEntities) {
            userModel.addRow(new Object[]{u.getId(), u.getUsername(), u.getFullName(), u.getEmail(), u.getPhone(), u.getRole()});
        }
    }

    private void handleChangePassword() {
        // Lấy mật khẩu hiện tại từ form
        String currentPasswordInput = new String(tfCurrentPassword.getPassword());
        String newPassword = new String(tfPassword.getPassword());
        String confirmPassword = new String(tfConfirmPassword.getPassword());
        
        // Kiểm tra mật khẩu hiện tại
        if (currentPasswordInput.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập mật khẩu hiện tại!", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Kiểm tra mật khẩu hiện tại có đúng không - sử dụng AuthService
        if (!AuthService.checkPassword(currentPasswordInput, currentUserEntity.getPassword())) {
            JOptionPane.showMessageDialog(this, 
                "Mật khẩu hiện tại không đúng!", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Kiểm tra mật khẩu mới không được để trống
        if (newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập mật khẩu mới!", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Kiểm tra mật khẩu xác nhận phải trùng với mật khẩu mới
        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, 
                "Mật khẩu xác nhận không khớp với mật khẩu mới!", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Lưu lại mật khẩu cũ để khôi phục nếu cần
        String oldPassword = currentUserEntity.getPassword();
        
        // Cập nhật mật khẩu mới - lưu ý đây là mật khẩu chưa mã hóa
        // UserDao sẽ xử lý việc mã hóa khi updateUser
        currentUserEntity.setPassword(newPassword);
        
        // Cập nhật thông tin người dùng
        if (userDao.updateUser(currentUserEntity)) {
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!");
            // Xóa các trường mật khẩu
            tfCurrentPassword.setText("");
            tfPassword.setText("");
            tfConfirmPassword.setText("");
        } else {
            // Khôi phục mật khẩu cũ nếu cập nhật thất bại
            currentUserEntity.setPassword(oldPassword);
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdateProfile() {
        // Lấy thông tin từ form
        String hoTen = tfHoTen.getText().trim();
        String email = tfEmail.getText().trim();
        String soDienThoai = tfSoDienThoai.getText().trim();
        
        // Kiểm tra tên không được để trống
        if (hoTen.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Họ tên không được để trống!", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Kiểm tra định dạng email
        if (!email.isEmpty() && !isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, 
                "Email không đúng định dạng!", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Kiểm tra định dạng số điện thoại
        if (!soDienThoai.isEmpty() && !isValidPhoneNumber(soDienThoai)) {
            JOptionPane.showMessageDialog(this, 
                "Số điện thoại chỉ được chứa các chữ số!", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Cập nhật thông tin cá nhân (không bao gồm mật khẩu)
        currentUserEntity.setFullName(hoTen);
        currentUserEntity.setEmail(email);
        currentUserEntity.setPhone(soDienThoai);
        
        // Cập nhật thông tin người dùng
        if (userDao.updateUser(currentUserEntity)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công!");
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Kiểm tra định dạng email
     * @param email Chuỗi email cần kiểm tra
     * @return true nếu email đúng định dạng, false nếu không
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }
    
    /**
     * Kiểm tra định dạng số điện thoại
     * @param phoneNumber Chuỗi số điện thoại cần kiểm tra
     * @return true nếu chỉ chứa các chữ số, false nếu không
     */
    private boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^[0-9]+$";
        return phoneNumber.matches(phoneRegex);
    }
} 