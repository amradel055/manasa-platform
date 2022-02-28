/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.prodetailtype;

import com.toby.businessservice.ProDetailTypeService;
import com.toby.dto.ProDetailTypeDTO;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "proDetailTypeListBean")
@ViewScoped
public class ProDetailTypeListBean extends BaseListBean implements Serializable {

    private List<ProDetailTypeDTO> proDetailTypeDTOList;
    private ProDetailTypeDTO proDetailTypeDTO;
    private Integer proDetailTypeSelected;

    @EJB
    private ProDetailTypeService proDetailTypeService;

    public ProDetailTypeListBean() {

    }

    @Override
    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        reset();
    }

    public void reset() {
        setProDetailTypeDTO(null);
        getProDetailTypeDTOList();
    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            if (proDetailTypeDTO != null) {
                try {
                    proDetailTypeService.deleteProDetailTypeDTO(proDetailTypeDTO);
                    reset();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "تم الحذف بنجاح", null));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "ProDetailTypeListBean", "delete");
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
            exit("../proDetailType/proDetailTypeForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "ProDetailTypeListBean", "delete");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (proDetailTypeSelected != null && proDetailTypeSelected > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("proDetailTypeSelected", proDetailTypeSelected);
                context.getSessionMap().put("ScreenMode", "Edit");
                exit("../proDetailType/proDetailTypeForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "ProDetailTypeListBean", "goToEdit");
            return null;
        }
    }

    @Override
    public void load() {

    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the proDetailTypeDTOList
     */
    public List<ProDetailTypeDTO> getProDetailTypeDTOList() {
        if (proDetailTypeDTOList == null || proDetailTypeDTOList.isEmpty()) {
            proDetailTypeDTOList = proDetailTypeService.getallProDetailTypeByBranchId(getUserData().getUserBranch().getId(), null);
        }
        return proDetailTypeDTOList;
    }

    /**
     * @param proDetailTypeDTOList the proDetailTypeDTOList to set
     */
    public void setProDetailTypeDTOList(List<ProDetailTypeDTO> proDetailTypeDTOList) {
        this.proDetailTypeDTOList = proDetailTypeDTOList;
    }

    /**
     * @return the proDetailTypeDTO
     */
    public ProDetailTypeDTO getProDetailTypeDTO() {
        return proDetailTypeDTO;
    }

    /**
     * @param proDetailTypeDTO the proDetailTypeDTO to set
     */
    public void setProDetailTypeDTO(ProDetailTypeDTO proDetailTypeDTO) {
        this.proDetailTypeDTO = proDetailTypeDTO;
    }

    /**
     * @return the proDetailTypeSelected
     */
    public Integer getProDetailTypeSelected() {
        return proDetailTypeSelected;
    }

    /**
     * @param proDetailTypeSelected the proDetailTypeSelected to set
     */
    public void setProDetailTypeSelected(Integer proDetailTypeSelected) {
        this.proDetailTypeSelected = proDetailTypeSelected;
    }
}
