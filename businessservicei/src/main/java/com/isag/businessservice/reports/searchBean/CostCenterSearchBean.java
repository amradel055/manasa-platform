package com.isag.businessservice.reports.searchBean;

import com.isag.entity.CostCenter;

public class CostCenterSearchBean {

    private CostCenter costCenterIdFrom;
    private CostCenter costCenterIdTo;
    private Integer branchId;

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public CostCenter getCostCenterIdFrom() {
        return costCenterIdFrom;
    }

    public void setCostCenterIdFrom(CostCenter costCenterIdFrom) {
        this.costCenterIdFrom = costCenterIdFrom;
    }

    public CostCenter getCostCenterIdTo() {
        return costCenterIdTo;
    }

    public void setCostCenterIdTo(CostCenter costCenterIdTo) {
        this.costCenterIdTo = costCenterIdTo;
    }

}
