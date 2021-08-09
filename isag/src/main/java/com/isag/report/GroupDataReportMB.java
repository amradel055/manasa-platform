/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.InvGroupService;
import com.isag.businessservice.reports.GroupDataReportService;

import com.isag.businessservice.reports.searchBean.GroupDataReportSearchBean;
import com.isag.converter.InvGroupConverter;
import com.isag.entity.InvGroup;
import com.isag.isag.BaseReportBean;
import com.isag.isag.UserData;
import com.isag.report.entity.GroupDataReportViewBean;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author hhhh
 */
@Named("groupDataReportMB")
@ViewScoped
public class GroupDataReportMB extends BaseReportBean {

    // Objects
    private Integer branchId;
    private String uri;
    private String screenMode;
    private List<GroupDataReportViewBean> groupDataReportViewBeanList;
    private GroupDataReportSearchBean groupDataReportSearchBean;
    private List<InvGroup> invGroupList;
    private InvGroupConverter invGroupConverter;

    @EJB
    private GroupDataReportService groupDataReportService;
    @EJB
    private InvGroupService groupService;

    @Override
    @PostConstruct
    public void load() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        setScreenMode((String) context.getSessionMap().get("ScreenMode"));
        setBranchId(getUserData().getUserBranch().getId());
        setUri(((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI());
        initObjs();
        fillLists();
    }

    private void initObjs() {
        invGroupList = new ArrayList<>();
        groupDataReportSearchBean = new GroupDataReportSearchBean();
        groupDataReportSearchBean.setBranchId(getBranchId());
        groupDataReportSearchBean.setShowReport(false);
        groupDataReportViewBeanList = new ArrayList<>();
    }

    private void fillLists() {
        invGroupList = groupService.getallInvGroupByBranchId(getBranchId());
        invGroupConverter = new InvGroupConverter(getInvGroupList());
    }

    @Override
    public void reset() {
        groupDataReportSearchBean = new GroupDataReportSearchBean();
        groupDataReportSearchBean.setBranchId(getBranchId());
        groupDataReportSearchBean.setShowReport(false);
        groupDataReportViewBeanList = new ArrayList<>();
    }

    public void showItemReport() {
        invGroupList = new ArrayList<>();
    }

    @Override
    public void search() {

        groupDataReportViewBeanList = new ArrayList<>();

        groupDataReportSearchBean.setBranchId(getUserData().getUserBranch().getId());

        List<InvGroup> InvGroupLList = new ArrayList<>();

        InvGroupLList = groupDataReportService.getAllGroupsSearchBean(getGroupDataReportSearchBean());
        if (InvGroupLList != null && !InvGroupLList.isEmpty()) {
            for (InvGroup list : InvGroupLList) {
                GroupDataReportViewBean bean = new GroupDataReportViewBean();
                bean.setCode(list.getCode());
                bean.setName(list.getName());

                bean.setParent(list.getParent() != null ? (list.getParent().getName() != null ? list.getParent().getName() : null) : null);
                getGroupDataReportViewBeanList().add(bean);
            }
        }

    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
      if(getGroupDataReportViewBeanList() != null && !getGroupDataReportViewBeanList().isEmpty())
        fillReport(prepareReport(), getUserData().getReportPath() + "GroupDataReport.jasper", getGroupDataReportViewBeanList(), "pdf");

    }

    @Override
    public <T> void fillReport(HashMap hashMap, String reportPath, List<T> listBean, String exportType) throws JRException, IOException {
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listBean);
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, hashMap, beanCollectionDataSource);
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        if ("pdf".equals(exportType)) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            servletOutputStream.flush();
            servletOutputStream.close();
            httpServletResponse.getOutputStream().flush();
            httpServletResponse.getOutputStream().close();
        } else if ("excel".equals(exportType)) {

        } else if ("html".equals(exportType)) {

        }
    }

    @Override
    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();

        hashMap.put("UserName", getUserData().getUser().getName());
        hashMap.put("Branch", getUserData().getUserBranch().getNameAr());
      //  hashMap.put("CompanyName", getUserData().getCompany().getName());

        //   hashMap.put("CompanyLogo", getUserData().getImageReportPath());
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        hashMap.put("dateText", userDDs.get("DATE"));

        hashMap.put("header2", userDDs.get("GROUP_ITEMS"));
        hashMap.put("codeText", userDDs.get("GROP_COD"));

        hashMap.put("groupText", userDDs.get("NAME"));

        hashMap.put("parentText", userDDs.get("MAIN_GROP"));
        if(getGroupDataReportSearchBean().getFromGroupName() != null ){
        hashMap.put("groupFromText", "من مجموعة");
        hashMap.put("groupFromValue",  getGroupDataReportSearchBean().getFromGroupName().getName());
        }
        if(getGroupDataReportSearchBean().getToGroupName() != null ){
        hashMap.put("groupToText", "الي مجموعة");
        hashMap.put("groupToValue",   getGroupDataReportSearchBean().getToGroupName().getName());
        }
        return hashMap;
    }

    public List<InvGroup> completGroup(String query) {
        List<InvGroup> invGroupList = getInvGroupList();
        if (query == null || query.trim().equals("")) {

            setInvGroupConverter(new InvGroupConverter(invGroupList));
            return invGroupList;
        }
        List<InvGroup> filteredInvs = new ArrayList<>();

        String nameAr;
        String code;
        InvGroup invGroupFilter;

        for (int i = 0; i < getInvGroupList().size(); i++) {
            invGroupFilter = invGroupList.get(i);
            nameAr = invGroupFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invGroupFilter)) {
                    filteredInvs.add(invGroupFilter);
                }
            }

            code = invGroupFilter.getCode().toString();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invGroupFilter)) {
                    filteredInvs.add(invGroupFilter);
                }
            }
        }

        setInvGroupConverter(new InvGroupConverter(filteredInvs));
        return filteredInvs;
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

    public String getScreenMode() {
        return screenMode;
    }

    public void setScreenMode(String screenMode) {
        this.screenMode = screenMode;
    }

    /*  public Boolean getIsSales() {
     return isSales;
     }

     public void setIsSales(Boolean isSales) {
     this.isSales = isSales;
     } */
    /**
     * @return the branchId
     */
    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * @return the branchId
     */
    public Integer getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the groupDataReportViewBeanList
     */
    public List<GroupDataReportViewBean> getGroupDataReportViewBeanList() {
        return groupDataReportViewBeanList;
    }

    /**
     * @param groupDataReportViewBeanList the groupDataReportViewBeanList to set
     */
    public void setGroupDataReportViewBeanList(List<GroupDataReportViewBean> groupDataReportViewBeanList) {
        this.groupDataReportViewBeanList = groupDataReportViewBeanList;
    }

    /**
     * @return the groupDataReportSearchBean
     */
    public GroupDataReportSearchBean getGroupDataReportSearchBean() {
        return groupDataReportSearchBean;
    }

    /**
     * @param groupDataReportSearchBean the groupDataReportSearchBean to set
     */
    public void setGroupDataReportSearchBean(GroupDataReportSearchBean groupDataReportSearchBean) {
        this.groupDataReportSearchBean = groupDataReportSearchBean;
    }

    /**
     * @return the invGroupList
     */
    public List<InvGroup> getInvGroupList() {
        return invGroupList;
    }

    /**
     * @param invGroupList the invGroupList to set
     */
    public void setInvGroupList(List<InvGroup> invGroupList) {
        this.invGroupList = invGroupList;
    }

    /**
     * @return the invGroupConverter
     */
    public InvGroupConverter getInvGroupConverter() {
        return invGroupConverter;
    }

    /**
     * @param invGroupConverter the invGroupConverter to set
     */
    public void setInvGroupConverter(InvGroupConverter invGroupConverter) {
        this.invGroupConverter = invGroupConverter;
    }

    /**
     * @return the groupDataReportService
     */
    public GroupDataReportService getGroupDataReportService() {
        return groupDataReportService;
    }

    /**
     * @param groupDataReportService the groupDataReportService to set
     */
    public void setGroupDataReportService(GroupDataReportService groupDataReportService) {
        this.groupDataReportService = groupDataReportService;
    }

    /**
     * @return the groupService
     */
    public InvGroupService getGroupService() {
        return groupService;
    }

    /**
     * @param groupService the groupService to set
     */
    public void setGroupService(InvGroupService groupService) {
        this.groupService = groupService;
    }

}
