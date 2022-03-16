package com.isag.businessservice;

import com.isag.define.ScreenNameClassEnum;
import com.isag.dto.InvDetailDTO;
import com.isag.dto.InvInvoiceDTO;
import com.isag.entity.InvTransferDetail;
import com.isag.entity.IsagUser;

import javax.ejb.Remote;
import java.util.List;

/**
 * @author khaled
 */
@Remote
public interface InvTransferDetailService {

    void addInvTransferDetails(InvTransferDetail details);

    void deleteInvTransferDetails(InvTransferDetail details);

    InvTransferDetail updateInvTransferDetails(InvTransferDetail details);

    List<InvTransferDetail> getAllInvTransferDetails();

    List<InvTransferDetail> getAllInvTransferDetails(Integer id);
    
    InvTransferDetail findInvTransferDetailsById(Integer invTransferDetailsId);
    
   List<InvTransferDetail> getAllInvTransferDetailsByInvTransferId(Integer invTransferId) ;
   
    public List<InvTransferDetail> getAllInvTransferDetailsByInvTransferIdWithStatus(Integer invTransferId);
    
    public List<InvDetailDTO> getAllInvTransferDetailsByInvPurchaseInvoiceIdDTO(InvInvoiceDTO invPurchaseInvoiceDTO, Integer branchId);
    
    public void deleteInvTransferDetailListDTO(List<InvDetailDTO> invDetailDTOList);
    
    public String addInvTransferDetailsDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList, InvInvoiceDTO invInvoiceDTO, 
            ScreenNameClassEnum screenNameClassEnum, IsagUser isagUser);
   
}
