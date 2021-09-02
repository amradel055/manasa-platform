/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.common.Validation;
import com.toby.core.GenericDAO;
import com.toby.define.CompanyActivityClassEnum;
import com.toby.define.ScreenNameClassEnum;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.SymbolDTO;
import com.toby.entity.InvInventory;
import com.toby.entity.InvItem;
import com.toby.views.ItemsBarcodesDetailsView;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
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
    private GenericDAO dao;
    @EJB
    private InvItemService itemService;
    
    @Override
    public InvDetailDTO calculateDetailTotalsRow(InvDetailDTO detailEntity, InvInvoiceDTO invInvoiceDTO, Boolean isNeeadQuantity) {
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
//        quantity = detailEntity.getQuantity() != null ? detailEntity.getQuantity() : BigDecimal.ZERO;
        quantity = calculateQuantity(detailEntity, invInvoiceDTO);
        weight = detailEntity.getWeight() == null ? BigDecimal.ZERO : detailEntity.getWeight();
        number = detailEntity.getNumber() == null ? BigDecimal.ONE : detailEntity.getNumber();
//        cost = detailEntity.getCost() != null ? detailEntity.getCost().multiply(invInvoiceDTO.getRate()) : BigDecimal.ZERO;
        cost = calcCost(detailEntity, invInvoiceDTO).setScale(3, BigDecimal.ROUND_HALF_UP);
        if (invInvoiceDTO.getCompanyActivity() != null) {
            if (invInvoiceDTO.getCompanyActivity().equals(CompanyActivityClassEnum.Alumetal)) {
                total = AlumetalActivity(weight, total, quantity, cost);
            } else if (invInvoiceDTO.getCompanyActivity().equals(CompanyActivityClassEnum.RetailSaleForClothFactory)
                    && (ScreenNameClassEnum.INVSALESINVOICEFORM.equals(invInvoiceDTO.getScreenName())
                    || ScreenNameClassEnum.PROWORKORDER.equals(invInvoiceDTO.getScreenName()))) {
                if (detailEntity.getItemsBarcodesDetail().getProGroupId() != null && detailEntity.getItemsBarcodesDetail().getProGroupId() == 22) {
                    number = number.multiply(detailEntity.getQuantityOfOneUnit());
                }
                total = RetailSaleForClothFactoryActivity(total, number, cost);
            } else {
                total = RetailActivity(total, quantity, cost);
            }
        } else {
            detailEntity.setMsg("يجب تحديد نشاط الشركة برجاء النواصل مع الدعم");
        }
//        discount = detailEntity.getDiscount() != null ? detailEntity.getDiscount() : BigDecimal.ZERO;
        discount = calcDiscount(detailEntity, invInvoiceDTO);
        valueAfterDiscount = total.subtract((total.multiply(discount).divide(new BigDecimal(100))));
        detailEntity.setValueAfterDiscount(valueAfterDiscount);
        detailEntity.setValueBeforeDiscount(total);
        detailEntity.setNet(valueAfterDiscount.setScale(3, BigDecimal.ROUND_HALF_UP));
        detailEntity.setTotalQuantityWithScrewing(detailEntity.getQuantity().multiply(detailEntity.getScrewing()));
        if (isNeeadQuantity && invInvoiceDTO.getInventorySetupDTO() != null && !invInvoiceDTO.getInventorySetupDTO().getNegativeSell()) {
            detailEntity = updateAvailableQuantityRow(invInvoiceDTO.getScreenName(), detailEntity, invInvoiceDTO.getInvInventoryId() != null ? invInvoiceDTO.getInvInventoryId().getId() : null, invInvoiceDTO.getInventorySetupDTO().getSellBuy(), invInvoiceDTO.getInventorySetupDTO().getNegativeSell(), invInvoiceDTO.getIsEdit());
        }
        if (detailEntity.getItemId() != null
                && detailEntity.getItemId().getProGroupId() != null && detailEntity.getItemId().getProGroupId().getId() != null
                && detailEntity.getItemId().getProGroupId().getId() == 22) {
            detailEntity.setCostComparable(detailEntity.getNet().divide(detailEntity.getNumber(), 3, RoundingMode.HALF_UP));
        } else {
            detailEntity.setCostComparable(detailEntity.getCost());
        }
        return detailEntity;
    }
    
    private BigDecimal calcDiscount(InvDetailDTO detailEntity, InvInvoiceDTO invInvoiceDTO) {
//        if (invInventoryId == 70) {
        if (!ScreenNameClassEnum.INVTRANSFERINVENTORY.equals(invInvoiceDTO.getScreenName())) {
            if (invInvoiceDTO.getOfferType() == null) {
                if ((detailEntity.getItemsBarcodesDetail().getProGroupId() == null
                        || (detailEntity.getItemsBarcodesDetail().getProGroupId() != null
                        && detailEntity.getItemsBarcodesDetail().getProGroupId() != 5
                        && detailEntity.getItemsBarcodesDetail().getProGroupId() != 13))) {
                    if (detailEntity.getDiscount().compareTo(BigDecimal.ZERO) != 1) {
                        BigDecimal discountCustomer = BigDecimal.ZERO;
                        BigDecimal discountCompany = BigDecimal.ZERO;
                        if (invInvoiceDTO.getOrganizationSiteId() != null) {
                            discountCustomer = invInvoiceDTO.getOrganizationSiteId().getDiscount();
                        }
                        if (invInvoiceDTO.getOrganizationSiteId() != null && invInvoiceDTO.getOrganizationSiteId().getOfferCompanyDTO() != null) {
                            discountCompany = invInvoiceDTO.getOrganizationSiteId().getOfferCompanyDTO().getDiscount();
                        }
                        if (discountCustomer.compareTo(discountCompany) == 1) {
                            detailEntity.setDiscount(discountCustomer);
                            return discountCustomer;
                        } else {
                            detailEntity.setDiscount(discountCompany);
                            return discountCompany;
                        }
                    } else {
                        return detailEntity.getDiscount();
                    }
                } else {
                    return detailEntity.getDiscount();
                }
                
            } else {
                if ((invInvoiceDTO != null) && (detailEntity.getItemsBarcodesDetail().getProGroupId() == null
                        || (detailEntity.getItemsBarcodesDetail().getProGroupId() != null && detailEntity.getItemsBarcodesDetail().getProGroupId() != 1
                        && detailEntity.getItemsBarcodesDetail().getProGroupId() != 5 && detailEntity.getItemsBarcodesDetail().getProGroupId() != 13))) {
                    detailEntity.setDiscount(new BigDecimal(25));
                    return new BigDecimal(25);
                }
            }
        }
        return detailEntity.getDiscount() != null ? detailEntity.getDiscount() : BigDecimal.ZERO;
    }
    
    private BigDecimal calculateQuantity(InvDetailDTO detailEntity, InvInvoiceDTO invInvoiceDTO) {
        BigDecimal quantity;
        quantity = detailEntity.getQuantity() != null ? detailEntity.getQuantity() : BigDecimal.ZERO;
        if (quantity.equals(BigDecimal.ZERO) && invInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVSALESINVOICEFORM)) {
            quantity = BigDecimal.ONE;
        }
        detailEntity.setQuantity(quantity);
        return quantity;
    }
    
    private BigDecimal calcCost(InvDetailDTO detailEntity, InvInvoiceDTO invInvoiceDTO) {
        BigDecimal cost = BigDecimal.ZERO;
        if (invInvoiceDTO.getCompanyActivity().equals(CompanyActivityClassEnum.RetailSaleForClothFactory)
                && (ScreenNameClassEnum.PROWORKORDER.equals(invInvoiceDTO.getScreenName()))
                && detailEntity.getCost().compareTo(BigDecimal.ZERO) != 1) {
            if (detailEntity.getItemsBarcodesDetail().getProGroupId() != null && detailEntity.getItemsBarcodesDetail().getProGroupId() == 22) { // زرار وهاروت
                BigDecimal price;
                if (invInvoiceDTO.getPricetype() == 1) {
                    price = detailEntity.getItemsBarcodesDetail().getMaxPriceMen();
                } else {
                    price = detailEntity.getItemsBarcodesDetail().getMaxPriceYoung();
                }
                
                BigDecimal oneBean = BigDecimal.ZERO;
                if (detailEntity.getItemsBarcodesDetail().getNumberMetersMen().compareTo(new BigDecimal(3)) == 0
                        && detailEntity.getItemsBarcodesDetail().getMaxPriceMen().compareTo(new BigDecimal(150)) == 0) {
                    if (detailEntity.getQuantityOfOneUnit().compareTo(new BigDecimal(3)) == 1) {
                        cost = price.divide(detailEntity.getQuantityOfOneUnit(), 3, RoundingMode.HALF_UP);
                    } else if (detailEntity.getQuantityOfOneUnit().compareTo(new BigDecimal(3)) == 0) {
                        cost = (new BigDecimal(100)).divide(new BigDecimal(3), 3, RoundingMode.HALF_UP);
                    } else if (detailEntity.getQuantityOfOneUnit().compareTo(new BigDecimal(3)) == -1) {
                        oneBean = new BigDecimal(50);
                        cost = price.add(oneBean.multiply(detailEntity.getQuantityOfOneUnit().subtract(detailEntity.getItemsBarcodesDetail().getNumberMetersMen())));
                        cost = cost.divide(detailEntity.getQuantityOfOneUnit(), 3, RoundingMode.HALF_UP);
                    }
                } else {
                    if (detailEntity.getQuantityOfOneUnit().compareTo(new BigDecimal(4)) != 1) {
                        oneBean = calaOneBean(detailEntity, oneBean, price);
                        cost = oneBean;
                    } else if (detailEntity.getQuantityOfOneUnit().compareTo(new BigDecimal(8)) != 1 && detailEntity.getQuantityOfOneUnit().compareTo(new BigDecimal(4)) == 1) {
                        cost = price.divide(detailEntity.getQuantityOfOneUnit(), 3, RoundingMode.HALF_UP);
                    } else if (detailEntity.getQuantityOfOneUnit().compareTo(new BigDecimal(8)) == 1) {
                        oneBean = calaOneBean(detailEntity, oneBean, price);
                        cost = price.add(oneBean.multiply(detailEntity.getQuantityOfOneUnit().subtract(detailEntity.getItemsBarcodesDetail().getNumberMetersMen())));
                        cost = cost.divide(detailEntity.getQuantityOfOneUnit(), 3, RoundingMode.HALF_UP);
                    }
                }
            } else {
                if (invInvoiceDTO.getOfferType() != null && invInvoiceDTO.getOfferType() == 1) {
                    if (invInvoiceDTO.getPricetype() == 1) {
                        if (detailEntity.getItemsBarcodesDetail().getTypeShow() != null
                                && detailEntity.getItemsBarcodesDetail().getProGroupId() != null && detailEntity.getItemsBarcodesDetail().getTypeShow() == 1) {
                            if (detailEntity.getItemsBarcodesDetail().getSellPrice().compareTo(BigDecimal.valueOf(16.67)) == 1
                                    && detailEntity.getItemsBarcodesDetail().getProGroupId() != null && detailEntity.getItemsBarcodesDetail().getProGroupId() == 1) {
                                cost = calculateIncrease(detailEntity.getQuantityOfOneUnit(), new BigDecimal(5), detailEntity.getItemsBarcodesDetail().getMaxPriceMen());
                            } else {
                                cost = calculateIncrease(detailEntity.getQuantityOfOneUnit(), new BigDecimal(5), BigDecimal.valueOf(289.565));
                            }
                        } else if (detailEntity.getItemsBarcodesDetail().getTypeShow() != null && detailEntity.getItemsBarcodesDetail().getProGroupId() != null
                                && detailEntity.getItemsBarcodesDetail().getTypeShow() == 2) {
                            if (detailEntity.getItemsBarcodesDetail().getSellPrice().compareTo(new BigDecimal(20)) == 1
                                    && detailEntity.getItemsBarcodesDetail().getProGroupId() != null && detailEntity.getItemsBarcodesDetail().getProGroupId() == 1) {
                                cost = calculateIncrease(detailEntity.getQuantityOfOneUnit(), new BigDecimal(3.5), detailEntity.getItemsBarcodesDetail().getMaxPriceMen());
                            } else {
                                cost = calculateIncrease(detailEntity.getQuantityOfOneUnit(), new BigDecimal(3.5), BigDecimal.valueOf(289.565));
                            }
                        } else {
                            cost = detailEntity.getItemsBarcodesDetail().getMaxPriceMen();
                        }
                    } else {
                        if (detailEntity.getItemsBarcodesDetail().getTypeShow() != null && detailEntity.getItemsBarcodesDetail().getProGroupId() != null
                                && detailEntity.getItemsBarcodesDetail().getTypeShow() == 1) {
                            if (detailEntity.getItemsBarcodesDetail().getSellPrice().compareTo(BigDecimal.valueOf(16.67)) == 1
                                    && detailEntity.getItemsBarcodesDetail().getProGroupId() != null && detailEntity.getItemsBarcodesDetail().getProGroupId() == 1) {
                                cost = calculateIncrease(detailEntity.getQuantityOfOneUnit(), new BigDecimal(4), detailEntity.getItemsBarcodesDetail().getMaxPriceYoung());
                            } else {
                                cost = calculateIncrease(detailEntity.getQuantityOfOneUnit(), new BigDecimal(4), BigDecimal.valueOf(289.565));
                            }
                        } else if (detailEntity.getItemsBarcodesDetail().getTypeShow() != null && detailEntity.getItemsBarcodesDetail().getProGroupId() != null
                                && detailEntity.getItemsBarcodesDetail().getTypeShow() == 2) {
                            if (detailEntity.getItemsBarcodesDetail().getSellPrice().compareTo(new BigDecimal(20)) == 1
                                    && detailEntity.getItemsBarcodesDetail().getProGroupId() == 1) {
                                cost = calculateIncrease(detailEntity.getQuantityOfOneUnit(), BigDecimal.valueOf(2.5), detailEntity.getItemsBarcodesDetail().getMaxPriceYoung());
                            } else {
                                cost = calculateIncrease(detailEntity.getQuantityOfOneUnit(), BigDecimal.valueOf(2.5), BigDecimal.valueOf(289.565));
                            }
                        } else {
                            cost = detailEntity.getItemsBarcodesDetail().getMaxPriceMen();
                        }
                    }
                } else {
                    if (invInvoiceDTO.getPricetype() == 1) {
                        if (detailEntity.getItemsBarcodesDetail().getTypeShow() != null
                                && detailEntity.getItemsBarcodesDetail().getProGroupId() != null && detailEntity.getItemsBarcodesDetail().getTypeShow() == 1) {
                            cost = calculateIncrease(detailEntity.getQuantityOfOneUnit(), new BigDecimal(5), detailEntity.getItemsBarcodesDetail().getMaxPriceMen());
                        } else if (detailEntity.getItemsBarcodesDetail().getTypeShow() != null && detailEntity.getItemsBarcodesDetail().getProGroupId() != null
                                && detailEntity.getItemsBarcodesDetail().getTypeShow() == 2) {
                            cost = calculateIncrease(detailEntity.getQuantityOfOneUnit(), new BigDecimal(3.5), detailEntity.getItemsBarcodesDetail().getMaxPriceMen());
                        } else {
                            cost = detailEntity.getItemsBarcodesDetail().getMaxPriceMen();
                        }
                    } else {
                        if (detailEntity.getItemsBarcodesDetail().getTypeShow() != null && detailEntity.getItemsBarcodesDetail().getProGroupId() != null
                                && detailEntity.getItemsBarcodesDetail().getTypeShow() == 1) {
                            cost = calculateIncrease(detailEntity.getQuantityOfOneUnit(), new BigDecimal(4), detailEntity.getItemsBarcodesDetail().getMaxPriceYoung());
                        } else if (detailEntity.getItemsBarcodesDetail().getTypeShow() != null && detailEntity.getItemsBarcodesDetail().getProGroupId() != null
                                && detailEntity.getItemsBarcodesDetail().getTypeShow() == 2) {
                            cost = calculateIncrease(detailEntity.getQuantityOfOneUnit(), BigDecimal.valueOf(2.5), detailEntity.getItemsBarcodesDetail().getMaxPriceYoung());
                        } else {
                            cost = detailEntity.getItemsBarcodesDetail().getMaxPriceMen();
                        }
                    }
                }
            }
        } else {
            cost = detailEntity.getCost();
        }
        return cost;
    }
    
    private BigDecimal calaOneBean(InvDetailDTO detailEntity, BigDecimal oneBean, BigDecimal price) {
        if (price.compareTo(new BigDecimal(200)) == 0) {
            oneBean = new BigDecimal(35);
        } else if (price.compareTo(new BigDecimal(180)) == 0) {
            oneBean = new BigDecimal(30);
        } else if (price.compareTo(new BigDecimal(150)) == 0) {
            oneBean = new BigDecimal(35);
        } else if (price.compareTo(new BigDecimal(120)) == 0) {
            oneBean = new BigDecimal(20);
        } else if (price.compareTo(new BigDecimal(100)) == 0) {
            oneBean = new BigDecimal(15);
        } else {
            oneBean = price;
        }
        return oneBean;
    }
    
    public BigDecimal calculateIncrease(BigDecimal total, BigDecimal main, BigDecimal price) {
        if (total.compareTo(main) == 1) {
            BigDecimal constanceNumber = BigDecimal.valueOf(0.5);
            BigDecimal constanceCost = new BigDecimal(50);
            BigDecimal remain = total.subtract(main);
            Integer number = 0;
            for (int i = 1; remain.compareTo(constanceNumber) == 1; i++) {
                number = i;
                remain = remain.subtract(constanceNumber);
            }
            if (remain.compareTo(BigDecimal.ZERO) == 1) {
                number = number + 1;
            }
            BigDecimal value = (new BigDecimal(number).multiply(constanceCost)).add(price);
            return value;
        }
        return price;
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
        invPurchaseInvoiceDTO.setTotalNetAfterDiscount((invPurchaseInvoiceDTO.getTotalNet().subtract(invPurchaseInvoiceDTO.getDiscountValue() != null ? invPurchaseInvoiceDTO.getDiscountValue() : BigDecimal.ZERO)).setScale(3, BigDecimal.ROUND_HALF_UP));
        invPurchaseInvoiceDTO = calculateTaxDiscValue(invPurchaseInvoiceDTO);
        invPurchaseInvoiceDTO = calculateTaxValue(invPurchaseInvoiceDTO);
        BigDecimal finalN = (invPurchaseInvoiceDTO.getTotalNetAfterDiscount().add(invPurchaseInvoiceDTO.getTaxvalue() != null ? invPurchaseInvoiceDTO.getTaxvalue() : BigDecimal.ZERO).subtract(invPurchaseInvoiceDTO.getTaxdiscvalue() != null ? invPurchaseInvoiceDTO.getTaxdiscvalue() : BigDecimal.ZERO)).setScale(2, BigDecimal.ROUND_HALF_UP);
        if (BigDecimal.valueOf(998.999).equals(finalN)) {
            invPurchaseInvoiceDTO.setFinalNet(BigDecimal.valueOf(999));
        } else {
//            invPurchaseInvoiceDTO.setFinalNet(finalN.setScale(0, BigDecimal.ROUND_DOWN));
            invPurchaseInvoiceDTO.setFinalNet(finalN);
        }
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
                
                if (invPurchaseInvoiceDTO.getDiscHalala() != null) {
                    
                    invPurchaseInvoiceDTO.setDiscountValue(invPurchaseInvoiceDTO.getDiscountValue().add(invPurchaseInvoiceDTO.getDiscHalala()));
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
                invPurchaseInvoiceDTO.setTaxvalue(invPurchaseInvoiceDTO.getTotalNetAfterDiscount().multiply(BigDecimal.valueOf(0.15)));
            } else {
                invPurchaseInvoiceDTO.setTaxvalue(BigDecimal.ZERO);
            }
        } catch (Exception e) {
//            saveError(e, "InvPurchaseInvoiceFormMB", "updateTax");

        }
        return invPurchaseInvoiceDTO;
    }
    
    private InvInvoiceDTO claculateHeadTotalFromDetail(InvDetailDTO detailEntity, InvInvoiceDTO invPurchaseInvoiceDTO) {
        invPurchaseInvoiceDTO.setTotalQuatity(invPurchaseInvoiceDTO.getTotalQuatity().add(detailEntity.getQuantity() != null ? detailEntity.getQuantity() : BigDecimal.ZERO));
        invPurchaseInvoiceDTO.setTotalNet((invPurchaseInvoiceDTO.getTotalNet().add(detailEntity.getNet() != null ? detailEntity.getNet() : BigDecimal.ZERO)).setScale(3, BigDecimal.ROUND_HALF_UP));
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
            if (invInvoiceDTO != null && invInvoiceDTO.getInvPurchaseInvoiceDetailList() != null && !invInvoiceDTO.getInvPurchaseInvoiceDetailList().isEmpty()) {
                invPurchaseInvoiceDetailsEntityNew.setClothNumber(invInvoiceDTO.getInvPurchaseInvoiceDetailList().get(invInvoiceDTO.getInvPurchaseInvoiceDetailList().size() - 1).getClothNumber());
            }
            
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
                        || ScreenNameClassEnum.PROWORKORDER.equals(screenNameClassEnum)
                        || ScreenNameClassEnum.INVRESERVATIONFORM.equals(screenNameClassEnum)
                        || ScreenNameClassEnum.INVTRANSFERINVENTORY.equals(screenNameClassEnum)
                        || ScreenNameClassEnum.INVRETURNPURCHASEFORM.equals(screenNameClassEnum))) {
//                    if (!negativeSell && (invDetailDTO.getIsInventoryItem() == 1)) {
//                        StringBuilder ee = validateQuantityWhenSave(invDetailDTO, inventoryId, screenNameClassEnum, isload, sellBuy, companyActivityClassEnum);
//                        if (!ee.toString().isEmpty()) {
//                            error.append(ee);
//                        }
//                    }
                    if (!negativeSell && (invDetailDTO.getIsInventoryItem() == 1)) {
                        invDetailDTO = validateQuantityWhenSave(invDetailDTO, inventoryId, screenNameClassEnum, isload, sellBuy, companyActivityClassEnum);
                        if (invDetailDTO.getMsg() != null && !invDetailDTO.getMsg().isEmpty()) {
                            error.append(invDetailDTO.getMsg());
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

//    @Override
    public Boolean validateQuantityOfUnit(InvDetailDTO detailDTO, CompanyActivityClassEnum companyActivityClassEnum, ScreenNameClassEnum screenNameClassEnum) {
        if (companyActivityClassEnum.equals(CompanyActivityClassEnum.RetailSaleForClothFactory)
                && (ScreenNameClassEnum.PROWORKORDER.equals(screenNameClassEnum))) {
            return detailDTO != null && detailDTO.getItemsBarcodesDetail() != null
                    && (detailDTO.getQuantityOfOneUnit()) != null
                    && (detailDTO.getQuantityOfOneUnit()).compareTo(BigDecimal.ZERO) == 1;
        }
        return false;
        
    }
    
    @Override
    public Boolean validateQuantity(InvDetailDTO detailDTO, CompanyActivityClassEnum companyActivityClassEnum, ScreenNameClassEnum screenNameClassEnum) {
        if (companyActivityClassEnum.equals(CompanyActivityClassEnum.RetailSaleForClothFactory) && (ScreenNameClassEnum.INVSALESINVOICEFORM.equals(screenNameClassEnum) || ScreenNameClassEnum.PROWORKORDER.equals(screenNameClassEnum))) {
            return detailDTO != null && detailDTO.getItemsBarcodesDetail() != null
                    && (detailDTO.getQuantityOfOneUnit().multiply(detailDTO.getNumber())) != null
                    && (detailDTO.getQuantityOfOneUnit().multiply(detailDTO.getNumber())).compareTo(BigDecimal.ZERO) == 1;
        } else {
            return detailDTO != null && detailDTO.getItemsBarcodesDetail() != null
                    && detailDTO.getQuantity() != null
                    && detailDTO.getQuantity().compareTo(BigDecimal.ZERO) == 1;
        }
        
    }
    
    public InvDetailDTO validateQuantityWhenSave(InvDetailDTO detailDTO, Integer inventoryId, ScreenNameClassEnum screenNameClassEnum,
            Boolean isload, Boolean sellBuy, CompanyActivityClassEnum companyActivityClassEnum) {
        StringBuilder error = new StringBuilder();
        try {
            
            detailDTO = checkQuantityFromLoaded(isload, detailDTO, companyActivityClassEnum, screenNameClassEnum);
            
            if (detailDTO.getMsg() != null && !detailDTO.getMsg().isEmpty()) {
                error.append(detailDTO.getMsg());
                return detailDTO;
            }
            
            if (ScreenNameClassEnum.INVPERMISSIONORDERFORM.equals(screenNameClassEnum)
                    || ScreenNameClassEnum.INVSALESINVOICEFORM.equals(screenNameClassEnum)
                    || ScreenNameClassEnum.PROWORKORDER.equals(screenNameClassEnum)
                    || ScreenNameClassEnum.INVRETURNPURCHASEFORM.equals(screenNameClassEnum)
                    || ScreenNameClassEnum.INVTRANSFERINVENTORY.equals(screenNameClassEnum)
                    || ScreenNameClassEnum.INVRESERVATIONFORM.equals(screenNameClassEnum)) {
//                BigDecimal avilableQuantity = quantityItemsStoreAddExitService.getQuantatyItemByinventory(inventoryId, detailDTO.getItemsBarcodesDetail().getItemId(), sellBuy);
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
    
    private InvDetailDTO setDefaultQuantity(InvDetailDTO detailDTO) {
        detailDTO.setQuantity(detailDTO.getOriginQuantity());
        detailDTO.setTotalQuantityWithScrewing(detailDTO.getOriginQuantity().multiply(detailDTO.getScrewing()));
        return detailDTO;
    }
    
    @Override
    public List<InvDetailDTO> updatePriceAndQuantityForDetail(InvInvoiceDTO invInvoiceDTO) {
        List<InvDetailDTO> detailDTOList = new ArrayList<>();
        for (InvDetailDTO dTO : invInvoiceDTO.getInvPurchaseInvoiceDetailList()) {
            dTO = findDataForItem(dTO, invInvoiceDTO, null);
            detailDTOList.add(dTO);
        }
        return detailDTOList;
    }
    
    @Override
    public InvDetailDTO validateItems(InvDetailDTO detailDTO, InvInvoiceDTO invInvoiceDTO) {
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
                    detailDTO.setItemId(itemService.findInvItemDTO(detailDTO.getItemsBarcodesDetail().getItemId()));
                    detailDTO.setIsInventoryItem(detailDTO.getItemsBarcodesDetail().getIsInventoryItem());
                    detailDTO = findDataForItem(detailDTO, invInvoiceDTO, null);
                    if (!invInvoiceDTO.getInventorySetupDTO().getNegativeSell()) {
                        detailDTO = updateAvailableQuantityRow(invInvoiceDTO.getScreenName(), detailDTO, invInvoiceDTO.getInvInventoryId() == null ? null : invInvoiceDTO.getInvInventoryId().getId(), invInvoiceDTO.getInventorySetupDTO().getSellBuy(), invInvoiceDTO.getInventorySetupDTO().getNegativeSell(), invInvoiceDTO.getIsEdit());
                    }
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
    
    private InvDetailDTO findDataForItem(InvDetailDTO invDetailDTO, InvInvoiceDTO invInvoiceDTO, Integer offerType) {
        if (invInvoiceDTO.getCompanyActivity().equals(CompanyActivityClassEnum.RetailSaleForClothFactory)
                && (ScreenNameClassEnum.INVSALESINVOICEFORM.equals(invInvoiceDTO.getScreenName())
                || ScreenNameClassEnum.PROWORKORDER.equals(invInvoiceDTO.getScreenName())
                || ScreenNameClassEnum.INVTRANSFERINVENTORY.equals(invInvoiceDTO.getScreenName()))) {
            ItemsBarcodesDetailsView barcodesDetailsView = invDetailDTO.getItemsBarcodesDetail();
            
            if (barcodesDetailsView.getDiscountrate() != null
                    && barcodesDetailsView.getDiscountrate().compareTo(BigDecimal.ZERO) == 1) {
                invDetailDTO.setShowDiscount(true);
                invDetailDTO.setShowPrice(true);
            }
            
            if (invInvoiceDTO.getPricetype() != null && 1 == invInvoiceDTO.getPricetype()) {
                if (offerType != null && offerType == 1) {
                    offer999(barcodesDetailsView, invDetailDTO, invInvoiceDTO);
                } else {
                    if (barcodesDetailsView.getTypeShow() == null) {
                        invDetailDTO.setQuantityOfOneUnit(barcodesDetailsView.getNumberMetersMen());
                        invDetailDTO.setCost(calcCost(invDetailDTO, invInvoiceDTO));
                    } else if (barcodesDetailsView.getTypeShow() == 1) {
                        invDetailDTO.setCost(barcodesDetailsView.getMaxPriceMen());
                        invDetailDTO.setQuantityOfOneUnit(new BigDecimal(5));
                    } else if (barcodesDetailsView.getTypeShow() == 2) {
                        invDetailDTO.setCost(barcodesDetailsView.getMaxPriceMen());
                        invDetailDTO.setQuantityOfOneUnit(new BigDecimal(3.5));
                    }
                    
                }
            } else {
                invDetailDTO.setCost(barcodesDetailsView.getMaxPriceYoung());
                if (offerType != null && offerType == 1) {
                    if (barcodesDetailsView.getTypeShow() == null) {
                        invDetailDTO.setQuantityOfOneUnit(barcodesDetailsView.getNumberMetersYoung());
                        invDetailDTO.setCost(calcCost(invDetailDTO, invInvoiceDTO));
                    } else if (barcodesDetailsView.getTypeShow() == 1) {
                        invDetailDTO.setQuantityOfOneUnit(new BigDecimal(4));
                        if (invDetailDTO.getItemsBarcodesDetail().getSellPrice().compareTo(BigDecimal.valueOf(16.67)) == 1
                                && invDetailDTO.getItemsBarcodesDetail().getProGroupId() != null && invDetailDTO.getItemsBarcodesDetail().getProGroupId() == 1) {
                            invDetailDTO.setCost(barcodesDetailsView.getMaxPriceYoung());
                        } else {
                            invDetailDTO.setCost(BigDecimal.valueOf(289.565));
                        }
                    } else if (barcodesDetailsView.getTypeShow() == 2) {
                        invDetailDTO.setQuantityOfOneUnit(BigDecimal.valueOf(2.5));
                        if (invDetailDTO.getItemsBarcodesDetail().getSellPrice().compareTo(new BigDecimal(20)) == 1
                                && invDetailDTO.getItemsBarcodesDetail().getProGroupId() != null && invDetailDTO.getItemsBarcodesDetail().getProGroupId() == 1) {
                            invDetailDTO.setCost(barcodesDetailsView.getMaxPriceYoung());
                        } else {
                            invDetailDTO.setCost(BigDecimal.valueOf(289.565));
                        }
                    }
                } else {
                    if (barcodesDetailsView.getTypeShow() == null) {
                        invDetailDTO.setQuantityOfOneUnit(barcodesDetailsView.getNumberMetersYoung());
                        invDetailDTO.setCost(calcCost(invDetailDTO, invInvoiceDTO));
                    } else if (barcodesDetailsView.getTypeShow() == 1) {
                        invDetailDTO.setQuantityOfOneUnit(new BigDecimal(4));
                        invDetailDTO.setCost(barcodesDetailsView.getMaxPriceYoung());
                    } else if (barcodesDetailsView.getTypeShow() == 2) {
                        invDetailDTO.setQuantityOfOneUnit(BigDecimal.valueOf(2.5));
                        invDetailDTO.setCost(barcodesDetailsView.getMaxPriceYoung());
                        
                    }
                }
            }
            invDetailDTO.setQuantity(invDetailDTO.getQuantityOfOneUnit().multiply(invDetailDTO.getNumber()));
            invDetailDTO.setDiscount(barcodesDetailsView.getDiscountrate());
            invDetailDTO.setScrewing(barcodesDetailsView.getScrewing());
        } else {
            invDetailDTO = updatePriceRow(invInvoiceDTO.getScreenName(), invDetailDTO);
        }
        return invDetailDTO;
    }

    private void offer999(ItemsBarcodesDetailsView barcodesDetailsView, InvDetailDTO invDetailDTO, InvInvoiceDTO invInvoiceDTO) {
        if (barcodesDetailsView.getTypeShow() == null) {
            invDetailDTO.setQuantityOfOneUnit(barcodesDetailsView.getNumberMetersMen());
            invDetailDTO.setCost(calcCost(invDetailDTO, invInvoiceDTO));
        } else if (barcodesDetailsView.getTypeShow() == 1) {
            if (invDetailDTO.getItemsBarcodesDetail().getSellPrice().compareTo(BigDecimal.valueOf(16.67)) == 1
                    && invDetailDTO.getItemsBarcodesDetail().getProGroupId() != null && invDetailDTO.getItemsBarcodesDetail().getProGroupId() == 1) {
                invDetailDTO.setCost(barcodesDetailsView.getMaxPriceMen());
            } else {
                invDetailDTO.setCost(BigDecimal.valueOf(289.565));
            }
            invDetailDTO.setQuantityOfOneUnit(new BigDecimal(5));
        } else if (barcodesDetailsView.getTypeShow() == 2) {
            if (invDetailDTO.getItemsBarcodesDetail().getSellPrice().compareTo(new BigDecimal(20)) == 1
                    && invDetailDTO.getItemsBarcodesDetail().getProGroupId() != null && invDetailDTO.getItemsBarcodesDetail().getProGroupId() == 1) {
                invDetailDTO.setCost(barcodesDetailsView.getMaxPriceMen());
            } else {
                invDetailDTO.setCost(BigDecimal.valueOf(289.565));
            }
            invDetailDTO.setQuantityOfOneUnit(new BigDecimal(3.5));
        }
    }
    
    private InvDetailDTO updatePriceRow(ScreenNameClassEnum screenNameClassEnum, InvDetailDTO detailDTO) {
        if (ScreenNameClassEnum.INVSALESINVOICEFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVRETURNSALESFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVQUOTATIONFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.PROWORKORDER.equals(screenNameClassEnum)
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
                || ScreenNameClassEnum.PROWORKORDER.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVRESERVATIONFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVRETURNPURCHASEFORM.equals(screenNameClassEnum)) {
            detailDTO.setTotalQuantityWithScrewing(detailDTO.getQuantity().multiply(detailDTO.getScrewing()));
            detailDTO.setAvailableQuantityRow(quantityItemsStoreAddExitService.getQuantatyItemByinventory(inventoryId, detailDTO.getItemsBarcodesDetail().getItemId(), sellBuy, null));
//            detailDTO.setAvailableQuantityRow(quantityItemsStoreAddExitService.getQuantatyItemByinventory(inventoryId, detailDTO.getItemsBarcodesDetail().getItemId(), sellBuy));
            if ((detailDTO.getAvailableQuantityRow() == null || detailDTO.getAvailableQuantityRow().compareTo(BigDecimal.ZERO) < 1) && !sellNegative && !isEdit) {
                detailDTO.setItemsBarcodesDetail(null);
                detailDTO.setItemsBarcodesDetailTrans(null);
                detailDTO.setQuantity(BigDecimal.ZERO);
                detailDTO.setCost(BigDecimal.ZERO);
                detailDTO.setMsg("الصنف لا يوجد به كمية بالمخزن");
            } else if (ScreenNameClassEnum.INVTRANSFERINVENTORY.equals(screenNameClassEnum)) {
                detailDTO.setAvailableQuantityRow(quantityItemsStoreAddExitService.getQuantatyItemByinventory(inventoryId, detailDTO.getItemsBarcodesDetail().getItemId(), sellBuy, null));
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
    public InvDetailDTO validateQuantityOfUnitColum(InvDetailDTO detailDTO, InvInvoiceDTO invInvoiceDTO) {
        try {
            validateQuantityColum(detailDTO, (invInvoiceDTO.getLoadedSerial() != null), invInvoiceDTO.getScreenName(),
                    invInvoiceDTO.getInventorySetupDTO().getNegativeSell(), invInvoiceDTO.getCompanyActivity());
            detailDTO.setCost(calcCost(detailDTO, invInvoiceDTO));
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
                if (companyActivityClassEnum.equals(CompanyActivityClassEnum.RetailSaleForClothFactory) && (screenNameClassEnum.equals(ScreenNameClassEnum.INVSALESINVOICEFORM) || screenNameClassEnum.equals(ScreenNameClassEnum.PROWORKORDER))) {
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
    
    private InvDetailDTO setDefaultQuantity(InvDetailDTO detailDTO, CompanyActivityClassEnum companyActivityClassEnum, ScreenNameClassEnum screenNameClassEnum) {
        if (companyActivityClassEnum.equals(CompanyActivityClassEnum.RetailSaleForClothFactory)
                && (screenNameClassEnum.equals(ScreenNameClassEnum.PROWORKORDER))) {
            detailDTO.setNumber(detailDTO.getOriginQuantity());
            detailDTO.setQuantity(detailDTO.getNumber().multiply(detailDTO.getQuantityOfOneUnit()));
        } else {
            detailDTO.setQuantity(detailDTO.getOriginQuantity());
        }
        detailDTO.setTotalQuantityWithScrewing(detailDTO.getQuantity().multiply(detailDTO.getScrewing()));
        return detailDTO;
    }
    
    private InvDetailDTO validateTotalQuantity(InvDetailDTO detailDTO, Boolean isload, ScreenNameClassEnum screenNameClassEnum, Boolean negativeSell,
            CompanyActivityClassEnum companyActivityClassEnum) {
        if ((ScreenNameClassEnum.INVSALESINVOICEFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.PROWORKORDER.equals(screenNameClassEnum)
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
    public InvDetailDTO fillItemDetail(InvDetailDTO dTO, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap,
            Integer id, Integer serial, Integer branchId, BigDecimal cost, BigDecimal discount,
            BigDecimal quantity, Integer discountType, Integer invInvoiceId,
            BigDecimal extraCost, BigDecimal net, Integer index, Date createdDate, Integer createdBy,
            Integer clothNumber, Integer idParent, String itemBarcodeParent, String itemNameParent, Integer proof,
            String itemBarcode, BigDecimal screwing, Integer itemId, BigDecimal weight, Integer unitId,
            Integer unitSerial, String unitName, Integer inventoryItem, BigDecimal number,
            BigDecimal quantityOfOneUnit, InvInventory invInventory, InvItem invItem,
            CompanyActivityClassEnum companyActivityClassEnum, ScreenNameClassEnum screenNameClassEnum, Integer detailId) {
        
        if (companyActivityClassEnum.equals(CompanyActivityClassEnum.RetailSaleForClothFactory)
                && (ScreenNameClassEnum.INVSALESINVOICEFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.PROWORKORDER.equals(screenNameClassEnum)
                || ScreenNameClassEnum.PROFACTORYDELIVERY.equals(screenNameClassEnum))) {
            dTO.setNumber(number);
            dTO.setQuantityOfOneUnit(quantityOfOneUnit);
            dTO.setClothNumber(clothNumber);
            dTO.setProof(proof != null && proof == 1);
            if (invInventory != null && invInventory.getId() != null) {
                InvInventoryDTO invInventoryDTO = new InvInventoryDTO();
                invInventoryDTO.setId(invInventory.getId());
                invInventoryDTO.setCode(invInventory.getCode());
                invInventoryDTO.setName(invInventory.getName());
                dTO.setInventoryDTO(invInventoryDTO);
            }
            if (idParent != null) {
                InvItemDTO invItemDTO = new InvItemDTO();
                invItemDTO.setId(idParent);
                invItemDTO.setCode(itemBarcodeParent);
                invItemDTO.setName(itemNameParent);
                dTO.setInvItemParentId(invItemDTO);
            }
            
            if (discount != null && discount.compareTo(BigDecimal.ZERO) == 1) {
                dTO.setShowPrice(Boolean.TRUE);
            }
            if (ScreenNameClassEnum.PROFACTORYDELIVERY.equals(screenNameClassEnum) && detailId != null) {
                dTO.setInvPurchaseInvoiceDetailId(detailId);
            }
        }
        dTO.setId(id);
        dTO.setSerial(serial);
        
        dTO.setBranchId(branchId);
        dTO.setCost(cost != null ? cost.setScale(3, BigDecimal.ROUND_UP) : BigDecimal.ZERO);
        dTO.setDiscount(discount != null ? discount.setScale(3, BigDecimal.ROUND_UP) : BigDecimal.ZERO);
        dTO.setQuantity(quantity != null ? quantity.setScale(3, BigDecimal.ROUND_UP) : BigDecimal.ZERO);
        dTO.setOriginQuantity(dTO.getQuantity().multiply(screwing == null ? BigDecimal.ONE : screwing));
        dTO.setTotalQuantityWithScrewing(dTO.getQuantity().multiply(dTO.getScrewing()));
        dTO.setIsEdit(Boolean.TRUE);
        dTO.setDiscountType(discountType);
        dTO.setInvInvoiceId(invInvoiceId);
        dTO.setIsInventoryItem(inventoryItem);
        
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
        
        if (itemId != null) {
            InvItemDTO invItemDTO = new InvItemDTO();
            invItemDTO.setId(itemId);
            dTO.setItemId(invItemDTO);
        }
        dTO.setExtraCost(extraCost);
        dTO.setNet(net);
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
    public Integer getDeliveryDaysByGalleryId(Integer galleryId) {
        Integer deliveryDays = null;
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT gs.delivery_days FROM toby.gallery_settings gs ").
                append("where gs.gallery_id = \n ").
                append(galleryId).append(" ");
        List<Integer> details = dao.executeNativeQuery(builder.toString());
        for (Integer deliveryDaysByGalleryId : details) {
            deliveryDays = deliveryDaysByGalleryId;
        }
        return deliveryDays;
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
