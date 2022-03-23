/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.RscExtractRegisterService;
import com.isag.businessservice.reports.entityBean.RscExtractReport;
import com.isag.businessservice.reports.searchBean.ProjectAccountSearchBean;
import com.isag.businessservice.reports.searchBean.RscExtractReportSearchBean;
import com.isag.core.GenericDAO;
import com.isag.dto.InstProjectGuideDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.RscExtractRegisterDTO;
import com.isag.dto.RscExtractRegisterDetailDTO;
import com.isag.dto.RscWorkUnitsGuideDTO;
import com.isag.entity.IsagUser;
import com.isag.entity.RscExtractRegister;
import com.isag.entity.RscExtractRegisterDetail;
import com.isag.views.OrganizationSiteStatementView;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Mostafa
 */
@Stateless
public class RscExtractReportServiceImpl implements RscExtractReportService {
    
    @PersistenceContext(unitName = "ISAG_PU")
    private EntityManager em;
    @EJB
    private GenericDAO dao;
    @EJB
    private RscExtractRegisterService rscExtractRegisterService;
    
    @Override
    public List<RscExtractReport> getAllRscExtractReport(RscExtractReportSearchBean rscExtractReportSearchBean, Integer branchId) {
        String fromCustomer = null, toCustomer = null;
        
        if (rscExtractReportSearchBean.getInvOrganizationSiteDTOFromCustomer() != null && rscExtractReportSearchBean.getInvOrganizationSiteDTOToCustomer() != null) {
            fromCustomer = rscExtractReportSearchBean.getInvOrganizationSiteDTOFromCustomer().getCode();
            toCustomer = rscExtractReportSearchBean.getInvOrganizationSiteDTOToCustomer().getCode();
        }
        List<RscExtractRegister> rscExtractRegisterList = em
                .createNamedQuery("RscExtractRegister.findByRscExtractReport")
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
        List<RscExtractReport> rscExtractReportList = cutlist(rscExtractRegisterList);
        return rscExtractReportList;
    }
    
    private List<RscExtractReport> cutlist(List<RscExtractRegister> rscExtractRegisters) {
        List<RscExtractReport> rscExtractReportList = new ArrayList<>();
        if (rscExtractRegisters != null && !rscExtractRegisters.isEmpty()) {
            for (RscExtractRegister rscExtractRegisterList1 : rscExtractRegisters) {
                RscExtractReport rscExtractReport = new RscExtractReport();
                rscExtractReport.setSerial(rscExtractRegisterList1.getSerial());
                rscExtractReport.setExtractPrice(rscExtractRegisterList1.getNetExtractPrice());
                rscExtractReport.setContractorName(rscExtractRegisterList1.getSiteId().getName());
                rscExtractReport.setDate(rscExtractRegisterList1.getDate());
                rscExtractReportList.add(rscExtractReport);
            }
        }
        return rscExtractReportList;
    }
    
    @Override
    public List<InstProjectGuideDTO> getAllRscExtractRegisterDetailReportMB(ProjectAccountSearchBean projectAccountSearchBean) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", projectAccountSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT e FROM RscExtractRegister e WHERE e.branchId.id = :branchId ");
        appendDateForQueryBuilder(stringBuilder, params, projectAccountSearchBean);
        appendInstProjectGuideDTOForQueryBuilder(stringBuilder, params, projectAccountSearchBean);
        stringBuilder.append(" Group by e.instProjectGuide.name ");
        List<RscExtractRegister> details = dao.findListByQuery(stringBuilder.toString(), params);
        List<InstProjectGuideDTO> dTOList = new ArrayList<>();
        returnListGuideDTO(details, dTOList);
        return dTOList;
        
    }
    
    @Override
    public List<RscExtractRegisterDTO> getAllRscExtractRegisterReportMB(ProjectAccountSearchBean projectAccountSearchBean) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", projectAccountSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT e FROM RscExtractRegister e WHERE e.branchId.id = :branchId ");
        appendDateForQueryBuilder(stringBuilder, params, projectAccountSearchBean);
        appendInstProjectGuideDTOForQueryBuilder(stringBuilder, params, projectAccountSearchBean);
        stringBuilder.append(" AND e.serial IS NOT NULL AND e.instProjectGuide IS NOT NULL");
        stringBuilder.append(" order by e.instProjectGuide.code  ");
        List<RscExtractRegister> details = dao.findListByQuery(stringBuilder.toString(), params);
        List<RscExtractRegisterDTO> dTOList = new ArrayList<>();
        returnListDTO(details, dTOList, projectAccountSearchBean.getDetailed());
        return dTOList;
        
    }
    
    private void appendDateForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, ProjectAccountSearchBean projectAccountSearchBean) {
        if (projectAccountSearchBean.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(projectAccountSearchBean.getDateFrom());
            queryBuilder.append(" and e.date >= '").append(formatDateFrom).append("'");
        }
        
        if (projectAccountSearchBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(projectAccountSearchBean.getDateTo());
            queryBuilder.append(" and e.date <= '").append(formatDateTo).append("'");
        }
    }
    
    private void appendInstProjectGuideDTOForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, ProjectAccountSearchBean projectAccountSearchBean) {
        if (projectAccountSearchBean.getFromInstProjectGuideDTO() != null) {
            params.put("fromProject", projectAccountSearchBean.getFromInstProjectGuideDTO().getCode());
            queryBuilder.append(" and e.instProjectGuide.code >= :fromProject");
        }
        
        if (projectAccountSearchBean.getToInstProjectGuideDTO() != null) {
            params.put("toProject", projectAccountSearchBean.getToInstProjectGuideDTO().getCode());
            queryBuilder.append(" and e.instProjectGuide.code <= :toProject");
        }
    }
    
    private List<RscExtractRegisterDetailDTO> getAllRscExtractRegisterWithDetailReportMB(Integer branchId, Integer rscExtractRegisterId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("rscExtractRegisterId", rscExtractRegisterId);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT r From RscExtractRegisterDetail r WHERE r.rscExtractRegister.branchId.id = :branchId ");
        stringBuilder.append(" AND r.rscExtractRegister.id = :rscExtractRegisterId AND r.rscExtractRegister.serial IS NOT NULL AND r.rscExtractRegister.instProjectGuide IS NOT NULL");
        List<RscExtractRegisterDetail> details = dao.findListByQuery(stringBuilder.toString(), params);
        List<RscExtractRegisterDetailDTO> dTOList = new ArrayList<>();
        returnListDetialDTO(details, dTOList);
        return dTOList;
    }
    
    private void returnListDTO(List<RscExtractRegister> list, List<RscExtractRegisterDTO> dTOList, Boolean detailed) {
        for (RscExtractRegister extractRegister : list) {
            dTOList.add(mapTODTO(extractRegister, detailed));
        }
    }
    
    private void returnListDetialDTO(List<RscExtractRegisterDetail> list, List<RscExtractRegisterDetailDTO> dTOList) {
        for (RscExtractRegisterDetail extractRegisterDetail : list) {
            dTOList.add(mapTODDetialTO(extractRegisterDetail));
        }
    }
    
    private RscExtractRegisterDTO mapTODTO(RscExtractRegister entity, Boolean detailed) {
        RscExtractRegisterDTO dto = new RscExtractRegisterDTO();
        dto.setSerial(entity.getSerial());
        dto.setDate(entity.getDate());
        dto.setBranchId(entity.getBranchId().getId());
        dto.setId(entity.getId());
        if (entity.getInstProjectGuide() != null) {
            InstProjectGuideDTO guideDTO = new InstProjectGuideDTO();
            guideDTO.setCode(entity.getInstProjectGuide().getCode());
            guideDTO.setName(entity.getInstProjectGuide().getName());
            guideDTO.setId(entity.getInstProjectGuide().getId());
            dto.setInstProjectGuideDTO(guideDTO);
        }
        if (entity.getSiteId() != null) {
            InvOrganizationSiteDTO invOrganizationSiteDTO = new InvOrganizationSiteDTO();
            invOrganizationSiteDTO.setName(entity.getSiteId().getName());
            dto.setSiteId(invOrganizationSiteDTO);
        }
        dto.setRemarks(entity.getRemarks());
        dto.setNetExtractPrice(entity.getNetExtractPrice());
        if (detailed) {
            dto.setRscExtractRegisterDetailDTOList(getAllRscExtractRegisterWithDetailReportMB(entity.getBranchId().getId(), entity.getId()));
            
        }
        return dto;
    }
    
    private RscExtractRegisterDetailDTO mapTODDetialTO(RscExtractRegisterDetail entity) {
        RscExtractRegisterDetailDTO dtoDetial = new RscExtractRegisterDetailDTO();
        if (entity.getWorkUnitId() != null) {
            RscWorkUnitsGuideDTO rscWorkUnitsGuideDTO = new RscWorkUnitsGuideDTO();
            rscWorkUnitsGuideDTO.setCode(entity.getWorkUnitId().getCode());
            rscWorkUnitsGuideDTO.setName(entity.getWorkUnitId().getName());
            dtoDetial.setGuideDTO(rscWorkUnitsGuideDTO);
        }
        dtoDetial.setExcuteQty(entity.getExcuteQuantity());
        dtoDetial.setPriceSell(entity.getPriceSell());
        dtoDetial.setCurrentValue(entity.getCurrentValue());
        dtoDetial.setDiscountValue(entity.getDiscountValue());
        dtoDetial.setDiscountPercentage(entity.getDiscountPercentage());
        dtoDetial.setNetCost(entity.getNetCost());
        dtoDetial.setMaster(Boolean.FALSE);
        return dtoDetial;
    }
    
    private RscExtractRegisterDetailDTO mapTODDetialTONull(RscExtractRegisterDetail entity) {
        RscExtractRegisterDetailDTO dtoDetial = new RscExtractRegisterDetailDTO();
        RscWorkUnitsGuideDTO rscWorkUnitsGuideDTO = new RscWorkUnitsGuideDTO();
        dtoDetial.setGuideDTO(rscWorkUnitsGuideDTO);
        
        dtoDetial.setExcuteQty(entity.getExcuteQuantity());
        dtoDetial.setPriceSell(entity.getPriceSell());
        dtoDetial.setCurrentValue(entity.getCurrentValue());
        dtoDetial.setDiscountValue(entity.getDiscountValue());
        dtoDetial.setDiscountPercentage(entity.getDiscountPercentage());
        dtoDetial.setNetCost(entity.getNetCost());
        return dtoDetial;
    }
    
    private void returnListGuideDTO(List<RscExtractRegister> list, List<InstProjectGuideDTO> dTOList) {
        for (RscExtractRegister extractRegister : list) {
            dTOList.add(mapTOGuideDTO(extractRegister));
        }
    }
    
    private InstProjectGuideDTO mapTOGuideDTO(RscExtractRegister entity) {
        InstProjectGuideDTO guideDTO = new InstProjectGuideDTO();
        if (entity.getInstProjectGuide() != null) {
            guideDTO.setCode(entity.getInstProjectGuide().getCode());
            guideDTO.setName(entity.getInstProjectGuide().getName());
        }
        return guideDTO;
    }
    
    @Override
    public List<RscExtractRegisterDTO> getAllRscExtractRegisterReport(ProjectAccountSearchBean projectAccountSearchBean) {
        List<RscExtractRegisterDTO> extractRegisterDTOList = getAllRscExtractRegisterReportMB(projectAccountSearchBean);
        List<RscExtractRegisterDTO> result = new ArrayList<>();
        Map<Integer, InstProjectGuideDTO> map = new HashMap<>();
        BigDecimal totle = BigDecimal.ZERO;
        for (RscExtractRegisterDTO rscExtractRegisterDTO : extractRegisterDTOList) {
            if (!map.containsKey(rscExtractRegisterDTO.getInstProjectGuideDTO().getId())) {
                RscExtractRegisterDTO registerDTO = new RscExtractRegisterDTO();
                InstProjectGuideDTO instProjectGuideDTO = new InstProjectGuideDTO();
                instProjectGuideDTO.setName(rscExtractRegisterDTO.getInstProjectGuideDTO().getName());
                instProjectGuideDTO.setCode(rscExtractRegisterDTO.getInstProjectGuideDTO().getCode());
                map.put(rscExtractRegisterDTO.getInstProjectGuideDTO().getId(), instProjectGuideDTO);
                registerDTO.setInstProjectGuideDTO(instProjectGuideDTO);
                registerDTO.setRowExpansion(Boolean.FALSE);
                registerDTO.setId(rscExtractRegisterDTO.getId());

                RscWorkUnitsGuideDTO rscWorkUnitsGuideDTO = new RscWorkUnitsGuideDTO();
//                RscExtractRegisterDetailDTO de = new RscExtractRegisterDetailDTO();
                RscExtractRegisterDTO extractRegisterDTO = rscExtractRegisterService.getAllRscExtractRegisterById(rscExtractRegisterDTO.getId());
//                de.setGuideDTO(rscWorkUnitsGuideDTO);
//                de.setMaster(Boolean.TRUE);
                List<RscExtractRegisterDetailDTO> li = new ArrayList<>();
//                li.add(de);
                
                registerDTO.setRscExtractRegisterDetailDTOList(extractRegisterDTO.getRscExtractRegisterDetailDTOList());
                result.add(registerDTO);
            }
            //      rscExtractRegisterDTO.setInstProjectGuideDTO(null);
            totle = totle.add(rscExtractRegisterDTO.getNetExtractPrice());
            rscExtractRegisterDTO.setRowExpansion(Boolean.TRUE);
            result.add(rscExtractRegisterDTO);
        }
        extractRegisterDTOList = null;
        map = null;
        if (result != null && !result.isEmpty()) {
            result.get(result.size() - 1 >= 0 ? result.size() - 1 : 0).setTotalFinal(totle);
        }
        return result;
    }
}
