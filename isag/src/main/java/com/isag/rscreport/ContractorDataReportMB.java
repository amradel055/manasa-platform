/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rscreport;

import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.reports.entityBean.contractorDataReportEntity;
import com.isag.businessservice.reports.searchBean.contractorDataReportSearchBean;
import com.isag.dto.InvOrganizationSiteDTO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;

@Named(value = "contractorDataReportMB")
@ViewScoped
public class ContractorDataReportMB extends BaseRSCReportBean{
    
   
    private contractorDataReportSearchBean contarctorDataReportSearchBean;
    private List<contractorDataReportEntity> contractorDataReportEntityList;
    
   
    
    
    @EJB
    OrganizationSiteService impl;


    @Override
    public void reset() {
        try {
        contarctorDataReportSearchBean.setContractorCodeFrom(new InvOrganizationSiteDTO());
        contarctorDataReportSearchBean.setContractorCodeTo(new InvOrganizationSiteDTO());
       contarctorDataReportSearchBean = new  contractorDataReportSearchBean();
          contractorDataReportEntityList = new ArrayList<>();
          } catch (Exception e) {
            saveError(e, "ContractorDataReportMB", "reset");
        }
          
          
    }

    @Override
    public void search() {
        try {
      
        setcontractorDataReportEntityList(impl.getcontractorByCode(getContarctorDataReportSearchBean()));
          } catch (Exception e) {
            saveError(e, "ContractorDataReportMB", "search");
        }
        
    }
    private String hostname;
    private String ipAddress;
    @Override
    public HashMap prepareReport() {
         HashMap hashMap = new HashMap();
        try {
        Map<String, String> userDDs = getUserData().getUserDDs();
       

        hashMap.put("UserName", getUserData().getUser().getName());
        hashMap.put("Branch", getUserData().getUserBranch().getNameAr());
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        hashMap.put("pageHeader", "البيانات الأساسيه للمقاولين");
        hashMap.put("contractorCode", "الكود");
        hashMap.put("contractorName", "الأسم");
        hashMap.put("contractorOpenning", "الرصيد الافتتاحي");
        hashMap.put("contractorDate", "التاريخ");
        hashMap.put("contractorAddress", "العنوان");
        hashMap.put("contractorFax", "الفاكس");
        hashMap.put("contractorPhone", "تليفون");
        
        
        return hashMap;
         } catch (Exception e) {
            saveError(e, "ContractorDataReportMB", "search");
            return null;
        }
        
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if(contractorDataReportEntityList!= null && !contractorDataReportEntityList.isEmpty()){

      fillReport(prepareReport(), getUserData().getReportPath() + "contractorDataReport.jasper", getcontractorDataReportEntityList(), "pdf");
    }}

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
     * @return the contarctorDataReportSearchBean
     */
    public contractorDataReportSearchBean getContarctorDataReportSearchBean() {
        if(contarctorDataReportSearchBean==null){
        contarctorDataReportSearchBean = new contractorDataReportSearchBean();
        }
        contarctorDataReportSearchBean.setBranchId(getUserData().getUserBranch().getId());
        return contarctorDataReportSearchBean;
    }

    /**
     * @param contarctorDataReportSearchBean the contarctorDataReportSearchBean to set
     */
    public void setContarctorDataReportSearchBean(contractorDataReportSearchBean contarctorDataReportSearchBean) {
        this.contarctorDataReportSearchBean = contarctorDataReportSearchBean;
    }

    /**
     * @return the contractordatareportMB
     */
    public List<contractorDataReportEntity> getcontractorDataReportEntityList() {
        if(contractorDataReportEntityList ==null || contractorDataReportEntityList.isEmpty()){
            contractorDataReportEntityList = new ArrayList<>();
        }       return contractorDataReportEntityList;
    }

    /**
     * @param contractordatareportMB the contractordatareportMB to set
     */
    public void setcontractorDataReportEntityList(List<contractorDataReportEntity> contractorDataReportEntityList) {
        this.contractorDataReportEntityList = contractorDataReportEntityList;
    }

    /**
     * @return the hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * @param hostname the hostname to set
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * @return the ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * @param ipAddress the ipAddress to set
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
