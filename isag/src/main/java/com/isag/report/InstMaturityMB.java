/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.InstProjectGuideService;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.reports.InstViewService;
import com.isag.businessservice.reports.TempMaturityService;
import com.isag.businessservice.reports.searchBean.CollectionAndPaymentReportSearchBean;
import com.isag.converter.InstProjectGuideDTOConverter;
import com.isag.converter.InvOrganizationSiteDTOConverter;
import com.isag.dto.InstProjectGuideDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.isag.AutoComplete;
import com.isag.isag.BaseReportBean;
import com.isag.isag.UserData;
import com.isag.report.entity.InstMaturityBean;
import com.isag.views.InstView;
import com.isag.procedure.TempMaturity;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
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
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.collections.ListUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author ahmed
 */
@Named("instMaturityMB")
@ViewScoped
public class InstMaturityMB extends BaseReportBean {

    private List<InvOrganizationSiteDTO> invOrgSiteFromList;
    private InvOrganizationSiteDTOConverter invOrgSiteFromConverter;
    private List<InvOrganizationSiteDTO> invOrgSiteToList;
    private InvOrganizationSiteDTOConverter invOrgSiteToConverter;
    private CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean;
    private List<InstMaturityBean> instMaturityBeanList;
    private List<InstView> instViewList;
    private Map<Integer, Map<Integer, BigDecimal>> totalsMap;
    private Map<Integer, BigDecimal> CustomerTotalMap;
    private String uri;
    private List<InstProjectGuideDTO> instProjectGuideList;
    private InstProjectGuideDTOConverter instProjectGuideConverter;
    private Map<Integer, Map<Integer, List<InstMaturityBean>>> orderedProjectAndCustomerMap;
    private String totalValue;
    private LazyDataModel<TempMaturity> dataModel;
    private List<TempMaturity> tempMaturityList;

    private AutoComplete autoComplete;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private InstViewService instViewService;
    @EJB
    private InstProjectGuideService instProjectGuideService;

    @EJB
    private TempMaturityService tempMaturityService;
    private boolean search = false;
    private List<TempMaturity> report_list;

    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();

        reset();
        load();
    }
    Integer Start = 0;

    @Override
    public void load() {
        //old///  invOrgSiteFromList = organizationSiteService.getCustomerByBranchIdForReport(getUserData().getUserBranch() != null ? getUserData().getUserBranch().getId() : null);
        /*
        getInvOrgSiteFromList();
        getInvOrgSiteToList();
        getInstProjectGuideList();*/
//        instProjectGuideList = instProjectGuideService.getAllSubInstProjectGuideByBranchIdActive(getUserData().getUserBranch().getId());
        //  instProjectGuideConverter = new InstProjectGuideConverter(instProjectGuideList);
    }

    @Override
    public void reset() {
        collectionAndPaymentReportSearchBean = new CollectionAndPaymentReportSearchBean();
        instMaturityBeanList = new ArrayList<>();
//        instProjectGuideList = new ArrayList<>();
        instViewList = new ArrayList<>();
        orderedProjectAndCustomerMap = new HashMap<>();
        totalsMap = new HashMap<>();
        totalValue = new String();
        search = Boolean.FALSE;
        tempMaturityList = new ArrayList<>();
    }

    public void intializeSearch() {
        instMaturityBeanList = new ArrayList<>();
        instViewList = new ArrayList<>();
        totalValue = new String();
        totalsMap = new HashMap<>();
        orderedProjectAndCustomerMap = new HashMap<>();
        tempMaturityList = new ArrayList<>();
    }

    @Override
    public void search() {
        intializeSearch();

        collectionAndPaymentReportSearchBean.setBranchId(getUserData().getUserBranch() != null ? getUserData().getUserBranch().getId() : null);
        if (uri != null && uri.contains("Maturity")) {
            collectionAndPaymentReportSearchBean.setStatus(2);
            instViewList = instViewService.getInstViewBySearchBeanByLevel(collectionAndPaymentReportSearchBean);
        } else {
            collectionAndPaymentReportSearchBean.setToDate(new Date());
            instViewList = instViewService.getInstViewBySearchBean(collectionAndPaymentReportSearchBean);
        }

        List<InstView> instTotalView;
        instTotalView = instViewService.getInstViewTotalsByLevel(collectionAndPaymentReportSearchBean);
        if (instTotalView != null && !instTotalView.isEmpty()) {
            for (InstView instView : instTotalView) {

                if (!totalsMap.containsKey(instView.getSerial())) {
                    Map<Integer, BigDecimal> map = new HashMap<>();
                    map.put(instView.getCustomerId(), instView.getValue());
                    totalsMap.put(instView.getSerial(), map);
                } else {
                    Map<Integer, BigDecimal> map = totalsMap.get(instView.getSerial());
                    map.put(instView.getCustomerId(), instView.getValue());
                    totalsMap.put(instView.getSerial(), map);
                }
            }
        }
        if (instViewList != null && !instViewList.isEmpty()) {
            BigDecimal total = BigDecimal.ZERO;
            for (InstView instView : instViewList) {

                if (orderedProjectAndCustomerMap != null) {
//                    if (orderedProjectAndCustomerMap.containsKey(instView.getProjectId())) {
                    if (orderedProjectAndCustomerMap.containsKey(instView.getSerial())) {

//                        Map<Integer, List<InstMaturityBean>> mapOfCustomers = orderedProjectAndCustomerMap.get(instView.getProjectId());
                        Map<Integer, List<InstMaturityBean>> mapOfCustomers = orderedProjectAndCustomerMap.get(instView.getSerial());
                        List<InstMaturityBean> maturityBeanList = new ArrayList<>();
                        Map<Integer, List<InstMaturityBean>> map = new HashMap<>();
                        if (!mapOfCustomers.containsKey(instView.getCustomerId())) {
                            map.put(instView.getCustomerId(), new ArrayList<>());
                            maturityBeanList = map.get(instView.getCustomerId());

                            InstMaturityBean imb = new InstMaturityBean();
                            imb.setCustomer(instView.getCustomerName());
//                            imb.setProjectId(instView.getProjectId());
//                            imb.setProjectName(instView.getProjectName());
                            imb.setSerial(instView.getProjectId());
//                            Map<Integer, BigDecimal> totalMap = totalsMap.get(instView.getProjectId());
                            Map<Integer, BigDecimal> totalMap = totalsMap.get(instView.getSerial());
                            imb.setInstallmentValue(totalMap.get(instView.getCustomerId()));
                            maturityBeanList.add(imb);
                        } else {
                            maturityBeanList = mapOfCustomers.get(instView.getCustomerId());
                        }

                        InstMaturityBean instMaturityBean = new InstMaturityBean();
                        instMaturityBean.setSerial(instView.getInstSerial());
//                        instMaturityBean.setProjectId(instView.getProjectId());
//                        instMaturityBean.setProjectName(instView.getProjectName());
                        instMaturityBean.setDate(instView.getDueDate());
                        //  instMaturityBean.setCustomer(instView.getCustomerName());
                        instMaturityBean.setTotalValue(instView.getTotalValue());
                        instMaturityBean.setInstallmentValue(instView.getValue());
                        maturityBeanList.add(instMaturityBean);
                        mapOfCustomers.put(instView.getCustomerId(), maturityBeanList);
//                        orderedProjectAndCustomerMap.put(instView.getProjectId(), mapOfCustomers);
                        orderedProjectAndCustomerMap.put(instView.getSerial(), mapOfCustomers);
                    } else {
                        List<InstMaturityBean> maturityBeanList = new ArrayList<>();
                        Map<Integer, List<InstMaturityBean>> map = new HashMap<>();
                        map.put(instView.getCustomerId(), new ArrayList<>());
                        maturityBeanList = map.get(instView.getCustomerId());

                        InstMaturityBean imb = new InstMaturityBean();
                        imb.setCustomer(instView.getCustomerName());
//                        imb.setProjectId(instView.getProjectId());
//                        imb.setProjectName(instView.getProjectName());
                        imb.setSerial(instView.getSerial());
//                        Map<Integer, BigDecimal> totalMap = totalsMap.get(instView.getProjectId());
                        Map<Integer, BigDecimal> totalMap = totalsMap.get(instView.getSerial());
                        imb.setInstallmentValue(totalMap.get(instView.getCustomerId()));
                        maturityBeanList.add(imb);

                        InstMaturityBean instMaturityBean = new InstMaturityBean();
                        instMaturityBean.setSerial(instView.getInstSerial());
//                        instMaturityBean.setProjectId(instView.getProjectId());
//                        instMaturityBean.setProjectName(instView.getProjectName());
                        instMaturityBean.setSerial(instView.getSerial());
                        instMaturityBean.setDate(instView.getDueDate());
                        //     instMaturityBean.setCustomer(instView.getCustomerName());
                        instMaturityBean.setTotalValue(instView.getTotalValue());
                        instMaturityBean.setInstallmentValue(instView.getValue());
                        maturityBeanList.add(instMaturityBean);
                        map.put(instView.getCustomerId(), maturityBeanList);
//                        orderedProjectAndCustomerMap.put(instView.getProjectId(), map);
                        orderedProjectAndCustomerMap.put(instView.getSerial(), map);
                    }
                }
                total = total.add(instView.getValue());
                DecimalFormat df = new DecimalFormat("#,##0.00");
                totalValue = df.format(total);
            }
        }

        for (Map.Entry<Integer, Map<Integer, List<InstMaturityBean>>> entry : orderedProjectAndCustomerMap.entrySet()) {
            Map<Integer, List<InstMaturityBean>> map = entry.getValue();
            for (Map.Entry<Integer, List<InstMaturityBean>> entry1 : map.entrySet()) {
                List<InstMaturityBean> asd = entry1.getValue();
                instMaturityBeanList = ListUtils.union(instMaturityBeanList, asd);
            }
        }
        /* if (instViewList != null && !instViewList.isEmpty()) {
            for (InstView instView : instViewList) {
                InstMaturityBean instMaturityBean = new InstMaturityBean();
                instMaturityBean.setSerial(instView.getInstSerial());
                instMaturityBean.setDate(instView.getDueDate());
                instMaturityBean.setCustomer(instView.getCustomerName());
                instMaturityBean.setTotalValue(instView.getTotalValue());
                instMaturityBean.setInstallmentValue(instView.getValue());
                instMaturityBeanList.add(instMaturityBean);
            }
        }*/
    }

    public void search2() {
        intializeSearch();
        search = Boolean.TRUE;
        setDataModel(new LazyDataModel<TempMaturity>() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<TempMaturity> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                collectionAndPaymentReportSearchBean.setBranchId(getUserData().getUserBranch() != null ? getUserData().getUserBranch().getId() : null);
                collectionAndPaymentReportSearchBean.setUserId(getUserData().getUser().getId());
                if (uri != null && uri.contains("Maturity")) {
                    collectionAndPaymentReportSearchBean.setStatus(2);
                    tempMaturityList = tempMaturityService.getTempMaturitySearchBean(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), collectionAndPaymentReportSearchBean, filters, search);
                    setRowCount(tempMaturityService.getCount());
                } else {
                    collectionAndPaymentReportSearchBean.setToDate(new Date());
                    tempMaturityList = tempMaturityService.getTempMaturitySearchBean(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), collectionAndPaymentReportSearchBean, filters, search);
                    setRowCount(tempMaturityService.getCount());
                }
                DecimalFormat df = new DecimalFormat("#,##0.00");
                totalValue = df.format(tempMaturityService.getTotalValue());
                search = Boolean.FALSE;
                return getTempMaturityList();
            }
        });

    }
    
    public void hishis(){
        System.out.println("com.isag.report.InstMaturityMB.hishis()");
    }

    @Override
    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();

        HashMap hashMap = new HashMap();
        hashMap.put("customerFromText", userDDs.get("FROM_CUSTOM"));
        hashMap.put("customerToText", userDDs.get("TO_CUSTOM"));
        hashMap.put("yearFromText", userDDs.get("YEAR_FROM"));
        hashMap.put("yearToText", userDDs.get("UNTIL_DATE"));
        hashMap.put("serialText", userDDs.get("SERIAL"));
        hashMap.put("dateText", userDDs.get("DATE"));
        hashMap.put("totalValueText", userDDs.get("TOTAL_CONTRACT"));
        hashMap.put("installmentText", userDDs.get("INSTALLMENT"));
        hashMap.put("customerNameText", userDDs.get("CUSTOM_NAME"));
        hashMap.put("projectNameText", userDDs.get("PROJ_NAME"));
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        hashMap.put("PrintedBy", getUserData().getUser().getName());

        hashMap.put("yearFromValue", collectionAndPaymentReportSearchBean.getFromDate());
        hashMap.put("yearToValue", collectionAndPaymentReportSearchBean.getToDate());
        hashMap.put("customerFromValue", collectionAndPaymentReportSearchBean.getOrgNameFrom() != null ? collectionAndPaymentReportSearchBean.getOrgNameFrom().getName() : null);
        hashMap.put("customerToValue", collectionAndPaymentReportSearchBean.getOrgNameTo() != null ? collectionAndPaymentReportSearchBean.getOrgNameTo().getName() : null);

        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        if (uri != null && uri.contains("Maturity")) {
            hashMap.put("reportTitleText", userDDs.get("INSTA_DUE_REP"));
        } else {
            hashMap.put("reportTitleText", userDDs.get("OVERDUE_AMOU_REP"));
        }

        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (instMaturityBeanList != null && !instMaturityBeanList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "instMaturityReport.jasper", instMaturityBeanList, "pdf");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا توجد نتائج للطباعة"));
        }
    }

    public void exportPDF2(ActionEvent actionEvent) throws JRException, IOException {
        report_list = tempMaturityService.getreportList(collectionAndPaymentReportSearchBean);
        if (report_list != null && !report_list.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "instMaturityReport_1.jasper", report_list, "pdf");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا توجد نتائج للطباعة"));
        }

        report_list = null;
        System.gc();
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

    public List<InvOrganizationSiteDTO> completOrgSiteFrom(String query) {
        return getAutoComplete().completOrgSiteDTO(query, getInvOrgSiteFromList(), invOrgSiteFromConverter);
    }

    public List<InvOrganizationSiteDTO> completOrgSiteTo(String query) {
        return getAutoComplete().completOrgSiteDTO(query, getInvOrgSiteToList(), invOrgSiteToConverter);
    }

    public List<InstProjectGuideDTO> completeInstProject(String query) {
        return getAutoComplete().completeInstProjectDTO(query, getInstProjectGuideList(), instProjectGuideConverter);
    }

    public void onMoreText(javax.faces.event.AjaxBehaviorEvent event) {
        // org.primefaces.component.autocomplete.AutoComplete ac = (org.primefaces.component.autocomplete.AutoComplete) event.getSource();
        //ac.setMaxResults(ac.getMaxResults() + 10);
        Start += 10;
        executeJS("PF('organization').search('" + getInvOrgSiteFromList() + "')");
    }

    public void executeJS(String source) {
        org.primefaces.context.RequestContext.getCurrentInstance().execute(source);
    }

    /**
     * @return the invOrgSiteFromList
     */
    public List<InvOrganizationSiteDTO> getInvOrgSiteFromList() {
        invOrgSiteFromList = organizationSiteService.getCustomerByBranchIdForReportDTO(getUserData().getUserBranch() != null ? getUserData().getUserBranch().getId() : null);
        setInvOrgSiteFromConverter(new InvOrganizationSiteDTOConverter(invOrgSiteFromList));
        return invOrgSiteFromList;
    }

    private List<InvOrganizationSiteDTO> invOrganizationSiteDTOList(Integer start) {
        List<InvOrganizationSiteDTO> invOrgSiteList = new ArrayList<>();
        //   invOrgSiteList = organizationSiteService.getCustomerByBranchIdForReportDTO(getUserData().getUserBranch() != null ? getUserData().getUserBranch().getId() : null, start, 11);
        return invOrgSiteList;
    }

    /**
     * @param invOrgSiteFromList the invOrgSiteFromList to set
     */
    public void setInvOrgSiteFromList(List<InvOrganizationSiteDTO> invOrgSiteFromList) {
        this.invOrgSiteFromList = invOrgSiteFromList;
    }

    /**
     * @return the invOrgSiteFromConverter
     */
    public InvOrganizationSiteDTOConverter getInvOrgSiteFromConverter() {
        return invOrgSiteFromConverter;
    }

    /**
     * @param invOrgSiteFromConverter the invOrgSiteFromConverter to set
     */
    public void setInvOrgSiteFromConverter(InvOrganizationSiteDTOConverter invOrgSiteFromConverter) {
        this.invOrgSiteFromConverter = invOrgSiteFromConverter;
    }

    /**
     * @return the invOrgSiteToList
     */
    public List<InvOrganizationSiteDTO> getInvOrgSiteToList() {
        invOrgSiteToList = organizationSiteService.getCustomerByBranchIdForReportDTO(getUserData().getUserBranch() != null ? getUserData().getUserBranch().getId() : null);
        setInvOrgSiteToConverter(new InvOrganizationSiteDTOConverter(invOrgSiteToList));
        return invOrgSiteToList;
    }

    /**
     * @param invOrgSiteToList the invOrgSiteToList to set
     */
    public void setInvOrgSiteToList(List<InvOrganizationSiteDTO> invOrgSiteToList) {
        this.invOrgSiteToList = invOrgSiteToList;
    }

    /**
     * @return the invOrgSiteToConverter
     */
    public InvOrganizationSiteDTOConverter getInvOrgSiteToConverter() {
        return invOrgSiteToConverter;
    }

    /**
     * @param invOrgSiteToConverter the invOrgSiteToConverter to set
     */
    public void setInvOrgSiteToConverter(InvOrganizationSiteDTOConverter invOrgSiteToConverter) {
        this.invOrgSiteToConverter = invOrgSiteToConverter;
    }

    /**
     * @return the collectionAndPaymentReportSearchBean
     */
    public CollectionAndPaymentReportSearchBean getCollectionAndPaymentReportSearchBean() {
        if (collectionAndPaymentReportSearchBean == null) {
            collectionAndPaymentReportSearchBean = new CollectionAndPaymentReportSearchBean();
        }
        return collectionAndPaymentReportSearchBean;
    }

    /**
     * @param collectionAndPaymentReportSearchBean the
     * collectionAndPaymentReportSearchBean to set
     */
    public void setCollectionAndPaymentReportSearchBean(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean) {
        this.collectionAndPaymentReportSearchBean = collectionAndPaymentReportSearchBean;
    }

    /**
     * @return the instMaturityBeanList
     */
    public List<InstMaturityBean> getInstMaturityBeanList() {
        return instMaturityBeanList;
    }

    /**
     * @param instMaturityBeanList the instMaturityBeanList to set
     */
    public void setInstMaturityBeanList(List<InstMaturityBean> instMaturityBeanList) {
        this.instMaturityBeanList = instMaturityBeanList;
    }

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
     * @return the instProjectGuideList
     */
    public List<InstProjectGuideDTO> getInstProjectGuideList() {
        Integer level = null;
        if (getCollectionAndPaymentReportSearchBean().getProjectGuideFrom() != null) {
            level = getCollectionAndPaymentReportSearchBean().getProjectGuideFrom().getLevel();
        } else if (getCollectionAndPaymentReportSearchBean().getProjectGuideTo() != null) {
            level = getCollectionAndPaymentReportSearchBean().getProjectGuideTo().getLevel();
        }
        instProjectGuideList = instProjectGuideService.findInstProjectGuidesByBranch(getUserData().getUserBranch().getId(), level);
        setInstProjectGuideConverter(new InstProjectGuideDTOConverter(instProjectGuideList));
        return instProjectGuideList;
    }

    /**
     * @param instProjectGuideList the instProjectGuideList to set
     */
    public void setInstProjectGuideList(List<InstProjectGuideDTO> instProjectGuideList) {
        this.instProjectGuideList = instProjectGuideList;
    }

    /**
     * @return the instProjectGuideConverter
     */
    public InstProjectGuideDTOConverter getInstProjectGuideConverter() {
        return instProjectGuideConverter;
    }

    /**
     * @param instProjectGuideConverter the instProjectGuideConverter to set
     */
    public void setInstProjectGuideConverter(InstProjectGuideDTOConverter instProjectGuideConverter) {
        this.instProjectGuideConverter = instProjectGuideConverter;
    }

    /**
     * @return the orderedProjectAndCustomerMap
     */
    public Map<Integer, Map<Integer, List<InstMaturityBean>>> getOrderedProjectAndCustomerMap() {
        return orderedProjectAndCustomerMap;
    }

    /**
     * @param orderedProjectAndCustomerMap the orderedProjectAndCustomerMap to
     * set
     */
    public void setOrderedProjectAndCustomerMap(Map<Integer, Map<Integer, List<InstMaturityBean>>> orderedProjectAndCustomerMap) {
        this.orderedProjectAndCustomerMap = orderedProjectAndCustomerMap;
    }

    /**
     * @return the totalValue
     */
    public String getTotalValue() {
        return totalValue;
    }

    /**
     * @param totalValue the totalValue to set
     */
    public void setTotalValue(String totalValue) {
        this.totalValue = totalValue;
    }

    /**
     * @return the autoComplete
     */
    public AutoComplete getAutoComplete() {
        if (autoComplete == null) {
            autoComplete = new AutoComplete();
        }
        return autoComplete;
    }

    /**
     * @param autoComplete the autoComplete to set
     */
    public void setAutoComplete(AutoComplete autoComplete) {
        this.autoComplete = autoComplete;
    }

    /**
     * @return the dataModel
     */
    public LazyDataModel<TempMaturity> getDataModel() {
        return dataModel;
    }

    /**
     * @param dataModel the dataModel to set
     */
    public void setDataModel(LazyDataModel<TempMaturity> dataModel) {
        this.dataModel = dataModel;
    }

    /**
     * @return the tempMaturityList
     */
    public List<TempMaturity> getTempMaturityList() {
        return tempMaturityList;
    }

    /**
     * @param tempMaturityList the tempMaturityList to set
     */
    public void setTempMaturityList(List<TempMaturity> tempMaturityList) {
        this.tempMaturityList = tempMaturityList;
    }
}
