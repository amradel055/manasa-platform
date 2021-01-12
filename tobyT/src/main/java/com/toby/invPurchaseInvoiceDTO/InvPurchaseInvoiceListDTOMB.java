/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.invPurchaseInvoiceDTO;

import com.toby.businessservice.InvPurchaseInvoiceService_1;
import com.toby.businessservice.TobyUserInventoryService;
import com.toby.converter.InvInventoryConverter;
import com.toby.converter.InvInventoryDTOConverter;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.entity.InvInventory;
import com.toby.toby.InventoryBasicDataForm;
import com.toby.toby.UserData;
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
    private List<InvInventoryDTO> gallaryList;
    private InvInventory invInventorySelection;
    private InvInventoryDTO invGallarySelection;
    @EJB
    private InvPurchaseInvoiceService_1 invPurchaseInvoiceService;
    @EJB
    private TobyUserInventoryService tobyUserInventoryService;

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
                    Integer type = 0, invInventorySelectionId = null, invGallarySelectionId = null;
                    if (getUri().contains("invpurchaseinvoicelist")) {
                        type = 0;
                    } else if (getUri().contains("invsalesinvoicelist")) {
                        type = 1;
                    } else if (getUri().contains("invreturnpurchaselist")) {
                        type = 2;
                    } else if (getUri().contains("invreturnsaleslist")) {
                        type = 3;
                    } else if (getUri().contains("proworkorderlist")) {
                        type = 4;
                    }
                    invInventorySelectionId = getInvInventorySelection() != null ? getInvInventorySelection().getId() : null;
                    invGallarySelectionId = getInvGallarySelection() != null ? getInvGallarySelection().getId() : null;
                    setRowCount(invPurchaseInvoiceService.getTotalRegistorsForGallary(type, invGallarySelectionId, getUserData().getUserBranch().getId(), filters).intValue());
                    return invPurchaseInvoiceService.getALLInvPurchaseInvoiceDTOByTypeAndBranchIdFroGallary(type, invGallarySelectionId, first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), getUserData().getUserBranch().getId(), filters);
                }
            });
            //----------------------------------------------
            setInventoryList(tobyUserInventoryService.getAllInventroisByUserAndBranchPer(getUserData().getUser().getId(), getUserlogin().getBranchId().getId()));
            setGallaryList(tobyUserInventoryService.getAllGallaryByUserAndBranch(getUserData().getUser()));
            if (getInventoryList() != null && !inventoryList.isEmpty()) {
                setInvInventoryConverter(new InvInventoryConverter(getInventoryList()));
            }
            if (getGallaryList() != null && !getGallaryList().isEmpty()) {
                setGallaryDTOConverter(new InvInventoryDTOConverter(getGallaryList()));
            }
            if (getInvInventorySelection().getId() == null) {
                setInvInventorySelection(getInventoryList() == null || getInventoryList().isEmpty() ? null : getInventoryList().get(0));
            }
            if (getInvGallarySelection().getId() == null) {
                setInvGallarySelection(getGallaryList() == null || getGallaryList().isEmpty() ? null : getGallaryList().get(0));
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
    public void loadGallaryList() {
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
            if (getUri().contains("invpurchaseinvoicelist")) {
                exit("../invpurchaseinvoice_1/invpurchaseinvoiceform.xhtml");
            } else if (getUri().contains("invsalesinvoicelist")) {
                exit("../invsalesinvoice_1/invsalesinvoiceform.xhtml");
            } else if (getUri().contains("invreturnpurchaselist")) {
                exit("../invreturnpurchase_1/invreturnpurchaseform.xhtml");
            } else if (getUri().contains("invreturnsaleslist")) {
                exit("../invreturnsales_1/invreturnsalesform.xhtml");
            } else if (getUri().contains("proworkorderlist")) {
                exit("../proworkorder/proworkorderform.xhtml");
            }

            return "";
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "goToAdd");
            return null;
        }
    }
    
     public void stop() {
         if (getInvInvoiceDTOSelected() != null && getInvInvoiceDTOSelected().getId() > 0) {
             invPurchaseInvoiceService.updateInvPurchaseInvoiceForStopped(getInvInvoiceDTOSelected().getId(), 1);
             savedMeesage(null);
         }         
     }
     public void run() {
         if (getInvInvoiceDTOSelected() != null && getInvInvoiceDTOSelected().getId() > 0) {
             invPurchaseInvoiceService.updateInvPurchaseInvoiceForStopped(getInvInvoiceDTOSelected().getId(), 0);
             savedMeesage(null);
         }
     }

    public String goToEdit() {
        try {
            if (getInvInvoiceDTOSelected() != null && getInvInvoiceDTOSelected().getId() > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("invPurchaseInvoiceIdSeclected", getInvInvoiceDTOSelected().getId());
                context.getSessionMap().put("ScreenMode", "editInvoice");

                if (getUri().contains("invpurchaseinvoicelist")) {
                    exit("../invpurchaseinvoice_1/invpurchaseinvoiceform.xhtml");
                } else if (getUri().contains("invsalesinvoicelist")) {
                    exit("../invsalesinvoice_1/invsalesinvoiceform.xhtml");
                } else if (getUri().contains("invreturnpurchaselist")) {
                    exit("../invreturnpurchase_1/invreturnpurchaseform.xhtml");
                } else if (getUri().contains("invreturnsaleslist")) {
                    exit("../invreturnsales_1/invreturnsalesform.xhtml");
                } else if (getUri().contains("proworkorderlist")) {
                    exit("../proworkorder/proworkorderform.xhtml");
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

    public void delete() {
        try {
            if (getInvInvoiceDTOSelected() != null && getInvInvoiceDTOSelected().getId().toString() != null) {
                invPurchaseInvoiceService.deleteInvPurchaseInvoiceDTO(getInvInvoiceDTOSelected(), 1);
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

    /**
     * @return the invGallarySelection
     */
    public InvInventoryDTO getInvGallarySelection() {
        if (invGallarySelection == null) {
            invGallarySelection = new InvInventoryDTO();
        }
        return invGallarySelection;
    }

    /**
     * @param invGallarySelection the invGallarySelection to set
     */
    public void setInvGallarySelection(InvInventoryDTO invGallarySelection) {
        this.invGallarySelection = invGallarySelection;
    }

    /**
     * @return the gallaryList
     */
    public List<InvInventoryDTO> getGallaryList() {
        return gallaryList;
    }

    /**
     * @param gallaryList the gallaryList to set
     */
    public void setGallaryList(List<InvInventoryDTO> gallaryList) {
        this.gallaryList = gallaryList;
    }
}
