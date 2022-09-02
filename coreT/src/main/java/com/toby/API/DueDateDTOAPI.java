/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.API;

import com.toby.dto.BaseEntityDTO;
import java.util.Date;

public class DueDateDTOAPI extends BaseEntityDTO {

    private Date dueDate;
    private Integer dayNumber;


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

}
