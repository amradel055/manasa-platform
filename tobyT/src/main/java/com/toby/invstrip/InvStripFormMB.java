package com.toby.invstrip;

import com.toby.businessservice.InvItemService;
import com.toby.businessservice.InvStripDetailService;
import com.toby.businessservice.InvStripService;
import com.toby.businessservice.QuantityItemsStoreService;
import com.toby.businessservice.TobyUserInventoryService;
import com.toby.converter.InvInventoryDTOConverter;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvStripDTO;
import com.toby.dto.InvStripDetailDTO;
import com.toby.dto.QuantityItemsStoreDTO;
import com.toby.entity.InvInventory;
import com.toby.entity.InvItem;
import com.toby.entity.InvStripDetail;
import com.toby.entity.InventorySetup;
import com.toby.entiy.InvStripDetailEntity;
import com.toby.toby.AutoComplete;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.math.BigDecimal;
import java.util.*;
import com.toby.businessservice.InventorySetupService;

/**
 * @author khaled
 */
@Named(value = "invStripFormMB")
@ViewScoped
public class InvStripFormMB extends BaseFormBean {

    private InvItem invItem;
    private InvStripDTO invStripDTO;
    private InventorySetup invSetup;
    private InvStripDetailDTO invStripDetailSelected;
    private List<InvInventory> invInventoryList;
    private List<InvInventoryDTO> invInventoryDTOList;

    private InvInventoryDTOConverter invInventoryDTOConverter;

    private Integer invStripId;




    private Boolean markEdit = Boolean.FALSE;
    private Boolean showMessageDetails = Boolean.FALSE;
    private Boolean showMessageGeneral = Boolean.FALSE;
    private boolean showMessage = Boolean.FALSE;
    private Boolean delete = Boolean.FALSE;

    private Map<Integer, InvStripDetail> invStripDetailMap;

    @EJB
    private InvItemService invItemService;
    @EJB
    private InventorySetupService inventorySetupService;
    @EJB
    private InvStripService invStripService;
    @EJB
    private InvStripDetailService invStripDetailService;
    @EJB
    private TobyUserInventoryService isagUserInventoryService;
    @EJB
    private QuantityItemsStoreService quantityItemsStoreService;

    @Override
    @PostConstruct
    public void init() {
        load();
    }

    @Override
    public void load() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        setScreenMode((String) context.getSessionMap().get("ScreenMode"));
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("add")) {

        } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
            try {
                invStripId = (Integer) context.getSessionMap().get("invStripSelected");
                invStripDTO = invStripService.findInvStripByInvStripIdDTO(invStripId, getUserData().getUser());
                invStripDTO.setInvStripDetailDTOList(invStripDetailService.getAllInvStripDetailsDTO(invStripDTO.getId(), getUserData().getUser()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void fillItemMap() {
        if (invStripDTO.getInventoryId() != null) {
           List<QuantityItemsStoreDTO> quantityItemsStoreDTOList = quantityItemsStoreService.findInventoryItemsListDTO(invStripDTO.getInventoryId().getId(), getUserData().getUser(), true);
          invStripDTO.setInvStripDetailDTOList(invStripDetailService.mapToDTOList1(quantityItemsStoreDTOList,  invStripDTO,getUserData().getUser()));
         }
    }

    @Override
    public String save() {

        return "";
    }

    public void reset() {
        invStripDTO = new InvStripDTO();
        invStripDetailSelected = new InvStripDetailDTO();
    }

    public void deleteInvStripDetail() {

        setShowMessageGeneral(Boolean.FALSE);
        setShowMessageDetails(Boolean.TRUE);
        if (invStripDetailSelected.getId() != null) {
            setDelete(Boolean.TRUE);

            invStripDTO.getInvStripDetailDTOList().remove(invStripDetailSelected);

           
            for (InvStripDetailDTO detailEntity : invStripDTO.getInvStripDetailDTOList()) {
                updateSummition();
            }
        }
    }

    public void updateDifference(InvStripDetailEntity invStripTable) {
        if (invStripTable.getActualAmount() != null) {
            invStripTable.setDifference(
                    (invStripTable.getBookBalance() != null ? invStripTable.getBookBalance() : BigDecimal.ZERO).
                    subtract(invStripTable.getActualAmount())
            );
        }

        for (InvStripDetailDTO detailEntity : invStripDTO.getInvStripDetailDTOList()) {
            updateSummition();
        }
    }

    private void updateSummition() {
    invStripDTO=  invStripDetailService.updateSummition(invStripDetailSelected, invStripDTO);
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String exit() {
        exit("../invstrip/invStripList.xhtml");
        return "";
    }

    public List<InvInventoryDTO> completeInventory(String query) {
        return AutoComplete.completeInventory(query, getInvInventoryDTOList(), invInventoryDTOConverter);

    }

    public InvItem getInvItem() {
        return invItem;
    }

    public void setInvItem(InvItem invItem) {
        this.invItem = invItem;
    }

    public Integer getInvStripId() {
        return invStripId;
    }

    public void setInvStripId(Integer invStripId) {
        this.invStripId = invStripId;
    }

   
    public Boolean getMarkEdit() {
        return markEdit;
    }

    public void setMarkEdit(Boolean markEdit) {
        this.markEdit = markEdit;
    }

    public Boolean getShowMessageDetails() {
        return showMessageDetails;
    }

    public void setShowMessageDetails(Boolean showMessageDetails) {
        this.showMessageDetails = showMessageDetails;
    }

    public Boolean getShowMessageGeneral() {
        return showMessageGeneral;
    }

    public void setShowMessageGeneral(Boolean showMessageGeneral) {
        this.showMessageGeneral = showMessageGeneral;
    }

    public boolean isShowMessage() {
        return showMessage;
    }

    public void setShowMessage(boolean showMessage) {
        this.showMessage = showMessage;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public Map<Integer, InvStripDetail> getInvStripDetailMap() {
        return invStripDetailMap;
    }

    public void setInvStripDetailMap(Map<Integer, InvStripDetail> invStripDetailMap) {
        this.invStripDetailMap = invStripDetailMap;
    }

    public List<InvInventory> getInvInventoryList() {
        return invInventoryList;
    }

    public void setInvInventoryList(List<InvInventory> invInventoryList) {
        this.invInventoryList = invInventoryList;
    }

    public InventorySetup getInvSetup() {
        return invSetup;
    }

    public void setInvSetup(InventorySetup invSetup) {
        this.invSetup = invSetup;
    }

    public InvStripDTO getInvStripDTO() {
        if (invStripDTO == null) {
            invStripDTO = new InvStripDTO();
            invStripDTO.setInvStripDetailDTOList(new ArrayList<>());
            invStripDTO.setTotalQuantity(BigDecimal.ZERO);
            invStripDTO.setTotalBalance(BigDecimal.ZERO);
        }

        return invStripDTO;
    }

    public void setInvStripDTO(InvStripDTO invStripDTO) {
        this.invStripDTO = invStripDTO;
    }

    public InvStripDetailDTO getInvStripDetailSelected() {
        if(invStripDetailSelected==null){
        invStripDetailSelected=new InvStripDetailDTO();
        invStripDetailSelected.setActualAmount(BigDecimal.ZERO);
                
        }
        return invStripDetailSelected;
    }

    public void setInvStripDetailSelected(InvStripDetailDTO invStripDetailSelected) {
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
}
