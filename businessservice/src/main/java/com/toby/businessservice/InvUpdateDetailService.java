package com.toby.businessservice;

import com.toby.define.ScreenNameClassEnum;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.entity.InvUpdateDetail;
import com.toby.entity.TobyUser;

import javax.ejb.Remote;
import java.util.List;

/**
 * @author khaled
 */
@Remote
public interface InvUpdateDetailService {

    void addInvUpdateDetails(InvUpdateDetail details);

    void deleteInvUpdateDetails(InvUpdateDetail details);

    InvUpdateDetail updateInvUpdateDetails(InvUpdateDetail details);

    List<InvUpdateDetail> getAllInvUpdateDetails();

    List<InvUpdateDetail> getAllInvUpdateDetails(Integer id);
    
    public List<InvDetailDTO> getAllInvDetailDTOByInvUpdateDetails(Integer id) ;

    public List<InvDetailDTO> getAllInvTransferDetailsByInvPurchaseInvoiceIdDTO(InvInvoiceDTO invInvoiceDTO, Integer branchId);

    public void deleteInvUpdateDetailListDTO(List<InvDetailDTO> invDetailDTOList);
    
    public void deleteInvUpdateDetailDTO(InvDetailDTO invDetailDTO);

    public String addInvUpdateDetailsDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList, InvInvoiceDTO invInvoiceDTO, ScreenNameClassEnum screenNameClassEnum, TobyUser tobyUser);

    public InvDetailDTO addInvUpdateDetailsDTONew(InvDetailDTO detailDTO, Integer invUpdateId, TobyUser tobyUser);
    
    public List<InvDetailDTO> getAllInvUpdatesDetailFromInvTransferDetail(String invStripSerial, Integer branchId, Integer inventoryId);
    
    public List<InvDetailDTO> getAllInvUpdatesDetailFromInvTransferDetail(Integer invStripSerial, Integer branchId, Integer inventoryId);
    
    public List<InvDetailDTO> addInvUpdateDetailsDTONewList(List<InvDetailDTO> detailDTOList, Integer invUpdateId, TobyUser tobyUser);
}
