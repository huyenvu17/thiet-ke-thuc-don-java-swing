package controller;

import dao.IMonAnDao;
import dao.MonAnDao;
import doanthietkethucdon.BHException;
import dto.MonAnDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.MonAn;
import service.MonAnService;

/**
 *
 * @author ADMIN
 */
public class MonAnController implements IMonAnController {
    private static MonAnController instance;
    private MonAnService monAnService;
    
    public static MonAnController getInstance() {
        if (MonAnController.instance == null) {
            MonAnController.instance = new MonAnController();
        }
        return instance;
    }

    private MonAnController() {
        monAnService = MonAnService.getInstance();
        IMonAnDao monAnDao = MonAnDao.getInstance();
        monAnService.setMonAnDaoService(monAnDao);
        monAnService.loadAllMonAnFromDatabase();
    }

    @Override
    public List<MonAnDTO> getAllMonAn() {
        monAnService.loadAllMonAnFromDatabase();
        
        List<MonAnDTO> monDtoList = new ArrayList<>();
        for (MonAn model : monAnService.getDanhSachMonAn()) {
            monDtoList.add(toDto(model));
        }
        return monDtoList;
    }
    
    @Override
    public MonAnDTO getMonAnById(int id) {
        MonAn model = monAnService.getMonAnById(id);
        if (model != null) {
            return toDto(model);
        }
        return null;
    }

    @Override
    public boolean addMonAn(MonAnDTO monAnDto) {
        try {
            boolean result = monAnService.addMonAn(toModel(monAnDto));
            if (result) {
                monAnService.loadAllMonAnFromDatabase();
            }
            return result;
        } catch (BHException ex) {
            Logger.getLogger(MonAnService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    @Override
    public boolean updateMonAn(MonAnDTO monAnDto) {
        try {
            boolean result = monAnService.updateMonAn(toModel(monAnDto));
            if (result) {
                monAnService.loadAllMonAnFromDatabase();
            }
            return result;
        } catch (BHException ex) {
            Logger.getLogger(MonAnService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    @Override
    public boolean deleteMonAn(int id) {
        boolean result = monAnService.deleteMonAn(id);
        if (result) {
            monAnService.loadAllMonAnFromDatabase();
        }
        return result;
    }
    
    private MonAn toModel(MonAnDTO dto) throws BHException {
        return new MonAn(
                dto.getId(),
                dto.getTenMon(),
                dto.getLoaiMon(),
                dto.getCachCheBien());
    }

    private MonAnDTO toDto(MonAn model) {
        return new MonAnDTO(
                model.getId(),
                model.getTenMon(),
                model.getLoaiMon(),
                model.getCachCheBien());
    }
} 