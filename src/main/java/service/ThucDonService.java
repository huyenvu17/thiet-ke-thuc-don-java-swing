package service;

import dao.MonAnDao;
import dao.ThucDonDao;
import dao.ChiTietThucDonDao;
import dao.CongThucMonAnDao;
import doanthietkethucdon.BHException;
import entity.MonAnEntity;
import entity.ThucDonEntity;
import entity.ChiTietThucDonEntity;
import entity.CongThucMonAnEntity;
import model.ThucDon;
import model.ChiTietThucDon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author ADMIN
 */
public class ThucDonService {
    private static ThucDonService instance;
    private final MonAnDao monAnDao;
    private final ThucDonDao thucDonDao;
    private final ChiTietThucDonDao chiTietThucDonDao;
    private final CongThucMonAnDao congThucMonAnDao;
    private final Random random;
    private final List<ThucDon> danhSachThucDon;
    
    public static ThucDonService getInstance() {
        if (ThucDonService.instance == null) {
            ThucDonService.instance = new ThucDonService();
        }
        return instance;
    }
    
    /**
     * Constructor
     */
    private ThucDonService() {
        this.monAnDao = MonAnDao.getInstance();
        this.thucDonDao = ThucDonDao.getInstance();
        this.chiTietThucDonDao = ChiTietThucDonDao.getInstance();
        this.congThucMonAnDao = CongThucMonAnDao.getInstance();
        this.random = new Random();
        this.danhSachThucDon = new ArrayList<>();
    }

    public List<ThucDon> getAllThucDon() {
        loadAllThucDonFromDatabase();
        return danhSachThucDon;
    }

    public void loadAllThucDonFromDatabase() {
        this.danhSachThucDon.clear();
        List<ThucDonEntity> entityList = this.thucDonDao.getAllThucDon();
        for (ThucDonEntity entity : entityList) {
            try {
                this.danhSachThucDon.add(toModel(entity));
            } catch (Exception ex) {
                Logger.getLogger(ThucDonService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public ThucDon getThucDonById(int id) {
        ThucDonEntity entity = this.thucDonDao.getThucDonById(id);
        if (entity != null) {
            try {
                return toModel(entity);
            } catch (Exception ex) {
                Logger.getLogger(ThucDonService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public List<ChiTietThucDon> getChiTietByThucDonId(int thucDonId) {
        List<ChiTietThucDon> result = new ArrayList<>();
        List<ChiTietThucDonEntity> entityList = this.chiTietThucDonDao.getByThucDonId(thucDonId);
        for (ChiTietThucDonEntity entity : entityList) {
            try {
                result.add(toModel(entity));
            } catch (Exception ex) {
                Logger.getLogger(ThucDonService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * generateThucDon
     * 
     * @param tenThucDon
     * @param soNgay
     * @param maxBudgetPerMeal
     * @param selectedNguyenLieuIds
     */
    public int generateThucDon(String tenThucDon, int soNgay, Map<String, Double> maxBudgetPerMeal, List<Integer> selectedNguyenLieuIds) {
        List<MonAnEntity> allMonAn = monAnDao.getAllMonAn();

        List<MonAnEntity> filteredMonAn = allMonAn;
        if (selectedNguyenLieuIds != null && !selectedNguyenLieuIds.isEmpty()) {
            filteredMonAn = filterMonAnByNguyenLieu(allMonAn, selectedNguyenLieuIds);

            if (filteredMonAn.isEmpty()) {
                return -2; // không có món ăn phù hợp
            }
        }
        
        // Kiểm tra đủ món cho mỗi loại bữa
        Map<String, List<MonAnEntity>> monAnByType = filteredMonAn.stream()
                .collect(Collectors.groupingBy(MonAnEntity::loaiMon));

        String[] mealTypes = {"sang", "trua", "xe"};
        for (String mealType : mealTypes) {
            if (monAnByType.getOrDefault(mealType, new ArrayList<>()).size() < 2) {
                return -3; // Cần ít nhất 2 món cho mỗi loại bữa
            }
        }
        
        ThucDon thucDon = new ThucDon(0, tenThucDon, soNgay);
        int thucDonId = thucDonDao.addThucDon(toEntity(thucDon));
        
        if (thucDonId <= 0) {
            return -1; // Failed
        }
        
        boolean success = true;
        
        Map<String, List<MonAnEntity>> selectedDishes = new HashMap<>();
        for (String mealType : mealTypes) {
            selectedDishes.put(mealType, new ArrayList<>());
        }
        
        for (int day = 1; day <= soNgay; day++) {
            for (String mealType : mealTypes) {
                List<MonAnEntity> availableDishes = monAnByType.getOrDefault(mealType, new ArrayList<>());
                
                Double maxBudget = maxBudgetPerMeal.getOrDefault(mealType, Double.MAX_VALUE);
                List<MonAnEntity> affordableDishes = filterByBudget(availableDishes, maxBudget);
                
                if (affordableDishes.isEmpty()) {
                    affordableDishes = availableDishes;
                }
                
                MonAnEntity selectedDish = selectDishForMeal(affordableDishes, day, mealType, selectedDishes.get(mealType));
                
                if (selectedDish == null) {
                    continue;
                }
                
                selectedDishes.get(mealType).add(selectedDish);
                
                ChiTietThucDon chiTiet = new ChiTietThucDon(
                        0, 
                        thucDonId, 
                        day, 
                        mealType, 
                        selectedDish.id()
                );
                
                int chiTietId = chiTietThucDonDao.addChiTietThucDon(toEntity(chiTiet));
                
                if (chiTietId <= 0) {
                    success = false;
                    break;
                }
            }
            
            if (!success) {
                break;
            }
        }
        
        if (!success) {
            chiTietThucDonDao.deleteByThucDonId(thucDonId);
            thucDonDao.deleteThucDon(thucDonId);
            return -1;
        }
        
        loadAllThucDonFromDatabase();
        return thucDonId;
    }

    private List<MonAnEntity> filterMonAnByNguyenLieu(List<MonAnEntity> allMonAn, List<Integer> selectedNguyenLieuIds) {
        List<MonAnEntity> filteredMonAn = new ArrayList<>();
        Map<MonAnEntity, Integer> matchCount = new HashMap<>();
        
        for (MonAnEntity monAn : allMonAn) {
            // Lấy công thức của món ăn
            List<CongThucMonAnEntity> congThuc = congThucMonAnDao.getCongThucByMonAnId(monAn.id());
            
            // Đếm số nguyên liệu trùng khớp
            int count = 0;
            for (CongThucMonAnEntity ct : congThuc) {
                if (selectedNguyenLieuIds.contains(ct.nguyenLieuId())) {
                    count++;
                }
            }
            
            // Thêm món ăn vào danh sách nếu có ít nhất một nguyên liệu đã chọn
            if (count > 0) {
                filteredMonAn.add(monAn);
                matchCount.put(monAn, count);
            }
        }
        
        // Sắp xếp danh sách để ưu tiên món có nhiều nguyên liệu trùng khớp hơn
        filteredMonAn.sort((a, b) -> matchCount.get(b) - matchCount.get(a));
        
        return filteredMonAn;
    }

    private List<MonAnEntity> filterByBudget(List<MonAnEntity> monAnList, Double maxBudget) {
        return new ArrayList<>(monAnList);
    }

    private MonAnEntity pickRandomDish(List<MonAnEntity> dishes) {
        int randomIndex = random.nextInt(dishes.size());
        return dishes.get(randomIndex);
    }

    private MonAnEntity selectDishForMeal(List<MonAnEntity> dishes, int day, String mealType, List<MonAnEntity> selectedDishes) {
        if (dishes.isEmpty()) return null;
        
        // Tạo bản sao danh sách món ăn để không ảnh hưởng đến danh sách gốc
        List<MonAnEntity> candidates = new ArrayList<>(dishes);
        
        // Loại bỏ các món đã được chọn gần đây để tránh trùng lặp
        if (!selectedDishes.isEmpty() && candidates.size() > 1) {
            // Lấy tối đa 2 món đã chọn gần nhất
            int recentDishCount = Math.min(2, selectedDishes.size());
            List<MonAnEntity> recentDishes = selectedDishes.subList(
                selectedDishes.size() - recentDishCount, selectedDishes.size());
                
            // Loại bỏ các món đã chọn gần đây, nhưng đảm bảo vẫn còn ít nhất một món
            candidates.removeAll(recentDishes);
            if (candidates.isEmpty()) {
                candidates = new ArrayList<>(dishes);
            }
        }
        
        // Nếu chỉ còn lại một món, trả về món đó
        if (candidates.size() == 1) {
            return candidates.get(0);
        }
        
        // Chọn ngẫu nhiên từ các món phù hợp nhất (top 3 hoặc ít hơn nếu không đủ)
        int topN = Math.min(3, candidates.size());
        List<MonAnEntity> topDishes = candidates.subList(0, topN);
        return topDishes.get(random.nextInt(topN));
    }

    public Map<String, Object> getThucDonWithDetails(int thucDonId) {
        Map<String, Object> result = new HashMap<>();
        
        ThucDonEntity thucDon = thucDonDao.getThucDonById(thucDonId);
        if (thucDon == null) {
            return result; // Empty result if menu not found
        }
        
        List<ChiTietThucDonEntity> chiTietList = chiTietThucDonDao.getByThucDonId(thucDonId);
        
        result.put("thucDon", thucDon);
        result.put("chiTietList", chiTietList);
        
        return result;
    }

    public List<Map<String, Object>> getAllThucDonWithDetails() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        List<ThucDonEntity> thucDonList = thucDonDao.getAllThucDon();
        
        for (ThucDonEntity thucDon : thucDonList) {
            Map<String, Object> thucDonMap = new HashMap<>();
            List<ChiTietThucDonEntity> chiTietList = chiTietThucDonDao.getByThucDonId(thucDon.id());
            
            thucDonMap.put("thucDon", thucDon);
            thucDonMap.put("chiTietList", chiTietList);
            
            result.add(thucDonMap);
        }
        
        return result;
    }

    public boolean deleteThucDon(int thucDonId) {
        boolean detailsDeleted = chiTietThucDonDao.deleteByThucDonId(thucDonId);
        boolean menuDeleted = thucDonDao.deleteThucDon(thucDonId);
        
        if (detailsDeleted && menuDeleted) {
            loadAllThucDonFromDatabase();
        }
        
        return detailsDeleted && menuDeleted;
    }

    public ThucDon toModel(ThucDonEntity entity) {
        return new ThucDon(
                entity.id(),
                entity.tenThucDon(),
                entity.soNgay());
    }

    public ThucDonEntity toEntity(ThucDon model) {
        return new ThucDonEntity(
                model.getId(),
                model.getTenThucDon(),
                model.getSoNgay());
    }

    public ChiTietThucDon toModel(ChiTietThucDonEntity entity) {
        return new ChiTietThucDon(
                entity.id(),
                entity.thucDonId(),
                entity.ngay(),
                entity.buoi(),
                entity.monAnId());
    }

    public ChiTietThucDonEntity toEntity(ChiTietThucDon model) {
        return new ChiTietThucDonEntity(
                model.getId(),
                model.getThucDonId(),
                model.getNgay(),
                model.getBuoi(),
                model.getMonAnId());
    }

    public int generateThucDon(String tenThucDon, int soNgay, Map<String, Double> maxBudgetPerMeal) {
        return generateThucDon(tenThucDon, soNgay, maxBudgetPerMeal, new ArrayList<>());
    }
} 