/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.isag.dashboard;

import com.isag.businessservice.GlBankService;
import com.isag.businessservice.GlBankTransactionService;
import com.isag.businessservice.GmSubscriptionsService;
import com.isag.businessservice.InvPurchaseInvoiceDetailsService;
import com.isag.businessservice.InvPurchaseInvoiceService;
import com.isag.businessservice.InvPurchaseInvoiceService_1;
import com.isag.businessservice.InvStripService;
import com.isag.businessservice.IsagUserInventoryService;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.QuantityItemsStoreByDateService;
import com.isag.businessservice.reports.BankBalanceViewService;
import com.isag.businessservice.reports.SupplierAccountService;
import com.isag.dto.GlBankDTO;
import com.isag.entity.GlBank;
import com.isag.entity.InvInventory;
import com.isag.isag.BaseListBean;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.TabChangeEvent;

/**
 *
 * @author hq002
 */
@Named(value = "dashboardMB")
@ViewScoped
public class DashboardMB extends BaseListBean {

    private Integer invType;
    private Integer serial;
    private BigDecimal TotalNetSalesByDay;
    private BigDecimal TotalNetSalesByMonth;
    private BigDecimal TotalNetReturbSalesByDay;
    private BigDecimal TotalNetReturbSalesByMonth;
    private BigDecimal TotalNetPurchaseByDate;
    private BigDecimal TotalNetPurchaseByMonth;
    private BigDecimal TotalNetReturnPurchaseByDate;
    private BigDecimal TotalNetReturnPurchaseByMonth;
    private Integer NetSalesNumberByDay;
    private Integer NetSalesNumberByMonth;
    private Integer NetReturbSalesNumberByDay;
    private Integer NetReturbSalesNumberByMonth;
    private Integer NetPurchaseNumberByDate;
    private Integer NetPurchaseNumberByMonth;
    private Integer NetReturnPurchaseNumberByDate;
    private Integer NetReturnPurchaseNumberByMonth;
    private BigDecimal TotalNetGmByDate;
    private BigDecimal TotalNetGmByMonth;
    private Integer invNumGmByDate;
    private Integer invNumGmByMonth;
    private List<InvInventory> inventoryList;
    private List<GlBank> glBanksList;
    private List<GlBankDTO> cashByDateList;
    private List<GlBankDTO> cashByMonthList;
    private List<GlBankDTO> cashByDateNumberList;
    private List<GlBankDTO> cashByMonthNumberList;
    private List<GlBankDTO> settlementByDateList;
    private List<GlBankDTO> settlementByMonthList;
    private List<GlBankDTO> settlementByDateNumberList;
    private List<GlBankDTO> settlementByMonthNumberList;
    private List<GlBankDTO> transferByDateList;
    private List<GlBankDTO> transferByMonthList;
    private List<GlBankDTO> transferByDateNumberList;
    private List<GlBankDTO> transferByMonthNumberList;
    private Integer supplierNum;
    private Integer invoiceNumsupplierByDate;
    private Integer invoiceNumsupplierByMonth;
    private BigDecimal totalNetSuppliers;
    private Integer customerNum;
    private Integer invoiceNumcustomerByDate;
    private Integer invoiceNumcustomerByMonth;
    private BigDecimal totalNetCustomers;
    private Integer itemNumbers;

    @EJB
    InvPurchaseInvoiceService_1 invPurchaseInvoiceService_1;
    @EJB
    GlBankTransactionService glBankTransactionService;
    @EJB
    GmSubscriptionsService gmSubscriptionsService;
    @EJB
    InvPurchaseInvoiceDetailsService invPurchaseInvoiceDetailsService;
    @EJB
    private IsagUserInventoryService isagUserInventoryService;
    @EJB
    GlBankService glBankService;
    @EJB
    BankBalanceViewService bankBalanceViewService;
    @EJB
    OrganizationSiteService organizationSiteService;
    @EJB
    InvPurchaseInvoiceService invPurchaseInvoiceService;
    @EJB
    SupplierAccountService supplierAccountService;
    @EJB
    InvStripService invStripService;
  
    @PostConstruct
    @Override

    public void init() {
        try {

            loadPer();
        } catch (Exception e) {
            saveError(e, "dashboardMB", "init");
        }
    }

    public void loadPer() {

    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            if (null != getInvType()) {
                switch (getInvType()) {
                    case 0:
                        context.getSessionMap().put("ScreenMode", "addInvoice");
                        context.getSessionMap().put("typeActivity", 0);
                        exit("/igma/faces/views/invsalesinvoice_1/invsalesinvoiceform.xhtml");
                        return "";
                    case 1:
                        context.getSessionMap().put("ScreenMode", "addInvoice");
                        context.getSessionMap().put("typeActivity", 0);
                        exit("/igma/faces/views/invreturnsales_1/invreturnsalesform.xhtml");
                        return "";
                    case 2:
                        context.getSessionMap().put("ScreenMode", "addInvoice");
                        exit("/igma/faces/views/invpurchaseinvoice_1/invpurchaseinvoiceform.xhtml");
                        context.getSessionMap().put("typeActivity", 0);
                        return "";
                    case 3:
                        context.getSessionMap().put("ScreenMode", "addInvoice");
                        exit("/igma/faces/views/invreturnpurchase_1/invreturnpurchaseform.xhtml");
                        context.getSessionMap().put("typeActivity", 0);
                        return "";
                    case 4:
                        context.getSessionMap().put("ScreenMode", "Add");
                        exit("/igma/faces/views/settlementdeed/settlementdeedForm.xhtml");
                        return "";
                    case 5:
                        context.getSessionMap().put("ScreenMode", "Add");
                        exit("/igma/faces/views/notesreceivables/notesreceivablesForm.xhtml");
                        return "";
                    case 6:
                        context.getSessionMap().put("ScreenMode", "Add");
                        exit("/igma/faces/views/glBankTransaction/glBankTransactionForm.xhtml");
                        return "";
                    case 7:
                        context.getSessionMap().put("ScreenMode", "addGmSubscriptions");
                        exit("/igma/faces/views/gmsubscriptions/gmsubscriptionsform.xhtml");
                        return "";
                    default:
                        break;
                }
            }

        } catch (Exception e) {
            saveError(e, "dashboardMB", "goToAdd");
            return null;
        }
        return "";
    }

    /**
     * @return the invType
     */
    public Integer getInvType() {
        return invType;
    }

    /**
     * @param invType the invType to set
     */
    public void setInvType(Integer invType) {
        this.invType = invType;
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToEdit() {
        if (serial != null && serial > 0) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

            if (null != getInvType()) {
                switch (getInvType()) {
                    case 0:
                        context.getSessionMap().put("ScreenMode", "editInvoice");
                        Integer id = invPurchaseInvoiceService_1.findInvPurchaseInvoiceBySerialNew(serial, getUserlogin().getBranchId().getId(), 1);
                        if (id != null) {
                            context.getSessionMap().put("invPurchaseInvoiceIdSeclected", id);
                            exit("/igma/faces/views/invsalesinvoice_1/invsalesinvoiceform.xhtml");
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null,
                                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "غير موجوده"));
                        }
                        break;
                    case 1:
                        context.getSessionMap().put("ScreenMode", "editInvoice");
                        Integer id1 = invPurchaseInvoiceService_1.findInvPurchaseInvoiceBySerialNew(serial, getUserlogin().getBranchId().getId(), 3);
                        if (id1 != null) {
                            context.getSessionMap().put("invPurchaseInvoiceIdSeclected", id1);
                            exit("/igma/faces/views/invreturnsales_1/invreturnsalesform.xhtml");
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null,
                                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "غير موجوده"));
                        }
                        break;
                    case 2:
                        context.getSessionMap().put("ScreenMode", "editInvoice");
                        Integer id2 = invPurchaseInvoiceService_1.findInvPurchaseInvoiceBySerialNew(serial, getUserlogin().getBranchId().getId(), 0);
                        if (id2 != null) {
                            context.getSessionMap().put("invPurchaseInvoiceIdSeclected", id2);
                            exit("/igma/faces/views/invpurchaseinvoice_1/invpurchaseinvoiceform.xhtml");
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null,
                                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "غير موجوده"));
                        }
                        break;

                    case 3:
                        context.getSessionMap().put("ScreenMode", "editInvoice");
                        Integer id3 = invPurchaseInvoiceService_1.findInvPurchaseInvoiceBySerialNew(serial, getUserlogin().getBranchId().getId(), 2);
                        if (id3 != null) {
                            context.getSessionMap().put("invPurchaseInvoiceIdSeclected", id3);
                            exit("/igma/faces/views/invreturnpurchase_1/invreturnpurchaseform.xhtml");
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null,
                                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "غير موجوده"));
                        }
                        break;
                    case 4:
                        context.getSessionMap().put("ScreenMode", "edit");
                        Integer id4 = glBankTransactionService.findGlBankTransactionBySerial(serial, getUserlogin().getBranchId().getId(), 0);
                        if (id4 != null) {
                            context.getSessionMap().put("glBankTransactionIdSeclected", id4);
                            exit("/igma/faces/views/settlementdeed/settlementdeedForm.xhtml");
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null,
                                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "غير موجوده"));
                        }
                        break;
                    case 5:
                        context.getSessionMap().put("ScreenMode", "edit");
                        Integer id5 = glBankTransactionService.findGlBankTransactionBySerial(serial, getUserlogin().getBranchId().getId(), 1);
                        if (id5 != null) {
                            context.getSessionMap().put("glBankTransactionIdSeclected", id5);
                            exit("/igma/faces/views/notesreceivables/notesreceivablesForm.xhtml");
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null,
                                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "غير موجوده"));
                        }
                        break;

                    case 6:
                        context.getSessionMap().put("ScreenMode", "edit");
                        Integer id6 = glBankTransactionService.findGlBankTransactionBySerial(serial, getUserlogin().getBranchId().getId(), 2);
                        if (id6 != null) {
                            context.getSessionMap().put("glBankTransactionIdSeclected", id6);
                            exit("/igma/faces/views/glBankTransaction/glBankTransactionForm.xhtml");
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null,
                                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "غير موجوده"));
                        }
                        break;

                    case 7:
                        context.getSessionMap().put("ScreenMode", "editGmSubscriptions");
                        Integer id7 = gmSubscriptionsService.findGmSubscriptionsDTOBySerialNew(serial, getUserlogin().getBranchId().getId());
                        if (id7 != null) {
                            context.getSessionMap().put("gmSubscriptionsIdSelected", id7);
                            exit("/igma/faces/views/gmsubscriptions/gmsubscriptionsform.xhtml");
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null,
                                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "غير موجوده"));
                        }
                        return "";

                    default:
                        break;
                }
            }
            return "";
        }

        return "";

    }

    /**
     * @return the serial
     */
    public Integer getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public void onTabChange(TabChangeEvent event) {
        switch (event.getTab().getTitle()) {
            case "المبيعات":
                fillSales();
                break;
            case "مردود المبيعات":
                fillSalesReturn();
                break;
            case "المشتريات":
                fillPurchase();
                break;
            case "مردود المشتريات ":
                fillPurchaseReturn();
                break;
            case "الموردين":
                fillSuppliers();
                break;
            case "العملاء":
                fillCustomers();
                break;
            case "المخزن":
                fillventory();
                break;
            case "المقبوضات":
                fillCach();
                break;
            case "الخزائن":
                fillInventory();
                break;
            case "المدفوعات":
                fillSettlement();
                break;
            case "التحويلات":
                fillTransfer();
                break;
            case "الجيم":
                fillElGem();
                break;
            default:
                break;
        }
    }

    public void fillSales() {
        setTotalNetSalesByDay(invPurchaseInvoiceDetailsService.FindTotalvalueByDate(getInventoryList(), getUserData().getUserBranch().getId(), 1));
        setNetSalesNumberByDay(invPurchaseInvoiceDetailsService.FindInvoiceNumberByDate(getInventoryList(), getUserData().getUserBranch().getId(), 1));
        setNetSalesNumberByMonth(invPurchaseInvoiceDetailsService.FindInvoiceNumberByMonth(getInventoryList(), getUserData().getUserBranch().getId(), 1));
        setTotalNetSalesByMonth(invPurchaseInvoiceDetailsService.FindTotalvalueByMonth(getInventoryList(), getUserData().getUserBranch().getId(), 1));
    }

    public void fillSalesReturn() {
        setTotalNetReturbSalesByMonth(invPurchaseInvoiceDetailsService.FindTotalvalueByMonth(getInventoryList(), getUserData().getUserBranch().getId(), 3));
        setTotalNetReturbSalesByDay(invPurchaseInvoiceDetailsService.FindTotalvalueByDate(getInventoryList(), getUserData().getUserBranch().getId(), 3));
        setNetReturbSalesNumberByDay(invPurchaseInvoiceDetailsService.FindInvoiceNumberByDate(getInventoryList(), getUserData().getUserBranch().getId(), 3));
        setNetReturbSalesNumberByMonth(invPurchaseInvoiceDetailsService.FindInvoiceNumberByMonth(getInventoryList(), getUserData().getUserBranch().getId(), 3));

    }

    public void fillPurchase() {

        setTotalNetPurchaseByDate(invPurchaseInvoiceDetailsService.FindTotalvalueByDate(getInventoryList(), getUserData().getUserBranch().getId(), 0));
        setTotalNetPurchaseByMonth(invPurchaseInvoiceDetailsService.FindTotalvalueByMonth(getInventoryList(), getUserData().getUserBranch().getId(), 0));
        setNetPurchaseNumberByDate(invPurchaseInvoiceDetailsService.FindInvoiceNumberByDate(getInventoryList(), getUserData().getUserBranch().getId(), 0));
        setNetPurchaseNumberByMonth(invPurchaseInvoiceDetailsService.FindInvoiceNumberByMonth(getInventoryList(), getUserData().getUserBranch().getId(), 0));
    }

    public void fillPurchaseReturn() {

        setTotalNetReturnPurchaseByDate(invPurchaseInvoiceDetailsService.FindTotalvalueByDate(getInventoryList(), getUserData().getUserBranch().getId(), 2));
        setTotalNetReturnPurchaseByMonth(invPurchaseInvoiceDetailsService.FindTotalvalueByMonth(getInventoryList(), getUserData().getUserBranch().getId(), 2));
        setNetReturnPurchaseNumberByDate(invPurchaseInvoiceDetailsService.FindInvoiceNumberByDate(getInventoryList(), getUserData().getUserBranch().getId(), 2));
        setNetReturnPurchaseNumberByMonth(invPurchaseInvoiceDetailsService.FindInvoiceNumberByMonth(getInventoryList(), getUserData().getUserBranch().getId(), 2));
    }

    public void fillSuppliers() {
        setSupplierNum(organizationSiteService.FindSupplier(getUserData().getUserBranch().getId(), null));
        setInvoiceNumsupplierByDate(invPurchaseInvoiceService.FindSupplierInvoiceNumberByDate(getUserData().getUserBranch().getId(), null));
        setInvoiceNumsupplierByMonth(invPurchaseInvoiceService.FindSupplierInvoiceNumberByMonth(getUserData().getUserBranch().getId(), null));
        setTotalNetSuppliers(supplierAccountService.getAccountForAllOrganizationSiteNew(getUserData().getUserBranch().getId(), null));

    }

    public void fillCustomers() {
        setCustomerNum(organizationSiteService.FindSupplier(getUserData().getUserBranch().getId(), 0));
        setInvoiceNumcustomerByDate(invPurchaseInvoiceService.FindSupplierInvoiceNumberByDate(getUserData().getUserBranch().getId(), 0));
        setInvoiceNumsupplierByMonth(invPurchaseInvoiceService.FindSupplierInvoiceNumberByMonth(getUserData().getUserBranch().getId(), 0));
        setTotalNetCustomers(supplierAccountService.getAccountForAllOrganizationSiteNew(getUserData().getUserBranch().getId(), 0));
    }

    public void fillventory() {
        setItemNumbers(invStripService.FindItemsNumber(getUserData().getUserBranch().getId()));

    }

    public void fillElGem() {
        setTotalNetGmByDate(gmSubscriptionsService.FindTotalvalueByDateGM(getGlBanksList(), getUserData().getUserBranch().getId()));
        setTotalNetGmByMonth(gmSubscriptionsService.FindTotalvalueByMonthGM(getGlBanksList(), getUserData().getUserBranch().getId()));
        setInvNumGmByMonth(gmSubscriptionsService.FindInvoiceNumByMounthGM(getGlBanksList(), getUserData().getUserBranch().getId()));
        setInvNumGmByDate(gmSubscriptionsService.FindInvoiceNumByDateGM(getGlBanksList(), getUserData().getUserBranch().getId()));
        for (GlBank glBank : getGlBanksList()) {
            glBank.setOpeninngBalance(bankBalanceViewService.findBalanceForBankId(glBank.getId()));
        }

    }

    public void fillInventory() {
        for (GlBank glBank : getGlBanksList()) {
            glBank.setOpeninngBalance(bankBalanceViewService.findBalanceForBankId(glBank.getId()));
        }
    }

    public void fillCach() {
        setCashByDateList(glBankService.getAllGlBankDTOByBranchId(getUserData().getUserBranch().getId()));
        for (GlBankDTO glBankDTO : cashByDateList) {
            glBankDTO.setValue(bankBalanceViewService.findCashBalanceByDate(glBankDTO.getId()));
        }
        setCashByMonthList(glBankService.getAllGlBankDTOByBranchId(getUserData().getUserBranch().getId()));
        for (GlBankDTO glBankDTO : cashByMonthList) {
            glBankDTO.setValue(bankBalanceViewService.findCashBalanceByMonth(glBankDTO.getId()));
        }
        setCashByDateNumberList(glBankService.getAllGlBankDTOByBranchId(getUserData().getUserBranch().getId()));
        for (GlBankDTO glBankDTO : cashByDateNumberList) {
            glBankDTO.setNumber(bankBalanceViewService.FindInvoiceNumCashSettTranByTypeByDate(getGlBanksList(), getUserData().getUserBranch().getId(), 1));

        }
        setCashByMonthNumberList(glBankService.getAllGlBankDTOByBranchId(getUserData().getUserBranch().getId()));
        for (GlBankDTO glBankDTO : cashByMonthNumberList) {
            glBankDTO.setNumber(bankBalanceViewService.FindInvoiceNumCashSettTranByTypeByMonth(getGlBanksList(), getUserData().getUserBranch().getId(), 1));

        }

    }

    public void fillSettlement() {
        setSettlementByDateList(glBankService.getAllGlBankDTOByBranchId(getUserData().getUserBranch().getId()));
        for (GlBankDTO glBankDTO : settlementByDateList) {
            glBankDTO.setValue(bankBalanceViewService.findSettelmentBalanceByDate(glBankDTO.getId()));
        }
        setSettlementByMonthList(glBankService.getAllGlBankDTOByBranchId(getUserData().getUserBranch().getId()));
        for (GlBankDTO glBankDTO : settlementByMonthList) {
            glBankDTO.setValue(bankBalanceViewService.findSettelmentBalanceByMonth(glBankDTO.getId()));
        }
        setSettlementByDateNumberList(glBankService.getAllGlBankDTOByBranchId(getUserData().getUserBranch().getId()));
        for (GlBankDTO glBankDTO : settlementByDateNumberList) {
            glBankDTO.setNumber(bankBalanceViewService.FindInvoiceNumCashSettTranByTypeByDate(getGlBanksList(), getUserData().getUserBranch().getId(), 0));
        }
        setSettlementByMonthNumberList(glBankService.getAllGlBankDTOByBranchId(getUserData().getUserBranch().getId()));
        for (GlBankDTO glBankDTO : settlementByMonthNumberList) {
            glBankDTO.setNumber(bankBalanceViewService.FindInvoiceNumCashSettTranByTypeByMonth(getGlBanksList(), getUserData().getUserBranch().getId(), 0));
        }

    }

    public void fillTransfer() {
        setTransferByDateList(glBankService.getAllGlBankDTOByBranchId(getUserData().getUserBranch().getId()));
        for (GlBankDTO glBankDTO : transferByDateList) {
            glBankDTO.setValue(bankBalanceViewService.findTransferBalanceByDate(glBankDTO.getId()));
        }
        setTransferByMonthList(glBankService.getAllGlBankDTOByBranchId(getUserData().getUserBranch().getId()));
        for (GlBankDTO glBankDTO : transferByMonthList) {
            glBankDTO.setValue(bankBalanceViewService.findTransferBalanceByMonth(glBankDTO.getId()));
        }
        setTransferByDateNumberList(glBankService.getAllGlBankDTOByBranchId(getUserData().getUserBranch().getId()));
        for (GlBankDTO glBankDTO : transferByDateNumberList) {
            glBankDTO.setNumber(bankBalanceViewService.FindInvoiceNumCashSettTranByTypeByDate(getGlBanksList(), getUserData().getUserBranch().getId(), 2));
        }
        setTransferByMonthNumberList(glBankService.getAllGlBankDTOByBranchId(getUserData().getUserBranch().getId()));
        for (GlBankDTO glBankDTO : transferByMonthNumberList) {
            glBankDTO.setNumber(bankBalanceViewService.FindInvoiceNumCashSettTranByTypeByMonth(getGlBanksList(), getUserData().getUserBranch().getId(), 2));
        }

    }

    /**
     * @return the TotalNetSalesByDay
     */
    public BigDecimal getTotalNetSalesByDay() {
        return TotalNetSalesByDay;
    }

    /**
     * @param TotalNetSalesByDay the TotalNetSalesByDay to set
     */
    public void setTotalNetSalesByDay(BigDecimal TotalNetSalesByDay) {
        this.TotalNetSalesByDay = TotalNetSalesByDay;
    }

    /**
     * @return the inventoryList
     */
    @Override
    public List<InvInventory> getInventoryList() {
        if (inventoryList == null) {
            setInventoryList(isagUserInventoryService.getAllInventroisByUserAndBranchPer(getUserData().getUser().getId(), getUserlogin().getBranchId().getId()));
        }

        return inventoryList;
    }

    /**
     * @param inventoryList the inventoryList to set
     */
    public void setInventoryList(List<InvInventory> inventoryList) {
        this.inventoryList = inventoryList;
    }

    /**
     * @return the TotalNetSalesByMonth
     */
    public BigDecimal getTotalNetSalesByMonth() {

        return TotalNetSalesByMonth;
    }

    /**
     * @param TotalNetSalesByMonth the TotalNetSalesByMonth to set
     */
    public void setTotalNetSalesByMonth(BigDecimal TotalNetSalesByMonth) {
        this.TotalNetSalesByMonth = TotalNetSalesByMonth;
    }

    /**
     * @return the TotalNetReturbSalesByDay
     */
    public BigDecimal getTotalNetReturbSalesByDay() {

        return TotalNetReturbSalesByDay;
    }

    /**
     * @param TotalNetReturbSalesByDay the TotalNetReturbSalesByDay to set
     */
    public void setTotalNetReturbSalesByDay(BigDecimal TotalNetReturbSalesByDay) {
        this.TotalNetReturbSalesByDay = TotalNetReturbSalesByDay;
    }

    /**
     * @return the TotalNetReturbSalesByMonth
     */
    public BigDecimal getTotalNetReturbSalesByMonth() {

        return TotalNetReturbSalesByMonth;
    }

    /**
     * @param TotalNetReturbSalesByMonth the TotalNetReturbSalesByMonth to set
     */
    public void setTotalNetReturbSalesByMonth(BigDecimal TotalNetReturbSalesByMonth) {
        this.TotalNetReturbSalesByMonth = TotalNetReturbSalesByMonth;
    }

    /**
     * @return the TotalNetPurchaseByDate
     */
    public BigDecimal getTotalNetPurchaseByDate() {

        return TotalNetPurchaseByDate;
    }

    /**
     * @param TotalNetPurchaseByDate the TotalNetPurchaseByDate to set
     */
    public void setTotalNetPurchaseByDate(BigDecimal TotalNetPurchaseByDate) {
        this.TotalNetPurchaseByDate = TotalNetPurchaseByDate;
    }

    /**
     * @return the TotalNetPurchaseByMonth
     */
    public BigDecimal getTotalNetPurchaseByMonth() {

        return TotalNetPurchaseByMonth;
    }

    /**
     * @param TotalNetPurchaseByMonth the TotalNetPurchaseByMonth to set
     */
    public void setTotalNetPurchaseByMonth(BigDecimal TotalNetPurchaseByMonth) {
        this.TotalNetPurchaseByMonth = TotalNetPurchaseByMonth;
    }

    /**
     * @return the TotalNetReturnPurchaseByDate
     */
    public BigDecimal getTotalNetReturnPurchaseByDate() {

        return TotalNetReturnPurchaseByDate;
    }

    /**
     * @param TotalNetReturnPurchaseByDate the TotalNetReturnPurchaseByDate to
     * set
     */
    public void setTotalNetReturnPurchaseByDate(BigDecimal TotalNetReturnPurchaseByDate) {
        this.TotalNetReturnPurchaseByDate = TotalNetReturnPurchaseByDate;
    }

    /**
     * @return the TotalNetReturnPurchaseByMonth
     */
    public BigDecimal getTotalNetReturnPurchaseByMonth() {

        return TotalNetReturnPurchaseByMonth;
    }

    /**
     * @param TotalNetReturnPurchaseByMonth the TotalNetReturnPurchaseByMonth to
     * set
     */
    public void setTotalNetReturnPurchaseByMonth(BigDecimal TotalNetReturnPurchaseByMonth) {
        this.TotalNetReturnPurchaseByMonth = TotalNetReturnPurchaseByMonth;
    }

    /**
     * @return the NetSalesNumberByDay
     */
    public Integer getNetSalesNumberByDay() {
        return NetSalesNumberByDay;
    }

    /**
     * @param NetSalesNumberByDay the NetSalesNumberByDay to set
     */
    public void setNetSalesNumberByDay(Integer NetSalesNumberByDay) {
        this.NetSalesNumberByDay = NetSalesNumberByDay;
    }

    /**
     * @return the NetSalesNumberByMonth
     */
    public Integer getNetSalesNumberByMonth() {

        return NetSalesNumberByMonth;
    }

    /**
     * @param NetSalesNumberByMonth the NetSalesNumberByMonth to set
     */
    public void setNetSalesNumberByMonth(Integer NetSalesNumberByMonth) {
        this.NetSalesNumberByMonth = NetSalesNumberByMonth;
    }

    /**
     * @return the NetReturbSalesNumberByDay
     */
    public Integer getNetReturbSalesNumberByDay() {

        return NetReturbSalesNumberByDay;
    }

    /**
     * @param NetReturbSalesNumberByDay the NetReturbSalesNumberByDay to set
     */
    public void setNetReturbSalesNumberByDay(Integer NetReturbSalesNumberByDay) {
        this.NetReturbSalesNumberByDay = NetReturbSalesNumberByDay;
    }

    /**
     * @return the NetReturbSalesNumberByMonth
     */
    public Integer getNetReturbSalesNumberByMonth() {

        return NetReturbSalesNumberByMonth;
    }

    /**
     * @param NetReturbSalesNumberByMonth the NetReturbSalesNumberByMonth to set
     */
    public void setNetReturbSalesNumberByMonth(Integer NetReturbSalesNumberByMonth) {
        this.NetReturbSalesNumberByMonth = NetReturbSalesNumberByMonth;
    }

    /**
     * @return the NetPurchaseNumberByDate
     */
    public Integer getNetPurchaseNumberByDate() {

        return NetPurchaseNumberByDate;
    }

    /**
     * @param NetPurchaseNumberByDate the NetPurchaseNumberByDate to set
     */
    public void setNetPurchaseNumberByDate(Integer NetPurchaseNumberByDate) {
        this.NetPurchaseNumberByDate = NetPurchaseNumberByDate;
    }

    /**
     * @return the NetPurchaseNumberByMonth
     */
    public Integer getNetPurchaseNumberByMonth() {

        return NetPurchaseNumberByMonth;
    }

    /**
     * @param NetPurchaseNumberByMonth the NetPurchaseNumberByMonth to set
     */
    public void setNetPurchaseNumberByMonth(Integer NetPurchaseNumberByMonth) {
        this.NetPurchaseNumberByMonth = NetPurchaseNumberByMonth;
    }

    /**
     * @return the NetReturnPurchaseNumberByDate
     */
    public Integer getNetReturnPurchaseNumberByDate() {

        return NetReturnPurchaseNumberByDate;
    }

    /**
     * @param NetReturnPurchaseNumberByDate the NetReturnPurchaseNumberByDate to
     * set
     */
    public void setNetReturnPurchaseNumberByDate(Integer NetReturnPurchaseNumberByDate) {
        this.NetReturnPurchaseNumberByDate = NetReturnPurchaseNumberByDate;
    }

    /**
     * @return the NetReturnPurchaseNumberByMonth
     */
    public Integer getNetReturnPurchaseNumberByMonth() {

        return NetReturnPurchaseNumberByMonth;
    }

    /**
     * @param NetReturnPurchaseNumberByMonth the NetReturnPurchaseNumberByMonth
     * to set
     */
    public void setNetReturnPurchaseNumberByMonth(Integer NetReturnPurchaseNumberByMonth) {
        this.NetReturnPurchaseNumberByMonth = NetReturnPurchaseNumberByMonth;
    }

    /**
     * @return the TotalNetGmByDate
     */
    public BigDecimal getTotalNetGmByDate() {

        return TotalNetGmByDate;
    }

    /**
     * @param TotalNetGmByDate the TotalNetGmByDate to set
     */
    public void setTotalNetGmByDate(BigDecimal TotalNetGmByDate) {
        this.TotalNetGmByDate = TotalNetGmByDate;
    }

    /**
     * @return the TotalNetGmByMonth
     */
    public BigDecimal getTotalNetGmByMonth() {

        return TotalNetGmByMonth;
    }

    /**
     * @param TotalNetGmByMonth the TotalNetGmByMonth to set
     */
    public void setTotalNetGmByMonth(BigDecimal TotalNetGmByMonth) {
        this.TotalNetGmByMonth = TotalNetGmByMonth;
    }

    /**
     * @return the glBanksList
     */
    public List<GlBank> getGlBanksList() {
        if (glBanksList == null) {
            glBanksList = glBankService.getAllGlBankByBranchId(getUserData().getUserBranch().getId());
        }

        return glBanksList;
    }

    /**
     * @param glBanksList the glBanksList to set
     */
    public void setGlBanksList(List<GlBank> glBanksList) {
        this.glBanksList = glBanksList;
    }

    /**
     * @return the invNumGmByDate
     */
    public Integer getInvNumGmByDate() {

        return invNumGmByDate;
    }

    /**
     * @param invNumGmByDate the invNumGmByDate to set
     */
    public void setInvNumGmByDate(Integer invNumGmByDate) {
        this.invNumGmByDate = invNumGmByDate;
    }

    /**
     * @return the invNumGmByMonth
     */
    public Integer getInvNumGmByMonth() {

        return invNumGmByMonth;
    }

    /**
     * @param invNumGmByMonth the invNumGmByMonth to set
     */
    public void setInvNumGmByMonth(Integer invNumGmByMonth) {
        this.invNumGmByMonth = invNumGmByMonth;
    }

    /**
     * @return the cashByDateList
     */
    public List<GlBankDTO> getCashByDateList() {
        if (cashByDateList == null) {
            cashByDateList = new ArrayList<>();
        }
        return cashByDateList;
    }

    /**
     * @param cashByDateList the cashByDateList to set
     */
    public void setCashByDateList(List<GlBankDTO> cashByDateList) {
        this.cashByDateList = cashByDateList;
    }

    /**
     * @return the cashByMonthList
     */
    public List<GlBankDTO> getCashByMonthList() {
        if (cashByMonthList == null) {
            cashByMonthList = new ArrayList<>();
        }

        return cashByMonthList;
    }

    /**
     * @param cashByMonthList the cashByMonthList to set
     */
    public void setCashByMonthList(List<GlBankDTO> cashByMonthList) {
        this.cashByMonthList = cashByMonthList;
    }

    /**
     * @return the cashByDateNumberList
     */
    public List<GlBankDTO> getCashByDateNumberList() {
        if (cashByDateNumberList == null) {
            cashByDateNumberList = new ArrayList<>();
        }

        return cashByDateNumberList;
    }

    /**
     * @param cashByDateNumberList the cashByDateNumberList to set
     */
    public void setCashByDateNumberList(List<GlBankDTO> cashByDateNumberList) {
        this.cashByDateNumberList = cashByDateNumberList;
    }

    /**
     * @return the cashByMonthNumberList
     */
    public List<GlBankDTO> getCashByMonthNumberList() {
        if (cashByMonthNumberList == null) {
            cashByMonthNumberList = new ArrayList<>();
        }

        return cashByMonthNumberList;
    }

    /**
     * @param cashByMonthNumberList the cashByMonthNumberList to set
     */
    public void setCashByMonthNumberList(List<GlBankDTO> cashByMonthNumberList) {
        this.cashByMonthNumberList = cashByMonthNumberList;
    }

    /**
     * @return the settlementByDateList
     */
    public List<GlBankDTO> getSettlementByDateList() {
        if (settlementByDateList == null) {
            settlementByDateList = new ArrayList<>();
        }

        return settlementByDateList;
    }

    /**
     * @param settlementByDateList the settlementByDateList to set
     */
    public void setSettlementByDateList(List<GlBankDTO> settlementByDateList) {
        this.settlementByDateList = settlementByDateList;
    }

    /**
     * @return the settlementByMonthList
     */
    public List<GlBankDTO> getSettlementByMonthList() {
        if (settlementByMonthList == null) {
            settlementByMonthList = new ArrayList<>();
        }

        return settlementByMonthList;
    }

    /**
     * @param settlementByMonthList the settlementByMonthList to set
     */
    public void setSettlementByMonthList(List<GlBankDTO> settlementByMonthList) {
        this.settlementByMonthList = settlementByMonthList;
    }

    /**
     * @return the settlementByDateNumberList
     */
    public List<GlBankDTO> getSettlementByDateNumberList() {
        if (settlementByDateNumberList == null) {
            settlementByDateNumberList = new ArrayList<>();
        }

        return settlementByDateNumberList;
    }

    /**
     * @param settlementByDateNumberList the settlementByDateNumberList to set
     */
    public void setSettlementByDateNumberList(List<GlBankDTO> settlementByDateNumberList) {
        this.settlementByDateNumberList = settlementByDateNumberList;
    }

    /**
     * @return the settlementByMonthNumberList
     */
    public List<GlBankDTO> getSettlementByMonthNumberList() {
        if (settlementByMonthNumberList == null) {
            settlementByMonthNumberList = new ArrayList<>();
        }

        return settlementByMonthNumberList;
    }

    /**
     * @param settlementByMonthNumberList the settlementByMonthNumberList to set
     */
    public void setSettlementByMonthNumberList(List<GlBankDTO> settlementByMonthNumberList) {
        this.settlementByMonthNumberList = settlementByMonthNumberList;
    }

    /**
     * @return the transferByDateList
     */
    public List<GlBankDTO> getTransferByDateList() {
        if (transferByDateList == null) {
            transferByDateList = new ArrayList<>();
        }

        return transferByDateList;
    }

    /**
     * @param transferByDateList the transferByDateList to set
     */
    public void setTransferByDateList(List<GlBankDTO> transferByDateList) {
        this.transferByDateList = transferByDateList;
    }

    /**
     * @return the transferByMonthList
     */
    public List<GlBankDTO> getTransferByMonthList() {
        if (transferByMonthList == null) {
            transferByMonthList = new ArrayList<>();
        }
        return transferByMonthList;
    }

    /**
     * @param transferByMonthList the transferByMonthList to set
     */
    public void setTransferByMonthList(List<GlBankDTO> transferByMonthList) {
        this.transferByMonthList = transferByMonthList;
    }

    /**
     * @return the transferByDateNumberList
     */
    public List<GlBankDTO> getTransferByDateNumberList() {
        if (transferByDateNumberList == null) {
            transferByDateNumberList = new ArrayList<>();
        }

        return transferByDateNumberList;
    }

    /**
     * @param transferByDateNumberList the transferByDateNumberList to set
     */
    public void setTransferByDateNumberList(List<GlBankDTO> transferByDateNumberList) {
        this.transferByDateNumberList = transferByDateNumberList;
    }

    /**
     * @return the transferByMonthNumberList
     */
    public List<GlBankDTO> getTransferByMonthNumberList() {
        if (transferByMonthNumberList == null) {
            transferByMonthNumberList = new ArrayList<>();
        }

        return transferByMonthNumberList;
    }

    /**
     * @param transferByMonthNumberList the transferByMonthNumberList to set
     */
    public void setTransferByMonthNumberList(List<GlBankDTO> transferByMonthNumberList) {
        this.transferByMonthNumberList = transferByMonthNumberList;
    }

    /**
     * @return the supplierNum
     */
    public Integer getSupplierNum() {

        return supplierNum;
    }

    /**
     * @param supplierNum the supplierNum to set
     */
    public void setSupplierNum(Integer supplierNum) {
        this.supplierNum = supplierNum;
    }

    /**
     * @return the invoiceNumsupplierByDate
     */
    public Integer getInvoiceNumsupplierByDate() {

        return invoiceNumsupplierByDate;
    }

    /**
     * @param invoiceNumsupplierByDate the invoiceNumsupplierByDate to set
     */
    public void setInvoiceNumsupplierByDate(Integer invoiceNumsupplierByDate) {
        this.invoiceNumsupplierByDate = invoiceNumsupplierByDate;
    }

    /**
     * @return the invoiceNumsupplierByMonth
     */
    public Integer getInvoiceNumsupplierByMonth() {

        return invoiceNumsupplierByMonth;
    }

    /**
     * @param invoiceNumsupplierByMonth the invoiceNumsupplierByMonth to set
     */
    public void setInvoiceNumsupplierByMonth(Integer invoiceNumsupplierByMonth) {
        this.invoiceNumsupplierByMonth = invoiceNumsupplierByMonth;
    }

    /**
     * @return the totalNetSuppliers
     */
    public BigDecimal getTotalNetSuppliers() {

        return totalNetSuppliers;
    }

    /**
     * @param totalNetSuppliers the totalNetSuppliers to set
     */
    public void setTotalNetSuppliers(BigDecimal totalNetSuppliers) {
        this.totalNetSuppliers = totalNetSuppliers;
    }

    /**
     * @return the customerNum
     */
    public Integer getCustomerNum() {

        return customerNum;
    }

    /**
     * @param customerNum the customerNum to set
     */
    public void setCustomerNum(Integer customerNum) {
        this.customerNum = customerNum;
    }

    /**
     * @return the invoiceNumcustomerByDate
     */
    public Integer getInvoiceNumcustomerByDate() {

        return invoiceNumcustomerByDate;
    }

    /**
     * @param invoiceNumcustomerByDate the invoiceNumcustomerByDate to set
     */
    public void setInvoiceNumcustomerByDate(Integer invoiceNumcustomerByDate) {
        this.invoiceNumcustomerByDate = invoiceNumcustomerByDate;
    }

    /**
     * @return the invoiceNumcustomerByMonth
     */
    public Integer getInvoiceNumcustomerByMonth() {

        return invoiceNumcustomerByMonth;
    }

    /**
     * @param invoiceNumcustomerByMonth the invoiceNumcustomerByMonth to set
     */
    public void setInvoiceNumcustomerByMonth(Integer invoiceNumcustomerByMonth) {
        this.invoiceNumcustomerByMonth = invoiceNumcustomerByMonth;
    }

    /**
     * @return the totalNetCustomers
     */
    public BigDecimal getTotalNetCustomers() {

        return totalNetCustomers;
    }

    /**
     * @param totalNetCustomers the totalNetCustomers to set
     */
    public void setTotalNetCustomers(BigDecimal totalNetCustomers) {
        this.totalNetCustomers = totalNetCustomers;
    }

    /**
     * @return the itemNumbers
     */
    public Integer getItemNumbers() {
        return itemNumbers;
    }

    /**
     * @param itemNumbers the itemNumbers to set
     */
    public void setItemNumbers(Integer itemNumbers) {
        this.itemNumbers = itemNumbers;
    }
}
