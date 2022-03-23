package com.isag.report;

import com.isag.businessservice.CostCenterService;
import com.isag.businessservice.GeneralSymbolService;
import com.isag.businessservice.GeneraljournalDetailsService;
import com.isag.businessservice.GeneraljournalService;
import com.isag.businessservice.GlAccountService;
import com.isag.businessservice.GlAdminUnitService;
import com.isag.businessservice.reports.searchBean.JournalDocumentsReportSearchBean;
import com.isag.converter.CostCenterConverter;
import com.isag.converter.GlAdminUnitConverter;
import com.isag.converter.GlaccountConverter;
import com.isag.entity.CostCenter;
import com.isag.entity.GeneralJournal;
import com.isag.entity.GeneralJournalDetails;
import com.isag.entity.GlAccount;
import com.isag.entity.GlAdminUnit;
import com.isag.entity.Symbol;
import com.isag.isag.BaseGlAccountReportBean;
import com.isag.report.entity.JournalDocumentArrangedReportBean;
import com.isag.report.entity.JournalDocumentDailyReportBean;
import com.isag.report.entity.JournalDocumentReportBean;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.servlet.http.HttpServletRequest;

public class JournalDocumentReportMB extends BaseGlAccountReportBean {

    private List<GeneralJournal> generalJournalList;
    private JournalDocumentsReportSearchBean journalDocumentsReportSearchBean;
    private JournalDocumentReportBean journalDocumentReportBean;
    private List<JournalDocumentReportBean> journalDocumentReportBeanList;
    private List<JournalDocumentReportBean> journalDocumentReportBeanForPrintList;
    private List<JournalDocumentArrangedReportBean> journalDocumentArrangedReportBeanList;
    private JournalDocumentArrangedReportBean journalDocumentArrangedReportBean;
    private List<JournalDocumentDailyReportBean> journalDocumentDailyReportBeanList;
    private JournalDocumentDailyReportBean journalDocumentDailyReportBean;
    private String url;
    private List<Symbol> symbolsList;
    private HashMap hashMap;
    private JasperPrint jasperPrint;
    private Integer i;
    private GlaccountConverter accountConverter;
    private CostCenterConverter costCenterConverter;
    private GlAdminUnitConverter glAdminUnitConverter;
    private CostCenter costCenterToSelected;
    private CostCenter costCenterFromSelected;
    private GlAdminUnit glAdminUnitToSelected;
    private GlAdminUnit glAdminUnitFromSelected;
    private GlAccount glAccountSelectedTo;
    private GlAccount glAccountSelectedFrom;
    private Map<Integer, Symbol> symbolsMap = new HashMap<>();

    @EJB
    GeneraljournalService generaljournalService;
    @EJB
    GeneraljournalDetailsService generaljournalDetailsService;

    @EJB
    GlAccountService accountService;
    @EJB
    CostCenterService costCenterService;
    @EJB
    GlAdminUnitService adminUnitService;
    @EJB
    private GeneralSymbolService generalSymbolService;

    public JournalDocumentReportMB() {
    }

    @PostConstruct
    public void init() {
        if (getGlYearSelection() != null) {

            reset();

            resetDateFrom();
            resetDateTo();
        } else {
            redirectFinancailYearPage();
        }

        journalDocumentDailyReportBean = new JournalDocumentDailyReportBean();

        journalDocumentDailyReportBeanList = new ArrayList<>();

        symbolsList = new ArrayList<>();

        setSymbolsList(generalSymbolService.getSymbolsByCompanyId(getUserData().getCompany().getId()));
        symbolsMap = new HashMap<>();
        if (symbolsList != null && !symbolsList.isEmpty()) {
            for (Symbol sym : symbolsList) {
                symbolsMap.put(sym.getId(), sym);
            }
        }
        setGlAccountList(accountService.getBranchGLAccountsActive(getSelectedBranch()));
        setCostCenterList(costCenterService.getActiveSubCostCentersByBranch(getSelectedBranch()));
        setGlAdminUnitList(adminUnitService.getAllAdminUnitByBranchIdActive(getSelectedBranch()));

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        url = request.getRequestURL().toString();

        System.out.println("url: " + url);
        costCenterConverter = new CostCenterConverter(getCostCenterList());
        glAdminUnitConverter = new GlAdminUnitConverter(getGlAdminUnitList());
        accountConverter = new GlaccountConverter(getGlAccountList());
    }

    @Override
    public void reset() {
        generalJournalList = new ArrayList<>(0);
        journalDocumentReportBeanList = new ArrayList<>(0);
        journalDocumentsReportSearchBean = new JournalDocumentsReportSearchBean();
        journalDocumentsReportSearchBean.setBranchId(getUserData().getSelectedBranch());
        resetDateFrom();
        resetDateTo();
        costCenterToSelected = null;
        costCenterFromSelected = null;
        glAdminUnitFromSelected = null;
        glAdminUnitToSelected = null;
        glAccountSelectedTo = null;
        glAccountSelectedFrom = null;
    }

    @Override
    public void search() {
        i = 0;
        journalDocumentArrangedReportBeanList = new ArrayList<>();
        generalJournalList = generaljournalService.getALLGeneralJournalDocumentsReport(journalDocumentsReportSearchBean);
        List<GeneralJournalDetails> generalJournalDetailsList = new ArrayList<>(0);
        journalDocumentReportBeanList = new ArrayList<>(0);
        for (GeneralJournal generalJournal : generalJournalList) {
            BigDecimal totalDebit = BigDecimal.ZERO;
            BigDecimal totalCredit = BigDecimal.ZERO;
            journalDocumentReportBean = new JournalDocumentReportBean();
            journalDocumentReportBean.setJournalNum(generalJournal.getSerial());
            journalDocumentReportBean.setDocumentNum(generalJournal.getGeneralDecument());
            journalDocumentReportBean.setJournalDate(generalJournal.getGeneralData());
            journalDocumentReportBean.setUser(generalJournal.getCreatedBy().getName());
            journalDocumentArrangedReportBean = new JournalDocumentArrangedReportBean();
            journalDocumentArrangedReportBean.setJournalNum(generalJournal.getSerial());
            journalDocumentArrangedReportBean.setDocumentNum(generalJournal.getGeneralDecument());
            journalDocumentArrangedReportBean.setJournalDate(generalJournal.getGeneralData());
            journalDocumentArrangedReportBean.setUser(generalJournal.getCreatedBy().getName());
            generalJournalDetailsList = generaljournalDetailsService.getAllJournalDetailsForJorunalBySearchBean(generalJournal.getId(), journalDocumentsReportSearchBean);
            for (GeneralJournalDetails generalJournalDetails : generalJournalDetailsList) {
                totalDebit = totalDebit.add(generalJournalDetails.getDebitAmount() == null ? BigDecimal.ZERO : generalJournalDetails.getDebitAmount());
                totalCredit = totalCredit.add(generalJournalDetails.getCreditamount() == null ? BigDecimal.ZERO : generalJournalDetails.getCreditamount());
            }
            if (totalDebit.compareTo(totalCredit) == 0) {
                journalDocumentReportBean.setPosting("موزون");
                journalDocumentArrangedReportBean.setPosting("موزون");
            } else {
                journalDocumentReportBean.setPosting("غير موزون");
                journalDocumentArrangedReportBean.setPosting("موزون");
            }
            if (generalJournal.isPost_flag()) {
                journalDocumentReportBean.setPostFlag("روجع");
                journalDocumentArrangedReportBean.setPosting("موزون");

            } else {
                journalDocumentReportBean.setPostFlag("لم يراجع");
                journalDocumentArrangedReportBean.setPosting("موزون");
            }
            journalDocumentReportBean.setAmount(totalDebit);
            journalDocumentReportBean.setRemarks(generalJournal.getGeneralStatement());
            journalDocumentArrangedReportBean.setAmount(totalDebit);
            journalDocumentArrangedReportBean.setRemarks(generalJournal.getGeneralStatement());

            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String url = request.getRequestURL().toString();

            if (url.contains("journalDocumentDailyReport.xhtml")) {
                journalDocumentReportBean.setJournalDocumentDailyReportBeanList(new ArrayList<JournalDocumentDailyReportBean>());
                for (GeneralJournalDetails generalJournalDetails : generalJournalDetailsList) {
                    journalDocumentDailyReportBean = new JournalDocumentDailyReportBean();
                    journalDocumentDailyReportBean.setAccountName(generalJournalDetails.getGlACCOUNTId() != null ? (generalJournalDetails.getGlACCOUNTId().getName() != null ? generalJournalDetails.getGlACCOUNTId().getName() : null) : null);
                    journalDocumentDailyReportBean.setAccountNumber(generalJournalDetails.getGlACCOUNTId() != null ? (generalJournalDetails.getGlACCOUNTId().getAccNumber() != null ? generalJournalDetails.getGlACCOUNTId().getAccNumber() : null) : null);
                    journalDocumentDailyReportBean.setAdminUnitName(generalJournalDetails.getAdminUnitId() != null ? (generalJournalDetails.getAdminUnitId().getName() != null ? generalJournalDetails.getAdminUnitId().getName() : null) : null);
                    journalDocumentDailyReportBean.setCostCenterName(generalJournalDetails.getCostCenterId() != null ? (generalJournalDetails.getCostCenterId().getName() != null ? generalJournalDetails.getCostCenterId().getName() : null) : null);
                    journalDocumentDailyReportBean.setCreditAmount(generalJournalDetails.getCreditamount() != null ? generalJournalDetails.getCreditamount() : BigDecimal.ZERO);
                    journalDocumentDailyReportBean.setDebitAmount(generalJournalDetails.getDebitAmount() != null ? generalJournalDetails.getDebitAmount() : BigDecimal.ZERO);
                    journalDocumentDailyReportBean.setDiscribtion(generalJournalDetails.getDiscribtion() != null ? generalJournalDetails.getDiscribtion() : null);
                    journalDocumentArrangedReportBean.setAccountName(generalJournalDetails.getGlACCOUNTId() != null ? (generalJournalDetails.getGlACCOUNTId().getName() != null ? generalJournalDetails.getGlACCOUNTId().getName() : null) : null);
                    journalDocumentArrangedReportBean.setAccountNumber(generalJournalDetails.getGlACCOUNTId() != null ? (generalJournalDetails.getGlACCOUNTId().getAccNumber() != null ? generalJournalDetails.getGlACCOUNTId().getAccNumber() : null) : null);
                    journalDocumentArrangedReportBean.setAdminUnitName(generalJournalDetails.getAdminUnitId() != null ? (generalJournalDetails.getAdminUnitId().getName() != null ? generalJournalDetails.getAdminUnitId().getName() : null) : null);
                    journalDocumentArrangedReportBean.setCostCenterName(generalJournalDetails.getCostCenterId() != null ? (generalJournalDetails.getCostCenterId().getName() != null ? generalJournalDetails.getCostCenterId().getName() : null) : null);
                    journalDocumentArrangedReportBean.setCreditAmount(generalJournalDetails.getCreditamount() != null ? generalJournalDetails.getCreditamount() : BigDecimal.ZERO);
                    journalDocumentArrangedReportBean.setDebitAmount(generalJournalDetails.getDebitAmount() != null ? generalJournalDetails.getDebitAmount() : BigDecimal.ZERO);
                    journalDocumentArrangedReportBean.setDiscribtion(generalJournalDetails.getDiscribtion() != null ? generalJournalDetails.getDiscribtion() : null);
                    journalDocumentArrangedReportBeanList.add(journalDocumentArrangedReportBean);
                    journalDocumentArrangedReportBean = new JournalDocumentArrangedReportBean();
                    journalDocumentReportBean.getJournalDocumentDailyReportBeanList().add(journalDocumentDailyReportBean);
                }
            }
            journalDocumentReportBeanList.add(journalDocumentReportBean);
        }
        if (journalDocumentsReportSearchBean.getPosting() != null && !journalDocumentsReportSearchBean.getPosting().equals("")) {
            List<JournalDocumentReportBean> journalDocumentReportBeanListFilter = new ArrayList<>(0);
            for (JournalDocumentReportBean journalDocumentReportBean : journalDocumentReportBeanList) {
                if (journalDocumentReportBean.getPosting().equals(journalDocumentsReportSearchBean.getPosting())) {
                    journalDocumentReportBeanListFilter.add(journalDocumentReportBean);
                }
            }
            journalDocumentReportBeanList = journalDocumentReportBeanListFilter;
        }
    }

    public void search2() {
        i = 0;
        journalDocumentArrangedReportBeanList = new ArrayList<>();
        journalDocumentsReportSearchBean.setGlAccountFrom(glAccountSelectedFrom != null ? glAccountSelectedFrom.getId() : null);
        journalDocumentsReportSearchBean.setGlAccountTo(glAccountSelectedTo != null ? glAccountSelectedTo.getId() : null);
        journalDocumentsReportSearchBean.setCostCenterFrom(costCenterFromSelected != null ? costCenterFromSelected.getId() : null);
        journalDocumentsReportSearchBean.setCostCenterTo(costCenterToSelected != null ? costCenterToSelected.getId() : null);
        journalDocumentsReportSearchBean.setAdminUnitFrom(glAdminUnitFromSelected != null ? glAdminUnitFromSelected.getId() : null);
        journalDocumentsReportSearchBean.setAdminUnitTo(glAdminUnitToSelected != null ? glAdminUnitToSelected.getId() : null);
        
        generalJournalList = generaljournalService.getALLGeneralJournalDocumentsReport(journalDocumentsReportSearchBean);
        List<GeneralJournalDetails> generalJournalDetailsList = new ArrayList<>(0);
        journalDocumentReportBeanList = new ArrayList<>(0);

        for (GeneralJournal generalJournal : generalJournalList) {
            BigDecimal totalDebit = BigDecimal.ZERO;
            BigDecimal totalCredit = BigDecimal.ZERO;
            journalDocumentReportBean = new JournalDocumentReportBean();
            journalDocumentReportBean.setJournalNum(generalJournal.getSerial() != null ? generalJournal.getSerial() : 0);
            journalDocumentReportBean.setDocumentNum(generalJournal.getGeneralDecument() != null ? generalJournal.getGeneralDecument() : 0);
            journalDocumentReportBean.setJournalDate(generalJournal.getGeneralData() != null ? generalJournal.getGeneralData() : null);
            journalDocumentReportBean.setUser(generalJournal.getCreatedBy() != null ? generalJournal.getCreatedBy().getName() : null);
            journalDocumentReportBean.setGeneralTypeName(generalJournal.getGeneralType() != null ? generalJournal.getGeneralType().getName() : null);
            generalJournalDetailsList = generaljournalDetailsService.getAllJournalDetailsForJorunalBySearchBean(generalJournal.getId(), journalDocumentsReportSearchBean);
            for (GeneralJournalDetails generalJournalDetails : generalJournalDetailsList) {
                totalDebit = totalDebit.add(generalJournalDetails.getDebitAmountLocal()== null ? BigDecimal.ZERO : generalJournalDetails.getDebitAmountLocal());
                totalCredit = totalCredit.add(generalJournalDetails.getCreditamountLocal()== null ? BigDecimal.ZERO : generalJournalDetails.getCreditamountLocal());
            }

            if (totalDebit.compareTo(totalCredit) == 0) {
                journalDocumentReportBean.setPosting("موزون");
            } else {
                journalDocumentReportBean.setPosting("غير موزون");
            }
            if (generalJournal.isPost_flag()) {
                journalDocumentReportBean.setPostFlag("روجع");
            } else {
                journalDocumentReportBean.setPostFlag("لم يراجع");
            }

            journalDocumentReportBean.setAmount(totalDebit);
            journalDocumentReportBean.setRemarks(generalJournal.getGeneralStatement());

            if (url.contains("journalDocumentDailyReport.xhtml") || url.contains("journalDocumentsReport.xhtml")) {
                journalDocumentReportBean.setJournalDocumentDailyReportBeanList(new ArrayList<JournalDocumentDailyReportBean>());
                for (GeneralJournalDetails generalJournalDetails : generalJournalDetailsList) {
                    journalDocumentDailyReportBean = new JournalDocumentDailyReportBean();
                    journalDocumentArrangedReportBean = new JournalDocumentArrangedReportBean();
                    setHeaderDate(journalDocumentArrangedReportBean, generalJournal, totalDebit, totalCredit);
                    journalDocumentDailyReportBean.setAccountName(generalJournalDetails.getGlACCOUNTId() != null ? (generalJournalDetails.getGlACCOUNTId().getName() != null ? generalJournalDetails.getGlACCOUNTId().getName() : null) : null);
                    journalDocumentDailyReportBean.setAccountNumber(generalJournalDetails.getGlACCOUNTId() != null ? (generalJournalDetails.getGlACCOUNTId().getAccNumber() != null ? generalJournalDetails.getGlACCOUNTId().getAccNumber() : null) : null);
                    journalDocumentDailyReportBean.setAdminUnitName(generalJournalDetails.getAdminUnitId() != null ? (generalJournalDetails.getAdminUnitId().getName() != null ? generalJournalDetails.getAdminUnitId().getName() : null) : null);
                    journalDocumentDailyReportBean.setCostCenterName(generalJournalDetails.getCostCenterId() != null ? (generalJournalDetails.getCostCenterId().getName() != null ? generalJournalDetails.getCostCenterId().getName() : null) : null);
                    journalDocumentDailyReportBean.setCreditAmount(generalJournalDetails.getCreditamountLocal()!= null ? generalJournalDetails.getCreditamountLocal(): BigDecimal.ZERO);
                    journalDocumentDailyReportBean.setDebitAmount(generalJournalDetails.getDebitAmountLocal()!= null ? generalJournalDetails.getDebitAmountLocal(): BigDecimal.ZERO);
                    journalDocumentDailyReportBean.setDiscribtion(generalJournalDetails.getDiscribtion() != null ? generalJournalDetails.getDiscribtion() : null);
                    journalDocumentArrangedReportBean.setAccountName(generalJournalDetails.getGlACCOUNTId() != null ? (generalJournalDetails.getGlACCOUNTId().getName() != null ? generalJournalDetails.getGlACCOUNTId().getName() : null) : null);
                    journalDocumentArrangedReportBean.setAccountNumber(generalJournalDetails.getGlACCOUNTId() != null ? (generalJournalDetails.getGlACCOUNTId().getAccNumber() != null ? generalJournalDetails.getGlACCOUNTId().getAccNumber() : null) : null);
                    journalDocumentArrangedReportBean.setAdminUnitName(generalJournalDetails.getAdminUnitId() != null ? (generalJournalDetails.getAdminUnitId().getName() != null ? generalJournalDetails.getAdminUnitId().getName() : null) : null);
                    journalDocumentArrangedReportBean.setCostCenterName(generalJournalDetails.getCostCenterId() != null ? (generalJournalDetails.getCostCenterId().getName() != null ? generalJournalDetails.getCostCenterId().getName() : null) : null);
                    journalDocumentArrangedReportBean.setCreditAmount(generalJournalDetails.getCreditamountLocal()!= null ? generalJournalDetails.getCreditamountLocal(): BigDecimal.ZERO);
                    journalDocumentArrangedReportBean.setDebitAmount(generalJournalDetails.getDebitAmountLocal()!= null ? generalJournalDetails.getDebitAmountLocal(): BigDecimal.ZERO);
                    journalDocumentArrangedReportBean.setDiscribtion(generalJournalDetails.getDiscribtion() != null ? generalJournalDetails.getDiscribtion() : null);
                    journalDocumentArrangedReportBean.setCreationDate(generalJournalDetails.getCreationDate() != null ? generalJournalDetails.getCreationDate() : null);
                    journalDocumentArrangedReportBeanList.add(journalDocumentArrangedReportBean);

                    journalDocumentReportBean.getJournalDocumentDailyReportBeanList().add(journalDocumentDailyReportBean);
                }
            }
            journalDocumentReportBeanList.add(journalDocumentReportBean);
        }
        if (journalDocumentsReportSearchBean.getPosting() != null && !journalDocumentsReportSearchBean.getPosting().equals("")) {
            List<JournalDocumentReportBean> journalDocumentReportBeanListFilter = new ArrayList<>(0);
            for (JournalDocumentReportBean journalDocumentReportBean : journalDocumentReportBeanList) {
                if (journalDocumentReportBean.getPosting().equals(journalDocumentsReportSearchBean.getPosting())) {
                    journalDocumentReportBeanListFilter.add(journalDocumentReportBean);
                }
            }
            journalDocumentReportBeanList = journalDocumentReportBeanListFilter;
        }
    }

    public void setHeaderDate(JournalDocumentArrangedReportBean journalDocumentArrangedReportBean, GeneralJournal generalJournal, BigDecimal totalDebit, BigDecimal totalCredit) {
        journalDocumentArrangedReportBean.setJournalNum(generalJournal.getSerial());
        journalDocumentArrangedReportBean.setDocumentNum(generalJournal.getGeneralDecument());
        journalDocumentArrangedReportBean.setJournalDate(generalJournal.getGeneralData());
        journalDocumentArrangedReportBean.setUser(generalJournal.getCreatedBy().getName());
        journalDocumentArrangedReportBean.setGeneralType(generalJournal.getGeneralType() != null ? generalJournal.getGeneralType().getName() : null);

        if (totalDebit.compareTo(totalCredit) == 0) {
            journalDocumentArrangedReportBean.setPosting("موزون");
        } else {
            journalDocumentArrangedReportBean.setPosting("غير موزون");
        }
        if (generalJournal.isPost_flag()) {
            journalDocumentArrangedReportBean.setPostFlag("مرحل");

        } else {
            journalDocumentArrangedReportBean.setPostFlag("غير مرحل");
        }
        journalDocumentArrangedReportBean.setAmount(totalDebit);
        journalDocumentArrangedReportBean.setRemarks(generalJournal.getGeneralStatement());
    }

    public void print(ActionEvent actionEvent) throws JRException, IOException {

        String x = "3mr";
        HashMap hashMap = new HashMap();
        hashMap.put("name1", x);
        hashMap.put("journalNumFrom", journalDocumentsReportSearchBean.getJournalNumForm());
        hashMap.put("journalNumTo", journalDocumentsReportSearchBean.getJournalNumTo());
        hashMap.put("postFlag", journalDocumentsReportSearchBean.getPostFlag());
        hashMap.put("posting", journalDocumentsReportSearchBean.getPosting());
        hashMap.put("documentNumFrom", journalDocumentsReportSearchBean.getDocumentNumForm());
        hashMap.put("documentNumTo", journalDocumentsReportSearchBean.getDocumentNumTo());
        hashMap.put("documentTypeFrom", journalDocumentsReportSearchBean.getDocumentTypeFrom());
        hashMap.put("documentTypeTo", journalDocumentsReportSearchBean.getDocumentTypeTo());
        hashMap.put("userFrom", journalDocumentsReportSearchBean.getUserFrom());
        hashMap.put("userTo", journalDocumentsReportSearchBean.getUserTo());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());

        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(journalDocumentReportBeanList);
        HttpServletResponse httpServletResponse;
        jasperPrint = JasperFillManager.fillReport("D:\\Reports\\instrumentSummary.jasper", hashMap, beanCollectionDataSource);
        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        httpServletResponse.sendRedirect(x);

        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);

    }

    public void printDetail(ActionEvent actionEvent) throws JRException, IOException {

        HashMap hashMap = new HashMap();
        hashMap.put("journalNumberText", userDDs.get("GENERAL_NUMBER"));
        hashMap.put("journalDocumentText", userDDs.get("DOCUMENT_NUMBER"));
        hashMap.put("documentTypeText", userDDs.get("TYPE_BOND"));
        hashMap.put("documentDateText", userDDs.get("DOCUMENT_DATE"));
        hashMap.put("statementText", userDDs.get("STATEMENT"));

        hashMap.put("debitText", userDDs.get("DEBIT"));
        hashMap.put("creditText", userDDs.get("CREDIT"));
        hashMap.put("accountNumberText", userDDs.get("ACCOUNT_NUMBER"));
        hashMap.put("accountNameText", userDDs.get("ACCOUNT_NAME"));
        hashMap.put("costCenterText", userDDs.get("SINGLE_COST_CENTER"));
        hashMap.put("adminUnitText", userDDs.get("ADMIN_UNIT"));
        hashMap.put("journalCreatorText", userDDs.get("NAM_DAT_CONS"));
        hashMap.put("journalDateText", userDDs.get("CREA_CONST_DATE"));
    }

    public List<CostCenter> completeCostCenter(String query) {
        List<CostCenter> costCenterList = getCostCenterList();
        if (query == null || query.trim().equals("")) {
            costCenterConverter = new CostCenterConverter(costCenterList);
            return costCenterList;
        }
        List<CostCenter> filteredCostCenters = new ArrayList<>();

        String nameAr;
        Integer code;
        CostCenter costCenter;
        for (int i = 0; i < getCostCenterList().size(); i++) {
            costCenter = costCenterList.get(i);
            nameAr = costCenter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredCostCenters.contains(costCenter)) {
                    filteredCostCenters.add(costCenter);
                }
            }

            code = costCenter.getCode();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredCostCenters.contains(costCenter)) {
                    filteredCostCenters.add(costCenter);
                }
            }
        }

        costCenterConverter = new CostCenterConverter(filteredCostCenters);
        return filteredCostCenters;
    }

    public List<GlAdminUnit> completeAdminUnit(String query) {
        List<GlAdminUnit> glAdminUnits = getGlAdminUnitList();
        if (query == null || query.trim().equals("")) {
            glAdminUnitConverter = new GlAdminUnitConverter(glAdminUnits);
            return glAdminUnits;
        }
        List<GlAdminUnit> filteredGlAdminUnitList = new ArrayList<>();

        String nameAr;
        Integer code;
        GlAdminUnit glAdminUnit;
        for (int i = 0; i < getGlAdminUnitList().size(); i++) {
            glAdminUnit = glAdminUnits.get(i);
            nameAr = glAdminUnit.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredGlAdminUnitList.contains(glAdminUnit)) {
                    filteredGlAdminUnitList.add(glAdminUnit);
                }
            }

            code = glAdminUnit.getCode();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredGlAdminUnitList.contains(glAdminUnit)) {
                    filteredGlAdminUnitList.add(glAdminUnit);
                }
            }
        }
        glAdminUnitConverter = new GlAdminUnitConverter(filteredGlAdminUnitList);
        return filteredGlAdminUnitList;
    }

    public List<GlAccount> completeGlAccount(String query) {
        List<GlAccount> glaccounts = getGlAccountList();//45
        if (query == null || query.trim().equals("")) {
            accountConverter = new GlaccountConverter(glaccounts);
            return glaccounts;
        }
        List<GlAccount> filteredGlaccounts = new ArrayList<>();
        String nameAr;
        Integer code;
        GlAccount glaccount;
        for (int i = 0; i < getGlAccountList().size(); i++) {
            glaccount = glaccounts.get(i);
            nameAr = glaccount.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredGlaccounts.contains(glaccount)) {
                    filteredGlaccounts.add(glaccount);
                }
            }

            code = glaccount.getAccNumber();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredGlaccounts.contains(glaccount)) {
                    filteredGlaccounts.add(glaccount);
                }
            }
        }

        accountConverter = new GlaccountConverter(filteredGlaccounts);
        return filteredGlaccounts;
    }

    public List<GeneralJournal> getGeneralJournalList() {
        return generalJournalList;
    }

    public void setGeneralJournalList(List<GeneralJournal> generalJournalList) {
        this.generalJournalList = generalJournalList;
    }

    public JournalDocumentsReportSearchBean getJournalDocumentsReportSearchBean() {
        return journalDocumentsReportSearchBean;
    }

    public void setJournalDocumentsReportSearchBean(JournalDocumentsReportSearchBean journalDocumentsReportSearchBean) {
        this.journalDocumentsReportSearchBean = journalDocumentsReportSearchBean;
    }

    public List<JournalDocumentReportBean> getJournalDocumentReportBeanList() {
        return journalDocumentReportBeanList;
    }

    public void setJournalDocumentReportBeanList(List<JournalDocumentReportBean> journalDocumentReportBeanList) {
        this.journalDocumentReportBeanList = journalDocumentReportBeanList;
    }

    public GeneraljournalService getGeneraljournalService() {
        return generaljournalService;
    }

    public void setGeneraljournalService(GeneraljournalService generaljournalService) {
        this.generaljournalService = generaljournalService;
    }

    public GeneraljournalDetailsService getGeneraljournalDetailsService() {
        return generaljournalDetailsService;
    }

    public void setGeneraljournalDetailsService(GeneraljournalDetailsService generaljournalDetailsService) {
        this.generaljournalDetailsService = generaljournalDetailsService;
    }

    @Override
    public HashMap prepareReport() {

        /* Map<String, String> userDDs = getUserData().getUserDDs();
         String journalDocumentReportText = userDDs.get("JOURNAL_DOCUMENT_REPORT");
         String fromJournalDocumentTypeText = userDDs.get("FROM_JOURNAL_DOCUMENT_TYPE");
         String toJournalDocumentTypeText = userDDs.get("TO_JOURNAL_DOCUMENT_TYPE");
         String fromJournalDocumentNumberText = userDDs.get("FROM_JOURNAL_DOCUMENT_NUMBER");
         String toJournalDocumentNumberText = userDDs.get("TO_JOURNAL_DOCUMENT_NUMBER");
         String fromJournalNumberText = userDDs.get("FROM_JOURNAL_NUMBER");
         String toJournalNumberText = userDDs.get("TO_JOURNAL_NUMBER");
         String yearFromText = userDDs.get("YEAR_FROM");
         String yearToText = userDDs.get("YEAR_TO");
         String reviewAccountText = userDDs.get("REVIEW_ACCOUNT");
         String journalDailyBalancedText = userDDs.get("JOURNALS_DAILY_BALANCED");
         String fromUserText = userDDs.get("FROM_USER");
         String toUserText = userDDs.get("TO_USER");
         String generalNumberText = userDDs.get("GENERAL_NUMBER");
         String documentNumberText = userDDs.get("DOCUMENT_NUMBER");
         String dateText = userDDs.get("DATE");
         String reviewAccountingText = userDDs.get("REVIEW_ACCOUNTING");
         String postingText = userDDs.get("POSTING");
         String amountText = userDDs.get("AMOUNT");
         String journalAddedByText = userDDs.get("JOURNAL_ADDED_BY");
         String generalStatementText = userDDs.get("GERNAL_STATEMENT");
         String PrintedBy = getUserData().getUser().getName();
         String companyName = getUserData().getCompany().getName();
         String companyImage = getUserData().getImageReportPath();
         String branchName = getUserData().getUserBranch().getNameAr();

         HashMap hashMap = new HashMap();
         hashMap.put("companyName", companyName);
         hashMap.put("documentTypeFrom", journalDocumentsReportSearchBean.getDocumentTypeFrom());
         hashMap.put("documentTypeTo", journalDocumentsReportSearchBean.getDocumentTypeTo());
         hashMap.put("documentNumForm", journalDocumentsReportSearchBean.getDocumentNumForm());
         hashMap.put("documentNumTo", journalDocumentsReportSearchBean.getDocumentNumTo());
         hashMap.put("journalNumForm", journalDocumentsReportSearchBean.getJournalNumForm());
         hashMap.put("journalNumTo", journalDocumentsReportSearchBean.getJournalNumTo());
         hashMap.put("periodFrom", journalDocumentsReportSearchBean.getPeriodFrom());
         hashMap.put("periodTo", journalDocumentsReportSearchBean.getPeriodTo());
         hashMap.put("userFrom", journalDocumentsReportSearchBean.getUserFrom());
         hashMap.put("userTo", journalDocumentsReportSearchBean.getUserTo());
         hashMap.put("postFlag", journalDocumentsReportSearchBean.getPostFlag());
         hashMap.put("posting", journalDocumentsReportSearchBean.getPosting());

         hashMap.put("journalDocumentReportText", journalDocumentReportText);
         hashMap.put("fromJournalDocumentTypeText", fromJournalDocumentTypeText);
         hashMap.put("toJournalDocumentTypeText", toJournalDocumentTypeText);
         hashMap.put("fromJournalDocumentNumberText", fromJournalDocumentNumberText);
         hashMap.put("toJournalDocumentNumberText", toJournalDocumentNumberText);
         hashMap.put("fromJournalNumberText", fromJournalNumberText);
         hashMap.put("toJournalNumberText", toJournalNumberText);
         hashMap.put("yearFromText", yearFromText);
         hashMap.put("yearToText", yearToText);
         hashMap.put("reviewAccountText", reviewAccountText);
         hashMap.put("journalDailyBalancedText", journalDailyBalancedText);
         hashMap.put("fromUserText", fromUserText);
         hashMap.put("toUserText", toUserText);
         hashMap.put("generalNumberText", generalNumberText);
         hashMap.put("documentNumberText", documentNumberText);
         hashMap.put("dateText", dateText);
         hashMap.put("reviewAccountingText", reviewAccountingText);
         hashMap.put("postingText", postingText);
         hashMap.put("amountText", amountText);
         hashMap.put("journalAddedByText", journalAddedByText);
         hashMap.put("generalStatementText", generalStatementText);
         hashMap.put("PrintedBy", PrintedBy);
         hashMap.put("companyImage", companyImage);
         hashMap.put("branchName", branchName);

         return hashMap;*/
        Map<String, String> userDDs = getUserData().getUserDDs();

        hashMap = new HashMap();

        hashMap.put("documentTypeFrom", journalDocumentsReportSearchBean.getDocumentTypeFrom());
        hashMap.put("documentTypeTo", journalDocumentsReportSearchBean.getDocumentTypeTo());
        hashMap.put("documentNumForm", journalDocumentsReportSearchBean.getDocumentNumForm());
        hashMap.put("documentNumTo", journalDocumentsReportSearchBean.getDocumentNumTo());
        hashMap.put("journalNumForm", journalDocumentsReportSearchBean.getJournalNumForm());
        hashMap.put("journalNumTo", journalDocumentsReportSearchBean.getJournalNumTo());
        hashMap.put("periodFrom", journalDocumentsReportSearchBean.getPeriodFrom());
        hashMap.put("periodTo", journalDocumentsReportSearchBean.getPeriodTo());
        hashMap.put("userFrom", journalDocumentsReportSearchBean.getUserFrom());
        hashMap.put("userTo", journalDocumentsReportSearchBean.getUserTo());
        hashMap.put("postFlag", journalDocumentsReportSearchBean.getPostFlag());
        hashMap.put("posting", journalDocumentsReportSearchBean.getPosting());
        hashMap.put("journalDocumentDailyReportText", "بيان قيوداليومية");
        hashMap.put("journalNumberText", "رقم القيد");
        hashMap.put("journalDocumentText", "رقم السند");
        hashMap.put("documentTypeText", "نوع السند");
        hashMap.put("documentDateText", "تاريخ السند");
        hashMap.put("statementText", "البيان");

        hashMap.put("debitText", "مدين");
        hashMap.put("creditText", "دائن");
        hashMap.put("accountNumberText", "رقم الحساب");
        hashMap.put("accountNameText", "اسم الحساب");
        hashMap.put("costCenterText", "مركز التكلفة");
        hashMap.put("adminUnitText", "وحدة ادارية");
        hashMap.put("journalCreatorText", "اسم وتاريخ منشئ القيد");
        hashMap.put("journalDateText", "تاريخ انشاء القيد");
        hashMap.put("journaText", "قيد");
        hashMap.put("accountFromText", userDDs.get("ACCOUNT_FROM"));
        hashMap.put("accountToText", userDDs.get("ACCOUNT_TO"));
        hashMap.put("accountNameText", userDDs.get("ACCOUNT_NAME"));
        hashMap.put("accountNumberText", userDDs.get("ACCOUNT_NUMBER"));
        hashMap.put("fromCostCenterText", userDDs.get("COST_CENTER_FROM"));
        hashMap.put("toCostCenterText", userDDs.get("COST_CENTER_TO"));
        hashMap.put("fromAdminUnitText", userDDs.get("ADMIN_UNIT_FROM"));
        hashMap.put("toAdminUnitText", userDDs.get("ADMIN_UNIT_TO"));
        hashMap.put("journalDocumentReportText", userDDs.get("JOURNAL_DOCUMENT_REPORT"));
        hashMap.put("fromJournalDocumentTypeText", userDDs.get("FROM_JOURNAL_DOCUMENT_TYPE"));
        hashMap.put("toJournalDocumentTypeText", userDDs.get("TO_JOURNAL_DOCUMENT_TYPE"));
        hashMap.put("fromJournalDocumentNumberText", userDDs.get("FROM_JOURNAL_DOCUMENT_NUMBER"));
        hashMap.put("toJournalDocumentNumberText", userDDs.get("TO_JOURNAL_DOCUMENT_NUMBER"));
        hashMap.put("fromJournalNumberText", userDDs.get("FROM_JOURNAL_NUMBER"));
        hashMap.put("toJournalNumberText", userDDs.get("TO_JOURNAL_NUMBER"));
        hashMap.put("yearFromText", userDDs.get("YEAR_FROM"));
        hashMap.put("yearToText", userDDs.get("YEAR_TO"));
        hashMap.put("reviewAccountText", userDDs.get("REVIEW_ACCOUNT"));
        hashMap.put("journalDailyBalancedText", userDDs.get("JOURNALS_DAILY_BALANCED"));
        hashMap.put("fromUserText", userDDs.get("FROM_USER"));
        hashMap.put("toUserText", userDDs.get("TO_USER"));
        hashMap.put("generalNumberText", userDDs.get("GENERAL_NUMBER"));
        hashMap.put("documentNumberText", userDDs.get("DOCUMENT_NUMBER"));
        hashMap.put("dateText", userDDs.get("DATE"));
        hashMap.put("reviewAccountingText", ("وزن القيد"));
        hashMap.put("postingText", userDDs.get("POSTING"));
        hashMap.put("amountText", userDDs.get("AMOUNT"));
        hashMap.put("journalAddedByText", userDDs.get("JOURNAL_ADDED_BY"));
        hashMap.put("generalStatementText", userDDs.get("GERNAL_STATEMENT"));

        hashMap.put("PrintedBy", getUserData().getUser().getName());
        // hashMap.put("companyImage", getUserData().getImageReportPath());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
      //  hashMap.put("companyName", getUserData().getCompany().getName());
        hashMap.put("accountFromName", glAccountSelectedFrom != null ? glAccountSelectedFrom.getName() : null);
        hashMap.put("accountFromCode", glAccountSelectedFrom != null ? glAccountSelectedFrom.getAccNumber() : null);
        hashMap.put("accountToName", glAccountSelectedTo != null ? glAccountSelectedTo.getName() : null);
        hashMap.put("accountToCode", glAccountSelectedTo != null ? glAccountSelectedTo.getAccNumber() : null);
        hashMap.put("fromCostCenterName", costCenterFromSelected != null ? costCenterFromSelected.getName() : null);
        hashMap.put("toCostCenterName", costCenterToSelected != null ? costCenterToSelected.getName() : null);
        hashMap.put("fromAdminUnitName", glAdminUnitFromSelected != null ? glAdminUnitFromSelected.getName() : null);
        hashMap.put("toAdminUnitName", glAdminUnitToSelected != null ? glAdminUnitToSelected.getName() : null);

        hashMap.put("documentDailyTypeFromText", "من نوع يومية");
        hashMap.put("documentDailyTypeToText", "الى نوع يومية");

        if (symbolsMap.containsKey(journalDocumentsReportSearchBean.getDocumentTypeFrom())) {
            hashMap.put("documentDailyTypeFrom", symbolsMap.get(journalDocumentsReportSearchBean.getDocumentTypeFrom()) != null ? symbolsMap.get(journalDocumentsReportSearchBean.getDocumentTypeFrom()).getName() : null);
        }

        if (symbolsMap.containsKey(journalDocumentsReportSearchBean.getDocumentTypeTo())) {
            hashMap.put("documentDailyTypeTo", symbolsMap.get(journalDocumentsReportSearchBean.getDocumentTypeTo()) != null ? symbolsMap.get(journalDocumentsReportSearchBean.getDocumentTypeTo()).getName() : null);
        }

        if (journalDocumentsReportSearchBean.getPostFlag() != null) {
            if (journalDocumentsReportSearchBean.getPostFlag().contains("0")) {
                hashMap.put("postFlagValue", "لم يراجع");
            } else if (journalDocumentsReportSearchBean.getPostFlag().contains("1")) {
                hashMap.put("postFlagValue", "تمت مراجعته");
            }
        }
        if (journalDocumentsReportSearchBean.getPosting() != null) {
            if (journalDocumentsReportSearchBean.getPosting().contains("موزون")) {
                hashMap.put("postingValue", "موزون");
            } else if (journalDocumentsReportSearchBean.getPosting().contains("غير موزون")) {
                hashMap.put("postingValue", "غير موزون");
            }
        }
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {

        if (journalDocumentReportBeanList != null && !journalDocumentReportBeanList.isEmpty() && url.contains("journalDocumentsReport.xhtml")) {
            fillReport(prepareReport(), getUserData().getReportPath() + "journalDocumentsReport.jasper", journalDocumentReportBeanList, "pdf");
        } else if (url.contains("journalDocumentsReport.xhtml")) {
            search2();
            fillReport(prepareReport(), getUserData().getReportPath() + "journalDocumentsReport.jasper", journalDocumentReportBeanList, "pdf");

            //  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد نتائج للطباعة"));
        }
        if (journalDocumentArrangedReportBeanList != null && !journalDocumentArrangedReportBeanList.isEmpty() && url.contains("journalDocumentDailyReport.xhtml")) {
            fillReport(prepareReport(), getUserData().getReportPath() + "journalDocumentDailyReport3.jasper", journalDocumentArrangedReportBeanList, "pdf");
        } else if (url.contains("journalDocumentDailyReport.xhtml")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد نتائج للطباعة"));
        }

        /* if (journalDocumentReportBeanForPrintList == null && journalDocumentReportBeanForPrintList.isEmpty()) {
         search();
         }
         for (JournalDocumentReportBean jdrb : journalDocumentReportBeanForPrintList) {
         if (journalDocumentReportBeanForPrintList != null && !journalDocumentReportBeanForPrintList.isEmpty() && getI() != journalDocumentReportBeanForPrintList.size()) {
         printMultiplePage(jdrb);
         exportPDF(actionEvent);
         }
         }
         journalDocumentReportBeanForPrintList = new ArrayList<>();*/
    }

    public void printMultiplePage() throws JRException, IOException {
        hashMap = new HashMap();
        hashMap.put("journalNum", journalDocumentReportBeanForPrintList.get(getI()).getJournalNum());
        hashMap.put("documentNum", journalDocumentReportBeanForPrintList.get(getI()).getDocumentNum());
        hashMap.put("journalDate", journalDocumentReportBeanForPrintList.get(getI()).getJournalDate());
        hashMap.put("user", journalDocumentReportBeanForPrintList.get(getI()).getUser());
        hashMap.put("remarks", journalDocumentReportBeanForPrintList.get(getI()).getRemarks());
        // fillReport(prepareReport(), getUserData().getReportPath() + "journalDocumentDailyReport.jasper", jdrb.getJournalDocumentDailyReportBeanList(), "pdf");
        /*setI((Integer) (getI() + 1));

         JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(jdrb.getJournalDocumentDailyReportBeanList());
         JasperPrint jasperPrint = JasperFillManager.fillReport(getUserData().getReportPath() + "journalDocumentDailyReport.jasper", prepareReport(), beanCollectionDataSource);
         HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
         try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
         if ("pdf".equals("pdf")) {
         JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
         servletOutputStream.flush();
         servletOutputStream.close();

         } else if ("excel".equals("pdf")) {

         } else if ("html".equals("pdf")) {

         }
         }*/
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

    @Override
    public void resetDateFrom() {
        journalDocumentsReportSearchBean.setPeriodFrom(getGlYearSelection() != null ? getGlYearSelection().getDateFrom() : null);

    }

    @Override
    public void resetDateTo() {
        journalDocumentsReportSearchBean.setPeriodTo(getGlYearSelection() != null ? getGlYearSelection().getDateTo() : null);

    }

    @Override
    public void checkDate(Boolean dateType) {
        if (dateType) {
            if (checkFinancailYear(journalDocumentsReportSearchBean.getPeriodFrom())) {
                resetDateFrom();
            }
        } else {
            if (checkFinancailYear(journalDocumentsReportSearchBean.getPeriodTo())) {
                resetDateTo();
            }
        }
    }

    public List<JournalDocumentDailyReportBean> getJournalDocumentDailyReportBeanList() {
        return journalDocumentDailyReportBeanList;
    }

    public void setJournalDocumentDailyReportBeanList(List<JournalDocumentDailyReportBean> journalDocumentDailyReportBeanList) {
        this.journalDocumentDailyReportBeanList = journalDocumentDailyReportBeanList;
    }

    public List<Symbol> getSymbolsList() {
        return symbolsList;
    }

    public void setSymbolsList(List<Symbol> symbolsList) {
        this.symbolsList = symbolsList;
    }

    /**
     * @return the journalDocumentReportBeanForPrintList
     */
    public List<JournalDocumentReportBean> getJournalDocumentReportBeanForPrintList() {
        return journalDocumentReportBeanForPrintList;
    }

    /**
     * @param journalDocumentReportBeanForPrintList the
     * journalDocumentReportBeanForPrintList to set
     */
    public void setJournalDocumentReportBeanForPrintList(List<JournalDocumentReportBean> journalDocumentReportBeanForPrintList) {
        this.journalDocumentReportBeanForPrintList = journalDocumentReportBeanForPrintList;
    }

    /**
     * @return the journalDocumentReportBean
     */
    public JournalDocumentReportBean getJournalDocumentReportBean() {
        return journalDocumentReportBean;
    }

    /**
     * @param journalDocumentReportBean the journalDocumentReportBean to set
     */
    public void setJournalDocumentReportBean(JournalDocumentReportBean journalDocumentReportBean) {
        this.journalDocumentReportBean = journalDocumentReportBean;
    }

    /**
     * @return the hashMap
     */
    public HashMap getHashMap() {
        return hashMap;
    }

    /**
     * @param hashMap the hashMap to set
     */
    public void setHashMap(HashMap hashMap) {
        this.hashMap = hashMap;
    }

    /**
     * @return the i
     */
    public Integer getI() {
        return i;
    }

    /**
     * @param i the i to set
     */
    public void setI(Integer i) {
        this.i = i;
    }

    /**
     * @return the journalDocumentArrangedReportBeanList
     */
    public List<JournalDocumentArrangedReportBean> getJournalDocumentArrangedReportBeanList() {
        return journalDocumentArrangedReportBeanList;
    }

    /**
     * @param journalDocumentArrangedReportBeanList the
     * journalDocumentArrangedReportBeanList to set
     */
    public void setJournalDocumentArrangedReportBeanList(List<JournalDocumentArrangedReportBean> journalDocumentArrangedReportBeanList) {
        this.journalDocumentArrangedReportBeanList = journalDocumentArrangedReportBeanList;
    }

    /**
     * @return the journalDocumentArrangedReportBean
     */
    public JournalDocumentArrangedReportBean getJournalDocumentArrangedReportBean() {
        return journalDocumentArrangedReportBean;
    }

    /**
     * @param journalDocumentArrangedReportBean the
     * journalDocumentArrangedReportBean to set
     */
    public void setJournalDocumentArrangedReportBean(JournalDocumentArrangedReportBean journalDocumentArrangedReportBean) {
        this.journalDocumentArrangedReportBean = journalDocumentArrangedReportBean;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the accountConverter
     */
    public GlaccountConverter getAccountConverter() {
        return accountConverter;
    }

    /**
     * @param accountConverter the accountConverter to set
     */
    public void setAccountConverter(GlaccountConverter accountConverter) {
        this.accountConverter = accountConverter;
    }

    /**
     * @return the costCenterConverter
     */
    public CostCenterConverter getCostCenterConverter() {
        return costCenterConverter;
    }

    /**
     * @param costCenterConverter the costCenterConverter to set
     */
    public void setCostCenterConverter(CostCenterConverter costCenterConverter) {
        this.costCenterConverter = costCenterConverter;
    }

    /**
     * @return the glAdminUnitConverter
     */
    public GlAdminUnitConverter getGlAdminUnitConverter() {
        return glAdminUnitConverter;
    }

    /**
     * @param glAdminUnitConverter the glAdminUnitConverter to set
     */
    public void setGlAdminUnitConverter(GlAdminUnitConverter glAdminUnitConverter) {
        this.glAdminUnitConverter = glAdminUnitConverter;
    }

    /**
     * @return the costCenterToSelected
     */
    public CostCenter getCostCenterToSelected() {
        return costCenterToSelected;
    }

    /**
     * @param costCenterToSelected the costCenterToSelected to set
     */
    public void setCostCenterToSelected(CostCenter costCenterToSelected) {
        this.costCenterToSelected = costCenterToSelected;
    }

    /**
     * @return the costCenterFromSelected
     */
    public CostCenter getCostCenterFromSelected() {
        return costCenterFromSelected;
    }

    /**
     * @param costCenterFromSelected the costCenterFromSelected to set
     */
    public void setCostCenterFromSelected(CostCenter costCenterFromSelected) {
        this.costCenterFromSelected = costCenterFromSelected;
    }

    /**
     * @return the glAdminUnitToSelected
     */
    public GlAdminUnit getGlAdminUnitToSelected() {
        return glAdminUnitToSelected;
    }

    /**
     * @param glAdminUnitToSelected the glAdminUnitToSelected to set
     */
    public void setGlAdminUnitToSelected(GlAdminUnit glAdminUnitToSelected) {
        this.glAdminUnitToSelected = glAdminUnitToSelected;
    }

    /**
     * @return the glAdminUnitFromSelected
     */
    public GlAdminUnit getGlAdminUnitFromSelected() {
        return glAdminUnitFromSelected;
    }

    /**
     * @param glAdminUnitFromSelected the glAdminUnitFromSelected to set
     */
    public void setGlAdminUnitFromSelected(GlAdminUnit glAdminUnitFromSelected) {
        this.glAdminUnitFromSelected = glAdminUnitFromSelected;
    }

    /**
     * @return the glAccountSelectedTo
     */
    public GlAccount getGlAccountSelectedTo() {
        return glAccountSelectedTo;
    }

    /**
     * @param glAccountSelectedTo the glAccountSelectedTo to set
     */
    public void setGlAccountSelectedTo(GlAccount glAccountSelectedTo) {
        this.glAccountSelectedTo = glAccountSelectedTo;
    }

    /**
     * @return the glAccountSelectedFrom
     */
    public GlAccount getGlAccountSelectedFrom() {
        return glAccountSelectedFrom;
    }

    /**
     * @param glAccountSelectedFrom the glAccountSelectedFrom to set
     */
    public void setGlAccountSelectedFrom(GlAccount glAccountSelectedFrom) {
        this.glAccountSelectedFrom = glAccountSelectedFrom;
    }

    /**
     * @return the symbolsMap
     */
    public Map<Integer, Symbol> getSymbolsMap() {
        return symbolsMap;
    }

    /**
     * @param symbolsMap the symbolsMap to set
     */
    public void setSymbolsMap(Map<Integer, Symbol> symbolsMap) {
        this.symbolsMap = symbolsMap;
    }

    private static class userDDs {

        private static Object get(String general_number) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public userDDs() {
        }
    }
}
