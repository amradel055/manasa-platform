/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.reports;

import com.toby.businessservice.GeneralBudgetGuideService;
import com.toby.businessservice.reports.searchBean.GeneralBudgetReportSearchBean;
import com.toby.entity.GeneralBudgetGuide;
import com.toby.entity.TobyCompany;
import com.toby.report.entity.GeneralBudgetBean;
import com.toby.toby.BaseGlAccountReportBean;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author hq002
 */
@Named("generalBudgetReportMB")
@ViewScoped
public class GeneralBudgetReportMB extends BaseGlAccountReportBean {

    private List<GeneralBudgetGuide> budgetGuideList;
    private GeneralBudgetGuide budgetGuideSelect;
    private List<GeneralBudgetBean> generalBudgetBeanList;
    private GeneralBudgetReportSearchBean generalBudgetReportSearchBean;
    private Integer budgetGuideId;
    private JasperPrint jasperPrint;
    private String unitNameValue;
    @EJB
    private GeneralBudgetGuideService generalBudgetGuideService;

    @PostConstruct
    public void init() {
        reset();
    }

    public void load() {
        if (getUserData() != null) {
            TobyCompany companyId = getUserData().getCompany();
            if (companyId != null && companyId.getId() != null) {
                budgetGuideList = generalBudgetGuideService.getAllGeneralBudgetGuideByCompanyId(companyId.getId());
            } else {
                budgetGuideList = generalBudgetGuideService.getAllGeneralBudgetGuide();
            }
        }
    }

    @Override
    public void reset() {
        generalBudgetReportSearchBean = new GeneralBudgetReportSearchBean();
    }

    @Override
    public void search() {
        TobyCompany companyId = getUserData().getCompany();
        generalBudgetReportSearchBean.setCompanyId(companyId.getId());
        unitNameValue = generalBudgetReportSearchBean.getItemName();
        budgetGuideList = generalBudgetGuideService.getGeneralBudgetGuide(getGeneralBudgetReportSearchBean());

        Map<String, String> userDDs = getUserData().getUserDDs();

        generalBudgetBeanList = new ArrayList<>(0);
        for (GeneralBudgetGuide generalBudgetGuides : budgetGuideList) {
            GeneralBudgetBean newItem = new GeneralBudgetBean();
            newItem.setNumber(generalBudgetGuides.getNumber());
            newItem.setNameAr(generalBudgetGuides.getNameAr());
            newItem.setNameEn(generalBudgetGuides.getNameEn());
            newItem.setAccGroup(userDDs.get(generalBudgetGuides.getAccGroup().name()));

            generalBudgetBeanList.add(newItem);
        }
    }

    @Override
    public HashMap prepareReport() {

        String PrintedBy = getUserData().getUser().getName();
        String companyName = getUserData().getCompany().getName();
        String companyImage = getUserData().getImageReportPath();
        String branchName = getUserData().getUserBranch().getNameAr();
        String generalBudgetReportText = "تقرير بنود الميزانية";
        String itemNameText = "اسم الصنف";
        String bandNameText = "اسم البند";
        String bandGroupText = "مجموعة البند";
        String seriaFromText = " من مسلسل";
        String serialToText = "الى مسلسل";
        String unitNameText = "اسم الوحدة";
        String serialText = "مسلسل";
        HashMap hashMap = new HashMap();

        if (unitNameValue != null && unitNameValue.length() == 0) {
            unitNameValue = null;
        }
        hashMap.put("itemName", unitNameValue);
        hashMap.put("serialNumberFrom", generalBudgetReportSearchBean.getSerialNumberFrom());
        hashMap.put("serialNumberTo", generalBudgetReportSearchBean.getSerialNumberTo());
        hashMap.put("generalBudgetReportText", generalBudgetReportText);
        hashMap.put("itemNameText", itemNameText);
        hashMap.put("bandNameText", bandNameText);
        hashMap.put("bandGroupText", bandGroupText);
        hashMap.put("seriaFromText", seriaFromText);
        hashMap.put("serialToText", serialToText);
        hashMap.put("unitNameText", unitNameText);
        hashMap.put("PrintedBy", PrintedBy);
        //  hashMap.put("companyImage", companyImage);
        hashMap.put("branchName", branchName);
       
        hashMap.put("serialText", serialText);
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        return hashMap;
    }

    /*@Override
     public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
     if (generalBudgetBeanList != null && !generalBudgetBeanList.isEmpty()) {
     fillReport(prepareReport(), getUserData().getReportPath() + "generalBudgetReport.jasper", generalBudgetBeanList, "pdf");
     } else {
     search();
     fillReport(prepareReport(), getUserData().getReportPath() + "generalBudgetReport.jasper", generalBudgetBeanList, "pdf");
     }
     }*/
    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (generalBudgetBeanList != null && !generalBudgetBeanList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "generalBudgetReport.jasper", generalBudgetBeanList, "pdf");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد نتائج للطباعة"));
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

    public List<GeneralBudgetGuide> getBudgetGuideList() {
        return budgetGuideList;
    }

    public void setBudgetGuideList(List<GeneralBudgetGuide> budgetGuideList) {
        this.budgetGuideList = budgetGuideList;
    }

    public GeneralBudgetGuide getBudgetGuideSelect() {
        return budgetGuideSelect;
    }

    public void setBudgetGuideSelect(GeneralBudgetGuide budgetGuideSelect) {
        this.budgetGuideSelect = budgetGuideSelect;
    }

    public Integer getBudgetGuideId() {
        return budgetGuideId;
    }

    public void setBudgetGuideId(Integer budgetGuideId) {
        this.budgetGuideId = budgetGuideId;
    }

    public JasperPrint getJasperPrint() {
        return jasperPrint;
    }

    public void setJasperPrint(JasperPrint jasperPrint) {
        this.jasperPrint = jasperPrint;
    }

    public GeneralBudgetGuideService getGeneralBudgetGuideService() {
        return generalBudgetGuideService;
    }

    public void setGeneralBudgetGuideService(GeneralBudgetGuideService generalBudgetGuideService) {
        this.generalBudgetGuideService = generalBudgetGuideService;
    }

    public GeneralBudgetReportSearchBean getGeneralBudgetReportSearchBean() {
        return generalBudgetReportSearchBean;
    }

    public void setGeneralBudgetReportSearchBean(GeneralBudgetReportSearchBean generalBudgetReportSearchBean) {
        this.generalBudgetReportSearchBean = generalBudgetReportSearchBean;
    }

    /**
     * @return the generalBudgetBeanList
     */
    public List<GeneralBudgetBean> getGeneralBudgetBeanList() {
        return generalBudgetBeanList;
    }

    /**
     * @param generalBudgetBeanList the generalBudgetBeanList to set
     */
    public void setGeneralBudgetBeanList(List<GeneralBudgetBean> generalBudgetBeanList) {
        this.generalBudgetBeanList = generalBudgetBeanList;
    }

    @Override
    public void checkDate(Boolean dateType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetDateFrom() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetDateTo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
