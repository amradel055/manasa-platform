/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.managedBean.periods;

import com.isag.businessservice.BranchService;
import com.isag.businessservice.IgmaUserService;
import com.isag.businessservice.MaterialsService;
import com.isag.businessservice.PeriodsService;
import com.isag.businessservice.SchoolStagesGuideService;
import com.isag.businessservice.StudentPeriodsOrderService;
import com.isag.converter.SchoolStagesGuideDTOConverter;
import com.isag.dto.MaterialsDTO;
import com.isag.dto.PeriodsDTO;
import com.isag.dto.SchoolStagesGuideDTO;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.isag.dto.StudentPeriodsOrderDTO;
import com.isag.entity.Branch;
import com.isag.entity.IgmaCompany;
import com.isag.entity.IgmaUser;
import com.isag.isag.BaseFormBean;
import java.util.ArrayList;
import java.util.List;

@Named(value = "periodsFormMB")
@ViewScoped
public class PeriodsFormMB extends BaseFormBean {

    private StudentPeriodsOrderDTO studentPeriodsOrderDTO;

    @EJB
    SchoolStagesGuideService schoolStagesGuideService;
    @EJB
    StudentPeriodsOrderService studentPeriodsOrderService;
    @EJB
    BranchService branchService;
    @EJB
    MaterialsService materialsService;
    @EJB
    PeriodsService periodsService;

    private List<SchoolStagesGuideDTO> schoolStagesGuideDTOList;
    private SchoolStagesGuideDTO schoolStagesGuideDTO;
    private SchoolStagesGuideDTOConverter schoolStagesGuideDTOConverter;

    private List<MaterialsDTO> materialsDTOList;

    private PeriodsDTO periodsDTO;
    private List<PeriodsDTO> periodsDTOParentList;

    public void fillMaterialList() {
        setMaterialsDTOList(materialsService.findMaterialsByStage(getSchoolStagesGuideDTO().getId()));
    }

    public void fillParentList() {
        periodsDTOParentList = periodsService.findPeriodsByMaterialId(getPeriodsDTO().getMaterialId());

    }

    @Override
    public String save() {
        
        IgmaCompany companySelected = getCompanySelected() != null ? getCompanySelected() : getUserData().getUser().getCompanyId();

        IgmaUser teacherSelected = getTeacherSelected() != null ? getTeacherSelected() : getUserData().getUser();

        List<Branch> branchs = branchService.getAllBranchByCompanyId(companySelected.getId());

        getUserData().getUser().setBranchId(branchs != null && !branchs.isEmpty() ? branchs.get(0) : null);
        getPeriodsDTO().setBranchId(branchs != null && !branchs.isEmpty() ? branchs.get(0).getId() : null);
        getUserData().getUser().setCompanyId(companySelected);
        getUserData().getUser().setId(teacherSelected.getId());
        
        getPeriodsDTO().setTeacherId(teacherSelected.getId());
        
        periodsService.savePeriods(getPeriodsDTO(), getUserData().getUser());
        reset();
        exit() ;
        return "";
    }

    @Override
    @PostConstruct
    public void init() {
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("AddPeriods")) {

        } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditPeriods")) {
            try {
                Integer studenPeriodId = (Integer) getContext().getSessionMap().get("periodsSelected");
                setPeriodsDTO(periodsService.findPeriodsById(studenPeriodId));
                setSchoolStagesGuideDTO(getPeriodsDTO().getSchoolStagesGuideDTO());
                fillMaterialList();
                fillParentList();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void reset() {
        setPeriodsDTO(null);
    }

    public String exit() {
        exit("../periods/periodsList.xhtml");
        return "";
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the studentPeriodsOrderDTO
     */
    public StudentPeriodsOrderDTO getStudentPeriodsOrderDTO() {
        if (studentPeriodsOrderDTO == null) {
            setStudentPeriodsOrderDTO(new StudentPeriodsOrderDTO());
        }
        return studentPeriodsOrderDTO;
    }

    /**
     * @param studentPeriodsOrderDTO the studentPeriodsOrderDTO to set
     */
    public void setStudentPeriodsOrderDTO(StudentPeriodsOrderDTO studentPeriodsOrderDTO) {
        this.studentPeriodsOrderDTO = studentPeriodsOrderDTO;
    }

    /**
     * @return the materialsDTOList
     */
    public List<MaterialsDTO> getMaterialsDTOList() {
        if (materialsDTOList == null) {
            setMaterialsDTOList(new ArrayList<>());
        }
        return materialsDTOList;
    }

    /**
     * @param materialsDTOList the materialsDTOList to set
     */
    public void setMaterialsDTOList(List<MaterialsDTO> materialsDTOList) {
        this.materialsDTOList = materialsDTOList;
    }

    /**
     * @return the schoolStagesGuideDTOList
     */
    public List<SchoolStagesGuideDTO> getSchoolStagesGuideDTOList() {
//        if (schoolStagesGuideDTOList == null) {
//
//            setSchoolStagesGuideDTOList(schoolStagesGuideService.findschoolStagesGuide(232));
//            setSchoolStagesGuideDTOConverter(new SchoolStagesGuideDTOConverter(schoolStagesGuideDTOList));
//        }
        return schoolStagesGuideDTOList;
    }
    
    public void findSchoolStagesGuideDTOList() {

        IgmaCompany companySelected = getCompanySelected() != null ? getCompanySelected() : getUserData().getUser().getCompanyId();
        List<Branch> branchs = branchService.getAllBranchByCompanyId(companySelected.getId());

        setSchoolStagesGuideDTOList(schoolStagesGuideService.findschoolStagesGuide(branchs.get(0).getId()));
        setSchoolStagesGuideDTOConverter(new SchoolStagesGuideDTOConverter(schoolStagesGuideDTOList));
    }

    /**
     * @param schoolStagesGuideDTOList the schoolStagesGuideDTOList to set
     */
    public void setSchoolStagesGuideDTOList(List<SchoolStagesGuideDTO> schoolStagesGuideDTOList) {
        this.schoolStagesGuideDTOList = schoolStagesGuideDTOList;
    }

    /**
     * @return the schoolStagesGuideDTO
     */
    public SchoolStagesGuideDTO getSchoolStagesGuideDTO() {
        if (schoolStagesGuideDTO == null) {
            setSchoolStagesGuideDTO(new SchoolStagesGuideDTO());
        }
        return schoolStagesGuideDTO;
    }

    /**
     * @param schoolStagesGuideDTO the schoolStagesGuideDTO to set
     */
    public void setSchoolStagesGuideDTO(SchoolStagesGuideDTO schoolStagesGuideDTO) {
        this.schoolStagesGuideDTO = schoolStagesGuideDTO;
    }



    /**
     * @return the schoolStagesGuideDTOConverter
     */
    public SchoolStagesGuideDTOConverter getSchoolStagesGuideDTOConverter() {
        return schoolStagesGuideDTOConverter;
    }

    /**
     * @param schoolStagesGuideDTOConverter the schoolStagesGuideDTOConverter to
     * set
     */
    public void setSchoolStagesGuideDTOConverter(SchoolStagesGuideDTOConverter schoolStagesGuideDTOConverter) {
        this.schoolStagesGuideDTOConverter = schoolStagesGuideDTOConverter;
    }

    /**
     * @return the periodsDTO
     */
    public PeriodsDTO getPeriodsDTO() {
        if (periodsDTO == null) {
            setPeriodsDTO(new PeriodsDTO());
        }
        return periodsDTO;
    }

    /**
     * @param periodsDTO the periodsDTO to set
     */
    public void setPeriodsDTO(PeriodsDTO periodsDTO) {
        this.periodsDTO = periodsDTO;
    }

    /**
     * @return the periodsDTOParentList
     */
    public List<PeriodsDTO> getPeriodsDTOParentList() {
        if (periodsDTOParentList == null) {
            setPeriodsDTOParentList(new ArrayList<>());

        }
        return periodsDTOParentList;
    }

    /**
     * @param periodsDTOParentList the periodsDTOParentList to set
     */
    public void setPeriodsDTOParentList(List<PeriodsDTO> periodsDTOParentList) {
        this.periodsDTOParentList = periodsDTOParentList;
    }

}
