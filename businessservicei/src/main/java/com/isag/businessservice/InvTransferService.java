package com.isag.businessservice;

import com.isag.bussinessservice.global.InvPurchaseReturnSave;
import com.isag.dto.InvInvoiceDTO;
import com.isag.entity.InvTransfer;
import com.isag.entity.InvTransferDetail;
import com.isag.entity.IsagUser;

import javax.ejb.Remote;
import java.util.List;
import java.util.Map;

/**
 * @author khaled
 */
@Remote
public interface InvTransferService {

    InvPurchaseReturnSave addInvTransfer(InvTransfer invTransfer, List<InvTransferDetail> invTransferDetailDetailList, List<InvTransferDetail> TransferDetailDeleted,
            List<Integer> updatedInvTransferList,
            StringBuilder headIdList, Boolean isPurchaseOrSales);

    void deleteInvTransfer(InvTransfer invTransfer, Integer invTransferIdToPass);

    List<InvTransfer> getALLInvTransfer();

    List<InvTransfer> getALLInvTransferByCompanyId(Integer Id);

    List<InvTransfer> getALLInvTransferByBranchId(Integer Id);

    List<InvTransferDetail> getALLInvTransferDetailsByInvTransfer(Integer invTransferId);

    InvTransfer findInvTransferByInvTransferId(Integer invTransferId);

    Integer findMaxSerialNoByBranch(Integer branchId);
    
    public InvTransfer updateInvTransfer(InvTransfer invTransfer);
    
    public List<InvTransfer> getALLInvTransferByBranchIdAndTransferType(Integer Id,Integer transferType) ;
    
    public Long getTotalRegistors( Integer branchId,Integer transferType, Map<String, Object> filters);
    
    public List<InvInvoiceDTO> getALLInvTransferByBranchId( int first, int pageSize, String sortField, Boolean asc, Integer branchId,Integer transferType, Map<String, Object> filters);
    
    public List<InvTransfer> getALLInvTransferByBranchIdAndStatusandType(Integer branchId);
    
    public List<InvTransfer> getALLInvTransferByBranchIdAndStatus(Integer branchId);
    
     public InvTransfer addInvTransferMofakroun(InvTransfer invTransfer);
     
     public InvInvoiceDTO findInvTransferById(InvInvoiceDTO invInvoiceDTO);
     
     public InvInvoiceDTO save(InvInvoiceDTO invInvoiceDTO, IsagUser isagUser); 

}
