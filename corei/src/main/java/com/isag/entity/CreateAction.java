package com.isag.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author khaled
 */
@Entity
@Table(name = "create_action")
public class CreateAction extends BaseEntity {

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private IsagUser userId;
    @Column(name = "type")
    private String type;
      @Column(name = "screen_name")
    private String screenName;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAction;  
      
    



   

    public IsagUser getUserId() {
        return userId;
    }

    public void setUserId(IsagUser userId) {
        this.userId = userId;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the screenName
     */
    public String getScreenName() {
        return screenName;
    }

    /**
     * @param screenName the screenName to set
     */
    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    /**
     * @return the dateAction
     */
    public Date getDateAction() {
        return dateAction;
    }

    /**
     * @param dateAction the dateAction to set
     */
    public void setDateAction(Date dateAction) {
        this.dateAction = dateAction;
    }


   
  

}
