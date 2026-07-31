/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.managedBean.material;

import com.isag.businessservice.MaterialsService;
import com.isag.dto.MaterialsDTO;
import com.isag.isag.BaseListBean;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author hq004
 */
@Named(value = "materialListMB")
@ViewScoped
public class MaterialListMB extends BaseListBean {

    private MaterialsDTO materialsDTO;
    private List<MaterialsDTO> materialsDTOList;
    private Integer materialsDTOSelected;

    @EJB
    MaterialsService materialsService;

    @Override
    @PostConstruct
    public void init() {
    }
    

    @Override
    public void delete() {
        materialsService.deleteMaterials(getMaterialsDTO().getId());
        getMaterialsDTOList().remove(getMaterialsDTO());
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "AddMaterials");
            exit("../material/materialForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "MaterialListBean", "load");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (getMaterialsDTOSelected() != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("materialsSelected", getMaterialsDTOSelected());
                context.getSessionMap().put("ScreenMode", "EditMaterials");
                exit("../material/materialForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "MaterialListBean", "goToEdit");
            return null;
        }
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
     * @return the materialsDTOList
     */
    public List<MaterialsDTO> getMaterialsDTOList() {
        if (materialsDTOList == null) {
            setmaterialsDTOList(materialsService.findMaterialsByTeacher(getUserData().getUser().getId()));
        }
        return materialsDTOList;
    }

    /**
     * @return the materialsDTOList
     */
    public void findMaterialsDTOList() {
            Integer companySelected = getSelectedCompanyId() != null ? getSelectedCompanyId() : getUserData().getUser().getCompanyId() == null ? null : getUserData().getUser().getCompanyId().getId();
            setmaterialsDTOList(materialsService.findMaterialsByCompanyAndTeachear( companySelected , getTeacherSelected() == null ? null :  getTeacherSelected().getId()));
    }

    /**
     * @param materialsDTOList the materialsDTOList to set
     */
    public void setmaterialsDTOList(List<MaterialsDTO> materialsDTOList) {
        this.materialsDTOList = materialsDTOList;
    }

    /**
     * @return the materialsDTOSelected
     */
    public Integer getMaterialsDTOSelected() {
        return materialsDTOSelected;
    }

    /**
     * @param materialsDTOSelected the materialsDTOSelected to set
     */
    public void setMaterialsDTOSelected(Integer materialsDTOSelected) {
        this.materialsDTOSelected = materialsDTOSelected;
    }

}
