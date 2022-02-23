/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rscRestrictionQuantities;

import com.isag.businessservice.RscContractRegisterDetailWorkUnitService;
import com.isag.businessservice.RscContractRegisterService;
import com.isag.businessservice.RscRestrictionQuantitiesDetailService;
import com.isag.businessservice.RscRestrictionQuantitiesService;
import com.isag.businessservice.RscTenderDetailWorkUnitService;
import com.isag.businessservice.RscTenderService;
import com.isag.converter.InvOrganizationSiteConverter;
import com.isag.converter.SymbolConverter;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.RscContractRegisterDTO;
import com.isag.dto.RscContractRegisterDetailWorkUnitDTO;
import com.isag.dto.RscPriceTenderDTO;
import com.isag.dto.RscQotationPriceTenderDTO;
import com.isag.dto.RscRestrictionQuantitiesDTO;
import com.isag.dto.RscRestrictionQuantitiesDetailDTO;
import com.isag.dto.RscTenderDTO;
import com.isag.dto.RscTenderDetailWorkUnitDTO;
import com.isag.isag.RSCFormBasicData;
import com.isag.isag.UserData;
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
@Named(value = "rscRestrictionQuantitiesFormMB")
@ViewScoped
public class rscRestrictionQuantitiesFormMB extends RSCFormBasicData {

    private List<RscRestrictionQuantitiesDetailDTO> rscRestrictionQuantitiesDetailDeletedList;
    private RscRestrictionQuantitiesDTO restrictionQuantitiesDTO;
    private RscContractRegisterDTO contractRegisterDTO;
    private RscRestrictionQuantitiesDetailDTO RscRestrictionQuantitiesDetailDTOSelected;
    private List<RscRestrictionQuantitiesDetailDTO> RestrictionQuantitiesDetail;
    private List<RscRestrictionQuantitiesDetailDTO> selectedUnit;
    private SymbolConverter symbolConverter;
    private Integer restrictionQuantitiesId;
    private InvOrganizationSiteConverter invOrganizationSiteConverter;
    Integer serialDetail = 0;
    private Integer organizationType;

    private List<RscRestrictionQuantitiesDTO> restrictionList;
    private List<RscContractRegisterDTO> contractRegisterDTOList;
    private List<RscTenderDTO> rscTenderList;

    private RscRestrictionQuantitiesCommon rscRestrictionQuantitiesCommon;

    @EJB
    private RscRestrictionQuantitiesService restrictionQuantitiesService;
    @EJB
    private RscRestrictionQuantitiesDetailService restrictionQuantitiesDetailService;
    @EJB
    private RscContractRegisterService contractRegisterService;
    @EJB
    private RscContractRegisterDetailWorkUnitService rscContractRegisterDetailWorkUnitService;
    @EJB
    private RscTenderService tenderService;
    @EJB
    private RscTenderDetailWorkUnitService tenderDetailWorkUnitService;
    

    @PostConstruct
    @Override
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "rscRestrictionQuantitiesFormMB", "init");
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
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("AddrscRestrictionQuantitiesDTOSelected")) {
                reset();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditrscRestrictionQuantitiesDTOSelected")) {
                // get tenderiD From Session
                restrictionQuantitiesId = (Integer) context.getSessionMap().get("rscRestrictionQuantitiesDTOSelected");
                restrictionQuantitiesDTO = getRscRestrictionQuantitiesCommon().returnObject(restrictionQuantitiesId);
                fillList();
            }
        } catch (Exception e) {
            saveError(e, "rscRestrictionQuantitiesFormMB", "load");
        }
    }

    public void fillLists() {
        try {
            getRscWorkUnitsGuideDTOList();
        } catch (Exception e) {
            saveError(e, "rscRestrictionQuantitiesFormMB", "fillLists");
        }

    }

    public void print() {
        try {
            if (restrictionQuantitiesDTO != null && restrictionQuantitiesDTO.getId() != null
                    && restrictionQuantitiesDTO.getRscRestrictionQuantitiesDetailDTOList() != null
                    && !restrictionQuantitiesDTO.getRscRestrictionQuantitiesDetailDTOList().isEmpty()) {
                rscRestrictionQuantitiesCommon.print(restrictionQuantitiesDTO);
            }
        } catch (Exception e) {
            saveError(e, "rscRestrictionQuantitiesFormMB", "print");
        }
    }

    @Override
    public String save() {
        try {
            restrictionQuantitiesDTO = restrictionQuantitiesService.addRscRestrictionQuantities(restrictionQuantitiesDTO, restrictionQuantitiesDTO.getRscRestrictionQuantitiesDetailDTOList(), getRscRestrictionQuantitiesDetailDeletedList(), getUserData().getUser());
            if (restrictionQuantitiesDTO.getMsg() != null && !restrictionQuantitiesDTO.getMsg().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), restrictionQuantitiesDTO.getMsg()));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), "تم الحفظ بنجاح"));
                exit("../rscRestrictionQuantities/rscRestrictionQuantitiesList.xhtml");
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "حدث خطأ!"));
            e.printStackTrace();
            saveError(e, "rscRestrictionQuantitiesFormMB", "save");
        }
        CloseDlg("dlg3");
        return "";

    }

    public void reset() {
        try {
            RscRestrictionQuantitiesDetailDTOSelected = null;
            restrictionQuantitiesDTO = new RscRestrictionQuantitiesDTO();
            restrictionQuantitiesDTO.setDate(new Date());
            restrictionQuantitiesDTO.setRscRestrictionQuantitiesDetailDTOList(new ArrayList<>());

            getRestrictionList();
        } catch (Exception e) {
            saveError(e, "rscRestrictionQuantitiesFormMB", "reset");
        }

    }

    public void addChild() {
        try {
            if (restrictionQuantitiesDTO.getRscRestrictionQuantitiesDetailDTOList() == null) {
                restrictionQuantitiesDTO.setRscRestrictionQuantitiesDetailDTOList(new ArrayList<>());
            }
            RscRestrictionQuantitiesDetailDTO detailWorkUnitDTO = new RscRestrictionQuantitiesDetailDTO();
            setIndex(getIndex() + 1);
            detailWorkUnitDTO.setIndex(getIndex());
            restrictionQuantitiesDTO.getRscRestrictionQuantitiesDetailDTOList().add(detailWorkUnitDTO);
        } catch (Exception e) {
            saveError(e, "rscRestrictionQuantitiesFormMB", "addChild");
        }
    }

    public void validateContract() {
        try {
            if (restrictionQuantitiesDTO != null) {
                if (restrictionQuantitiesDTO.getContractId() == null) {
                    errorMessage("يجب ادخال بيانات العقد");
                }
            }
        } catch (Exception e) {
            saveError(e, "rscRestrictionQuantitiesFormMB", "validateContract");
        }

    }

    public void validateUnitGuide() {
        try {
            if (RscRestrictionQuantitiesDetailDTOSelected != null) {
                if (RscRestrictionQuantitiesDetailDTOSelected.getGuideDTO() != null) {
                    RscRestrictionQuantitiesDetailDTOSelected.setGuideDTOBak(RscRestrictionQuantitiesDetailDTOSelected.getGuideDTO());
                }
            }
        } catch (Exception e) {
            saveError(e, "rscRestrictionQuantitiesFormMB", "validateUnitGuide");
        }

    }

    public String exit() {
        try {

            exit("../rscRestrictionQuantities/rscRestrictionQuantitiesList.xhtml");

            return "";
        } catch (Exception e) {
            saveError(e, "rscRestrictionQuantitiesFormMB", "exit");
            return null;
        }
    }

    //-------------------------------------------------------------------------------------
    public void deleteRscExtractDetail() {
        try {
            setRscRestrictionQuantitiesDetailDeletedList(new ArrayList<>());
            if (RscRestrictionQuantitiesDetailDTOSelected != null) {
                restrictionQuantitiesDTO.getRscRestrictionQuantitiesDetailDTOList().remove(RscRestrictionQuantitiesDetailDTOSelected);
                if (RscRestrictionQuantitiesDetailDTOSelected.getId() != null) {
                    if (getRscRestrictionQuantitiesDetailDeletedList() != null) {
                        getRscRestrictionQuantitiesDetailDeletedList().add(RscRestrictionQuantitiesDetailDTOSelected);
                    }
                }
            }
        } catch (Exception e) {
            saveError(e, "rscRestrictionQuantitiesFormMB", "deleteRscExtractDetail");
        }
    }
    //--------------------------------- Load Contract-------------------------------------------------

    public void loadcontract() {
        try {
            if (restrictionQuantitiesDTO.getContractId() != null && restrictionQuantitiesDTO.getContractId().getId() != null) {
                List<RscContractRegisterDetailWorkUnitDTO> rscDetailDTOList = rscContractRegisterDetailWorkUnitService.getContractDetailWorkUnitByContractId(restrictionQuantitiesDTO.getContractId().getId());
                restrictionList = new ArrayList<>();
                restrictionQuantitiesDTO.setRscRestrictionQuantitiesDetailDTOList(new ArrayList<>());
                for (RscContractRegisterDetailWorkUnitDTO detailWorkUnitDTO : rscDetailDTOList) {
                    RscRestrictionQuantitiesDetailDTO detail = new RscRestrictionQuantitiesDetailDTO();
                    detail.setContractPrice(detailWorkUnitDTO.getPrice());
                    detail.setContractQty(detailWorkUnitDTO.getQuantity());
                    detail.setGuideDTO(detailWorkUnitDTO.getGuideDTO());
                    detail.setGuideDTOBak(detailWorkUnitDTO.getGuideDTO());
                    detail.setIndex(getIndex() + 1);
                    detailWorkUnitDTO.setIndex(getIndex());

                    restrictionQuantitiesDTO.getRscRestrictionQuantitiesDetailDTOList().add(detail);
                    restrictionList.add(restrictionQuantitiesDTO);
                }

                if (restrictionQuantitiesDTO.getContractId() != null) {
                    RscContractRegisterDTO contractRegisterDTO = new RscContractRegisterDTO();
                    contractRegisterDTO.setId(restrictionQuantitiesDTO.getContractId().getId());
                    contractRegisterDTO.setDate(restrictionQuantitiesDTO.getContractId().getDate());
                    contractRegisterDTO.setDocumentNumber(restrictionQuantitiesDTO.getContractId().getDocumentNumber());
                    contractRegisterDTO.setContractTotal(restrictionQuantitiesDTO.getContractId().getContractTotal());

                    // contractRegisterDTO.setFinallReceivedDate(restrictionQuantitiesDTO.getContractId().getFinallReceivedDate());
                    contractRegisterDTO.setRscQotationPriceTenderId(restrictionQuantitiesDTO.getContractId().getRscQotationPriceTenderId());
                    contractRegisterDTO.setRscTenderDTO(restrictionQuantitiesDTO.getContractId().getRscTenderDTO());
                    if (restrictionQuantitiesDTO.getContractId().getSiteId() != null) {
                        InvOrganizationSiteDTO siteDTO = new InvOrganizationSiteDTO();
                        siteDTO.setId(restrictionQuantitiesDTO.getContractId().getSiteId().getId());
                        siteDTO.setName(restrictionQuantitiesDTO.getContractId().getSiteId().getName());
                        siteDTO.setCode(restrictionQuantitiesDTO.getContractId().getSiteId().getCode());
                        contractRegisterDTO.setSiteId(siteDTO);
                    }
                    if (restrictionQuantitiesDTO.getContractId().getRscQotationPriceTenderId() != null) {
                        RscQotationPriceTenderDTO qotationPriceTenderDTO = new RscQotationPriceTenderDTO();
                        qotationPriceTenderDTO.setId(restrictionQuantitiesDTO.getContractId().getRscQotationPriceTenderId().getId());
                        if (restrictionQuantitiesDTO.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId() != null) {
                            RscPriceTenderDTO rscPriceTenderDTO = new RscPriceTenderDTO();
                            rscPriceTenderDTO.setId(restrictionQuantitiesDTO.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getId());

                            if (restrictionQuantitiesDTO.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getRscTenderDTO() != null) {
                                RscTenderDTO rscTenderDTO = new RscTenderDTO();
                                rscTenderDTO.setId(restrictionQuantitiesDTO.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getRscTenderDTO().getId());
                                rscTenderDTO.setDocNumber(restrictionQuantitiesDTO.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getRscTenderDTO().getDocNumber());
                                rscTenderDTO.setDate(restrictionQuantitiesDTO.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getRscTenderDTO().getDate());
                                rscTenderDTO.setSerial(restrictionQuantitiesDTO.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getRscTenderDTO().getSerial());
                                // rscTenderDTO.setRemark(restrictionQuantitiesDTO.getContractId().getRscTenderDTO().getRemark());
                                if (restrictionQuantitiesDTO.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getRscTenderDTO().getCustomerId() != null) {
                                    InvOrganizationSiteDTO orgSiteDTO = new InvOrganizationSiteDTO();
                                    orgSiteDTO.setId(restrictionQuantitiesDTO.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getRscTenderDTO().getCustomerId().getId());
                                    orgSiteDTO.setName(restrictionQuantitiesDTO.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getRscTenderDTO().getCustomerId().getName());
                                    orgSiteDTO.setCode(restrictionQuantitiesDTO.getContractId().getRscQotationPriceTenderId().getRscPriceTenderId().getRscTenderDTO().getCustomerId().getCode());
                                    rscTenderDTO.setCustomerId(orgSiteDTO);
                                }
                                restrictionQuantitiesDTO.setTenderId(rscTenderDTO);
                            }
                            qotationPriceTenderDTO.setRscPriceTenderId(rscPriceTenderDTO);
                        }
                        contractRegisterDTO.setRscQotationPriceTenderId(qotationPriceTenderDTO);

                        contractRegisterDTO.setIndex(getIndex() + 1);
                    }
                    restrictionQuantitiesDTO.setIndex(getIndex() + 1);
                    restrictionQuantitiesDTO.setContractId(contractRegisterDTO);
                }

                CloseDlg("dlg2");
            }
        } catch (Exception e) {
            saveError(e, "rscRestrictionQuantitiesFormMB", "loadcontract");
        }
    }

    public void loadTender() {
        try {
            if (restrictionQuantitiesDTO.getTenderId() != null && restrictionQuantitiesDTO.getTenderId().getId() != null) {
                List<RscTenderDetailWorkUnitDTO> rscTenderDetailWorkUnitDTOList = tenderDetailWorkUnitService.getTenderDetailWorkUnitByTenderId(restrictionQuantitiesDTO.getTenderId().getId());
                restrictionList = new ArrayList<>();
                restrictionQuantitiesDTO.setRscRestrictionQuantitiesDetailDTOList(new ArrayList<>());
                for (RscTenderDetailWorkUnitDTO detailWorkUnitDTO : rscTenderDetailWorkUnitDTOList) {
                    RscRestrictionQuantitiesDetailDTO detail = new RscRestrictionQuantitiesDetailDTO();
                    detail.setContractPrice(detailWorkUnitDTO.getPrice());
                    detail.setContractQty(detailWorkUnitDTO.getQuantity());
                    detail.setGuideDTO(detailWorkUnitDTO.getGuideDTO());
                    detail.setGuideDTOBak(detailWorkUnitDTO.getGuideDTO());
                    detail.setIndex(getIndex() + 1);
                    detailWorkUnitDTO.setIndex(getIndex());

                    restrictionQuantitiesDTO.getRscRestrictionQuantitiesDetailDTOList().add(detail);
                    restrictionList.add(restrictionQuantitiesDTO);
                }

//                if (restrictionQuantitiesDTO.getTenderId() != null) {
//                RscContractRegisterDTO contractRegisterDTO = new RscContractRegisterDTO();
//                contractRegisterDTO.setId(restrictionQuantitiesDTO.getTenderId().getId());
//                contractRegisterDTO.setDate(restrictionQuantitiesDTO.getTenderId().getDate());
//                contractRegisterDTO.setDocumentNumber(restrictionQuantitiesDTO.getTenderId().getDocNumber());
//                contractRegisterDTO.setSerial(restrictionQuantitiesDTO.getTenderId().getSerial());

//                if (restrictionQuantitiesDTO.getTenderId().getCustomerId() != null) {
//                    InvOrganizationSiteDTO siteDTO = new InvOrganizationSiteDTO();
//                    siteDTO.setId(restrictionQuantitiesDTO.getTenderId().getCustomerId().getId());
//                    siteDTO.setName(restrictionQuantitiesDTO.getTenderId().getCustomerId().getName());
//                    siteDTO.setCode(restrictionQuantitiesDTO.getTenderId().getCustomerId().getCode());
//                    contractRegisterDTO.setSiteId(siteDTO);
//                }
//                restrictionQuantitiesDTO.setIndex(getIndex() + 1);
//                restrictionQuantitiesDTO.setContractId(contractRegisterDTO);
//                }

                CloseDlg("dlg2");
            }
        } catch (Exception e) {
            saveError(e, "rscRestrictionQuantitiesFormMB", "loadcontract");
        }
    }

    //-------------------------------------------------------------------------------------------- 
    @Override
    public String getScreenName() {
        return "costcenter";
    }

    /**
     * @return the tenderDTO
     */
    public RscRestrictionQuantitiesDTO getRestrictionQuantitiesDTO() {
        if (restrictionQuantitiesDTO == null) {
            restrictionQuantitiesDTO = new RscRestrictionQuantitiesDTO();
        }
        return restrictionQuantitiesDTO;
    }

    /**
     * @param tenderDTO the tenderDTO to set
     */
    public void setRestrictionQuantitiesDTO(RscRestrictionQuantitiesDTO RestrictionQuantitiesDTO) {
        this.restrictionQuantitiesDTO = RestrictionQuantitiesDTO;
    }

    /**
     * @return the rscTenderDetailWorkUnitDTOSelected
     */
    public RscRestrictionQuantitiesDetailDTO getRscRestrictionQuantitiesDetailDTOSelected() {
        return RscRestrictionQuantitiesDetailDTOSelected;
    }

    /**
     * @param rscTenderDetailWorkUnitDTOSelected the
     * rscTenderDetailWorkUnitDTOSelected to set
     */
    public void setRscRestrictionQuantitiesDetailDTOSelected(RscRestrictionQuantitiesDetailDTO RscRestrictionQuantitiesDetailDTOSelected) {
        this.RscRestrictionQuantitiesDetailDTOSelected = RscRestrictionQuantitiesDetailDTOSelected;
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
     * @return the invOrganizationSiteConverter
     */
    public InvOrganizationSiteConverter getInvOrganizationSiteConverter() {
        return invOrganizationSiteConverter;
    }

    /**
     * @param invOrganizationSiteConverter the invOrganizationSiteConverter to
     * set
     */
    public void setInvOrganizationSiteConverter(InvOrganizationSiteConverter invOrganizationSiteConverter) {
        this.invOrganizationSiteConverter = invOrganizationSiteConverter;
    }

    public Integer getRestrictionQuantitiesId() {
        return restrictionQuantitiesId;
    }

    public void setRestrictionQuantitiesId(Integer RestrictionQuantitiesId) {
        this.restrictionQuantitiesId = RestrictionQuantitiesId;
    }

    /**
     * @return the ExtractRegisterDetail
     */
    public List<RscRestrictionQuantitiesDetailDTO> getRestrictionQuantitiesDetail() {
        if (RestrictionQuantitiesDetail == null) {
            setRestrictionQuantitiesDetail(new ArrayList<>());
        }
        return RestrictionQuantitiesDetail;
    }

    /**
     * @param ExtractRegisterDetail the ExtractRegisterDetail to set
     */
    public void setRestrictionQuantitiesDetail(List<RscRestrictionQuantitiesDetailDTO> RestrictionQuantitiesDetail) {
        this.RestrictionQuantitiesDetail = RestrictionQuantitiesDetail;
    }

    /**
     * @return the selectedUnit
     */
    public List<RscRestrictionQuantitiesDetailDTO> getSelectedUnit() {
        return selectedUnit;
    }

    /**
     * @param selectedUnit the selectedUnit to set
     */
    public void setSelectedUnit(List<RscRestrictionQuantitiesDetailDTO> selectedUnit) {
        this.selectedUnit = selectedUnit;
    }

    /**
     * @return the rscExtractRegisterDetailWorkUnitDeletedList
     */
    public List<RscRestrictionQuantitiesDetailDTO> getRscRestrictionQuantitiesDetailDeletedList() {
        if (rscRestrictionQuantitiesDetailDeletedList == null) {
            rscRestrictionQuantitiesDetailDeletedList = new ArrayList<>();
        }
        return rscRestrictionQuantitiesDetailDeletedList;
    }

    public void setRscRestrictionQuantitiesDetailDeletedList(List<RscRestrictionQuantitiesDetailDTO> rscRestrictionQuantitiesDetailDeletedList) {
        this.rscRestrictionQuantitiesDetailDeletedList = rscRestrictionQuantitiesDetailDeletedList;
    }

    /**
     * @return the tenderList
     */
    public List<RscRestrictionQuantitiesDTO> getRestrictionList() {
        if (restrictionList == null || restrictionList.isEmpty()) {
            restrictionList = restrictionQuantitiesService.getBranchRscRestrictionQuantitiesActiveByBranch(getUserData().getUserBranch().getId());
        }
        return restrictionList;
    }

    /**
     * @param tenderList the tenderList to set
     */
    public void setRestrictionList(List<RscRestrictionQuantitiesDTO> restrictionList) {
        this.restrictionList = restrictionList;
    }

    /**
     * @return the contractRegisterDTO
     */
    public RscContractRegisterDTO getContractRegisterDTO() {
        if (contractRegisterDTO == null) {
            contractRegisterDTO = new RscContractRegisterDTO();
        }

        return contractRegisterDTO;
    }

    /**
     * @param contractRegisterDTO the contractRegisterDTO to set
     */
    public void setContractRegisterDTO(RscContractRegisterDTO contractRegisterDTO) {
        this.contractRegisterDTO = contractRegisterDTO;
    }

    /**
     * @return the contractRegisterDTOList
     */
    public List<RscContractRegisterDTO> getContractRegisterDTOList() {
        if (contractRegisterDTOList == null || contractRegisterDTOList.isEmpty()) {
            contractRegisterDTOList = contractRegisterService.getBranchRscContractRegisterActiveByBranch(getUserData().getUserBranch().getId());
        }

        return contractRegisterDTOList;
    }

    /**
     * @param contractRegisterDTOList the contractRegisterDTOList to set
     */
    public void setContractRegisterDTOList(List<RscContractRegisterDTO> contractRegisterDTOList) {
        this.contractRegisterDTOList = contractRegisterDTOList;
    }

    /**
     * @return the contractRegisterService
     */
    public RscContractRegisterService getContractRegisterService() {
        return contractRegisterService;
    }

    /**
     * @param contractRegisterService the contractRegisterService to set
     */
    public void setContractRegisterService(RscContractRegisterService contractRegisterService) {
        this.contractRegisterService = contractRegisterService;
    }

    /**
     * @return the rscRestrictionQuantitiesCommon
     */
    public RscRestrictionQuantitiesCommon getRscRestrictionQuantitiesCommon() {
        if (rscRestrictionQuantitiesCommon == null) {
            rscRestrictionQuantitiesCommon = new RscRestrictionQuantitiesCommon(restrictionQuantitiesService, getUserData());
        }
        return rscRestrictionQuantitiesCommon;
    }

    /**
     * @param rscRestrictionQuantitiesCommon the rscRestrictionQuantitiesCommon
     * to set
     */
    public void setRscRestrictionQuantitiesCommon(RscRestrictionQuantitiesCommon rscRestrictionQuantitiesCommon) {
        this.rscRestrictionQuantitiesCommon = rscRestrictionQuantitiesCommon;
    }

    /**
     * @return the rscTenderList
     */
    public List<RscTenderDTO> getRscTenderList() {

        if (rscTenderList == null || rscTenderList.isEmpty()) {
            rscTenderList = new ArrayList<>();
            rscTenderList = tenderService.getAllRscTenderByBranchId(getUserData().getUser());

        }
        return rscTenderList;
    }

    /**
     * @param rscTenderList the rscTenderList to set
     */
    public void setRscTenderList(List<RscTenderDTO> rscTenderList) {
        this.rscTenderList = rscTenderList;
    }

    /**
     * @return the tenderService
     */
    public RscTenderService getTenderService() {
        return tenderService;
    }

    /**
     * @param tenderService the tenderService to set
     */
    public void setTenderService(RscTenderService tenderService) {
        this.tenderService = tenderService;
    }

    /**
     * @return the tenderDetailWorkUnitService
     */
    public RscTenderDetailWorkUnitService getTenderDetailWorkUnitService() {
        return tenderDetailWorkUnitService;
    }

    /**
     * @param tenderDetailWorkUnitService the tenderDetailWorkUnitService to set
     */
    public void setTenderDetailWorkUnitService(RscTenderDetailWorkUnitService tenderDetailWorkUnitService) {
        this.tenderDetailWorkUnitService = tenderDetailWorkUnitService;
    }

    /**
     * @return the organizationType
     */
    public Integer getOrganizationType() {
        if(organizationType == null){
            organizationType = 1;
}
        return organizationType;
    }

    /**
     * @param organizationType the organizationType to set
     */
    public void setOrganizationType(Integer organizationType) {
        this.organizationType = organizationType;
    }

}
