/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.HosDoctorService;
import com.isag.businessservice.PatientRegistrationService;
import com.isag.businessservice.reports.HosClinicViewService;
import com.isag.businessservice.reports.searchBean.HosDoctorSearchBean;
import com.isag.converter.HosDoctorConverter;
import com.isag.converter.HosPatientRegistrationConvertor;
import com.isag.entity.HosDoctor;
import com.isag.entity.HosPatientRegistration;
import com.isag.isag.BaseReportBean;
import com.isag.isag.UserData;
import com.isag.report.entity.HosReportEntity;
import com.isag.views.HosClinicView;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author ahmed
 */
@Named("hosDoctorTotalIncomeMB")
@ViewScoped
public class HosDoctorTotalIncomeMB extends BaseReportBean {

    private List<HosDoctor> hosClinicsList;
    private HosDoctorConverter hosClinicConverter;
    private List<HosDoctor> hosDoctorsList;
    private List<HosReportEntity> hosReportEntityList;
    private HosDoctorConverter hosDoctorConverter;
    private List<HosPatientRegistration> patientRegistrationList;
    private HosPatientRegistrationConvertor patientRegistrationConvertor;
    private HosDoctorSearchBean hosDoctorSearchBean;
    private Map<Integer, BigDecimal> totalMap;
    private String uri;

    @EJB
    private HosDoctorService hosDoctorService;
    @EJB
    private HosClinicViewService hosClinicViewService;
    @EJB
    private PatientRegistrationService patientRegistrationService;

    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));

        reset();
        load();
    }

    @Override
    public void load() {
        hosClinicsList = hosDoctorService.getAllSubHosDoctorByBranchIdActiveAndLevel(getUserData().getUserBranch().getId(), 1);
        hosClinicConverter = new HosDoctorConverter(hosClinicsList);
        hosDoctorsList = hosDoctorService.getAllSubHosDoctorByBranchIdActiveAndLevel(getUserData().getUserBranch().getId(), 2);
        hosDoctorConverter = new HosDoctorConverter(hosDoctorsList);
        patientRegistrationList = patientRegistrationService.getPatientByBranchId(getUserData().getUserBranch().getId());
        patientRegistrationConvertor = new HosPatientRegistrationConvertor(patientRegistrationList);
        uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();

        fillTotalMap();
    }

    @Override
    public void reset() {
        hosDoctorSearchBean = new HosDoctorSearchBean();
        hosReportEntityList = new ArrayList<>();
        totalMap = new HashMap<>();
    }

    @Override
    public void search() {
        hosDoctorSearchBean.setBranchId(getUserData().getUserBranch().getId());
        hosReportEntityList = new ArrayList<>();
        fillTotalMap();
        List<HosClinicView> hosClinicViewList = new ArrayList<>();
        if (uri.contains("TotalIncome")) {
            hosClinicViewList = hosClinicViewService.getHosClinicViewBySearchBeanOrderedBy(hosDoctorSearchBean, " ORDER BY e.hosDoctorId , e.date ");
        } else {
            hosClinicViewList = hosClinicViewService.getHosClinicViewBySearchBeanOrderedBy(hosDoctorSearchBean, " ORDER BY e.hosPatientId , e.date ");
        }

        if (hosClinicViewList != null && !hosClinicViewList.isEmpty()) {
            for (HosClinicView hosClinicView : hosClinicViewList) {
                HosReportEntity hosReportEntity = new HosReportEntity();
                hosReportEntity.setDoctorName(hosClinicView.getDoctorName());
                hosReportEntity.setDate(hosClinicView.getDate());
                hosReportEntity.setPatientName(hosClinicView.getHosPatientName());
                hosReportEntity.setService(hosClinicView.getServiceName());
                hosReportEntity.setValue(hosClinicView.getValue());
                if (uri.contains("TotalIncome")) {
                    hosReportEntity.setTotalValue(totalMap.get(hosClinicView.getHosDoctorId()));
                } else {
                    hosReportEntity.setTotalValue(totalMap.get(hosClinicView.getHosPatientId()));
                }
                hosReportEntityList.add(hosReportEntity);
            }
        }
    }

    public void fillTotalMap() {

        totalMap = new HashMap<>();
        List<HosClinicView> totalsList = new ArrayList<>();
        totalsList = hosClinicViewService.getTotalsBySearchBeanGroupedBy(hosDoctorSearchBean, "TotalIncome");

        if (totalsList != null && !totalsList.isEmpty()) {
            for (HosClinicView view : totalsList) {
                if (uri.contains("TotalIncome")) {
                    totalMap.put(view.getHosDoctorId(), view.getValue());
                } else {
                    totalMap.put(view.getHosPatientId(), view.getValue());
                }
            }
        }
    }

    public void setSearchBeanInValidation() {
        hosDoctorSearchBean.setBranchId(getUserData().getUserBranch().getId());
    }

    @Override
    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();

        hashMap.put("PrintedBy", getUserData().getUser().getName());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        hashMap.put("companyName", getUserData().getCompany().getName());
        hashMap.put("doctorNameText", userDDs.get("DOCTOR"));
        hashMap.put("patientNameText", userDDs.get("PATIENT"));
        hashMap.put("dateText", userDDs.get("DATE"));
        hashMap.put("serviceText", userDDs.get("SERVICE"));
        hashMap.put("valueText", userDDs.get("VALUE"));
        hashMap.put("yearFromText", userDDs.get("YEAR_FROM"));
        hashMap.put("yearToText", userDDs.get("YEAR_TO"));
        hashMap.put("clinicFromText", userDDs.get("FROM_CLINIC"));
        hashMap.put("clinicToText", userDDs.get("TO_CLINIC"));
        hashMap.put("doctorFromText", userDDs.get("FROM_DOC"));
        hashMap.put("doctorToText", userDDs.get("TO_DOC"));
        hashMap.put("patientFromText", userDDs.get("FROM_PATIENT"));
        hashMap.put("patientToText", userDDs.get("TO_PATIENT"));
        hashMap.put("totalText", userDDs.get("TOTAL"));
        hashMap.put("reportTitleText", userDDs.get("TOTA_DOC_REVEN"));
        hashMap.put("yearFromValue", hosDoctorSearchBean.getDateFrom());
        hashMap.put("yearToValue", hosDoctorSearchBean.getDateTo());
        hashMap.put("clinicFromValue", hosDoctorSearchBean.getHosClinicIdFrom() != null ? hosDoctorSearchBean.getHosClinicIdFrom().getName() : null);
        hashMap.put("clinicToValue", hosDoctorSearchBean.getHosClinicIdTo() != null ? hosDoctorSearchBean.getHosClinicIdTo().getName() : null);
        hashMap.put("doctorFromValue", hosDoctorSearchBean.getHosDoctorIdFrom() != null ? hosDoctorSearchBean.getHosDoctorIdFrom().getName() : null);
        hashMap.put("doctorToValue", hosDoctorSearchBean.getHosDoctorIdTo() != null ? hosDoctorSearchBean.getHosDoctorIdTo().getName() : null);
        hashMap.put("patientFromValue", hosDoctorSearchBean.getHosPatientIdFrom() != null ? hosDoctorSearchBean.getHosPatientIdFrom().getName() : null);
        hashMap.put("patientToValue", hosDoctorSearchBean.getHosPatientIdTo() != null ? hosDoctorSearchBean.getHosPatientIdTo().getName() : null);
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (hosReportEntityList != null && !hosReportEntityList.isEmpty()) {
            if (uri.contains("TotalIncome")) {
                fillReport(prepareReport(), getUserData().getReportPath() + "hosDoctorTotalIncomeReport.jasper", hosReportEntityList, "pdf");
            } else {
                fillReport(prepareReport(), getUserData().getReportPath() + "hosStatementReport.jasper", hosReportEntityList, "pdf");
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد نتائج للطباعة"));
        }
    }

    public List<HosDoctor> completeClinicHosDoctor(String query) {
        List<HosDoctor> doctorsList = hosClinicsList;
        if (query == null || query.trim().equals("")) {

            hosClinicConverter = new HosDoctorConverter(doctorsList);
            return doctorsList;
        }
        List<HosDoctor> filteredHosDoctors = new ArrayList<>();

        String nameAr;
        String code;
        HosDoctor hosDoctorFilter;
        for (int i = 0; i < hosClinicsList.size(); i++) {
            hosDoctorFilter = doctorsList.get(i);
            nameAr = hosDoctorFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredHosDoctors.contains(hosDoctorFilter)) {
                    filteredHosDoctors.add(hosDoctorFilter);
                }
            }

            code = hosDoctorFilter.getCode();
            if (code != null && String.valueOf(code).contains(query.toLowerCase())) {
                if (!filteredHosDoctors.contains(hosDoctorFilter)) {
                    filteredHosDoctors.add(hosDoctorFilter);
                }
            }
        }

        hosClinicConverter = new HosDoctorConverter(filteredHosDoctors);
        return filteredHosDoctors;
    }

    public List<HosDoctor> completeDoctorHosDoctor(String query) {
        List<HosDoctor> doctorsList = hosDoctorsList;
        if (query == null || query.trim().equals("")) {

            hosDoctorConverter = new HosDoctorConverter(doctorsList);
            return doctorsList;
        }
        List<HosDoctor> filteredHosDoctors = new ArrayList<>();

        String nameAr;
        String code;
        HosDoctor hosDoctorFilter;
        for (int i = 0; i < hosDoctorsList.size(); i++) {
            hosDoctorFilter = doctorsList.get(i);
            nameAr = hosDoctorFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredHosDoctors.contains(hosDoctorFilter)) {
                    filteredHosDoctors.add(hosDoctorFilter);
                }
            }

            code = hosDoctorFilter.getCode();
            if (code != null && String.valueOf(code).contains(query.toLowerCase())) {
                if (!filteredHosDoctors.contains(hosDoctorFilter)) {
                    filteredHosDoctors.add(hosDoctorFilter);
                }
            }
        }

        hosDoctorConverter = new HosDoctorConverter(filteredHosDoctors);
        return filteredHosDoctors;
    }

    public List<HosPatientRegistration> completPatient(String query) {
        List<HosPatientRegistration> hosPatientRegistrationList = getPatientRegistrationList();
        if (query == null || query.trim().equals("")) {

            setPatientRegistrationConvertor(new HosPatientRegistrationConvertor(hosPatientRegistrationList));
            return hosPatientRegistrationList;
        }
        List<HosPatientRegistration> filteredInvs = new ArrayList<>();

        String nameAr;
        String code;
        HosPatientRegistration hosPatientFilter;

        for (int i = 0; i < getPatientRegistrationList().size(); i++) {
            hosPatientFilter = hosPatientRegistrationList.get(i);
            nameAr = hosPatientFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(hosPatientFilter)) {
                    filteredInvs.add(hosPatientFilter);
                }
            }

            code = hosPatientFilter.getCode().toString();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(hosPatientFilter)) {
                    filteredInvs.add(hosPatientFilter);
                }
            }
        }

        setPatientRegistrationConvertor(new HosPatientRegistrationConvertor(filteredInvs));
        return filteredInvs;
    }

    @Override
    public void exportExcel(ActionEvent actionEvent) throws JRException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportHtml(ActionEvent actionEvent) throws JRException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the hosClinicsList
     */
    public List<HosDoctor> getHosClinicsList() {
        return hosClinicsList;
    }

    /**
     * @param hosClinicsList the hosClinicsList to set
     */
    public void setHosClinicsList(List<HosDoctor> hosClinicsList) {
        this.hosClinicsList = hosClinicsList;
    }

    /**
     * @return the hosClinicConverter
     */
    public HosDoctorConverter getHosClinicConverter() {
        return hosClinicConverter;
    }

    /**
     * @param hosClinicConverter the hosClinicConverter to set
     */
    public void setHosClinicConverter(HosDoctorConverter hosClinicConverter) {
        this.hosClinicConverter = hosClinicConverter;
    }

    /**
     * @return the hosDoctorsList
     */
    public List<HosDoctor> getHosDoctorsList() {
        return hosDoctorsList;
    }

    /**
     * @param hosDoctorsList the hosDoctorsList to set
     */
    public void setHosDoctorsList(List<HosDoctor> hosDoctorsList) {
        this.hosDoctorsList = hosDoctorsList;
    }

    /**
     * @return the hosDoctorConverter
     */
    public HosDoctorConverter getHosDoctorConverter() {
        return hosDoctorConverter;
    }

    /**
     * @param hosDoctorConverter the hosDoctorConverter to set
     */
    public void setHosDoctorConverter(HosDoctorConverter hosDoctorConverter) {
        this.hosDoctorConverter = hosDoctorConverter;
    }

    /**
     * @return the hosDoctorSearchBean
     */
    public HosDoctorSearchBean getHosDoctorSearchBean() {
        return hosDoctorSearchBean;
    }

    /**
     * @param hosDoctorSearchBean the hosDoctorSearchBean to set
     */
    public void setHosDoctorSearchBean(HosDoctorSearchBean hosDoctorSearchBean) {
        this.hosDoctorSearchBean = hosDoctorSearchBean;
    }

    /**
     * @return the hosReportEntityList
     */
    public List<HosReportEntity> getHosReportEntityList() {
        return hosReportEntityList;
    }

    /**
     * @param hosReportEntityList the hosReportEntityList to set
     */
    public void setHosReportEntityList(List<HosReportEntity> hosReportEntityList) {
        this.hosReportEntityList = hosReportEntityList;
    }

    /**
     * @return the totalMap
     */
    public Map<Integer, BigDecimal> getTotalMap() {
        return totalMap;
    }

    /**
     * @param totalMap the totalMap to set
     */
    public void setTotalMap(Map<Integer, BigDecimal> totalMap) {
        this.totalMap = totalMap;
    }

    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * @return the patientRegistrationList
     */
    public List<HosPatientRegistration> getPatientRegistrationList() {
        return patientRegistrationList;
    }

    /**
     * @param patientRegistrationList the patientRegistrationList to set
     */
    public void setPatientRegistrationList(List<HosPatientRegistration> patientRegistrationList) {
        this.patientRegistrationList = patientRegistrationList;
    }

    /**
     * @return the patientRegistrationConvertor
     */
    public HosPatientRegistrationConvertor getPatientRegistrationConvertor() {
        return patientRegistrationConvertor;
    }

    /**
     * @param patientRegistrationConvertor the patientRegistrationConvertor to
     * set
     */
    public void setPatientRegistrationConvertor(HosPatientRegistrationConvertor patientRegistrationConvertor) {
        this.patientRegistrationConvertor = patientRegistrationConvertor;
    }

}
