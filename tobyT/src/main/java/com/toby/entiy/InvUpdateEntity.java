package com.toby.entiy;

import com.toby.entity.Branch;
import com.toby.entity.InvInventory;
import com.toby.entity.InvUpdateDetail;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InvUpdateEntity extends BaseEntity {

    private Date updateDate;
    private String updateDocument;
    private String remark;

    private Integer postFlag;
    private Branch branchId;
    private InvInventory invInventory;
    private List<InvUpdateDetail> invUpdateDetailEntityList = new ArrayList<>();

    public InvUpdateEntity() {
        setCounter(getCounter() + 1);
    }

    /**
     * @return the updateDate
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate the updateDate to set
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * @return the updateDocument
     */
    public String getUpdateDocument() {
        return updateDocument;
    }

    /**
     * @param updateDocument the updateDocument to set
     */
    public void setUpdateDocument(String updateDocument) {
        this.updateDocument = updateDocument;
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
     * @return the postFlag
     */
    public Integer getPostFlag() {
        return postFlag;
    }

    /**
     * @param postFlag the postFlag to set
     */
    public void setPostFlag(Integer postFlag) {
        this.postFlag = postFlag;
    }

    /**
     * @return the branchId
     */
    public Branch getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the invInventory
     */
    public InvInventory getInvInventory() {
        return invInventory;
    }

    /**
     * @param invInventory the invInventory to set
     */
    public void setInvInventory(InvInventory invInventory) {
        this.invInventory = invInventory;
    }

    /**
     * @return the invUpdateDetailEntityList
     */
    public List<InvUpdateDetail> getInvUpdateDetailEntityList() {
        return invUpdateDetailEntityList;
    }

    /**
     * @param invUpdateDetailEntityList the invUpdateDetailEntityList to set
     */
    public void setInvUpdateDetailEntityList(List<InvUpdateDetail> invUpdateDetailEntityList) {
        this.invUpdateDetailEntityList = invUpdateDetailEntityList;
    }

  
}
