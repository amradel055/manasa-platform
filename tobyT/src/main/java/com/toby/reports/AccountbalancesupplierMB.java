/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.reports;


import com.toby.businessservice.report.OrganizationSiteStatementServiceReport;
import com.toby.businessservice.CurrencyOperationService;
import com.toby.businessservice.OrganizationSiteService;
import com.toby.businessservice.reports.searchBean.OrganizationSiteStatementSearchBean;
import com.toby.converter.InvOrganizationSiteConverter;
import com.toby.entity.Currency;
import com.toby.entity.CurrencyOperation;
import com.toby.entity.InvOrganizationSite;
import com.toby.report.entity.OrganizationSiteStatementBean;
import com.toby.toby.BaseReportBean;
import com.toby.toby.UserData;
import com.toby.views.OrganizationSiteStatementView;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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

/**
 *
 * @author ahmed
 */
@Named("accountbalancesupplierMB")
@ViewScoped
public class AccountbalancesupplierMB extends BaseReportBean {

    private BigDecimal totalbalance;
    private BigDecimal totaladding;
    private BigDecimal totalexitt;
    private InvOrganizationSite invOrganizationSite;
    private List<InvOrganizationSite> invOrgSiteList;
    private List<Currency> currencyList;
    private CurrencyOperation currencyOperation;
    private InvOrganizationSiteConverter invOrgSiteConverter;
    private OrganizationSiteStatementSearchBean organizationSiteStatementSearchBean;
    private List<OrganizationSiteStatementView> organizationSiteStatementViewList;
    private List<OrganizationSiteStatementBean> organizationSiteStatementBeanList;
    private List<OrganizationSiteStatementBean> holdOrganizationSiteStatementBeanListTemp;
    private boolean zeroAmount = false;
    private String uri;
    private Integer type;

    @EJB
    OrganizationSiteStatementServiceReport organizationSiteStatementServiceReport;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private CurrencyOperationService currencyOperationService;

    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        setUri(((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI());
        if (uri.contains("accountbalancesupplier")) {
            type = 0;
        } else {
            type = 1;
        }
        //------------------------------
        //  invOrganizationSite = new InvOrganizationSite();
        reset();
        //-------------------------------
        load();
    }

    @Override
    public void load() {
        invOrganizationSite = new InvOrganizationSite();
        if (type == 0) {
            invOrgSiteList = organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(getUserData().getUserBranch().getId(), true, 1);
        } else {
            invOrgSiteList = organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(getUserData().getUserBranch().getId(), true, 0);
        }
        invOrgSiteConverter = new InvOrganizationSiteConverter(invOrgSiteList);
    }

    @Override
    public void reset() {
        setTotaladding(null);
        setTotalbalance(null);
        setTotalexitt(null);
        zeroAmount = false;

        organizationSiteStatementViewList = new ArrayList<>();
        organizationSiteStatementBeanList = new ArrayList<>();
        holdOrganizationSiteStatementBeanListTemp = new ArrayList<>();
        organizationSiteStatementSearchBean = new OrganizationSiteStatementSearchBean();
        invOrganizationSite = new InvOrganizationSite();
        //  getInvOrganizationSite();
    }

    @Override
    public void search() {
        Map<Integer, BigDecimal> openningBalance;
        invOrganizationSite = new InvOrganizationSite();
        organizationSiteStatementBeanList = new ArrayList<>();
        organizationSiteStatementViewList = new ArrayList<>();
        organizationSiteStatementSearchBean.setBranchId(getUserData().getUserBranch() != null ? getUserData().getUserBranch().getId() : null);
        organizationSiteStatementSearchBean.setOrganizationType(type);
        organizationSiteStatementViewList = organizationSiteStatementServiceReport.getAllOrganizationSiteStatementBalancesSearchBean(organizationSiteStatementSearchBean, 0);
        openningBalance = organizationSiteStatementServiceReport.getAllOrganizationSiteStatementOpeningBalancesSearchBean(organizationSiteStatementSearchBean);
        totalbalance = BigDecimal.ZERO;
        totaladding = BigDecimal.ZERO;
        totalexitt = BigDecimal.ZERO;
        if (organizationSiteStatementViewList != null && !organizationSiteStatementViewList.isEmpty()) {

            for (OrganizationSiteStatementView statementView : organizationSiteStatementViewList) {
                OrganizationSiteStatementBean statementBean = new OrganizationSiteStatementBean();
                statementBean.setSerial(statementView.getSerial());
                statementBean.setOrganizationName(statementView.getOrganizationName());
                statementBean.setOrganizationCode(statementView.getOrganizationCode());
                statementBean.setOrganizationId(statementView.getOrganizationSiteId());
                statementBean.setOpenningBalance(openningBalance.get(statementBean.getOrganizationId()) != null ? openningBalance.get(statementBean.getOrganizationId()) : (statementBean.getOpenningBalance() != null ? statementBean.getOpenningBalance() : BigDecimal.ZERO));
                statementBean.setAdding(statementView.getAdding());
                statementBean.setExitt(statementView.getExitt());
                statementBean.setDate(statementView.getDate());
                if (updateRate(statementView)) {
                    setShowMessageGeneral(true);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", " عميل غير مربوط بعملة" + statementView.getOrganizationName()));
                    organizationSiteStatementBeanList = new ArrayList<>();
                    totalbalance = BigDecimal.ZERO;
                    totaladding = BigDecimal.ZERO;
                    totalexitt = BigDecimal.ZERO;
                    break;
                }

                if (type == 0) {

                    statementBean.setBalance(statementBean.getOpenningBalance().add(statementBean.getAdding()).subtract(statementBean.getExitt()));
                    totalbalance = totalbalance.add(statementBean.getOpenningBalance().add(statementBean.getAdding()).subtract(statementBean.getExitt()));
                    statementBean.setTotalbalance(totalbalance);

                } else {
                    statementBean.setBalance(statementBean.getOpenningBalance().add(statementBean.getExitt()).subtract(statementBean.getAdding()));
                    totalbalance = totalbalance.add(statementBean.getOpenningBalance().add(statementBean.getExitt()).subtract(statementBean.getAdding()));
                    statementBean.setTotalbalance(totalbalance);
                }
                statementBean.setLocalBalance((currencyOperation.getRate() != null ? BigDecimal.ONE : currencyOperation.getRate()).multiply(statementBean.getBalance() != null ? statementBean.getBalance() : BigDecimal.ZERO));
                totaladding = totaladding.add(statementBean.getAdding());
                statementBean.setTotaladding(totaladding.add(statementBean.getAdding()));
                totalexitt = totalexitt.add(statementBean.getExitt());
                statementBean.setTotalexitt(totalexitt);

                organizationSiteStatementBeanList.add(statementBean);
            }

        }
        deleteZeroRecords();

    }

    public Boolean updateRate(OrganizationSiteStatementView siteStatementBean) {

        currencyOperation = currencyOperationService.getRatesByDates(siteStatementBean.getOrganizationCurrencyId(), (siteStatementBean.getDate() == null ? new Date() : siteStatementBean.getDate()), getUserData().getCompany().getId());
        if (currencyOperation != null) {
            currencyOperation.setRate(currencyOperation.getRate() != null ? currencyOperation.getRate() : BigDecimal.ONE);
            return false;
        }
        // invOrganizationSite.setRate((currencyOperation == null ? BigDecimal.ONE : currencyOperation.getRate()).multiply());
        return true;
    }

    public void deleteZeroRecords() {
        invOrganizationSite = new InvOrganizationSite();
        if (zeroAmount && organizationSiteStatementBeanList != null && !organizationSiteStatementBeanList.isEmpty()) {
            holdOrganizationSiteStatementBeanListTemp = new ArrayList<>(organizationSiteStatementBeanList);
            Iterator it = organizationSiteStatementBeanList.iterator();
            List<OrganizationSiteStatementBean> organizationSiteStatementBeanIterationList = new ArrayList<>();
            while (it.hasNext()) {
                OrganizationSiteStatementBean imb = new OrganizationSiteStatementBean();
                imb = (OrganizationSiteStatementBean) it.next();

                if (imb.getOpenningBalance().compareTo(BigDecimal.ZERO) != 0 || imb.getAdding().compareTo(BigDecimal.ZERO) != 0
                        || imb.getExitt().compareTo(BigDecimal.ZERO) != 0) {
                    organizationSiteStatementBeanIterationList.add(imb);
                }
            }
            organizationSiteStatementBeanList = new ArrayList<>(organizationSiteStatementBeanIterationList);
        } else if (holdOrganizationSiteStatementBeanListTemp != null && !holdOrganizationSiteStatementBeanListTemp.isEmpty()) {
            organizationSiteStatementBeanList = new ArrayList<>(holdOrganizationSiteStatementBeanListTemp);
        }
    }

    @Override
    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();
        hashMap.put("PrintedBy", getUserData().getUser().getName());
        hashMap.put("branchName", getUserData().getCompany().getName());
        //  hashMap.put("companyName", getUserData().getCompany().getName());
        if (type == 0) {
            hashMap.put("reportNameText", userDDs.get("ACC_BALA_SUPPLIERS"));
            hashMap.put("supplierFromText", userDDs.get("FROM_SUPPLIER"));
            hashMap.put("supplierToText", userDDs.get("TO_SUPPLIER"));
            hashMap.put("supplierName", userDDs.get("SUPPLIER_NAME"));
        } else {
            hashMap.put("reportNameText", userDDs.get("CUSTOM_ACC_BALA"));
            hashMap.put("supplierFromText", userDDs.get("FROM_CUSTOM"));
            hashMap.put("supplierToText", userDDs.get("TO_CUSTOM"));
            hashMap.put("supplierName", userDDs.get("CUSTOM_NAME"));
        }

        hashMap.put("supplierFrom", organizationSiteStatementSearchBean.getFromorganizationName() != null ? organizationSiteStatementSearchBean.getFromorganizationName().getName() : null);

        hashMap.put("supplierTo", organizationSiteStatementSearchBean.getToorganizationName() != null ? organizationSiteStatementSearchBean.getToorganizationName().getName() : null);
        hashMap.put("dateFromText", userDDs.get("YEAR_FROM"));
        hashMap.put("dateFrom", organizationSiteStatementSearchBean.getDateFrom());
        hashMap.put("dateToText", userDDs.get("YEAR_TO"));
        hashMap.put("dateTo", organizationSiteStatementSearchBean.getDateTo());

        hashMap.put("organizationCode", userDDs.get("SUPPL_CODE"));

        hashMap.put("openingBalance", userDDs.get("INITIAL_BALAN"));
        hashMap.put("transactionTotal", userDDs.get("TOTA_TRANSACT"));
        hashMap.put("debit", userDDs.get("DEBIT"));
        hashMap.put("credit", userDDs.get("CREDIT"));
        hashMap.put("balance", userDDs.get("BALANCE"));

        hashMap.put("localBalance", userDDs.get("BALA_LOCA_CURRE"));

        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (organizationSiteStatementBeanList != null && !organizationSiteStatementBeanList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "accountBalanceSupplierReport.jasper", organizationSiteStatementBeanList, "pdf");
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

    public List<InvOrganizationSite> completOrgSite(String query) {
        List<InvOrganizationSite> invOrganizationSites = invOrgSiteList;
        if (query == null || query.trim().equals("")) {

            setInvOrgSiteConverter(new InvOrganizationSiteConverter(invOrganizationSites));
            return invOrganizationSites;
        }
        List<InvOrganizationSite> filteredInvs = new ArrayList<>();

        String nameAr;
        String code;
        InvOrganizationSite invOrgSiteFilter;

        for (int i = 0; i < invOrgSiteList.size(); i++) {
            invOrgSiteFilter = invOrganizationSites.get(i);
            nameAr = invOrgSiteFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invOrgSiteFilter)) {
                    filteredInvs.add(invOrgSiteFilter);
                }
            }

            code = invOrgSiteFilter.getCode();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invOrgSiteFilter)) {
                    filteredInvs.add(invOrgSiteFilter);
                }
            }
        }

        setInvOrgSiteConverter(new InvOrganizationSiteConverter(filteredInvs));
        return filteredInvs;
    }

    /**
     * @return the invOrgSiteList
     */
    public List<InvOrganizationSite> getInvOrgSiteList() {
        if (invOrgSiteList == null || invOrgSiteList.isEmpty()) {
            invOrgSiteList = new ArrayList<>();
        }
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
     * @return the zeroAmount
     */
    public boolean isZeroAmount() {
        return zeroAmount;
    }

    /**
     * @param zeroAmount the zeroAmount to set
     */
    public void setZeroAmount(boolean zeroAmount) {
        this.zeroAmount = zeroAmount;
    }

    /**
     * @return the holdOrganizationSiteStatementBeanListTemp
     */
    public List<OrganizationSiteStatementBean> getHoldOrganizationSiteStatementBeanListTemp() {
        return holdOrganizationSiteStatementBeanListTemp;
    }

    /**
     * @param holdOrganizationSiteStatementBeanListTemp the
     * holdOrganizationSiteStatementBeanListTemp to set
     */
    public void setHoldOrganizationSiteStatementBeanListTemp(List<OrganizationSiteStatementBean> holdOrganizationSiteStatementBeanListTemp) {
        this.holdOrganizationSiteStatementBeanListTemp = holdOrganizationSiteStatementBeanListTemp;
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
     * @return the totalbalance
     */
    public BigDecimal getTotalbalance() {
        return totalbalance;
    }

    /**
     * @param totalbalance the totalbalance to set
     */
    public void setTotalbalance(BigDecimal totalbalance) {
        this.totalbalance = totalbalance;
    }

    /**
     * @return the totaladding
     */
    public BigDecimal getTotaladding() {
        return totaladding;
    }

    /**
     * @param totaladding the totaladding to set
     */
    public void setTotaladding(BigDecimal totaladding) {
        this.totaladding = totaladding;
    }

    /**
     * @return the totalexitt
     */
    public BigDecimal getTotalexitt() {
        return totalexitt;
    }

    /**
     * @param totalexitt the totalexitt to set
     */
    public void setTotalexitt(BigDecimal totalexitt) {
        this.totalexitt = totalexitt;
    }

    /**
     * @return the currencyOperationService
     */
    public CurrencyOperationService getCurrencyOperationService() {
        return currencyOperationService;
    }

    /**
     * @param currencyOperationService the currencyOperationService to set
     */
    public void setCurrencyOperationService(CurrencyOperationService currencyOperationService) {
        this.currencyOperationService = currencyOperationService;
    }

    /**
     * @return the invOrganizationSite
     */
    public InvOrganizationSite getInvOrganizationSite() {
        return invOrganizationSite;
    }

    /**
     * @param invOrganizationSite the invOrganizationSite to set
     */
    public void setInvOrganizationSite(InvOrganizationSite invOrganizationSite) {
        this.invOrganizationSite = invOrganizationSite;
    }

    /**
     * @return the currencyList
     */
    public List<Currency> getCurrencyList() {
        return currencyList;
    }

    /**
     * @param currencyList the currencyList to set
     */
    public void setCurrencyList(List<Currency> currencyList) {
        this.currencyList = currencyList;
    }

}
