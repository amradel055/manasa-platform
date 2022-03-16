/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.instReport;

import com.isag.businessservice.InstContractService;
import com.isag.businessservice.reports.searchBean.InstallmentSearchBean;
import com.isag.dto.InstContractDTO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author ahmedEssam <ahmedEssam@isag.com>
 */
@Named("soldUnitsReportMB")
@ViewScoped
public class SoldUnitsReportMB extends BaseInstReportBean {

    @EJB
    private InstContractService instContractService;
    private List<InstContractDTO> instContractDTOList;

    @Override
    @PostConstruct
    public void load() {
        reset();
        getInstProjectGuideList();
    }

    @Override
    public void reset() {
        setInstallmentSearchBean(new InstallmentSearchBean());
        getInstallmentSearchBean().setBranchId(getUserData().getSelectedBranch());
        setInstContractDTOList(new ArrayList<>());
    }

    @Override
    public void search() {
        setInstContractDTOList(instContractService.findInstContractByProject(getInstallmentSearchBean()));
    }

    @Override
    public HashMap prepareReport() {
        HashMap hashMap = new HashMap();
        reportManagement(hashMap);
        hashMap.put("reportNameText","الوحدات المباعه");
        hashMap.put("ContractNumberText", "رقم العقد");
        hashMap.put("ContractDateText", "تاريخ العقد");
        hashMap.put("CustomerNameText", "اسم العميل");
        hashMap.put("ProjectNameText", "اسم المشروع");
        hashMap.put("TotalPaymentText", "مبلغ الوحده");
        hashMap.put("FromProjectText", "من مشروع");
        hashMap.put("ToProjectText", "الي مشروع");
        hashMap.put("FromDateText", "من تاريخ");
        hashMap.put("ToDateText", "الي تاريخ");

        if (getInstallmentSearchBean() != null) {
            hashMap.put("FromDateValue", getInstallmentSearchBean().getFromDate() != null ? getInstallmentSearchBean().getFromDate() : null);
            hashMap.put("ToDateValue", getInstallmentSearchBean().getToDate() != null ? getInstallmentSearchBean().getToDate() : null);
            hashMap.put("FromProjectValue", getInstallmentSearchBean().getProjectGuideFrom() != null ? getInstallmentSearchBean().getProjectGuideFrom().getName() : null);
            hashMap.put("ToProjectValue", getInstallmentSearchBean().getProjectGuideTo() != null ? getInstallmentSearchBean().getProjectGuideTo().getName() : null);
        }

        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (getInstContractDTOList() != null && !getInstContractDTOList().isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "soldUnits.jasper", getInstContractDTOList(), "pdf");
        } else {
            errorMessage("لا يوجد بيانات للطباعه");
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
     * @return the instContractDTOList
     */
    public List<InstContractDTO> getInstContractDTOList() {
        if (instContractDTOList == null) {
            instContractDTOList = new ArrayList<>();
        }
        return instContractDTOList;
    }

    /**
     * @param instContractDTOList the instContractDTOList to set
     */
    public void setInstContractDTOList(List<InstContractDTO> instContractDTOList) {
        this.instContractDTOList = instContractDTOList;
    }

}
