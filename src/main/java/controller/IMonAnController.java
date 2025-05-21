package controller;

import dto.MonAnDTO;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface IMonAnController {

    List<MonAnDTO> getAllMonAn();
    MonAnDTO getMonAnById(int id);
    boolean addMonAn(MonAnDTO monAnDto);
    boolean updateMonAn(MonAnDTO monAnDto);
    boolean deleteMonAn(int id);
} 