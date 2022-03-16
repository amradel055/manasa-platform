package com.toby.invtransfer;

import com.toby.businessservice.InvItemService;
import com.toby.businessservice.InvTransferDetailService;
import com.toby.businessservice.InvTransferService;
import com.toby.businessservice.QuantityItemsStoreAddExitService;
import com.toby.businessservice.TobyUserInventoryService;
import com.toby.businessservice.UnitsItemsService;
import com.toby.converter.InvInventoryDTOConverter;
import com.toby.converter.ItemDTOConverter;
import com.toby.converter.UnitsItemsConverter;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.InvTransferDTO;
import com.toby.dto.InvTransferDetailDTO;
import com.toby.entity.UnitsItems;
import com.toby.toby.AutoComplete;
import com.toby.toby.BaseFormBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.math.BigDecimal;
import java.util.*;
import javax.faces.application.FacesMessage;
import javax.servlet.http.HttpServletRequest;

/**
 * @author khaled
 */
@Named(value = "invTransferFormMB")
@ViewScoped
public class InvTransferFormMB extends BaseFormBean {

    private List<InvInventoryDTO> invInventoryDTOList;
    private InvInventoryDTO invInventoryDTO;

    private InvInventoryDTOConverter invInventoryDTOConverter;
    private InvInventoryDTOConverter invInventoryDTOConverter1;
    private Map<Integer, BigDecimal> avialableQuantityMap;
    private List<InvItemDTO> invItemDTOList;
    private InvItemDTO invItemDTO;
    private InvTransferDTO invTransferDTO;
    private List<InvTransferDTO> invTransferDTOList;
    private InvTransferDetailDTO invTransferDetailDTOSelected;
    private UnitsItemsConverter unitsItemsConverter;
    private ItemDTOConverter itemDTOConverter;

    private boolean showMessage = Boolean.FALSE;
    @EJB
    private InvItemService invItemService;
    @EJB
    private InvTransferService invTransferService;
    @EJB
    private InvTransferDetailService invTransferDetailService;
    @EJB
    private TobyUserInventoryService tobyUserInventoryService;
    @EJB
    private QuantityItemsStoreAddExitService quantityItemsStoreAddExitService;
    @EJB
    private UnitsItemsService unitsItemsService;

    @Override
    @PostConstruct
    public void init() {
        load();
    }

    @Override
    public void load() {
        try {

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("add")) {
                reset();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
                try {
                    Integer invTransferId = (Integer) context.getSessionMap().get("invTransferSelected");
                    invTransferDTO = invTransferService.findInvTransferByInvTransferId(invTransferId);
                    invTransferDTO.setInvTransferDetailDTOList(invTransferDetailService.getAllInvTransferDetaiDTOlList(invTransferId, getUserData().getUser()));
//                    getInvInventoryDTOList();
//                    fillInventoryItem();
//                    fillUnitItem();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "load()");
        }
    }

    public void addRow() {
        if (invTransferDTO != null && invTransferDTO.getInvFrom() != null && invTransferDTO.getInvFrom().getId() != null
                && invTransferDTO.getInvTo() != null && invTransferDTO.getInvTo().getId() != null) {
            if (validateChangeInventory() && validateDetailsWhenAddRow()) {
                InvTransferDetailDTO invTransferDetailDTO = new InvTransferDetailDTO();
                invTransferDetailDTO.setIndex(getIndex());

                if (invTransferDTO.getInvTransferDetailDTOList() != null) {
                    invTransferDTO.getInvTransferDetailDTOList().add(invTransferDetailDTO);
                }
                invTransferDetailDTOSelected = invTransferDetailDTO;
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "يجب اختيار من مستودع و إلى مستودع", null));
        }
    }

    public boolean validateDetailsWhenAddRow() {
        return invTransferService.validateDetailsWhenAddRow(invTransferDTO);
    }

    public void sumTotalQuantity() {
        invTransferDTO.setTotal(BigDecimal.ZERO);
        for (InvTransferDetailDTO x : invTransferDTO.getInvTransferDetailDTOList()) {
            if (x.getAmount() != null) {
                invTransferDTO.setTotal(x.getAmount().add(invTransferDTO.getTotal()));
            }
        }
    }

    public boolean validateDetailsWhenSave() {
        return invTransferService.validateDetailsWhenSave(invTransferDTO);
    }

    @Override
    public String save() {
        String uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
        if (uri.contains("invtransferInventory")) {
            invTransferDTO.setTransferType(0);
        }
        if (uri.contains("invtransfer")) {
            invTransferDTO.setTransferType(1);
        }
        invTransferDTO = invTransferService.save(invTransferDTO, getUserData().getUser());
        return "";
    }

    public List<InvInventoryDTO> completeInventory(String query) {
        getInvInventoryDTOList();
        return AutoComplete.autoCompleteInventoryDTO(query, invInventoryDTOList, invInventoryDTOConverter);
    }

    public List<InvItemDTO> completeItemsData(String query) {
        return AutoComplete.autoCompleteInvItemDTOData(query, invItemDTOList, itemDTOConverter);
    }

    public void ValidateQuantity() {
        if (invTransferDetailDTOSelected.getInvItemId() != null && invTransferDetailDTOSelected.getInvItemId().getId() != null
                && invTransferDetailDTOSelected.getUnitsItem() != null && invTransferDetailDTOSelected.getUnitsItem().getUnitId() != null) {
            boolean b = invTransferService.ValidateQuantity(invTransferDTO, invTransferDetailDTOSelected, avialableQuantityMap);
            if (b) {
                sumTotalQuantity();
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "اختر الصنف و الوحدة", null));

        }
    }

    private UnitsItems getScrewingAndPrice() {
        UnitsItems unitsItems = new UnitsItems();
        if (invTransferDetailDTOSelected != null && invTransferDetailDTOSelected.getInvItemId() != null
                && invTransferDetailDTOSelected.getInvItemId().getId() != null && invTransferDetailDTOSelected.getUnitsItemsSelected() != null) {
            unitsItems = unitsItemsService.getScrewingAndPrice(invTransferDetailDTOSelected.getInvItemId().getId(), invTransferDetailDTOSelected.getUnitsItemsSelected());
        }
        return unitsItems;
    }

    public void fillUnitItem() {
        if (invTransferDetailDTOSelected != null && invTransferDetailDTOSelected.getInvItemId() != null && invTransferDetailDTOSelected.getInvItemId().getId() != null) {
            invTransferDetailDTOSelected.setUnitsItemseList(unitsItemsService.getUnitsByItemId(invTransferDetailDTOSelected.getInvItemId().getId()));
            setUnitsItemsConverter(new UnitsItemsConverter(invTransferDetailDTOSelected.getUnitsItemseList()));

        }
    }

    public void getInvtransferBySerialNo() {
        if (invTransferDTO.getSerialNo() != null) {
            InvTransferDTO invTransferDTO1 = new InvTransferDTO();
            invTransferDTO1 = invTransferService.findInvTransferBySerialNoAndBranch(invTransferDTO.getSerialNo(), getUserData().getUser());
            invTransferDTO = invTransferDTO1;
            if (invTransferDTO != null && invTransferDTO.getId() != null) {
                invTransferDTO.setInvTransferDetailDTOList(invTransferDetailService.getAllInvTransferDetaiDTOlList(invTransferDTO.getId(), getUserData().getUser()));
            }
        }
    }

    public void fillUnitsItemsSelected() {
        invTransferDetailDTOSelected.setUnitsItem(getScrewingAndPrice());
    }

    public void deleteRow() {
        invTransferDTO.getInvTransferDetailDTOList().remove(invTransferDetailDTOSelected);
        sumTotalQuantity();
    }

    public Map<Integer, BigDecimal> getAvialableQuantityMap() {
        if (avialableQuantityMap == null) {
            if (invTransferDTO.getInvFrom() != null && invTransferDTO.getInvFrom().getId() != null) {
                avialableQuantityMap = quantityItemsStoreAddExitService.findInventoryDTOList(getUserData().getUser(), invTransferDTO.getInvFrom().getId());
            }
        }
        return avialableQuantityMap;
    }

    public void setAvialableQuantityMap(Map<Integer, BigDecimal> avialableQuantityMap) {
        this.avialableQuantityMap = avialableQuantityMap;
    }

    public boolean validateChangeInventory() {
        return invTransferService.validateChangeInventory(invTransferDTO);
    }

    public void fillInventoryItem() {
        try {
            if (invTransferDTO != null && invTransferDTO.getInvFrom() != null && invTransferDTO.getInvFrom().getId() != null) {
                invItemDTOList = invItemService.findInventoryDTOItem(invTransferDTO.getInvFrom().getId(), getUserData().getUser());
                itemDTOConverter = new ItemDTOConverter(invItemDTOList);
            }
            getAvialableQuantityMap();
        } catch (Exception e) {
            saveError(e, "InvTransferFormMB", "fillInventoryItem()");
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void reset() {
        invTransferDTO = new InvTransferDTO();
        invTransferDTO.setInvTransferDetailDTOList(new ArrayList<>());
        String uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
        if (uri.contains("invtransferInventory")) {
            invTransferDTO.setSerialNoList(invTransferService.getSerialNoInvTransferDTO(getUserData().getUser(), 0));
        }
    }

    public String backToList() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            exit("../invtransfer/invTransferList.xhtml");
            return "";

        } catch (Exception e) {
            saveError(e, "invsalesinvoiceformMB", "backToList");
            return null;
        }

    }

    public boolean isShowMessage() {
        return showMessage;
    }

    public void setShowMessage(boolean showMessage) {
        this.showMessage = showMessage;
    }

    /**
     * @return the invInventoryDTOConverter
     */
    public InvInventoryDTOConverter getInvInventoryDTOConverter() {
        return invInventoryDTOConverter;
    }

    /**
     * @param invInventoryDTOConverter the invInventoryDTOConverter to set
     */
    public void setInvInventoryDTOConverter(InvInventoryDTOConverter invInventoryDTOConverter) {
        this.invInventoryDTOConverter = invInventoryDTOConverter;
    }

    /**
     * @return the invInventoryDTOList
     */
    public List<InvInventoryDTO> getInvInventoryDTOList() {
        if (invInventoryDTOList == null || invInventoryDTOList.isEmpty()) {
            invInventoryDTOList = tobyUserInventoryService.getAllInventroyDTOByUserAndBranch(getUserData().getUser());
            invInventoryDTOConverter = new InvInventoryDTOConverter(invInventoryDTOList);
        }
        return invInventoryDTOList;
    }

    /**
     * @param invInventoryDTOList the invInventoryDTOList to set
     */
    public void setInvInventoryDTOList(List<InvInventoryDTO> invInventoryDTOList) {
        this.invInventoryDTOList = invInventoryDTOList;
    }

    /**
     * @return the invItemDTO
     */
    public InvItemDTO getInvItemDTO() {
        if (invItemDTO == null) {
            invItemDTO = new InvItemDTO();
        }
        return invItemDTO;
    }

    /**
     * @param invItemDTO the invItemDTO to set
     */
    public void setInvItemDTO(InvItemDTO invItemDTO) {
        this.invItemDTO = invItemDTO;
    }

    /**
     * @return the invItemDTOList
     */
    public List<InvItemDTO> getInvItemDTOList() {
        if (invItemDTOList == null) {
            invItemDTOList = new ArrayList<>();
        }
        return invItemDTOList;
    }

    /**
     * @param invItemDTOList the invItemDTOList to set
     */
    public void setInvItemDTOList(List<InvItemDTO> invItemDTOList) {
        this.invItemDTOList = invItemDTOList;
    }

    /**
     * @return the invTransferDTO
     */
    public InvTransferDTO getInvTransferDTO() {
        if (invTransferDTO == null) {
            invTransferDTO = new InvTransferDTO();
        }
        return invTransferDTO;
    }

    /**
     * @param invTransferDTO the invTransferDTO to set
     */
    public void setInvTransferDTO(InvTransferDTO invTransferDTO) {
        this.invTransferDTO = invTransferDTO;
    }

    /**
     * @return the invTransferDetailDTO
     */
    public InvTransferDetailDTO getInvTransferDetailDTOSelected() {
        return invTransferDetailDTOSelected;
    }

    /**
     * @param invTransferDetailDTO the invTransferDetailDTO to set
     */
    public void setInvTransferDetailDTOSelected(InvTransferDetailDTO invTransferDetailDTO) {
        this.invTransferDetailDTOSelected = invTransferDetailDTO;
    }

    /**
     * @return the invTransferDTOList
     */
    public List<InvTransferDTO> getInvTransferDTOList() {
        return invTransferDTOList;
    }

    /**
     * @param invTransferDTOList the invTransferDTOList to set
     */
    public void setInvTransferDTOList(List<InvTransferDTO> invTransferDTOList) {
        this.invTransferDTOList = invTransferDTOList;
    }

    public InvInventoryDTO getInvInventoryDTO() {
        if (invInventoryDTO == null) {
            invInventoryDTO = new InvInventoryDTO();

        }
        return invInventoryDTO;
    }

    /**
     * @param invInventoryDTO the invInventoryDTO to set
     */
    public void setInvInventoryDTO(InvInventoryDTO invInventoryDTO) {
        this.invInventoryDTO = invInventoryDTO;
    }

    /**
     * @return the invInventoryDTOConverter1
     */
    public InvInventoryDTOConverter getInvInventoryDTOConverter1() {
        return invInventoryDTOConverter1;
    }

    /**
     * @param invInventoryDTOConverter1 the invInventoryDTOConverter1 to set
     */
    public void setInvInventoryDTOConverter1(InvInventoryDTOConverter invInventoryDTOConverter1) {
        this.invInventoryDTOConverter1 = invInventoryDTOConverter1;
    }

    public UnitsItemsConverter getUnitsItemsConverter() {
        return unitsItemsConverter;
    }

    /**
     * @param unitsItemsConverter the unitsItemsConverter to set
     */
    public void setUnitsItemsConverter(UnitsItemsConverter unitsItemsConverter) {
        this.unitsItemsConverter = unitsItemsConverter;
    }

    /**
     * @return the itemDTOConverter
     */
    public ItemDTOConverter getItemDTOConverter() {
        return itemDTOConverter;
    }

    /**
     * @param itemDTOConverter the itemDTOConverter to set
     */
    public void setItemDTOConverter(ItemDTOConverter itemDTOConverter) {
        this.itemDTOConverter = itemDTOConverter;
    }

}
