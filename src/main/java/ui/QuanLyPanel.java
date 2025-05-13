package ui;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;

/**
 * Panel for management functions with three tabs:
 * 1. Nguyên Liệu
 * 2. Công Thức Món Ăn
 * 3. Món Ăn
 */
public class QuanLyPanel extends JPanel {
    
    private JTabbedPane tabbedPane;
    
    public QuanLyPanel() {
        initComponents();
    }
    
    private void initComponents() {
        // Set layout to BorderLayout
        setLayout(new BorderLayout());
        
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Create panels for each tab
        JPanel nguyenLieuPanel = new QuanLyNguyenLieuPanel();
        JPanel congThucMonAnPanel = new CongThucMonAnPanel();
        JPanel monAnPanel = new MonAnPanel();
        JPanel nhomThucPhamPanel = new NhomThucPhamPanel();
        
        // Add tabs to the tabbed pane
        tabbedPane.addTab("Nguyên Liệu", nguyenLieuPanel);
        tabbedPane.addTab("Nhóm thực phẩm", nhomThucPhamPanel);
        tabbedPane.addTab("Công Thức Món Ăn", congThucMonAnPanel);
        tabbedPane.addTab("Món Ăn", monAnPanel);
        
        // Add tabbed pane to panel
        add(tabbedPane, BorderLayout.CENTER);
    }
} 