/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.checkUp;

import com.isag.businessservice.CheckUpService;
import com.isag.entity.HosCheckUp;
import com.isag.entiy.HosCheckUpEntity;
import com.isag.entiy.HosCheckingUpEntity;
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
 * @author WIN7
 */
@Named(value = "checkUpListMB")
@ViewScoped
public class checkUpListMB extends BaseListBean {

    private UserData userData;
    private String screenMode;
    private Integer branchId;
    private Integer companyId;

    private Integer hosCheckUpIdSeclected;

    // DB entites
    private HosCheckUp hosCheckUp;
    private List<HosCheckUp> hosCheckUpList;

    // Interface Entities
    private HosCheckUpEntity hosCheckUpEntity;
    private HosCheckingUpEntity hosCheckUpEntitySelected;
    private List<HosCheckUpEntity> hosCheckUpEntityList;
    private HosCheckingUpEntity hosCheckingUpEntity;
    private List<HosCheckingUpEntity> hosCheckingUpEntityList;
    @EJB
    private CheckUpService hosCheckUpService;

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "checkUpListMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            userData = new UserData();
            hosCheckUp = new HosCheckUp();

            hosCheckUpList = new ArrayList<>();
            hosCheckUpEntity = new HosCheckUpEntity();
            hosCheckUpEntitySelected = new HosCheckingUpEntity();
            hosCheckingUpEntity = new HosCheckingUpEntity();
            hosCheckUpEntityList = new ArrayList<>();
            hosCheckingUpEntityList = new ArrayList<>();

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setBranchId(userData.getUserBranch().getId());
            setCompanyId(userData.getCompany().getId());

            hosCheckUpList = hosCheckUpService.getALLHosCheckUpRecievableByBranchId(getBranchId());

            for (HosCheckUp gbt : hosCheckUpList) {

                hosCheckingUpEntity = new HosCheckingUpEntity();
                hosCheckingUpEntity.setSerial(gbt.getSerial());
                hosCheckingUpEntity.setHosDoctor(gbt.getHosDoctorId());
                hosCheckingUpEntity.setHosPatientRegistration(gbt.getHosPatientRegisterationId());
                hosCheckingUpEntity.setDate(gbt.getDate());
                hosCheckingUpEntity.setRemark(gbt.getRemark());
                hosCheckingUpEntity.setId(gbt.getId());
                hosCheckingUpEntityList.add(hosCheckingUpEntity);
            }
        } catch (Exception e) {
            saveError(e, "checkUpListMB", "load");
        }
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");
            exit("../MovementClinics/CheckingUpFrom.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "checkUpListMB", "goToAdd");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (hosCheckUpIdSeclected != null && hosCheckUpIdSeclected > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("hosCheckUpIdSeclected", hosCheckUpIdSeclected);
                context.getSessionMap().put("ScreenMode", "Edit");
                exit("../MovementClinics/CheckingUpFrom.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "checkUpListMB", "goToEdit");
            return null;
        }
    }

    @Override
    public void delete() {
        try {

            Map<String, String> userDDs = userData.getUserDDs();

            Integer hosCheckUpId = hosCheckUpEntitySelected.getId() != null ? hosCheckUpEntitySelected.getId() : null;
            hosCheckUp = new HosCheckUp();
            hosCheckUp.setId(hosCheckUpId);

            try {
                hosCheckUpService.deleteHosCheckUp(hosCheckUp);
                hosCheckingUpEntityList.remove(hosCheckUpEntitySelected);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
            }
        } catch (Exception e) {
            saveError(e, "checkUpListMB", "delete");
        }

    }

    /*    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {

     HosCheckUpEntity transactionEntity = (HosCheckUpEntity) actionEvent.getComponent().getAttributes().get("varValue");
     startPrintingProcess(transactionEntity);
     List<HosCheckUpDetailEntity> gbtdes = new ArrayList<>();

     if (hosCheckUpEntity != null && hosCheckUpEntity.getId() != null) {
     if (getHosCheckUpDetailEntityList() != null && !getHosCheckUpDetailEntityList().isEmpty()) {
     gbtdes.add(getHosCheckUpDetailEntityList().get(0));
     fillReport(prepareReport(), getUserData().getReportPath() + "notesReceivables.jasper", gbtdes, "pdf");
     }
     } else {
     if (getHosCheckUpDetailEntityList() != null && !getHosCheckUpDetailEntityList().isEmpty()) {
     gbtdes.add(getHosCheckUpDetailEntityList().get(0));
     fillReport(prepareReport(), getUserData().getReportPath() + "notesReceivables.jasper", gbtdes, "pdf");
     }
     }
     }*/
    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public String getScreenMode() {
        return screenMode;
    }

    public void setScreenMode(String screenMode) {
        this.screenMode = screenMode;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getHosCheckUpIdSeclected() {
        return hosCheckUpIdSeclected;
    }

    public void setHosCheckUpIdSeclected(Integer hosCheckUpIdSeclected) {
        this.hosCheckUpIdSeclected = hosCheckUpIdSeclected;
    }

    public HosCheckUp getHosCheckUp() {
        return hosCheckUp;
    }

    public void setHosCheckUp(HosCheckUp hosCheckUp) {
        this.hosCheckUp = hosCheckUp;
    }

    public List<HosCheckUp> getHosCheckUpList() {
        return hosCheckUpList;
    }

    public void setHosCheckUpList(List<HosCheckUp> hosCheckUpList) {
        this.hosCheckUpList = hosCheckUpList;
    }

    public HosCheckUpEntity getHosCheckUpEntity() {
        return hosCheckUpEntity;
    }

    public void setHosCheckUpEntity(HosCheckUpEntity hosCheckUpEntity) {
        this.hosCheckUpEntity = hosCheckUpEntity;
    }

    public HosCheckingUpEntity getHosCheckUpEntitySelected() {
        return hosCheckUpEntitySelected;
    }

    public void setHosCheckUpEntitySelected(HosCheckingUpEntity hosCheckUpEntitySelected) {
        this.hosCheckUpEntitySelected = hosCheckUpEntitySelected;
    }

    public List<HosCheckUpEntity> getHosCheckUpEntityList() {
        return hosCheckUpEntityList;
    }

    public void setHosCheckUpEntityList(List<HosCheckUpEntity> hosCheckUpEntityList) {
        this.hosCheckUpEntityList = hosCheckUpEntityList;
    }

    /**
     * @return the hosCheckingUpEntity
     */
    public HosCheckingUpEntity getHosCheckingUpEntity() {
        return hosCheckingUpEntity;
    }

    /**
     * @param hosCheckingUpEntity the hosCheckingUpEntity to set
     */
    public void setHosCheckingUpEntity(HosCheckingUpEntity hosCheckingUpEntity) {
        this.hosCheckingUpEntity = hosCheckingUpEntity;
    }

    /**
     * @return the HosCheckingUpEntityList
     */
    public List<HosCheckingUpEntity> getHosCheckingUpEntityList() {
        return hosCheckingUpEntityList;
    }

    /**
     * @param HosCheckingUpEntityList the HosCheckingUpEntityList to set
     */
    public void setHosCheckingUpEntityList(List<HosCheckingUpEntity> hosCheckingUpEntityList) {
        this.hosCheckingUpEntityList = hosCheckingUpEntityList;
    }
}
