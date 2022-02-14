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
import com.isag.businessservice.InvPurchaseInvoiceService_1;
import com.isag.businessservice.IsagUserInventoryService;
import com.isag.businessservice.reports.BankBalanceViewService;
import com.isag.entity.GlBank;
import com.isag.entity.InvInventory;
import com.isag.isag.BaseListBean;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

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
    private BigDecimal totalCashByDate;
    private BigDecimal totalCashByMonth;
    private BigDecimal totalSettlementByDate;
    private BigDecimal totalSettlementByMonth;
    private BigDecimal totalTransferByDate;
    private BigDecimal totalTransferByMonth;

    private List<InvInventory> inventoryList;
    private List<GlBank> glBanksList;

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

    /**
     * @return the TotalNetSalesByDay
     */
    public BigDecimal getTotalNetSalesByDay() {
        if (TotalNetSalesByDay == null) {
            setTotalNetSalesByDay(invPurchaseInvoiceDetailsService.FindTotalvalueByDate(getInventoryList(), getUserData().getUserBranch().getId(), 1));
        }
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
        if (TotalNetSalesByMonth == null) {
            setTotalNetSalesByMonth(invPurchaseInvoiceDetailsService.FindTotalvalueByMonth(getInventoryList(), getUserData().getUserBranch().getId(), 1));
        }

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
        if (TotalNetReturbSalesByDay == null) {
            setTotalNetReturbSalesByDay(invPurchaseInvoiceDetailsService.FindTotalvalueByDate(getInventoryList(), getUserData().getUserBranch().getId(), 3));
        }
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
        if (TotalNetReturbSalesByMonth == null) {
            setTotalNetReturbSalesByMonth(invPurchaseInvoiceDetailsService.FindTotalvalueByMonth(getInventoryList(), getUserData().getUserBranch().getId(), 3));
        }
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
        if (TotalNetPurchaseByDate == null) {
            setTotalNetPurchaseByDate(invPurchaseInvoiceDetailsService.FindTotalvalueByDate(getInventoryList(), getUserData().getUserBranch().getId(), 0));
        }
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
        if (TotalNetPurchaseByMonth == null) {
            setTotalNetPurchaseByMonth(invPurchaseInvoiceDetailsService.FindTotalvalueByMonth(getInventoryList(), getUserData().getUserBranch().getId(), 0));
        }
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
        if (TotalNetReturnPurchaseByDate == null) {
            setTotalNetReturnPurchaseByDate(invPurchaseInvoiceDetailsService.FindTotalvalueByDate(getInventoryList(), getUserData().getUserBranch().getId(), 2));
        }
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
        if (TotalNetReturnPurchaseByMonth == null) {
            setTotalNetReturnPurchaseByMonth(invPurchaseInvoiceDetailsService.FindTotalvalueByMonth(getInventoryList(), getUserData().getUserBranch().getId(), 2));
        }
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
        if (NetSalesNumberByDay == null) {
            setNetSalesNumberByDay(invPurchaseInvoiceDetailsService.FindInvoiceNumberByDate(getInventoryList(), getUserData().getUserBranch().getId(), 1));
        }
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
        if (NetSalesNumberByMonth == null) {
            setNetSalesNumberByMonth(invPurchaseInvoiceDetailsService.FindInvoiceNumberByMonth(getInventoryList(), getUserData().getUserBranch().getId(), 1));
        }
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
        if (NetReturbSalesNumberByDay == null) {
            setNetReturbSalesNumberByDay(invPurchaseInvoiceDetailsService.FindInvoiceNumberByDate(getInventoryList(), getUserData().getUserBranch().getId(), 3));
        }
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
        if (NetReturbSalesNumberByMonth == null) {
            setNetReturbSalesNumberByMonth(invPurchaseInvoiceDetailsService.FindInvoiceNumberByMonth(getInventoryList(), getUserData().getUserBranch().getId(), 3));
        }
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
        if (NetPurchaseNumberByDate == null) {
            setNetPurchaseNumberByDate(invPurchaseInvoiceDetailsService.FindInvoiceNumberByDate(getInventoryList(), getUserData().getUserBranch().getId(), 0));
        }
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
        if (NetPurchaseNumberByMonth == null) {
            setNetPurchaseNumberByMonth(invPurchaseInvoiceDetailsService.FindInvoiceNumberByMonth(getInventoryList(), getUserData().getUserBranch().getId(), 0));
        }
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
        if (NetReturnPurchaseNumberByDate == null) {
            setNetReturnPurchaseNumberByDate(invPurchaseInvoiceDetailsService.FindInvoiceNumberByDate(getInventoryList(), getUserData().getUserBranch().getId(), 3));
        }
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
        if (NetReturnPurchaseNumberByMonth == null) {
            setNetReturnPurchaseNumberByMonth(invPurchaseInvoiceDetailsService.FindInvoiceNumberByMonth(getInventoryList(), getUserData().getUserBranch().getId(), 3));
        }
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
        if (TotalNetGmByDate == null) {
            setTotalNetGmByDate(gmSubscriptionsService.FindTotalvalueByDateGM(getGlBanksList(), getUserData().getUserBranch().getId()));
        }
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
        if (TotalNetGmByMonth == null) {
            setTotalNetGmByMonth(gmSubscriptionsService.FindTotalvalueByMonthGM(getGlBanksList(), getUserData().getUserBranch().getId()));
        }
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
            for (GlBank glBank : glBanksList) {
                glBank.setOpeninngBalance(bankBalanceViewService.findBalanceForBankId(glBank.getId()));
            }
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
        if (invNumGmByDate == null) {
            setInvNumGmByDate(gmSubscriptionsService.FindInvoiceNumByDateGM(getGlBanksList(), getUserData().getUserBranch().getId()));
        }
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
        if (invNumGmByMonth == null) {
            setInvNumGmByMonth(gmSubscriptionsService.FindInvoiceNumByMounthGM(getGlBanksList(), getUserData().getUserBranch().getId()));
        }
        return invNumGmByMonth;
    }

    /**
     * @param invNumGmByMonth the invNumGmByMonth to set
     */
    public void setInvNumGmByMonth(Integer invNumGmByMonth) {
        this.invNumGmByMonth = invNumGmByMonth;
    }

    /**
     * @return the totalCashByDate
     */
    public BigDecimal getTotalCashByDate() {
      
            for (GlBank glBank : getGlBanksList()) {
                if (totalCashByDate == null) {
                    setTotalCashByDate(bankBalanceViewService.findCashBalanceByDate(glBank.getId()));
                } else {
                    setTotalCashByDate(totalCashByDate.add(bankBalanceViewService.findCashBalanceByDate(glBank.getId())));

                }
            }
        
        return totalCashByDate;
    }

    /**
     * @param totalCashByDate the totalCashByDate to set
     */
    public void setTotalCashByDate(BigDecimal totalCashByDate) {
        this.totalCashByDate = totalCashByDate;
    }

    /**
     * @return the totalCashByMonth
     */
    public BigDecimal getTotalCashByMonth() {
         for (GlBank glBank : getGlBanksList()) {
                if (totalCashByMonth== null) {
                    setTotalCashByMonth(bankBalanceViewService.findCashBalanceByMonth(glBank.getId()));
                } else {
                    setTotalCashByMonth(totalCashByMonth.add(bankBalanceViewService.findCashBalanceByMonth(glBank.getId())));

                }
            }
        return totalCashByMonth;
    }

    /**
     * @param totalCashByMonth the totalCashByMonth to set
     */
    public void setTotalCashByMonth(BigDecimal totalCashByMonth) {
        this.totalCashByMonth = totalCashByMonth;
    }

    /**
     * @return the totalSettlementByDate
     */
    public BigDecimal getTotalSettlementByDate() {
        return totalSettlementByDate;
    }

    /**
     * @param totalSettlementByDate the totalSettlementByDate to set
     */
    public void setTotalSettlementByDate(BigDecimal totalSettlementByDate) {
        this.totalSettlementByDate = totalSettlementByDate;
    }

    /**
     * @return the totalSettlementByMonth
     */
    public BigDecimal getTotalSettlementByMonth() {
        return totalSettlementByMonth;
    }

    /**
     * @param totalSettlementByMonth the totalSettlementByMonth to set
     */
    public void setTotalSettlementByMonth(BigDecimal totalSettlementByMonth) {
        this.totalSettlementByMonth = totalSettlementByMonth;
    }

    /**
     * @return the totalTransferByDate
     */
    public BigDecimal getTotalTransferByDate() {
        return totalTransferByDate;
    }

    /**
     * @param totalTransferByDate the totalTransferByDate to set
     */
    public void setTotalTransferByDate(BigDecimal totalTransferByDate) {
        this.totalTransferByDate = totalTransferByDate;
    }

    /**
     * @return the totalTransferByMonth
     */
    public BigDecimal getTotalTransferByMonth() {
        return totalTransferByMonth;
    }

    /**
     * @param totalTransferByMonth the totalTransferByMonth to set
     */
    public void setTotalTransferByMonth(BigDecimal totalTransferByMonth) {
        this.totalTransferByMonth = totalTransferByMonth;
    }
}
