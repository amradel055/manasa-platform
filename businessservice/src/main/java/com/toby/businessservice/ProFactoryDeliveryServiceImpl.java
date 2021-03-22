/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.define.ScreenNameClassEnum;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.PrintProductionDTO;
import com.toby.entity.Branch;
import com.toby.entity.InvPurchaseInvoice;
import com.toby.entity.ProFactoryDelivery;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq004
 */
@Stateless
public class ProFactoryDeliveryServiceImpl implements ProFactoryDeliveryService {

    @EJB
    private GenericDAO dao;
    @EJB
    private GenericService genericService;
    @EJB
    private ProFactoryDeliveryDetailService proFactoryDeliveryDetailService;
    @EJB
    private InvPurchaseInvoiceDetailsService invPurchaseInvoiceDetailsService;
    @EJB
    InvPurchaseInvoiceService_1 invPurchaseInvoiceService_1;

    @Override
    public Long getTotalRegistors(Integer type, Integer branchId, Map<String, Object> filters) {
        StringBuilder builder = new StringBuilder();
        filters.put("branchId", branchId);
        filters.put("type", type);
        builder.append("SELECT count(s) FROM ProFactoryDelivery s WHERE   s.branchId.id = :branchId AND s.type = :type ");
        appendFilter(filters, builder);
        Long details = dao.findEntityByQuery(builder.toString(), filters);

        return details == null ? 0 : details;
    }

    private void appendFilter(Map<String, Object> filters, StringBuilder builder) {
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and  ( s.invPurchaseInvoiceId.serial like CONCAT('%',:globalFilter ,'%')) ";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
    }

    @Override
    public List<InvInvoiceDTO> getALLProFactoryDeliveryDTOByBranchId(Integer type, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters) {
        filters.put("branchId", branchId);
        filters.put("type", type);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT s FROM ProFactoryDelivery s WHERE   s.branchId.id = :branchId AND s.type = :type ");
        appendFilter(filters, builder);
        builder.append(" order by s.serial DESC");

        List<ProFactoryDelivery> details = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        return returnListDTOForList(details);
    }

    private List<InvInvoiceDTO> returnListDTOForList(List<ProFactoryDelivery> proFactoryDeliveryList) {
        List<InvInvoiceDTO> invInvoiceDTOList = new ArrayList<>();
        for (ProFactoryDelivery invAddingorder : proFactoryDeliveryList) {
            InvInvoiceDTO invInvoiceDTO = new InvInvoiceDTO();
            invInvoiceDTO = initDataForList(invInvoiceDTO, invAddingorder);
            invInvoiceDTOList.add(invInvoiceDTO);
        }
        return invInvoiceDTOList;
    }

    private InvInvoiceDTO initDataForList(InvInvoiceDTO invInvoiceDTO, ProFactoryDelivery factoryDelivery) {
        commonData(invInvoiceDTO, factoryDelivery);
        return invInvoiceDTO;
    }

    private void commonData(InvInvoiceDTO invPurchaseInvoiceDTO, ProFactoryDelivery entity) {
        invPurchaseInvoiceDTO.setId(entity.getId());
        invPurchaseInvoiceDTO.setSerial(entity.getSerial());
        invPurchaseInvoiceDTO.setDate(entity.getDate());
        invPurchaseInvoiceDTO.setIndex(entity.getSerial());
        invPurchaseInvoiceDTO.setRemarks(entity.getRemark());

        if (entity.getInvPurchaseInvoiceId() != null) {
            invPurchaseInvoiceDTO.setInvoiceSerial(entity.getInvPurchaseInvoiceId().getSerial());
            invPurchaseInvoiceDTO.setDueDate(entity.getInvPurchaseInvoiceId().getDueDate());
            if (entity.getInvPurchaseInvoiceId().getDueDate() != null) {
                invPurchaseInvoiceDTO.setDiffrentTime(entity.getInvPurchaseInvoiceId().getDueDate().getTime() - (new Date()).getTime());
            }
            if (entity.getInvPurchaseInvoiceId().getGalaryId() != null) {
                InvInventoryDTO invInventoryDTO = new InvInventoryDTO();
                invInventoryDTO.setId(entity.getInvPurchaseInvoiceId().getGalaryId().getId());
                invInventoryDTO.setName(entity.getInvPurchaseInvoiceId().getGalaryId().getName());
                invInventoryDTO.setCode(entity.getInvPurchaseInvoiceId().getGalaryId().getCode());
                invPurchaseInvoiceDTO.setGallary(invInventoryDTO);
                invPurchaseInvoiceDTO.setGallaryId(invInventoryDTO);
                invPurchaseInvoiceDTO.setGallaryName(invInventoryDTO.getName());

            }
            if (entity.getInvPurchaseInvoiceId().getOrganizationSiteId() != null) {
                InvOrganizationSiteDTO invOrganizationSiteDTO = new InvOrganizationSiteDTO();
                invOrganizationSiteDTO.setId(entity.getInvPurchaseInvoiceId().getOrganizationSiteId().getId());
                invOrganizationSiteDTO.setName(entity.getInvPurchaseInvoiceId().getOrganizationSiteId().getName());
                invOrganizationSiteDTO.setCode(entity.getInvPurchaseInvoiceId().getOrganizationSiteId().getCode());
                invOrganizationSiteDTO.setMobile(entity.getInvPurchaseInvoiceId().getOrganizationSiteId().getMobile());
                invPurchaseInvoiceDTO.setOrganizationSiteName(entity.getInvPurchaseInvoiceId().getOrganizationSiteId().getName());
                invPurchaseInvoiceDTO.setOrganizationSiteId(invOrganizationSiteDTO);
                invPurchaseInvoiceDTO.setCustomerCode(invOrganizationSiteDTO.getCode());
                invPurchaseInvoiceDTO.setCustomerName(invOrganizationSiteDTO.getName());
                invPurchaseInvoiceDTO.setCustomerMobile(invOrganizationSiteDTO.getMobile());
            }
        }

        if (entity.getInvPurchaseInvoiceId() != null) {
            invPurchaseInvoiceDTO.setLoadedSerial(entity.getInvPurchaseInvoiceId().getSerial());
        }
    }

    @Override
    public InvInvoiceDTO findProFactoryDeliveryById(InvInvoiceDTO invInvoiceDTO) {
        ProFactoryDelivery proFactoryDelivery = dao.findEntityById(ProFactoryDelivery.class, invInvoiceDTO.getId());
        InvInvoiceDTO invPurchaseInvoiceDTO = mapToDTO(proFactoryDelivery, invInvoiceDTO, false);
        return invPurchaseInvoiceDTO;
    }

    private InvInvoiceDTO mapToDTO(ProFactoryDelivery entity, InvInvoiceDTO invInvoiceDTO, Boolean isLoaded) {

        invInvoiceDTO = initData(invInvoiceDTO, entity, isLoaded);

        return invInvoiceDTO;
    }

    private InvInvoiceDTO initData(InvInvoiceDTO invInvoiceDTO, ProFactoryDelivery entity, Boolean isLoaded) {
        invInvoiceDTO.setId(entity.getId());
        invInvoiceDTO.setSerial(entity.getInvPurchaseInvoiceId() == null ? null : entity.getSerial());
        invInvoiceDTO.setCompanyActivity(entity.getBranchId().getCompanyActivity());
        invInvoiceDTO.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);

        invInvoiceDTO.setDate(entity.getDate());

        invInvoiceDTO.setRemarks(entity.getRemark());
        invInvoiceDTO.setType(entity.getType());
        invInvoiceDTO.setTime(entity.getTime());

        invInvoiceDTO.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        invInvoiceDTO.setCreatedDate(entity.getCreationDate());
        invInvoiceDTO.setIndex(entity.getSerial());
        invInvoiceDTO.setCompanyId(entity.getCompanyId().getId() != null ? entity.getCompanyId().getId() : null);
        invInvoiceDTO.setCreatedByName(entity.getCreatedBy() != null ? entity.getCreatedBy().getName() : null);

//        invInvoiceDTO.setProFactoryDelivery(entity.getId());
        if (entity.getInvPurchaseInvoiceId() != null) {
            invInvoiceDTO.setLoadedSerial(entity.getInvPurchaseInvoiceId() != null ? entity.getInvPurchaseInvoiceId().getSerial() : null);
            invInvoiceDTO.setInvPurchaseInvoiceId(entity.getInvPurchaseInvoiceId() != null ? entity.getInvPurchaseInvoiceId().getId() : null);
        }

        List<InvDetailDTO> invPurchaseInvoiceDetailsListDTO;
        if (isLoaded) {
//            invPurchaseInvoiceDetailsListDTO = proFactoryDeliveryDetailService.getAllProPurchaseInvoiceDetailsByInvPurchaseInvoiceIdDTO(invInvoiceDTO);
            if (ScreenNameClassEnum.PROSENDTOGALLARY.equals(invInvoiceDTO.getScreenName())) {
                invPurchaseInvoiceDetailsListDTO = proFactoryDeliveryDetailService.getAllInvPurchaseInvoiceDetailsByInvPurchaseInvoiceIdDTO(invInvoiceDTO, 1, 0);
            } else if (ScreenNameClassEnum.PRORECIEVEGALLARY.equals(invInvoiceDTO.getScreenName())) {
                invPurchaseInvoiceDetailsListDTO = proFactoryDeliveryDetailService.getAllInvPurchaseInvoiceDetailsByInvPurchaseInvoiceIdDTO(invInvoiceDTO, 2, 1);
            } else if (ScreenNameClassEnum.PROSALES.equals(invInvoiceDTO.getScreenName())) {
                invPurchaseInvoiceDetailsListDTO = proFactoryDeliveryDetailService.getAllInvPurchaseInvoiceDetailsByInvPurchaseInvoiceIdDTO(invInvoiceDTO, 3, 2);
            } else {
                invPurchaseInvoiceDetailsListDTO = proFactoryDeliveryDetailService.getAllProPurchaseInvoiceDetailsByInvPurchaseInvoiceIdDTO(invInvoiceDTO);
            }

        } else {
            invInvoiceDTO.setIsOld(Boolean.TRUE);
            invPurchaseInvoiceDetailsListDTO = proFactoryDeliveryDetailService.getAllProFactoryDeliveryDetailList(invInvoiceDTO);
        }
        invInvoiceDTO.setInvPurchaseInvoiceDetailsDeletedList(new ArrayList<InvDetailDTO>());
        invInvoiceDTO.setInvPurchaseInvoiceDetailList(invPurchaseInvoiceDetailsListDTO);

        return invInvoiceDTO;
    }

    @Override
    public InvInvoiceDTO convertProFactoryDeliveryToInvInvoice(InvInvoiceDTO invInvoiceDTO, Integer type) {
        ProFactoryDelivery proFactoryDelivery = null;
        if (invInvoiceDTO.getNeedLoadedSerial() != null && invInvoiceDTO.getNeedLoadedSerial() != 0) {
            proFactoryDelivery = findProFactoryDeliveryByInvInvoiceId(invInvoiceDTO.getNeedLoadedSerial(), type, invInvoiceDTO.getBranchId());
            if (proFactoryDelivery != null && proFactoryDelivery.getInvPurchaseInvoiceId() != null && proFactoryDelivery.getInvPurchaseInvoiceId().getId() != null) {
                invInvoiceDTO.setInvPurchaseInvoiceId(proFactoryDelivery.getInvPurchaseInvoiceId().getId());
                invInvoiceDTO.setRemain(invPurchaseInvoiceService_1.remainForInvPurchaseInvoice(proFactoryDelivery.getInvPurchaseInvoiceId().getId()));
            } else {
                invInvoiceDTO.setMsg("لم يتم استلام الفاتوره");
            }
        } else {
            proFactoryDelivery = dao.findEntityById(ProFactoryDelivery.class, invInvoiceDTO.getInvDailogeDataSelected().getId());
        }
        if (proFactoryDelivery != null) {
            if (invInvoiceDTO.getInvDailogeDataSelected() != null && invInvoiceDTO.getInvDailogeDataSelected().getProFactoryDeliveryId() != null) {
                invInvoiceDTO.setProFactoryDeliveryId(invInvoiceDTO.getInvDailogeDataSelected().getProFactoryDeliveryId());
            }
            invInvoiceDTO = mapToDTO(proFactoryDelivery, invInvoiceDTO, true);
            modifyReturnData(invInvoiceDTO, proFactoryDelivery);
            invInvoiceDTO.setOrganizationSiteName((proFactoryDelivery.getInvPurchaseInvoiceId() != null && proFactoryDelivery.getInvPurchaseInvoiceId().getOrganizationSiteId() != null) ? proFactoryDelivery.getInvPurchaseInvoiceId().getOrganizationSiteId().getName() : null);
            invInvoiceDTO.setNumberOfToob(invPurchaseInvoiceDetailsService.getNumberOftoob(proFactoryDelivery.getInvPurchaseInvoiceId().getId()));
        } else {
            invInvoiceDTO.setMsg("حدث خطأ - تم حذفه ");
        }
        return invInvoiceDTO;
    }

    private ProFactoryDelivery findProFactoryDeliveryByInvInvoiceId(Integer serial, Integer type, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("serial", serial);
        params.put("branchId", branchId);
        params.put("type", type);
        List<ProFactoryDelivery> details = dao.findListByQuery("SELECT e FROM ProFactoryDelivery e WHERE e.invPurchaseInvoiceId.serial = :serial AND e.branchId.id = :branchId AND e.type = :type order by e.id asc", params);
        return (details == null || details.isEmpty()) ? null : details.get(0);
    }

    private void modifyReturnData(InvInvoiceDTO invInvoiceDTO, ProFactoryDelivery proFactoryDelivery) {
        invInvoiceDTO.setId(null);
        invInvoiceDTO.setSerial(null);
        invInvoiceDTO.setDate(new Date());

        invInvoiceDTO.setProFactoryDeliveryId(proFactoryDelivery.getId());

        invInvoiceDTO.setLoadedSerial(proFactoryDelivery.getInvPurchaseInvoiceId() == null ? null : proFactoryDelivery.getInvPurchaseInvoiceId().getSerial());
        List<InvDetailDTO> detailDTOs = new ArrayList<>();
        for (InvDetailDTO invDetailDTO : invInvoiceDTO.getInvPurchaseInvoiceDetailList()) {
            invDetailDTO.setId(null);
            invDetailDTO.setSerial(null);
            detailDTOs.add(invDetailDTO);
        }
        invInvoiceDTO.setInvPurchaseInvoiceDetailList(detailDTOs);
    }

    private ProFactoryDelivery mapToEntity(InvInvoiceDTO invoiceDTO, TobyUser tobyUser) {

        ProFactoryDelivery entity = new ProFactoryDelivery();

        entity.setId(invoiceDTO.getId());
        entity.setSerial(invoiceDTO.getSerial());
        entity.setDate(invoiceDTO.getDate());
        entity.setRemark(invoiceDTO.getRemarks());
        entity.setType(invoiceDTO.getType());

        if (invoiceDTO.getInvPurchaseInvoiceId() != null) {
            InvPurchaseInvoice invPurchaseInvoice = new InvPurchaseInvoice();
            invPurchaseInvoice.setId(invoiceDTO.getInvPurchaseInvoiceId());
            entity.setInvPurchaseInvoiceId(invPurchaseInvoice);
        }

        if (invoiceDTO.getProFactoryDeliveryId() != null) {
            ProFactoryDelivery proFactoryDelivery = new ProFactoryDelivery();
            proFactoryDelivery.setId(invoiceDTO.getProFactoryDeliveryId());
            entity.setParent(proFactoryDelivery);
        }

        entity.setTime(invoiceDTO.getTime());

        if (invoiceDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(invoiceDTO.getCreatedBy());
            entity.setCreatedBy(user);
            entity.setCreationDate(invoiceDTO.getCreatedDate());
            entity.setModifiedBy(tobyUser);
            entity.setModificationDate(new Date());
            if (invoiceDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(invoiceDTO.getCompanyId());
                entity.setCompanyId(company);
            }

            if (invoiceDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(invoiceDTO.getBranchId());
                entity.setBranchId(branch);
            }
        } else {
            entity.setCreatedBy(tobyUser);
            entity.setCreationDate(new Date());
            entity.setCompanyId(tobyUser.getCompanyId());
            entity.setBranchId(tobyUser.getBranchId());
        }

        return entity;
    }

    @Override
    public InvInvoiceDTO save(InvInvoiceDTO invInvoiceDTO, TobyUser tobyUser) {
        try {
//            invInvoiceDTO = validateSave(invInvoiceDTO);

            ProFactoryDelivery proFactoryDelivery;
            if (invInvoiceDTO != null && (invInvoiceDTO.getMsg() == null || invInvoiceDTO.getMsg().isEmpty())) {
                proFactoryDelivery = mapToEntity(invInvoiceDTO, tobyUser);
                if (proFactoryDelivery.getId() != null) {
                    proFactoryDelivery = dao.updateEntity(proFactoryDelivery);
                } else {
                    Integer serial = genericService.getMaxGenericSerialByType(ProFactoryDelivery.class, proFactoryDelivery.getBranchId().getId(), invInvoiceDTO.getType());
                    proFactoryDelivery.setSerial(serial);
                    proFactoryDelivery = dao.updateEntity(proFactoryDelivery);
                    invInvoiceDTO.setId(proFactoryDelivery.getId());
                }

                proFactoryDeliveryDetailService.deleteProFactoryDeliveryDetailListDTO(invInvoiceDTO.getInvPurchaseInvoiceDetailsDeletedList());
                String error;
                error = proFactoryDeliveryDetailService.addProFactoryDeliveryDetailsDTO(invInvoiceDTO.getInvPurchaseInvoiceDetailList(), invInvoiceDTO, tobyUser);

                if (error != null && !error.isEmpty()) {
                    invInvoiceDTO.setMsg(error);
                    dao.rollbackQuery();
                } else {
                    invInvoiceDTO = mapToDTO(proFactoryDelivery, invInvoiceDTO, false);
                }

            }
        } catch (Exception e) {
            invInvoiceDTO.setMsg(e.toString());
            dao.rollbackQuery();
        }

        return invInvoiceDTO;
    }

    public InvInvoiceDTO validateSave(InvInvoiceDTO invPurchaseInvoiceDTO) {
        StringBuilder error = new StringBuilder();

        if (invPurchaseInvoiceDTO.getOrganizationSiteId() == null) {
            error.append("\n يجب أختيار عميل / مورد");
        }

        checkIfLoaded(invPurchaseInvoiceDTO, error);

        if (!error.toString().isEmpty()) {
            invPurchaseInvoiceDTO.setMsg(error.toString());
        }
        return invPurchaseInvoiceDTO;
    }

    public void checkIfLoaded(InvInvoiceDTO invPurchaseInvoiceDTO, StringBuilder error) {
        if (invPurchaseInvoiceDTO.getId() != null) {
            List<Integer> id = dao.executeNativeQuery("select id from (select inv_adding_order_id as id from inv_purchase_invoice "
                    + " where branch_id = " + invPurchaseInvoiceDTO.getBranchId() + " and inv_adding_order_id is not null\n" + " ) as s\n"
                    + " where id = " + invPurchaseInvoiceDTO.getId());
            if (!id.isEmpty()) {
                error.append("\n لا يجوز تعديل هذا السند لأنه تم تحميلة");
            }
        }
    }

    @Override
    public void deleteInvAddingOrder(InvInvoiceDTO invoiceDTO) {
        if (invoiceDTO.getId() != null && invoiceDTO.getId() > 0) {
            dao.executeDeleteQuery("delete from ProFactoryDeliveryDetail e WHERE e.id > 0 AND e.proFactoryDeliveryId.id = " + invoiceDTO.getId());
        }
        ProFactoryDelivery entity = new ProFactoryDelivery();
        entity.setId(invoiceDTO.getId());
        dao.deleteEntity(entity);
    }

    @Override
    public List<InvInvoiceDTO> getALLProFactoryDeliveryDTOByStatus(PrintProductionDTO printProductionDTO, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", printProductionDTO.getInvoiceStatus());
        params.put("galaryId", printProductionDTO.getGallaryFrom().getId());
        params.put("dateFrom", printProductionDTO.getFromDate());
        params.put("dateTo", printProductionDTO.getToDate());
        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT s FROM ProFactoryDelivery s WHERE   s.invPurchaseInvoiceId.branchId.id = :branchId AND s.type = :type ");
        if (printProductionDTO.getGallaryFrom() != null) {
            builder.append(" AND  (:galaryId is Null OR  s.invPurchaseInvoiceId.galaryId.id = :galaryId) ");
        }
        if (printProductionDTO.getFromDate() != null) {
            builder.append(" AND   s.date >= :dateFrom ");
        }
        if (printProductionDTO.getToDate() != null) {
            builder.append(" AND   s.date <= :dateTo ");
        }
        builder.append(" order by s.serial DESC");

        List<ProFactoryDelivery> details = dao.findListByQuery(builder.toString(), params);
        return returnListDTOForList(details);
    }

    @Override
    public List<InvInvoiceDTO> getALLProFactoryDeliveryDTOByStatusRemain(PrintProductionDTO printProductionDTO, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("galaryId", printProductionDTO.getGallaryFrom().getId());
        params.put("dateFrom", printProductionDTO.getFromDate());
        params.put("dateTo", printProductionDTO.getToDate());
        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT s FROM ProFactoryDelivery s WHERE   s.invPurchaseInvoiceId.branchId.id = :branchId AND s.type = 0 AND s.invPurchaseInvoiceId.id not in (select p.invPurchaseInvoiceId.id FROM ProFactoryDelivery p WHERE   p.invPurchaseInvoiceId.branchId.id = :branchId AND p.type = 1) ");
        if (printProductionDTO.getGallaryFrom() != null) {
            builder.append(" AND  (:galaryId is Null OR  s.invPurchaseInvoiceId.galaryId.id = :galaryId) ");
        }
        if (printProductionDTO.getFromDate() != null) {
            builder.append(" AND   s.invPurchaseInvoiceId.date >= :dateFrom ");
        }
        if (printProductionDTO.getToDate() != null) {
            builder.append(" AND   s.invPurchaseInvoiceId.date <= :dateTo ");
        }
        builder.append(" order by s.serial DESC");

        List<ProFactoryDelivery> details = dao.findListByQuery(builder.toString(), params);
        return returnListDTOForList(details);
    }
}
