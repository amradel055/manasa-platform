/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.generalbudgetguide;

import com.toby.businessservice.CompanyService;
import com.toby.businessservice.GeneralBudgetGuideService;
import com.toby.converter.GlaccountConverter;
import com.toby.define.GroupItemEnum;
import com.toby.entity.GeneralBudgetGuide;
import com.toby.entity.GlAccount;
import com.toby.entity.TobyCompany;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author ahmedabasy
 */
@Named(value = "generalBudgetFormBean")
@ViewScoped
public class GeneralBudgetFormBean extends BaseFormBean implements Serializable {

    private Integer serial;
    private Integer generalBudgetId;
    private String nameAr;
    private String nameEn;
    private String ScreenMode;
    private UserData user;
    private boolean showMessage = Boolean.FALSE;
    private GroupItemEnum groupItemEnum;
    private Integer selectedCompanyId;
    private List<TobyCompany> userCompany;
    private GlAccount selectedAccount;
    private GlaccountConverter glaccountConverter;

    @EJB
    private GeneralBudgetGuideService budgetGuideService;

    @EJB
    private CompanyService companyService;

    public GeneralBudgetFormBean() {
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "GeneralBudgetFormBean", "init");
        }
    }

    @Override
    public String save() {
        try {
            showMessage = Boolean.TRUE;
            GeneralBudgetGuide budgetGuide = new GeneralBudgetGuide();
            validateSave();
            if (showMessage) {
                budgetGuide.setNameAr(getNameAr());
                budgetGuide.setNameEn(getNameEn());
                budgetGuide.setNumber(getSerial().toString());
                budgetGuide.setAccGroup(groupItemEnum);
                budgetGuide.setAccountGroup(getSelectedAccount());
                budgetGuide.setBranchId(user.getUserBranch());
                if (user.getCompany() != null) {
                    budgetGuide.setCompanyId(user.getCompany());
                } else if (selectedCompanyId != null) {
                    TobyCompany companySelected = new TobyCompany();
                    companySelected.setId(selectedCompanyId);
                    budgetGuide.setCompanyId(companySelected);
                }
                if (generalBudgetId == null) {
                    budgetGuide.setCreatedBy(user.getUser());
                    budgetGuide.setCreationDate(new Date());
                } else {
                    budgetGuide.setModifiedBy(user.getUser());
                    budgetGuide.setModificationDate(new Date());
                }

                if (getScreenMode().equalsIgnoreCase("add")) {
                    try {
                        budgetGuide = budgetGuideService.addGeneralBudgetGuide(budgetGuide);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, user.getUserDDs().get("INFO"), user.getUserDDs().get("SAVED")));
                        exit("../generalbudget/generalbudget.xhtml");
                    } catch (Exception e) {
                        e.printStackTrace();
                        e.getMessage();
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, user.getUserDDs().get("ERROR"), user.getUserDDs().get("UNIQE_SERIAL")));
                        return "";
                    }

                } else if (getScreenMode().equalsIgnoreCase("edit")) {
                    try {
                        budgetGuide.setId(generalBudgetId);
                        budgetGuideService.updateGeneralBudgetGuide(budgetGuide);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, user.getUserDDs().get("INFO"), user.getUserDDs().get("SAVED")));
                        exit("../generalbudget/generalbudget.xhtml");
                    } catch (Exception e) {
                        e.printStackTrace();
                        e.getMessage();
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, user.getUserDDs().get("ERROR"), user.getUserDDs().get("UNIQE_SERIAL")));
                        return "";
                    }
                }
            }
            return "";
        } catch (Exception e) {
            saveError(e, "GeneralBudgetFormBean", "save");
            return null;
        }

    }

    @Override
    public void load() {
        try {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        user = (UserData) context.getSessionMap().get("userLogInData");
        ScreenMode = ((String) context.getSessionMap().get("ScreenMode"));

        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("add")) {
            setGeneralBudgetDataEmpty();

        } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
            try {
                generalBudgetId = (Integer) context.getSessionMap().get("budgetGuideId");
                setEditedGeneralBudgetValues(generalBudgetId);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
         } catch (Exception e) {
            saveError(e, "GeneralBudgetFormBean", "load");
        }
    }

    private void setGeneralBudgetDataEmpty() {
        try {
        setSerial(null);
        setNameAr("");
        setNameEn("");
        setSelectedAccount(null);
        userCompany = new ArrayList<>();
        if (user.getCompany() != null) {
            userCompany.add(user.getCompany());
            selectedCompanyId = user.getCompany().getId();
        } else {
            try {
                userCompany = companyService.getAllCompanies();
            } catch (Exception e) {
            }
        }
        } catch (Exception e) {
            saveError(e, "GeneralBudgetFormBean", "setGeneralBudgetDataEmpty");
        }
    }

    private void setEditedGeneralBudgetValues(Integer generalBudgetId) {
        try {
        GeneralBudgetGuide budgetGuide = budgetGuideService.findGeneralBudgetGuide(generalBudgetId);

        setSerial(Integer.parseInt(budgetGuide.getNumber()));
        setNameAr(budgetGuide.getNameAr());
        setNameEn(budgetGuide.getNameEn());
        setSelectedAccount(budgetGuide.getAccountGroup() != null ? budgetGuide.getAccountGroup() : null);
        setGroupItemEnum(budgetGuide.getAccGroup());

        userCompany = new ArrayList<>();
        if (user.getCompany() != null) {
            userCompany.add(user.getCompany());

        } else {
            try {
                userCompany = companyService.getAllCompanies();
            } catch (Exception e) {
            }

        }
         } catch (Exception e) {
            saveError(e, "GeneralBudgetFormBean", "setEditedGeneralBudgetValues");
        }
    }

    public void validateSave() {
        try {
        if (nameAr == null || nameAr.isEmpty() || nameEn == null || nameEn.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, user.getUserDDs().get("ERROR"), "يجب ملئ جميع الخانات"));
            showMessage = false;
        }
        /*  if (selectedAccount == null) {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, user.getUserDDs().get("ERROR"), "يجب اختيار الحساب"));
         showMessage = false;
         }*/
        validateCode();
           } catch (Exception e) {
            saveError(e, "GeneralBudgetFormBean", "validateSave");
        }
    }

    public void validateCode() {
        try {
        if (serial != null) {
            List<GeneralBudgetGuide> generalBudgetGuides = budgetGuideService.getGeneralBudgetGuideByBranchIdAndCode(user.getUserBranch().getId(), serial.toString(), generalBudgetId);
            if (generalBudgetGuides != null && !generalBudgetGuides.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, user.getUserDDs().get("ERROR"), "لا يمكن تكرار الكود"));
                showMessage = false;
            }
        } else if (serial.toString() == null || serial.toString().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, user.getUserDDs().get("ERROR"), "لا يمكن انشاء بند بدون كود"));
            showMessage = false;
        }
          } catch (Exception e) {
            saveError(e, "GeneralBudgetFormBean", "validateCode");
        }
    }

    public String cancel() {
        try{
        exit("../generalbudget/generalbudget.xhtml");
        return "";
          } catch (Exception e) {
            saveError(e, "GeneralBudgetFormBean", "cancel");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        return "budgetGuide";
    }

    public GroupItemEnum[] getAccountGroups() {
        return GroupItemEnum.values();
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getScreenMode() {
        return ScreenMode;
    }

    public void setScreenMode(String ScreenMode) {
        this.ScreenMode = ScreenMode;
    }

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

    public boolean isShowMessage() {
        return showMessage;
    }

    public void setShowMessage(boolean showMessage) {
        this.showMessage = showMessage;
    }

    public Integer getGeneralBudgetId() {
        return generalBudgetId;
    }

    public void setGeneralBudgetId(Integer generalBudgetId) {
        this.generalBudgetId = generalBudgetId;
    }

    public GeneralBudgetGuideService getBudgetGuideService() {
        return budgetGuideService;
    }

    public void setBudgetGuideService(GeneralBudgetGuideService budgetGuideService) {
        this.budgetGuideService = budgetGuideService;
    }

    public GroupItemEnum getGroupItemEnum() {
        return groupItemEnum;
    }

    public void setGroupItemEnum(GroupItemEnum groupItemEnum) {
        this.groupItemEnum = groupItemEnum;
    }

    public Integer getSelectedCompanyId() {
        return selectedCompanyId;
    }

    public void setSelectedCompanyId(Integer selectedCompanyId) {
        this.selectedCompanyId = selectedCompanyId;
    }

    public List<TobyCompany> getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(List<TobyCompany> userCompany) {
        this.userCompany = userCompany;
    }

    public CompanyService getCompanyService() {
        return companyService;
    }

    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    /**
     * @return the selectedAccount
     */
    public GlAccount getSelectedAccount() {
        return selectedAccount;
    }

    /**
     * @param selectedAccount the selectedAccount to set
     */
    public void setSelectedAccount(GlAccount selectedAccount) {
        this.selectedAccount = selectedAccount;
    }

    /**
     * @return the glaccountConverter
     */
    public GlaccountConverter getGlaccountConverter() {
        return glaccountConverter;
    }

    /**
     * @param glaccountConverter the glaccountConverter to set
     */
    public void setGlaccountConverter(GlaccountConverter glaccountConverter) {
        this.glaccountConverter = glaccountConverter;
    }

}
