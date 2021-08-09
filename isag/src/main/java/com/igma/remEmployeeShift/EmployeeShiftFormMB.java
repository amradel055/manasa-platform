/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.remEmployeeShift;

import com.isag.businessservice.RemEmployeeShiftDetailService;
import com.isag.isag.BaseFormBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.isag.businessservice.RemEmployeeShiftService;
import com.isag.dto.RemEmployeeShiftDTO;
import com.isag.dto.RemEmployeeShiftDetailDTO;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author hq004
 */
@Named(value = "employeeShiftFormMB")
@ViewScoped
public class EmployeeShiftFormMB extends BaseFormBean {

    private RemEmployeeShiftDTO remEmployeeShiftDTO;

    @EJB
    RemEmployeeShiftService remEmployeeShiftService;
    @EJB
    RemEmployeeShiftDetailService remEmployeeShiftDetailService;

    @Override
    public String save() {
        if (getRemEmployeeShiftDTO().getRemEmployeeShiftDetailList() != null && !getRemEmployeeShiftDTO().getRemEmployeeShiftDetailList().isEmpty()) {
            setRemEmployeeShiftDTO(remEmployeeShiftService.save(getRemEmployeeShiftDTO(), getUserData().getUser()));
            msgSave(getRemEmployeeShiftDTO().getMsg());
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

        } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditemployeeShiftDTO")) {
            try {
                Integer employeeShiftId = (Integer) getContext().getSessionMap().get("employeeShiftDTOlected");
                setRemEmployeeShiftDTO(remEmployeeShiftService.findEmployeeShift(employeeShiftId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteDetail(RemEmployeeShiftDetailDTO remEmployeeShiftDetailDTO) {
        if (remEmployeeShiftDetailDTO != null) {
            if (getRemEmployeeShiftDTO().getRemEmployeeShiftDetailSelected().getId() != null) {
                getRemEmployeeShiftDTO().getRemEmployeeShiftDetailListDeleted().add(remEmployeeShiftDetailDTO);
            }
            getRemEmployeeShiftDTO().getRemEmployeeShiftDetailList().remove(remEmployeeShiftDetailDTO);

            for (RemEmployeeShiftDetailDTO detail : getRemEmployeeShiftDTO().getRemEmployeeShiftDetailList()) {
                if (detail.getEmployeeId()== null && detail.getEmployeeIdBak()!= null) {
                    detail.setEmployeeId(detail.getEmployeeIdBak());
                }
            }
        }
    }

    public void addNewDetail() {
        try {
            setRemEmployeeShiftDTO(remEmployeeShiftDetailService.addDetail(getRemEmployeeShiftDTO(), getIndex()));
        } catch (Exception e) {
            saveError(e, "InstContractFormMB", "addNewDetail");
        }

    }

    public void reset() {
        setRemEmployeeShiftDTO(null);
    }
    
    public void updateRemCompany(){
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSessionMap().put("remCompany", getRemEmployeeShiftDTO().getRemCompanyId().getId());
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
     * @return the remEmployeeShiftDTO
     */
    public RemEmployeeShiftDTO getRemEmployeeShiftDTO() {
        if (remEmployeeShiftDTO == null) {
            remEmployeeShiftDTO = new RemEmployeeShiftDTO();
        }
        return remEmployeeShiftDTO;
    }

    /**
     * @param remEmployeeShiftDTO the remEmployeeShiftDTO to set
     */
    public void setRemEmployeeShiftDTO(RemEmployeeShiftDTO remEmployeeShiftDTO) {
        this.remEmployeeShiftDTO = remEmployeeShiftDTO;
    }

}
