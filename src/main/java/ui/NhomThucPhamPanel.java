package ui;

import controller.INhomThucPhamController;
import controller.NhomThucPhamController;
import dto.NhomThucPhamDTO;
import entity.UserEntity;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class NhomThucPhamPanel extends JPanel {
    
    private INhomThucPhamController nhomThucPhamController;
    private JTable nhomThucPhamTable;
    private DefaultTableModel tableModel;
    private JTextField tenNhomField;
    private JTextArea moTaArea;
    private JButton themButton;
    private JButton suaButton;
    private JButton xoaButton;
    private JButton lamMoiButton;
    private int selectedId = -1;
    private UserEntity currentUserEntity;
    private JPanel formPanel;
    private JPanel buttonPanel;
    
    /**
     * Khởi tạo panel quản lý nhóm thực phẩm
     */
    public NhomThucPhamPanel() {
        this(null);
    }
    
    /**
     * Khởi tạo panel quản lý nhóm thực phẩm với UserEntity
     */
    public NhomThucPhamPanel(UserEntity userEntity) {
        try {
            this.nhomThucPhamController = NhomThucPhamController.getInstance();
            this.currentUserEntity = userEntity;
            initComponents();
            loadTableData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo: " + ex.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Khởi tạo các thành phần giao diện
     */
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(5, 5, 5, 5));
        
        // Panel tiêu đề
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("QUẢN LÝ NHÓM THỰC PHẨM");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        // Panel chính chia làm 2 phần: bảng (trái) và form nhập liệu (phải)
        JPanel mainPanel  = new JPanel(new GridLayout(1, 2, 10, 0));

        // Bảng danh sách đặt ở bên trái
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Danh sách nhóm thực phẩm"));
        
        // Tạo model cho bảng
        String[] columnNames = {"ID", "Tên nhóm", "Mô tả", "Số lượng nguyên liệu"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép sửa trực tiếp trên bảng
            }
        };
        
        nhomThucPhamTable = new JTable(tableModel);
        nhomThucPhamTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        nhomThucPhamTable.getTableHeader().setReorderingAllowed(false);
        nhomThucPhamTable.getColumnModel().getColumn(0).setMinWidth(40);
        nhomThucPhamTable.getColumnModel().getColumn(0).setMaxWidth(60);
        nhomThucPhamTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        
        // Thêm sự kiện click chuột vào hàng trong bảng
        nhomThucPhamTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = nhomThucPhamTable.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedId = (int) nhomThucPhamTable.getValueAt(selectedRow, 0);
                    tenNhomField.setText((String) nhomThucPhamTable.getValueAt(selectedRow, 1));
                    moTaArea.setText((String) nhomThucPhamTable.getValueAt(selectedRow, 2));
                    
                    // Kích hoạt các nút sửa, xóa
                    if (currentUserEntity != null && currentUserEntity.getRole().equals("admin")) {
                        suaButton.setEnabled(true);
                        xoaButton.setEnabled(true);
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(nhomThucPhamTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Form nhập liệu đặt ở bên phải
        formPanel = new JPanel(new BorderLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin nhóm thực phẩm"));
        
        // Tạo panel cho các trường nhập liệu sử dụng GridBagLayout
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(new EmptyBorder(10, 10, 5, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Tên nhóm - label ở trên, input ở dưới
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        inputPanel.add(new JLabel("Tên nhóm:"), gbc);
        
        gbc.gridy = 1;
        tenNhomField = new JTextField(20);
        inputPanel.add(tenNhomField, gbc);
        
        // Mô tả - label ở trên, textarea ở dưới
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Mô tả:"), gbc);
        
        gbc.gridy = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        moTaArea = new JTextArea(5, 20);
        moTaArea.setLineWrap(true);
        moTaArea.setWrapStyleWord(true);
        JScrollPane moTaScrollPane = new JScrollPane(moTaArea);
        inputPanel.add(moTaScrollPane, gbc);
        
        // Panel chứa các nút thao tác
        buttonPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        buttonPanel.setBorder(new EmptyBorder(5, 10, 10, 10));
        
        themButton = new JButton("Thêm");
        suaButton = new JButton("Sửa");
        xoaButton = new JButton("Xóa");
        lamMoiButton = new JButton("Làm mới");
        
        // Mặc định disable các nút sửa, xóa cho đến khi người dùng chọn một hàng trong bảng
        suaButton.setEnabled(false);
        xoaButton.setEnabled(false);
        
        // Thêm sự kiện cho các nút
        themButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                themNhomThucPham();
            }
        });
        
        suaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                suaNhomThucPham();
            }
        });
        
        xoaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xoaNhomThucPham();
            }
        });
        
        lamMoiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetForm();
            }
        });
        
        buttonPanel.add(themButton);
        buttonPanel.add(suaButton);
        buttonPanel.add(xoaButton);
        buttonPanel.add(lamMoiButton);
        
        // Gắn input panel và button panel vào form panel
        formPanel.add(inputPanel, BorderLayout.CENTER);
        formPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Gắn table panel và form panel vào main panel
        mainPanel.add(tablePanel);
        if (currentUserEntity != null && currentUserEntity.getRole().equals("admin")) {
            mainPanel.add(formPanel);
        }
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    /**
     * Tải dữ liệu vào bảng
     */
    private void loadTableData() {
        // Xóa dữ liệu cũ trong bảng
        tableModel.setRowCount(0);
        
        try {
            // Lấy danh sách nhóm thực phẩm từ controller
            List<NhomThucPhamDTO> danhSachNhom = nhomThucPhamController.getAllNhomThucPham();
            
            // Thêm dữ liệu vào bảng
            for (NhomThucPhamDTO nhom : danhSachNhom) {
                Object[] rowData = {nhom.getId(), nhom.getTenNhom(), nhom.getMoTa(), nhom.getSoLuongNguyenLieu()};
                tableModel.addRow(rowData);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                    "Lỗi khi tải danh sách nhóm thực phẩm: " + ex.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Thêm nhóm thực phẩm mới
     */
    private void themNhomThucPham() {
        String tenNhom = tenNhomField.getText().trim();
        String moTa = moTaArea.getText().trim();
        
        // Kiểm tra dữ liệu đầu vào
        if (tenNhom.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên nhóm thực phẩm", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Tạo đối tượng DTO mới
        NhomThucPhamDTO nhomMoi = new NhomThucPhamDTO(0, tenNhom, moTa);
        
        // Thêm vào database thông qua controller
        boolean success = nhomThucPhamController.addNhomThucPham(nhomMoi);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Thêm nhóm thực phẩm thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            resetForm();
            loadTableData();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm nhóm thực phẩm thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Sửa nhóm thực phẩm
     */
    private void suaNhomThucPham() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhóm thực phẩm để sửa", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String tenNhom = tenNhomField.getText().trim();
        String moTa = moTaArea.getText().trim();
        
        // Kiểm tra dữ liệu đầu vào
        if (tenNhom.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên nhóm thực phẩm", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Tạo đối tượng DTO để cập nhật
        NhomThucPhamDTO nhomCapNhat = new NhomThucPhamDTO(selectedId, tenNhom, moTa);
        
        // Cập nhật thông qua controller
        boolean success = nhomThucPhamController.updateNhomThucPham(nhomCapNhat);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Cập nhật nhóm thực phẩm thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            resetForm();
            loadTableData();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật nhóm thực phẩm thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Xóa nhóm thực phẩm
     */
    private void xoaNhomThucPham() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhóm thực phẩm để xóa", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Kiểm tra số lượng nguyên liệu thuộc nhóm
        int soLuong = nhomThucPhamController.getSoLuongNguyenLieu(selectedId);
        if (soLuong > 0) {
            JOptionPane.showMessageDialog(this, 
                    "Không thể xóa nhóm thực phẩm này vì có " + soLuong + " nguyên liệu đang thuộc nhóm này.", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Xác nhận xóa
        int option = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn xóa nhóm thực phẩm này?", 
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        
        if (option != JOptionPane.YES_OPTION) {
            return;
        }
        
        // Xóa thông qua controller
        boolean success = nhomThucPhamController.deleteNhomThucPham(selectedId);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Xóa nhóm thực phẩm thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            resetForm();
            loadTableData();
        } else {
            JOptionPane.showMessageDialog(this, "Xóa nhóm thực phẩm thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Reset form về trạng thái ban đầu
     */
    private void resetForm() {
        selectedId = -1;
        tenNhomField.setText("");
        moTaArea.setText("");
        suaButton.setEnabled(false);
        xoaButton.setEnabled(false);
        nhomThucPhamTable.clearSelection();
    }
}
