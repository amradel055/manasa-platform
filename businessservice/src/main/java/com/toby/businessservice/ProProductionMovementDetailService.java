package com.toby.businessservice;

import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.ProProductMovementDTO;
import com.toby.dto.ProProductMovementDetailDTO;
import com.toby.entity.TobyUser;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface ProProductionMovementDetailService {
    
    public List<ProProductMovementDetailDTO> addProProductMovementDetailList(List<ProProductMovementDetailDTO> proProductMovementDetailDTOList, Integer id, TobyUser tobyUser);

    public List<ProProductMovementDetailDTO> getDetailsBySerial(Integer id);
    
    public List<ProProductMovementDetailDTO> getDetailsByDelivery(Integer deliveryId);

    public ProProductMovementDTO findByParent(ProProductMovementDTO dTO);

    public List<ProProductMovementDetailDTO> getinpurchaseinvoice(TobyUser tobyUser);
    
    public List<ProProductMovementDetailDTO> getAllInvPurchaseInvoice(TobyUser tobyUser,ProProductMovementDTO movementDTO);
    
    public List<ProProductMovementDetailDTO> getAllInvoiceFromGalaryNotInClient(TobyUser tobyUser);
    
    public List<ProProductMovementDetailDTO> getAllInvoiceByClient(TobyUser tobyUser,InvOrganizationSiteDTO dTO);
    
    public List<ProProductMovementDetailDTO> getAllByType(Integer type);
    
    public void deleteByInvoice(ProProductMovementDetailDTO inv);
    
    public void deleteBySelected(ProProductMovementDetailDTO inv);
    
    
    
}
