package ui;

import controller.IMonAnController;
import controller.MonAnController;
import dto.MonAnDTO;
import entity.UserEntity;
import java.awt.*;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ADMIN
 */
public class MonAnPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField tenField;
    private JComboBox<String> loaiMonAnComboBox;
    private JTextArea cachCheBienArea;
    private JPanel buttonsPanel;
    private JButton themButton, editButton, deleteButton, backButton;
    private IMonAnController monAnController;
    private UserEntity currentUserEntity;
    
    public MonAnPanel() {
    }
    
    public MonAnPanel(UserEntity userEntity) {
        try {
            this.monAnController = MonAnController.getInstance();
            this.currentUserEntity = userEntity;
            initComponents();
            loadMonAn();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo: " + ex.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(5, 5, 5, 5));
        JLabel titleLabel = new JLabel("QUẢN LÝ MÓN ĂN", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        // Add components to panel
        add(titleLabel, BorderLayout.NORTH);
        
        // Create table model with column names
        model = new DefaultTableModel(new String[]{"ID", "Tên món ăn", "Loại món ăn", "Cách chế biến"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        table = new JTable(model);
        table.getColumnModel().getColumn(0).setMinWidth(40);
        table.getColumnModel().getColumn(0).setMaxWidth(60);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Create input panel with form fields
        JPanel inputPanel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        
        formPanel.add(new JLabel("Tên món ăn:"));
        tenField = new JTextField();
        formPanel.add(tenField);
        
        formPanel.add(new JLabel("Loại món ăn:"));
        loaiMonAnComboBox = new JComboBox<>(new String[]{"sáng", "trưa", "xế"});
        formPanel.add(loaiMonAnComboBox);
        
        formPanel.add(new JLabel("Cách chế biến:"));
        cachCheBienArea = new JTextArea();
        cachCheBienArea.setRows(3);
        cachCheBienArea.setLineWrap(true);
        cachCheBienArea.setWrapStyleWord(true);
        JScrollPane cachCheBienScrollPane = new JScrollPane(cachCheBienArea);
        formPanel.add(cachCheBienScrollPane);
        
        // Create buttons panel
        buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        themButton = new JButton("Thêm");
        editButton = new JButton("Sửa");
        deleteButton = new JButton("Xóa");
        backButton = new JButton("Quay lại");
        
        // Add all buttons to panel
        buttonsPanel.add(themButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(backButton);
        
        // Set initial visibility
        editButton.setVisible(false);
        deleteButton.setVisible(false);
        backButton.setVisible(false);
        
        // Add panels to input panel
        inputPanel.add(formPanel, BorderLayout.CENTER);
        inputPanel.add(buttonsPanel, BorderLayout.SOUTH);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        add(inputPanel, BorderLayout.SOUTH);
        
        // Kiểm tra quyền người dùng
        if(currentUserEntity != null && !currentUserEntity.getRole().equals("admin")) {
            inputPanel.setVisible(false);
            buttonsPanel.setVisible(false);
        }

        // Add action listeners for buttons
        themButton.addActionListener(e -> {
            addMonAn();
        });
        
        editButton.addActionListener(e -> {
            editMonAn();
        });
        
        deleteButton.addActionListener(e -> {
            deleteMonAn();
        });
        
        backButton.addActionListener(e -> {
            // Cancel editing and go back to default state
            clearFields();
            table.clearSelection();
        });
        
        // Add listener for table selection
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Row selected - load data and show edit/delete buttons
                    tenField.setText(model.getValueAt(selectedRow, 1).toString());
                    loaiMonAnComboBox.setSelectedItem(model.getValueAt(selectedRow, 2).toString());
                    cachCheBienArea.setText(model.getValueAt(selectedRow, 3).toString());
                    
                    // Show edit/delete/back buttons, hide add button
                    themButton.setVisible(false);
                    editButton.setVisible(true);
                    deleteButton.setVisible(true);
                    backButton.setVisible(true);
                } else {
                    // No row selected - show add button, hide edit/delete buttons
                    themButton.setVisible(true);
                    editButton.setVisible(false);
                    deleteButton.setVisible(false);
                    backButton.setVisible(false);
                }
            }
        });
    }
    
    private void loadMonAn() {
        model.setRowCount(0); // Clear existing rows
        
        try {
            // Load from database through controller
            List<MonAnDTO> danhSachMonAn = monAnController.getAllMonAn();
            
            // Add data to table model
            for (MonAnDTO monAn : danhSachMonAn) {
                Object[] row = {
                    monAn.getId(),
                    monAn.getTenMon(),
                    convertLoaiMonToDisplay(monAn.getLoaiMon()),
                    monAn.getCachCheBien()
                };
                model.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                    "Lỗi khi tải danh sách món ăn: " + ex.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String convertLoaiMonToDisplay(String loaiMon) {
        switch (loaiMon) {
            case "sang":
                return "sáng";
            case "trua":
                return "trưa";
            case "xe":
                return "xế";
            default:
                return loaiMon;
        }
    }

    private String convertDisplayToLoaiMon(String displayLoaiMon) {
        switch (displayLoaiMon) {
            case "sáng":
                return "sang";
            case "trưa":
                return "trua";
            case "xế":
                return "xe";
            default:
                return displayLoaiMon;
        }
    }
    
    private void addMonAn() {
        String ten = tenField.getText().trim();
        String loaiMonAn = convertDisplayToLoaiMon(loaiMonAnComboBox.getSelectedItem().toString());
        String cachCheBien = cachCheBienArea.getText().trim();
        
        // Validate input
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên món ăn", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Create a DTO object
            MonAnDTO monAnDto = new MonAnDTO(0, ten, loaiMonAn, cachCheBien);
            
            // Add through controller
            boolean success = monAnController.addMonAn(monAnDto);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Thêm món ăn thành công!");
                // Reset fields and reload data
                clearFields();
                loadMonAn();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm món ăn thất bại", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editMonAn() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món ăn cần sửa", 
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int id = (int) model.getValueAt(selectedRow, 0);
        String ten = tenField.getText().trim();
        String loaiMonAn = convertDisplayToLoaiMon(loaiMonAnComboBox.getSelectedItem().toString());
        String cachCheBien = cachCheBienArea.getText().trim();
        
        // Validate input
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên món ăn", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Create updated DTO
            MonAnDTO monAnDto = new MonAnDTO(id, ten, loaiMonAn, cachCheBien);
            
            // Update through controller
            boolean success = monAnController.updateMonAn(monAnDto);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật món ăn thành công!");
                // Reset fields and reload data
                clearFields();
                loadMonAn();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật món ăn thất bại", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteMonAn() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món ăn cần xóa", 
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int id = (int) model.getValueAt(selectedRow, 0);
        
        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn xóa món ăn này?", 
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Delete through controller
                boolean success = monAnController.deleteMonAn(id);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "Xóa món ăn thành công!");
                    // Reset fields and reload data
                    clearFields();
                    loadMonAn();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa món ăn thất bại", 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearFields() {
        tenField.setText("");
        loaiMonAnComboBox.setSelectedIndex(0);
        cachCheBienArea.setText("");
        themButton.setVisible(true);
        editButton.setVisible(false);
        deleteButton.setVisible(false);
        backButton.setVisible(false);
    }
} 