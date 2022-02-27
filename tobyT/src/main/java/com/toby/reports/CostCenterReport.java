/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.reports;

import com.toby.businessservice.CostCenterService;
import com.toby.entity.CostCenter;
import com.toby.toby.UserData;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author hq002
 */
@Named("costCenterReport")
@ViewScoped
public class CostCenterReport implements Serializable{

    
   private UserData userData;
    private List<CostCenter> costCenterList;
    private List<CostCenterMBean> costCenterReportList = new ArrayList<>();
    @EJB
    CostCenterService ccs;

    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        userData = (UserData) context.getSessionMap().get("userLogInData");
        costCenterList = ccs.getCompanyCostCenters(userData.getCompany().getId());
        for (CostCenter cost : costCenterList) {
            CostCenterMBean costReport = new CostCenterMBean();
            costReport.setLevel(cost.getLevel());
            costReport.setName(cost.getName());
//             Code=glAdminUnit.getParent().getCode();
            if (cost.getParent() != null) {
                costReport.setParent(cost.getParent().getCode());
            } else {
                costReport.setParent(null);
            }

            costReport.setCode(cost.getCode());
            costCenterReportList.add(costReport);
        }

    }
    public void viewPDF(ActionEvent actionEvent) throws JRException, IOException {
        String webpath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/CostCenter.jasper");
//        Map<String, Object> params = new HashMap();
//        params.put("reportName", "Users Report");
        File file = new File(webpath);
        JRBeanCollectionDataSource bCDSource = new JRBeanCollectionDataSource(costCenterReportList);
        byte[] bytes = JasperRunManager.runReportToPdf(file.getPath(), new HashMap(), bCDSource);
        HttpServletResponse hsr = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        hsr.setHeader("Content-Disposition", "inline");
        hsr.setContentType("application/pdf");
        hsr.setContentLength(bytes.length);
        ServletOutputStream outputStream = hsr.getOutputStream();
        outputStream.write(bytes, 0, bytes.length);
        outputStream.flush();
        outputStream.close();
        FacesContext.getCurrentInstance().responseComplete();
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public List<CostCenter> getCostCenterList() {
        return costCenterList;
    }

    public void setCostCenterList(List<CostCenter> costCenterList) {
        this.costCenterList = costCenterList;
    }

    public List<CostCenterMBean> getCostCenterReportList() {
        return costCenterReportList;
    }

    public void setCostCenterReportList(List<CostCenterMBean> costCenterReportList) {
        this.costCenterReportList = costCenterReportList;
    }
    
}
