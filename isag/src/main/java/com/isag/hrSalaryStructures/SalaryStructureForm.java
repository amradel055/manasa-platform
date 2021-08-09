package com.isag.hrSalaryStructures;

import com.isag.businessservice.EmployeeService;
import com.isag.businessservice.HrSalariesEffectService;
import com.isag.businessservice.HrSalaryStructureAccuralDeductionEffectTypeService;
import com.isag.businessservice.HrSalaryStructureService;
import com.isag.businessservice.RscAccrualDeductionService;
import com.isag.dto.EmployeeDTO;
import com.isag.dto.HrSalaryStructureAccuralDeductionDTO;
import com.isag.dto.HrSalariesStructureEffectDTO;
import com.isag.dto.HrSalaryStructureDTO;
import com.isag.dto.RscAccrualDeductionDTO;
import com.isag.isag.HRBasicDataForm;
import com.isag.isag.UserData;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import com.isag.businessservice.HrSalaryStructureAccuralDeductionService;
import com.isag.dto.HrSalaryStructureAccuralDeductionEffectTypeDTO;
import java.util.Calendar;
import java.util.List;
import org.primefaces.event.SelectEvent;

@Named(value = "salaryStructureFormMB")
@ViewScoped
public class SalaryStructureForm extends HRBasicDataForm {

    private HrSalaryStructureDTO salaryStructureDTO;
    private HrSalariesStructureEffectDTO salariesEffectDTO;
    private HrSalaryStructureAccuralDeductionDTO accuralDTOSelected;
    private HrSalaryStructureAccuralDeductionDTO accuralAddDTOSelected;
    private HrSalaryStructureAccuralDeductionDTO deductionDTOSelected;
    private HrSalariesStructureEffectDTO hrSalariesEffectDTOSelected;
    private String uri;
    private BigDecimal totalAccural;
    private BigDecimal totalDeduction;
    private BigDecimal totalIncome;
    private BigDecimal totalIncomeAfterEffect;
    private HrSalaryStructureAccuralDeductionEffectTypeDTO hrSalaryStructureAccuralDeductionEffectTypeDTOSelected;

    @EJB
    HrSalaryStructureAccuralDeductionService hrEmployeeAccuralDeductionService;
    @EJB
    RscAccrualDeductionService accrualDeductionService;
    @EJB
    HrSalaryStructureService salaryStructureService;
    @EJB
    HrSalariesEffectService salariesEffectService;
    @EJB
    EmployeeService employeeService;
    @EJB
    HrSalaryStructureAccuralDeductionEffectTypeService hrSalaryStructureAccuralDeductionEffectTypeService;

    @Override
    @PostConstruct
    public void init() {
        try {
            // load the basic user data and then fill the lists and autocompletes once page loads.
            load();
            if (getSalaryStructureDTO().getId() == null) {
                getSalaryStructureDTO().setEntryDate(new Date());
            } else {
                salaryStructureDTO.getEntryDate();
            }

            getDepartmentDTOList();

        } catch (Exception e) {
            saveError(e, "SalaryStructureForm", "init");
        }
    }

    @Override
    public void load() {

        try {

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            getUserData().getUser().setBranchId(getUserData().getUserBranch());
            setUri(((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI());
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("AdddetailsViewSelected")) {
                reset();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("Edit")) {
                Integer id = (Integer) context.getSessionMap().get("hrSalaryStructureDTOSelected");
                salaryStructureDTO = salaryStructureService.findSalaryStructure(id);
                // getHremployeeDTOList();
                getHremployeeDTOList().add(salaryStructureDTO.getEmployeeId() != null ? salaryStructureDTO.getEmployeeId() : null);
                findEmployeeData();
                getHrSalaryEffectTypeDTOAccuralList();
                getHrSalaryEffectTypeDTODeductionList();
            }
        } catch (Exception e) {
            saveError(e, "SalaryStructureForm", "load");
        }
    }

    public void findEmployeeData() {
        try {
            if (getUri().contains("hremployeesalaryvoucher")) {
                if (getSalaryStructureDTO().getDateFrom() != null
                        && getSalaryStructureDTO().getMonths() != null && getSalaryStructureDTO().getEntryDate() != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(getSalaryStructureDTO().getEntryDate());
                    Integer salaryStructureDTOId = salaryStructureService.getSalaryStructureByEmployeeIdAndDate(getSalaryStructureDTO().getEmployeeId().getId(), getSalaryStructureDTO().getMonths(), cal.get(Calendar.YEAR));
                    if (salaryStructureDTOId != null) {
                        setSalaryStructureDTO(salaryStructureService.findSalaryStructure(salaryStructureDTOId));
                        getSalaryStructureDTO().setHrEmployeeAccuralDTOList(hrEmployeeAccuralDeductionService.findHrEmpAccuralDTOBySalaryStructureId(salaryStructureDTOId, 0));
                        addsalaryForFirstTime();
                        restrictSalary();
                        getSalaryStructureDTO().setHrEmployeeDeductionDTOList(hrEmployeeAccuralDeductionService.findHrEmpAccuralDTOBySalaryStructureId(salaryStructureDTOId, 1));
                        calculateTotal();
                        calculateTotalDeduction();
                        calculateTotalIncome();
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب تسجيل قسيمة راتب لهذا الموظف بتارخ يقع فى الفترة"));
                        return;
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار تاريخ و سنة مالية وشهر"));
                    return;
                }
            }
            getSalaryStructureDTO().setEmployeeId(employeeService.findEmployeeId(getSalaryStructureDTO().getEmployeeId().getId()));

        } catch (Exception e) {
            saveError(e, "SalaryStructureForm", "findEmployeeData");
        }
    }

    @Override
    public String save() {
        try {
            if (!getUri().contains("salaryVoucherForm")) {
                getSalaryStructureDTO().setType(0);
            } else {
                getSalaryStructureDTO().setType(1);
            }

            salaryStructureDTO = salaryStructureService.addAccuralDeduction(getSalaryStructureDTO(), getUserData().getUser());
            if (getSalaryStructureDTO().getMsg() != null && !getSalaryStructureDTO().getMsg().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), getSalaryStructureDTO().getMsg()));
            } else {

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), "تم الحفظ بنجاح"));
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "حدث خطأ!"));
            e.printStackTrace();
        }
        return "";
    }

    public void deleteAccural() {
        // this method was implemented to delete the data of the Accural table based on the employeeID
        Map<String, String> userDDs = getUserData().getUserDDs();
        try {
            if (getAccuralDTOSelected() != null) {
                if (getAccuralDTOSelected().getAccuralDeductionId().getRscAccrualDeductionType() == 0 && getAccuralDTOSelected().getAccuralDeductionId().getSerial().equals(0)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "لا يمكن مسح خانة الراتب"));
                } else {
                    if (getAccuralDTOSelected().getHrSalaryStructureAccuralEffectType() != null && !getAccuralDTOSelected().getHrSalaryStructureAccuralEffectType().isEmpty()) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                    } else {
                        if (getAccuralDTOSelected().getId() != null) {
                            getSalaryStructureDTO().getHrSalaryStructureAccuralDeductionDTODeletedList().add(getAccuralDTOSelected());
                        }
                        getSalaryStructureDTO().getHrEmployeeAccuralDTOList().remove(getAccuralDTOSelected());
                        calculateTotal();
                        calculateTotalIncome();
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                    }
                }
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "حدث خطاء"));
            saveError(e, "SalaryStructureForm", "deleteDeduction");
        }

    }

    public void deleteHrSalaryStructureAccuralDeductionEffectTypeDTO() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        try {
            if (getAccuralDTOSelected() != null) {
                if (getHrSalaryStructureAccuralDeductionEffectTypeDTOSelected() != null) {
                    if (hrSalaryStructureAccuralDeductionEffectTypeDTOSelected.getHrSalaryEffectType() != null && !getHrSalaryStructureAccuralDeductionEffectTypeDTOSelected().getId().equals(null)) {
                        accuralAddDTOSelected.getHrSalaryStructureAccuralDeductionEffectTypeDelete().add(getHrSalaryStructureAccuralDeductionEffectTypeDTOSelected());
                    }
                    if (getHrSalaryStructureAccuralDeductionEffectTypeDTOSelected().getType().equals(0)) {
                        accuralAddDTOSelected.getHrSalaryStructureAccuralEffectType().remove(getHrSalaryStructureAccuralDeductionEffectTypeDTOSelected());
                    } else if (getHrSalaryStructureAccuralDeductionEffectTypeDTOSelected().getType().equals(1)) {
                        accuralAddDTOSelected.getHrSalaryStructureDeductionEffectType().remove(getHrSalaryStructureAccuralDeductionEffectTypeDTOSelected());
                    }
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                }
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("ERROR"), userDDs.get("حدث خطاء")));
            saveError(e, "SalaryStructureForm", "deleteDeduction");
        }

    }

    public void deleteHrSalaryStructureAccuralEffectTypeDTOList() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        try {
            if (accuralAddDTOSelected != null && (accuralAddDTOSelected.getHrSalaryStructureAccuralEffectType() != null && !accuralAddDTOSelected.getHrSalaryStructureAccuralEffectType().isEmpty())) {
                List<HrSalaryStructureAccuralDeductionEffectTypeDTO> accuralDeductionEffectTypeDTOList = new ArrayList<>();
                accuralDeductionEffectTypeDTOList.addAll(accuralAddDTOSelected.getHrSalaryStructureAccuralEffectType());
                for (HrSalaryStructureAccuralDeductionEffectTypeDTO accuralDeductionEffectTypeDTO : accuralDeductionEffectTypeDTOList) {
                    if (accuralDeductionEffectTypeDTO.getHrSalaryEffectType() != null && accuralDeductionEffectTypeDTO.getId() != null) {
                        accuralAddDTOSelected.getHrSalaryStructureAccuralDeductionEffectTypeDelete().add(accuralDeductionEffectTypeDTO);
                    }
                    if (accuralDeductionEffectTypeDTO.getType().equals(0)) {
                        accuralAddDTOSelected.getHrSalaryStructureAccuralEffectType().remove(accuralDeductionEffectTypeDTO);
                    }

                    if (accuralAddDTOSelected.getHrSalaryStructureAccuralEffectType().isEmpty()) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                    }
                }
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("ERROR"), userDDs.get("حدث خطاء")));
            saveError(e, "SalaryStructureForm", "deleteHrSalaryStructureAccuralEffectTypeDTOList");
        }

    }

    public void deleteHrSalaryStructureDeductionEffectTypeDTOList() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        try {
            if (accuralAddDTOSelected != null && (accuralAddDTOSelected.getHrSalaryStructureDeductionEffectType() != null && !accuralAddDTOSelected.getHrSalaryStructureDeductionEffectType().isEmpty())) {
                List<HrSalaryStructureAccuralDeductionEffectTypeDTO> accuralDeductionEffectTypeDTOList = new ArrayList<>();
                accuralDeductionEffectTypeDTOList.addAll(accuralAddDTOSelected.getHrSalaryStructureDeductionEffectType());
                for (HrSalaryStructureAccuralDeductionEffectTypeDTO accuralDeductionEffectTypeDTO : accuralDeductionEffectTypeDTOList) {
                    if (accuralDeductionEffectTypeDTO.getHrSalaryEffectType() != null && accuralDeductionEffectTypeDTO.getId() != null) {
                        accuralAddDTOSelected.getHrSalaryStructureAccuralDeductionEffectTypeDelete().add(accuralDeductionEffectTypeDTO);
                    }
                    if (accuralDeductionEffectTypeDTO.getType().equals(1)) {
                        accuralAddDTOSelected.getHrSalaryStructureDeductionEffectType().remove(accuralDeductionEffectTypeDTO);
                    }

                    if (accuralAddDTOSelected.getHrSalaryStructureDeductionEffectType().isEmpty()) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                    }
                }
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("ERROR"), userDDs.get("حدث خطاء")));
            saveError(e, "SalaryStructureForm", "deleteHrSalaryStructureAccuralDeductionEffectTypeDTOList");
        }

    }

    public void deleteDeduction() {
        // this method was implemented to delete the data of the Deduction table based on the employeeID
        Map<String, String> userDDs = getUserData().getUserDDs();
        try {
            if (getDeductionDTOSelected() != null) {
                if (getDeductionDTOSelected().getAccuralDeductionId().getRscAccrualDeductionType() == 0 && getDeductionDTOSelected().getAccuralDeductionId().getSerial().equals(0)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "لا يمكن مسح خانة الراتب"));
                } else {
                    if (getDeductionDTOSelected().getHrSalaryStructureDeductionEffectType() != null && !getDeductionDTOSelected().getHrSalaryStructureDeductionEffectType().isEmpty()) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                    } else {
                        if (getDeductionDTOSelected().getId() != null) {
                            getSalaryStructureDTO().getHrSalaryStructureAccuralDeductionDTODeletedList().add(getDeductionDTOSelected());
                        }
                        getSalaryStructureDTO().getHrEmployeeDeductionDTOList().remove(getDeductionDTOSelected());
                        calculateTotalDeduction();
                        calculateTotalIncome();
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                    }
                }
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "حدث خطاء"));
            saveError(e, "SalaryStructureForm", "deleteDeduction");
        }
    }

    public void deleteSalariesEffect() {
        // this method was implemented to delete the data of the Accural table based on the employeeID
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            if (getHrSalariesEffectDTOSelected() != null) {
                try {
                    if (getHrSalariesEffectDTOSelected() != null) {
                        if (getHrSalariesEffectDTOSelected().getId() != null) {
                            salariesEffectService.deleteSalariesEffect(getHrSalariesEffectDTOSelected(), getUserData().getUser());
                        }
                        getSalaryStructureDTO().getSalariesEffectDTOList().remove(getHrSalariesEffectDTOSelected());

                    }
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }
            }

        } catch (Exception e) {
            saveError(e, "SalaryStructureForm", "deleteDeduction");
        }

    }

    public void accuralselect(HrSalaryStructureAccuralDeductionDTO accural) {
        setAccuralAddDTOSelected(accural);
    }

    public void deductionselect(HrSalaryStructureAccuralDeductionDTO deductionTable) {
        setDeductionDTOSelected(deductionTable);
    }

    public Boolean validateAccuralRow() {

        Boolean check = true;

        return check;

    }

    public void validateDeduction() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            Map<Integer, Integer> map = new HashMap<>();
            for (HrSalaryStructureAccuralDeductionDTO hrEmployeeAccuralDeductionDTOList : getSalaryStructureDTO().getHrEmployeeDeductionDTOList()) {

                if (hrEmployeeAccuralDeductionDTOList != null && hrEmployeeAccuralDeductionDTOList.getAccuralDeductionId() != null && hrEmployeeAccuralDeductionDTOList.getAccuralDeductionId().getSerial() != null) {
                    if (!map.containsKey(hrEmployeeAccuralDeductionDTOList.getAccuralDeductionId().getSerial())) {
                        map.put(hrEmployeeAccuralDeductionDTOList.getAccuralDeductionId().getSerial(), hrEmployeeAccuralDeductionDTOList.getAccuralDeductionId().getSerial());

                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("لا يمكن تكرار نفس العنصر "), userDDs.get("لا يمكن تكرار نفس العنصر ")));
                        hrEmployeeAccuralDeductionDTOList.setAccuralDeductionId(new RscAccrualDeductionDTO());
                    }

                }

            }
        } catch (Exception e) {
            saveError(e, "SalaryStructureForm", "validateDeduction");
        }

    }

    public void validateAccural() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            Map<Integer, Integer> map = new HashMap<>();
            for (HrSalaryStructureAccuralDeductionDTO hrEmployeeAccuralDeductionDTOList : getSalaryStructureDTO().getHrEmployeeAccuralDTOList()) {

                if (hrEmployeeAccuralDeductionDTOList != null && hrEmployeeAccuralDeductionDTOList.getAccuralDeductionId() != null && hrEmployeeAccuralDeductionDTOList.getAccuralDeductionId().getSerial() != null) {
                    if (!map.containsKey(hrEmployeeAccuralDeductionDTOList.getAccuralDeductionId().getSerial())) {
                        map.put(hrEmployeeAccuralDeductionDTOList.getAccuralDeductionId().getSerial(), hrEmployeeAccuralDeductionDTOList.getAccuralDeductionId().getSerial());

                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("لا يمكن تكرار نفس العنصر "), userDDs.get("لا يمكن تكرار نفس العنصر ")));
                        hrEmployeeAccuralDeductionDTOList.setAccuralDeductionId(new RscAccrualDeductionDTO());
                    }

                }

            }
        } catch (Exception e) {
            saveError(e, "SalaryStructureForm", "validateDeduction");
        }
    }

    public void addsalaryForFirstTime() {
        try {
            if (getSalaryStructureDTO().getHrEmployeeAccuralDTOList() == null || getSalaryStructureDTO().getHrEmployeeAccuralDTOList().isEmpty()) {

                HrSalaryStructureAccuralDeductionDTO hrEmployeeAccuralDeductionDTO = initColumnData();
                hrEmployeeAccuralDeductionDTO.setAccuralDeductionId(accrualDeductionService.getAllRscAccrualByBranchIdAndserial(getUserData().getUserBranch().getId(), 0));
                getSalaryStructureDTO().getHrEmployeeAccuralDTOList().add(hrEmployeeAccuralDeductionDTO);

            }
        } catch (Exception e) {
            saveError(e, "SalaryStructureForm", "addsalaryForFirstTime");
        }
    }

    public void restrictSalary() {
        try {
            Map<Integer, Integer> map = new HashMap<>();
            for (HrSalaryStructureAccuralDeductionDTO hrEmployeeAccuralDeductionDTOList : getSalaryStructureDTO().getHrEmployeeAccuralDTOList()) {
                if (hrEmployeeAccuralDeductionDTOList != null && hrEmployeeAccuralDeductionDTOList.getAccuralDeductionId() != null && hrEmployeeAccuralDeductionDTOList.getAccuralDeductionId().getSerial() != null) {
                    if (!map.containsKey(hrEmployeeAccuralDeductionDTOList.getAccuralDeductionId().getSerial())) {
                        map.put(hrEmployeeAccuralDeductionDTOList.getAccuralDeductionId().getSerial(), hrEmployeeAccuralDeductionDTOList.getAccuralDeductionId().getSerial());

                    }
                    if (map.containsValue(0)) {
                        hrEmployeeAccuralDeductionDTOList.setSalaryText(true);
                    }

                }
            }
        } catch (Exception e) {
            saveError(e, "SalaryStructureForm", "restrictSalary");
        }
    }

    public void addAccuralRow() {
        try {
            if (validateAccuralRow()) {
                HrSalaryStructureAccuralDeductionDTO hrEmployeeAccuralDeductionDTO = initColumnData();
                getSalaryStructureDTO().getHrEmployeeAccuralDTOList().add(hrEmployeeAccuralDeductionDTO);
                hrEmployeeAccuralDeductionDTO.setSalaryText(false);

            }

        } catch (Exception e) {
            saveError(e, "SalaryStructureform", "addAccuralRow");

        }
    }

    public void addHrSalaryStructureAccuralEffectTypeRow() {
        try {
            if (accuralAddDTOSelected != null) {
                HrSalaryStructureAccuralDeductionEffectTypeDTO hrSalaryStructureAccuralDeductionEffectTypeDTO = new HrSalaryStructureAccuralDeductionEffectTypeDTO();
                hrSalaryStructureAccuralDeductionEffectTypeDTO.setIndex(getIndex());
                hrSalaryStructureAccuralDeductionEffectTypeDTO.setType(0);
                accuralAddDTOSelected.getHrSalaryStructureAccuralEffectType().add(hrSalaryStructureAccuralDeductionEffectTypeDTO);
            }
        } catch (Exception e) {
            saveError(e, "SalaryStructureform", "addHrSalaryStructureAccuralEffectTypeRow");

        }
    }

    public void addHrSalaryStructureDeductionEffectTypeRow() {
        try {

            HrSalaryStructureAccuralDeductionEffectTypeDTO hrSalaryStructureAccuralDeductionEffectTypeDTO = new HrSalaryStructureAccuralDeductionEffectTypeDTO();
            hrSalaryStructureAccuralDeductionEffectTypeDTO.setIndex(getIndex());
            hrSalaryStructureAccuralDeductionEffectTypeDTO.setType(1);
            accuralAddDTOSelected.getHrSalaryStructureDeductionEffectType().add(hrSalaryStructureAccuralDeductionEffectTypeDTO);

        } catch (Exception e) {
            saveError(e, "SalaryStructureform", "addHrSalaryStructureDeductionEffectTypeRow");

        }
    }

    public void onRowSelectDetails(SelectEvent e) {

        e.getSource();

    }

    private HrSalaryStructureAccuralDeductionDTO initColumnData() {
        HrSalaryStructureAccuralDeductionDTO hrEmployeeAccuralDeductionDTO = new HrSalaryStructureAccuralDeductionDTO();
        setIndex(getIndex() + 1);
        hrEmployeeAccuralDeductionDTO.setIndex(getIndex());
        //   hrEmployeeAccuralDeductionDTO.setEmployeeId(getSalaryStructureDTO().getEmployeeId().getId());
        return hrEmployeeAccuralDeductionDTO;

    }

    public void addDeductionRow() {
        //implemented to add a new empty row on the qualifications datatable
        try {
            if (validateAccuralRow()) {

                HrSalaryStructureAccuralDeductionDTO hrEmployeeAccuralDeductionDTO = initColumnData();
                setIndex(getIndex() + 1);
                hrEmployeeAccuralDeductionDTO.setIndex(getIndex());
//                hrEmployeeAccuralDeductionDTO.setEmployeeId(getSalaryStructureDTO().getEmployeeId().getId());

                getSalaryStructureDTO().getHrEmployeeDeductionDTOList().add(hrEmployeeAccuralDeductionDTO);
            }

        } catch (Exception e) {
            saveError(e, "SalaryStructureForm", "addDeductionRow");
        }
    }

    public void addSalaryEffectRow() {
        //implemented to add a new empty row on the fellows datatable
        try {
            HrSalariesStructureEffectDTO hrSalariesEffectDTO = new HrSalariesStructureEffectDTO();
            setIndex(getIndex() + 1);
            hrSalariesEffectDTO.setIndex(getIndex());
            getSalaryStructureDTO().getSalariesEffectDTOList().add(hrSalariesEffectDTO);

        } catch (Exception e) {
            saveError(e, "SalaryStructureForm", "addSalaryEffectRow");
        }
    }

    public void calculateTotal() {
        try {
            setTotalAccural(BigDecimal.ZERO);
            for (HrSalaryStructureAccuralDeductionDTO detailEntity : getSalaryStructureDTO().getHrEmployeeAccuralDTOList()) {
                setTotalAccural((detailEntity.getValue() == null ? BigDecimal.ZERO : detailEntity.getValue()).add(getTotalAccural()));
            }
        } catch (Exception e) {
            saveError(e, "SalaryStructureForm", "calculateTotal");
        }

    }

    public void calculateTotalDeduction() {
        try {
            setTotalDeduction(BigDecimal.ZERO);
            for (HrSalaryStructureAccuralDeductionDTO detailEntity : getSalaryStructureDTO().getHrEmployeeDeductionDTOList()) {
                setTotalDeduction((detailEntity.getValue() == null ? BigDecimal.ZERO : detailEntity.getValue()).add(getTotalDeduction()));
            }
        } catch (Exception e) {
            saveError(e, "SalaryStructureForm", "calculateTotalDeduction");
        }
    }

    public void calculateTotalIncome() {
        try {
            setTotalIncome(BigDecimal.ZERO);
            setTotalIncome(getTotalAccural().subtract(getTotalDeduction()));
        } catch (Exception e) {
            saveError(e, "SalaryStructureForm", "calculateTotalIncome");
        }

    }

    public void finalTotal(HrSalariesStructureEffectDTO hrSalariesStructureEffectDTO) {
        if (hrSalariesStructureEffectDTO != null && hrSalariesStructureEffectDTO.getEffectType() != null
                && hrSalariesStructureEffectDTO.getUnitEffectCount() != null && hrSalariesStructureEffectDTO.getSalaryEffectType() != null) {
            List<String> codeAccural = hrSalaryStructureAccuralDeductionEffectTypeService.getAccuralDeductionCode(hrSalariesStructureEffectDTO.getEffectType().getId(), getSalaryStructureDTO().getId(), 0);
            List<String> codeDeduction = hrSalaryStructureAccuralDeductionEffectTypeService.getAccuralDeductionCode(hrSalariesStructureEffectDTO.getEffectType().getId(), getSalaryStructureDTO().getId(), 1);
            BigDecimal totalAccuralEffect = BigDecimal.ZERO;
            BigDecimal totalDeductionEffect = BigDecimal.ZERO;
            for (HrSalaryStructureAccuralDeductionDTO accuralDeductionDTO : getSalaryStructureDTO().getHrEmployeeAccuralDTOList()) {
                for (String code : codeAccural) {
                    if (code.equals(accuralDeductionDTO.getAccuralDeductionId().getRscAccrualDeductionCode())) {
                        totalDeductionEffect = totalDeductionEffect.add(accuralDeductionDTO.getValue());
                    }
                }
            }
            for (HrSalaryStructureAccuralDeductionDTO accuralDeductionDTO : getSalaryStructureDTO().getHrEmployeeDeductionDTOList()) {
                for (String code : codeDeduction) {
                    if (code.equals(accuralDeductionDTO.getAccuralDeductionId().getRscAccrualDeductionCode())) {
                        totalAccuralEffect = totalAccuralEffect.add(accuralDeductionDTO.getValue());
                    }
                }
            }

            BigDecimal x = new BigDecimal(getSalaryStructureDTO().getWorkDayOfMonth());
            BigDecimal y = new BigDecimal(getSalaryStructureDTO().getWorkHourOfDay());
            BigDecimal z = totalDeductionEffect.add(totalAccuralEffect);
            BigDecimal hourCost = z.divide(x,4,4).divide(y,4,4);
            
//            valuePerMonth = installmentValue.divide(BigDecimal.valueOf(instContractEntity.getInstallmentNumber()), 4, 4);
//                valuePerMonth = valuePerMonth.setScale(2, BigDecimal.ROUND_UP);
            BigDecimal hourLost = BigDecimal.ZERO;
            if (hrSalariesStructureEffectDTO.getSalaryEffectType() == 1) {
                hourLost = hourCost.multiply(hrSalariesStructureEffectDTO.getUnitEffectCount());
            } else if (hrSalariesStructureEffectDTO.getSalaryEffectType() == 0) {
                hourLost = hourCost.multiply(new BigDecimal(getSalaryStructureDTO().getWorkHourOfDay())).multiply(hrSalariesStructureEffectDTO.getUnitEffectCount());
            }
            setTotalIncomeAfterEffect(getTotalIncome().subtract(hourLost));
        }
    }

    public String exit() {
        try {
            exit("../hremployeesalarystructure/salaryStructureList.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "employeeFormMB", "exit");
            return null;
        }
    }

    public void reset() {
        try {

            getSalaryStructureDTO().setEmployeeId(new EmployeeDTO());
            getSalaryStructureDTO().setHrEmployeeAccuralDTOList(new ArrayList<>());
            getSalaryStructureDTO().setHrEmployeeDeductionDTOList(new ArrayList<>());
            setTotalAccural(BigDecimal.ZERO);
            setTotalDeduction(BigDecimal.ZERO);
            setTotalIncome(BigDecimal.ZERO);
        } catch (Exception e) {
            saveError(e, "employeeFormMB", "reset");
        }

    }

    /**
     * @return the url
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param url the url to set
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * @return the totalAccural
     */
    public BigDecimal getTotalAccural() {
        return totalAccural;
    }

    /**
     * @param totalAccural the totalAccural to set
     */
    public void setTotalAccural(BigDecimal totalAccural) {
        this.totalAccural = totalAccural;
    }

    /**
     * @return the totalDeduction
     */
    public BigDecimal getTotalDeduction() {
        return totalDeduction;
    }

    /**
     * @param totalDeduction the totalDeduction to set
     */
    public void setTotalDeduction(BigDecimal totalDeduction) {
        this.totalDeduction = totalDeduction;
    }

    /**
     * @return the totalIncome
     */
    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    /**
     * @param totalIncome the totalIncome to set
     */
    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    /**
     * @return the salaryStructureDTO
     */
    public HrSalaryStructureDTO getSalaryStructureDTO() {
        if (salaryStructureDTO == null) {
            salaryStructureDTO = new HrSalaryStructureDTO();

        }
        return salaryStructureDTO;
    }

    /**
     * @param salaryStructureDTO the salaryStructureDTO to set
     */
    public void setSalaryStructureDTO(HrSalaryStructureDTO salaryStructureDTO) {
        this.salaryStructureDTO = salaryStructureDTO;
    }

    /**
     * @return the accuralDTOSelected
     */
    public HrSalaryStructureAccuralDeductionDTO getAccuralDTOSelected() {
        if (accuralDTOSelected == null) {

            accuralDTOSelected = new HrSalaryStructureAccuralDeductionDTO();
        }
        return accuralDTOSelected;
    }

    /**
     * @param accuralDTOSelected the accuralDTOSelected to set
     */
    public void setAccuralDTOSelected(HrSalaryStructureAccuralDeductionDTO accuralDTOSelected) {
        this.accuralDTOSelected = accuralDTOSelected;
    }

    /**
     * @return the deductionDTOSelected
     */
    public HrSalaryStructureAccuralDeductionDTO getDeductionDTOSelected() {
        if (deductionDTOSelected == null) {
            deductionDTOSelected = new HrSalaryStructureAccuralDeductionDTO();
        }
        return deductionDTOSelected;
    }

    /**
     * @param deductionDTOSelected the deductionDTOSelected to set
     */
    public void setDeductionDTOSelected(HrSalaryStructureAccuralDeductionDTO deductionDTOSelected) {

        this.deductionDTOSelected = deductionDTOSelected;
    }

    /**
     * @return the hrSalariesEffectDTOSelected
     */
    public HrSalariesStructureEffectDTO getHrSalariesEffectDTOSelected() {
        if (hrSalariesEffectDTOSelected == null) {
            hrSalariesEffectDTOSelected = new HrSalariesStructureEffectDTO();

        }
        return hrSalariesEffectDTOSelected;
    }

    /**
     * @param hrSalariesEffectDTOSelected the hrSalariesEffectDTOSelected to set
     */
    public void setHrSalariesEffectDTOSelected(HrSalariesStructureEffectDTO hrSalariesEffectDTOSelected) {
        this.hrSalariesEffectDTOSelected = hrSalariesEffectDTOSelected;
    }

    /**
     * @return the salariesEffectDTO
     */
    public HrSalariesStructureEffectDTO getSalariesEffectDTO() {
        if (salariesEffectDTO == null) {

            salariesEffectDTO = new HrSalariesStructureEffectDTO();
        }
        return salariesEffectDTO;
    }

    /**
     * @param salariesEffectDTO the salariesEffectDTO to set
     */
    public void setSalariesEffectDTO(HrSalariesStructureEffectDTO salariesEffectDTO) {
        this.salariesEffectDTO = salariesEffectDTO;
    }

    /**
     * @return the accuralAddDTOSelected
     */
    public HrSalaryStructureAccuralDeductionDTO getAccuralAddDTOSelected() {
        if (accuralAddDTOSelected != null) {
            accuralAddDTOSelected = new HrSalaryStructureAccuralDeductionDTO();
        }
        return accuralAddDTOSelected;
    }

    /**
     * @param accuralAddDTOSelected the accuralAddDTOSelected to set
     */
    public void setAccuralAddDTOSelected(HrSalaryStructureAccuralDeductionDTO accuralAddDTOSelected) {
        this.accuralAddDTOSelected = accuralAddDTOSelected;
    }

    /**
     * @return the hrSalaryStructureAccuralDeductionEffectTypeDTOSelected
     */
    public HrSalaryStructureAccuralDeductionEffectTypeDTO getHrSalaryStructureAccuralDeductionEffectTypeDTOSelected() {
        if (hrSalaryStructureAccuralDeductionEffectTypeDTOSelected == null) {
            hrSalaryStructureAccuralDeductionEffectTypeDTOSelected = new HrSalaryStructureAccuralDeductionEffectTypeDTO();
        }
        return hrSalaryStructureAccuralDeductionEffectTypeDTOSelected;
    }

    /**
     * @param hrSalaryStructureAccuralDeductionEffectTypeDTOSelected the
     * hrSalaryStructureAccuralDeductionEffectTypeDTOSelected to set
     */
    public void setHrSalaryStructureAccuralDeductionEffectTypeDTOSelected(HrSalaryStructureAccuralDeductionEffectTypeDTO hrSalaryStructureAccuralDeductionEffectTypeDTOSelected) {
        this.hrSalaryStructureAccuralDeductionEffectTypeDTOSelected = hrSalaryStructureAccuralDeductionEffectTypeDTOSelected;
    }

    /**
     * @return the totalIncomeAfterEffect
     */
    public BigDecimal getTotalIncomeAfterEffect() {
        return totalIncomeAfterEffect;
    }

    /**
     * @param totalIncomeAfterEffect the totalIncomeAfterEffect to set
     */
    public void setTotalIncomeAfterEffect(BigDecimal totalIncomeAfterEffect) {
        this.totalIncomeAfterEffect = totalIncomeAfterEffect;
    }

}
