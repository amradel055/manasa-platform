package com.toby.views;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "production_report")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductionReport.findAll", query = "SELECT p FROM ProductionReport p"),
    @NamedQuery(name = "ProductionReport.findByRowNum", query = "SELECT p FROM ProductionReport p WHERE p.rowNum = :rowNum"),
    @NamedQuery(name = "ProductionReport.findByInvoiceId", query = "SELECT p FROM ProductionReport p WHERE p.invoiceId = :invoiceId"),
    @NamedQuery(name = "ProductionReport.findByProductionStage", query = "SELECT p FROM ProductionReport p WHERE p.productionStage = :productionStage"),
    @NamedQuery(name = "ProductionReport.findByDate", query = "SELECT p FROM ProductionReport p WHERE p.date = :date"),
    @NamedQuery(name = "ProductionReport.findByQuantity", query = "SELECT p FROM ProductionReport p WHERE p.quantity = :quantity"),
    @NamedQuery(name = "ProductionReport.findByPrice", query = "SELECT p FROM ProductionReport p WHERE p.price = :price"),
    @NamedQuery(name = "ProductionReport.findByEmpName", query = "SELECT p FROM ProductionReport p WHERE p.empName = :empName"),
    @NamedQuery(name = "ProductionReport.findByTotal", query = "SELECT p FROM ProductionReport p WHERE p.total = :empName")})
public class ProductionReport implements Serializable {

   
    private static final long serialVersionUID = 1L;
    @Column(name = "rowNum")
    @Id
    private Integer rowNum;
    @Column(name = "invoiceId")
    private Integer invoiceId;
    @Column(name = "invoice_serial")
    private Integer invoiceSerial;
    @Column(name = "productionStage")
    private Integer productionStage;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantity")
    private BigDecimal quantity;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "total")
    private BigDecimal total;
    @Size(max = 45)
    @Column(name = "empName")
    private String empName;
    @Column(name = "stage_name")
    private String stageName;

    public ProductionReport() {
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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
 
    public Integer getInvoiceSerial() {
        return invoiceSerial;
    }

    
    public void setInvoiceSerial(Integer invoiceSerial) {
        this.invoiceSerial = invoiceSerial;
    }

   
    public String getStageName() {
        return stageName;
    }

    
    public void setStageName(String stageName) {
        this.stageName = stageName;
    }
    }
