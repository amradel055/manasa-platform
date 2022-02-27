package com.toby.reports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.toby.businessservice.CostCenterService;
import com.toby.businessservice.reports.searchBean.CostCenterSearchBean;
import com.toby.converter.CostCenterConverter;
import com.toby.entity.CostCenter;
import com.toby.report.entity.CostCenterIndexBean;
import com.toby.toby.BaseGlAccountReportBean;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

@Named("costCenterIndexMB")
@ViewScoped
public class CostCenterIndexMB extends BaseGlAccountReportBean {

    private List<CostCenter> costCenter;
    private List<CostCenterIndexBean> costCenterIndexBeanList;
    private JasperPrint jasperPrint;

    private CostCenterSearchBean costCenterSearchBean;

    @EJB
    private CostCenterService costCenterService;

    private CostCenterConverter costCenterConverter;

    public CostCenterIndexMB() {
    }

    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
//        userData = (UserData) context.getSessionMap().get("userLogInData");

        reset();
        load();

        costCenterConverter = new CostCenterConverter(getCostCenterList());
    }

    @Override
    public void reset() {

        //complexRevisionBalanceSearchBean = new ComplexRevisionBalanceSearchBean();
        costCenterSearchBean = new CostCenterSearchBean();
    }

    @Override
    public void search() {
        costCenterIndexBeanList = new ArrayList<>(0);
        costCenterSearchBean.setBranchId(getUserData().getUserBranch().getId());
        costCenter = costCenterService.getAllCostCentersIndex(costCenterSearchBean);
        CostCenterIndexBean costCenterIndexBean;
        for (CostCenter center : costCenter) {
            costCenterIndexBean = new CostCenterIndexBean();
            if (center.getActive()) {
                costCenterIndexBean.setActive("نشط");
            } else {
                costCenterIndexBean.setActive("غير نشط");
            }
            costCenterIndexBean.setName(center.getName());
            costCenterIndexBean.setCode(center.getCode());
            costCenterIndexBean.setShort_code(center.getShortCode());
            costCenterIndexBean.setLevel(center.getLevel());
            costCenterIndexBean.setParent(center.getParent() != null ? (center.getParent().getName() != null ? center.getParent().getName() : null) : null);

            costCenterIndexBeanList.add(costCenterIndexBean);
        }
    }

    public JasperPrint getJasperPrint() {
        return jasperPrint;
    }

    public void setJasperPrint(JasperPrint jasperPrint) {
        this.jasperPrint = jasperPrint;
    }

    @Override
    public HashMap prepareReport() {

        Map<String, String> userDDs = getUserData().getUserDDs();

        HashMap hashMap = new HashMap();
      
        hashMap.put("costCenterIdFrom", costCenterSearchBean.getCostCenterIdFrom() != null ? costCenterSearchBean.getCostCenterIdFrom().getCode() : null);
        hashMap.put("costCenterNameFrom", costCenterSearchBean.getCostCenterIdFrom() != null ? costCenterSearchBean.getCostCenterIdFrom().getName() : null);
        hashMap.put("costCenterIdTo", costCenterSearchBean.getCostCenterIdTo() != null ? costCenterSearchBean.getCostCenterIdTo().getCode() : null);
        hashMap.put("costCenterNameTo", costCenterSearchBean.getCostCenterIdTo() != null ? costCenterSearchBean.getCostCenterIdTo().getName() : null);
        hashMap.put("costCenterIndexText", userDDs.get("COST_CENTER_REPORT"));
        hashMap.put("costCenterFromText", userDDs.get("COST_CENTER_FROM"));
        hashMap.put("costCenterToText", userDDs.get("COST_CENTER_TO"));
        hashMap.put("codeText", userDDs.get("CS_COD"));
        hashMap.put("unitNameText", userDDs.get("SINGLE_COST_CENTER"));
        hashMap.put("levelText", userDDs.get("LEVEL"));
        hashMap.put("shortCodeText", userDDs.get("SHORT_CODE"));
        hashMap.put("parentCostCenterText", userDDs.get("MAIN_COST_CENTER"));
        hashMap.put("accountStatusText", userDDs.get("ACCOUNT_STATUS"));
        hashMap.put("PrintedBy", getUserData().getUser().getName());
        // hashMap.put("companyImage", getUserData().getImageReportPath());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {

        if (costCenterIndexBeanList != null && !costCenterIndexBeanList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "costCenterIndex.jasper", costCenterIndexBeanList, "pdf");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد نتائج للطباعة"));
        }
    }

    public List<CostCenter> completeCostCenter(String query) {
        List<CostCenter> costCenterList = getCostCenterList();
        if (query == null || query.trim().equals("")) {
            costCenterConverter = new CostCenterConverter(costCenterList);
            return costCenterList;
        }
        List<CostCenter> filteredCostCenters = new ArrayList<>();

        String nameAr;
        Integer code;
        CostCenter costCenter;
        for (int i = 0; i < getCostCenterList().size(); i++) {
            costCenter = costCenterList.get(i);
            nameAr = costCenter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredCostCenters.contains(costCenter)) {
                    filteredCostCenters.add(costCenter);
                }
            }

            code = costCenter.getCode();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredCostCenters.contains(costCenter)) {
                    filteredCostCenters.add(costCenter);
                }
            }
        }

        costCenterConverter = new CostCenterConverter(filteredCostCenters);
        return filteredCostCenters;
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

    public List<CostCenter> getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(List<CostCenter> costCenter) {
        this.costCenter = costCenter;
    }

    public CostCenterSearchBean getCostCenterSearchBean() {
        return costCenterSearchBean;
    }

    public void setCostCenterSearchBean(CostCenterSearchBean costCenterSearchBean) {
        this.costCenterSearchBean = costCenterSearchBean;
    }

    public CostCenterService getCostCenterService() {
        return costCenterService;
    }

    public void setCostCenterService(CostCenterService costCenterService) {
        this.costCenterService = costCenterService;
    }

    public List<CostCenterIndexBean> getCostCenterIndexBeanList() {
        return costCenterIndexBeanList;
    }

    public void setCostCenterIndexBeanList(List<CostCenterIndexBean> costCenterIndexBeanList) {
        this.costCenterIndexBeanList = costCenterIndexBeanList;
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

    public CostCenterConverter getCostCenterConverter() {
        return costCenterConverter;
    }

    public void setCostCenterConverter(CostCenterConverter costCenterConverter) {
        this.costCenterConverter = costCenterConverter;
    }
}
