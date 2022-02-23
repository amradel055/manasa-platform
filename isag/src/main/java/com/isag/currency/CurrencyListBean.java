/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.currency;

import com.isag.businessservice.CompanyService;
import com.isag.businessservice.CurrencyOperationService;
import com.isag.businessservice.CurrencyService;
import com.isag.entity.Currency;

import com.isag.entity.CurrencyOperation;
import com.isag.entity.IsagCompany;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author hq004
 */
@Named(value = "currencyListBean")
@ViewScoped
public class CurrencyListBean extends BaseFormBean implements Serializable {

    private Currency currencyselected;

    private Currency currency;
    private UserData user;
    private List<Currency> currencylist;
    private List<CurrencyOperation> currencyOperations;
    private CurrencyOperation operation;
    // private Integer deleteSelectedCurreny;
    private CurrencyOperation selectedCurrencyOperation;
    private Integer selectedCompany;
    private Currency currencyToSave;
    private boolean currencyMsg = Boolean.FALSE;
    private boolean operationMsg = Boolean.FALSE;
    private List<IsagCompany> companies;
    private boolean exitBoolean = Boolean.FALSE;
    private Date currentDate;
    private Date lastDate;
    private Integer maxAllowedLines = 0;
    @EJB
    private CurrencyService currencyService;
    @EJB
    private CurrencyOperationService currencyOperationService;
    @EJB
    private CompanyService companyService;

    public CurrencyListBean() {
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "CurrencyListBean", "init");
        }

    }

    @Override
    public void load() {
        try {
            companies = new ArrayList<>();
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            user = ((UserData) context.getSessionMap().get("userLogInData"));
            if (user.getCompany() != null && user.getCompany().getId() != null) {
                companies.add(user.getCompany());
                selectedCompany = user.getCompany().getId();
                currencylist = getCurrencyService().getAllCurrencyByCompanyId(selectedCompany);
            } else {

                companies = companyService.getAllCompanies();

            }
        } catch (Exception e) {
            saveError(e, "CurrencyListBean", "load");
        }

    }

    @Override
    public String getScreenName() {
        return "";

    }

    public String exit() {
//        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Message Title", "Message body");
//        RequestContext.getCurrentInstance().showMessageInDialog(message);
//       if(exitBoolean)
//       {
//         
//       }
        return "exit";
    }

    @Override
    public String save() {

        return "";
    }

    public void onCompanyChange() {
        try {
            if (selectedCompany != null) {
                currencylist = currencyService.getAllCurrencyByCompanyId(selectedCompany);
            }
        } catch (Exception e) {
            saveError(e, "CurrencyListBean", "onCompanyChange");
        }
    }

    public void addCurrency() {
        try {
            exitBoolean = Boolean.FALSE;
            currencyMsg = Boolean.TRUE;
            operationMsg = Boolean.FALSE;
            List<Currency> currencyList;
            currencyList = currencyService.getAllCurrencyByCompanyIdAndCode(user.getCompany().getId(), currencyselected);
            if (currencyList == null || currencyList.isEmpty()) {
                if (currencyselected != null && selectedCompany != null && currencyselected.getName() != null && currencyselected.getSerial() != null) {
                    IsagCompany company = new IsagCompany();
                    company.setId(selectedCompany);
                    currencyselected.setCompanyId(company);
                    currencyselected.setMarkEdit(Boolean.FALSE);
                    currencyselected.setCreationDate(new Date());
                    currencyselected.setCreatedBy(user.getUser());
                    try {
                        currencyService.addCurrency(currencyselected);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, user.getUserDDs().get("INFO"), user.getUserDDs().get("SAVED")));
                        currencylist = currencyService.getAllCurrencyByCompanyId(selectedCompany);

                    } catch (Exception e) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, user.getUserDDs().get("ERROR"), user.getUserDDs().get("UNIQE_SERIAL")));
                        currencyselected.setMarkEdit(Boolean.TRUE);
                    }

                } else {
                    currencyselected.setMarkEdit(Boolean.TRUE);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, user.getUserDDs().get("ERROR"), user.getUserDDs().get("SAVED_ERROR")));
                }
            } else {
                currencyselected.setMarkEdit(Boolean.TRUE);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, user.getUserDDs().get("ERROR"), "يجب عدم تكرار الكود"));
            }
        } catch (Exception e) {
            saveError(e, "CurrencyListBean", "addCurrency");
        }
    }

    public void editCurrency() {
        try {
            exitBoolean = Boolean.TRUE;
            for (Currency currencylist1 : currencylist) {
                currencylist1.setMarkEdit(Boolean.FALSE);
            }
            currencyselected.setMarkEdit(Boolean.TRUE);
        } catch (Exception e) {
            saveError(e, "CurrencyListBean", "editCurrency");
        }
    }

    public void reload() {
        try {
            exitBoolean = Boolean.FALSE;
            currencyselected.setMarkEdit(Boolean.FALSE);
            if (currencyselected.getId() == null) {
                currencylist.remove(currencyselected);
            }
        } catch (Exception e) {
            saveError(e, "CurrencyListBean", "reload");
        }
    }

    public void reloadOpretions() {
        try {
            selectedCurrencyOperation.setMarkEdit(Boolean.FALSE);
            if (selectedCurrencyOperation.getId() == null) {
                currencyOperations.remove(selectedCurrencyOperation);
            }
        } catch (Exception ex) {
            saveError(ex, "CurrencyListBean", "reloadOpretions");
        }
    }

    public void addCurrencyOperation() {
        try {
            selectedCurrencyOperation.getOperationDate();
            if (lastDate != null && selectedCurrencyOperation.getOperationDate().before(lastDate)) {
                operationMsg = Boolean.TRUE;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, user.getUserDDs().get("ERROR"), user.getUserDDs().get("ADD_CURRENCY_OPERATION_ERROR")));
            } else {
                currencyMsg = Boolean.FALSE;
                operationMsg = Boolean.TRUE;
                if (currencyselected != null && selectedCompany != null) {
                    IsagCompany company = new IsagCompany();
                    company.setId(selectedCompany);
                    currencyselected.setCompanyId(company);
                    if (selectedCurrencyOperation != null && selectedCurrencyOperation.getRate() != null) {
                        selectedCurrencyOperation.setCurrencyId(currencyselected);
                        selectedCurrencyOperation.setCreationDate(new Date());
                        selectedCurrencyOperation.setCreatedBy(user.getUser());
                        selectedCurrencyOperation.setMarkEdit(Boolean.FALSE);
                        try {
                            currencyOperationService.updateCurrencyOpration(selectedCurrencyOperation);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, user.getUserDDs().get("INFO"), user.getUserDDs().get("SAVED")));
                            setCurrencyOperations(getCurrencyService().getCurrencyOperationForCurrency(currency.getId()));
                        } catch (Exception e) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, user.getUserDDs().get("ERROR"), user.getUserDDs().get("SAVED_ERROR")));
                            selectedCurrencyOperation.setMarkEdit(Boolean.TRUE);
                        }

                    } else {
                        selectedCurrencyOperation.setMarkEdit(Boolean.TRUE);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, user.getUserDDs().get("ERROR"), user.getUserDDs().get("SAVED_ERROR")));
                    }
                }
            }
            maxAllowedLines = 0;
        } catch (Exception e) {
            saveError(e, "CurrencyListBean", "addCurrencyOperation");
        }
    }

    public void add() {
        try {
            exitBoolean = Boolean.TRUE;
            Currency cur = new Currency();
            for (Currency currencylist1 : currencylist) {
                currencylist1.setMarkEdit(Boolean.FALSE);
            }
            cur.setMarkEdit(Boolean.TRUE);
            currencylist.add(0, cur);
        } catch (Exception e) {
            saveError(e, "CurrencyListBean", "add");
        }
    }

    public void addOperation() {
        try {

            if (maxAllowedLines == 0) {
                CurrencyOperation operations = new CurrencyOperation();

                operations.setCurrencyId(currencyselected);

                operations.setMarkEdit(Boolean.TRUE);
                IsagCompany company = new IsagCompany();
                if (selectedCompany != null) {
                    try {
                        company.setId(selectedCompany);
                        operations.setCompanyId(company);
                        currencyOperations.add(0, operations);
                        maxAllowedLines++;
                    } catch (Exception e) {
                        operationMsg = Boolean.TRUE;
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, user.getUserDDs().get("ERROR"), user.getUserDDs().get("CHOOSE_CURRENCY_FIRST_ERROR")));
                    }
                }
            } else {
                operationMsg = Boolean.TRUE;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, user.getUserDDs().get("ERROR"), user.getUserDDs().get("MUST_FILL_NEW_OPERATION")));
            }
        } catch (Exception e) {
            saveError(e, "CurrencyListBean", "addOperation");
        }
    }

    public void editCurrencyOperation() {
        try {

            for (CurrencyOperation currencyOp : currencyOperations) {
                currencyOp.setMarkEdit(Boolean.FALSE);
            }
            selectedCurrencyOperation.setMarkEdit(Boolean.TRUE);
        } catch (Exception e) {
            saveError(e, "CurrencyListBean", "editCurrencyOperation");
        }

    }

    public void deleteCurrency() {
        try {
            currencyMsg = Boolean.TRUE;
            operationMsg = Boolean.FALSE;
            if (currencyselected != null && currencyselected.getId() != null) {
                try {
                    currencyService.deleteCurrency(currencyselected);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, user.getUserDDs().get("INFO"), user.getUserDDs().get("DELETED")));
                    currencylist = currencyService.getAllCurrencyByCompanyId(selectedCompany);
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, user.getUserDDs().get("ERROR"), user.getUserDDs().get("UNIQE_KEY_ERROR")));
                }

            } else {
                currencylist.remove(currencyselected);
            }
        } catch (Exception e) {
            saveError(e, "CurrencyListBean", "deleteCurrency");
        }

    }

    public void deleteCurrencyOperation() {
        try {
            currencyMsg = Boolean.FALSE;
            operationMsg = Boolean.TRUE;
            if (selectedCurrencyOperation != null && selectedCurrencyOperation.getId() != null) {
                try {
                    currencyOperationService.deleteCurrencyOperation(selectedCurrencyOperation);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, user.getUserDDs().get("INFO"), user.getUserDDs().get("DELETED")));
                    setCurrencyOperations(getCurrencyService().getCurrencyOperationForCurrency(currency.getId()));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, user.getUserDDs().get("INFO"), user.getUserDDs().get("UNIQE_KEY_ERROR")));
                }

            } else {
                currencyOperations.remove(selectedCurrencyOperation);
            }
        } catch (Exception e) {
            saveError(e, "CurrencyListBean", "deleteCurrencyOperation");
        }

    }

    public void onRowCancel() {

    }

    public void onRowUnselect() {

    }

    public void onRowSelect(SelectEvent e) {
        try {
            currency = (Currency) e.getObject();
            if (currency != null) {
                currencyOperations = getCurrencyService().getCurrencyOperationForCurrency(currency.getId());

                if (currencyOperations != null && !currencyOperations.isEmpty()) {
                    lastDate = currencyOperations.get(0).getCreationDate();
                }

                for (CurrencyOperation currencyOperations : currencyOperations) {
                    currentDate = currencyOperations.getCreationDate();
                    if (currentDate.after(lastDate)) {
                        setLastDate(currentDate);
                    }
                }
            }
        } catch (Exception ex) {
            saveError(ex, "CurrencyListBean", "onRowSelect");
        }
    }

    public Currency getCurrencyselected() {
        return currencyselected;
    }

    public void setCurrencyselected(Currency currencyselected) {
        this.currencyselected = currencyselected;
    }

    public Currency getCurrency() {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    /**
     * @return the user
     */
    public UserData getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(UserData user) {
        this.user = user;
    }

    public List<Currency> getCurrencylist() {
        return currencylist;
    }

    public void setCurrencylist(List<Currency> currencylist) {
        this.currencylist = currencylist;
    }

    public void setCurrencyService(CurrencyService CurrencyService) {
        this.currencyService = CurrencyService;
    }

    /**
     * @return the id
     */
    /**
     * @return the currencyOperations
     */
    public List<CurrencyOperation> getCurrencyOperations() {
        return currencyOperations;
    }

    /**
     * @param currencyOperations the currencyOperations to set
     */
    public void setCurrencyOperations(List<CurrencyOperation> currencyOperations) {
        this.currencyOperations = currencyOperations;
    }

    /**
     * @return the operation
     */
    public CurrencyOperation getOperation() {
        return operation;
    }

    /**
     * @param operation the operation to set
     */
    public void setOperation(CurrencyOperation operation) {
        this.operation = operation;
    }

    /**
     * @return the currencyOperationService
     */
    public CurrencyOperationService getCurrencyOperationService() {
        return currencyOperationService;
    }

    /**
     * @param currencyOperationService the currencyOperationService to set
     */
    public void setCurrencyOperationService(CurrencyOperationService currencyOperationService) {
        this.currencyOperationService = currencyOperationService;
    }

    /**
     * @return the selectedCompany
     */
    public Integer getSelectedCompany() {
        return selectedCompany;
    }

    /**
     * @param selectedCompany the selectedCompany to set
     */
    public void setSelectedCompany(Integer selectedCompany) {
        this.selectedCompany = selectedCompany;
    }

    /**
     * @return the companies
     */
    public List<IsagCompany> getCompanies() {
        return companies;
    }

    /**
     * @param companies the companies to set
     */
    public void setCompanies(List<IsagCompany> companies) {
        this.companies = companies;
    }

    /**
     * @return the selectedCurrencyOperation
     */
    public CurrencyOperation getSelectedCurrencyOperation() {
        return selectedCurrencyOperation;
    }

    /**
     * @param selectedCurrencyOperation the selectedCurrencyOperation to set
     */
    public void setSelectedCurrencyOperation(CurrencyOperation selectedCurrencyOperation) {
        this.selectedCurrencyOperation = selectedCurrencyOperation;
    }

    /**
     * @return the currencyService
     */
    public CurrencyService getCurrencyService() {
        return currencyService;
    }

    /**
     * @return the currencyToSave
     */
    public Currency getCurrencyToSave() {
        return currencyToSave;
    }

    /**
     * @param currencyToSave the currencyToSave to set
     */
    public void setCurrencyToSave(Currency currencyToSave) {
        this.currencyToSave = currencyToSave;
    }

    public boolean isCurrencyMsg() {
        return currencyMsg;
    }

    public void setCurrencyMsg(boolean currencyMsg) {
        this.currencyMsg = currencyMsg;
    }

    public boolean isOperationMsg() {
        return operationMsg;
    }

    public void setOperationMsg(boolean operationMsg) {
        this.operationMsg = operationMsg;
    }

    public boolean isExitBoolean() {
        return exitBoolean;
    }

    public void setExitBoolean(boolean exitBoolean) {
        this.exitBoolean = exitBoolean;
    }

    /**
     * @return the currentDate
     */
    public Date getCurrentDate() {
        return currentDate;
    }

    /**
     * @param currentDate the currentDate to set
     */
    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    /**
     * @return the lastDate
     */
    public Date getLastDate() {
        return lastDate;
    }

    /**
     * @param lastDate the lastDate to set
     */
    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    /**
     * @return the maxAllowedLines
     */
    public Integer getMaxAllowedLines() {
        return maxAllowedLines;
    }

    /**
     * @param maxAllowedLines the maxAllowedLines to set
     */
    public void setMaxAllowedLines(Integer maxAllowedLines) {
        this.maxAllowedLines = maxAllowedLines;
    }

}
