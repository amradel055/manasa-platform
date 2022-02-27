/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author hq004
 */
@Entity
@Table(name = "toby_privilege")
public class TobyPrivilege extends BaseEntity {

    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @ManyToOne()
    private TobyRole roleId;

    @JoinColumn(name = "screen_id", referencedColumnName = "id")
    @ManyToOne()
    private TobyScreen screenId;

    @Column(name = "add_priv")
    private Boolean add;

    @Column(name = "view_priv")
    private Boolean view;

    @Column(name = "edit_priv")
    private Boolean edit;

    @Column(name = "delete_priv")
    private Boolean delete;

    /**
     * @return the roleId
     */
    public TobyRole getRoleId() {
        return roleId;
    }

    /**
     * @param roleId the roleId to set
     */
    public void setRoleId(TobyRole roleId) {
        this.roleId = roleId;
    }

    /**
     * @return the screenId
     */
    public TobyScreen getScreenId() {
        return screenId;
    }

    /**
     * @param screenId the screenId to set
     */
    public void setScreenId(TobyScreen screenId) {
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

}
