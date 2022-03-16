
package com.isag.dto;
import com.isag.views.InvItemInstProjectGuideView;
import java.math.BigDecimal;


public class InstPurchaseInvoiceDetailDTO extends BaseEntityDTO{

  

    private BigDecimal unitPrice;
    private BigDecimal quantity;
    private InvItemInstProjectGuideView itemId;
    private InstPurchaseInvoiceDTO instPurchaseInvoice;
    private Integer tableType;
    private BigDecimal total;

   
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public InvItemInstProjectGuideView getItemId() {
        return itemId;
    }

    public void setItemId(InvItemInstProjectGuideView itemId) {
        this.itemId = itemId;
    }

    
    /**
     * @return the instPurchaseInvoiceId
     */
    public InstPurchaseInvoiceDTO getInstPurchaseInvoice() {
        return instPurchaseInvoice;
    }

    /**
     * @param instPurchaseInvoiceId the instPurchaseInvoiceId to set
     */
    public void setInstPurchaseInvoice(InstPurchaseInvoiceDTO instPurchaseInvoice) {
        this.instPurchaseInvoice = instPurchaseInvoice;
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
        if (!(object instanceof InstPurchaseInvoiceDetailDTO)) {
            return false;
        }
        InstPurchaseInvoiceDetailDTO other = (InstPurchaseInvoiceDetailDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.InstPurchaseInvoiceDetail[ id=" + id + " ]";
    }

    /**
     * @return the total
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * @return the tableType
     */
    public Integer getTableType() {
        return tableType;
    }

    /**
     * @param tableType the tableType to set
     */
    public void setTableType(Integer tableType) {
        this.tableType = tableType;
    }

    
}
