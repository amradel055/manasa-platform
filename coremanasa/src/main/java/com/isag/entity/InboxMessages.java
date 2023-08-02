/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

/**
 *
 * @author m lapan
 */
@Entity
@Table(name = "inbox_messages")
public class InboxMessages extends BaseEntity {

    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @ManyToOne()
    private Student studentId;

    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    @ManyToOne()
    private IgmaUser teacherId;

    @JoinColumn(name = "material_id", referencedColumnName = "id")
    @ManyToOne()
    private Materials materialId;
    
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne()
    private Branch branchId;

    @Column(name = "subject")
    private String subject;

    @Column(name = "body")
    private String body;

    @Column(name = "image")
    private String image;

    @Column(name = "sent_date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date sentDate;

    @Column(name = "read_done")
    private Integer readDone;

    @Transient
    private InboxReply inboxReply;

    // getters and setters
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public Student getStudentId() {
        return studentId;
    }

    public void setStudentId(Student studentId) {
        this.studentId = studentId;
    }

    public IgmaUser getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(IgmaUser teacherId) {
        this.teacherId = teacherId;
    }

    public Materials getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Materials materialId) {
        this.materialId = materialId;
    }

    public Integer getReadDone() {
        return readDone;
    }

    public void setReadDone(Integer readDone) {
        this.readDone = readDone;
    }

    /**
     * @return the inboxReply
     */
    public InboxReply getInboxReply() {
        return inboxReply;
    }

    /**
     * @param inboxReply the inboxReply to set
     */
    public void setInboxReply(InboxReply inboxReply) {
        this.inboxReply = inboxReply;
    }

    /**
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return the branchId
     */
    public Branch getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

}
