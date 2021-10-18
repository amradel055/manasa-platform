/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "stu_documents")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Documents.findAll", query = "SELECT s FROM Documents s")
    , @NamedQuery(name = "Documents.findById", query = "SELECT s FROM Documents s WHERE s.id = :id")
    , @NamedQuery(name = "Documents.findByLink", query = "SELECT s FROM Documents s WHERE s.link = :link")
    , @NamedQuery(name = "Documents.findByCreationDate", query = "SELECT s FROM Documents s WHERE s.creationDate = :creationDate")
    , @NamedQuery(name = "Documents.findByModificationDate", query = "SELECT s FROM Documents s WHERE s.modificationDate = :modificationDate")})
public class Documents extends BaseEntity {

    @Size(max = 450)
    @Column(name = "link")
    private String link;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "lesson_id", referencedColumnName = "id")
    @ManyToOne
    private Lesson lessonId;

    public Documents() {
    }

   

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Documents)) {
            return false;
        }
        Documents other = (Documents) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com_igma_entity.Documents[ id=" + id + " ]";
    }

}
