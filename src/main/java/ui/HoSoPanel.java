package ui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.Dimension;

/**
 * Panel for user profile information
 */
public class HoSoPanel extends JPanel {
    
    private JTextField tfHoTen;
    private JTextField tfEmail;
    private JTextField tfSoDienThoai;
    private JTextField tfDiaChi;
    
    public HoSoPanel() {
        initComponents();
    }
    
    private void initComponents() {
        // Set layout to BorderLayout
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(5, 5, 5, 5));
        
        // Create title
        JLabel titleLabel = new JLabel("THÔNG TIN HỒ SƠ", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        
        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Add form fields
        JLabel lblHoTen = new JLabel("Họ và tên:");
        tfHoTen = new JTextField("Nguyễn Văn A"); // Sample data
        
        JLabel lblEmail = new JLabel("Email:");
        tfEmail = new JTextField("nguyenvana@example.com"); // Sample data
        
        JLabel lblSoDienThoai = new JLabel("Số điện thoại:");
        tfSoDienThoai = new JTextField("0912345678"); // Sample data
        
        JLabel lblDiaChi = new JLabel("Địa chỉ:");
        tfDiaChi = new JTextField("123 Đường ABC, Quận XYZ, TP. HCM"); // Sample data
        
        // Add components to form panel
        formPanel.add(lblHoTen);
        formPanel.add(tfHoTen);
        formPanel.add(lblEmail);
        formPanel.add(tfEmail);
        formPanel.add(lblSoDienThoai);
        formPanel.add(tfSoDienThoai);
        formPanel.add(lblDiaChi);
        formPanel.add(tfDiaChi);
        
        // Create buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JButton btnCapNhat = new JButton("Cập nhật thông tin");
        JButton btnDoiMatKhau = new JButton("Đổi mật khẩu");
        
        buttonPanel.add(btnCapNhat);
        buttonPanel.add(btnDoiMatKhau);
        
        // Create a wrapper panel for the form with some spacing
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(buttonPanel, BorderLayout.CENTER);
        
        // Add components to main panel
        add(titleLabel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
} 