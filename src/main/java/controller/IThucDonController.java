package controller;

import dto.ThucDonDTO;
import dto.ChiTietThucDonDTO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public interface IThucDonController {

    List<ThucDonDTO> getAllThucDon();
    ThucDonDTO getThucDonById(int id);
    List<ChiTietThucDonDTO> getChiTietByThucDonId(int thucDonId);
    int generateThucDon(String tenThucDon, int soNgay, Map<String, Double> maxBudgetPerMeal);
    boolean deleteThucDon(int thucDonId);
    Map<String, Object> getThucDonWithDetails(int thucDonId);
    List<Map<String, Object>> getAllThucDonWithDetails();
} 