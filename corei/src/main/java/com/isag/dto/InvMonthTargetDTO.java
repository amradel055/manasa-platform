/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author ahmed
 */
public class InvMonthTargetDTO extends BaseEntityDTO {

    private Integer target;
    private Date dateFrom;
    private Date dateTo;
    private Integer code;
    private Integer type;
    private Integer month;
    private List<String> monthList;
    private BigDecimal actualTotal;
    private BigDecimal difference;
    private BigDecimal plus;
    private Integer daysLeft;
    private Integer daysPassed;
    private Integer daysLeftPercentage;

    /**
     * @return the target
     */
    public Integer getTarget() {
        return target;
    }

    /**
     * @param target the target to set
     */
    public void setTarget(Integer target) {
        this.target = target;
    }

    /**
     * @return the dateFrom
     */
    public Date getDateFrom() {
        return dateFrom;
    }

    /**
     * @param dateFrom the dateFrom to set
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * @return the dateTo
     */
    public Date getDateTo() {
        return dateTo;
    }

    /**
     * @param dateTo the dateTo to set
     */
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    /**
     * @return the code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(Integer code) {
        this.code = code;
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
     * @return the month
     */
    public Integer getMonth() {
        return month;
    }

    /**
     * @param month the month to set
     */
    public void setMonth(Integer month) {
        this.month = month;
    }

    /**
     * @return the monthList
     */
    public List<String> getMonthList() {
        if (monthList == null) {
            monthList = new ArrayList<>();
            monthList.add("يناير");
            monthList.add("فبراير");
            monthList.add("مارس");
            monthList.add("ابريل");
            monthList.add("مايو");
            monthList.add("يونيو");
            monthList.add("يوليو");
            monthList.add("اغسطس");
            monthList.add("سبتمبر");
            monthList.add("اكتوبر");
            monthList.add("نوفمبر");
            monthList.add("ديسمبر");
        }
        return monthList;
    }

    /**
     * @param monthList the monthList to set
     */
    public void setMonthList(List<String> monthList) {
        this.monthList = monthList;
    }

    /**
     * @return the actualTotal
     */
    public BigDecimal getActualTotal() {
        if (actualTotal == null) {
            actualTotal = BigDecimal.ZERO;
        }
        return actualTotal;
    }

    /**
     * @param actualTotal the actualTotal to set
     */
    public void setActualTotal(BigDecimal actualTotal) {
        this.actualTotal = actualTotal;
    }

    /**
     * @return the difference
     */
    public BigDecimal getDifference() {
        if (getTarget() != null && getActualTotal() != null) {
            difference = BigDecimal.valueOf(getTarget()).subtract(getActualTotal());
            if (difference.signum() == -1) {
                difference = BigDecimal.valueOf(getTarget());
                setPlus(getActualTotal().subtract(BigDecimal.valueOf(getTarget())));
            } else if (difference.signum() == 0) {
                difference = BigDecimal.ZERO;
            }
        } else {
            difference = BigDecimal.ZERO;
        }
        return difference;
    }

    /**
     * @param difference the difference to set
     */
    public void setDifference(BigDecimal difference) {
        this.difference = difference;
    }

    /**
     * @return the plus
     */
    public BigDecimal getPlus() {
        if (plus == null) {
            plus = BigDecimal.ZERO;
        }
        return plus;
    }

    /**
     * @param plus the plus to set
     */
    public void setPlus(BigDecimal plus) {
        this.plus = plus;
    }

    /**
     * @return the daysLeft
     */
    public Integer getDaysLeft() {
        if (getDateFrom() != null && getDateTo() != null) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            setDaysPassed(cal.get(Calendar.DAY_OF_MONTH));
            long diffInMillies = Math.abs(getDateTo().getTime() - cal.getTime().getTime());
            Long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            daysLeft = diff.intValue();
        }
        return daysLeft;
    }

    /**
     * @param daysLeft the daysLeft to set
     */
    public void setDaysLeft(Integer daysLeft) {
        this.daysLeft = daysLeft;
    }

    /**
     * @return the daysPassed
     */
    public Integer getDaysPassed() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * @param daysPassed the daysPassed to set
     */
    public void setDaysPassed(Integer daysPassed) {
        this.daysPassed = daysPassed;
    }

    /**
     * @return the daysLeftPercentage
     */
    public Integer getDaysLeftPercentage() {
        if (getDateFrom() != null && getDateTo() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDateTo());
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            daysLeftPercentage = Float.valueOf(Float.valueOf(getDaysPassed()) / Float.valueOf(cal.get(Calendar.DAY_OF_MONTH)) * 100.0f).intValue();
        }
        return daysLeftPercentage;
    }

    /**
     * @param daysLeftPercentage the daysLeftPercentage to set
     */
    public void setDaysLeftPercentage(Integer daysLeftPercentage) {
        this.daysLeftPercentage = daysLeftPercentage;
    }

}
