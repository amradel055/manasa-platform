package com.toby.invstrip;

import com.toby.businessservice.InvStripDetailService;
import com.toby.businessservice.InvStripService;
import com.toby.businessservice.TobyUserInventoryService;
import com.toby.converter.InvInventoryDTOConverter;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvStripDTO;
import com.toby.entity.InvInventory;
import com.toby.toby.AutoComplete;
import com.toby.toby.BaseFormBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.util.*;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * @author khaled
 */
@Named(value = "invStripFormNewMB")
@ViewScoped
public class InvStripFormNewMB extends BaseFormBean {

    private InvStripDTO invStripDTO;
    private InvDetailDTO invStripDetailSelected;
    private List<InvInventory> invInventoryList;
    private List<InvInventoryDTO> invInventoryDTOList;

    private InvInventoryDTOConverter invInventoryDTOConverter;

    private List<InvDetailDTO> invStripDetailList;

    @EJB
    private InvStripService invStripService;
    @EJB
    private InvStripDetailService invStripDetailService;
    @EJB
    private TobyUserInventoryService isagUserInventoryService;

    @Override
    @PostConstruct
    public void init() {
        load();
    }

    @Override
    public void load() {
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
            try {
                Integer invStripId = (Integer) getContext().getSessionMap().get("invStripSelected");
                invStripDTO = invStripService.findInvStripByInvStripIdDTO(invStripId, getUserData().getUser());
                setInvStripDetailList(invStripDetailService.getAllInvStripDetailsDTOASINVDEtail(invStripDTO.getId(), getUserData().getUser()));
                completeInventory(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public String save() {

        return "";
    }

    public void saveHead() {
        if (getInvStripDTO() != null) {
            invStripDTO.setType(0);
           invStripDTO = invStripService.saveNew(invStripDTO, getUserData().getUser());
            msgSave(getInvStripDTO().getMsg());
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد تفاصيل "));
        }
    }
    
    public void replacedetail(InvDetailDTO invPurchaseInvoiceDetailsValidate) {
        for (InvDetailDTO detailDTO : getInvStripDetailList()) {
            if (detailDTO.getIndex().equals(invPurchaseInvoiceDetailsValidate.getIndex())) {
                int itemIndex = getInvStripDetailList().indexOf(detailDTO);
                if (itemIndex != -1) {
                    getInvStripDetailList().set(itemIndex, invPurchaseInvoiceDetailsValidate);
                }
            }
        }
    }

    public void validateActulQuantityColumNew(InvDetailDTO detailDTO) {
        try {
            if (getInvStripDTO() != null && getInvStripDTO().getId() != null) {
                detailDTO.setMsg(null);
                detailDTO = invStripDetailService.addInvStripDetailsDTONew(detailDTO, getInvStripDTO().getId(), getUserData().getUser());
                replacedetail(detailDTO);
                addInvDetail();
                changeFocus("form:invStripTable:" + (getInvStripDetailList().size()-1) + ":invItemNameIn");
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "validateQuantityColum");
        }
    }

    public void deleteInvDetailNew(InvDetailDTO invDetailDTO) {
        try {
            if (invDetailDTO.getId() != null) {
                invStripDetailService.deleteInvStripDetailDTO(invDetailDTO);
            }
            getInvStripDetailList().remove(invDetailDTO);
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "deleteInvPurchaseInvoiceDetail");
        }
    }
    
    public void addInvDetail() {
        try {
            getInvStripDTO().setMsg(null);
            
            InvDetailDTO invPurchaseInvoiceDetailsEntityNew = new InvDetailDTO();
            invPurchaseInvoiceDetailsEntityNew.setIndex(getIndex());
            setInvStripDetailSelected(invPurchaseInvoiceDetailsEntityNew);
            getInvStripDetailList().add(invPurchaseInvoiceDetailsEntityNew);
            
            errorMsg(getInvStripDTO().getMsg());
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "addInvPurchaseInvoiceDetail");
        }
    }

    public void reset() {
        invStripDTO = new InvStripDTO();
        setInvStripDetailList(null);
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String exit() {
        exit("../invstripNew/invStripList.xhtml");
        return "";
    }

    public List<InvInventoryDTO> completeInventory(String query) {
        return AutoComplete.completeInventory(query, getInvInventoryDTOList(), invInventoryDTOConverter);

    }
    
    public List<InvInventory> getInvInventoryList() {
        return invInventoryList;
    }

    public void setInvInventoryList(List<InvInventory> invInventoryList) {
        this.invInventoryList = invInventoryList;
    }

    public InvStripDTO getInvStripDTO() {
        if (invStripDTO == null) {
            invStripDTO = new InvStripDTO();
        }

        return invStripDTO;
    }

    public void setInvStripDTO(InvStripDTO invStripDTO) {
        this.invStripDTO = invStripDTO;
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

    public List<InvInventoryDTO> getInvInventoryDTOList() {
        if (invInventoryDTOList == null || invInventoryDTOList.isEmpty()) {
            invInventoryDTOList = isagUserInventoryService.getAllInventroyDTOByUserAndBranch(getUserData().getUser());
            setInvInventoryDTOConverter(new InvInventoryDTOConverter(invInventoryDTOList));
        }
        return invInventoryDTOList;
    }

    public void setInvInventoryDTOList(List<InvInventoryDTO> invInventoryDTOList) {
        this.invInventoryDTOList = invInventoryDTOList;
    }

    public InvInventoryDTOConverter getInvInventoryDTOConverter() {
        return invInventoryDTOConverter;
    }

    public void setInvInventoryDTOConverter(InvInventoryDTOConverter invInventoryDTOConverter) {
        this.invInventoryDTOConverter = invInventoryDTOConverter;
    }

    /**
     * @return the invStripDetailList
     */
    public List<InvDetailDTO> getInvStripDetailList() {
        if(invStripDetailList == null){
            invStripDetailList = new ArrayList<>();
        }
        return invStripDetailList;
    }

    /**
     * @param invStripDetailList the invStripDetailList to set
     */
    public void setInvStripDetailList(List<InvDetailDTO> invStripDetailList) {
        this.invStripDetailList = invStripDetailList;
    }
}
