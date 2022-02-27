package com.toby.dto;

public class ProItemProductionStagesDTO extends BaseEntityDTO {

    private InvItemDTO invItemId;
    private ProGroupDTO proGroupId;
    private ProProductionStagesDTO proProductionStagesId;
    private ProProductionStagesDTO proProductionStagesIdBak;
    
    public ProItemProductionStagesDTO() {
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getIndex() != null ? getIndex() .hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProItemProductionStagesDTO)) {
            return false;
        }
        ProItemProductionStagesDTO other = (ProItemProductionStagesDTO) object;
        if ((this.getIndex() == null && other.getIndex() != null) || (this.getIndex() != null && !this.getIndex().equals(other.getIndex()))) {
            return false;
        }
        return true;
    }

   
    @Override
    public String toString() {
        return getSerial() + "" + id;
    }

    
    public InvItemDTO getInvItemId() {
        return invItemId;
    }

    
    public void setInvItemId(InvItemDTO invItemId) {
        this.invItemId = invItemId;
    }

    
    public ProProductionStagesDTO getProProductionStagesId() {
        return proProductionStagesId;
    }

    
    public void setProProductionStagesId(ProProductionStagesDTO proProductionStagesId) {
        this.proProductionStagesId = proProductionStagesId;
    }

    /**
     * @return the proProductionStagesIdBak
     */
    public ProProductionStagesDTO getProProductionStagesIdBak() {
        return proProductionStagesIdBak;
    }

    /**
     * @param proProductionStagesIdBak the proProductionStagesIdBak to set
     */
    public void setProProductionStagesIdBak(ProProductionStagesDTO proProductionStagesIdBak) {
        this.proProductionStagesIdBak = proProductionStagesIdBak;
    }

    /**
     * @return the proGroupId
     */
    public ProGroupDTO getProGroupId() {
        return proGroupId;
    }

    /**
     * @param proGroupId the proGroupId to set
     */
    public void setProGroupId(ProGroupDTO proGroupId) {
        this.proGroupId = proGroupId;
    }


}
