
package com.toby.dto;


public class ProProductMovementDetailDTO extends BaseEntityDTO{
    
   
    private Integer serial;
    private Integer invPurchesInvoiceId;
    private Integer invPurchesInvoiceIdBackUp;
    private Integer invPurchesInvoiceSerial;
    private Integer proProductionDeliveryId;

    public ProProductMovementDetailDTO() {
    }

    public ProProductMovementDetailDTO(Integer id) {
        this.id = id;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    
    public Integer getInvPurchesInvoiceId() {
        return invPurchesInvoiceId;
    }

    
    public void setInvPurchesInvoiceId(Integer invPurchesInvoiceId) {
        this.invPurchesInvoiceId = invPurchesInvoiceId;
    }

    
    public Integer getProProductionDeliveryId() {
        return proProductionDeliveryId;
    }

    
    public void setProProductionDeliveryId(Integer proProductionDeliveryId) {
        this.proProductionDeliveryId = proProductionDeliveryId;
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
        if (!(object instanceof ProProductMovementDetailDTO)) {
            return false;
        }
        ProProductMovementDetailDTO other = (ProProductMovementDetailDTO) object;
        if ((this.getIndex() == null && other.getIndex() != null) || (this.getIndex() != null && !this.getIndex().equals(other.getIndex()))) {
            return false;
        }
        return true;
    }

    
    public Integer getInvPurchesInvoiceSerial() {
        return invPurchesInvoiceSerial;
    }

    
    public void setInvPurchesInvoiceSerial(Integer invPurchesInvoiceSerial) {
        this.invPurchesInvoiceSerial = invPurchesInvoiceSerial;
    }

    
    public Integer getInvPurchesInvoiceIdBackUp() {
        return invPurchesInvoiceIdBackUp;
    }

    
    public void setInvPurchesInvoiceIdBackUp(Integer invPurchesInvoiceIdBackUp) {
        this.invPurchesInvoiceIdBackUp = invPurchesInvoiceIdBackUp;
    }

    

    
    
}
