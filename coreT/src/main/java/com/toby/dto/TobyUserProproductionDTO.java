package com.toby.dto;

public class TobyUserProproductionDTO extends BaseEntityDTO {

    private Integer proproductionId;
    private Integer userId;

    public TobyUserProproductionDTO() {
    }

    public TobyUserProproductionDTO(Integer id) {
        this.id = id;
    }

    
    public Integer getProproductionId() {
        return proproductionId;
    }

    
    public void setProproductionId(Integer proproductionId) {
        this.proproductionId = proproductionId;
    }

    
    public Integer getUserId() {
        return userId;
    }

    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    
    @Override
    public String toString() {
        return "com.toby.omar.TobyUserProproduction[ id=" + id + " ]";
    }


}


