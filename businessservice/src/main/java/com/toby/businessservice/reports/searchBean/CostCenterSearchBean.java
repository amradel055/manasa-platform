package com.toby.businessservice.reports.searchBean;

import com.toby.entity.CostCenter;

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
