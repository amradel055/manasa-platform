/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.*;
import com.isag.businessservice.reports.searchBean.HosPatientReportSearchBean;
import com.isag.entity.HosPatientRegistration;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface HosPatientServiceReport {


    public List<HosPatientRegistration> getAllPatientDataBySearchBean(HosPatientReportSearchBean patientReportSearchBean);

    

  

}
