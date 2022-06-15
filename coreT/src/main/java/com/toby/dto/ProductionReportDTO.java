package com.toby.dto;

import java.math.BigDecimal;

public class ProductionReportDTO extends BaseEntityDTO{

    private Integer rowNum;

    private Integer invoiceId;

    private Integer productionStage;

    private BigDecimal quantity;

    private BigDecimal price;

    private String empName;

    public ProductionReportDTO() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getProductionStage() {
        return productionStage;
    }

    public void setProductionStage(Integer productionStage) {
        this.productionStage = productionStage;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }   

}
