/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.SalaryEffect;

import com.isag.businessservice.HrSalaryEffectService;
import com.isag.dto.HrSalaryEffectDTO;
import com.isag.isag.BaseListBean;
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
@Named(value = "salaryEffectTypeListMB")
@ViewScoped
public class SalaryEffectListMB extends BaseListBean {

    private List<HrSalaryEffectDTO> hrSalaryEffectDTOList;
    private HrSalaryEffectDTO hrSalaryEffectDTOSelected;

    @EJB
    private HrSalaryEffectService hrSalaryEffectService;

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "AddSalaryEffect");

            exit("../hrsalaryeffectt/salaryEffectForm.xhtml");

            return "";
        } catch (Exception e) {
            saveError(e, "SalaryEffectTypeListMB", "goToAdd");
            return null;
        }
    }

    @Override
    @PostConstruct
    public void init() {
        load();
    }

    @Override
    public void delete() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        if (hrSalaryEffectDTOSelected != null) {
            try {
                hrSalaryEffectService.deleteSalaryEffectType(hrSalaryEffectDTOSelected);
                hrSalaryEffectDTOList.remove(hrSalaryEffectDTOSelected);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
            }
        }
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToEdit() {
        if (hrSalaryEffectDTOSelected != null && hrSalaryEffectDTOSelected.getId() != null) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("hrSalaryEffectDTOSelected", hrSalaryEffectDTOSelected.getId());
            context.getSessionMap().put("ScreenMode", "EdithrSalaryEffectDTO");
            exit("../hrsalaryeffectt/salaryEffectForm.xhtml");
            return "";
        } else {
            return "";
        }
    }

    @Override
    public void load() {
        try {
            setHrSalaryEffectDTOList(hrSalaryEffectService.getALLHrSalaryEffectByBranchId(getUserData().getUserlogin().getBranchId().getId()));

//            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
//            setDataModel(new LazyDataModel<HrSalaryEffectDTO>() {
//                private static final long serialVersionUID = 1L;
//
//                @Override
//                public List<HrSalaryEffectDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
//                    setRowCount(hrSalaryEffectService.getTotalRegistors(getUserData().getUserlogin().getBranchId().getId(), filters).intValue());
//                    setHrSalaryEffectDTOList(hrSalaryEffectService.getALLHrSalaryEffectByBranchId(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), getUserData().getUserlogin().getBranchId().getId(), filters));
//                    return getHrSalaryEffectDTOList();
//                }
//            });
        } catch (Exception e) {
            saveError(e, "salaryStructureListMB", "load");
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the hrSalaryEffectDTOList
     */
    public List<HrSalaryEffectDTO> getHrSalaryEffectDTOList() {
        return hrSalaryEffectDTOList;
    }

    /**
     * @param hrSalaryEffectDTOList the hrSalaryEffectDTOList to set
     */
    public void setHrSalaryEffectDTOList(List<HrSalaryEffectDTO> hrSalaryEffectDTOList) {
        this.hrSalaryEffectDTOList = hrSalaryEffectDTOList;
    }

    /**
     * @return the hrSalaryEffectDTOSelected
     */
    public HrSalaryEffectDTO getHrSalaryEffectDTOSelected() {
        return hrSalaryEffectDTOSelected;
    }

    /**
     * @param hrSalaryEffectDTOSelected the hrSalaryEffectDTOSelected to set
     */
    public void setHrSalaryEffectDTOSelected(HrSalaryEffectDTO hrSalaryEffectDTOSelected) {
        this.hrSalaryEffectDTOSelected = hrSalaryEffectDTOSelected;
    }

}
