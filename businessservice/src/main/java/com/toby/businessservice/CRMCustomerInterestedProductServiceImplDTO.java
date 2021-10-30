/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.CrmEventsReportSearchBean;
import com.toby.core.GenericDAO;
import com.toby.dto.CrmCustomerInterestedProductsDTO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.entity.CrmCustomerInterestedProducts;
import com.toby.entity.InvItem;
import com.toby.entity.InvOrganizationSite;
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
 * @author AhmedEssam
 */
@Stateless
public class CRMCustomerInterestedProductServiceImplDTO implements CRMCustomerInterestedProductsServiceDTO {

    @EJB
    private GenericDAO dao;
    @EJB
    private GenericService genericService;

    @Override
    public List<CrmCustomerInterestedProductsDTO> getCrmCustomerInterestedProductsByOrganizationSiteId(Integer organizationSiteId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("organizationSiteId", organizationSiteId);
        sql.append("SELECT c FROM CrmCustomerInterestedProducts c WHERE c.organizationSiteId.id = :organizationSiteId");
        List<CrmCustomerInterestedProducts> detailses = dao.findListByQuery(sql.toString(), params);
        List<CrmCustomerInterestedProductsDTO> list = mapToDTOList(detailses);
        return list;
    }

    @Override
    public List<CrmCustomerInterestedProductsDTO> saveList(List<CrmCustomerInterestedProductsDTO> dtoList, Integer invOrganizationSiteId, TobyUser tobyUser) {
        List<CrmCustomerInterestedProductsDTO> list = new ArrayList<>();
        if (dtoList != null && !dtoList.isEmpty()) {
            for (CrmCustomerInterestedProductsDTO productsDTO : dtoList) {
                InvOrganizationSiteDTO invOrganizationSiteDTO = new InvOrganizationSiteDTO();
                invOrganizationSiteDTO.setId(invOrganizationSiteId);
                productsDTO.setOrganizationSiteId(invOrganizationSiteDTO);
                CrmCustomerInterestedProductsDTO dto = save(productsDTO, tobyUser);
                list.add(dto);
            }
        }
        return list;
    }

    @Override
    public CrmCustomerInterestedProductsDTO save(CrmCustomerInterestedProductsDTO dto, TobyUser tobyUser) {
        try {
            String error = validateSave(dto);
            if (error == null) {
                CrmCustomerInterestedProducts entity;
                if (dto != null && (dto.getMsg() == null || dto.getMsg().isEmpty())) {
                    entity = mapToEntity(dto, tobyUser);
                    if (entity.getId() == null) {
                        Integer serial = genericService.getMaxSerialForInvoice(CrmCustomerInterestedProducts.class, entity.getBranchId().getId(), null, null);
                        entity.setSerial(serial);
                    }
                    entity = dao.updateEntity(entity);

                    dto = mapToDTO(dto, entity);

                }
            } else {
                dto.setMsg(error);
            }
        } catch (Exception e) {
            dto.setMsg(e.toString());
            dao.rollbackQuery();
        }

        return dto;
    }

    @Override
    public String validateSave(CrmCustomerInterestedProductsDTO dto) {
        StringBuilder error = new StringBuilder();

        if (dto != null) {
//            if (dto.getInterestedDegree() == null) {
//                error.append("\n يجب ادخال درجة الاهتمام");
//            }
            if (dto.getOrganizationSiteId() == null) {
                error.append("\n يجب اختيار عميل محتمل");
            }
        } else {
            error.append("لا يوجد تفاصيل");
        }

        return (error == null || error.toString().isEmpty()) ? null : error.toString();
    }

    @Override
    public void deleteCrmCustomerInterestedProductDTO(Integer id, TobyUser tobyUser) {
        if (id != null && id > 0) {
            CrmCustomerInterestedProducts entity = dao.findEntityById(CrmCustomerInterestedProducts.class, id);
            entity.setDeletedDate(new Date());
            entity.setDeletedBy(tobyUser);
            dao.updateEntity(entity);
        }
    }

    @Override
    public void deleteCrmCustomerInterestedProductDTOList(List<CrmCustomerInterestedProductsDTO> list, TobyUser tobyUser) {
        if (list != null && !list.isEmpty()) {
            for (CrmCustomerInterestedProductsDTO products : list) {
                deleteCrmCustomerInterestedProductDTO(products.getId(), tobyUser);
            }
        }
    }

    @Override
    public List<CrmCustomerInterestedProductsDTO> findCrmCustomerInterestedProducts(CrmEventsReportSearchBean eventsReportSearchBean, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        query.append("select c from CrmCustomerInterestedProducts c where c.branchId.id = :branchId ");
        params.put("branchId", branchId);
        if (eventsReportSearchBean.getLeadsFrom() != null && eventsReportSearchBean.getLeadsFrom().getCode() != null) {
            params.put("leadsFrom", eventsReportSearchBean.getLeadsFrom().getCode());
            query.append(" AND c.organizationSiteId.code >= :leadsFrom ");
        }
        if (eventsReportSearchBean.getLeadsTo() != null && eventsReportSearchBean.getLeadsTo().getCode() != null) {
            params.put("leadsTo", eventsReportSearchBean.getLeadsTo().getCode());
            query.append(" AND c.organizationSiteId.code <= :leadsTo");
        }
//        if (eventsReportSearchBean.getInstProjectGuideDTOFrom() != null && eventsReportSearchBean.getInstProjectGuideDTOFrom().getCode() != null) {
//            params.put("projectFrom", eventsReportSearchBean.getInstProjectGuideDTOFrom().getCode());
//            query.append(" AND c.projectId.code >= :projectFrom");
//        }
//        if (eventsReportSearchBean.getInstProjectGuideDTOTo() != null && eventsReportSearchBean.getInstProjectGuideDTOTo().getCode() != null) {
//            params.put("projectTo", eventsReportSearchBean.getInstProjectGuideDTOTo().getCode());
//            query.append("  AND c.projectId.code <= :projectTo ");
//        }
        List<CrmCustomerInterestedProducts> detail = dao.findListByQuery(query.toString(), params);
        List<CrmCustomerInterestedProductsDTO> list = mapToDTOList(detail);
        return list;
    }

    private List<CrmCustomerInterestedProductsDTO> returnListDTO(List<CrmCustomerInterestedProducts> list) {
        List<CrmCustomerInterestedProductsDTO> dTOList = new ArrayList<>();
        for (CrmCustomerInterestedProducts entity : list) {
            dTOList.add(mapToDTO(null, entity));
        }
        return dTOList;
    }

    private List<CrmCustomerInterestedProductsDTO> mapToDTOList(List<CrmCustomerInterestedProducts> entityList) {
        List<CrmCustomerInterestedProductsDTO> crmEventDTOList = new ArrayList<>();
        if (entityList != null && !entityList.isEmpty()) {
            for (CrmCustomerInterestedProducts entity : entityList) {
                CrmCustomerInterestedProductsDTO crmEventDTO = initMapToDTO(entity);
                crmEventDTOList.add(crmEventDTO);
            }
        }
        return crmEventDTOList;
    }

    private CrmCustomerInterestedProductsDTO mapToDTO(CrmCustomerInterestedProductsDTO dto, CrmCustomerInterestedProducts entity) {
        if (dto == null) {
            dto = new CrmCustomerInterestedProductsDTO();
        }
        dto = initMapToDTO(entity);
        return dto;
    }

    private CrmCustomerInterestedProductsDTO initMapToDTO(CrmCustomerInterestedProducts entity) {

        CrmCustomerInterestedProductsDTO dTO = new CrmCustomerInterestedProductsDTO();
        dTO.setId(entity.getId());

        dTO.setAskDate(entity.getAskDate());
        dTO.setInterestedDegree(entity.getInterestedDegree());

        if (entity.getItemId() != null) {
            InvItemDTO invItemDTO = new InvItemDTO();
            invItemDTO.setId(entity.getItemId().getId());
            invItemDTO.setName(entity.getItemId().getName());
            invItemDTO.setCode(entity.getItemId().getCode());
            dTO.setItemId(invItemDTO);
            dTO.setItemIdBak(invItemDTO);
        }

//        if (entity.getProjectId() != null) {
//            InstProjectGuideDTO guideDTO = new InstProjectGuideDTO();
//            guideDTO.setId(entity.getProjectId().getId());
//            guideDTO.setName(entity.getProjectId().getName());
//            guideDTO.setCode(entity.getProjectId().getCode());
//            dTO.setProjectId(guideDTO);
//            dTO.setProjectIdBak(guideDTO);
//        }

        if (entity.getOrganizationSiteId() != null) {
            InvOrganizationSiteDTO invOrganizationSiteDTO = new InvOrganizationSiteDTO();
            invOrganizationSiteDTO.setId(entity.getOrganizationSiteId().getId());
            invOrganizationSiteDTO.setName(entity.getOrganizationSiteId().getName());
            invOrganizationSiteDTO.setCode(entity.getOrganizationSiteId().getCode());
            invOrganizationSiteDTO.setPhone(entity.getOrganizationSiteId().getPhone());
            invOrganizationSiteDTO.setMobile(entity.getOrganizationSiteId().getMobile());
//            if (entity.getOrganizationSiteId().getLearnAboutCompanyfrom() != null) {
//                SymbolDTO symbolDTO = new SymbolDTO();
//                symbolDTO.setName(entity.getOrganizationSiteId().getLearnAboutCompanyfrom().getName());
//                invOrganizationSiteDTO.setLearnAboutCompanyFrom(symbolDTO);
//            }
            dTO.setOrganizationSiteId(invOrganizationSiteDTO);
        }

        dTO.setBranchId(entity.getBranchId() == null ? null : entity.getBranchId().getId());
        dTO.setCompanyId(entity.getCompanyId().getId());
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());

        return dTO;
    }

    private CrmCustomerInterestedProducts mapToEntity(CrmCustomerInterestedProductsDTO dTO, TobyUser tobyUser) {
        CrmCustomerInterestedProducts entity = new CrmCustomerInterestedProducts();

        entity.setAskDate(dTO.getAskDate());
        entity.setInterestedDegree(dTO.getInterestedDegree());

        if ((dTO.getItemId() != null && dTO.getItemId().getId() != null)
                || (dTO.getItemIdBak() != null && dTO.getItemIdBak().getId() != null)) {
            InvItem invItem = new InvItem();
            invItem.setId(dTO.getItemId() != null ? dTO.getItemId().getId() : dTO.getItemIdBak().getId());
            entity.setItemId(invItem);
        }

//        if ((dTO.getProjectId() != null && dTO.getProjectId().getId() != null)
//                || (dTO.getProjectIdBak() != null && dTO.getProjectIdBak().getId() != null)) {
//            InstProjectGuide guide = new InstProjectGuide();
//            guide.setId(dTO.getProjectId() != null ? dTO.getProjectId().getId() : dTO.getProjectIdBak().getId());
//            entity.setProjectId(guide);
//        }

        if (dTO.getOrganizationSiteId() != null && dTO.getOrganizationSiteId().getId() != null) {
            InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
            invOrganizationSite.setId(dTO.getOrganizationSiteId().getId());
            entity.setOrganizationSiteId(invOrganizationSite);
        }

        if (dTO.getId() == null) {
            entity.setCreatedBy(tobyUser);
            entity.setCreationDate(new Date());
        } else {
            TobyUser tobyUser1 = new TobyUser();
            tobyUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(tobyUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(tobyUser);
        }

        entity.setBranchId(tobyUser.getBranchId());
        entity.setCompanyId(tobyUser.getCompanyId());
        return entity;
    }

}
