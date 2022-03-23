/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rscQotationPriceTender;

import com.isag.businessservice.RscPriceTenderDetailWorkUnitService;
import com.isag.businessservice.RscPriceTenderService;
import com.isag.businessservice.RscQotationPriceTenderDetailWorkUnitService;
import com.isag.businessservice.RscQotationPriceTenderService;
import com.isag.dto.RscGenaricLoad;
import com.isag.dto.RscPriceTenderDTO;
import com.isag.dto.RscPriceTenderDetailWorkUnitDTO;
import com.isag.dto.RscQotationPriceTenderDTO;
import com.isag.dto.RscQotationPriceTenderDetailWorkUnitDTO;
import com.isag.dto.RscTenderDTO;
import com.isag.isag.RSCFormBasicData;
import com.isag.isag.UserData;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author hq003
 */
@Named(value = "rscQotationPriceTenderFormMB")
@ViewScoped
public class rscQotationPriceTenderFormMB extends RSCFormBasicData {

    private List<RscQotationPriceTenderDetailWorkUnitDTO> rscPriceTenderDetailWorkUnitDeletedList;
    private List<RscGenaricLoad> priceTenderDTOList;
    private RscQotationPriceTenderDTO priceTenderDTO;
    private RscQotationPriceTenderDetailWorkUnitDTO RscQotationPriceTenderDetailWorkUnitDTOSelected;
    private Integer priceTenderId;
    Integer serialDetail = 0;
    private RscQotationPriceTenderCommon qotationPriceTenderCommon;
    private Boolean subcontractor;

    @EJB
    private RscQotationPriceTenderService rscPriceTenderService;
    @EJB
    private RscPriceTenderDetailWorkUnitService rscPriceTenderDetailWorkUnitService;
    @EJB
    private RscQotationPriceTenderDetailWorkUnitService rscQotationPriceTenderDetailWorkUnitService;
    @EJB
    private RscPriceTenderService priceTenderService;

    @PostConstruct
    @Override
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "rscQotationPriceTenderFormMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            getUserData().getUser().setBranchId(getUserData().getUserBranch());
            fillLists();
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("AddrscPriceTenderDTOSelected")) {
                reset();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditrscPriceTenderDTOSelected")) {
                // get tenderiD From Session
                priceTenderId = (Integer) context.getSessionMap().get("rscPriceTenderDTOSelected");
                priceTenderDTO = getQotationPriceTenderCommon().returnObject(priceTenderId);
                if (priceTenderDTO.getTenderId() != null && priceTenderDTO.getTenderId().getType() == 1) {
                    subcontractor = Boolean.TRUE;
                }

            }
        } catch (Exception e) {
            saveError(e, "rscQotationPriceTenderFormMB", "load");
        }
    }

    public void fillLists() {
        try {
            getRscWorkUnitsGuideDTOList();
        } catch (Exception e) {
            saveError(e, "rscQotationPriceTenderFormMB", "fillLists");
        }
    }

    @Override
    public String save() {
        try {
            priceTenderDTO = rscPriceTenderService.addRscQotationPriceTender(priceTenderDTO, priceTenderDTO.getRscQotationPriceTenderDetailWorkUnitDTOList(), getRscPriceTenderDetailWorkUnitDeletedList(), getUserData().getUser());
            if (priceTenderDTO.getMsg() != null && !priceTenderDTO.getMsg().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), priceTenderDTO.getMsg()));
            } else {

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), "تم الحفظ بنجاح"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "حدث خطأ!"));
            e.printStackTrace();
            saveError(e, "rscQotationPriceTenderFormMB", "save");
        }
        return "";

    }

    public void reset() {
        try {
            RscQotationPriceTenderDetailWorkUnitDTOSelected = null;
            priceTenderDTO = new RscQotationPriceTenderDTO();
            priceTenderDTO.setRscQotationPriceTenderDetailWorkUnitDTOList(new ArrayList<>());
            rscPriceTenderDetailWorkUnitDeletedList = new ArrayList<>();
            priceTenderDTO.setDate(new Date());
        } catch (Exception e) {
            saveError(e, "rscQotationPriceTenderFormMB", "reset");
        }
    }

    public void print() {
        try {

            priceTenderDTO = getQotationPriceTenderCommon().returnObject(priceTenderDTO.getId());
            if (priceTenderDTO != null && priceTenderDTO.getId() != null
                    && priceTenderDTO.getRscQotationPriceTenderDetailWorkUnitDTOList() != null
                    && !priceTenderDTO.getRscQotationPriceTenderDetailWorkUnitDTOList().isEmpty()) {

                getQotationPriceTenderCommon().print(priceTenderDTO);
            }
        } catch (Exception e) {
            saveError(e, "rscQotationPriceTenderFormMB", "print");
        }
    }

    public void updateSummary() {
        try {

            for (RscQotationPriceTenderDetailWorkUnitDTO detail : priceTenderDTO.getRscQotationPriceTenderDetailWorkUnitDTOList()) {
                if (detail.getGuideDTO() == null) {
                    detail.setGuideDTO(detail.getGuideDTOBak());
                }

            }
        } catch (Exception e) {
            saveError(e, "rscQotationPriceTenderFormMB", "updateSummary");
        }
    }

    public String exit() {
        try {

            exit("../rscQotationPriceTender/rscQotationPriceTenderList.xhtml");

            return "";
        } catch (Exception e) {
            saveError(e, "rscQotationPriceTenderFormMB", "exit");
            return null;
        }
    }

    public void addChild() {
        try {
            if (priceTenderDTO.getRscQotationPriceTenderDetailWorkUnitDTOList() == null) {
                priceTenderDTO.setRscQotationPriceTenderDetailWorkUnitDTOList(new ArrayList<>());
            }
            RscQotationPriceTenderDetailWorkUnitDTO detailWorkUnitDTO = new RscQotationPriceTenderDetailWorkUnitDTO();
            setIndex(getIndex() + 1);
            detailWorkUnitDTO.setIndex(getIndex());
            priceTenderDTO.getRscQotationPriceTenderDetailWorkUnitDTOList().add(detailWorkUnitDTO);
        } catch (Exception e) {
            saveError(e, "rscQotationPriceTenderFormMB", "addChild");
        }
    }

    public void validateUnitGuide() {
        try {
            if (RscQotationPriceTenderDetailWorkUnitDTOSelected != null) {
                if (RscQotationPriceTenderDetailWorkUnitDTOSelected.getGuideDTO() != null) {
                    RscQotationPriceTenderDetailWorkUnitDTOSelected.setGuideDTOBak(RscQotationPriceTenderDetailWorkUnitDTOSelected.getGuideDTO());
                }
            }
        } catch (Exception e) {
            saveError(e, "rscQotationPriceTenderFormMB", "validateUnitGuide");
        }
    }

    //---------------------Calculate Methods---------------------------------
    public void calculateTotalUnits() {
        try {

            for (RscQotationPriceTenderDetailWorkUnitDTO tenderDetail : priceTenderDTO.getRscQotationPriceTenderDetailWorkUnitDTOList()) {
                if (tenderDetail.getGuideDTO() != null) {
                    tenderDetail.setQuantity(tenderDetail.getQuantity() != null ? tenderDetail.getQuantity() : BigDecimal.ZERO);
                    tenderDetail.setPrice(tenderDetail.getPrice() != null ? tenderDetail.getPrice() : BigDecimal.ZERO);
                }
                tenderDetail.setTotal((tenderDetail.getQuantity() != null ? tenderDetail.getQuantity() : BigDecimal.ZERO).multiply(tenderDetail.getPrice() != null ? tenderDetail.getPrice() : BigDecimal.ZERO));
            }
        } catch (Exception e) {
            saveError(e, "rscQotationPriceTenderFormMB", "calculateTotalUnits");
        }
    }

    public void deleteRscTenderDetail() {
        try {

            if (RscQotationPriceTenderDetailWorkUnitDTOSelected != null) {
                priceTenderDTO.getRscQotationPriceTenderDetailWorkUnitDTOList().remove(RscQotationPriceTenderDetailWorkUnitDTOSelected);
                if (RscQotationPriceTenderDetailWorkUnitDTOSelected.getId() != null) {
                    rscPriceTenderDetailWorkUnitDeletedList.add(RscQotationPriceTenderDetailWorkUnitDTOSelected);
                }
                updateSummary();
            }
        } catch (Exception e) {
            saveError(e, "rscQotationPriceTenderFormMB", "deleteRscTenderDetail");
        }
    }

    public void loadTender() {
        try {
            priceTenderDTO.setRscQotationPriceTenderDetailWorkUnitDTOList(rscPriceTenderService.loadrscGenaric(priceTenderDTO.getRscGenaricLoad()));
            if (priceTenderDTO.getRscGenaricLoad().getTenderType() != null && priceTenderDTO.getRscGenaricLoad().getTenderType() == 1) {
                subcontractor = Boolean.TRUE;
            } else {
                subcontractor = Boolean.FALSE;
            }
            CloseDlg("dlg2");
            // يحتاج إلى تعديل
//            if (priceTenderDTO.getRscPriceTenderId() != null && priceTenderDTO.getRscPriceTenderId().getId() != null) {
//                priceTenderDTO.setRscQotationPriceTenderDetailWorkUnitDTOList(new ArrayList<>());
//                priceTenderDTO.setRemark(priceTenderDTO.getRscPriceTenderId().getRemark());
//                if (priceTenderDTO.getRscPriceTenderId().getRscTenderDTO() != null && priceTenderDTO.getRscPriceTenderId().getRscTenderDTO().getId() != null) {
//                    RscTenderDTO rscTenderDTO = new RscTenderDTO();
//                    rscTenderDTO.setDocNumber(priceTenderDTO.getRscPriceTenderId().getRscTenderDTO().getDocNumber());
//                    rscTenderDTO.setRemark(priceTenderDTO.getRscPriceTenderId().getRscTenderDTO().getRemark() != null ? priceTenderDTO.getRscPriceTenderId().getRscTenderDTO().getRemark() : "");
//                    rscTenderDTO.setCustomerId(priceTenderDTO.getRscPriceTenderId().getRscTenderDTO().getCustomerId());
//                    rscTenderDTO.setType(priceTenderDTO.getRscPriceTenderId().getRscTenderDTO().getType());
//                }
//
//                CloseDlg("dlg2");
//            }
        } catch (Exception e) {
            saveError(e, "rscQotationPriceTenderFormMB", "loadTender");
        }
    }

    @Override
    public String getScreenName() {
        return "costcenter";
    }

    /**
     * @return the tenderDTO
     */
    public RscQotationPriceTenderDTO getPriceTenderDTO() {
        if (priceTenderDTO == null) {
            priceTenderDTO = new RscQotationPriceTenderDTO();
        }
        return priceTenderDTO;
    }

    /**
     * @param tenderDTO the tenderDTO to set
     */
    public void setPriceTenderDTO(RscQotationPriceTenderDTO priceTenderDTO) {
        this.priceTenderDTO = priceTenderDTO;
    }

    /**
     * @return the rscTenderDetailWorkUnitDTOSelected
     */
    public RscQotationPriceTenderDetailWorkUnitDTO getRscQotationPriceTenderDetailWorkUnitDTOSelected() {
        return RscQotationPriceTenderDetailWorkUnitDTOSelected;
    }

    /**
     * @param rscTenderDetailWorkUnitDTOSelected the
     * rscTenderDetailWorkUnitDTOSelected to set
     */
    public void setRscQotationPriceTenderDetailWorkUnitDTOSelected(RscQotationPriceTenderDetailWorkUnitDTO RscQotationPriceTenderDetailWorkUnitDTOSelected) {
        this.RscQotationPriceTenderDetailWorkUnitDTOSelected = RscQotationPriceTenderDetailWorkUnitDTOSelected;
    }

    /**
     * @return the priceTenderId
     */
    public Integer getPriceTenderId() {
        return priceTenderId;
    }

    /**
     * @param priceTenderId the priceTenderId to set
     */
    public void setPriceTenderId(Integer priceTenderId) {
        this.priceTenderId = priceTenderId;
    }

    /**
     * @return the rscPriceTenderDetailWorkUnitDeletedList
     */
    public List<RscQotationPriceTenderDetailWorkUnitDTO> getRscPriceTenderDetailWorkUnitDeletedList() {
        if (rscPriceTenderDetailWorkUnitDeletedList == null) {
            rscPriceTenderDetailWorkUnitDeletedList = new ArrayList<>();
        }
        return rscPriceTenderDetailWorkUnitDeletedList;
    }

    /**
     * @param rscPriceTenderDetailWorkUnitDeletedList the
     * rscPriceTenderDetailWorkUnitDeletedList to set
     */
    public void setRscPriceTenderDetailWorkUnitDeletedList(List<RscQotationPriceTenderDetailWorkUnitDTO> rscPriceTenderDetailWorkUnitDeletedList) {
        this.rscPriceTenderDetailWorkUnitDeletedList = rscPriceTenderDetailWorkUnitDeletedList;
    }

    /**
     * @return the priceTenderDTOList
     */
    public List<RscGenaricLoad> getPriceTenderDTOList() {
        if (priceTenderDTOList == null || priceTenderDTOList.isEmpty()) {
//            priceTenderDTOList = priceTenderService.getBranchRscPriceTenderActiveByBranch(getUserData().getUserBranch().getId());
            priceTenderDTOList = priceTenderService.loadRscPriceTenderByBranch(getUserData().getUserBranch().getId());
        }
        return priceTenderDTOList;
    }

    /**
     * @param priceTenderDTOList the priceTenderDTOList to set
     */
    public void setPriceTenderDTOList(List<RscGenaricLoad> priceTenderDTOList) {
        this.priceTenderDTOList = priceTenderDTOList;
    }

    /**
     * @return the qotationPriceTenderCommon
     */
    public RscQotationPriceTenderCommon getQotationPriceTenderCommon() {
        if (qotationPriceTenderCommon == null) {
            qotationPriceTenderCommon = new RscQotationPriceTenderCommon(rscPriceTenderService, getUserData());
        }
        return qotationPriceTenderCommon;
    }

    /**
     * @param qotationPriceTenderCommon the qotationPriceTenderCommon to set
     */
    public void setQotationPriceTenderCommon(RscQotationPriceTenderCommon qotationPriceTenderCommon) {
        this.qotationPriceTenderCommon = qotationPriceTenderCommon;
    }

    /**
     * @return the subcontractor
     */
    public Boolean getSubcontractor() {
        return subcontractor;
    }

    /**
     * @param subcontractor the subcontractor to set
     */
    public void setSubcontractor(Boolean subcontractor) {
        this.subcontractor = subcontractor;
    }

}
