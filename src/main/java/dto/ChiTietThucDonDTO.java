package dto;

/**
 *
 * @author ADMIN
 */
public class ChiTietThucDonDTO {
    private int id;
    private int thucDonId;
    private int ngay;
    private String buoi;
    private int monAnId;
    private String tenMon;

    public ChiTietThucDonDTO() {
    }

    public ChiTietThucDonDTO(int id, int thucDonId, int ngay, String buoi, int monAnId, String tenMon) {
        this.id = id;
        this.thucDonId = thucDonId;
        this.ngay = ngay;
        this.buoi = buoi;
        this.monAnId = monAnId;
        this.tenMon = tenMon;
    }

    public ChiTietThucDonDTO(int id, int thucDonId, int ngay, String buoi, int monAnId) {
        this.id = id;
        this.thucDonId = thucDonId;
        this.ngay = ngay;
        this.buoi = buoi;
        this.monAnId = monAnId;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getThucDonId() {
        return thucDonId;
    }

    public void setThucDonId(int thucDonId) {
        this.thucDonId = thucDonId;
    }

    public int getNgay() {
        return ngay;
    }

    public void setNgay(int ngay) {
        this.ngay = ngay;
    }

    public String getBuoi() {
        return buoi;
    }

    public void setBuoi(String buoi) {
        this.buoi = buoi;
    }

    public int getMonAnId() {
        return monAnId;
    }

    public void setMonAnId(int monAnId) {
        this.monAnId = monAnId;
    }
    
    public String getTenMon() {
        return tenMon;
    }
    
    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }
    
    @Override
    public String toString() {
        return "Ngày " + ngay + " - " + formatBuoi(buoi) + " - " + tenMon;
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
} 