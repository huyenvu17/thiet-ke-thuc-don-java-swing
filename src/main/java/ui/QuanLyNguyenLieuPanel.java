package ui;

import dao.NguyenLieuDao;
import entity.NguyenLieuEntity;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * Panel for managing ingredients
 */
public class QuanLyNguyenLieuPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField tenField, donViField, donGiaField;
    private NguyenLieuDao nguyenLieuDao;
    
    public QuanLyNguyenLieuPanel() {
        try {
            nguyenLieuDao = NguyenLieuDao.getInstance();
            initComponents();
            loadNguyenLieu();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo: " + ex.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());

        // Create table model with column names
        model = new DefaultTableModel(new String[]{"ID", "Tên nguyên liệu", "Đơn vị", "Đơn giá"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Create input panel with form fields
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.add(new JLabel("Tên nguyên liệu:"));
        tenField = new JTextField();
        inputPanel.add(tenField);
        
        inputPanel.add(new JLabel("Đơn vị:"));
        donViField = new JTextField();
        inputPanel.add(donViField);
        
        inputPanel.add(new JLabel("Đơn giá:"));
        donGiaField = new JTextField();
        inputPanel.add(donGiaField);
        
        JButton themButton = new JButton("Thêm");
        inputPanel.add(themButton);
        add(inputPanel, BorderLayout.SOUTH);

        // Add action listener for the add button
        themButton.addActionListener(e -> {
            addNguyenLieu();
        });
        
        // Add a button panel for edit and delete
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        JButton editButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");
        
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.NORTH);
        
        // Add action listeners for edit and delete buttons
        editButton.addActionListener(e -> {
            editNguyenLieu();
        });
        
        deleteButton.addActionListener(e -> {
            deleteNguyenLieu();
        });
        
        // Add listener for table selection
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                tenField.setText(model.getValueAt(row, 1).toString());
                donViField.setText(model.getValueAt(row, 2).toString());
                donGiaField.setText(model.getValueAt(row, 3).toString());
            }
        });
    }
    
    private void loadNguyenLieu() {
        model.setRowCount(0); // Clear existing rows
        
        for (NguyenLieuEntity nl : nguyenLieuDao.getAllNguyenLieu()) {
            Object[] row = {
                nl.id(),
                nl.tenNguyenLieu(),
                nl.donViTinh(),
                nl.donGia()
            };
            model.addRow(row);
        }
    }
    
    private void addNguyenLieu() {
        String ten = tenField.getText().trim();
        String donVi = donViField.getText().trim();
        String donGiaStr = donGiaField.getText().trim();
        
        // Validate input
        if (ten.isEmpty() || donVi.isEmpty() || donGiaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            BigDecimal donGia = new BigDecimal(donGiaStr);
            // Create with id=0 (it will be ignored in the insert method)
            NguyenLieuEntity nl = new NguyenLieuEntity(0, ten, donVi, donGia);
            
            int newId = nguyenLieuDao.addNguyenLieu(nl);
            if (newId > 0) {
                JOptionPane.showMessageDialog(this, "Thêm nguyên liệu thành công!");
                // Reset fields and reload data
                clearFields();
                loadNguyenLieu();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm nguyên liệu thất bại", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Đơn giá phải là số", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editNguyenLieu() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nguyên liệu cần sửa", 
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int id = (int) model.getValueAt(selectedRow, 0);
        String ten = tenField.getText().trim();
        String donVi = donViField.getText().trim();
        String donGiaStr = donGiaField.getText().trim();
        
        // Validate input
        if (ten.isEmpty() || donVi.isEmpty() || donGiaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            BigDecimal donGia = new BigDecimal(donGiaStr);
            NguyenLieuEntity nl = new NguyenLieuEntity(id, ten, donVi, donGia);
            
            boolean success = nguyenLieuDao.updateNguyenLieu(nl);
            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật nguyên liệu thành công!");
                // Reset fields and reload data
                clearFields();
                loadNguyenLieu();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật nguyên liệu thất bại", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Đơn giá phải là số", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteNguyenLieu() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nguyên liệu cần xóa", 
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int id = (int) model.getValueAt(selectedRow, 0);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn xóa nguyên liệu này?", 
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = nguyenLieuDao.deleteNguyenLieu(id);
            if (success) {
                JOptionPane.showMessageDialog(this, "Xóa nguyên liệu thành công!");
                // Reset fields and reload data
                clearFields();
                loadNguyenLieu();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa nguyên liệu thất bại", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearFields() {
        tenField.setText("");
        donViField.setText("");
        donGiaField.setText("");
        table.clearSelection();
    }
} 