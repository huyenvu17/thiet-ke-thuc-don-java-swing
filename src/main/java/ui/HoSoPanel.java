package ui;

import controller.IUserController;
import controller.UserController;
import dto.UserDTO;
import entity.UserEntity;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class HoSoPanel extends JPanel {
    
    private JTextField tfHoTen, tfEmail, tfSoDienThoai, tfUsername;
    private JPasswordField tfPassword, tfConfirmPassword, tfCurrentPassword;
    private JButton btnCapNhat, btnDoiMatKhau;
    private JTable userTable;
    private DefaultTableModel userModel;
    private IUserController userController;
    private UserEntity currentUserEntity;
    private JComboBox<String> cbRole;
    
    public HoSoPanel(UserEntity userEntity) {
        this.currentUserEntity = userEntity;
        this.userController = UserController.getInstance();
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
            JPanel mainPanel = new JPanel(new GridLayout(1, 2, 15, 0));
            mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            
            JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
            tablePanel.setBorder(BorderFactory.createTitledBorder("Danh sách người dùng"));
            
            userModel = new DefaultTableModel(new String[]{"ID", "Username", "Họ tên", "Email", "SĐT", "Role"}, 0) {
                @Override public boolean isCellEditable(int row, int col) { return false; }
            };
            userTable = new JTable(userModel);
            JScrollPane scrollPane = new JScrollPane(userTable);
            loadUserTable();
            tablePanel.add(scrollPane, BorderLayout.CENTER);
            
            JPanel formPanel = new JPanel(new BorderLayout(0, 10));
            formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin người dùng"));
            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
            inputPanel.setBorder(new EmptyBorder(15, 10, 15, 10));
            
            // Khởi tạo các trường nhập liệu
            tfUsername = new JTextField();
            tfUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
            tfPassword = new JPasswordField();
            tfPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
            tfHoTen = new JTextField();
            tfHoTen.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
            tfEmail = new JTextField();
            tfEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
            tfSoDienThoai = new JTextField();
            tfSoDienThoai.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
            cbRole = new JComboBox<>(new String[]{"user", "admin"});
            cbRole.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
            cbRole.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JLabel lblUsername = new JLabel("Username:");
            lblUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel lblPassword = new JLabel("Mật khẩu:");
            lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel lblHoTen = new JLabel("Họ tên:");
            lblHoTen.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel lblEmail = new JLabel("Email:");
            lblEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel lblSoDienThoai = new JLabel("Số điện thoại:");
            lblSoDienThoai.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel lblRole = new JLabel("Vai trò:");
            lblRole.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            tfUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
            tfPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
            tfHoTen.setAlignmentX(Component.LEFT_ALIGNMENT);
            tfEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
            tfSoDienThoai.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            inputPanel.add(lblUsername);
            inputPanel.add(Box.createVerticalStrut(5));
            inputPanel.add(tfUsername);
            inputPanel.add(Box.createVerticalStrut(10));
            
            inputPanel.add(lblPassword);
            inputPanel.add(Box.createVerticalStrut(5));
            inputPanel.add(tfPassword);
            inputPanel.add(Box.createVerticalStrut(10));
            
            inputPanel.add(lblHoTen);
            inputPanel.add(Box.createVerticalStrut(5));
            inputPanel.add(tfHoTen);
            inputPanel.add(Box.createVerticalStrut(10));
            
            inputPanel.add(lblEmail);
            inputPanel.add(Box.createVerticalStrut(5));
            inputPanel.add(tfEmail);
            inputPanel.add(Box.createVerticalStrut(10));
            
            inputPanel.add(lblSoDienThoai);
            inputPanel.add(Box.createVerticalStrut(5));
            inputPanel.add(tfSoDienThoai);
            
            inputPanel.add(lblRole);
            inputPanel.add(Box.createVerticalStrut(10));
            inputPanel.add(cbRole);
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            JButton btnThem = new JButton("Thêm");
            JButton btnSua = new JButton("Sửa");
            JButton btnXoa = new JButton("Xóa");
            JButton btnLamMoi = new JButton("Làm mới");
            
            btnThem.addActionListener(e -> handleAddUser());
            btnSua.addActionListener(e -> handleEditUser());
            btnXoa.addActionListener(e -> handleDeleteUser());
            btnLamMoi.addActionListener(e -> {
                clearForm();
                loadUserTable();
            });
            
            userTable.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = userTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int userId = (int) userModel.getValueAt(selectedRow, 0);
                        UserEntity selectedUser = userController.getUserById(userId);
                        if (selectedUser != null) {
                            tfUsername.setText(selectedUser.getUsername());
                            tfPassword.setForeground(Color.GRAY);
                            tfHoTen.setText(selectedUser.getFullName());
                            tfEmail.setText(selectedUser.getEmail());
                            tfSoDienThoai.setText(selectedUser.getPhone());
                            cbRole.setSelectedItem(selectedUser.getRole());
                        }
                    }
                }
            });
            
            buttonPanel.add(btnThem);
            buttonPanel.add(btnSua);
            buttonPanel.add(btnXoa);
            buttonPanel.add(btnLamMoi);
            formPanel.add(inputPanel, BorderLayout.CENTER);
            formPanel.add(buttonPanel, BorderLayout.SOUTH);
            mainPanel.add(tablePanel);
            mainPanel.add(formPanel);
            
            add(mainPanel, BorderLayout.CENTER);
        } else {
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
            
            JLabel lblUsername = new JLabel("Tên đăng nhập:");
            lblUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel lblHoTen = new JLabel("Họ và tên:");
            lblHoTen.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel lblEmail = new JLabel("Email:");
            lblEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel lblSoDienThoai = new JLabel("Số điện thoại:");
            lblSoDienThoai.setAlignmentX(Component.LEFT_ALIGNMENT);
            
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
            
            tfCurrentPassword = new JPasswordField();
            tfCurrentPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
            
            tfPassword = new JPasswordField();
            tfPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
            
            tfConfirmPassword = new JPasswordField();
            tfConfirmPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
            
            JLabel lblCurrentPassword = new JLabel("Mật khẩu hiện tại:");
            lblCurrentPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel lblNewPassword = new JLabel("Mật khẩu mới:");
            lblNewPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel lblConfirmPassword = new JLabel("Xác nhận mật khẩu:");
            lblConfirmPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
            
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
            
            mainPanel.add(infoPanel);
            mainPanel.add(passwordPanel);
            add(mainPanel, BorderLayout.CENTER);
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
        List<UserEntity> userEntities = userController.getAllUsers();
        for (UserEntity u : userEntities) {
            userModel.addRow(new Object[]{u.getId(), u.getUsername(), u.getFullName(), u.getEmail(), u.getPhone(), u.getRole()});
        }
    }

    private void handleChangePassword() {
        String currentPasswordInput = new String(tfCurrentPassword.getPassword());
        String newPassword = new String(tfPassword.getPassword());
        String confirmPassword = new String(tfConfirmPassword.getPassword());
        
        if (currentPasswordInput.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập mật khẩu hiện tại!", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập mật khẩu mới!", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, 
                "Mật khẩu xác nhận không khớp với mật khẩu mới!", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (userController.changePassword(currentUserEntity.getId(), currentPasswordInput, newPassword)) {
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!");
            tfCurrentPassword.setText("");
            tfPassword.setText("");
            tfConfirmPassword.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thất bại! Kiểm tra lại mật khẩu hiện tại.", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdateProfile() {
        String hoTen = tfHoTen.getText().trim();
        String email = tfEmail.getText().trim();
        String soDienThoai = tfSoDienThoai.getText().trim();
        
        if (hoTen.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Họ tên không được để trống!", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!email.isEmpty() && !userController.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, 
                "Email không đúng định dạng!", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!soDienThoai.isEmpty() && !userController.isValidPhoneNumber(soDienThoai)) {
            JOptionPane.showMessageDialog(this, 
                "Số điện thoại chỉ được chứa các chữ số!", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        UserDTO userDto = new UserDTO(
                currentUserEntity.getId(),
                currentUserEntity.getUsername(),
                currentUserEntity.getPassword(),
                hoTen,
                email,
                soDienThoai,
                currentUserEntity.getRole()
        );
        
        if (userController.updateUserProfile(userDto)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công!");
            currentUserEntity.setFullName(hoTen);
            currentUserEntity.setEmail(email);
            currentUserEntity.setPhone(soDienThoai);
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        tfUsername.setText("");
        tfPassword.setText("");
        tfPassword.setEchoChar('*');
        tfPassword.setForeground(Color.BLACK);
        tfHoTen.setText("");
        tfEmail.setText("");
        tfSoDienThoai.setText("");
        cbRole.setSelectedIndex(0);
        userTable.clearSelection();
    }

    private void handleAddUser() {
        String username = tfUsername.getText().trim();
        String password = new String(tfPassword.getPassword());
        String fullName = tfHoTen.getText().trim();
        String email = tfEmail.getText().trim();
        String phone = tfSoDienThoai.getText().trim();
        String role = (String) cbRole.getSelectedItem();
        
        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin bắt buộc!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!email.isEmpty() && !userController.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Email không đúng định dạng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!phone.isEmpty() && !userController.isValidPhoneNumber(phone)) {
            JOptionPane.showMessageDialog(this, "Số điện thoại chỉ được chứa các chữ số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        UserDTO newUserDto = new UserDTO(
                0, // ID will be assigned by database
                username,
                password,
                fullName,
                email,
                phone,
                role
        );
        
        if (userController.addUser(newUserDto)) {
            JOptionPane.showMessageDialog(this, "Thêm người dùng thành công!");
            clearForm();
            loadUserTable();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm người dùng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleEditUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn người dùng cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int userId = (int) userModel.getValueAt(selectedRow, 0);
        UserEntity userToEdit = userController.getUserById(userId);
        if (userToEdit == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin người dùng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String fullName = tfHoTen.getText().trim();
        String email = tfEmail.getText().trim();
        String phone = tfSoDienThoai.getText().trim();
        String role = (String) cbRole.getSelectedItem();
        
        if (fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ tên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!email.isEmpty() && !userController.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Email không đúng định dạng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!phone.isEmpty() && !userController.isValidPhoneNumber(phone)) {
            JOptionPane.showMessageDialog(this, "Số điện thoại chỉ được chứa các chữ số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        UserDTO updateUserDto = new UserDTO(
                userId,
                userToEdit.getUsername(),
                userToEdit.getPassword(),
                fullName,
                email,
                phone,
                role
        );
        
        String newPassword = new String(tfPassword.getPassword());
        if (!newPassword.isEmpty() && !newPassword.equals("(Để trống nếu không đổi mật khẩu)")) {
            updateUserDto.setPassword(newPassword);
        }
        
        if (userController.updateUserProfile(updateUserDto)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công!");
            clearForm();
            loadUserTable();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleDeleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn người dùng cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int userId = (int) userModel.getValueAt(selectedRow, 0);
        String username = (String) userModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc chắn muốn xóa người dùng " + username + "?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            if (userController.deleteUser(userId)) {
                JOptionPane.showMessageDialog(this, "Xóa người dùng thành công!");
                loadUserTable();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa người dùng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
} 