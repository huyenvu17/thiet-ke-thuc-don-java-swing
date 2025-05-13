package ui;

import entity.MonAnEntity;
import service.ThucDonService;
import dao.MonAnDao;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Panel for menu design functionality
 */
public class ThietKeThucDonPanel extends JPanel {
    
    private final ThucDonService thucDonService;
    private final MonAnDao monAnDao;
    
    private JTextField tenThucDonField;
    private JSpinner soNgaySpinner;
    private JTextField budgetSangField;
    private JTextField budgetTruaField;
    private JTextField budgetXeField;
    private JTextArea resultArea;
    
    public ThietKeThucDonPanel() {
        thucDonService = new ThucDonService();
        monAnDao = MonAnDao.getInstance();
        initComponents();
    }
    
    private void initComponents() {
        // Set layout to BorderLayout
        setLayout(new BorderLayout());
        
        // Create a label for the title
        JLabel titleLabel = new JLabel("Thiết Kế Thực Đơn", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);
        
        // Create the input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Thuc don name input
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Tên thực đơn:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        tenThucDonField = new JTextField(20);
        inputPanel.add(tenThucDonField, gbc);
        
        // Number of days input
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        inputPanel.add(new JLabel("Số ngày:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(7, 1, 30, 1);
        soNgaySpinner = new JSpinner(spinnerModel);
        inputPanel.add(soNgaySpinner, gbc);
        
        // Budget inputs
        JPanel budgetPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        budgetPanel.setBorder(BorderFactory.createTitledBorder("Ngân sách tối đa cho mỗi bữa ăn"));
        
        budgetPanel.add(new JLabel("Bữa sáng:"));
        budgetSangField = new JTextField("50000");
        budgetPanel.add(budgetSangField);
        
        budgetPanel.add(new JLabel("Bữa trưa:"));
        budgetTruaField = new JTextField("100000");
        budgetPanel.add(budgetTruaField);
        
        budgetPanel.add(new JLabel("Bữa tối:"));
        budgetXeField = new JTextField("80000");
        budgetPanel.add(budgetXeField);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        inputPanel.add(budgetPanel, gbc);
        
        // Generate button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        JButton generateButton = new JButton("Tạo Thực Đơn");
        generateButton.addActionListener(e -> generateThucDon());
        inputPanel.add(generateButton, gbc);
        
        // Check if we have the required number of dishes
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        
        List<MonAnEntity> monAnList = monAnDao.getAllMonAn();
        boolean hasSang = monAnList.stream().anyMatch(m -> "sang".equals(m.loaiMon()));
        boolean hasTrua = monAnList.stream().anyMatch(m -> "trua".equals(m.loaiMon()));
        boolean hasXe = monAnList.stream().anyMatch(m -> "xe".equals(m.loaiMon()));
        
        if (!hasSang || !hasTrua || !hasXe) {
            JLabel warningLabel = new JLabel("<html><font color='red'>Cảnh báo: Thiếu món ăn cho một số bữa. Vui lòng thêm món ăn trong phần quản lý.</font></html>");
            inputPanel.add(warningLabel, gbc);
        }
        
        // Result area
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        inputPanel.add(scrollPane, gbc);
        
        add(inputPanel, BorderLayout.CENTER);
    }
    
    private void generateThucDon() {
        // Validate inputs
        String tenThucDon = tenThucDonField.getText().trim();
        if (tenThucDon.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên thực đơn", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int soNgay = (int) soNgaySpinner.getValue();
        
        // Validate and parse budget values
        Map<String, Double> budgetMap = new HashMap<>();
        try {
            double budgetSang = Double.parseDouble(budgetSangField.getText().trim());
            double budgetTrua = Double.parseDouble(budgetTruaField.getText().trim());
            double budgetXe = Double.parseDouble(budgetXeField.getText().trim());
            
            budgetMap.put("sang", budgetSang);
            budgetMap.put("trua", budgetTrua);
            budgetMap.put("xe", budgetXe);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ngân sách không hợp lệ. Vui lòng nhập số", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Generate menu
        int thucDonId = thucDonService.generateThucDon(tenThucDon, soNgay, budgetMap);
        
        if (thucDonId <= 0) {
            JOptionPane.showMessageDialog(this, "Không thể tạo thực đơn. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Show success message
        JOptionPane.showMessageDialog(this, "Đã tạo thực đơn thành công! Bạn có thể xem chi tiết trong phần 'Danh Sách Thực Đơn'");
        
        // Clear form
        tenThucDonField.setText("");
        soNgaySpinner.setValue(7);
    }
} 