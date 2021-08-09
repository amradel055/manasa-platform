/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author User
 */
@Entity
@Table(name = "crm_event")
@XmlRootElement
public class CrmEvent extends BaseEntity {
    @Size(max = 100)
    @Column(name = "subject")
    private String subject;
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    @JoinColumn(name = "activity_type", referencedColumnName = "id")
    @ManyToOne
    private Symbol activityType;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "status", referencedColumnName = "id")
    @ManyToOne
    private Symbol status;
    @JoinColumn(name = "assigned_to", referencedColumnName = "id")
    @ManyToOne
    private Employee assignedTo;
    @Column(name = "type")
    private Integer type;
    @Column(name = "serial")
    private Integer serial;
    @JoinColumn(name = "deleted_by", referencedColumnName = "id")
    @ManyToOne
    private IsagUser deletedBy;  
    
    @Column(name = "deleted_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedDate;
    
    @Column(name = "send_notification")
    private Integer sendNotification;
    @Size(max = 100)
    @Column(name = "location")
    private String location; 
    @JoinColumn(name = "priority", referencedColumnName = "id")
    @ManyToOne
    private Symbol priority; 
    @Column(name = "visibility")
    private Integer visibility;
    @Column(name = "send_email")
    private Integer sendEmail;
    @Column(name = "repeat1")
    private Integer repeat1;
    @Column(name = "repeat_number")
    private Integer repeatNumber;
    @Column(name = "time_type")
    private Integer timeType;
    @Column(name = "repeat_period")
    private Long repeatPeriod;
    @Size(max = 400)
    @Column(name = "discription")
    private String discription;
    @Column(name = "notification_time_type")
    private Integer notificationTimeType;
    @Column(name = "notification_number")
    private BigDecimal notificationNumber;
    @Size(max = 100)
    @Column(name = "remark")
    private String remark;    
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne
    private InvOrganizationSite customerId; 
    @Column(name = "event_type")
    private Boolean eventType;
    @JoinColumn(name = "lead_status", referencedColumnName = "id")
    @ManyToOne
    private Symbol leadStatus; 
    @Size(max = 100)
    @Column(name = "notification_title")
    private String notificationTitle;
    @Size(max = 300)
    @Column(name = "notification_message")
    private String notificationMessage;
    @Column(name = "notification_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date notificationDate;
    @Column(name = "close_notification")
    private Boolean closeNotification;
    @Column(name = "close_event")
    private Boolean closeEvent;
     
    public CrmEvent() {
    }

    public CrmEvent(Integer id) {
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

    public Symbol getActivityType() {
        return activityType;
    }

    public void setActivityType(Symbol activityType) {
        this.activityType = activityType;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public Symbol getStatus() {
        return status;
    }

    public void setStatus(Symbol status) {
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
        if (!(object instanceof CrmEvent)) {
            return false;
        }
        CrmEvent other = (CrmEvent) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "evdfd.CrmEvent[ id=" + id + " ]";
    }

    /**
     * @return the assignedTo
     */
    public Employee getAssignedTo() {
        return assignedTo;
    }

    /**
     * @param assignedTo the assignedTo to set
     */
    public void setAssignedTo(Employee assignedTo) {
        this.assignedTo = assignedTo;
    }

    /**
     * @return the type
     */
    public Integer getType() {
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
     * @return the deletedBy
     */
    public IsagUser getDeletedBy() {
        return deletedBy;
    }

    /**
     * @param deletedBy the deletedBy to set
     */
    public void setDeletedBy(IsagUser deletedBy) {
        this.deletedBy = deletedBy;
    }

    /**
     * @return the deletedDate
     */
    public Date getDeletedDate() {
        return deletedDate;
    }

    /**
     * @param deletedDate the deletedDate to set
     */
    public void setDeletedDate(Date deletedDate) {
        this.deletedDate = deletedDate;
    }

    /**
     * @return the sendNotification
     */
    public Integer getSendNotification() {
        return sendNotification;
    }

    /**
     * @param sendNotification the sendNotification to set
     */
    public void setSendNotification(Integer sendNotification) {
        this.sendNotification = sendNotification;
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
    public Symbol getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(Symbol priority) {
        this.priority = priority;
    }

    /**
     * @return the visibility
     */
    public Integer getVisibility() {
        return visibility;
    }

    /**
     * @param visibility the visibility to set
     */
    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
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
     * @return the repeat
     */
    public Integer getRepeat1() {
        return repeat1;
    }

    /**
     * @param repeat the repeat to set
     */
    public void setRepeat1(Integer repeat1) {
        this.repeat1 = repeat1;
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
     * @return the timeType
     */
    public Integer getTimeType() {
        return timeType;
    }

    /**
     * @param timeType the timeType to set
     */
    public void setTimeType(Integer timeType) {
        this.timeType = timeType;
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
    public InvOrganizationSite getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(InvOrganizationSite customerId) {
        this.customerId = customerId;
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
     * @return the leadStatus
     */
    public Symbol getLeadStatus() {
        return leadStatus;
    }

    /**
     * @param leadStatus the leadStatus to set
     */
    public void setLeadStatus(Symbol leadStatus) {
        this.leadStatus = leadStatus;
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
        return closeNotification;
    }

    /**
     * @param closeNotification the closeNotification to set
     */
    public void setCloseNotification(Boolean closeNotification) {
        this.closeNotification = closeNotification;
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
    
}
