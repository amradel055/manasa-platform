/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import com.toby.views.ItemsBarcodesDetailsView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amr
 */
public class InvDetailDTO extends BaseEntityDTO implements  Comparable< InvDetailDTO >{

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

    private ItemsBarcodesDetailsView itemsBarcodesDetail;
    private ItemsBarcodesDetailsView itemsBarcodesDetailTrans;
    private SymbolDTO paintColor;
    private BigDecimal length;
    private BigDecimal valueAfterDiscount;
    private BigDecimal valueBeforeDiscount;
    private BigDecimal availableQuantityRow;
    private BigDecimal totalQuantity;
    private BigDecimal totalQuantityWithScrewing;
    private BigDecimal realQuantity;
    private BigDecimal originQuantity;

    private BigDecimal quantity;
    private BigDecimal quantityInch;
    private String itemName;
    private String itemCode;
    private String unitName;
    private String color;
    private BigDecimal discount;
    private BigDecimal discountValue;
    private BigDecimal cost;
    private BigDecimal costBak;
    private BigDecimal net;
    private Integer serial;
    private BigDecimal screwing;
    private Integer isInventoryItem;
    private String barCode;
    private BigDecimal costComparable;

    //////Coth ////////
    private Boolean showPrice;
    private Boolean showDiscount;
    private InvItemDTO invItemParentId;
    private Integer clothNumber;
    private BigDecimal number;
    private BigDecimal bounse;
    private BigDecimal quantityOfOneUnit;
    private InvInventoryDTO inventoryDTO;
    private Integer invPurchaseInvoiceDetailId;
    private Boolean proof;
    private String statusName;
    
    private BigDecimal costBefor;
    private BigDecimal costAfter;
    private Integer indexSerial;
    private BigDecimal costAfterDiscount;
    private List<ProDetailTypeDTO> proDetailTypeDTOList ;
    
    private List<InvPurchaseInvoiceDetailDetailDTO> invPurchaseInvoiceDetailDetailDTOList;

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
     * @return the showPrice
     */
    public Boolean getShowPrice() {
        return showPrice;
    }

    /**
     * @param showPrice the showPrice to set
     */
    public void setShowPrice(Boolean showPrice) {
        this.showPrice = showPrice;
    }

    /**
     * @return the showDiscount
     */
    public Boolean getShowDiscount() {
        return showDiscount;
    }

    /**
     * @param showDiscount the showDiscount to set
     */
    public void setShowDiscount(Boolean showDiscount) {
        this.showDiscount = showDiscount;
    }

    /**
     * @return the invItemParentId
     */
    public InvItemDTO getInvItemParentId() {
        return invItemParentId;
    }

    /**
     * @param invItemParentId the invItemParentId to set
     */
    public void setInvItemParentId(InvItemDTO invItemParentId) {
        this.invItemParentId = invItemParentId;
    }

    /**
     * @return the clothNumber
     */
    public Integer getClothNumber() {
        if (clothNumber == null) {
            clothNumber = 1;
        }
        return clothNumber;
    }

    /**
     * @param clothNumber the clothNumber to set
     */
    public void setClothNumber(Integer clothNumber) {
        this.clothNumber = clothNumber;
    }

    /**
     * @return the number
     */
    public BigDecimal getNumber() {
        if (number == null) {
            number = BigDecimal.ONE;
        }
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    /**
     * @return the bounse
     */
    public BigDecimal getBounse() {
        if (bounse == null) {
            bounse = BigDecimal.ZERO;
        }
        return bounse;
    }

    /**
     * @param bounse the bounse to set
     */
    public void setBounse(BigDecimal bounse) {
        this.bounse = bounse;
    }

    /**
     * @return the quantityOfOneUnit
     */
    public BigDecimal getQuantityOfOneUnit() {
        if(quantityOfOneUnit==null){
       quantityOfOneUnit=BigDecimal.ZERO;      
        }
        return quantityOfOneUnit;     
    }

    /**
     * @param quantityOfOneUnit the quantityOfOneUnit to set
     */
    public void setQuantityOfOneUnit(BigDecimal quantityOfOneUnit) {
        this.quantityOfOneUnit = quantityOfOneUnit;
    }

    /**
     * @return the inventoryDTO
     */
    public InvInventoryDTO getInventoryDTO() {
        return inventoryDTO;
    }

    /**
     * @param inventoryDTO the inventoryDTO to set
     */
    public void setInventoryDTO(InvInventoryDTO inventoryDTO) {
        this.inventoryDTO = inventoryDTO;
    }

    /**
     * @return the invPurchaseInvoiceDetailId
     */
    public Integer getInvPurchaseInvoiceDetailId() {
        return invPurchaseInvoiceDetailId;
    }

    /**
     * @param invPurchaseInvoiceDetailId the invPurchaseInvoiceDetailId to set
     */
    public void setInvPurchaseInvoiceDetailId(Integer invPurchaseInvoiceDetailId) {
        this.invPurchaseInvoiceDetailId = invPurchaseInvoiceDetailId;
    }

    /**
     * @return the proof
     */
    public Boolean getProof() {
        if (proof == null) {
            proof = false;
        }
        return proof;
    }

    /**
     * @param proof the proof to set
     */
    public void setProof(Boolean proof) {
        this.proof = proof;
    }

    /**
     * @return the proDetailTypeDTOList
     */
    public List<ProDetailTypeDTO> getProDetailTypeDTOList() {
        if(proDetailTypeDTOList == null){
            proDetailTypeDTOList = new ArrayList<>();
        }
        return proDetailTypeDTOList;
    }

    /**
     * @param proDetailTypeDTOList the proDetailTypeDTOList to set
     */
    public void setProDetailTypeDTOList(List<ProDetailTypeDTO> proDetailTypeDTOList) {
        this.proDetailTypeDTOList = proDetailTypeDTOList;
    }

    /**
     * @return the invPurchaseInvoiceDetailDetailDTOList
     */
    public List<InvPurchaseInvoiceDetailDetailDTO> getInvPurchaseInvoiceDetailDetailDTOList() {
        return invPurchaseInvoiceDetailDetailDTOList;
    }

    /**
     * @param invPurchaseInvoiceDetailDetailDTOList the invPurchaseInvoiceDetailDetailDTOList to set
     */
    public void setInvPurchaseInvoiceDetailDetailDTOList(List<InvPurchaseInvoiceDetailDetailDTO> invPurchaseInvoiceDetailDetailDTOList) {
        this.invPurchaseInvoiceDetailDetailDTOList = invPurchaseInvoiceDetailDetailDTOList;
    }

    /**
     * @return the statusName
     */
    public String getStatusName() {
        return statusName;
    }

    /**
     * @param statusName the statusName to set
     */
    public void setStatusName(String statusName) {
        this.statusName = statusName;
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
     * @return the costBefor
     */
    public BigDecimal getCostBefor() {
        return costBefor;
    }

    /**
     * @param costBefor the costBefor to set
     */
    public void setCostBefor(BigDecimal costBefor) {
        this.costBefor = costBefor;
    }

    /**
     * @return the costAfter
     */
    public BigDecimal getCostAfter() {
        return costAfter;
    }

    /**
     * @param costAfter the costAfter to set
     */
    public void setCostAfter(BigDecimal costAfter) {
        this.costAfter = costAfter;
    }

    /**
     * @return the discountValue
     */
    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    /**
     * @param discountValue the discountValue to set
     */
    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    @Override
    public int compareTo(InvDetailDTO o) {
        return this.getCostComparable().compareTo(o.getCostComparable());
    }

    /**
     * @return the costComparable
     */
    public BigDecimal getCostComparable() {
        return costComparable;
    }

    /**
     * @param costComparable the costComparable to set
     */
    public void setCostComparable(BigDecimal costComparable) {
        this.costComparable = costComparable;
    }

    /**
     * @return the valueBeforeDiscount
     */
    public BigDecimal getValueBeforeDiscount() {
        return valueBeforeDiscount;
    }

    /**
     * @param valueBeforeDiscount the valueBeforeDiscount to set
     */
    public void setValueBeforeDiscount(BigDecimal valueBeforeDiscount) {
        this.valueBeforeDiscount = valueBeforeDiscount;
    }

    /**
     * @return the quantityInch
     */
    public BigDecimal getQuantityInch() {
        if(quantityInch == null){
            quantityInch = BigDecimal.ZERO;
        }
        return quantityInch;
    }

    /**
     * @param quantityInch the quantityInch to set
     */
    public void setQuantityInch(BigDecimal quantityInch) {
        this.quantityInch = quantityInch;
    }

    /**
     * @return the indexSerial
     */
    public Integer getIndexSerial() {
        return indexSerial;
    }

    /**
     * @param indexSerial the indexSerial to set
     */
    public void setIndexSerial(Integer indexSerial) {
        this.indexSerial = indexSerial;
    }

    /**
     * @return the costAfterDiscount
     */
    public BigDecimal getCostAfterDiscount() {
        return costAfterDiscount;
    }

    /**
     * @param costAfterDiscount the costAfterDiscount to set
     */
    public void setCostAfterDiscount(BigDecimal costAfterDiscount) {
        this.costAfterDiscount = costAfterDiscount;
    }

    /**
     * @return the costBak
     */
    public BigDecimal getCostBak() {
        if (costBak == null) {
            costBak = BigDecimal.ZERO;
        }
        return costBak;
    }

    /**
     * @param costBak the costBak to set
     */
    public void setCostBak(BigDecimal costBak) {
        this.costBak = costBak;
    }

}
