package ui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Font;

/**
 * Panel for displaying the list of menus
 */
public class DanhSachThucDonPanel extends JPanel {
    
    public DanhSachThucDonPanel() {
        initComponents();
    }
    
    private void initComponents() {
        // Set layout to BorderLayout
        setLayout(new BorderLayout());
        
        // Create a label for now (placeholder)
        JLabel titleLabel = new JLabel("Danh Sách Thực Đơn", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        
        // Add components to panel
        add(titleLabel, BorderLayout.NORTH);
        
        // Create a placeholder table
        String[] columnNames = {"ID", "Tên Thực Đơn", "Ngày Tạo", "Số Món", "Tổng Chi Phí"};
        Object[][] data = {
            {"TD001", "Thực đơn tiệc cưới", "01/01/2024", "10", "5,000,000 VNĐ"},
            {"TD002", "Thực đơn sinh nhật", "15/02/2024", "5", "2,000,000 VNĐ"},
            {"TD003", "Thực đơn họp mặt", "10/03/2024", "8", "3,500,000 VNĐ"}
        };
        
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        
        add(scrollPane, BorderLayout.CENTER);
    }
} 