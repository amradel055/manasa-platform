/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rscreport;

import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.reports.RscContractExtractionWorkunitService;
import com.isag.businessservice.reports.searchBean.RscContractExtractionWorkunitSearchBean;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.RscWorkUnitsGuideDTO;
import com.isag.businessservice.reports.entityBean.RscExtractJournalViewBean;
import com.isag.businessservice.reports.entityBean.RscGuideDTOViewBean;
import com.isag.businessservice.reports.entityBean.rscExtractJournalReportPrintEntity;
import com.isag.converter.InvOrganizationSiteDTOConverter;
import com.isag.converter.RscContractRegisterDTOConverter;
import com.isag.converter.RscWorkUnitDTOConverter;
import com.isag.dto.RscContractRegisterDTO;
import com.isag.isag.UserData;
import com.isag.print.IReportService;
import com.isag.print.IReportServiceImpl;
import com.isag.views.RscContractExtractionWorkunitView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author hq004
 */
@Named(value = "rscExtractJournalReportMB")
@ViewScoped
public class RscExtractJournalReportMB extends BaseRSCReportBean {

    //  RscExtractReportSearchBean 

    @EJB
    RscContractExtractionWorkunitService contractExtractionWorkunitService;
    @EJB
    private OrganizationSiteService organizationSiteServiceBean;

    private RscContractExtractionWorkunitSearchBean extractionWorkunitSearchBean;
    private List<RscContractExtractionWorkunitView> rscContractExtractionViewList;
    private List<RscExtractJournalViewBean> extractJournalViewBeanList;
    private List<RscGuideDTOViewBean> guideDTOViewBeanList;
    private rscExtractJournalReportPrintEntity entity;
    private List<rscExtractJournalReportPrintEntity> printEntityList;
    private List<InvOrganizationSiteDTO> organizationSiteDTOList;
    private List<RscWorkUnitsGuideDTO> rscWorkUnitsGuideDTOList;
    private List<RscContractRegisterDTO> contractRegisterDTOList;
    private RscContractExtractionWorkunitView extractionWorkunitView;
    private IReportService iReportService;
    private UserData userData;
    
    
    private RscWorkUnitDTOConverter rscWorkUnitDTOConverterBean;
    private InvOrganizationSiteDTOConverter invOrgSiteDTOConverterBean;
    
    private List<InvOrganizationSiteDTO> customerListDTOBean;
    private InvOrganizationSiteDTOConverter ownerInvOrgSiteDTOConverterBean;
    private RscContractRegisterDTOConverter contractRegisterDTOConverterBean;

    @PostConstruct
    public void init(){
        getCustomerListDTOBean();
    }
    
    @Override
    public void reset() {
        try {
            extractionWorkunitSearchBean = new RscContractExtractionWorkunitSearchBean();
            rscContractExtractionViewList = new ArrayList<>();
            setContractRegisterListDTOByOwnerList(new ArrayList<>());
            setOrganizationSiteDTOList(new ArrayList<>());
            setRscWorkUnitsGuideDTOList(new ArrayList<>());
            guideDTOViewBeanList = new ArrayList<>();
            
            
        } catch (Exception e) {
            saveError(e, "RscExtractJournalReportMB", "reset");
        }

    }

    public void projectWorkUnitsGuide() {
        try {
            extractionWorkunitSearchBean.setSiteList(getOrganizationSiteDTOList());
            extractionWorkunitSearchBean.setWorkUnitList(getRscWorkUnitsGuideDTOList());
        } catch (Exception e) {
            saveError(e, "RscExtractJournalReportMB", "projectWorkUnitsGuide");
        }

    }

    public void ownerProjectView() {
        try {
            getContractRegisterListDTOByOwnerList(extractionWorkunitSearchBean.getOwner().getId());
            getContractRegisterDTOList();
            extractionWorkunitSearchBean.setContract(null);
            extractionWorkunitSearchBean.setSiteList(getOrganizationSiteDTOList());
            extractionWorkunitSearchBean.setWorkUnitList(getRscWorkUnitsGuideDTOList());
        } catch (Exception e) {
            saveError(e, "RscExtractJournalReportMB", "ownerProjectView");
        }

    }

    @Override
    public void search() {
        try {
            guideDTOViewBeanList = contractExtractionWorkunitService.getAllRscContractExtractionWorkunitViewBySearchBean(extractionWorkunitSearchBean , getUserData().getUser());
        } catch (Exception e) {
            saveError(e, "RscExtractJournalReportMB", "search");
        }

    }

    public void print() {
        try {
            List<rscExtractJournalReportPrintEntity> extractJournalReportPrintEntityList = new ArrayList<>();
            rscExtractJournalReportPrintEntity extractJournalReportPrintEntity;
            if (guideDTOViewBeanList != null && !guideDTOViewBeanList.isEmpty()) {
                for (RscGuideDTOViewBean dTOViewBean : guideDTOViewBeanList) {
                    for (RscExtractJournalViewBean bean : dTOViewBean.getRscExtractJournalViewBeanList()) {
                        extractJournalReportPrintEntity = new rscExtractJournalReportPrintEntity();
                        extractJournalReportPrintEntity.setWorkUnitCodeDetail(dTOViewBean.getWorkUnitCode());
                        extractJournalReportPrintEntity.setWorkUnitNameDetail(dTOViewBean.getWorkUnitName());
                        extractJournalReportPrintEntity.setExtractDate(bean.getExtractDate());
                        extractJournalReportPrintEntity.setCurrentQty(bean.getCurrentQty());
                        extractJournalReportPrintEntityList.add(extractJournalReportPrintEntity);
                    }
                }
                getiReportService().printRscExtractJournalReport(extractionWorkunitSearchBean, extractJournalReportPrintEntityList, userData);
            }
        } catch (Exception e) {
            saveError(e, "RscExtractJournalReportMB", "print");
        }
    }
    
     public List<InvOrganizationSiteDTO> completeOwnerInvOrgSite(String query) {
        List<InvOrganizationSiteDTO> filteredInvs = new ArrayList<>();
        try {
            List<InvOrganizationSiteDTO> invOrganizationSites = getCustomerListDTOBean();
            if (query == null || query.trim().equals("")) {

                setInvOrgSiteDTOConverterBean(new InvOrganizationSiteDTOConverter(invOrganizationSites));
                return invOrganizationSites;
            }

            String nameAr;
            String code;
            InvOrganizationSiteDTO invOrgSiteFilter;

            for (int i = 0; i < getCustomerListDTO().size(); i++) {
                invOrgSiteFilter = invOrganizationSites.get(i);
                nameAr = invOrgSiteFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredInvs.contains(invOrgSiteFilter)) {
                        filteredInvs.add(invOrgSiteFilter);
                    }
                }

                code = invOrgSiteFilter.getCode();
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredInvs.contains(invOrgSiteFilter)) {
                        filteredInvs.add(invOrgSiteFilter);
                    }
                }
            }

            setInvOrgSiteDTOConverterBean(new InvOrganizationSiteDTOConverter(filteredInvs));
            return filteredInvs;
        } catch (Exception e) {
            saveError(e, "BaseRSCReportBean", "completeOwnerInvOrgSite");
            return filteredInvs;
        }
    }


    /**
     * @return the extractionWorkunitSearchBean
     */
    public RscContractExtractionWorkunitSearchBean getExtractionWorkunitSearchBean() {
            extractionWorkunitSearchBean = new RscContractExtractionWorkunitSearchBean();
        return extractionWorkunitSearchBean;
    }

    /**
     * @param extractionWorkunitSearchBean the extractionWorkunitSearchBean to
     * set
     */
    public void setExtractionWorkunitSearchBean(RscContractExtractionWorkunitSearchBean extractionWorkunitSearchBean) {
        this.extractionWorkunitSearchBean = extractionWorkunitSearchBean;
    }

    /**
     * @return the rscContractExtractionViewList
     */
    public List<RscContractExtractionWorkunitView> getRscContractExtractionViewList() {

        return rscContractExtractionViewList;
    }

    /**
     * @param rscContractExtractionViewList the rscContractExtractionViewList to
     * set
     */
    public void setRscContractExtractionViewList(List<RscContractExtractionWorkunitView> rscContractExtractionViewList) {
        this.rscContractExtractionViewList = rscContractExtractionViewList;
    }

    /**
     * @return the rscWorkUnitsGuideDTOList
     */
    public List<RscWorkUnitsGuideDTO> getRscWorkUnitsGuideDTOList() {
//        if (rscWorkUnitsGuideDTOList == null || rscWorkUnitsGuideDTOList.isEmpty()) {
            if (extractionWorkunitSearchBean.getContract() != null && extractionWorkunitSearchBean.getContract().getId() != null) {

                rscWorkUnitsGuideDTOList = getRscWorkUnitsGuideDTOByContractList(extractionWorkunitSearchBean.getContract().getId());
                rscWorkUnitDTOConverterBean = new RscWorkUnitDTOConverter(rscWorkUnitsGuideDTOList);
            } else {
                setRscWorkUnitsGuideDTOList(new ArrayList<>());

            }
//        }
        return rscWorkUnitsGuideDTOList;
    }

    /**
     * @param rscWorkUnitsGuideDTOList the rscWorkUnitsGuideDTOList to set
     */
    public void setRscWorkUnitsGuideDTOList(List<RscWorkUnitsGuideDTO> rscWorkUnitsGuideDTOList) {
        this.rscWorkUnitsGuideDTOList = rscWorkUnitsGuideDTOList;
    }

    /**
     * @return the extractJournalViewBeanList
     */
    public List<RscExtractJournalViewBean> getExtractJournalViewBeanList() {

        return extractJournalViewBeanList;
    }

    /**
     * @param extractJournalViewBeanList the extractJournalViewBeanList to set
     */
    public void setExtractJournalViewBeanList(List<RscExtractJournalViewBean> extractJournalViewBeanList) {
        this.extractJournalViewBeanList = extractJournalViewBeanList;
    }

    /**
     * @return the organizationSiteDTOList
     */
    public List<InvOrganizationSiteDTO> getOrganizationSiteDTOList() {
//        if (organizationSiteDTOList == null || organizationSiteDTOList.isEmpty()) {
            if (extractionWorkunitSearchBean.getContract() != null && extractionWorkunitSearchBean.getContract().getId() != null) {
                organizationSiteDTOList = getInvOrganizationSiteDTOByContractList(extractionWorkunitSearchBean.getContract().getId());
                invOrgSiteDTOConverterBean = new InvOrganizationSiteDTOConverter(organizationSiteDTOList);
            } else {
                setOrganizationSiteDTOList(new ArrayList<>());
            }

//        }
        return organizationSiteDTOList;
    }

    /**
     * @param organizationSiteDTOList the organizationSiteDTOList to set
     */
    public void setOrganizationSiteDTOList(List<InvOrganizationSiteDTO> organizationSiteDTOList) {
        this.organizationSiteDTOList = organizationSiteDTOList;
    }

    /**
     * @return the guideDTOViewBeanList
     */
    public List<RscGuideDTOViewBean> getGuideDTOViewBeanList() {
        if (guideDTOViewBeanList == null || guideDTOViewBeanList.isEmpty()) {
            guideDTOViewBeanList = new ArrayList<>();
        }
        return guideDTOViewBeanList;
    }

    /**
     * @param guideDTOViewBeanList the guideDTOViewBeanList to set
     */
    public void setGuideDTOViewBeanList(List<RscGuideDTOViewBean> guideDTOViewBeanList) {
        this.guideDTOViewBeanList = guideDTOViewBeanList;
    }

    @Override
    public HashMap prepareReport() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        
    }

    @Override
    public void exportExcel(ActionEvent actionEvent) throws JRException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportHtml(ActionEvent actionEvent) throws JRException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the entity
     */
    public rscExtractJournalReportPrintEntity getEntity() {
        if (entity == null) {
            entity = new rscExtractJournalReportPrintEntity();
        }
        return entity;
    }

    /**
     * @param entity the entity to set
     */
    public void setEntity(rscExtractJournalReportPrintEntity entity) {
        this.entity = entity;
    }

    /**
     * @return the printEntityList
     */
    public List<rscExtractJournalReportPrintEntity> getPrintEntityList() {
        if (printEntityList == null) {
            printEntityList = new ArrayList<>();
        }
        return printEntityList;
    }

    /**
     * @param printEntityList the printEntityList to set
     */
    public void setPrintEntityList(List<rscExtractJournalReportPrintEntity> printEntityList) {
        this.printEntityList = printEntityList;
    }

    /**
     * @return the extractionWorkunitView
     */
    public RscContractExtractionWorkunitView getExtractionWorkunitView() {
        if (extractionWorkunitView == null) {
            extractionWorkunitView = new RscContractExtractionWorkunitView();
        }
        return extractionWorkunitView;
    }

    /**
     * @param extractionWorkunitView the extractionWorkunitView to set
     */
    public void setExtractionWorkunitView(RscContractExtractionWorkunitView extractionWorkunitView) {
        this.extractionWorkunitView = extractionWorkunitView;
    }

    /**
     * @return the iReportService
     */
    public IReportService getiReportService() {
        if (iReportService == null) {
            iReportService = new IReportServiceImpl();
        }
        return iReportService;
    }

    /**
     * @param iReportService the iReportService to set
     */
    public void setiReportService(IReportService iReportService) {
        this.iReportService = iReportService;
    }

    /**
     * @return the userData
     */
    public UserData getUserData() {
        return userData;
    }

    /**
     * @param userData the userData to set
     */
    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    /**
     * @return the rscWorkUnitDTOConverterBean
     */
    public RscWorkUnitDTOConverter getRscWorkUnitDTOConverterBean() {
        return rscWorkUnitDTOConverterBean;
    }

    /**
     * @param rscWorkUnitDTOConverterBean the rscWorkUnitDTOConverterBean to set
     */
    public void setRscWorkUnitDTOConverterBean(RscWorkUnitDTOConverter rscWorkUnitDTOConverterBean) {
        this.rscWorkUnitDTOConverterBean = rscWorkUnitDTOConverterBean;
    }

    /**
     * @return the invOrgSiteDTOConverterBean
     */
    public InvOrganizationSiteDTOConverter getInvOrgSiteDTOConverterBean() {
        return invOrgSiteDTOConverterBean;
    }

    /**
     * @param invOrgSiteDTOConverterBean the invOrgSiteDTOConverterBean to set
     */
    public void setInvOrgSiteDTOConverterBean(InvOrganizationSiteDTOConverter invOrgSiteDTOConverterBean) {
        this.invOrgSiteDTOConverterBean = invOrgSiteDTOConverterBean;
    }

    /**
     * @return the customerListDTOBean
     */
    public List<InvOrganizationSiteDTO> getCustomerListDTOBean() {
        if(customerListDTOBean==null){
            customerListDTOBean=organizationSiteServiceBean.getorganizationSiteByBranchIdDTO(userData.getUserBranch().getId(), true, 0);
            ownerInvOrgSiteDTOConverterBean = new InvOrganizationSiteDTOConverter(customerListDTOBean);
        }
        return customerListDTOBean;
    }

    /**
     * @param customerListDTOBean the customerListDTOBean to set
     */
    public void setCustomerListDTOBean(List<InvOrganizationSiteDTO> customerListDTOBean) {
        this.customerListDTOBean = customerListDTOBean;
    }

    /**
     * @return the ownerInvOrgSiteDTOConverterBean
     */
    public InvOrganizationSiteDTOConverter getOwnerInvOrgSiteDTOConverterBean() {
        return ownerInvOrgSiteDTOConverterBean;
    }

    /**
     * @param ownerInvOrgSiteDTOConverterBean the ownerInvOrgSiteDTOConverterBean to set
     */
    public void setOwnerInvOrgSiteDTOConverterBean(InvOrganizationSiteDTOConverter ownerInvOrgSiteDTOConverterBean) {
        this.ownerInvOrgSiteDTOConverterBean = ownerInvOrgSiteDTOConverterBean;
    }

    /**
     * @return the contractRegisterDTOList
     */
    public List<RscContractRegisterDTO> getContractRegisterDTOList() {
        if(extractionWorkunitSearchBean.getOwner() != null && extractionWorkunitSearchBean.getOwner().getId() != null){
            contractRegisterDTOList = getContractRegisterListDTOByOwnerList(extractionWorkunitSearchBean.getOwner().getId());
            contractRegisterDTOConverterBean = new RscContractRegisterDTOConverter(contractRegisterDTOList);
        }        
        return contractRegisterDTOList;
    }

    /**
     * @param contractRegisterDTOList the contractRegisterDTOList to set
     */
    public void setContractRegisterDTOList(List<RscContractRegisterDTO> contractRegisterDTOList) {
        this.contractRegisterDTOList = contractRegisterDTOList;
    }

    /**
     * @return the contractRegisterDTOConverterBean
     */
    public RscContractRegisterDTOConverter getContractRegisterDTOConverterBean() {
        return contractRegisterDTOConverterBean;
    }

    /**
     * @param contractRegisterDTOConverterBean the contractRegisterDTOConverterBean to set
     */
    public void setContractRegisterDTOConverterBean(RscContractRegisterDTOConverter contractRegisterDTOConverterBean) {
        this.contractRegisterDTOConverterBean = contractRegisterDTOConverterBean;
    }

   

}
