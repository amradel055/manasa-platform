package com.toby.views;

import com.toby.entity.BaseEntity;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "rsc_tender_contract_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RscTenderContractView.findAll", query = "SELECT r FROM RscTenderContractView r")
    , @NamedQuery(name = "RscTenderContractView.findById", query = "SELECT r FROM RscTenderContractView r WHERE r.id = :id")
    , @NamedQuery(name = "RscTenderContractView.findByDocNumber", query = "SELECT r FROM RscTenderContractView r WHERE r.docNumber = :docNumber")
    , @NamedQuery(name = "RscTenderContractView.findByDate", query = "SELECT r FROM RscTenderContractView r WHERE r.date = :date")
    , @NamedQuery(name = "RscTenderContractView.findByStatus", query = "SELECT r FROM RscTenderContractView r WHERE r.status = :status")
    , @NamedQuery(name = "RscTenderContractView.findByCustomerId", query = "SELECT r FROM RscTenderContractView r WHERE r.customerId = :customerId")
    , @NamedQuery(name = "RscTenderContractView.findByProjectManager", query = "SELECT r FROM RscTenderContractView r WHERE r.projectManager = :projectManager")
    , @NamedQuery(name = "RscTenderContractView.findByGeneralConsultant", query = "SELECT r FROM RscTenderContractView r WHERE r.generalConsultant = :generalConsultant")
    , @NamedQuery(name = "RscTenderContractView.findByProfissionalBussinessConsultant", query = "SELECT r FROM RscTenderContractView r WHERE r.profissionalBussinessConsultant = :profissionalBussinessConsultant")
    , @NamedQuery(name = "RscTenderContractView.findByRemark", query = "SELECT r FROM RscTenderContractView r WHERE r.remark = :remark")
    , @NamedQuery(name = "RscTenderContractView.findByBranchId", query = "SELECT r FROM RscTenderContractView r WHERE r.branchId = :branchId")
    , @NamedQuery(name = "RscTenderContractView.findByCreatedBy", query = "SELECT r FROM RscTenderContractView r WHERE r.createdBy = :createdBy")
    , @NamedQuery(name = "RscTenderContractView.findByCreationDate", query = "SELECT r FROM RscTenderContractView r WHERE r.creationDate = :creationDate")
    , @NamedQuery(name = "RscTenderContractView.findByModifiedBy", query = "SELECT r FROM RscTenderContractView r WHERE r.modifiedBy = :modifiedBy")
    , @NamedQuery(name = "RscTenderContractView.findByModificationDate", query = "SELECT r FROM RscTenderContractView r WHERE r.modificationDate = :modificationDate")
    , @NamedQuery(name = "RscTenderContractView.findByCompanyId", query = "SELECT r FROM RscTenderContractView r WHERE r.companyId = :companyId")
    , @NamedQuery(name = "RscTenderContractView.findBySerial", query = "SELECT r FROM RscTenderContractView r WHERE r.serial = :serial")})
public class RscTenderContractView extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Size(max = 45)
    @Column(name = "doc_number")
    private String docNumber;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "status")
    private Integer status;
    @Column(name = "customer_id")
    private Integer customerId;
    @Column(name = "project_manager")
    private Integer projectManager;
    @Column(name = "general_consultant")
    private Integer generalConsultant;
    @Column(name = "profissional_bussiness_consultant")
    private Integer profissionalBussinessConsultant;
    @Size(max = 45)
    @Column(name = "remark")
    private String remark;
    @Column(name = "branch_id")
    private Integer branchId;

    @Column(name = "serial")
    private Integer serial;
    @Column(name = "customer_name")
    private String customerName;
    @Size(max = 45)
    @Column(name = "customer_code")
    private String customerCode;
    @Size(max = 255)
    @Column(name = "project_manager_name")
    private String projectManagerName;
    @Size(max = 255)
    @Column(name = "general_consultant_name")
    private String generalConsultantName;
    @Size(max = 255)
    @Column(name = "profissional_bussiness_consultant_name")
    private String profissionalBussinessConsultantName;

    public RscTenderContractView() {
    }

    /**
     * @return the docNumber
     */
    public String getDocNumber() {
        return docNumber;
    }

    /**
     * @param docNumber the docNumber to set
     */
    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
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
     * @return the customerId
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the projectManager
     */
    public Integer getProjectManager() {
        return projectManager;
    }

    /**
     * @param projectManager the projectManager to set
     */
    public void setProjectManager(Integer projectManager) {
        this.projectManager = projectManager;
    }

    /**
     * @return the generalConsultant
     */
    public Integer getGeneralConsultant() {
        return generalConsultant;
    }

    /**
     * @param generalConsultant the generalConsultant to set
     */
    public void setGeneralConsultant(Integer generalConsultant) {
        this.generalConsultant = generalConsultant;
    }

    /**
     * @return the profissionalBussinessConsultant
     */
    public Integer getProfissionalBussinessConsultant() {
        return profissionalBussinessConsultant;
    }

    /**
     * @param profissionalBussinessConsultant the
     * profissionalBussinessConsultant to set
     */
    public void setProfissionalBussinessConsultant(Integer profissionalBussinessConsultant) {
        this.profissionalBussinessConsultant = profissionalBussinessConsultant;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the branchId
     */
    public Integer getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the serial
     */
    public Integer getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return the customerCode
     */
    public String getCustomerCode() {
        return customerCode;
    }

    /**
     * @param customerCode the customerCode to set
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    /**
     * @return the projectManagerName
     */
    public String getProjectManagerName() {
        return projectManagerName;
    }

    /**
     * @param projectManagerName the projectManagerName to set
     */
    public void setProjectManagerName(String projectManagerName) {
        this.projectManagerName = projectManagerName;
    }

    /**
     * @return the generalConsultantName
     */
    public String getGeneralConsultantName() {
        return generalConsultantName;
    }

    /**
     * @param generalConsultantName the generalConsultantName to set
     */
    public void setGeneralConsultantName(String generalConsultantName) {
        this.generalConsultantName = generalConsultantName;
    }

    /**
     * @return the profissionalBussinessConsultantName
     */
    public String getProfissionalBussinessConsultantName() {
        return profissionalBussinessConsultantName;
    }

    /**
     * @param profissionalBussinessConsultantName the profissionalBussinessConsultantName to set
     */
    public void setProfissionalBussinessConsultantName(String profissionalBussinessConsultantName) {
        this.profissionalBussinessConsultantName = profissionalBussinessConsultantName;
    }

}
