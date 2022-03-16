package com.isag.businessservice.reports.searchBean;

import com.isag.entity.HosDoctor;
import com.isag.entity.HosPatientRegistration;
import java.util.Date;

public class HosDoctorSearchBean {

    private HosDoctor hosDoctorIdFrom;
    private HosDoctor hosDoctorIdTo;
    private HosDoctor hosClinicIdFrom;
    private HosDoctor hosClinicIdTo;
    private HosPatientRegistration hosPatientIdFrom;
    private HosPatientRegistration hosPatientIdTo;
    private Date dateFrom;
    private Date dateTo;
    private Integer branchId;

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the hosDoctorIdFrom
     */
    public HosDoctor getHosDoctorIdFrom() {
        return hosDoctorIdFrom;
    }

    /**
     * @param hosDoctorIdFrom the hosDoctorIdFrom to set
     */
    public void setHosDoctorIdFrom(HosDoctor hosDoctorIdFrom) {
        this.hosDoctorIdFrom = hosDoctorIdFrom;
    }

    /**
     * @return the hosDoctorIdTo
     */
    public HosDoctor getHosDoctorIdTo() {
        return hosDoctorIdTo;
    }

    /**
     * @param hosDoctorIdTo the hosDoctorIdTo to set
     */
    public void setHosDoctorIdTo(HosDoctor hosDoctorIdTo) {
        this.hosDoctorIdTo = hosDoctorIdTo;
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
     * @return the hosClinicIdFrom
     */
    public HosDoctor getHosClinicIdFrom() {
        return hosClinicIdFrom;
    }

    /**
     * @param hosClinicIdFrom the hosClinicIdFrom to set
     */
    public void setHosClinicIdFrom(HosDoctor hosClinicIdFrom) {
        this.hosClinicIdFrom = hosClinicIdFrom;
    }

    /**
     * @return the hosClinicIdTo
     */
    public HosDoctor getHosClinicIdTo() {
        return hosClinicIdTo;
    }

    /**
     * @param hosClinicIdTo the hosClinicIdTo to set
     */
    public void setHosClinicIdTo(HosDoctor hosClinicIdTo) {
        this.hosClinicIdTo = hosClinicIdTo;
    }

    /**
     * @return the hosPatientIdFrom
     */
    public HosPatientRegistration getHosPatientIdFrom() {
        return hosPatientIdFrom;
    }

    /**
     * @param hosPatientIdFrom the hosPatientIdFrom to set
     */
    public void setHosPatientIdFrom(HosPatientRegistration hosPatientIdFrom) {
        this.hosPatientIdFrom = hosPatientIdFrom;
    }

    /**
     * @return the hosPatientIdTo
     */
    public HosPatientRegistration getHosPatientIdTo() {
        return hosPatientIdTo;
    }

    /**
     * @param hosPatientIdTo the hosPatientIdTo to set
     */
    public void setHosPatientIdTo(HosPatientRegistration hosPatientIdTo) {
        this.hosPatientIdTo = hosPatientIdTo;
    }

   

   

}
