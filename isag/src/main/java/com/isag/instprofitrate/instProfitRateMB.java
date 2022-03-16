/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.instprofitrate;

import com.isag.businessservice.InstProfitRateService;
import com.isag.dto.InstProfitRateDTO;
import com.isag.isag.BaseFormBean;
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
 * @author `*"<:ahmedShaaban:>"*`
 */
@Named(value = "instProfitRateMB")
@ViewScoped
public class instProfitRateMB extends BaseFormBean {

    private InstProfitRateDTO profitRateDTO;
    private InstProfitRateDTO profitRateDTOSelected;
    private List<InstProfitRateDTO> profitRateDTOList;
    private Integer index = 999999;

    @EJB
    InstProfitRateService instProfitRateService;

    @Override
    public String save() {
        try {
            if (isDuplicate() != true) {

                profitRateDTOList = instProfitRateService.addInstProfitRate(profitRateDTOList, getUserData().getUser());

            } else if (getProfitRateDTO().getMsg() != null && !getProfitRateDTO().getMsg().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), "تم الحفظ بنجاح"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لايمكن تكرار عدد الشهور"));

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
            load();
        } catch (Exception e) {
            saveError(e, "instProftRateMB", "init");
        }
    }

    private boolean isDuplicate() {
        for (int i = 0; i < profitRateDTOList.size(); i++) {
            for (int j = i + 1; j < profitRateDTOList.size(); j++) {
                if (profitRateDTOList.get(i).getMonthNumber().equals(profitRateDTOList.get(j).getMonthNumber())) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            getUserData().getUser().setBranchId(getUserData().getUserBranch());
            profitRateDTOList = instProfitRateService.getAllInstProfitRatesListByBranchId(getUserData().getUserBranch().getId());

        } catch (Exception e) {
            saveError(e, "instProftRateMB", "load");
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void edit() {
        try {
            for (InstProfitRateDTO profitRateDTOs : profitRateDTOList) {
                profitRateDTOs.setMarkEdit(Boolean.TRUE);
            }
            if (profitRateDTOSelected != null) {
                profitRateDTOSelected.setChBoolean(Boolean.FALSE);
            }

        } catch (Exception e) {
            saveError(e, "instProftRateMB", "edit");
        }
    }

    /**
     * @return the profitRateDTO
     */
    public InstProfitRateDTO getProfitRateDTO() {
        if (profitRateDTO == null) {
            profitRateDTO = new InstProfitRateDTO();

        }
        return profitRateDTO;
    }

    public void delete() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            if (getProfitRateDTOSelected() != null) {
                try {
                    if (getProfitRateDTOSelected() != null) {
                        if (getProfitRateDTOSelected().getId() != null) {
                            instProfitRateService.deleteInstProfitRate(getProfitRateDTOSelected(), getUserData().getUser());
                        }
                        getProfitRateDTOList().remove(getProfitRateDTOSelected());
                    }
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "instProftRateMB", "delete");
        }
    }

    public void addNewRow() {
        try {
            InstProfitRateDTO profitRateDTOs = new InstProfitRateDTO();
            setIndex((Integer) getIndex() + 1);
            profitRateDTOs.setChBoolean(Boolean.FALSE);
            getProfitRateDTOList().add(profitRateDTOs);

        } catch (Exception e) {
            saveError(e, "instProftRateMB", "addNewRow");
        }

    }

    /**
     * @param profitRateDTO the profitRateDTO to set
     */
    public void setProfitRateDTO(InstProfitRateDTO profitRateDTO) {
        this.profitRateDTO = profitRateDTO;
    }

    /**
     * @return the profitRateDTOList
     */
    public List<InstProfitRateDTO> getProfitRateDTOList() {
        if (profitRateDTOList == null || profitRateDTOList.isEmpty()) {
            profitRateDTOList = new ArrayList<>();

        }
        return profitRateDTOList;
    }

    /**
     * @param profitRateDTOList the profitRateDTOList to set
     */
    public void setProfitRateDTOList(List<InstProfitRateDTO> profitRateDTOList) {
        this.profitRateDTOList = profitRateDTOList;
    }

    /**
     * @return the profitRateDTOSelected
     */
    public InstProfitRateDTO getProfitRateDTOSelected() {
        if (profitRateDTOSelected == null) {
            profitRateDTOSelected = new InstProfitRateDTO();
        }
        return profitRateDTOSelected;
    }

    /**
     * @param profitRateDTOSelected the profitRateDTOSelected to set
     */
    public void setProfitRateDTOSelected(InstProfitRateDTO profitRateDTOSelected) {
        this.profitRateDTOSelected = profitRateDTOSelected;
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

    /**
     * @return the chBoolean
     */
}
