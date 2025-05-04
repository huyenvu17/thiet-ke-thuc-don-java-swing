package doanthietkethucdon;

import dao.DatabaseConnection;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.ImageIcon;
import ui.QuanLyPanel;
import ui.ThietKeThucDonPanel;
import ui.DanhSachThucDonPanel;
import ui.HoSoPanel;

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
        // Set up the JFrame
        setTitle("Ứng Dụng Thiết Kế Thực Đơn");
        setSize(new Dimension(1000, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create content panel with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        
        // Create panel instances
        QuanLyPanel quanLyPanel = new QuanLyPanel();
        ThietKeThucDonPanel thietKeThucDonPanel = new ThietKeThucDonPanel();
        DanhSachThucDonPanel danhSachThucDonPanel = new DanhSachThucDonPanel();
        HoSoPanel hoSoPanel = new HoSoPanel();
        
        // Add panels to content panel with card names
        contentPanel.add(quanLyPanel, QUAN_LY_PANEL);
        contentPanel.add(thietKeThucDonPanel, THIET_KE_THUC_DON_PANEL);
        contentPanel.add(danhSachThucDonPanel, DANH_SACH_THUC_DON_PANEL);
        contentPanel.add(hoSoPanel, HO_SO_PANEL);
        
        // Add content panel to frame
        add(contentPanel, BorderLayout.CENTER);
        
        // Show the initial panel
        cardLayout.show(contentPanel, QUAN_LY_PANEL);
        
        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        
        // Create menus
        JMenu quanlyMenu = new JMenu("Quản lý");
        JMenu thucdonMenu = new JMenu("Thực đơn");
        JMenu taikhoanMenu = new JMenu("Tài khoản");
        
        // Create menu items
        JMenuItem thietkethucdonMenuItem = new JMenuItem("Thiết kế thực đơn");
        JMenuItem dsthucdonMenuItem = new JMenuItem("Danh sách thực đơn");
        JMenuItem hosoMenuItem = new JMenuItem("Hồ sơ");
        JMenuItem exitMenuItem = new JMenuItem("Thoát");
        
        // Set icons using absolute path to resources
        thietkethucdonMenuItem.setIcon(new ImageIcon("src/main/resources/icons/menu-design.png"));
        dsthucdonMenuItem.setIcon(new ImageIcon("src/main/resources/icons/menu-list.png"));
        hosoMenuItem.setIcon(new ImageIcon("src/main/resources/icons/profile.png"));
        
        // Add MouseListener to quanlyMenu to show panel when clicked
        quanlyMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(contentPanel, QUAN_LY_PANEL);
            }
        });
        
        // Add action listeners to menu items to switch panels
        thietkethucdonMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, THIET_KE_THUC_DON_PANEL);
            }
        });
        
        dsthucdonMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, DANH_SACH_THUC_DON_PANEL);
            }
        });
        
        hosoMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, HO_SO_PANEL);
            }
        });
        
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Will handle logout later
                JOptionPane.showMessageDialog(MainApp.this, "Chức năng đăng xuất sẽ được xử lý sau");
            }
        });
        
        // Add menu items to menus
        thucdonMenu.add(thietkethucdonMenuItem);
        thucdonMenu.add(dsthucdonMenuItem);
        taikhoanMenu.add(hosoMenuItem);
        taikhoanMenu.add(exitMenuItem);
        
        // Add menus to menu bar
        menuBar.add(quanlyMenu);
        menuBar.add(thucdonMenu);
        menuBar.add(taikhoanMenu);
        
        // Set menuBar to the JFrame
        setJMenuBar(menuBar);
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