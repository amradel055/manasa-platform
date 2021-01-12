/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

/**
 *
 * @author amr
 */
public class InvPurchaseInvoiceDetailDetailDTO extends BaseEntityDTO {

    
    private Integer invPurchaseInvoiceDetailDTO;
    private ProDetailTypeDTO proDetailTypeDTO;
    
    public InvPurchaseInvoiceDetailDetailDTO() {
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
        if (!(object instanceof InvPurchaseInvoiceDetailDetailDTO)) {
            return false;
        }
        InvPurchaseInvoiceDetailDetailDTO other = (InvPurchaseInvoiceDetailDetailDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "";
    }

    /**
     * @return the invPurchaseInvoiceDetailDTO
     */
    public Integer getInvPurchaseInvoiceDetailDTO() {
        return invPurchaseInvoiceDetailDTO;
    }

    /**
     * @param invPurchaseInvoiceDetailDTO the invPurchaseInvoiceDetailDTO to set
     */
    public void setInvPurchaseInvoiceDetailDTO(Integer invPurchaseInvoiceDetailDTO) {
        this.invPurchaseInvoiceDetailDTO = invPurchaseInvoiceDetailDTO;
    }

    /**
     * @return the proDetailTypeDTO
     */
    public ProDetailTypeDTO getProDetailTypeDTO() {
        return proDetailTypeDTO;
    }

    /**
     * @param proDetailTypeDTO the proDetailTypeDTO to set
     */
    public void setProDetailTypeDTO(ProDetailTypeDTO proDetailTypeDTO) {
        this.proDetailTypeDTO = proDetailTypeDTO;
    }


}
