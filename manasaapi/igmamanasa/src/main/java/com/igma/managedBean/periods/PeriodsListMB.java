/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.managedBean.periods;

import com.isag.businessservice.PeriodsService;
import com.isag.dto.PeriodsDTO;
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
@Named(value = "periodsListMB")
@ViewScoped
public class PeriodsListMB extends BaseListBean {

    private PeriodsDTO periodsDTO;
    private List<PeriodsDTO> PeriodsDTOList;
    private Integer periodsDTOSelected;

    @EJB
    PeriodsService periodsService;

    @Override
    @PostConstruct
    public void init() {
    }
    

    @Override
    public void delete() {
        periodsService.deletePeriods(getPeriodsDTO().getId());
        getPeriodsDTOList().remove(getPeriodsDTO());
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "AddPeriods");
            exit("../periods/periodsForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "PeriodListBean", "load");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (getPeriodsDTOSelected() != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("periodsSelected", getPeriodsDTOSelected());
                context.getSessionMap().put("ScreenMode", "EditPeriods");
                exit("../periods/periodsForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "PeriodListBean", "goToEdit");
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
     * @return the PeriodsDTOList
     */
    public List<PeriodsDTO> getPeriodsDTOList() {
        if (PeriodsDTOList == null) {
//            setPeriodsDTOList(periodsService.findPeriodsByTeacherId(getUserData().getUser().getId()));
            setPeriodsDTOList(periodsService.findPeriods(getUserData().getUserBranch() == null ? null : getUserData().getUserBranch().getId()));
        }
        return PeriodsDTOList;
    }

    /**
     * @param PeriodsDTOList the PeriodsDTOList to set
     */
    public void setPeriodsDTOList(List<PeriodsDTO> PeriodsDTOList) {
        this.PeriodsDTOList = PeriodsDTOList;
    }

    /**
     * @return the periodsDTOSelected
     */
    public Integer getPeriodsDTOSelected() {
        return periodsDTOSelected;
    }

    /**
     * @param periodsDTOSelected the periodsDTOSelected to set
     */
    public void setPeriodsDTOSelected(Integer periodsDTOSelected) {
        this.periodsDTOSelected = periodsDTOSelected;
    }

}
