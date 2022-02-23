/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.HREmployee;

import com.isag.businessservice.EmployeeService;
import com.isag.businessservice.HrEmpQualificationsService;
import com.isag.businessservice.HrEmployeeFellowsService;
import com.isag.businessservice.HrSponsorService;
import com.isag.dto.EmployeeDTO;
import com.isag.dto.HrEmpQualificationsDTO;
import com.isag.dto.HrEmployeeFellowsDTO;
import com.isag.dto.HrSponsorDTO;
import com.isag.isag.HRBasicDataForm;
import com.isag.isag.UserData;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Ahmed Shaaban
 */
@Named(value = "employeeFormMB")
@ViewScoped
public class employeeFormMB extends HRBasicDataForm {

    private EmployeeDTO employeeDTO;
    private String hrSponsorName;
    private HrEmployeeFellowsDTO hrEmployeeFellowsDTOSelected;
    private HrEmpQualificationsDTO hrEmpQualificationsDTOSelected;

    private Integer index = 999999;

    @EJB
    EmployeeService employeeService;
    @EJB
    HrSponsorService hrSponsorService;
    @EJB
    HrEmployeeFellowsService employeeFellowsService;
    @EJB
    HrEmpQualificationsService empQualificationsService;

    @Override
    public String save() {
        try {

            if (getUri().contains("rememployee")) {
                employeeDTO.setRemType(1);
            } else if (getUri().contains("remdrivers")) {
                employeeDTO.setRemType(2);
            }
            employeeDTO = employeeService.addEmployee(employeeDTO, getUserData().getUser());
            if (employeeDTO.getMsg() != null && !employeeDTO.getMsg().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), employeeDTO.getMsg()));
            } else {

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), "تم الحفظ بنجاح"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "حدث خطأ!"));
            e.printStackTrace();
        }
        return "";
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            // load the basic user data and then fill the lists and autocompletes once page loads.
            load();
            getDepartmentDTOList();
            getSponsorDTOList();
            getDeisgnationList();
            getRelationList();
            getReligionList();
            getPlaceBirth();
            getMaritalStatusList();
            getGenderList();
            getJobGradList();
            getEducationLevelList();
            employeeDTO.getHrEmergencyContactDTO();
        } catch (Exception e) {
            saveError(e, "employeeFormMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            getUserData().getUser().setBranchId(getUserData().getUserBranch());
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("AddemployeeDTOSelected")) {
                reset();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditemployeeDTOSelected")) {
                Integer employeeId = (Integer) context.getSessionMap().get("employeeDTOSelected");
                employeeDTO = employeeService.findEmployee(employeeId);

            }
        } catch (Exception e) {
            saveError(e, "employeeFormMB", "load");
        }
    }

    public String cancel() {
        try {
            exit("../HREmployee/employeeList.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "employeeFormMB", "cancel");
            return null;
        }
    }

    public String saveDialog() {

        // meant to save the data of the sponsor comes from the dialog save button.
        try {
            if (getHrSponsorName() != null && !getHrSponsorName().isEmpty()) {
                HrSponsorDTO hrSponsorDTO = new HrSponsorDTO();
                hrSponsorDTO.setName(getHrSponsorName());
                hrSponsorDTO = hrSponsorService.addHrSponsor(hrSponsorDTO, getUserData().getUser());

                if (hrSponsorDTO.getMsg() != null && !hrSponsorDTO.getMsg().isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), hrSponsorDTO.getMsg()));
                } else {
                    getSponsorDTOList().add(0, hrSponsorDTO);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), "تم الحفظ بنجاح"));
                }
                CloseDlg("dlg1");
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "حدث خطأ!"));
            e.printStackTrace();
        }
        return "";

    }

    public void deleteFellows() {
        // this method was implemented to delete the data of the fellows table based on the employeeID
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            if (hrEmployeeFellowsDTOSelected != null) {
                try {
                    if (hrEmployeeFellowsDTOSelected != null) {
                        if (hrEmployeeFellowsDTOSelected.getId() != null) {
                            employeeFellowsService.deleteHrEmployeeFellows(hrEmployeeFellowsDTOSelected, getUserData().getUser());
                        }
                        employeeDTO.getHrEmployeeFellowsDTOList().remove(hrEmployeeFellowsDTOSelected);
                    }
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "employeeFormMB", "delete");
        }

    }

    public void deleteQualifications() {
        // this method was implemented to delete the data of the qulaifications table based on the employeeID
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            if (getHrEmpQualificationsDTOSelected() != null) {
                try {
                    if (getHrEmpQualificationsDTOSelected() != null) {
                        if (getHrEmpQualificationsDTOSelected().getId() != null) {
                            empQualificationsService.deleteHrEmpQualifications(getHrEmpQualificationsDTOSelected(), getUserData().getUser());
                        }
                        employeeDTO.getHrEmpQualificationsDTOList().remove(getHrEmpQualificationsDTOSelected());
                    }
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "employeeFormMB", "deleteQualifications");
        }

    }

    public void OpenDlg(String dlgvar) {
        // A method called by the add new button to open the dialog.    

        try {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('" + dlgvar + "').show();");
        } catch (Exception e) {
            saveError(e, "employeeFormMB", "OpenDlg");
        }
    }

    public void CloseDlg(String dlgvar) {
        // Once the sponsor name is saved this method works to close the dialog
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('" + dlgvar + "').hide();");
        } catch (Exception e) {
            saveError(e, "employeeFormMB", "CloseDlg");
        }
    }

    public void onEmployeeListChange() {
        try {
            RequestContext.getCurrentInstance().update("ScreenAddEditForm:com");
        } catch (Exception e) {
            saveError(e, "employeeFormMB", "onDepartmentListChange");
        }
    }

    public void addFellowRow() {
        //implemented to add a new empty row on the fellows datatable
        try {
            HrEmployeeFellowsDTO hrEmployeeFellowsDTO = new HrEmployeeFellowsDTO();
            setIndex(getIndex() + 1);
            hrEmployeeFellowsDTO.setIndex(getIndex());
            employeeDTO.getHrEmployeeFellowsDTOList().add(hrEmployeeFellowsDTO);

        } catch (Exception e) {
            saveError(e, "employeeFormMB", "addFellowRow");
        }
    }

    public void addQualificationsRow() {
        //implemented to add a new empty row on the qualifications datatable
        try {
            HrEmpQualificationsDTO hrEmpQualificationsDTO = new HrEmpQualificationsDTO();
            setIndex(getIndex() + 1);
            hrEmpQualificationsDTO.setIndex(getIndex());
            employeeDTO.getHrEmpQualificationsDTOList().add(hrEmpQualificationsDTO);

        } catch (Exception e) {
            saveError(e, "employeeFormMB", "addQualificationsRow");
        }
    }

    public void reset() {

        employeeDTO = new EmployeeDTO();
    }

    public String exit() {
        try {
            exit("../hremployee/employeeList.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "employeeFormMB", "exit");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the employeeDTO
     */
    public EmployeeDTO getEmployeeDTO() {
        if (employeeDTO == null) {
            employeeDTO = new EmployeeDTO();
        }
        return employeeDTO;

    }

    /**
     * @return the hrSponsorName
     */
    public String getHrSponsorName() {
        return hrSponsorName;
    }

    /**
     * @param hrSponsorName the hrSponsorName to set
     */
    public void setHrSponsorName(String hrSponsorName) {
        this.hrSponsorName = hrSponsorName;
    }

    /**
     * @return the hrEmployeeFellowsDTOSelected
     */
    public HrEmployeeFellowsDTO getHrEmployeeFellowsDTOSelected() {
        return hrEmployeeFellowsDTOSelected;
    }

    /**
     * @param hrEmployeeFellowsDTOSelected the hrEmployeeFellowsDTOSelected to
     * set
     */
    public void setHrEmployeeFellowsDTOSelected(HrEmployeeFellowsDTO hrEmployeeFellowsDTOSelected) {
        this.hrEmployeeFellowsDTOSelected = hrEmployeeFellowsDTOSelected;
    }

    /**
     * @return the hrEmpQualificationsDTOSelected
     */
    public HrEmpQualificationsDTO getHrEmpQualificationsDTOSelected() {
        return hrEmpQualificationsDTOSelected;
    }

    /**
     * @param hrEmpQualificationsDTOSelected the hrEmpQualificationsDTOSelected
     * to set
     */
    public void setHrEmpQualificationsDTOSelected(HrEmpQualificationsDTO hrEmpQualificationsDTOSelected) {
        this.hrEmpQualificationsDTOSelected = hrEmpQualificationsDTOSelected;
    }

    /**
     * @return the index
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(Integer index) {
        this.index = index;
    }
}
