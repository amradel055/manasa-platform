package com.toby.dto;

import com.toby.entity.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;


public class InvTransferDetailDTO extends BaseEntityDTO {

    private BigDecimal amount;
    private BigDecimal balance;
    private BigDecimal screwing;
    private Integer postFlag;
    private String itemBarcode;
    private Integer transferFrom;
    private Integer status;
    private BigDecimal finalQuantity;
    private Collection<InvTransferDetailDTO> invTransferDetailCollection;
    private InvTransferDetailDTO selectedId;
    private InvItemDTO invItemId;
    private InvTransferDTO invTransferId;
    private SymbolDTO unitId;
    private UnitsItems unitsItem;
    private List<UnitsItems> unitsItemseList;
    private Integer unitsItemsSelected;
    public InvTransferDetailDTO() {
    }

    public InvTransferDetailDTO(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public InvItemDTO getInvItemId() {
        return invItemId;
    }

    public void setInvItemId(InvItemDTO invItemId) {
        this.invItemId = invItemId;
    }

    public InvTransferDTO getInvTransferId() {
        return invTransferId;
    }

    public void setInvTransferId(InvTransferDTO invTransferId) {
        this.invTransferId = invTransferId;
    }
    
    /**
     * @return the screwing
     */
    public BigDecimal getScrewing() {
        return screwing;
    }

    /**
     * @param screwing the screwing to set
     */
    public void setScrewing(BigDecimal screwing) {
        this.screwing = screwing;
    }

    /**
     * @return the postFlag
     */
    public Integer getPostFlag() {
        return postFlag;
    }

    /**
     * @param postFlag the postFlag to set
     */
    public void setPostFlag(Integer postFlag) {
        this.postFlag = postFlag;
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


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvTransferDetailDTO)) {
            return false;
        }
        InvTransferDetailDTO other = (InvTransferDetailDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.InvTransferDetail[ id=" + id + " ]";
    }

   
    /**
     * @return the transferFrom
     */
    public Integer getTransferFrom() {
        return transferFrom;
    }

    /**
     * @param transferFrom the transferFrom to set
     */
    public void setTransferFrom(Integer transferFrom) {
        this.transferFrom = transferFrom;
    }

    /**
     * @return the invTransferDetailCollection
     */
    public Collection<InvTransferDetailDTO> getInvTransferDetailCollection() {
        return invTransferDetailCollection;
    }

    /**
     * @param invTransferDetailCollection the invTransferDetailCollection to set
     */
    public void setInvTransferDetailCollection(Collection<InvTransferDetailDTO> invTransferDetailCollection) {
        this.invTransferDetailCollection = invTransferDetailCollection;
    }

    /**
     * @return the unitId
     */
    public SymbolDTO getUnitId() {
        return unitId;
    }

    /**
     * @param unitId the unitId to set
     */
    public void setUnitId(SymbolDTO unitId) {
        this.unitId = unitId;
    }

    /**
     * @return the selectedId
     */
    public InvTransferDetailDTO getSelectedId() {
        return selectedId;
    }

    /**
     * @param selectedId the selectedId to set
     */
    public void setSelectedId(InvTransferDetailDTO selectedId) {
        this.selectedId = selectedId;
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
     * @return the finalQuantity
     */
    public BigDecimal getFinalQuantity() {
        return finalQuantity;
    }

    /**
     * @param finalQuantity the finalQuantity to set
     */
    public void setFinalQuantity(BigDecimal finalQuantity) {
        this.finalQuantity = finalQuantity;
    }

    /**
     * @return the unitsItemseList
     */
    public List<UnitsItems> getUnitsItemseList() {
        return unitsItemseList;
    }

    /**
     * @param unitsItemseList the unitsItemseList to set
     */
    public void setUnitsItemseList(List<UnitsItems> unitsItemseList) {
        this.unitsItemseList = unitsItemseList;
    }

    /**
     * @return the unitsItemsSelected
     */
    public Integer getUnitsItemsSelected() {
        return unitsItemsSelected;
    }

    /**
     * @param unitsItemsSelected the unitsItemsSelected to set
     */
    public void setUnitsItemsSelected(Integer unitsItemsSelected) {
        this.unitsItemsSelected = unitsItemsSelected;
    }

    /**
     * @return the unitsItem
     */
    public UnitsItems getUnitsItem() {
        return unitsItem;
    }

    /**
     * @param unitsItem the unitsItem to set
     */
    public void setUnitsItem(UnitsItems unitsItem) {
        this.unitsItem = unitsItem;
    }

}
