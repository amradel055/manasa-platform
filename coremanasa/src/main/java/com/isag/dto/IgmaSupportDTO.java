/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.util.Date;
import java.util.List;

/**
 *
 * @author khale
 */
public class IgmaSupportDTO extends BaseEntityDTO {

    private Date date;
    private Integer module;
    private IgmaScreenDTO screen; 
    private String title;
    private String description;
    private String img;
    private Integer status;
    private String statusName;
    private Integer priorety;
    private Integer tecincal;
    private String reply;
    private String remark;
    private Integer serial;
    
    private Date ticketSolveDate;
    private Date transferDate;
    
    private String branchName;
    private String companyName;
    private String employeeName;
    
    private List<IgmaUserDTO> isagUserDTOSelected ;
    private List<Integer> statusSelected;
    private List<IgmaScreenDTO> screenSelected;
    private List<APIBranchDTO> branchSelected;

    /**
     * @return the date
     */
    public Date getDate() {
        if(date == null){
            date = new Date();
        }
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the module
     */
    public Integer getModule() {
        return module;
    }

    /**
     * @param module the module to set
     */
    public void setModule(Integer module) {
        this.module = module;
    }

    /**
     * @return the screen
     */
    public IgmaScreenDTO getScreen() {
        return screen;
    }

    /**
     * @param screen the screen to set
     */
    public void setScreen(IgmaScreenDTO screen) {
        this.screen = screen;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the img
     */
    public String getImg() {
        return img;
    }

    /**
     * @param img the img to set
     */
    public void setImg(String img) {
        this.img = img;
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
     * @return the priorety
     */
    public Integer getPriorety() {
        return priorety;
    }

    /**
     * @param priorety the priorety to set
     */
    public void setPriorety(Integer priorety) {
        this.priorety = priorety;
    }

    /**
     * @return the tecincal
     */
    public Integer getTecincal() {
        return tecincal;
    }

    /**
     * @param tecincal the tecincal to set
     */
    public void setTecincal(Integer tecincal) {
        this.tecincal = tecincal;
    }

    /**
     * @return the reply
     */
    public String getReply() {
        return reply;
    }

    /**
     * @param reply the reply to set
     */
    public void setReply(String reply) {
        this.reply = reply;
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
     * @return the ticketSolveDate
     */
    public Date getTicketSolveDate() {
        return ticketSolveDate;
    }

    /**
     * @param ticketSolveDate the ticketSolveDate to set
     */
    public void setTicketSolveDate(Date ticketSolveDate) {
        this.ticketSolveDate = ticketSolveDate;
    }

    /**
     * @return the transferDate
     */
    public Date getTransferDate() {
        return transferDate;
    }

    /**
     * @param transferDate the transferDate to set
     */
    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    /**
     * @return the branchName
     */
    public String getBranchName() {
        return branchName;
    }

    /**
     * @param branchName the branchName to set
     */
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return the employeeName
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * @param employeeName the employeeName to set
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    /**
     * @return the screenSelected
     */
    public List<IgmaScreenDTO> getScreenSelected() {
        return screenSelected;
    }

    /**
     * @param screenSelected the screenSelected to set
     */
    public void setScreenSelected(List<IgmaScreenDTO> screenSelected) {
        this.screenSelected = screenSelected;
    }

    /**
     * @return the branchSelected
     */
    public List<APIBranchDTO> getBranchSelected() {
        return branchSelected;
    }

    /**
     * @param branchSelected the branchSelected to set
     */
    public void setBranchSelected(List<APIBranchDTO> branchSelected) {
        this.branchSelected = branchSelected;
    }

    /**
     * @return the isagUserDTOSelected
     */
    public List<IgmaUserDTO> getIsagUserDTOSelected() {
        return isagUserDTOSelected;
    }

    /**
     * @param isagUserDTOSelected the isagUserDTOSelected to set
     */
    public void setIsagUserDTOSelected(List<IgmaUserDTO> isagUserDTOSelected) {
        this.isagUserDTOSelected = isagUserDTOSelected;
    }

    /**
     * @return the statusSelected
     */
    public List<Integer> getStatusSelected() {
        return statusSelected;
    }

    /**
     * @param statusSelected the statusSelected to set
     */
    public void setStatusSelected(List<Integer> statusSelected) {
        this.statusSelected = statusSelected;
    }

    /**
     * @return the statusName
     */
    public String getStatusName() {
        return statusName;
    }

    /**
     * @param statusName the statusName to set
     */
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

}
