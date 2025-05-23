package dto;

import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public class ThietKeThucDonDTO {
    private String tenThucDon;
    private int soNgay;
    private double budgetSang;
    private double budgetTrua;
    private double budgetXe;
    private List<Integer> selectedNguyenLieuIds;
    
    // Default constructor
    public ThietKeThucDonDTO() {
    }

    public ThietKeThucDonDTO(String tenThucDon, int soNgay, double budgetSang, double budgetTrua, double budgetXe, List<Integer> selectedNguyenLieuIds) {
        this.tenThucDon = tenThucDon;
        this.soNgay = soNgay;
        this.budgetSang = budgetSang;
        this.budgetTrua = budgetTrua;
        this.budgetXe = budgetXe;
        this.selectedNguyenLieuIds = selectedNguyenLieuIds;
    }
    
    // Getters and Setters
    public String getTenThucDon() {
        return tenThucDon;
    }

    public void setTenThucDon(String tenThucDon) {
        this.tenThucDon = tenThucDon;
    }

    public int getSoNgay() {
        return soNgay;
    }

    public void setSoNgay(int soNgay) {
        this.soNgay = soNgay;
    }

    public double getBudgetSang() {
        return budgetSang;
    }

    public void setBudgetSang(double budgetSang) {
        this.budgetSang = budgetSang;
    }

    public double getBudgetTrua() {
        return budgetTrua;
    }

    public void setBudgetTrua(double budgetTrua) {
        this.budgetTrua = budgetTrua;
    }

    public double getBudgetXe() {
        return budgetXe;
    }

    public void setBudgetXe(double budgetXe) {
        this.budgetXe = budgetXe;
    }

    public List<Integer> getSelectedNguyenLieuIds() {
        return selectedNguyenLieuIds;
    }

    public void setSelectedNguyenLieuIds(List<Integer> selectedNguyenLieuIds) {
        this.selectedNguyenLieuIds = selectedNguyenLieuIds;
    }

    public Map<String, Double> getBudgetMap() {
        return Map.of(
            "sang", budgetSang,
            "trua", budgetTrua,
            "xe", budgetXe
        );
    }
} 