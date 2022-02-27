/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.crmTasks;

import com.toby.businessservice.CRMEventServiceDTO;
import com.toby.dto.CrmEventDTO;
import com.toby.toby.CrmBasicDataForm;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author WIN7
 */
@Named(value = "crmTasksListDTOMB")
@ViewScoped
public class CrmTasksListDTOMB extends CrmBasicDataForm {

    private List<CrmEventDTO> crmEventDTOList;

    private Integer crmTaskDTOSelected;

    private LazyDataModel<CrmEventDTO> dataModel;

    @EJB
    CRMEventServiceDTO cRMEventServiceDTO;

    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @PostConstruct
    public void init() {
        load();
    }

    @Override
    public void load() {
        this.setDataModel(new LazyDataModel<CrmEventDTO>() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<CrmEventDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

                setRowCount(cRMEventServiceDTO.getTotalRegistors(null, getUserData().getUserBranch().getId(), filters, getEmployeeDTOSelected() == null ? null : getEmployeeDTOSelected().getId()).intValue());

                crmEventDTOList = cRMEventServiceDTO.findALLCrmEventByAssignedToAndBranch(null, first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), getUserData().getUserBranch().getId(), filters, getEmployeeDTOSelected() == null ? null : getEmployeeDTOSelected().getId());
                
                return crmEventDTOList;
            }
        });
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void delete() {
        cRMEventServiceDTO.deleteCrmEventDTO(crmTaskDTOSelected, getUserData().getUser());
        deletedMeesage();
    }

    public String goToEdit() {
        try {
            if (crmTaskDTOSelected != null && crmTaskDTOSelected > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("crmEventDTOSelected", crmTaskDTOSelected);
                context.getSessionMap().put("ScreenMode", "EditcrmTaskDTO");
                exit("../crmTodoEvent/TodoEvent.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception ex) {
            saveError(ex, "crmTasksListDTOMB", "goToEdit");
            return null;
        }
    } 
    
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "AddcrmTaskDTO");
            exit("../crmTodoEvent/TodoEvent.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "BranchListBean", "goToAdd");
            return null;
        }
    }

    /**
     * @return the crmEventDTOList
     */
    public List<CrmEventDTO> getCrmEventDTOList() {
        return crmEventDTOList;
    }

    /**
     * @param crmEventDTOList the crmEventDTOList to set
     */
    public void setCrmEventDTOList(List<CrmEventDTO> crmEventDTOList) {
        this.crmEventDTOList = crmEventDTOList;
    }

    /**
     * @return the crmTaskDTOSelected
     */
    public Integer getCrmTaskDTOSelected() {
        return crmTaskDTOSelected;
    }

    /**
     * @param crmTaskDTOSelected the crmTaskDTOSelected to set
     */
    public void setCrmTaskDTOSelected(Integer crmTaskDTOSelected) {
        this.crmTaskDTOSelected = crmTaskDTOSelected;
    }

    /**
     * @return the dataModel
     */
    public LazyDataModel<CrmEventDTO> getDataModel() {
        return dataModel;
    }

    /**
     * @param dataModel the dataModel to set
     */
    public void setDataModel(LazyDataModel<CrmEventDTO> dataModel) {
        this.dataModel = dataModel;
    }
}
