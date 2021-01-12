package com.toby.invtransfer;

import com.toby.businessservice.InvTransferService;
import com.toby.dto.InvInvoiceDTO;
import com.toby.entity.InvTransfer;
import com.toby.entiy.InvTransferEntity;
import com.toby.toby.InventoryBasicDataForm;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 * @author khaled
 */
@Named(value = "invTransferListMB")
@ViewScoped
public class InvTransferListMB extends InventoryBasicDataForm {

    private List<InvTransfer> invTransferList = new ArrayList<>();
    private InvTransfer invTransfer;
    private Integer invTransferSelected;
    private Boolean isPermission;
    private InvTransferEntity invTransferEntity;
    private List<InvTransferEntity> invTransferEntityList = new ArrayList<>();
    

    @EJB
    private InvTransferService invTransferService;

    @Override
    @PostConstruct
    public void init() {
        try {

            invTransfer = new InvTransfer();
            invTransferEntity = new InvTransferEntity();
            invTransferList = new ArrayList<>();
            if (getUri().contains("invtransferInventory")) {
                isPermission = false;
            } else {
                isPermission = true;
            }
            this.load();
        } catch (Exception e) {
            saveError(e, "InvTransferListMB", "init()");
        }
    }

    public void delete() {
        try {

            if (invTransfer != null) {
                Integer invTransferIdToPass = invTransferEntity.getId();
                invTransfer.setId(invTransferEntity.getId());
                try {
                    invTransferService.deleteInvTransfer(invTransfer, invTransferIdToPass);
                    getInvTransferEntityList().remove(invTransferEntity);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserDDs().get("INFO"), getUserDDs().get("DELETED")));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserDDs().get("ERROR"), getUserDDs().get("UNIQE_KEY_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "InvTransferListMB", "delete()");
        }
    }

    public String goToAdd() {
        try {

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");
            if (!isPermission) {
                exit("../invtransferInventory/invTransferInventoryForm.xhtml");
            } else {
                exit("../invtransfer/invTransferForm.xhtml");
            }
            return "";
        } catch (Exception e) {
            saveError(e, "InvTransferListMB", "goToAdd()");
            return "";
        }
    }

    public String goToEdit() {
        try {

            if (invTransferSelected != null && invTransferSelected > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("invTransferSelected", invTransferSelected);
                context.getSessionMap().put("ScreenMode", "Edit");
                if (!isPermission) {
                    exit("../invtransferInventory/invTransferInventoryForm.xhtml");
                } else {
                    exit("../invtransfer/invTransferForm.xhtml");
                }
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "InvTransferListMB", "goToEdit()");
            return "";
        }
    }
    
    @Override
    public void load() {
        try {
            this.setDataModel(new LazyDataModel<InvInvoiceDTO>() {
                private static final long serialVersionUID = 1L;

                @Override
                public List<InvInvoiceDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                    setRowCount(invTransferService.getTotalRegistors(getUserData().getUserBranch().getId(), isPermission ? 1 : 0, filters).intValue());
                    return invTransferService.getALLInvTransferByBranchId(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), getUserData().getUserBranch().getId(), isPermission ? 1 : 0, filters);
                }
            });
        } catch (Exception e) {
            saveError(e, "invQuotationListMB", "load");
        }
    }

    @Override
    public String getScreenName() {
        return null;
    }

    public List<InvTransfer> getInvTransferList() {
        return invTransferList;
    }

    public void setInvTransferList(List<InvTransfer> invTransferList) {
        this.invTransferList = invTransferList;
    }

    public InvTransfer getInvTransfer() {
        return invTransfer;
    }

    public void setInvTransfer(InvTransfer invTransfer) {
        this.invTransfer = invTransfer;
    }

    public Integer getInvTransferSelected() {
        return invTransferSelected;
    }

    public void setInvTransferSelected(Integer invTransferSelected) {
        this.invTransferSelected = invTransferSelected;
    }

    public InvTransferEntity getInvTransferEntity() {
        return invTransferEntity;
    }

    public void setInvTransferEntity(InvTransferEntity invTransferEntity) {
        this.invTransferEntity = invTransferEntity;
    }

    public List<InvTransferEntity> getInvTransferEntityList() {
        return invTransferEntityList;
    }

    public void setInvTransferEntityList(List<InvTransferEntity> invTransferEntityList) {
        this.invTransferEntityList = invTransferEntityList;
    }

    /**
     * @return the isPermission
     */
    public Boolean getIsPermission() {
        return isPermission;
    }

    /**
     * @param isPermission the isPermission to set
     */
    public void setIsPermission(Boolean isPermission) {
        this.isPermission = isPermission;
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
