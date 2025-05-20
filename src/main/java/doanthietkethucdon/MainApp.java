package doanthietkethucdon;

import dao.DatabaseConnection;
import entity.UserEntity;
import service.AuthService;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import ui.QuanLyPanel;
import ui.ThietKeThucDonPanel;
import ui.DanhSachThucDonPanel;
import ui.HoSoPanel;
import ui.DangNhapDangKyPanel;
import jiconfont.swing.IconFontSwing;
import jiconfont.icons.font_awesome.FontAwesome;

/**
 * MainApp
 */
public class MainApp extends JFrame implements DangNhapDangKyPanel.AuthenticationListener {
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private static final String LOGIN_PANEL = "loginPanel";
    private static final String MAIN_CONTENT = "mainContent";
    private JPanel mainContentPanel;
    private CardLayout mainCardLayout;
    private HoSoPanel hoSoPanel;
    private static final String QUAN_LY_PANEL = "quanLyPanel";
    private static final String THIET_KE_THUC_DON_PANEL = "thietKeThucDonPanel";
    private static final String DANH_SACH_THUC_DON_PANEL = "danhSachThucDonPanel";
    private static final String HO_SO_PANEL = "hoSoPanel";
    private boolean mainAppInitialized = false;

    public MainApp() {
        initUI();
    }

    private void initUI() {
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
        DangNhapDangKyPanel loginPanel = new DangNhapDangKyPanel(this);
        contentPanel.add(loginPanel, LOGIN_PANEL);
        JPanel placeholderPanel = new JPanel();
        contentPanel.add(placeholderPanel, MAIN_CONTENT);
        cardLayout.show(contentPanel, LOGIN_PANEL);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void initMainContent(UserEntity userEntity) {
        JPanel mainAppPanel = new JPanel(new BorderLayout());
        mainCardLayout = new CardLayout();
        mainContentPanel = new JPanel(mainCardLayout);
        
        QuanLyPanel quanLyPanel = new QuanLyPanel(userEntity);
        ThietKeThucDonPanel thietKeThucDonPanel = new ThietKeThucDonPanel(userEntity);
        DanhSachThucDonPanel danhSachThucDonPanel = new DanhSachThucDonPanel(userEntity);
        
        hoSoPanel = new HoSoPanel(userEntity);
        
        mainContentPanel.add(quanLyPanel, QUAN_LY_PANEL);
        mainContentPanel.add(thietKeThucDonPanel, THIET_KE_THUC_DON_PANEL);
        mainContentPanel.add(danhSachThucDonPanel, DANH_SACH_THUC_DON_PANEL);
        mainContentPanel.add(hoSoPanel, HO_SO_PANEL);
        
        JToolBar toolBar = createToolbar();
        mainAppPanel.add(toolBar, BorderLayout.NORTH);
        mainAppPanel.add(mainContentPanel, BorderLayout.CENTER);
        contentPanel.remove(contentPanel.getComponent(1));
        contentPanel.add(mainAppPanel, MAIN_CONTENT);
        mainAppInitialized = true;
    }
    
    private JToolBar createToolbar() {
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
        JButton btnThoat = createToolbarButton("Đăng xuất", exitIcon);
        
        btnQuanLy.addActionListener(e -> mainCardLayout.show(mainContentPanel, QUAN_LY_PANEL));
        btnThietKeThucDon.addActionListener(e -> mainCardLayout.show(mainContentPanel, THIET_KE_THUC_DON_PANEL));
        btnDanhSachThucDon.addActionListener(e -> mainCardLayout.show(mainContentPanel, DANH_SACH_THUC_DON_PANEL));
        btnHoSo.addActionListener(e -> mainCardLayout.show(mainContentPanel, HO_SO_PANEL));
        btnThoat.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(MainApp.this, 
                    "Bạn có chắc chắn muốn đăng xuất?", 
                    "Xác nhận đăng xuất", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                logout();
            }
        });
        
        styleButton(btnQuanLy);
        styleButton(btnThietKeThucDon);
        styleButton(btnDanhSachThucDon);
        styleButton(btnHoSo);
        styleButton(btnThoat);
        toolBar.add(btnQuanLy);
        toolBar.addSeparator(new Dimension(10, 0));
        
        // Chỉ hiển thị nút Thiết kế thực đơn nếu người dùng có quyền admin
        UserEntity currentUser = AuthService.getCurrentUser();
        if (currentUser != null && "admin".equals(currentUser.getRole())) {
            toolBar.add(btnThietKeThucDon);
            toolBar.addSeparator(new Dimension(10, 0));
        }
        
        toolBar.add(btnDanhSachThucDon);
        toolBar.addSeparator(new Dimension(10, 0));
        toolBar.add(btnHoSo);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(btnThoat);
        
        return toolBar;
    }

    private void logout() {
        AuthService.logout();
        cardLayout.show(contentPanel, LOGIN_PANEL);
    }

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
        
        // Thêm hiệu ứng hover cho nút
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

    @Override
    public void onAuthenticationSuccess(UserEntity userEntity) {
        if (!mainAppInitialized) {
            initMainContent(userEntity);
        } else if (hoSoPanel != null) {
            hoSoPanel.updateUser(userEntity);
        }
        
        cardLayout.show(contentPanel, MAIN_CONTENT);
        mainCardLayout.show(mainContentPanel, QUAN_LY_PANEL);
    }

    public static void main(String[] args) {
        try {
            DatabaseConnection.dangKyDriver();
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
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