package doanthietkethucdon;

import dao.DatabaseConnection;
import dao.UserDao;
import entity.User;
import service.AuthService;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import ui.QuanLyPanel;
import ui.ThietKeThucDonPanel;
import ui.DanhSachThucDonPanel;
import ui.HoSoPanel;
import ui.LoginRegisterPanel;
import jiconfont.swing.IconFontSwing;
import jiconfont.icons.font_awesome.FontAwesome;

/**
 * Lớp ứng dụng chính
 */
public class MainApp extends JFrame implements LoginRegisterPanel.AuthenticationListener {
    
    // Panel chính với CardLayout
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    // Tên các panel trong CardLayout
    private static final String LOGIN_PANEL = "loginPanel";
    private static final String MAIN_CONTENT = "mainContent";
    
    // Các thành phần của ứng dụng chính
    private JPanel mainContentPanel;
    private CardLayout mainCardLayout;
    private HoSoPanel hoSoPanel;
    
    // Tên các panel chức năng
    private static final String QUAN_LY_PANEL = "quanLyPanel";
    private static final String THIET_KE_THUC_DON_PANEL = "thietKeThucDonPanel";
    private static final String DANH_SACH_THUC_DON_PANEL = "danhSachThucDonPanel";
    private static final String HO_SO_PANEL = "hoSoPanel";
    
    // Cờ báo ứng dụng đã được khởi tạo
    private boolean mainAppInitialized = false;
    
    public MainApp() {
        initUI();
    }

    private void initUI() {
        // Biểu tượng ứng dụng
        try {
            ImageIcon appIcon = new ImageIcon(getClass().getResource("/logo.png"));
            setIconImage(appIcon.getImage());
        } catch (Exception e) {
            System.err.println("Không thể tải biểu tượng ứng dụng: " + e.getMessage());
        }
        
        // Khởi tạo giao diện
        IconFontSwing.register(FontAwesome.getIconFont());
        setTitle("Ứng Dụng Thiết Kế Thực Đơn");
        setSize(new Dimension(1000, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Khởi tạo panel chính với CardLayout để chuyển đổi giữa đăng nhập và nội dung chính
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        
        // Thêm LoginRegisterPanel vào panel chính
        LoginRegisterPanel loginPanel = new LoginRegisterPanel(this);
        contentPanel.add(loginPanel, LOGIN_PANEL);
        
        // Thêm placeholder cho giao diện chính (chưa khởi tạo)
        JPanel placeholderPanel = new JPanel();
        contentPanel.add(placeholderPanel, MAIN_CONTENT);
        
        // Hiển thị panel đăng nhập ban đầu
        cardLayout.show(contentPanel, LOGIN_PANEL);
        
        // Thêm panel chính vào frame
        add(contentPanel, BorderLayout.CENTER);
    }
    
    /**
     * Khởi tạo giao diện ứng dụng chính sau khi đăng nhập thành công
     */
    private void initMainContent(User user) {
        // Tạo panel chính của ứng dụng
        JPanel mainAppPanel = new JPanel(new BorderLayout());
        
        // Tạo panel nội dung với CardLayout để chuyển đổi giữa các chức năng
        mainCardLayout = new CardLayout();
        mainContentPanel = new JPanel(mainCardLayout);
        
        // Khởi tạo các panel chức năng
        QuanLyPanel quanLyPanel = new QuanLyPanel();
        ThietKeThucDonPanel thietKeThucDonPanel = new ThietKeThucDonPanel();
        DanhSachThucDonPanel danhSachThucDonPanel = new DanhSachThucDonPanel();
        
        // Khởi tạo HoSoPanel với thông tin người dùng đã đăng nhập
        hoSoPanel = new HoSoPanel(user);
        
        // Thêm các panel chức năng vào panel nội dung
        mainContentPanel.add(quanLyPanel, QUAN_LY_PANEL);
        mainContentPanel.add(thietKeThucDonPanel, THIET_KE_THUC_DON_PANEL);
        mainContentPanel.add(danhSachThucDonPanel, DANH_SACH_THUC_DON_PANEL);
        mainContentPanel.add(hoSoPanel, HO_SO_PANEL);
        
        // Tạo thanh công cụ
        JToolBar toolBar = createToolbar();
        
        // Thêm các thành phần vào panel chính
        mainAppPanel.add(toolBar, BorderLayout.NORTH);
        mainAppPanel.add(mainContentPanel, BorderLayout.CENTER);
        
        // Thay thế placeholder bằng panel ứng dụng chính
        contentPanel.remove(contentPanel.getComponent(1));
        contentPanel.add(mainAppPanel, MAIN_CONTENT);
        
        // Đánh dấu ứng dụng đã được khởi tạo
        mainAppInitialized = true;
    }
    
    private JToolBar createToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        toolBar.setBorderPainted(false);
        toolBar.setBackground(new Color(240, 240, 240));
        
        // Tạo các biểu tượng
        Icon manageIcon = IconFontSwing.buildIcon(FontAwesome.COG, 20, new Color(50, 50, 50));
        Icon menuDesignIcon = IconFontSwing.buildIcon(FontAwesome.CALENDAR, 20, new Color(50, 50, 50));
        Icon menuListIcon = IconFontSwing.buildIcon(FontAwesome.LIST, 20, new Color(50, 50, 50));
        Icon profileIcon = IconFontSwing.buildIcon(FontAwesome.USER, 20, new Color(50, 50, 50));
        Icon exitIcon = IconFontSwing.buildIcon(FontAwesome.SIGN_OUT, 20, new Color(50, 50, 50));
        
        // Tạo các nút trên thanh công cụ
        JButton btnQuanLy = createToolbarButton("Quản lý", manageIcon);
        JButton btnThietKeThucDon = createToolbarButton("Thiết kế thực đơn", menuDesignIcon);
        JButton btnDanhSachThucDon = createToolbarButton("Danh sách thực đơn", menuListIcon);
        JButton btnHoSo = createToolbarButton("Hồ sơ", profileIcon);
        JButton btnThoat = createToolbarButton("Đăng xuất", exitIcon);
        
        // Thêm hành động cho các nút
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
        
        // Định dạng các nút
        styleButton(btnQuanLy);
        styleButton(btnThietKeThucDon);
        styleButton(btnDanhSachThucDon);
        styleButton(btnHoSo);
        styleButton(btnThoat);
        
        // Thêm các nút vào thanh công cụ
        toolBar.add(btnQuanLy);
        toolBar.addSeparator(new Dimension(10, 0));
        toolBar.add(btnThietKeThucDon);
        toolBar.addSeparator(new Dimension(10, 0));
        toolBar.add(btnDanhSachThucDon);
        toolBar.addSeparator(new Dimension(10, 0));
        toolBar.add(btnHoSo);
        
        // Thêm khoảng trống và nút đăng xuất ở bên phải
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(btnThoat);
        
        return toolBar;
    }
    
    /**
     * Đăng xuất khỏi hệ thống
     */
    private void logout() {
        // Xóa thông tin người dùng hiện tại
        AuthService.logout();
        // Quay lại màn hình đăng nhập
        cardLayout.show(contentPanel, LOGIN_PANEL);
    }
    
    /**
     * Phương thức tạo nút cho thanh công cụ
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

    /**
     * Phương thức định dạng nút
     */
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
    
    /**
     * Xử lý sau khi xác thực thành công
     */
    @Override
    public void onAuthenticationSuccess(User user) {
        // Khởi tạo giao diện chính nếu chưa khởi tạo
        if (!mainAppInitialized) {
            initMainContent(user);
        } else if (hoSoPanel != null) {
            // Cập nhật HoSoPanel với thông tin người dùng đã đăng nhập
            hoSoPanel.updateUser(user);
        }
        
        // Chuyển đến giao diện chính
        cardLayout.show(contentPanel, MAIN_CONTENT);
        
        // Hiển thị panel Quản lý ban đầu
        mainCardLayout.show(mainContentPanel, QUAN_LY_PANEL);
    }
    
    /**
     * Điểm vào của ứng dụng
     */
    public static void main(String[] args) {
        try {
            // Đăng ký driver MySQL JDBC
            DatabaseConnection.dangKyDriver();
            
            // Sử dụng giao diện hệ thống
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Khởi động ứng dụng
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