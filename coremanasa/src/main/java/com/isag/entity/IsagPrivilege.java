/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author hq004
 */
@Entity
@Table(name = "isag_privilege")
public class IsagPrivilege extends BaseEntity {

    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @ManyToOne()
    private IsagRole roleId;

    @JoinColumn(name = "screen_id", referencedColumnName = "id")
    @ManyToOne()
    private IsagScreen screenId;

    @Column(name = "add_priv")
    private Boolean add;

    @Column(name = "view_priv")
    private Boolean view;

    @Column(name = "edit_priv")
    private Boolean edit;

    @Column(name = "delete_priv")
    private Boolean delete;
    
    @Transient
    private static Integer serial ;
    /**
     * @return the roleId
     */
    public IsagRole getRoleId() {
        return roleId;
    }

    /**
     * @param roleId the roleId to set
     */
    public void setRoleId(IsagRole roleId) {
        this.roleId = roleId;
    }

    /**
     * @return the screenId
     */
    public IsagScreen getScreenId() {
        return screenId;
    }

    /**
     * @param screenId the screenId to set
     */
    public void setScreenId(IsagScreen screenId) {
        this.screenId = screenId;
    }

    /**
     * @return the add
     */
    public Boolean getAdd() {
        return add;
    }

    /**
     * @param add the add to set
     */
    public void setAdd(Boolean add) {
        this.add = add;
    }

    /**
     * @return the view
     */
    public Boolean getView() {
        return view;
    }

    /**
     * @param view the view to set
     */
    public void setView(Boolean view) {
        this.view = view;
    }

    /**
     * @return the edit
     */
    public Boolean getEdit() {
        return edit;
    }

    /**
     * @param edit the edit to set
     */
    public void setEdit(Boolean edit) {
        this.edit = edit;
    }

    /**
     * @return the delete
     */
    public Boolean getDelete() {
        return delete;
    }

    /**
     * @param delete the delete to set
     */
    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

      @Override
    public String toString() {
        String module_name = (getScreenId() != null ? getScreenId().getModuleAr() : " ");
        return  module_name+ " " + getSerial();
    }
    
      @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * @return the serial
     */
    public Integer getSerial() {
        if(serial == null){
        serial = 0;}
//        }else {
//        serial = serial + 1;
//        }
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(Integer serial) {
        this.serial = serial;
    }
}
