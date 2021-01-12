/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.report.entity;

import java.util.Collection;

/**
 *
 * @author ahmed
 */
public class ClinicIndexReportBean {

    private Integer code;
    private String name;
    private Integer short_code;
    private Integer level;
    private String active;
    private String parent;
    private Collection<ClinicIndexReportBean> clinicIndexReportBeans;

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

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the short_code
     */
    public Integer getShort_code() {
        return short_code;
    }

    /**
     * @param short_code the short_code to set
     */
    public void setShort_code(Integer short_code) {
        this.short_code = short_code;
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

    /**
     * @return the active
     */
    public String getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(String active) {
        this.active = active;
    }

    /**
     * @return the parent
     */
    public String getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(String parent) {
        this.parent = parent;
    }

    /**
     * @return the clinicIndexReportBeans
     */
    public Collection<ClinicIndexReportBean> getClinicIndexReportBeans() {
        return clinicIndexReportBeans;
    }

    /**
     * @param clinicIndexReportBeans the clinicIndexReportBeans to set
     */
    public void setClinicIndexReportBeans(Collection<ClinicIndexReportBean> clinicIndexReportBeans) {
        this.clinicIndexReportBeans = clinicIndexReportBeans;
    }
}
