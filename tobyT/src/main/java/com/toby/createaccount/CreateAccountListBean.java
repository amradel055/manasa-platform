package com.toby.createaccount;

import com.toby.bean.AccountSearchBean;
import com.toby.businessservice.CurrencyService;
import com.toby.businessservice.GlAccountService;
import com.toby.converter.CurrencyConverter;
import com.toby.entity.Currency;
import com.toby.entity.GlAccount;
import com.toby.entity.TobyCompany;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author FreeComp
 */
@Named(value = "createAccountListBean")
@ViewScoped
public class CreateAccountListBean extends BaseListBean implements Serializable {

    private StringBuilder errorMessage;

    private GlAccount glaccount;
    private GlAccount accountParent;
    private GlAccount accountSelect;
    private AccountSearchBean accountSearchBean;
    private List<GlAccount> accountList;
    private List<GlAccount> accountListSearch;
    private String barcodeParent;
    private String nameParent;
    private List<Currency> currencys = new ArrayList<>();
    private CurrencyConverter currencyConverter;
    private String type;

    @EJB
    private GlAccountService accountFacadeREST;
    @EJB
    private CurrencyService currencyFacadeREST;

    public CreateAccountListBean() {
    }
//
//    @Override
//    @PostConstruct
//    public void init() {
//        load();
//    }

    @Override
    public void load() {
        // reset();
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
            setErrorMessage(new StringBuilder());
            setRoot(new DefaultTreeNode("Root", null));
            setUrl(new DefaultTreeNode("Root", null));
            setBarcodeParent("");
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            UserData userData = (UserData) context.getSessionMap().get("userLogInData");
            if (userData != null) {
                TobyCompany companyId = userData.getCompany();
                if (companyId != null && companyId.getId() != null) {
                    setAccountList(getAccountFacadeREST().getAllAccountByCompanyId(companyId.getId()));
                } else {
                    setAccountList(getAccountFacadeREST().getAllGlAccount());
                }
            }

            TreeNode accounts = new DefaultTreeNode("الحسابات", getRoot());
            if (getAccountList() != null) {
                drawTree(getAccountList(), accounts);
            }
        } catch (Exception e) {
            saveError(e, "CreateAccountListBean", "reset");
        }
    }

    public void drawTree(List<GlAccount> accountList, TreeNode node) {
        try {
            for (GlAccount parent : accountList) {
                if (parent.getParentAcc() == null) {
                    TreeNode parentNode = new DefaultTreeNode(parent.getName() + "-" + parent.getShotCode(), node);
                    drawChilds(parent.getId(), parentNode);
                }
            }
        } catch (Exception e) {
            saveError(e, "CreateAccountListBean", "drawTree");

        }
    }

    public void drawChilds(int i, TreeNode node) {
        try {
            for (GlAccount child : getAccountList()) {
                if (child.getParentAcc() != null) {
                    if (child.getParentAcc().getId() == i) {
                        TreeNode childNode = new DefaultTreeNode(child.getName() + "-" + child.getShotCode(), node);
                        drawChilds(child.getId(), childNode);
                    }
                }
            }
        } catch (Exception e) {
            saveError(e, "CreateAccountListBean", "drawChilds");

        }
    }

    public void onSelectTree(NodeSelectEvent event) {
        try {
            if (event.getTreeNode().getData() != null) {
                String barCode = (String) event.getTreeNode().getData();
                String[] barString = barCode.split("-");
                setGlaccount(getAccountFacadeREST().findParentAccount(Integer.parseInt(barString[1])));
                if (getGlaccount().getParentAcc() != null) {
                    setNameParent(getGlaccount().getParentAcc().getName());
                    setBarcodeParent(getGlaccount().getParentAcc().getShotCode().toString());
                }
            }
        } catch (Exception e) {
            saveError(e, "CreateAccountListBean", "onSelectTree");
        }
    }

    public void onSelectUrlTree(NodeSelectEvent event) {
        try {
            if (event.getTreeNode().getData() != null) {
                String pageName = (String) event.getTreeNode().getData();
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                try {
                    if (pageName.equals("إنشاء مركز تكلفة")) {
                        ec.redirect(ec.getRequestContextPath() + "/view/pages/GL/guide/createprofitcenter.xhtml");
                    } else if (pageName.equals("إنشاء وحدة ادارية")) {
                        ec.redirect(ec.getRequestContextPath() + "/view/pages/GL/guide/createmanageunit.xhtml");
                    } else if (pageName.equals("إنشاء حساب")) {
                        ec.redirect(ec.getRequestContextPath() + "/view/pages/GL/guide/createaccount.xhtml");
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception e) {
            saveError(e, "CreateAccountListBean", "onSelectUrlTree");
        }
    }

    public void onUnSelectTree(NodeUnselectEvent event) {
        reset();
    }

    public void find() {
        try {
            getAccountFacadeREST().findGlAccount(getGlaccount().getId());
        } catch (Exception e) {
            saveError(e, "CreateAccountListBean", "find");
        }
    }

    public void update() {
        try {
            getAccountFacadeREST().updateGlAccount(getGlaccount());
            FacesContext context = FacesContext.getCurrentInstance();
            reset();
            context.addMessage(null, new FacesMessage("", "تم التحديث بنجاح"));
        } catch (Exception e) {
            saveError(e, "CreateAccountListBean", "update");
        }
    }

    public void delete() {
        try {
            getAccountFacadeREST().updateGlAccount(getGlaccount());
            reset();
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("", "تم الحذف بنجاح"));
        } catch (Exception e) {
            saveError(e, "CreateAccountListBean", "delete");
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
            saveError(e, "CreateAccountListBean", "search");
        }
    }

    /**
     * set parent Account
     */
    public void parentAccount() {
        try {
            if (getBarcodeParent() != null && !"".equals(getBarcodeParent().trim())) {
                GlAccount parentAccount = getAccountFacadeREST().findParentAccount(Integer.parseInt(getBarcodeParent()));
                setNameParent(parentAccount.getName());
                getGlaccount().setParentAcc(parentAccount);
                if (parentAccount.getLevelAcc() != null) {
                    getGlaccount().setLevelAcc(parentAccount.getLevelAcc() + 1);
                }

            }
        } catch (Exception e) {
            saveError(e, "CreateAccountListBean", "parentAccount");
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

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.getSessionMap().put("ScreenMode", "Add");
        exit("../glaccount/glAccountForm.xhtml");
        return "";
    }

    @Override
    public String goToEdit() {
        if (getGlaccount() != null) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("glaccount", getGlaccount());
            context.getSessionMap().put("ScreenMode", "Edit");
            exit("../glaccount/glAccountForm.xhtml");
        } else {
            return "";
        }
        return "";
    }

    /**
     * @return the accountFacadeREST
     */
    public GlAccountService getAccountFacadeREST() {
        return accountFacadeREST;
    }

    /**
     * @param accountFacadeREST the accountFacadeREST to set
     */
    public void setAccountFacadeREST(GlAccountService accountFacadeREST) {
        this.accountFacadeREST = accountFacadeREST;
    }

    /**
     * @return the currencyFacadeREST
     */
    public CurrencyService getCurrencyFacadeREST() {
        return currencyFacadeREST;
    }

    /**
     * @param currencyFacadeREST the currencyFacadeREST to set
     */
    public void setCurrencyFacadeREST(CurrencyService currencyFacadeREST) {
        this.currencyFacadeREST = currencyFacadeREST;
    }

    @Override
    public void init() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private TreeNode getRoot() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void setRoot(DefaultTreeNode defaultTreeNode) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void setUrl(DefaultTreeNode defaultTreeNode) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
