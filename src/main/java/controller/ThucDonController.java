package controller;

import dao.MonAnDao;
import dao.ThucDonDao;
import dao.ChiTietThucDonDao;
import dto.ThucDonDTO;
import dto.ChiTietThucDonDTO;
import entity.MonAnEntity;
import entity.ThucDonEntity;
import entity.ChiTietThucDonEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ThucDon;
import model.ChiTietThucDon;
import service.ThucDonService;

/**
 *
 * @author ADMIN
 */
public class ThucDonController implements IThucDonController {
    private static ThucDonController instance;
    private ThucDonService thucDonService;
    
    public static ThucDonController getInstance() {
        if (ThucDonController.instance == null) {
            ThucDonController.instance = new ThucDonController();
        }
        return instance;
    }

    private ThucDonController() {
        thucDonService = ThucDonService.getInstance();
    }
    
    @Override
    public List<ThucDonDTO> getAllThucDon() {
        List<ThucDon> modelList = thucDonService.getAllThucDon();
        List<ThucDonDTO> dtoList = new ArrayList<>();
        
        for (ThucDon model : modelList) {
            dtoList.add(toDto(model));
        }
        
        return dtoList;
    }
    
    @Override
    public ThucDonDTO getThucDonById(int id) {
        ThucDon model = thucDonService.getThucDonById(id);
        if (model != null) {
            return toDto(model);
        }
        return null;
    }
    
    @Override
    public List<ChiTietThucDonDTO> getChiTietByThucDonId(int thucDonId) {
        List<ChiTietThucDon> modelList = thucDonService.getChiTietByThucDonId(thucDonId);
        List<ChiTietThucDonDTO> dtoList = new ArrayList<>();
        
        for (ChiTietThucDon model : modelList) {
            MonAnEntity monAnEntity = MonAnDao.getInstance().getMonAnById(model.getMonAnId());
            String tenMon = "";
            if (monAnEntity != null) {
                tenMon = monAnEntity.tenMon();
            }
            
            ChiTietThucDonDTO dto = toDtoWithNames(model, tenMon);
            dtoList.add(dto);
        }
        
        return dtoList;
    }
    
    @Override
    public int generateThucDon(String tenThucDon, int soNgay, Map<String, Double> maxBudgetPerMeal) {
        return thucDonService.generateThucDon(tenThucDon, soNgay, maxBudgetPerMeal);
    }
    
    @Override
    public boolean deleteThucDon(int thucDonId) {
        return thucDonService.deleteThucDon(thucDonId);
    }
    
    @Override
    public Map<String, Object> getThucDonWithDetails(int thucDonId) {
        return thucDonService.getThucDonWithDetails(thucDonId);
    }
    
    @Override
    public List<Map<String, Object>> getAllThucDonWithDetails() {
        return thucDonService.getAllThucDonWithDetails();
    }
    
    private ThucDonDTO toDto(ThucDon model) {
        return new ThucDonDTO(
                model.getId(),
                model.getTenThucDon(),
                model.getSoNgay());
    }
    
    private ThucDon toModel(ThucDonDTO dto) {
        return new ThucDon(
                dto.getId(),
                dto.getTenThucDon(),
                dto.getSoNgay());
    }
    
    private ChiTietThucDonDTO toDto(ChiTietThucDon model) {
        return new ChiTietThucDonDTO(
                model.getId(),
                model.getThucDonId(),
                model.getNgay(),
                model.getBuoi(),
                model.getMonAnId());
    }
    
    private ChiTietThucDonDTO toDtoWithNames(ChiTietThucDon model, String tenMon) {
        return new ChiTietThucDonDTO(
                model.getId(),
                model.getThucDonId(),
                model.getNgay(),
                model.getBuoi(),
                model.getMonAnId(),
                tenMon);
    }
    
    private ChiTietThucDon toModel(ChiTietThucDonDTO dto) {
        return new ChiTietThucDon(
                dto.getId(),
                dto.getThucDonId(),
                dto.getNgay(),
                dto.getBuoi(),
                dto.getMonAnId());
    }
} 