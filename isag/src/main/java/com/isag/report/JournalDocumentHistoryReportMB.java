package com.isag.report;

import com.isag.businessservice.HistoryGeneraljournalDetailsService;
import com.isag.businessservice.HistoryGeneraljournalService;
import com.isag.businessservice.reports.searchBean.JournalDocumentsReportSearchBean;
import com.isag.entity.HistoryGeneralJournal;
import com.isag.isag.BaseGlAccountReportBean;
import com.isag.isag.UserData;
import com.isag.report.entity.JournalDocumentHistoryReportBean;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ManagedBean(name = "journalDocumentHistoryReportMB")
public class JournalDocumentHistoryReportMB extends BaseGlAccountReportBean {

    private List<HistoryGeneralJournal> historyGeneralJournalList;
    private JournalDocumentsReportSearchBean journalDocumentsReportSearchBean;
    private List<JournalDocumentHistoryReportBean> documentHistoryReportBeanList;
    private UserData userData;
    private JasperPrint jasperPrint;
    @EJB
    HistoryGeneraljournalService historyGeneraljournalService;
    @EJB
    HistoryGeneraljournalDetailsService historyGeneraljournalDetailsService;

    public JournalDocumentHistoryReportMB() {
    }

    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        reset();
    }

    public void reset() {
        setHistoryGeneralJournalList(new ArrayList<HistoryGeneralJournal>());
        setJournalDocumentsReportSearchBean(new JournalDocumentsReportSearchBean());
        getJournalDocumentsReportSearchBean().setBranchId(userData.getSelectedBranch());
    }

    public void search() {
//        setGeneralJournalList(generaljournalService.getALLGeneralJournalDocumentsReport(getJournalDocumentsReportSearchBean()));
//        List<GeneralJournalDetails> generalJournalDetailsList = new ArrayList<>(0);
//        setJournalDocumentReportBeanList(new ArrayList<>(0));
//        for (GeneralJournal generalJournal : getGeneralJournalList()) {
//            BigDecimal totalDebit = BigDecimal.ZERO;
//            BigDecimal totalCredit = BigDecimal.ZERO;
//            JournalDocumentReportBean journalDocumentReportBean = new JournalDocumentReportBean();
//            journalDocumentReportBean.setJournalNum(generalJournal.getId());
//            journalDocumentReportBean.setDocumentNum(generalJournal.getGeneralDecument());
//            journalDocumentReportBean.setJournalDate(generalJournal.getGeneralData());
//            journalDocumentReportBean.setUser(generalJournal.getCreatedBy().getName());
//            generalJournalDetailsList = generaljournalDetailsService.getAllJournalDetailsForJorunal(generalJournal.getId());
//            for (GeneralJournalDetails generalJournalDetails : generalJournalDetailsList) {
//                totalDebit = totalDebit.add(generalJournalDetails.getDebitAmount() == null ? BigDecimal.ZERO : generalJournalDetails.getDebitAmount());
//                totalCredit = totalCredit.add(generalJournalDetails.getCreditamount() == null ? BigDecimal.ZERO : generalJournalDetails.getCreditamount());
//            }
//            if (totalDebit.compareTo(totalCredit) == 0)
//                journalDocumentReportBean.setPosting("موزون");
//            else
//                journalDocumentReportBean.setPosting("غير موزون");
//            if (generalJournal.isPost_flag()) {
//                journalDocumentReportBean.setPostFlag("روجع");
//            } else {
//                journalDocumentReportBean.setPostFlag("لم يراجع");
//            }
//            journalDocumentReportBean.setAmount(totalDebit);
//            journalDocumentReportBean.setRemarks(generalJournal.getGeneralStatement());
//            getJournalDocumentReportBeanList().add(journalDocumentReportBean);
//        }
//        if (getJournalDocumentsReportSearchBean().getPosting() != null && !journalDocumentsReportSearchBean.getPosting().equals("")) {
//            List<JournalDocumentReportBean> journalDocumentReportBeanListFilter = new ArrayList<>(0);
//            for (JournalDocumentReportBean journalDocumentReportBean : getJournalDocumentReportBeanList()) {
//                if (journalDocumentReportBean.getPosting().equals(getJournalDocumentsReportSearchBean().getPosting())) {
//                    journalDocumentReportBeanListFilter.add(journalDocumentReportBean);
//                }
//            }
//            setJournalDocumentReportBeanList(journalDocumentReportBeanListFilter);
//        }

    }

    public void print(ActionEvent actionEvent) throws JRException, IOException {
                    fillReport(prepareReport(), getUserData().getReportPath() + "instrumentSummary.jasper", getHistoryGeneralJournalList(), "pdf");
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public JournalDocumentsReportSearchBean getJournalDocumentsReportSearchBean() {
        return journalDocumentsReportSearchBean;
    }

    public void setJournalDocumentsReportSearchBean(JournalDocumentsReportSearchBean journalDocumentsReportSearchBean) {
        this.journalDocumentsReportSearchBean = journalDocumentsReportSearchBean;
    }

    /**
     * @return the documentHistoryReportBeanList
     */
    public List<JournalDocumentHistoryReportBean> getDocumentHistoryReportBeanList() {
        return documentHistoryReportBeanList;
    }

    /**
     * @param documentHistoryReportBeanList the documentHistoryReportBeanList to
     * set
     */
    public void setDocumentHistoryReportBeanList(List<JournalDocumentHistoryReportBean> documentHistoryReportBeanList) {
        this.documentHistoryReportBeanList = documentHistoryReportBeanList;
    }

    /**
     * @return the historyGeneralJournalList
     */
    public List<HistoryGeneralJournal> getHistoryGeneralJournalList() {
        return historyGeneralJournalList;
    }

    /**
     * @param historyGeneralJournalList the historyGeneralJournalList to set
     */
    public void setHistoryGeneralJournalList(List<HistoryGeneralJournal> historyGeneralJournalList) {
        this.historyGeneralJournalList = historyGeneralJournalList;
    }

    @Override
    public void checkDate(Boolean dateType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetDateFrom() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetDateTo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HashMap prepareReport() {
        String x = "3mr";
        HashMap hashMap = new HashMap();
        hashMap.put("name1", x);
        hashMap.put("journalNumFrom", getJournalDocumentsReportSearchBean().getJournalNumForm());
        hashMap.put("journalNumTo", getJournalDocumentsReportSearchBean().getJournalNumTo());
        hashMap.put("postFlag", getJournalDocumentsReportSearchBean().getPostFlag());
        hashMap.put("posting", getJournalDocumentsReportSearchBean().getPosting());
        hashMap.put("documentNumFrom", getJournalDocumentsReportSearchBean().getDocumentNumForm());
        hashMap.put("documentNumTo", getJournalDocumentsReportSearchBean().getDocumentNumTo());
        hashMap.put("documentTypeFrom", getJournalDocumentsReportSearchBean().getDocumentTypeFrom());
        hashMap.put("documentTypeTo", getJournalDocumentsReportSearchBean().getDocumentTypeTo());
        hashMap.put("userFrom", getJournalDocumentsReportSearchBean().getUserFrom());
        hashMap.put("userTo", getJournalDocumentsReportSearchBean().getUserTo());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
}
