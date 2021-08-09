/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.remWorkOrder;

import com.isag.isag.BaseListBean;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.isag.businessservice.RemWorkOrderService;
import com.isag.dto.RemWorkOrderDTO;

/**
 *
 * @author hq004
 */
@Named(value = "workOrderListMB")
@ViewScoped
public class WorkOrderListMB extends BaseListBean {

    private RemWorkOrderDTO remWorkOrderDTO;
    private List<RemWorkOrderDTO> remWorkOrderDTOList;

    @EJB
    RemWorkOrderService remWorkOrderService;
    

    @Override
    @PostConstruct
    public void init() {
        setRemWorkOrderDTOList(remWorkOrderService.findWorkOrders(getUserData().getUserBranch().getId()));
    }

    @Override
    public void delete() {
        setRemWorkOrderDTO(remWorkOrderService.findWorkOrder(getRemWorkOrderDTO().getId()));
        remWorkOrderService.deleteWorkOrder(getRemWorkOrderDTO());
        remWorkOrderDTOList.remove(getRemWorkOrderDTO());
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "AddworkOrderDTO");
            exit("../remWorkOrder/workorderForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "notesReceivablesListMB", "load");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (getRemWorkOrderDTO() != null && getRemWorkOrderDTO().getId() != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("workOrderDTOlected", getRemWorkOrderDTO().getId());
                context.getSessionMap().put("ScreenMode", "EditworkOrderDTO");
                exit("../remWorkOrder/workorderForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "BranchListBean", "goToEdit");
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
     * @return the remWorkOrderDTO
     */
    public RemWorkOrderDTO getRemWorkOrderDTO() {
        return remWorkOrderDTO;
    }

    /**
     * @param remWorkOrderDTO the remWorkOrderDTO to set
     */
    public void setRemWorkOrderDTO(RemWorkOrderDTO remWorkOrderDTO) {
        this.remWorkOrderDTO = remWorkOrderDTO;
    }

    /**
     * @return the remWorkOrderDTOList
     */
    public List<RemWorkOrderDTO> getRemWorkOrderDTOList() {
        return remWorkOrderDTOList;
    }

    /**
     * @param remWorkOrderDTOList the remWorkOrderDTOList to set
     */
    public void setRemWorkOrderDTOList(List<RemWorkOrderDTO> remWorkOrderDTOList) {
        this.remWorkOrderDTOList = remWorkOrderDTOList;
    }

}
