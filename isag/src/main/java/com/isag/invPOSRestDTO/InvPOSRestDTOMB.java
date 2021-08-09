/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.invPOSRestDTO;

import com.isag.businessservice.CurrencyServiceDTO;
import com.isag.businessservice.InvGroupService;
import com.isag.businessservice.InvItemService;
import com.isag.businessservice.InvPurchaseInvoiceService_1;
import com.isag.businessservice.ItemsBarcodesDetailsViewService;
import com.isag.converter.ItemsBarcodesDetailsViewConverter;
import com.isag.define.ScreenNameClassEnum;
import com.isag.dto.CurrencyDTO;
import com.isag.dto.InvDetailDTO;
import com.isag.dto.InvInvoiceDTO;
import com.isag.dto.InvItemDTO;
import com.isag.entity.InvGroup;
import com.isag.isag.InventoryBasicDataForm;
import com.isag.views.ItemsBarcodesDetailsView;
import java.math.BigDecimal;
import java.util.ArrayList;
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
 * @author WIN7
 */
@Named(value = "invPOSRestDTOMB")
@ViewScoped
public class InvPOSRestDTOMB extends InventoryBasicDataForm {

    // EJBs
    @EJB
    private InvPurchaseInvoiceService_1 invPurchaseInvoiceService;
    @EJB
    ItemsBarcodesDetailsViewService itemsBarcodesDetailsViewService;
    @EJB
    InvGroupService invGroupService;
    @EJB
    InvItemService invItemService;
    @EJB
    CurrencyServiceDTO currencyServiceDTO;

    private BigDecimal remain;
    private List<InvGroup> groupList;
    private InvGroup groupSelected;
    private List<InvItemDTO> itemList;
    private InvItemDTO itemSelected;
    private String amount = "";
    private CurrencyDTO currencyDTODefault;
    private Boolean eating;
    private Boolean drinking;

    private List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList;

    public void concatAmount(String amountScreen) {
        amount = amount + amountScreen;
    }

    public void removeAmount() {
        setAmount("");
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            String error = validateloginDistributedScreens(getInvInvoiceDTO().getScreenName());
            if (error.isEmpty()) {
                load();
            } else {
                setErrorGlobal(error);
                exit("../base/error.xhtml");
            }
            //   getItemsBarcodesDetailsViewList();

        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            resetRest();
            fillDefaultList();
            getInvInvoiceDTO().setOrganizationSiteId(getInvOrganizationSiteDTOList(true, 0).get(0));
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "load");
        }
    }

    @Override
    public String save() {
        if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getInvPurchaseInvoiceDetailList() != null && !getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().isEmpty()) {
            getInvInvoiceDTO().setMsg(null);
            if (getInvInvoiceDTO().getTypeRest() != null && getInvInvoiceDTO().getTypeRest() == 1) {
                if (getInvInvoiceDTO().getCloseTable() == null || getInvInvoiceDTO().getCloseTable() != 1) {
                    getInvInvoiceDTO().setGlBankId(null);
                } else {
                    setGlBankList(getUserData().getGlBankDTOList());
                    if (getGlBankList() != null && !getGlBankList().isEmpty()) {
                        getInvInvoiceDTO().setGlBankId(getGlBankList().get(0));
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"),
                                        "لا يوجد خزنة للكاشير من فضلك الذهاب للإعدادات واعطاء صلاحية خزنة للمستخدم"));
                    }
                }
            }
            setInvInvoiceDTO(invPurchaseInvoiceService.save(getInvInvoiceDTO(), getUserData().getUser()));
            checkGroups();
            msgSave(getInvInvoiceDTO().getMsg());
//            printRest();
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد تفاصيل للفاتورة"));
        }
        return "";
    }

    public void checkGroups() {
        for (InvDetailDTO invDetailDTO : getInvInvoiceDTO().getInvPurchaseInvoiceDetailList()) {
            if (invDetailDTO.getItemId() != null && invDetailDTO.getItemId().getGroupId() != null
                    && invDetailDTO.getItemId().getGroupId().getParent() != null && invDetailDTO.getItemId().getGroupId().getParent().getCode() != null
                    && invDetailDTO.getItemId().getGroupId().getParent().getCode().equals(1)) {
                setEating(Boolean.TRUE);
            } else if (invDetailDTO.getItemId() != null && invDetailDTO.getItemId().getGroupId() != null
                    && invDetailDTO.getItemId().getGroupId().getParent() != null && invDetailDTO.getItemId().getGroupId().getParent().getCode() != null
                    && invDetailDTO.getItemId().getGroupId().getParent().getCode().equals(2)) {
                setDrinking(Boolean.TRUE);
            }
        }
    }

    public void resetRedirect() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.getSessionMap().put("ScreenMode", "addInvoice");
        exit("../invposrest/invposrestform.xhtml");
    }

    public void resetRest() {
        reset();
        getInvInvoiceDTO().setCurrencyId(getCurrencyDTODefault());
        getInvInvoiceDTO().setOrganizationSiteId(completeCustomerFrom(null).get(0));
        setEating(Boolean.FALSE);
        setDrinking(Boolean.FALSE);
    }

    public void printRest() {
        if (getInvInvoiceDTO().getTypeRest() != 1) {
            print();
//            print();
//            reset();
        } else {
            print();
        }
    }

    public void printRest(Integer printType) {
        getInvInvoiceDTO().setPrintType(printType);
        if (getInvInvoiceDTO().getTypeRest() != 1) {
            print();
//            print();
//            reset();
        } else {
            print();
        }
    }

    public String closeTable() {
        getInvInvoiceDTO().setCloseTable(1);
        return save();
    }

    public void selectTable() throws CloneNotSupportedException {
        InvInvoiceDTO invoiceDTO = invPurchaseInvoiceService.findInvPurchaseInvoiceByTableId(getInvInvoiceDTO(), getUserData().getUserBranch().getId());
        InvInvoiceDTO invoiceDTOBak = (InvInvoiceDTO) getInvInvoiceDTO().clone();
        if (invoiceDTO != null) {
            setInvInvoiceDTO(invoiceDTO);
        } else {
            reset();
            if (invoiceDTOBak != null) {
                getInvInvoiceDTO().setTableNumber(invoiceDTOBak.getTableNumber());
            }
            getInvInvoiceDTO().setTypeRest(1);
        }

    }

    public void calcAfterPayCash() {
        try {
            if (getFirstLoad()) {
                calculateRemainValue();
                savePOS();
                setFirstLoad(Boolean.FALSE);
            }
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "calculateRemainValue");
        }
    }

    public void transferTable() {
        getInvInvoiceDTO().setTableNumber(getInvInvoiceDTO().getTableNumberTo());
        save();
        getInvInvoiceDTO().setTableNumberTo(null);
    }

    public String savePOS() {
        OpenDlg("remainconfirm");
        setRemain(getInvInvoiceDTO().getRemain());
        save();
        changeFocus("sure");
        return "";
    }

    public void closePOS() {
        CloseDlg("remainconfirm");
        reset();
        addInvDetail();
        changeFocus("form:invPurchaseInvoiceDetailsTable:0:invItemNameIn");
    }

    public void selectGroup() {
        setItemList(invItemService.getInvItemByBranchIdAndGroupIdDTO(getUserData().getUserBranch().getId(), getGroupSelected().getId()));
        if (getItemList() != null && !getItemList().isEmpty()) {
//            setItemSelected(getItemList().get(0));
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String exit() {
        if (ScreenNameClassEnum.INVPURCHASEINVOICEFORM.equals(getInvInvoiceDTO().getScreenName())) {
            exit("../invpurchaseinvoice_1/invpurchaseinvoicelist.xhtml");
        } else if (ScreenNameClassEnum.INVRETURNPURCHASEFORM.equals(getInvInvoiceDTO().getScreenName())) {
            exit("../invreturnpurchase_1/invreturnpurchaselist.xhtml");
        } else if (ScreenNameClassEnum.INVSALESINVOICEFORM.equals(getInvInvoiceDTO().getScreenName())) {
            exit("../invsalesinvoice_1/invsalesinvoicelist.xhtml");
        } else if (ScreenNameClassEnum.INVRETURNSALESFORM.equals(getInvInvoiceDTO().getScreenName())) {
            exit("../invreturnsales_1/invreturnsaleslist.xhtml");
        } else if (ScreenNameClassEnum.INVPOSINVOICEFORM.equals(getInvInvoiceDTO().getScreenName())) {
            exit("../invsalesinvoice_1/invsalesinvoicelist.xhtml");
        }
        return "";
    }

    public List<ItemsBarcodesDetailsView> findItemsBarcodesDetailsViewList(Integer invPurchaseInvoiceId) {
        setItemsBarcodesDetailsViewList(itemsBarcodesDetailsViewService.findItemsBarcodesDetailsViewBranchId(getUserData().getUserBranch().getId(), groupSelected.getId()));
        setItemsBarcodesDetailsViewConverter(new ItemsBarcodesDetailsViewConverter(getItemsBarcodesDetailsViewList()));
        return getItemsBarcodesDetailsViewList();
    }

    public void addDetail() {
        if (getItemSelected() != null) {
            if (checkItem(getItemSelected())) {
                ItemsBarcodesDetailsView barcodesDetailsView = new ItemsBarcodesDetailsView();
                barcodesDetailsView.setSellPrice(getItemSelected().getSellPrice());
                barcodesDetailsView.setBarcode(getItemSelected().getCode());
                barcodesDetailsView.setItemId(getItemSelected().getId());
                barcodesDetailsView.setScrewing(BigDecimal.ONE);
                barcodesDetailsView.setUnitId(getItemSelected().getUnitId().getId());
                barcodesDetailsView.setUnitName(getItemSelected().getUnitId().getName());
                barcodesDetailsView.setName(getItemSelected().getName());
                barcodesDetailsView.setInvItem(itemSelected);
                getInvInvoiceDTO().setItemsBarcodesDetailsViewSelected(barcodesDetailsView);
                addInvDetail();
                if (getInvInvoiceDTO().getInvPurchaseInvoiceDetailList() != null && !getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().isEmpty()) {
                    getInvInvoiceDTO().setInvPurchaseInvoiceDetailsSelected(getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().get(getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().size() - 1));
                    if (getAmount() != null && !getAmount().isEmpty() && getAmount().compareTo("0") != 0) {
                        getInvInvoiceDTO().setTotalAmount(new BigDecimal(getAmount()));
                        getInvInvoiceDTO().getInvPurchaseInvoiceDetailsSelected().setQuantity(getInvInvoiceDTO().getTotalAmount());
                    } else {
                        getInvInvoiceDTO().getInvPurchaseInvoiceDetailsSelected().setQuantity(BigDecimal.ONE);
                    }
                    validateItems(getInvInvoiceDTO().getInvPurchaseInvoiceDetailsSelected());
                }
                removeAmount();
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "الصنف موجود بالفاتورة من فضلك عدل الكمية"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "من فضلك اختر صنف"));
        }
    }

    public Boolean checkItem(InvItemDTO invItem) {
        for (InvDetailDTO detailDTO : getInvInvoiceDTO().getInvPurchaseInvoiceDetailList()) {
            if (detailDTO.getItemsBarcodesDetail().getItemId().equals(invItem.getId())) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    /**
     * @return the remain
     */
    public BigDecimal getRemain() {
        if (remain == null) {
            remain = BigDecimal.ZERO;
        }
        return remain;
    }

    /**
     * @param remain the remain to set
     */
    public void setRemain(BigDecimal remain) {
        this.remain = remain;
    }

    /**
     * @return the groupList
     */
    public List<InvGroup> getGroupList() {
        if (groupList == null || groupList.isEmpty()) {
            groupList = invGroupService.getInvGroupByBranchIdANDItem(getUserData().getUserBranch().getId());
        }
        return groupList;
    }

    /**
     * @param groupList the groupList to set
     */
    public void setGroupList(List<InvGroup> groupList) {
        this.groupList = groupList;
    }

    /**
     * @return the groupSelected
     */
    public InvGroup getGroupSelected() {
        return groupSelected;
    }

    /**
     * @param groupSelected the groupSelected to set
     */
    public void setGroupSelected(InvGroup groupSelected) {
        this.groupSelected = groupSelected;
    }

    /**
     * @return the itemsBarcodesDetailsViewList
     */
    public List<ItemsBarcodesDetailsView> getItemsBarcodesDetailsViewList() {
        return itemsBarcodesDetailsViewList;
    }

    /**
     * @param itemsBarcodesDetailsViewList the itemsBarcodesDetailsViewList to
     * set
     */
    public void setItemsBarcodesDetailsViewList(List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList) {
        this.itemsBarcodesDetailsViewList = itemsBarcodesDetailsViewList;
    }

    /**
     * @return the itemList
     */
    public List<InvItemDTO> getItemList() {
        return itemList;
    }

    /**
     * @param itemList the itemList to set
     */
    public void setItemList(List<InvItemDTO> itemList) {
        this.itemList = itemList;
    }

    /**
     * @return the itemSelected
     */
    public InvItemDTO getItemSelected() {
        return itemSelected;
    }

    /**
     * @param itemSelected the itemSelected to set
     */
    public void setItemSelected(InvItemDTO itemSelected) {
        this.itemSelected = itemSelected;
    }

    /**
     * @return the amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     * @return the currencyDTODefault
     */
    public CurrencyDTO getCurrencyDTODefault() {
        if (currencyDTODefault == null) {
            currencyDTODefault = currencyServiceDTO.getDefaultCurrenciesByCompanyId(getUserData().getCompany().getId());
        }
        return currencyDTODefault;
    }

    /**
     * @param currencyDTODefault the currencyDTODefault to set
     */
    public void setCurrencyDTODefault(CurrencyDTO currencyDTODefault) {
        this.currencyDTODefault = currencyDTODefault;
    }

    /**
     * @return the eating
     */
    public Boolean getEating() {
        if (eating == null) {
            eating = Boolean.FALSE;
        }
        return eating;
    }

    /**
     * @param eating the eating to set
     */
    public void setEating(Boolean eating) {
        this.eating = eating;
    }

    /**
     * @return the drinking
     */
    public Boolean getDrinking() {
        if (drinking == null) {
            drinking = Boolean.FALSE;
        }
        return drinking;
    }

    /**
     * @param drinking the drinking to set
     */
    public void setDrinking(Boolean drinking) {
        this.drinking = drinking;
    }

}
