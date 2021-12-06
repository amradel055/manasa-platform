/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.invPurchaseInvoiceDTO;

import com.toby.businessservice.InvPurchaseInvoiceService_1;
import com.toby.dto.InvInvoiceDTO;
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

    

    @EJB
    private InvPurchaseInvoiceService_1 invPurchaseInvoiceService;

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
                    Integer type = 0;
                    Boolean isGallary = true;
                    if (getUri().contains("invpurchaseinvoicelist")) {
                        type = 0;
                        isGallary = false;
                    } else if (getUri().contains("invsalesinvoicelist")) {
                        type = 1;
                    } else if (getUri().contains("invreturnpurchaselist")) {
                        type = 2;
                        isGallary = false;
                    } else if (getUri().contains("invreturnsaleslist")) {
                        type = 3;
                    } else if (getUri().contains("proworkorderlist")) {
                        type = 4;
                    } else if (getUri().contains("invpurchaseaddinglist")) {
                        type = 5;
                    }
                    Integer invInventorySelectionId = getInventoryDTOSelect() != null ? getInventoryDTOSelect().getId() : null;
                    Integer invGallarySelectionId = getGallaryDTOSelect() != null ? getGallaryDTOSelect().getId() : null;
                    if (isGallary) {
                        setRowCount(invPurchaseInvoiceService.getTotalRegistorsForGallary(type, invGallarySelectionId, getUserData().getUserBranch().getId(), filters, isGallary).intValue());
                        return invPurchaseInvoiceService.getALLInvPurchaseInvoiceDTOByTypeAndBranchIdFroGallary(type, invGallarySelectionId, first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), getUserData().getUserBranch().getId(), filters, isGallary);
                    } else {
                        setRowCount(invPurchaseInvoiceService.getTotalRegistorsForGallary(type, invInventorySelectionId, getUserData().getUserBranch().getId(), filters, isGallary).intValue());
                        return invPurchaseInvoiceService.getALLInvPurchaseInvoiceDTOByTypeAndBranchIdFroGallary(type, invInventorySelectionId, first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), getUserData().getUserBranch().getId(), filters, isGallary);
                    }
                }
            });
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "load");
        }
    }

    public void search() {
        Integer type = 0, invInventorySelectionId = null, invGallarySelectionId = null;
        invGallarySelectionId = getGallaryDTOSelect() != null ? getGallaryDTOSelect().getId() : null;

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
        } else if (getUri().contains("invpurchaseaddinglist")) {
            type = 5;
        }
        setInvInvoiceList(invPurchaseInvoiceService.getALLInvPurchaseInvoiceDTOByTypeAndBranchIdFroGallary2(type, invGallarySelectionId, getInvInvoiceDTO().getOrganizationSiteId() == null ? null : getInvInvoiceDTO().getOrganizationSiteId().getId()));
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
            if (getGallaryDTOSelect() != null && getGallaryDTOSelect().getId() != null && getGallaryDTOSelect().getId() > 0) {
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
            } else if (getUri().contains("invpurchaseadding")) {
                exit("../invpurchaseadding/invpurchaseaddingform.xhtml");
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
                } else if (getUri().contains("invpurchaseadding")) {
                    exit("../invpurchaseadding/invpurchaseaddingform.xhtml");
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
                String msg = invPurchaseInvoiceService.deleteInvPurchaseInvoiceDTO(getInvInvoiceDTOSelected(), 1);
                if (msg == null) {
                    load();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserDDs().get("INFO"), getUserDDs().get("DELETED")));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserDDs().get("ERROR"), msg));
                }
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

}
