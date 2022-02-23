/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.SalaryEffectType;

import com.isag.businessservice.HrSalaryEffectTypeService;
import com.isag.dto.HrSalaryEffectTypeDTO;
import com.isag.isag.BaseListBean;
import com.isag.isag.UserData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author AhmedEssam
 */
@Named(value = "hrSalaryEffectTypeListMB")
@ViewScoped
public class SalaryEffectTypeListMB extends BaseListBean {

    private UserData userData;
    private HrSalaryEffectTypeDTO hrSalaryEffectTypeDTO;
    private HrSalaryEffectTypeDTO hrSalaryEffectTypeDTOSelected;
    private List<HrSalaryEffectTypeDTO> hrSalaryEffectTypeDTOList;
    private Integer index = 0;
    @EJB
    HrSalaryEffectTypeService hrSalaryEffectTypeService;

    @Override
    @PostConstruct
    public void init() {
        try {
            hrSalaryEffectTypeDTOList = new ArrayList<>();
            hrSalaryEffectTypeDTO = new HrSalaryEffectTypeDTO();
            load();
        } catch (Exception e) {
            saveError(e, "SalaryEffectTypeListMB", "init");
        }
    }

    @Override
    public void delete() {
        Map<String, String> userDDs = userData.getUserDDs();
        try {
            hrSalaryEffectTypeService.deleteSalaryEffectType(hrSalaryEffectTypeDTOSelected, userData.getUserlogin());
            hrSalaryEffectTypeDTOList.remove(hrSalaryEffectTypeDTOSelected);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "لا يمكن مسح نوع المؤثر"));
            saveError(e, "SalaryEffectTypeListMB", "delete");
        }
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");

            exit("../hrsalaryeffecttype/hrSalaryEffectTypeForm.xhtml");

            return "";
        } catch (Exception e) {
            saveError(e, "SalaryEffectTypeListMB", "goToAdd");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (hrSalaryEffectTypeDTOSelected != null && hrSalaryEffectTypeDTOSelected.getId() > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("hrSalaryEffectTypeDTOSelected", hrSalaryEffectTypeDTOSelected.getId());
                context.getSessionMap().put("ScreenMode", "Edit");
                exit("../hrsalaryeffecttype/hrSalaryEffectTypeForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "SalaryEffectTypeListMB", "goToEdit");
            return null;
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");
            hrSalaryEffectTypeDTOList = hrSalaryEffectTypeService.findAllSalaryEffectTypeByBranchId(userData.getUserBranch().getId());
        } catch (Exception e) {
            saveError(e, "SalaryEffectTypeListMB", "load");
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the userData
     */
    public UserData getUserData() {
        return userData;
    }

    /**
     * @param userData the userData to set
     */
    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    /**
     * @return the hrSalaryEffectTypeDTO
     */
    public HrSalaryEffectTypeDTO getHrSalaryEffectTypeDTO() {
        return hrSalaryEffectTypeDTO;
    }

    /**
     * @param hrSalaryEffectTypeDTO the hrSalaryEffectTypeDTO to set
     */
    public void setHrSalaryEffectTypeDTO(HrSalaryEffectTypeDTO hrSalaryEffectTypeDTO) {
        this.hrSalaryEffectTypeDTO = hrSalaryEffectTypeDTO;
    }

    /**
     * @return the hrSalaryEffectTypeDTOSelected
     */
    public HrSalaryEffectTypeDTO getHrSalaryEffectTypeDTOSelected() {
        return hrSalaryEffectTypeDTOSelected;
    }

    /**
     * @param hrSalaryEffectTypeDTOSelected the hrSalaryEffectTypeDTOSelected to
     * set
     */
    public void setHrSalaryEffectTypeDTOSelected(HrSalaryEffectTypeDTO hrSalaryEffectTypeDTOSelected) {
        this.hrSalaryEffectTypeDTOSelected = hrSalaryEffectTypeDTOSelected;
    }

    /**
     * @return the hrSalaryEffectTypeDTOList
     */
    public List<HrSalaryEffectTypeDTO> getHrSalaryEffectTypeDTOList() {
        return hrSalaryEffectTypeDTOList;
    }

    /**
     * @param hrSalaryEffectTypeDTOList the hrSalaryEffectTypeDTOList to set
     */
    public void setHrSalaryEffectTypeDTOList(List<HrSalaryEffectTypeDTO> hrSalaryEffectTypeDTOList) {
        this.hrSalaryEffectTypeDTOList = hrSalaryEffectTypeDTOList;
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
