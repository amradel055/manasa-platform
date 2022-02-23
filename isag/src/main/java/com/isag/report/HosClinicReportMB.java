/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.HosDoctorService;
import com.isag.businessservice.reports.HosClinicViewService;
import com.isag.businessservice.reports.searchBean.HosDoctorSearchBean;
import com.isag.converter.HosDoctorConverter;
import com.isag.entity.HosDoctor;
import com.isag.isag.BaseReportBean;
import com.isag.isag.UserData;
import com.isag.report.entity.HosClinicReportBean;
import com.isag.views.HosClinicView;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
@Named("hosClinicReportMB")
@ViewScoped
public class HosClinicReportMB extends BaseReportBean {

    private List<HosDoctor> hosClinicsList;
    private List<HosClinicReportBean> hosClinicReportBeanList;
    private HosDoctorConverter hosClinicConverter;
    private HosDoctorSearchBean hosDoctorSearchBean;
    @EJB
    private HosDoctorService hosDoctorService;
    @EJB
    private HosClinicViewService hosClinicViewService;

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

    }

    @Override
    public void reset() {

        hosDoctorSearchBean = new HosDoctorSearchBean();
        hosDoctorSearchBean.setBranchId(getUserData().getUserBranch().getId());
    }

    @Override
    public void search() {
        hosClinicReportBeanList = new ArrayList<>();
        Map<Integer, HosClinicReportBean> hosCheckUpMap = new HashMap();
        Map<Integer, HosClinicReportBean> hosClinicMap = new HashMap();
        hosClinicMap.put(0, new HosClinicReportBean());
        Set<Integer> set = new HashSet<>();

        List<HosClinicView> hosClinicViewList = hosClinicViewService.getHosClinicViewBySearchBean(hosDoctorSearchBean);
        HosClinicReportBean clinicReportBean = new HosClinicReportBean();
        if (hosClinicViewList != null && !hosClinicViewList.isEmpty()) {
            for (HosClinicView hosClinicView : hosClinicViewList) {
                HosClinicReportBean hosClinicReportBean = new HosClinicReportBean();
                if (hosCheckUpMap.containsKey(hosClinicView.getHosCheckUpId())) {
                    hosClinicReportBean = hosCheckUpMap.get(hosClinicView.getHosCheckUpId());
                    fillEachRecord(hosClinicView, hosClinicReportBean);
                    hosCheckUpMap.put(hosClinicView.getHosCheckUpId(), hosClinicReportBean);

                    if (hosClinicMap.containsKey(hosClinicView.getHosClinicId())) {
                        hosClinicMap.put(hosClinicView.getHosClinicId(), hosClinicReportBean);
                    }
                } else {
                    fillEachRecord(hosClinicView, hosClinicReportBean);
                    hosClinicReportBean.setClinicName(hosClinicView.getClinicName());

                    hosCheckUpMap.put(hosClinicView.getHosCheckUpId(), hosClinicReportBean);

                    if (hosClinicMap.containsKey(hosClinicView.getHosClinicId())) {
                        clinicReportBean = hosClinicMap.get(hosClinicView.getHosClinicId());
                    } else {
                        clinicReportBean = hosClinicReportBean;
                    }
                    if (hosClinicView.getHosInsuranceId() != null) {
                        clinicReportBean.setNetCompanyCases(clinicReportBean.getNetCompanyCases() + 1);
                    } else {
                        clinicReportBean.setNetCachCases(clinicReportBean.getNetCachCases() + 1);
                    }
                    clinicReportBean.setCasesNumber(clinicReportBean.getCasesNumber() + 1);

                    if (hosClinicView.getHosDoctorId() != null && !set.contains(hosClinicView.getHosDoctorId())) {
                        clinicReportBean.setDocorsNumber(clinicReportBean.getDocorsNumber() + 1);
                        set.add(hosClinicView.getHosDoctorId());
                    }
                    if (hosClinicMap.containsKey(hosClinicView.getHosClinicId())) {
                        HosClinicReportBean reportBean = hosClinicMap.get(hosClinicView.getHosClinicId());
                        reportBean.setCardPaid(reportBean.getCardPaid().add(hosClinicReportBean.getCardPaid()));
                        reportBean.setCashPaid(reportBean.getCashPaid().add(hosClinicReportBean.getCashPaid()));
                        reportBean.setClincCompanyIncome(reportBean.getClincCompanyIncome().add(hosClinicReportBean.getClincCompanyIncome()));
                        reportBean.setNet(reportBean.getNet().add(hosClinicReportBean.getNet()));
                        reportBean.setClinicName(hosClinicView.getClinicName());
                        hosClinicMap.put(hosClinicView.getHosClinicId(), reportBean);
                    } else {
                        hosClinicMap.put(hosClinicView.getHosClinicId(), clinicReportBean);
                    }
                }
            }
            calculateTotals(hosClinicMap);
        }
        HosClinicReportBean bean = hosClinicMap.get(0);
        hosClinicMap.remove(0);
        hosClinicReportBeanList = new ArrayList<>(hosClinicMap.values());
        hosClinicReportBeanList.add(bean);
    }

    public void calculateTotals(Map<Integer, HosClinicReportBean> hosClinicMap) {
        HosClinicReportBean bean = hosClinicMap.get(0);
        bean.setClinicName("الاجمالي");
        for (Map.Entry<Integer, HosClinicReportBean> entry : hosClinicMap.entrySet()) {
            HosClinicReportBean hcrb = entry.getValue();
            bean.setCasesNumber(bean.getCasesNumber() + hcrb.getCasesNumber());
            bean.setNetCachCases(bean.getNetCachCases() + hcrb.getNetCachCases());
            bean.setNetCompanyCases(bean.getNetCompanyCases() + hcrb.getNetCompanyCases());
            bean.setCashPaid(bean.getCashPaid().add(hcrb.getCashPaid()));
            bean.setCardPaid(bean.getCardPaid().add(hcrb.getCardPaid()));
            bean.setClincCompanyIncome(bean.getClincCompanyIncome().add(hcrb.getClincCompanyIncome()));
            bean.setNet(bean.getNet().add(hcrb.getNet()));
            bean.setDocorsNumber(bean.getDocorsNumber() + hcrb.getDocorsNumber());
        }
    }

    public void fillEachRecord(HosClinicView hosClinicView, HosClinicReportBean hosClinicReportBean) {
        if (hosClinicView.getPaymentType() != null && hosClinicView.getPaymentType() == 1) {
            hosClinicReportBean.setCardPaid(hosClinicReportBean.getCardPaid().add(hosClinicView.getTotal()));
            hosClinicReportBean.setCashPaid(BigDecimal.ZERO);
        } else {
            hosClinicReportBean.setCashPaid(hosClinicReportBean.getCashPaid().add(hosClinicView.getTotal()));
            hosClinicReportBean.setCardPaid(BigDecimal.ZERO);
        }
        BigDecimal totalClinicCompanyIncome = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal hundred = BigDecimal.valueOf(100);
        if (hosClinicView.getHosInsuranceId() != null) {
            totalClinicCompanyIncome = hosClinicView.getValue() != null ? hosClinicView.getValue().setScale(2, BigDecimal.ROUND_UP) : BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_UP);
            totalClinicCompanyIncome = totalClinicCompanyIncome.subtract((totalClinicCompanyIncome.multiply(hosClinicView.getDiscountRatio().divide(hundred.abs(), 4, 4))));
            total = totalClinicCompanyIncome.multiply(hosClinicView.getBearRatio().divide(hundred.abs(), 4, 4));
            totalClinicCompanyIncome = totalClinicCompanyIncome.subtract(total);
            totalClinicCompanyIncome = totalClinicCompanyIncome.setScale(2, BigDecimal.ROUND_UP);
            hosClinicReportBean.setClincCompanyIncome(totalClinicCompanyIncome);
        }
        hosClinicReportBean.setNet(hosClinicReportBean.getClincCompanyIncome().add(hosClinicReportBean.getCardPaid()).add(hosClinicReportBean.getCashPaid()));
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
        hashMap.put("clinicNameText", userDDs.get("CLINIC_NAME"));
        hashMap.put("casesNumberText", userDDs.get("NO_OF_CASES"));
        hashMap.put("netCachCasesText", userDDs.get("NET_CASH_CASES"));
        hashMap.put("netCompanyCasesText", userDDs.get("NET_COMP_CASES"));
        hashMap.put("cashPaidText", userDDs.get("CASH_PAID"));
        hashMap.put("cardPaidText", userDDs.get("PAID_BY_CARD"));
        hashMap.put("clincCompanyIncomeText", userDDs.get("CLI_REVE_FR_COMPA"));
        hashMap.put("netText", userDDs.get("NET"));
        hashMap.put("docorsNumberText", userDDs.get("NO_OF_DOC"));
        hashMap.put("yearFromText", userDDs.get("YEAR_FROM"));
        hashMap.put("yearToText", userDDs.get("YEAR_TO"));
        hashMap.put("clinicFromText", userDDs.get("FROM_CLINIC"));
        hashMap.put("clinicToText", userDDs.get("TO_CLINIC"));
        hashMap.put("totalText", userDDs.get("TOTAL"));
        hashMap.put("reportTitleText", userDDs.get("CLINICS_REPORT"));
        hashMap.put("yearFromValue", hosDoctorSearchBean.getDateFrom());
        hashMap.put("yearToValue", hosDoctorSearchBean.getDateTo());
        hashMap.put("clinicFromValue", hosDoctorSearchBean.getHosClinicIdFrom() != null ? hosDoctorSearchBean.getHosClinicIdFrom().getName() : null);
        hashMap.put("clinicToValue", hosDoctorSearchBean.getHosClinicIdTo() != null ? hosDoctorSearchBean.getHosClinicIdTo().getName() : null);
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        search();
        if (hosClinicReportBeanList != null && !hosClinicReportBeanList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "hosClinicReportLandscape.jasper", hosClinicReportBeanList, "pdf");
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
     * @return the hosClinicReportBeanList
     */
    public List<HosClinicReportBean> getHosClinicReportBeanList() {
        return hosClinicReportBeanList;
    }

    /**
     * @param hosClinicReportBeanList the hosClinicReportBeanList to set
     */
    public void setHosClinicReportBeanList(List<HosClinicReportBean> hosClinicReportBeanList) {
        this.hosClinicReportBeanList = hosClinicReportBeanList;
    }

}
