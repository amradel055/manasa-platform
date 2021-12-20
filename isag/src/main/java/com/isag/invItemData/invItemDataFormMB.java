package com.isag.invItemData;

import com.isag.businessservice.CompanyService;
import com.isag.businessservice.InvBarcodeSevice;
import com.isag.businessservice.InvGroupService;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.isag.businessservice.InvItemService;
import com.isag.businessservice.InvPurchaseInvoiceDetailsService;
import com.isag.businessservice.InventorySetupService;
import com.isag.businessservice.ItemsDataViewService;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.QuantityItemsStoreService;
import com.isag.businessservice.SymbolService;
import com.isag.converter.InvGroupConverter;
import com.isag.converter.InvOrganizationSiteConverter;
import com.isag.converter.SymbolConverter;
import com.isag.dto.InvInventoryDTO;
import com.isag.entity.InvBarcode;
import com.isag.entity.InvGroup;
import com.isag.entity.InvInventory;
import com.isag.entity.InvItem;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.InventorySetup;
import com.isag.entity.Symbol;
import com.isag.entiy.InvBarcodeEntity;
import com.isag.entiy.ItemDataEntity;
import com.isag.isag.AutoComplete;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
import com.isag.uploadfile.FileUploadController;
import com.isag.views.QuantityItemsStore;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

@Named(value = "itemDataFormMB")
@ViewScoped
public class invItemDataFormMB extends BaseFormBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private ItemDataEntity itemDataEntity;
    private ItemDataEntity itemDataEntitySelected;
    private List<ItemDataEntity> itemDataEntityReportList;
    private List<ItemDataEntity> itemDataEntityList;
    private InvBarcodeEntity barcodeEntity;
    private List<InvBarcodeEntity> barcodeEntityList;
    private InvBarcodeEntity invBarcodeEntitySelection;
    private InvItem invItem;
    private List<InvItem> itemDataByBranch;
    private Boolean showMessageDetails = Boolean.FALSE;
    private Boolean showMessageGeneral = Boolean.FALSE;
    private Integer itemDataId;
    private Integer selectedCompanyId;
    private List<InvBarcode> invBarcodesList;
    private List<InvBarcode> invBarcodeDeletedList;
    private List<InvBarcodeEntity> invBarcodeDeleted = new ArrayList<>();

    private String fileName;
    private String showImage;
    private String uploadedImage;
    private List<InvBarcode> barcodeList;
    private boolean showMessage = Boolean.FALSE;
    private Boolean delete = Boolean.FALSE;

    private List<InvGroup> invGroupList;
    private InvGroupConverter invGroupConverter;
    private List<Symbol> unitList;
    private SymbolConverter unitConverter;
    private List<InvOrganizationSite> invOrganizationSiteList;
    private InvOrganizationSiteConverter organizationSiteConvertor;
    private List<Symbol> brandList;
    private List<Symbol> countryList;
    private List<Symbol> paintColorList;
    private List<Symbol> enamelColorList;
    private List<Symbol> stoneList;
    private List<Symbol> typeCatList;
    private List<Symbol> addon1List;
    private List<Symbol> addon2List;
    private List<Symbol> sizeList;
    private List<Symbol> SortingList;
    private List<Symbol> cartonsNumberList;
    private List<Symbol> cartonCapacityList;
    private List<Symbol> metersNumberList;
    private SymbolConverter typeCatConverter;
    private SymbolConverter type2CatConverter;
    private SymbolConverter paintColorConverter;

    private SymbolConverter sizeConverter;
    private SymbolConverter SortingConverter;
    private SymbolConverter cartonsNumberConverter;
    private SymbolConverter cartonCapacityConverter;
    private SymbolConverter metersNumberConverter;

    private List<QuantityItemsStore> quantityItemsStoreList;

    private BigDecimal qtySummition = BigDecimal.ZERO;

    private StreamedContent dbImage;

    private Integer companyId;
    private Integer branchId;
    String imageName;
    InvItem invItemValidate;
    private Integer settingType;

    private String destination;
    private StringBuilder errorMessage = new StringBuilder();

    private FileUploadEvent fileUploadEvent;

    private Map itemBarcodeMap = new HashMap();

    @EJB
    private InvItemService invItemService;

    @EJB
    private InvGroupService invGroupService;
    @EJB
    private OrganizationSiteService organizationSiteService;

    @EJB
    private SymbolService symbolService;
    @EJB
    private InvBarcodeSevice invBarcodeSevice;

    @EJB
    private CompanyService companyService;

    @EJB
    private InvPurchaseInvoiceDetailsService invPurchaseInvoiceDetailsService;
    @EJB
    ItemsDataViewService itemsDataViewService;
    @EJB
    QuantityItemsStoreService quantityItemsStoreService;
    @EJB
    InventorySetupService inventorySetupService;

    private FileUploadController fileUploadController = new FileUploadController();

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "init");
        }
    }

    @Override
    public void load() {
        try {

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            companyId = getUserData().getCompany().getId();
            branchId = getUserData().getUserBranch().getId();

            initObjs();
            fillLists();
            InventorySetup setup = inventorySetupService.getInventoryByBranchId(branchId);
            setSettingType(setup.getItemCoding());

            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("add")) {
                resetItemDataForm();

            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
                try {
                    itemDataId = ((Integer) context.getSessionMap().get("itemDataEntitySelected"));

                    if (itemDataId != null) {
//                        quantityItemsStoreList = quantityItemsStoreService.findItemsForBranchList(itemDataId, branchId);

                        qtySummition = BigDecimal.ZERO;
//                        for (QuantityItemsStore itemsStore : quantityItemsStoreList) {
//                            qtySummition = (qtySummition.add(itemsStore.getQty() != null ? itemsStore.getQty().setScale(2) : BigDecimal.ZERO)).setScale(2);
//                        }
                    }

                    invItem = invItemService.findInvItem(getItemDataId());
                    setLastCost(invItem);
                    invBarcodesList = invBarcodeSevice.getInvBarcodeByInvItemId(branchId, invItem);
                    itemDataEntity = mapInvItemDataToItemDataEntityForm(invItem);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "load");
        }
    }

    private void initObjs() {
        try {
            invItem = new InvItem();
            itemDataEntity = new ItemDataEntity();
            itemDataEntityList = new ArrayList<>();
            invBarcodeDeletedList = new ArrayList<>();
            invBarcodesList = new ArrayList<>();
            quantityItemsStoreList = new ArrayList<>();
            barcodeEntity = new InvBarcodeEntity();
            barcodeEntityList = new ArrayList<>();
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "initObjs");
        }
    }

    private void fillLists() {
        try {
            invGroupList = invGroupService.getallInvGroupByBranchId(branchId); // groups
//            invGroupList = invGroupService.getAllSubInvGroupByBranchId(null, branchId);
            invGroupConverter = new InvGroupConverter(invGroupList);

            unitList = symbolService.getUnitsByCompanyId(companyId); //units
            unitConverter = new SymbolConverter(unitList);

            invOrganizationSiteList = organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(branchId, true, 1); //suppliers
            organizationSiteConvertor = new InvOrganizationSiteConverter(invOrganizationSiteList);

            stoneList = symbolService.getStoneByCompanyId(companyId);
            addon1List = symbolService.getAddon1ByCompanyId(companyId);
            addon2List = symbolService.getAddon2ByCompanyId(companyId);
            paintColorList = symbolService.getPaintColorByCompanyId(companyId);
            paintColorConverter = new SymbolConverter(paintColorList);
            enamelColorList = symbolService.getEnamelColorByCompanyId(companyId);
            countryList = symbolService.getCountryByCompanyId(companyId);
            brandList = symbolService.getBrandByCompanyId(companyId);

            typeCatList = symbolService.getTypeCatByCompanyId(companyId);
            typeCatConverter = new SymbolConverter(typeCatList);

            sizeList = symbolService.getSymbolByGeneralSymbol(companyId, 63);
            setSizeConverter(new SymbolConverter(sizeList));
            SortingList = symbolService.getSymbolByGeneralSymbol(companyId, 64);
            setSortingConverter(new SymbolConverter(SortingList));
            cartonsNumberList = symbolService.getSymbolByGeneralSymbol(companyId, 65);
            setCartonsNumberConverter(new SymbolConverter(cartonsNumberList));
            cartonCapacityList = symbolService.getSymbolByGeneralSymbol(companyId, 66);
            setCartonCapacityConverter(new SymbolConverter(cartonCapacityList));
            metersNumberList = symbolService.getSymbolByGeneralSymbol(companyId, 67);
            setMetersNumberConverter(new SymbolConverter(metersNumberList));

        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "fillLists");
        }

    }

    ItemDataEntity mapInvItemDataToItemDataEntityForm(InvItem invItem) {
        try {
            itemDataEntitySelected = new ItemDataEntity();
            itemBarcodeMap = new HashMap();
            if (invItem != null) {
                itemDataEntitySelected.setId(invItem.getId());
                itemDataEntitySelected.setBranchId(invItem.getBranchId());
                itemDataEntitySelected.setBrandId(invItem.getBrandId());
                itemDataEntitySelected.setCode(invItem.getCode());
                itemDataEntitySelected.setCompanyId(invItem.getCompanyId());
                itemDataEntitySelected.setContractPrice(invItem.getContractPrice());
                itemDataEntitySelected.setCostAverage(invItem.getCostAverage());
                itemDataEntitySelected.setCreatedBy(invItem.getCreatedBy());
                itemDataEntitySelected.setCreationDate(invItem.getCreationDate());
                itemDataEntitySelected.setDateCreateCat(invItem.getCreationDate());
                itemDataEntitySelected.setEnamelColorId(invItem.getEnamelColor());
                itemDataEntitySelected.setGroupId(invItem.getGroupId());
                itemDataEntitySelected.setGroupName(invItem.getGroupId() != null ? (invItem.getGroupId().getName() != null ? invItem.getGroupId().getName() : null) : null);
                itemDataEntitySelected.setImage(invItem.getImage() != null ? invItem.getImage() : null);
                setFileName(invItem.getImage() != null ? invItem.getImage() : "1.png");
                setShowImage(invItem.getImage() != null ? fileUploadController.getDestination().concat(invItem.getImage()) : fileUploadController.getDestination() + "1.png");
                setUploadedImage(invItem.getImage());

                itemDataEntitySelected.setIsPurchase(invItem.getIspurchase());
                itemDataEntitySelected.setIsSell(invItem.getIssell());
                itemDataEntitySelected.setLastCost(invItem.getLastCost() != null ? invItem.getLastCost() : BigDecimal.ZERO);
                itemDataEntitySelected.setMarkEdit(invItem.getMarkEdit());
                itemDataEntitySelected.setMaxmumAmount(invItem.getMaxmumAmount());
                itemDataEntitySelected.setMinimumAmount(invItem.getMinimumAmount());
                itemDataEntitySelected.setModifiedBy(invItem.getModifiedBy());
                itemDataEntitySelected.setModifiedDate(invItem.getModificationDate());
                itemDataEntitySelected.setName(invItem.getName());
                itemDataEntitySelected.setNameen(invItem.getNameen());
                itemDataEntitySelected.setNickName(invItem.getNickname());
                itemDataEntitySelected.setOpeningCost(invItem.getOpeningCost());
                itemDataEntitySelected.setOriginCountryId(invItem.getOriginCountry());
                itemDataEntitySelected.setPaintColor(invItem.getPaintColor());
                itemDataEntitySelected.setWeightPackage(invItem.getWeightPackage());
                itemDataEntitySelected.setRemarks(invItem.getRemarks());
                itemDataEntitySelected.setSellPrice(invItem.getSellPrice());
                itemDataEntitySelected.setStatusCat(invItem.getStatusCat());
                itemDataEntitySelected.setStoneId(invItem.getStone());
                itemDataEntitySelected.setStorageLocation(invItem.getStorageLocation());
                itemDataEntitySelected.setStoresQuantity(invItem.getStoresQuality());
                itemDataEntitySelected.setTypeCat(invItem.getTypeCat());
                itemDataEntitySelected.setUndirectCost(invItem.getUndirectCost());
                itemDataEntitySelected.setUnitId(invItem.getUnitId());
                itemDataEntitySelected.setSiteId(invItem.getSiteId());
                itemDataEntitySelected.setLength(invItem.getLength());
                itemDataEntitySelected.setHeight(invItem.getHeight());
                itemDataEntitySelected.setWeight(invItem.getWeight());
                itemDataEntitySelected.setWidth(invItem.getWidth());
                itemDataEntitySelected.setAddon1(invItem.getAddon1());
                itemDataEntitySelected.setAddon2(invItem.getAddon2());
                itemDataEntitySelected.setIsinventoryitem(invItem.getIsInventoryItem());
                itemDataEntitySelected.setSize(invItem.getSize());
                itemDataEntitySelected.setSorting(invItem.getSorting());
                itemDataEntitySelected.setCartonsCapacity(invItem.getCartonsCapacity());
                itemDataEntitySelected.setAdd1(invItem.getAdd1());
                itemDataEntitySelected.setAdd2(invItem.getAdd2());
                itemDataEntitySelected.setAdd3(invItem.getAdd3());
                itemDataEntitySelected.setDisc1(invItem.getDisc1());
                itemDataEntitySelected.setDisc2(invItem.getDisc2());
                itemDataEntitySelected.setDisc3(invItem.getDisc3());
                itemDataEntitySelected.setPrintShow(invItem.getPrintShow());

                if (invItem.getInvInventoryId() != null) {
                    InvInventory invInventory = new InvInventory();
                    invInventory.setId(invItem.getInvInventoryId().getId());
                    invInventory.setName(invItem.getInvInventoryId().getName());
                    invInventory.setCode(invItem.getInvInventoryId().getCode());
                    itemDataEntitySelected.setInvInventoryId(invInventory);
                }

                // prepare to set barckodList in itemData object
                for (InvBarcode invBarcode : invBarcodesList) {
                    invBarcodeEntitySelection = new InvBarcodeEntity();
                    invBarcodeEntitySelection.setId(invBarcode.getId() != null ? invBarcode.getId() : null);
                    invBarcodeEntitySelection.setCode(invBarcode.getCode() != null ? invBarcode.getCode() : null);
                    invBarcodeEntitySelection.setItemId(invBarcode.getItemId() != null ? (invBarcode.getItemId().getId() != null ? invBarcode.getItemId().getId() : null) : null);
                    invBarcodeEntitySelection.setPrice1(invBarcode.getPrice1() != null ? invBarcode.getPrice1() : null);
                    invBarcodeEntitySelection.setScrewing(invBarcode.getScrewing() != null ? invBarcode.getScrewing() : null);
                    invBarcodeEntitySelection.setTypeBarcode(invBarcode.getTypeBarcode());
                    invBarcodeEntitySelection.setUnitId(invBarcode.getUnitId());
                    invBarcodeEntitySelection.setPaintColorId(invBarcode.getPaintColor());
                    invBarcodeEntitySelection.setHeight(invBarcode.getHeight());
                    invBarcodeEntitySelection.setWeight(invBarcode.getWeight());
                    invBarcodeEntitySelection.setWidth(invBarcode.getWidth());
                    invBarcodeEntitySelection.setLength(invBarcode.getLength());

                    itemBarcodeMap.put(invBarcode.getCode(), invBarcode);
                    barcodeEntityList.add(invBarcodeEntitySelection);
                }

                itemDataEntitySelected.setInvBarcodeEntity(barcodeEntityList);

            }
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "mapInvItemDataToItemDataEntityForm");
        }

        return itemDataEntitySelected;
    }

    public void upload(FileUploadEvent event) {
        try {
            fileUploadEvent = event;
            imageName = fileUploadController.upload(event);
            setUploadedImage(imageName);
            setFileName(imageName);
            setShowImage(fileUploadController.getDestination().concat(imageName));
            itemDataEntity.setImage(imageName);
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "upload");
        }

    }

    public void copyfile() throws IOException {
        try {
            fileUploadController.copyFile(imageName, fileUploadEvent.getFile().getInputstream(),
                    fileUploadController.getDestination());
            itemDataEntity.setImage(imageName);
            setFileName(imageName);
            setShowImage(fileUploadController.getDestination().concat(imageName));
//        setUploadedImage(fileUploadEvent.getFile().getFileName());
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "copyfile");
        }
    }

    public void CloseDlg(String dlgvar) {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('" + dlgvar + "').hide();");
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "CloseDlg");
        }
    }

    public void reset() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            context.getSessionMap().replace("ScreenMode", "Add");
            setScreenMode("Add");

            init();
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "reset");
        }

    }

    public void OpenDlg(String dlgvar) {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('" + dlgvar + "').show();");
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "OpenDlg");
        }
    }

    private void resetItemDataForm() {
        try {
            itemDataEntity = new ItemDataEntity();
            invBarcodeEntitySelection = new InvBarcodeEntity();
            itemDataEntity.setInvBarcodeEntity(new ArrayList<InvBarcodeEntity>());

            setFileName("1.png");
            setShowImage("");
            setUploadedImage("");
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "resetItemDataForm");
        }
    }

    public void findMaxItemCode() {
        try {
            if (getSettingType() == 0) {
                if (itemDataEntity.getSiteId() != null) {
                    itemDataEntity.setCode(invItemService.findMaxItemCode(getSettingType(), itemDataEntity.getSiteId().getId(), itemDataEntity.getSiteId().getCode()) + "");
                }
            } else if (getSettingType() == 1) {
                if (itemDataEntity.getGroupId() != null) {
                    itemDataEntity.setCode(invItemService.findMaxItemCode(getSettingType(), itemDataEntity.getGroupId().getId(), itemDataEntity.getSiteId().getCode()) + "");
                }
            }
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "findMaxItemCode");
        }

    }

    @Override
    public String save() {
        try {
            invItem = new InvItem();
            barcodeList = new ArrayList<>();
            if (itemDataEntity != null && itemDataEntity.getName() != null && !itemDataEntity.getName().isEmpty()
                    && itemDataEntity.getSellPrice() != null && itemDataEntity.getSiteId() != null && itemDataEntity.getTypeCat() != null) {
                errorMessage = new StringBuilder();
                validationSave();
                if (errorMessage == null || errorMessage.toString().isEmpty()) {

                    if (itemDataEntity.getId() != null) {
                        invItem.setId(itemDataEntity.getId());
                        InvItem item = invItemService.findInvItem(itemDataEntity.getId());
                        invItem.setCreatedBy(item.getCreatedBy());
                        invItem.setCreationDate(item.getCreationDate());

                        invItem.setModificationDate(new Date());
                        invItem.setModifiedBy(getUserData().getUser());
                    } else {
                        invItem.setCreatedBy(getUserData().getUser());
                        invItem.setCreationDate(new Date());
                    }

                    invItem.setBranchId(getUserData().getUserBranch());
                    invItem.setCode(itemDataEntity.getCode());
                    invItem.setCompanyId(getUserData().getCompany());
                    invItem.setContractPrice(itemDataEntity.getContractPrice());
                    invItem.setCostAverage(itemDataEntity.getCostAverage());
                    invItem.setDateCreateCat(itemDataEntity.getDateCreateCat());
//            invItem.setImage(itemDataEntity.getImage() != null ? itemDataEntity.getImage() : null);
                    invItem.setImage(getFileName());
                    invItem.setLastCost(itemDataEntity.getLastCost());
                    invItem.setMaxmumAmount(itemDataEntity.getMaxmumAmount());
                    invItem.setMinimumAmount(itemDataEntity.getMinimumAmount());
                    invItem.setName(itemDataEntity.getName());
                    invItem.setNameen(itemDataEntity.getNameen());
                    invItem.setNickname(itemDataEntity.getNickName());
                    invItem.setOpeningCost(itemDataEntity.getOpeningCost());
                    invItem.setRemarks(itemDataEntity.getRemarks());
                    invItem.setSellPrice(itemDataEntity.getSellPrice());
                    invItem.setStatusCat(itemDataEntity.getStatusCat());
                    invItem.setStorageLocation(itemDataEntity.getStorageLocation());
                    invItem.setStoresQuality(itemDataEntity.getStoresQuantity());
                    invItem.setWeightPackage(itemDataEntity.getWeightPackage());
                    invItem.setTypeCat(itemDataEntity.getTypeCat());
                    invItem.setUndirectCost(itemDataEntity.getUndirectCost());
                    invItem.setIssell(itemDataEntity.getIsSell());
                    invItem.setIspurchase(itemDataEntity.getIsPurchase());
                    invItem.setPrintShow(itemDataEntity.getPrintShow());

                    invItem.setHeight(itemDataEntity.getHeight());
                    invItem.setLength(itemDataEntity.getLength());
                    invItem.setWeight(itemDataEntity.getWeight());
                    invItem.setWidth(itemDataEntity.getWidth());

                    invItem.setBrandId(itemDataEntity.getBrandId());
                    invItem.setSiteId(itemDataEntity.getSiteId());
                    invItem.setUnitId(itemDataEntity.getUnitId());
                    invItem.setEnamelColor(itemDataEntity.getEnamelColorId());
                    invItem.setGroupId(itemDataEntity.getGroupId());
                    invItem.setOriginCountry(itemDataEntity.getOriginCountryId());
                    invItem.setPaintColor(itemDataEntity.getPaintColor());
                    invItem.setStone(itemDataEntity.getStoneId());
                    invItem.setAddon1(itemDataEntity.getAddon1());
                    invItem.setAddon2(itemDataEntity.getAddon2());
                    invItem.setIsInventoryItem(itemDataEntity.getIsinventoryitem());

                    invItem.setSize(itemDataEntity.getSize());
                    invItem.setSorting(itemDataEntity.getSorting());
                    invItem.setCartonsCapacity(itemDataEntity.getCartonsCapacity());
                    invItem.setAdd1(itemDataEntity.getAdd1());
                    invItem.setAdd2(itemDataEntity.getAdd2());
                    invItem.setAdd3(itemDataEntity.getAdd3());
                    invItem.setDisc1(itemDataEntity.getDisc1());
                    invItem.setDisc2(itemDataEntity.getDisc2());
                    invItem.setDisc3(itemDataEntity.getDisc3());

                    if (itemDataEntity.getInvInventoryId() != null) {
                        InvInventory invInventory = new InvInventory();
                        invInventory.setId(itemDataEntity.getInvInventoryId().getId());
                        invItem.setInvInventoryId(invInventory);
                    }

                    InvBarcode barcode, ib;
                    for (InvBarcodeEntity entity : itemDataEntity.getInvBarcodeEntity()) {

                        barcode = new InvBarcode();

                        barcode.setBranchId(getUserData().getUserBranch());
                        barcode.setCode(entity.getCode() != null ? entity.getCode() : null);
                        barcode.setCompanyId(getUserData().getCompany());
                        barcode.setPrice1(entity.getPrice1() != null ? entity.getPrice1() : null);
                        barcode.setScrewing(entity.getScrewing() != null ? entity.getScrewing() : null);

                        barcode.setHeight(entity.getHeight());
                        barcode.setLength(entity.getLength());
                        barcode.setWeight(entity.getWeight());
                        barcode.setWidth(entity.getWidth());

                        if (entity.getId() != null) {
                            barcode.setId(entity.getId());
                            ib = new InvBarcode();

                            ib = invBarcodeSevice.findInvBarcode(entity.getId());
                            barcode.setModifiedBy(getUserData().getUser() != null ? getUserData().getUser() : null);
                            barcode.setModificationDate(new Date());

                            barcode.setCreatedBy(ib.getCreatedBy() != null ? ib.getCreatedBy() : null);
                            barcode.setCreationDate(ib.getCreationDate());
                        } else {
                            barcode.setCreatedBy(getUserData().getUser() != null ? getUserData().getUser() : null);
                            barcode.setCreationDate(new Date());
                        }

                        barcode.setTypeBarcode(entity.getTypeBarcode());

                        barcode.setUnitId(entity.getUnitId());
                        barcode.setPaintColor(entity.getPaintColorId());

                        barcodeList.add(barcode);
                    }

                    if (getInvBarcodeDeleted() != null) {

                        for (InvBarcodeEntity deletedBarcode : getInvBarcodeDeleted()) {
                            ib = new InvBarcode();
                            ib.setId(deletedBarcode.getId());

                            invBarcodeDeletedList.add(ib);
                        }
                    }
                    invItem = invItemService.addInvItem(invItem, barcodeList, invBarcodeDeletedList);
                    invItemService.updateInvItem(invItem.getId());
                    if (invItem.getMsg() != null && !invItem.getMsg().isEmpty()) {
                        errorMessage(invItem.getMsg());
                        return "";
                    }
                    savedMeesage(getUserData().getUserDDs().get("SAVED"));
                    reset();
                    changeFocus("itemNumber");
                    return "";
//                    return cancel();
                } else {
                    errorMessage(errorMessage.toString());
                    return "";
                }

            }
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "save");
        }

        return "";
    }

    public Boolean validateDetailRow() {
        try {
            if (invBarcodeEntitySelection != null && invBarcodeEntitySelection.getCode() != null && invBarcodeEntitySelection.getUnitId() != null
                    && invBarcodeEntitySelection.getPrice1() != null && invBarcodeEntitySelection.getScrewing() != null) {
                return true;
            }
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "validateDetailRow");
        }
        return false;
    }

    public void validationSave() {
        try {
            validationCodeAndNickname();
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "validationSave");
        }

    }

    public void validateBlur() {
        try {
            initializeSeachObjet(itemDataEntity.getCode(), null, itemDataEntity.getId());

            if (invItemService.validateCodeFromView(invItemValidate)) {
                errorMessage.append(getUserData().getUserDDs().get("UNIQE_SERIAL"));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), errorMessage.toString()));
            }
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "validateBlur");
        }

    }

    public void validateBarcodeBlur(InvBarcodeEntity invBarcodeEntitySelectionTable) {
        try {
            if (invBarcodeEntitySelectionTable != null && itemBarcodeMap != null && !itemBarcodeMap.containsKey(invBarcodeEntitySelectionTable.getCode())) {
                initializeSeachObjet(invBarcodeEntitySelectionTable.getCode(), null, invBarcodeEntitySelectionTable.getId());
                if (invItemService.validateCodeFromView(invItemValidate)) {
                    invBarcodeEntitySelectionTable.setCode(null);
                    errorMessage.append(getUserData().getUserDDs().get("UNIQE_SERIAL"));
                    errorMessage(errorMessage.toString());
                }
            } else {
                invBarcodeEntitySelectionTable.setCode(null);
                errorMessage("تم ادخال هذا الكود من قبل");
            }
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "validateBarcodeBlur");
        }
    }

    public void validationCodeAndNickname() {
        try {
            initializeSeachObjet(itemDataEntity.getCode(), itemDataEntity.getNickName(), itemDataEntity.getId());
            if (itemDataEntity.getCode() != null && !itemDataEntity.getCode().isEmpty()) {
                if (invItemService.validateCodeFromView(invItemValidate)) {
                    errorMessage.append(getUserData().getUserDDs().get("UNIQE_SERIAL"));
                }
            }
//            if (itemDataEntity.getNickName() != null && !itemDataEntity.getNickName().isEmpty()) {
//                if (invItemService.validateNickName(invItemValidate)) {
//                    errorMessage.append("الكود المختصر مكرر");
//                }
//            }
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "validationCodeAndNickname");
        }
    }

    public void initializeSeachObjet(String code, String NichName, Integer itemId) {
        try {
            invItemValidate = new InvItem();

            invItemValidate.setCode(code);
            invItemValidate.setBranchId(getUserData().getUserBranch());
            invItemValidate.setNickname(NichName);
            invItemValidate.setId(itemId);
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "initializeSeachObjet");
        }
    }

    public void setLastCost(InvItem invItem) {
        try {
            invItem.setLastCost(invPurchaseInvoiceDetailsService.getLastCostItem(invItem.getId(), getUserData().getGlYear(), getUserData().getUserBranch().getId()));
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "setLastCost");
        }
    }

    public void deleteBarcode() {
        try {
            setShowMessageGeneral(Boolean.FALSE);
            showMessageDetails = Boolean.TRUE;
            if (invBarcodeEntitySelection.getId() != null) {
                setDelete(Boolean.TRUE);
                getInvBarcodeDeleted().add(invBarcodeEntitySelection);
            }
            itemDataEntity.getInvBarcodeEntity().remove(invBarcodeEntitySelection);
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "deleteBarcode");
        }

    }

    @Override
    public String getScreenName() {
        // TODO Auto-generated method stub
        return null;
    }

    public String adddetails() {
        try {
            InvBarcodeEntity invBarcodeEntity = new InvBarcodeEntity();
            for (InvBarcodeEntity barcodeEntity : itemDataEntity.getInvBarcodeEntity()) {
                barcodeEntity.setMarkEdit(Boolean.FALSE);
            }
            invBarcodeEntity.setMarkEdit(Boolean.TRUE);
            invBarcodeEntity.setUnitId(getItemDataEntity().getUnitId());
            invBarcodeEntity.setPrice1(getItemDataEntity().getSellPrice());
            itemDataEntity.getInvBarcodeEntity().add(invBarcodeEntity);
            itemBarcodeMap.put(invBarcodeEntity.getCode(), invBarcodeEntity);
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "adddetails");
        }
        return "";
    }

    public void print() {

        try {
            if (itemDataEntity != null) {
                itemDataEntityReportList = new ArrayList<>();
                itemDataEntityReportList.add(itemDataEntity);
            }
            fillReport(prepareReport(), getUserData().getReportPath() + "itemBarcode.jasper", itemDataEntityReportList, "pdf");
        } catch (JRException ex) {
            Logger.getLogger(invItemDataFormMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(invItemDataFormMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HashMap prepareReport() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            HashMap hashMap = new HashMap();

//            hashMap.put("PrintedBy", getUserData().getUser().getName());
//            hashMap.put("Branch", getUserData().getUserBranch().getNameAr());
            hashMap.put("companyName", getUserData().getCompany().getName());

//            if (isFileExist(getUserData().getImageReportPath())) {
//                hashMap.put("companyImage", getUserData().getImageReportPath());
//            } else {
//                hashMap.put("companyImage", null);
//            }
            //  hashMap.put("reportDate", new Date());
            return hashMap;
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "prepareReport");
            return null;
        }
    }

    public void onCellEdit(CellEditEvent event) {
        try {
            Object oldValue = event.getOldValue();
            Object newValue = event.getNewValue();
            String column_name = event.getColumn().getClientId();
            String[] parts = column_name.split("form:barcodeTable:0:");
            String part1 = parts[0];
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "onCellEdit");
        }

    }

    public List<InvGroup> completeInvGroups(String query) {
        List<InvGroup> invGroupsFiltered = new ArrayList<>();
        try {
            List<InvGroup> groups = invGroupList;

            if (query == null || query.trim().equals("")) {
                invGroupConverter = new InvGroupConverter(groups);
                return groups;
            }

            String nameAr;
            Integer code;
            InvGroup group;
            for (int i = 0; i < invGroupList.size(); i++) {
                group = groups.get(i);
                nameAr = group.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase()) && !invGroupsFiltered.contains(group)) {
                    invGroupsFiltered.add(group);
                }

                code = group.getCode();
                if (code != null && String.valueOf(code).contains(query) && !invGroupsFiltered.contains(group)) {
                    invGroupsFiltered.add(group);
                }
            }

            invGroupConverter = new InvGroupConverter(invGroupsFiltered);
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "completeInvGroups");
        }

        return invGroupsFiltered;

    }

    public List<Symbol> completeUnits(String query) {
        return AutoComplete.completeSymbol(query, unitList, unitConverter);
    }

    public List<Symbol> completePaintColor(String query) {
        return AutoComplete.completeSymbol(query, paintColorList, paintColorConverter);
    }

    public List<Symbol> completeAddon1(String query) {
        return AutoComplete.completeSymbol(query, addon1List, typeCatConverter);
    }

    public List<Symbol> completeAddon2(String query) {
        return AutoComplete.completeSymbol(query, addon2List, type2CatConverter);
    }

    public List<Symbol> completeSize(String query) {
        return AutoComplete.completeSymbol(query, sizeList, sizeConverter);
    }

    public List<Symbol> completeSorting(String query) {
        return AutoComplete.completeSymbol(query, SortingList, SortingConverter);
    }

    public List<Symbol> completeCartonsNumber(String query) {
        return AutoComplete.completeSymbol(query, cartonsNumberList, cartonsNumberConverter);
    }

    public List<Symbol> completeCartonsCapacity(String query) {
        return AutoComplete.completeSymbol(query, cartonCapacityList, cartonCapacityConverter);
    }

    public List<Symbol> completeMeterNumber(String query) {
        return AutoComplete.completeSymbol(query, metersNumberList, metersNumberConverter);
    }

    public List<Symbol> completeTypeCat(String query) {
        List<Symbol> list = new ArrayList<>();
        try {
            List<Symbol> symbols = typeCatList;
            if (query == null || query.trim().equals("")) {
                typeCatConverter = new SymbolConverter(symbols);
                return symbols;
            }

            String nameAr;
            Integer code;
            Symbol symbol;
            for (int i = 0; i < typeCatList.size(); i++) {
                symbol = symbols.get(i);
                nameAr = symbol.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase()) && !list.contains(symbol)) {
                    list.add(symbol);
                }

                code = symbol.getId();
                if (code != null && String.valueOf(code).contains(query) && !list.contains(symbol)) {
                    list.add(symbol);
                }
            }

            typeCatConverter = new SymbolConverter(list);
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "completeTypeCat");
        }
        return list;
    }

    public List<InvOrganizationSite> completeOrganizationSite(String query) {
        List<InvOrganizationSite> filteredSuppliers = new ArrayList<>();
        try {
            List<InvOrganizationSite> supplierList = invOrganizationSiteList;
            if (query == null || query.trim().equals("")) {

                organizationSiteConvertor = new InvOrganizationSiteConverter(supplierList);

                return supplierList;
            }

            String nameAr;
            String code;
            InvOrganizationSite supplierFilter;
            for (int i = 0; i < invOrganizationSiteList.size(); i++) {
                supplierFilter = supplierList.get(i);
                nameAr = supplierFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredSuppliers.contains(supplierFilter)) {
                        filteredSuppliers.add(supplierFilter);
                    }
                }

                code = supplierFilter.getCode();
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredSuppliers.contains(supplierFilter)) {
                        filteredSuppliers.add(supplierFilter);
                    }
                }
            }

            organizationSiteConvertor = new InvOrganizationSiteConverter(filteredSuppliers);
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "completeOrganizationSite");
        }
        return filteredSuppliers;
    }

    public String cancel() {
        try {
            if (getUri().contains("itemsdataformdis")) {
                exit("../itemsdata/itemsdatalist.xhtml");
            } else {
                exit("../itemsdata/itemsdatalistdis.xhtml");
            }
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "cancel");
        }
        return "";

    }

    public String exit() {
        // TODO Auto-generated method stub
        return null;
    }

    public ItemDataEntity getItemDataEntity() {
        return itemDataEntity;
    }

    public void setItemDataEntity(ItemDataEntity itemDataEntity) {
        this.itemDataEntity = itemDataEntity;
    }

    public List<ItemDataEntity> getItemDataEntityList() {
        return itemDataEntityList;
    }

    public void setItemDataEntityList(List<ItemDataEntity> itemDataEntityList) {
        this.itemDataEntityList = itemDataEntityList;
    }

    public InvItem getInvItem() {
        return invItem;
    }

    public void setInvItem(InvItem invItem) {
        this.invItem = invItem;
    }

    public List<InvItem> getItemDataByBranch() {
        return itemDataByBranch;
    }

    public void setItemDataByBranch(List<InvItem> itemDataByBranch) {
        this.itemDataByBranch = itemDataByBranch;
    }

    public Integer getItemDataId() {
        return itemDataId;
    }

    public void setItemDataId(Integer organizationSiteId) {
        this.itemDataId = organizationSiteId;
    }

    public Integer getSelectedCompanyId() {
        return selectedCompanyId;
    }

    public void setSelectedCompanyId(Integer selectedCompanyId) {
        this.selectedCompanyId = selectedCompanyId;
    }

    public boolean isShowMessage() {
        return showMessage;
    }

    public void setShowMessage(boolean showMessage) {
        this.showMessage = showMessage;
    }

    public List<InvGroup> getInvGroupList() {
        return invGroupList;
    }

    public void setInvGroupList(List<InvGroup> invGroupList) {
        this.invGroupList = invGroupList;
    }

    public List<InvOrganizationSite> getInvOrganizationSiteList() {
        return invOrganizationSiteList;
    }

    public void setInvOrganizationSiteList(List<InvOrganizationSite> invOrganizationSiteList) {
        this.invOrganizationSiteList = invOrganizationSiteList;
    }

    public List<Symbol> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<Symbol> brandList) {
        this.brandList = brandList;
    }

    public List<Symbol> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<Symbol> countryList) {
        this.countryList = countryList;
    }

    public List<Symbol> getPaintColorList() {
        return paintColorList;
    }

    public void setPaintColorList(List<Symbol> paintColorList) {
        this.paintColorList = paintColorList;
    }

    public List<Symbol> getEnamelColorList() {
        return enamelColorList;
    }

    public void setEnamelColorList(List<Symbol> enamelColorList) {
        this.enamelColorList = enamelColorList;
    }

    public List<Symbol> getStoneList() {
        return stoneList;
    }

    public void setStoneList(List<Symbol> stoneList) {
        this.stoneList = stoneList;
    }

    /**
     * @return the unitList
     */
    public List<Symbol> getUnitList() {
        return unitList;
    }

    /**
     * @param unitList the unitList to set
     */
    public void setUnitList(List<Symbol> unitList) {
        this.unitList = unitList;
    }

    public Boolean getShowMessageDetails() {
        return showMessageDetails;
    }

    public void setShowMessageDetails(Boolean showMessageDetails) {
        this.showMessageDetails = showMessageDetails;
    }

    public InvBarcodeEntity getInvBarcodeEntitySelection() {
        return invBarcodeEntitySelection;
    }

    public void setInvBarcodeEntitySelection(InvBarcodeEntity invBarcodeEntitySelection) {
        this.invBarcodeEntitySelection = invBarcodeEntitySelection;
    }

    public InvBarcodeEntity getBarcodeEntity() {
        return barcodeEntity;
    }

    public void setBarcodeEntity(InvBarcodeEntity barcodeEntity) {
        this.barcodeEntity = barcodeEntity;
    }

    public List<InvBarcodeEntity> getBarcodeEntityList() {
        return barcodeEntityList;
    }

    public void setBarcodeEntityList(List<InvBarcodeEntity> barcodeEntityList) {
        this.barcodeEntityList = barcodeEntityList;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the showMessageGeneral
     */
    public Boolean getShowMessageGeneral() {
        return showMessageGeneral;
    }

    /**
     * @param showMessageGeneral the showMessageGeneral to set
     */
    public void setShowMessageGeneral(Boolean showMessageGeneral) {
        this.showMessageGeneral = showMessageGeneral;
    }

    /**
     * @return the invBarcodeDeleted
     */
    public List<InvBarcodeEntity> getInvBarcodeDeleted() {
        return invBarcodeDeleted;
    }

    /**
     * @param invBarcodeDeleted the invBarcodeDeleted to set
     */
    public void setInvBarcodeDeleted(List<InvBarcodeEntity> invBarcodeDeleted) {
        this.invBarcodeDeleted = invBarcodeDeleted;
    }

    /**
     * @return the delete
     */
    public Boolean getDelete() {
        return delete;
    }

    /**
     * @param delete the delete to set
     */
    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public List<InvBarcode> getBarcodeList() {
        return barcodeList;
    }

    public void setBarcodeList(List<InvBarcode> barcodeList) {
        this.barcodeList = barcodeList;
    }

    public ItemDataEntity getItemDataEntitySelected() {
        return itemDataEntitySelected;
    }

    public void setItemDataEntitySelected(ItemDataEntity itemDataEntitySelected) {
        this.itemDataEntitySelected = itemDataEntitySelected;
    }

    public List<InvBarcode> getInvBarcodesList() {
        return invBarcodesList;
    }

    public void setInvBarcodesList(List<InvBarcode> invBarcodesList) {
        this.invBarcodesList = invBarcodesList;
    }

    public List<InvBarcode> getInvBarcodeDeletedList() {
        return invBarcodeDeletedList;
    }

    public void setInvBarcodeDeletedList(List<InvBarcode> invBarcodeDeletedList) {
        this.invBarcodeDeletedList = invBarcodeDeletedList;
    }

    public List<Symbol> getTypeCatList() {
        return typeCatList;
    }

    public void setTypeCatList(List<Symbol> typeCatList) {
        this.typeCatList = typeCatList;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getShowImage() {
        return showImage;
    }

    public void setShowImage(String showImage) {
        this.showImage = showImage;
    }

    public StreamedContent getDbImage() {
        return dbImage;
    }

    public void setDbImage(StreamedContent dbImage) {
        this.dbImage = dbImage;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getUploadedImage() {
        return uploadedImage;
    }

    public void setUploadedImage(String uploadedImage) {
        this.uploadedImage = uploadedImage;
    }

    public FileUploadController getFileUploadController() {
        return fileUploadController;
    }

    public void setFileUploadController(FileUploadController fileUploadController) {
        this.fileUploadController = fileUploadController;
    }

    public FileUploadEvent getFileUploadEvent() {
        return fileUploadEvent;
    }

    public void setFileUploadEvent(FileUploadEvent fileUploadEvent) {
        this.fileUploadEvent = fileUploadEvent;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public List<QuantityItemsStore> getQuantityItemsStoreList() {
        return quantityItemsStoreList;
    }

    public void setQuantityItemsStoreList(List<QuantityItemsStore> quantityItemsStoreList) {
        this.quantityItemsStoreList = quantityItemsStoreList;
    }

    public BigDecimal getQtySummition() {
        return qtySummition;
    }

    public void setQtySummition(BigDecimal qtySummition) {
        this.qtySummition = qtySummition;
    }

    public InvGroupConverter getInvGroupConverter() {
        return invGroupConverter;
    }

    public void setInvGroupConverter(InvGroupConverter invGroupConverter) {
        this.invGroupConverter = invGroupConverter;
    }

    public SymbolConverter getUnitConverter() {
        return unitConverter;
    }

    public void setUnitConverter(SymbolConverter unitConverter) {
        this.unitConverter = unitConverter;
    }

    public InvOrganizationSiteConverter getOrganizationSiteConvertor() {
        return organizationSiteConvertor;
    }

    public void setOrganizationSiteConvertor(InvOrganizationSiteConverter organizationSiteConvertor) {
        this.organizationSiteConvertor = organizationSiteConvertor;
    }

    public CompanyService getCompanyService() {
        return companyService;
    }

    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    public SymbolConverter getTypeCatConverter() {
        return typeCatConverter;
    }

    public void setTypeCatConverter(SymbolConverter typeCatConverter) {
        this.typeCatConverter = typeCatConverter;
    }

    /**
     * @return the settingType
     */
    public Integer getSettingType() {
        return settingType;
    }

    /**
     * @param settingType the settingType to set
     */
    public void setSettingType(Integer settingType) {
        this.settingType = settingType;
    }

    /**
     * @return the addon1List
     */
    public List<Symbol> getAddon1List() {
        return addon1List;
    }

    /**
     * @param addon1List the addon1List to set
     */
    public void setAddon1List(List<Symbol> addon1List) {
        this.addon1List = addon1List;
    }

    /**
     * @return the addon2List
     */
    public List<Symbol> getAddon2List() {
        return addon2List;
    }

    /**
     * @param addon2List the addon2List to set
     */
    public void setAddon2List(List<Symbol> addon2List) {
        this.addon2List = addon2List;
    }

    /**
     * @return the paintColorConverter
     */
    public SymbolConverter getPaintColorConverter() {
        return paintColorConverter;
    }

    /**
     * @param paintColorConverter the paintColorConverter to set
     */
    public void setPaintColorConverter(SymbolConverter paintColorConverter) {
        this.paintColorConverter = paintColorConverter;
    }

    /**
     * @return the type2CatConverter
     */
    public SymbolConverter getType2CatConverter() {
        return type2CatConverter;
    }

    /**
     * @param type2CatConverter the type2CatConverter to set
     */
    public void setType2CatConverter(SymbolConverter type2CatConverter) {
        this.type2CatConverter = type2CatConverter;
    }

    /**
     * @return the itemDataEntityReportList
     */
    public List<ItemDataEntity> getItemDataEntityReportList() {
        return itemDataEntityReportList;
    }

    /**
     * @param itemDataEntityReportList the itemDataEntityReportList to set
     */
    public void setItemDataEntityReportList(List<ItemDataEntity> itemDataEntityReportList) {
        this.itemDataEntityReportList = itemDataEntityReportList;
    }

    /**
     * @return the sizeList
     */
    public List<Symbol> getSizeList() {
        return sizeList;
    }

    /**
     * @param sizeList the sizeList to set
     */
    public void setSizeList(List<Symbol> sizeList) {
        this.sizeList = sizeList;
    }

    /**
     * @return the SortingList
     */
    public List<Symbol> getSortingList() {
        return SortingList;
    }

    /**
     * @param SortingList the SortingList to set
     */
    public void setSortingList(List<Symbol> SortingList) {
        this.SortingList = SortingList;
    }

    /**
     * @return the cartonsNumberList
     */
    public List<Symbol> getCartonsNumberList() {
        return cartonsNumberList;
    }

    /**
     * @param cartonsNumberList the cartonsNumberList to set
     */
    public void setCartonsNumberList(List<Symbol> cartonsNumberList) {
        this.cartonsNumberList = cartonsNumberList;
    }

    /**
     * @return the cartonCapacityList
     */
    public List<Symbol> getCartonCapacityList() {
        return cartonCapacityList;
    }

    /**
     * @param cartonCapacityList the cartonCapacityList to set
     */
    public void setCartonCapacityList(List<Symbol> cartonCapacityList) {
        this.cartonCapacityList = cartonCapacityList;
    }

    /**
     * @return the metersNumberList
     */
    public List<Symbol> getMetersNumberList() {
        return metersNumberList;
    }

    /**
     * @param metersNumberList the metersNumberList to set
     */
    public void setMetersNumberList(List<Symbol> metersNumberList) {
        this.metersNumberList = metersNumberList;
    }

    /**
     * @return the sizeConverter
     */
    public SymbolConverter getSizeConverter() {
        return sizeConverter;
    }

    /**
     * @param sizeConverter the sizeConverter to set
     */
    public void setSizeConverter(SymbolConverter sizeConverter) {
        this.sizeConverter = sizeConverter;
    }

    /**
     * @return the SortingConverter
     */
    public SymbolConverter getSortingConverter() {
        return SortingConverter;
    }

    /**
     * @param SortingConverter the SortingConverter to set
     */
    public void setSortingConverter(SymbolConverter SortingConverter) {
        this.SortingConverter = SortingConverter;
    }

    /**
     * @return the cartonsNumberConverter
     */
    public SymbolConverter getCartonsNumberConverter() {
        return cartonsNumberConverter;
    }

    /**
     * @param cartonsNumberConverter the cartonsNumberConverter to set
     */
    public void setCartonsNumberConverter(SymbolConverter cartonsNumberConverter) {
        this.cartonsNumberConverter = cartonsNumberConverter;
    }

    /**
     * @return the cartonCapacityConverter
     */
    public SymbolConverter getCartonCapacityConverter() {
        return cartonCapacityConverter;
    }

    /**
     * @param cartonCapacityConverter the cartonCapacityConverter to set
     */
    public void setCartonCapacityConverter(SymbolConverter cartonCapacityConverter) {
        this.cartonCapacityConverter = cartonCapacityConverter;
    }

    /**
     * @return the metersNumberConverter
     */
    public SymbolConverter getMetersNumberConverter() {
        return metersNumberConverter;
    }

    /**
     * @param metersNumberConverter the metersNumberConverter to set
     */
    public void setMetersNumberConverter(SymbolConverter metersNumberConverter) {
        this.metersNumberConverter = metersNumberConverter;
    }

}
