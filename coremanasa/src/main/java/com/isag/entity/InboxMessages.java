/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;


/**
 *
 * @author m lapan
 */
@Entity
@Table(name = "inbox_messages")
public class InboxMessages implements Serializable{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "student_id", nullable = false)
    private Integer studentId;

    @Column(name = "teacher_id", nullable = false)
    private Integer teacherId;
    
    @Column(name = "material_id", nullable = false)
    private Integer materialId;

    @Column(name = "subject")
    private String subject;

    @Column(name = "body")
    private String body;

    @Column(name = "sent_date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date sentDate;

    @Column(name = "read_done")
    private Integer readDone;
    
    @Transient
    private List<InboxReply> inboxReplys ;
   

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

	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public Integer getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}

	public Integer getMaterialId() {
		return materialId;
	}

	public void setMaterialId(Integer materialId) {
		this.materialId = materialId;
	}

	public Integer getReadDone() {
		return readDone;
	}

	public void setReadDone(Integer readDone) {
		this.readDone = readDone;
	}

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the inboxReplys
     */
    public List<InboxReply> getInboxReplys() {
        return inboxReplys;
    }

    /**
     * @param inboxReplys the inboxReplys to set
     */
    public void setInboxReplys(List<InboxReply> inboxReplys) {
        this.inboxReplys = inboxReplys;
    }

}
