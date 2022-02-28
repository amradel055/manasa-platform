/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.symbol;

import com.isag.businessservice.CompanyService;
import com.isag.businessservice.GeneralSymbolService;
import com.isag.businessservice.SymbolService;
import com.isag.entity.GeneralSymbol;
import com.isag.entity.IgmaCompany;
import com.isag.entity.Symbol;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author hq002
 */
public class GeneralSymbolBean extends BaseFormBean {

    private List<GeneralSymbol> generalSymbol;
    private List<Symbol> symbols = new ArrayList<>();
    private List<IgmaCompany> company;
    private Integer selectedCompany;
    private IgmaCompany companyId;
    private GeneralSymbol generalSymbolSelected = new GeneralSymbol();
    private Symbol symbol = new Symbol();
    private Symbol selectedSymbol = new Symbol();
    private Symbol newSymbol = new Symbol();
    private UserData userData;
    @EJB
    private GeneralSymbolService GeneralsymbolService;

    @EJB
    private SymbolService symbolService;

    @EJB
    private CompanyService companyService;

    public GeneralSymbolBean() {
    }

    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "GeneralSymbolBean", "init");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");
            companyId = userData.getCompany();
            if (companyId != null && companyId.getId() != null) {
                //get all symbole by company id  
                generalSymbol = new ArrayList<>();
                generalSymbol = getAllGSymbolWithoutLanguage();
                //setGlaccountList(glAccountService.getBranchGLAccountsActive(userData.getUserBranch().getId()));
//                setGlaccountList(glAccountService.getBranchGLAccountsActiveWithoutGlBankAccounts(userData.getUserBranch().getId()));
//                setGlaccountConverter(new GlaccountConverter(glaccountList));
            } else {
                company = new ArrayList<>();
                generalSymbol = getAllGSymbolWithoutLanguage();
                company = companyService.getAllCompanies();
            }
        } catch (Exception e) {
            saveError(e, "GeneralSymbolBean", "load");
        }
    }

    public List<GeneralSymbol> getAllGSymbolByCompanyId(Integer companyId) {
        try {
            return GeneralsymbolService.getAllGSymbolListByCompanyId(companyId);
        } catch (Exception e) {
            saveError(e, "GeneralSymbolBean", "getAllGSymbolByCompanyId");
            return null;
        }
    }

    public List<GeneralSymbol> getAllGSymbolWithoutLanguage() {
        try {
            return GeneralsymbolService.getAllGSymbolWithoutLanguage();
        } catch (Exception e) {
            saveError(e, "GeneralSymbolBean", "getAllGSymbolWithoutLanguage");
            return null;
        }
    }

    public void onSelect(SelectEvent event) {
        try {
            selectedSymbol = (Symbol) event.getObject();
        } catch (Exception e) {
            saveError(e, "GeneralSymbolBean", "onSelect");
        }
    }

    public void onEditedRow() {
        try {
            Map<String, String> userDDs = userData.getUserDDs();
            if (selectedSymbol != null && selectedSymbol.getName() != null && generalSymbolSelected != null) {
                if (selectedSymbol.getSerial() < 1000 || selectedSymbol.getSerial() > 2000) {
                    newSymbol = selectedSymbol;

                    if (newSymbol.getId() == null) {
                        newSymbol.setCreatedBy(userData.getUser());
                        newSymbol.setCreationDate(new Date());

                        List<Symbol> symbolsList = symbolService.getSymbolBySerialByGeneralSymbol(userData.getCompany().getId(), (newSymbol.getSerial() > 0 ? newSymbol.getSerial() : 0), generalSymbolSelected.getId());

                        if (!symbolsList.isEmpty()) {

                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("ERROR"), "هناك رمز له نفس الكود"));
                            return;
                        }

                    } else {
                        newSymbol.setModificationDate(new Date());
                        newSymbol.setModifiedBy(userData.getUser());

                        List<Symbol> symbolsList = symbolService.getSymbolByGeneralSymbol(userData.getCompany().getId(), newSymbol.getId(), (newSymbol.getSerial() > 0 ? newSymbol.getSerial() : 0), generalSymbolSelected.getId());

                        if (!symbolsList.isEmpty()) {

                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("ERROR"), "هناك رمز له نفس الكود"));
                            return;
                        }

                        symbolService.getSymbol(newSymbol.getId());

                    }
                    newSymbol.setGeneralSymbolId(generalSymbolSelected);

                    newSymbol.setMarkEdit(Boolean.FALSE);
//                    if (newSymbol.getAccountId() == null || newSymbol.getAccountId().getId() == null) {
//                        newSymbol.setAccountId(null);
//                    } else {
//                        GlAccount account = new GlAccount();
//                        account = glAccountService.findGlAccount(newSymbol.getAccountId().getId());
//                        newSymbol.setAccountId(account);
//                    }
                    if (userData.getIsAdmin() && selectedCompany != null) {
                        companyId = new IgmaCompany();
                        companyId.setId(selectedCompany);
                        symbol.setCompanyId(companyId);
                        try {
                            symbolService.updateSymbol(newSymbol);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("SAVED")));
//                            newSymbol.setAccountId(new GlAccount());
                        } catch (Exception e) {
                            e.getStackTrace();
                            selectedSymbol.setMarkEdit(Boolean.TRUE);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("ERROR"), userDDs.get("SAVED_ERROR")));
                        }
                    } else if (userData.getCompany() != null) {
                        symbol.setCompanyId(companyId);
                        try {
                            newSymbol.setCompanyId(companyId);
                            symbolService.updateSymbol(newSymbol);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("SAVED")));
//                            newSymbol.setAccountId(new GlAccount());
                        } catch (Exception e) {
                            e.getStackTrace();
                            selectedSymbol.setMarkEdit(Boolean.TRUE);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("ERROR"), userDDs.get("SAVED_ERROR")));
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), "Please Select Company"));
                        generalSymbolSelected.setMarkEdit(Boolean.TRUE);
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("ERROR"), "اختر رمز لا يقع بين 1000 إلى 2000"));
                    return;
                }
            }
        } catch (Exception e) {
            saveError(e, "GeneralSymbolBean", "onEditedRow");
        }
    }

    public void reload() {
        try {
            selectedSymbol.setMarkEdit(Boolean.FALSE);
            if (selectedSymbol.getId() == null) {
                symbols.remove(selectedSymbol);
            }
        } catch (Exception e) {
            saveError(e, "GeneralSymbolBean", "reload");
        }
    }

    public void selectGSymbol(SelectEvent event) {
        try {
            generalSymbolSelected = (GeneralSymbol) event.getObject();
            if (generalSymbolSelected != null) {
                symbols = new ArrayList<>();
                if (userData.getCompany() != null) {
                    symbols = GeneralsymbolService.getSymbolsForGeneralSymbol(generalSymbolSelected.getId(), userData.getCompany().getId());

                } else {
//                symbols = GeneralsymbolService.getSymbolsForGeneralSymbol(generalSymbolSelected.getId(), userData.getCompany().getId());

                }
//                for (Symbol s : symbols) {
//                    if (s.getAccountId() == null) {
//                        s.setAccountId(new GlAccount());
//                    }
//                }
            }
        } catch (Exception e) {
            saveError(e, "GeneralSymbolBean", "selectGSymbol");
        }
    }

    public void onCompanyChange() {
        try {
            symbols = new ArrayList<>();
            if (selectedCompany != null && generalSymbolSelected != null) {
                symbols = GeneralsymbolService.getSymbolsForGeneralSymbol(generalSymbolSelected.getId(), selectedCompany);
            }
        } catch (Exception e) {
            saveError(e, "GeneralSymbolBean", "onCompanyChange");
        }
    }

    public void cancel() {

    }

    public void edit() {
        try {
            for (Symbol sym : symbols) {
                sym.setMarkEdit(Boolean.FALSE);
            }
            if (selectedSymbol != null) {
                selectedSymbol.setMarkEdit(Boolean.TRUE);
            }

            init();
        } catch (Exception e) {
            saveError(e, "GeneralSymbolBean", "edit");
        }
    }

    public void addRow() {
        try {
            if (generalSymbolSelected != null) {
                for (Symbol sym : symbols) {
                    sym.setMarkEdit(Boolean.FALSE);
                }
                Symbol symbolnew = new Symbol();
                symbolnew.setMarkEdit(Boolean.TRUE);
//                symbolnew.setAccountId(new GlAccount());
                symbols.add(0, symbolnew);
//            selectedSymbol= new Symbol();
            }
        } catch (Exception e) {
            saveError(e, "GeneralSymbolBean", "addRow");
        }
    }

    public void delete() {
        try {
            Map<String, String> userDDs = userData.getUserDDs();
            if (selectedSymbol != null && selectedSymbol.getId() != null) {

                try {
                    symbolService.deleteSymbol(selectedSymbol);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", userDDs.get("DELETED")));
                    if (userData.getIsAdmin()) {
                        onCompanyChange();
                    } else {
                        symbols = GeneralsymbolService.getSymbolsForGeneralSymbol(generalSymbolSelected.getId(), userData.getCompany().getId());
                    }

                } catch (Exception e) {
                    e.getStackTrace();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", userDDs.get("UNIQE_KEY_ERROR")));
                }
            } else {
                symbols.remove(0);
            }
        } catch (Exception e) {
            saveError(e, "GeneralSymbolBean", "delete");
        }
    }

    @Override
    public String getScreenName() {
        return "symbol";
    }

    public Symbol getSelectedSymbol() {
        return selectedSymbol;
    }

    public void setSelectedSymbol(Symbol selectedSymbol) {
        this.selectedSymbol = selectedSymbol;
    }

    public List<GeneralSymbol> getGeneralSymbol() {
        return generalSymbol;
    }

    public void setGeneralSymbol(List<GeneralSymbol> generalSymbol) {
        this.generalSymbol = generalSymbol;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public IgmaCompany getCompanyId() {
        return companyId;
    }

    public void setCompanyId(IgmaCompany companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the symbols
     */
    public List<Symbol> getSymbols() {
        return symbols;
    }

    /**
     * @param symbols the symbols to set
     */
    public void setSymbols(List<Symbol> symbols) {
        this.symbols = symbols;
    }

    public List<IgmaCompany> getCompany() {
        return company;
    }

    public void setCompany(List<IgmaCompany> company) {
        this.company = company;
    }

    public Integer getSelectedCompany() {
        return selectedCompany;
    }

    public void setSelectedCompany(Integer selectedCompany) {
        this.selectedCompany = selectedCompany;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public GeneralSymbol getGeneralSymbolSelected() {
        return generalSymbolSelected;
    }

    public void setGeneralSymbolSelected(GeneralSymbol generalSymbolSelected) {
        this.generalSymbolSelected = generalSymbolSelected;
    }

    public Symbol getNewSymbol() {
        return newSymbol;
    }

    public void setNewSymbol(Symbol newSymbol) {
        this.newSymbol = newSymbol;
    }

}
