package com.toby.businessservice;

import com.toby.dto.InvInventoryTransactionDetailDTO;
import com.toby.dto.InvPurchaseInvoiceDTO1;
import com.toby.dto.InvPurchaseInvoiceDetailDTO;
import com.toby.dto.PrintProductionDTO;
import com.toby.dto.ProProductMovementDetailDTO;
import com.toby.dto.ProProductionStagesDTO;
import com.toby.dto.ProProductionTransactionDTO;
import com.toby.entity.TobyUser;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

@Remote
public interface ProProductionTransactionService {

    //public List<ProProductionTransactionDTO> getAllInvPurchaseInvoiceById(Integer stageId, TobyUser tobyUser);
    public void save(ProProductionTransactionDTO proProductionTransactionDTOSelected, int status, TobyUser tobyUser);

    public Integer getAllStagesByInvoice(Integer invoice, TobyUser tobyUser);

    public Integer getAllStagesByInvoiceFromView(Integer invoice, TobyUser tobyUser);

    public void finished(ProProductionTransactionDTO proProductionTransactionDTOSelected, TobyUser tobyUser);

    public void findProProductionTransaction(int id, ProProductionTransactionDTO proProductionTransactionDTOSelected, TobyUser tobyUser);

    public List<ProProductMovementDetailDTO> getInvPurchesInvoiceID(TobyUser tobyUser);

    public List<InvPurchaseInvoiceDetailDTO> getInvoiceDetailById(TobyUser tobyUser, int id);

    public List<InvInventoryTransactionDetailDTO> getInventoryDetailById(TobyUser tobyUser, int id);

    public List<ProProductionTransactionDTO> getInvPurchaseInvoiceById(TobyUser tobyUser, int proProductionId);

    public void deleteProProductionTransaction(ProProductionTransactionDTO productionTransactionDTO, TobyUser tobyUser);

    public List<ProProductionTransactionDTO> getAll(TobyUser tobyUser);

    public List<ProProductionTransactionDTO> findBy(PrintProductionDTO productionDTO, Integer branchId);

    public ProProductionStagesDTO findByProProduction(ProProductionTransactionDTO dTO);

    public List<InvPurchaseInvoiceDTO1> getAllInvoiceForFinalCheck(TobyUser tobyUser);

    public List<InvPurchaseInvoiceDTO1> getAllInvoiceForFinalCheckNew(TobyUser tobyUser);

    public Integer getAllInvoiceByItem(Integer item, TobyUser tobyUser);

    public Integer getAllItemsByInvoice(Integer invoice, TobyUser tobyUser);

    public Integer getAllItemsByInvoiceFromView(Integer invoice, TobyUser tobyUser);

    public Integer getAllStagesByItem(Integer item, TobyUser tobyUser);

    public List<ProProductionTransactionDTO> getAllCheckedInvoicePurchase(TobyUser tobyUser);

    public ProProductionTransactionDTO getProductionTransactionById(Integer id, TobyUser tobyUser);

    public Boolean checkFinish(Integer invPurchaseInvoiceId, Integer proFactoryDeliveryId, Integer clothNumber);

    public List<ProProductionTransactionDTO> getProProductionTransactionDTOofViewList(ProProductionStagesDTO productionStages, TobyUser tobyUser);

    public List<InvPurchaseInvoiceDetailDTO> getInvPurchaseInvoiceDetailsByPurchaseId(Integer invPurchaseInvoiceId, TobyUser tobyUser);

    public List<ProProductionTransactionDTO> getInvPurchaseInvoiceByIdByView(TobyUser tobyUser, int proProductionId);

    public Long getTotalRegistors(Integer branchId, Map<String, Object> filters);

    public List<ProProductionTransactionDTO> getALLInvPurchaseInvoiceDTOByTypeAndBranchId(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters);

}
