package doanthietkethucdon;

import dao.DatabaseConnection;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.ImageIcon;
import ui.QuanLyNguyenLieuPanel;

/**
 * Main application class
 */
public class MainApp extends JFrame {
    
    // Standard size for menu icons (16x16 pixels is the standard size for menu icons)
    private static final int ICON_SIZE = 16;
    
    public MainApp() {
        initUI();
    }
    
    private void initUI() {
        // Set up the JFrame
        setTitle("Ứng Dụng Thiết Kế Thực Đơn");
        setSize(new Dimension(1000, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // create menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu quanlyMenu = new JMenu("Quản lý");
        JMenu thucdonMenu = new JMenu("Thực đơn");
        JMenu taikhoanMenu = new JMenu("Tài khoản");
        
        // Set icons for main menus
        quanlyMenu.setIcon(loadIcon("/icons/manage.png"));
        thucdonMenu.setIcon(loadIcon("/icons/menu.png"));
        taikhoanMenu.setIcon(loadIcon("/icons/account.png"));
        
        // Create menu items with icons
        JMenuItem nguyenlieuMenuItem = new JMenuItem("Nguyên liệu", loadIcon("/icons/ingredient.png"));
        JMenuItem congthucmonanMenuItem = new JMenuItem("Công thức món ăn", loadIcon("/icons/recipe.png"));
        JMenuItem monanMenuItem = new JMenuItem("Món ăn", loadIcon("/icons/dish.png"));
        JMenuItem thietkethucdonMenuItem = new JMenuItem("Thiết kế thực đơn", loadIcon("/icons/design.png"));
        JMenuItem dsthucdonMenuItem = new JMenuItem("Danh sách thực đơn", loadIcon("/icons/list.png"));
        JMenuItem hosoMenuItem = new JMenuItem("Hồ sơ", loadIcon("/icons/profile.png"));
        JMenuItem exitMenuItem = new JMenuItem("Thoát", loadIcon("/icons/exit.png"));
        
        quanlyMenu.add(nguyenlieuMenuItem);
        quanlyMenu.add(congthucmonanMenuItem);
        quanlyMenu.add(monanMenuItem);
        thucdonMenu.add(thietkethucdonMenuItem);
        thucdonMenu.add(dsthucdonMenuItem);
        taikhoanMenu.add(hosoMenuItem);
        taikhoanMenu.add(exitMenuItem);
        
        menuBar.add(quanlyMenu);
        menuBar.add(thucdonMenu);
        menuBar.add(taikhoanMenu);
        
        // Set menuBar to the JFrame
        setJMenuBar(menuBar);
        
        // Create a tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Add tabs for different functionality
        tabbedPane.addTab("Nguyên liệu", new QuanLyNguyenLieuPanel());
        tabbedPane.addTab("Món ăn", new JPanel()); // Placeholder - will implement later
        tabbedPane.addTab("Thiết kế thực đơn", new JPanel()); // Placeholder - will implement later
        
        add(tabbedPane);
    }
    
    /**
     * Helper method to load icons from resources
     * @param path Path to the icon resource
     * @return ImageIcon or null if resource not found
     */
    private ImageIcon loadIcon(String path) {
        try {
            java.net.URL imgURL = getClass().getResource(path);
            if (imgURL != null) {
                return new ImageIcon(imgURL);
            } else {
                System.err.println("Couldn't find icon: " + path);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error loading icon: " + path + " - " + e.getMessage());
            return null;
        }
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