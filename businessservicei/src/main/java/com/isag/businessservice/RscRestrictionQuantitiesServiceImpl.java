/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.entityBean.RscExtractReport;
import com.isag.businessservice.reports.searchBean.RscContractExtractionWorkunitSearchBean;
import com.isag.businessservice.reports.searchBean.RscExtractReportSearchBean;
import com.isag.core.GenericDAO;
import com.isag.dto.InstProjectGuideDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.RscContractRegisterDTO;
import com.isag.dto.RscExtractRegisterDTO;
import com.isag.dto.RscPriceTenderDTO;
import com.isag.dto.RscQotationPriceTenderDTO;
import com.isag.dto.RscRestrictionQuantitiesDTO;
import com.isag.dto.RscRestrictionQuantitiesDetailDTO;
import com.isag.dto.RscTenderDTO;
import com.isag.entity.InstProjectGuide;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.IsagUser;
import com.isag.entity.RscContractRegister;
import com.isag.entity.RscExtractRegister;
import com.isag.entity.RscRestrictionQuantities;
import com.isag.entity.RscTender;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author hq004
 */
@Stateless
public class RscRestrictionQuantitiesServiceImpl implements RscRestrictionQuantitiesService {

    @PersistenceContext(unitName = "ISAG_PU")
    private EntityManager em;
    @EJB
    private GenericDAO dao;
    @EJB
    private RscRestrictionQuantitiesDetailService rscRestrictionQuantitiesDetailService;
    @EJB
    private GenericService genericService;
    @EJB
    private RscExtractRegisterService rscExtractRegisterService;
    @EJB
    private RscContractRegisterService rscContractRegisterService;

    @Override
    public void saveRscRestrictionQuantities(RscRestrictionQuantitiesDTO rscRestrictionQuantitiesDTO, IsagUser isagUser) {
        RscRestrictionQuantities rscRestrictionQuantities = mapToEntity(rscRestrictionQuantitiesDTO, isagUser);
        dao.saveEntity(rscRestrictionQuantities);
    }
    
    @Override
    public List<RscExtractReport> getAllRscExtractReport(RscExtractReportSearchBean rscExtractReportSearchBean, Integer branchId) {
        
        List<RscRestrictionQuantities> restrictionQuantitieseList = em
                .createNamedQuery("RscRestrictionQuantities.findByRscExtractReport")
                .setParameter("branchId", branchId)
                .setParameter("contractorFrom", rscExtractReportSearchBean.getInvOrganizationSiteDTOFromContractor() == null ? null : rscExtractReportSearchBean.getInvOrganizationSiteDTOFromContractor().getCode())
                .setParameter("contractorTo", rscExtractReportSearchBean.getInvOrganizationSiteDTOToContractor() == null ? null : rscExtractReportSearchBean.getInvOrganizationSiteDTOToContractor().getCode())
                //                .setParameter("customerFrom",fromCustomer )
                //                .setParameter("customerTo",toCustomer)
                .setParameter("dateFrom", rscExtractReportSearchBean.getDateFrom())
                .setParameter("dateTo", rscExtractReportSearchBean.getDateTo())
                .setParameter("extractFrom", rscExtractReportSearchBean.getExtractFrom().trim().isEmpty() ? null : rscExtractReportSearchBean.getExtractFrom())
                .setParameter("extractTo", rscExtractReportSearchBean.getExtractTo().trim().isEmpty() ? null : rscExtractReportSearchBean.getExtractTo())
                .getResultList();
        List<RscExtractReport> rscExtractReportList = cutlist(restrictionQuantitieseList);
        
        for (RscExtractReport rscExtractReport : rscExtractReportList) {            
            rscExtractReport.setRestrictionQuantitiesDetailDTOList(rscRestrictionQuantitiesDetailService.getRestrictionQuantitiesDetailByRestrictionId(rscExtractReport.getId()));
        }
        return rscExtractReportList;
    }
    
    private List<RscExtractReport> cutlist(List<RscRestrictionQuantities> restrictionQuantitieseList) {
        List<RscExtractReport> rscExtractReportList = new ArrayList<>();
        if (restrictionQuantitieseList != null  && !restrictionQuantitieseList.isEmpty() ) {
            for (RscRestrictionQuantities restrictionQuantities : restrictionQuantitieseList) {
                RscExtractReport rscExtractReport = new RscExtractReport();
                rscExtractReport.setId(restrictionQuantities.getId());
                rscExtractReport.setSerial(restrictionQuantities.getSerial());
                rscExtractReport.setContractorName(restrictionQuantities.getInvOrganizationSite().getName());
                rscExtractReport.setDate(restrictionQuantities.getDate());
                rscExtractReportList.add(rscExtractReport);
            }
        }
        return rscExtractReportList;
    }

    @Override
    public List<RscRestrictionQuantitiesDTO> getBranchRscRestrictionQuantitiesActiveByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<RscRestrictionQuantitiesDTO> dTOList = new ArrayList<>();
        List<RscRestrictionQuantities> list = dao.findListByQuery("SELECT acc FROM RscRestrictionQuantities acc WHERE acc.branchId.id=:branchId AND acc.id not in (select r.restrictionId.id from RscExtractRegister r where r.branchId.id=:branchId )", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<RscRestrictionQuantitiesDTO> getBranchRscRestrictionQuantitiesActiveByBranch(Integer branchId, RscContractExtractionWorkunitSearchBean extractionWorkunitSearchBean) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<RscRestrictionQuantitiesDTO> dTOList = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT acc FROM RscRestrictionQuantities acc WHERE acc.branchId.id=:branchId   ");
        if (extractionWorkunitSearchBean.getFromDate() != null) {
            params.put("dateFrom", extractionWorkunitSearchBean.getFromDate());
            query.append(" AND acc.date >= :dateFrom ");
        }
        if (extractionWorkunitSearchBean.getToDate() != null) {
            params.put("dateTO", extractionWorkunitSearchBean.getToDate());
            query.append(" AND acc.date <= :dateTO ");
        }
        if (extractionWorkunitSearchBean.getOwner() != null) {
            params.put("owner", extractionWorkunitSearchBean.getOwner().getId());
            query.append(" AND acc.invOrganizationSite.id = :owner ");
        }

        List<RscRestrictionQuantities> list = dao.findListByQuery(query.toString(), params);

        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<RscRestrictionQuantitiesDTO> getRscRestrictionQuantitiesByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<RscRestrictionQuantitiesDTO> dTOList = new ArrayList<>();
        List<RscRestrictionQuantities> list = dao.findListByQuery("SELECT acc FROM RscRestrictionQuantities acc WHERE acc.branchId.id=:branchId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public Set<Integer> getContractByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        Set<Integer> sets = new HashSet<Integer>();
        List<Integer> list = dao.findListByQuery("SELECT acc.contractId.id FROM RscRestrictionQuantities acc WHERE acc.branchId.id=:branchId", params);
        sets.addAll(list);
        return sets;
    }

    @Override
    public RscRestrictionQuantitiesDTO findRscRestrictionQuantities(Integer restrictionId) {
        if (restrictionId != null) {
            RscRestrictionQuantities quantities = dao.findEntityById(RscRestrictionQuantities.class, restrictionId);
            return mapTODTO(quantities, null, true);
        } else {
            return null;
        }
    }

    @Override
    public void updateRscRestrictionQuantities(Integer restrictionId, Integer extractId) {
        if (restrictionId != null) {
            RscRestrictionQuantities quantities = dao.findEntityById(RscRestrictionQuantities.class, restrictionId);
            RscExtractRegister rscExtractRegister = new RscExtractRegister();
            rscExtractRegister.setId(extractId);
            quantities.setRscExtractRegisterId(rscExtractRegister);
            dao.updateEntity(quantities);
        }
    }

    @Override
    public RscRestrictionQuantitiesDTO addRscRestrictionQuantities(RscRestrictionQuantitiesDTO restrictionQuantitiesDTO, List<RscRestrictionQuantitiesDetailDTO> rscRestrictionQuantitiesDetailsDTOList, List<RscRestrictionQuantitiesDetailDTO> rscRestrictionQuantitiesDetailDeletedList, IsagUser isagUser) {

        RscRestrictionQuantities restrictionQuantities = mapToEntity(restrictionQuantitiesDTO, isagUser);

        if (restrictionQuantities.getId() == null) {
            Integer serial = genericService.getMaxSerialForInvoice(RscRestrictionQuantities.class, restrictionQuantities.getBranchId().getId(), null, null);
            restrictionQuantities.setSerial(serial);
        }

        //------------------------      ْ
        restrictionQuantities = dao.updateEntity(restrictionQuantities);

        if (rscRestrictionQuantitiesDetailsDTOList != null && !rscRestrictionQuantitiesDetailsDTOList.isEmpty()) {
            rscRestrictionQuantitiesDetailService.addRestrictionQuantitiesDetailList(rscRestrictionQuantitiesDetailsDTOList, restrictionQuantities, isagUser);
        }
        if (rscRestrictionQuantitiesDetailDeletedList != null && !rscRestrictionQuantitiesDetailDeletedList.isEmpty()) {
            rscRestrictionQuantitiesDetailService.deleteRestrictionQuantitiesDetailList(rscRestrictionQuantitiesDetailDeletedList);
        }

        restrictionQuantitiesDTO = mapTODTO(restrictionQuantities, restrictionQuantitiesDTO, true);

        return restrictionQuantitiesDTO;

    }

    @Override
    public String deleteRscRestrictionQuantities(RscRestrictionQuantitiesDTO quantitiesDTO, IsagUser isagUser) {
        String error = null;
            rscRestrictionQuantitiesDetailService.deleteRestrictionQuantitiesDetailListByExtractID(quantitiesDTO.getId(), quantitiesDTO.getBranchId());
            RscRestrictionQuantities restrictionQuantities = new RscRestrictionQuantities();
            restrictionQuantities.setId(quantitiesDTO.getId());
            dao.deleteEntity(restrictionQuantities);
        return error;
    }

    @Override
    public List<RscRestrictionQuantitiesDTO> getAllRscRestrictionQuantitiesByBranchIdAndserial(Integer branchId, Integer serial) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        params.put("serial", serial);

        List<RscRestrictionQuantitiesDTO> dTOList = new ArrayList<>();
        List<RscRestrictionQuantities> list = dao.findListByQuery("SELECT acc FROM RscRestrictionQuantities acc WHERE acc.branchId.id = :id AND acc.serial = :serial ", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<RscRestrictionQuantitiesDTO> getAllRscRestrictionQuantitiesByBranchIdAndserialAndID(Integer branchId, Integer serial, Integer selectedId) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        params.put("serial", serial);
        params.put("selectedId", selectedId);
        String sql = "SELECT acc FROM RscRestrictionQuantities acc WHERE acc.id != :selectedId and acc.branchId.id = :id AND acc.serial = :serial ";
        List<RscRestrictionQuantitiesDTO> dTOList = new ArrayList<>();
        List<RscRestrictionQuantities> list = dao.findListByQuery(sql, params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    public RscRestrictionQuantitiesDTO mapTODTO(RscRestrictionQuantities quantities, RscRestrictionQuantitiesDTO dTO, Boolean check) {
        if (dTO == null) {
            dTO = new RscRestrictionQuantitiesDTO();
        }
        if (check) {
            dTO.setRscRestrictionQuantitiesDetailDTOList(rscRestrictionQuantitiesDetailService.getRestrictionQuantitiesDetailByRestrictionId(quantities.getId()));

        }
        initMapToDTO(dTO, quantities);

        return dTO;
    }

    private void initMapToDTO(RscRestrictionQuantitiesDTO dTO, RscRestrictionQuantities entity) {

        if (entity.getTenderId() != null && entity.getTenderId().getId() != null) {
            RscTenderDTO tenderDTO = new RscTenderDTO();
            tenderDTO.setId(entity.getTenderId().getId());
            tenderDTO.setSerial(entity.getTenderId().getSerial());
            tenderDTO.setDocNumber(entity.getTenderId().getDocNumber());
            tenderDTO.setRemark(entity.getTenderId().getRemark());
            tenderDTO.setDate(entity.getTenderId().getDate());
            if (entity.getTenderId().getCustomerId() != null && entity.getTenderId().getCustomerId().getId().toString() != null) {
                InvOrganizationSiteDTO siteDTO = new InvOrganizationSiteDTO();
                siteDTO.setId(entity.getTenderId().getCustomerId().getId());
                siteDTO.setName(entity.getTenderId().getCustomerId().getName());
                tenderDTO.setCustomerId(siteDTO);
            }
            dTO.setTenderId(tenderDTO);
        }

        if (entity.getRscExtractRegisterId() != null && entity.getRscExtractRegisterId() != null) {
            RscExtractRegisterDTO rscExtractRegisterDTO = new RscExtractRegisterDTO();
            rscExtractRegisterDTO.setId(entity.getRscExtractRegisterId().getId());
            dTO.setRscExtractRegisterDTO(rscExtractRegisterDTO);
        }

        if (entity.getContractId() != null && entity.getContractId().getId() != null) {
            RscContractRegisterDTO contractRegisterDTO = new RscContractRegisterDTO();
            contractRegisterDTO.setId(entity.getContractId().getId());
            contractRegisterDTO.setSerial(entity.getContractId().getSerial());
            contractRegisterDTO.setContractTotal(entity.getContractId().getContractTotal());
            contractRegisterDTO.setDocumentNumber(entity.getContractId().getDocumentNumber());

            //-----------------------------------------------   
            if (entity.getContractId() != null && entity.getContractId().getSiteId() != null && entity.getContractId().getSiteId().getId() != null) {
                InvOrganizationSiteDTO siteDTO = new InvOrganizationSiteDTO();
                siteDTO.setId(entity.getContractId().getSiteId().getId());
                siteDTO.setCode(entity.getContractId().getSiteId().getCode());
                siteDTO.setName(entity.getContractId().getSiteId().getName());

                contractRegisterDTO.setSiteId(siteDTO);
            }
            //-----------------------------------------------
            if (entity.getContractId().getRscQotationPriceTenderId() != null && entity.getContractId().getRscQotationPriceTenderId().getId() != null) {
                RscQotationPriceTenderDTO qotationDTO = new RscQotationPriceTenderDTO();
                qotationDTO.setId(entity.getContractId().getRscQotationPriceTenderId().getId());
                qotationDTO.setDocNumber(entity.getContractId().getRscQotationPriceTenderId().getDocNumber());
                qotationDTO.setDate(entity.getContractId().getRscQotationPriceTenderId().getDate());
                qotationDTO.setSerial(entity.getContractId().getRscQotationPriceTenderId().getSerial());
                qotationDTO.setStatus(entity.getContractId().getRscQotationPriceTenderId().getStatus());
                //-------
                if (qotationDTO.getStatus() == null || qotationDTO.getStatus() == 0) {
                    qotationDTO.setQotationStatus("لم يتم");
                } else if (qotationDTO.getStatus() == 1) {
                    qotationDTO.setQotationStatus("تم");
                }
                //--------
                qotationDTO.setRemark(entity.getContractId().getRscQotationPriceTenderId().getRemark());
                if (entity.getContractId().getRscQotationPriceTenderId().getCustomerId() != null && entity.getContractId().getRscQotationPriceTenderId().getCustomerId().getId() != null) {
                    InvOrganizationSiteDTO orgSiteDTO = new InvOrganizationSiteDTO();
                    orgSiteDTO.setId(entity.getContractId().getSiteId() != null ? entity.getContractId().getSiteId().getId() : null);
                    orgSiteDTO.setCode(entity.getContractId().getSiteId() != null ? entity.getContractId().getSiteId().getCode() : null);
                    orgSiteDTO.setName(entity.getContractId().getSiteId() != null ? entity.getContractId().getSiteId().getName() : null);
                    //  dTO.set(orgSiteDTO);
                }
                if (entity.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId() != null && entity.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getId() != null) {
                    RscPriceTenderDTO priceTenderDTO = new RscPriceTenderDTO();
                    priceTenderDTO.setId(entity.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getId());
                    priceTenderDTO.setDate(entity.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getDate());
                    priceTenderDTO.setDocNumber(entity.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getDocNumber());
                    priceTenderDTO.setSerial(entity.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getSerial());
                    priceTenderDTO.setStatus(entity.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getStatus());
                    if (entity.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getCustomerId() != null && entity.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getCustomerId().getId() != null) {
                        InvOrganizationSiteDTO tenderorgDTO = new InvOrganizationSiteDTO();
                        tenderorgDTO.setId(entity.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getCustomerId().getId());
                        tenderorgDTO.setCode(entity.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getCustomerId().getCode());
                        tenderorgDTO.setName(entity.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getCustomerId().getName());
                        priceTenderDTO.setCustomerId(tenderorgDTO);
                    }
                    if (entity.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId() != null && entity.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getId() != null) {
                        RscTenderDTO tenderDTO = new RscTenderDTO();
                        tenderDTO.setId(entity.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getId());
                        tenderDTO.setDocNumber(entity.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getDocNumber());
                        tenderDTO.setDate(entity.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getDate());
                        tenderDTO.setSerial(entity.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getSerial());
                        tenderDTO.setRemark(entity.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getRemark());
                        if (entity.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getCustomerId() != null && entity.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getCustomerId().getId() != null) {
                            InvOrganizationSiteDTO tendersiteDTO = new InvOrganizationSiteDTO();
                            tendersiteDTO.setId(entity.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getCustomerId().getId());
                            tendersiteDTO.setCode(entity.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getCustomerId().getCode());
                            tendersiteDTO.setName(entity.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getTenderId().getCustomerId().getName());
                            tenderDTO.setCustomerId(tendersiteDTO);
                        }

                        dTO.setTenderId(tenderDTO);
                    }
                }

            }
            //-----------------------------------------------
            dTO.setContractId(contractRegisterDTO);
        }
        dTO.setDate(entity.getDate());
        dTO.setId(entity.getId());
        dTO.setSerial(entity.getSerial());
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        if (entity.getInstProjectGuide() != null) {
            InstProjectGuideDTO instProjectGuideDTO = new InstProjectGuideDTO();
            instProjectGuideDTO.setId(entity.getInstProjectGuide().getId());
            instProjectGuideDTO.setCode(entity.getInstProjectGuide().getCode());
            instProjectGuideDTO.setName(entity.getInstProjectGuide().getName());
            dTO.setInstProjectGuideDTO(instProjectGuideDTO);
        }

        if (entity.getInvOrganizationSite() != null) {
            InvOrganizationSiteDTO invOrganizationSiteDTO = new InvOrganizationSiteDTO();
            invOrganizationSiteDTO.setId(entity.getInvOrganizationSite().getId());
            invOrganizationSiteDTO.setCode(entity.getInvOrganizationSite().getCode());
            invOrganizationSiteDTO.setName(entity.getInvOrganizationSite().getName());
            dTO.setContractor(invOrganizationSiteDTO);
        }

        if (entity.getId() != null) {
            Map<String, Object> params = new HashMap();
            params.put("branchId", entity.getBranchId().getId());
            params.put("restrictionId", entity.getId());
            Integer active = dao.findEntityByQuery("select r.restrictionId.id from RscExtractRegister r where  r.restrictionId.id = :restrictionId  AND r.branchId.id= :branchId ", params);
            dTO.setActive(active != null ? Boolean.TRUE : Boolean.FALSE);
        }

    }

    public RscRestrictionQuantities mapToEntity(RscRestrictionQuantitiesDTO dTO, IsagUser isagUser) {
        RscRestrictionQuantities entity = new RscRestrictionQuantities();

        if (dTO.getInstProjectGuideDTO() != null && dTO.getInstProjectGuideDTO().getId() != null) {
            InstProjectGuide instProjectGuide = new InstProjectGuide();
            instProjectGuide.setId(dTO.getInstProjectGuideDTO().getId());
            entity.setInstProjectGuide(instProjectGuide);
        }

        if (dTO.getContractor() != null && dTO.getContractor().getId() != null) {
            InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
            invOrganizationSite.setId(dTO.getContractor().getId());
            entity.setInvOrganizationSite(invOrganizationSite);
        }

        if (dTO.getRscExtractRegisterDTO() != null && dTO.getRscExtractRegisterDTO().getId() != null) {
            RscExtractRegister rscExtractRegister = new RscExtractRegister();
            rscExtractRegister.setId(dTO.getRscExtractRegisterDTO().getId());
            entity.setRscExtractRegisterId(rscExtractRegister);
        }

        if (dTO.getTenderId() != null) {
            RscTender tender = new RscTender();
            tender.setId(dTO.getTenderId().getId());
            tender.setSerial(dTO.getTenderId().getSerial());
            tender.setDocNumber(dTO.getTenderId().getDocNumber());
            tender.setRemark(dTO.getTenderId().getRemark());
            tender.setDate(dTO.getTenderId().getDate());
            if (dTO.getTenderId() != null && dTO.getTenderId().getCustomerId() != null && dTO.getTenderId().getCustomerId().getId().toString() != null) {
                InvOrganizationSite site = new InvOrganizationSite();
                site.setId(dTO.getTenderId().getCustomerId().getId());
                site.setName(dTO.getTenderId().getCustomerId().getName());
                tender.setCustomerId(site);
            }
            entity.setTenderId(tender);

            if (dTO.getTenderId().getId().toString() != null) {
                RscContractRegisterDTO contractRegisterDTO = rscContractRegisterService.getAllRscContractRegisterByBranchIdAndTender(isagUser.getBranchId().getId(), dTO.getTenderId().getId());
                if (contractRegisterDTO != null && contractRegisterDTO.getId().toString() != null) {
                    RscContractRegister contractRegister = new RscContractRegister();
                    contractRegister.setId(contractRegisterDTO.getId());
                    entity.setContractId(contractRegister);
                }
            }
        }

//        if (dTO.getContractId() != null) {
//           
//            
//          
//            contractRegister.setSerial(dTO.getContractId().getSerial());
//            contractRegister.setContractTotal(dTO.getContractId().getContractTotal());
//            contractRegister.setDocumentNumber(dTO.getContractId().getDocumentNumber());
//           
//        }
        entity.setDate(dTO.getDate());
        entity.setSerial(dTO.getSerial());
        if (dTO.getId() == null) {
            entity.setCreatedBy(isagUser);
            entity.setCreationDate(new Date());

        } else {
            IsagUser isagUser1 = new IsagUser();
            isagUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(isagUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);
        }

        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }

    private void returnListDTO(List<RscRestrictionQuantities> list, List<RscRestrictionQuantitiesDTO> dTOList) {
        for (RscRestrictionQuantities restrictionQuantities : list) {
            dTOList.add(mapTODTO(restrictionQuantities, null, false));
        }
    }

}
