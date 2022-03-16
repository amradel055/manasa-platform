package com.toby.dto;

import java.util.ArrayList;
import java.util.List;

public class ProProductionTransactionDetailViewDTO extends BaseEntityDTO {
     
    private Integer invItemCode;
    private String invItemName;
    private List<ProProductionTransactionDetailDetailViewDTO> detailDetailViewDTOs;


    /**
     * @return the invItemName
     */
    public String getInvItemName() {
        return invItemName;
    }

    /**
     * @param invItemName the invItemName to set
     */
    public void setInvItemName(String invItemName) {
        this.invItemName = invItemName;
    }

    /**
     * @return the detailDetailViewDTOs
     */
    public List<ProProductionTransactionDetailDetailViewDTO> getDetailDetailViewDTOs() {
        if(detailDetailViewDTOs == null){
            detailDetailViewDTOs = new ArrayList<>();
        }
        return detailDetailViewDTOs;
    }

    /**
     * @param detailDetailViewDTOs the detailDetailViewDTOs to set
     */
    public void setDetailDetailViewDTOs(List<ProProductionTransactionDetailDetailViewDTO> detailDetailViewDTOs) {
        this.detailDetailViewDTOs = detailDetailViewDTOs;
    }

    /**
     * @return the invItemCode
     */
    public Integer getInvItemCode() {
        return invItemCode;
    }

    /**
     * @param invItemCode the invItemCode to set
     */
    public void setInvItemCode(Integer invItemCode) {
        this.invItemCode = invItemCode;
    }
    
    
  
}
