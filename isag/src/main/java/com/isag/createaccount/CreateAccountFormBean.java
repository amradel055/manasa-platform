package com.isag.createaccount;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.isag.bean.AccountSearchBean;
import com.isag.businessservice.CurrencyService;
import com.isag.businessservice.GlAccountService;
import com.isag.converter.CurrencyConverter;
import com.isag.entity.GlAccount;
import com.isag.entity.Currency;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import org.primefaces.model.TreeNode;

/**
 *
 * @author FreeComp
 */
@Named(value = "createAccountFormBean")
@ViewScoped
public class CreateAccountFormBean extends BaseFormBean implements Serializable {

    private StringBuilder errorMessage;

    private GlAccount glaccount;
    private GlAccount accountParent;
    private GlAccount accountSelect;
    private AccountSearchBean accountSearchBean;
    private List<GlAccount> accountList;
    private List<GlAccount> accountListSearch;
    private String barcodeParent;
    private String nameParent;
    private TreeNode url;
    private TreeNode selectedNodeUrl;
    private TreeNode root;
    private TreeNode selectedNode;
    private List<Currency> currencys = new ArrayList<>();
    private CurrencyConverter currencyConverter;
    private String type;
    private UserData user;
    private String ScreenMode;

    @EJB
    GlAccountService accountFacadeREST;
    @EJB
    CurrencyService currencyFacadeREST;

    public CreateAccountFormBean() {
    }

    @Override
    public String save() {
        try {
            onvalidate();
            if (getErrorMessage() == null || getErrorMessage().toString().isEmpty()) {

                if (getBarcodeParent() == null && "".equals(getBarcodeParent().trim())) {
                    getGlaccount().setParentAcc(null);
                    getGlaccount().setLevelAcc(null);
                }
                accountFacadeREST.addGlAccount(getGlaccount());
                reset();

            }
            return "";
        } catch (Exception e) {
            saveError(e, "CreateAccountFormBean", "save");
            return null;
        }
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "CreateAccountFormBean", "init");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUser((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("add")) {
                reset();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
                try {
                    setGlaccount((GlAccount) context.getSessionMap().get("glaccount"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            saveError(e, "CreateAccountFormBean", "load");
        }
    }

    public void loadAccount() {
        try {
            if (getAccountSelect() != null) {
                setGlaccount(getAccountSelect());
                if (getGlaccount().getParentAcc() != null) {
                    setNameParent(getGlaccount().getParentAcc().getName());
                    setBarcodeParent(getGlaccount().getParentAcc().getShotCode().toString());
                }
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("PF('accountSearch').hide();");
            }
        } catch (Exception e) {
            saveError(e, "CreateAccountFormBean", "loadAccount");
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void reset() {
        try {
            setGlaccount(new GlAccount());
            setAccountParent(new GlAccount());
            setAccountSelect(new GlAccount());
            setAccountListSearch(new ArrayList<GlAccount>());
            setCurrencys(new ArrayList<Currency>());
            setAccountSearchBean(new AccountSearchBean());
            setNameParent(new String());
            getGlaccount().setLevelAcc(new Integer(1));
            setType("جذرى");
            Currency currency = new Currency();
            currency.setId(1);
            getGlaccount().setCurrencyId(currency);
            setAccountList(new ArrayList<GlAccount>());
            setBarcodeParent("");
        } catch (Exception e) {
            saveError(e, "CreateAccountFormBean", "reset");
        }
    }

    public void onvalidate() {
        try {
            if (getGlaccount().getAccNumber() == null || !accountFacadeREST.validateAccountNumber(getGlaccount().getAccNumber())) {

                getErrorMessage().append("رقم الحساب موجود مسبقا");

                getErrorMessage().append("\n");
            }
            if (getGlaccount().getShotCode() == null || !accountFacadeREST.validateshortcode(getGlaccount().getShotCode())) {

                getErrorMessage().append("رقم المختصر موجود مسبقا");

                getErrorMessage().append("\n");
            }

            if (getGlaccount().getName() == null || getGlaccount().getName().isEmpty()) {

                getErrorMessage().append("ادخل اسم الحساب");

                getErrorMessage().append("\n");
            }
        } catch (Exception e) {
            saveError(e, "CreateAccountFormBean", "onvalidate");
        }
    }

    public void find() {
        try {
            accountFacadeREST.findGlAccount(getGlaccount().getId());
        } catch (Exception e) {
            saveError(e, "CreateAccountFormBean", "find");
        }
    }

    public void update() {
        try {
            accountFacadeREST.updateGlAccount(getGlaccount());
            FacesContext context = FacesContext.getCurrentInstance();
            reset();
            context.addMessage(null, new FacesMessage("", "تم التحديث بنجاح"));
        } catch (Exception e) {
            saveError(e, "CreateAccountFormBean", "update");
        }
    }

    public void delete() {
        try {
            accountFacadeREST.updateGlAccount(getGlaccount());
            reset();
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("", "تم الحذف بنجاح"));
        } catch (Exception e) {
            saveError(e, "CreateAccountFormBean", "delete");
        }
    }

    public void search() {
        try {
            setAccountListSearch(new ArrayList());
            if (getAccountSearchBean().getBarcode() != null) {
                int search = getAccountSearchBean().getBarcode();
                for (GlAccount glaccount : getAccountList()) {
                    if (glaccount.getShotCode() == search) {
                        getAccountListSearch().add(glaccount);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            saveError(e, "CreateAccountFormBean", "search");
        }
    }

    /**
     * set parent Account
     */
    public void parentAccount() {
        try {
            if (getBarcodeParent() != null && !"".equals(getBarcodeParent().trim())) {
                GlAccount parentAccount = accountFacadeREST.findParentAccount(Integer.parseInt(getBarcodeParent()));
                setNameParent(parentAccount.getName());
                getGlaccount().setParentAcc(parentAccount);
                if (parentAccount.getLevelAcc() != null) {
                    getGlaccount().setLevelAcc(parentAccount.getLevelAcc() + 1);
                }

            }
        } catch (Exception e) {
            saveError(e, "CreateAccountFormBean", "parentAccount");
        }
    }

    /**
     * @return the errorMessage
     */
    public StringBuilder getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(StringBuilder errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return the glaccount
     */
    public GlAccount getGlaccount() {
        return glaccount;
    }

    /**
     * @param glaccount the glaccount to set
     */
    public void setGlaccount(GlAccount glaccount) {
        this.glaccount = glaccount;
    }

    /**
     * @return the accountParent
     */
    public GlAccount getAccountParent() {
        return accountParent;
    }

    /**
     * @param accountParent the accountParent to set
     */
    public void setAccountParent(GlAccount accountParent) {
        this.accountParent = accountParent;
    }

    /**
     * @return the accountSelect
     */
    public GlAccount getAccountSelect() {
        return accountSelect;
    }

    /**
     * @param accountSelect the accountSelect to set
     */
    public void setAccountSelect(GlAccount accountSelect) {
        this.accountSelect = accountSelect;
    }

    /**
     * @return the accountSearchBean
     */
    public AccountSearchBean getAccountSearchBean() {
        return accountSearchBean;
    }

    /**
     * @param accountSearchBean the accountSearchBean to set
     */
    public void setAccountSearchBean(AccountSearchBean accountSearchBean) {
        this.accountSearchBean = accountSearchBean;
    }

    /**
     * @return the accountList
     */
    public List<GlAccount> getAccountList() {
        return accountList;
    }

    /**
     * @param accountList the accountList to set
     */
    public void setAccountList(List<GlAccount> accountList) {
        this.accountList = accountList;
    }

    /**
     * @return the accountListSearch
     */
    public List<GlAccount> getAccountListSearch() {
        return accountListSearch;
    }

    /**
     * @param accountListSearch the accountListSearch to set
     */
    public void setAccountListSearch(List<GlAccount> accountListSearch) {
        this.accountListSearch = accountListSearch;
    }

    /**
     * @return the barcodeParent
     */
    public String getBarcodeParent() {
        return barcodeParent;
    }

    /**
     * @param barcodeParent the barcodeParent to set
     */
    public void setBarcodeParent(String barcodeParent) {
        this.barcodeParent = barcodeParent;
    }

    /**
     * @return the nameParent
     */
    public String getNameParent() {
        return nameParent;
    }

    /**
     * @param nameParent the nameParent to set
     */
    public void setNameParent(String nameParent) {
        this.nameParent = nameParent;
    }

    /**
     * @return the url
     */
    public TreeNode getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(TreeNode url) {
        this.url = url;
    }

    /**
     * @return the selectedNodeUrl
     */
    public TreeNode getSelectedNodeUrl() {
        return selectedNodeUrl;
    }

    /**
     * @param selectedNodeUrl the selectedNodeUrl to set
     */
    public void setSelectedNodeUrl(TreeNode selectedNodeUrl) {
        this.selectedNodeUrl = selectedNodeUrl;
    }

    /**
     * @return the root
     */
    public TreeNode getRoot() {
        return root;
    }

    /**
     * @param root the root to set
     */
    public void setRoot(TreeNode root) {
        this.root = root;
    }

    /**
     * @return the selectedNode
     */
    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    /**
     * @param selectedNode the selectedNode to set
     */
    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    /**
     * @return the currencys
     */
    public List<Currency> getCurrencys() {
        return currencys;
    }

    /**
     * @param currencys the currencys to set
     */
    public void setCurrencys(List<Currency> currencys) {
        this.currencys = currencys;
    }

    /**
     * @return the currencyConverter
     */
    public CurrencyConverter getCurrencyConverter() {
        return currencyConverter;
    }

    /**
     * @param currencyConverter the currencyConverter to set
     */
    public void setCurrencyConverter(CurrencyConverter currencyConverter) {
        this.currencyConverter = currencyConverter;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
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

    /**
     * @return the ScreenMode
     */
    public String getScreenMode() {
        return ScreenMode;
    }

    /**
     * @param ScreenMode the ScreenMode to set
     */
    public void setScreenMode(String ScreenMode) {
        this.ScreenMode = ScreenMode;
    }
}
