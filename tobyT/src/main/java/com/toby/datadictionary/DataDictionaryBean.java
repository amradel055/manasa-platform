/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.datadictionary;

import com.toby.businessservice.CompanyService;
import com.toby.businessservice.DDService;
import com.toby.entity.DataDictionary;
import com.toby.entity.TobyCompany;
import com.toby.entity.Symbol;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;

@Named(value = "dataDictionaryBean")
@ViewScoped
public class DataDictionaryBean extends BaseListBean implements Serializable {

    private DataDictionary dataDicionarySelected;
    private String propertyKey;
    private String propertyValueMother;
    private String propertyValueForiegn;
    private List<DataDictionary> dataDictionaryList;
    private Integer companyId;
    ;
    private List<TobyCompany> listCompanies;
    private List<TobyCompany> listCompaniesSave;
    private UserData userData;
    private TobyCompany companyvar;
    @EJB
    private CompanyService companyService;
    @EJB
    private DDService dDService;

    public DataDictionaryBean() {
    }

    @Override
    @PostConstruct
    public void init() {
        load();
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void filter() {

    }

    @Override
    public String goToAdd() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToEdit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");
            if (getUserData().getCompany() != null && getUserData().getCompany().getId() != null) {
                companyId = userData.getCompany().getId();
                dataDictionaryList = dDService.getALLDataDictionaryByCompanyId(companyId);

            } else {
                listCompanies = new ArrayList<>();
                listCompanies = companyService.getAllCompanies();

            }
        } catch (Exception e) {
            saveError(e, "DataDictionaryBean", "load");
        }
    }

    public void onCompanyChange() {
        try {
            TobyCompany company = new TobyCompany();
            if (getCompanyId() != null) {
                company.setId(getCompanyId());
                setDataDictionaryList(getdDService().getALLDataDictionaryByCompanyId(getCompanyId()));
            }
        } catch (Exception e) {
            saveError(e, "DataDictionaryBean", "onCompanyChange");
        }
    }

    public void save() {
        try {
            List<DataDictionary> dataDicionaryList = new ArrayList<>();
            Symbol langM = new Symbol();
            Symbol langF = new Symbol();
            langM.setId(26);
            langF.setId(27);
            listCompaniesSave = companyService.getAllCompanies();

            for (TobyCompany company : listCompaniesSave) {
                settingDataDicionary(langM, company, langF, dataDicionaryList);
            }

            settingDataDicionary(langM, null, langF, dataDicionaryList);

            dDService.saveAll(dataDicionaryList);
        } catch (Exception e) {
            saveError(e, "DataDictionaryBean", "save");
        }
    }

    public void settingDataDicionary(Symbol langM, TobyCompany company, Symbol langF, List<DataDictionary> dataDicionaryList) {
        try {
            DataDictionary dataDicionaryMother;
            DataDictionary dataDicionaryForiegn;
            dataDicionaryMother = new DataDictionary();
            dataDicionaryForiegn = new DataDictionary();
            dataDicionaryMother.setCreatedBy(userData.getUser());
            dataDicionaryMother.setCreationDate(new Date());
            dataDicionaryMother.setLang(langM);
            dataDicionaryMother.setPropertyKey(propertyKey);
            dataDicionaryMother.setPropertyValue(propertyValueMother);
            dataDicionaryMother.setCompanyId(company);
            dataDicionaryForiegn.setCreatedBy(userData.getUser());
            dataDicionaryForiegn.setCreationDate(new Date());
            dataDicionaryForiegn.setLang(langF);
            dataDicionaryForiegn.setPropertyKey(propertyKey);
            dataDicionaryForiegn.setPropertyValue(propertyValueForiegn);
            dataDicionaryForiegn.setCompanyId(company);
            dataDicionaryList.add(dataDicionaryMother);
            dataDicionaryList.add(dataDicionaryForiegn);
        } catch (Exception e) {
            saveError(e, "DataDictionaryBean", "settingDataDicionary");
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void onRowEdit(RowEditEvent e) {
        try {
            setDataDicionarySelected((DataDictionary) e.getObject());
            if (getDataDicionarySelected() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(getCompanyId());
                getDataDicionarySelected().setCompanyId(company);
                getDataDicionarySelected().setModificationDate(new Date());
                getDataDicionarySelected().setModifiedBy(getUserData().getUser());
                dDService.update(dataDicionarySelected);
            }
        } catch (Exception ex) {
            saveError(ex, "DataDictionaryBean", "onRowEdit");
        }
    }

    public void onRowCancel() {

    }
//    public void onRowSelect(SelectEvent e)
//       {
//      dataDicionarySelected  =((DataDictionary) e.getObject());
//        if (dataDicionarySelected != null) {
//            setDataDicionarySelected(dDService.getALLDataDictionaryByCompanyId(dataDicionarySelected.getId()));
//       }
//       }

    public void onRowUnselect() {
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public void setListCompanies(List<TobyCompany> listCompanies) {
        this.listCompanies = listCompanies;
    }

    public List<TobyCompany> getListCompanies() {
        return listCompanies;
    }

    public DataDictionary getDd() {
        return getDataDicionarySelected();
    }

    public void setDd(DataDictionary dataDicionarySelected) {
        this.setDataDicionarySelected(dataDicionarySelected);
    }

    public List<DataDictionary> getDataDictionaryList() {
        return dataDictionaryList;
    }

    public void setDataDictionaryList(List<DataDictionary> dataDictionaryList) {
        this.dataDictionaryList = dataDictionaryList;
    }

    public CompanyService getCompanyService() {
        return companyService;
    }

    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    public DDService getdDService() {
        return dDService;
    }

    public void setdDService(DDService dDService) {
        this.dDService = dDService;
    }

    public DataDictionary getDataDicionarySelected() {
        return dataDicionarySelected;
    }

    public void setDataDicionarySelected(DataDictionary dataDicionarySelected) {
        this.dataDicionarySelected = dataDicionarySelected;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    /**
     * @return the companyvar
     */
    public TobyCompany getCompanyvar() {
        return companyvar;
    }

    /**
     * @param companyvar the companyvar to set
     */
    public void setCompanyvar(TobyCompany companyvar) {
        this.companyvar = companyvar;
    }

    /**
     * @return the propertyKey
     */
    public String getPropertyKey() {
        return propertyKey;
    }

    /**
     * @param propertyKey the propertyKey to set
     */
    public void setPropertyKey(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    /**
     * @return the propertyValueMother
     */
    public String getPropertyValueMother() {
        return propertyValueMother;
    }

    /**
     * @param propertyValueMother the propertyValueMother to set
     */
    public void setPropertyValueMother(String propertyValueMother) {
        this.propertyValueMother = propertyValueMother;
    }

    /**
     * @return the propertyValueForiegn
     */
    public String getPropertyValueForiegn() {
        return propertyValueForiegn;
    }

    /**
     * @param propertyValueForiegn the propertyValueForiegn to set
     */
    public void setPropertyValueForiegn(String propertyValueForiegn) {
        this.propertyValueForiegn = propertyValueForiegn;
    }

}
