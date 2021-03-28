/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.common.Validation;
import com.toby.define.CompanyActivityClassEnum;
import com.toby.define.ScreenNameClassEnum;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.SymbolDTO;
import com.toby.views.ItemsBarcodesDetailsView;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class ProGlobalServiceImpl implements ProGlobalService {

    @EJB
    private QuantityItemsStoreByDateService quantityItemsStoreAddExitService;

    @Override
    public InvDetailDTO calculateDetailTotalsRow(InvDetailDTO detailEntity, InvInvoiceDTO invInvoiceDTO, Boolean isNeeadQuantity, ScreenNameClassEnum screenNameClassEnum) {
        BigDecimal quantity;
        BigDecimal weight;
        BigDecimal cost;
        BigDecimal number;
        BigDecimal discount;
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal valueAfterDiscount;
        if (detailEntity.getItemsBarcodesDetail() == null && detailEntity.getItemsBarcodesDetailTrans() != null) {
            detailEntity.setItemsBarcodesDetail(detailEntity.getItemsBarcodesDetailTrans());
        }
        quantity = detailEntity.getQuantity() != null ? detailEntity.getQuantity() : BigDecimal.ZERO;
        weight = detailEntity.getWeight() == null ? BigDecimal.ZERO : detailEntity.getWeight();
        number = detailEntity.getNumber() == null ? BigDecimal.ONE : detailEntity.getNumber();
        cost = detailEntity.getCost() != null ? detailEntity.getCost().multiply(invInvoiceDTO.getRate()) : BigDecimal.ZERO;
        if (invInvoiceDTO.getCompanyActivity() != null) {
            if (invInvoiceDTO.getCompanyActivity().equals(CompanyActivityClassEnum.Alumetal)) {
                total = AlumetalActivity(weight, total, quantity, cost);
            } else if (invInvoiceDTO.getCompanyActivity().equals(CompanyActivityClassEnum.RetailSaleForClothFactory) && ScreenNameClassEnum.INVSALESINVOICEFORM.equals(screenNameClassEnum)) {
                total = RetailActivity(total, quantity, cost);
            } else {
                total = RetailActivity(total, quantity, cost);
            }
        } else {
            detailEntity.setMsg("يجب تحديد نشاط الشركة برجاء النواصل مع الدعم");
        }
        discount = detailEntity.getDiscount() != null ? detailEntity.getDiscount() : BigDecimal.ZERO;
        valueAfterDiscount = total.subtract((total.multiply(discount).divide(new BigDecimal(100))));
        detailEntity.setValueAfterDiscount(valueAfterDiscount);
        detailEntity.setNet(valueAfterDiscount.setScale(3, BigDecimal.ROUND_HALF_UP));
        detailEntity.setTotalQuantityWithScrewing(detailEntity.getQuantity().multiply(detailEntity.getScrewing()));
        if (isNeeadQuantity && invInvoiceDTO.getInventorySetupDTO() != null && !invInvoiceDTO.getInventorySetupDTO().getNegativeSell()) {
            detailEntity = updateAvailableQuantityRow(invInvoiceDTO.getScreenName(), detailEntity, invInvoiceDTO.getInvInventoryId() != null ? invInvoiceDTO.getInvInventoryId().getId() : null, invInvoiceDTO.getInventorySetupDTO().getSellBuy(), invInvoiceDTO.getInventorySetupDTO().getNegativeSell(), invInvoiceDTO.getIsEdit());
        }
        return detailEntity;
    }

    private BigDecimal AlumetalActivity(BigDecimal weight, BigDecimal total, BigDecimal quantity, BigDecimal cost) {
        if (weight == null || weight.compareTo(BigDecimal.ZERO) == 0) {
            total = quantity.multiply(cost);
        } else {
            total = weight.multiply(cost);
        }
        return total;
    }

    private BigDecimal RetailActivity(BigDecimal total, BigDecimal quantity, BigDecimal cost) {
        total = quantity.multiply(cost);
        return total;
    }

    private BigDecimal RetailSaleForClothFactoryActivity(BigDecimal total, BigDecimal number, BigDecimal cost) {
        total = number.multiply(cost);
        return total;
    }

    @Override
    public InvInvoiceDTO updateSummition(InvInvoiceDTO invPurchaseInvoiceDTO) {
        try {

            invPurchaseInvoiceDTO = calculateDetailValues(invPurchaseInvoiceDTO);
            invPurchaseInvoiceDTO = recalcHeadValues(invPurchaseInvoiceDTO);
        } catch (Exception e) {
//            saveError(e, "InvPurchaseInvoiceFormMB", "updateSummition");
        }

        return invPurchaseInvoiceDTO;
    }

    private InvInvoiceDTO calculateDetailValues(InvInvoiceDTO invPurchaseInvoiceDTO) {
        if (invPurchaseInvoiceDTO != null) {
            invPurchaseInvoiceDTO.setTotalNet(BigDecimal.ZERO);
            invPurchaseInvoiceDTO.setTotalQuatity(BigDecimal.ZERO);
            invPurchaseInvoiceDTO.setTotalWeight(BigDecimal.ZERO);
            if (invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList() != null && !invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList().isEmpty()) {
                List<InvDetailDTO> invPurchaseInvoiceDetailDTOList = new ArrayList<>();

                for (InvDetailDTO detailEntity : invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList()) {
                    // حساب الاجماليات الخاصة بكل تفصيلى على حده
                    detailEntity = calculateDetailTotalsRow(detailEntity, invPurchaseInvoiceDTO, false, invPurchaseInvoiceDTO.getScreenName());
                    invPurchaseInvoiceDetailDTOList.add(detailEntity);
                    // حساب إجماليات الفاتورة بعد حساب كل سطر تفصيلى
                    invPurchaseInvoiceDTO = claculateHeadTotalFromDetail(detailEntity, invPurchaseInvoiceDTO);
                }
                invPurchaseInvoiceDTO.setInvPurchaseInvoiceDetailList(invPurchaseInvoiceDetailDTOList);
            }
        }
        return invPurchaseInvoiceDTO;
    }

    @Override
    public InvInvoiceDTO recalcHeadValues(InvInvoiceDTO invPurchaseInvoiceDTO) {
        resetHeadTotals(invPurchaseInvoiceDTO);
        invPurchaseInvoiceDTO = calculateDiscountValue(invPurchaseInvoiceDTO);
        invPurchaseInvoiceDTO.setTotalNetAfterDiscount((invPurchaseInvoiceDTO.getTotalNet().subtract(invPurchaseInvoiceDTO.getDiscountValue() != null ? invPurchaseInvoiceDTO.getDiscountValue() : BigDecimal.ZERO)).setScale(3, BigDecimal.ROUND_HALF_UP));
        invPurchaseInvoiceDTO = calculateTaxDiscValue(invPurchaseInvoiceDTO);
        invPurchaseInvoiceDTO = calculateTaxValue(invPurchaseInvoiceDTO);
        invPurchaseInvoiceDTO.setFinalNet((invPurchaseInvoiceDTO.getTotalNetAfterDiscount().add(invPurchaseInvoiceDTO.getTaxvalue() != null ? invPurchaseInvoiceDTO.getTaxvalue() : BigDecimal.ZERO).subtract(invPurchaseInvoiceDTO.getTaxdiscvalue() != null ? invPurchaseInvoiceDTO.getTaxdiscvalue() : BigDecimal.ZERO)).setScale(3, BigDecimal.ROUND_HALF_UP));
        return invPurchaseInvoiceDTO;
    }

    private void resetHeadTotals(InvInvoiceDTO invPurchaseInvoiceDTO) {
        invPurchaseInvoiceDTO.setTotalWithTaxValue(BigDecimal.ZERO);
        invPurchaseInvoiceDTO.setTaxdiscvalue(BigDecimal.ZERO);
        invPurchaseInvoiceDTO.setTaxvalue(BigDecimal.ZERO);
        invPurchaseInvoiceDTO.setDiscountValue(BigDecimal.ZERO);

        invPurchaseInvoiceDTO.setTotalNetAfterDiscount(BigDecimal.ZERO);
        invPurchaseInvoiceDTO.setFinalNet(BigDecimal.ZERO);
    }

    private InvInvoiceDTO calculateDiscountValue(InvInvoiceDTO invPurchaseInvoiceDTO) {
        // لحساب الاجمالى بعد الخصم 
        // الخصم ممكن يكون نسبة او قيمة 
        try {
            if (invPurchaseInvoiceDTO != null) {
                invPurchaseInvoiceDTO.setDiscountValue(BigDecimal.ZERO);
                if (invPurchaseInvoiceDTO.getDiscountType() == 0) {
                    if (invPurchaseInvoiceDTO.getTotalNet() != null
                            && invPurchaseInvoiceDTO.getDiscount() != null
                            && invPurchaseInvoiceDTO.getTotalNet().compareTo(BigDecimal.ZERO) == 1
                            && invPurchaseInvoiceDTO.getTotalNet().compareTo(invPurchaseInvoiceDTO.getDiscount()) != -1) {
                        invPurchaseInvoiceDTO.setDiscountValue((invPurchaseInvoiceDTO.getDiscount() != null ? invPurchaseInvoiceDTO.getDiscount() : BigDecimal.ZERO).setScale(3, BigDecimal.ROUND_HALF_UP));
                    }
                } else if (Validation.isDiscountValid(invPurchaseInvoiceDTO.getDiscountValue())) {
                    invPurchaseInvoiceDTO.setDiscountValue(invPurchaseInvoiceDTO.getTotalNet().multiply(invPurchaseInvoiceDTO.getDiscount()).divide(BigDecimal.valueOf(100)));
                }
            }
        } catch (Exception e) {

        }
        return invPurchaseInvoiceDTO;
    }

    private InvInvoiceDTO calculateTaxDiscValue(InvInvoiceDTO invPurchaseInvoiceDTO) {
        // لحساب ضريبة الخصم
        // تحسب ضريبة الخصم بعد حساب الاجمالة بعد الخصم
        try {

            invPurchaseInvoiceDTO.setTaxdiscvalue(BigDecimal.ZERO);

            if (invPurchaseInvoiceDTO.getTaxdiscflag() != null && invPurchaseInvoiceDTO.getTaxdiscflag()) {
                invPurchaseInvoiceDTO.setTaxdiscvalue(invPurchaseInvoiceDTO.getTotalNetAfterDiscount().multiply(BigDecimal.valueOf(0.01)));
            }

        } catch (Exception e) {

        }
        return invPurchaseInvoiceDTO;
    }

    private InvInvoiceDTO calculateTaxValue(InvInvoiceDTO invPurchaseInvoiceDTO) {
        // لحساب ضريبة القيمة المضافة
        // تحسب ضريبة القيمة المضافة بعد حساب الاجمالى بعد الخصم
        try {
            if (invPurchaseInvoiceDTO.getTaxflag() != null && invPurchaseInvoiceDTO.getTaxflag()) {
                invPurchaseInvoiceDTO.setTaxvalue(invPurchaseInvoiceDTO.getTotalNetAfterDiscount().multiply(BigDecimal.valueOf(0.14)));
            } else {
                invPurchaseInvoiceDTO.setTaxvalue(BigDecimal.ZERO);
            }
        } catch (Exception e) {
//            saveError(e, "InvPurchaseInvoiceFormMB", "updateTax");

        }
        return invPurchaseInvoiceDTO;
    }

    private InvInvoiceDTO claculateHeadTotalFromDetail(InvDetailDTO detailEntity, InvInvoiceDTO invPurchaseInvoiceDTO) {
        if (invPurchaseInvoiceDTO.getCompanyActivity().equals(CompanyActivityClassEnum.Alumetal)) {
            invPurchaseInvoiceDTO = alumetalActivity(invPurchaseInvoiceDTO, detailEntity);
        }
        invPurchaseInvoiceDTO.setTotalQuatity(invPurchaseInvoiceDTO.getTotalQuatity().add(detailEntity.getQuantity() != null ? detailEntity.getQuantity() : BigDecimal.ZERO));
        invPurchaseInvoiceDTO.setTotalNet((invPurchaseInvoiceDTO.getTotalNet().add(detailEntity.getNet() != null ? detailEntity.getNet() : BigDecimal.ZERO)).setScale(3, BigDecimal.ROUND_HALF_UP));
        return invPurchaseInvoiceDTO;
    }

    private InvInvoiceDTO alumetalActivity(InvInvoiceDTO invPurchaseInvoiceDTO, InvDetailDTO detailEntity) {
        if (invPurchaseInvoiceDTO.getCompanyActivity().equals(CompanyActivityClassEnum.Alumetal)) {
            BigDecimal weight;
            weight = detailEntity.getWeight() == null ? BigDecimal.ZERO : detailEntity.getWeight();
            if (weight.compareTo(BigDecimal.ZERO) > 0) {
                if (detailEntity.getQuantity() != null) {
                    if (weight.compareTo(BigDecimal.ZERO) == 0) {
                        invPurchaseInvoiceDTO.setTotalWeight(invPurchaseInvoiceDTO.getTotalWeight().add(weight.multiply(detailEntity.getQuantity())));
                    } else {
                        invPurchaseInvoiceDTO.setTotalWeight(invPurchaseInvoiceDTO.getTotalWeight().add(weight));
                    }
                } else {
                    invPurchaseInvoiceDTO.setTotalWeight(invPurchaseInvoiceDTO.getTotalWeight().add(weight));
                }
            } else {
                invPurchaseInvoiceDTO.setTotalWeight(invPurchaseInvoiceDTO.getTotalWeight().add(BigDecimal.ZERO));
            }
        }
        return invPurchaseInvoiceDTO;
    }

    @Override
    public InvInvoiceDTO addInvPurchaseInvoiceDetail(InvInvoiceDTO invInvoiceDTO, Integer index) {
        try {

            if (invInvoiceDTO.getInvPurchaseInvoiceDetailList() != null && !invInvoiceDTO.getInvPurchaseInvoiceDetailList().isEmpty()) {
                String error = validateSave(invInvoiceDTO.getInvPurchaseInvoiceDetailList(),
                        invInvoiceDTO.getInvInventoryId() != null ? invInvoiceDTO.getInvInventoryId().getId() : null,
                        invInvoiceDTO.getScreenName(),
                        invInvoiceDTO.getInventorySetupDTO().getNegativeSell(),
                        (invInvoiceDTO.getLoadedSerial() != null), invInvoiceDTO.getInventorySetupDTO().getSellBuy(), invInvoiceDTO.getCompanyActivity());
                if (error != null && !error.isEmpty()) {
                    invInvoiceDTO.setMsg(error);
                    return invInvoiceDTO;
                }
            }

            InvDetailDTO invPurchaseInvoiceDetailsEntityNew = new InvDetailDTO();
            invPurchaseInvoiceDetailsEntityNew.setIndex(index);
            invPurchaseInvoiceDetailsEntityNew.setInvItemParentId(invInvoiceDTO.getInvParentItem());
            invPurchaseInvoiceDetailsEntityNew.setInventoryDTO(invInvoiceDTO.getInvInventoryId());
            invInvoiceDTO.setInvPurchaseInvoiceDetailsSelected(invPurchaseInvoiceDetailsEntityNew);
            invInvoiceDTO.getInvPurchaseInvoiceDetailList().add(invPurchaseInvoiceDetailsEntityNew);

        } catch (Exception e) {
            invInvoiceDTO.setMsg(e.toString());
        }
        return invInvoiceDTO;
    }

    @Override
    public String validateSave(List<InvDetailDTO> invPurchaseInvoiceDetailDTOList, Integer invtentortId,
            ScreenNameClassEnum screenNameClassEnum, Boolean negativeSell, Boolean isload, Boolean sellBuy, CompanyActivityClassEnum companyActivityClassEnum) {
        StringBuilder error = new StringBuilder();
        for (InvDetailDTO detailDTO : invPurchaseInvoiceDetailDTOList) {
            detailDTO = validateRow(detailDTO, invtentortId, screenNameClassEnum, negativeSell, isload, sellBuy, companyActivityClassEnum);
            if (detailDTO.getMsg() != null && !detailDTO.getMsg().isEmpty()) {
                error.append("\n ").append(detailDTO.getMsg()).append("\n").append(detailDTO.getItemsBarcodesDetail() != null ? detailDTO.getItemsBarcodesDetail().getName() : "يوجد سطر فارغ برجاء ملئ السطر الفارغ");
                return error.toString();
            }
        }
        return error.toString();
    }

    public InvDetailDTO validateRow(InvDetailDTO invDetailDTO, Integer inventoryId, ScreenNameClassEnum screenNameClassEnum,
            Boolean negativeSell, Boolean isload, Boolean sellBuy, CompanyActivityClassEnum companyActivityClassEnum) {
        StringBuilder error = new StringBuilder();

        if (invDetailDTO.getItemsBarcodesDetail() == null && invDetailDTO.getItemsBarcodesDetailTrans() != null) {
            invDetailDTO.setItemsBarcodesDetail(invDetailDTO.getItemsBarcodesDetailTrans());
        } else if (invDetailDTO.getItemsBarcodesDetail() != null && invDetailDTO.getItemsBarcodesDetailTrans() == null) {
            invDetailDTO.setItemsBarcodesDetailTrans(invDetailDTO.getItemsBarcodesDetail());
        } else if (invDetailDTO.getItemsBarcodesDetail() == null && invDetailDTO.getItemsBarcodesDetailTrans() == null) {
            error.append("\n يجب ادخال الصنف");
        }

        if (invDetailDTO.getDiscount() != null) {
            if (!Validation.isDiscountValid(invDetailDTO.getDiscount())) {
                error.append("\n نسبة الخصم أكبر من 100%");
            }
        }

        if (!validatePrice(invDetailDTO)) {
            error.append("\n يجب ادخال سعر للصنف");

        }

        if (!validateQuantity(invDetailDTO, companyActivityClassEnum, screenNameClassEnum)) {
            error.append("\n يجب ادخال كمية للصنف");
        }

        if (invDetailDTO.getQuantity() != null) {
            if (!validateQuantity(invDetailDTO, companyActivityClassEnum, screenNameClassEnum)) {
                error.append("\n يجب اختيار الكميه ");
            } else {

                if ((ScreenNameClassEnum.INVSALESINVOICEFORM.equals(screenNameClassEnum)
                        || ScreenNameClassEnum.INVPERMISSIONORDERFORM.equals(screenNameClassEnum)
                        || ScreenNameClassEnum.INVRESERVATIONFORM.equals(screenNameClassEnum)
                        || ScreenNameClassEnum.INVRETURNPURCHASEFORM.equals(screenNameClassEnum))) {
                    if (!negativeSell && (invDetailDTO.getIsInventoryItem() == 1)) {
                        StringBuilder ee = validateQuantityWhenSave(invDetailDTO, inventoryId, screenNameClassEnum, isload, sellBuy, companyActivityClassEnum);
                        if (!ee.toString().isEmpty()) {
                            error.append(ee);
                        }
                    }
                }
            }
        }

        invDetailDTO.setMsg(error.toString());
        return invDetailDTO;
    }

    @Override
    public Boolean validatePrice(InvDetailDTO detailDTO) {
        return detailDTO != null
                && detailDTO.getItemsBarcodesDetail() != null
                && detailDTO.getCost() != null
                && detailDTO.getCost().compareTo(BigDecimal.ZERO) >= 0;
    }

    @Override
    public Boolean validateQuantity(InvDetailDTO detailDTO, CompanyActivityClassEnum companyActivityClassEnum, ScreenNameClassEnum screenNameClassEnum) {
        if (companyActivityClassEnum.equals(CompanyActivityClassEnum.RetailSaleForClothFactory) && ScreenNameClassEnum.INVSALESINVOICEFORM.equals(screenNameClassEnum)) {
            return detailDTO != null && detailDTO.getItemsBarcodesDetail() != null
                    && (detailDTO.getQuantityOfOneUnit().multiply(detailDTO.getNumber())) != null
                    && (detailDTO.getQuantityOfOneUnit().multiply(detailDTO.getNumber())).compareTo(BigDecimal.ZERO) == 1;
        } else {
            return detailDTO != null && detailDTO.getItemsBarcodesDetail() != null
                    && detailDTO.getQuantity() != null
                    && detailDTO.getQuantity().compareTo(BigDecimal.ZERO) == 1;
        }

    }

    public StringBuilder validateQuantityWhenSave(InvDetailDTO detailDTO, Integer inventoryId, ScreenNameClassEnum screenNameClassEnum,
            Boolean isload, Boolean sellBuy, CompanyActivityClassEnum companyActivityClassEnum) {
        StringBuilder error = new StringBuilder();
        try {

            detailDTO = checkQuantityFromLoaded(isload, detailDTO, companyActivityClassEnum, screenNameClassEnum);

            if (detailDTO.getMsg() != null && !detailDTO.getMsg().isEmpty()) {
                error.append(detailDTO.getMsg());
                return error;
            }

            if (ScreenNameClassEnum.INVPERMISSIONORDERFORM.equals(screenNameClassEnum)
                    || ScreenNameClassEnum.INVSALESINVOICEFORM.equals(screenNameClassEnum)
                    || ScreenNameClassEnum.INVRETURNPURCHASEFORM.equals(screenNameClassEnum)
                    || ScreenNameClassEnum.INVRESERVATIONFORM.equals(screenNameClassEnum)) {
                BigDecimal avilableQuantity = quantityItemsStoreAddExitService.getQuantatyItemByinventory(inventoryId, detailDTO.getItemsBarcodesDetail().getItemId(), sellBuy);
                avilableQuantity = avilableQuantity.add(detailDTO.getOriginQuantity());
                if (avilableQuantity.compareTo(detailDTO.getTotalQuantityWithScrewing()) < 0) {
                    error.append("\n الكمية غير موجودة بالمخزن ");
                    setDefaultQuantity(detailDTO, companyActivityClassEnum, screenNameClassEnum);
                    return error;
                }
            }
        } catch (Exception e) {
//            saveError(e, "InvPurchaseInvoiceFormMB", "validateQuantityColum");
        }
        return error;
    }

    @Override
    public List<InvDetailDTO> updatePriceAndQuantityForDetail(Integer priceType, List<InvDetailDTO> invDetailDTOList,
            ScreenNameClassEnum screenNameClassEnum, CompanyActivityClassEnum activityClassEnum) {
        List<InvDetailDTO> detailDTOList = new ArrayList<>();
        for (InvDetailDTO dTO : invDetailDTOList) {
            dTO = findDataForItem(priceType, dTO, screenNameClassEnum, activityClassEnum);
            detailDTOList.add(dTO);
        }
        return detailDTOList;
    }

    @Override
    public InvDetailDTO validateItems(InvDetailDTO detailDTO, Integer inventoryId,
            ScreenNameClassEnum screenNameClassEnum, Boolean sellBuy,
            Integer priceType, CompanyActivityClassEnum activityClassEnum,
            Boolean sellNegative, Boolean isEdit) {
        try {
            if (detailDTO.getItemsBarcodesDetail() == null) {
                detailDTO = resetInvItem(detailDTO);
            } else {
                detailDTO.setItemsBarcodesDetailTrans(detailDTO.getItemsBarcodesDetail());

                if (detailDTO.getItemsBarcodesDetail().getUnitName() != null
                        && !"".equals(detailDTO.getItemsBarcodesDetail().getUnitName())) {
                    SymbolDTO unit = new SymbolDTO();
                    unit.setId(detailDTO.getItemsBarcodesDetail().getUnitId());
                    unit.setName(detailDTO.getItemsBarcodesDetail().getUnitName());
                    detailDTO.setUnitId(unit);
                    SymbolDTO paintColor = new SymbolDTO();
                    paintColor.setId(detailDTO.getItemsBarcodesDetail().getPaintColor());
                    paintColor.setName(detailDTO.getItemsBarcodesDetail().getPaintColorName());
                    detailDTO.setPaintColor(paintColor);
//                invPurchaseInvoiceDetailsValidate.setCost(invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getSellPrice());
                    detailDTO.setScrewing(detailDTO.getItemsBarcodesDetail().getScrewing());
                    detailDTO.setLength(detailDTO.getItemsBarcodesDetail().getLength());
                    detailDTO.setWeight(detailDTO.getItemsBarcodesDetail().getWeight() != null ? detailDTO.getItemsBarcodesDetail().getWeight() : BigDecimal.ZERO);
                    detailDTO.setItemName(detailDTO.getItemsBarcodesDetail().getItemId() == null ? "" : detailDTO.getItemsBarcodesDetail().getName());
                    detailDTO.setItemCode(detailDTO.getItemsBarcodesDetail().getItemId() == null ? "" : detailDTO.getItemsBarcodesDetail().getBarcode());
                    detailDTO.setColor(detailDTO.getItemsBarcodesDetail().getItemId() == null ? "" : detailDTO.getItemsBarcodesDetail().getPaintColorName());
                    detailDTO.setUnitName(detailDTO.getItemsBarcodesDetail().getItemId() == null ? "" : detailDTO.getItemsBarcodesDetail().getUnitName());
                    detailDTO.setIsInventoryItem(detailDTO.getItemsBarcodesDetail().getIsInventoryItem());
                    detailDTO = findDataForItem(priceType, detailDTO, screenNameClassEnum, activityClassEnum);
                    detailDTO = updateAvailableQuantityRow(screenNameClassEnum, detailDTO, inventoryId, sellBuy, sellNegative, isEdit);
                } else {
                    detailDTO = resetInvItem(detailDTO);
                    detailDTO.setMsg("هذا الصنف ليس لديه وحدات.");
                    detailDTO.setItemsBarcodesDetail(null);
                    return detailDTO;
                }
            }

        } catch (Exception e) {
            detailDTO.setMsg(e.toString());
            return detailDTO;
        }
        return detailDTO;
    }

    private InvDetailDTO findDataForItem(Integer priceType, InvDetailDTO invDetailDTO,
            ScreenNameClassEnum screenNameClassEnum, CompanyActivityClassEnum activityClassEnum) {
        if (activityClassEnum.equals(CompanyActivityClassEnum.RetailSaleForClothFactory) && ScreenNameClassEnum.INVSALESINVOICEFORM.equals(screenNameClassEnum)) {
            ItemsBarcodesDetailsView barcodesDetailsView = invDetailDTO.getItemsBarcodesDetail();

            if (barcodesDetailsView.getDiscountrate() != null
                    && barcodesDetailsView.getDiscountrate().compareTo(BigDecimal.ZERO) == 1) {
                invDetailDTO.setShowDiscount(true);
                invDetailDTO.setShowPrice(true);
            }
            if (priceType == 1) {
                invDetailDTO.setCost(barcodesDetailsView.getMaxPriceMen());
                invDetailDTO.setQuantityOfOneUnit(new BigDecimal(4));
            } else {
                invDetailDTO.setCost(barcodesDetailsView.getMaxPriceYoung());
                invDetailDTO.setQuantityOfOneUnit(new BigDecimal(3.5));
            }
            invDetailDTO.setQuantity(invDetailDTO.getQuantityOfOneUnit().multiply(invDetailDTO.getNumber()));
            invDetailDTO.setDiscount(barcodesDetailsView.getDiscountrate());
            invDetailDTO.setScrewing(barcodesDetailsView.getScrewing());
        } else {
            invDetailDTO = updatePriceRow(screenNameClassEnum, invDetailDTO);
        }
        return invDetailDTO;
    }

    private InvDetailDTO updatePriceRow(ScreenNameClassEnum screenNameClassEnum, InvDetailDTO detailDTO) {
        if (ScreenNameClassEnum.INVSALESINVOICEFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVRETURNSALESFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVQUOTATIONFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVRESERVATIONFORM.equals(screenNameClassEnum)) {
            detailDTO.setCost(detailDTO.getItemsBarcodesDetail().getSellPrice());
        }
        return detailDTO;
    }

    private InvDetailDTO resetInvItem(InvDetailDTO detailDTO) {
        try {
            detailDTO.setQuantity(null);
            detailDTO.setWeight(null);
            detailDTO.setCost(null);
            detailDTO.setDiscount(null);
            detailDTO.setUnitId(null);
        } catch (Exception e) {
            detailDTO.setMsg(e.toString());
        }
        return detailDTO;
    }

    @Override
    public InvDetailDTO updateAvailableQuantityRow(ScreenNameClassEnum screenNameClassEnum, InvDetailDTO detailDTO, Integer inventoryId, Boolean sellBuy, Boolean sellNegative, Boolean isEdit) {
        if (ScreenNameClassEnum.INVSALESINVOICEFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVPERMISSIONORDERFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVRESERVATIONFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVRETURNPURCHASEFORM.equals(screenNameClassEnum)) {
            detailDTO.setTotalQuantityWithScrewing(detailDTO.getQuantity().multiply(detailDTO.getScrewing()));
            detailDTO.setAvailableQuantityRow(quantityItemsStoreAddExitService.getQuantatyItemByinventory(inventoryId, detailDTO.getItemsBarcodesDetail().getItemId(), sellBuy));
            if ((detailDTO.getAvailableQuantityRow() == null || detailDTO.getAvailableQuantityRow().compareTo(BigDecimal.ZERO) < 1) && !sellNegative && !isEdit) {
                detailDTO.setItemsBarcodesDetail(null);
                detailDTO.setItemsBarcodesDetailTrans(null);
                detailDTO.setMsg("الصنف لا يوجد به كمية بالمخزن");
            }
        }
        return detailDTO;
    }

    @Override
    public InvDetailDTO validatePriceColumn(InvDetailDTO detailDTO) {
        try {
            if (!validatePrice(detailDTO)) {
                if (detailDTO.getItemsBarcodesDetail() != null) {
                    detailDTO.setMsg("يجب ان يكون السعر أكبر من او يساوي الصفر");
                } else {
                    detailDTO.setMsg("يجب اختيار صنف اولا");
                }
                detailDTO.setCost(BigDecimal.ZERO);
            }
        } catch (Exception e) {
            detailDTO.setMsg(e.toString());
        }
        return detailDTO;
    }

    @Override
    public InvDetailDTO validateQuantityColum(InvDetailDTO detailDTO, Boolean isload, ScreenNameClassEnum screenNameClassEnum,
            Boolean negativeSell, CompanyActivityClassEnum companyActivityClassEnum) {
        try {

            if (!validateQuantity(detailDTO, companyActivityClassEnum, screenNameClassEnum)) {
                if (detailDTO.getItemsBarcodesDetail() != null) {
                    detailDTO.setMsg("يجب ان تكون الكمية أكبر من الصفر");
                } else {
                    detailDTO.setMsg("يجب اختيار صنف اولا");
                }
                setDefaultQuantity(detailDTO, companyActivityClassEnum, screenNameClassEnum);
            } else {
                if (companyActivityClassEnum.equals(CompanyActivityClassEnum.RetailSaleForClothFactory) && screenNameClassEnum.equals(ScreenNameClassEnum.INVSALESINVOICEFORM)) {
                    detailDTO.setQuantity(detailDTO.getNumber().multiply(detailDTO.getQuantityOfOneUnit()));
                    detailDTO.setTotalQuantityWithScrewing(detailDTO.getQuantity().multiply(detailDTO.getScrewing()));
                }
                detailDTO = validateTotalQuantity(detailDTO, isload, screenNameClassEnum, negativeSell, companyActivityClassEnum);
                if (detailDTO.getMsg() != null && !detailDTO.getMsg().isEmpty()) {
                    setDefaultQuantity(detailDTO, companyActivityClassEnum, screenNameClassEnum);
                }
            }
        } catch (Exception e) {
            detailDTO.setMsg(e.toString());
        }
        return detailDTO;
    }

    private void setDefaultQuantity(InvDetailDTO detailDTO, CompanyActivityClassEnum companyActivityClassEnum, ScreenNameClassEnum screenNameClassEnum) {
        if (companyActivityClassEnum.equals(CompanyActivityClassEnum.RetailSaleForClothFactory) && ScreenNameClassEnum.INVSALESINVOICEFORM.equals(screenNameClassEnum)) {
            detailDTO.setNumber(detailDTO.getOriginQuantity());
            detailDTO.setQuantity(detailDTO.getNumber().multiply(detailDTO.getQuantityOfOneUnit()));
        } else {
            detailDTO.setQuantity(detailDTO.getOriginQuantity());
        }
        detailDTO.setTotalQuantityWithScrewing(detailDTO.getQuantity().multiply(detailDTO.getScrewing()));
    }

    private InvDetailDTO validateTotalQuantity(InvDetailDTO detailDTO, Boolean isload, ScreenNameClassEnum screenNameClassEnum, Boolean negativeSell,
            CompanyActivityClassEnum companyActivityClassEnum) {
        if ((ScreenNameClassEnum.INVSALESINVOICEFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVPERMISSIONORDERFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVRESERVATIONFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVRETURNPURCHASEFORM.equals(screenNameClassEnum))) {
            BigDecimal difQuantity = detailDTO.getTotalQuantityWithScrewing();
            if (detailDTO.getId() != null) {
                difQuantity = difQuantity.subtract(detailDTO.getOriginQuantity());
            }

            if (((difQuantity).compareTo(detailDTO.getAvailableQuantityRow()) > 0) && !negativeSell && (detailDTO.getIsInventoryItem() == 1)) {
                detailDTO.setMsg("الكميه اكبر من الكميه الموجوده في المخزن ");
                setDefaultQuantity(detailDTO, companyActivityClassEnum, screenNameClassEnum);
            }
        }

        checkQuantityFromLoaded(isload, detailDTO, companyActivityClassEnum, screenNameClassEnum);
        return detailDTO;
    }

    private InvDetailDTO checkQuantityFromLoaded(Boolean isload, InvDetailDTO detailDTO,
            CompanyActivityClassEnum companyActivityClassEnum, ScreenNameClassEnum screenNameClassEnum) {
        if (isload) {
            if (((detailDTO.getQuantity()).multiply(detailDTO.getScrewing())).compareTo(detailDTO.getRealQuantity()) > 0) {
                detailDTO.setMsg("الكميه اكبر من الكميه الموجوده في الفاتورة ");
                setDefaultQuantity(detailDTO, companyActivityClassEnum, screenNameClassEnum);
            }
        }
        return detailDTO;
    }

    @Override
    public InvDetailDTO validateDiscountColumn(InvDetailDTO detailDTO) {
        try {
            if (!validateDiscount(detailDTO)) {
                detailDTO.setDiscount(BigDecimal.ZERO);
                detailDTO.setMsg("يجب ألا يزيد الخصم عن 100");
            }
        } catch (Exception e) {
            detailDTO.setMsg(e.toString());
        }
        return detailDTO;
    }

    public Boolean validateDiscount(InvDetailDTO detailDTO) {
        return Validation.isDiscountValid(detailDTO.getDiscount());
    }

    public Boolean validateUnit(InvDetailDTO detailDTO) {

        return detailDTO != null && detailDTO.getItemsBarcodesDetail() != null
                && detailDTO.getItemsBarcodesDetail().getUnitId() != null
                && detailDTO.getUnitId() != null;

    }

    @Override
    public InvDetailDTO fillItemDetail(Boolean isOld, InvDetailDTO dTO,
            Integer id, Integer invPurchaseInvoiceDetailId, Integer serial, Integer branchId, Integer invInvoiceId,
            Integer index, Date createdDate, Integer createdBy,
            Integer itemId, String itemBarcode, String itemName, Integer clothNumber,
            Integer unitSerial, String unitName, BigDecimal number,
            Integer idParent, String itemBarcodeParent, String itemNameParent,
            CompanyActivityClassEnum companyActivityClassEnum, ScreenNameClassEnum screenNameClassEnum) {

        if (companyActivityClassEnum.equals(CompanyActivityClassEnum.RetailSaleForClothFactory)
                && (ScreenNameClassEnum.PROFACTORYDELIVERY.equals(screenNameClassEnum)
                || ScreenNameClassEnum.PROSENDTOGALLARY.equals(screenNameClassEnum)
                || ScreenNameClassEnum.PRORECIEVEGALLARY.equals(screenNameClassEnum))) {
            if (idParent != null) {
                InvItemDTO invItemDTO = new InvItemDTO();
                invItemDTO.setId(idParent);
                invItemDTO.setCode(itemBarcodeParent);
                invItemDTO.setName(itemNameParent);

                dTO.setInvItemParentId(invItemDTO);
            }
        }
        if (isOld) {
            dTO.setId(id);
        }
        dTO.setSerial(serial);
        dTO.setItemCode(itemBarcode);
        dTO.setItemName(itemName);
        dTO.setUnitName(unitName);
        dTO.setNumber(number);
        dTO.setClothNumber(clothNumber);
        dTO.setInvPurchaseInvoiceDetailId(invPurchaseInvoiceDetailId);
        if (itemId != null) {
            InvItemDTO invItemDTO = new InvItemDTO();
            invItemDTO.setId(itemId);
            dTO.setItemId(invItemDTO);
        }

        dTO.setBranchId(branchId);
        dTO.setInvInvoiceId(invInvoiceId);
        dTO.setIndex(index);
        dTO.setCreatedDate(createdDate);
        dTO.setCreatedBy(createdBy);

        return dTO;
    }

    @Override
    public Date sumDateToDueDate(Date date, Date dueDate, Integer duePeroid) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, (duePeroid != null ? duePeroid : 0));
        return dueDate = cal.getTime();
    }

    @Override
    public Integer subtractDatefromDueDate(Date date, Date dueDate, Integer duePeroid) {
        LocalDate ld = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate ld1 = dueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();     //2
        Duration duration = Duration.between(ld.atStartOfDay(), ld1.atStartOfDay());
        System.out.println("duration.toDays(); " + duration.toDays());
        if ((int) (long) duration.toDays() < 0) {
            duePeroid = 0;
            sumDateToDueDate(date, dueDate, duePeroid);
        } else {

            duePeroid = ((int) (long) duration.toDays());
        }
        return duePeroid;
    }

    public Boolean validateNumber(InvDetailDTO detailDTO) {
        return detailDTO != null
                && detailDTO.getItemId() != null
                && detailDTO.getNumber() != null;
    }

}
