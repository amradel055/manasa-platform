/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

/**
 *
 * @author AhmedEssam
 */
public class EventResponseDTO extends BaseEntityDTO {

    private String comment;
    private Integer assignTo;
    private String assignToName;
    
    private EmployeeDTO assignedTo;

    private Integer eventId;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventResponseDTO)) {
            return false;
        }
        EventResponseDTO other = (EventResponseDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.CurrencyOperation[ id=" + id + " ]";
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return the assignTo
     */
    public Integer getAssignto() {
        return assignTo;
    }

    /**
     * @param assignTo the assignTo to set
     */
    public void setAssignto(Integer assignTo) {
        this.assignTo = assignTo;
    }

    /**
     * @return the eventId
     */
    public Integer getEventId() {
        return eventId;
    }

    /**
     * @param eventId the eventId to set
     */
    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    /**
     * @return the assignToName
     */
    public String getAssignToName() {
        return assignToName;
    }

    /**
     * @param assignToName the assignToName to set
     */
    public void setAssignToName(String assignToName) {
        this.assignToName = assignToName;
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


}
