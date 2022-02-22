/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "stu_comments")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comments.findAll", query = "SELECT s FROM Comments s")
    , @NamedQuery(name = "Comments.findById", query = "SELECT s FROM Comments s WHERE s.id = :id")
    , @NamedQuery(name = "Comments.findByComment", query = "SELECT s FROM Comments s WHERE s.comment = :comment")
    , @NamedQuery(name = "Comments.findByCreationDate", query = "SELECT s FROM Comments s WHERE s.creationDate = :creationDate")
    , @NamedQuery(name = "Comments.findByModificationDate", query = "SELECT s FROM Comments s WHERE s.modificationDate = :modificationDate")})
public class Comments extends BaseEntity {

    @Size(max = 450)
    @Column(name = "comment")
    private String comment;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "lesson_id", referencedColumnName = "id")
    @ManyToOne
    private Lesson lessonId;
    @OneToMany(mappedBy = "parent")
    private Collection<Comments> stuCommentsCollection;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private Comments parent;
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @ManyToOne
    private Student studentId;

    public Comments() {
    }

    public Comments(Integer id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public Lesson getLessonId() {
        return lessonId;
    }

    public void setLessonId(Lesson lessonId) {
        this.lessonId = lessonId;
    }

    @XmlTransient
    public Collection<Comments> getCommentsCollection() {
        return stuCommentsCollection;
    }

    public void setCommentsCollection(Collection<Comments> stuCommentsCollection) {
        this.stuCommentsCollection = stuCommentsCollection;
    }

    public Comments getParent() {
        return parent;
    }

    public void setParent(Comments parent) {
        this.parent = parent;
    }

    public Student getStudentId() {
        return studentId;
    }

    public void setStudentId(Student studentId) {
        this.studentId = studentId;
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
        if (!(object instanceof Comments)) {
            return false;
        }
        Comments other = (Comments) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com_igma_entity.Comments[ id=" + id + " ]";
    }

}
