/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.managedBean.material;

import com.isag.businessservice.MaterialsService;
import com.isag.businessservice.SchoolStagesGuideService;
import com.isag.dto.MaterialsDTO;
import com.isag.dto.SchoolStagesGuideDTO;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.isag.isag.BaseFormBean;
import java.util.List;

@Named(value = "materialsFormMB")
@ViewScoped
public class MaterialFormMB extends BaseFormBean {


    @EJB
    SchoolStagesGuideService schoolStagesGuideService;
    @EJB
    MaterialsService materialsService;

    private List<SchoolStagesGuideDTO> schoolStagesGuideDTOList;


    private MaterialsDTO materialsDTO;

   

    @Override
    public String save() {
        materialsService.saveMaterial(getMaterialsDTO(), getUserData().getUser());
        reset();
        exit() ;
        return "";
    }

    @Override
    @PostConstruct
    public void init() {
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("AddMaterials")) {

        } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditMaterials")) {
            try {
                Integer studenMaterialId = (Integer) getContext().getSessionMap().get("materialsSelected");
                setMaterialsDTO(materialsService.findMaterialDTO(studenMaterialId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void reset() {
        setMaterialsDTO(null);
    }

    public String exit() {
        exit("../material/materialList.xhtml");
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
     * @return the schoolStagesGuideDTOList
     */
    public List<SchoolStagesGuideDTO> getSchoolStagesGuideDTOList() {
        if (schoolStagesGuideDTOList == null) {

            setSchoolStagesGuideDTOList(schoolStagesGuideService.findschoolStagesGuide(232));
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
     * @return the materialsDTO
     */
    public MaterialsDTO getMaterialsDTO() {
        if(materialsDTO == null){
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



   
}
