/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.InvNoticeService;
import com.isag.dto.InvNoticeDTO;

import com.isag.isag.BaseReportBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author hhhh
 */
@Named("invNoticeReportMB")
@ViewScoped
public class InvNoticeReportMB extends BaseReportBean {

    // Objects
    private List<InvNoticeDTO> noticeList = new ArrayList<>();
    private Date dateFrom;
    private Date dateTo;
    private Integer noticeFrom;
    private Integer noticeTo;
    private Integer type;

    @EJB
    private InvNoticeService invNoticeService;

    @Override
    @PostConstruct
    public void load() {

    }

    @Override
    public void reset() {
        noticeList = new ArrayList<>();
        dateFrom = null;
        dateTo = null;
        setNoticeFrom(null);
        setNoticeTo(null);
    }

    public void showItemReport() {

    }

    @Override
    public void search() {
        setNoticeList(invNoticeService.getInvNoticeListDTO(getUserData().getUserBranch().getId(), type, dateFrom, dateTo, getNoticeFrom(), getNoticeTo()));
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        fillReport(prepareReport(), getUserData().getReportPath() + "invNoticeReport.jasper", getNoticeList(), "pdf");
    }

    @Override
    public HashMap prepareReport() {
        HashMap hashMap = new HashMap();
        reportManagement(hashMap);
        hashMap.put("reportNameText", "تقارير الاشعارات");
        hashMap.put("code", "الكود");
        hashMap.put("accountNumber", "رمز الحساب");
        hashMap.put("organizationSiteName", "العميل المقابل");
        hashMap.put("date", "التاريخ");
        hashMap.put("value", "المبلغ");
        hashMap.put("noticeName", "مسمي الاشعار");
        hashMap.put("remark", "ملاحظات");

        hashMap.put("noticeFromText", "من رقم اشعار");
        hashMap.put("noticeToText", "إلى رقم اشعار");
        hashMap.put("dateFromText", "من تاريخ");
        hashMap.put("dateToText", "الي تاريخ");
        hashMap.put("noticeNameText", "نوع الاشعار");

        hashMap.put("noticeFromValue", noticeFrom);
        hashMap.put("noticeToValue", noticeTo);
        hashMap.put("dateFromValue", dateFrom);
        hashMap.put("dateToValue", dateTo);
        String noticeName = null;
        if (type == 0) {
            noticeName = "اشعار اضافة للعميل";
        } else if (type == 1) {
            noticeName = "اشعار خصم للعميل";
        } else if (type == 2) {
            noticeName = "اشعار اضافة للمورد";
        } else if (type == 3) {
            noticeName = "اشعار خصم للمورد";
        }
        hashMap.put("noticeNameValue", noticeName);
        return hashMap;
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
     * @return the noticeList
     */
    public List<InvNoticeDTO> getNoticeList() {
        return noticeList;
    }

    /**
     * @param noticeList the noticeList to set
     */
    public void setNoticeList(List<InvNoticeDTO> noticeList) {
        this.noticeList = noticeList;
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
     * @return the noticeFrom
     */
    public Integer getNoticeFrom() {
        return noticeFrom;
    }

    /**
     * @param noticeFrom the noticeFrom to set
     */
    public void setNoticeFrom(Integer noticeFrom) {
        this.noticeFrom = noticeFrom;
    }

    /**
     * @return the noticeTo
     */
    public Integer getNoticeTo() {
        return noticeTo;
    }

    /**
     * @param noticeTo the noticeTo to set
     */
    public void setNoticeTo(Integer noticeTo) {
        this.noticeTo = noticeTo;
    }

}
