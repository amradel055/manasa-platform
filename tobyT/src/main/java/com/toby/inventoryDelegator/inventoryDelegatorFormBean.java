/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.inventoryDelegator;

import com.toby.businessservice.InvDelegatorService;
import com.toby.dto.InventoryDelegatorDTO;
import com.toby.toby.BaseFormBean;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author hq002
 */
@Named(value = "inventoryDelegatorFormBean")
@ViewScoped
public class inventoryDelegatorFormBean extends BaseFormBean implements Serializable {

    private InventoryDelegatorDTO inventoryDelegatorDTO;
    private Boolean showGeneralMessage;
    @EJB
    private InvDelegatorService inventoryDelegatorService;

    @Override
    @PostConstruct
    public void init() {
        load();

    }

    @Override
    public void load() {

        try {
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("add")) {
                reset();

            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
                try {
                    Integer delegatorId = (Integer) getContext().getSessionMap().get("inventoryDelegatorSelected");
                    setEditedInventoryDelegatedValues(delegatorId);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            saveError(e, "inventoryDelegatorFormBean", "load");
        }
    }

    public void setEditedInventoryDelegatedValues(Integer id) {
        try {
            inventoryDelegatorDTO = inventoryDelegatorService.findInventoryDelegator(id);
        } catch (Exception e) {
            saveError(e, "inventoryDelegatorFormBean", "setEditedInventoryDelegatedValues");
        }
    }

    public void reset() {
        try {
            inventoryDelegatorDTO = new InventoryDelegatorDTO();
            if (getUri().contains("purchasesrepresentative")) {
                inventoryDelegatorDTO.setType(0);
            } else {
                inventoryDelegatorDTO.setType(1);
            }
        } catch (Exception e) {
            saveError(e, "inventoryDelegatorFormBean", "setInventoryDelegatedDataEmpty");
        }
    }

    public String exit() {

        if (getUri().contains("purchasesrepresentativeForm")) {
            exit("../purchasesrepresentative/purchasesrepresentativeList.xhtml");
        } else if (getUri().contains("salespersonForm")) {
            exit("../salesperson/salespersonList.xhtml");
        }
        return "";
    }

    public boolean checkCodeExistence() {
        if (inventoryDelegatorDTO != null) {
            Boolean x = inventoryDelegatorService.validateCode(getUserData().getUser(), inventoryDelegatorDTO.getId(), inventoryDelegatorDTO.getCode(), inventoryDelegatorDTO.getType());
            if (!x) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "الكود موجود", null));
                setShowGeneralMessage((Boolean) true);
                return false;
            }
            return true;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "خطا فى الحفظ", null));
            setShowGeneralMessage((Boolean) true);
            return false;
        }

    }

    @Override
    public String save() {
        try {
            inventoryDelegatorDTO = inventoryDelegatorService.updateInventoryDelegatorDTO(inventoryDelegatorDTO, getUserData().getUser());
            if (inventoryDelegatorDTO != null && inventoryDelegatorDTO.getId() != null && (inventoryDelegatorDTO.getMsg() == null || inventoryDelegatorDTO.getMsg().isEmpty())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "تم الحفظ بنجاح", getUserData().getUserDDs().get("SAVED")));
                exit();
            } else {
                errorMessage(inventoryDelegatorDTO.getMsg());
            }
            showGeneralMessage = true;
            return "";
        } catch (Exception e) {
            saveError(e, "setupNewInventoryFormMB", "save");
            return null;

        }
    }

    @Override
    public String getScreenName() {

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public InventoryDelegatorDTO getInventoryDelegatorDTO() {
        if (inventoryDelegatorDTO == null) {
            inventoryDelegatorDTO = new InventoryDelegatorDTO();
        }
        return inventoryDelegatorDTO;
    }

    /**
     * @param inventoryDelegatorDTO the inventoryDelegatorDTO to set
     */
    public void setInventoryDelegatorDTO(InventoryDelegatorDTO inventoryDelegatorDTO) {
        this.inventoryDelegatorDTO = inventoryDelegatorDTO;
    }

    /**
     * @return the showGeneralMessage
     */
    public Boolean getShowGeneralMessage() {
        return showGeneralMessage;
    }

    /**
     * @param showGeneralMessage the showGeneralMessage to set
     */
    public void setShowGeneralMessage(Boolean showGeneralMessage) {
        this.showGeneralMessage = showGeneralMessage;
    }

}
