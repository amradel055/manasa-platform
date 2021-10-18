/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import com.isag.views.ItemsBarcodesDetailsView;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author amr
 */
public class InvDetailDTO extends BaseEntityDTO {

    private Integer discountType;
    private Integer transferFrom;
    private BigDecimal finalQuantity;
    private BigDecimal weight;
    private Integer invInvoiceId;
    private InvItemDTO itemId;
    private SymbolDTO unitId;
    private BigDecimal extraCost;
    private Integer selectedId;
    private String itemBarcode;
    private BigDecimal taxValue;
    private Integer status;
    private BigDecimal costAvarage;
    private Boolean isEdit;
    private Boolean cadoo;
    private String barCode;

    private ItemsBarcodesDetailsView itemsBarcodesDetail;
    private ItemsBarcodesDetailsView itemsBarcodesDetailTrans;
    private SymbolDTO paintColor;
    private BigDecimal length;
    private BigDecimal valueAfterDiscount;
    private BigDecimal availableQuantityRow;
    private BigDecimal totalQuantity;
    private BigDecimal totalQuantityWithScrewing;
    private BigDecimal realQuantity;
    private BigDecimal originQuantity;
    private BigDecimal actualQuantity;
    private BigDecimal actualQuantityBak;
    private BigDecimal oldQuantity;
    private BigDecimal oldQuantityBak;
    private BigDecimal differanceQuantity;
    private BigDecimal totalQuantityCost;
    private BigDecimal finalPrice;

    private BigDecimal quantity;
    private String itemName;
    private String itemCode;
    private String unitName;
    private String color;
    private BigDecimal discount;
    private BigDecimal cost;
    private BigDecimal net;
    private BigDecimal totalBeforeDiscount;
    private Integer serial;
    private BigDecimal screwing;
    private Integer isInventoryItem;
    private BigDecimal valueRowDiscount;

    // ceramic Data
    private BigDecimal addTotal;
    private BigDecimal add1;
    private BigDecimal add2;
    private BigDecimal add3;
    private BigDecimal disc1;
    private BigDecimal disc2;
    private BigDecimal disc3;
    private BigDecimal totalBeforefracture;
    private BigDecimal fractureNumber;
    private BigDecimal valuefracture;
    private BigDecimal cartonsNumber;
    private BigDecimal cartonsCapacity;
    private String sizeName;
    private String sortName;

    //
    private BigDecimal profitVlaue;
    private BigDecimal profitPercent;

    public InvDetailDTO() {
    }

    public BigDecimal getQuantity() {
        if (quantity == null) {
            quantity = BigDecimal.ONE;
        }
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getCost() {
        if (cost == null) {
            cost = BigDecimal.ZERO;
        }
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getDiscount() {
        if (discount == null) {
            discount = BigDecimal.ZERO;
        }
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public Integer getTransferFrom() {
        return transferFrom;
    }

    public void setTransferFrom(Integer transferFrom) {
        this.transferFrom = transferFrom;
    }

    public Integer getInvInvoiceId() {
        return invInvoiceId;
    }

    public void setInvInvoiceId(Integer invInvoiceId) {
        this.invInvoiceId = invInvoiceId;
    }

    public InvItemDTO getItemId() {
        return itemId;
    }

    public void setItemId(InvItemDTO itemId) {
        this.itemId = itemId;
    }

    public SymbolDTO getUnitId() {
        return unitId;
    }

    public void setUnitId(SymbolDTO unitId) {
        this.unitId = unitId;
    }

    /**
     * @return the extraCost
     */
    public BigDecimal getExtraCost() {
        if (extraCost == null) {
            extraCost = BigDecimal.ZERO;
        }
        return extraCost;
    }

    public BigDecimal getScrewing() {
        if (screwing == null) {
            screwing = BigDecimal.ONE;
        }
        return screwing;
    }

    public void setScrewing(BigDecimal screwing) {
        this.screwing = screwing;
    }

    /**
     * @param extraCost the extraCost to set
     */
    public void setExtraCost(BigDecimal extraCost) {
        this.extraCost = extraCost;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getIndex() != null ? getIndex().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvDetailDTO)) {
            return false;
        }
        InvDetailDTO other = (InvDetailDTO) object;
        if ((this.getIndex() == null && other.getIndex() != null) || (this.getIndex() != null && !this.getIndex().equals(other.getIndex()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return itemBarcode + " " + (itemId == null ? null : itemId.getName());
    }

    public Integer getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(Integer selectedId) {
        this.selectedId = selectedId;
    }

    /**
     * @return the itemBarcode
     */
    public String getItemBarcode() {
        return itemBarcode;
    }

    /**
     * @param itemBarcode the itemBarcode to set
     */
    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    /**
     * @return the net
     */
    public BigDecimal getNet() {
        if (net == null) {
            net = BigDecimal.ZERO;
        }
        return net;
    }

    /**
     * @param net the net to set
     */
    public void setNet(BigDecimal net) {
        this.net = net;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public BigDecimal getTaxValue() {
        if (taxValue == null) {
            taxValue = BigDecimal.ZERO;
        }
        return taxValue;
    }

    public void setTaxValue(BigDecimal taxValue) {
        this.taxValue = taxValue;
    }

    /**
     * @return the finalQuantity
     */
    public BigDecimal getFinalQuantity() {
        if (finalQuantity == null) {
            finalQuantity = BigDecimal.ZERO;
        }
        return finalQuantity;
    }

    /**
     * @param finalQuantity the finalQuantity to set
     */
    public void setFinalQuantity(BigDecimal finalQuantity) {
        this.finalQuantity = finalQuantity;
    }

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return the weight
     */
    public BigDecimal getWeight() {
        if (weight == null) {
            weight = BigDecimal.ZERO;
        }
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    /**
     * @return the costAvarage
     */
    public BigDecimal getCostAvarage() {
        if (costAvarage == null) {
            costAvarage = BigDecimal.ZERO;
        }
        return costAvarage;
    }

    /**
     * @param costAvarage the costAvarage to set
     */
    public void setCostAvarage(BigDecimal costAvarage) {
        this.costAvarage = costAvarage;
    }

    /**
     * @return the itemsBarcodesDetail
     */
    public ItemsBarcodesDetailsView getItemsBarcodesDetail() {
        return itemsBarcodesDetail;
    }

    /**
     * @param itemsBarcodesDetail the itemsBarcodesDetail to set
     */
    public void setItemsBarcodesDetail(ItemsBarcodesDetailsView itemsBarcodesDetail) {
        this.itemsBarcodesDetail = itemsBarcodesDetail;
    }

    /**
     * @return the itemsBarcodesDetailTrans
     */
    public ItemsBarcodesDetailsView getItemsBarcodesDetailTrans() {
        return itemsBarcodesDetailTrans;
    }

    /**
     * @param itemsBarcodesDetailTrans the itemsBarcodesDetailTrans to set
     */
    public void setItemsBarcodesDetailTrans(ItemsBarcodesDetailsView itemsBarcodesDetailTrans) {
        this.itemsBarcodesDetailTrans = itemsBarcodesDetailTrans;
    }

    /**
     * @return the paintColor
     */
    public SymbolDTO getPaintColor() {
        return paintColor;
    }

    /**
     * @param paintColor the paintColor to set
     */
    public void setPaintColor(SymbolDTO paintColor) {
        this.paintColor = paintColor;
    }

    /**
     * @return the length
     */
    public BigDecimal getLength() {
        if (length == null) {
            length = BigDecimal.ZERO;
        }
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(BigDecimal length) {
        this.length = length;
    }

    /**
     * @return the valueAfterDiscount
     */
    public BigDecimal getValueAfterDiscount() {
        return valueAfterDiscount;
    }

    /**
     * @param valueAfterDiscount the valueAfterDiscount to set
     */
    public void setValueAfterDiscount(BigDecimal valueAfterDiscount) {
        this.valueAfterDiscount = valueAfterDiscount;
    }

    /**
     * @return the availableQuantityRow
     */
    public BigDecimal getAvailableQuantityRow() {
        if (availableQuantityRow == null) {
            availableQuantityRow = BigDecimal.ZERO;
        }
        return availableQuantityRow;
    }

    /**
     * @param availableQuantityRow the availableQuantityRow to set
     */
    public void setAvailableQuantityRow(BigDecimal availableQuantityRow) {
        this.availableQuantityRow = availableQuantityRow;
    }

    /**
     * @return the totalQuantity
     */
    public BigDecimal getTotalQuantity() {
        if (totalQuantity == null) {
            totalQuantity = BigDecimal.ZERO;
        }
        return totalQuantity;
    }

    /**
     * @param totalQuantity the totalQuantity to set
     */
    public void setTotalQuantity(BigDecimal totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    /**
     * @return the totalQuantityWithScrewing
     */
    public BigDecimal getTotalQuantityWithScrewing() {
        if (totalQuantityWithScrewing == null) {
            totalQuantityWithScrewing = BigDecimal.ZERO;
        }
        return totalQuantityWithScrewing;
    }

    /**
     * @param totalQuantityWithScrewing the totalQuantityWithScrewing to set
     */
    public void setTotalQuantityWithScrewing(BigDecimal totalQuantityWithScrewing) {
        this.totalQuantityWithScrewing = totalQuantityWithScrewing;
    }

    /**
     * @return the realQuantity
     */
    public BigDecimal getRealQuantity() {
        return realQuantity;
    }

    /**
     * @param realQuantity the realQuantity to set
     */
    public void setRealQuantity(BigDecimal realQuantity) {
        this.realQuantity = realQuantity;
    }

    /**
     * @return the originQuantity
     */
    public BigDecimal getOriginQuantity() {
        if (originQuantity == null) {
            originQuantity = BigDecimal.ZERO;
        }
        return originQuantity;
    }

    /**
     * @param originQuantity the originQuantity to set
     */
    public void setOriginQuantity(BigDecimal originQuantity) {
        this.originQuantity = originQuantity;
    }

    /**
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName the itemName to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * @return the unitName
     */
    public String getUnitName() {
        return unitName;
    }

    /**
     * @param unitName the unitName to set
     */
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    /**
     * @return the itemCode
     */
    public String getItemCode() {
        return itemCode;
    }

    /**
     * @param itemCode the itemCode to set
     */
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @return the isEdit
     */
    public Boolean getIsEdit() {
        if (isEdit == null) {
            isEdit = false;
        }
        return isEdit;
    }

    /**
     * @param isEdit the isEdit to set
     */
    public void setIsEdit(Boolean isEdit) {
        this.isEdit = isEdit;
    }

    /**
     * @return the isInventoryItem
     */
    public Integer getIsInventoryItem() {
        return isInventoryItem;
    }

    /**
     * @param isInventoryItem the isInventoryItem to set
     */
    public void setIsInventoryItem(Integer isInventoryItem) {
        this.isInventoryItem = isInventoryItem;
    }

    /**
     * @return the totalBeforeDiscount
     */
    public BigDecimal getTotalBeforeDiscount() {
        return totalBeforeDiscount;
    }

    /**
     * @param totalBeforeDiscount the totalBeforeDiscount to set
     */
    public void setTotalBeforeDiscount(BigDecimal totalBeforeDiscount) {
        this.totalBeforeDiscount = totalBeforeDiscount;
    }

    /**
     * @return the barCode
     */
    public String getBarCode() {
        return barCode;
    }

    /**
     * @param barCode the barCode to set
     */
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    /**
     * @return the actualQuantity
     */
    public BigDecimal getActualQuantity() {
        if (actualQuantity == null) {
            actualQuantity = BigDecimal.ZERO;
        }
        return actualQuantity;
    }

    /**
     * @param actualQuantity the actualQuantity to set
     */
    public void setActualQuantity(BigDecimal actualQuantity) {
        this.actualQuantity = actualQuantity;
    }

    /**
     * @return the actualQuantityBak
     */
    public BigDecimal getActualQuantityBak() {
        return actualQuantityBak;
    }

    /**
     * @param actualQuantityBak the actualQuantityBak to set
     */
    public void setActualQuantityBak(BigDecimal actualQuantityBak) {
        this.actualQuantityBak = actualQuantityBak;
    }

    /**
     * @return the valueRowDiscount
     */
    public BigDecimal getValueRowDiscount() {
        if(valueRowDiscount == null){
            valueRowDiscount = BigDecimal.ZERO;
        }
        return valueRowDiscount;
    }

    /**
     * @param valueRowDiscount the valueRowDiscount to set
     */
    public void setValueRowDiscount(BigDecimal valueRowDiscount) {
        this.valueRowDiscount = valueRowDiscount;
    }

    public Boolean getCadoo() {
        if (cadoo == null) {
            cadoo = Boolean.FALSE;
        }
        return cadoo;
    }

    /**
     * @param cadoo the cadoo to set
     */
    public void setCadoo(Boolean cadoo) {
        this.cadoo = cadoo;
    }

    /**
     * @return the differanceQuantity
     */
    public BigDecimal getDifferanceQuantity() {
        differanceQuantity = getActualQuantity().subtract(getOldQuantity() == null ? BigDecimal.ZERO : getOldQuantity());
        return differanceQuantity;
    }

    /**
     * @param differanceQuantity the differanceQuantity to set
     */
    public void setDifferanceQuantity(BigDecimal differanceQuantity) {
        this.differanceQuantity = differanceQuantity;
    }

    /**
     * @return the totalQuantityCost
     */
    public BigDecimal getTotalQuantityCost() {
        totalQuantityCost = getDifferanceQuantity().multiply(getCostAvarage());
        return totalQuantityCost;
    }

    /**
     * @param totalQuantityCost the totalQuantityCost to set
     */
    public void setTotalQuantityCost(BigDecimal totalQuantityCost) {
        this.totalQuantityCost = totalQuantityCost;
    }

    /**
     * @return the oldQuantity
     */
    public BigDecimal getOldQuantity() {
        return oldQuantity;
    }

    /**
     * @param oldQuantity the oldQuantity to set
     */
    public void setOldQuantity(BigDecimal oldQuantity) {
        this.oldQuantity = oldQuantity;
    }

    /**
     * @return the oldQuantityBak
     */
    public BigDecimal getOldQuantityBak() {
        return oldQuantityBak;
    }

    /**
     * @param oldQuantityBak the oldQuantityBak to set
     */
    public void setOldQuantityBak(BigDecimal oldQuantityBak) {
        this.oldQuantityBak = oldQuantityBak;
    }

    /**
     * @return the add1
     */
    public BigDecimal getAdd1() {
        if (add1 == null) {
            add1 = BigDecimal.ZERO;
        } else {
            add1 = add1.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return add1;
    }

    /**
     * @param add1 the add1 to set
     */
    public void setAdd1(BigDecimal add1) {
        this.add1 = add1;
    }

    /**
     * @return the add2
     */
    public BigDecimal getAdd2() {
        if (add2 == null) {
            add2 = BigDecimal.ZERO;
        } else {
            add2 = add2.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return add2;
    }

    /**
     * @param add2 the add2 to set
     */
    public void setAdd2(BigDecimal add2) {
        this.add2 = add2;
    }

    /**
     * @return the add3
     */
    public BigDecimal getAdd3() {
        if (add3 == null) {
            add3 = BigDecimal.ZERO;
        } else {
            add3 = add3.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return add3;
    }

    /**
     * @param add3 the add3 to set
     */
    public void setAdd3(BigDecimal add3) {
        this.add3 = add3;
    }

    /**
     * @return the disc1
     */
    public BigDecimal getDisc1() {
        if (disc1 == null) {
            disc1 = BigDecimal.ZERO;
        } else {
            disc1 = disc1.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return disc1;
    }

    /**
     * @param disc1 the disc1 to set
     */
    public void setDisc1(BigDecimal disc1) {
        this.disc1 = disc1;
    }

    /**
     * @return the disc2
     */
    public BigDecimal getDisc2() {
        if (disc2 == null) {
            disc2 = BigDecimal.ZERO;
        } else {
            disc2 = disc2.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return disc2;
    }

    /**
     * @param disc2 the disc2 to set
     */
    public void setDisc2(BigDecimal disc2) {
        this.disc2 = disc2;
    }

    /**
     * @return the disc3
     */
    public BigDecimal getDisc3() {
        if (disc3 == null) {
            disc3 = BigDecimal.ZERO;
        } else {
            disc3 = disc3.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return disc3;
    }

    /**
     * @param disc3 the disc3 to set
     */
    public void setDisc3(BigDecimal disc3) {
        this.disc3 = disc3;
    }

    /**
     * @return the totalBeforefracture
     */
    public BigDecimal getTotalBeforefracture() {
        if (totalBeforefracture == null) {
            totalBeforefracture = BigDecimal.ZERO;
        } else {
            totalBeforefracture = totalBeforefracture.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return totalBeforefracture;
    }

    /**
     * @param totalBeforefracture the totalBeforefracture to set
     */
    public void setTotalBeforefracture(BigDecimal totalBeforefracture) {
        this.totalBeforefracture = totalBeforefracture;
    }

    /**
     * @return the fractureNumber
     */
    public BigDecimal getFractureNumber() {
        if (fractureNumber == null) {
            fractureNumber = BigDecimal.ZERO;
        } else {
            fractureNumber = fractureNumber.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return fractureNumber;
    }

    /**
     * @param fractureNumber the fractureNumber to set
     */
    public void setFractureNumber(BigDecimal fractureNumber) {
        this.fractureNumber = fractureNumber;
    }

    /**
     * @return the valuefracture
     */
    public BigDecimal getValuefracture() {
        if (valuefracture == null) {
            valuefracture = BigDecimal.ZERO;
        } else {
            valuefracture = valuefracture.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return valuefracture;
    }

    /**
     * @param valuefracture the valuefracture to set
     */
    public void setValuefracture(BigDecimal valuefracture) {
        this.valuefracture = valuefracture;
    }

    /**
     * @return the cartonsNumber
     */
    public BigDecimal getCartonsNumber() {
        if (cartonsNumber == null) {
            cartonsNumber = BigDecimal.ZERO;
        } else {
            cartonsNumber = cartonsNumber.setScale(1, BigDecimal.ROUND_HALF_UP);
        }
        return cartonsNumber;
    }

    /**
     * @param cartonsNumber the cartonsNumber to set
     */
    public void setCartonsNumber(BigDecimal cartonsNumber) {
        this.cartonsNumber = cartonsNumber;
    }

    /**
     * @return the cartonsCapacity
     */
    public BigDecimal getCartonsCapacity() {
        if (cartonsCapacity == null) {
            cartonsCapacity = BigDecimal.ZERO;
        }
        return cartonsCapacity;
    }

    /**
     * @param cartonsCapacity the cartonsCapacity to set
     */
    public void setCartonsCapacity(BigDecimal cartonsCapacity) {
        this.cartonsCapacity = cartonsCapacity;
    }

    /**
     * @return the finalPrice
     */
    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    /**
     * @param finalPrice the finalPrice to set
     */
    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    /**
     * @return the addTotal
     */
    public BigDecimal getAddTotal() {
        if (addTotal == null) {
            addTotal = BigDecimal.ZERO;
        } else {
            addTotal = addTotal.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return addTotal;
    }

    /**
     * @param addTotal the addTotal to set
     */
    public void setAddTotal(BigDecimal addTotal) {
        this.addTotal = addTotal;
    }

    /**
     * @return the sizeName
     */
    public String getSizeName() {
        if (itemsBarcodesDetail != null && itemsBarcodesDetail.getSize() != null) {
            sizeName = itemsBarcodesDetail.getSize();
        }
        return sizeName;
    }

    /**
     * @param sizeName the sizeName to set
     */
    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    /**
     * @return the sortName
     */
    public String getSortName() {
        return sortName;
    }

    /**
     * @param sortName the sortName to set
     */
    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    /**
     * @return the profitVlaue
     */
    public BigDecimal getProfitVlaue() {
        if (getItemsBarcodesDetail() != null || getItemsBarcodesDetailTrans() != null) {
            if (getItemsBarcodesDetail() == null && getItemsBarcodesDetailTrans() != null) {
                setItemsBarcodesDetail(getItemsBarcodesDetail());
            }
            profitVlaue = getItemsBarcodesDetail().getSellPrice().subtract(cost);
        }
        return profitVlaue;
    }

    /**
     * @param profitVlaue the profitVlaue to set
     */
    public void setProfitVlaue(BigDecimal profitVlaue) {
        this.profitVlaue = profitVlaue;
    }

    /**
     * @return the profitPercent
     */
    public BigDecimal getProfitPercent() {
        if (getItemsBarcodesDetail() != null || getItemsBarcodesDetailTrans() != null) {
            if (getItemsBarcodesDetail() == null && getItemsBarcodesDetailTrans() != null) {
                setItemsBarcodesDetail(getItemsBarcodesDetail());
            }
            BigDecimal sellPrices = BigDecimal.ONE ;
            if(getItemsBarcodesDetail().getSellPrice() != null && getItemsBarcodesDetail().getSellPrice().compareTo(BigDecimal.ZERO) != 0){
                sellPrices = getItemsBarcodesDetail().getSellPrice();
            }
            profitPercent = getProfitVlaue().divide(sellPrices, 2, RoundingMode.HALF_UP);
        }
        return profitPercent;
    }

    /**
     * @param profitPercent the profitPercent to set
     */
    public void setProfitPercent(BigDecimal profitPercent) {
        this.profitPercent = profitPercent;
    }

}
