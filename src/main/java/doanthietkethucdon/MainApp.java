package doanthietkethucdon;

import dao.DatabaseConnection;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import ui.QuanLyPanel;
import ui.ThietKeThucDonPanel;
import ui.DanhSachThucDonPanel;
import ui.HoSoPanel;
import jiconfont.swing.IconFontSwing;
import jiconfont.icons.font_awesome.FontAwesome;

/**
 * Main application class
 */
public class MainApp extends JFrame {
    
    // Main content panel with CardLayout
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    // Panel names for CardLayout
    private static final String QUAN_LY_PANEL = "quanLyPanel";
    private static final String THIET_KE_THUC_DON_PANEL = "thietKeThucDonPanel";
    private static final String DANH_SACH_THUC_DON_PANEL = "danhSachThucDonPanel";
    private static final String HO_SO_PANEL = "hoSoPanel";
    
    public MainApp() {
        initUI();
    }

    private void initUI() {
        // App icon
        try {
            ImageIcon appIcon = new ImageIcon(getClass().getResource("/logo.png"));
            setIconImage(appIcon.getImage());
        } catch (Exception e) {
            System.err.println("Không thể tải biểu tượng ứng dụng: " + e.getMessage());
        }
        
        IconFontSwing.register(FontAwesome.getIconFont());
        setTitle("Ứng Dụng Thiết Kế Thực Đơn");
        setSize(new Dimension(1000, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        
        // Danh sánh panels
        QuanLyPanel quanLyPanel = new QuanLyPanel();
        ThietKeThucDonPanel thietKeThucDonPanel = new ThietKeThucDonPanel();
        DanhSachThucDonPanel danhSachThucDonPanel = new DanhSachThucDonPanel();
        HoSoPanel hoSoPanel = new HoSoPanel();
        
        contentPanel.add(quanLyPanel, QUAN_LY_PANEL);
        contentPanel.add(thietKeThucDonPanel, THIET_KE_THUC_DON_PANEL);
        contentPanel.add(danhSachThucDonPanel, DANH_SACH_THUC_DON_PANEL);
        contentPanel.add(hoSoPanel, HO_SO_PANEL);
        
        // Toolbar menu
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        toolBar.setBorderPainted(false);
        toolBar.setBackground(new Color(240, 240, 240));
        
        Icon manageIcon = IconFontSwing.buildIcon(FontAwesome.COG, 20, new Color(50, 50, 50));
        Icon menuDesignIcon = IconFontSwing.buildIcon(FontAwesome.CALENDAR, 20, new Color(50, 50, 50));
        Icon menuListIcon = IconFontSwing.buildIcon(FontAwesome.LIST, 20, new Color(50, 50, 50));
        Icon profileIcon = IconFontSwing.buildIcon(FontAwesome.USER, 20, new Color(50, 50, 50));
        Icon exitIcon = IconFontSwing.buildIcon(FontAwesome.SIGN_OUT, 20, new Color(50, 50, 50));
        
        JButton btnQuanLy = createToolbarButton("Quản lý", manageIcon);
        JButton btnThietKeThucDon = createToolbarButton("Thiết kế thực đơn", menuDesignIcon);
        JButton btnDanhSachThucDon = createToolbarButton("Danh sách thực đơn", menuListIcon);
        JButton btnHoSo = createToolbarButton("Hồ sơ", profileIcon);
        JButton btnThoat = createToolbarButton("Thoát", exitIcon);
        
        btnQuanLy.addActionListener(e -> cardLayout.show(contentPanel, QUAN_LY_PANEL));
        btnThietKeThucDon.addActionListener(e -> cardLayout.show(contentPanel, THIET_KE_THUC_DON_PANEL));
        btnDanhSachThucDon.addActionListener(e -> cardLayout.show(contentPanel, DANH_SACH_THUC_DON_PANEL));
        btnHoSo.addActionListener(e -> cardLayout.show(contentPanel, HO_SO_PANEL));
        btnThoat.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(MainApp.this, 
                    "Bạn có chắc chắn muốn thoát ứng dụng?", 
                    "Xác nhận thoát", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                dispose();
                System.exit(0);
            }
        });
        
        styleButton(btnQuanLy);
        styleButton(btnThietKeThucDon);
        styleButton(btnDanhSachThucDon);
        styleButton(btnHoSo);
        styleButton(btnThoat);
        
        // Add buttons to toolbar
        toolBar.add(btnQuanLy);
        toolBar.addSeparator(new Dimension(10, 0));
        toolBar.add(btnThietKeThucDon);
        toolBar.addSeparator(new Dimension(10, 0));
        toolBar.add(btnDanhSachThucDon);
        toolBar.addSeparator(new Dimension(10, 0));
        toolBar.add(btnHoSo);
        
        // Add spacer and exit button at the right side
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(btnThoat);
        
        // Add content panel to frame
        add(contentPanel, BorderLayout.CENTER);
        add(toolBar, BorderLayout.NORTH);
        
        // Show the initial panel
        cardLayout.show(contentPanel, QUAN_LY_PANEL);
    }
    
    /**
     * Helper method to create a standardized toolbar button
     */
    private JButton createToolbarButton(String text, Icon icon) {
        JButton button = new JButton(text);
        if (icon != null) {
            button.setIcon(icon);
        }
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setMargin(new Insets(8, 15, 8, 15));
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        return button;
    }

    private void styleButton(JButton button) {
        button.setFont(new Font(button.getFont().getName(), Font.PLAIN, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBackground(new Color(240, 240, 240));
        
        // Add hover effect with listeners
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(220, 220, 220));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(240, 240, 240));
            }
        });
    }
    
    /**
     * Main method - entry point of the application
     */
    public static void main(String[] args) {
        try {
            // Register MySQL JDBC driver
            DatabaseConnection.dangKyDriver();
            
            // Set native look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Start application
            SwingUtilities.invokeLater(() -> {
                MainApp app = new MainApp();
                app.setVisible(true);
            });
            
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, 
                    "Không thể tìm thấy driver JDBC: " + ex.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, 
                    "Lỗi khởi động ứng dụng: " + ex.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
} 