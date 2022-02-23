/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report.entity;

/**
 *
 * @author hq001
 */
public class GlAccountReport {

    
    private Integer accNumber;
    private String lname;
    private Integer parentNumber;
    private String lgroup;
    private Integer llevel;
    private String hierarchyType;
    
    private String type;
    private String status;

    public Integer getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(Integer accNumber) {
        this.accNumber = accNumber;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public Integer getParentNumber() {
        return parentNumber;
    }

    public void setParentNumber(Integer parentNumber) {
        this.parentNumber = parentNumber;
    }

    public String getLgroup() {
        return lgroup;
    }

    public void setLgroup(String lgroup) {
        this.lgroup = lgroup;
    }

    public Integer getLlevel() {
        return llevel;
    }

    public void setLlevel(Integer llevel) {
        this.llevel = llevel;
    }

    /**
     * @return the hierarchyType
     */
    public String getHierarchyType() {
        return hierarchyType;
    }

    /**
     * @param hierarchyType the hierarchyType to set
     */
    public void setHierarchyType(String hierarchyType) {
        this.hierarchyType = hierarchyType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
