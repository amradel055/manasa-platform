/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

public class HrSalaryEffectDTO extends BaseEntityDTO {

    private String name;
    private Integer code;
    private SymbolDTO type;
    private Integer cofficientPerDay;
    private Integer cofficientPerHour;
    private GlAccountDTO glAccountId;
    private Integer impactIndicator;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public SymbolDTO getType() {
        return type;
    }

    public void setType(SymbolDTO type) {
        this.type = type;
    }

    public Integer getCofficientPerDay() {
        return cofficientPerDay;
    }

    public void setCofficientPerDay(Integer cofficientPerDay) {
        this.cofficientPerDay = cofficientPerDay;
    }

    public Integer getCofficientPerHour() {
        return cofficientPerHour;
    }

    public void setCofficientPerHour(Integer cofficientPerHour) {
        this.cofficientPerHour = cofficientPerHour;
    }

    public GlAccountDTO getGlAccountId() {
        return glAccountId;
    }

    public void setGlAccountId(GlAccountDTO glAccountId) {
        this.glAccountId = glAccountId;
    }

    public Integer getImpactIndicator() {
        return impactIndicator;
    }

    public void setImpactIndicator(Integer impactIndicator) {
        this.impactIndicator = impactIndicator;
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
        if (!(object instanceof HrSalaryEffectDTO)) {
            return false;
        }
        HrSalaryEffectDTO other = (HrSalaryEffectDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name+ " "+ code;
    }

}
