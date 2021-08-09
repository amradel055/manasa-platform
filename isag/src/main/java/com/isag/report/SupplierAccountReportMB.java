/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.GlBankTransactionService;
import com.isag.businessservice.InvPurchaseInvoiceService_1;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.reports.SupplierAccountService;
import com.isag.businessservice.reports.searchBean.SupplierAccountSearchBean;
import com.isag.converter.InvOrganizationSiteDTOConverter;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.entity.InvOrganizationSite;
import com.isag.isag.AutoComplete;
import com.isag.isag.BaseReportBean;
import com.isag.isag.UserData;
import com.isag.views.OrganizationSiteStatementView;
import com.isag.views.SupplierAccountViewBean;
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
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author hhhh
 */
@Named("supplierAccountReportMB")
@ViewScoped
public class SupplierAccountReportMB extends BaseReportBean {

    // Objects
    private AutoComplete autoComplete;
    private Integer branchId;
    Integer type = 0;
    BigDecimal balance = BigDecimal.ZERO;
    private BigDecimal totalBalance = BigDecimal.ZERO;
    private BigDecimal totalCredit = BigDecimal.ZERO;
    private BigDecimal totalDebit = BigDecimal.ZERO;
    private String uri;
    private String screenMode;
    private List<SupplierAccountViewBean> supplierAccountViewBeanList;
    private SupplierAccountViewBean supplierAccountViewBeanSelected;
    private SupplierAccountSearchBean supplierAccountSearchBean;
    private List<OrganizationSiteStatementView> organizationSiteStatementViewList;
    private List<OrganizationSiteStatementView> organizationSiteStatementViewBeforeList;
    private List<InvOrganizationSiteDTO> invOrgSiteList;
    private InvOrganizationSiteDTOConverter invOrgSiteConverter;
    //-----------------------
    private Integer stripMapReference = 0;
    private BigDecimal repositoryBalance = BigDecimal.ZERO;
    private SupplierAccountSearchBean organizationSiteStatementSearchViewBean;
    private SupplierAccountViewBean organizationSiteStatementViewBean;
    private Map<Integer, List<OrganizationSiteStatementView>> organizationSiteStatementmap = new HashMap<>();
    private Map<Integer, SupplierAccountViewBean> organizationSiteStatementBeanMap = new HashMap<>();
    private TreeMap<Integer, SupplierAccountViewBean> organizationSiteStatementTreeMap = new TreeMap<>();
    private SupplierAccountViewBean dataByOrgSiteBean;
    private List<InvOrganizationSite> organizationSiteRootList;

    private Map<Integer, BigDecimal> openingBalanceBeforeFromDateMap;
    private String url;

    BigDecimal decimal = BigDecimal.ZERO;
    BigDecimal qtyAll;
    private BigDecimal totalLook;
    private BigDecimal totalIsDone;
    private BigDecimal totalFinalAfterLook;
    private BigDecimal totalFinalAfterIsDone;

    //-----------------------
    @EJB
    private SupplierAccountService supplierAccountService;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private InvPurchaseInvoiceService_1 invPurchaseInvoiceService_1;
    @EJB
    private GlBankTransactionService glBankTransactionService;

    @Override
    @PostConstruct
    public void load() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        setScreenMode((String) context.getSessionMap().get("ScreenMode"));
        uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();

        setBranchId(getUserData().getUserBranch().getId());
        // supplierAccountSearchBean.setOrganizationType(0);
        initObjs();

        fillLists();
        organizationSiteStatementViewBeforeList = new ArrayList<>();

    }

    private void initObjs() {

//        setInvOrgSiteList(new ArrayList<>());
        setSupplierAccountSearchBean(new SupplierAccountSearchBean());

        getSupplierAccountSearchBean().setBranchId(getBranchId());
        getSupplierAccountSearchBean().setShowReport(false);
        supplierAccountSearchBean.setOrganizationType(type);

        setOrganizationSiteStatementTreeMap(new TreeMap<>());
        setOrganizationSiteStatementBeanMap(new HashMap<>());
        setOrganizationSiteStatementmap(new HashMap<>());
        openingBalanceBeforeFromDateMap = new HashMap<>();
        setSupplierAccountViewBeanList(new ArrayList<>());
    }

    public void fillLists() {
        if (getInvOrgSiteList() == null || getInvOrgSiteList().isEmpty()) {
            if (uri != null && uri.contains("clientacoount")) {
                setInvOrgSiteList(organizationSiteService.getorganizationSiteByBranchIdDTOSmall(getBranchId(), true, 0));
                setInvOrgSiteConverter(new InvOrganizationSiteDTOConverter(getInvOrgSiteList()));
            } else if (uri != null && uri.contains("contractoraccount")) {
//                setInvOrgSiteList(organizationSiteService.getSupplierByBranchIdForSupplierAccountReportMB(getBranchId(), 4));
                setInvOrgSiteList(organizationSiteService.getorganizationSiteByBranchIdDTOSmall(getBranchId(), true, 4));
                setInvOrgSiteConverter(new InvOrganizationSiteDTOConverter(getInvOrgSiteList()));
            } else {
//                setInvOrgSiteList(organizationSiteService.getSupplierByBranchIdForSupplierAccountReportMB(getBranchId(),true, 1));
                setInvOrgSiteList(organizationSiteService.getorganizationSiteByBranchIdDTOSmall(getBranchId(), true, 1));
                setInvOrgSiteConverter(new InvOrganizationSiteDTOConverter(getInvOrgSiteList()));
            }
        }
    }

    public void IntializeSearchLab() {
        organizationSiteStatementViewList = new ArrayList<>();
        setOrganizationSiteStatementmap(new HashMap<>());
        setOrganizationSiteStatementTreeMap(new TreeMap<>());
    }

    @Override
    public void reset() {
        initObjs();
//        fillLists();
        setTotalCredit(null);
        setTotalDebit(null);
        supplierAccountSearchBean = new SupplierAccountSearchBean();
        setOrganizationSiteStatementSearchViewBean(new SupplierAccountSearchBean());
        setSupplierAccountViewBeanList(new ArrayList<>());

    }

    public void showItemReport() {
    }

    @Override
    public void search() {

        setOrganizationSiteRootList(new ArrayList<>());
        if (uri != null && uri.contains("clientacoount")) {
            setOrganizationSiteRootList(organizationSiteService.getSupplierByBranchIdForSupplierAccountReportMB(branchId, 0));
        } else if (uri != null && uri.contains("contractoraccount")) {
            setOrganizationSiteRootList(organizationSiteService.getSupplierByBranchIdForSupplierAccountReportMB(branchId, 4));
        } else {
            setOrganizationSiteRootList(organizationSiteService.getSupplierByBranchIdForSupplierAccountReportMB(branchId, 1));
        }

        organizationSiteStatementmap = new HashMap<>();
        organizationSiteStatementBeanMap = new HashMap<>();
        openingBalanceBeforeFromDateMap = new HashMap<>();
        organizationSiteStatementViewBeforeList = new ArrayList<>();
        balance = BigDecimal.ZERO;
        setSupplierAccountViewBeanList(new ArrayList<>());
        getSupplierAccountSearchBean().setBranchId(getUserData().getUserBranch().getId());

        setOrganizationSiteStatementViewList(new ArrayList<>());
        if (uri != null && uri.contains("clientacoount")) {
            type = 0;
            supplierAccountSearchBean.setOrganizationType(type);
        } else if (uri != null && uri.contains("contractoraccount")) {
            type = 4;
            supplierAccountSearchBean.setOrganizationType(type);
        } else {
            type = 1;
            supplierAccountSearchBean.setOrganizationType(type);
        }
        organizationSiteStatementViewList = supplierAccountService.getAllOrganizationSiteStatementSearchBean(getSupplierAccountSearchBean());
        if (supplierAccountSearchBean.getDateFrom() != null) {
            organizationSiteStatementViewBeforeList = supplierAccountService.getAllOpeningBalanceBeforeBalanceBySearchBean(supplierAccountSearchBean);
        }
        fillOpeningBalanceMap();
        if (organizationSiteStatementViewList != null && !organizationSiteStatementViewList.isEmpty()) {
            fillGroupMapWithCorrespondingItem();
        }
        prepareRootGroupValues();
        mergeGroupsWithItems();

        if (supplierAccountViewBeanList != null && !supplierAccountViewBeanList.isEmpty()) {
            for (SupplierAccountViewBean supplierAccountViewBean : getSupplierAccountViewBeanList()) {
                BigDecimal diff = supplierAccountViewBean.getExitt().subtract(supplierAccountViewBean.getAdding());
                if (supplierAccountViewBean.getLook()) {
                    setTotalLook(getTotalLook().add(diff));
                }

                if (supplierAccountViewBean.getIsDone()) {
                    setTotalIsDone(getTotalIsDone().add(diff));
                }
            }
            setTotalFinalAfterLook((getTotalDebit().subtract(getTotalCredit())).subtract(getTotalLook()));
            setTotalFinalAfterIsDone((getTotalDebit().subtract(getTotalCredit())).subtract(getTotalIsDone()));
        }
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
        if (organizationSiteRootList != null && !organizationSiteRootList.isEmpty()) {
            for (InvOrganizationSite ig : organizationSiteRootList) {
                SupplierAccountViewBean imdbgb = new SupplierAccountViewBean();
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
                SupplierAccountViewBean imdbgb = new SupplierAccountViewBean();
                imdbgb.setOrganizationId(childGrp.getId());
                imdbgb.setOrganizationName(childGrp.getName());
                findListFromMapAndPutValue(childGrp, imdbgb);
                organizationSiteStatementBeanMap.put(childGrp.getId(), imdbgb);
                getOrganizationSiteStatementTreeMap().put(stripMapReference++, organizationSiteStatementBeanMap.get(childGrp.getId()));
                getChildTreeNodesForGroup(childGrp);
            }
        }
    }

    private void findListFromMapAndPutValue(InvOrganizationSite childGrp, SupplierAccountViewBean srvb) {
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

    public void update() {

    }

    public void save() {
        if (getSupplierAccountViewBeanList() != null && !supplierAccountViewBeanList.isEmpty()) {
            List<SupplierAccountViewBean> listInvoice = new ArrayList<>();
            List<SupplierAccountViewBean> listGlBank = new ArrayList<>();
            for (SupplierAccountViewBean supplierAccountViewBean : getSupplierAccountViewBeanList()) {
                if (supplierAccountViewBean != null && supplierAccountViewBean.getIdNumber() != null && supplierAccountViewBean.getIdNumber() > 0) {
                    if (supplierAccountViewBean.getScreenName().contains("فاتورة مشتريات")
                            || supplierAccountViewBean.getScreenName().contains("فاتورة مبيعات")
                            || supplierAccountViewBean.getScreenName().contains("مردودات المشتريات")
                            || supplierAccountViewBean.getScreenName().contains("مردودات المبيعات")) {
                        listInvoice.add(supplierAccountViewBean);
                    } else if (supplierAccountViewBean.getScreenName().contains("قبض نقدي")
                            || supplierAccountViewBean.getScreenName().contains("قبض شيك")
                            || supplierAccountViewBean.getScreenName().contains("قبض شيك خطي")) {
                        listGlBank.add(supplierAccountViewBean);
                    } else if (supplierAccountViewBean.getScreenName().contains("صرف نقدي")
                            || supplierAccountViewBean.getScreenName().contains("صرف شيك")
                            || supplierAccountViewBean.getScreenName().contains("صرف شيك حطي")) {
                        listGlBank.add(supplierAccountViewBean);
                    }
                }
            }
            invPurchaseInvoiceService_1.updatelookAndIsDone(listInvoice);
            glBankTransactionService.updatelookAndIsDone(listGlBank);
        }
    }

    public String goToEdit() {
        try {
            if (getSupplierAccountViewBeanSelected() != null && getSupplierAccountViewBeanSelected().getIdNumber() > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                if (getSupplierAccountViewBeanSelected().getScreenName().contains("فاتورة مشتريات")
                        || getSupplierAccountViewBeanSelected().getScreenName().contains("فاتورة مبيعات")
                        || getSupplierAccountViewBeanSelected().getScreenName().contains("مردودات المشتريات")
                        || getSupplierAccountViewBeanSelected().getScreenName().contains("مردودات المبيعات")) {
                    context.getSessionMap().put("invPurchaseInvoiceIdSeclected", getSupplierAccountViewBeanSelected().getIdNumber());
                    context.getSessionMap().put("ScreenMode", "editInvoice");
                    if (getUri().contains("Ceramic")) {
                        goToScreenCeramic();
                    } else {
                        goToScreen();
                    }
                } else if (getSupplierAccountViewBeanSelected().getScreenName().contains("قبض نقدي")
                        || getSupplierAccountViewBeanSelected().getScreenName().contains("قبض شيك")
                        || getSupplierAccountViewBeanSelected().getScreenName().contains("قبض شيك خطي")) {
                    context.getSessionMap().put("glBankTransactionIdSeclected", getSupplierAccountViewBeanSelected().getIdNumber());
                    context.getSessionMap().put("ScreenMode", "Edit");
                    exit("../notesreceivables/notesreceivablesForm.xhtml");
                } else if (getSupplierAccountViewBeanSelected().getScreenName().contains("صرف نقدي")
                        || getSupplierAccountViewBeanSelected().getScreenName().contains("صرف شيك")
                        || getSupplierAccountViewBeanSelected().getScreenName().contains("صرف شيك حطي")) {
                    context.getSessionMap().put("glBankTransactionIdSeclected", getSupplierAccountViewBeanSelected().getIdNumber());
                    context.getSessionMap().put("ScreenMode", "Edit");
                    exit("../settlementdeed/settlementdeedForm.xhtml");
                }
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "goToEdit");
            return null;
        }
    }

    public void goToScreen() {
        if (getSupplierAccountViewBeanSelected() != null) {
            if (getSupplierAccountViewBeanSelected().getScreenName().contains("فاتورة مشتريات")) {
                exit("../invpurchaseinvoice_1/invpurchaseinvoiceform.xhtml");
            } else if (getSupplierAccountViewBeanSelected().getScreenName().contains("فاتورة مبيعات")) {
                exit("../invsalesinvoice_1/invsalesinvoiceform.xhtml");
            } else if (getSupplierAccountViewBeanSelected().getScreenName().contains("مردودات المشتريات")) {
                exit("../invreturnpurchase_1/invreturnpurchaseform.xhtml");
            } else if (getSupplierAccountViewBeanSelected().getScreenName().contains("مردودات المبيعات")) {
                exit("../invreturnsales_1/invreturnsalesform.xhtml");
            }
        }
    }

    public void goToScreenCeramic() {
        if (getSupplierAccountViewBeanSelected().getScreenName().contains("فاتورة مشتريات")) {
            exit("../invpurchaseinvoice_1/invpurchaseinvoiceformCeramic.xhtml");
        } else if (getSupplierAccountViewBeanSelected().getScreenName().contains("فاتورة مبيعات")) {
            exit("../invsalesinvoice_1/invsalesinvoiceformCeramic.xhtml");
        } else if (getSupplierAccountViewBeanSelected().getScreenName().contains("مردودات المشتريات")) {
            exit("../invreturnpurchase_1/invreturnpurchaseformCeramic.xhtml");
        } else if (getSupplierAccountViewBeanSelected().getScreenName().contains("مردودات المبيعات")) {
            exit("../invreturnsales_1/invreturnsalesformCeramic.xhtml");
        }
    }

    public void putValueOfParent(InvOrganizationSite ParentGrp, BigDecimal bd) {
        if (organizationSiteStatementBeanMap.containsKey(ParentGrp.getId())) {
            SupplierAccountViewBean bean = organizationSiteStatementBeanMap.get(ParentGrp.getId());
            bean.setBalance(bd != null ? bd : BigDecimal.ZERO);
            bean.setBalance(bean.getBalance().add(bd));
            if (ParentGrp.getParent() != null) {
                putValueOfParent(ParentGrp.getParent(), bd);
            }
        }
    }

    public void mergeGroupsWithItems() {

        totalDebit = BigDecimal.ZERO;
        totalCredit = BigDecimal.ZERO;
        totalBalance = BigDecimal.ZERO;

        for (Map.Entry<Integer, SupplierAccountViewBean> entry : getOrganizationSiteStatementTreeMap().entrySet()) {
            SupplierAccountViewBean viewBean = entry.getValue();

            if (organizationSiteStatementmap.containsKey(viewBean.getOrganizationId())) {

                if (openingBalanceBeforeFromDateMap.containsKey(viewBean.getOrganizationId())) {
                    BigDecimal openningBalanceValue = openingBalanceBeforeFromDateMap.get(viewBean.getOrganizationId());
                    if (uri != null && uri.contains("clientacoount")) {
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

//                if (uri != null && uri.contains("clientacoount")) {
//                    if (openingBalanceBeforeFromDateMap.containsKey(viewBean.getOrganizationId())) {
//                        viewBean.setAdding(openingBalanceBeforeFromDateMap.get(viewBean.getOrganizationId())); الدائن
//                        viewBean.setScreenName("الرصيد الافتتاحي");
//                    }
//                } else {
//                    if (openingBalanceBeforeFromDateMap.containsKey(viewBean.getOrganizationId())) {
//                        viewBean.setExitt(openingBalanceBeforeFromDateMap.get(viewBean.getOrganizationId()));
//                        viewBean.setScreenName("الرصيد الافتتاحي");
//                    }
//                }
                viewBean.setMain(Boolean.TRUE);
                getSupplierAccountViewBeanList().add(viewBean);

                if (organizationSiteStatementmap.get(viewBean.getOrganizationId()) != null && !organizationSiteStatementmap.get(viewBean.getOrganizationId()).isEmpty()) {
                    Integer x = 0;
                    balance = BigDecimal.ZERO;
                    if (openingBalanceBeforeFromDateMap.containsKey(viewBean.getOrganizationId())) {
                        if (uri != null && uri.contains("clientacoount")) {
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
//                            balance = balance.add(idv.getOpenningBalance().add(idv.getAdding()).subtract(idv.getExitt()));
                            //balance = balance.add(idv.getAdding()).subtract(idv.getExitt());
                            balance = balance.add(idv.getExitt()).subtract(idv.getAdding());
                        } else {
//                            balance = balance.add(idv.getOpenningBalance().add(idv.getExitt()).subtract(idv.getAdding()));
                            balance = balance.add(idv.getExitt()).subtract(idv.getAdding());
                        }
                        SupplierAccountViewBean reportViewBean = new SupplierAccountViewBean();
                        reportViewBean.setAdding(idv.getAdding());
                        reportViewBean.setOrganizationId(idv.getOrganizationSiteId());
                        // reportViewBean.setOrganizationName(idv.getOrganizationName());
                        reportViewBean.setOpenningBalance(idv.getOpenningBalance());
                        reportViewBean.setSerial(idv.getSerial());
                        reportViewBean.setSupplierInvoiceNumber(idv.getSupplierInvoiceNumber());
                        reportViewBean.setFractureNumber(idv.getFractureNumber());
                        reportViewBean.setTotalBeforeFracture(idv.getTotalBeforeFracture());
                        reportViewBean.setValueFracture(idv.getValueFracture());
                        reportViewBean.setLook(idv.getLook() == 1);
                        reportViewBean.setIsDone(idv.getIsDone() == 1);
                        reportViewBean.setExitt(idv.getExitt());
                        reportViewBean.setDate(idv.getDate());
                        reportViewBean.setScreenName(idv.getScreenName2() == null ? idv.getScreenName() : idv.getScreenName2());
                        reportViewBean.setBalance(balance);
                        reportViewBean.setGlBankName(idv.getGlBankName());
                        reportViewBean.setInstContractSerial(idv.getInstContractSerial());
                        reportViewBean.setRemark(idv.getRemarks());
                        reportViewBean.setMain(Boolean.FALSE);
                        reportViewBean.setIdNumber(idv.getIdNumber());
                        calculateTotalForColumns(reportViewBean, 0);
                        getSupplierAccountViewBeanList().add(reportViewBean);
                    }
                }
            } else if (!organizationSiteStatementmap.containsKey(viewBean.getOrganizationId()) && openingBalanceBeforeFromDateMap.containsKey(viewBean.getOrganizationId())) {
//                viewBean.setExitt(openingBalanceBeforeFromDateMap.get(viewBean.getOrganizationId()));

                BigDecimal openningBalanceValue = openingBalanceBeforeFromDateMap.get(viewBean.getOrganizationId());
                viewBean.setOpenningBalance(openningBalanceValue);

                if (uri != null && uri.contains("clientacoount")) {
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
                getSupplierAccountViewBeanList().add(viewBean);

            }
        }

    }

    public void calculateTotalForColumns(SupplierAccountViewBean accountViewBean, Integer flow) {
        totalCredit = totalCredit.add(accountViewBean.getAdding() != null ? accountViewBean.getAdding() : BigDecimal.ZERO);
        if (flow == 0) {
            totalDebit = totalDebit.add(accountViewBean.getExitt());
            totalBalance = totalBalance.add(balance != null ? balance : BigDecimal.ZERO);
        } else {
            totalBalance = totalBalance.add(accountViewBean.getOpenningBalance() != null ? accountViewBean.getOpenningBalance() : BigDecimal.ZERO);
        }
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
//        search();
        String reportName = "supplierAccountReport.jasper";
        if (uri != null && uri.contains("contractoraccount")) {
            reportName = "contractorAccountReport.jasper";
        } else if (uri != null && uri.contains("Ceramic")) {
            reportName = "supplierAccountReportCeramic.jasper";
        }
        fillReport(prepareReport(), getUserData().getReportPath() + reportName, getSupplierAccountViewBeanList(), "pdf");
    }

//    @Override
//    public <T> void fillReport(HashMap hashMap, String reportPath, List<T> listBean, String exportType) throws JRException, IOException {
//        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listBean);
//        JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, hashMap, beanCollectionDataSource);
//        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
//        if ("pdf".equals(exportType)) {
//            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
//            servletOutputStream.flush();
//            servletOutputStream.close();
//            httpServletResponse.getOutputStream().flush();
//            httpServletResponse.getOutputStream().close();
//        } else if ("excel".equals(exportType)) {
//
//        } else if ("html".equals(exportType)) {
//
//        }
//    }
    @Override
    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();

        hashMap.put("UserName", getUserData().getUser().getName());
        hashMap.put("Branch", getUserData().getUserBranch().getNameAr());
        //    hashMap.put("CompanyName", getUserData().getCompany().getName());

        //     hashMap.put("CompanyLogo", getUserData().getImageReportPath());
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        hashMap.put("dateText", userDDs.get("DATE"));

        hashMap.put("dateFromText", userDDs.get("YEAR_FROM"));
        hashMap.put("dateFromValue", supplierAccountSearchBean.getDateFrom());

        hashMap.put("dateToText", userDDs.get("YEAR_TO"));
        hashMap.put("dateToValue", supplierAccountSearchBean.getDateTo());

        if (uri != null && uri.contains("clientacoount")) {
            hashMap.put("header2", userDDs.get("CLIENT_ACC_REVEAL"));
            hashMap.put("supplierText", userDDs.get("CUSTOMER"));
        } else if (uri != null && uri.contains("contractoraccount")) {
            hashMap.put("header2", "كشف حساب مقاول باطن");
            hashMap.put("supplierText", "المقاول الباطن");
        } else {
            hashMap.put("header2", userDDs.get("SUPPLIER_REVEAL_ACC"));
            hashMap.put("supplierText", userDDs.get("SUPPLIERR"));
        }

        hashMap.put("totalBeforeFractureText", "القيمة قبل الكسر");
        hashMap.put("fractureNumber", "عدد الكسر");
        hashMap.put("valueFractureText", "قيمة الكسر");
        hashMap.put("glBankName", "الخزينه");
        hashMap.put("openBalanceText", userDDs.get("INITIAL_BALAN"));
        hashMap.put("supplierInvoiceNumber", userDDs.get("SUPP_INVOI_NO"));
        hashMap.put("ContractNumber", userDDs.get("Contract_Number"));
        hashMap.put("ContractNumber", userDDs.get("Contract_Number"));
        hashMap.put("serialText", userDDs.get("INVOICE_NO"));
        hashMap.put("dateText", userDDs.get("DATE"));
        hashMap.put("screenNameText", userDDs.get("TRANSACTION_TYPE"));
        hashMap.put("orgtrans", userDDs.get("TRANSACTIONS"));

        hashMap.put("addingText", userDDs.get("CREDIT"));

        hashMap.put("exittText", userDDs.get("DEBIT"));
        hashMap.put("balanceText", "الرصيد");

        hashMap.put("totalText", userDDs.get("TOTAL"));

        hashMap.put("totalBalanceValue", totalBalance);
        hashMap.put("totalDebitValue", totalDebit);
        hashMap.put("totalCreditValue", totalCredit);
        hashMap.put("remarkText", "البيان");

        return hashMap;
    }

    public List<InvOrganizationSiteDTO> completOrgSiteFrom(String query) {
        return getAutoComplete().completOrgSiteDTO(query, getInvOrgSiteList(), invOrgSiteConverter);
    }

    public List<InvOrganizationSiteDTO> completOrgSiteTo(String query) {
        return getAutoComplete().completOrgSiteDTO(query, getInvOrgSiteList(), invOrgSiteConverter);
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
     * @return the invOrgSiteConverter
     */
    public InvOrganizationSiteDTOConverter getInvOrgSiteConverter() {
        return invOrgSiteConverter;
    }

    /**
     * @param invOrgSiteConverter the invOrgSiteConverter to set
     */
    public void setInvOrgSiteConverter(InvOrganizationSiteDTOConverter invOrgSiteConverter) {
        this.invOrgSiteConverter = invOrgSiteConverter;
    }

    /**
     * @return the supplierAccountViewBeanList
     */
    public List<SupplierAccountViewBean> getSupplierAccountViewBeanList() {
        return supplierAccountViewBeanList;
    }

    /**
     * @param supplierAccountViewBeanList the supplierAccountViewBeanList to set
     */
    public void setSupplierAccountViewBeanList(List<SupplierAccountViewBean> supplierAccountViewBeanList) {
        this.supplierAccountViewBeanList = supplierAccountViewBeanList;
    }

    /**
     * @return the supplierAccountSearchBean
     */
    public SupplierAccountSearchBean getSupplierAccountSearchBean() {
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
    public SupplierAccountViewBean getOrganizationSiteStatementViewBean() {
        return organizationSiteStatementViewBean;
    }

    /**
     * @param organizationSiteStatementViewBean the
     * organizationSiteStatementViewBean to set
     */
    public void setOrganizationSiteStatementViewBean(SupplierAccountViewBean organizationSiteStatementViewBean) {
        this.organizationSiteStatementViewBean = organizationSiteStatementViewBean;
    }

    /**
     * @return the organizationSiteStatementBeanMap
     */
    public Map<Integer, SupplierAccountViewBean> getOrganizationSiteStatementBeanMap() {
        return organizationSiteStatementBeanMap;
    }

    /**
     * @param organizationSiteStatementBeanMap the
     * organizationSiteStatementBeanMap to set
     */
    public void setOrganizationSiteStatementBeanMap(Map<Integer, SupplierAccountViewBean> organizationSiteStatementBeanMap) {
        this.organizationSiteStatementBeanMap = organizationSiteStatementBeanMap;
    }

    /**
     * @return the organizationSiteStatementTreeMap
     */
    public TreeMap<Integer, SupplierAccountViewBean> getOrganizationSiteStatementTreeMap() {
        return organizationSiteStatementTreeMap;
    }

    /**
     * @param organizationSiteStatementTreeMap the
     * organizationSiteStatementTreeMap to set
     */
    public void setOrganizationSiteStatementTreeMap(TreeMap<Integer, SupplierAccountViewBean> organizationSiteStatementTreeMap) {
        this.organizationSiteStatementTreeMap = organizationSiteStatementTreeMap;
    }

    /**
     * @return the dataByOrgSiteBean
     */
    public SupplierAccountViewBean getDataByOrgSiteBean() {
        return dataByOrgSiteBean;
    }

    /**
     * @param dataByOrgSiteBean the dataByOrgSiteBean to set
     */
    public void setDataByOrgSiteBean(SupplierAccountViewBean dataByOrgSiteBean) {
        this.dataByOrgSiteBean = dataByOrgSiteBean;
    }

    /**
     * @return the openingBalanceBeforeFromDateMap
     */
    public Map<Integer, BigDecimal> getOpeningBalanceBeforeFromDateMap() {
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
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
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
     * @return the autoComplete
     */
    private AutoComplete getAutoComplete() {
        if (autoComplete == null) {
            autoComplete = new AutoComplete();
        }
        return autoComplete;
    }

    /**
     * @return the supplierAccountViewBeanSelected
     */
    public SupplierAccountViewBean getSupplierAccountViewBeanSelected() {
        return supplierAccountViewBeanSelected;
    }

    /**
     * @param supplierAccountViewBeanSelected the
     * supplierAccountViewBeanSelected to set
     */
    public void setSupplierAccountViewBeanSelected(SupplierAccountViewBean supplierAccountViewBeanSelected) {
        this.supplierAccountViewBeanSelected = supplierAccountViewBeanSelected;
    }

    /**
     * @return the totalLook
     */
    public BigDecimal getTotalLook() {
        if (totalLook == null) {
            totalLook = BigDecimal.ZERO;
        }
        return totalLook;
    }

    /**
     * @param totalLook the totalLook to set
     */
    public void setTotalLook(BigDecimal totalLook) {
        this.totalLook = totalLook;
    }

    /**
     * @return the totalIsDone
     */
    public BigDecimal getTotalIsDone() {
        if (totalIsDone == null) {
            totalIsDone = BigDecimal.ZERO;
        }
        return totalIsDone;
    }

    /**
     * @param totalIsDone the totalIsDone to set
     */
    public void setTotalIsDone(BigDecimal totalIsDone) {
        this.totalIsDone = totalIsDone;
    }

    /**
     * @return the totalFinalAfterLook
     */
    public BigDecimal getTotalFinalAfterLook() {
        if (totalFinalAfterLook == null) {
            totalFinalAfterLook = BigDecimal.ZERO;
        }
        return totalFinalAfterLook;
    }

    /**
     * @param totalFinalAfterLook the totalFinalAfterLook to set
     */
    public void setTotalFinalAfterLook(BigDecimal totalFinalAfterLook) {
        this.totalFinalAfterLook = totalFinalAfterLook;
    }

    /**
     * @return the totalFinalAfterIsDone
     */
    public BigDecimal getTotalFinalAfterIsDone() {
        if (totalFinalAfterIsDone == null) {
            totalFinalAfterIsDone = BigDecimal.ZERO;
        }
        return totalFinalAfterIsDone;
    }

    /**
     * @param totalFinalAfterIsDone the totalFinalAfterIsDone to set
     */
    public void setTotalFinalAfterIsDone(BigDecimal totalFinalAfterIsDone) {
        this.totalFinalAfterIsDone = totalFinalAfterIsDone;
    }
}
