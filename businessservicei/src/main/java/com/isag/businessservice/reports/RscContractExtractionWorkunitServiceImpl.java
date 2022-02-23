/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.entityBean.RscExtractJournalViewBean;
import com.isag.businessservice.reports.entityBean.RscGuideDTOViewBean;
import com.isag.businessservice.reports.entityBean.completionRateReportEntity;
import com.isag.businessservice.reports.entityBean.rscExtractJournalReportPrintEntity;
import com.isag.businessservice.reports.searchBean.RscContractExtractionWorkunitSearchBean;
import com.isag.core.GenericDAO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.RscWorkUnitsGuideDTO;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.IsagUser;
import com.isag.entity.RscWorkUnitsGuide;
import com.isag.entity.Symbol;
import com.isag.views.RscContractExtractionWorkunitView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Mostafa
 */
@Stateless
public class RscContractExtractionWorkunitServiceImpl implements RscContractExtractionWorkunitService {

    @EJB
    private GenericDAO dao;

    StringBuilder appendQuery;

    @Override
    public List<RscGuideDTOViewBean> getAllRscContractExtractionWorkunitViewBySearchBean(RscContractExtractionWorkunitSearchBean contractExtractionWorkunitSearchBean,IsagUser isagUser) {

        contractExtractionWorkunitSearchBean.setBranchId(isagUser.getBranchId().getId());
        
        List<RscContractExtractionWorkunitView> list = getAllRscContractExtractionWorkUnitViewBySearchBeanCommon(contractExtractionWorkunitSearchBean);

        List<RscGuideDTOViewBean> guideDTOViewBeanList = RscExtractJournalReportBehavior(contractExtractionWorkunitSearchBean, list);

        return guideDTOViewBeanList;
    }

    @Override
    public List<completionRateReportEntity> getAllcompletionRateReportEntityViewBySearchBean(RscContractExtractionWorkunitSearchBean contractExtractionWorkunitSearchBean) {

        List<RscContractExtractionWorkunitView> list = getAllRscContractExtractionWorkUnitViewBySearchBeanCommon(contractExtractionWorkunitSearchBean);
        List<completionRateReportEntity> completionRateReportEntityList = completionRateReportBehaviour(list);
        return completionRateReportEntityList;
    }

    private List<completionRateReportEntity> completionRateReportBehaviour(List<RscContractExtractionWorkunitView> list) {
        List<completionRateReportEntity> rateReportEntityList = new ArrayList<>();
        for (RscContractExtractionWorkunitView view : list) {
            completionRateReportEntity rateReportEntity = new completionRateReportEntity();
//            rateReportEntity.setCompletionRate(view.getCurrentQty().divide(view.()));
            rateReportEntity.setCompletionRate((view.getCurrentQty().divide(view.getContractQty(), 2, BigDecimal.ROUND_CEILING)).multiply(new BigDecimal(100)));

            rateReportEntity.setSiteName(view.getSiteName());
            rateReportEntity.setWorkUnitName(view.getWorkUnitName());
            rateReportEntity.setContractQty(view.getContractQty());
            rateReportEntity.setCurrentQty(view.getCurrentQty());
            rateReportEntity.setOwnerName(view.getOwnerName());
            rateReportEntityList.add(rateReportEntity);

        }
        return rateReportEntityList;
    }

    private List<RscGuideDTOViewBean> RscExtractJournalReportBehavior(RscContractExtractionWorkunitSearchBean contractExtractionWorkunitSearchBean, List<RscContractExtractionWorkunitView> list) {
        List<RscGuideDTOViewBean> guideDTOViewBeanList = new ArrayList<>();
        List<RscExtractJournalViewBean> extractJournalViewBeanList;
        for (RscWorkUnitsGuideDTO guideDTO : contractExtractionWorkunitSearchBean.getWorkUnitList()) {
            RscGuideDTOViewBean guideDTOViewBean = new RscGuideDTOViewBean();
            extractJournalViewBeanList = new ArrayList<>();
            for (RscContractExtractionWorkunitView extractionWorkunitView : list) {
                if (extractionWorkunitView.getWorkUnitId().equals(guideDTO.getId())) {
                    RscExtractJournalViewBean extractJournal = new RscExtractJournalViewBean();
                    extractJournal.setCurrentQty(extractionWorkunitView.getCurrentQty());
                    extractJournal.setExtractDate(extractionWorkunitView.getExtractDate());
                    extractJournalViewBeanList.add(extractJournal);
                }
            }

            guideDTOViewBean.setWorkUnitName(guideDTO.getName());
            guideDTOViewBean.setWorkUnitCode(guideDTO.getCode());
            guideDTOViewBean.setRscExtractJournalViewBeanList(extractJournalViewBeanList);
            guideDTOViewBeanList.add(guideDTOViewBean);
        }

        return guideDTOViewBeanList;
    }

   

    public String printContractorList(RscContractExtractionWorkunitSearchBean contractExtractionWorkunitSearchBean) {
        StringBuilder siteSelected = new StringBuilder();
        //---------------------------
        if (contractExtractionWorkunitSearchBean.getSiteList() != null && !contractExtractionWorkunitSearchBean.getSiteList().isEmpty()
                && contractExtractionWorkunitSearchBean.getSiteList().size() > 0) {

            for (InvOrganizationSiteDTO siteId : contractExtractionWorkunitSearchBean.getSiteList()) {
                if (siteSelected.toString().isEmpty()) {
                    siteSelected.append(siteId.getName());
                } else {
                    siteSelected.append(" , ").append(siteId);
                }
            }

        }
        return siteSelected.toString();
    }

    public String printWorkUnitList(RscContractExtractionWorkunitSearchBean contractExtractionWorkunitSearchBean) {
        //---------------------------
        StringBuilder unitSelected = new StringBuilder();
        if (contractExtractionWorkunitSearchBean.getWorkUnitList() != null && !contractExtractionWorkunitSearchBean.getWorkUnitList().isEmpty()
                && contractExtractionWorkunitSearchBean.getWorkUnitList().size() > 0) {

            for (RscWorkUnitsGuideDTO workUnitId : contractExtractionWorkunitSearchBean.getWorkUnitList()) {
                if (unitSelected.toString().isEmpty()) {
                    unitSelected.append(workUnitId.getName());
                } else {
                    unitSelected.append(" , ").append(workUnitId);
                }
            }

        }
        return unitSelected.toString();
    }

    private List<RscContractExtractionWorkunitView> getAllRscContractExtractionWorkUnitViewBySearchBeanCommon(RscContractExtractionWorkunitSearchBean contractExtractionWorkunitSearchBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", contractExtractionWorkunitSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT RCEV FROM RscContractExtractionWorkunitView RCEV WHERE RCEV.branchId = :branchId ");
        stringBuilder.append(appendDate(contractExtractionWorkunitSearchBean, params));
        stringBuilder.append(appendOrgSite(contractExtractionWorkunitSearchBean, params));
        stringBuilder.append(appendContractor(contractExtractionWorkunitSearchBean, params));
        stringBuilder.append(appendWorkUnit(contractExtractionWorkunitSearchBean, params));
        stringBuilder.append(appendProject(contractExtractionWorkunitSearchBean, params));
        List<RscContractExtractionWorkunitView> list = dao.findListByQuery(stringBuilder.toString(), params);
        return list;
    }

    private String appendOrgSite(RscContractExtractionWorkunitSearchBean contractExtractionWorkunitSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (contractExtractionWorkunitSearchBean.getOwner() != null) {
            params.put("ownerId", contractExtractionWorkunitSearchBean.getOwner().getId());
            appendQuery.append(" AND RCEV.ownerId = :ownerId");
        }

        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String appendDate(RscContractExtractionWorkunitSearchBean contractExtractionWorkunitSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (contractExtractionWorkunitSearchBean.getFromDate() != null) {
            params.put("fromDate", contractExtractionWorkunitSearchBean.getFromDate());
            appendQuery.append(" AND RCEV.extractDate >= :fromDate");
        }

        if (contractExtractionWorkunitSearchBean.getToDate() != null) {
            params.put("toDate", contractExtractionWorkunitSearchBean.getToDate());
            appendQuery.append(" AND RCEV.extractDate <= :toDate");
        }

        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String appendContractor(RscContractExtractionWorkunitSearchBean contractExtractionWorkunitSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (contractExtractionWorkunitSearchBean.getSiteList() != null && !contractExtractionWorkunitSearchBean.getSiteList().isEmpty()
                && contractExtractionWorkunitSearchBean.getSiteList().size() > 0) {
            StringBuilder siteSelected = new StringBuilder();
            for (InvOrganizationSiteDTO siteId : contractExtractionWorkunitSearchBean.getSiteList()) {
                if (siteSelected.toString().isEmpty()) {
                    siteSelected.append(siteId.getId());
                } else {
                    siteSelected.append(" , ").append(siteId.getId());
                }
            }
            appendQuery.append(" AND RCEV.siteId in ( ").append(siteSelected).append(" )");
        }

        return appendQuery.toString();

    }

    private String appendWorkUnit(RscContractExtractionWorkunitSearchBean contractExtractionWorkunitSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (contractExtractionWorkunitSearchBean.getWorkUnitList() != null && !contractExtractionWorkunitSearchBean.getWorkUnitList().isEmpty()
                && contractExtractionWorkunitSearchBean.getWorkUnitList().size() > 0) {
            StringBuilder unitSelected = new StringBuilder();
            for (RscWorkUnitsGuideDTO workUnitId : contractExtractionWorkunitSearchBean.getWorkUnitList()) {
                if (unitSelected.toString().isEmpty()) {
                    unitSelected.append(workUnitId.getId());
                } else {
                    unitSelected.append(" , ").append(workUnitId.getId());
                }
            }
            appendQuery.append(" AND RCEV.workUnitId in ( ").append(unitSelected).append(" ) ");
        }

        return appendQuery.toString();
    }

    private String appendProject(RscContractExtractionWorkunitSearchBean contractExtractionWorkunitSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (contractExtractionWorkunitSearchBean.getContract() != null) {
            params.put("contractId", contractExtractionWorkunitSearchBean.getContract().getId());
            appendQuery.append(" AND RCEV.contractId = :contractId");
        }

        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //---------------------------------------------------------
    @Override
    public List<InvOrganizationSiteDTO> getAllInvOrganizationSiteByContract(Integer contractId, Integer branchId) {
        Map<String, Object> params = new HashMap<>();

        params.put("branchId", branchId);
        params.put("contractId", contractId);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT RCR.siteId  FROM RscContractRegister RCR WHERE RCR.branchId.id = :branchId AND RCR.id=:contractId");
        List<InvOrganizationSiteDTO> dTOList = new ArrayList<>();
        List<InvOrganizationSite> list = dao.findListByQuery(stringBuilder.toString(), params);
        returnListSiteDTO(list, dTOList);
        return dTOList;

    }
    //-----------------------------------------------------------------

    private void returnListSiteDTO(List<InvOrganizationSite> list, List<InvOrganizationSiteDTO> dTOList) {
        for (InvOrganizationSite site : list) {
            dTOList.add(mapTOSiteDTO(site));
        }
    }

    public InvOrganizationSiteDTO mapTOSiteDTO(InvOrganizationSite site) {
        InvOrganizationSiteDTO dTO = new InvOrganizationSiteDTO();
        initMapToSiteDTO(dTO, site);

        return dTO;
    }

    private void initMapToSiteDTO(InvOrganizationSiteDTO dTO, InvOrganizationSite site) {

        dTO.setAddress(site.getAddress());
        dTO.setCode(site.getCode());
        dTO.setName(site.getName());
        dTO.setId(site.getId());

    }

    //-----------------------------------------------------
    @Override
    public List<RscWorkUnitsGuideDTO> getAllWorkunitViewByContract(Integer contractId, Integer branchId) {
        Map<String, Object> params = new HashMap<>();

        params.put("branchId", branchId);
        params.put("contractId", contractId);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT RCEV.workUnitId  FROM RscContractRegisterDetailWorkUnit RCEV WHERE RCEV.branchId.id = :branchId AND RCEV.rscContractRegisterId.id=:contractId");
        List<RscWorkUnitsGuideDTO> dTOList = new ArrayList<>();
        List<RscWorkUnitsGuide> list = dao.findListByQuery(stringBuilder.toString(), params);
        returnListDTO(list, dTOList);
        return dTOList;

    }

    //-----------------------------------------------------------------
    private void returnListDTO(List<RscWorkUnitsGuide> list, List<RscWorkUnitsGuideDTO> dTOList) {
        for (RscWorkUnitsGuide contract : list) {
            dTOList.add(mapTODTO(contract));
        }
    }

    public RscWorkUnitsGuideDTO mapTODTO(RscWorkUnitsGuide contract) {
        RscWorkUnitsGuideDTO dTO = new RscWorkUnitsGuideDTO();
        initMapToDTO(dTO, contract);

        return dTO;
    }

    private void initMapToDTO(RscWorkUnitsGuideDTO dTO, RscWorkUnitsGuide guide) {

        dTO.setActive(guide.getActive());
        dTO.setCode(guide.getCode());
        dTO.setId(guide.getId());
        dTO.setLevel(guide.getLevel());
        dTO.setName(guide.getName());
        dTO.setType(guide.getType());
        if (guide.getUnitId() != null && guide.getUnitId().getId() != null) {
            Symbol unit = new Symbol();
            unit.setId(guide.getUnitId().getId());
            unit.setName(guide.getUnitId().getName());
            unit.setSerial(guide.getUnitId().getSerial());
            dTO.setUnitName(unit);
        }
        dTO.setQuantity(guide.getQuantity());
        dTO.setValue(guide.getValue());

    }

}
