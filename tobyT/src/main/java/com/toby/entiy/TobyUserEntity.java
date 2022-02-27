package com.toby.entiy;

import com.toby.entity.TobyRole;
import com.toby.entity.Symbol;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author WIN7
 */
public class TobyUserEntity extends NewBaseEntity {

    private String userCode;
    private String password;
    private String name;
    private Symbol lang;
    private TobyRole roleId;
    private Integer branchId;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Symbol getLang() {
        return lang;
    }

    public void setLang(Symbol lang) {
        this.lang = lang;
    }

    public TobyRole getRoleId() {
        return roleId;
    }

    public void setRoleId(TobyRole roleId) {
        this.roleId = roleId;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }
}
