package com.toby.dto;

import java.util.ArrayList;
import java.util.List;

public class ProProductionProductNumberDTO extends BaseEntityDTO {
     
    private Integer productNumber;
    private String itemName;
    private List<ProProductionTransactionDetailViewDTO> detailDetailViewDTOList;

    /**
     * @return the productNumber
     */
    public Integer getProductNumber() {
        return productNumber;
    }

    /**
     * @param productNumber the productNumber to set
     */
    public void setProductNumber(Integer productNumber) {
        this.productNumber = productNumber;
    }


    /**
     * @return the detailDetailViewDTOList
     */
    public List<ProProductionTransactionDetailViewDTO> getDetailDetailViewDTOList() {
        if(detailDetailViewDTOList == null){
            detailDetailViewDTOList = new ArrayList<>();
        }
        return detailDetailViewDTOList;
    }

    /**
     * @param detailDetailViewDTOList the detailDetailViewDTOList to set
     */
    public void setDetailDetailViewDTOList(List<ProProductionTransactionDetailViewDTO> detailDetailViewDTOList) {
        this.detailDetailViewDTOList = detailDetailViewDTOList;
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
    
    
  
}
