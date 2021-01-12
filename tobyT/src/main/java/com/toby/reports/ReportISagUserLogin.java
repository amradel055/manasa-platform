/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.reports;

import com.toby.businessservice.TobyUserLoginService;
import com.toby.businessservice.TobyUserService;
import com.toby.entity.TobyUser;
import com.toby.entity.TobyUserLogin;
import com.toby.businessservice.reports.searchBean.TobyUserLoginBeanSearch;
import com.toby.toby.BaseReportBean;
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

/**
 *
 * @author WIN7
 */
@ManagedBean(name = "reportISagUserLogin")
@ViewScoped
public class ReportISagUserLogin extends BaseReportBean {

    private TobyUserLoginBeanSearch tobyUserLoginBeanSearch;
    private List<TobyUserLoginBeanSearch> tobyUserLoginBeanSearchList;
    private List<TobyUser> tobyUsersList;

    @EJB
    TobyUserLoginService tobyUserLoginService;

    @EJB
    TobyUserService tobyUserService;

    @PostConstruct
    public void init() {
        load();
    }

    @Override
    public void load() {
        tobyUserLoginBeanSearch = new TobyUserLoginBeanSearch();
        tobyUserLoginBeanSearch.setBranchId(getUserData().getUserBranch().getId());
        tobyUserLoginBeanSearchList = new ArrayList<>();
        tobyUsersList = new ArrayList<>();

        tobyUsersList = tobyUserService.getAllUsersListByCompanyId(getUserData().getCompany().getId());
    }

    @Override
    public void reset() {
        load();
    }

    @Override
    public void search() {
        tobyUserLoginBeanSearchList = new ArrayList<>();
        List<TobyUserLogin> tobyUserLoginList = tobyUserLoginService.getALLgetTobyUserLoginByBranchIdAndUserCodeAndDate(tobyUserLoginBeanSearch);

        TobyUserLoginBeanSearch tobyUserLogin;
        for (TobyUserLogin userLogin : tobyUserLoginList) {
            tobyUserLogin = new TobyUserLoginBeanSearch();

            if (userLogin.getDateLogin() != null) {
                tobyUserLogin.setDateLogin(userLogin.getDateLogin());
                DateFormat dateTime = new SimpleDateFormat("HH:mm:ss");

                tobyUserLogin.setTime(dateTime.format(userLogin.getDateLogin()));
            }

            if (userLogin.getUserId() != null) {
                tobyUserLogin.setUserName(userLogin.getUserId().getName() != null ? userLogin.getUserId().getName() : null);
                tobyUserLogin.setTobyUserCode(userLogin.getUserId().getUserCode() != null ? userLogin.getUserId().getUserCode() : null);

            }

            tobyUserLogin.setMacId(userLogin.getMacId() != null ? userLogin.getMacId() : null);

            tobyUserLoginBeanSearchList.add(tobyUserLogin);

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
        hashMap.put("tobyUserLogin", userDDs.get("USER_ENT_REP"));

        hashMap.put("codeFrom", tobyUserLoginBeanSearch.getUserCodeFrom() != null ? tobyUserLoginBeanSearch.getUserCodeFrom() : null);
        hashMap.put("codeTo", tobyUserLoginBeanSearch.getUserCodeTo() != null ? tobyUserLoginBeanSearch.getUserCodeTo() : null);
        hashMap.put("yearFrom", tobyUserLoginBeanSearch.getDateFrom() != null ? tobyUserLoginBeanSearch.getDateFrom() : null);
        hashMap.put("yearTo", tobyUserLoginBeanSearch.getDateTo() != null ? tobyUserLoginBeanSearch.getDateTo() : null);

        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        search();
        if (tobyUserLoginBeanSearchList != null && !tobyUserLoginBeanSearchList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "tobyUserLoginReport.jasper", tobyUserLoginBeanSearchList, "pdf");
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

    public TobyUserLoginBeanSearch getTobyUserLoginBeanSearch() {
        return tobyUserLoginBeanSearch;
    }

    public void setTobyUserLoginBeanSearch(TobyUserLoginBeanSearch tobyUserLoginBeanSearch) {
        this.tobyUserLoginBeanSearch = tobyUserLoginBeanSearch;
    }

    public List<TobyUserLoginBeanSearch> getTobyUserLoginBeanSearchList() {
        return tobyUserLoginBeanSearchList;
    }

    public void setTobyUserLoginBeanSearchList(List<TobyUserLoginBeanSearch> tobyUserLoginBeanSearchList) {
        this.tobyUserLoginBeanSearchList = tobyUserLoginBeanSearchList;
    }

    public List<TobyUser> getTobyUsersList() {
        return tobyUsersList;
    }

    public void setTobyUsersList(List<TobyUser> tobyUsersList) {
        this.tobyUsersList = tobyUsersList;
    }
}
