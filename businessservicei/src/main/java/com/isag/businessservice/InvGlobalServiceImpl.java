/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.common.Validation;
import com.isag.define.CompanyActivityClassEnum;
import com.isag.define.ScreenNameClassEnum;
import com.isag.dto.InvDetailDTO;
import com.isag.dto.InvInvoiceDTO;
import com.isag.dto.SymbolDTO;
import com.isag.views.ItemsBarcodesDetailsView;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class InvGlobalServiceImpl implements InvGlobalService {

    @EJB
    private QuantityItemsStoreByDateService quantityItemsStoreAddExitService;
    @EJB
    private InvPurchaseInvoiceDetailsService invPurchaseInvoiceDetailsService;

    @Override
    public InvDetailDTO calculateDetailTotalsRow(InvDetailDTO detailEntity, InvInvoiceDTO invInvoiceDTO, Boolean isNeeadQuantity) {
        BigDecimal quantity;
        BigDecimal weight;
        BigDecimal cost;
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal valueAfterDiscount = BigDecimal.ZERO;
        if (detailEntity.getItemsBarcodesDetail() == null && detailEntity.getItemsBarcodesDetailTrans() != null) {
            detailEntity.setItemsBarcodesDetail(detailEntity.getItemsBarcodesDetailTrans());
        }
        quantity = new BigDecimal((calculateQuantity(detailEntity, invInvoiceDTO)).setScale(3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString());
        weight = detailEntity.getWeight() == null ? BigDecimal.ZERO : detailEntity.getWeight();
        cost = detailEntity.getCost() != null ? detailEntity.getCost().multiply(invInvoiceDTO.getRate()) : BigDecimal.ZERO;

        if (invInvoiceDTO.getCompanyActivity() != null) {
            if (!invInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVOPENINGBALANCEITEMFORM)
                    && invInvoiceDTO.getCompanyActivity().equals(CompanyActivityClassEnum.Alumetal)) {
                total = AlumetalActivity(weight, total, quantity, cost);
            } else if (invInvoiceDTO.getCompanyActivity().equals(CompanyActivityClassEnum.RetailSale)
                    || invInvoiceDTO.getCompanyActivity().equals(CompanyActivityClassEnum.CERAMIC)
                    || invInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVOPENINGBALANCEITEMFORM)) {
                total = RetailActivity(total, quantity, cost);
            }
        } else {
            detailEntity.setMsg("يجب تحديد نشاط الشركة برجاء النواصل مع الدعم");
        }

        if ((invInvoiceDTO.getCompanyActivity().equals(CompanyActivityClassEnum.CERAMIC) && invInvoiceDTO.getTypeActivity() != null && 1 == invInvoiceDTO.getTypeActivity())
                || (invInvoiceDTO.getCompanyActivity().equals(CompanyActivityClassEnum.CERAMIC) && invInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVOPENINGBALANCEITEMFORM))) {
            valueAfterDiscount = calFinalPrice(detailEntity, invInvoiceDTO.getCompanyActivity());
        } else {
            if (detailEntity.getValueRowDiscount() != null && detailEntity.getValueRowDiscount().compareTo(BigDecimal.ZERO) == 1) {
                BigDecimal totalNet = detailEntity.getCost().multiply(detailEntity.getQuantity());
                if (totalNet.compareTo(BigDecimal.ZERO) == 1) {

                    valueAfterDiscount = total.subtract(detailEntity.getValueRowDiscount()).add(detailEntity.getAddTotal());
                    detailEntity.setDiscount(detailEntity.getValueRowDiscount().divide(totalNet, 3, RoundingMode.HALF_DOWN).multiply(new BigDecimal(100)));
                }
            } else if (detailEntity.getDiscount() != null && detailEntity.getDiscount().compareTo(BigDecimal.ZERO) == 1) {
                valueAfterDiscount = total.subtract((total.multiply(detailEntity.getDiscount()).divide(new BigDecimal(100)))).add(detailEntity.getAddTotal());
                detailEntity.setValueRowDiscount(total.subtract(valueAfterDiscount));
            } else {
                valueAfterDiscount = total.add(detailEntity.getAddTotal());
            }
            detailEntity.setValueAfterDiscount(valueAfterDiscount);
            detailEntity.setTotalBeforeDiscount(new BigDecimal(total.setScale(3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()));
        }

        detailEntity.setNet(new BigDecimal(valueAfterDiscount.setScale(3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()));
        detailEntity.setTotalQuantityWithScrewing(new BigDecimal((detailEntity.getQuantity().multiply(detailEntity.getScrewing())).setScale(3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()));
        detailEntity.setDifferanceQuantity(detailEntity.getDifferanceQuantity());
        if (isNeeadQuantity && invInvoiceDTO.getInventorySetupDTO() != null && !invInvoiceDTO.getInventorySetupDTO().getNegativeSell()) {
            detailEntity = updateAvailableQuantityRow(invInvoiceDTO.getScreenName(), detailEntity, invInvoiceDTO.getInvInventoryId() != null ? invInvoiceDTO.getInvInventoryId().getId() : null, invInvoiceDTO.getInventorySetupDTO().getSellBuy(), invInvoiceDTO.getInventorySetupDTO().getNegativeSell(), invInvoiceDTO.getIsEdit());
        }
        return detailEntity;
    }

    private BigDecimal calDiscountAndAddonsRow(BigDecimal total, InvDetailDTO detailEntity, CompanyActivityClassEnum companyActivityClassEnum) {
        BigDecimal discount = null;
        BigDecimal valueAfterDiscount = total;

        if (detailEntity.getCadoo()) {
            discount = new BigDecimal(100);
            detailEntity.setDiscount(new BigDecimal(100));
        } else {
            discount = detailEntity.getDiscount() != null ? detailEntity.getDiscount() : BigDecimal.ZERO;
        }

        if (companyActivityClassEnum.equals(CompanyActivityClassEnum.CERAMIC)) {
            if (detailEntity.getDisc1() != null) {
                valueAfterDiscount = total.subtract((total.multiply(detailEntity.getDisc1()).divide(new BigDecimal(100))));
                if (detailEntity.getDisc2() != null) {
                    valueAfterDiscount = valueAfterDiscount.subtract((valueAfterDiscount.multiply(detailEntity.getDisc2()).divide(new BigDecimal(100))));
                    if (detailEntity.getDisc3() != null) {
                        valueAfterDiscount = valueAfterDiscount.subtract((valueAfterDiscount.multiply(detailEntity.getDisc3()).divide(new BigDecimal(100))));
                    }
                }
            }
        } else {
            valueAfterDiscount = total.subtract((total.multiply(detailEntity.getDiscount()).divide(new BigDecimal(100))));
        }
        valueAfterDiscount = valueAfterDiscount.add((detailEntity.getAdd1().multiply(detailEntity.getQuantity())).add((detailEntity.getAdd2().multiply(detailEntity.getQuantity())).add((detailEntity.getAdd3().multiply(detailEntity.getQuantity())))));
        detailEntity.setValuefracture(detailEntity.getCost().multiply(detailEntity.getFractureNumber()));
        valueAfterDiscount = valueAfterDiscount.subtract(detailEntity.getValuefracture());
        return valueAfterDiscount;
    }

    private BigDecimal calFinalPrice(InvDetailDTO detailEntity, CompanyActivityClassEnum companyActivityClassEnum) {
        BigDecimal finalPrice = detailEntity.getCost();
        BigDecimal valueAfterDiscount = BigDecimal.ZERO;

        if (detailEntity.getDisc1() != null) {
            finalPrice = finalPrice.subtract((finalPrice.multiply(detailEntity.getDisc1()).divide(new BigDecimal(100))));
            if (detailEntity.getDisc2() != null) {
                finalPrice = finalPrice.subtract((finalPrice.multiply(detailEntity.getDisc2()).divide(new BigDecimal(100))));
                if (detailEntity.getDisc3() != null) {
                    finalPrice = finalPrice.subtract((finalPrice.multiply(detailEntity.getDisc3()).divide(new BigDecimal(100))));
                }
            }
        }

        BigDecimal totalAdd = (detailEntity.getAdd1().add(detailEntity.getAdd2().add(detailEntity.getAdd3())));

        finalPrice = finalPrice.add(totalAdd);
        detailEntity.setFinalPrice(new BigDecimal(finalPrice.setScale(3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()));
        detailEntity.setValuefracture(new BigDecimal((finalPrice.multiply(detailEntity.getFractureNumber()).multiply(detailEntity.getCartonsCapacity())).setScale(3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()));
        valueAfterDiscount = finalPrice.multiply(detailEntity.getQuantity());
        detailEntity.setTotalBeforefracture(new BigDecimal(valueAfterDiscount.setScale(3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()));
        valueAfterDiscount = new BigDecimal((valueAfterDiscount.subtract(detailEntity.getValuefracture())).setScale(3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString());

        return valueAfterDiscount;
    }

    private BigDecimal calculateQuantity(InvDetailDTO detailEntity, InvInvoiceDTO invInvoiceDTO) {
        BigDecimal quantity = BigDecimal.ZERO;
        if ((invInvoiceDTO.getCompanyActivity().equals(CompanyActivityClassEnum.CERAMIC) && invInvoiceDTO.getTypeActivity() != null && 1 == invInvoiceDTO.getTypeActivity())
                || (invInvoiceDTO.getCompanyActivity().equals(CompanyActivityClassEnum.CERAMIC) && invInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVOPENINGBALANCEITEMFORM))
                || (invInvoiceDTO.getCompanyActivity().equals(CompanyActivityClassEnum.CERAMIC) && invInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVTRANSFERINVENTORY))
                || (invInvoiceDTO.getCompanyActivity().equals(CompanyActivityClassEnum.CERAMIC) && (invInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVUPDATE)) && invInvoiceDTO.getTypeActivity() != null && invInvoiceDTO.getTypeActivity() == 1)) {
            detailEntity.setQuantity(new BigDecimal((detailEntity.getCartonsCapacity().multiply(detailEntity.getCartonsNumber())).setScale(3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()));
            detailEntity.setActualQuantity(new BigDecimal((detailEntity.getCartonsCapacity().multiply(detailEntity.getCartonsNumber())).setScale(3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()));
            quantity = new BigDecimal((detailEntity.getCartonsCapacity().multiply(detailEntity.getCartonsNumber())).setScale(3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString());
        } else {
            quantity = detailEntity.getQuantity() != null ? detailEntity.getQuantity() : BigDecimal.ZERO;
            if (quantity.equals(BigDecimal.ZERO) && invInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVSALESINVOICEFORM)) {
                quantity = BigDecimal.ONE;
            }
            detailEntity.setQuantity(new BigDecimal(quantity.setScale(3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()));
        }
        return quantity;
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
            invPurchaseInvoiceDTO.setTotalQuatityCost(BigDecimal.ZERO);
            invPurchaseInvoiceDTO.setTotalWeight(BigDecimal.ZERO);
            invPurchaseInvoiceDTO.setDiscountValue(BigDecimal.ZERO);
            if (invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList() != null && !invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList().isEmpty()) {
                List<InvDetailDTO> invPurchaseInvoiceDetailDTOList = new ArrayList<>();

                for (InvDetailDTO detailEntity : invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList()) {
                    // حساب الاجماليات الخاصة بكل تفصيلى على حده
                    detailEntity = calculateDetailTotalsRow(detailEntity, invPurchaseInvoiceDTO, false);
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

        invPurchaseInvoiceDTO.setTotalNetAfterDiscount(new BigDecimal((invPurchaseInvoiceDTO.getTotalNet().subtract(invPurchaseInvoiceDTO.getDiscountValue() != null ? invPurchaseInvoiceDTO.getDiscountValue() : BigDecimal.ZERO)).setScale(3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()));
        invPurchaseInvoiceDTO = calculateTaxDiscValue(invPurchaseInvoiceDTO);
        invPurchaseInvoiceDTO = calculateTaxValue(invPurchaseInvoiceDTO);
        invPurchaseInvoiceDTO.setFinalNet(new BigDecimal((invPurchaseInvoiceDTO.getTotalNetAfterDiscount().add(invPurchaseInvoiceDTO.getTaxvalue() != null ? invPurchaseInvoiceDTO.getTaxvalue() : BigDecimal.ZERO).subtract(invPurchaseInvoiceDTO.getTaxdiscvalue() != null ? invPurchaseInvoiceDTO.getTaxdiscvalue() : BigDecimal.ZERO)).setScale(3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()));
        return invPurchaseInvoiceDTO;
    }

    private void resetHeadTotals(InvInvoiceDTO invPurchaseInvoiceDTO) {
        invPurchaseInvoiceDTO.setTotalWithTaxValue(BigDecimal.ZERO);
        invPurchaseInvoiceDTO.setTaxdiscvalue(BigDecimal.ZERO);
        invPurchaseInvoiceDTO.setTaxvalue(BigDecimal.ZERO);
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
                        invPurchaseInvoiceDTO.setDiscountValue(new BigDecimal((invPurchaseInvoiceDTO.getDiscount() != null ? invPurchaseInvoiceDTO.getDiscount() : BigDecimal.ZERO).setScale(3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()));
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
            if (invPurchaseInvoiceDTO.getTaxflag() != null && invPurchaseInvoiceDTO.getTaxflag()
                    && invPurchaseInvoiceDTO.getInventorySetupDTO() != null && invPurchaseInvoiceDTO.getInventorySetupDTO().getTaxValue() != null) {
                invPurchaseInvoiceDTO.setTaxvalue(invPurchaseInvoiceDTO.getTotalNetAfterDiscount().multiply(invPurchaseInvoiceDTO.getInventorySetupDTO().getTaxValue()));
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
        invPurchaseInvoiceDTO.setTotalQuatityCost(invPurchaseInvoiceDTO.getTotalQuatityCost().add(detailEntity.getTotalQuantityCost() != null ? detailEntity.getTotalQuantityCost() : BigDecimal.ZERO));
        invPurchaseInvoiceDTO.setTotalNet(new BigDecimal((invPurchaseInvoiceDTO.getTotalNet().add(detailEntity.getNet() != null ? detailEntity.getNet() : BigDecimal.ZERO)).setScale(3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()));
        invPurchaseInvoiceDTO.setTotalBeforeDiscount(new BigDecimal((invPurchaseInvoiceDTO.getTotalBeforeDiscount().add(detailEntity.getTotalBeforeDiscount() != null ? detailEntity.getTotalBeforeDiscount() : BigDecimal.ZERO)).setScale(3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()));
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
                        (invInvoiceDTO.getLoadedSerial() != null), invInvoiceDTO.getInventorySetupDTO().getSellBuy(), invInvoiceDTO.getSupplierInvoiceNumber());
                if (error != null && !error.isEmpty()) {
                    invInvoiceDTO.setMsg(error);
                    return invInvoiceDTO;
                }
            }

            InvDetailDTO invPurchaseInvoiceDetailsEntityNew = new InvDetailDTO();
            invPurchaseInvoiceDetailsEntityNew.setIndex(index);
            if (invInvoiceDTO.getOrganizationSiteId() != null) {
                invPurchaseInvoiceDetailsEntityNew.setDiscount(invInvoiceDTO.getOrganizationSiteId().getDiscount());
            }
            invInvoiceDTO.setItemsNumber(Boolean.TRUE);

            if (invInvoiceDTO.getScreenName() != null && invInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVPOSREST)) {
                if (invInvoiceDTO.getItemsBarcodesDetailsViewSelected() != null) {
                    invPurchaseInvoiceDetailsEntityNew.setItemsBarcodesDetail(invInvoiceDTO.getItemsBarcodesDetailsViewSelected());
                    invPurchaseInvoiceDetailsEntityNew.setItemsBarcodesDetailTrans(invInvoiceDTO.getItemsBarcodesDetailsViewSelected());
                    invPurchaseInvoiceDetailsEntityNew.setItemId(invInvoiceDTO.getItemsBarcodesDetailsViewSelected().getInvItem());
                }
            }

            invInvoiceDTO.setInvPurchaseInvoiceDetailsSelected(invPurchaseInvoiceDetailsEntityNew);
            invInvoiceDTO.getInvPurchaseInvoiceDetailList().add(invPurchaseInvoiceDetailsEntityNew);

        } catch (Exception e) {
            invInvoiceDTO.setMsg(e.toString());
        }
        return invInvoiceDTO;
    }

    @Override
    public String validateSave(List<InvDetailDTO> invPurchaseInvoiceDetailDTOList, Integer invtentortId,
            ScreenNameClassEnum screenNameClassEnum, Boolean negativeSell, Boolean isload,
            Boolean sellBuy, String invoiceSupplierNumber) {
        StringBuilder error = new StringBuilder();
        for (InvDetailDTO detailDTO : invPurchaseInvoiceDetailDTOList) {
            detailDTO = validateRow(detailDTO, invtentortId, screenNameClassEnum, negativeSell, isload, sellBuy, invoiceSupplierNumber);
            if (detailDTO.getMsg() != null && !detailDTO.getMsg().isEmpty()) {
                error.append("\n ").append(detailDTO.getMsg()).append("\n").append(detailDTO.getItemsBarcodesDetail() != null ? detailDTO.getItemsBarcodesDetail().getName() : "يوجد سطر فارغ برجاء ملئ السطر الفارغ");
                return error.toString();
            }
        }
        return error.toString();
    }

    public InvDetailDTO validateRow(InvDetailDTO invDetailDTO, Integer inventoryId, ScreenNameClassEnum screenNameClassEnum,
            Boolean negativeSell, Boolean isload, Boolean sellBuy, String invoiceSupplierNumber) {
        StringBuilder error = new StringBuilder();

        if (invDetailDTO.getItemsBarcodesDetail() == null && invDetailDTO.getItemsBarcodesDetailTrans() != null) {
            invDetailDTO.setItemsBarcodesDetail(invDetailDTO.getItemsBarcodesDetailTrans());
        } else if (invDetailDTO.getItemsBarcodesDetail() != null && invDetailDTO.getItemsBarcodesDetailTrans() == null) {
            invDetailDTO.setItemsBarcodesDetailTrans(invDetailDTO.getItemsBarcodesDetail());
        } else if (invDetailDTO.getItemsBarcodesDetail() == null && invDetailDTO.getItemsBarcodesDetailTrans() == null) {
            error.append("\n يجب ادخال الصنف");
        }

        if ((ScreenNameClassEnum.INVUPDATE.equals(screenNameClassEnum))) {
            if (invDetailDTO.getActualQuantity() == null && invDetailDTO.getActualQuantityBak() != null) {
                invDetailDTO.setActualQuantity(invDetailDTO.getActualQuantityBak());
            } else if (invDetailDTO.getActualQuantity() != null && invDetailDTO.getActualQuantityBak() == null) {
                invDetailDTO.setActualQuantityBak(invDetailDTO.getActualQuantity());
            }

            if (!validateActualQuantity(invDetailDTO)) {
                error.append("\n يجب ادخال كمية فعلية");
            }

        } else {
            if (invDetailDTO.getDiscount() != null) {
                if (!Validation.isDiscountValid(invDetailDTO.getDiscount())) {
                    error.append("\n نسبة الخصم أكبر من 100%");
                }
            }

            if (!validatePrice(invDetailDTO)) {
                error.append("\n يجب ادخال سعر للصنف");

            }

            if (!validateQuantity(invDetailDTO)) {
                error.append("\n يجب ادخال كمية للصنف");
            }

            if (invDetailDTO.getQuantity() != null) {
                if (!validateQuantity(invDetailDTO)) {
                    error.append("\n يجب اختيار الكميه ");
                } else {

                    if ((ScreenNameClassEnum.INVSALESINVOICEFORM.equals(screenNameClassEnum)
                            || ScreenNameClassEnum.INVPERMISSIONORDERFORM.equals(screenNameClassEnum)
                            || ScreenNameClassEnum.INVRESERVATIONFORM.equals(screenNameClassEnum)
                            || ScreenNameClassEnum.INVTRANSFERINVENTORY.equals(screenNameClassEnum)
                            || ScreenNameClassEnum.INVRETURNPURCHASEFORM.equals(screenNameClassEnum))) {
                        if ((invoiceSupplierNumber == null || invoiceSupplierNumber.isEmpty()) && !negativeSell && (invDetailDTO.getIsInventoryItem() == 1)) {
                            invDetailDTO = validateQuantityWhenSave(invDetailDTO, inventoryId, screenNameClassEnum, isload, sellBuy);
                            if (invDetailDTO.getMsg() != null && !invDetailDTO.getMsg().isEmpty()) {
                                error.append(invDetailDTO.getMsg());
                            }
                        }
                    }
                }
            }
        }

        invDetailDTO.setMsg(error.toString());
        return invDetailDTO;
    }

    @Override
    public Boolean validateNet(InvDetailDTO detailDTO) {
        return detailDTO != null
                && detailDTO.getItemsBarcodesDetail() != null
                && detailDTO.getNet() != null
                && detailDTO.getNet().compareTo(BigDecimal.ZERO) >= 0;
    }

    @Override
    public Boolean validatePrice(InvDetailDTO detailDTO) {
        return detailDTO != null
                && detailDTO.getItemsBarcodesDetail() != null
                && detailDTO.getCost() != null
                && detailDTO.getCost().compareTo(BigDecimal.ZERO) >= 0;
    }

    @Override
    public Boolean validateActualQuantity(InvDetailDTO detailDTO) {
        return detailDTO != null
                && detailDTO.getItemsBarcodesDetail() != null
                && detailDTO.getCost() != null
                && detailDTO.getCost().compareTo(BigDecimal.ZERO) >= 0;
    }

    @Override
    public Boolean validateQuantity(InvDetailDTO detailDTO) {

        return detailDTO != null && detailDTO.getItemsBarcodesDetail() != null
                && detailDTO.getQuantity() != null
                && detailDTO.getQuantity().compareTo(BigDecimal.ZERO) == 1;

    }

    public InvDetailDTO validateQuantityWhenSave(InvDetailDTO detailDTO, Integer inventoryId, ScreenNameClassEnum screenNameClassEnum, Boolean isload, Boolean sellBuy) {
        StringBuilder error = new StringBuilder();
        try {

            detailDTO = checkQuantityFromLoaded(isload, detailDTO);

            if (detailDTO.getMsg() != null && !detailDTO.getMsg().isEmpty()) {
                error.append(detailDTO.getMsg());
                return detailDTO;
            }

            if (ScreenNameClassEnum.INVPERMISSIONORDERFORM.equals(screenNameClassEnum)
                    || ScreenNameClassEnum.INVSALESINVOICEFORM.equals(screenNameClassEnum)
                    || ScreenNameClassEnum.INVRETURNPURCHASEFORM.equals(screenNameClassEnum)
                    || ScreenNameClassEnum.INVTRANSFERINVENTORY.equals(screenNameClassEnum)
                    || ScreenNameClassEnum.INVRESERVATIONFORM.equals(screenNameClassEnum)) {
                BigDecimal avilableQuantity = quantityItemsStoreAddExitService.getQuantatyItemByinventory(inventoryId, detailDTO.getItemsBarcodesDetail().getItemId(), sellBuy, null);
                avilableQuantity = avilableQuantity.add(detailDTO.getOriginQuantity());
                detailDTO.setAvailableQuantityRow(avilableQuantity);
                if (avilableQuantity.compareTo(detailDTO.getTotalQuantityWithScrewing()) < 0) {
                    error.append("\n الكمية غير موجودة بالمخزن ");
                    detailDTO.setMsg("\n الكمية غير موجودة بالمخزن ");
                    detailDTO = setDefaultQuantity(detailDTO);
                    return detailDTO;
                }
            }
        } catch (Exception e) {
//            saveError(e, "InvPurchaseInvoiceFormMB", "validateQuantityColum");
        }
        return detailDTO;
    }

    @Override
    public InvDetailDTO validateItems(InvDetailDTO detailDTO, Integer inventoryId, ScreenNameClassEnum screenNameClassEnum,
            Boolean sellBuy, Boolean sellNegative, Boolean isEdit, CompanyActivityClassEnum companyActivityClassEnum) {
        try {
            if (detailDTO.getItemsBarcodesDetail() == null) {
                detailDTO = resetInvItem(detailDTO);
            } else {
                detailDTO.setItemsBarcodesDetailTrans(detailDTO.getItemsBarcodesDetail());

                if (detailDTO.getItemsBarcodesDetail().getUnitName() != null
                        && !"".equals(detailDTO.getItemsBarcodesDetail().getUnitName())) {
                    if (companyActivityClassEnum.equals(CompanyActivityClassEnum.CERAMIC)) {
                        detailDTO.setCost(detailDTO.getItemsBarcodesDetail().getSellPrice());
                    } else if (screenNameClassEnum.INVPURCHASEINVOICEFORM.equals(screenNameClassEnum)) {
                        detailDTO.setCost(invPurchaseInvoiceDetailsService.findLastCost(detailDTO.getItemsBarcodesDetail().getItemId()));
                    }
                    SymbolDTO unit = new SymbolDTO();
                    unit.setId(detailDTO.getItemsBarcodesDetail().getUnitId());
                    unit.setName(detailDTO.getItemsBarcodesDetail().getUnitName());
                    detailDTO.setUnitId(unit);
                    SymbolDTO paintColor = new SymbolDTO();
                    paintColor.setId(detailDTO.getItemsBarcodesDetail().getPaintColor());
                    paintColor.setName(detailDTO.getItemsBarcodesDetail().getPaintColorName());
                    detailDTO.setPaintColor(paintColor);
                    detailDTO.setAdd1(detailDTO.getItemsBarcodesDetail().getAdd1());
                    detailDTO.setAdd2(detailDTO.getItemsBarcodesDetail().getAdd2());
                    detailDTO.setAdd3(detailDTO.getItemsBarcodesDetail().getAdd3());
                    detailDTO.setDiscount(detailDTO.getItemsBarcodesDetail().getDisc1());
                    detailDTO.setDisc1(detailDTO.getItemsBarcodesDetail().getDisc1());
                    detailDTO.setDisc2(detailDTO.getItemsBarcodesDetail().getDisc2());
                    detailDTO.setDisc3(detailDTO.getItemsBarcodesDetail().getDisc3());
                    detailDTO.setCartonsCapacity(detailDTO.getItemsBarcodesDetail().getCartonsCapacity());
//                invPurchaseInvoiceDetailsValidate.setCost(invPurchaseInvoiceDetailsValidate.getItemsBarcodesDetail().getSellPrice());
                    detailDTO.setScrewing(detailDTO.getItemsBarcodesDetail().getScrewing());
                    detailDTO.setLength(detailDTO.getItemsBarcodesDetail().getLength());
                    detailDTO.setWeight(detailDTO.getItemsBarcodesDetail().getWeight() != null ? detailDTO.getItemsBarcodesDetail().getWeight() : BigDecimal.ZERO);
                    detailDTO.setItemName(detailDTO.getItemsBarcodesDetail().getItemId() == null ? "" : detailDTO.getItemsBarcodesDetail().getName());
                    detailDTO.setItemCode(detailDTO.getItemsBarcodesDetail().getItemId() == null ? "" : detailDTO.getItemsBarcodesDetail().getBarcode());
                    detailDTO.setColor(detailDTO.getItemsBarcodesDetail().getItemId() == null ? "" : detailDTO.getItemsBarcodesDetail().getPaintColorName());
                    detailDTO.setUnitName(detailDTO.getItemsBarcodesDetail().getItemId() == null ? "" : detailDTO.getItemsBarcodesDetail().getUnitName());
                    detailDTO.setIsInventoryItem(detailDTO.getItemsBarcodesDetail().getIsInventoryItem());
                    detailDTO = findDataForItem(detailDTO, screenNameClassEnum);
                    detailDTO = updateAvailableQuantityRow(screenNameClassEnum, detailDTO, inventoryId, sellBuy, sellNegative, isEdit);
                    detailDTO.setOldQuantity(detailDTO.getAvailableQuantityRow());
                    detailDTO.setOldQuantityBak(detailDTO.getAvailableQuantityRow());

                } else {
                    detailDTO = resetInvItem(detailDTO);
                    detailDTO.setMsg("هذا الصنف ليس لديه وحدات.");
                    return detailDTO;
                }
            }

        } catch (Exception e) {
            detailDTO.setMsg(e.toString());
            return detailDTO;
        }
        return detailDTO;
    }

    private InvDetailDTO findDataForItem(InvDetailDTO invDetailDTO, ScreenNameClassEnum screenNameClassEnum) {
        invDetailDTO = updatePriceRow(screenNameClassEnum, invDetailDTO);
        return invDetailDTO;
    }

    private InvDetailDTO updatePriceRow(ScreenNameClassEnum screenNameClassEnum, InvDetailDTO detailDTO) {
        if (ScreenNameClassEnum.INVSALESINVOICEFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVRETURNSALESFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVPOSINVOICEFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVPOSREST.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVQUOTATIONFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVCREATEBARCODE.equals(screenNameClassEnum)
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
    public List<InvDetailDTO> AvailableQuantity(List<InvDetailDTO> detailDTOList, InvInvoiceDTO invInvoiceDTO) {
        if (detailDTOList != null && !detailDTOList.isEmpty()) {
            List<InvDetailDTO> detailDTOListNew = new ArrayList<>();
            for (InvDetailDTO detailDTO : detailDTOList) {
                detailDTO = updateAvailableQuantityRow(invInvoiceDTO.getScreenName(), detailDTO, invInvoiceDTO.getInvInventoryId() == null ? null : invInvoiceDTO.getInvInventoryId().getId(), invInvoiceDTO.getInventorySetupDTO().getSellBuy(), invInvoiceDTO.getInventorySetupDTO().getNegativeSell(), false);
                detailDTOListNew.add(detailDTO);
            }
            return detailDTOListNew;
        }
        return detailDTOList;
    }

    @Override
    public InvDetailDTO updateAvailableQuantityRow(ScreenNameClassEnum screenNameClassEnum, InvDetailDTO detailDTO, Integer inventoryId, Boolean sellBuy, Boolean sellNegative, Boolean isEdit) {
        if (ScreenNameClassEnum.INVSALESINVOICEFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVPERMISSIONORDERFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVRESERVATIONFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVRETURNPURCHASEFORM.equals(screenNameClassEnum)) {
            detailDTO.setTotalQuantityWithScrewing(detailDTO.getQuantity().multiply(detailDTO.getScrewing()));
            detailDTO.setAvailableQuantityRow(new BigDecimal((quantityItemsStoreAddExitService.getQuantatyItemByinventory(inventoryId, detailDTO.getItemsBarcodesDetail().getItemId(), sellBuy, null)).setScale(3, BigDecimal.ROUND_UP).stripTrailingZeros().toPlainString()));
            if ((detailDTO.getAvailableQuantityRow() == null || detailDTO.getAvailableQuantityRow().compareTo(BigDecimal.ZERO) < 1) && !sellNegative && !isEdit) {
                detailDTO.setItemsBarcodesDetail(null);
                detailDTO.setItemsBarcodesDetailTrans(null);
                detailDTO.setQuantity(BigDecimal.ZERO);
                detailDTO.setCost(BigDecimal.ZERO);
                detailDTO.setMsg("الصنف لا يوجد به كمية بالمخزن");
            }
        } else if (ScreenNameClassEnum.INVPOSINVOICEFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVTRANSFERINVENTORY.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVSTRIP.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVCREATEBARCODE.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVUPDATE.equals(screenNameClassEnum)) {
            detailDTO.setAvailableQuantityRow(quantityItemsStoreAddExitService.getQuantatyItemByinventory(inventoryId, detailDTO.getItemsBarcodesDetail().getItemId(), sellBuy, null));
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
    public InvDetailDTO validateNetColumn(InvDetailDTO detailDTO) {
        try {
            if (!validateNet(detailDTO)) {
                if (detailDTO.getItemsBarcodesDetail() != null) {
                    detailDTO.setMsg("يجب ان يكون السعر أكبر من او يساوي الصفر");
                } else {
                    detailDTO.setMsg("يجب اختيار صنف اولا");
                }
                detailDTO.setNet(BigDecimal.ZERO);
            }
        } catch (Exception e) {
            detailDTO.setMsg(e.toString());
        }
        return detailDTO;
    }

    @Override
    public InvDetailDTO validateActualQuantityColumn(InvDetailDTO detailDTO) {
        try {
            if (!validateActualQuantity(detailDTO)) {
                if (detailDTO.getItemsBarcodesDetail() != null) {
                    detailDTO.setMsg("يجب ان يكون السعر أكبر من او يساوي الصفر");
                } else {
                    detailDTO.setMsg("يجب اختيار صنف اولا");
                }
                detailDTO.setActualQuantity(BigDecimal.ZERO);
            }
        } catch (Exception e) {
            detailDTO.setMsg(e.toString());
        }
        return detailDTO;
    }

    @Override
    public InvDetailDTO validateQuantityColum(InvDetailDTO detailDTO, Boolean isload, ScreenNameClassEnum screenNameClassEnum, Boolean negativeSell, CompanyActivityClassEnum companyActivityClassEnum) {
        try {

            if (CompanyActivityClassEnum.CERAMIC.equals(companyActivityClassEnum)) {
                if (detailDTO.getQuantity() == null || detailDTO.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
                    detailDTO.setQuantity(detailDTO.getCartonsNumber().multiply(detailDTO.getCartonsCapacity()));
                }
            }

            if (!validateQuantity(detailDTO)) {
                if (detailDTO.getItemsBarcodesDetail() != null) {
                    detailDTO.setMsg("يجب ان تكون الكمية أكبر من الصفر");
                } else {
                    detailDTO.setMsg("يجب اختيار صنف اولا");
                }
                setDefaultQuantity(detailDTO);
            } else {
                detailDTO.setTotalQuantityWithScrewing(detailDTO.getQuantity().multiply(detailDTO.getScrewing()));
                detailDTO = validateTotalQuantity(detailDTO, isload, screenNameClassEnum, negativeSell);
                if (detailDTO.getMsg() != null && !detailDTO.getMsg().isEmpty()) {
                    setDefaultQuantity(detailDTO);
                }
            }
        } catch (Exception e) {
            detailDTO.setMsg(e.toString());
        }
        return detailDTO;
    }

    private InvDetailDTO setDefaultQuantity(InvDetailDTO detailDTO) {
        detailDTO.setQuantity(new BigDecimal(detailDTO.getOriginQuantity().setScale(3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()));
        detailDTO.setTotalQuantityWithScrewing(detailDTO.getOriginQuantity().multiply(detailDTO.getScrewing()));
        return detailDTO;
    }

    private InvDetailDTO validateTotalQuantity(InvDetailDTO detailDTO, Boolean isload, ScreenNameClassEnum screenNameClassEnum, Boolean negativeSell) {
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
                setDefaultQuantity(detailDTO);
            }
        }

        checkQuantityFromLoaded(isload, detailDTO);
        return detailDTO;
    }

    private InvDetailDTO checkQuantityFromLoaded(Boolean isload, InvDetailDTO detailDTO) {
        if (isload) {
            if (((detailDTO.getQuantity()).multiply(detailDTO.getScrewing())).compareTo(detailDTO.getRealQuantity()) > 0) {
                detailDTO.setMsg("الكميه اكبر من الكميه الموجوده في الفاتورة ");
                setDefaultQuantity(detailDTO);
            }
        }
        return detailDTO;
    }

    @Override
    public InvDetailDTO validateDiscountColumn(InvDetailDTO detailDTO) {
        try {
            if (!validateDiscount(detailDTO)) {
                detailDTO.setDiscount(null);
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
    public InvDetailDTO fillItemDetail(InvDetailDTO dTO, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap,
            Integer id, Integer serial, Integer branchId, BigDecimal cost, BigDecimal discount, BigDecimal quantity, Integer discountType, Integer invInvoiceId,
            BigDecimal extraCost, BigDecimal net, Integer index, Date createdDate, Integer createdBy,
            String itemBarcode, BigDecimal screwing, Integer itemId, BigDecimal weight, Integer unitId, Integer unitSerial, String unitName, Integer inventoryItem,
            BigDecimal actualQuantity, BigDecimal oldQuantity, BigDecimal add1, BigDecimal add2, BigDecimal add3, BigDecimal disc1, BigDecimal disc2, BigDecimal disc3,
            BigDecimal totalBeforefracture, BigDecimal fractureNumber, BigDecimal valuefracture, BigDecimal cartonsNumber, BigDecimal cartonsCapacity, BigDecimal addTotal, BigDecimal discountValue) {

        dTO.setId(id);
        dTO.setSerial(serial);

        dTO.setBranchId(branchId);
        dTO.setCost(cost != null ? new BigDecimal(cost.setScale(3, BigDecimal.ROUND_UP).stripTrailingZeros().toPlainString()) : BigDecimal.ZERO);
        dTO.setDiscount(discount != null ? new BigDecimal(discount.setScale(3, BigDecimal.ROUND_UP).stripTrailingZeros().toPlainString()) : BigDecimal.ZERO);
        dTO.setQuantity(quantity != null ? new BigDecimal(quantity.setScale(3, BigDecimal.ROUND_UP).stripTrailingZeros().toPlainString()) : BigDecimal.ZERO);
        dTO.setOriginQuantity(dTO.getQuantity().multiply(screwing == null ? BigDecimal.ONE : screwing));
        dTO.setActualQuantity(actualQuantity != null ? new BigDecimal(actualQuantity.setScale(3, BigDecimal.ROUND_UP).stripTrailingZeros().toPlainString()) : BigDecimal.ZERO);
        dTO.setOldQuantity(oldQuantity != null ? new BigDecimal(oldQuantity.setScale(3, BigDecimal.ROUND_UP).stripTrailingZeros().toPlainString()) : BigDecimal.ZERO);
        dTO.setTotalQuantityWithScrewing(dTO.getQuantity().multiply(dTO.getScrewing()));
        dTO.setIsEdit(Boolean.TRUE);
        dTO.setDiscountType(discountType);
        dTO.setInvInvoiceId(invInvoiceId);
        dTO.setIsInventoryItem(inventoryItem);
        dTO.setValuefracture(valuefracture);
        dTO.setFractureNumber(fractureNumber);
        dTO.setTotalBeforefracture(totalBeforefracture);
        dTO.setCartonsNumber(cartonsNumber);
        dTO.setCartonsCapacity(cartonsCapacity);
        dTO.setAddTotal(addTotal);
        dTO.setValueRowDiscount(discountValue);

        if (itemBarcode != null && !itemBarcode.isEmpty()) {
            ItemsBarcodesDetailsView barcodesDetailsView = itemsBarcodeMap.get(itemBarcode);
            dTO.setItemsBarcodesDetail(barcodesDetailsView);
            dTO.setItemsBarcodesDetailTrans(dTO.getItemsBarcodesDetail());
            dTO.setItemName(dTO.getItemsBarcodesDetail().getName());
            dTO.setItemCode(dTO.getItemsBarcodesDetail().getBarcode());
            dTO.setScrewing(screwing);
            if (itemId != null && barcodesDetailsView != null && barcodesDetailsView.getPaintColor() != null) {
                SymbolDTO paintColor = new SymbolDTO();
                paintColor.setId(barcodesDetailsView.getPaintColor());
                paintColor.setName(barcodesDetailsView.getPaintColorName());
                dTO.setPaintColor(paintColor);
            }
            dTO.setLength(barcodesDetailsView == null ? null : barcodesDetailsView.getLength());
            dTO.setSizeName(barcodesDetailsView == null ? null : barcodesDetailsView.getSize());
            dTO.setSortName(barcodesDetailsView == null ? null : barcodesDetailsView.getSorting());
        }

        dTO.setWeight(weight != null ? weight : BigDecimal.ZERO);
        if (unitId != null) {
            SymbolDTO unitDTO = new SymbolDTO();
            unitDTO.setId(unitId);
            unitDTO.setName(unitName);
            unitDTO.setSerial(unitSerial);
            dTO.setUnitName(unitName);
            dTO.setUnitId(unitDTO);
        }

        dTO.setDisc1(disc1);
        dTO.setDisc2(disc2);
        dTO.setDisc3(disc3);
        dTO.setAdd1(add1);
        dTO.setAdd2(add2);
        dTO.setAdd3(add3);
        dTO.setTotalBeforefracture(totalBeforefracture);
        dTO.setFractureNumber(fractureNumber);
        dTO.setValuefracture(valuefracture);

        dTO.setExtraCost(extraCost);
        dTO.setNet(net);
        dTO.setIndex(id);
        dTO.setCreatedDate(createdDate);
        dTO.setCreatedBy(createdBy);

        return dTO;
    }

    @Override
    public List<InvDetailDTO> updatePriceAndQuantityForDetail(List<InvDetailDTO> invDetailDTOList, ScreenNameClassEnum screenNameClassEnum) {
        List<InvDetailDTO> detailDTOList = new ArrayList<>();
        for (InvDetailDTO dTO : invDetailDTOList) {
            dTO = findDataForItem(dTO, screenNameClassEnum);
            detailDTOList.add(dTO);
        }
        return detailDTOList;
    }

}
