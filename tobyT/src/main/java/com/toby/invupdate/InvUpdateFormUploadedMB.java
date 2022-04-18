package com.toby.invupdate;

import com.toby.businessservice.InvUpdateDetailService;
import com.toby.businessservice.InvUpdateService;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.toby.BaseFormBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * @author khaled
 */
@Named(value = "invUpdateFormUploadedMB")
@ViewScoped
public class InvUpdateFormUploadedMB extends BaseFormBean {

    private InvInvoiceDTO invInvoiceDTO;
    private InvDetailDTO invStripDetailSelected;

    @EJB
    private InvUpdateService invUpdateService;
    @EJB
    private InvUpdateDetailService invUpdateDetailService;

    @Override
    @PostConstruct
    public void init() {
        load();
    }

    @Override
    public void load() {
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditInvUpdate")) {
            try {
                Integer invStripId = (Integer) getContext().getSessionMap().get("invUpdateSelected");
                if (invStripId != null) {
                    invInvoiceDTO = invUpdateService.findInvInvoiceByInvUpdateId(invStripId);
                    invInvoiceDTO.setInvPurchaseInvoiceDetailList(invUpdateDetailService.getAllInvDetailDTOByInvUpdateDetails(invStripId));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String save() {

        return "";
    }

    public void loadInvStrip() {
        if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getSearchBySerial() != null) {
            getInvInvoiceDTO().setInvInventoryId(invUpdateService.findinvinventoryDTO(getInvInvoiceDTO().getSearchBySerial(), getUserData().getUserBranch().getId()));
            if (getInvInvoiceDTO().getInvInventoryId() != null && getInvInvoiceDTO().getInvInventoryId().getId() != null) {
                getInvInvoiceDTO().setInvPurchaseInvoiceDetailList(invUpdateDetailService.getAllInvUpdatesDetailFromInvTransferDetail(getInvInvoiceDTO().getSearchBySerial(), getUserData().getUserBranch().getId(), getInvInvoiceDTO().getInvInventoryId().getId()));
            }
        }
    }

    public void saveHead() {
        if (getInvInvoiceDTO() != null) {
            invInvoiceDTO = invUpdateService.saveNew(getInvInvoiceDTO(), getUserData().getUser());
            msgSave(getInvInvoiceDTO().getMsg());
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد تفاصيل "));
        }
    }

    public void saveAll() {
        if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getInvPurchaseInvoiceDetailList() != null && !getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().isEmpty()) {
            invInvoiceDTO = invUpdateService.saveNewAll(getInvInvoiceDTO(), getUserData().getUser());
            msgSave(getInvInvoiceDTO().getMsg());
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد تفاصيل "));
        }
    }

    public void replacedetail(InvDetailDTO invPurchaseInvoiceDetailsValidate) {
        for (InvDetailDTO detailDTO : getInvInvoiceDTO().getInvPurchaseInvoiceDetailList()) {
            if (detailDTO.getIndex().equals(invPurchaseInvoiceDetailsValidate.getIndex())) {
                int itemIndex = getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().indexOf(detailDTO);
                if (itemIndex != -1) {
                    getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().set(itemIndex, invPurchaseInvoiceDetailsValidate);
                }
            }
        }
    }

    public void reset() {
        invInvoiceDTO = new InvInvoiceDTO();
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String exit() {
        exit("../invstripNew/invStripList.xhtml");
        return "";
    }

    public InvDetailDTO getInvStripDetailSelected() {
        if (invStripDetailSelected == null) {
            invStripDetailSelected = new InvDetailDTO();
        }
        return invStripDetailSelected;
    }

    public void setInvStripDetailSelected(InvDetailDTO invStripDetailSelected) {
        this.invStripDetailSelected = invStripDetailSelected;
    }

    /**
     * @return the invInvoiceDTO
     */
    public InvInvoiceDTO getInvInvoiceDTO() {
        if (invInvoiceDTO == null) {
            invInvoiceDTO = new InvInvoiceDTO();
        }
        return invInvoiceDTO;
    }

    /**
     * @param invInvoiceDTO the invInvoiceDTO to set
     */
    public void setInvInvoiceDTO(InvInvoiceDTO invInvoiceDTO) {
        this.invInvoiceDTO = invInvoiceDTO;
    }
}
