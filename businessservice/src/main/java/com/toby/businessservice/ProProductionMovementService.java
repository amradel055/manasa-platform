package com.toby.businessservice;

import com.toby.dto.ProProductMovementDTO;
import com.toby.entity.TobyUser;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface ProProductionMovementService {

    public ProProductMovementDTO addProProductMovement(ProProductMovementDTO proProductMovementDTO, int type, TobyUser tobyUser);

    public List<ProProductMovementDTO> getMovementSerial();

    public ProProductMovementDTO getMovementByDelivery(ProProductMovementDTO dTO);
    
    public List<ProProductMovementDTO> getAllByType(Integer type);
    
    public void deleteBySelected(Integer id);
    
    public ProProductMovementDTO getById(Integer id);
 
}
