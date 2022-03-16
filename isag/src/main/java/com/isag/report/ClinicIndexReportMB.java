/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.HosDoctorService;
import com.isag.businessservice.reports.searchBean.HosDoctorSearchBean;
import com.isag.converter.HosDoctorConverter;
import com.isag.entity.HosDoctor;
import com.isag.isag.BaseReportBean;
import com.isag.report.entity.ClinicIndexReportBean;
import com.isag.report.entity.CostCenterIndexBean;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author ahmed
 */
@ViewScoped
@Named(value = "clinicIndexReportMB")
public class ClinicIndexReportMB extends BaseReportBean {

    private List<HosDoctor> hosDoctorList;
    private List<HosDoctor> hosDoctorSearchedList;
    private List<CostCenterIndexBean> costCenterIndexBeanList;
    private List<ClinicIndexReportBean> clinicIndexReportBeanList;
    private List<ClinicIndexReportBean> clinicIndexReportBeanForReportList;
    private Map<Integer, ClinicIndexReportBean> map = new TreeMap<>();
    private Map<Integer, ClinicIndexReportBean> parentWithChildsMap = new TreeMap<>();
    private JasperPrint jasperPrint;
    private Integer increment;

    private HosDoctorSearchBean hosDoctorSearchBean;

    @EJB
    private HosDoctorService costCenterService;
    private HosDoctorConverter hosDoctorConverter;

    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
//       userData = (UserData) context.getSessionMap().get("userLogInData");
        reset();
        load();
    }

    @Override
    public void load() {
        costCenterIndexBeanList = new ArrayList<>(0);
        hosDoctorSearchedList = new ArrayList<>(0);
        clinicIndexReportBeanList = new ArrayList<>(0);
        clinicIndexReportBeanForReportList = new ArrayList<>(0);
        hosDoctorSearchBean.setBranchId(getUserData().getUserBranch().getId());
        hosDoctorList = new ArrayList<>(0);
        hosDoctorSearchBean.setBranchId(getUserData().getUserBranch().getId());
        hosDoctorList = costCenterService.getAllSubHosDoctorByBranchIdActiveAndLevel(getUserData().getUserBranch().getId(), 1);

    }

    @Override
    public void reset() {
        hosDoctorSearchBean = new HosDoctorSearchBean();
        map = new TreeMap<>();
        parentWithChildsMap = new TreeMap<>();
    }

    @Override
    public void search() {
        increment = 0;
        costCenterIndexBeanList = new ArrayList<>(0);
        hosDoctorSearchedList = new ArrayList<>(0);
        clinicIndexReportBeanForReportList = new ArrayList<>(0);
        hosDoctorSearchBean.setBranchId(getUserData().getUserBranch().getId());
        //hosDoctorList = costCenterService.getAllSubHosDoctorByBranchIdActiveAndLevel(getUserData().getUserBranch().getId(), 1);
        hosDoctorSearchedList = costCenterService.getAllHosDoctorsIndex(hosDoctorSearchBean);
        if (hosDoctorSearchedList != null && !hosDoctorSearchedList.isEmpty()) {
            for (HosDoctor doctor : hosDoctorSearchedList) {
                increment++;
                ClinicIndexReportBean cirb = new ClinicIndexReportBean();
                cirb.setActive(doctor.getActive() != null ? doctor.getActive().toString() : null);
                cirb.setCode(Integer.parseInt(doctor.getCode()));
                cirb.setLevel(doctor.getLevel());
                cirb.setName(doctor.getName());
                cirb.setClinicIndexReportBeans(new ArrayList<>());
                parentWithChildsMap.put(doctor.getId(), cirb);
                map.put(increment, cirb);
                fillHeirarchy(doctor);
            }
        }
        clinicIndexReportBeanForReportList = new ArrayList<>(map.values());
        clinicIndexReportBeanList = new ArrayList<>(parentWithChildsMap.values());
    }

    public void fillHeirarchy(HosDoctor hosDoctors) {
        if (hosDoctors.getChildNodes() != null && !hosDoctors.getChildNodes().isEmpty()) {
            for (HosDoctor hosDoctor : hosDoctors.getChildNodes()) {
                increment++;
                if (hosDoctor.getLevel() == 3) {
                    ClinicIndexReportBean indexReportBean = parentWithChildsMap.get(hosDoctor.getParent().getId());

                    ClinicIndexReportBean cirb = new ClinicIndexReportBean();
                    cirb.setActive(hosDoctor.getActive() != null ? hosDoctor.getActive().toString() : null);
                    cirb.setCode(Integer.parseInt(hosDoctor.getCode()));
                    cirb.setLevel(hosDoctor.getLevel());
                    cirb.setName(hosDoctor.getName());
                    indexReportBean.getClinicIndexReportBeans().add(cirb);
                    parentWithChildsMap.put(hosDoctor.getParent().getId(), indexReportBean);
                    map.put(increment, cirb);
                    fillHeirarchy(hosDoctor);
                } else {
                    ClinicIndexReportBean cirb = new ClinicIndexReportBean();
                    cirb.setActive(hosDoctor.getActive() != null ? hosDoctor.getActive().toString() : null);
                    cirb.setCode(Integer.parseInt(hosDoctor.getCode()));
                    cirb.setLevel(hosDoctor.getLevel());
                    cirb.setName(hosDoctor.getName());
                    map.put(increment, cirb);
                    cirb.setClinicIndexReportBeans(new ArrayList<>());
                    parentWithChildsMap.put(hosDoctor.getId(), cirb);
                    fillHeirarchy(hosDoctor);
                }
            }

        }
    }

    @Override
    public HashMap prepareReport() {

        Map<String, String> userDDs = getUserData().getUserDDs();

        HashMap hashMap = new HashMap();

        hashMap.put("hosDoctorIdFrom", hosDoctorSearchBean.getHosDoctorIdFrom() != null ? hosDoctorSearchBean.getHosDoctorIdFrom().getCode() : null);
        hashMap.put("hosDoctorNameFrom", hosDoctorSearchBean.getHosDoctorIdFrom() != null ? hosDoctorSearchBean.getHosDoctorIdFrom().getName() : null);
        hashMap.put("hosDoctorIdTo", hosDoctorSearchBean.getHosDoctorIdTo() != null ? hosDoctorSearchBean.getHosDoctorIdTo().getCode() : null);
        hashMap.put("hosDoctorNameTo", hosDoctorSearchBean.getHosDoctorIdTo() != null ? hosDoctorSearchBean.getHosDoctorIdTo().getName() : null);
        hashMap.put("hosDoctorFromText", userDDs.get("FROM_CLINIC"));
        hashMap.put("hosDoctorToText", userDDs.get("TO_CLINIC"));
        hashMap.put("hosDoctorText", userDDs.get("NAME"));
        hashMap.put("codeText", userDDs.get("CS_CODE"));
        hashMap.put("unitNameText", userDDs.get("SINGLE_COST_CENTER"));
        hashMap.put("levelText", userDDs.get("LEVEL"));
        hashMap.put("shortCodeText", userDDs.get("SHORT_CODE"));
        hashMap.put("parentCostCenterText", userDDs.get("MAIN_COST_CENTER"));
        hashMap.put("accountStatusText", userDDs.get("ACCOUNT_STATUS"));
        hashMap.put("reportTitleText", userDDs.get("CLINICS_REPORT"));
        hashMap.put("PrintedBy", getUserData().getUser().getName());
        // hashMap.put("companyImage", getUserData().getImageReportPath());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (clinicIndexReportBeanForReportList != null && !clinicIndexReportBeanForReportList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "clinicIndexReport.jasper", clinicIndexReportBeanForReportList, "pdf");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد نتائج للطباعة"));
        }
    }

    @Override
    public void exportExcel(ActionEvent actionEvent) throws JRException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<HosDoctor> completeHosDoctor(String query) {
        List<HosDoctor> hosDoctors = hosDoctorList;
        if (query == null || query.trim().equals("")) {
            setHosDoctorConverter(new HosDoctorConverter(hosDoctors));
            return hosDoctors;
        }
        List<HosDoctor> filteredHosDoctor = new ArrayList<>();

        String nameAr;
        String code;
        HosDoctor hosDoctor;

        for (int i = 0; i < hosDoctorList.size(); i++) {
            hosDoctor = hosDoctors.get(i);
            nameAr = hosDoctor.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredHosDoctor.contains(hosDoctor)) {
                    filteredHosDoctor.add(hosDoctor);
                }
            }

            code = hosDoctor.getCode();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredHosDoctor.contains(hosDoctor)) {
                    filteredHosDoctor.add(hosDoctor);
                }
            }
        }

        setHosDoctorConverter(new HosDoctorConverter(filteredHosDoctor));
        return filteredHosDoctor;
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
     * @return the hosDoctorList
     */
    public List<HosDoctor> getHosDoctorList() {
        return hosDoctorList;
    }

    /**
     * @param hosDoctorList the hosDoctorList to set
     */
    public void setHosDoctorList(List<HosDoctor> hosDoctorList) {
        this.hosDoctorList = hosDoctorList;
    }

    /**
     * @return the costCenterIndexBeanList
     */
    public List<CostCenterIndexBean> getCostCenterIndexBeanList() {
        return costCenterIndexBeanList;
    }

    /**
     * @param costCenterIndexBeanList the costCenterIndexBeanList to set
     */
    public void setCostCenterIndexBeanList(List<CostCenterIndexBean> costCenterIndexBeanList) {
        this.costCenterIndexBeanList = costCenterIndexBeanList;
    }

    /**
     * @return the jasperPrint
     */
    public JasperPrint getJasperPrint() {
        return jasperPrint;
    }

    /**
     * @param jasperPrint the jasperPrint to set
     */
    public void setJasperPrint(JasperPrint jasperPrint) {
        this.jasperPrint = jasperPrint;
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
     * @return the clinicIndexReportBeanList
     */
    public List<ClinicIndexReportBean> getClinicIndexReportBeanList() {
        return clinicIndexReportBeanList;
    }

    /**
     * @param clinicIndexReportBeanList the clinicIndexReportBeanList to set
     */
    public void setClinicIndexReportBeanList(List<ClinicIndexReportBean> clinicIndexReportBeanList) {
        this.clinicIndexReportBeanList = clinicIndexReportBeanList;
    }

    /**
     * @return the map
     */
    public Map<Integer, ClinicIndexReportBean> getMap() {
        return map;
    }

    /**
     * @param map the map to set
     */
    public void setMap(Map<Integer, ClinicIndexReportBean> map) {
        this.map = map;
    }

    /**
     * @return the increment
     */
    public Integer getIncrement() {
        return increment;
    }

    /**
     * @param increment the increment to set
     */
    public void setIncrement(Integer increment) {
        this.increment = increment;
    }

    /**
     * @return the parentWithChildsMap
     */
    public Map<Integer, ClinicIndexReportBean> getParentWithChildsMap() {
        return parentWithChildsMap;
    }

    /**
     * @param parentWithChildsMap the parentWithChildsMap to set
     */
    public void setParentWithChildsMap(Map<Integer, ClinicIndexReportBean> parentWithChildsMap) {
        this.parentWithChildsMap = parentWithChildsMap;
    }

    /**
     * @return the hosDoctorSearchedList
     */
    public List<HosDoctor> getHosDoctorSearchedList() {
        return hosDoctorSearchedList;
    }

    /**
     * @param hosDoctorSearchedList the hosDoctorSearchedList to set
     */
    public void setHosDoctorSearchedList(List<HosDoctor> hosDoctorSearchedList) {
        this.hosDoctorSearchedList = hosDoctorSearchedList;
    }

    /**
     * @return the clinicIndexReportBeanForReportList
     */
    public List<ClinicIndexReportBean> getClinicIndexReportBeanForReportList() {
        return clinicIndexReportBeanForReportList;
    }

    /**
     * @param clinicIndexReportBeanForReportList the
     * clinicIndexReportBeanForReportList to set
     */
    public void setClinicIndexReportBeanForReportList(List<ClinicIndexReportBean> clinicIndexReportBeanForReportList) {
        this.clinicIndexReportBeanForReportList = clinicIndexReportBeanForReportList;
    }

}
