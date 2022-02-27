/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.InvInventoryTransactionDTO;
import com.toby.dto.InvInventoryTransactionDetailDTO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.InvPurchaseInvoiceDTO1;
import com.toby.dto.InvPurchaseInvoiceDetailDTO;
import com.toby.dto.InvPurchaseOrderDetailDTO;
import com.toby.dto.SymbolDTO;
import com.toby.entity.Branch;
import com.toby.entity.InvInventoryTransaction;
import com.toby.entity.InvInventoryTransactionDetail;
import com.toby.entity.InvItem;
import com.toby.entity.InvPurchaseInvoiceDetail;
import com.toby.entity.Symbol;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import com.toby.entity.UnitsItems;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author user4
 */
@Stateless
public class InvInventoryTransactionDetailServiceImpl implements InvInventoryTransactionDetailService {

    @EJB
    GenericDAO dao;

    @EJB
    UnitsItemsService unitsItemsService;

    @Override
    public List<InvInventoryTransactionDetailDTO> savainvInventoryTransactionDetailDTO(List<InvInventoryTransactionDetailDTO> invInventoryTransactionDetailDTOList, Integer invInventoryTransactionDTOId, TobyUser tobyUser) {
        List<UnitsItems> detailsItem = unitsItemsService.getUnitsItemsList();

        InvPurchaseInvoiceDetailDTO invPurchaseInvoiceDetailDTO = new InvPurchaseInvoiceDetailDTO();
        List<InvInventoryTransactionDetailDTO> details = new ArrayList<>();
        for (InvInventoryTransactionDetailDTO inventoryTransactionDetailDTO : invInventoryTransactionDetailDTOList) {
            for (UnitsItems unitsItems : detailsItem) {
                if ((inventoryTransactionDetailDTO.getItemId().getId() == unitsItems.getItemId()) && (inventoryTransactionDetailDTO.getUnitId().getId() == unitsItems.getItemId())) {

                    inventoryTransactionDetailDTO.setScrewing(unitsItems.getScrewing());
                    break;
                }
            }
            InvInventoryTransactionDTO invInventoryTransactionDTO = new InvInventoryTransactionDTO();
            invInventoryTransactionDTO.setId(invInventoryTransactionDTOId);
            inventoryTransactionDetailDTO.setInventoryTransactionId(invInventoryTransactionDTO);
            InvInventoryTransactionDetail invInventoryTransactionDetail = mapToEntity(inventoryTransactionDetailDTO, tobyUser);
            invInventoryTransactionDetail = dao.updateEntity(invInventoryTransactionDetail);
            InvInventoryTransactionDetailDTO invInventoryTransactionDetailDTO1 = mapToDTO(invInventoryTransactionDetail);

            details.add(invInventoryTransactionDetailDTO1);
        }
        return details;
    }

    public InvInventoryTransactionDetail mapToEntity(InvInventoryTransactionDetailDTO invInventoryTransactionDetailDTO, TobyUser tobyUser) {
        InvInventoryTransactionDetail invInventoryTransactionDetail = new InvInventoryTransactionDetail();
        invInventoryTransactionDetail.setFinalQuantity(invInventoryTransactionDetailDTO.getFinalQuantity());
        invInventoryTransactionDetail.setId(invInventoryTransactionDetailDTO.getId());
        invInventoryTransactionDetail.setCreationDate(invInventoryTransactionDetailDTO.getCreatedDate());
        invInventoryTransactionDetail.setItemBarcode(invInventoryTransactionDetailDTO.getItemBarcode());
        invInventoryTransactionDetail.setMarkDisable(invInventoryTransactionDetailDTO.getMarkDisable());
        invInventoryTransactionDetail.setMarkEdit(invInventoryTransactionDetailDTO.getMarkEdit());
        invInventoryTransactionDetail.setMsg(invInventoryTransactionDetailDTO.getMsg());
        invInventoryTransactionDetail.setQuantity(invInventoryTransactionDetailDTO.getQuantity());
        invInventoryTransactionDetail.setScrewing(BigDecimal.ONE);
        invInventoryTransactionDetail.setSerial(invInventoryTransactionDetailDTO.getSerial());
        invInventoryTransactionDetail.setStatus(invInventoryTransactionDetailDTO.getStatus());
        invInventoryTransactionDetail.setIsdeleted(invInventoryTransactionDetailDTO.getIsdeleted());
        invInventoryTransactionDetail.setTransferFrom(invInventoryTransactionDetailDTO.getTransferFrom());
        if (invInventoryTransactionDetailDTO.getItemId() != null) {
            InvItem invItem = new InvItem();
            invItem.setId(invInventoryTransactionDetailDTO.getItemId().getId());
            invInventoryTransactionDetail.setItemId(invItem);

        }
//        if(invInventoryTransactionDetailDTO.getItemId().getUnitId() !=null){
//         Symbol symbol = new Symbol();
////        symbol.setId(invInventoryTransactionDetail.getItemId().getUnitId().getId());
//        symbol.setName(invInventoryTransactionDetail.getItemId().getUnitId().getName());
//          invInventoryTransactionDetail.setUnitId(symbol);
//        
//        }
        if (invInventoryTransactionDetailDTO.getUnitId() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invInventoryTransactionDetailDTO.getUnitId().getId());
            symbol.setName(invInventoryTransactionDetailDTO.getUnitId().getName());
            invInventoryTransactionDetail.setUnitId(symbol);
        }

        if (invInventoryTransactionDetailDTO.getInventoryTransactionId() != null) {
            InvInventoryTransaction invInventoryTransaction = new InvInventoryTransaction();
            invInventoryTransaction.setId(invInventoryTransactionDetailDTO.getInventoryTransactionId().getId());
            invInventoryTransactionDetail.setInventoryTransactionId(invInventoryTransaction);
        }
        if (invInventoryTransactionDetailDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(invInventoryTransactionDetailDTO.getCreatedBy());
            invInventoryTransactionDetail.setCreatedBy(user);
            invInventoryTransactionDetail.setCreationDate(invInventoryTransactionDetailDTO.getCreatedDate());
            invInventoryTransactionDetail.setModifiedBy(tobyUser);
            invInventoryTransactionDetail.setModificationDate(new Date());
            if (invInventoryTransactionDetailDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(invInventoryTransactionDetailDTO.getCompanyId());
                invInventoryTransactionDetail.setCompanyId(company);
            }

            if (invInventoryTransactionDetailDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(invInventoryTransactionDetailDTO.getBranchId());
                invInventoryTransactionDetail.setBranchId(branch);
            }
        } else {
            invInventoryTransactionDetail.setCreatedBy(tobyUser);
            invInventoryTransactionDetail.setCreationDate(new Date());
            invInventoryTransactionDetail.setCompanyId(tobyUser.getCompanyId());
            invInventoryTransactionDetail.setBranchId(tobyUser.getBranchId());
        }
//         Map<String, Object> Gserial = new HashMap<>();
//        Gserial.put("branchId", tobyUser.getBranchId().getId());
//        Gserial.put("inventoryTransactionId", invInventoryTransactionDetailDTO.getInventoryTransactionId().getId());
//        
//        Integer S = 0;
//        synchronized (S) {
//            try {
//                S = dao.findEntityByQuery("SELECT MAX(e.serial) FROM InvInventoryTransactionDetail e  WHERE e.branchId.id =:branchId and e.inventoryTransactionId.id =:inventoryTransactionId", Gserial);
//            } catch (Exception e) {
//              e.printStackTrace();
//            }
//            }
//        
//            if (S == null) {
//                S = 0;
//            
//            }
//            invInventoryTransactionDetail.setSerial(S + 1);
//        

        return invInventoryTransactionDetail;
    }

    //mapToDTO
    public InvInventoryTransactionDetailDTO mapToDTO(InvInventoryTransactionDetail invInventoryTransactionDetail) {

        InvInventoryTransactionDetailDTO invInventoryTransactionDetailDTO = new InvInventoryTransactionDetailDTO();

        invInventoryTransactionDetailDTO.setFinalQuantity(invInventoryTransactionDetail.getFinalQuantity());
        invInventoryTransactionDetailDTO.setId(invInventoryTransactionDetail.getId());
        invInventoryTransactionDetailDTO.setCreatedDate(invInventoryTransactionDetail.getCreationDate());
        invInventoryTransactionDetailDTO.setItemBarcode(invInventoryTransactionDetail.getItemBarcode());
        invInventoryTransactionDetailDTO.setMarkEdit(invInventoryTransactionDetail.getMarkEdit());
        invInventoryTransactionDetailDTO.setMsg(invInventoryTransactionDetail.getMsg());
        invInventoryTransactionDetailDTO.setIsdeleted(invInventoryTransactionDetail.getIsdeleted());
        invInventoryTransactionDetailDTO.setMarkDisable(invInventoryTransactionDetail.getMarkDisable());
        invInventoryTransactionDetailDTO.setQuantity(invInventoryTransactionDetail.getQuantity());
        invInventoryTransactionDetailDTO.setSerial(invInventoryTransactionDetail.getSerial());
        invInventoryTransactionDetailDTO.setStatus(invInventoryTransactionDetail.getStatus());
        invInventoryTransactionDetailDTO.setScrewing(BigDecimal.ONE);
        invInventoryTransactionDetailDTO.setTransferFrom(invInventoryTransactionDetail.getTransferFrom());
        invInventoryTransactionDetailDTO.setCreatedBy(invInventoryTransactionDetail.getCreatedBy() != null ? invInventoryTransactionDetail.getCreatedBy().getId() : null);
        invInventoryTransactionDetailDTO.setCreatedDate(invInventoryTransactionDetail.getCreationDate());
        invInventoryTransactionDetailDTO.setBranchId(invInventoryTransactionDetail.getBranchId().getId() != null ? invInventoryTransactionDetail.getBranchId().getId() : null);
        invInventoryTransactionDetailDTO.setCompanyId(invInventoryTransactionDetail.getCompanyId() != null ? invInventoryTransactionDetail.getCompanyId().getId() : null);
        if (invInventoryTransactionDetail.getInventoryTransactionId() != null) {
            InvInventoryTransactionDTO invInventoryTransactionDTO = new InvInventoryTransactionDTO();
            invInventoryTransactionDTO.setId(invInventoryTransactionDetail.getInventoryTransactionId().getId());
            invInventoryTransactionDetailDTO.setInventoryTransactionId(invInventoryTransactionDTO);
        }

        if (invInventoryTransactionDetail.getSelectedPurchaseOrderId() != null) {
            InvPurchaseOrderDetailDTO invPurchaseOrderDetailDTO = new InvPurchaseOrderDetailDTO();
            invPurchaseOrderDetailDTO.setId(invInventoryTransactionDetail.getSelectedPurchaseOrderId().getId());
            invInventoryTransactionDetailDTO.setSelectedPurchaseOrderId(invPurchaseOrderDetailDTO);
        }
        if (invInventoryTransactionDetail.getSelectedId() != null) {
            InvPurchaseInvoiceDetailDTO invPurchaseInvoiceDetailDTO = new InvPurchaseInvoiceDetailDTO();
            invPurchaseInvoiceDetailDTO.setId(invInventoryTransactionDetail.getSelectedId().getId());
            invInventoryTransactionDetailDTO.setSelectedId(invPurchaseInvoiceDetailDTO);
            invInventoryTransactionDetailDTO.setSelectedPurchaseId(invPurchaseInvoiceDetailDTO);
        }
        if (invInventoryTransactionDetail.getItemId() != null) {
            InvItemDTO invItemDTO = new InvItemDTO();
            invItemDTO.setId(invInventoryTransactionDetail.getItemId().getId());
            invItemDTO.setName(invInventoryTransactionDetail.getItemId().getName());
            invInventoryTransactionDetailDTO.setItemId(invItemDTO);
        }
        if (invInventoryTransactionDetail.getItemId().getUnitId() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(invInventoryTransactionDetail.getItemId().getUnitId().getId());
            symbolDTO.setName(invInventoryTransactionDetail.getItemId().getUnitId().getName());
            invInventoryTransactionDetailDTO.setUnitId(symbolDTO);

        }
        if (invInventoryTransactionDetail.getUnitId() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(invInventoryTransactionDetail.getUnitId().getId());
            symbolDTO.setName(invInventoryTransactionDetail.getUnitId().getName());
            invInventoryTransactionDetailDTO.setUnitId(symbolDTO);
        }
        if (invInventoryTransactionDetail.getInventoryTransactionId() != null) {
            InvInventoryTransactionDTO invInventoryTransactionDTO = new InvInventoryTransactionDTO();
            invInventoryTransactionDTO.setId(invInventoryTransactionDetail.getInventoryTransactionId().getId());
            invInventoryTransactionDetailDTO.setInventoryTransactionId(invInventoryTransactionDTO);
        }
        return invInventoryTransactionDetailDTO;
    }

    public List<InvInventoryTransactionDetailDTO> mapToDTOList(List<InvInventoryTransactionDetail> invInventoryTransactionDetailList, TobyUser tobyUser) {
        List<InvInventoryTransactionDetailDTO> invInventoryTransactionDetailDTOList = new ArrayList<>();
        for (InvInventoryTransactionDetail invInventoryTransactionDetail : invInventoryTransactionDetailList) {
            invInventoryTransactionDetailDTOList.add(mapToDTO(invInventoryTransactionDetail));
        }
        return invInventoryTransactionDetailDTOList;
    }

//    public List<InvInventoryTransactionDetailDTO> goherdto (InvPurchaseInvoiceDetailDTO invPurchaseInvoiceDetailDTO){
//        
//        InvInventoryTransactionDetailDTO iitddto =new InvInventoryTransactionDetailDTO();
//        
//        iitddto.setItemId(invPurchaseInvoiceDetailDTO.getItemId());
//        iitddto.setUnitId(invPurchaseInvoiceDetailDTO.getUnitId());
//        iitddto.setQuantity(invPurchaseInvoiceDetailDTO.getQuantity());
//        
//        return (List<InvInventoryTransactionDetailDTO>) iitddto; 
//    }
    @Override
    public List<InvInventoryTransactionDetailDTO> selectDetailsInvInvetoryByIdDTO(Integer inventoryTransactionId, TobyUser tobyUser) {
        Map<String, Object> map = new HashMap<>();
        map.put("inventoryTransactionId", inventoryTransactionId);
        map.put("branchId", tobyUser.getBranchId().getId());
        List<InvInventoryTransactionDetail> detailes = dao.findListByQuery("SELECT e FROM InvInventoryTransactionDetail e where e.inventoryTransactionId.id =:inventoryTransactionId and e.branchId.id =:branchId ", map);

        return mapToDTOList(detailes, tobyUser);
    }

    @Override
    public List<InvInventoryTransactionDetailDTO> deleteInvInventoryTransactionDetailList(Integer inventoryTransactionId, TobyUser tobyUser) {
        Map<String, Object> map = new HashMap<>();
        map.put("inventoryTransactionId", inventoryTransactionId);
        List<InvInventoryTransactionDetail> details = dao.findListByQuery("SELECT e FROM InvInventoryTransactionDetail e where e.inventoryTransactionId.id =:inventoryTransactionId", map);
        for (InvInventoryTransactionDetail invInventoryTransactionDetail : details) {

            invInventoryTransactionDetail.setIsdeleted(1);
            dao.updateEntity(invInventoryTransactionDetail);
            mapToDTO(invInventoryTransactionDetail);
        }
        return mapToDTOList(details, tobyUser);
    }

    @Override
    public List<InvInventoryTransactionDetailDTO> invInventoryTransactionDetails(TobyUser tobyUser) {
        Map<String, Object> map = new HashMap<>();
        map.put("branchId", tobyUser.getBranchId().getId());
        List<InvInventoryTransactionDetail> details = dao.findListByQuery("SELECT e FROM InvInventoryTransactionDetail e where e.branchId.id =:branchId ", map);
        return mapToDTOList(details, tobyUser);
    }

}
