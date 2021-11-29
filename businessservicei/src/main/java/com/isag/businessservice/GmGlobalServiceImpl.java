/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.GmSubscriptionsDetailDTO;
import com.isag.dto.GmSubscriptionsDTO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;

@Stateless
public class GmGlobalServiceImpl implements GmGlobalService {


    @Override
    public GmSubscriptionsDTO updateSummition(GmSubscriptionsDTO gmSubscriptionsDTO) {
        try {

            gmSubscriptionsDTO = calculateDetailValues(gmSubscriptionsDTO);
            gmSubscriptionsDTO = recalcHeadValues(gmSubscriptionsDTO);
        } catch (Exception e) {
//            saveError(e, "InvPurchaseInvoiceFormMB", "updateSummition");
        }

        return gmSubscriptionsDTO;
    }

    private GmSubscriptionsDTO calculateDetailValues(GmSubscriptionsDTO gmSubscriptionsDTO) {
        if (gmSubscriptionsDTO != null) {
            if (gmSubscriptionsDTO.getGmSubscriptionsDetailList() != null && !gmSubscriptionsDTO.getGmSubscriptionsDetailList().isEmpty()) {
                List<GmSubscriptionsDetailDTO> gmSubscriptionsDetailDTOList = new ArrayList<>();

                for (GmSubscriptionsDetailDTO detailEntity : gmSubscriptionsDTO.getGmSubscriptionsDetailList()) {
                    // حساب الاجماليات الخاصة بكل تفصيلى على حده
                    detailEntity = calculateDetailTotalsRow(detailEntity, gmSubscriptionsDTO);
                    gmSubscriptionsDetailDTOList.add(detailEntity);
                    // حساب إجماليات الفاتورة بعد حساب كل سطر تفصيلى
                    gmSubscriptionsDTO = claculateHeadTotalFromDetail(detailEntity, gmSubscriptionsDTO);
                }
                gmSubscriptionsDTO.setGmSubscriptionsDetailList(gmSubscriptionsDetailDTOList);
            }
        }
        return gmSubscriptionsDTO;
    }

    private GmSubscriptionsDTO claculateHeadTotalFromDetail(GmSubscriptionsDetailDTO detailEntity, GmSubscriptionsDTO gmSubscriptionsDTO) {

        gmSubscriptionsDTO.setTotalNet((gmSubscriptionsDTO.getTotalNet().add(detailEntity.getNet() != null ? detailEntity.getNet() : BigDecimal.ZERO)).setScale(3, BigDecimal.ROUND_HALF_UP));
        gmSubscriptionsDTO.setTotalBeforeDiscount((gmSubscriptionsDTO.getTotalBeforeDiscount().add(detailEntity.getTotalBeforeDiscount() != null ? detailEntity.getTotalBeforeDiscount() : BigDecimal.ZERO)).setScale(3, BigDecimal.ROUND_HALF_UP));
        return gmSubscriptionsDTO;
    }
    
    @Override
    public GmSubscriptionsDTO recalcHeadValues(GmSubscriptionsDTO dto) {
        resetHeadTotals(dto);
        dto.setTotalNetAfterDiscount((dto.getTotalNet().subtract(dto.getDiscount() != null ? dto.getDiscount() : BigDecimal.ZERO)).setScale(3, BigDecimal.ROUND_HALF_UP));
        dto = calculateTaxValue(dto);
        dto.setFinalNet((dto.getTotalNetAfterDiscount().add(dto.getTaxvalue() != null ? dto.getTaxvalue() : BigDecimal.ZERO)).setScale(3, BigDecimal.ROUND_HALF_UP));
        return dto;
    }
    
    private GmSubscriptionsDTO calculateTaxValue(GmSubscriptionsDTO gmSubscriptionsDTO) {
        // لحساب ضريبة القيمة المضافة
        // تحسب ضريبة القيمة المضافة بعد حساب الاجمالى بعد الخصم
        try {
            if (gmSubscriptionsDTO.getTaxFlag() != null && gmSubscriptionsDTO.getTaxFlag()) {
                gmSubscriptionsDTO.setTaxvalue(gmSubscriptionsDTO.getTotalNetAfterDiscount().multiply(new BigDecimal(0.15)));
            } else {
                gmSubscriptionsDTO.setTaxvalue(BigDecimal.ZERO);
            }
        } catch (Exception e) {
//            saveError(e, "InvPurchaseInvoiceFormMB", "updateTax");

        }
        return gmSubscriptionsDTO;
    }
    
    

    
    private void resetHeadTotals(GmSubscriptionsDTO dto) {
        dto.setTotalWithTaxValue(BigDecimal.ZERO);
        dto.setTaxvalue(BigDecimal.ZERO);
        dto.setDiscountValue(BigDecimal.ZERO);

        dto.setTotalNetAfterDiscount(BigDecimal.ZERO);
        dto.setFinalNet(BigDecimal.ZERO);
    }

    public GmSubscriptionsDetailDTO calculateDetailTotalsRow(GmSubscriptionsDetailDTO detailEntity, GmSubscriptionsDTO invInvoiceDTO) {
        BigDecimal quantity;
        BigDecimal weight;
        BigDecimal cost;
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal valueAfterDiscount = BigDecimal.ZERO;
        if (detailEntity.getItemSymbolId() == null && detailEntity.getItemSymbolIdTrans() != null) {
            detailEntity.setItemSymbolId(detailEntity.getItemSymbolIdTrans());
        }
        quantity = detailEntity.getQuantity();
        cost = detailEntity.getCost();
        total = quantity.multiply(cost);

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
        detailEntity.setTotalBeforeDiscount(total.setScale(3, BigDecimal.ROUND_HALF_UP));

        detailEntity.setNet(valueAfterDiscount.setScale(3, BigDecimal.ROUND_HALF_UP));
        return detailEntity;
    }
    
    

    @Override
    public GmSubscriptionsDTO addGmSubscriptionsDTODetail(GmSubscriptionsDTO gmSubscriptionsDTO, Integer index) {
        try {

            GmSubscriptionsDetailDTO detailNew = new GmSubscriptionsDetailDTO();
            detailNew.setIndex(index);
            if (gmSubscriptionsDTO.getOrganizationSiteId() != null) {
                detailNew.setDiscount(gmSubscriptionsDTO.getOrganizationSiteId().getDiscount());
            }

            gmSubscriptionsDTO.setGmSubscriptionsDetailDTOSelected(detailNew);
            gmSubscriptionsDTO.getGmSubscriptionsDetailList().add(detailNew);

        } catch (Exception e) {
            gmSubscriptionsDTO.setMsg(e.toString());
        }
        return gmSubscriptionsDTO;
    }


}
