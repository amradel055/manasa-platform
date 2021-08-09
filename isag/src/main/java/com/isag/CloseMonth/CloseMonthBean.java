/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.CloseMonth;

import com.isag.businessservice.BranchService;
import com.isag.businessservice.CloseAndSaveMonthService;
import com.isag.businessservice.GlYearService;
import com.isag.entity.CloseAndSaveMonth;
import com.isag.entity.GlYear;
import com.isag.entity.IsagCompany;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author hq002
 */
@Named(value = "closeMonthBean")
@ViewScoped
public class CloseMonthBean extends BaseFormBean {

    private UserData userData;
    private IsagCompany selectedCompanyId;
    private List<GlYear> glYears;
    private List<CloseAndSaveMonth> closeMonths;
    private CloseAndSaveMonth selectedCloseMonth;
    private List<Integer> MonthsNumbers;
    private List<String> MonthsNames;
    private Integer selectBranch;
    private Integer monthNumber;
    private String monthNameValue;
    @EJB
    private CloseAndSaveMonthService closeMonthService;
    @EJB
    private GlYearService glYearService;
    @EJB
    private BranchService branchService;

    @Override
    public String save() {
        try {
            if (selectedCloseMonth != null) {
                if (monthNumber != null && monthNameValue != null) {
                    selectedCloseMonth.setMonthNumber(monthNumber);
                    selectedCloseMonth.setMonthName(monthNameValue);
                }
                List<CloseAndSaveMonth> closeMonthlist;
                if (selectedCloseMonth.getId() != null) {
                    closeMonthlist = closeMonthService.findCloseAndSaveMonth(selectedCloseMonth.getId(), selectedCloseMonth.getMonthNumber(), selectedCloseMonth.getYear().getId(), userData.getUserBranch().getId());
                } else {
                    closeMonthlist = closeMonthService.findCloseAndSaveMonth(null, selectedCloseMonth.getMonthNumber(), selectedCloseMonth.getYear().getId(), userData.getUserBranch().getId());
                }

                if (closeMonthlist == null || closeMonthlist.size() <= 0) {
                    if (selectedCloseMonth.getId() != null) {
                        selectedCloseMonth.setModificationDate(new Date());
                        selectedCloseMonth.setModifiedBy(userData.getUser());
                        selectedCloseMonth.setMarkEdit(Boolean.FALSE);
                        try {
                            closeMonthService.updateCloseAndSaveMonth(selectedCloseMonth);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userData.getUserDDs().get("INFO"), userData.getUserDDs().get("SAVED")));
                            setMonthNameValue(null);
                            monthNumber = null;
                            load();
                        } catch (Exception e) {
                            e.printStackTrace();
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userData.getUserDDs().get("ERROR"), userData.getUserDDs().get("SAVED_ERROR")));
                        }

                    } else {
                        selectedCloseMonth.setBranchId(branchService.findBranch(selectBranch));
                        selectedCloseMonth.setCompanyId(selectedCompanyId);
                        selectedCloseMonth.setMarkEdit(Boolean.FALSE);
                        selectedCloseMonth.setCreationDate(new Date());
                        selectedCloseMonth.setCreatedBy(userData.getUser());
                        try {
                            closeMonthService.addCloseAndSaveMonth(selectedCloseMonth);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userData.getUserDDs().get("INFO"), userData.getUserDDs().get("SAVED")));
                            load();
                            setMonthNameValue(null);
                            monthNumber = null;
                        } catch (Exception e) {
                            e.printStackTrace();
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userData.getUserDDs().get("ERROR"), userData.getUserDDs().get("SAVED_ERROR")));
                        }

                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "غير مسموح بتكرار الشهر على نفس السنه الماليه", userData.getUserDDs().get("SAVED_ERROR")));
                }
            }
            return "";
        } catch (Exception ex) {
            saveError(ex, "CloseMonthBean", "save");
            return null;
        }
    }

    @PostConstruct
    @Override
    public void init() {
        try {
            load();
        } catch (Exception ex) {
            saveError(ex, "CloseMonthBean", "init");
        }
    }

    @Override
    public void load() {
        try {
            MonthsNumbers = new ArrayList<>();
            selectedCloseMonth = new CloseAndSaveMonth();
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");
            if (userData.getCompany() != null && userData.getSelectedBranch() != null && userData.getCompany().getId() != null) {
                selectedCompanyId = userData.getCompany();
                selectBranch = userData.getSelectedBranch();
                closeMonths = closeMonthService.getAllCloseAndSaveMonthsByBranchId(selectBranch);
                glYears = glYearService.getALLGlyearByBranchId(selectBranch);
                for (int i = 1; i < 13; i++) {
                    MonthsNumbers.add(new Integer(i));
                }

            }
        } catch (Exception ex) {
            saveError(ex, "CloseMonthBean", "load");
        }
    }

    public void edit() {
        try {
            for (CloseAndSaveMonth closeMonth : closeMonths) {
                closeMonth.setMarkEdit(Boolean.FALSE);
            }

            monthNumber = selectedCloseMonth.getMonthNumber();
            monthNameValue = selectedCloseMonth.getMonthName();
            selectedCloseMonth.setMarkEdit(Boolean.TRUE);
        } catch (Exception ex) {
            saveError(ex, "CloseMonthBean", "edit");
        }

    }

    public void setMonthName() {
        try {
            setMonthNameValue(null);
            boolean lang = Boolean.TRUE;
            if (userData.getUser().getLang().getName().equalsIgnoreCase("العربية")) {
                lang = Boolean.FALSE;
            }
            Integer month = monthNumber;
            switch (month) {
                case 1:
                    setMonthNameValue(lang != Boolean.TRUE ? "يناير" : "January");
                    break;
                case 2:
                    setMonthNameValue(lang != true ? "فبراير" : "February");
                    break;
                case 3:
                    setMonthNameValue(lang != true ? "مارس" : "March");
                    break;
                case 4:
                    setMonthNameValue(lang != true ? "ابريل" : "April");
                    break;
                case 5:
                    setMonthNameValue(lang != true ? "مايو" : "May");
                    break;
                case 6:
                    setMonthNameValue(lang != true ? "يونيو" : "June");
                    break;
                case 7:
                    setMonthNameValue(lang != true ? "يوليو" : "July");
                    break;
                case 8:
                    setMonthNameValue(lang != true ? "اغسطس" : "August");
                    break;
                case 9:
                    setMonthNameValue(lang != true ? "سبتمبر" : "September");
                    break;
                case 10:
                    setMonthNameValue(lang != true ? "اكتوبر" : "October");
                    break;
                case 11:
                    setMonthNameValue(lang != true ? "نوفمبر" : "November");
                    break;
                case 12:
                    setMonthNameValue(lang != true ? "ديسمبر" : "December");
                    break;

            }
        } catch (Exception ex) {
            saveError(ex, "CloseMonthBean", "setMonthName");
        }
    }

    public void reload() {
        try {
            selectedCloseMonth.setMarkEdit(Boolean.FALSE);
            if (selectedCloseMonth.getId() == null) {
                closeMonths.remove(selectedCloseMonth);
            }
        } catch (Exception ex) {
            saveError(ex, "CloseMonthBean", "reload");
        }
    }

    public void addRow() {
        try {

            CloseAndSaveMonth closeMonth = new CloseAndSaveMonth();
            closeMonth.setYear(new GlYear());
            closeMonth.setMarkEdit(Boolean.TRUE);
            closeMonths.add(0, closeMonth);
        } catch (Exception ex) {
            saveError(ex, "CloseMonthBean", "addRow");
        }

    }

    public void delete() {
        try {
            Map<String, String> userDDs = userData.getUserDDs();
            if (selectedCloseMonth != null && selectedCloseMonth.getId() != null) {
                try {
                    closeMonthService.deleteCloseAndSaveMonth(selectedCloseMonth);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                    load();
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }

            } else {
                closeMonths.remove(0);
            }
        } catch (Exception ex) {
            saveError(ex, "CloseMonthBean", "delete");
        }
    }

    public void setMonthsNames() {
        try {
            MonthsNames = new ArrayList<>();
            if (userData.getUser().getLang().getName().equalsIgnoreCase("العربية")) {
                MonthsNames.add("يناير");
                MonthsNames.add("فبراير");
                MonthsNames.add("مارس");
                MonthsNames.add("ابريل");
                MonthsNames.add("مايو");
                MonthsNames.add("يونيو");
                MonthsNames.add("يوليو");
                MonthsNames.add("اغسطس");
                MonthsNames.add("سبتمبر");
                MonthsNames.add("اكتوبر");
                MonthsNames.add("نوفمبر");
                MonthsNames.add("ديسمبر");

            } else {
                MonthsNames.add("January");
                MonthsNames.add("February");
                MonthsNames.add("March");
                MonthsNames.add("April");
                MonthsNames.add("May");
                MonthsNames.add("June");
                MonthsNames.add("July");
                MonthsNames.add("August");
                MonthsNames.add("September");
                MonthsNames.add("October");
                MonthsNames.add("November");
                MonthsNames.add("December");

            }
        } catch (Exception ex) {
            saveError(ex, "CloseMonthBean", "setMonthsNames");
        }
    }

    @Override
    public String getScreenName() {
        return "closemonth";
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public IsagCompany getSelectedCompanyId() {
        return selectedCompanyId;
    }

    public void setSelectedCompanyId(IsagCompany selectedCompanyId) {
        this.selectedCompanyId = selectedCompanyId;
    }

    public List<GlYear> getGlYears() {
        return glYears;
    }

    public void setGlYears(List<GlYear> glYears) {
        this.glYears = glYears;
    }

    public List<CloseAndSaveMonth> getCloseMonths() {
        return closeMonths;
    }

    public void setCloseMonths(List<CloseAndSaveMonth> closeMonths) {
        this.closeMonths = closeMonths;
    }

    public List<Integer> getMonthsNumbers() {
        return MonthsNumbers;
    }

    public void setMonthsNumbers(List<Integer> MonthsNumbers) {
        this.MonthsNumbers = MonthsNumbers;
    }

    public Integer getSelectBranch() {
        return selectBranch;
    }

    public void setSelectBranch(Integer selectBranch) {
        this.selectBranch = selectBranch;
    }

    public CloseAndSaveMonth getSelectedCloseMonth() {
        return selectedCloseMonth;
    }

    public void setSelectedCloseMonth(CloseAndSaveMonth selectedCloseMonth) {
        this.selectedCloseMonth = selectedCloseMonth;
    }

    public List<String> getMonthsNames() {
        return MonthsNames;
    }

    public void setMonthsNames(List<String> MonthsNames) {
        this.MonthsNames = MonthsNames;
    }

    public Integer getMonthNumber() {
        return monthNumber;
    }

    public void setMonthNumber(Integer monthNumber) {
        this.monthNumber = monthNumber;
    }

    public String getMonthNameValue() {
        return monthNameValue;
    }

    public void setMonthNameValue(String monthNameValue) {
        this.monthNameValue = monthNameValue;
    }

}
