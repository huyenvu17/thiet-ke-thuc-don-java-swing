package ui;

import controller.INguyenLieuController;
import controller.NguyenLieuController;
import dao.NguyenLieuDao;
import dao.NhomThucPhamDao;
import dto.NguyenLieuDTO;
import entity.NguyenLieuEntity;
import entity.NhomThucPhamEntity;
import entity.UserEntity;
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
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * Panel Quản Lý Nguyên Liệu
 */
public class QuanLyNguyenLieuPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField tenField, donViField, donGiaField;
    private JComboBox<NhomThucPhamEntity> nhomThucPhamComboBox;
    private JButton themButton, editButton, deleteButton, backButton;
    private JPanel buttonsPanel;
    private INguyenLieuController nguyenLieuController;
    private UserEntity currentUserEntity;

    public QuanLyNguyenLieuPanel(UserEntity userEntity) {
        try {
            this.nguyenLieuController = NguyenLieuController.getInstance();
            this.currentUserEntity = userEntity;
            initComponents();
            loadNguyenLieu();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo: " + ex.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(5, 5, 5, 5));
        JLabel titleLabel = new JLabel("QUẢN LÝ NGUYÊN LIỆU", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        add(titleLabel, BorderLayout.NORTH);
        model = new DefaultTableModel(new String[]{"ID", "Tên nguyên liệu", "Khối lượng(kg)", "Đơn giá", "Nhóm thực phẩm"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(model);
        table.getColumnModel().getColumn(0).setMinWidth(40);
        table.getColumnModel().getColumn(0).setMaxWidth(60);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        
        formPanel.add(new JLabel("Tên nguyên liệu:"));
        tenField = new JTextField();
        formPanel.add(tenField);
        
        formPanel.add(new JLabel("Khối lượng(kg):"));
        donViField = new JTextField();
        formPanel.add(donViField);
        
        formPanel.add(new JLabel("Đơn giá:"));
        donGiaField = new JTextField();
        formPanel.add(donGiaField);
        
        formPanel.add(new JLabel("Nhóm thực phẩm:"));
        nhomThucPhamComboBox = new JComboBox<>();
        formPanel.add(nhomThucPhamComboBox);
        
        // Tải danh sách nhóm thực phẩm
        loadNhomThucPhamComboBox();
        
        // buttons panel
        buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        themButton = new JButton("Thêm");
        editButton = new JButton("Sửa");
        deleteButton = new JButton("Xóa");
        backButton = new JButton("Quay lại");

        buttonsPanel.add(themButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(backButton);
        
        // Hiển thị
        editButton.setVisible(false);
        deleteButton.setVisible(false);
        backButton.setVisible(false);

        inputPanel.add(formPanel, BorderLayout.CENTER);
        inputPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        add(inputPanel, BorderLayout.SOUTH);
        if(!currentUserEntity.getRole().equals("admin"))
        {
            inputPanel.setVisible(false);
            buttonsPanel.setVisible(false);
        }

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
            clearFields();
            table.clearSelection();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    tenField.setText(model.getValueAt(selectedRow, 1).toString());
                    donViField.setText(model.getValueAt(selectedRow, 2).toString());
                    donGiaField.setText(model.getValueAt(selectedRow, 3).toString());

                    int id = (int) model.getValueAt(selectedRow, 0);
                    try {
                        List<NguyenLieuDTO> dsNguyenLieu = nguyenLieuController.getAllNguyenLieu();
                        for (NguyenLieuDTO nl : dsNguyenLieu) {
                            if (nl.getId() == id) {
                                selectNhomThucPhamInComboBox(nl.getNhomThucPhamId());
                                break;
                            }
                        }
                    } catch (Exception ex) {
                    }

                    themButton.setVisible(false);
                    editButton.setVisible(true);
                    deleteButton.setVisible(true);
                    backButton.setVisible(true);
                } else {
                    themButton.setVisible(true);
                    editButton.setVisible(false);
                    deleteButton.setVisible(false);
                    backButton.setVisible(false);
                }
            }
        });
    }
    
    /**
     * Tải dữ liệu nguyên liệu từ controller
     */
    private void loadNguyenLieu() {
        model.setRowCount(0);
        
        try {
            List<NguyenLieuDTO> dsNguyenLieu = nguyenLieuController.getAllNguyenLieu();
            if (dsNguyenLieu.isEmpty()) {
                System.out.println("Danh sách nguyên liệu trống, kiểm tra kết nối cơ sở dữ liệu");
            }

            for (NguyenLieuDTO nl : dsNguyenLieu) {
                Object[] row = {
                    nl.getId(),
                    nl.getTenNguyenLieu(),
                    nl.getKhoiLuong(),
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
            nhomThucPhamComboBox.addItem(new NhomThucPhamEntity(0, "-- Chọn nhóm thực phẩm --", ""));
            List<NhomThucPhamEntity> danhSachNhom = NhomThucPhamDao.getInstance().getAllNhomThucPham();
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

        NhomThucPhamEntity selectedNhom = (NhomThucPhamEntity) nhomThucPhamComboBox.getSelectedItem();
        int nhomThucPhamId = (selectedNhom != null) ? selectedNhom.id() : 0;
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
            Double khoiLuong = Double.parseDouble(donVi);
            BigDecimal donGia = new BigDecimal(donGiaStr);
            NguyenLieuDTO nlDto = new NguyenLieuDTO();
            nlDto.setId(0);
            nlDto.setTenNguyenLieu(ten);
            nlDto.setKhoiLuong(khoiLuong);
            nlDto.setDonGia(donGia);
            nlDto.setNhomThucPhamId(nhomThucPhamId);

            boolean success = nguyenLieuController.addNguyenLieu(nlDto);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Thêm nguyên liệu thành công!");
                clearFields();
                loadNguyenLieu();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm nguyên liệu thất bại", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Khối lượng và đơn giá phải là số", 
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

        NhomThucPhamEntity selectedNhom = (NhomThucPhamEntity) nhomThucPhamComboBox.getSelectedItem();
        int nhomThucPhamId = (selectedNhom != null) ? selectedNhom.id() : 0;
        
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
            Double khoiLuong = Double.parseDouble(donVi);
            BigDecimal donGia = new BigDecimal(donGiaStr);

            NguyenLieuDTO nlDto = new NguyenLieuDTO();
            nlDto.setId(id);
            nlDto.setTenNguyenLieu(ten);
            nlDto.setKhoiLuong(khoiLuong);
            nlDto.setDonGia(donGia);
            nlDto.setNhomThucPhamId(nhomThucPhamId);

            boolean success = nguyenLieuController.updateNguyenLieu(nlDto);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật nguyên liệu thành công!");
                clearFields();
                loadNguyenLieu();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật nguyên liệu thất bại", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Khối lượng và đơn giá phải là số", 
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
        nhomThucPhamComboBox.setSelectedIndex(0);
        table.clearSelection();
        
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
        
        nhomThucPhamComboBox.setSelectedIndex(0);
    }
} 