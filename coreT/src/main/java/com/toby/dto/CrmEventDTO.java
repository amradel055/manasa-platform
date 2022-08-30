/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author User
 */
public class CrmEventDTO extends BaseEntityDTO {

    private String subject;
    private Date startDate;
    private Date startTime;
    private Date endDate;
    private Date endTime;
    private SymbolDTO activityType;
    private SymbolDTO status;
    private EmployeeDTO assignedTo;
    private Integer type;
    private Integer serial;
    private Boolean sendNotification;
    private Integer notificationTimeType;
    private BigDecimal notificationNumber;
    private String location;
    private Integer priority;
    private Boolean visibility;
    private Integer sendEmail;
    private Boolean repeat1;
    private Integer repeatNumber;
    private Integer repeattimeType;
    private Long repeatPeriod;
    private String discription;
    private String remark;
    private InvOrganizationSiteDTO customerId;
    private SymbolDTO SymbolPriority;
    private Boolean nowDate = Boolean.FALSE;
    private Integer numberOfAllEvent;
    private Integer numberOfLateEvent;
    private Integer numberOfNewEvent;
    private Integer numberOfNowEvent;
    private Integer numberOfEvent;
    private Integer numberOfToDo;
    private Integer numberOfEventAssigned;
    private Integer numberOfToDoAssigned;
    private Boolean activeAssignedTo = Boolean.FALSE;
    private Boolean eventType = Boolean.FALSE;
    private SymbolDTO leadStatus;
    private Boolean leadStatusActive;
    private String notificationTitle;
    private String notificationMessage;
    private Date notificationDate;
    private Boolean closeNotification;
    private Boolean notificationOldTime;
    private Boolean closeEvent;
    private Boolean closeLeadStatus;
    private Integer gallaryId;
    private SymbolDTO crmType;
    private Integer invoiceId;
    private SymbolDTO periority;
    private InvInventoryDTO gallarySelected;

    public CrmEventDTO() {
    }

    public CrmEventDTO(Integer id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public SymbolDTO getActivityType() {
        return activityType;
    }

    public void setActivityType(SymbolDTO activityType) {
        this.activityType = activityType;
    }

    public SymbolDTO getStatus() {
        return status;
    }

    public void setStatus(SymbolDTO status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CrmEventDTO)) {
            return false;
        }
        CrmEventDTO other = (CrmEventDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return subject;
    }

    /**
     * @return the assignedTo
     */
    public EmployeeDTO getAssignedTo() {
        return assignedTo;
    }

    /**
     * @param assignedTo the assignedTo to set
     */
    public void setAssignedTo(EmployeeDTO assignedTo) {
        this.assignedTo = assignedTo;
    }

    /**
     * @return the type
     */
    public Integer getType() {
        if (type == null) {
            type = 0;
        }
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
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
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the priority
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
     * @return the sendEmail
     */
    public Integer getSendEmail() {
        return sendEmail;
    }

    /**
     * @param sendEmail the sendEmail to set
     */
    public void setSendEmail(Integer sendEmail) {
        this.sendEmail = sendEmail;
    }

    /**
     * @return the repeatNumber
     */
    public Integer getRepeatNumber() {
        return repeatNumber;
    }

    /**
     * @param repeatNumber the repeatNumber to set
     */
    public void setRepeatNumber(Integer repeatNumber) {
        this.repeatNumber = repeatNumber;
    }

    /**
     * @return the repeatPeriod
     */
    public Long getRepeatPeriod() {
        return repeatPeriod;
    }

    /**
     * @param repeatPeriod the repeatPeriod to set
     */
    public void setRepeatPeriod(Long repeatPeriod) {
        this.repeatPeriod = repeatPeriod;
    }

    /**
     * @return the discription
     */
    public String getDiscription() {
        return discription;
    }

    /**
     * @param discription the discription to set
     */
    public void setDiscription(String discription) {
        this.discription = discription;
    }

    /**
     * @return the sendNotification
     */
    public Boolean getSendNotification() {
        return sendNotification;
    }

    /**
     * @param sendNotification the sendNotification to set
     */
    public void setSendNotification(Boolean sendNotification) {
        this.sendNotification = sendNotification;
    }

    /**
     * @return the notificationTimeType
     */
    public Integer getNotificationTimeType() {
        return notificationTimeType;
    }

    /**
     * @param notificationTimeType the notificationTimeType to set
     */
    public void setNotificationTimeType(Integer notificationTimeType) {
        this.notificationTimeType = notificationTimeType;
    }

    /**
     * @return the notificationNumber
     */
    public BigDecimal getNotificationNumber() {
        return notificationNumber;
    }

    /**
     * @param notificationNumber the notificationNumber to set
     */
    public void setNotificationNumber(BigDecimal notificationNumber) {
        this.notificationNumber = notificationNumber;
    }

    /**
     * @return the repeattimeType
     */
    public Integer getRepeattimeType() {
        return repeattimeType;
    }

    /**
     * @param repeattimeType the repeattimeType to set
     */
    public void setRepeattimeType(Integer repeattimeType) {
        this.repeattimeType = repeattimeType;
    }

    /**
     * @return the visibility
     */
    public Boolean getVisibility() {
        return visibility;
    }

    /**
     * @param visibility the visibility to set
     */
    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

    /**
     * @return the repeat1
     */
    public Boolean getRepeat1() {
        return repeat1;
    }

    /**
     * @param repeat1 the repeat1 to set
     */
    public void setRepeat1(Boolean repeat1) {
        this.repeat1 = repeat1;
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
     * @return the customerId
     */
    public InvOrganizationSiteDTO getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(InvOrganizationSiteDTO customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the SymbolPriority
     */
    public SymbolDTO getSymbolPriority() {
        return SymbolPriority;
    }

    /**
     * @param SymbolPriority the SymbolPriority to set
     */
    public void setSymbolPriority(SymbolDTO SymbolPriority) {
        this.SymbolPriority = SymbolPriority;
    }

    /**
     * @return the nowDate
     */
    public Boolean getNowDate() {
        return nowDate;
    }

    /**
     * @param nowDate the nowDate to set
     */
    public void setNowDate(Boolean nowDate) {
        this.nowDate = nowDate;
    }

    /**
     * @return the numberOfEvent
     */
    public Integer getNumberOfEvent() {
        return numberOfEvent;
    }

    /**
     * @param numberOfEvent the numberOfEvent to set
     */
    public void setNumberOfEvent(Integer numberOfEvent) {
        this.numberOfEvent = numberOfEvent;
    }

    /**
     * @return the numberOfToDo
     */
    public Integer getNumberOfToDo() {
        return numberOfToDo;
    }

    /**
     * @param numberOfToDo the numberOfToDo to set
     */
    public void setNumberOfToDo(Integer numberOfToDo) {
        this.numberOfToDo = numberOfToDo;
    }

    /**
     * @return the activeAssignedTo
     */
    public Boolean getActiveAssignedTo() {
        return activeAssignedTo;
    }

    /**
     * @param activeAssignedTo the activeAssignedTo to set
     */
    public void setActiveAssignedTo(Boolean activeAssignedTo) {
        this.activeAssignedTo = activeAssignedTo;
    }

    /**
     * @return the numberOfEventAssigned
     */
    public Integer getNumberOfEventAssigned() {
        return numberOfEventAssigned;
    }

    /**
     * @param numberOfEventAssigned the numberOfEventAssigned to set
     */
    public void setNumberOfEventAssigned(Integer numberOfEventAssigned) {
        this.numberOfEventAssigned = numberOfEventAssigned;
    }

    /**
     * @return the numberOfToDoAssigned
     */
    public Integer getNumberOfToDoAssigned() {
        return numberOfToDoAssigned;
    }

    /**
     * @param numberOfToDoAssigned the numberOfToDoAssigned to set
     */
    public void setNumberOfToDoAssigned(Integer numberOfToDoAssigned) {
        this.numberOfToDoAssigned = numberOfToDoAssigned;
    }

    /**
     * @return the eventType
     */
    public Boolean getEventType() {
        return eventType;
    }

    /**
     * @param eventType the eventType to set
     */
    public void setEventType(Boolean eventType) {
        this.eventType = eventType;
    }

    /**
     * @return the numberOfAllEvent
     */
    public Integer getNumberOfAllEvent() {
        return numberOfAllEvent;
    }

    /**
     * @param numberOfAllEvent the numberOfAllEvent to set
     */
    public void setNumberOfAllEvent(Integer numberOfAllEvent) {
        this.numberOfAllEvent = numberOfAllEvent;
    }

    /**
     * @return the numberOfLateEvent
     */
    public Integer getNumberOfLateEvent() {
        return numberOfLateEvent;
    }

    /**
     * @param numberOfLateEvent the numberOfLateEvent to set
     */
    public void setNumberOfLateEvent(Integer numberOfLateEvent) {
        this.numberOfLateEvent = numberOfLateEvent;
    }

    /**
     * @return the numberOfNewEvent
     */
    public Integer getNumberOfNewEvent() {
        return numberOfNewEvent;
    }

    /**
     * @param numberOfNewEvent the numberOfNewEvent to set
     */
    public void setNumberOfNewEvent(Integer numberOfNewEvent) {
        this.numberOfNewEvent = numberOfNewEvent;
    }

    /**
     * @return the numberOfNowEvent
     */
    public Integer getNumberOfNowEvent() {
        return numberOfNowEvent;
    }

    /**
     * @param numberOfNowEvent the numberOfNowEvent to set
     */
    public void setNumberOfNowEvent(Integer numberOfNowEvent) {
        this.numberOfNowEvent = numberOfNowEvent;
    }

    /**
     * @return the leadStatus
     */
    public SymbolDTO getLeadStatus() {
        return leadStatus;
    }

    /**
     * @param leadStatus the leadStatus to set
     */
    public void setLeadStatus(SymbolDTO leadStatus) {
        this.leadStatus = leadStatus;
    }

    /**
     * @return the leadStatusActive
     */
    public Boolean getLeadStatusActive() {
        return leadStatusActive;
    }

    /**
     * @param leadStatusActive the leadStatusActive to set
     */
    public void setLeadStatusActive(Boolean leadStatusActive) {
        this.leadStatusActive = leadStatusActive;
    }

    /**
     * @return the notificationTitle
     */
    public String getNotificationTitle() {
        return notificationTitle;
    }

    /**
     * @param notificationTitle the notificationTitle to set
     */
    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    /**
     * @return the notificationMessage
     */
    public String getNotificationMessage() {
        return notificationMessage;
    }

    /**
     * @param notificationMessage the notificationMessage to set
     */
    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    /**
     * @return the notificationDate
     */
    public Date getNotificationDate() {
        return notificationDate;
    }

    /**
     * @param notificationDate the notificationDate to set
     */
    public void setNotificationDate(Date notificationDate) {
        this.notificationDate = notificationDate;
    }

    /**
     * @return the closeNotification
     */
    public Boolean getCloseNotification() {
        if (closeNotification == null) {
            closeNotification = Boolean.FALSE;
        }
        return closeNotification;
    }

    /**
     * @param closeNotification the closeNotification to set
     */
    public void setCloseNotification(Boolean closeNotification) {
        this.closeNotification = closeNotification;
    }

    /**
     * @return the notificationOldTime
     */
    public Boolean getNotificationOldTime() {
        return notificationOldTime;
    }

    /**
     * @param notificationOldTime the notificationOldTime to set
     */
    public void setNotificationOldTime(Boolean notificationOldTime) {
        this.notificationOldTime = notificationOldTime;
    }

    /**
     * @return the closeEvent
     */
    public Boolean getCloseEvent() {
        return closeEvent;
    }

    /**
     * @param closeEvent the closeEvent to set
     */
    public void setCloseEvent(Boolean closeEvent) {
        this.closeEvent = closeEvent;
    }

    /**
     * @return the closeLeadStatus
     */
    public Boolean getCloseLeadStatus() {
        return closeLeadStatus;
    }

    /**
     * @param closeLeadStatus the closeLeadStatus to set
     */
    public void setCloseLeadStatus(Boolean closeLeadStatus) {
        this.closeLeadStatus = closeLeadStatus;
    }

    /**
     * @return the gallaryId
     */
    public Integer getGallaryId() {
        return gallaryId;
    }

    /**
     * @param gallaryId the gallaryId to set
     */
    public void setGallaryId(Integer gallaryId) {
        this.gallaryId = gallaryId;
    }

   

 

    /**
     * @return the crmType
     */
    public SymbolDTO getCrmType() {
        return crmType;
    }

    /**
     * @param crmType the crmType to set
     */
    public void setCrmType(SymbolDTO crmType) {
        this.crmType = crmType;
    }

    /**
     * @return the invoiceId
     */
    public Integer getInvoiceId() {
        return invoiceId;
    }

    /**
     * @param invoiceId the invoiceId to set
     */
    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    /**
     * @return the periority
     */
    public SymbolDTO getPeriority() {
        return periority;
    }

    /**
     * @param periority the periority to set
     */
    public void setPeriority(SymbolDTO periority) {
        this.periority = periority;
    }

    /**
     * @return the gallarySelected
     */
    public InvInventoryDTO getGallarySelected() {
        return gallarySelected;
    }

    /**
     * @param gallarySelected the gallarySelected to set
     */
    public void setGallarySelected(InvInventoryDTO gallarySelected) {
        this.gallarySelected = gallarySelected;
    }

   

}
