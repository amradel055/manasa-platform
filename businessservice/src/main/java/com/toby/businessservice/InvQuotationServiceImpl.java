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
import com.toby.dto.CurrencyDTO;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.InventoryDelegatorDTO;
import com.toby.entity.Branch;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.InvQotation;
import com.toby.entity.InvQotationDetail;
import com.toby.entity.InventoryDelegator;
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
public class InvQuotationServiceImpl implements InvQuotationService {

    @EJB
    private GenericDAO dao;
    @EJB
    private GenericService genericService;
    @EJB
    public InvGlobalService invGlobalService;
    @EJB
    private InvQuotationDetailsService invQuotationDetailsService;
    @EJB
    private OrganizationSiteService organizationSiteService;

    InvPurchaseReturnSave invPurchaseReturnSave;

    @Override
    public InvPurchaseReturnSave addInvQotation(InvQotation invQotation, List<InvQotationDetail> invQotationDetailDetailList, List<InvQotationDetail> qotationDetailDeleted) {
        Integer serial;
        invPurchaseReturnSave = new InvPurchaseReturnSave();
        if (invQotation.getId() != null) {
            dao.updateEntity(invQotation);
        } else {
            serial = genericService.getMaxGenericSerialByType(InvQotation.class, invQotation.getBranchId().getId(), null);
            invQotation.setSerial(serial);
            dao.saveEntity(invQotation);
        }

        for (InvQotationDetail deleted : qotationDetailDeleted) {
            invQuotationDetailsService.deleteInvQotationDetails(deleted);
        }

        for (InvQotationDetail detail : invQotationDetailDetailList) {
            detail.setQotationId(invQotation);
            if (detail.getId() != null) {
                invQuotationDetailsService.updateInvQotationDetails(detail);
            } else {
                serial = getMaxSerialForInvoiceDetail(invQotation);
                detail.setSerial(serial);
                invQuotationDetailsService.addInvQotationDetails(detail);
            }
        }
        invPurchaseReturnSave.setInvQotation(invQotation);
        invPurchaseReturnSave.setInvQotationDetailList(invQuotationDetailsService.getAllInvQotationDetailsByIInvQotationIdWithStatus(invQotation.getId()));
        return invPurchaseReturnSave;
    }

    @Override
    public void deleteInvQotation(InvQotation invQotation, Integer invQotationIdToPass) {
        if (invQotationIdToPass != null && invQotationIdToPass > 0) {
            dao.executeDeleteQuery("delete from InvQotationDetail e WHERE e.id > 0 AND e.qotationId.id = " + invQotationIdToPass);
        }
        dao.deleteEntity(invQotation);
    }

    @Override
    public void deleteInvQotation(InvInvoiceDTO invInvoiceDTO) {
        if (invInvoiceDTO != null && invInvoiceDTO.getId() > 0) {
            dao.executeDeleteQuery("delete from InvQotationDetail e WHERE e.id > 0 AND e.qotationId.id = " + invInvoiceDTO.getId());
        }
        InvQotation invQotation = new InvQotation();
        invQotation.setId(invInvoiceDTO.getId());
        dao.deleteEntity(invQotation);
    }

    @Override
    public List<InvQotation> getALLInvQotation() {
        return dao.findAll(InvQotation.class);
    }

    @Override
    public List<InvQotation> getALLInvQotationByCompanyId(Integer Id) {
        return dao.findEntityListByCompanyId(InvQotation.class, Id);
    }

    @Override
    public InvQotation updateInvQotation(InvQotation invQotation) {
        return dao.updateEntity(invQotation);
    }

    @Override
    public List<InvQotation> getALLInvQotationByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvQotation> invQotations = dao.findListByQuery("SELECT e FROM InvQotation e WHERE e.branchId.id = :branchId", params);
        return invQotations;
    }

    @Override
    public List<InvQotation> getALLInvQotationByBranchIdByStatus(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvQotation> InvQotations = dao.findListByQuery("SELECT e FROM InvQotation e WHERE e.branchId.id = :branchId and (e.status != 2  OR e.status is null) ", params);
        return InvQotations;
    }

    @Override
    public List<InvQotationDetail> getALLInvQotationDetailsByInvQotation(Integer invQotationId) {
        Map<String, Object> params = new HashMap<>();
        params.put("invQotationId", invQotationId);
        List<InvQotationDetail> details = dao.findListByQuery("SELECT e FROM InvQotationDetail e WHERE e.qotationId.id = :invQotationId", params);
        return details;
    }

    @Override
    public InvQotation findInvQotationByInvQotationId(Integer QuotationId) {
        return dao.findEntityById(InvQotation.class, QuotationId);
    }

    public synchronized Integer getMaxSerialForInvoiceDetail(InvQotation invQotation) {
        Map<String, Object> params = new HashMap<>();
        params.put("qotationId", invQotation.getId());

        Integer serial = dao.findEntityByQuery("SELECT max(e.serial) FROM InvQotationDetail e WHERE e.qotationId.id =:qotationId ", params);

        if (serial == null) {
            return 1;
        } else {
            serial = serial + 1;
            return serial;
        }
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
            List<Integer> id = dao.executeNativeQuery("select id from (select inv_qotation_id as id from inv_purchase_invoice "
                    + " where branch_id = " + invPurchaseInvoiceDTO.getBranchId() + " and inv_qotation_id is not null\n" + " ) as s\n"
                    + " where id = " + invPurchaseInvoiceDTO.getId());
            if (!id.isEmpty()) {
                error.append("\n لا يجوز تعديل هذا السند لأنه تم تحميلة");
            }
        }
    }

    @Override
    public InvInvoiceDTO save(InvInvoiceDTO invPurchaseInvoiceDTO, TobyUser tobyUser) {
        try {
            invPurchaseInvoiceDTO = validateSave(invPurchaseInvoiceDTO);

            InvQotation invQotation;
            if (invPurchaseInvoiceDTO != null && (invPurchaseInvoiceDTO.getMsg() == null || invPurchaseInvoiceDTO.getMsg().isEmpty())) {
                invQotation = mapToEntity(invPurchaseInvoiceDTO, tobyUser);
                if (invQotation.getId() != null) {
                    //بعمل حفظ فى حالة التعديل
                    invQotation = dao.updateEntity(invQotation);
                } else {
                    // فى حالة ان الفاتورة جديدة بروح اجيب السيريال الاول 
                    Integer serial = genericService.getMaxGenericSerialByType(InvQotation.class, invQotation.getBranchId().getId(), null);
                    invQotation.setSerial(serial);

                    invQotation = dao.updateEntity(invQotation);
                    invPurchaseInvoiceDTO.setId(invQotation.getId());
                }

                invQuotationDetailsService.deleteInvQuotationDetailListDTO(invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailsDeletedList());

                String error = invQuotationDetailsService.addInvQuotationDetailsDTO(invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList(), invPurchaseInvoiceDTO, invPurchaseInvoiceDTO.getScreenName(), tobyUser);

                if (error != null && !error.isEmpty()) {
                    invPurchaseInvoiceDTO.setMsg(error);
                    dao.rollbackQuery();
                } else {
                    invPurchaseInvoiceDTO = mapToDTO(invQotation, invPurchaseInvoiceDTO);
                    invPurchaseInvoiceDTO = invGlobalService.updateSummition(invPurchaseInvoiceDTO);
                }

            }
        } catch (Exception e) {
            invPurchaseInvoiceDTO.setMsg(e.toString());
            dao.rollbackQuery();
        }

        return invPurchaseInvoiceDTO;
    }

    @Override
    public InvInvoiceDTO findInvPurchaseOrderById(InvInvoiceDTO invPurchaseInvoiceDto, TobyUser tobyUser) {
        InvQotation invQotation = dao.findEntityById(InvQotation.class, invPurchaseInvoiceDto.getId());
        InvInvoiceDTO invPurchaseInvoiceDTO = mapToDTO(invQotation, invPurchaseInvoiceDto);
        invPurchaseInvoiceDTO = invGlobalService.updateSummition(invPurchaseInvoiceDTO);
        return invPurchaseInvoiceDTO;
    }

    private InvInvoiceDTO mapToDTO(InvQotation entity, InvInvoiceDTO invPurchaseInvoiceDTO) {

        invPurchaseInvoiceDTO = initData(invPurchaseInvoiceDTO, entity);

        return invPurchaseInvoiceDTO;
    }

    private InvInvoiceDTO initData(InvInvoiceDTO invInvoiceDTO, InvQotation entity) {
        invInvoiceDTO.setId(entity.getId());
        invInvoiceDTO.setSerial(entity.getSerial());
        invInvoiceDTO.setCompanyActivity(entity.getBranchId().getCompanyActivity());
        invInvoiceDTO.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);
        invInvoiceDTO.setDate(entity.getDate());
        invInvoiceDTO.setRemarks(entity.getRemark());
        invInvoiceDTO.setDiscount(entity.getDiscount());
        invInvoiceDTO.setDiscountType(entity.getDiscountType() ? 1 : 0);
        invInvoiceDTO.setDateEndOffer(entity.getEndDate());
        invInvoiceDTO.setType(entity.getType());

        if (entity.getCustomerId() != null) {
            invInvoiceDTO.setOrganizationSiteId(organizationSiteService.mapToDTOSmall(entity.getCustomerId()));
        }

        if (entity.getDelegatorId() != null) {
            InventoryDelegatorDTO inventoryDelegatorDTO = new InventoryDelegatorDTO();
            inventoryDelegatorDTO.setId(entity.getDelegatorId().getId());
            inventoryDelegatorDTO.setName(entity.getDelegatorId().getName());
            inventoryDelegatorDTO.setCode(entity.getDelegatorId().getCode());
            invInvoiceDTO.setInvDelegatorId(inventoryDelegatorDTO);
        }
        invInvoiceDTO.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        invInvoiceDTO.setCreatedDate(entity.getCreationDate());
        invInvoiceDTO.setIndex(entity.getSerial());
        invInvoiceDTO.setCompanyId(entity.getCompanyId().getId() != null ? entity.getCompanyId().getId() : null);
        invInvoiceDTO.setCreatedByName(entity.getCreatedBy() != null ? entity.getCreatedBy().getName() : null);
        List<InvDetailDTO> invPurchaseInvoiceDetailsListDTO = invQuotationDetailsService.getAllInvQutationDetailByInvPurchaseInvoiceIdDTO(invInvoiceDTO);
        invInvoiceDTO.setInvPurchaseInvoiceDetailList(invPurchaseInvoiceDetailsListDTO);
        invInvoiceDTO.setInvPurchaseInvoiceDetailsDeletedList(new ArrayList<InvDetailDTO>());
        invInvoiceDTO = invGlobalService.updateSummition(invInvoiceDTO);
        return invInvoiceDTO;
    }

    //mapToEntity
    private InvQotation mapToEntity(InvInvoiceDTO invInvoiceDTO, TobyUser tobyUser) {

        InvQotation invQotation = new InvQotation();

        if (invInvoiceDTO.getOrganizationSiteId() != null) {
            InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
            invOrganizationSite.setId(invInvoiceDTO.getOrganizationSiteId().getId());
            invOrganizationSite.setName(invInvoiceDTO.getOrganizationSiteId().getName());
            invQotation.setCustomerId(invOrganizationSite);
        }

        if (invInvoiceDTO.getInvDelegatorId() != null) {
            InventoryDelegator inventoryDelegator = new InventoryDelegator();
            inventoryDelegator.setId(invInvoiceDTO.getInvDelegatorId().getId());
            invQotation.setDelegatorId(inventoryDelegator);
        }

        invQotation.setId(invInvoiceDTO.getId());
        invQotation.setSerial(invInvoiceDTO.getSerial());
        invQotation.setDate(invInvoiceDTO.getDate());
        invQotation.setRemark(invInvoiceDTO.getRemarks());
        invQotation.setDiscount(invInvoiceDTO.getDiscount());
        invQotation.setDiscountType(invInvoiceDTO.getDiscountType() != 0);
        invQotation.setEndDate(invInvoiceDTO.getDateEndOffer());
        invQotation.setType(invInvoiceDTO.getType());

        if (invInvoiceDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(invInvoiceDTO.getCreatedBy());
            invQotation.setCreatedBy(user);
            invQotation.setCreationDate(invInvoiceDTO.getCreatedDate());
            invQotation.setModifiedBy(tobyUser);
            invQotation.setModificationDate(new Date());
            if (invInvoiceDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(invInvoiceDTO.getCompanyId());
                invQotation.setCompanyId(company);
            }

            if (invInvoiceDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(invInvoiceDTO.getBranchId());
                invQotation.setBranchId(branch);
            }
        } else {
            invQotation.setCreatedBy(tobyUser);
            invQotation.setCreationDate(new Date());
            invQotation.setCompanyId(tobyUser.getCompanyId());
            invQotation.setBranchId(tobyUser.getBranchId());
        }

        return invQotation;
    }

    @Override
    public Long getTotalRegistors(Integer branchId, Map<String, Object> filters) {
        StringBuilder builder = new StringBuilder();
        filters.put("branchId", branchId);
        builder.append("SELECT count(s) FROM InvQotation s WHERE   s.branchId.id = :branchId");
        appendFilter(filters, builder);
        Long details = dao.findEntityByQuery(builder.toString(), filters);
        return details == null ? 0 : details;
    }

    private void appendFilter(Map<String, Object> filters, StringBuilder builder) {
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and ( FUNCTION('DATE_FORMAT',s.date, '%d-%m-%Y')  like CONCAT('%',:globalFilter ,'%') OR  s.serial like CONCAT('%',:globalFilter ,'%') OR FUNCTION('DATE_FORMAT',s.endDate, '%d-%m-%Y')  like CONCAT('%',:globalFilter ,'%')  OR  s.customerId.name like CONCAT('%',:globalFilter ,'%') OR  s.delegatorId.name like CONCAT('%',:globalFilter ,'%')  OR s.remark like CONCAT('%',:globalFilter ,'%'))";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
    }

    @Override
    public List<InvInvoiceDTO> getALLInvQotationByBranchId(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters) {
        filters.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT s FROM InvQotation s WHERE  s.branchId.id = :branchId ");
        appendFilter(filters, builder);
        builder.append(" order by s.serial DESC");
        List<InvQotation> details = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        return returnListDTOForList(details);
    }

    private List<InvInvoiceDTO> returnListDTOForList(List<InvQotation> invQotationList) {
        List<InvInvoiceDTO> invInvoiceDTOList = new ArrayList<>();
        for (InvQotation invQotation : invQotationList) {
            InvInvoiceDTO invInvoiceDTO = new InvInvoiceDTO();
            invInvoiceDTO = initDataForList(invInvoiceDTO, invQotation);
            invInvoiceDTOList.add(invInvoiceDTO);
        }
        return invInvoiceDTOList;
    }

    private InvInvoiceDTO initDataForList(InvInvoiceDTO invInvoiceDTO, InvQotation invQotation) {
        commonData(invInvoiceDTO, invQotation);
        return invInvoiceDTO;
    }

    private void commonData(InvInvoiceDTO invPurchaseInvoiceDTO, InvQotation entity) {
        invPurchaseInvoiceDTO.setId(entity.getId());
        invPurchaseInvoiceDTO.setSerial(entity.getSerial());
        invPurchaseInvoiceDTO.setDate(entity.getDate());
        invPurchaseInvoiceDTO.setDateEndOffer(entity.getDate());
        invPurchaseInvoiceDTO.setIndex(entity.getSerial());
        invPurchaseInvoiceDTO.setPostFlag(entity.getPostFlag());
        invPurchaseInvoiceDTO.setRemarks(entity.getRemark());
        if (entity.getCustomerId() != null) {
            invPurchaseInvoiceDTO.setOrganizationSiteName(entity.getCustomerId().getName());
        }
        if (entity.getDelegatorId() != null) {
            invPurchaseInvoiceDTO.setInvDelegatorName(entity.getDelegatorId().getName());
        }
    }

    private InvQotation findInvQotationByInvQotationId(Integer serial, Integer branchId, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("serial", serial);
        params.put("branchId", branchId);
        params.put("type", type);
        List<InvQotation> details = dao.findListByQuery("SELECT e FROM InvQotation e WHERE e.serial = :serial AND e.branchId.id = :branchId AND e.type = :type", params);
        return (details == null || details.isEmpty()) ? null : details.get(0);
    }

    @Override
    public InvInvoiceDTO convertQotationToPurchaseInvoice(InvInvoiceDTO invInvoiceDTO, Integer type) {
        InvQotation invQotation;
        if (invInvoiceDTO.getNeedLoadedSerial() != null && invInvoiceDTO.getNeedLoadedSerial() != 0) {
            invQotation = findInvQotationByInvQotationId(invInvoiceDTO.getNeedLoadedSerial(), invInvoiceDTO.getBranchId(), type);
        } else {
            invQotation = findInvQotationByInvQotationId(invInvoiceDTO.getInvDailogeDataSelected().getId());
        }
        if (invQotation != null) {
            invInvoiceDTO = mapToDTO(invQotation, invInvoiceDTO);
            modifyReturnData(invInvoiceDTO, invQotation);
        } else {
            invInvoiceDTO.setMsg("هذا السند غير موجود");
        }
        return invInvoiceDTO;
    }

    private void modifyReturnData(InvInvoiceDTO invPurchaseInvoiceDTO, InvQotation invQotation) {
//        if (!invPurchaseInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVPURCHASEINVOICEFORM)) {
        invPurchaseInvoiceDTO.setParent(invPurchaseInvoiceDTO.getId());
//        }
        invPurchaseInvoiceDTO.setId(null);
        invPurchaseInvoiceDTO.setSerial(null);
        invPurchaseInvoiceDTO.setLoadedSerial(invQotation.getSerial());
        invPurchaseInvoiceDTO.setInvQotationId(invQotation.getId());
        List<InvDetailDTO> detailDTOs = new ArrayList<>();
        for (InvDetailDTO invDetailDTO : invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList()) {
            invDetailDTO.setId(null);
            invDetailDTO.setSerial(null);
            invDetailDTO.setIsEdit(Boolean.TRUE);
            detailDTOs.add(invDetailDTO);
        }
        invPurchaseInvoiceDTO.setInvPurchaseInvoiceDetailList(detailDTOs);
    }

}
