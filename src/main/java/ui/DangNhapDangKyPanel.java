package ui;

import controller.AuthController;
import controller.IAuthController;
import dto.UserDTO;
import entity.UserEntity;
import service.AuthService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author ADMIN
 */
public class DangNhapDangKyPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JTextField loginUsernameField, registerUsernameField, registerFullNameField, registerEmailField, registerPhoneField;
    private JPasswordField loginPasswordField, registerPasswordField, registerConfirmPasswordField;
    private JButton loginButton, switchToRegisterButton, registerButton, switchToLoginButton;
    private IAuthController authController;
    private AuthenticationListener authListener;
    
    public interface AuthenticationListener {
        void onAuthenticationSuccess(UserEntity userEntity);
    }

    public DangNhapDangKyPanel(AuthenticationListener listener) {
        this.authListener = listener;
        this.authController = AuthController.getInstance();
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        initLoginPanel();
        initRegisterPanel();

        setLayout(new BorderLayout());
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(cardPanel);
        add(centerPanel, BorderLayout.CENTER);
        setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    private void initLoginPanel() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tiêu đề "Đăng nhập"
        JLabel titleLabel = new JLabel("ĐĂNG NHẬP", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(titleLabel, gbc);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(new JLabel("Tên đăng nhập:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        loginUsernameField = new JTextField(30);
        loginUsernameField.setPreferredSize(new Dimension(300, 25));
        loginPanel.add(loginUsernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        loginPanel.add(new JLabel("Mật khẩu:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        loginPasswordField = new JPasswordField(30);
        loginPasswordField.setPreferredSize(new Dimension(300, 25));
        loginPanel.add(loginPasswordField, gbc);

        // Nút "Đăng nhập"
        gbc.insets = new Insets(10, 10, 5, 10); // Sử dụng insets giống với các trường nhập liệu
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        loginButton = new JButton("Đăng Nhập");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginPanel.add(loginButton, gbc);

        // Dòng "Chưa có tài khoản? Đăng ký"
        gbc.insets = new Insets(5, 10, 10, 10);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        
        JPanel linkPanel = new JPanel();
        linkPanel.setLayout(new BoxLayout(linkPanel, BoxLayout.X_AXIS));
        linkPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        linkPanel.setBackground(loginPanel.getBackground());
        
        JLabel prefixLabel = new JLabel("Chưa có tài khoản?");
        JLabel registerLinkLabel = new JLabel("<html><u>Đăng ký</u></html>");
        registerLinkLabel.setForeground(Color.BLUE);
        registerLinkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLinkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardPanel, "register");
            }
        });

        linkPanel.add(prefixLabel);
        linkPanel.add(Box.createRigidArea(new Dimension(5, 0))); // Khoảng cách 5px
        linkPanel.add(registerLinkLabel);
        
        loginPanel.add(linkPanel, gbc);

        loginButton.addActionListener(e -> handleLogin());

        cardPanel.add(loginPanel, "login");
    }

    private void initRegisterPanel() {
        JPanel registerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 5, 10); // Khoảng cách giữa các thành phần
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tiêu đề "Đăng ký"
        JLabel titleLabel = new JLabel("ĐĂNG KÝ", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        registerPanel.add(titleLabel, gbc);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        registerPanel.add(new JLabel("Tên đăng nhập:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        registerUsernameField = new JTextField(30);
        registerUsernameField.setPreferredSize(new Dimension(300, 25));
        registerPanel.add(registerUsernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        registerPanel.add(new JLabel("Mật khẩu:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        registerPasswordField = new JPasswordField(30);
        registerPasswordField.setPreferredSize(new Dimension(300, 25));
        registerPanel.add(registerPasswordField, gbc);

        // Confirm Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        registerPanel.add(new JLabel("Xác nhận mật khẩu:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        registerConfirmPasswordField = new JPasswordField(30);
        registerConfirmPasswordField.setPreferredSize(new Dimension(300, 25));
        registerPanel.add(registerConfirmPasswordField, gbc);

        // Full Name
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        registerPanel.add(new JLabel("Họ tên:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        registerFullNameField = new JTextField(30);
        registerFullNameField.setPreferredSize(new Dimension(300, 25));
        registerPanel.add(registerFullNameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0;
        registerPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        registerEmailField = new JTextField(30);
        registerEmailField.setPreferredSize(new Dimension(300, 25));
        registerPanel.add(registerEmailField, gbc);

        // Phone
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0;
        registerPanel.add(new JLabel("Số điện thoại:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 1.0;
        registerPhoneField = new JTextField(30);
        registerPhoneField.setPreferredSize(new Dimension(300, 25));
        registerPanel.add(registerPhoneField, gbc);

        // Nút "Đăng ký"
        gbc.insets = new Insets(10, 10, 5, 10); // Sử dụng insets giống với các trường nhập liệu
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        registerButton = new JButton("Đăng Ký");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerPanel.add(registerButton, gbc);

        // Dòng "Đã có tài khoản? Đăng nhập"
        gbc.insets = new Insets(5, 10, 10, 10);
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        
        JPanel linkPanel = new JPanel();
        linkPanel.setLayout(new BoxLayout(linkPanel, BoxLayout.X_AXIS));
        linkPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        linkPanel.setBackground(registerPanel.getBackground());
        
        JLabel prefixLabel = new JLabel("Đã có tài khoản?");
        JLabel loginLinkLabel = new JLabel("<html><u>Đăng nhập</u></html>");
        loginLinkLabel.setForeground(Color.BLUE);
        loginLinkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLinkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardPanel, "login");
            }
        });

        linkPanel.add(prefixLabel);
        linkPanel.add(Box.createRigidArea(new Dimension(5, 0))); // Khoảng cách 5px
        linkPanel.add(loginLinkLabel);
        
        registerPanel.add(linkPanel, gbc);

        registerButton.addActionListener(e -> handleRegister());

        cardPanel.add(registerPanel, "register");
    }

    private void handleLogin() {
        String username = loginUsernameField.getText().trim();
        String password = new String(loginPasswordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        UserDTO loginDto = new UserDTO(username, password);
        UserEntity userEntity = authController.login(loginDto);
        
        if (userEntity != null) {
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công! Xin chào " + userEntity.getFullName());
            if (authListener != null) {
                authListener.onAuthenticationSuccess(userEntity);
            }
            loginUsernameField.setText("");
            loginPasswordField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegister() {
        String username = registerUsernameField.getText().trim();
        String password = new String(registerPasswordField.getPassword());
        String confirmPassword = new String(registerConfirmPasswordField.getPassword());
        String fullName = registerFullNameField.getText().trim();
        String email = registerEmailField.getText().trim();
        String phone = registerPhoneField.getText().trim();
        
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin bắt buộc", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (authController.usernameExists(username)) {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập đã tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        UserDTO registerDto = new UserDTO(username, password, fullName, email, phone);
        
        if (authController.register(registerDto)) {
            JOptionPane.showMessageDialog(this, "Đăng ký thành công! Bạn có thể đăng nhập.");
            cardLayout.show(cardPanel, "login");
            registerUsernameField.setText("");
            registerPasswordField.setText("");
            registerConfirmPasswordField.setText("");
            registerFullNameField.setText("");
            registerEmailField.setText("");
            registerPhoneField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Đăng ký thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
} 