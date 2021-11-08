package com.toby.invItemData;

import com.toby.businessservice.CompanyService;
import com.toby.businessservice.InvGroupService;
import com.toby.businessservice.InvItemService;
import com.toby.businessservice.InvPurchaseInvoiceDetailService;
import com.toby.businessservice.ItemsDataViewService;
import com.toby.businessservice.OrganizationSiteService;
import com.toby.businessservice.QuantityItemsStoreService;
import com.toby.businessservice.SymbolService;
import com.toby.converter.InvGroupDTOConverter;
import com.toby.converter.InvOrganizationSiteConverter;
import com.toby.converter.SymbolConverter;
import com.toby.converter.SymbolDTOConverter;
import com.toby.dto.InvBarcodeDTO;
import com.toby.dto.InvGroupDTO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.SymbolDTO;
import com.toby.entity.InvItem;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.InventorySetup;
import com.toby.entity.Symbol;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import com.toby.uploadfile.FileUploadController;
import com.toby.views.QuantityItemsStore;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import com.toby.businessservice.InventorySetupService;
import com.toby.businessservice.ProGroupService;
import com.toby.converter.ProGroupDTOConverter;
import com.toby.dto.ProGroupDTO;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;

@Named(value = "itemDataFormMB")
@ViewScoped
public class invItemDataFormMB extends BaseFormBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer determinePrice;
    private InvItemDTO itemDataEntity;
    private InvItemDTO itemDataEntitySelected;
    private List<InvItemDTO> itemDataEntityReportList;
    private List<InvItemDTO> itemDataEntityList;
    private Boolean showMessageDetails = Boolean.FALSE;
    private Boolean showMessageGeneral = Boolean.FALSE;
    private String fileName;
    private String showImage;
    private String uploadedImage;

    private boolean showMessage = Boolean.FALSE;
    private Boolean delete = Boolean.FALSE;

    private List<InvGroupDTO> invGroupList;
    private InvGroupDTOConverter invGroupDTOConverter;
    private List<SymbolDTO> unitList;
    private List<ProGroupDTO> proGroupDTOList;
    private SymbolConverter unitConverter;
    private List<InvOrganizationSite> invOrganizationSiteList;
    private InvOrganizationSiteConverter organizationSiteConvertor;
    private List<SymbolDTO> brandList;
    private List<SymbolDTO> countryList;
    private List<SymbolDTO> paintColorList;
    private List<SymbolDTO> enamelColorList;
    private List<SymbolDTO> stoneList;
    private List<SymbolDTO> typeCatList;
    private List<SymbolDTO> addon1List;
    private List<SymbolDTO> addon2List;
    private SymbolDTOConverter typeCatConverter;
    private SymbolDTOConverter unitDTOConverter;
    private ProGroupDTOConverter proGroupDTOConverter;
    private SymbolConverter paintColorConverter;

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

    // barcode
    private InvBarcodeDTO barcodeEntity;
    private List<InvBarcodeDTO> barcodeEntityList;
    private InvBarcodeDTO invBarcodeEntitySelection;
    private List<InvBarcodeDTO> invBarcodesList;
    private List<QuantityItemsStore> quantityItemsStoreList;
    private Integer itemDataId;

    @EJB
    private InvItemService invItemService;

    @EJB
    private InvGroupService invGroupService;
    @EJB
    private OrganizationSiteService organizationSiteService;

    @EJB
    private SymbolService symbolService;
    
    @EJB
    private ProGroupService proGroupService;

    @EJB
    private CompanyService companyService;

    @EJB
    private InvPurchaseInvoiceDetailService invPurchaseInvoiceDetailsService;

    @EJB
    QuantityItemsStoreService quantityItemsStoreService;
    @EJB
    InventorySetupService inventorySetupService;

    @EJB
    private ItemsDataViewService itemsDataViewService;

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

//                    if (itemDataId != null) {
//                        quantityItemsStoreList = quantityItemsStoreService.findItemsForBranchList(itemDataId, branchId);
//
//                        qtySummition = BigDecimal.ZERO;
//                        for (QuantityItemsStore itemsStore : quantityItemsStoreList) {
//                            qtySummition = (qtySummition.add(itemsStore.getQty() != null ? itemsStore.getQty().setScale(2) : BigDecimal.ZERO)).setScale(2);
//                        }
//                    }
                    itemDataEntity = itemsDataViewService.findInvItem(itemDataId, getUserlogin());
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
//            invItem = new InvItem();
//            setItemDataEntity(new InvItemDTO());
//            itemDataEntityList = new ArrayList<>();
//            quantityItemsStoreList = new ArrayList<>();
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "initObjs");
        }
    }

    private void fillLists() {
        try {
//             invGroupList = invGroupService.getallInvGroupByBranchId(branchId); // groups
//            invGroupList = invGroupService.getAllSubInvGroupByBranchId(null, branchId);
//            invGroupConverter = new InvGroupConverter(invGroupList);

            unitList = symbolService.getUnitCompanyId(companyId); //units
            unitDTOConverter = new SymbolDTOConverter(unitList);
            
            proGroupDTOList = proGroupService.getallProGroupByBranchIdDTO(getUserData().getUserlogin());
            setProGroupDTOConverter(new ProGroupDTOConverter(proGroupDTOList));

            invOrganizationSiteList = organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(branchId, true, 1); //suppliers
            organizationSiteConvertor = new InvOrganizationSiteConverter(invOrganizationSiteList);

//            stoneList = symbolService.getStoneByCompanyId(companyId);
//            addon1List = symbolService.getAddon1ByCompanyId(companyId);
//            addon2List = symbolService.getAddon2ByCompanyId(companyId);
//            paintColorList = symbolService.getPaintColorByCompanyId(companyId);
//            paintColorConverter = new SymbolConverter(paintColorList);
//            enamelColorList = symbolService.getEnamelColorByCompanyId(companyId);
//            countryList = symbolService.getCountryByCompanyId(companyId);
//            brandList = symbolService.getBrandByCompanyId(companyId);
//
//            typeCatList = symbolService.getTypeCatByCompanyId(companyId);
//            typeCatConverter = new SymbolConverter(typeCatList);
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "fillLists");
        }

    }

    InvItemDTO mapInvItemDataToItemDataEntityForm(InvItem invItem) {
        try {
            setItemDataEntitySelected(new InvItemDTO());
            itemBarcodeMap = new HashMap();
            if (invItem != null) {
//                getItemDataEntitySelected().setId(invItem.getId());
//                getItemDataEntitySelected().setBranchId(invItem.getBranchId());
//                getItemDataEntitySelected().setBrandId(invItem.getBrandId());
//                getItemDataEntitySelected().setCode(invItem.getCode());
//                getItemDataEntitySelected().setCompanyId(invItem.getCompanyId());
//                getItemDataEntitySelected().setContractPrice(invItem.getContractPrice());
//                getItemDataEntitySelected().setCostAverage(invItem.getCostAverage());
//                getItemDataEntitySelected().setCreatedBy(invItem.getCreatedBy());
//                getItemDataEntitySelected().setCreationDate(invItem.getCreationDate());
//                getItemDataEntitySelected().setDateCreateCat(invItem.getCreationDate());
//                getItemDataEntitySelected().setEnamelColorId(invItem.getEnamelColor());
//                getItemDataEntitySelected().setGroupId(invItem.getGroupId());
//                getItemDataEntitySelected().setGroupName(invItem.getGroupId() != null ? (invItem.getGroupId().getName() != null ? invItem.getGroupId().getName() : null) : null);
//                getItemDataEntitySelected().setImage(invItem.getImage() != null ? invItem.getImage() : null);
//                setFileName(invItem.getImage() != null ? invItem.getImage() : "1.png");
//                setShowImage(invItem.getImage() != null ? fileUploadController.getDestination().concat(invItem.getImage()) : fileUploadController.getDestination() + "1.png");
//                setUploadedImage(invItem.getImage());
//
//                getItemDataEntitySelected().setIsPurchase(invItem.getIspurchase());
//                getItemDataEntitySelected().setIsSell(invItem.getIssell());
//                getItemDataEntitySelected().setLastCost(invItem.getLastCost() != null ? invItem.getLastCost() : BigDecimal.ZERO);
//                getItemDataEntitySelected().setMarkEdit(invItem.getMarkEdit());
//                getItemDataEntitySelected().setMaxmumAmount(invItem.getMaxmumAmount());
//                getItemDataEntitySelected().setMinimumAmount(invItem.getMinimumAmount());
//                getItemDataEntitySelected().setModifiedBy(invItem.getModifiedBy());
//                getItemDataEntitySelected().setModifiedDate(invItem.getModificationDate());
//                getItemDataEntitySelected().setName(invItem.getName());
//                getItemDataEntitySelected().setNameen(invItem.getNameen());
//                getItemDataEntitySelected().setNickName(invItem.getNickname());
//                getItemDataEntitySelected().setOpeningCost(invItem.getOpeningCost());
//                getItemDataEntitySelected().setOriginCountryId(invItem.getOriginCountry());
//                getItemDataEntitySelected().setPaintColor(invItem.getPaintColor());
//                getItemDataEntitySelected().setWeightPackage(invItem.getWeightPackage());
//                getItemDataEntitySelected().setRemarks(invItem.getRemarks());
//                getItemDataEntitySelected().setSellPrice(invItem.getSellPrice());
//                getItemDataEntitySelected().setStatusCat(invItem.getStatusCat());
//                getItemDataEntitySelected().setStoneId(invItem.getStone());
//                getItemDataEntitySelected().setStorageLocation(invItem.getStorageLocation());
//                getItemDataEntitySelected().setStoresQuantity(invItem.getStoresQuality());
//                getItemDataEntitySelected().setTypeCat(invItem.getTypeCat());
//                getItemDataEntitySelected().setUndirectCost(invItem.getUndirectCost());
//                getItemDataEntitySelected().setUnitId(invItem.getUnitId());
//                getItemDataEntitySelected().setSiteId(invItem.getSiteId());
//                getItemDataEntitySelected().setLength(invItem.getLength());
//                getItemDataEntitySelected().setHeight(invItem.getHeight());
//                getItemDataEntitySelected().setWeight(invItem.getWeight());
//                getItemDataEntitySelected().setWidth(invItem.getWidth());
//                getItemDataEntitySelected().setAddon1(invItem.getAddon1());
//                getItemDataEntitySelected().setAddon2(invItem.getAddon2());

            }
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "mapInvItemDataToItemDataEntityForm");
        }

        return getItemDataEntitySelected();
    }

    public void upload(FileUploadEvent event) {
        try {
            fileUploadEvent = event;
            imageName = fileUploadController.upload(event);
            setUploadedImage(imageName);
            setFileName(imageName);
            setShowImage(fileUploadController.getDestination().concat(imageName));
            getItemDataEntity().setImage(imageName);
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "upload");
        }

    }

    public void copyfile() throws IOException {
        try {
            fileUploadController.copyFile(imageName, fileUploadEvent.getFile().getInputstream(),
                    fileUploadController.getDestination());
            getItemDataEntity().setImage(imageName);
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
//            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
//            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
//            context.getSessionMap().replace("ScreenMode", "Add");
//            setScreenMode("Add");
//
//            init();
            getItemDataEntity().setId(null);
            getItemDataEntity().setCode(null);
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
            setItemDataEntity(new InvItemDTO());

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
                if (getItemDataEntity().getSiteId() != null) {
                    getItemDataEntity().setCode(invItemService.findMaxItemCode(getSettingType(), getItemDataEntity().getSiteId().getId(), getItemDataEntity().getSiteId().getCode()) + "");
                }
            } else if (getSettingType() == 1) {
                if (getItemDataEntity().getGroupId() != null) {
                    getItemDataEntity().setCode(invItemService.findMaxItemCode(getSettingType(), getItemDataEntity().getGroupId().getId(), getItemDataEntity().getSiteId().getCode()) + "");
                }
            }
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "findMaxItemCode");
        }

    }

    public void saveItem() {
        System.out.println("");
        if (itemDataEntity != null && itemDataEntity.getName() != null && getItemDataEntity().getSiteId().getId() != null) {
            itemDataEntity = itemsDataViewService.addItemsData(itemDataEntity, getUserData().getUser());

        }
    }

    @Override
    public String save() {
        System.out.println("");
        if (itemDataEntity != null && itemDataEntity.getName() != null) {
            itemDataEntity = itemsDataViewService.addItemsData(itemDataEntity, getUserData().getUser());
            savedMeesage(getUserData().getUserDDs().get("SAVED"));
        }
        return "";
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
            initializeSeachObjet(getItemDataEntity().getCode(), null, getItemDataEntity().getId());

            if (invItemService.validateCodeFromView(invItemValidate)) {
                errorMessage.append(getUserData().getUserDDs().get("UNIQE_SERIAL"));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), errorMessage.toString()));
            }
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "validateBlur");
        }

    }

    public void validateBarcodeBlur(InvBarcodeDTO invBarcodeEntitySelectionTable) {
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
            initializeSeachObjet(getItemDataEntity().getCode(), getItemDataEntity().getNickname(), getItemDataEntity().getId());
            if (invItemService.validateCodeFromView(invItemValidate)) {
                errorMessage.append(getUserData().getUserDDs().get("UNIQE_SERIAL"));
            }
            if (!itemDataEntity.getNickname().isEmpty()) {
                if (invItemService.validateNickName(invItemValidate)) {
                    errorMessage.append("الكود المختصر مكرر");
                }
            }
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

    @Override
    public String getScreenName() {
        // TODO Auto-generated method stub
        return null;
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

    // barcode 
    public void deleteBarcode() {
        try {
            setShowMessageGeneral(Boolean.FALSE);
            setShowMessageDetails(Boolean.TRUE);
            if (getInvBarcodeEntitySelection().getId() != null) {
                setDelete(Boolean.TRUE);
                itemDataEntity.getInvBarcodeDeletedList().add(getInvBarcodeEntitySelection());
            }
            itemDataEntity.getInvBarcodeEntity().remove(getInvBarcodeEntitySelection());
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "deleteBarcode");
        }

    }

    public String adddetails() {
        try {
            InvBarcodeDTO invBarcodeEntity = new InvBarcodeDTO();
            for (InvBarcodeDTO barcodeEntity : itemDataEntity.getInvBarcodeEntity()) {
                barcodeEntity.setMarkEdit(Boolean.FALSE);
            }
            invBarcodeEntity.setMarkEdit(Boolean.TRUE);
            invBarcodeEntity.setIndex(getIndex());
            itemDataEntity.getInvBarcodeEntity().add(invBarcodeEntity);
            itemBarcodeMap.put(invBarcodeEntity.getCode(), invBarcodeEntity);
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "adddetails");
        }
        return "";
    }

    public List<InvGroupDTO> completeInvGroups(String query) {
        List<InvGroupDTO> invGroupsFiltered = new ArrayList<>();
        try {
            List<InvGroupDTO> groups = invGroupList;

            if (query == null || query.trim().equals("")) {
                setInvGroupDTOConverter(new InvGroupDTOConverter(groups));
                return groups;
            }

            String nameAr;
            Integer code;
            InvGroupDTO group;
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

            invGroupDTOConverter = new InvGroupDTOConverter(invGroupsFiltered);
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "completeInvGroups");
        }

        return invGroupsFiltered;

    }

       public List<ProGroupDTO> completeproGroups(String query) {
        List<ProGroupDTO> invGroupsFiltered = new ArrayList<>();
        try {
            List<ProGroupDTO> groups = proGroupDTOList;

            if (query == null || query.trim().equals("")) {
                setProGroupDTOConverter(new ProGroupDTOConverter(groups));
                return groups;
            }

            String nameAr;
            Integer code;
            ProGroupDTO group;
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

            proGroupDTOConverter = new ProGroupDTOConverter(invGroupsFiltered);
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "completeInvGroups");
        }

        return invGroupsFiltered;

    }

    
    public List<SymbolDTO> completeUnits(String query) {
        List<SymbolDTO> list = new ArrayList<>();
        try {
            List<SymbolDTO> symbols = unitList;
            if (query == null || query.trim().equals("")) {
                unitDTOConverter = new SymbolDTOConverter(symbols);
                return symbols;
            }

            String nameAr;
            Integer code;
            SymbolDTO symbol;
            for (int i = 0; i < unitList.size(); i++) {
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

            unitDTOConverter = new SymbolDTOConverter(list);
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "completeUnits");
        }
        return list;
    }

    public List<Symbol> completePaintColor(String query) {
        List<Symbol> list = new ArrayList<>();
//        try {
//            List<Symbol> symbols = paintColorList;
//            if (query == null || query.trim().equals("")) {
//                paintColorConverter = new SymbolConverter(symbols);
//                return symbols;
//            }
//
//            String nameAr;
//            Integer code;
//            Symbol symbol;
//            for (int i = 0; i < paintColorList.size(); i++) {
//                symbol = symbols.get(i);
//                nameAr = symbol.getName();
//                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase()) && !list.contains(symbol)) {
//                    list.add(symbol);
//                }
//
//                code = symbol.getId();
//                if (code != null && String.valueOf(code).contains(query) && !list.contains(symbol)) {
//                    list.add(symbol);
//                }
//            }
//
//            paintColorConverter = new SymbolConverter(list);
//        } catch (Exception e) {
//            saveError(e, "invItemDataFormMB", "completeUnits");
//        }
        return list;
    }

    public List<SymbolDTO> completeAddon1(String query) {
        List<SymbolDTO> list = new ArrayList<>();
        try {
            List<SymbolDTO> symbols = addon1List;
            if (query == null || query.trim().equals("")) {
                setTypeCatConverter(new SymbolDTOConverter(symbols));
                return symbols;
            }

            String nameAr;
            Integer code;
            SymbolDTO symbol;
            for (int i = 0; i < addon1List.size(); i++) {
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

            setTypeCatConverter(new SymbolDTOConverter(list));
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "completeAddon1");
        }
        return list;
    }

    public List<SymbolDTO> completeAddon2(String query) {
        List<SymbolDTO> list = new ArrayList<>();
        try {
            List<SymbolDTO> symbols = addon2List;
            if (query == null || query.trim().equals("")) {
                setTypeCatConverter(new SymbolDTOConverter(symbols));
                return symbols;
            }

            String nameAr;
            Integer code;
            SymbolDTO symbol;
            for (int i = 0; i < addon2List.size(); i++) {
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

            setTypeCatConverter(new SymbolDTOConverter(list));
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "completeAddon2");
        }
        return list;
    }

    public List<Symbol> completeTypeCat(String query) {
        List<Symbol> list = new ArrayList<>();
//        try {
//            List<Symbol> symbols = typeCatList;
//            if (query == null || query.trim().equals("")) {
//                setTypeCatConverter(new SymbolConverter(symbols));
//                return symbols;
//            }
//
//            String nameAr;
//            Integer code;
//            Symbol symbol;
//            for (int i = 0; i < typeCatList.size(); i++) {
//                symbol = symbols.get(i);
//                nameAr = symbol.getName();
//                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase()) && !list.contains(symbol)) {
//                    list.add(symbol);
//                }
//
//                code = symbol.getId();
//                if (code != null && String.valueOf(code).contains(query) && !list.contains(symbol)) {
//                    list.add(symbol);
//                }
//            }
//
//            setTypeCatConverter(new SymbolConverter(list));
//        } catch (Exception e) {
//            saveError(e, "invItemDataFormMB", "completeTypeCat");
//        }
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
    
    public void print() {
        
        try {
            if(itemDataEntity != null){
                setItemDataEntityReportList(new ArrayList<>());
                getItemDataEntityReportList().add(itemDataEntity);
            }
            fillReport(prepareReport() , getUserData().getReportPath() + "itemBarcode.jasper", getItemDataEntityReportList(), "pdf");
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

    public String cancel() {
        try {
            exit("../itemsdata/itemsdatalist.xhtml");
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "cancel");
        }
        return "";

    }

    public String exit() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isShowMessage() {
        return showMessage;
    }

    public void setShowMessage(boolean showMessage) {
        this.showMessage = showMessage;
    }

    public List<InvGroupDTO> getInvGroupList() {
        return invGroupList;
    }

    public void setInvGroupList(List<InvGroupDTO> invGroupList) {
        this.invGroupList = invGroupList;
    }

    public List<InvOrganizationSite> getInvOrganizationSiteList() {
        return invOrganizationSiteList;
    }

    public void setInvOrganizationSiteList(List<InvOrganizationSite> invOrganizationSiteList) {
        this.invOrganizationSiteList = invOrganizationSiteList;
    }

    public List<SymbolDTO> getUnitList() {
        return unitList;
    }

    /**
     * @param unitList the unitList to set
     */
    public void setUnitList(List<SymbolDTO> unitList) {
        this.unitList = unitList;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Boolean getDelete() {
        return delete;
    }

    /**
     * @param delete the delete to set
     */
    public void setDelete(Boolean delete) {
        this.delete = delete;
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

    public BigDecimal getQtySummition() {
        return qtySummition;
    }

    public void setQtySummition(BigDecimal qtySummition) {
        this.qtySummition = qtySummition;
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
    public List<SymbolDTO> getAddon1List() {
        return addon1List;
    }

    /**
     * @param addon1List the addon1List to set
     */
    public void setAddon1List(List<SymbolDTO> addon1List) {
        this.addon1List = addon1List;
    }

    /**
     * @return the addon2List
     */
    public List<SymbolDTO> getAddon2List() {
        return addon2List;
    }

    /**
     * @param addon2List the addon2List to set
     */
    public void setAddon2List(List<SymbolDTO> addon2List) {
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
     * @return the itemDataEntity
     */
    public InvItemDTO getItemDataEntity() {
        if (itemDataEntity == null) {
            itemDataEntity = new InvItemDTO();
        }
        return itemDataEntity;
    }

    /**
     * @param itemDataEntity the itemDataEntity to set
     */
    public void setItemDataEntity(InvItemDTO itemDataEntity) {
        this.itemDataEntity = itemDataEntity;
    }

    /**
     * @return the itemDataEntitySelected
     */
    public InvItemDTO getItemDataEntitySelected() {
        return itemDataEntitySelected;
    }

    /**
     * @param itemDataEntitySelected the itemDataEntitySelected to set
     */
    public void setItemDataEntitySelected(InvItemDTO itemDataEntitySelected) {
        this.itemDataEntitySelected = itemDataEntitySelected;
    }

    /**
     * @return the determinePrice
     */
    public Integer getDeterminePrice() {
        return determinePrice;
    }

    /**
     * @param determinePrice the determinePrice to set
     */
    public void setDeterminePrice(Integer determinePrice) {
        this.determinePrice = determinePrice;
    }

    /**
     * @return the invGroupDTOConverter
     */
    public InvGroupDTOConverter getInvGroupDTOConverter() {
        return invGroupDTOConverter;
    }

    /**
     * @param invGroupDTOConverter the invGroupDTOConverter to set
     */
    public void setInvGroupDTOConverter(InvGroupDTOConverter invGroupDTOConverter) {
        this.invGroupDTOConverter = invGroupDTOConverter;
    }

    /**
     * @return the typeCatConverter
     */
    public SymbolDTOConverter getTypeCatConverter() {
        return typeCatConverter;
    }

    /**
     * @param typeCatConverter the typeCatConverter to set
     */
    public void setTypeCatConverter(SymbolDTOConverter typeCatConverter) {
        this.typeCatConverter = typeCatConverter;
    }

    /**
     * @return the unitDTOConverter
     */
    public SymbolDTOConverter getUnitDTOConverter() {
        return unitDTOConverter;
    }

    /**
     * @param unitDTOConverter the unitDTOConverter to set
     */
    public void setUnitDTOConverter(SymbolDTOConverter unitDTOConverter) {
        this.unitDTOConverter = unitDTOConverter;
    }

    /**
     * @return the showMessageDetails
     */
    public Boolean getShowMessageDetails() {
        return showMessageDetails;
    }

    /**
     * @param showMessageDetails the showMessageDetails to set
     */
    public void setShowMessageDetails(Boolean showMessageDetails) {
        this.showMessageDetails = showMessageDetails;
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
     * @return the barcodeEntity
     */
    public InvBarcodeDTO getBarcodeEntity() {
        return barcodeEntity;
    }

    /**
     * @param barcodeEntity the barcodeEntity to set
     */
    public void setBarcodeEntity(InvBarcodeDTO barcodeEntity) {
        this.barcodeEntity = barcodeEntity;
    }

    /**
     * @return the barcodeEntityList
     */
    public List<InvBarcodeDTO> getBarcodeEntityList() {
        return barcodeEntityList;
    }

    /**
     * @param barcodeEntityList the barcodeEntityList to set
     */
    public void setBarcodeEntityList(List<InvBarcodeDTO> barcodeEntityList) {
        this.barcodeEntityList = barcodeEntityList;
    }

    /**
     * @return the invBarcodeEntitySelection
     */
    public InvBarcodeDTO getInvBarcodeEntitySelection() {
        return invBarcodeEntitySelection;
    }

    /**
     * @param invBarcodeEntitySelection the invBarcodeEntitySelection to set
     */
    public void setInvBarcodeEntitySelection(InvBarcodeDTO invBarcodeEntitySelection) {
        this.invBarcodeEntitySelection = invBarcodeEntitySelection;
    }

    /**
     * @return the invBarcodesList
     */
    public List<InvBarcodeDTO> getInvBarcodesList() {
        return invBarcodesList;
    }

    /**
     * @param invBarcodesList the invBarcodesList to set
     */
    public void setInvBarcodesList(List<InvBarcodeDTO> invBarcodesList) {
        this.invBarcodesList = invBarcodesList;
    }

    /**
     * @return the quantityItemsStoreList
     */
    public List<QuantityItemsStore> getQuantityItemsStoreList() {
        return quantityItemsStoreList;
    }

    /**
     * @param quantityItemsStoreList the quantityItemsStoreList to set
     */
    public void setQuantityItemsStoreList(List<QuantityItemsStore> quantityItemsStoreList) {
        this.quantityItemsStoreList = quantityItemsStoreList;
    }

    /**
     * @return the itemDataEntityReportList
     */
    public List<InvItemDTO> getItemDataEntityReportList() {
        return itemDataEntityReportList;
    }

    /**
     * @param itemDataEntityReportList the itemDataEntityReportList to set
     */
    public void setItemDataEntityReportList(List<InvItemDTO> itemDataEntityReportList) {
        this.itemDataEntityReportList = itemDataEntityReportList;
    }

    /**
     * @return the proGroupDTOConverter
     */
    public ProGroupDTOConverter getProGroupDTOConverter() {
        return proGroupDTOConverter;
    }

    /**
     * @param proGroupDTOConverter the proGroupDTOConverter to set
     */
    public void setProGroupDTOConverter(ProGroupDTOConverter proGroupDTOConverter) {
        this.proGroupDTOConverter = proGroupDTOConverter;
    }

    /**
     * @return the proGroupDTOList
     */
    public List<ProGroupDTO> getProGroupDTOList() {
        return proGroupDTOList;
    }

    /**
     * @param proGroupDTOList the proGroupDTOList to set
     */
    public void setProGroupDTOList(List<ProGroupDTO> proGroupDTOList) {
        this.proGroupDTOList = proGroupDTOList;
    }

}
