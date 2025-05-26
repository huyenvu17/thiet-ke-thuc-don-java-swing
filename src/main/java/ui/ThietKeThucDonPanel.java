package ui;

import controller.IThietKeThucDonController;
import controller.ThietKeThucDonController;
import dto.ThietKeThucDonDTO;
import dto.NguyenLieuDTO;
import dto.NhomThucPhamDTO;
import entity.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public class ThietKeThucDonPanel extends JPanel {
    
    private final IThietKeThucDonController thietKeThucDonController;
    private UserEntity currentUserEntity;
    private JTextField tenThucDonField;
    private JSpinner soNgaySpinner;
    private JTextField budgetSangField;
    private JTextField budgetTruaField;
    private JTextField budgetXeField;
    private JTextArea resultArea;
    private JPanel nhomThucPhamPanel;
    private Map<Integer, JCheckBox> nguyenLieuCheckboxes;
    private JPanel inputPanel;
    private JButton generateButton;
    
    public ThietKeThucDonPanel() {
        this(null);
    }
    
    public ThietKeThucDonPanel(UserEntity userEntity) {
        this.thietKeThucDonController = ThietKeThucDonController.getInstance();
        this.nguyenLieuCheckboxes = new HashMap<>();
        this.currentUserEntity = userEntity;
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        JLabel titleLabel = new JLabel("THIẾT KẾ THỰC ĐƠN", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);
        
        inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Tên thực đơn:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        tenThucDonField = new JTextField(20);
        inputPanel.add(tenThucDonField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        inputPanel.add(new JLabel("Số ngày:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(5, 1, 30, 1);
        soNgaySpinner = new JSpinner(spinnerModel);
        inputPanel.add(soNgaySpinner, gbc);
        
        JPanel budgetPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        budgetPanel.setBorder(BorderFactory.createTitledBorder("Ngân sách tối đa cho mỗi bữa ăn"));
        
        budgetPanel.add(new JLabel("Bữa sáng:"));
        budgetSangField = new JTextField("30000");
        budgetPanel.add(budgetSangField);
        
        budgetPanel.add(new JLabel("Bữa trưa:"));
        budgetTruaField = new JTextField("30000");
        budgetPanel.add(budgetTruaField);
        
        budgetPanel.add(new JLabel("Bữa xế:"));
        budgetXeField = new JTextField("20000");
        budgetPanel.add(budgetXeField);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        inputPanel.add(budgetPanel, gbc);
        
        nhomThucPhamPanel = createNguyenLieuSelectionPanel();
        JScrollPane scrollPaneNguyenLieu = new JScrollPane(nhomThucPhamPanel);
        scrollPaneNguyenLieu.setPreferredSize(new Dimension(600, 250));
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        inputPanel.add(scrollPaneNguyenLieu, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        generateButton = new JButton("Tạo Thực Đơn");
        generateButton.addActionListener(e -> generateThucDon());
        inputPanel.add(generateButton, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        
        Map<String, Boolean> monAnAvailability = thietKeThucDonController.checkMonAnAvailability();
        boolean hasSang = monAnAvailability.get("sang");
        boolean hasTrua = monAnAvailability.get("trua");
        boolean hasXe = monAnAvailability.get("xe");
        
        if (!hasSang || !hasTrua || !hasXe) {
            JLabel warningLabel = new JLabel("<html><font color='red'>Cảnh báo: Thiếu món ăn cho một số bữa. Vui lòng thêm món ăn trong phần quản lý.</font></html>");
            inputPanel.add(warningLabel, gbc);
        }
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        inputPanel.add(scrollPane, gbc);
        
        add(inputPanel, BorderLayout.CENTER);
        
        if (currentUserEntity == null || !currentUserEntity.getRole().equals("admin")) {
            JPanel restrictedPanel = new JPanel(new BorderLayout());
            restrictedPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            
            JLabel restrictedLabel = new JLabel("<html><div style='text-align: center;'>" +
                    "<h2>Chức năng này chỉ dành cho quản trị viên</h2>" +
                    "<p>Bạn không có quyền truy cập chức năng Thiết kế thực đơn.</p>" +
                    "<p>Vui lòng liên hệ quản trị viên để được hỗ trợ.</p>" +
                    "</div></html>", JLabel.CENTER);
            restrictedLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            restrictedPanel.add(restrictedLabel, BorderLayout.CENTER);
            
            // Ẩn panel nhập liệu và hiển thị thông báo giới hạn quyền
            inputPanel.setVisible(false);
            add(restrictedPanel, BorderLayout.CENTER);
        }
    }
    
    /**
     * Tạo panel chọn nguyên liệu theo từng nhóm thực phẩm
     */
    private JPanel createNguyenLieuSelectionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Chọn nguyên liệu theo nhóm thực phẩm"));
        List<NhomThucPhamDTO> nhomThucPhamList = thietKeThucDonController.getAllNhomThucPham();
        
        if (nhomThucPhamList.isEmpty()) {
            panel.add(new JLabel("Không có nhóm thực phẩm nào."));
            return panel;
        }
        
        List<NguyenLieuDTO> allNguyenLieu = thietKeThucDonController.getAllNguyenLieu();
        for (NhomThucPhamDTO nhom : nhomThucPhamList) {
            JPanel nhomPanel = new JPanel();
            nhomPanel.setLayout(new BorderLayout());
            String title = nhom.getTenNhom() + " (" + nhom.getMoTa() + ")";
            nhomPanel.setBorder(BorderFactory.createTitledBorder(title));
            List<NguyenLieuDTO> nguyenLieuThuocNhom = thietKeThucDonController.getNguyenLieuByNhomThucPhamId(nhom.getId());
            
            if (nguyenLieuThuocNhom.isEmpty()) {
                nhomPanel.add(new JLabel("Không có nguyên liệu thuộc nhóm này."), BorderLayout.CENTER);
            } else {
                JPanel checkboxPanel = new JPanel(new GridLayout(0, 3, 10, 5)); // 3 cột
                
                for (NguyenLieuDTO nguyenLieu : nguyenLieuThuocNhom) {
                    String checkboxText = nguyenLieu.getTenNguyenLieu() + " (" + 
                                         nguyenLieu.getKhoiLuong() + " kg - " + 
                                         nguyenLieu.getDonGia() + " VND)";
                    
                    JCheckBox checkBox = new JCheckBox(checkboxText);
                    // Mặc định chọn tất cả nguyên liệu
                    checkBox.setSelected(true);
                    
                    // Lưu checkbox vào map để có thể truy cập sau này
                    nguyenLieuCheckboxes.put(nguyenLieu.getId(), checkBox);
                    checkboxPanel.add(checkBox);
                }
                
                // Thêm các nút chọn/bỏ chọn tất cả
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JButton selectAllButton = new JButton("Chọn tất cả");
                JButton deselectAllButton = new JButton("Bỏ chọn tất cả");
                
                final int nhomId = nhom.getId();
                
                selectAllButton.addActionListener(e -> {
                    for (NguyenLieuDTO nl : nguyenLieuThuocNhom) {
                        JCheckBox cb = nguyenLieuCheckboxes.get(nl.getId());
                        if (cb != null) {
                            cb.setSelected(true);
                        }
                    }
                });
                
                deselectAllButton.addActionListener(e -> {
                    for (NguyenLieuDTO nl : nguyenLieuThuocNhom) {
                        JCheckBox cb = nguyenLieuCheckboxes.get(nl.getId());
                        if (cb != null) {
                            cb.setSelected(false);
                        }
                    }
                });
                
                buttonPanel.add(selectAllButton);
                buttonPanel.add(deselectAllButton);
                
                nhomPanel.add(buttonPanel, BorderLayout.NORTH);
                nhomPanel.add(checkboxPanel, BorderLayout.CENTER);
            }
            
            panel.add(nhomPanel);
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        return panel;
    }
    
    /**
     * Lấy danh sách ID của các nguyên liệu đã chọn
     */
    private List<Integer> getSelectedNguyenLieuIds() {
        List<Integer> selectedIds = new ArrayList<>();
        
        for (Map.Entry<Integer, JCheckBox> entry : nguyenLieuCheckboxes.entrySet()) {
            if (entry.getValue().isSelected()) {
                selectedIds.add(entry.getKey());
            }
        }
        
        return selectedIds;
    }
    
    private void generateThucDon() {
        String tenThucDon = tenThucDonField.getText().trim();
        if (tenThucDon.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên thực đơn", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int soNgay = (int) soNgaySpinner.getValue();
        double budgetSang, budgetTrua, budgetXe;
        try {
            budgetSang = Double.parseDouble(budgetSangField.getText().trim());
            budgetTrua = Double.parseDouble(budgetTruaField.getText().trim());
            budgetXe = Double.parseDouble(budgetXeField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ngân sách không hợp lệ. Vui lòng nhập số", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        List<Integer> selectedNguyenLieuIds = getSelectedNguyenLieuIds();
        
        if (selectedNguyenLieuIds.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Bạn chưa chọn nguyên liệu nào. Tiếp tục tạo thực đơn?", 
                    "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        }
        
        ThietKeThucDonDTO thietKeDto = new ThietKeThucDonDTO(
                tenThucDon,
                soNgay,
                budgetSang,
                budgetTrua,
                budgetXe,
                selectedNguyenLieuIds
        );
        
        int thucDonId = thietKeThucDonController.generateThucDon(thietKeDto);
        
        if (thucDonId < 0) {
            switch (thucDonId) {
                case -1:
                    JOptionPane.showMessageDialog(this, 
                            "Không thể tạo thực đơn. Có lỗi xảy ra trong quá trình tạo.", 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    break;
                case -2:
                    JOptionPane.showMessageDialog(this, 
                            "Không thể tạo thực đơn. Không tìm thấy món ăn nào phù hợp với nguyên liệu đã chọn.\n" +
                            "Vui lòng chọn thêm nguyên liệu hoặc kiểm tra lại công thức món ăn.", 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    break;
                case -3:
                    JOptionPane.showMessageDialog(this, 
                            "Không thể tạo thực đơn. Không đủ món ăn cho tất cả các bữa (sáng, trưa, tối).\n" +
                            "Vui lòng thêm món ăn cho các bữa còn thiếu hoặc chọn nhiều nguyên liệu hơn.", 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    break;
                default:
                    JOptionPane.showMessageDialog(this, 
                            "Không thể tạo thực đơn. Mã lỗi: " + thucDonId, 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }
        
        JOptionPane.showMessageDialog(this, "Đã tạo thực đơn thành công! Bạn có thể xem chi tiết trong phần 'Danh Sách Thực Đơn'");
        
        tenThucDonField.setText("");
        soNgaySpinner.setValue(7);
        displayThucDonResult(thucDonId, selectedNguyenLieuIds);
    }
    
    /**
     * Hiển thị kết quả thực đơn đã tạo
     */
    private void displayThucDonResult(int thucDonId, List<Integer> selectedNguyenLieuIds) {
        Map<String, Object> thucDonInfo = thietKeThucDonController.getThucDonWithDetails(thucDonId);
        if (thucDonInfo.isEmpty()) {
            resultArea.setText("Không thể tải thông tin thực đơn.");
            return;
        }
        
        ThucDonEntity thucDon = (ThucDonEntity) thucDonInfo.get("thucDon");
        @SuppressWarnings("unchecked")
        List<ChiTietThucDonEntity> chiTietList = (List<ChiTietThucDonEntity>) thucDonInfo.get("chiTietList");
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== THÔNG TIN THỰC ĐƠN ĐÃ TẠO ===\n\n");
        sb.append("ID: ").append(thucDonId).append("\n");
        sb.append("Tên thực đơn: ").append(thucDon.tenThucDon()).append("\n");
        sb.append("Số ngày: ").append(thucDon.soNgay()).append("\n\n");
        sb.append("Số nguyên liệu đã chọn: ").append(selectedNguyenLieuIds.size()).append("\n\n");
        
        // Hiển thị chi tiết thực đơn
        sb.append("Chi tiết thực đơn theo ngày:\n");
        
        if (chiTietList != null && !chiTietList.isEmpty()) {
            Map<Integer, List<ChiTietThucDonEntity>> chiTietTheoNgay = new HashMap<>();
            
            // Nhóm các chi tiết theo ngày
            for (ChiTietThucDonEntity chiTiet : chiTietList) {
                chiTietTheoNgay.computeIfAbsent(chiTiet.ngay(), k -> new ArrayList<>()).add(chiTiet);
            }
            
            // Hiển thị chi tiết theo từng ngày
            for (int day = 1; day <= thucDon.soNgay(); day++) {
                sb.append("\nNgày ").append(day).append(":\n");
                List<ChiTietThucDonEntity> chiTietNgay = chiTietTheoNgay.getOrDefault(day, new ArrayList<>());
                
                if (chiTietNgay.isEmpty()) {
                    sb.append("  Không có dữ liệu\n");
                } else {
                    for (ChiTietThucDonEntity chiTiet : chiTietNgay) {
                        String loaiBua = "";
                        switch (chiTiet.buoi()) {
                            case "sang": loaiBua = "Bữa sáng"; break;
                            case "trua": loaiBua = "Bữa trưa"; break;
                            case "xe": loaiBua = "Bữa tối"; break;
                            default: loaiBua = chiTiet.buoi();
                        }
                        
                        sb.append("  ").append(loaiBua).append(": ").append(chiTiet.tenMon()).append("\n");
                    }
                }
            }
        } else {
            sb.append("Không có dữ liệu chi tiết\n");
        }
        
        resultArea.setText(sb.toString());
    }
} 