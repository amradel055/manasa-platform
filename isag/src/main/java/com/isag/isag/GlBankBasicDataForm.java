/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.isag;

import com.isag.businessservice.CurrencyOperationService;
import com.isag.businessservice.GlAccountService;
import com.isag.businessservice.GlBankService;
import com.isag.businessservice.GlBankTransactionService;
import com.isag.businessservice.InstContractDetailsService;
import com.isag.businessservice.InstContractService;
import com.isag.dto.InstContractDetailDTO;
import com.isag.entity.Currency;
import com.isag.entity.CurrencyOperation;
import com.isag.entity.GlAccount;
import com.isag.entity.GlBank;
import com.isag.entity.GlBankTransaction;
import com.isag.entity.GlBankTransactionDetail;
import com.isag.entity.InstContract;
import com.isag.entity.InstContractDetail;
import com.isag.notesreceivables.notesReceivablesFormMB;
import com.isag.report.DeletedAndUpdatedGeneralJournalMB;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author m_els
 */
public abstract class GlBankBasicDataForm extends BaseFormBean {

    @EJB
    CurrencyOperationService currencyOperationService;
    @EJB
    InstContractService instContractService;

    @EJB
    InstContractDetailsService instContractDetailsService;
    @EJB
    private GlBankService glBankService;
    @EJB
    private GlBankTransactionService glBankTransactionService;
    @EJB
    private GlAccountService accountService;

    private UploadedFile ContractFile;
    private InputStream inputStreamUploadContract;
    private Integer progress;
    private Boolean validateExcel = Boolean.FALSE;

    public BigDecimal updateRate(Currency currency, Date date) {

        if (currency != null) {
            if (date != null) {
                CurrencyOperation currencyOperation = currencyOperationService.getRatesByDates(currency.getId(), date, getUserData().getCompany().getId());

                return currencyOperation == null ? BigDecimal.ONE : currencyOperation.getRate();
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("ERROR"), getUserData().getUserDDs().get("MUST_SELECT_DATE")));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("ERROR"), getUserData().getUserDDs().get("NO_CURRENCY_ERROR")));
        }
        return null;
    }

    public abstract void checkDate(Boolean dateType);

    public abstract void resetDateFrom();

    public abstract void resetDateTo();

    public void resetFinancailYearDate() {
        resetDateFrom();
        resetDateTo();
    }

    public void pushGlYearSelection() {
        resetFinancailYearDate();
        getUserData().setGlYear(getGlYearSelection());
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.getSessionMap().put("userLogInData", getUserData());
    }

    public void redirectFinancailYearPage() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            fc.getExternalContext().redirect("../base/financailyear.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(DeletedAndUpdatedGeneralJournalMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Boolean checkFinancailYear(Date date) {
        if ((date.before(getGlYearSelection().getDateFrom()) || date.after(getGlYearSelection().getDateTo()))) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار تاريخ يقع بين الفترة الماليه"));
            return true;
        }
        return false;
    }

    public abstract void calculateLocalValue();

    public void readCashTasahel() {
        try {
            setInputStreamUploadContract(getContractFile().getInputstream());

            Workbook workbook = null;
            workbook = Workbook.getWorkbook(getInputStreamUploadContract());
            Sheet sheet = workbook.getSheet(0);
            importCashTasahel(sheet);
            workbook.close();
        } catch (IOException ex) {
            Logger.getLogger(notesReceivablesFormMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BiffException ex) {
            Logger.getLogger(notesReceivablesFormMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void importCashTasahel(Sheet sheet) {
        GlBankTransaction glBankTransaction1;
        List<GlBankTransactionDetail> glBankTransactionDetailList;
        Map<Integer, List<InstContractDetailDTO>> instContractDTOMap = new HashMap<>();
        Map<String, GlBank> glBankMap = new HashMap<>();

        instContractDTOMap = instContractService.getALLInstContractByBranchIds(getUserData().getUserBranch().getId());// first modify

        List<GlBank> glBanks = glBankService.getAllGlBankByBranchId(getUserData().getUserBranch().getId());

        for (GlBank bank : glBanks) {
            glBankMap.put(bank.getCode(), bank);
        }

        Map<String, String> userDDs = getUserData().getUserDDs();
        Integer total = sheet.getRows() - 1;
        for (int i = 1; i < sheet.getRows(); i++) {
            setProgress((Integer) Float.valueOf((Integer.valueOf(i).floatValue() / total) * 100.0f).intValue());
            if (!sheet.getCell(0, i).getContents().isEmpty() || !sheet.getCell(1, i).getContents().isEmpty()) {
                glBankTransaction1 = new GlBankTransaction();
                glBankTransactionDetailList = new ArrayList<>();
                initCashDataTasahel(sheet, i, glBankTransaction1, glBankTransactionDetailList, userDDs, instContractDTOMap, glBankMap);
                if (validateExcel && glBankTransaction1.getValue() != null && glBankTransaction1.getDate() != null) {
                    GlBankTransaction glBankTransaction = glBankTransactionService.addGlBankTransactionForInstallment(glBankTransaction1, glBankTransactionDetailList, null);
                    if (glBankTransaction.getMsg() != null && !glBankTransaction.getMsg().isEmpty()) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), glBankTransaction.getMsg()));
                        return;
                    }
                }
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("SAVED")));
    }

    public void initCashDataTasahel(Sheet sheet, int i, GlBankTransaction glBankTransaction, List<GlBankTransactionDetail> glBankTransactionDetailList,
            Map<String, String> userDDs, Map<Integer, List<InstContractDetailDTO>> instContractDTOMap, Map<String, GlBank> glBankMap) {
        try {

            String serialExcel = sheet.getCell(6, i).getContents().trim();
            String accountCode = sheet.getCell(0, i).getContents().trim();
            String contractSerial = sheet.getCell(1, i).getContents().trim();
            String bankCode = sheet.getCell(2, i).getContents().trim();
            String value = sheet.getCell(3, i).getContents().trim();
            String date = sheet.getCell(4, i).getContents().trim();
            String remark = sheet.getCell(5, i).getContents().trim();
            Boolean isCount = Boolean.TRUE;
            if (contractSerial != null && !contractSerial.isEmpty()) {
                isCount = Boolean.FALSE;
            }
            GlAccount glAccount = null;

            if (!sheet.getCell(4, i).getType().equals(CellType.DATE) || !sheet.getCell(3, i).getType().equals(CellType.NUMBER)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "نوع خليه التاريخ غير صحيحه او خليه المبلغ غير رقميه"));
                validateExcel = Boolean.FALSE;
                return;
            }
            if (value.isEmpty() || date.isEmpty() || bankCode.isEmpty() || serialExcel.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجداو سيريال او رقم عقد او تاريخ او المبلغ او كود خزينه"));
                validateExcel = Boolean.FALSE;
                return;
            }
            if (contractSerial.isEmpty() && accountCode.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لايوجد رقم عقد او رقم حساب"));
                validateExcel = Boolean.FALSE;
                return;
            }
            InstContract contractSerialFound = null;
            if (!isCount) {
                contractSerialFound = instContractService.findInstContractByBranchIdAndSerial(getUserData().getUserBranch().getId(), Integer.valueOf(contractSerial));
                if (contractSerialFound == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "رقم العقد غير موجود رقم السطر" + (i + 1)));
                    validateExcel = Boolean.FALSE;
                    return;
                }
            }

            if (isCount) {
                glAccount = accountService.getAccountNumber(Integer.parseInt(accountCode), getUserData().getUserBranch().getId());
            } else {
                if (contractSerialFound != null && contractSerialFound.getOrganizationSiteId() != null) {
                    glAccount = contractSerialFound.getOrganizationSiteId().getAccountId();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "العقد لا يوجد به عميل رقم السطر" + (i + 1)));
                    validateExcel = Boolean.FALSE;
                    return;
                }
            }

            glBankTransaction.setSerialExcel(Integer.parseInt(serialExcel));
            glBankTransaction.setBranchId(getUserData().getUserBranch() != null ? getUserData().getUserBranch() : null);
            glBankTransaction.setCompanyId(getUserData().getCompany() != null ? getUserData().getCompany() : null);
            glBankTransaction.setCreatedBy(getUserData().getUser());
            glBankTransaction.setCreationDate(new Date());
            DateCell dateCell = (DateCell) sheet.getCell(4, i);
//           String date_format = new SimpleDateFormat().format(dateCell);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateCell.getDate());
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            glBankTransaction.setDate(calendar.getTime());
            glBankTransaction.setPostFlag(0);
            glBankTransaction.setPaymentType(0);
            glBankTransaction.setRemark(remark);
            if (isCount) {
                glBankTransaction.setOrganizationType(2);
            } else {
                glBankTransaction.setOrganizationType(0);
            }

            if (getUri().contains("settlementdeed")) {
                glBankTransaction.setTransactionType(0);
            } else if (getUri().contains("notesreceivables")) {
                glBankTransaction.setTransactionType(1);
            }

            glBankTransaction.setGlYear(getUserData().getGlYear().getYear());

            glBankTransaction.setValue(new BigDecimal(value));
            glBankTransaction.setValueLocal(new BigDecimal(value));
            glBankTransaction.setRate(BigDecimal.ONE);
            GlBank glBank = glBankMap.get(bankCode);
            if (glBank != null) {
                glBankTransaction.setGlBankId(glBank);
            } else {
                return;
            }
//            glBankTransaction.setGlBankId(null);

            if (!isCount) {
                List<InstContractDetailDTO> contractDetails = instContractDTOMap.get(Integer.parseInt(contractSerial));

                InstContractDetailDTO contractDetailDTOSelected = new InstContractDetailDTO();
                if (contractDetails != null && !contractDetails.isEmpty()) {
                    contractDetailDTOSelected = contractDetails.get(0);
                    InstContractDetail contractDetail = new InstContractDetail();
                    contractDetail.setId(contractDetailDTOSelected.getId());
                    glBankTransaction.setInstallmentId(contractDetail);
                } else {
                    glBankTransaction.setInstallmentId(null);
                }
            }

//Adding Detail
            GlBankTransactionDetail bankTransactionDetail = new GlBankTransactionDetail();

            bankTransactionDetail.setCreatedBy(getUserData().getUser() != null ? getUserData().getUser() : null);
            bankTransactionDetail.setCreationDate(new Date());

            bankTransactionDetail.setCompanyId(getUserData().getCompany() != null ? getUserData().getCompany() : null);

            bankTransactionDetail.setRemarks(remark);
            bankTransactionDetail.setValue(new BigDecimal(value));
            bankTransactionDetail.setValueLocal(new BigDecimal(value));

            if (!isCount) {
                bankTransactionDetail.setOrganizationSiteId(contractSerialFound.getOrganizationSiteId());
            }

            if (getUri().contains("settlementdeed")) {
                bankTransactionDetail.setAccountDebitId(glAccount);
                bankTransactionDetail.setAccountCreditId(glBank.getAccountId());
                bankTransactionDetail.setBankIdTo(glBank);
                bankTransactionDetail.setRateBankTo(BigDecimal.ONE);
            } else if (getUri().contains("notesreceivables")) {
                bankTransactionDetail.setAccountDebitId(glBank.getAccountId());
                bankTransactionDetail.setAccountCreditId(glAccount);
                bankTransactionDetail.setBankIdFrom(glBank);
                bankTransactionDetail.setRateBankFrom(BigDecimal.ONE);
            }

            glBankTransactionDetailList.add(bankTransactionDetail);
            validateExcel = Boolean.TRUE;
        } catch (Exception ex) {

            Logger.getLogger(notesReceivablesFormMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the ContractFile
     */
    public UploadedFile getContractFile() {
        return ContractFile;
    }

    /**
     * @param ContractFile the ContractFile to set
     */
    public void setContractFile(UploadedFile ContractFile) {
        this.ContractFile = ContractFile;
    }

    /**
     * @return the inputStreamUploadContract
     */
    public InputStream getInputStreamUploadContract() {
        return inputStreamUploadContract;
    }

    /**
     * @param inputStreamUploadContract the inputStreamUploadContract to set
     */
    public void setInputStreamUploadContract(InputStream inputStreamUploadContract) {
        this.inputStreamUploadContract = inputStreamUploadContract;
    }

    /**
     * @return the progress
     */
    public Integer getProgress() {
        return progress;
    }

    /**
     * @param progress the progress to set
     */
    public void setProgress(Integer progress) {
        this.progress = progress;
    }

}
