/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.reports;

import com.toby.businessservice.TobyUserRoleService;
import com.toby.businessservice.TobyUserService;
import com.toby.entity.TobyUser;
import com.toby.report.entity.TobyUserBean;
import com.toby.toby.BaseGlAccountReportBean;
import com.toby.toby.UserData;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;

/**
 *
 * @author hq002
 */
@Named("reportsManagedBean")
@ViewScoped
public class ReportsManagedBean extends BaseGlAccountReportBean implements Serializable {

    private List<TobyUser> users = new ArrayList<>();
    private TobyUser user;
    private List<TobyUserBean> tobyUserBeanList;
    private String userName;
    private String userCode;
    private Integer Code;
    private Integer companyId;

    @EJB
    private TobyUserService userService;

    @EJB
    private TobyUserRoleService tobyUserRoleService;

    private UserData userData;

    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        userData = (UserData) context.getSessionMap().get("userLogInData");
        user = new TobyUser();
        if (userData.getCompany() != null && userData.getCompany().getId() != null) {
            if (userData.getSelectedBranch() != null) {
                users = tobyUserRoleService.getUsersForBranch(userData.getSelectedBranch());
            }
        }
    }

    public void filter() {
        users = new ArrayList<>();
        user.setName(userName);
        user.setUserCode(userCode);
        users = userService.getUsersForBranch(userData.getUserBranch().getId(), user);
        tobyUserBeanList = new ArrayList<>();
        for (TobyUser tobyUsers : users) {
            TobyUserBean newItem = new TobyUserBean();
            newItem.setUserName(tobyUsers.getName());
            newItem.setUserCode(tobyUsers.getUserCode());

            tobyUserBeanList.add(newItem);
        }
    }

    public void PDF(ActionEvent actionEvent) throws JRException, IOException {
        String webpath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/Leaf_Red.jasper");
        Map<String, Object> params = new HashMap();
        params.put("reportName", "Users Report");
        File file = new File(webpath);
        JRBeanCollectionDataSource bCDSource = new JRBeanCollectionDataSource(users);
        JasperPrint print = JasperFillManager.fillReport(file.getPath(), params, bCDSource);
        HttpServletResponse hsr = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        hsr.addHeader("Content-disposition", "attachment; filename=report.pdf");
        ServletOutputStream outputStream = hsr.getOutputStream();
        JasperExportManager.exportReportToPdfStream(print, outputStream);
        outputStream.flush();
        outputStream.close();

        FacesContext.getCurrentInstance().responseComplete();
    }

    public void viewPDF(ActionEvent actionEvent) throws JRException, IOException {
        String webpath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/users.jasper");
//        Map<String, Object> params = new HashMap();
//        params.put("reportName", "Users Report");
        File file = new File(webpath);
        JRBeanCollectionDataSource bCDSource = new JRBeanCollectionDataSource(users);
        byte[] bytes = JasperRunManager.runReportToPdf(file.getPath(), new HashMap(), bCDSource);
        HttpServletResponse hsr = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        hsr.setHeader("Content-Disposition", "inline");
        hsr.setContentType("application/pdf");
        hsr.setCharacterEncoding("utf-8");
        hsr.setContentLength(bytes.length);
        ServletOutputStream outputStream = hsr.getOutputStream();
        outputStream.write(bytes, 0, bytes.length);
        outputStream.flush();
        outputStream.close();
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void HTML() throws JRException, IOException {
        String webpath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/Leaf_Red.jasper");
        //  JasperPrint print=JasperFillManager.fillReport("C:\\Users\\hq002\\Desktop\\Leaf_Red.jasper", new HashMap(),bCDSource);
        Map<String, Object> params = new HashMap();
        params.put("reportName", "Users Report");
        File file = new File(webpath);
        JRBeanCollectionDataSource bCDSource = new JRBeanCollectionDataSource(users, false);
        JasperPrint print = JasperFillManager.fillReport(file.getPath(), params, bCDSource);
        HttpServletResponse hsr = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        hsr.addHeader("Content-disposition", "attachment; filename=report.html");
        ServletOutputStream outputStream = hsr.getOutputStream();
        Exporter exporter = new HtmlExporter();
        exporter.setExporterOutput(new SimpleHtmlExporterOutput(outputStream));
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.exportReport();
//        FacesContext.getCurrentInstance().responseComplete();

    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void search() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HashMap prepareReport() {

        Map<String, String> userDDs = userData.getUserDDs();
        String userNameText = userDDs.get("USER_NAME");
        String userCodeText = userDDs.get("CODE_USER");
        String usersReport = userDDs.get("USERS_REPORT");
        String PrintedBy = userData.getUser().getName();
        String companyName = userData.getCompany().getName();
        String companyImage = userData.getImageReportPath();
        String branchName = getUserData().getUserBranch().getNameAr();

        HashMap hashMap = new HashMap();
   //     hashMap.put("companyName", companyName);
        if (userName != null && userName.length() == 0) {
            userName = null;
        }
        hashMap.put("userName", userName);
        if (userCode != null && userCode.length() == 0) {
            userCode = null;
        }
        hashMap.put("userCode", userCode);
        hashMap.put("userNameText", userNameText);
        hashMap.put("userCodeText", userCodeText);
        hashMap.put("usersReport", usersReport);
        hashMap.put("PrintedBy", PrintedBy);
        // hashMap.put("companyImage", companyImage);
        hashMap.put("branchName", branchName);
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (tobyUserBeanList != null && !tobyUserBeanList.isEmpty()) {
            String x = userData.getReportPath();
            fillReport(prepareReport(), getUserData().getReportPath() + "userreport.jasper", tobyUserBeanList, "pdf");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا توجد نتائج للطباعة"));
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

    public List<TobyUser> getUsers() {
        return users;
    }

    public void setUsers(List<TobyUser> users) {
        this.users = users;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public TobyUser getUser() {
        return user;
    }

    public void setUser(TobyUser user) {
        this.user = user;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public Integer getCode() {
        return Code;
    }

    public void setCode(Integer Code) {
        this.Code = Code;
    }

    /**
     * @return the tobyUserBeanList
     */
    public List<TobyUserBean> getTobyUserBeanList() {
        return tobyUserBeanList;
    }

    /**
     * @param tobyUserBeanList the tobyUserBeanList to set
     */
    public void setTobyUserBeanList(List<TobyUserBean> tobyUserBeanList) {
        this.tobyUserBeanList = tobyUserBeanList;
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

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

}
