/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rscreport;

import com.isag.businessservice.reports.RscContractExtractionWorkunitService;
import com.isag.businessservice.reports.entityBean.completionRateReportEntity;
import com.isag.businessservice.reports.searchBean.RscContractExtractionWorkunitSearchBean;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.RscWorkUnitsGuideDTO;
import com.isag.isag.UserData;
import com.isag.print.IReportService;
import com.isag.print.IReportServiceImpl;
import com.isag.views.RscContractExtractionWorkunitView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author Mostafa
 */
@Named(value = "completionRateReportMB")
@ViewScoped
public class completionRateReportMB extends BaseRSCReportBean {

    private RscContractExtractionWorkunitSearchBean rscContractExtractionWorkunitSearchBean;
    private List<RscContractExtractionWorkunitView> rscContractExtractionWorkunitViewList;
    private List<completionRateReportEntity> completionRateReportEntityList;
    private List<InvOrganizationSiteDTO> invOrganizationSiteDTOList;
    private List<RscWorkUnitsGuideDTO> rscWorkUnitsGuideDTOList;
    private completionRateReportEntity printEntity;
    private IReportService iReportService;

    private UserData userData;

    @EJB
    RscContractExtractionWorkunitService rscContractExtractionWorkunitService;

    @Override
    public void reset() {
        try {
            rscContractExtractionWorkunitSearchBean = new RscContractExtractionWorkunitSearchBean();
            rscContractExtractionWorkunitViewList = new ArrayList<>();
            completionRateReportEntityList = new ArrayList<>();
            invOrganizationSiteDTOList = new ArrayList<>();
            rscWorkUnitsGuideDTOList = new ArrayList<>();

        } catch (Exception e) {
            saveError(e, "completionRateReportMB", "reset");
        }
    }

    public void projectWorkUnitsGuide() {
        try {
            getRscWorkUnitsGuideDTOList();
            getInvOrganizationSiteDTOList();
        } catch (Exception e) {
            saveError(e, "completionRateReportMB", "projectWorkUnitsGuide");
        }
    }

    public void ownerProjectView() {
        try {
            getContractRegisterListDTOByOwnerList(getRscContractExtractionWorkunitSearchBean().getOwner().getId());
        } catch (Exception e) {
            saveError(e, "RscExtractJournalReportMB", "ownerProjectView");
        }

    }

    @Override
    public void search() {
        try {

            completionRateReportEntityList = rscContractExtractionWorkunitService.getAllcompletionRateReportEntityViewBySearchBean(getRscContractExtractionWorkunitSearchBean());
        } catch (Exception e) {
            saveError(e, "completionRateReportMB", "search");
        }
    }

    @Override
    public HashMap prepareReport() {

        return null;

    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (completionRateReportEntityList != null && !completionRateReportEntityList.isEmpty()) {
            for (completionRateReportEntity printEntity1 : completionRateReportEntityList) {
                printEntity = printEntity1;

            }
            getiReportService().printCompletionRateReportMB(getRscContractExtractionWorkunitSearchBean(), printEntity, completionRateReportEntityList, userData);
        }
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
     * @return the rscContractExtractionWorkunitSearchBean
     */
    public RscContractExtractionWorkunitSearchBean getRscContractExtractionWorkunitSearchBean() {
        try {
            if (rscContractExtractionWorkunitSearchBean == null) {
                rscContractExtractionWorkunitSearchBean = new RscContractExtractionWorkunitSearchBean();
                rscContractExtractionWorkunitSearchBean.setBranchId(getUserData().getUserBranch().getId());

            }

            return rscContractExtractionWorkunitSearchBean;
        } catch (Exception e) {
            saveError(e, "completionRateReportMB", "getRscContractExtractionWorkunitSearchBean");
        }
        return null;
    }

    /**
     * @param rscContractExtractionWorkunitSearchBean the
     * rscContractExtractionWorkunitSearchBean to set
     */
    public void setRscContractExtractionWorkunitSearchBean(RscContractExtractionWorkunitSearchBean rscContractExtractionWorkunitSearchBean) {
        this.rscContractExtractionWorkunitSearchBean = rscContractExtractionWorkunitSearchBean;
    }

    /**
     * @return the rscContractExtractionWorkunitViewList
     */
    public List<RscContractExtractionWorkunitView> getRscContractExtractionWorkunitViewList() {
        try {
            if (rscContractExtractionWorkunitViewList == null || rscContractExtractionWorkunitViewList.isEmpty()) {
                rscContractExtractionWorkunitViewList = new ArrayList<>();
            }
            return rscContractExtractionWorkunitViewList;
        } catch (Exception e) {
            saveError(e, "completionRateReportMB", "getRscContractExtractionWorkunitViewList");
        }
        return null;
    }

    /**
     * @param rscContractExtractionWorkunitViewList the
     * rscContractExtractionWorkunitViewList to set
     */
    public void setRscContractExtractionWorkunitViewList(List<RscContractExtractionWorkunitView> rscContractExtractionWorkunitViewList) {
        this.rscContractExtractionWorkunitViewList = rscContractExtractionWorkunitViewList;
    }

    /**
     * @return the completionRateReportEntityList
     */
    public List<completionRateReportEntity> getCompletionRateReportEntityList() {
        if (completionRateReportEntityList == null || completionRateReportEntityList.isEmpty()) {
            completionRateReportEntityList = new ArrayList<>();
        }
        return completionRateReportEntityList;
    }

    /**
     * @param completionRateReportEntityList the completionRateReportEntityList
     * to set
     */
    public void setCompletionRateReportEntityList(List<completionRateReportEntity> completionRateReportEntityList) {
        this.completionRateReportEntityList = completionRateReportEntityList;
    }

    public List<InvOrganizationSiteDTO> getInvOrganizationSiteDTOList() {
        if (invOrganizationSiteDTOList == null || invOrganizationSiteDTOList.isEmpty()) {
            if (getRscContractExtractionWorkunitSearchBean().getContract() != null && getRscContractExtractionWorkunitSearchBean().getContract().getId() != null) {
                invOrganizationSiteDTOList = getInvOrganizationSiteDTOByContractList(getRscContractExtractionWorkunitSearchBean().getContract().getId());
            } else {
                invOrganizationSiteDTOList = new ArrayList<>();
            }

        }
        return invOrganizationSiteDTOList;
    }

    /**
     * @param invOrganizationSiteDTOList the invOrganizationSiteDTOList to set
     */
    public void setInvOrganizationSiteDTOList(List<InvOrganizationSiteDTO> invOrganizationSiteDTOList) {
        this.invOrganizationSiteDTOList = invOrganizationSiteDTOList;
    }

    /**
     * @return the rscWorkUnitsGuideDTOList
     */
    public List<RscWorkUnitsGuideDTO> getRscWorkUnitsGuideDTOList() {
        if (rscWorkUnitsGuideDTOList == null || rscWorkUnitsGuideDTOList.isEmpty()) {
            if (getRscContractExtractionWorkunitSearchBean().getContract() != null && getRscContractExtractionWorkunitSearchBean().getContract().getId() != null) {

                rscWorkUnitsGuideDTOList = getRscWorkUnitsGuideDTOByContractList(getRscContractExtractionWorkunitSearchBean().getContract().getId());

            } else {
                rscWorkUnitsGuideDTOList = new ArrayList<>();

            }
        }

        return rscWorkUnitsGuideDTOList;
    }

    /**
     * @param rscWorkUnitsGuideDTOList the rscWorkUnitsGuideDTOList to set
     */
    public void setRscWorkUnitsGuideDTOList(List<RscWorkUnitsGuideDTO> rscWorkUnitsGuideDTOList) {
        this.rscWorkUnitsGuideDTOList = rscWorkUnitsGuideDTOList;
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
     * @return the printEntity
     */
    public completionRateReportEntity getPrintEntity() {
        if (printEntity == null) {
            printEntity = new completionRateReportEntity();
        }
        return printEntity;
    }

    /**
     * @param printEntity the printEntity to set
     */
    public void setPrintEntity(completionRateReportEntity printEntity) {
        this.printEntity = printEntity;
    }

}
