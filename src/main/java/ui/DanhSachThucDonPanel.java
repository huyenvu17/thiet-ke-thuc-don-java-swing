package ui;

import controller.IThucDonController;
import controller.ThucDonController;
import dto.ThucDonDTO;
import dto.ChiTietThucDonDTO;
import entity.ChiTietThucDonEntity;
import entity.ThucDonEntity;
import entity.UserEntity;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Panel Danh Sách Thực Đơn
 */
public class DanhSachThucDonPanel extends JPanel {
    
    private final IThucDonController thucDonController;
    private JTable thucDonTable;
    private DefaultTableModel thucDonModel;
    private JTable chiTietTable;
    private DefaultTableModel chiTietModel;
    private JButton deleteButton;
    private JButton printButton;
    private JButton refreshButton;
    private UserEntity currentUserEntity;
    
    public DanhSachThucDonPanel(UserEntity userEntity) {
        this.currentUserEntity = userEntity;
        this.thucDonController = ThucDonController.getInstance();
        initComponents();
        loadThucDonList();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        JLabel titleLabel = new JLabel("DANH SÁCH THỰC ĐƠN", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5);

        JPanel thucDonPanel = new JPanel(new BorderLayout());
        thucDonPanel.setBorder(BorderFactory.createTitledBorder("Danh sách thực đơn"));
        
        thucDonModel = new DefaultTableModel(
                new String[]{"ID", "Tên thực đơn", "Số ngày"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        thucDonTable = new JTable(thucDonModel);
        thucDonTable.getColumnModel().getColumn(0).setMinWidth(40);
        thucDonTable.getColumnModel().getColumn(0).setMaxWidth(60);
        thucDonTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        JScrollPane thucDonScrollPane = new JScrollPane(thucDonTable);
        thucDonPanel.add(thucDonScrollPane, BorderLayout.CENTER);

        JPanel thucDonButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        deleteButton = new JButton("Xóa");
        printButton = new JButton("In thực đơn");
        refreshButton = new JButton("Làm mới");
        
        deleteButton.setEnabled(false);
        printButton.setEnabled(false);
        
        if(currentUserEntity != null && !currentUserEntity.getRole().equals("admin")) {
            thucDonButtonPanel.add(refreshButton);
            thucDonButtonPanel.add(printButton);
        }
        else {
            thucDonButtonPanel.add(refreshButton);
            thucDonButtonPanel.add(deleteButton);
            thucDonButtonPanel.add(printButton);
        }
        thucDonPanel.add(thucDonButtonPanel, BorderLayout.SOUTH);

        JPanel chiTietPanel = new JPanel(new BorderLayout());
        chiTietPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết thực đơn"));
        
        chiTietModel = new DefaultTableModel(
                new String[]{"Ngày", "Bữa", "Món ăn"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        chiTietTable = new JTable(chiTietModel);
        JScrollPane chiTietScrollPane = new JScrollPane(chiTietTable);
        chiTietPanel.add(chiTietScrollPane, BorderLayout.CENTER);
        
        // Add the panels to the split pane
        splitPane.setTopComponent(thucDonPanel);
        splitPane.setBottomComponent(chiTietPanel);
        
        add(splitPane, BorderLayout.CENTER);
        
        // Add listeners
        thucDonTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = thucDonTable.getSelectedRow();
                if (selectedRow != -1) {
                    int thucDonId = (int) thucDonModel.getValueAt(selectedRow, 0);
                    loadChiTietThucDon(thucDonId);
                    deleteButton.setEnabled(true);
                    printButton.setEnabled(true);
                } else {
                    chiTietModel.setRowCount(0);
                    deleteButton.setEnabled(false);
                    printButton.setEnabled(false);
                }
            }
        });
        
        deleteButton.addActionListener(e -> deleteSelectedThucDon());
        printButton.addActionListener(e -> printSelectedThucDon());
        refreshButton.addActionListener(e -> loadThucDonList());
    }
    
    private void loadThucDonList() {
        thucDonModel.setRowCount(0);
        List<Map<String, Object>> thucDonList = thucDonController.getAllThucDonWithDetails();
        
        for (Map<String, Object> item : thucDonList) {
            ThucDonEntity thucDon = (ThucDonEntity) item.get("thucDon");
            Object[] row = {
                    thucDon.id(),
                    thucDon.tenThucDon(),
                    thucDon.soNgay()
            };
            thucDonModel.addRow(row);
        }
        
        // Clear chi tiet table when loading new data
        chiTietModel.setRowCount(0);
    }
    
    private void loadChiTietThucDon(int thucDonId) {
        chiTietModel.setRowCount(0);
        Map<String, Object> thucDonDetails = thucDonController.getThucDonWithDetails(thucDonId);
        
        if (thucDonDetails.isEmpty()) {
            return;
        }
        
        @SuppressWarnings("unchecked")
        List<ChiTietThucDonEntity> chiTietList = (List<ChiTietThucDonEntity>) thucDonDetails.get("chiTietList");
        
        for (ChiTietThucDonEntity chiTiet : chiTietList) {
            // Format the meal type
            String buoi = formatBuoi(chiTiet.buoi());
            
            Object[] row = {
                    chiTiet.ngay(),
                    buoi,
                    chiTiet.tenMon()
            };
            chiTietModel.addRow(row);
        }
    }
    
    private String formatBuoi(String buoi) {
        switch (buoi) {
            case "sang":
                return "Bữa sáng";
            case "trua":
                return "Bữa trưa";
            case "xe":
                return "Bữa tối";
            default:
                return buoi;
        }
    }
    
    private void deleteSelectedThucDon() {
        int selectedRow = thucDonTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một thực đơn để xóa", 
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int thucDonId = (int) thucDonModel.getValueAt(selectedRow, 0);
        String tenThucDon = (String) thucDonModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn xóa thực đơn '" + tenThucDon + "'?", 
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = thucDonController.deleteThucDon(thucDonId);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Đã xóa thực đơn thành công!");
                loadThucDonList();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa thực đơn. Vui lòng thử lại.", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void printSelectedThucDon() {
        int selectedRow = thucDonTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một thực đơn để in", 
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int thucDonId = (int) thucDonModel.getValueAt(selectedRow, 0);
        String tenThucDon = (String) thucDonModel.getValueAt(selectedRow, 1);
        
        Map<String, Object> thucDonDetails = thucDonController.getThucDonWithDetails(thucDonId);
        
        if (thucDonDetails.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không thể tìm thấy chi tiết thực đơn", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        ThucDonEntity thucDon = (ThucDonEntity) thucDonDetails.get("thucDon");
        @SuppressWarnings("unchecked")
        List<ChiTietThucDonEntity> chiTietList = (List<ChiTietThucDonEntity>) thucDonDetails.get("chiTietList");
        
        // Create a preview dialog with a formatted text area
        JDialog printPreviewDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), 
                "Xem trước bản in: " + tenThucDon, true);
        printPreviewDialog.setLayout(new BorderLayout());
        
        JTextArea previewArea = new JTextArea();
        previewArea.setEditable(false);
        previewArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        // Format the preview content
        StringBuilder content = new StringBuilder();
        content.append("THỰC ĐƠN: ").append(thucDon.tenThucDon()).append("\n");
        content.append("Số ngày: ").append(thucDon.soNgay()).append("\n\n");
        
        // Group by day
        for (int day = 1; day <= thucDon.soNgay(); day++) {
            content.append("Ngày ").append(day).append(":\n");
            
            // Create a final copy of day for use in lambda
            final int currentDay = day;
            
            // Get meals for this day
            List<ChiTietThucDonEntity> mealsForDay = chiTietList.stream()
                    .filter(chiTiet -> chiTiet.ngay() == currentDay)
                    .sorted((a, b) -> a.buoi().compareTo(b.buoi()))
                    .toList();
            
            if (mealsForDay.isEmpty()) {
                content.append("  Không có món ăn\n");
            } else {
                for (ChiTietThucDonEntity meal : mealsForDay) {
                    content.append("  ")
                            .append(formatBuoi(meal.buoi()))
                            .append(": ")
                            .append(meal.tenMon())
                            .append("\n");
                }
            }
            
            content.append("\n");
        }
        
        previewArea.setText(content.toString());
        
        JScrollPane scrollPane = new JScrollPane(previewArea);
        printPreviewDialog.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton printButton = new JButton("In");
        JButton closeButton = new JButton("Đóng");
        
        printButton.addActionListener(e -> {
            try {
                previewArea.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(printPreviewDialog, 
                        "Lỗi khi in: " + ex.getMessage(), 
                        "Lỗi in", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        closeButton.addActionListener(e -> printPreviewDialog.dispose());
        
        buttonPanel.add(printButton);
        buttonPanel.add(closeButton);
        printPreviewDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        printPreviewDialog.setSize(500, 600);
        printPreviewDialog.setLocationRelativeTo(this);
        printPreviewDialog.setVisible(true);
    }
} 