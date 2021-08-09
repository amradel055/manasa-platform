/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.remWorkOrder;

import com.isag.businessservice.RemWorkOrderDetailService;
import com.isag.isag.BaseFormBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.isag.businessservice.RemWorkOrderService;
import com.isag.dto.RemWorkOrderDTO;
import com.isag.dto.RemWorkOrderDetailDTO;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author hq004
 */
@Named(value = "workOrderFormMB")
@ViewScoped
public class WorkOrderFormMB extends BaseFormBean {

    private RemWorkOrderDTO remWorkOrderDTO;

    @EJB
    RemWorkOrderService remWorkOrderService;
    @EJB
    RemWorkOrderDetailService remWorkOrderDetailService;

    @Override
    public String save() {
        if (getRemWorkOrderDTO().getRemWorkOrderDetailDTOList() != null && !getRemWorkOrderDTO().getRemWorkOrderDetailDTOList().isEmpty()) {
            setRemWorkOrderDTO(remWorkOrderService.save(getRemWorkOrderDTO(), getUserData().getUser()));
            msgSave(getRemWorkOrderDTO().getMsg());
        }else{
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد تفاصيل للفاتورة"));
        
        }
        return "";
    }

    @Override
    @PostConstruct
    public void init() {
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("AddbusesDTO")) {

        } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditworkOrderDTO")) {
            try {
                Integer workOrderId = (Integer) getContext().getSessionMap().get("workOrderDTOlected");
                setRemWorkOrderDTO(remWorkOrderService.findWorkOrder(workOrderId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteDetail(RemWorkOrderDetailDTO remWorkOrderDetailDTO) {
        if (remWorkOrderDetailDTO != null) {
            if (getRemWorkOrderDTO().getRemWorkOrderDetailDTOSelected().getId() != null) {
                getRemWorkOrderDTO().getRemWorkOrderDetailDTOListDeleted().add(remWorkOrderDetailDTO);
            }
            getRemWorkOrderDTO().getRemWorkOrderDetailDTOList().remove(remWorkOrderDetailDTO);

            for (RemWorkOrderDetailDTO detail : getRemWorkOrderDTO().getRemWorkOrderDetailDTOList()) {
                if (detail.getTrafficId() == null && detail.getTrafficIdBak()!= null) {
                    detail.setTrafficId(detail.getTrafficIdBak());
                }
            }
        }
    }

    public void addNewDetail() {
        try {
            setRemWorkOrderDTO(remWorkOrderDetailService.addDetail(getRemWorkOrderDTO(), getIndex()));
        } catch (Exception e) {
            saveError(e, "InstContractFormMB", "addNewDetail");
        }

    }

    public void reset() {
        setRemWorkOrderDTO(null);
    }

    public String exit() {
        exit("../rembuses/busesList.xhtml");
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
     * @return the remWorkOrderDTO
     */
    public RemWorkOrderDTO getRemWorkOrderDTO() {
        if (remWorkOrderDTO == null) {
            remWorkOrderDTO = new RemWorkOrderDTO();
        }
        return remWorkOrderDTO;
    }

    /**
     * @param remWorkOrderDTO the remWorkOrderDTO to set
     */
    public void setRemWorkOrderDTO(RemWorkOrderDTO remWorkOrderDTO) {
        this.remWorkOrderDTO = remWorkOrderDTO;
    }

}
