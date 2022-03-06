package com.igma.InvItemType;

import com.toby.businessservice.InvItemTypeService;
import com.toby.dto.InvItemTypeDTO;
import com.toby.entity.InvItemType;
import com.toby.toby.BaseFormBean;
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
 * @author H
 */
@Named(value = "invItemTypeMBList")
@ViewScoped
public class InvItemTypeMBList extends BaseFormBean {

    private InvItemType invItemType;
    private InvItemTypeDTO invItemTypedDTOSelected;
    private List<InvItemTypeDTO> invItemTypeDtoList;

    @EJB
    InvItemTypeService invItemTypeService;

    @Override
    @PostConstruct
    public void init() {
        load();
    }

    @Override
    public void load() {
        System.out.println("ssssssss");

    }

    @Override
    public String save() {

        return "";
    }

    public String goToEdit() {
        try {
            if (getInvItemTypedDTOSelected() != null && getInvItemTypedDTOSelected().getId() > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("InvItemTypedDTOSelected", getInvItemTypedDTOSelected().getId());
                context.getSessionMap().put("ScreenMode", "editInvItemTyped");
                exit("../InvItemType/invItemTypeForm.xhtml");
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
            if (getInvItemTypedDTOSelected() != null && getInvItemTypedDTOSelected().getId() != null) {
                invItemTypeService.deleteInvItemType(getInvItemTypedDTOSelected().getId());
                getInvItemTypeDtoList().remove(getInvItemTypedDTOSelected());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "تم الجذذف بنجاح", "تم الجذذف بنجاح"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "خطأ", "خطأ فى الحف"));
            saveError(e, "InvPurchaseInvoiceListMB", "delete");
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the invItemType
     */
    public InvItemType getInvItemType() {
        if (invItemType == null) {
            setInvItemType(new InvItemType());
        }
        return invItemType;
    }

    /**
     * @param invItemType the invItemType to set
     */
    public void setInvItemType(InvItemType invItemType) {
        this.invItemType = invItemType;
    }

    /**
     * @return the invItemTypeDtoList
     */
    public List<InvItemTypeDTO> getInvItemTypeDtoList() {
        if (invItemTypeDtoList == null) {
            setInvItemTypeDtoList(invItemTypeService.getALLInvItemTypeDTOByBranchId(getUserData().getUserBranch().getId()));
        }
        return invItemTypeDtoList;
    }

    /**
     * @param invItemTypeDtoList the invItemTypeDtoList to set
     */
    public void setInvItemTypeDtoList(List<InvItemTypeDTO> invItemTypeDtoList) {
        this.invItemTypeDtoList = invItemTypeDtoList;
    }

    /**
     * @return the invItemTypedDTOSelected
     */
    public InvItemTypeDTO getInvItemTypedDTOSelected() {
        if (invItemTypedDTOSelected == null) {
            invItemTypedDTOSelected = new InvItemTypeDTO();
        }
        return invItemTypedDTOSelected;
    }

    /**
     * @param invItemTypedDTOSelected the invItemTypedDTOSelected to set
     */
    public void setInvItemTypedDTOSelected(InvItemTypeDTO invItemTypedDTOSelected) {
        this.invItemTypedDTOSelected = invItemTypedDTOSelected;
    }

}
