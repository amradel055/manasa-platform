/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entiy;

import com.toby.entity.TobyUser;
import com.toby.entity.InvGroup;
import java.util.Date;

public class InvGroupEntity {

    Integer id;
    String name;
    InvGroup invGroupParentId;
    String parentName;
    Integer level;
    private TobyUser createdBy;
    private Date creationDate;
    private Integer code;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InvGroup getInvGroupParentId() {
        return invGroupParentId;
    }

    public void setInvGroupParentId(InvGroup invGroupParentId) {
        this.invGroupParentId = invGroupParentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * @return the createdBy
     */
    public TobyUser getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(TobyUser createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return the code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(Integer code) {
        this.code = code;
    }

}
