package com.toby.businessservice;

import com.toby.bussinessservice.global.InvPurchaseReturnSave;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.InvTransferDTO;
import com.toby.dto.InvTransferDetailDTO;
import com.toby.entity.InvTransfer;
import com.toby.entity.InvTransferDetail;
import com.toby.entity.TobyUser;
import java.math.BigDecimal;

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

//    InvTransfer findInvTransferByInvTransferId(Integer invTransferId);
    Integer findMaxSerialNoByBranch(Integer branchId);

    public InvTransfer updateInvTransfer(InvTransfer invTransfer);

    public List<InvTransfer> getALLInvTransferByBranchIdAndTransferType(Integer Id, Integer transferType);
    
    public Long getTotalRegistors( Integer branchId,Integer transferType, Map<String, Object> filters);
    
    public List<InvInvoiceDTO> getALLInvTransferByBranchId( int first, int pageSize, String sortField, Boolean asc, Integer branchId,Integer transferType, Map<String, Object> filters);
    
    public List<InvTransfer> getALLInvTransferByBranchIdAndStatusandType(Integer branchId);

    public List<InvTransfer> getALLInvTransferByBranchIdAndStatus(Integer branchId);

    public InvTransfer addInvTransferMofakroun(InvTransfer invTransfer);

    ///////////////////////////
    public InvTransferDTO addInvTransferDTO(InvTransferDTO invTransferDTO, TobyUser tobyUser);
    
    public InvTransferDetailDTO addRow(InvTransferDTO invTransferDTO);

    public boolean validateDetailsWhenSave(InvTransferDTO invTransferDTO);

    public boolean ValidateQuantity(InvTransferDTO invTransferDTO, InvTransferDetailDTO invTransferDetailDTOSelected, Map<Integer, BigDecimal> avialableQuantityMap);

    public boolean validateChangeInventory(InvTransferDTO invTransferDTO);

    public boolean validateDetailsWhenAddRow(InvTransferDTO invTransferDTO);

    public InvTransferDTO save(InvTransferDTO invTransferDTO, TobyUser tobyUser);

    public InvTransferDTO findInvTransferByInvTransferId(Integer invTransferId);

    public InvTransferDTO findInvTransferBySerialNoAndBranch(Integer serial, TobyUser tobyUser);

    public List<Integer> getSerialNoInvTransferDTO(TobyUser tobyUser, Integer transferType);

    public List<InvTransferDTO> getALLInvTransferByBranchIdAndTransferType(TobyUser tobyUser, Integer transferType);
    
    public InvInvoiceDTO findInvTransferById(InvInvoiceDTO invInvoiceDTO);
     
     public InvInvoiceDTO save(InvInvoiceDTO invInvoiceDTO, TobyUser isagUser); 

}
