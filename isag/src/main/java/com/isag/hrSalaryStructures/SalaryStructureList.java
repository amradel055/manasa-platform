/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.hrSalaryStructures;

import com.isag.businessservice.HrSalaryStructureAccuralDeductionService;
import com.isag.businessservice.HrSalaryStructureService;
import com.isag.dto.HrSalaryStructureAccuralDeductionDTO;
import com.isag.dto.HrSalaryStructureDTO;
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
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Ahmed Shaaban
 */
@Named(value = "salaryStructureListMB")
@ViewScoped
public class SalaryStructureList extends BaseListBean {

    private List<HrSalaryStructureDTO> salaryStructureDTOList;
    private HrSalaryStructureDTO hrSalaryStructureDTOSelected;
    private LazyDataModel<HrSalaryStructureDTO> dataModel;
    private UserData userData;

    @EJB
    HrSalaryStructureService salaryStructureService;
    @EJB
    HrSalaryStructureAccuralDeductionService accuralDeductionService;

    @Override
    @PostConstruct
    public void init() {
        try {
            userData = new UserData();
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            this.load();
        } catch (Exception e) {
            saveError(e, "salaryStructureListMB", "init");
        }
    }

    @Override
    public void delete() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        try {
            Integer hrSalaryStructureDTOId = hrSalaryStructureDTOSelected.getId() != null ? hrSalaryStructureDTOSelected.getId() : null;
            List<HrSalaryStructureAccuralDeductionDTO> accuralDeductionDTOList = accuralDeductionService.findHrEmpAccuralDTOBySalaryStructureBysalaryStructureId(hrSalaryStructureDTOId);
            if (!accuralDeductionDTOList.isEmpty() && accuralDeductionDTOList.size() > 1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
            } else if (accuralDeductionDTOList.size() == 1) {
                if (!accuralDeductionDTOList.get(0).getHrSalaryStructureAccuralEffectType().isEmpty() || !accuralDeductionDTOList.get(0).getHrSalaryStructureDeductionEffectType().isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                } else {
                    accuralDeductionService.deleteByEmployeeId(hrSalaryStructureDTOSelected.getEmployeeId().getId());
                    salaryStructureService.deleteSalaryStructure(hrSalaryStructureDTOSelected, getUserData().getUserlogin());
                    salaryStructureDTOList.remove(hrSalaryStructureDTOSelected);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                }
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "حدث خطاء"));
            saveError(e, "salaryStructureListMB", "delete");

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
            context.getSessionMap().put("ScreenMode", "AddemployeeDTOSelected");

            exit("../hremployeesalarystructure/salaryStructureForm.xhtml");

            return "";
        } catch (Exception e) {
            saveError(e, "salaryStructureListMB", "goToAdd");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (hrSalaryStructureDTOSelected != null && hrSalaryStructureDTOSelected.getId() != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("hrSalaryStructureDTOSelected", hrSalaryStructureDTOSelected.getId());
                context.getSessionMap().put("ScreenMode", "Edit");
                exit("../hremployeesalarystructure/salaryStructureForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "salaryStructureListMB", "goToEdit");
            return null;
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setDataModel(new LazyDataModel<HrSalaryStructureDTO>() {
                private static final long serialVersionUID = 1L; 
                @Override
                public List<HrSalaryStructureDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                    setRowCount(salaryStructureService.getTotalRegistors(userData.getUserBranch().getId(), filters).intValue());
                    setSalaryStructureDTOList(salaryStructureService.getALLSalaryStructureByBranchId(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), userData.getUserBranch().getId(), filters));
                    return getSalaryStructureDTOList();
                }
            });
        } catch (Exception e) {
            saveError(e, "salaryStructureListMB", "load");
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the salaryStructureDTOList
     */
    public List<HrSalaryStructureDTO> getSalaryStructureDTOList() {
        if (salaryStructureDTOList == null || salaryStructureDTOList.isEmpty()) {
            salaryStructureDTOList = new ArrayList<>();
        }
        return salaryStructureDTOList;
    }

    /**
     * @param salaryStructureDTOList the salaryStructureDTOList to set
     */
    public void setSalaryStructureDTOList(List<HrSalaryStructureDTO> salaryStructureDTOList) {
        this.salaryStructureDTOList = salaryStructureDTOList;
    }

    /**
     * @return the hrSalaryStructureDTOSelected
     */
    public HrSalaryStructureDTO getHrSalaryStructureDTOSelected() {
        return hrSalaryStructureDTOSelected;
    }

    /**
     * @param hrSalaryStructureDTOSelected the hrSalaryStructureDTOSelected to
     * set
     */
    public void setHrSalaryStructureDTOSelected(HrSalaryStructureDTO hrSalaryStructureDTOSelected) {
        this.hrSalaryStructureDTOSelected = hrSalaryStructureDTOSelected;
    }

    /**
     * @return the dataModel
     */
    public LazyDataModel<HrSalaryStructureDTO> getDataModel() {
        return dataModel;
    }

    /**
     * @param dataModel the dataModel to set
     */
    public void setDataModel(LazyDataModel<HrSalaryStructureDTO> dataModel) {
        this.dataModel = dataModel;
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
}
