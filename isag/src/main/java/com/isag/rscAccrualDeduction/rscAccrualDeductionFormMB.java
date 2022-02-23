/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rscAccrualDeduction;

import com.isag.businessservice.RscAccrualDeductionService;
import com.isag.converter.SymbolConverter;
import com.isag.dto.RscAccrualDeductionDTO;
import com.isag.isag.RSCFormBasicData;
import com.isag.isag.UserData;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author hq003
 */
@Named(value = "rscAccrualDeductionFormMB")
@ViewScoped
public class rscAccrualDeductionFormMB extends RSCFormBasicData {

    Integer serialDetail = 0;
    private SymbolConverter symbolConverter;

    private RscAccrualDeductionDTO rscAccrualDeductionDTO;
    private Integer accrualDeductionId;

    private List<RscAccrualDeductionDTO> accrualDeductionList;

    private RscAccrualDeductionCommon rscAccrualDeductionCommon;
    private String uri;

    @EJB
    private RscAccrualDeductionService rscAccrualDeductionService;

    @PostConstruct
    @Override
    public void init() {
        load();
    }

    @Override
    public void load() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        setScreenMode((String) context.getSessionMap().get("ScreenMode"));
        getUserData().getUser().setBranchId(getUserData().getUserBranch());
        setUri(((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI());
        fillLists();
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("AddrscAccrualDeductionDTOSelected")) {
            reset();
        } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditrscAccrualDeductionDTOSelected")) {
            // get tenderiD From Session
            setAccrualDeductionId((Integer) context.getSessionMap().get("rscAccrualDeductionDTOSelected"));
            //    extractRegisterDTO = rscExtractRegisterService.findRscExtractRegister(extractRegisterId);
            setRscAccrualDeductionDTO(getRscAccrualDeductionCommon().returnObject(accrualDeductionId));

        }
    }

    public void fillLists() {

    }

    public void print() {
        if (rscAccrualDeductionDTO != null && rscAccrualDeductionDTO.getId() != null) {
            rscAccrualDeductionCommon.print(rscAccrualDeductionDTO);
        }
    }

    @Override
    public String save() {
        try {
            rscAccrualDeductionDTO = rscAccrualDeductionService.addRscAccrual(rscAccrualDeductionDTO, getUserData().getUser());
            if (rscAccrualDeductionDTO.getMsg() != null && !rscAccrualDeductionDTO.getMsg().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), rscAccrualDeductionDTO.getMsg()));
            } else {

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), "تم الحفظ بنجاح"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "حدث خطأ!"));
            e.printStackTrace();
        }
        CloseDlg("dlg3");
        return "";

    }

    public void reset() {

        rscAccrualDeductionDTO = new RscAccrualDeductionDTO();
        getAccrualDeductionList();

    }

    public String exit() {
        if (getUri().contains("rscAccrualDeductionform")) {
            //RSC
            exit("../rscAccrualDeduction/rscAccrualDeductiontList.xhtml");
        } else {
            //HR
            exit("../hrAccrualDeduction/hrAccrualDeductiontList.xhtml");
        }
        return "";
    }

    //-------------------------------------------------------------------------------------------- 
    @Override
    public String getScreenName() {
        return "costcenter";
    }

    /**
     * @return the symbolConverter
     */
    public SymbolConverter getSymbolConverter() {
        return symbolConverter;
    }

    /**
     * @param symbolConverter the symbolConverter to set
     */
    public void setSymbolConverter(SymbolConverter symbolConverter) {
        this.symbolConverter = symbolConverter;
    }

    /**
     * @return the rscAccrualDeductionDTO
     */
    public RscAccrualDeductionDTO getRscAccrualDeductionDTO() {
        if (rscAccrualDeductionDTO == null) {
            rscAccrualDeductionDTO = new RscAccrualDeductionDTO();
        }
        return rscAccrualDeductionDTO;
    }

    /**
     * @param rscAccrualDeductionDTO the rscAccrualDeductionDTO to set
     */
    public void setRscAccrualDeductionDTO(RscAccrualDeductionDTO rscAccrualDeductionDTO) {
        this.rscAccrualDeductionDTO = rscAccrualDeductionDTO;
    }

    /**
     * @return the accrualDeductionId
     */
    public Integer getAccrualDeductionId() {
        return accrualDeductionId;
    }

    /**
     * @param accrualDeductionId the accrualDeductionId to set
     */
    public void setAccrualDeductionId(Integer accrualDeductionId) {
        this.accrualDeductionId = accrualDeductionId;
    }

    /**
     * @return the accrualDeductionList
     */
    public List<RscAccrualDeductionDTO> getAccrualDeductionList() {
        if (accrualDeductionList == null || accrualDeductionList.isEmpty()) {
            if (getUri().contains("rscAccrualDeductionform")) {
                //RSC
                accrualDeductionList = rscAccrualDeductionService.getBranchRscAccrualActiveByBranch(getUserData().getUserBranch().getId(), 2, 3);
            } else {
                //HR
                accrualDeductionList = rscAccrualDeductionService.getBranchRscAccrualActiveByBranch(getUserData().getUserBranch().getId(), 0, 1);
            }
        }
        return accrualDeductionList;
    }

    /**
     * @param accrualDeductionList the accrualDeductionList to set
     */
    public void setAccrualDeductionList(List<RscAccrualDeductionDTO> accrualDeductionList) {
        this.accrualDeductionList = accrualDeductionList;
    }

    /**
     * @return the rscAccrualDeductionCommon
     */
    public RscAccrualDeductionCommon getRscAccrualDeductionCommon() {
        if (rscAccrualDeductionCommon == null) {
            rscAccrualDeductionCommon = new RscAccrualDeductionCommon(rscAccrualDeductionService, getUserData());
        }
        return rscAccrualDeductionCommon;
    }

    /**
     * @param rscAccrualDeductionCommon the rscAccrualDeductionCommon to set
     */
    public void setRscAccrualDeductionCommon(RscAccrualDeductionCommon rscAccrualDeductionCommon) {
        this.rscAccrualDeductionCommon = rscAccrualDeductionCommon;
    }

    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

}
