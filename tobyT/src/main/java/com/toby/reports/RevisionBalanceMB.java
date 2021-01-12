package com.toby.reports;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import com.toby.businessservice.report.RevisionBalanceViewService;
import com.toby.businessservice.reports.searchBean.RevisionBalanceSearchBean;
import com.toby.toby.UserData;
import com.toby.views.RevisionBalanceView;
import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

@Named("revisionBalanceMB")
@ViewScoped
public class RevisionBalanceMB implements Serializable{

    private List<RevisionBalanceView> revisionBalanceViewList;
    private List<RevisionBalanceSearchBean> revisionBalanceSearchBeanList;
    private JasperPrint jasperPrint;
    private UserData userData;
    private List<RevisionBalanceView> revisionBalanceList;
    private String dateFrom;
    private String dateTo;
    private Integer levelFrom;
    private Integer levelTo;
    private Integer accountIdFrom;
    private Integer accountIdTo;
    private Integer adminUnitFrom;
    private Integer adminUnitTo;
    private int adminUnitId;

    @EJB
    private RevisionBalanceViewService revisionBalanceViewService;

    public RevisionBalanceMB() {
    }

    @PostConstruct
    public void init() {
        // rest();
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        userData = (UserData) context.getSessionMap().get("userLogInData");
        revisionBalanceList = revisionBalanceViewService.getAllRevisionBalance();
        revisionBalanceSearchBeanList = new ArrayList<>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

        for (RevisionBalanceView balanceView : revisionBalanceList) {
            RevisionBalanceSearchBean newItemsSearch = new RevisionBalanceSearchBean();
            try {
                newItemsSearch.setDateFrom(dateFormat.parse(balanceView.getDate()));
                newItemsSearch.setDateTo(dateFormat.parse(balanceView.getDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            revisionBalanceSearchBeanList.add(newItemsSearch);
        }
    }

    public void filter() {
        if (dateFrom != null || dateTo != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            try {
                Date datefrom = dateFormat.parse(dateFrom);
                Date dateto = dateFormat.parse(dateTo);
                revisionBalanceList = revisionBalanceViewService.getRevisionBalanceByDate(datefrom, dateto);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (accountIdFrom != null || accountIdTo != null) {
            revisionBalanceList = revisionBalanceViewService.getRevisionBalanceByAccountNumber(accountIdFrom);
        } else {
            revisionBalanceList = revisionBalanceViewService.getAllRevisionBalance();
        }
        revisionBalanceSearchBeanList = new ArrayList<>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        for (RevisionBalanceView balanceView : revisionBalanceList) {
            RevisionBalanceSearchBean newItemsSearch = new RevisionBalanceSearchBean();
            try {
                newItemsSearch.setDateFrom(dateFormat.parse(balanceView.getDate()));
                newItemsSearch.setDateTo(dateFormat.parse(balanceView.getDate()));

            } catch (ParseException e) {
                e.printStackTrace();
            }
            revisionBalanceSearchBeanList.add(newItemsSearch);
        }
    }

    public void reset() {
        //revisionBalanceViewList = new ArrayList<>(0);

        dateFrom = "";
        dateTo = "";
        levelFrom = null;
        levelTo= null;
        accountIdFrom= null;
        accountIdTo= null;
        adminUnitFrom= null;
        adminUnitTo= null;
//    private int adminUnitId;


        revisionBalanceSearchBeanList = new ArrayList<>();
    }

    public void search() {
        revisionBalanceViewList = new ArrayList<>(0);
        // revisionBalanceViewList =
        // revisionBalanceViewService.getRevisionBalanceViewList(revisionBalanceSearchBean);
    }

    public void print(ActionEvent actionEvent) throws JRException, IOException {

    }

    public List<RevisionBalanceView> getRevisionBalanceViewList() {
        return revisionBalanceViewList;
    }

    public void setRevisionBalanceViewList(List<RevisionBalanceView> revisionBalanceViewList) {
        this.revisionBalanceViewList = revisionBalanceViewList;
    }

    public RevisionBalanceViewService getRevisionBalanceViewService() {
        return revisionBalanceViewService;
    }

    public void setRevisionBalanceViewService(RevisionBalanceViewService revisionBalanceViewService) {
        this.revisionBalanceViewService = revisionBalanceViewService;
    }

    public JasperPrint getJasperPrint() {
        return jasperPrint;
    }

    public void setJasperPrint(JasperPrint jasperPrint) {
        this.jasperPrint = jasperPrint;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public List<RevisionBalanceView> getRevisionBalanceList() {
        return revisionBalanceList;
    }

    public void setRevisionBalanceList(List<RevisionBalanceView> revisionBalanceList) {
        this.revisionBalanceList = revisionBalanceList;
    }

    public List<RevisionBalanceSearchBean> getRevisionBalanceSearchBeanList() {
        return revisionBalanceSearchBeanList;
    }

    public void setRevisionBalanceSearchBeanList(List<RevisionBalanceSearchBean> revisionBalanceSearchBeanList) {
        this.revisionBalanceSearchBeanList = revisionBalanceSearchBeanList;
    }

    public Integer getLevelFrom() {
        return levelFrom;
    }

    public void setLevelFrom(Integer levelFrom) {
        this.levelFrom = levelFrom;
    }

    public Integer getLevelTo() {
        return levelTo;
    }

    public void setLevelTo(Integer levelTo) {
        this.levelTo = levelTo;
    }

    public Integer getAccountIdFrom() {
        return accountIdFrom;
    }

    public void setAccountIdFrom(Integer accountIdFrom) {
        this.accountIdFrom = accountIdFrom;
    }

    public Integer getAccountIdTo() {
        return accountIdTo;
    }

    public void setAccountIdTo(Integer accountIdTo) {
        this.accountIdTo = accountIdTo;
    }

    public Integer getAdminUnitFrom() {
        return adminUnitFrom;
    }

    public void setAdminUnitFrom(Integer adminUnitFrom) {
        this.adminUnitFrom = adminUnitFrom;
    }

    public Integer getAdminUnitTo() {
        return adminUnitTo;
    }

    public void setAdminUnitTo(Integer adminUnitTo) {
        this.adminUnitTo = adminUnitTo;
    }

    public int getAdminUnitId() {
        return adminUnitId;
    }

    public void setAdminUnitId(int adminUnitId) {
        this.adminUnitId = adminUnitId;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

}
