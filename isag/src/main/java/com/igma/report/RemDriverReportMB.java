/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.report;


import com.isag.businessservice.EmployeeService;
import com.isag.businessservice.reports.searchBean.EmployeeReportSearchBean;
import com.isag.dto.EmployeeDTO;
import com.isag.isag.BaseReportBean;
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
@Named("remDriverReportMB")
@ViewScoped
public class RemDriverReportMB extends BaseReportBean {

   private List<EmployeeDTO> employeeDTOList ;
   private EmployeeReportSearchBean employeeReportSearchBean;
            
    @EJB
    EmployeeService employeeService;
    
    
    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reset() {
        employeeReportSearchBean = new EmployeeReportSearchBean();
        employeeDTOList = new ArrayList<>();
    }

    @Override
    public void search() {
       employeeDTOList =  employeeService.getEmployeebyBranchId(getUserData().getUserBranch().getId(), employeeReportSearchBean);
    }

    @Override
    public HashMap prepareReport() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
     * @return the employeeDTOList
     */
    public List<EmployeeDTO> getEmployeeDTOList() {
        return employeeDTOList;
    }

    /**
     * @param employeeDTOList the employeeDTOList to set
     */
    public void setEmployeeDTOList(List<EmployeeDTO> employeeDTOList) {
        this.employeeDTOList = employeeDTOList;
    }

    /**
     * @return the employeeReportSearchBean
     */
    public EmployeeReportSearchBean getEmployeeReportSearchBean() {
        if(employeeReportSearchBean == null){
            employeeReportSearchBean = new EmployeeReportSearchBean();
        }
        return employeeReportSearchBean;
    }

    /**
     * @param employeeReportSearchBean the employeeReportSearchBean to set
     */
    public void setEmployeeReportSearchBean(EmployeeReportSearchBean employeeReportSearchBean) {
        this.employeeReportSearchBean = employeeReportSearchBean;
    }

    
    
    
}
