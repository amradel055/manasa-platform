/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.isag.dashboard;

import com.isag.businessservice.GlBankTransactionService;
import com.isag.businessservice.GmSubscriptionsService;
import com.isag.businessservice.InvPurchaseInvoiceDetailsService;
import com.isag.businessservice.InvPurchaseInvoiceService_1;
import com.isag.businessservice.IsagUserInventoryService;
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
    private BigDecimal TotalNet;
    private List<InvInventory> inventoryList;

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
     * @return the TotalNet
     */
    public BigDecimal getTotalNet() {
        if (TotalNet == null) {
            setTotalNet(invPurchaseInvoiceDetailsService.FindTotalvalueByDate(getInventoryList(), getUserData().getUserBranch().getId()));
        }
        return TotalNet;
    }

    /**
     * @param TotalNet the TotalNet to set
     */
    public void setTotalNet(BigDecimal TotalNet) {
        this.TotalNet = TotalNet;
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
}
