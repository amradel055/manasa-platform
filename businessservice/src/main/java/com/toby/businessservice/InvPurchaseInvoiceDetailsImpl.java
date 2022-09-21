/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.InvPurchaseInvoiceDTO1;
import com.toby.dto.InvPurchaseInvoiceDetailDTO;
import com.toby.dto.ProProductionProductNumberDTO;
import com.toby.dto.ProProductionTransactionDetailDetailViewDTO;
import com.toby.dto.ProProductionTransactionDetailViewDTO;
import com.toby.entity.Branch;
import com.toby.entity.GlYear;
import com.toby.entity.InvInventory;
import com.toby.entity.InvItem;
import com.toby.entity.InvPurchaseInvoice;
import com.toby.entity.InvPurchaseInvoiceDetail;
import com.toby.entity.ProFactoryDeliveryDetail;
import com.toby.entity.Symbol;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
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
 * @author H
 */
@Stateless
public class InvPurchaseInvoiceDetailsImpl implements InvPurchaseInvoiceDetailService {

    @EJB
    private GenericDAO dao;
    @EJB
    private UnitsItemsService unitsItemsService;
    @EJB
    private InvPurchaseInvoiceDetailDetailService invPurchaseInvoiceDetailDetailService;

    @Override
    public InvPurchaseInvoiceDetailDTO addInvPurchaseDetailsInvoice(InvPurchaseInvoiceDetailDTO invPurchaseInvoiceDTO, TobyUser tobyUser) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //mapToEntity
    public InvPurchaseInvoiceDetail mapToEntity(InvPurchaseInvoiceDetailDTO invPurchaseInvoiceDetailDTO, TobyUser tobyUser) {

        InvPurchaseInvoiceDetail invPurchaseInvoiceDetail = new InvPurchaseInvoiceDetail();
        invPurchaseInvoiceDetail.setDiscount(invPurchaseInvoiceDetailDTO.getDiscount());
        invPurchaseInvoiceDetail.setCost(invPurchaseInvoiceDetailDTO.getCost());
        invPurchaseInvoiceDetail.setCostAvarage(invPurchaseInvoiceDetailDTO.getCostAvarage());
        invPurchaseInvoiceDetail.setDiscountType(invPurchaseInvoiceDetailDTO.getDiscountType());
        invPurchaseInvoiceDetail.setExtraCost(invPurchaseInvoiceDetailDTO.getExtraCost());
        invPurchaseInvoiceDetail.setFinalQuantity(invPurchaseInvoiceDetailDTO.getFinalQuantity());
        invPurchaseInvoiceDetail.setId(invPurchaseInvoiceDetailDTO.getId());
        invPurchaseInvoiceDetail.setNet(invPurchaseInvoiceDetailDTO.getNet());
        invPurchaseInvoiceDetail.setQuantity(invPurchaseInvoiceDetailDTO.getQuantity());
        invPurchaseInvoiceDetail.setScrewing(invPurchaseInvoiceDetailDTO.getScrewing());
        invPurchaseInvoiceDetail.setSelectedId(invPurchaseInvoiceDetailDTO.getSelectedId());
        invPurchaseInvoiceDetail.setSerial(invPurchaseInvoiceDetailDTO.getSerial());
        invPurchaseInvoiceDetail.setStatus(invPurchaseInvoiceDetailDTO.getStatus());
        invPurchaseInvoiceDetail.setTaxValue(invPurchaseInvoiceDetailDTO.getTaxValue());
        invPurchaseInvoiceDetail.setTransferFrom(invPurchaseInvoiceDetailDTO.getTransferFrom());
        invPurchaseInvoiceDetail.setWeight(invPurchaseInvoiceDetailDTO.getWeight());
        invPurchaseInvoiceDetail.setBounse(invPurchaseInvoiceDetailDTO.getBounse());
        invPurchaseInvoiceDetail.setIsdeleted(invPurchaseInvoiceDetailDTO.getIsdeleted());
        invPurchaseInvoiceDetail.setNumber(invPurchaseInvoiceDetailDTO.getNumber());
        invPurchaseInvoiceDetail.setClothNumber(invPurchaseInvoiceDetailDTO.getClothNumber());

        if (invPurchaseInvoiceDetailDTO.getInvItemParentId() != null) {
            InvItem invItem = new InvItem();
            invItem.setId(invPurchaseInvoiceDetailDTO.getInvItemParentId().getId());
            invPurchaseInvoiceDetail.setInvItemParentId(invItem);
        }
        if (invPurchaseInvoiceDetailDTO.getInvInventoryDTO() != null) {
            InvInventory invInventory = new InvInventory();
            invInventory.setId(invPurchaseInvoiceDetailDTO.getInvInventoryDTO().getId());
            invInventory.setName(invPurchaseInvoiceDetailDTO.getItemId().getName());
            invPurchaseInvoiceDetail.setInvInventoryId(invInventory);
        }
        if (invPurchaseInvoiceDetailDTO.getItemId() != null) {
            InvItem invItem = new InvItem();
            invItem.setId(invPurchaseInvoiceDetailDTO.getItemId().getId());
            invItem.setName(invPurchaseInvoiceDetailDTO.getItemId().getName());

            if (invPurchaseInvoiceDetailDTO.getItemId().getUnitId() != null) {
                Symbol symbol = new Symbol();
                symbol.setId(invPurchaseInvoiceDetailDTO.getItemId().getUnitId().getId());
                symbol.setName(invPurchaseInvoiceDetailDTO.getItemId().getUnitId().getName());
                invPurchaseInvoiceDetail.setUnitId(symbol);
            }
            invItem.setCode(invPurchaseInvoiceDetailDTO.getItemId().getCode());
            invItem.setSellPrice(invPurchaseInvoiceDetailDTO.getItemId().getSellPrice());
            invPurchaseInvoiceDetail.setItemId(invItem);
        }
        if (invPurchaseInvoiceDetailDTO.getInvPurchaseInvoiceId() != null) {
            InvPurchaseInvoice invPurchaseInvoice = new InvPurchaseInvoice();
            invPurchaseInvoice.setId(invPurchaseInvoiceDetailDTO.getInvPurchaseInvoiceId().getId());
            invPurchaseInvoiceDetail.setInvPurchaseInvoiceId(invPurchaseInvoice);
        }
//        if (invPurchaseInvoiceDetailDTO.getUnitId() != null) {
//            Symbol symbol = new Symbol();
//            symbol.setId(invPurchaseInvoiceDetailDTO.getUnitId());
//            invPurchaseInvoiceDetail.setUnitId(symbol);
//        }

        if (invPurchaseInvoiceDetailDTO.getUnitsItemsSelected() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invPurchaseInvoiceDetailDTO.getUnitsItemsSelected());
            invPurchaseInvoiceDetail.setUnitId(symbol);
        }

        if (invPurchaseInvoiceDetailDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(invPurchaseInvoiceDetailDTO.getCreatedBy());
            invPurchaseInvoiceDetail.setCreatedBy(user);
            invPurchaseInvoiceDetail.setCreationDate(invPurchaseInvoiceDetailDTO.getCreatedDate());
            invPurchaseInvoiceDetail.setModifiedBy(tobyUser);
            invPurchaseInvoiceDetail.setModificationDate(new Date());
            if (invPurchaseInvoiceDetailDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(invPurchaseInvoiceDetailDTO.getCompanyId());
                invPurchaseInvoiceDetail.setCompanyId(company);
            }

            if (invPurchaseInvoiceDetailDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(invPurchaseInvoiceDetailDTO.getBranchId());
                invPurchaseInvoiceDetail.setBranchId(branch);
            }
        } else {
            invPurchaseInvoiceDetail.setCreatedBy(tobyUser);
            invPurchaseInvoiceDetail.setCreationDate(new Date());
            invPurchaseInvoiceDetail.setCompanyId(tobyUser.getCompanyId());
            invPurchaseInvoiceDetail.setBranchId(tobyUser.getBranchId());
        }
        if (invPurchaseInvoiceDetailDTO.getId() == null) {
            Map<String, Object> params = new HashMap<>();
            params.put("branchId", tobyUser.getBranchId().getId());
            params.put("invPurchaseInvoiceId", invPurchaseInvoiceDetail.getInvPurchaseInvoiceId().getId());
            Integer serialmax = 0;
            synchronized (serialmax) {
                try {
                    serialmax = dao.findEntityByQuery("SELECT MAX(g.serial) FROM InvPurchaseInvoiceDetail g  WHERE g.branchId.id =:branchId and g.invPurchaseInvoiceId.id =:invPurchaseInvoiceId ", params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (serialmax == null) {
                serialmax = 0;
            }
            invPurchaseInvoiceDetail.setSerial(serialmax + 1);
        } else {
            invPurchaseInvoiceDetail.setSerial(invPurchaseInvoiceDetailDTO.getSerial());
        }
        return invPurchaseInvoiceDetail;
    }

    public InvPurchaseInvoiceDetailDTO mapToDTO(InvPurchaseInvoiceDetail invPurchaseInvoiceDetail, TobyUser tobyUser) {

        InvPurchaseInvoiceDetailDTO invPurchaseInvoiceDetailDTO = new InvPurchaseInvoiceDetailDTO();
        invPurchaseInvoiceDetailDTO.setCreatedBy(invPurchaseInvoiceDetail.getCreatedBy() != null ? invPurchaseInvoiceDetail.getCreatedBy().getId() : null);
        invPurchaseInvoiceDetailDTO.setCreatedDate(invPurchaseInvoiceDetail.getCreationDate());
        invPurchaseInvoiceDetailDTO.setBranchId(invPurchaseInvoiceDetail.getBranchId() != null ? invPurchaseInvoiceDetail.getBranchId().getId() : null);
        invPurchaseInvoiceDetailDTO.setExtraCost(invPurchaseInvoiceDetail.getExtraCost());
        invPurchaseInvoiceDetailDTO.setDiscount(invPurchaseInvoiceDetail.getDiscount());
        invPurchaseInvoiceDetailDTO.setDiscountType(invPurchaseInvoiceDetail.getDiscountType());
        invPurchaseInvoiceDetailDTO.setId(invPurchaseInvoiceDetail.getId());
        invPurchaseInvoiceDetailDTO.setCost(invPurchaseInvoiceDetail.getCost());
        invPurchaseInvoiceDetailDTO.setCostAvarage(invPurchaseInvoiceDetail.getCostAvarage());
        invPurchaseInvoiceDetailDTO.setFinalQuantity(invPurchaseInvoiceDetail.getFinalQuantity());
        invPurchaseInvoiceDetailDTO.setNet(invPurchaseInvoiceDetail.getNet());
        invPurchaseInvoiceDetailDTO.setQuantity(invPurchaseInvoiceDetail.getQuantity());
        invPurchaseInvoiceDetailDTO.setScrewing(invPurchaseInvoiceDetail.getScrewing());
        invPurchaseInvoiceDetailDTO.setSelectedId(invPurchaseInvoiceDetail.getSelectedId());
        invPurchaseInvoiceDetailDTO.setSerial(invPurchaseInvoiceDetail.getSerial());
        invPurchaseInvoiceDetailDTO.setStatus(invPurchaseInvoiceDetail.getStatus());
        invPurchaseInvoiceDetailDTO.setTaxValue(invPurchaseInvoiceDetail.getTaxValue());
        invPurchaseInvoiceDetailDTO.setTransferFrom(invPurchaseInvoiceDetail.getTransferFrom());
        invPurchaseInvoiceDetailDTO.setSerial(invPurchaseInvoiceDetail.getSerial());
        invPurchaseInvoiceDetailDTO.setIndex(invPurchaseInvoiceDetail.getSerial());
        invPurchaseInvoiceDetailDTO.setIsdeleted(invPurchaseInvoiceDetail.getIsdeleted());
        invPurchaseInvoiceDetailDTO.setClothNumber(invPurchaseInvoiceDetail.getClothNumber());

        invPurchaseInvoiceDetailDTO.setWeight(invPurchaseInvoiceDetail.getWeight());
        invPurchaseInvoiceDetailDTO.setBounse(invPurchaseInvoiceDetail.getBounse());
        invPurchaseInvoiceDetailDTO.setNumber(invPurchaseInvoiceDetail.getNumber());
        if (invPurchaseInvoiceDetail.getInvItemParentId() != null) {
            InvItemDTO invItemDTO = new InvItemDTO();
            invItemDTO.setId(invPurchaseInvoiceDetail.getInvItemParentId().getId());
            invItemDTO.setName(invPurchaseInvoiceDetail.getInvItemParentId().getName());
            invPurchaseInvoiceDetailDTO.setInvItemParentId(invItemDTO);
        }

        invPurchaseInvoiceDetailDTO.setCompanyId(invPurchaseInvoiceDetail.getCompanyId() != null ? invPurchaseInvoiceDetail.getCompanyId().getId() : null);

        if (invPurchaseInvoiceDetail.getInvInventoryId() != null) {
            InvInventoryDTO invInventoryDTO = new InvInventoryDTO();
            invInventoryDTO.setId(invPurchaseInvoiceDetail.getInvInventoryId().getId());
            invInventoryDTO.setName(invPurchaseInvoiceDetail.getInvInventoryId().getName());
            invPurchaseInvoiceDetailDTO.setInvInventoryDTO(invInventoryDTO);
        }
        if (invPurchaseInvoiceDetail.getInvPurchaseInvoiceId() != null) {
            InvPurchaseInvoiceDTO1 invPurchaseInvoiceDTO = new InvPurchaseInvoiceDTO1();
            invPurchaseInvoiceDTO.setId(invPurchaseInvoiceDetail.getInvPurchaseInvoiceId().getId());
            invPurchaseInvoiceDetailDTO.setInvPurchaseInvoiceId(invPurchaseInvoiceDTO);
        }
        if (invPurchaseInvoiceDetail.getItemId() != null) {
            InvItemDTO invItemDTO = new InvItemDTO();
            invItemDTO.setId(invPurchaseInvoiceDetail.getItemId().getId());
            invItemDTO.setName(invPurchaseInvoiceDetail.getItemId().getName());
            invItemDTO.setCode(invPurchaseInvoiceDetail.getItemId().getCode());
//            if (invPurchaseInvoiceDetail.getItemId().getUnitId() != null) {
//                SymbolDTO symbolDTO = new SymbolDTO();
//                symbolDTO.setId(invPurchaseInvoiceDetail.getItemId().getUnitId().getId());
//                symbolDTO.setName(invPurchaseInvoiceDetail.getItemId().getUnitId().getName());
//                invItemDTO.setUnitId(symbolDTO);
//            }
            invPurchaseInvoiceDetailDTO.setItemId(invItemDTO);
        }
        if (invPurchaseInvoiceDetail.getItemId() != null && invPurchaseInvoiceDetail.getUnitId() != null) {
            invPurchaseInvoiceDetailDTO.setUnitsItemseList(unitsItemsService.getUnitsByItemId(invPurchaseInvoiceDetail.getItemId().getId()));
            invPurchaseInvoiceDetailDTO.setUnitsItemsSelected(invPurchaseInvoiceDetail.getUnitId().getId());
//            invPurchaseInvoiceDetailDTO.setUnitId(invPurchaseInvoiceDetail.getUnitId().getId());
        }

        return invPurchaseInvoiceDetailDTO;
    }

    public List<InvPurchaseInvoiceDetailDTO> mapToDTOList(List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailList, TobyUser tobyUser) {
        List<InvPurchaseInvoiceDetailDTO> invPurchaseInvoiceDTOList = new ArrayList<>();
        for (InvPurchaseInvoiceDetail invPurchaseInvoiceDetail : invPurchaseInvoiceDetailList) {
            invPurchaseInvoiceDTOList.add(mapToDTO(invPurchaseInvoiceDetail, tobyUser));
        }
        return invPurchaseInvoiceDTOList;
    }

    @Override
    public InvPurchaseInvoiceDetailDTO addInvPurchaseInvoice(InvPurchaseInvoiceDetailDTO invPurchaseInvoiceDetailDTO, TobyUser tobyUser) {
        InvPurchaseInvoiceDetail invPurchaseInvoiceDetail = mapToEntity(invPurchaseInvoiceDetailDTO, tobyUser);
        invPurchaseInvoiceDetail = dao.updateEntity(invPurchaseInvoiceDetail);
        InvPurchaseInvoiceDetailDTO invPurchaseInvoiceDetailDTO1 = mapToDTO(invPurchaseInvoiceDetail, tobyUser);
        return invPurchaseInvoiceDetailDTO1;
    }

    @Override
    public List<InvPurchaseInvoiceDetailDTO> getInvPurchaseInvoiceDetailListByBranchId(Integer invPurchaseInvoiceId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("invPurchaseInvoiceId", invPurchaseInvoiceId);         //////  AND ((e.type = 0 OR e.type = 2) AND e.active = 1) ORDER BY e.code
        List<InvPurchaseInvoiceDetail> detailses = dao.findListByQuery("SELECT e FROM InvPurchaseInvoiceDetail e WHERE e.invPurchaseInvoiceId.id=:invPurchaseInvoiceId ", params);
        return mapToDTOList(detailses, tobyUser);
    }

    @Override
    public InvPurchaseInvoiceDetailDTO getInvPurchaseInvoiceDetailsByBranchId(Integer invPurchaseInvoiceId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("invPurchaseInvoiceId", invPurchaseInvoiceId);         //////  AND ((e.type = 0 OR e.type = 2) AND e.active = 1) ORDER BY e.code
        InvPurchaseInvoiceDetail detailses = dao.findEntityByQuery("SELECT e FROM InvPurchaseInvoiceDetail e WHERE e.invPurchaseInvoiceId.id=:invPurchaseInvoiceId ", params);
        return mapToDTO(detailses, tobyUser);
    }

    @Override
    public List<InvPurchaseInvoiceDetailDTO> addInvPurchaseInvoiceDetailList(List<InvPurchaseInvoiceDetailDTO> invPurchaseInvoiceDetailDTOList, Integer invPurchaseInvoiceDTOId, TobyUser tobyUser) {
        List<InvPurchaseInvoiceDetailDTO> list = new ArrayList<>();
        for (InvPurchaseInvoiceDetailDTO detailDTO : invPurchaseInvoiceDetailDTOList) {
            InvPurchaseInvoiceDTO1 invPurchaseInvoiceDTO = new InvPurchaseInvoiceDTO1();
            invPurchaseInvoiceDTO.setId(invPurchaseInvoiceDTOId);
            detailDTO.setInvPurchaseInvoiceId(invPurchaseInvoiceDTO);
            InvPurchaseInvoiceDetail invPurchaseInvoiceDetail = mapToEntity(detailDTO, tobyUser);
            invPurchaseInvoiceDetail = dao.updateEntity(invPurchaseInvoiceDetail);
            detailDTO.setId(invPurchaseInvoiceDetail.getId());
//            detailDTO = invPurchaseInvoiceDetailDetailService.save(detailDTO, tobyUser);
            InvPurchaseInvoiceDetailDTO invPurchaseInvoiceDetailDTO = mapToDTO(invPurchaseInvoiceDetail, tobyUser);
            list.add(invPurchaseInvoiceDetailDTO);
        }
        return list;

    }

    @Override
    public BigDecimal getLastCostItem(Integer itemId, GlYear glYear, Integer branchId) {
        List<BigDecimal> purchaseInvoiceDetails = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        params.put("itemId", itemId);
        params.put("branchId", branchId);
        params.put("dateFrom", glYear.getDateFrom());
        params.put("dateTo", glYear.getDateTo());
        purchaseInvoiceDetails = dao.findListByQuery("SELECT d.price FROM InvPurchaseOrder ipo Left join InvPurchaseOrderDetail d  WHERE ipo.branchId.id = :branchId AND d.itemId.id = :itemId AND ipo.date >= :dateFrom AND ipo.date <= :dateTo order by ipo.date desc", params);
        if (purchaseInvoiceDetails != null && !purchaseInvoiceDetails.isEmpty()) {
            return purchaseInvoiceDetails.get(0);
        }

        return BigDecimal.ZERO;
    }

    @Override
    public List<InvPurchaseInvoiceDetailDTO> getInvPurchaseInvoiceDetailsByPurchaseId(Integer invPurchaseInvoiceId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("invPurchaseInvoiceId", invPurchaseInvoiceId);         //////  AND ((e.type = 0 OR e.type = 2) AND e.active = 1) ORDER BY e.code
        List<InvPurchaseInvoiceDetail> detailses = dao.findEntityByQuery("SELECT e FROM InvPurchaseInvoiceDetail e WHERE e.invPurchaseInvoiceId.id=:invPurchaseInvoiceId ", params);
        return mapToDTOList(detailses, tobyUser);
    }

    @Override
    public InvPurchaseInvoiceDetailDTO deleteInvPurchaseInvoiceDTO(InvPurchaseInvoiceDetailDTO invPurchaseInvoiceDetailDTO, TobyUser tobyUser) {
        InvPurchaseInvoiceDetail invPurchaseInvoiceDetail = mapToEntity(invPurchaseInvoiceDetailDTO, tobyUser);
        invPurchaseInvoiceDetail.setIsdeleted(1);
        dao.updateEntity(invPurchaseInvoiceDetail);
        return mapToDTO(invPurchaseInvoiceDetail, tobyUser);
    }

    @Override
    public List<InvPurchaseInvoiceDetailDTO> deleteInvPurchaseInvoiceDetailDTO(InvPurchaseInvoice invPurchaseInvoiceId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("invPurchaseInvoiceId", invPurchaseInvoiceId.getId());         //////  AND ((e.type = 0 OR e.type = 2) AND e.active = 1) ORDER BY e.code
        List<InvPurchaseInvoiceDetail> detailses = dao.findListByQuery("SELECT e FROM InvPurchaseInvoiceDetail e WHERE e.invPurchaseInvoiceId.id=:invPurchaseInvoiceId ", params);
        for (InvPurchaseInvoiceDetail invPurchaseInvoiceDetail : detailses) {
            deleteInvPurchaseInvoiceDTO(mapToDTO(invPurchaseInvoiceDetail, tobyUser), tobyUser);

        }
        return mapToDTOList(detailses, tobyUser);
    }

    @Override
    public List<ProProductionProductNumberDTO> getInvPurchaseInvoiceDetailClothNumber(Integer invPurchaseInvoiceId, Integer productionTransactionDTO) {
        Map<String, Object> params = new HashMap<>();
        params.put("invPurchaseInvoiceId", invPurchaseInvoiceId);
        List<ProProductionProductNumberDTO> proProductionProductNumberDTOList = new ArrayList<>();
        Integer index = 1;
        List<Integer> res = dao.executeNativeQuery("select distinct(cloth_number)  from pro_factory_delivery_detail pfdd \n"
                + "				left join pro_factory_delivery pfd on pfd.id = pfdd.pro_factory_delivery_id\n"
                //                + "                left join pro_item_production_stages pips on pips.inv_item_id = pfdd.item_id\n"
                + "                left join inv_item ii on ii.id = pfdd.item_id \n"
                + "                left join pro_item_production_stages pips on pips.pro_group_id = ii.pro_group_id \n"
                + "                left join pro_production_items_transaction ppti on ppti.pro_factory_delivery_detail_id = pfdd.id\n"
                + "                where pfd.inv_purchase_invoice_id = " + invPurchaseInvoiceId + "\n"
                + "                and (pfdd.id not in (select invoiceDetailId from pro_production_transaction_of_invoice where numberRemain <= 0 and productionStagesId = " + productionTransactionDTO + " ) or ppti.pro_factory_delivery_detail_id is null)\n"
                + "                and  pfd.type = 0 AND pips.pro_production_stages_id = " + productionTransactionDTO);
        for (Integer object : res) {
            ProProductionProductNumberDTO productNumberDTO = new ProProductionProductNumberDTO();
            productNumberDTO.setProductNumber(object);
            productNumberDTO.setIndex(index++);
            proProductionProductNumberDTOList.add(productNumberDTO);
        }
        return proProductionProductNumberDTOList;

    }

    public List<ProProductionProductNumberDTO> mapToIntegerclothNumberList(List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailList) {
        List<ProProductionProductNumberDTO> productionProductNumberDTOList = new ArrayList<>();

        for (InvPurchaseInvoiceDetail invPurchaseInvoiceDetail : invPurchaseInvoiceDetailList) {
            ProProductionProductNumberDTO productNumberDTO = new ProProductionProductNumberDTO();
            productNumberDTO.setProductNumber(invPurchaseInvoiceDetail.getClothNumber());
            productNumberDTO.setId(invPurchaseInvoiceDetail.getId());
            productNumberDTO.setIndex(invPurchaseInvoiceDetail.getId());
            productionProductNumberDTOList.add(productNumberDTO);
        }
        return productionProductNumberDTOList;
    }

    @Override
    public List<ProProductionTransactionDetailViewDTO> getInvPurchaseInvoiceDetailItemCompleted(Integer invPurchaseInvoiceId, Integer clothNumber, Integer productionTransactionDTO) {
        List<ProProductionTransactionDetailViewDTO> proProductionTransactionDetailViewDTOs = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("invPurchaseInvoiceId", invPurchaseInvoiceId);         //////  AND ((e.type = 0 OR e.type = 2) AND e.active = 1) ORDER BY e.code
        params.put("clothNumber", clothNumber);         //////  AND ((e.type = 0 OR e.type = 2) AND e.active = 1) ORDER BY e.code
        Integer index = 1;
        List<Object[]> res = dao.executeNativeQuery("select distinct(inv_item_parent_id) , ii.name  from pro_factory_delivery_detail pfdd \n"
                + "left join pro_factory_delivery pfd on pfd.id = pfdd.pro_factory_delivery_id\n"
                + "                left join pro_item_production_stages pips on pips.inv_item_id = pfdd.item_id\n"
                + "                left join pro_production_items_transaction ppti on ppti.pro_factory_delivery_detail_id = pfdd.id\n"
                + "                left join inv_item ii on ii.id = pfdd.inv_item_parent_id\n"
                + "                where pfd.inv_purchase_invoice_id = " + invPurchaseInvoiceId + "  and pfdd.cloth_number = " + clothNumber + " \n"
                + "                and (ppti.pro_factory_delivery_detail_id not in (select invoiceDetailId from pro_production_transaction_of_invoice where numberRemain = 0 and productionStagesId = " + productionTransactionDTO + " ) \n"
                + "                or ppti.pro_factory_delivery_detail_id is null)\n"
                + "                and pips.pro_production_stages_id = " + productionTransactionDTO);
        for (Object[] object : res) {
            ProProductionTransactionDetailViewDTO proProductionTransactionDetailViewDTO = new ProProductionTransactionDetailViewDTO();
            proProductionTransactionDetailViewDTO.setInvItemCode((Integer) object[0]);
            proProductionTransactionDetailViewDTO.setInvItemName((String) object[1]);
            proProductionTransactionDetailViewDTO.setIndex(index++);

            proProductionTransactionDetailViewDTOs.add(proProductionTransactionDetailViewDTO);
        }
        return proProductionTransactionDetailViewDTOs;
    }

    public List<ProProductionTransactionDetailViewDTO> mapToIntegerItemCompletedList(List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailList) {
        List<ProProductionTransactionDetailViewDTO> productionTransactionDetailViewDTOList = new ArrayList<>();

        for (InvPurchaseInvoiceDetail invPurchaseInvoiceDetail : invPurchaseInvoiceDetailList) {
            ProProductionTransactionDetailViewDTO productionTransactionDetailViewDTO = new ProProductionTransactionDetailViewDTO();
            productionTransactionDetailViewDTO.setInvItemName(invPurchaseInvoiceDetail.getInvItemParentId().getName());
            productionTransactionDetailViewDTO.setIndex(invPurchaseInvoiceDetail.getId());
            productionTransactionDetailViewDTO.setId(invPurchaseInvoiceDetail.getInvItemParentId().getId());
            productionTransactionDetailViewDTOList.add(productionTransactionDetailViewDTO);
        }
        return productionTransactionDetailViewDTOList;
    }

    @Override
    public List<ProProductionTransactionDetailDetailViewDTO> getInvPurchaseInvoiceDetailItems(Integer invPurchaseInvoiceId,
            Integer clothNumber, Integer proProductionStagesId) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        Map<String, Object> params = new HashMap<>();
        params.put("invPurchaseInvoiceId", invPurchaseInvoiceId);         //////  AND ((e.type = 0 OR e.type = 2) AND e.active = 1) ORDER BY e.code
        params.put("clothNumber", clothNumber);         //////  AND ((e.type = 0 OR e.type = 2) AND e.active = 1) ORDER BY e.code
//        params.put("invItemParentId", invItemParentId);         //////  AND ((e.type = 0 OR e.type = 2) AND e.active = 1) ORDER BY e.code
        params.put("proProductionStagesId", proProductionStagesId);         //////  AND ((e.type = 0 OR e.type = 2) AND e.active = 1) ORDER BY e.code
//        List<ProFactoryDeliveryDetail> detailses = dao.findListByQuery("SELECT e FROM ProFactoryDeliveryDetail e WHERE ( "
//                + "e.itemId.id in (select pe.invItemId.id from  ProItemProductionStages pe where pe.proProductionStagesId.id = :proProductionStagesId) AND e.invPurchaseInvoiceDetailId.invPurchaseInvoiceId.id=:invPurchaseInvoiceId AND e.clothNumber=:clothNumber AND e.invItemParentId.id=:invItemParentId )group by e.itemId", params);
        List<ProFactoryDeliveryDetail> detailses = dao.findListByQuery("SELECT e FROM ProFactoryDeliveryDetail e WHERE e.proFactoryDeliveryId.type = 0 AND  ( "
                + " e.itemId.proGroupId.id in (select pe.proGroupId.id from  ProItemProductionStages pe where pe.proProductionStagesId.id = :proProductionStagesId) "
                + " AND e.invPurchaseInvoiceDetailId.invPurchaseInvoiceId.id=:invPurchaseInvoiceId AND e.clothNumber=:clothNumber )group by e.itemId", params);
        return mapToIntegerItemsList(detailses);
    }

    @Override
    public List<ProProductionTransactionDetailDetailViewDTO> getInvPurchaseInvoiceDetailItems(Integer invInvoiceSerial, Integer proProductionStagesId) {
        Map<String, Object> params = new HashMap<>();
        params.put("invInvoiceSerial", invInvoiceSerial);
        params.put("proProductionStagesId", proProductionStagesId);
        List<ProFactoryDeliveryDetail> detailses = dao.findListByQuery("SELECT e FROM ProFactoryDeliveryDetail e "
                + " WHERE "
                + " e.proFactoryDeliveryId.type = 0 AND  ( "
                + " e.itemId.proGroupId.id in (select pe.proGroupId.id from  ProItemProductionStages pe where pe.proProductionStagesId.id = :proProductionStagesId) "
                + " AND e.invPurchaseInvoiceDetailId.invPurchaseInvoiceId.serial=:invInvoiceSerial )", params);
        return mapToIntegerItemsList(detailses);
    }

    public List<ProProductionTransactionDetailDetailViewDTO> mapToIntegerItemsList(List<ProFactoryDeliveryDetail> invPurchaseInvoiceDetailList) {
        List<ProProductionTransactionDetailDetailViewDTO> detailDetailViewDTOList = new ArrayList<>();
        Integer index = 1;
        Map<Integer, ProProductionTransactionDetailDetailViewDTO> map = new HashMap<>();
        for (ProFactoryDeliveryDetail proFactoryDeliveryDetail : invPurchaseInvoiceDetailList) {
            ProProductionTransactionDetailDetailViewDTO productionTransactionDetailDetailViewDTO = new ProProductionTransactionDetailDetailViewDTO();

            productionTransactionDetailDetailViewDTO.setInvItemName(proFactoryDeliveryDetail.getItemId().getName());
            productionTransactionDetailDetailViewDTO.setQuantity(proFactoryDeliveryDetail.getNumber());
            productionTransactionDetailDetailViewDTO.setId(proFactoryDeliveryDetail.getId());
            productionTransactionDetailDetailViewDTO.setInvItemCode(proFactoryDeliveryDetail.getItemId().getId());
            productionTransactionDetailDetailViewDTO.setIndex(index++);
            productionTransactionDetailDetailViewDTO.setProFactoryDeliveryDetailId(proFactoryDeliveryDetail.getId());
            if(map.containsKey(proFactoryDeliveryDetail.getItemId().getId())){
                ProProductionTransactionDetailDetailViewDTO detailDetailViewDTO = map.get(proFactoryDeliveryDetail.getItemId().getId());
                productionTransactionDetailDetailViewDTO.setQuantity(productionTransactionDetailDetailViewDTO.getQuantity().add(detailDetailViewDTO.getQuantity()));
            }
            map.put(proFactoryDeliveryDetail.getItemId().getId(), productionTransactionDetailDetailViewDTO);
        }
        detailDetailViewDTOList = new ArrayList<>(map.values());
        return detailDetailViewDTOList;
    }
    
    

    @Override
    public List<InvPurchaseInvoiceDetailDTO> getInvPurchaseInvoiceDetailList(Integer invPurchaseInvoiceId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("invPurchaseInvoiceId", invPurchaseInvoiceId);         
        List<InvPurchaseInvoiceDetail> detailses = dao.findListByQuery("SELECT e FROM InvPurchaseInvoiceDetail e WHERE e.invPurchaseInvoiceId.id=:invPurchaseInvoiceId ", params);
        return mapToDTOList(detailses, tobyUser);
    }

}
