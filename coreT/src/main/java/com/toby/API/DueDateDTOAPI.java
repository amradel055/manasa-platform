/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.API;

import java.util.Date;

public class DueDateDTOAPI extends BaseEntityDTOAPI {

    private Date dueDate;
    private Integer dayNumber;
    private Integer show;


    /**
     * @return the dueDate
     */
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * @param dueDate the dueDate to set
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * @return the dayNumber
     */
    public Integer getDayNumber() {
        return dayNumber;
    }

    /**
     * @param dayNumber the dayNumber to set
     */
    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }

    /**
     * @return the show
     */
    public Integer getShow() {
        return show;
    }

    /**
     * @param show the show to set
     */
    public void setShow(Integer show) {
        this.show = show;
    }

}
