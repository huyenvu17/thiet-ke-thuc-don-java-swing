package ui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Font;

/**
 * Panel for menu design functionality
 */
public class ThietKeThucDonPanel extends JPanel {
    
    public ThietKeThucDonPanel() {
        initComponents();
    }
    
    private void initComponents() {
        // Set layout to BorderLayout
        setLayout(new BorderLayout());
        
        // Create a label for now (placeholder)
        JLabel titleLabel = new JLabel("Thiết Kế Thực Đơn", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        
        // Add components to panel
        add(titleLabel, BorderLayout.NORTH);
        
        // Add placeholder content panel
        JPanel contentPanel = new JPanel();
        contentPanel.add(new JLabel("Chức năng thiết kế thực đơn đang được phát triển..."));
        add(contentPanel, BorderLayout.CENTER);
    }
} 