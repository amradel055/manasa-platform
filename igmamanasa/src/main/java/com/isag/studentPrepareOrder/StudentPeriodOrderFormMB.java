/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.studentPrepareOrder;

import com.isag.api.dto.SearchStudentDTO;
import com.isag.businessservice.IgmaUserService;
import com.isag.businessservice.MaterialsService;
import com.isag.businessservice.PeriodsService;
import com.isag.businessservice.SchoolStagesGuideService;
import com.isag.businessservice.StudentPeriodsOrderService;
import com.isag.converter.MaterialDTOConverter;
import com.isag.converter.SchoolStagesGuideDTOConverter;
import com.isag.converter.SearchStudentDTOConverter;
import com.isag.converter.periodDTOConverter;
import com.isag.dto.IgmaUserDTO;
import com.isag.dto.MaterialsDTO;
import com.isag.dto.PeriodsDTO;
import com.isag.dto.SchoolStagesGuideDTO;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.isag.dto.StudentPeriodsOrderDTO;
import com.isag.isag.BaseFormBean;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hq004
 */
@Named(value = "studentPeriodOrderFormMB")
@ViewScoped
public class StudentPeriodOrderFormMB extends BaseFormBean {

    private StudentPeriodsOrderDTO studentPeriodsOrderDTO;

    @EJB
    SchoolStagesGuideService schoolStagesGuideService;
    @EJB
    StudentPeriodsOrderService studentPeriodsOrderService;
    @EJB
    IgmaUserService igmaUserService;
    @EJB
    MaterialsService materialsService;
    @EJB
    PeriodsService periodsService;

    private List<SearchStudentDTO> searchStudentDTOList;
    private SearchStudentDTO searchStudentDTO;
    SearchStudentDTOConverter searchStudentDTOConverter;

    private List<IgmaUserDTO> igmaUserDTOList;

    private List<SchoolStagesGuideDTO> schoolStagesGuideDTOList;
    private SchoolStagesGuideDTO schoolStagesGuideDTO;
    private SchoolStagesGuideDTOConverter schoolStagesGuideDTOConverter;

    private List<MaterialsDTO> materialsDTOList;
    private MaterialsDTO materialsDTO;
    private MaterialDTOConverter materialDTOConverter;

    private List<PeriodsDTO> periodsDTOList;
    private PeriodsDTO periodsDTO;
    private periodDTOConverter periodDtoConverter;

    public void fillMaterialList() {
        setMaterialsDTOList(materialsService.findMaterialsByStage(getSchoolStagesGuideDTO().getId()));
        setMaterialDTOConverter(new MaterialDTOConverter(materialsDTOList));
    }

    public void fillPeriodList() {
        setPeriodsDTOList(periodsService.findPeriodsByMaterialId(getMaterialsDTO().getId()));
        setPeriodDtoConverter(new periodDTOConverter(periodsDTOList));
    }

    @Override
    public String save() {
        studentPeriodsOrderService.save(getStudentPeriodsOrderDTO(), getUserData().getUser());
        searchStudentDTOList.remove(getSearchStudentDTO());
        return "";
    }

    public void search() {
        setSearchStudentDTOList(studentPeriodsOrderService.searchStudent(getPeriodsDTO().getId(), getMaterialsDTO().getId()));
    }

    public void transaction() {
        studentPeriodsOrderService.transaction(getSearchStudentDTO().getId(), getUserData().getUser());
    }

    public void delete() {
        studentPeriodsOrderService.delete(getSearchStudentDTO().getId());
        searchStudentDTOList.remove(getSearchStudentDTO());

    }

    @Override
    @PostConstruct
    public void init() {
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("AddteachersDTO")) {

        } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditteachersDTO")) {
            try {
                Integer studenPeriodId = (Integer) getContext().getSessionMap().get("teachersDTOSelected");
                setStudentPeriodsOrderDTO(studentPeriodsOrderService.findStudentPeriod(studenPeriodId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void reset() {
        setStudentPeriodsOrderDTO(null);
    }

    public String exit() {
        exit("../studentPeriodOrder/studentPeriodOrderList.xhtml");
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
     * @return the igmaUserDTOList
     */
    public List<IgmaUserDTO> getIgmaUserDTOList() {
        if (igmaUserDTOList == null) {
            setIgmaUserDTOList(igmaUserService.getUsersByCompanyId(getUserData().getCompany().getId()));
        }
        return igmaUserDTOList;
    }

    /**
     * @param igmaUserDTOList the igmaUserDTOList to set
     */
    public void setIgmaUserDTOList(List<IgmaUserDTO> igmaUserDTOList) {
        this.igmaUserDTOList = igmaUserDTOList;
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
     * @return the periodsDTOList
     */
    public List<PeriodsDTO> getPeriodsDTOList() {
        if (periodsDTOList == null) {
            setPeriodsDTOList(new ArrayList<>());
        }
        return periodsDTOList;
    }

    /**
     * @param periodsDTOList the periodsDTOList to set
     */
    public void setPeriodsDTOList(List<PeriodsDTO> periodsDTOList) {
        this.periodsDTOList = periodsDTOList;
    }

    /**
     * @return the schoolStagesGuideDTOList
     */
    public List<SchoolStagesGuideDTO> getSchoolStagesGuideDTOList() {
        if (schoolStagesGuideDTOList == null) {

            setSchoolStagesGuideDTOList(schoolStagesGuideService.findschoolStagesGuide(232));
            setSchoolStagesGuideDTOConverter(new SchoolStagesGuideDTOConverter(schoolStagesGuideDTOList));
        }
        return schoolStagesGuideDTOList;
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
     * @return the materialsDTO
     */
    public MaterialsDTO getMaterialsDTO() {
        if (materialsDTO == null) {
            setMaterialsDTO(new MaterialsDTO());
        }
        return materialsDTO;
    }

    /**
     * @param materialsDTO the materialsDTO to set
     */
    public void setMaterialsDTO(MaterialsDTO materialsDTO) {
        this.materialsDTO = materialsDTO;
    }

    /**
     * @return the materialDTOConverter
     */
    public MaterialDTOConverter getMaterialDTOConverter() {
        return materialDTOConverter;
    }

    /**
     * @param materialDTOConverter the materialDTOConverter to set
     */
    public void setMaterialDTOConverter(MaterialDTOConverter materialDTOConverter) {
        this.materialDTOConverter = materialDTOConverter;
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
     * @return the periodDtoConverter
     */
    public periodDTOConverter getPeriodDtoConverter() {
        return periodDtoConverter;
    }

    /**
     * @param periodDtoConverter the periodDtoConverter to set
     */
    public void setPeriodDtoConverter(periodDTOConverter periodDtoConverter) {
        this.periodDtoConverter = periodDtoConverter;
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
     * @return the searchStudentDTOList
     */
    public List<SearchStudentDTO> getSearchStudentDTOList() {
        if (searchStudentDTOList == null) {
            setSearchStudentDTOList(new ArrayList<>());
        }
        return searchStudentDTOList;
    }

    /**
     * @param searchStudentDTOList the searchStudentDTOList to set
     */
    public void setSearchStudentDTOList(List<SearchStudentDTO> searchStudentDTOList) {
        this.searchStudentDTOList = searchStudentDTOList;
    }

    /**
     * @return the searchStudentDTO
     */
    public SearchStudentDTO getSearchStudentDTO() {
        if (searchStudentDTO == null) {
            setSearchStudentDTO(new SearchStudentDTO());
        }

        return searchStudentDTO;
    }

    /**
     * @param searchStudentDTO the searchStudentDTO to set
     */
    public void setSearchStudentDTO(SearchStudentDTO searchStudentDTO) {
        this.searchStudentDTO = searchStudentDTO;
    }

}
