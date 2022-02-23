/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rscTender;

import com.isag.businessservice.RscTenderService;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.IsagDocumentDTO;
import com.isag.dto.RscTenderDTO;
import com.isag.dto.RscTenderDetailWorkUnitDTO;
import com.isag.dto.RscTenderRscAccrualDeductionDTO;
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
import org.primefaces.model.UploadedFile;

/**
 *
 * @author hq003
 */
@Named(value = "rscTenderFormMB")
@ViewScoped
public class RscTenderFormMB extends RSCFormBasicData {

    private RscTenderDTO tenderDTO;
    private RscTenderDetailWorkUnitDTO rscTenderDetailWorkUnitDTOSelected;
    private RscTenderRscAccrualDeductionDTO rscTenderRscAccrualDeductionDTOSelected;
    private RscTenderRscAccrualDeductionDTO rscTenderRscAccrualDeductionDTODeductionSelected;

    private List<IsagDocumentDTO> isagDocumentDTOList;
    private List<IsagDocumentDTO> isagDocumentDTODeletedList;
    private IsagDocumentDTO isagDocumentDTOSelected;

    private InvOrganizationSiteDTO test;
    private boolean updateSelectedValue;
    private RscTenderCommon rscTenderCommon;

    @EJB
    private RscTenderService rscTenderService;

    private UploadedFile file;
    private List<UploadedFile> files;

    @PostConstruct
    @Override
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "RscTenderFormMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            getUserData().getUser().setBranchId(getUserData().getUserBranch());

            updateSelectedValue = false;
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("AddrscTenderDTOSelected")) {
                reset();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditrscTenderDTOSelected")) {
                Integer tenderId = (Integer) context.getSessionMap().get("rscTenderDTOSelected");
                tenderDTO = getRscTenderCommon().returnObject(tenderId);
                fillLists();
            }
        } catch (Exception e) {
            saveError(e, "RscTenderFormMB", "load");
        }
    }

    public void fillLists() {
        try {
            getCustomerList();
            getRscWorkUnitsGuideDTOList();
            getRscDeductionDTOList();
            getRscAccrualDTOList();
            getProfessionalBusinessConsultantList();
            getProjectManagerList();
            getGeneralConsultantList();
        } catch (Exception e) {
            saveError(e, "RscTenderFormMB", "fillLists");
        }
    }

    @Override
    public String save() {
        try {
            if (tenderDTO != null && tenderDTO.getRscTenderDetailWorkUnitDTOList() != null && !tenderDTO.getRscTenderDetailWorkUnitDTOList().isEmpty()) {
                if (getUri().contains("newrscTenderform_1_1")) {
                    tenderDTO.setType(1);
                } else {
                    tenderDTO.setType(0);
                }
                tenderDTO = rscTenderService.addRscTender(tenderDTO, getUserData().getUser());
                if (tenderDTO.getMsg() != null && !tenderDTO.getMsg().isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), tenderDTO.getMsg()));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), "تم الحفظ بنجاح"));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد تفاصيل!"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "حدث خطأ!"));
            e.printStackTrace();
            saveError(e, "RscTenderFormMB", "save");

        }
        return "";

    }

    public void print() {
        try {
            if (tenderDTO != null && tenderDTO.getId() != null
                    && tenderDTO.getRscTenderDetailWorkUnitDTOList() != null
                    && !tenderDTO.getRscTenderDetailWorkUnitDTOList().isEmpty()) {
                rscTenderCommon.print(tenderDTO);
            }
        } catch (Exception e) {
            saveError(e, "RscTenderFormMB", "print");
        }
    }

    public String exit() {
        try {

            exit("../rscTender/rscTenderList.xhtml");

            return "";
        } catch (Exception e) {
            saveError(e, "RscTenderFormMB", "exit");
            return null;
        }
    }

    public void reset() {
        try {
            rscTenderDetailWorkUnitDTOSelected = null;
            tenderDTO = new RscTenderDTO();
            tenderDTO.setRscTenderDetailWorkUnitDTOList(new ArrayList<>());
            tenderDTO.setDate(new Date());
            setIsagDocumentDTOList(new ArrayList<>());
            setIsagDocumentDTODeletedList(new ArrayList<>());
        } catch (Exception e) {
            saveError(e, "RscTenderFormMB", "reset");
        }
    }

    public void updateWorkUnitSummary() {
        try {
            tenderDTO = rscTenderService.updateWorkUnitSummary(tenderDTO);
        } catch (Exception e) {
            saveError(e, "RscTenderFormMB", "updateSummary");
        }
    }

    public void updateAccuralSummary() {
        try {
            tenderDTO = rscTenderService.updateAccuralSummary(tenderDTO);
        } catch (Exception e) {
            saveError(e, "RscTenderFormMB", "updateSummary");
        }
    }

    public void updateDeductionAccural() {
        try {
            tenderDTO = rscTenderService.updateDeductionAccural(tenderDTO);
        } catch (Exception e) {
            saveError(e, "SalaryStructureForm", "calculateTotalDeduction");
        }
    }

    public void validateUnitGuide() {
        try {
            if (rscTenderDetailWorkUnitDTOSelected != null) {
                if (rscTenderDetailWorkUnitDTOSelected.getGuideDTO() != null) {
                    rscTenderDetailWorkUnitDTOSelected.setGuideDTOBak(rscTenderDetailWorkUnitDTOSelected.getGuideDTO());
                }
            }
        } catch (Exception e) {
            saveError(e, "RscTenderFormMB", "validateUnitGuide");
        }
    }

    public void addAccrualDetail() {
        try {
            RscTenderRscAccrualDeductionDTO rscTenderRscAccrualDeductionDTO = new RscTenderRscAccrualDeductionDTO();
            rscTenderRscAccrualDeductionDTO.setIndex(getIndex() + 1);
            rscTenderRscAccrualDeductionDTO.setRscTenderId(tenderDTO);
            tenderDTO.getRscTenderRscAccrualDeductionDTOAccruaList().add(rscTenderRscAccrualDeductionDTO);
        } catch (Exception e) {
            saveError(e, "RscTenderFormMB", "addAccrualDetail");
        }
    }

    public void calculateTotalUnits() {
        try {
            tenderDTO.setTotalValue(BigDecimal.ZERO);
            for (RscTenderDetailWorkUnitDTO detail : tenderDTO.getRscTenderDetailWorkUnitDTOList()) {
                if (detail.getGuideDTO() != null) {
                    detail.setQuantity(detail.getQuantity() != null ? detail.getQuantity() : BigDecimal.ZERO);
                    detail.setPrice(detail.getPrice() != null ? detail.getPrice() : BigDecimal.ZERO);
                }
                detail.setTotal((detail.getQuantity() != null ? detail.getQuantity() : BigDecimal.ZERO).multiply(detail.getPrice() != null ? detail.getPrice() : BigDecimal.ZERO));
                tenderDTO.setTotalValue((tenderDTO.getTotalValue() == null ? BigDecimal.ZERO : tenderDTO.getTotalValue()).add(detail.getTotal() == null ? BigDecimal.ZERO : detail.getTotal()));
            }
        } catch (Exception e) {
            saveError(e, "rscQotationPriceTenderFormMB", "calculateTotalUnits");
        }
    }

    public void deleteAccrualDetail() {
        try {
            if (rscTenderRscAccrualDeductionDTOSelected != null) {
                tenderDTO.getRscTenderRscAccrualDeductionDTOAccruaList().remove(rscTenderRscAccrualDeductionDTOSelected);
                updateAccuralSummary();

                if (rscTenderRscAccrualDeductionDTOSelected.getId() != null) {
                    tenderDTO.getRscTenderRscAccrualDeductionDTODeletedList().add(rscTenderRscAccrualDeductionDTOSelected);
                }
            }
        } catch (Exception e) {
            saveError(e, "RscTenderFormMB", "deleteAccrualDetail");
        }
    }

    public void addDeductionDetail() {
        try {
            RscTenderRscAccrualDeductionDTO rscTenderRscAccrualDeductionDTO = new RscTenderRscAccrualDeductionDTO();
            rscTenderRscAccrualDeductionDTO.setIndex(getIndex() + 1);
            rscTenderRscAccrualDeductionDTO.setRscTenderId(tenderDTO);
            tenderDTO.getRscTenderRscAccrualDeductionDTODeductionList().add(rscTenderRscAccrualDeductionDTO);
        } catch (Exception e) {
            saveError(e, "RscTenderFormMB", "addDeductionDetail");
        }
    }

    public void deletedeDuctionDetail() {
        try {
            if (rscTenderRscAccrualDeductionDTODeductionSelected != null) {
                tenderDTO.getRscTenderRscAccrualDeductionDTODeductionList().remove(rscTenderRscAccrualDeductionDTODeductionSelected);
                updateDeductionAccural();
                if (rscTenderRscAccrualDeductionDTODeductionSelected.getId() != null) {
                    tenderDTO.getRscTenderRscAccrualDeductionDTODeletedList().add(rscTenderRscAccrualDeductionDTODeductionSelected);
                }
            }
        } catch (Exception e) {
            saveError(e, "RscTenderFormMB", "deletedeDuctionDetail");
        }
    }

    public void addDocumentDetail() {
        try {
            IsagDocumentDTO isagDocumentDTO = new IsagDocumentDTO();
            isagDocumentDTO.setIndex(getIndex() + 1);
            isagDocumentDTO.setIdentityId(tenderDTO.getId());
            isagDocumentDTO.setDocumentName(file.getFileName());
            getFiles().add(file);
            isagDocumentDTOList.add(isagDocumentDTO);

        } catch (Exception e) {
            saveError(e, "RscTenderFormMB", "addDocumentDetail");
        }
    }

    public void deletedeDocumentDetail() {
        try {
            if (isagDocumentDTOSelected != null) {
                isagDocumentDTOList.remove(isagDocumentDTOSelected);
                if (isagDocumentDTOSelected.getId() != null) {
                    getIsagDocumentDTODeletedList().add(isagDocumentDTOSelected);
                }
            }
        } catch (Exception e) {
            saveError(e, "RscTenderFormMB", "deletedeDocumentDetail");
        }
    }

    public void addDetail() {
        try {

            if (tenderDTO.getRscTenderDetailWorkUnitDTOList() == null) {
                tenderDTO.setRscTenderDetailWorkUnitDTOList(new ArrayList<>());
            }
            RscTenderDetailWorkUnitDTO detail = new RscTenderDetailWorkUnitDTO();
            setIndex(getIndex() + 1);
            detail.setIndex(getIndex());
            tenderDTO.getRscTenderDetailWorkUnitDTOList().add(detail);
        } catch (Exception e) {
            saveError(e, "RscTenderFormMB", "addDetail");
        }
    }

    public void deleteDetail() {
        try {
            if (rscTenderDetailWorkUnitDTOSelected != null) {
                tenderDTO.getRscTenderDetailWorkUnitDTOList().remove(rscTenderDetailWorkUnitDTOSelected);
                if (rscTenderDetailWorkUnitDTOSelected.getId() != null) {
                    tenderDTO.getRscTenderDetailWorkUnitDTODeletedList().add(rscTenderDetailWorkUnitDTOSelected);
                }
                updateWorkUnitSummary();
            }
        } catch (Exception e) {
            saveError(e, "RscTenderFormMB", "deleteDetail");
        }
    }

    @Override
    public String getScreenName() {
        return "costcenter";
    }

    /**
     * @return the tenderDTO
     */
    public RscTenderDTO getTenderDTO() {
        if (tenderDTO == null) {
            tenderDTO = new RscTenderDTO();
        }
        return tenderDTO;
    }

    /**
     * @param tenderDTO the tenderDTO to set
     */
    public void setTenderDTO(RscTenderDTO tenderDTO) {
        this.tenderDTO = tenderDTO;
    }

    /**
     * @return the rscTenderDetailWorkUnitDTOSelected
     */
    public RscTenderDetailWorkUnitDTO getRscTenderDetailWorkUnitDTOSelected() {
        return rscTenderDetailWorkUnitDTOSelected;
    }

    /**
     * @param rscTenderDetailWorkUnitDTOSelected the
     * rscTenderDetailWorkUnitDTOSelected to set
     */
    public void setRscTenderDetailWorkUnitDTOSelected(RscTenderDetailWorkUnitDTO rscTenderDetailWorkUnitDTOSelected) {
        this.rscTenderDetailWorkUnitDTOSelected = rscTenderDetailWorkUnitDTOSelected;
    }

    /**
     * @return the test
     */
    public InvOrganizationSiteDTO getTest() {
        return test;
    }

    /**
     * @param test the test to set
     */
    public void setTest(InvOrganizationSiteDTO test) {
        this.test = test;
    }

    /**
     * @return the rscTenderCommon
     */
    public RscTenderCommon getRscTenderCommon() {
        if (rscTenderCommon == null) {
            rscTenderCommon = new RscTenderCommon(rscTenderService, getUserData());
        }
        return rscTenderCommon;
    }

    /**
     * @param rscTenderCommon the rscTenderCommon to set
     */
    public void setRscTenderCommon(RscTenderCommon rscTenderCommon) {
        this.rscTenderCommon = rscTenderCommon;
    }

    /**
     * @return the updateSelectedValue
     */
    public boolean isUpdateSelectedValue() {
        return updateSelectedValue;
    }

    /**
     * @param updateSelectedValue the updateSelectedValue to set
     */
    public void setUpdateSelectedValue(boolean updateSelectedValue) {
        this.updateSelectedValue = updateSelectedValue;
    }

    /**
     * @return the rscTenderRscAccrualDeductionDTOSelected
     */
    public RscTenderRscAccrualDeductionDTO getRscTenderRscAccrualDeductionDTOSelected() {
        return rscTenderRscAccrualDeductionDTOSelected;
    }

    /**
     * @param rscTenderRscAccrualDeductionDTOSelected the
     * rscTenderRscAccrualDeductionDTOSelected to set
     */
    public void setRscTenderRscAccrualDeductionDTOSelected(RscTenderRscAccrualDeductionDTO rscTenderRscAccrualDeductionDTOSelected) {
        this.rscTenderRscAccrualDeductionDTOSelected = rscTenderRscAccrualDeductionDTOSelected;
    }

    /**
     * @return the rscTenderRscAccrualDeductionDTODeductionSelected
     */
    public RscTenderRscAccrualDeductionDTO getRscTenderRscAccrualDeductionDTODeductionSelected() {
        return rscTenderRscAccrualDeductionDTODeductionSelected;
    }

    /**
     * @param rscTenderRscAccrualDeductionDTODeductionSelected the
     * rscTenderRscAccrualDeductionDTODeductionSelected to set
     */
    public void setRscTenderRscAccrualDeductionDTODeductionSelected(RscTenderRscAccrualDeductionDTO rscTenderRscAccrualDeductionDTODeductionSelected) {
        this.rscTenderRscAccrualDeductionDTODeductionSelected = rscTenderRscAccrualDeductionDTODeductionSelected;
    }

    /**
     * @return the isagDocumentDTOList
     */
    public List<IsagDocumentDTO> getIsagDocumentDTOList() {
        if (isagDocumentDTOList == null || isagDocumentDTOList.isEmpty()) {
            isagDocumentDTOList = new ArrayList<>();
        }
        return isagDocumentDTOList;
    }

    /**
     * @param isagDocumentDTOList the isagDocumentDTOList to set
     */
    public void setIsagDocumentDTOList(List<IsagDocumentDTO> isagDocumentDTOList) {
        this.isagDocumentDTOList = isagDocumentDTOList;
    }

    /**
     * @return the isagDocumentDTOSelected
     */
    public IsagDocumentDTO getIsagDocumentDTOSelected() {
        return isagDocumentDTOSelected;
    }

    /**
     * @param isagDocumentDTOSelected the isagDocumentDTOSelected to set
     */
    public void setIsagDocumentDTOSelected(IsagDocumentDTO isagDocumentDTOSelected) {
        this.isagDocumentDTOSelected = isagDocumentDTOSelected;
    }

    /**
     * @return the isagDocumentDTODeletedList
     */
    public List<IsagDocumentDTO> getIsagDocumentDTODeletedList() {
        if (isagDocumentDTODeletedList == null || isagDocumentDTODeletedList.isEmpty()) {
            isagDocumentDTODeletedList = new ArrayList<>();
        }
        return isagDocumentDTODeletedList;
    }

    /**
     * @param isagDocumentDTODeletedList the isagDocumentDTODeletedList to set
     */
    public void setIsagDocumentDTODeletedList(List<IsagDocumentDTO> isagDocumentDTODeletedList) {
        this.isagDocumentDTODeletedList = isagDocumentDTODeletedList;
    }

    /**
     * @return the file
     */
    public UploadedFile getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(UploadedFile file) {
        this.file = file;
    }

    /**
     * @return the files
     */
    public List<UploadedFile> getFiles() {
        if (files == null || files.isEmpty()) {
            files = new ArrayList<>();
        }
        return files;
    }

    /**
     * @param files the files to set
     */
    public void setFiles(List<UploadedFile> files) {
        this.files = files;
    }

}
