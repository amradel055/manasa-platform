/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.invPurchaseInvoiceDTO;

import com.isag.businessservice.InvPurchaseInvoiceService_1;
import com.isag.businessservice.IsagUserInventoryService;
import com.isag.converter.InvInventoryConverter;
import com.isag.dto.InvInvoiceDTO;
import com.isag.entity.InvInventory;
import com.isag.isag.InventoryBasicDataForm;
import com.isag.isag.UserData;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author WIN7
 */
@Named(value = "invPurchaseInvoiceListDTOMB")
@ViewScoped
public class InvPurchaseInvoiceListDTOMB extends InventoryBasicDataForm {

    private List<InvInventory> inventoryList;
    private InvInventory invInventorySelection;
    @EJB
    private InvPurchaseInvoiceService_1 invPurchaseInvoiceService;
    @EJB
    private IsagUserInventoryService isagUserInventoryService;

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            this.setDataModel(new LazyDataModel<InvInvoiceDTO>() {
                private static final long serialVersionUID = 1L;

                @Override
                public List<InvInvoiceDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                    Integer type = 0, invInventorySelectionId = null;
                    if (getUri().contains("invpurchaseinvoicelist")) {
                        type = 0;
                    } else if (getUri().contains("invsalesinvoicelist") || getUri().contains("invposinvoicelist")) {
                        type = 1;
                        getInvInvoiceDTO().getInvMonthTargetDTO().setType(1);
                    } else if (getUri().contains("invreturnpurchaselist")) {
                        type = 2;
                    } else if (getUri().contains("invreturnsaleslist")) {
                        type = 3;
                    }
                    invInventorySelectionId = getInvInventorySelection() != null ? getInvInventorySelection().getId() : null;
                    /*  if (invInventorySelection != null) { 
                        context.getSessionMap().put("defaultInventory", invInventorySelection);
                    } else { 
                        context.getSessionMap().put("defaultInventory", null);
                    } */
                    setRowCount(invPurchaseInvoiceService.getTotalRegistors(type, invInventorySelectionId, getUserData().getUserBranch().getId(), filters).intValue());
                    return invPurchaseInvoiceService.getALLInvPurchaseInvoiceDTOByTypeAndBranchId(type, invInventorySelectionId, first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), getUserData().getUserBranch().getId(), filters);
                }
            });
            //----------------------------------------------
            setInventoryList(isagUserInventoryService.getAllInventroisByUserAndBranchPer(getUserData().getUser().getId(), getUserlogin().getBranchId().getId()));
            if (getInventoryList() != null && !inventoryList.isEmpty()) {
                setInvInventoryConverter(new InvInventoryConverter(getInventoryList()));
            }
            if (getInvInventorySelection().getId() == null) {
                setInvInventorySelection(getInventoryList() == null || getInventoryList().isEmpty() ? null : getInventoryList().get(0));

            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "load");
        }
    }

    public void loadInventoryList() {
        try {
            if (getInvInventorySelection() != null && getInvInventorySelection().getId() != null && getInvInventorySelection().getId() > 0) {
                load();
            } else {
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "loadInventoryList");
        }

    }

    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "addInvoice");
            context.getSessionMap().put("typeActivity", 0);
            goToScreen();

            return "";
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "goToAdd");
            return null;
        }
    }

    public String goToAddCeramic() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "addInvoice");
            Integer typeActivity = 1 ;
            context.getSessionMap().put("typeActivity", typeActivity);
            goToScreenCeramic();

            return "";
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "goToAdd");
            return null;
        }
    }

    public void goToScreenCeramic() {
        if (getUri().contains("invpurchaseinvoicelist")) {
            exit("../invpurchaseinvoice_1/invpurchaseinvoiceformCeramic.xhtml");
        } else if (getUri().contains("invsalesinvoicelist")) {
            exit("../invsalesinvoice_1/invsalesinvoiceformCeramic.xhtml");
        } else if (getUri().contains("invreturnpurchaselist")) {
            exit("../invreturnpurchase_1/invreturnpurchaseformCeramic.xhtml");
        } else if (getUri().contains("invreturnsaleslist")) {
            exit("../invreturnsales_1/invreturnsalesformCeramic.xhtml");
        } else if (getUri().contains("invposinvoicelist")) {
            exit("../invpos/invposinvoiceform.xhtml");
        }
    }

    public String goToEdit() {
        try {
            if (getInvInvoiceDTOSelected() != null && getInvInvoiceDTOSelected().getId() > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("invPurchaseInvoiceIdSeclected", getInvInvoiceDTOSelected().getId());
                context.getSessionMap().put("ScreenMode", "editInvoice");
                if (getInvInvoiceDTOSelected().getTypeActivity() != null && getInvInvoiceDTOSelected().getTypeActivity() == 1) {
                    goToScreenCeramic();
                } else {
                    goToScreen();
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
        if (getUri().contains("invpurchaseinvoicelist")) {
            exit("../invpurchaseinvoice_1/invpurchaseinvoiceform.xhtml");
        } else if (getUri().contains("/invsalesinvoice_1/invsalesinvoicelist")) {
            exit("../invsalesinvoice_1/invsalesinvoiceform.xhtml");
        } else if (getUri().contains("invreturnpurchaselist")) {
            exit("../invreturnpurchase_1/invreturnpurchaseform.xhtml");
        } else if (getUri().contains("invreturnsaleslist")) {
            exit("../invreturnsales_1/invreturnsalesform.xhtml");
        } else if (getUri().contains("invposinvoicelist")) {
            exit("../invpos/invposinvoiceform.xhtml");
        }
    }

    public void delete() {
        try {
            if (getInvInvoiceDTOSelected() != null && getInvInvoiceDTOSelected().getId() != null) {
                invPurchaseInvoiceService.deleteInvPurchaseInvoiceDTO(getInvInvoiceDTOSelected().getId());
                load();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserDDs().get("INFO"), getUserDDs().get("DELETED")));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserDDs().get("ERROR"), getUserDDs().get("UNIQE_KEY_ERROR")));
            saveError(e, "InvPurchaseInvoiceListMB", "delete");
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String exit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the inventoryList
     */
    public List<InvInventory> getInventoryList() {
        return inventoryList;
    }

    /**
     * @param inventoryList the inventoryList to set
     */
    public void setInventoryList(List<InvInventory> inventoryList) {
        this.inventoryList = inventoryList;
    }

    /**
     * @return the invInventorySelection
     */
    public InvInventory getInvInventorySelection() {
        if (invInventorySelection == null) {
            invInventorySelection = new InvInventory();
        }
        return invInventorySelection;
    }

    /**
     * @param invInventorySelection the invInventorySelection to set
     */
    public void setInvInventorySelection(InvInventory invInventorySelection) {
        this.invInventorySelection = invInventorySelection;
    }
}
