package ui;

import entity.UserEntity;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;

/**
 * Panel Quản Lý
 * 1. Nguyên Liệu
 * 2. Nhóm Thực Phẩm
 * 3. Công Thức Món Ăn
 * 4. Món Ăn
 */
public class QuanLyPanel extends JPanel {
    
    private JTabbedPane tabbedPane;
    private UserEntity currentUser;
    
    public QuanLyPanel(UserEntity userEntity) {
        this.currentUser = userEntity;
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        tabbedPane = new JTabbedPane();

        JPanel nguyenLieuPanel = new QuanLyNguyenLieuPanel(currentUser);
        JPanel congThucMonAnPanel = new CongThucMonAnPanel(currentUser);
        JPanel monAnPanel = new MonAnPanel(currentUser);
        JPanel nhomThucPhamPanel = new NhomThucPhamPanel(currentUser);
        
        tabbedPane.addTab("Nguyên Liệu", nguyenLieuPanel);
        tabbedPane.addTab("Nhóm thực phẩm", nhomThucPhamPanel);
        tabbedPane.addTab("Công Thức Món Ăn", congThucMonAnPanel);
        tabbedPane.addTab("Món Ăn", monAnPanel);
        add(tabbedPane, BorderLayout.CENTER);
    }
} 