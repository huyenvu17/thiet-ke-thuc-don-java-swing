package ui;

import controller.INguyenLieuController;
import controller.NguyenLieuController;
import dao.NguyenLieuDao;
import dao.NhomThucPhamDao;
import dto.NguyenLieuDTO;
import entity.NguyenLieuEntity;
import entity.NhomThucPhamEntity;

import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
    private JComboBox<NhomThucPhamEntity> nhomThucPhamComboBox;
    private JButton themButton, editButton, deleteButton, backButton;
    private JPanel buttonsPanel;
    private INguyenLieuController nguyenLieuController;
    
    public QuanLyNguyenLieuPanel() {
        try {
            // Khởi tạo controller thay vì dao
            nguyenLieuController = NguyenLieuController.getInstance();
            initComponents();
            loadNguyenLieu();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo: " + ex.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("Quản Lý Nguyên Liệu", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        // Add components to panel
        add(titleLabel, BorderLayout.NORTH);
        // Create table model with column names
        model = new DefaultTableModel(new String[]{"ID", "Tên nguyên liệu", "Đơn vị", "Đơn giá", "Nhóm thực phẩm"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Create input panel with form fields
        JPanel inputPanel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        
        formPanel.add(new JLabel("Tên nguyên liệu:"));
        tenField = new JTextField();
        formPanel.add(tenField);
        
        formPanel.add(new JLabel("Đơn vị:"));
        donViField = new JTextField();
        formPanel.add(donViField);
        
        formPanel.add(new JLabel("Đơn giá:"));
        donGiaField = new JTextField();
        formPanel.add(donGiaField);
        
        formPanel.add(new JLabel("Nhóm thực phẩm:"));
        nhomThucPhamComboBox = new JComboBox<>();
        formPanel.add(nhomThucPhamComboBox);
        
        // Tải danh sách nhóm thực phẩm vào combo box
        loadNhomThucPhamComboBox();
        
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
        
        add(inputPanel, BorderLayout.SOUTH);

        // Add action listeners for buttons
        themButton.addActionListener(e -> {
            addNguyenLieu();
        });
        
        editButton.addActionListener(e -> {
            editNguyenLieu();
        });
        
        deleteButton.addActionListener(e -> {
            deleteNguyenLieu();
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
                    donViField.setText(model.getValueAt(selectedRow, 2).toString());
                    donGiaField.setText(model.getValueAt(selectedRow, 3).toString());
                    
                    // Lấy thông tin nguyên liệu để tìm nhóm thực phẩm
                    int id = (int) model.getValueAt(selectedRow, 0);
                    
                    try {
                        // Lấy danh sách nguyên liệu từ controller
                        List<NguyenLieuDTO> dsNguyenLieu = nguyenLieuController.getAllNguyenLieu();
                        
                        // Tìm nguyên liệu có ID tương ứng
                        for (NguyenLieuDTO nl : dsNguyenLieu) {
                            if (nl.getId() == id) {
                                // Tìm và chọn nhóm thực phẩm tương ứng trong ComboBox
                                selectNhomThucPhamInComboBox(nl.getNhomThucPhamId());
                                break;
                            }
                        }
                    } catch (Exception ex) {
                        // Bỏ qua lỗi
                    }
                    
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
    
    /**
     * Tải dữ liệu nguyên liệu từ controller theo mô hình MVC
     */
    private void loadNguyenLieu() {
        model.setRowCount(0); // Xóa dữ liệu cũ trong bảng
        
        try {
            // Lấy danh sách nguyên liệu từ controller
            List<NguyenLieuDTO> dsNguyenLieu = nguyenLieuController.getAllNguyenLieu();
            
            // Kiểm tra nếu danh sách trống
            if (dsNguyenLieu.isEmpty()) {
                System.out.println("Danh sách nguyên liệu trống, kiểm tra kết nối cơ sở dữ liệu");
            }
            
            // Thêm dữ liệu vào bảng
            for (NguyenLieuDTO nl : dsNguyenLieu) {
                Object[] row = {
                    nl.getId(),
                    nl.getTenNguyenLieu(),
                    nl.getDonViTinh(),
                    nl.getDonGia(),
                    nl.getTenNhomThucPham()
                };
                model.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi tải danh sách nguyên liệu: " + ex.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    /**
     * Tải danh sách nhóm thực phẩm vào combo box
     */
    private void loadNhomThucPhamComboBox() {
        try {
            nhomThucPhamComboBox.removeAllItems();
            
            // Thêm mục mặc định
            nhomThucPhamComboBox.addItem(new NhomThucPhamEntity(0, "-- Chọn nhóm thực phẩm --", ""));
            
            // Lấy danh sách nhóm thực phẩm
            List<NhomThucPhamEntity> danhSachNhom = NhomThucPhamDao.getInstance().getAllNhomThucPham();
            
            // Thêm vào combo box
            for (NhomThucPhamEntity nhom : danhSachNhom) {
                nhomThucPhamComboBox.addItem(nhom);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi tải danh sách nhóm thực phẩm: " + ex.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Thêm nguyên liệu thông qua controller
     */
    private void addNguyenLieu() {
        String ten = tenField.getText().trim();
        String donVi = donViField.getText().trim();
        String donGiaStr = donGiaField.getText().trim();
        
        // Lấy nhóm thực phẩm đã chọn
        NhomThucPhamEntity selectedNhom = (NhomThucPhamEntity) nhomThucPhamComboBox.getSelectedItem();
        int nhomThucPhamId = (selectedNhom != null) ? selectedNhom.id() : 0;
        
        // Validate input
        if (ten.isEmpty() || donVi.isEmpty() || donGiaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (nhomThucPhamId == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhóm thực phẩm", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            BigDecimal donGia = new BigDecimal(donGiaStr);
            
            // Tạo DTO cho nguyên liệu mới (id=0 sẽ được bỏ qua khi thêm mới)
            NguyenLieuDTO nlDto = new NguyenLieuDTO();
            nlDto.setId(0);
            nlDto.setTenNguyenLieu(ten);
            nlDto.setDonViTinh(donVi);
            nlDto.setDonGia(donGia);
            nlDto.setNhomThucPhamId(nhomThucPhamId);
            
            // Gọi controller để thêm nguyên liệu
            boolean success = nguyenLieuController.addNguyenLieu(nlDto);
            
            if (success) {
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
    
    /**
     * Cập nhật nguyên liệu thông qua controller
     */
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
        
        // Lấy nhóm thực phẩm đã chọn
        NhomThucPhamEntity selectedNhom = (NhomThucPhamEntity) nhomThucPhamComboBox.getSelectedItem();
        int nhomThucPhamId = (selectedNhom != null) ? selectedNhom.id() : 0;
        
        // Validate input
        if (ten.isEmpty() || donVi.isEmpty() || donGiaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (nhomThucPhamId == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhóm thực phẩm", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            BigDecimal donGia = new BigDecimal(donGiaStr);
            
            // Tạo DTO cho nguyên liệu cần cập nhật
            NguyenLieuDTO nlDto = new NguyenLieuDTO();
            nlDto.setId(id);
            nlDto.setTenNguyenLieu(ten);
            nlDto.setDonViTinh(donVi);
            nlDto.setDonGia(donGia);
            nlDto.setNhomThucPhamId(nhomThucPhamId);
            
            // Sử dụng controller để cập nhật
            boolean success = nguyenLieuController.updateNguyenLieu(nlDto);
            
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
    
    /**
     * Xóa nguyên liệu thông qua controller
     */
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
            // Sử dụng controller để xóa
            boolean success = nguyenLieuController.deleteNguyenLieu(id);
            
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
        nhomThucPhamComboBox.setSelectedIndex(0); // Reset về mục mặc định
        table.clearSelection();
        
        // Reset button visibility
        themButton.setVisible(true);
        editButton.setVisible(false);
        deleteButton.setVisible(false);
        backButton.setVisible(false);
    }
    
    /**
     * Chọn nhóm thực phẩm trong ComboBox dựa vào ID
     */
    private void selectNhomThucPhamInComboBox(int nhomThucPhamId) {
        for (int i = 0; i < nhomThucPhamComboBox.getItemCount(); i++) {
            NhomThucPhamEntity nhom = nhomThucPhamComboBox.getItemAt(i);
            if (nhom.id() == nhomThucPhamId) {
                nhomThucPhamComboBox.setSelectedIndex(i);
                return;
            }
        }
        
        // Nếu không tìm thấy, chọn mục đầu tiên (mặc định)
        nhomThucPhamComboBox.setSelectedIndex(0);
    }
} 