/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.reports;

import com.toby.businessservice.report.OrganizationSiteStatementServiceReport;

import com.toby.businessservice.OrganizationSiteService;
import com.toby.businessservice.reports.searchBean.OrganizationSiteStatementSearchBean;
import com.toby.converter.InvOrganizationSiteConverter;
import com.toby.entity.InvOrganizationSite;
import com.toby.report.entity.OrganizationSiteStatementBean;
import com.toby.toby.BaseReportBean;
import com.toby.toby.UserData;
import com.toby.views.OrganizationSiteStatementView;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
@Named("organizationSiteStatementReportMB")
@ViewScoped
public class OrganizationSiteStatementReportMB extends BaseReportBean {

    // Objects
    private Integer branchId;
    Integer type = 1;
    BigDecimal balance = BigDecimal.ZERO;
    private String uri;
    private String screenMode;
    private List<OrganizationSiteStatementBean> organizationSiteStatementBeanList;
    private OrganizationSiteStatementSearchBean organizationSiteStatementSearchBean;
    private List<OrganizationSiteStatementView> organizationSiteStatementViewList;
    private List<InvOrganizationSite> invOrgSiteList;
    private InvOrganizationSiteConverter invOrgSiteConverter;
    //-----------------------
    private Integer stripMapReference = 0;
    private BigDecimal repositoryBalance = BigDecimal.ZERO;
    private OrganizationSiteStatementSearchBean organizationSiteStatementSearchViewBean;
    private OrganizationSiteStatementBean organizationSiteStatementViewBean;
    private List<InvOrganizationSite> organizationSiteRootList;
    private Map<Integer, List<OrganizationSiteStatementView>> organizationSiteStatementmap = new HashMap<>();
    private Map<Integer, OrganizationSiteStatementBean> organizationSiteStatementBeanMap = new HashMap<>();
    private TreeMap<Integer, OrganizationSiteStatementBean> organizationSiteStatementTreeMap = new TreeMap<>();
    // private OrganizationSiteStatementBean dataByOrgSiteBean;

    //-----------------------
    @EJB
    private OrganizationSiteStatementServiceReport organizationSiteStatementServiceReport;
    @EJB
    private OrganizationSiteService organizationSiteService;
    
    @Override
    @PostConstruct
    public void load() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        setScreenMode((String) context.getSessionMap().get("ScreenMode"));
        setBranchId(getUserData().getUserBranch().getId());
        //   organizationSiteStatementSearchBean.setOrganizationType(type);
        setUri(((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI());
        initObjs();
        
        fillLists();
        setOrganizationSiteRootList(new ArrayList<>());
        setOrganizationSiteRootList(organizationSiteService.getCustomerByBranchId(branchId));
    }
    
    private void initObjs() {
        
        setInvOrgSiteList(new ArrayList<>());
        setOrganizationSiteStatementSearchBean(new OrganizationSiteStatementSearchBean());
        getOrganizationSiteStatementSearchBean().setBranchId(getBranchId());
        getOrganizationSiteStatementSearchBean().setShowReport(false);
        organizationSiteStatementSearchBean.setOrganizationType(type);
        setOrganizationSiteStatementmap(new HashMap<>());
        setOrganizationSiteStatementTreeMap(new TreeMap<>());
        setOrganizationSiteStatementBeanMap(new HashMap<>());
        
        setOrganizationSiteStatementBeanList(new ArrayList<>());
        
    }
    
    private void fillLists() {
        setInvOrgSiteList(organizationSiteService.getCustomerByBranchId(getBranchId()));
        setInvOrgSiteConverter(new InvOrganizationSiteConverter(getInvOrgSiteList()));
    }
    
    public void IntializeSearchLab() {
        organizationSiteStatementViewList = new ArrayList<>();
        organizationSiteStatementmap = new HashMap<>();
        
        organizationSiteStatementTreeMap = new TreeMap<>();
    }
    
    @Override
    public void reset() {
        initObjs();
        fillLists();
        organizationSiteStatementSearchBean = new OrganizationSiteStatementSearchBean();
        setOrganizationSiteStatementSearchViewBean(new OrganizationSiteStatementSearchBean());
        organizationSiteStatementViewList = new ArrayList<>();
    }
    
    public void showItemReport() {
    }
    
    @Override
    public void search() {
        balance = BigDecimal.ZERO;
        setOrganizationSiteStatementBeanList(new ArrayList<>());
        setOrganizationSiteStatementSearchViewBean(new OrganizationSiteStatementSearchBean());
        
        getOrganizationSiteStatementSearchBean().setBranchId(getUserData().getUserBranch().getId());
        
        organizationSiteStatementViewList = new ArrayList<>();
        getOrganizationSiteStatementSearchBean().setOrganizationType(type);
        
        organizationSiteStatementViewList = organizationSiteStatementServiceReport.getAllOrganizationSiteStatementSearchBean(getOrganizationSiteStatementSearchBean());
        if (organizationSiteStatementViewList != null && !organizationSiteStatementViewList.isEmpty()) {
            fillGroupMapWithCorrespondingItem();
            prepareRootGroupValues();
        }
        
        mergeGroupsWithItems();
        
    }
    
    private void fillGroupMapWithCorrespondingItem() {
        if (organizationSiteStatementViewList != null && !organizationSiteStatementViewList.isEmpty()) {
            for (OrganizationSiteStatementView orgView : organizationSiteStatementViewList) {
                if (organizationSiteStatementmap.containsKey(orgView.getOrganizationSiteId())) {
                    List list = organizationSiteStatementmap.get(orgView.getOrganizationSiteId());
                    list.add(orgView);
                    organizationSiteStatementmap.put(orgView.getOrganizationSiteId(), list);
                } else {
                    List list = new ArrayList();
                    list.add(orgView);
                    organizationSiteStatementmap.put(orgView.getOrganizationSiteId(), list);
                }
            }
        }
    }
    
    public void prepareRootGroupValues() {
        stripMapReference = 0;
        if (organizationSiteRootList != null && !organizationSiteRootList.isEmpty()) {
            for (InvOrganizationSite ig : organizationSiteRootList) {
                
                OrganizationSiteStatementBean imdbgb = new OrganizationSiteStatementBean();
                imdbgb.setOrganizationId(ig.getId());
                imdbgb.setOrganizationName(ig.getName());
                organizationSiteStatementBeanMap.put(ig.getId(), imdbgb);
                findListFromMapAndPutValue(ig, imdbgb);
                getOrganizationSiteStatementTreeMap().put(stripMapReference++, organizationSiteStatementBeanMap.get(ig.getId()));
                getChildTreeNodesForGroup(ig);
            }
        }
    }
    
    private void getChildTreeNodesForGroup(InvOrganizationSite grp) {
        //stripMapReference = 0;
        if (grp.getInvOrganizationSiteCollection() != null && !grp.getInvOrganizationSiteCollection().isEmpty()) {
            for (InvOrganizationSite childGrp : grp.getInvOrganizationSiteCollection()) {
                OrganizationSiteStatementBean imdbgb = new OrganizationSiteStatementBean();
                imdbgb.setOrganizationId(childGrp.getId());
                imdbgb.setOrganizationName(childGrp.getName());
                findListFromMapAndPutValue(childGrp, imdbgb);
                organizationSiteStatementBeanMap.put(childGrp.getId(), imdbgb);
                getOrganizationSiteStatementTreeMap().put(stripMapReference++, organizationSiteStatementBeanMap.get(childGrp.getId()));
                getChildTreeNodesForGroup(childGrp);
            }
        }
    }
    
    private void findListFromMapAndPutValue(InvOrganizationSite childGrp, OrganizationSiteStatementBean srvb) {
        if (organizationSiteStatementmap.containsKey(childGrp.getId())) {
            if (organizationSiteStatementmap.get(childGrp.getId()) != null && !organizationSiteStatementmap.get(childGrp.getId()).isEmpty()) {
                BigDecimal decimal = BigDecimal.ZERO;
                for (OrganizationSiteStatementView orgSiteView : organizationSiteStatementmap.get(childGrp.getId())) {
                    decimal = decimal.add(orgSiteView.getOpenningBalance());
                }
                srvb.setOpenningBalance(decimal);
                if (childGrp.getParent() != null) {
                    putValueOfParent(childGrp.getParent(), decimal);
                } else {
                    BigDecimal qtyAll = organizationSiteStatementBeanMap.get(childGrp.getId()).getBalance();
                    repositoryBalance = repositoryBalance.add(qtyAll != null ? qtyAll : BigDecimal.ZERO);
                }
            }
        }
    }
    
    public void putValueOfParent(InvOrganizationSite ParentGrp, BigDecimal bd) {
        if (organizationSiteStatementBeanMap.containsKey(ParentGrp.getId())) {
            OrganizationSiteStatementBean bean = organizationSiteStatementBeanMap.get(ParentGrp.getId());
            bean.setBalance(bd != null ? bd : BigDecimal.ZERO);
            bean.setBalance(bean.getBalance().add(bd));
            if (ParentGrp.getParent() != null) {
                putValueOfParent(ParentGrp.getParent(), bd);
            }
        }
    }
    
    public void mergeGroupsWithItems() {
        //  getDataByOrgSiteBean().setBalance(repositoryBalance.setScale(2, BigDecimal.ROUND_UP));
        //   getOrganizationSiteStatementBeanList().add(dataByOrgSiteBean);
        for (Map.Entry<Integer, OrganizationSiteStatementBean> entry : getOrganizationSiteStatementTreeMap().entrySet()) {
            OrganizationSiteStatementBean viewBean = entry.getValue();
            getOrganizationSiteStatementBeanList().add(viewBean);
            if (organizationSiteStatementmap.containsKey(viewBean.getOrganizationId())) {
                if (organizationSiteStatementmap.get(viewBean.getOrganizationId()) != null && !organizationSiteStatementmap.get(viewBean.getOrganizationId()).isEmpty()) {
                    
                    Integer x = 0;
                    balance = BigDecimal.ZERO;
                    for (OrganizationSiteStatementView idv : organizationSiteStatementmap.get(viewBean.getOrganizationId())) {
                        
                        if (!x.equals(idv.getRowNum())) {
                            x = idv.getRowNum();
                            balance = balance.add(idv.getOpenningBalance() != null ? idv.getOpenningBalance() : BigDecimal.ZERO);
                        }
                        balance = balance.add(idv.getOpenningBalance().add(idv.getAdding()).subtract(idv.getExitt()));
                        
                        OrganizationSiteStatementBean reportViewBean = new OrganizationSiteStatementBean();
                        reportViewBean.setAdding(idv.getAdding());
                        reportViewBean.setOrganizationId(idv.getOrganizationSiteId());
                        reportViewBean.setOrganizationName(idv.getOrganizationName());
                        reportViewBean.setSerial(idv.getSerial());
                        reportViewBean.setExitt(idv.getExitt());
                        reportViewBean.setDate(idv.getDate());
                        reportViewBean.setScreenName(idv.getScreenName());
                        reportViewBean.setOpenningBalance(idv.getOpenningBalance());
                        reportViewBean.setBalance(balance);
                        getOrganizationSiteStatementBeanList().add(reportViewBean);
                    }
                }
            }
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
    
    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        search();
        fillReport(prepareReport(), getUserData().getReportPath() + "organizationSiteStatementReport.jasper", getOrganizationSiteStatementBeanList(), "pdf");
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
        hashMap.put("CompanyName", getUserData().getCompany().getName());
        
        hashMap.put("CompanyLogo", getUserData().getImageReportPath());
        
        hashMap.put("dateText", userDDs.get("DATE"));
        
        hashMap.put("dateFromText", userDDs.get("YEAR_FROM"));
        
        hashMap.put("dateToText", userDDs.get("YEAR_TO"));
        hashMap.put("dateFromValue", organizationSiteStatementSearchBean.getDateFrom());
        hashMap.put("dateToValue", organizationSiteStatementSearchBean.getDateTo());
        hashMap.put("header2", userDDs.get("CLIENT_ACC_REVEAL"));
        hashMap.put("orgSiteText", userDDs.get("CUSTOMERR"));
        
        hashMap.put("openBalanceText", userDDs.get("INITIAL_BALAN"));
        
        hashMap.put("serialText", userDDs.get("INVOICE_NO"));
        hashMap.put("dateText", userDDs.get("DATE"));
        hashMap.put("screenNameText", userDDs.get("TRANSACTION_TYPE"));
        hashMap.put("orgtrans", userDDs.get("TRANSACTIONS"));
        
        hashMap.put("addingText", userDDs.get("EXPORT"));
        
        hashMap.put("exittText", userDDs.get("IMPORT"));
        hashMap.put("balanceText", userDDs.get("BALANCE"));
        
        return hashMap;
    }
    
    public List<InvOrganizationSite> completOrgSite(String query) {
        List<InvOrganizationSite> invOrgSiteList = getInvOrgSiteList();
        if (query == null || query.trim().equals("")) {
            
            setInvOrgSiteConverter(new InvOrganizationSiteConverter(invOrgSiteList));
            return invOrgSiteList;
        }
        List<InvOrganizationSite> filteredInvs = new ArrayList<>();
        
        String nameAr;
        String code;
        InvOrganizationSite invOrgSiteFilter;
        
        for (int i = 0; i < getInvOrgSiteList().size(); i++) {
            invOrgSiteFilter = invOrgSiteList.get(i);
            nameAr = invOrgSiteFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invOrgSiteFilter)) {
                    filteredInvs.add(invOrgSiteFilter);
                }
            }
            
            code = invOrgSiteFilter.getCode().toString();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invOrgSiteFilter)) {
                    filteredInvs.add(invOrgSiteFilter);
                }
            }
        }
        
        setInvOrgSiteConverter(new InvOrganizationSiteConverter(filteredInvs));
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
     * @return the organizationSiteStatementBeanList
     */
    public List<OrganizationSiteStatementBean> getOrganizationSiteStatementBeanList() {
        return organizationSiteStatementBeanList;
    }

    /**
     * @param organizationSiteStatementBeanList the
     * organizationSiteStatementBeanList to set
     */
    public void setOrganizationSiteStatementBeanList(List<OrganizationSiteStatementBean> organizationSiteStatementBeanList) {
        this.organizationSiteStatementBeanList = organizationSiteStatementBeanList;
    }

    /**
     * @return the organizationSiteStatementSearchBean
     */
    public OrganizationSiteStatementSearchBean getOrganizationSiteStatementSearchBean() {
        return organizationSiteStatementSearchBean;
    }

    /**
     * @param organizationSiteStatementSearchBean the
     * organizationSiteStatementSearchBean to set
     */
    public void setOrganizationSiteStatementSearchBean(OrganizationSiteStatementSearchBean organizationSiteStatementSearchBean) {
        this.organizationSiteStatementSearchBean = organizationSiteStatementSearchBean;
    }

    /**
     * @return the organizationSiteStatementViewList
     */
    public List<OrganizationSiteStatementView> getOrganizationSiteStatementViewList() {
        return organizationSiteStatementViewList;
    }

    /**
     * @param organizationSiteStatementViewList the
     * organizationSiteStatementViewList to set
     */
    public void setOrganizationSiteStatementViewList(List<OrganizationSiteStatementView> organizationSiteStatementViewList) {
        this.organizationSiteStatementViewList = organizationSiteStatementViewList;
    }

    /**
     * @return the organizationSiteStatementServiceReport
     */
    public OrganizationSiteStatementServiceReport getOrganizationSiteStatementServiceReport() {
        return organizationSiteStatementServiceReport;
    }

    /**
     * @param organizationSiteStatementServiceReport the
     * organizationSiteStatementServiceReport to set
     */
    public void setOrganizationSiteStatementServiceReport(OrganizationSiteStatementServiceReport organizationSiteStatementServiceReport) {
        this.organizationSiteStatementServiceReport = organizationSiteStatementServiceReport;
    }

    /**
     * @return the invOrgSiteList
     */
    public List<InvOrganizationSite> getInvOrgSiteList() {
        return invOrgSiteList;
    }

    /**
     * @param invOrgSiteList the invOrgSiteList to set
     */
    public void setInvOrgSiteList(List<InvOrganizationSite> invOrgSiteList) {
        this.invOrgSiteList = invOrgSiteList;
    }

    /**
     * @return the invOrgSiteConverter
     */
    public InvOrganizationSiteConverter getInvOrgSiteConverter() {
        return invOrgSiteConverter;
    }

    /**
     * @param invOrgSiteConverter the invOrgSiteConverter to set
     */
    public void setInvOrgSiteConverter(InvOrganizationSiteConverter invOrgSiteConverter) {
        this.invOrgSiteConverter = invOrgSiteConverter;
    }

    /**
     * @return the organizationSiteStatementSearchViewBean
     */
    public OrganizationSiteStatementSearchBean getOrganizationSiteStatementSearchViewBean() {
        return organizationSiteStatementSearchViewBean;
    }

    /**
     * @param organizationSiteStatementSearchViewBean the
     * organizationSiteStatementSearchViewBean to set
     */
    public void setOrganizationSiteStatementSearchViewBean(OrganizationSiteStatementSearchBean organizationSiteStatementSearchViewBean) {
        this.organizationSiteStatementSearchViewBean = organizationSiteStatementSearchViewBean;
    }

    /**
     * @return the organizationSiteStatementViewBean
     */
    public OrganizationSiteStatementBean getOrganizationSiteStatementViewBean() {
        return organizationSiteStatementViewBean;
    }

    /**
     * @param organizationSiteStatementViewBean the
     * organizationSiteStatementViewBean to set
     */
    public void setOrganizationSiteStatementViewBean(OrganizationSiteStatementBean organizationSiteStatementViewBean) {
        this.organizationSiteStatementViewBean = organizationSiteStatementViewBean;
    }

    /**
     * @return the organizationSiteStatementmap
     */
    public Map<Integer, List<OrganizationSiteStatementView>> getOrganizationSiteStatementmap() {
        return organizationSiteStatementmap;
    }

    /**
     * @param organizationSiteStatementmap the organizationSiteStatementmap to
     * set
     */
    public void setOrganizationSiteStatementmap(Map<Integer, List<OrganizationSiteStatementView>> organizationSiteStatementmap) {
        this.organizationSiteStatementmap = organizationSiteStatementmap;
    }

    /**
     * @return the organizationSiteStatementTreeMap
     */
    public TreeMap<Integer, OrganizationSiteStatementBean> getOrganizationSiteStatementTreeMap() {
        return organizationSiteStatementTreeMap;
    }

    /**
     * @param organizationSiteStatementTreeMap the
     * organizationSiteStatementTreeMap to set
     */
    public void setOrganizationSiteStatementTreeMap(TreeMap<Integer, OrganizationSiteStatementBean> organizationSiteStatementTreeMap) {
        this.organizationSiteStatementTreeMap = organizationSiteStatementTreeMap;
    }

    /**
     * @return the organizationSiteRootList
     */
    public List<InvOrganizationSite> getOrganizationSiteRootList() {
        return organizationSiteRootList;
    }

    /**
     * @param organizationSiteRootList the organizationSiteRootList to set
     */
    public void setOrganizationSiteRootList(List<InvOrganizationSite> organizationSiteRootList) {
        this.organizationSiteRootList = organizationSiteRootList;
    }

    /**
     * @return the organizationSiteStatementBeanMap
     */
    public Map<Integer, OrganizationSiteStatementBean> getOrganizationSiteStatementBeanMap() {
        return organizationSiteStatementBeanMap;
    }

    /**
     * @param organizationSiteStatementBeanMap the
     * organizationSiteStatementBeanMap to set
     */
    public void setOrganizationSiteStatementBeanMap(Map<Integer, OrganizationSiteStatementBean> organizationSiteStatementBeanMap) {
        this.organizationSiteStatementBeanMap = organizationSiteStatementBeanMap;
    }
    
}
