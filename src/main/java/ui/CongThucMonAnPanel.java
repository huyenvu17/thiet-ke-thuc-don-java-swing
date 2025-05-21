package ui;

import controller.ICongThucMonAnController;
import controller.CongThucMonAnController;
import dto.CongThucMonAnDTO;
import dto.MonAnDTO;
import dto.NguyenLieuDTO;
import entity.UserEntity;

import java.awt.*;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class CongThucMonAnPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField khoiLuongField;
    private JComboBox<MonAnDTO> monAnComboBox;
    private JComboBox<NguyenLieuDTO> nguyenLieuComboBox;
    private JPanel buttonsPanel;
    private JPanel inputPanel;
    private JButton themButton, editButton, deleteButton, backButton;
    private ICongThucMonAnController congThucMonAnController;
    private UserEntity currentUserEntity;
    
    public CongThucMonAnPanel() {
        this(null);
    }
    
    public CongThucMonAnPanel(UserEntity userEntity) {
        try {
            this.congThucMonAnController = CongThucMonAnController.getInstance();
            this.currentUserEntity = userEntity;
            initComponents();
            loadCongThucMonAn();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo: " + ex.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(5, 5, 5, 5));
        JLabel titleLabel = new JLabel("QUẢN LÝ CÔNG THỨC MÓN ĂN", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        // Add components to panel
        add(titleLabel, BorderLayout.NORTH);
        
        // Create table model with column names
        model = new DefaultTableModel(new String[]{"ID", "Món ăn", "Nguyên liệu", "Khối lượng (kg)"}, 0) {
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
        inputPanel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        
        formPanel.add(new JLabel("Món ăn:"));
        monAnComboBox = new JComboBox<>();
        loadMonAnComboBox();
        formPanel.add(monAnComboBox);
        
        formPanel.add(new JLabel("Nguyên liệu:"));
        nguyenLieuComboBox = new JComboBox<>();
        loadNguyenLieuComboBox();
        formPanel.add(nguyenLieuComboBox);
        
        formPanel.add(new JLabel("Khối lượng:"));
        khoiLuongField = new JTextField();
        formPanel.add(khoiLuongField);
        
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
            addCongThucMonAn();
        });
        
        editButton.addActionListener(e -> {
            editCongThucMonAn();
        });
        
        deleteButton.addActionListener(e -> {
            deleteCongThucMonAn();
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
                    // Get the selected item's data
                    int id = (int) model.getValueAt(selectedRow, 0);
                    CongThucMonAnDTO selectedCongThuc = congThucMonAnController.getCongThucMonAnById(id);
                    
                    if (selectedCongThuc != null) {
                        // Set selected MonAn in combobox
                        selectMonAnById(selectedCongThuc.getMonAnId());
                        
                        // Set selected NguyenLieu in combobox
                        selectNguyenLieuById(selectedCongThuc.getNguyenLieuId());
                        
                        // Set khối lượng
                        khoiLuongField.setText(selectedCongThuc.getKhoiLuong().toString());
                        
                        // Show edit/delete/back buttons, hide add button
                        themButton.setVisible(false);
                        editButton.setVisible(true);
                        deleteButton.setVisible(true);
                        backButton.setVisible(true);
                    }
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
    
    private void loadMonAnComboBox() {
        monAnComboBox.removeAllItems();
        List<MonAnDTO> monAnList = congThucMonAnController.getAllMonAn();
        for (MonAnDTO monAn : monAnList) {
            monAnComboBox.addItem(monAn);
        }
        
        // Custom renderer to display the ten_mon
        monAnComboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            if (value == null) {
                return new JLabel("");
            }
            return new JLabel(((MonAnDTO) value).getTenMon());
        });
    }
    
    private void loadNguyenLieuComboBox() {
        nguyenLieuComboBox.removeAllItems();
        List<NguyenLieuDTO> nguyenLieuList = congThucMonAnController.getAllNguyenLieu();
        for (NguyenLieuDTO nguyenLieu : nguyenLieuList) {
            nguyenLieuComboBox.addItem(nguyenLieu);
        }
        
        // Custom renderer to display the ten_nguyen_lieu
        nguyenLieuComboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            if (value == null) {
                return new JLabel("");
            }
            return new JLabel(((NguyenLieuDTO) value).getTenNguyenLieu());
        });
    }
    
    private void selectMonAnById(int monAnId) {
        for (int i = 0; i < monAnComboBox.getItemCount(); i++) {
            MonAnDTO monAn = monAnComboBox.getItemAt(i);
            if (monAn.getId() == monAnId) {
                monAnComboBox.setSelectedIndex(i);
                break;
            }
        }
    }
    
    private void selectNguyenLieuById(int nguyenLieuId) {
        for (int i = 0; i < nguyenLieuComboBox.getItemCount(); i++) {
            NguyenLieuDTO nguyenLieu = nguyenLieuComboBox.getItemAt(i);
            if (nguyenLieu.getId() == nguyenLieuId) {
                nguyenLieuComboBox.setSelectedIndex(i);
                break;
            }
        }
    }
    
    private void loadCongThucMonAn() {
        model.setRowCount(0); // Clear existing rows

        try {
            // Load from database through controller
            List<CongThucMonAnDTO> congThucList = congThucMonAnController.getAllCongThucMonAn();
            for (CongThucMonAnDTO congThuc : congThucList) {
                Object[] row = {
                    congThuc.getId(),
                    congThuc.getTenMonAn(),
                    congThuc.getTenNguyenLieu(),
                    congThuc.getKhoiLuong()
                };
                model.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                    "Lỗi khi tải danh sách công thức: " + ex.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void addCongThucMonAn() {
        try {
            // Get selected MonAn
            MonAnDTO selectedMonAn = (MonAnDTO) monAnComboBox.getSelectedItem();
            if (selectedMonAn == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn món ăn", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Get selected NguyenLieu
            NguyenLieuDTO selectedNguyenLieu = (NguyenLieuDTO) nguyenLieuComboBox.getSelectedItem();
            if (selectedNguyenLieu == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nguyên liệu", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Get khối lượng
            String khoiLuongText = khoiLuongField.getText().trim();
            if (khoiLuongText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập khối lượng", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Double khoiLuong;
            try {
                khoiLuong = Double.parseDouble(khoiLuongText);
                if (khoiLuong <= 0) {
                    JOptionPane.showMessageDialog(this, "Khối lượng phải lớn hơn 0", 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Khối lượng không hợp lệ", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create DTO
            CongThucMonAnDTO congThucDto = new CongThucMonAnDTO();
            congThucDto.setMonAnId(selectedMonAn.getId());
            congThucDto.setNguyenLieuId(selectedNguyenLieu.getId());
            congThucDto.setKhoiLuong(khoiLuong);
            
            // Save through controller
            boolean success = congThucMonAnController.addCongThucMonAn(congThucDto);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Thêm công thức thành công!");
                clearFields();
                loadCongThucMonAn();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm công thức thất bại", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void editCongThucMonAn() {
        try {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn công thức cần sửa", 
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            int id = (int) model.getValueAt(selectedRow, 0);
            
            // Get selected MonAn
            MonAnDTO selectedMonAn = (MonAnDTO) monAnComboBox.getSelectedItem();
            if (selectedMonAn == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn món ăn", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Get selected NguyenLieu
            NguyenLieuDTO selectedNguyenLieu = (NguyenLieuDTO) nguyenLieuComboBox.getSelectedItem();
            if (selectedNguyenLieu == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nguyên liệu", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Get khối lượng
            String khoiLuongText = khoiLuongField.getText().trim();
            if (khoiLuongText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập khối lượng", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Double khoiLuong;
            try {
                khoiLuong = Double.parseDouble(khoiLuongText);
                if (khoiLuong <= 0) {
                    JOptionPane.showMessageDialog(this, "Khối lượng phải lớn hơn 0", 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Khối lượng không hợp lệ", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create DTO
            CongThucMonAnDTO congThucDto = new CongThucMonAnDTO();
            congThucDto.setId(id);
            congThucDto.setMonAnId(selectedMonAn.getId());
            congThucDto.setNguyenLieuId(selectedNguyenLieu.getId());
            congThucDto.setKhoiLuong(khoiLuong);
            
            // Update through controller
            boolean success = congThucMonAnController.updateCongThucMonAn(congThucDto);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật công thức thành công!");
                clearFields();
                loadCongThucMonAn();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật công thức thất bại", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void deleteCongThucMonAn() {
        try {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn công thức cần xóa", 
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            int id = (int) model.getValueAt(selectedRow, 0);
            
            // Confirm deletion
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Bạn có chắc chắn muốn xóa công thức này?", 
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                // Delete through controller
                boolean success = congThucMonAnController.deleteCongThucMonAn(id);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "Xóa công thức thành công!");
                    clearFields();
                    loadCongThucMonAn();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa công thức thất bại", 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void clearFields() {
        if (monAnComboBox.getItemCount() > 0) {
            monAnComboBox.setSelectedIndex(0);
        }
        if (nguyenLieuComboBox.getItemCount() > 0) {
            nguyenLieuComboBox.setSelectedIndex(0);
        }
        khoiLuongField.setText("");
        themButton.setVisible(true);
        editButton.setVisible(false);
        deleteButton.setVisible(false);
        backButton.setVisible(false);
    }
} 