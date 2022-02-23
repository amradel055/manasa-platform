/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.reportSignature;

import com.isag.businessservice.ReportSignatureService;
import com.isag.dto.ReportSignatureDTO;
import com.isag.isag.BaseFormBean;
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
@Named(value = "reportSignatureFormMB")
public class ReportSignatureFormMB extends BaseFormBean {

    private ReportSignatureDTO reportSignatureDTO;

    @EJB
    private ReportSignatureService reportSignatureService;

    @PostConstruct
    @Override
    public void init() {
        load();
    }

    @Override
    public void load() {
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("AddReportSignature")) {
            reset();
        } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditReportSignature")) {
            Integer Id = (Integer) getContext().getSessionMap().get("reportSignatureSelected");
            setReportSignatureDTO(reportSignatureService.getReportSignatureDTOByIdANDBranchId(Id, getUserData().getUserBranch().getId()));
        }
    }

    @Override
    public String save() {
        try {
            if (getReportSignatureDTO() != null) {
                getReportSignatureDTO().setBranchId(getUserData().getUserBranch().getId());
                setReportSignatureDTO(reportSignatureService.addReportSignature(getReportSignatureDTO(), getUserData().getUser()));
                msgSave(getReportSignatureDTO().getMsg());
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد  بيانات"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "حدث خطأ!"));
            e.printStackTrace();
            saveError(e, "ReportSignatureMB", "save");

        }
        return "";
    }

    public void reset() {
        try {
            setReportSignatureDTO(new ReportSignatureDTO());
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "resetInvPurchaseInvoiceForm");
        }
    }

    public void loadThePageAgain() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "AddReportSignature");
            exit("../reportSignature/reportSignatureForm.xhtml");
            setReportSignatureDTO(new ReportSignatureDTO());
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "loadThePageAgain");
        }
    }

    public String exit() {
        try {
            exit("../reportSignature/reportSignatureList.xhtml");
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "exit");
        }
        return "";

    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the reportSignatureDTO
     */
    public ReportSignatureDTO getReportSignatureDTO() {
        return reportSignatureDTO;
    }

    /**
     * @param reportSignatureDTO the reportSignatureDTO to set
     */
    public void setReportSignatureDTO(ReportSignatureDTO reportSignatureDTO) {
        this.reportSignatureDTO = reportSignatureDTO;
    }

}
