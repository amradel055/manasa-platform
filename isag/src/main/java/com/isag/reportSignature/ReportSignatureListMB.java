/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.reportSignature;

import com.isag.businessservice.ReportSignatureService;
import com.isag.dto.ReportSignatureDTO;
import com.isag.isag.BaseFormBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author ahmedEssam
 */
@ViewScoped
@Named(value = "reportSignatureListMB")
public class ReportSignatureListMB extends BaseFormBean {

    private ReportSignatureDTO reportSignatureDTOSelected;
    private List<ReportSignatureDTO> reportSignatureDTOList;

    @EJB
    private ReportSignatureService reportSignatureService;

    @PostConstruct
    @Override
    public void init() {
        load();
    }

    @Override
    public void load() {
        getReportSignatureDTOList();
    }

    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "AddReportSignature");
            exit("../reportSignature/reportSignatureForm.xhtml");

            return "";
        } catch (Exception e) {
            saveError(e, "ReportSignatureListMB", "goToAdd");
            return null;
        }
    }

    public String goToEdit() {
        try {
            if (getReportSignatureDTOSelected() != null && getReportSignatureDTOSelected().getId() != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("reportSignatureSelected", getReportSignatureDTOSelected().getId());
                context.getSessionMap().put("ScreenMode", "EditReportSignature");
                exit("../reportSignature/reportSignatureForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "ReportSignatureListMB", "goToEdit");
            return null;
        }
    }

    public void delete() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            if (getReportSignatureDTOSelected() != null && getReportSignatureDTOSelected().getId() != null) {
                try {
                    reportSignatureService.deleteReportSignatureDTO(getReportSignatureDTOSelected(), getUserData().getUser());
                    setReportSignatureDTOList(null);
                    getReportSignatureDTOList();
                    deletedMeesage();
                } catch (Exception e) {
                    errorMsg(userDDs.get("UNIQE_KEY_ERROR"));
                }
            }
        } catch (Exception ex) {
            saveError(ex, "ReportSignatureListMB", "delete");
        }
    }

    @Override
    public String save() {
        return "";
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the reportSignatureDTOSelected
     */
    public ReportSignatureDTO getReportSignatureDTOSelected() {
        return reportSignatureDTOSelected;
    }

    /**
     * @param reportSignatureDTOSelected the reportSignatureDTOSelected to set
     */
    public void setReportSignatureDTOSelected(ReportSignatureDTO reportSignatureDTOSelected) {
        this.reportSignatureDTOSelected = reportSignatureDTOSelected;
    }

    /**
     * @return the reportSignatureDTOList
     */
    public List<ReportSignatureDTO> getReportSignatureDTOList() {
        reportSignatureDTOList = reportSignatureService.getAllReportSignatureDTOByBranchId(getUserData().getUserBranch().getId());
        if (reportSignatureDTOList == null) {
            reportSignatureDTOList = new ArrayList<>();
        }
        return reportSignatureDTOList;
    }

    /**
     * @param reportSignatureDTOList the reportSignatureDTOList to set
     */
    public void setReportSignatureDTOList(List<ReportSignatureDTO> reportSignatureDTOList) {
        this.reportSignatureDTOList = reportSignatureDTOList;
    }

}
