/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.searchBean.HosDoctorSearchBean;
import com.isag.core.GenericDAO;
import com.isag.views.HosClinicView;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author ahmed
 */
@Stateless
public class HosClinicViewServiceImpl implements HosClinicViewService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<HosClinicView> getHosClinicViewBySearchBean(HosDoctorSearchBean hosDoctorSearchBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", hosDoctorSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        String query = "SELECT e FROM HosClinicView e WHERE e.branchId = :branchId ";
        stringBuilder.append(query);
        appendDateForQueryBuilder(stringBuilder, params, hosDoctorSearchBean);
        appendClinicForQueryBuilder(stringBuilder, params, hosDoctorSearchBean);
        appendDoctorForQueryBuilder(stringBuilder, params, hosDoctorSearchBean);
        appendPatientForQueryBuilder(stringBuilder, params, hosDoctorSearchBean);
        List<HosClinicView> details = dao.findListByQuery(stringBuilder.toString(), params);
        return details;
    }

    public void appendDateForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, HosDoctorSearchBean hosDoctorSearchBean) {
        if (hosDoctorSearchBean.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(hosDoctorSearchBean.getDateFrom());
            queryBuilder.append(" and e.date >= '").append(formatDateFrom).append("'");
        }

        if (hosDoctorSearchBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(hosDoctorSearchBean.getDateTo());
            queryBuilder.append(" and e.date <= '").append(formatDateTo).append("'");
        }
    }

    private void appendClinicForQueryBuilder(StringBuilder stringBuilder, Map<String, Object> params, HosDoctorSearchBean hosDoctorSearchBean) {

        if (hosDoctorSearchBean.getHosClinicIdFrom() != null) {
            params.put("clinicIdFrom", hosDoctorSearchBean.getHosClinicIdFrom().getId());
            stringBuilder.append(" AND e.hosClinicId >= :clinicIdFrom");
        }

        if (hosDoctorSearchBean.getHosClinicIdTo() != null) {
            params.put("clinicIdTo", hosDoctorSearchBean.getHosClinicIdTo().getId());
            stringBuilder.append(" AND e.hosClinicId <= :clinicIdTo");
        }

    }

    private void appendDoctorForQueryBuilder(StringBuilder stringBuilder, Map<String, Object> params, HosDoctorSearchBean hosDoctorSearchBean) {

        if (hosDoctorSearchBean.getHosDoctorIdFrom() != null) {
            params.put("doctorIdFrom", hosDoctorSearchBean.getHosDoctorIdFrom().getId());
            stringBuilder.append(" AND e.hosDoctorId >= :doctorIdFrom");
        }

        if (hosDoctorSearchBean.getHosDoctorIdTo() != null) {
            params.put("doctorIdTo", hosDoctorSearchBean.getHosDoctorIdTo().getId());
            stringBuilder.append(" AND e.hosDoctorId <= :doctorIdTo");
        }

    }

    private void appendPatientForQueryBuilder(StringBuilder stringBuilder, Map<String, Object> params, HosDoctorSearchBean hosDoctorSearchBean) {

        if (hosDoctorSearchBean.getHosPatientIdFrom() != null) {
            params.put("patientIdFrom", hosDoctorSearchBean.getHosPatientIdFrom().getId());
            stringBuilder.append(" AND e.hosPatientId >= :patientIdFrom");
        }

        if (hosDoctorSearchBean.getHosPatientIdTo() != null) {
            params.put("patientIdTo", hosDoctorSearchBean.getHosPatientIdTo().getId());
            stringBuilder.append(" AND e.hosPatientId <= :patientIdTo");
        }

    }

    @Override
    public List<HosClinicView> getHosClinicViewBySearchBeanOrderedBy(HosDoctorSearchBean hosDoctorSearchBean, String orderBy) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", hosDoctorSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        String query = "SELECT e FROM HosClinicView e WHERE e.branchId = :branchId ";
        stringBuilder.append(query);
        appendDateForQueryBuilder(stringBuilder, params, hosDoctorSearchBean);
        appendClinicForQueryBuilder(stringBuilder, params, hosDoctorSearchBean);
        appendDoctorForQueryBuilder(stringBuilder, params, hosDoctorSearchBean);
        appendPatientForQueryBuilder(stringBuilder, params, hosDoctorSearchBean);
        stringBuilder.append(orderBy);
        List<HosClinicView> details = dao.findListByQuery(stringBuilder.toString(), params);
        return details;
    }

    @Override
    public List<HosClinicView> getTotalsBySearchBeanGroupedBy(HosDoctorSearchBean hosDoctorSearchBean, String uri) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", hosDoctorSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        String query ;
        if (uri.contains("TotalIncome")) {
            query = "SELECT sum(COALESCE(e.value,0)),e.hosDoctorId,null FROM HosClinicView e WHERE e.branchId = :branchId ";
        }else{
            query = "SELECT sum(COALESCE(e.value,0)),null,e.hosPatientId FROM HosClinicView e WHERE e.branchId = :branchId ";
        }
        
        stringBuilder.append(query);
        appendDateForQueryBuilder(stringBuilder, params, hosDoctorSearchBean);
        appendClinicForQueryBuilder(stringBuilder, params, hosDoctorSearchBean);
        appendDoctorForQueryBuilder(stringBuilder, params, hosDoctorSearchBean);
        appendPatientForQueryBuilder(stringBuilder, params, hosDoctorSearchBean);

        if (uri.contains("TotalIncome")) {
            stringBuilder.append(" GROUP BY e.hosDoctorId  ");
        }else{
            stringBuilder.append(" GROUP BY e.hosPatientId ");
        }
        
        List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);
        List<HosClinicView> details = new ArrayList<>();
        details = getTheTotals(res);
        return details;
    }

    public List<HosClinicView> getTheTotals(List<Object[]> res) {

        List<HosClinicView> hosClinicViewList = new ArrayList<>();

        Iterator it = res.iterator();
        while (it.hasNext()) {
            Object[] line = (Object[]) it.next();
            HosClinicView clinicView = new HosClinicView();
            clinicView.setValue((BigDecimal) line[0]);
            clinicView.setHosDoctorId((Integer) line[1]);
            clinicView.setHosPatientId((Integer) line[2]);
            hosClinicViewList.add(clinicView);
        }
        return hosClinicViewList;
    }
}
