/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.IsagUserLoginService;
import com.isag.isag.BaseReportBean;
import com.isag.businessservice.reports.searchBean.IsagUserLoginBeanSearch;
import com.isag.entity.IsagUser;
import com.isag.entity.IsagUserLogin;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import net.sf.jasperreports.engine.JRException;
import com.isag.businessservice.IsagUserService;

/**
 *
 * @author WIN7
 */
@ManagedBean(name = "reportISagUserLogin")
@ViewScoped
public class ReportISagUserLogin extends BaseReportBean {

    private IsagUserLoginBeanSearch isagUserLoginBeanSearch;
    private List<IsagUserLoginBeanSearch> isagUserLoginBeanSearchList;
    private List<IsagUser> isagUsersList;

    @EJB
    IsagUserLoginService isagUserLoginService;

    @EJB
    IsagUserService isagUserService;

    @PostConstruct
    public void init() {
        load();
    }

    @Override
    public void load() {
        isagUserLoginBeanSearch = new IsagUserLoginBeanSearch();
        isagUserLoginBeanSearch.setBranchId(getUserData().getUserBranch().getId());
        isagUserLoginBeanSearchList = new ArrayList<>();
        isagUsersList = new ArrayList<>();

        isagUsersList = isagUserService.getAllUsersListByCompanyId(getUserData().getCompany().getId());
    }

    @Override
    public void reset() {
        load();
    }

    @Override
    public void search() {
        isagUserLoginBeanSearchList = new ArrayList<>();
        List<IsagUserLogin> isagUserLoginList = isagUserLoginService.getALLgetIsagUserLoginByBranchIdAndUserCodeAndDate(isagUserLoginBeanSearch);

        IsagUserLoginBeanSearch isagUserLogin;
        for (IsagUserLogin userLogin : isagUserLoginList) {
            isagUserLogin = new IsagUserLoginBeanSearch();

            if (userLogin.getDateLogin() != null) {
                isagUserLogin.setDateLogin(userLogin.getDateLogin());
                DateFormat dateTime = new SimpleDateFormat("HH:mm:ss");

                isagUserLogin.setTime(dateTime.format(userLogin.getDateLogin()));
            }

            if (userLogin.getUserId() != null) {
                isagUserLogin.setUserName(userLogin.getUserId().getName() != null ? userLogin.getUserId().getName() : null);
                isagUserLogin.setIsagUserCode(userLogin.getUserId().getUserCode() != null ? userLogin.getUserId().getUserCode() : null);

            }

            isagUserLogin.setMacId(userLogin.getMacId() != null ? userLogin.getMacId() : null);

            isagUserLoginBeanSearchList.add(isagUserLogin);

        }

    }

    @Override
    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();
        hashMap.put("companyName", getUserData().getCompany().getName());
   //     hashMap.put("companyImage", getUserData().getImageReportPath());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        hashMap.put("PrintedBy", getUserData().getUser().getName());
        hashMap.put("codeFromText", userDDs.get("CODE_FROM"));
        hashMap.put("codeToText", userDDs.get("CODE_TO"));
        hashMap.put("yearFromText", userDDs.get("YEAR_FROM"));
        hashMap.put("yearToText", userDDs.get("YEAR_TO"));

        hashMap.put("userCodeText", userDDs.get("USER_CODE"));
        hashMap.put("userNameText", userDDs.get("USER_NAME"));
        hashMap.put("date", userDDs.get("DATE"));
        hashMap.put("timeText", userDDs.get("TIME"));
        hashMap.put("deviceText", userDDs.get("USED_DEVICE"));
        hashMap.put("isagUserLogin", userDDs.get("USER_ENT_REP"));

        hashMap.put("codeFrom", isagUserLoginBeanSearch.getUserCodeFrom() != null ? isagUserLoginBeanSearch.getUserCodeFrom() : null);
        hashMap.put("codeTo", isagUserLoginBeanSearch.getUserCodeTo() != null ? isagUserLoginBeanSearch.getUserCodeTo() : null);
        hashMap.put("yearFrom", isagUserLoginBeanSearch.getDateFrom() != null ? isagUserLoginBeanSearch.getDateFrom() : null);
        hashMap.put("yearTo", isagUserLoginBeanSearch.getDateTo() != null ? isagUserLoginBeanSearch.getDateTo() : null);

        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        search();
        if (isagUserLoginBeanSearchList != null && !isagUserLoginBeanSearchList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "isagUserLoginReport.jasper", isagUserLoginBeanSearchList, "pdf");
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

    public IsagUserLoginBeanSearch getIsagUserLoginBeanSearch() {
        return isagUserLoginBeanSearch;
    }

    public void setIsagUserLoginBeanSearch(IsagUserLoginBeanSearch isagUserLoginBeanSearch) {
        this.isagUserLoginBeanSearch = isagUserLoginBeanSearch;
    }

    public List<IsagUserLoginBeanSearch> getIsagUserLoginBeanSearchList() {
        return isagUserLoginBeanSearchList;
    }

    public void setIsagUserLoginBeanSearchList(List<IsagUserLoginBeanSearch> isagUserLoginBeanSearchList) {
        this.isagUserLoginBeanSearchList = isagUserLoginBeanSearchList;
    }

    public List<IsagUser> getIsagUsersList() {
        return isagUsersList;
    }

    public void setIsagUsersList(List<IsagUser> isagUsersList) {
        this.isagUsersList = isagUsersList;
    }
}
