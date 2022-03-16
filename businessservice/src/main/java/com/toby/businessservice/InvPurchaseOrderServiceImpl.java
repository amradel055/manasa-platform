/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import com.toby.bussinessservice.global.InvPurchaseReturnSave;
import com.toby.core.GenericDAO;
import com.toby.define.ScreenNameClassEnum;
import com.toby.dto.CurrencyDTO;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.InventoryDelegatorDTO;
import com.toby.entity.Branch;
import com.toby.entity.Currency;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.InvPurchaseOrder;
import com.toby.entity.InvPurchaseOrderDetail;
import com.toby.entity.InventoryDelegator;
import com.toby.views.PurchaseOrderNotLoadedFromInvAddingOrder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq002
 */
@Stateless
public class InvPurchaseOrderServiceImpl implements InvPurchaseOrderService {

    @EJB
    private GenericDAO dao;
    @EJB
    private GenericService genericService;
    @EJB
    private InvPurchaseOrderDetailsService invPurchaseOrderDetailsService;
    @EJB
    public InvGlobalService invGlobalService;
    @EJB
    private OrganizationSiteService organizationSiteService;
    
    InvPurchaseReturnSave invPurchaseReturnSave;

    @Override
    public InvPurchaseReturnSave addPurchaseOrder(InvPurchaseOrder invPurchaseOrder, List<InvPurchaseOrderDetail> invPurchaseOrderDetailList, List<InvPurchaseOrderDetail> invPurchaseOrderDeleted) {
        Integer serial;
        invPurchaseReturnSave = new InvPurchaseReturnSave();
        if (invPurchaseOrder.getId() != null) {
            dao.updateEntity(invPurchaseOrder);
        } else {
            serial = genericService.getMaxGenericSerialByType(InvPurchaseOrder.class, invPurchaseOrder.getBranchId().getId(), null);
            invPurchaseOrder.setSerial(serial);
            dao.saveEntity(invPurchaseOrder);
        }

        for (InvPurchaseOrderDetail deleted : invPurchaseOrderDeleted) {
            invPurchaseOrderDetailsService.deletePurchaseOrderDetails(deleted);
        }

        for (InvPurchaseOrderDetail detail : invPurchaseOrderDetailList) {
            detail.setInvPurchaseOrderId(invPurchaseOrder);
            if (detail.getId() != null) {
                invPurchaseOrderDetailsService.updatePurchaseOrderDetails(detail);
            } else {
                serial = getMaxSerialForInvoiceDetail(invPurchaseOrder);
                detail.setSerial(serial);
                invPurchaseOrderDetailsService.addPurchaseOrderDetails(detail);
            }
        }
        invPurchaseReturnSave.setInvPurchaseOrder(invPurchaseOrder);
        invPurchaseReturnSave.setInvPurchaseOrderDetailList(invPurchaseOrderDetailsService.getAllPurchaseOrderDetailsByInvPurchaseOrderId(invPurchaseOrder.getId()));

        return invPurchaseReturnSave;
    }

    @Override
    public void deletePurchaseOrder(Integer invPurchaseOrderId) {
        if (invPurchaseOrderId != null && invPurchaseOrderId > 0) {
            InvPurchaseOrder invPurchaseOrder = new InvPurchaseOrder();
            invPurchaseOrder.setId(invPurchaseOrderId);
            dao.executeDeleteQuery("delete from InvPurchaseOrderDetail e WHERE e.id > 0 AND e.invPurchaseOrderId.id = " + invPurchaseOrderId);
            dao.deleteEntity(invPurchaseOrder);
        }
    }

    @Override
    public List<InvPurchaseOrder> getALLPurchaseOrder() {
        return dao.findAll(InvPurchaseOrder.class);
    }

    @Override
    public List<InvPurchaseOrder> getALLPurchaseOrderByCompanyId(Integer Id) {
        return dao.findEntityListByCompanyId(InvPurchaseOrder.class, Id);
    }

    @Override
    public List<InvPurchaseOrder> getALLPurchaseOrderByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvPurchaseOrder> invPurchaseOrders = dao.findListByQuery("SELECT e FROM InvPurchaseOrder e WHERE e.branchId.id = :branchId order by e.serial desc", params);
        return invPurchaseOrders;
    }

    @Override
    public List<InvPurchaseOrderDetail> getALLPurchaseOrderDetailsByPurchaseOrder(Integer purchaseOrderId) {
        Map<String, Object> params = new HashMap<>();
        params.put("purchaseOrderId", purchaseOrderId);
        List<InvPurchaseOrderDetail> details = dao.findListByQuery("SELECT e FROM InvPurchaseOrderDetail e WHERE e.invPurchaseOrderId.id = :purchaseOrderId", params);
        return details;
    }

    @Override
    public InvPurchaseOrder findPurchaseOrderByPurchaseOrderId(Integer purchaseOrderId) {
        return dao.findEntityById(InvPurchaseOrder.class, purchaseOrderId);
    }

    private InvPurchaseOrder findPurchaseOrderByPurchaseOrderId(Integer purchaseOrderSerial, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("purchaseOrderSerial", purchaseOrderSerial);
        params.put("branchId", branchId);
        List<InvPurchaseOrder> details = dao.findListByQuery("SELECT e FROM InvPurchaseOrder e WHERE e.serial = :purchaseOrderSerial AND e.branchId.id = :branchId AND e.type = 0", params);
        return (details == null || details.isEmpty()) ? null : details.get(0);
    }

    @Override
    public List<PurchaseOrderNotLoadedFromInvAddingOrder> getAllPurchaseOrderNotLoaded(Integer branchId) {
        Map<String, Object> params = new HashMap<>();

        params.put("branchId", branchId);
        List<PurchaseOrderNotLoadedFromInvAddingOrder> details = dao.findListByQuery("SELECT e FROM PurchaseOrderNotLoadedFromInvAddingOrder e WHERE e.branchId= :branchId", params);
        return details;
    }

    @Override
    public InvPurchaseOrder updateInvPurchaseOrder(InvPurchaseOrder invPurchaseOrder) {
        return dao.updateEntity(invPurchaseOrder);
    }

    @Override
    public List<InvPurchaseOrder> getALLPurchaseOrderByBranchIdByStatus(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvPurchaseOrder> invPurchaseOrders = dao.findListByQuery("SELECT e FROM InvPurchaseOrder e WHERE e.branchId.id = :branchId and e.status != 2 ", params);
        return invPurchaseOrders;
    }

    public synchronized Integer getMaxSerialForInvoiceDetail(InvPurchaseOrder invPurchaseOrder) {
        Map<String, Object> params = new HashMap<>();
        params.put("invPurchaseOrderId", invPurchaseOrder.getId());

        Integer serial = dao.findEntityByQuery("SELECT max(e.serial) FROM InvPurchaseOrderDetail e WHERE e.invPurchaseOrderId.id =:invPurchaseOrderId ", params);

        if (serial == null) {
            return 1;
        } else {
            serial = serial + 1;
            return serial;
        }
    }

    @Override
    public InvInvoiceDTO convertPurchaseOrderToPurchaseInvoice(InvInvoiceDTO invPurchaseInvoiceDTO) {
        InvPurchaseOrder invPurchaseOrder = null;
        if (invPurchaseInvoiceDTO.getNeedLoadedSerial() != null && invPurchaseInvoiceDTO.getNeedLoadedSerial() != 0) {
            invPurchaseOrder = findPurchaseOrderByPurchaseOrderId(invPurchaseInvoiceDTO.getNeedLoadedSerial(), invPurchaseInvoiceDTO.getBranchId());
        } else {
            invPurchaseOrder = findPurchaseOrderByPurchaseOrderId(invPurchaseInvoiceDTO.getInvDailogeDataSelected().getId());
        }
        if (invPurchaseOrder != null) {
            invPurchaseInvoiceDTO = mapToDTO(invPurchaseOrder, invPurchaseInvoiceDTO);
            modifyReturnData(invPurchaseInvoiceDTO, invPurchaseOrder);
        } else {
            invPurchaseInvoiceDTO.setMsg("حدث خطأ - تم حذفه ");
        }
        return invPurchaseInvoiceDTO;
    }

    private void modifyReturnData(InvInvoiceDTO invPurchaseInvoiceDTO, InvPurchaseOrder invPurchaseOrder) {
//        if (!invPurchaseInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVPURCHASEINVOICEFORM)) {
        invPurchaseInvoiceDTO.setParent(invPurchaseInvoiceDTO.getId());
//        }
        invPurchaseInvoiceDTO.setId(null);
        invPurchaseInvoiceDTO.setSerial(null);
        invPurchaseInvoiceDTO.setLoadedSerial(invPurchaseOrder.getSerial());
        invPurchaseInvoiceDTO.setInvPurchaseOrderId(invPurchaseOrder.getId());
        List<InvDetailDTO> detailDTOs = new ArrayList<>();
        for (InvDetailDTO invDetailDTO : invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList()) {
            invDetailDTO.setId(null);
            invDetailDTO.setSerial(null);
            invDetailDTO.setIsEdit(Boolean.TRUE);
            detailDTOs.add(invDetailDTO);
        }
        invPurchaseInvoiceDTO.setInvPurchaseInvoiceDetailList(detailDTOs);
    }

    private InvInvoiceDTO mapToDTO(InvPurchaseOrder entity, InvInvoiceDTO invPurchaseInvoiceDTO) {

        invPurchaseInvoiceDTO = initData(invPurchaseInvoiceDTO, entity);

        return invPurchaseInvoiceDTO;
    }

    private InvInvoiceDTO initData(InvInvoiceDTO invInvoiceDTO, InvPurchaseOrder entity) {
        commonData(invInvoiceDTO, entity);

//        invPurchaseInvoiceDTO.setParent(entity.getId());
        invInvoiceDTO.setCompanyActivity(entity.getBranchId().getCompanyActivity());
        invInvoiceDTO.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);
        invInvoiceDTO.setDiscount(entity.getDiscount());
        invInvoiceDTO.setDiscountType(entity.getDiscountType());
        invInvoiceDTO.setRate(entity.getRate());
        invInvoiceDTO.setType(entity.getType());
        invInvoiceDTO.setInvPurchaseOrderId(entity.getId());
//        invPurchaseInvoiceDTO.setLoadedSerial(entity.getSerial());
        if (entity.getCurrencyId() != null) {
            CurrencyDTO currencyDTO = new CurrencyDTO();
            currencyDTO.setId(entity.getCurrencyId().getId());
            currencyDTO.setName(entity.getCurrencyId().getName());
            currencyDTO.setCode(entity.getCurrencyId().getCode());
            invInvoiceDTO.setCurrencyId(currencyDTO);
        }
        if (entity.getSupplierId() != null) {
            invInvoiceDTO.setOrganizationSiteId(organizationSiteService.mapToDTOSmall(entity.getSupplierId()));
        }
//        if (entity.getInvInventoryId() != null) {
//            InvInventoryDTO invInventoryDTO = new InvInventoryDTO();
//            invInventoryDTO.setId(entity.getInvInventoryId().getId());
//            invInventoryDTO.setName(entity.getInvInventoryId().getName());
//            invInventoryDTO.setCode(entity.getInvInventoryId().getCode());
//            invPurchaseInvoiceDTO.setInvInventoryId(invInventoryDTO);
//        }
        if (entity.getDelegatorId() != null) {
            InventoryDelegatorDTO inventoryDelegatorDTO = new InventoryDelegatorDTO();
            inventoryDelegatorDTO.setId(entity.getDelegatorId().getId());
            inventoryDelegatorDTO.setName(entity.getDelegatorId().getName());
            inventoryDelegatorDTO.setCode(entity.getDelegatorId().getCode());
            invInvoiceDTO.setInvDelegatorId(inventoryDelegatorDTO);
        }
        invInvoiceDTO.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        invInvoiceDTO.setCreatedDate(entity.getCreationDate());
        invInvoiceDTO.setCompanyId(entity.getCompanyId().getId() != null ? entity.getCompanyId().getId() : null);
        List<InvDetailDTO> invPurchaseInvoiceDetailsListDTO = invPurchaseOrderDetailsService.getAllInvPurchaseInvoiceDetailsByInvPurchaseInvoiceIdDTO(invInvoiceDTO);
        invInvoiceDTO.setInvPurchaseInvoiceDetailList(invPurchaseInvoiceDetailsListDTO);
        invInvoiceDTO.setInvPurchaseInvoiceDetailsDeletedList(new ArrayList<InvDetailDTO>());
        invInvoiceDTO = invGlobalService.updateSummition(invInvoiceDTO,false);
        return invInvoiceDTO;
    }

    private void commonData(InvInvoiceDTO invPurchaseInvoiceDTO, InvPurchaseOrder entity) {
        invPurchaseInvoiceDTO.setId(entity.getId());
        invPurchaseInvoiceDTO.setSerial(entity.getSerial());
        invPurchaseInvoiceDTO.setDate(entity.getDate());
        invPurchaseInvoiceDTO.setRemarks(entity.getRemarks());
        invPurchaseInvoiceDTO.setIndex(entity.getSerial());
        invPurchaseInvoiceDTO.setCreatedByName(entity.getCreatedBy() != null ? entity.getCreatedBy().getName() : null);
    }

    private InvInvoiceDTO initDataForList(InvInvoiceDTO invPurchaseInvoiceDTO, InvPurchaseOrder entity) {
        commonData(invPurchaseInvoiceDTO, entity);
        if (entity.getSupplierId() != null) {
            invPurchaseInvoiceDTO.setOrganizationSiteName(entity.getSupplierId().getName());
        }
        if (entity.getDelegatorId() != null) {
            invPurchaseInvoiceDTO.setInvDelegatorName(entity.getDelegatorId().getName());
        }

        return invPurchaseInvoiceDTO;
    }

    @Override
    public InvInvoiceDTO findInvPurchaseOrderById(InvInvoiceDTO invPurchaseInvoiceDto, TobyUser tobyUser) {
        InvPurchaseOrder invPurchaseOrder = dao.findEntityById(InvPurchaseOrder.class, invPurchaseInvoiceDto.getId());
        InvInvoiceDTO invPurchaseInvoiceDTO = mapToDTO(invPurchaseOrder, invPurchaseInvoiceDto);
        invPurchaseInvoiceDTO = invGlobalService.updateSummition(invPurchaseInvoiceDTO,false);
        return invPurchaseInvoiceDTO;
    }

    public InvInvoiceDTO validateSave(InvInvoiceDTO invPurchaseInvoiceDTO) {
        StringBuilder error = new StringBuilder();

        if (invPurchaseInvoiceDTO.getOrganizationSiteId() == null) {
            error.append("\n يجب أختيار عميل / مورد");
        }
        if (invPurchaseInvoiceDTO.getDate() == null) {
            error.append("\n يجب اختيار تاريخ");
        }

        checkIfLoaded(invPurchaseInvoiceDTO, error);        
        
        if (!error.toString().isEmpty()) {
            invPurchaseInvoiceDTO.setMsg(error.toString());
        }
        return invPurchaseInvoiceDTO;
    }

    public void checkIfLoaded(InvInvoiceDTO invPurchaseInvoiceDTO, StringBuilder error) {
        if (invPurchaseInvoiceDTO.getId() != null) {
            List<Integer> id = dao.executeNativeQuery("select id from (select inv_purchase_order_id as id from inv_purchase_invoice "
                    + " where branch_id = " + invPurchaseInvoiceDTO.getBranchId() + " and inv_purchase_order_id is not null\n"
                    + " union all \n"
                    + " select purchase_order_id as id from inv_addingorder where branch_id = " + invPurchaseInvoiceDTO.getBranchId() + " and purchase_order_id is not null) as s\n"
                    + " where id = "+invPurchaseInvoiceDTO.getId());
            if (!id.isEmpty()) {
                    error.append("\n لا يجوز تعديل هذا السند لأنه تم تحميلة");
            }
        }
    }

    @Override
    public InvInvoiceDTO save(InvInvoiceDTO invPurchaseInvoiceDTO, TobyUser tobyUser) {
        try {
            invPurchaseInvoiceDTO = validateSave(invPurchaseInvoiceDTO);

            InvPurchaseOrder invPurchaseOrder;
            if (invPurchaseInvoiceDTO != null && (invPurchaseInvoiceDTO.getMsg() == null || invPurchaseInvoiceDTO.getMsg().isEmpty())) {
                invPurchaseOrder = mapToEntity(invPurchaseInvoiceDTO, tobyUser);
                if (invPurchaseOrder.getId() != null) {
                    //بعمل حفظ فى حالة التعديل
                    invPurchaseOrder = dao.updateEntity(invPurchaseOrder);
                } else {
                    // فى حالة ان الفاتورة جديدة بروح اجيب السيريال الاول 
                    Integer serial = genericService.getMaxGenericSerialByType(InvPurchaseOrder.class, invPurchaseOrder.getBranchId().getId(), invPurchaseOrder.getType());
                    invPurchaseOrder.setSerial(serial);

                    invPurchaseOrder = dao.updateEntity(invPurchaseOrder);
                    invPurchaseInvoiceDTO.setId(invPurchaseOrder.getId());
                }

                invPurchaseOrderDetailsService.deleteInvPurchaseInvoiceDetailListDTO(invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailsDeletedList());

                String error = invPurchaseOrderDetailsService.addInvPurchaseOrderDetailsDTO(invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList(), invPurchaseInvoiceDTO, invPurchaseInvoiceDTO.getScreenName(), tobyUser);

                if (error != null && !error.isEmpty()) {
                    invPurchaseInvoiceDTO.setMsg(error);
                    dao.rollbackQuery();
                } else {
                    invPurchaseInvoiceDTO = mapToDTO(invPurchaseOrder, invPurchaseInvoiceDTO);
                    invPurchaseInvoiceDTO = invGlobalService.updateSummition(invPurchaseInvoiceDTO,false);
                }

            }
        } catch (Exception e) {
            invPurchaseInvoiceDTO.setMsg(e.toString());
            dao.rollbackQuery();
        }

        return invPurchaseInvoiceDTO;
    }

    //mapToEntity
    private InvPurchaseOrder mapToEntity(InvInvoiceDTO invPurchaseInvoiceDTO, TobyUser tobyUser) {

        InvPurchaseOrder invPurchaseOrder = new InvPurchaseOrder();

        if (invPurchaseInvoiceDTO.getCurrencyId() != null) {
            Currency currency = new Currency();
            currency.setId(invPurchaseInvoiceDTO.getCurrencyId().getId());
            invPurchaseOrder.setCurrencyId(currency);
        }

        if (invPurchaseInvoiceDTO.getOrganizationSiteId() != null) {
            InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
            invOrganizationSite.setId(invPurchaseInvoiceDTO.getOrganizationSiteId().getId());
            invOrganizationSite.setName(invPurchaseInvoiceDTO.getOrganizationSiteId().getName());
            invPurchaseOrder.setSupplierId(invOrganizationSite);
        }

        if (invPurchaseInvoiceDTO.getInvDelegatorId() != null) {
            InventoryDelegator inventoryDelegator = new InventoryDelegator();
            inventoryDelegator.setId(invPurchaseInvoiceDTO.getInvDelegatorId().getId());
            invPurchaseOrder.setDelegatorId(inventoryDelegator);
        }

        if (invPurchaseInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVPURCHASEORDERFORM)) {
            invPurchaseOrder.setType(0);
        } else if (invPurchaseInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVSALESORDERFORM)) {
            invPurchaseOrder.setType(1);
        }

        invPurchaseOrder.setId(invPurchaseInvoiceDTO.getId());
        invPurchaseOrder.setSerial(invPurchaseInvoiceDTO.getSerial());
        invPurchaseOrder.setDate(invPurchaseInvoiceDTO.getDate());
        invPurchaseOrder.setRemarks(invPurchaseInvoiceDTO.getRemarks());
        invPurchaseOrder.setRate(invPurchaseInvoiceDTO.getRate() != null ? invPurchaseInvoiceDTO.getRate() : null);

        if (invPurchaseInvoiceDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(invPurchaseInvoiceDTO.getCreatedBy());
            invPurchaseOrder.setCreatedBy(user);
            invPurchaseOrder.setCreationDate(invPurchaseInvoiceDTO.getCreatedDate());
            invPurchaseOrder.setModifiedBy(tobyUser);
            invPurchaseOrder.setModificationDate(new Date());
            if (invPurchaseInvoiceDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(invPurchaseInvoiceDTO.getCompanyId());
                invPurchaseOrder.setCompanyId(company);
            }

            if (invPurchaseInvoiceDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(invPurchaseInvoiceDTO.getBranchId());
                invPurchaseOrder.setBranchId(branch);
            }
        } else {
            invPurchaseOrder.setCreatedBy(tobyUser);
            invPurchaseOrder.setCreationDate(new Date());
            invPurchaseOrder.setCompanyId(tobyUser.getCompanyId());
            invPurchaseOrder.setBranchId(tobyUser.getBranchId());
        }

        return invPurchaseOrder;
    }

    @Override
    public Long getTotalRegistors(Integer type, Integer branchId, Map<String, Object> filters) {
        StringBuilder builder = new StringBuilder();
        filters.put("type", type);
        filters.put("branchId", branchId);
        builder.append("SELECT count(s) FROM InvPurchaseOrder s WHERE s.branchId.id = :branchId AND s.type =:type ");
        appendFilter(filters, branchId, builder);
        Long details = dao.findEntityByQuery(builder.toString(), filters);

        return details;
    }

    public void appendFilter(Map<String, Object> filters, Integer branchId, StringBuilder builder) {
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and ( FUNCTION('DATE_FORMAT',s.date, '%d-%m-%Y')  like CONCAT('%',:globalFilter ,'%') OR  s.serial like CONCAT('%',:globalFilter ,'%')  OR s.supplierId.name  like CONCAT('%',:globalFilter ,'%') OR s.delegatorId.name like CONCAT('%',:globalFilter ,'%') OR s.createdBy.name like CONCAT('%',:globalFilter ,'%')  OR s.remarks like CONCAT('%',:globalFilter ,'%')) ";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
    }

    @Override
    public List<InvInvoiceDTO> getALLPurchaseOrderByBranchIdDTO(Integer type, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters) {
        filters.put("type", type);
        filters.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT s FROM InvPurchaseOrder s WHERE s.branchId.id = :branchId AND s.type =:type  ");
        appendFilter(filters, branchId, builder);
        builder.append(" order by s.serial DESC");
        List<InvPurchaseOrder> details = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        return returnListDTOForList(details);
    }

    private List<InvInvoiceDTO> returnListDTOForList(List<InvPurchaseOrder> invPurchaseOrderList) {
        List<InvInvoiceDTO> invPurchaseInvoiceDTOList = new ArrayList<>();
        for (InvPurchaseOrder invPurchaseOrder : invPurchaseOrderList) {
            InvInvoiceDTO invPurchaseInvoiceDTO = new InvInvoiceDTO();
            invPurchaseInvoiceDTO = initDataForList(invPurchaseInvoiceDTO, invPurchaseOrder);
            invPurchaseInvoiceDTOList.add(invPurchaseInvoiceDTO);
        }
        return invPurchaseInvoiceDTOList;
    }

}
