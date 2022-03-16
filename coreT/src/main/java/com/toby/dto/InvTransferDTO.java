package com.toby.dto;

import com.toby.entity.*;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Collection;
import java.util.Date;
import java.util.List;


public class InvTransferDTO extends BaseEntityDTO {

    private int serialNo;
    private Date transferDate;
    private String remarks;
    private Integer postFlag;
    private Date date;
    private InvInventoryDTO invFrom;
    private InvInventoryDTO invTo;
    private Collection<InvTransferDetail> invTransferDetailCollection;
    private Integer transferType;
    private Integer status;
    private Collection<InvTransferDTO> invTransferCollection;
    private InvTransferDTO parent;
    private List<InvTransferDetailDTO> invTransferDetailDTOList;
    private BigDecimal total;
    private List<Integer> SerialNoList;

    public InvTransferDTO() {
    }

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @XmlTransient
    public Collection<InvTransferDetail> getInvTransferDetailCollection() {
        return invTransferDetailCollection;
    }

    public void setInvTransferDetailCollection(Collection<InvTransferDetail> invTransferDetailCollection) {
        this.invTransferDetailCollection = invTransferDetailCollection;
    }

    

    public InvInventoryDTO getInvFrom() {
        return invFrom;
    }

    public void setInvFrom(InvInventoryDTO invFrom) {
        this.invFrom = invFrom;
    }

    public InvInventoryDTO getInvTo() {
        return invTo;
    }

    public void setInvTo(InvInventoryDTO invTo) {
        this.invTo = invTo;
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

  
    @XmlTransient
    public Collection<InvTransferDTO> getInvTransferCollection() {
        return invTransferCollection;
    }

    public void setInvTransferCollection(Collection<InvTransferDTO> invTransferCollection) {
        this.invTransferCollection = invTransferCollection;
    }

    public InvTransferDTO getParent() {
        return parent;
    }

    public void setParent(InvTransferDTO parent) {
        this.parent = parent;
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
        if (!(object instanceof InvTransferDTO)) {
            return false;
        }
        InvTransferDTO other = (InvTransferDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ""+invTo+ invFrom + serialNo  ;
    }

    /**
     * @return the transferType
     */
    public Integer getTransferType() {
        return transferType;
    }

    /**
     * @param transferType the transferType to set
     */
    public void setTransferType(Integer transferType) {
        this.transferType = transferType;
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
     * @return the invTransferDetailDTOList
     */
    public List<InvTransferDetailDTO> getInvTransferDetailDTOList() {
        return invTransferDetailDTOList;
    }

    /**
     * @param invTransferDetailDTOList the invTransferDetailDTOList to set
     */
    public void setInvTransferDetailDTOList(List<InvTransferDetailDTO> invTransferDetailDTOList) {
        this.invTransferDetailDTOList = invTransferDetailDTOList;
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
     * @return the SerialNoList
     */
    public List<Integer> getSerialNoList() {
        return SerialNoList;
    }

    /**
     * @param SerialNoList the SerialNoList to set
     */
    public void setSerialNoList(List<Integer> SerialNoList) {
        this.SerialNoList = SerialNoList;
    }
    
}
