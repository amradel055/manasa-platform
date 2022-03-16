package com.toby.reports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;

import com.toby.businessservice.report.DeletedAndUpdatedGeneralJournalService;
import com.toby.entity.TobyCompany;
import com.toby.businessservice.reports.searchBean.DeletedAndUpdatedSearchBean;
import com.toby.converter.GlaccountConverter;
import com.toby.entity.GlAccount;
import com.toby.report.entity.DeletedAndUpdatedGeneralJournalBeanData;
import com.toby.toby.BaseGlAccountReportBean;
import com.toby.views.DeletedAndUpdatedGeneralJournal;
import java.util.Map;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import net.sf.jasperreports.engine.JRException;

@Named("deletedAndUpdatedGeneralJournalMB")
@ViewScoped
public class DeletedAndUpdatedGeneralJournalMB extends BaseGlAccountReportBean {

    private static final long serialVersionUID = 1L;
    private DeletedAndUpdatedSearchBean deletedAndUpdatedSearchBean;
    private List<DeletedAndUpdatedGeneralJournalBeanData> deletedAndUpdatedGeneralJournalBeanDataList;
    private GlaccountConverter accountConverter;
    private GlAccount glAccountSelectedTo;
    private GlAccount glAccountSelectedFrom;
    List<DeletedAndUpdatedGeneralJournal> deletedAndUpdatedGeneralJournalList;
    TobyCompany companyId;

    public TobyCompany getCompanyId() {
        return companyId;
    }

    public void setCompanyId(TobyCompany companyId) {
        this.companyId = companyId;
    }

    @EJB
    DeletedAndUpdatedGeneralJournalService deletedAndUpdatedGeneralJournalService;

    @PostConstruct
    public void init() {
        if (getGlYearSelection() != null) {
            reset();
            load();
            resetDateFrom();
            resetDateTo();
        } else {
            redirectFinancailYearPage();
        }
        accountConverter = new GlaccountConverter(getGlAccountList());
    }

    @Override
    public void reset() {
        deletedAndUpdatedSearchBean = new DeletedAndUpdatedSearchBean();
        deletedAndUpdatedGeneralJournalList = new ArrayList<>();
        glAccountSelectedTo = null;
        glAccountSelectedFrom = null;
        resetDateFrom();
        resetDateTo();
    }

    @Override
    public void search() {
        deletedAndUpdatedSearchBean.setAccountNumberFrom(glAccountSelectedFrom != null ? glAccountSelectedFrom.getAccNumber() : null);
        deletedAndUpdatedSearchBean.setAccountNumberTo(glAccountSelectedTo != null ? glAccountSelectedTo.getAccNumber() : null);
        deletedAndUpdatedGeneralJournalList = deletedAndUpdatedGeneralJournalService
                .findAllDeletedAndUpdateGeneralJournals(deletedAndUpdatedSearchBean,
                        getUserData().getUserBranch().getId());

        DeletedAndUpdatedGeneralJournalBeanData data;
        deletedAndUpdatedGeneralJournalBeanDataList = new ArrayList<>(0);
        for (DeletedAndUpdatedGeneralJournal deletedAndUpdatedGeneralJournalBeanData : deletedAndUpdatedGeneralJournalList) {

            data = new DeletedAndUpdatedGeneralJournalBeanData();
            data.setAccountName(deletedAndUpdatedGeneralJournalBeanData.getAccountName() != null ? deletedAndUpdatedGeneralJournalBeanData.getAccountName() : null);
            data.setAccountNameNew(deletedAndUpdatedGeneralJournalBeanData.getAccountNameNew() != null ? deletedAndUpdatedGeneralJournalBeanData.getAccountNameNew() : null);
            data.setAccountNumber(deletedAndUpdatedGeneralJournalBeanData.getAccountNumber() != null ? deletedAndUpdatedGeneralJournalBeanData.getAccountNumber() : null);
            data.setAccountNumberNew(deletedAndUpdatedGeneralJournalBeanData.getAccountNumberNew() != null ? deletedAndUpdatedGeneralJournalBeanData.getAccountNumberNew() : null);
            data.setMacId(deletedAndUpdatedGeneralJournalBeanData.getMacId() != null ? deletedAndUpdatedGeneralJournalBeanData.getMacId() : null);
            data.setMacIdNew(deletedAndUpdatedGeneralJournalBeanData.getMacIdNew() != null ? deletedAndUpdatedGeneralJournalBeanData.getMacIdNew() : null);
            data.setTotalAmmountAfter(deletedAndUpdatedGeneralJournalBeanData.getTotalAmountNew() != null ? deletedAndUpdatedGeneralJournalBeanData.getTotalAmountNew() : null);
            data.setTotalAmmountBefore(deletedAndUpdatedGeneralJournalBeanData.getTotalAmountNew() != null ? deletedAndUpdatedGeneralJournalBeanData.getTotalAmountNew() : null);
            data.setOperationDate(deletedAndUpdatedGeneralJournalBeanData.getGeneralDate() != null ? deletedAndUpdatedGeneralJournalBeanData.getGeneralDate() : null);
            data.setOperationDateNew(deletedAndUpdatedGeneralJournalBeanData.getGeneralDateNew() != null ? deletedAndUpdatedGeneralJournalBeanData.getGeneralDateNew() : null);
            data.setUserName(deletedAndUpdatedGeneralJournalBeanData.getUserNameNew() != null ? deletedAndUpdatedGeneralJournalBeanData.getUserNameNew() : null);
            data.setVoucherId(deletedAndUpdatedGeneralJournalBeanData.getVoucherId() != null ? deletedAndUpdatedGeneralJournalBeanData.getVoucherId() : null);
            data.setSerial(deletedAndUpdatedGeneralJournalBeanData.getSerial() != null ? deletedAndUpdatedGeneralJournalBeanData.getSerial() : null);
            data.setModificationDateNew(deletedAndUpdatedGeneralJournalBeanData.getModificationDateNew() != null ? deletedAndUpdatedGeneralJournalBeanData.getModificationDateNew() : null);
            Integer operationStatus = (deletedAndUpdatedGeneralJournalBeanData.getOperationStatue() != null ? deletedAndUpdatedGeneralJournalBeanData.getOperationStatue() : null);

            if (data.getVoucherId() == 146) {
                int x = 0;
            }
            if (operationStatus != null) {
                if (operationStatus == 0) {
                    data.setOperationStatus("معدلة");
                } else if (operationStatus == 1) {
                    data.setOperationStatus("ملغاه");
                }
            }
            deletedAndUpdatedGeneralJournalBeanDataList.add(data);
        }
    }

    @Override
    public HashMap prepareReport() {

        Map<String, String> userDDs = getUserData().getUserDDs();

        String modifiedGeneralJournalReportText = userDDs.get("MODIFIED_GENERAL_JOURNAL");
        String fromJournalNumberText = userDDs.get("FROM_JOURNAL_NUMBER");
        String toJournalNumberText = userDDs.get("TO_JOURNAL_NUMBER");
        String yearFromText = userDDs.get("YEAR_FROM");
        String yearToText = userDDs.get("YEAR_TO");
        String accountFromText = userDDs.get("ACCOUNT_FROM");
        String accountToText = userDDs.get("ACCOUNT_TO");
        String beforeChangeText = userDDs.get("BEFORE_CHANGE");
        String afterChangeText = userDDs.get("AFTER_CHANGE");
        String accountNumberBeforeChangeText = userDDs.get("ACCOUNT_NUMBER_BEFORE_CHANGE");
        String accountNameBeforeChangeText = userDDs.get("ACCOUNT_NAME_BEFORE_CHANGE");
        String accountNumberAfterChangeText = userDDs.get("ACCOUNT_NUMBER_AFTER_CHANGE");
        String accountNameAfterChangeText = userDDs.get("ACCOUNT_NAME_AFTER_CHANGE");
        String generalNumberText = userDDs.get("GENERAL_NUMBER");
        String dateText = userDDs.get("DATE");
        String modificationDateText = userDDs.get("MODIFIED_DATE");
        String userText = userDDs.get("USER");
        String macIdText = userDDs.get("MAC_ID");
        String modifiedMacIdText = userDDs.get("MODIFIED_MAC");
        String operationStatusText = userDDs.get("OPERATION_STATUS");
        String PrintedBy = getUserData().getUser().getName();
        String companyName = getUserData().getCompany().getName();
        //  String companyImage = getUserData().getImageReportPath();
        String branchName = getUserData().getUserBranch().getNameAr();

        HashMap hashMap = new HashMap();
      //  hashMap.put("companyName", companyName);
        hashMap.put("voucherIdFrom", deletedAndUpdatedSearchBean.getVoucherIdFrom());
        hashMap.put("voucherIdTo", deletedAndUpdatedSearchBean.getVoucherIdTo());

        hashMap.put("dateFrom", deletedAndUpdatedSearchBean.getDateFrom());
        hashMap.put("dateTo", deletedAndUpdatedSearchBean.getDateTo());

        hashMap.put("modifiedGeneralJournalReportText", modifiedGeneralJournalReportText);
        hashMap.put("fromJournalNumberText", fromJournalNumberText);
        hashMap.put("toJournalNumberText", toJournalNumberText);
        hashMap.put("yearFromText", yearFromText);
        hashMap.put("yearToText", yearToText);
        hashMap.put("accountFromText", accountFromText);
        hashMap.put("accountToText", accountToText);
        hashMap.put("beforeChangeText", beforeChangeText);
        hashMap.put("afterChangeText", afterChangeText);
        hashMap.put("accountNumberBeforeChangeText", accountNumberBeforeChangeText);
        hashMap.put("accountNameBeforeChangeText", accountNameBeforeChangeText);
        hashMap.put("accountNumberAfterChangeText", accountNumberAfterChangeText);
        hashMap.put("accountNameAfterChangeText", accountNameAfterChangeText);
        hashMap.put("generalNumberText", generalNumberText);
        hashMap.put("dateText", dateText);
        hashMap.put("modificationDateText", modificationDateText);
        hashMap.put("userText", userText);
        hashMap.put("macIdText", macIdText);
        hashMap.put("modifiedMacIdText", modifiedMacIdText);
        hashMap.put("operationStatusText", operationStatusText);
        hashMap.put("PrintedBy", PrintedBy);
        //    hashMap.put("companyImage", companyImage);
        hashMap.put("branchName", branchName);
        hashMap.put("accountFromName", glAccountSelectedFrom != null ? glAccountSelectedFrom.getName() : null);
        hashMap.put("accountFromCode", glAccountSelectedFrom != null ? glAccountSelectedFrom.getAccNumber() : null);
        hashMap.put("accountToName", glAccountSelectedTo != null ? glAccountSelectedTo.getName() : null);
        hashMap.put("accountToCode", glAccountSelectedTo != null ? glAccountSelectedTo.getAccNumber() : null);
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (deletedAndUpdatedGeneralJournalBeanDataList != null && !deletedAndUpdatedGeneralJournalBeanDataList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "journalDocumentsHistoryReport.jasper", deletedAndUpdatedGeneralJournalBeanDataList, "pdf");
        } else {
            search();
            fillReport(prepareReport(), getUserData().getReportPath() + "journalDocumentsHistoryReport.jasper", deletedAndUpdatedGeneralJournalBeanDataList, "pdf");
        }
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

    @Override
    public void exportExcel(ActionEvent actionEvent) throws JRException, IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void exportHtml(ActionEvent actionEvent) throws JRException, IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public String getScreenName() {
        // TODO Auto-generated method stub
        return null;
    }

    public DeletedAndUpdatedSearchBean getDeletedAndUpdatedSearchBean() {
        return deletedAndUpdatedSearchBean;
    }

    public void setDeletedAndUpdatedSearchBean(DeletedAndUpdatedSearchBean deletedAndUpdatedSearchBean) {
        this.deletedAndUpdatedSearchBean = deletedAndUpdatedSearchBean;
    }

    public List<DeletedAndUpdatedGeneralJournalBeanData> getDeletedAndUpdatedGeneralJournalBeanDataList() {
        return deletedAndUpdatedGeneralJournalBeanDataList;
    }

    public void setDeletedAndUpdatedGeneralJournalBeanDataList(
            List<DeletedAndUpdatedGeneralJournalBeanData> deletedAndUpdatedGeneralJournalBeanDataList) {
        this.deletedAndUpdatedGeneralJournalBeanDataList = deletedAndUpdatedGeneralJournalBeanDataList;
    }

    @Override
    public void resetDateFrom() {
        deletedAndUpdatedSearchBean.setDateFrom(getGlYearSelection() != null ? getGlYearSelection().getDateFrom() : null);

    }

    @Override
    public void resetDateTo() {
        deletedAndUpdatedSearchBean.setDateTo(getGlYearSelection() != null ? getGlYearSelection().getDateTo() : null);

    }

    @Override
    public void checkDate(Boolean dateType) {
        if (dateType) {
            if (checkFinancailYear(deletedAndUpdatedSearchBean.getDateFrom())) {
                resetDateFrom();
            }
        } else {
            if (checkFinancailYear(deletedAndUpdatedSearchBean.getDateTo())) {
                resetDateTo();
            }
        }
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
}
