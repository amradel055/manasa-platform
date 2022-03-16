/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.*;
import com.isag.businessservice.reports.searchBean.HosPatientReportSearchBean;
import com.isag.core.GenericDAO;
import com.isag.entity.HosPatientRegistration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq002
 */
@Stateless
public class HosPatientServiceReportImpl implements HosPatientServiceReport {

    @EJB
    private GenericDAO dao;
   StringBuilder appendQuery;
   @Override
   public List<HosPatientRegistration> getAllPatientDataBySearchBean(HosPatientReportSearchBean patientReportSearchBean)
   {
        Map<String, Object> params = new HashMap<>();

        params.put("branchId", patientReportSearchBean.getBranchId());
        params.put("type", patientReportSearchBean.getType());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT RIV FROM HosPatientRegistration RIV WHERE RIV.branchId.id = :branchId and RIV.type = :type");
        
        stringBuilder.append(appendPatient(patientReportSearchBean, params));
        
        List<HosPatientRegistration> details = dao.findListByQuery(stringBuilder.toString(), params);
        return details;
    }

    private String appendPatient(HosPatientReportSearchBean patientReportSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (patientReportSearchBean.getPatient() != null) {
            params.put("Patient", patientReportSearchBean.getPatient().getCode());
            appendQuery.append(" AND RIV.code = :Patient ");
        }

        
        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
