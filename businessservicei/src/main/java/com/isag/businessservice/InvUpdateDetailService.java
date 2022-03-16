package com.isag.businessservice;
import com.isag.define.ScreenNameClassEnum;
import com.isag.dto.InvDetailDTO;
import com.isag.dto.InvInvoiceDTO;
import com.isag.entity.InvUpdateDetail;
import com.isag.entity.IsagUser;
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
    
    public List<InvDetailDTO> getAllInvTransferDetailsByInvPurchaseInvoiceIdDTO(InvInvoiceDTO invInvoiceDTO, Integer branchId);
    
    public void deleteInvUpdateDetailListDTO(List<InvDetailDTO> invDetailDTOList);
    
    public String addInvUpdateDetailsDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList, InvInvoiceDTO invInvoiceDTO,ScreenNameClassEnum screenNameClassEnum,  IsagUser isagUser) ;
    
}
