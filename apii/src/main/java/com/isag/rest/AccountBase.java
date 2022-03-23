/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.isag.api.dto.ApiAccountSearchBean;
import com.isag.api.dto.ApiSupplierAccountViewBean;
import com.isag.businessservice.CurrencyOperationService;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.reports.OrganizationSiteStatementServiceReport;
import com.isag.businessservice.reports.SupplierAccountService;
import com.isag.businessservice.reports.searchBean.OrganizationSiteStatementSearchBean;
import com.isag.businessservice.reports.searchBean.SupplierAccountSearchBean;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.entity.CurrencyOperation;
import com.isag.entity.InvOrganizationSite;
import com.isag.views.OrganizationSiteStatementView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.ejb.EJB;

/**
 *
 * @author ahmedEssam <ahmedEssam@isag.com>
 */
public class AccountBase {

    private String message;
    // Objects
    BigDecimal balance = BigDecimal.ZERO;
    private BigDecimal totalBalance = BigDecimal.ZERO;
    private BigDecimal totalCredit = BigDecimal.ZERO;
    private BigDecimal totalDebit = BigDecimal.ZERO;
    private List<ApiSupplierAccountViewBean> supplierAccountViewBeanList;
    private SupplierAccountSearchBean supplierAccountSearchBean;
    private List<OrganizationSiteStatementView> organizationSiteStatementViewList;
    private List<OrganizationSiteStatementView> organizationSiteStatementViewBeforeList;
    private List<InvOrganizationSiteDTO> invOrgSiteList;
    //-----------------------
    private Integer stripMapReference = 0;
    private BigDecimal repositoryBalance = BigDecimal.ZERO;
    private SupplierAccountSearchBean organizationSiteStatementSearchViewBean;
    private ApiSupplierAccountViewBean organizationSiteStatementViewBean;
    private Map<Integer, List<OrganizationSiteStatementView>> organizationSiteStatementmap = new HashMap<>();
    private Map<Integer, ApiSupplierAccountViewBean> organizationSiteStatementBeanMap = new HashMap<>();
    private TreeMap<Integer, ApiSupplierAccountViewBean> organizationSiteStatementTreeMap = new TreeMap<>();
    private ApiSupplierAccountViewBean dataByOrgSiteBean;
    private List<InvOrganizationSite> organizationSiteRootList;

    private Map<Integer, BigDecimal> openingBalanceBeforeFromDateMap;

    BigDecimal decimal = BigDecimal.ZERO;
    BigDecimal qtyAll;
    //-----------------------
    @EJB
    private SupplierAccountService supplierAccountService;
    @EJB
    private OrganizationSiteService organizationSiteService;

    /*-----------------------*/
    private boolean zeroAmount = false;
    private BigDecimal totalbalance;
    private BigDecimal totaladding;
    private BigDecimal totalexitt;
    private InvOrganizationSite invOrganizationSite;
    private CurrencyOperation currencyOperation;
    private List<ApiSupplierAccountViewBean> holdsupplierAccountViewBeanListTemp;

    private OrganizationSiteStatementSearchBean organizationSiteStatementSearchBean;
    @EJB
    private OrganizationSiteStatementServiceReport organizationSiteStatementServiceReport;
    @EJB
    private CurrencyOperationService currencyOperationService;

    public void searchAccountBalance(ApiAccountSearchBean apiAccountSearchBean, Integer type) {
        prepareOrganizationSiteStatementSearchBean(apiAccountSearchBean);
        Map<Integer, BigDecimal> openningBalance;
        setInvOrganizationSite(new InvOrganizationSite());
        supplierAccountViewBeanList = new ArrayList<>();
        organizationSiteStatementViewList = new ArrayList<>();
        organizationSiteStatementViewList = organizationSiteStatementServiceReport.getAllOrganizationSiteStatementBalancesSearchBean(getOrganizationSiteStatementSearchBean(), 0);
        openningBalance = organizationSiteStatementServiceReport.getAllOrganizationSiteStatementOpeningBalancesSearchBean(getOrganizationSiteStatementSearchBean());
        totalbalance = BigDecimal.ZERO;
        totaladding = BigDecimal.ZERO;
        totalexitt = BigDecimal.ZERO;
        if (organizationSiteStatementViewList != null && !organizationSiteStatementViewList.isEmpty()) {

            for (OrganizationSiteStatementView statementView : organizationSiteStatementViewList) {
                ApiSupplierAccountViewBean statementBean = new ApiSupplierAccountViewBean();
                statementBean.setSerial(statementView.getSerial());
                statementBean.setOrganizationName(statementView.getOrganizationName());
                statementBean.setOrganizationCode(statementView.getOrganizationCode());
                statementBean.setOpenningBalance(openningBalance.get(statementBean.getOrganizationId()) != null ? openningBalance.get(statementBean.getOrganizationId()) : (statementBean.getOpenningBalance() != null ? statementBean.getOpenningBalance() : BigDecimal.ZERO));
                statementBean.setAdding(statementView.getAdding());
                statementBean.setExitt(statementView.getExitt());
                statementBean.setDate(statementView.getDate());
                if (updateRate(statementView)) {
                    supplierAccountViewBeanList = new ArrayList<>();
                    setMessage(statementView.getOrganizationName() +" غير مربوط بعمله" );
                    totalbalance = BigDecimal.ZERO;
                    totaladding = BigDecimal.ZERO;
                    totalexitt = BigDecimal.ZERO;
                    break;
                }

                if (type == 0 || type == 4) {

                    statementBean.setBalance(statementBean.getOpenningBalance().add(statementBean.getAdding()).subtract(statementBean.getExitt()));
                    totalbalance = totalbalance.add(statementBean.getOpenningBalance().add(statementBean.getAdding()).subtract(statementBean.getExitt()));
                    statementBean.setTotalbalance(totalbalance);

                } else {
                    statementBean.setBalance(statementBean.getOpenningBalance().add(statementBean.getExitt()).subtract(statementBean.getAdding()));
                    totalbalance = totalbalance.add(statementBean.getOpenningBalance().add(statementBean.getExitt()).subtract(statementBean.getAdding()));
                    statementBean.setTotalbalance(totalbalance);
                }
                statementBean.setLocalBalance((getCurrencyOperation().getRate() != null ? BigDecimal.ONE : getCurrencyOperation().getRate()).multiply(statementBean.getBalance() != null ? statementBean.getBalance() : BigDecimal.ZERO));
                totaladding = totaladding.add(statementBean.getAdding());
                statementBean.setTotaladding(totaladding.add(statementBean.getAdding()));
                totalexitt = totalexitt.add(statementBean.getExitt());
                statementBean.setTotalexitt(totalexitt);

                supplierAccountViewBeanList.add(statementBean);
            }

        }
        zeroAmount = apiAccountSearchBean.getZeroAmount() != null ? apiAccountSearchBean.getZeroAmount() :zeroAmount;
        deleteZeroRecords();

    }

    public Boolean updateRate(OrganizationSiteStatementView siteStatementBean) {
        setCurrencyOperation(currencyOperationService.getRatesByDates(siteStatementBean.getOrganizationCurrencyId(), (siteStatementBean.getDate() == null ? new Date() : siteStatementBean.getDate()), getOrganizationSiteStatementSearchBean().getCompanyId()));
        if (getCurrencyOperation() != null) {
            getCurrencyOperation().setRate(getCurrencyOperation().getRate() != null ? getCurrencyOperation().getRate() : BigDecimal.ONE);
            return false;
        }
        return true;
    }

    public void deleteZeroRecords() {
        invOrganizationSite = new InvOrganizationSite();
        if (zeroAmount && supplierAccountViewBeanList != null && !supplierAccountViewBeanList.isEmpty()) {
            setHoldsupplierAccountViewBeanListTemp(new ArrayList<>(supplierAccountViewBeanList));
            Iterator it = supplierAccountViewBeanList.iterator();
            List<ApiSupplierAccountViewBean> apiSupplierAccountViewBeanIterationList = new ArrayList<>();
            while (it.hasNext()) {
                ApiSupplierAccountViewBean imb = new ApiSupplierAccountViewBean();
                imb = (ApiSupplierAccountViewBean) it.next();

                if (imb.getOpenningBalance().compareTo(BigDecimal.ZERO) != 0 || imb.getAdding().compareTo(BigDecimal.ZERO) != 0
                        || imb.getExitt().compareTo(BigDecimal.ZERO) != 0) {
                    apiSupplierAccountViewBeanIterationList.add(imb);
                }
            }
            supplierAccountViewBeanList = new ArrayList<>(apiSupplierAccountViewBeanIterationList);
        } else if (holdsupplierAccountViewBeanListTemp != null && !holdsupplierAccountViewBeanListTemp.isEmpty()) {
            supplierAccountViewBeanList = new ArrayList<>(holdsupplierAccountViewBeanListTemp);
        }
    }

    public void search(String reportName, Integer type) {
        organizationSiteStatementmap = new HashMap<>();
        organizationSiteStatementBeanMap = new HashMap<>();
        openingBalanceBeforeFromDateMap = new HashMap<>();
        organizationSiteStatementViewBeforeList = new ArrayList<>();
        balance = BigDecimal.ZERO;
        setApiSupplierAccountViewBeanList(new ArrayList());

        setOrganizationSiteStatementViewList(new ArrayList<OrganizationSiteStatementView>());

        organizationSiteStatementViewList = supplierAccountService.getAllOrganizationSiteStatementSearchBean(getSupplierAccountSearchBean());
        if (supplierAccountSearchBean.getDateFrom() != null) {
            organizationSiteStatementViewBeforeList = supplierAccountService.getAllOpeningBalanceBeforeBalanceBySearchBean(supplierAccountSearchBean);
        }
        fillOpeningBalanceMap();
        if (organizationSiteStatementViewList != null && !organizationSiteStatementViewList.isEmpty()) {
            fillGroupMapWithCorrespondingItem();
        }
        prepareRootGroupValues();
        mergeGroupsWithItems(reportName, type);

    }

    public void fillOpeningBalanceMap() {
        if (organizationSiteStatementViewBeforeList != null && !organizationSiteStatementViewBeforeList.isEmpty()) {
            for (OrganizationSiteStatementView view : organizationSiteStatementViewBeforeList) {
                if (openingBalanceBeforeFromDateMap != null && !openingBalanceBeforeFromDateMap.containsKey(view.getOrganizationSiteId())) {
                    openingBalanceBeforeFromDateMap.put(view.getOrganizationSiteId(), view.getOpenningBalance());
                }
            }
        }
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
        if (getOrganizationSiteRootList() != null && !getOrganizationSiteRootList().isEmpty()) {
            for (InvOrganizationSite ig : organizationSiteRootList) {
                ApiSupplierAccountViewBean imdbgb = new ApiSupplierAccountViewBean();
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
        if (grp.getInvOrganizationSiteCollection() != null && !grp.getInvOrganizationSiteCollection().isEmpty()) {
            for (InvOrganizationSite childGrp : grp.getInvOrganizationSiteCollection()) {
                ApiSupplierAccountViewBean imdbgb = new ApiSupplierAccountViewBean();
                imdbgb.setOrganizationId(childGrp.getId());
                imdbgb.setOrganizationName(childGrp.getName());
                findListFromMapAndPutValue(childGrp, imdbgb);
                organizationSiteStatementBeanMap.put(childGrp.getId(), imdbgb);
                getOrganizationSiteStatementTreeMap().put(stripMapReference++, organizationSiteStatementBeanMap.get(childGrp.getId()));
                getChildTreeNodesForGroup(childGrp);
            }
        }
    }

    private void findListFromMapAndPutValue(InvOrganizationSite childGrp, ApiSupplierAccountViewBean srvb) {
        if (organizationSiteStatementmap.containsKey(childGrp.getId())) {
            if (organizationSiteStatementmap.get(childGrp.getId()) != null && !organizationSiteStatementmap.get(childGrp.getId()).isEmpty()) {
                decimal = BigDecimal.ZERO;
                for (OrganizationSiteStatementView orgSiteView : organizationSiteStatementmap.get(childGrp.getId())) {
                    decimal = decimal.add(orgSiteView.getOpenningBalance() != null ? orgSiteView.getOpenningBalance() : BigDecimal.ZERO);
                }
                srvb.setOpenningBalance(decimal);
                if (childGrp.getParent() != null) {
                    putValueOfParent(childGrp.getParent(), decimal);
                } else {
                    qtyAll = organizationSiteStatementBeanMap.get(childGrp.getId()) == null ? BigDecimal.ZERO : organizationSiteStatementBeanMap.get(childGrp.getId()).getBalance();
                    repositoryBalance = repositoryBalance.add(qtyAll != null ? qtyAll : BigDecimal.ZERO);
                }
            }
        }
    }

    public void putValueOfParent(InvOrganizationSite ParentGrp, BigDecimal bd) {
        if (organizationSiteStatementBeanMap.containsKey(ParentGrp.getId())) {
            ApiSupplierAccountViewBean bean = organizationSiteStatementBeanMap.get(ParentGrp.getId());
            bean.setBalance(bd != null ? bd : BigDecimal.ZERO);
            bean.setBalance(bean.getBalance().add(bd));
            if (ParentGrp.getParent() != null) {
                putValueOfParent(ParentGrp.getParent(), bd);
            }
        }
    }

    public void mergeGroupsWithItems(String reportName, Integer type) {

        totalDebit = BigDecimal.ZERO;
        totalCredit = BigDecimal.ZERO;
        totalBalance = BigDecimal.ZERO;

        for (Map.Entry<Integer, ApiSupplierAccountViewBean> entry : getOrganizationSiteStatementTreeMap().entrySet()) {
            ApiSupplierAccountViewBean viewBean = entry.getValue();

            if (organizationSiteStatementmap.containsKey(viewBean.getOrganizationId())) {

                if (openingBalanceBeforeFromDateMap.containsKey(viewBean.getOrganizationId())) {
                    BigDecimal openningBalanceValue = openingBalanceBeforeFromDateMap.get(viewBean.getOrganizationId());
                    if (reportName.contains("clientacoount")) {
                        if (openingBalanceBeforeFromDateMap.containsKey(viewBean.getOrganizationId())) {
                            if (openningBalanceValue.compareTo(BigDecimal.ZERO) != -1) {
                                viewBean.setAdding(openningBalanceValue);

                            } else {
                                viewBean.setExitt(openningBalanceValue.multiply(new BigDecimal(-1)));
                            }
                        }
                    } else {
                        if (openingBalanceBeforeFromDateMap.containsKey(viewBean.getOrganizationId())) {
                            openningBalanceValue = openningBalanceValue != null ? openningBalanceValue.multiply(new BigDecimal(-1)) : BigDecimal.ZERO;
                            if (openningBalanceValue.compareTo(BigDecimal.ZERO) != -1) {
                                viewBean.setExitt(openningBalanceValue);
                            } else {
                                viewBean.setAdding(openningBalanceValue.multiply(new BigDecimal(-1)));
                            }
                        }
                    }
                    viewBean.setScreenName("الرصيد الافتتاحي");
                }

                viewBean.setMain(Boolean.TRUE);
                getApiSupplierAccountViewBeanList().add(viewBean);

                if (organizationSiteStatementmap.get(viewBean.getOrganizationId()) != null && !organizationSiteStatementmap.get(viewBean.getOrganizationId()).isEmpty()) {
                    Integer x = 0;
                    balance = BigDecimal.ZERO;
                    if (openingBalanceBeforeFromDateMap.containsKey(viewBean.getOrganizationId())) {
                        if (reportName.contains("clientacoount")) {
                            balance = balance.add((viewBean.getExitt() == null ? BigDecimal.ZERO : viewBean.getExitt()).subtract(viewBean.getAdding() == null ? BigDecimal.ZERO : viewBean.getAdding()));
                        } else {
                            balance = balance.add((viewBean.getExitt() == null ? BigDecimal.ZERO : viewBean.getExitt()).subtract(viewBean.getAdding() == null ? BigDecimal.ZERO : viewBean.getAdding()));
//                            balance = balance.add((viewBean.getAdding() == null ? BigDecimal.ZERO : viewBean.getAdding()).subtract(viewBean.getExitt() == null ? BigDecimal.ZERO : viewBean.getExitt()));
                        }
                    }

                    for (OrganizationSiteStatementView idv : organizationSiteStatementmap.get(viewBean.getOrganizationId())) {
//                        if (x.compareTo(idv.getRowNum()) != 0) {
//                            x = idv.getRowNum();
//                            balance = balance.add(idv.getOpenningBalance() != null ? idv.getOpenningBalance() : BigDecimal.ZERO);
//                        }

                        if (type == 0) {

                            balance = balance.add(idv.getExitt()).subtract(idv.getAdding());
                        } else {
                            balance = balance.add(idv.getExitt()).subtract(idv.getAdding());
                        }
                        ApiSupplierAccountViewBean reportViewBean = new ApiSupplierAccountViewBean();
                        reportViewBean.setAdding(idv.getAdding());
                        //     reportViewBean.setOrganizationId(idv.getOrganizationSiteId());
                        // reportViewBean.setOrganizationName(idv.getOrganizationName());
                        reportViewBean.setOpenningBalance(idv.getOpenningBalance());
                        reportViewBean.setSerial(idv.getSerial());
                        reportViewBean.setExitt(idv.getExitt());
                        reportViewBean.setDate(idv.getDate());
                        reportViewBean.setScreenName(idv.getScreenName2() == null ? idv.getScreenName() : idv.getScreenName2());
                        reportViewBean.setBalance(balance);
                        reportViewBean.setGlBankName(idv.getGlBankName());
                        reportViewBean.setInstContractSerial(idv.getInstContractSerial());
                        reportViewBean.setRemark(idv.getRemarks());
                        reportViewBean.setMain(Boolean.FALSE);
                        calculateTotalForColumns(reportViewBean, 0);
                        getApiSupplierAccountViewBeanList().add(reportViewBean);
                    }
                }
            } else if (!organizationSiteStatementmap.containsKey(viewBean.getOrganizationId()) && openingBalanceBeforeFromDateMap.containsKey(viewBean.getOrganizationId())) {
//                viewBean.setExitt(openingBalanceBeforeFromDateMap.get(viewBean.getOrganizationId()));

                BigDecimal openningBalanceValue = openingBalanceBeforeFromDateMap.get(viewBean.getOrganizationId());
                viewBean.setOpenningBalance(openningBalanceValue);

                if (reportName.contains("clientacoount")) {
                    if (openingBalanceBeforeFromDateMap.containsKey(viewBean.getOrganizationId())) {
                        if (openningBalanceValue.compareTo(BigDecimal.ZERO) != -1) {
                            viewBean.setAdding(openningBalanceValue);

                        } else {
                            viewBean.setExitt(openningBalanceValue.multiply(new BigDecimal(-1)));
                        }
                    }
                } else {
                    if (openingBalanceBeforeFromDateMap.containsKey(viewBean.getOrganizationId())) {
                        openningBalanceValue = openningBalanceValue != null ? openningBalanceValue.multiply(new BigDecimal(-1)) : BigDecimal.ZERO;
                        if (openningBalanceValue.compareTo(BigDecimal.ZERO) != -1) {
                            viewBean.setExitt(openningBalanceValue);
                        } else {
                            viewBean.setAdding(openningBalanceValue.multiply(new BigDecimal(-1)));
                        }
                    }
                }

                viewBean.setScreenName("الرصيد الافتتاحي");
                calculateTotalForColumns(viewBean, 1);
                getApiSupplierAccountViewBeanList().add(viewBean);

            }
        }

    }

    public void calculateTotalForColumns(ApiSupplierAccountViewBean accountViewBean, Integer flow) {
        totalCredit = totalCredit.add(accountViewBean.getAdding() != null ? accountViewBean.getAdding() : BigDecimal.ZERO);
        if (flow == 0) {
            totalDebit = totalDebit.add(accountViewBean.getExitt());
            totalBalance = totalBalance.add(balance != null ? balance : BigDecimal.ZERO);
        } else {
            totalBalance = totalBalance.add(accountViewBean.getOpenningBalance() != null ? accountViewBean.getOpenningBalance() : BigDecimal.ZERO);
        }
    }

    public void searchBean(ApiAccountSearchBean apiAccountSearchBean) {
        if (apiAccountSearchBean != null) {
            if (apiAccountSearchBean.getShowReport() != null) {
                getSupplierAccountSearchBean().setShowReport(apiAccountSearchBean.getShowReport());
            }
            if (apiAccountSearchBean.getDateFrom() != null) {
                getSupplierAccountSearchBean().setDateFrom(new Date(apiAccountSearchBean.getDateFrom()));
            }
            if (apiAccountSearchBean.getDateTo() != null) {
                getSupplierAccountSearchBean().setDateTo(new Date(apiAccountSearchBean.getDateTo()));
            }
            if (apiAccountSearchBean.getFromOrganizationSite() != null) {
                getSupplierAccountSearchBean().setFromorganizationNameDTO(organizationSiteService.getorganizationSiteByBranchIdAndCodeDTOSmall(getSupplierAccountSearchBean().getBranchId(), true, getSupplierAccountSearchBean().getOrganizationType(), apiAccountSearchBean.getFromOrganizationSite()));

            }
            if (apiAccountSearchBean.getToOrganizationSite() != null) {
                getSupplierAccountSearchBean().setToorganizationNameDTO(organizationSiteService.getorganizationSiteByBranchIdAndCodeDTOSmall(getSupplierAccountSearchBean().getBranchId(), true, getSupplierAccountSearchBean().getOrganizationType(), apiAccountSearchBean.getToOrganizationSite()));
            }
        }
    }

    public void prepareOrganizationSiteStatementSearchBean(ApiAccountSearchBean apiAccountSearchBean) {
        if (apiAccountSearchBean != null) {
            if (apiAccountSearchBean.getShowReport() != null) {
                getOrganizationSiteStatementSearchBean().setShowReport(apiAccountSearchBean.getShowReport());
            }
            if (apiAccountSearchBean.getDateFrom() != null) {
                getOrganizationSiteStatementSearchBean().setDateFrom(new Date(apiAccountSearchBean.getDateFrom()));
            }
            if (apiAccountSearchBean.getDateTo() != null) {
                getOrganizationSiteStatementSearchBean().setDateTo(new Date(apiAccountSearchBean.getDateTo()));
            }
            if (apiAccountSearchBean.getFromOrganizationSite() != null) {
                getOrganizationSiteStatementSearchBean().setFromorganizationName(organizationSiteService.getorganizationSiteByBranchIdAndCodeSmall(getOrganizationSiteStatementSearchBean().getBranchId(), true, apiAccountSearchBean.getOrganizationType(), apiAccountSearchBean.getFromOrganizationSite()));

            }
            if (apiAccountSearchBean.getToOrganizationSite() != null) {
                getOrganizationSiteStatementSearchBean().setToorganizationName(organizationSiteService.getorganizationSiteByBranchIdAndCodeSmall(getOrganizationSiteStatementSearchBean().getBranchId(), true, apiAccountSearchBean.getOrganizationType(), apiAccountSearchBean.getToOrganizationSite()));
            }
        }
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
     * @return the invOrgSiteList
     */
    public List<InvOrganizationSiteDTO> getInvOrgSiteList() {
        return invOrgSiteList;
    }

    /**
     * @param invOrgSiteList the invOrgSiteList to set
     */
    public void setInvOrgSiteList(List<InvOrganizationSiteDTO> invOrgSiteList) {
        this.invOrgSiteList = invOrgSiteList;
    }

    /**
     * @return the supplierAccountViewBeanList
     */
    public List<ApiSupplierAccountViewBean> getApiSupplierAccountViewBeanList() {
        if (supplierAccountViewBeanList == null) {
            setApiSupplierAccountViewBeanList(new ArrayList());
        }
        return supplierAccountViewBeanList;
    }

    /**
     * @param supplierAccountViewBeanList the supplierAccountViewBeanList to set
     */
    public void setApiSupplierAccountViewBeanList(List<ApiSupplierAccountViewBean> supplierAccountViewBeanList) {
        this.supplierAccountViewBeanList = supplierAccountViewBeanList;
    }

    /**
     * @return the supplierAccountSearchBean
     */
    public SupplierAccountSearchBean getSupplierAccountSearchBean() {
        if (supplierAccountSearchBean == null) {
            setSupplierAccountSearchBean(new SupplierAccountSearchBean());
        }
        return supplierAccountSearchBean;
    }

    /**
     * @param supplierAccountSearchBean the supplierAccountSearchBean to set
     */
    public void setSupplierAccountSearchBean(SupplierAccountSearchBean supplierAccountSearchBean) {
        this.supplierAccountSearchBean = supplierAccountSearchBean;
    }

    /**
     * @return the supplierAccountService
     */
    public SupplierAccountService getSupplierAccountService() {
        return supplierAccountService;
    }

    /**
     * @param supplierAccountService the supplierAccountService to set
     */
    public void setSupplierAccountService(SupplierAccountService supplierAccountService) {
        this.supplierAccountService = supplierAccountService;
    }

    /**
     * @return the organizationSiteService
     */
    public OrganizationSiteService getOrganizationSiteService() {
        return organizationSiteService;
    }

    /**
     * @param organizationSiteService the organizationSiteService to set
     */
    public void setOrganizationSiteService(OrganizationSiteService organizationSiteService) {
        this.organizationSiteService = organizationSiteService;
    }

    /**
     * @return the stripMapReference
     */
    public Integer getStripMapReference() {
        return stripMapReference;
    }

    /**
     * @param stripMapReference the stripMapReference to set
     */
    public void setStripMapReference(Integer stripMapReference) {
        this.stripMapReference = stripMapReference;
    }

    /**
     * @return the repositoryBalance
     */
    public BigDecimal getRepositoryBalance() {
        return repositoryBalance;
    }

    /**
     * @param repositoryBalance the repositoryBalance to set
     */
    public void setRepositoryBalance(BigDecimal repositoryBalance) {
        this.repositoryBalance = repositoryBalance;
    }

    public Map<Integer, List<OrganizationSiteStatementView>> getOrganizationSiteStatementmap() {
        if (organizationSiteStatementmap == null) {
            setOrganizationSiteStatementmap(new HashMap());
        }
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
     * @return the organizationSiteStatementSearchViewBean
     */
    public SupplierAccountSearchBean getOrganizationSiteStatementSearchViewBean() {
        return organizationSiteStatementSearchViewBean;
    }

    /**
     * @param organizationSiteStatementSearchViewBean the
     * organizationSiteStatementSearchViewBean to set
     */
    public void setOrganizationSiteStatementSearchViewBean(SupplierAccountSearchBean organizationSiteStatementSearchViewBean) {
        this.organizationSiteStatementSearchViewBean = organizationSiteStatementSearchViewBean;
    }

    /**
     * @return the organizationSiteStatementViewBean
     */
    public ApiSupplierAccountViewBean getOrganizationSiteStatementViewBean() {
        return organizationSiteStatementViewBean;
    }

    /**
     * @param organizationSiteStatementViewBean the
     * organizationSiteStatementViewBean to set
     */
    public void setOrganizationSiteStatementViewBean(ApiSupplierAccountViewBean organizationSiteStatementViewBean) {
        this.organizationSiteStatementViewBean = organizationSiteStatementViewBean;
    }

    /**
     * @return the organizationSiteStatementBeanMap
     */
    public Map<Integer, ApiSupplierAccountViewBean> getOrganizationSiteStatementBeanMap() {
        if (organizationSiteStatementBeanMap == null) {
            setOrganizationSiteStatementBeanMap(new HashMap());
        }
        return organizationSiteStatementBeanMap;
    }

    /**
     * @param organizationSiteStatementBeanMap the
     * organizationSiteStatementBeanMap to set
     */
    public void setOrganizationSiteStatementBeanMap(Map<Integer, ApiSupplierAccountViewBean> organizationSiteStatementBeanMap) {
        this.organizationSiteStatementBeanMap = organizationSiteStatementBeanMap;
    }

    /**
     * @return the organizationSiteStatementTreeMap
     */
    public TreeMap<Integer, ApiSupplierAccountViewBean> getOrganizationSiteStatementTreeMap() {
        if (organizationSiteStatementTreeMap == null) {
            setOrganizationSiteStatementTreeMap(new TreeMap());
        }
        return organizationSiteStatementTreeMap;
    }

    /**
     * @param organizationSiteStatementTreeMap the
     * organizationSiteStatementTreeMap to set
     */
    public void setOrganizationSiteStatementTreeMap(TreeMap<Integer, ApiSupplierAccountViewBean> organizationSiteStatementTreeMap) {
        this.organizationSiteStatementTreeMap = organizationSiteStatementTreeMap;
    }

    /**
     * @return the dataByOrgSiteBean
     */
    public ApiSupplierAccountViewBean getDataByOrgSiteBean() {
        return dataByOrgSiteBean;
    }

    /**
     * @param dataByOrgSiteBean the dataByOrgSiteBean to set
     */
    public void setDataByOrgSiteBean(ApiSupplierAccountViewBean dataByOrgSiteBean) {
        this.dataByOrgSiteBean = dataByOrgSiteBean;
    }

    /**
     * @return the openingBalanceBeforeFromDateMap
     */
    public Map<Integer, BigDecimal> getOpeningBalanceBeforeFromDateMap() {
        if (openingBalanceBeforeFromDateMap == null) {
            openingBalanceBeforeFromDateMap = new HashMap<>();
        }
        return openingBalanceBeforeFromDateMap;
    }

    /**
     * @param openingBalanceBeforeFromDateMap the
     * openingBalanceBeforeFromDateMap to set
     */
    public void setOpeningBalanceBeforeFromDateMap(Map<Integer, BigDecimal> openingBalanceBeforeFromDateMap) {
        this.openingBalanceBeforeFromDateMap = openingBalanceBeforeFromDateMap;
    }

    /**
     * @return the organizationSiteStatementViewBeforeList
     */
    public List<OrganizationSiteStatementView> getOrganizationSiteStatementViewBeforeList() {
        return organizationSiteStatementViewBeforeList;
    }

    /**
     * @param organizationSiteStatementViewBeforeList the
     * organizationSiteStatementViewBeforeList to set
     */
    public void setOrganizationSiteStatementViewBeforeList(List<OrganizationSiteStatementView> organizationSiteStatementViewBeforeList) {
        this.organizationSiteStatementViewBeforeList = organizationSiteStatementViewBeforeList;
    }

    /**
     * @return the totalBalance
     */
    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    /**
     * @param totalBalance the totalBalance to set
     */
    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    /**
     * @return the totalCredit
     */
    public BigDecimal getTotalCredit() {
        return totalCredit;
    }

    /**
     * @param totalCredit the totalCredit to set
     */
    public void setTotalCredit(BigDecimal totalCredit) {
        this.totalCredit = totalCredit;
    }

    /**
     * @return the totalDebit
     */
    public BigDecimal getTotalDebit() {
        return totalDebit;
    }

    /**
     * @param totalDebit the totalDebit to set
     */
    public void setTotalDebit(BigDecimal totalDebit) {
        this.totalDebit = totalDebit;
    }

    /**
     * @return the organizationSiteStatementSearchBean
     */
    public OrganizationSiteStatementSearchBean getOrganizationSiteStatementSearchBean() {
        if (organizationSiteStatementSearchBean == null) {
            setOrganizationSiteStatementSearchBean(new OrganizationSiteStatementSearchBean());
        }
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
     * @return the currencyOperation
     */
    public CurrencyOperation getCurrencyOperation() {
        return currencyOperation;
    }

    /**
     * @param currencyOperation the currencyOperation to set
     */
    public void setCurrencyOperation(CurrencyOperation currencyOperation) {
        this.currencyOperation = currencyOperation;
    }

    /**
     * @return the holdsupplierAccountViewBeanListTemp
     */
    public List<ApiSupplierAccountViewBean> getHoldsupplierAccountViewBeanListTemp() {
        return holdsupplierAccountViewBeanListTemp;
    }

    /**
     * @param holdsupplierAccountViewBeanListTemp the
     * holdsupplierAccountViewBeanListTemp to set
     */
    public void setHoldsupplierAccountViewBeanListTemp(List<ApiSupplierAccountViewBean> holdsupplierAccountViewBeanListTemp) {
        this.holdsupplierAccountViewBeanListTemp = holdsupplierAccountViewBeanListTemp;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
