/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.invQuotation;

import com.isag.businessservice.InvDelegatorService;
import com.isag.businessservice.InvItemService;
import com.isag.businessservice.InvQuotationDetailsService;
import com.isag.businessservice.InvQuotationService;
import com.isag.businessservice.ItemsBarcodesDetailsViewService;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.bussinessservice.global.InvPurchaseReturnSave;
import com.isag.converter.InvDelegatorConvertor;
import com.isag.converter.InvOrganizationSiteConverter;
import com.isag.converter.ItemConverter;
import com.isag.converter.ItemsBarcodesDetailsViewConverter;
import com.isag.entity.InvItem;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.InvPurchaseInvoiceDetail;
import com.isag.entity.InvQotation;
import com.isag.entity.InvQotationDetail;
import com.isag.entity.InventoryDelegator;
import com.isag.entiy.InvQuotationDetailsEntity;
import com.isag.entiy.InvQuotationEntity;
import com.isag.invPurchaseOrder.invPurchaseOrderFormMB;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
import com.isag.views.ItemsBarcodesDetailsView;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author hq002
 */
@Named(value = "invQuotationFormMB")
@ViewScoped
public class invQuotationFormMB extends BaseFormBean {

    private List<InventoryDelegator> inventoryDelegatorList;

    private List<InvItem> invItemList;

    private List<InvOrganizationSite> customerAndSupplierList;

    private InvQotation invQotation;
//    private List<InvQotation> InvQotations;

    private InvQotationDetail invQotationDetail;
    private List<InvQotationDetail> invQotationDetails;

    private InvQuotationEntity invQuotationEntity;
    private InvQuotationDetailsEntity invQuotationdetailsEntity;
    private InvQuotationDetailsEntity invQuotationdetailsEntitySelection;
    private List<InvQuotationDetailsEntity> invQuotationDetailsEntityList;
    private List<InvQuotationDetailsEntity> rowDeleted = new ArrayList<>();

    private List<InvQotationDetail> invQotationDetailList;
    private List<InvQotationDetail> invQuotationDeletedList;
    private List<InvQotationDetail> inveDetailsList;

    private Integer invQotationId, branchId;

    private BigDecimal totalPrice = BigDecimal.ZERO;
    private BigDecimal totalQuatity = BigDecimal.ZERO;
    private BigDecimal total = BigDecimal.ZERO;
    private BigDecimal totalNet = BigDecimal.ZERO;
    BigDecimal hundred = new BigDecimal(100);

    private Boolean showMessageDetails = Boolean.FALSE;
    private Boolean showMessageGeneral = Boolean.FALSE;
    private boolean showMessage = Boolean.FALSE;
    private Boolean delete = Boolean.FALSE;

    private ItemConverter itemConverter;
    private InvDelegatorConvertor invDelegatorConverter;
    private InvOrganizationSiteConverter organizationSiteConverter;

    private List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList;
    private ItemsBarcodesDetailsViewConverter itemsBarcodesDetailsViewConverter;

    Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap = new HashMap<>();

    @EJB
    private InvDelegatorService invDelegatorService;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private InvItemService invItemService;
    @EJB
    private InvQuotationService invQuotationService;
    @EJB
    private InvQuotationDetailsService invQuotationDetailsService;
    @EJB
    private ItemsBarcodesDetailsViewService itemsBarcodesDetailsViewService;

    @Override
    @PostConstruct
    public void init() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            branchId = getUserData().getUserBranch().getId();
            load();
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "init");

        }

    }

    @Override
    public void load() {
        try {

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));

            initObjs();

            fillLists();

            invQotationDetails = invQuotationDetailsService.getAllInvQotationDetails(invQotation.getId());

            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("add")) {
                resetItemDataForm();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
                try {
                    invQuotationdetailsEntitySelection = new InvQuotationDetailsEntity();

                    invQotationId = (Integer) context.getSessionMap().get("invQuotationSelected");

                    invQotation = new InvQotation();
                    invQotation = invQuotationService.findInvQotationByInvQotationId(invQotationId);
                    invQotationDetailList = invQuotationService.getALLInvQotationDetailsByInvQotation(invQotationId);

                    invQuotationEntity = mapInvQutationToEntity(invQotation);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "load");

        }
    }

    private void initObjs() {
        try {
            totalPrice = BigDecimal.ZERO;
            totalQuatity = BigDecimal.ZERO;
            total = BigDecimal.ZERO;

            invQotation = new InvQotation();
            invQotationDetail = new InvQotationDetail();
            invQuotationEntity = new InvQuotationEntity();
            invQuotationdetailsEntity = new InvQuotationDetailsEntity();
            inveDetailsList = new ArrayList<>();
            inventoryDelegatorList = new ArrayList<>();
            customerAndSupplierList = new ArrayList<>();
            invItemList = new ArrayList<>();
            invQotationDetailList = new ArrayList<>();
            invQuotationDeletedList = new ArrayList<>();
            invQuotationdetailsEntitySelection = new InvQuotationDetailsEntity();
            invQuotationDetailsEntityList = new ArrayList<>();
            itemsBarcodesDetailsViewList = new ArrayList<>();
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "initObjs");

        }
    }

    private void fillLists() {
        try {
            invQotationDetailList = invQuotationDetailsService.getAllInvQotationDetails(invQotation.getId());

            inventoryDelegatorList = invDelegatorService.getSalesByBranch(branchId);
            invDelegatorConverter = new InvDelegatorConvertor(inventoryDelegatorList);

            customerAndSupplierList = organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(branchId, true, 0);
            organizationSiteConverter = new InvOrganizationSiteConverter(customerAndSupplierList);

//        invItemList = invItemService.getInvItemByBranchId(branchId);
//        itemConverter = new ItemConverter(invItemList);
            itemsBarcodesDetailsViewList = itemsBarcodesDetailsViewService.findItemsBarcodesDetailsViewBranchId(branchId);
            itemsBarcodesDetailsViewConverter = new ItemsBarcodesDetailsViewConverter(itemsBarcodesDetailsViewList);

            for (ItemsBarcodesDetailsView itemsBarcodesDetailsView : itemsBarcodesDetailsViewList) {
                itemsBarcodeMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
            }
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "fillLists");
        }
    }

    private void resetItemDataForm() {
        try {
            invQuotationEntity = new InvQuotationEntity();
            invQuotationEntity.setDate(new Date());
            invQotationDetailList = new ArrayList<>();
            invQuotationdetailsEntitySelection = new InvQuotationDetailsEntity();
            invQuotationDetailsEntityList = new ArrayList<>();
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "resetItemDataForm");
        }
    }
    //----------------------------------------------------

    public void calculatePercentage() {
        try {
            BigDecimal percentage = BigDecimal.ZERO;
            if (invQuotationEntity.getDiscountType() && invQuotationEntity.getDiscount() != null) {
                percentage = invQuotationEntity.getDiscount().divide(new BigDecimal(100)).multiply(totalNet != null ? totalNet : BigDecimal.ZERO).setScale(3);

//                invQuotationEntity.setDiscount(((totalNet!= null ? invQuotationEntity.getDiscountValue() : BigDecimal.ZERO)
//                        .divide(totalNet.compareTo(BigDecimal.ZERO) == 0 || totalNet == null ? BigDecimal.ONE : totalNet, 4, BigDecimal.ROUND_CEILING))
//                        .multiply(new BigDecimal(100)));
                calculateTotal(percentage);
            }

        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "calculatePercentage");
        }

    }

    public void calculateValue() {
        try {

            if (invQuotationEntity.getDiscountType() != null && !invQuotationEntity.getDiscountType() && invQuotationEntity.getDiscount() != null && invQuotationEntity.getDiscount().compareTo(BigDecimal.ZERO) == 1) {
                calculateTotal(invQuotationEntity.getDiscount());
//       invQuotationEntity.setDiscountValue((totalNet != null ? totalNet : BigDecimal.ZERO)
//                        .divide(invQuotationEntity.getDiscountPercentage() != null ? invQuotationEntity.getDiscountPercentage() : BigDecimal.ZERO, BigDecimal.ROUND_HALF_UP));
            }

        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "calculateValue");
        }
    }

    public void calculateTotal(BigDecimal result) {
        try {

            invQuotationEntity.setTotalDiscount(((totalNet != null ? totalNet : BigDecimal.ZERO).subtract(result != null ? result : BigDecimal.ZERO)));
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "calculateTotal");
        }
    }

    public InvQuotationEntity mapInvQutationToEntity(InvQotation invQotation) {
        try {

            invQuotationEntity.setId(invQotation.getId());
            invQuotationEntity.setSerial(invQotation.getSerial());
            invQuotationEntity.setBranchId(invQotation.getBranchId() != null ? invQotation.getBranchId().getId() : null);
            invQuotationEntity.setMarkEdit(invQotation.getMarkEdit());
            invQuotationEntity.setDate(invQotation.getDate());
            invQuotationEntity.setEndDate(invQotation.getEndDate());
            invQuotationEntity.setOrganizationSite(invQotation.getCustomerId());
            invQuotationEntity.setInvDelegator(invQotation.getDelegatorId());
            invQuotationEntity.setRemark(invQotation.getRemark());
            invQuotationEntity.setDiscount(invQotation.getDiscount());
            invQuotationEntity.setDiscountType(invQotation.getDiscountType());
//            invQuotationEntity.setDiscountPercentage(invQotation.getDiscountPercentage());
//            invQuotationEntity.setDiscountValue(invQotation.getDiscountValue());
//            invQuotationEntity.setTotalDiscount(invQotation.getTotalDiscount());
            invQuotationEntity.setPostFlag(invQotation.getPostFlag());
            invQuotationEntity.setCreatedBy(invQotation.getCreatedBy());
            invQuotationEntity.setCreationDate(invQotation.getCreationDate());

            invQuotationDetailsEntityList = new ArrayList<>();

            BigDecimal net2 = BigDecimal.ZERO;
            BigDecimal disc = BigDecimal.ZERO;
            BigDecimal total1 = BigDecimal.ZERO;
            for (InvQotationDetail qotationDetail : invQotationDetailList) {
                invQuotationdetailsEntity = new InvQuotationDetailsEntity();
                invQuotationdetailsEntity.setId(qotationDetail.getId());
                invQuotationdetailsEntity.setSerial(qotationDetail.getSerial());
                invQuotationdetailsEntity.setBranchId(qotationDetail.getBranchId() != null ? qotationDetail.getBranchId() : null);
                invQuotationdetailsEntity.setCreatedBy(qotationDetail.getCreatedBy());
                invQuotationdetailsEntity.setCreationDate(qotationDetail.getCreationDate());
                invQuotationdetailsEntity.setWeight(qotationDetail.getWeight());

                if (qotationDetail.getItemBarcode() != null && !"".equals(qotationDetail.getItemBarcode())) {
                    invQuotationdetailsEntity.setItemsBarcodesDetail(itemsBarcodeMap.get(qotationDetail.getItemBarcode()));
                    invQuotationdetailsEntity.setUnit(itemsBarcodeMap.get(qotationDetail.getItemBarcode()).getName());
                    invQuotationdetailsEntity.setItemsBarcodesDetailTrans(invQuotationdetailsEntity.getItemsBarcodesDetail());
                    invQuotationdetailsEntity.setScrewing(invQuotationdetailsEntity.getItemsBarcodesDetail().getScrewing());
                    invQuotationdetailsEntity.setPaintColor(invQuotationdetailsEntity.getItemsBarcodesDetail().getPaintColor());
                    invQuotationdetailsEntity.setPaintColorName(invQuotationdetailsEntity.getItemsBarcodesDetail().getPaintColorName());
                    invQuotationdetailsEntity.setLength(invQuotationdetailsEntity.getItemsBarcodesDetail().getLength());
                }

                invQuotationdetailsEntity.setInvItem(qotationDetail.getItemId());
                invQuotationdetailsEntity.setItem(qotationDetail.getItemId().getName());
                invQuotationdetailsEntity.setQuantity(qotationDetail.getQuantity());
                invQuotationdetailsEntity.setPrice(qotationDetail.getPrice());
                invQuotationdetailsEntity.setDiscount(qotationDetail.getDiscount());
                invQuotationdetailsEntity.setDiscountId(qotationDetail.getDiscountId());

                if (qotationDetail.getPrice() != null && qotationDetail.getPrice() != BigDecimal.ZERO
                        && qotationDetail.getQuantity() != null && qotationDetail.getQuantity() != BigDecimal.ZERO) {
                    total1 = qotationDetail.getPrice().multiply(qotationDetail.getQuantity());
                    invQuotationdetailsEntity.setTotal(total1);
                }

                net2 = invQuotationdetailsEntity.getTotal() != null ? invQuotationdetailsEntity.getTotal() : BigDecimal.ZERO;
                disc = invQuotationdetailsEntity.getDiscount() != null ? invQuotationdetailsEntity.getDiscount() : BigDecimal.ZERO;
                invQuotationdetailsEntity.setNet(net2.subtract(disc));

                total = total.add(invQuotationdetailsEntity.getTotal() != null ? invQuotationdetailsEntity.getTotal() : BigDecimal.ZERO);
                totalNet = totalNet.add(invQuotationdetailsEntity.getNet());
                totalQuatity = totalQuatity.add(qotationDetail.getQuantity() != null ? qotationDetail.getQuantity() : BigDecimal.ZERO);

                invQuotationDetailsEntityList.add(invQuotationdetailsEntity);

            }
//            calculatePercentage();
//            calculateValue();
              updateSummition();
            return invQuotationEntity;
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "mapInvQutationToEntity");
            return null;
        }
    }

    public void adddetails() {
        try {
            for (InvQuotationDetailsEntity detailEntity : invQuotationDetailsEntityList) {
                if (detailEntity.getItemsBarcodesDetail() == null && detailEntity.getItemsBarcodesDetailTrans() != null) {
                    detailEntity.setItemsBarcodesDetail(detailEntity.getItemsBarcodesDetailTrans());
                    detailEntity.setUnit(detailEntity.getItemsBarcodesDetailTrans().getName());
                }

                if (detailEntity.getItemsBarcodesDetailTrans() == null
                        || !validateQuantity(detailEntity) || !validatePrice(detailEntity)) {

                    errorMessage("يجب ادخال تفاصيل الفاتورة");
                    return;
                }
                detailEntity.setMarkEdit(Boolean.FALSE);
            }
            InvQuotationDetailsEntity quotationDetailsEntity = new InvQuotationDetailsEntity();
            quotationDetailsEntity.setMarkEdit(Boolean.TRUE);
            invQuotationdetailsEntitySelection = quotationDetailsEntity;
            invQuotationDetailsEntityList.add(quotationDetailsEntity);
            calculatePercentage();
            calculateValue();
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "adddetails");
        }
    }

    public void deleteInvQuotationDetail() {
        try {

            if (invQuotationdetailsEntitySelection != null) {
                setShowMessageGeneral(Boolean.FALSE);
                setShowMessageDetails(Boolean.TRUE);
                if (invQuotationdetailsEntitySelection.getId() != null) {
                    setDelete(Boolean.TRUE);
                    rowDeleted.add(invQuotationdetailsEntitySelection);
                }

                invQuotationDetailsEntityList.remove(invQuotationdetailsEntitySelection);

                for (InvQuotationDetailsEntity detailEntity : invQuotationDetailsEntityList) {
                    if (detailEntity.getItemsBarcodesDetail() == null && detailEntity.getItemsBarcodesDetailTrans() != null) {
                        detailEntity.setItemsBarcodesDetail(detailEntity.getItemsBarcodesDetailTrans());
                        detailEntity.setUnit(detailEntity.getItemsBarcodesDetailTrans().getName());
                    }
                }

                updateSummition();
            } else {
                errorMessage("يجب اختيار سطر للمسح");
            }
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "deleteInvQuotationDetail");
        }
    }

    public void onCellEdit(CellEditEvent event) {
        try {
            Object oldValue = event.getOldValue() != null ? event.getOldValue() : null;
            Object newValue = event.getNewValue() != null ? event.getNewValue() : null;

            String column_name = event.getColumn().getClientId();

            DataTable dataTable = (DataTable) event.getSource();
            invQuotationdetailsEntitySelection = (InvQuotationDetailsEntity) dataTable.getRowData();

            if (column_name.contains("itemNumber") || column_name.contains("itemName")) {
                validateItems(invQuotationdetailsEntity);
            }

            if (column_name.contains("Quantity")) {
//            if (!validateQuantity(newValue != null ? (BigDecimal) newValue : null)) {
//                invQuotationdetailsEntitySelection.setQuantity((BigDecimal) oldValue);
//            }
            }

            if (column_name.contains("Price")) {
//            if (!validatePrice(newValue != null ? (BigDecimal) newValue : null)) {
//                invQuotationdetailsEntitySelection.setPrice((BigDecimal) oldValue);
//            }
            }

            if (column_name.contains("Discount")) {
//            validateDiscount(newValue != null ? (BigDecimal) newValue : null);
            }

            updateSummition();

        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "onCellEdit");
        }
    }

    public void validatePriceColumn(InvQuotationDetailsEntity invQuotationTable) {
        try {
            if (!validatePrice(invQuotationTable)) {
                invQuotationTable.setPrice(BigDecimal.ZERO);
            }

            updateSummition();
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "validatePriceColumn");
        }
    }

    public Boolean validatePrice(InvQuotationDetailsEntity invQuotationTable) {
        try {
            return invQuotationTable != null
                    && invQuotationTable.getItemsBarcodesDetailTrans() != null
                    && invQuotationTable.getPrice() != null
                    && invQuotationTable.getPrice().compareTo(BigDecimal.ZERO) == 1;
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "validatePrice");
            return null;
        }
    }

    public void validateDiscountColumn(InvQuotationDetailsEntity invQuotationTable) {
        try {
            if (!validateDiscount(invQuotationTable)) {
                invQuotationTable.setDiscount(BigDecimal.ZERO);
            }
            updateSummition();
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "validateDiscountColumn");

        }

    }

    public Boolean validateDiscount(InvQuotationDetailsEntity invQuotationTable) {
        try {
            return isDiscountValid(invQuotationTable.getDiscount());
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "validateDiscount");
            return null;

        }
    }

    private Boolean isDiscountValid(BigDecimal discValue) {
        try {
            if (discValue != null) {
                if ((discValue.compareTo(BigDecimal.ZERO) == 0 || discValue.compareTo(BigDecimal.ZERO) == 1)
                        && (discValue.compareTo(hundred) == 0 || hundred.compareTo(discValue) == 1)) {
                    return true;
                } else {
                    return errorMessage("لا يجب ادخال خصم اعلى من 100% في حالة النسبة");
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "isDiscountValid");
            return null;

        }
    }

    public void validateQuantityColum(InvQuotationDetailsEntity invQuotationTable) {
        try {
            if (!validateQuantity(invQuotationTable)) {
                if (invQuotationTable.getItemsBarcodesDetail() != null) {
                    invQuotationTable.setQuantity(BigDecimal.ZERO);
                } else {
                    invQuotationTable.setQuantity(null);
                }
            }
            updateSummition();
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "validateQuantityColum");

        }
    }

    public Boolean validateQuantity(InvQuotationDetailsEntity invQuotationTable) {
        try {
            return invQuotationTable != null && invQuotationTable.getItemsBarcodesDetail() != null
                    && invQuotationTable.getQuantity() != null
                    && invQuotationTable.getQuantity().compareTo(BigDecimal.ZERO) == 1;
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "validateQuantity");
            return null;
        }
    }

    public Boolean validateWeight(InvQuotationDetailsEntity invQuotationTable) {
        try {
            return invQuotationTable != null && invQuotationTable.getWeight() != null
                    && invQuotationTable.getWeight() != null
                    && invQuotationTable.getWeight().compareTo(BigDecimal.ZERO) == 1;
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "validateWeight");
            return null;
        }
    }

    public Boolean validateItems(InvQuotationDetailsEntity invQuotationTable) {
        try {
            if (invQuotationTable.getItemsBarcodesDetail() == null) {
                resetInvItem(invQuotationTable);
            } else {
                invQuotationTable.setItemsBarcodesDetailTrans(invQuotationTable.getItemsBarcodesDetail());

                if (invQuotationTable.getItemsBarcodesDetail().getUnitName() != null
                        && !"".equals(invQuotationTable.getItemsBarcodesDetail().getUnitName())) {
                    invQuotationTable.setPrice(invQuotationTable.getItemsBarcodesDetail().getSellPrice());
                } else {
                    resetInvItem(invQuotationTable);
                    updateSummition();
                    return errorMessage("هذا الصنف ليس لديه وحدات");
                }
            }
            updateSummition();
            return true;
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "validateItems");
            return null;
        }
    }

    private void resetInvItem(InvQuotationDetailsEntity invQuotationTable) {
        try {
            invQuotationTable.setQuantity(null);
            invQuotationTable.setPrice(null);
            invQuotationTable.setDiscount(null);
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "resetInvItem");
        }
    }

    public void updateSummition() {
        try {

            BigDecimal quantity, weight, discount, cost, net, totalSum;
            totalSum = BigDecimal.ZERO;
            totalQuatity = BigDecimal.ZERO;
            totalNet = BigDecimal.ZERO;

            for (InvQuotationDetailsEntity detailEntity : invQuotationDetailsEntityList) {
                if (detailEntity.getItemsBarcodesDetail() == null && detailEntity.getItemsBarcodesDetailTrans() != null) {
                    detailEntity.setItemsBarcodesDetail(detailEntity.getItemsBarcodesDetailTrans());
                }
                quantity = detailEntity.getQuantity() != null ? detailEntity.getQuantity() : BigDecimal.ZERO;
                weight = (detailEntity.getWeight() == null || detailEntity.getWeight().compareTo(BigDecimal.ZERO) == 0) ? BigDecimal.ZERO : detailEntity.getWeight();
                cost = detailEntity.getPrice() != null ? detailEntity.getPrice() : BigDecimal.ZERO;
                discount = detailEntity.getDiscount() != null ? detailEntity.getDiscount() : BigDecimal.ZERO;
                // total = quantity.multiply(cost);
                if (weight == null || weight.compareTo(BigDecimal.ZERO) == 0) {
                    total = quantity.multiply(cost);
                } else {
                    total = weight.multiply(cost);
                }

                net = total.subtract((total.multiply(discount).divide(hundred)));
                detailEntity.setNet(net.setScale(2, BigDecimal.ROUND_UP));

                totalSum = totalSum.add(detailEntity.getTotal() != null ? detailEntity.getTotal() : BigDecimal.ZERO);
                totalQuatity = totalQuatity.add(quantity);
                totalNet = (totalNet.add(detailEntity.getNet())).setScale(2, BigDecimal.ROUND_UP);
            }
            calculatePercentage();
            calculateValue();
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "updateSummition");
        }

    }

    @Override
    public String save() {
        try {

            invQotation.setCompanyId(getUserData().getCompany());
            invQotation.setBranchId(getUserData().getUserBranch());

            if (invQotation.getId() == null) {
                invQotation.setCreationDate(new Date());
                invQotation.setCreatedBy(getUserData().getUser());
            } else {
                invQotation.setModificationDate(new Date());
                invQotation.setModifiedBy(getUserData().getUser());
                invQotation.setStatus(0);
            }

            invQotation.setCustomerId(invQuotationEntity.getOrganizationSite());
            invQotation.setDelegatorId(invQuotationEntity.getInvDelegator());
            invQotation.setDate(invQuotationEntity.getDate() != null ? invQuotationEntity.getDate() : null);
            invQotation.setEndDate(invQuotationEntity.getEndDate() != null ? invQuotationEntity.getEndDate() : null);
            invQotation.setRemark(invQuotationEntity.getRemark() != null ? invQuotationEntity.getRemark() : null);
            invQotation.setDiscount(invQuotationEntity.getDiscount());
            invQotation.setDiscountType(invQuotationEntity.getDiscountType());
//            invQotation.setDiscountPercentage(invQuotationEntity.getDiscountPercentage() != null ? invQuotationEntity.getDiscountPercentage() : BigDecimal.ZERO);
//            invQotation.setDiscountValue(invQuotationEntity.getDiscountValue() != null ? invQuotationEntity.getDiscountValue() : BigDecimal.ZERO);
//            invQotation.setTotalDiscount(invQuotationEntity.getTotalDiscount() != null ? invQuotationEntity.getTotalDiscount() : BigDecimal.ZERO);
            InvQotationDetail qotationDetail;
            InvItem invItem;
            inveDetailsList = new ArrayList<>();
            if (invQuotationDetailsEntityList != null && !invQuotationDetailsEntityList.isEmpty()) {
                for (InvQuotationDetailsEntity detailsEntity : invQuotationDetailsEntityList) {
                    qotationDetail = new InvQotationDetail();

                    if (detailsEntity.getId() != null) {
                        qotationDetail.setId(detailsEntity.getId());
                        qotationDetail.setSerial(detailsEntity.getSerial());
                        qotationDetail.setCreationDate(detailsEntity.getCreationDate());
                        qotationDetail.setCreatedBy(detailsEntity.getCreatedBy());
                        qotationDetail.setModificationDate(new Date());
                        qotationDetail.setModifiedBy(getUserData().getUser());

                    } else {
                        qotationDetail.setCreationDate(new Date());
                        qotationDetail.setCreatedBy(getUserData().getUser());
                        qotationDetail.setStatus(0);
                    }
                    qotationDetail.setBranchId(getUserData().getUserBranch());
                    qotationDetail.setCompanyId(getUserData().getCompany());
                    qotationDetail.setScrewing(detailsEntity.getScrewing());

                    if (validatePrice(detailsEntity)) {
                        qotationDetail.setPrice(detailsEntity.getPrice());
                    } else {
                        errorMessage("يجب ادخال السعر");
                        return "";
                    }

                    if (detailsEntity.getItemsBarcodesDetail() == null && detailsEntity.getItemsBarcodesDetailTrans() == null) {
                        errorMessage("يجب ادخال صنف");
                        return "";
                    } else {
                        invItem = new InvItem();
                        invItem.setId(detailsEntity.getItemsBarcodesDetailTrans().getItemId());
                        qotationDetail.setItemId(invItem);

                        qotationDetail.setItemBarcode(detailsEntity.getItemsBarcodesDetailTrans().getBarcode());
                        detailsEntity.setItemsBarcodesDetail(detailsEntity.getItemsBarcodesDetailTrans());
                    }

                    if (validateQuantity(detailsEntity)) {
                        qotationDetail.setQuantity(detailsEntity.getQuantity());
                    }
                    if (detailsEntity.getWeight() != null && detailsEntity.getWeight().compareTo(BigDecimal.ZERO) == 1) {
                        qotationDetail.setWeight(detailsEntity.getWeight());
                    }
                    if (!validateQuantity(detailsEntity) && !validateWeight(detailsEntity)) {
                        errorMessage("يجب ادخال الكمية او الوزن");
                        return "";
                    }
                    qotationDetail.setDiscount(detailsEntity.getDiscount());
                    qotationDetail.setFinalQuantity(detailsEntity.getQuantity());

                    inveDetailsList.add(qotationDetail);
                }
            } else {
                errorMessage("يجب ادخال عروض");
                return "";
            }

            if (rowDeleted != null) {
                InvQotationDetail ipo;

                for (InvQuotationDetailsEntity deletedDetailsEntity : getRowDeleted()) {
                    ipo = new InvQotationDetail();
                    ipo.setId(deletedDetailsEntity.getId());
                    invQuotationDeletedList.add(ipo);
                }
            }
            InvPurchaseReturnSave invPurchaseReturnSave;

            invPurchaseReturnSave = invQuotationService.addInvQotation(invQotation, inveDetailsList, invQuotationDeletedList);
            invQuotationEntity.setId(invQotation.getId());
            resetquotation();
            invQotation = invPurchaseReturnSave.getInvQotation();
            invQotationDetailList = (List<InvQotationDetail>) invPurchaseReturnSave.getInvQotationDetailList();
            invQuotationEntity = mapInvQutationToEntity(invPurchaseReturnSave.getInvQotation());
            savedMeesage(getUserData().getUserDDs().get("SAVED"));

            return "";
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "save");
            return null;
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
            saveError(e, "invQuotationFormMB", "reset");
        }
    }

    public String print() {
        try {

            try {

                fillReport(prepareReport(), getUserData().getReportPath() + "QuotationReport.jasper", invQuotationDetailsEntityList, "pdf");

            } catch (JRException ex) {
                Logger.getLogger(invPurchaseOrderFormMB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(invPurchaseOrderFormMB.class.getName()).log(Level.SEVERE, null, ex);
            }

            return "";
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "print");
            return null;
        }

    }

    public <T> void fillReport(HashMap hashMap, String reportPath, List<T> listBean, String exportType) throws JRException, IOException {
        try {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listBean);
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, hashMap, beanCollectionDataSource);
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            if ("pdf".equals(exportType)) {
                JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
                servletOutputStream.flush();
                servletOutputStream.close();
                httpServletResponse.getOutputStream().flush();
                httpServletResponse.getOutputStream().close();
            } else if ("excel".equals(exportType)) {

            } else if ("html".equals(exportType)) {

            }
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "fillReport");
        }
    }

    public HashMap prepareReport() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            HashMap hashMap = new HashMap();

            hashMap.put("PrintedBy", getUserData().getUser().getName());
            hashMap.put("Branch", getUserData().getUserBranch().getNameAr());
            hashMap.put("companyName", getUserData().getCompany().getName());

            if (isFileExist(getUserData().getImageReportPath())) {
                hashMap.put("companyImage", getUserData().getImageReportPath());
            } else {
                hashMap.put("companyImage", null);
            }

            hashMap.put("header2", " تقرير طلب شراء");
            hashMap.put("serialText", "رقم العرض");
            hashMap.put("serialvalue", invQuotationEntity.getSerial() != null ? invQuotationEntity.getSerial() : "");
            hashMap.put("supplierText", "اسم العميل");
            hashMap.put("supplierValue", invQuotationEntity.getOrganizationSite() != null ? invQuotationEntity.getOrganizationSite().getName() : "");
            hashMap.put("BillSDateText", "التاريخ");
            hashMap.put("BillSDatevalue", invQuotationEntity.getDate() != null ? invQuotationEntity.getDate() : new Date());
            hashMap.put("delegatebillText", "مندوب المبيعات");
            hashMap.put("delegateBillValue", invQuotationEntity.getInvDelegator() != null ? invQuotationEntity.getInvDelegator().getName() : "");
            hashMap.put("pOrderNumText", "تاريخ نهاية العرض");
            hashMap.put("pOrderNumValue", invQuotationEntity.getEndDate() != null ? invQuotationEntity.getEndDate() : new Date());

            hashMap.put("remarkText", "الملاحظات");
            hashMap.put("remarkValue", invQuotationEntity.getRemark() != null ? invQuotationEntity.getRemark() : "");

            hashMap.put("itemText", "الصنف");
            hashMap.put("unitText", "رقم الوحدة");
            hashMap.put("quantityText", "الكمية");
            hashMap.put("priceText", "السعر");
            hashMap.put("discountText", "الخصم");
            hashMap.put("totalText", "الاجمالى");

            hashMap.put("reportDate", new Date());

            return hashMap;
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "prepareReport");
            return null;
        }
    }

    //------------------------------------    
    public void resetquotation() {
        try {
            invQotation = new InvQotation();

            invQuotationDeletedList = new ArrayList<>();
            invQuotationEntity.setId(null);
            invQuotationEntity.setSerial(null);
            totalPrice = BigDecimal.ZERO;
            totalQuatity = BigDecimal.ZERO;
            total = BigDecimal.ZERO;
            totalNet = BigDecimal.ZERO;
            invQuotationDetailsEntityList = new ArrayList<>();
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "resetquotation");

        }

    }

    public List<InvItem> completeInvItemData(String query) {
        try {
            List<InvItem> itemsList = invItemList;
            if (query == null || query.trim().equals("")) {

                itemConverter = new ItemConverter(itemsList);
                return itemsList;
            }
            List<InvItem> filteredItems = new ArrayList<>();

            String nameAr;
            String code;
            InvItem itemFilter;
            for (int i = 0; i < invItemList.size(); i++) {
                itemFilter = itemsList.get(i);
                nameAr = itemFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredItems.contains(itemFilter)) {
                        filteredItems.add(itemFilter);
                    }
                }

                code = itemFilter.getCode();
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredItems.contains(itemFilter)) {
                        filteredItems.add(itemFilter);
                    }
                }
            }

            itemConverter = new ItemConverter(filteredItems);
            return filteredItems;
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "completeInvItemData");
            return null;

        }
    }

    public List<ItemsBarcodesDetailsView> completeItemsData(String query) {
        try {
            List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViews = itemsBarcodesDetailsViewList;
            if (query == null || query.trim().equals("")) {
                itemsBarcodesDetailsViewConverter = new ItemsBarcodesDetailsViewConverter(itemsBarcodesDetailsViews);
                return itemsBarcodesDetailsViews;
            }
            List<ItemsBarcodesDetailsView> filteredItems = new ArrayList<>();

            String nameAr;
            String code;
            ItemsBarcodesDetailsView barcodesDetailsView;
            for (int i = 0; i < itemsBarcodesDetailsViewList.size(); i++) {
                barcodesDetailsView = itemsBarcodesDetailsViews.get(i);
                nameAr = barcodesDetailsView.getName();
                if (nameAr != null && nameAr.toLowerCase().trim().contains(query.toLowerCase().trim())) {
                    if (!filteredItems.contains(barcodesDetailsView)) {
                        filteredItems.add(barcodesDetailsView);
                    }
                }

                code = barcodesDetailsView.getBarcode();
                if (code != null && code.toLowerCase().trim().contains(query.toLowerCase().trim())) {
                    if (!filteredItems.contains(barcodesDetailsView)) {
                        filteredItems.add(barcodesDetailsView);
                    }
                }
            }

            itemsBarcodesDetailsViewConverter = new ItemsBarcodesDetailsViewConverter(filteredItems);
            return filteredItems;
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "completeItemsData");
            return null;

        }
    }

    public List<InvOrganizationSite> completeOrganizationSite(String query) {
        try {
            List<InvOrganizationSite> invOrganizationList = customerAndSupplierList;
            if (query == null || query.trim().equals("")) {

                organizationSiteConverter = new InvOrganizationSiteConverter(invOrganizationList);
                return invOrganizationList;
            }
            List<InvOrganizationSite> filteredInvOrganizations = new ArrayList<>();

            String nameAr;
            String code;
            InvOrganizationSite invOrganizationFilter;
            for (int i = 0; i < customerAndSupplierList.size(); i++) {
                invOrganizationFilter = invOrganizationList.get(i);
                nameAr = invOrganizationFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredInvOrganizations.contains(invOrganizationFilter)) {
                        filteredInvOrganizations.add(invOrganizationFilter);
                    }
                }

                code = invOrganizationFilter.getCode();
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredInvOrganizations.contains(invOrganizationFilter)) {
                        filteredInvOrganizations.add(invOrganizationFilter);
                    }
                }
            }

            organizationSiteConverter = new InvOrganizationSiteConverter(filteredInvOrganizations);
            return filteredInvOrganizations;
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "completeOrganizationSite");
            return null;

        }
    }

    public List<InventoryDelegator> completeInvDelegator(String query) {
        try {
            List<InventoryDelegator> invDelegatorList = inventoryDelegatorList;
            if (query == null || query.trim().equals("")) {
                invDelegatorConverter = new InvDelegatorConvertor(invDelegatorList);
                return invDelegatorList;
            }
            List<InventoryDelegator> filteredInvDelegators = new ArrayList<>();

            String nameAr;
            Integer code;
            InventoryDelegator invDelegatorFilter;
            for (int i = 0; i < inventoryDelegatorList.size(); i++) {
                invDelegatorFilter = invDelegatorList.get(i);
                nameAr = invDelegatorFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredInvDelegators.contains(invDelegatorFilter)) {
                        filteredInvDelegators.add(invDelegatorFilter);
                    }
                }

                code = invDelegatorFilter.getId();
                if (code != null && String.valueOf(code).toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredInvDelegators.contains(invDelegatorFilter)) {
                        filteredInvDelegators.add(invDelegatorFilter);
                    }
                }
            }

            invDelegatorConverter = new InvDelegatorConvertor(filteredInvDelegators);
            return filteredInvDelegators;
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "completeInvDelegator");
            return null;

        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String exit() {
        try {
            exit("../invQuotation/invQuotationList.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "invQuotationFormMB", "exit");
            return null;

        }
    }

    public List<InventoryDelegator> getInventoryDelegatorList() {
        return inventoryDelegatorList;
    }

    public void setInventoryDelegatorList(List<InventoryDelegator> inventoryDelegatorList) {
        this.inventoryDelegatorList = inventoryDelegatorList;
    }

    public List<InvItem> getInvItemList() {
        return invItemList;
    }

    public void setInvItemList(List<InvItem> invItemList) {
        this.invItemList = invItemList;
    }

    public List<InvOrganizationSite> getCustomerAndSupplierList() {
        return customerAndSupplierList;
    }

    public void setCustomerAndSupplierList(List<InvOrganizationSite> customerAndSupplierList) {
        this.customerAndSupplierList = customerAndSupplierList;
    }

    public InvQotation getInvQotation() {
        return invQotation;
    }

    public void setInvQotation(InvQotation invQotation) {
        this.invQotation = invQotation;
    }

    public InvQotationDetail getInvQotationDetail() {
        return invQotationDetail;
    }

    public void setInvQotationDetail(InvQotationDetail invQotationDetail) {
        this.invQotationDetail = invQotationDetail;
    }

    public List<InvQotationDetail> getInvQotationDetails() {
        return invQotationDetails;
    }

    public void setInvQotationDetails(List<InvQotationDetail> invQotationDetails) {
        this.invQotationDetails = invQotationDetails;
    }

    public InvQuotationEntity getInvQuotationEntity() {
        return invQuotationEntity;
    }

    public void setInvQuotationEntity(InvQuotationEntity invQuotationEntity) {
        this.invQuotationEntity = invQuotationEntity;
    }

    public InvQuotationDetailsEntity getInvQuotationdetailsEntity() {
        return invQuotationdetailsEntity;
    }

    public void setInvQuotationdetailsEntity(InvQuotationDetailsEntity invQuotationdetailsEntity) {
        this.invQuotationdetailsEntity = invQuotationdetailsEntity;
    }

    public InvQuotationDetailsEntity getInvQuotationdetailsEntitySelection() {
        return invQuotationdetailsEntitySelection;
    }

    public void setInvQuotationdetailsEntitySelection(InvQuotationDetailsEntity invQuotationdetailsEntitySelection) {
        this.invQuotationdetailsEntitySelection = invQuotationdetailsEntitySelection;
    }

    public List<InvQuotationDetailsEntity> getRowDeleted() {
        return rowDeleted;
    }

    public void setRowDeleted(List<InvQuotationDetailsEntity> rowDeleted) {
        this.rowDeleted = rowDeleted;
    }

    public List<InvQotationDetail> getInvQotationDetailList() {
        return invQotationDetailList;
    }

    public void setInvQotationDetailList(List<InvQotationDetail> invQotationDetailList) {
        this.invQotationDetailList = invQotationDetailList;
    }

    public List<InvQotationDetail> getInvQuotationDeletedList() {
        return invQuotationDeletedList;
    }

    public void setInvQuotationDeletedList(List<InvQotationDetail> invQuotationDeletedList) {
        this.invQuotationDeletedList = invQuotationDeletedList;
    }

    public List<InvQotationDetail> getInveDetailsList() {
        return inveDetailsList;
    }

    public void setInveDetailsList(List<InvQotationDetail> inveDetailsList) {
        this.inveDetailsList = inveDetailsList;
    }

    public Integer getInvQotationId() {
        return invQotationId;
    }

    public void setInvQotationId(Integer invQotationId) {
        this.invQotationId = invQotationId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalQuatity() {
        return totalQuatity;
    }

    public void setTotalQuatity(BigDecimal totalQuatity) {
        this.totalQuatity = totalQuatity;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getTotalNet() {
        return totalNet;
    }

    public void setTotalNet(BigDecimal totalNet) {
        this.totalNet = totalNet;
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

    public ItemConverter getItemConverter() {
        return itemConverter;
    }

    public void setItemConverter(ItemConverter itemConverter) {
        this.itemConverter = itemConverter;
    }

    public InvDelegatorConvertor getInvDelegatorConverter() {
        return invDelegatorConverter;
    }

    public void setInvDelegatorConverter(InvDelegatorConvertor invDelegatorConverter) {
        this.invDelegatorConverter = invDelegatorConverter;
    }

    public InvOrganizationSiteConverter getOrganizationSiteConverter() {
        return organizationSiteConverter;
    }

    public void setOrganizationSiteConverter(InvOrganizationSiteConverter organizationSiteConverter) {
        this.organizationSiteConverter = organizationSiteConverter;
    }

    public List<InvQuotationDetailsEntity> getInvQuotationDetailsEntityList() {
        return invQuotationDetailsEntityList;
    }

    public void setInvQuotationDetailsEntityList(List<InvQuotationDetailsEntity> invQuotationDetailsEntityList) {
        this.invQuotationDetailsEntityList = invQuotationDetailsEntityList;
    }

    public ItemsBarcodesDetailsViewConverter getItemsBarcodesDetailsViewConverter() {
        return itemsBarcodesDetailsViewConverter;
    }

    public void setItemsBarcodesDetailsViewConverter(ItemsBarcodesDetailsViewConverter itemsBarcodesDetailsViewConverter) {
        this.itemsBarcodesDetailsViewConverter = itemsBarcodesDetailsViewConverter;
    }
}
