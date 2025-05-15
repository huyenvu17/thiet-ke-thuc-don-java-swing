package ui;

import entity.*;
import service.ThucDonService;
import dao.MonAnDao;
import dao.NguyenLieuDao;
import dao.NhomThucPhamDao;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Panel for menu design functionality
 */
public class ThietKeThucDonPanel extends JPanel {
    
    private final ThucDonService thucDonService;
    private final MonAnDao monAnDao;
    private final NguyenLieuDao nguyenLieuDao;
    private final NhomThucPhamDao nhomThucPhamDao;
    
    private JTextField tenThucDonField;
    private JSpinner soNgaySpinner;
    private JTextField budgetSangField;
    private JTextField budgetTruaField;
    private JTextField budgetXeField;
    private JTextArea resultArea;
    
    // Panel chứa các danh sách nguyên liệu theo nhóm
    private JPanel nhomThucPhamPanel;
    // Map lưu các checkbox theo nguyên liệu ID
    private Map<Integer, JCheckBox> nguyenLieuCheckboxes;
    
    public ThietKeThucDonPanel() {
        thucDonService = new ThucDonService();
        monAnDao = MonAnDao.getInstance();
        nguyenLieuDao = NguyenLieuDao.getInstance();
        nhomThucPhamDao = NhomThucPhamDao.getInstance();
        nguyenLieuCheckboxes = new HashMap<>();
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        JLabel titleLabel = new JLabel("THIẾT KẾ THỰC ĐƠN", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
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
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(5, 1, 30, 1);
        soNgaySpinner = new JSpinner(spinnerModel);
        inputPanel.add(soNgaySpinner, gbc);
        
        // Budget inputs
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
        
        // Tạo panel chọn nguyên liệu theo nhóm thực phẩm
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
        
        // Generate button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JButton generateButton = new JButton("Tạo Thực Đơn");
        generateButton.addActionListener(e -> generateThucDon());
        inputPanel.add(generateButton, gbc);
        
        // Check if we have the required number of dishes
        gbc.gridx = 0;
        gbc.gridy = 5;
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
    }
    
    /**
     * Tạo panel chọn nguyên liệu theo từng nhóm thực phẩm
     */
    private JPanel createNguyenLieuSelectionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Chọn nguyên liệu theo nhóm thực phẩm"));
        
        // Lấy danh sách tất cả nhóm thực phẩm
        List<NhomThucPhamEntity> nhomThucPhamList = nhomThucPhamDao.getAllNhomThucPham();
        
        if (nhomThucPhamList.isEmpty()) {
            panel.add(new JLabel("Không có nhóm thực phẩm nào."));
            return panel;
        }
        
        // Lấy danh sách tất cả nguyên liệu
        List<NguyenLieuEntity> allNguyenLieu = nguyenLieuDao.getAllNguyenLieu();
        
        // Tạo panel cho mỗi nhóm thực phẩm
        for (NhomThucPhamEntity nhom : nhomThucPhamList) {
            JPanel nhomPanel = new JPanel();
            nhomPanel.setLayout(new BorderLayout());
            
            // Tiêu đề panel nhóm
            String title = nhom.tenNhom() + " (" + nhom.moTa() + ")";
            nhomPanel.setBorder(BorderFactory.createTitledBorder(title));
            
            // Lọc nguyên liệu thuộc nhóm hiện tại
            List<NguyenLieuEntity> nguyenLieuThuocNhom = allNguyenLieu.stream()
                    .filter(nl -> nl.nhomThucPhamId() == nhom.id())
                    .toList();
            
            if (nguyenLieuThuocNhom.isEmpty()) {
                nhomPanel.add(new JLabel("Không có nguyên liệu thuộc nhóm này."), BorderLayout.CENTER);
            } else {
                // Tạo panel chứa các checkbox nguyên liệu
                JPanel checkboxPanel = new JPanel(new GridLayout(0, 3, 10, 5)); // 3 cột
                
                for (NguyenLieuEntity nguyenLieu : nguyenLieuThuocNhom) {
                    String checkboxText = nguyenLieu.tenNguyenLieu() + " (" + 
                                         nguyenLieu.khoiLuong() + " kg - " + 
                                         nguyenLieu.donGia() + " VND)";
                    
                    JCheckBox checkBox = new JCheckBox(checkboxText);
                    // Mặc định chọn tất cả nguyên liệu
                    checkBox.setSelected(true);
                    
                    // Lưu checkbox vào map để có thể truy cập sau này
                    nguyenLieuCheckboxes.put(nguyenLieu.id(), checkBox);
                    checkboxPanel.add(checkBox);
                }
                
                // Thêm các nút chọn/bỏ chọn tất cả
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JButton selectAllButton = new JButton("Chọn tất cả");
                JButton deselectAllButton = new JButton("Bỏ chọn tất cả");
                
                final int nhomId = nhom.id();
                
                selectAllButton.addActionListener(e -> {
                    for (NguyenLieuEntity nl : nguyenLieuThuocNhom) {
                        JCheckBox cb = nguyenLieuCheckboxes.get(nl.id());
                        if (cb != null) {
                            cb.setSelected(true);
                        }
                    }
                });
                
                deselectAllButton.addActionListener(e -> {
                    for (NguyenLieuEntity nl : nguyenLieuThuocNhom) {
                        JCheckBox cb = nguyenLieuCheckboxes.get(nl.id());
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
            panel.add(Box.createRigidArea(new Dimension(0, 10))); // Khoảng cách giữa các nhóm
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
        
        // Lấy danh sách ID nguyên liệu đã chọn
        List<Integer> selectedNguyenLieuIds = getSelectedNguyenLieuIds();
        
        if (selectedNguyenLieuIds.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Bạn chưa chọn nguyên liệu nào. Tiếp tục tạo thực đơn?", 
                    "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        }
        
        // Sử dụng phương thức generateThucDon có sẵn
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
        
        // Hiển thị thông tin thực đơn đã tạo
        displayThucDonResult(thucDonId, selectedNguyenLieuIds);
    }
    
    /**
     * Hiển thị kết quả thực đơn đã tạo
     */
    private void displayThucDonResult(int thucDonId, List<Integer> selectedNguyenLieuIds) {
        Map<String, Object> thucDonInfo = thucDonService.getThucDonWithDetails(thucDonId);
        if (thucDonInfo.isEmpty()) {
            resultArea.setText("Không thể tải thông tin thực đơn.");
            return;
        }
        
        ThucDonEntity thucDon = (ThucDonEntity) thucDonInfo.get("thucDon");
        @SuppressWarnings("unchecked")
        List<ChiTietThucDonEntity> chiTietList = (List<ChiTietThucDonEntity>) thucDonInfo.get("chiTietList");
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== THÔNG TIN THỰC ĐƠN ĐÃ TẠO ===\n\n");
        
        // Hiển thị thông tin cơ bản của thực đơn
        sb.append("ID: ").append(thucDonId).append("\n");
        sb.append("Tên thực đơn: ").append(thucDon.tenThucDon()).append("\n");
        sb.append("Số ngày: ").append(thucDon.soNgay()).append("\n\n");
        
        // Hiển thị thông tin chi tiết về nguyên liệu đã chọn
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
                        
                        MonAnEntity monAn = monAnDao.getMonAnById(chiTiet.monAnId());
                        String tenMon = monAn != null ? monAn.tenMon() : "Không có dữ liệu";
                        
                        sb.append("  ").append(loaiBua).append(": ").append(tenMon).append("\n");
                    }
                }
            }
        } else {
            sb.append("Không có dữ liệu chi tiết\n");
        }
        
        resultArea.setText(sb.toString());
    }
} 