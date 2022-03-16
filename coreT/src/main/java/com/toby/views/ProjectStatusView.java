/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "project_status_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProjectStatusView.findAll", query = "SELECT c FROM ProjectStatusView c"),
    @NamedQuery(name = "ProjectStatusView.findByBranchId", query = "SELECT c FROM ProjectStatusView c WHERE c.branchId = :branchId"),
    @NamedQuery(name = "ProjectStatusView.findByprojectId", query = "SELECT c FROM ProjectStatusView c WHERE c.projectId = :projectId"),
    @NamedQuery(name = "ProjectStatusView.findByprojectName", query = "SELECT c FROM ProjectStatusView c WHERE c.projectName = :projectName"),
    @NamedQuery(name = "ProjectStatusView.findByprojectCode", query = "SELECT c FROM ProjectStatusView c WHERE c.projectCode = :projectCode"),
    @NamedQuery(name = "ProjectStatusView.findBystatus", query = "SELECT c FROM ProjectStatusView c WHERE c.status = :status")})
public class ProjectStatusView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "rowNum")
    @Id
    private Integer rowNum;
    @Column(name = "project_id")
    private Integer projectId;
    @Column(name = "branch_id")
    private Integer branchId;
    
    @Column(name = "status")
    private Integer status;
    @Column(name = "parent")
    private Integer parent;
    @Size(max = 45)
    @Column(name = "project_name")
    private String projectName;
    @Size(max = 45)
    @Column(name = "project_code")
    private String projectCode;
    @Column(name = "level")
    private Integer level;
    
   

    public ProjectStatusView() {
    }

    /**
     * @return the rowNum
     */
    public Integer getRowNum() {
        return rowNum;
    }

    /**
     * @param rowNum the rowNum to set
     */
    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    /**
     * @return the projectId
     */
    public Integer getProjectId() {
        return projectId;
    }

    /**
     * @param projectId the projectId to set
     */
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
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
     * @return the projectName
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * @param projectName the projectName to set
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * @return the projectCode
     */
    public String getProjectCode() {
        return projectCode;
    }

    /**
     * @param projectCode the projectCode to set
     */
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    /**
     * @return the parent
     */
    public Integer getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Integer parent) {
        this.parent = parent;
    }

    /**
     * @return the level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

   
}
