/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rscExtract;

import com.isag.businessservice.InstProjectGuideService;
import com.isag.businessservice.RscContractRegisterService;
import com.isag.businessservice.RscExtractRegisterDetailService;
import com.isag.businessservice.RscExtractRegisterService;
import com.isag.businessservice.RscRestrictionQuantitiesDetailService;
import com.isag.businessservice.RscRestrictionQuantitiesService;
import com.isag.businessservice.reports.searchBean.RscContractExtractionWorkunitSearchBean;
import com.isag.converter.InstProjectGuideDTOConverter;
import com.isag.converter.InvOrganizationSiteConverter;
import com.isag.converter.SymbolConverter;
import com.isag.dto.InstProjectGuideDTO;
import com.isag.dto.RscContractRegisterDTO;
import com.isag.dto.RscExtractAccuralDeductionDTO;
import com.isag.dto.RscExtractRegisterDTO;
import com.isag.dto.RscExtractRegisterDetailDTO;
import com.isag.dto.RscRestrictionQuantitiesDTO;
import com.isag.isag.AutoComplete;
import com.isag.isag.RSCFormBasicData;
import com.isag.isag.UserData;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author hq003
 */
@Named(value = "rscExtractFormMB")
@ViewScoped
public class rscExtractFormMB extends RSCFormBasicData {

    private List<RscExtractRegisterDetailDTO> rscExtractRegisterDetailWorkUnitDeletedList;
    private List<RscExtractAccuralDeductionDTO> rscExtractAccrualDeletedList;
    private List<RscExtractAccuralDeductionDTO> rscExtractDeductionDeletedList;
    private RscExtractRegisterDTO extractRegisterDTO;
    private RscContractRegisterDTO contractRegisterDTO;
    private RscRestrictionQuantitiesDTO quantitiesDTO;
    private RscExtractRegisterDetailDTO RscExtractRegisterDetailDTOSelected;
    private RscExtractAccuralDeductionDTO RscExtractAccrualDTOSelected;
    private RscExtractAccuralDeductionDTO RscExtractDeductionDTOSelected;
    private SymbolConverter symbolConverter;
    private Integer extractRegisterId;
    private InvOrganizationSiteConverter invOrganizationSiteConverter;
    Integer serialDetail = 0;
    private BigDecimal totalDetailNet = BigDecimal.ZERO;
    private BigDecimal totalAccrualNet = BigDecimal.ZERO;
    private BigDecimal totalDeductionNet = BigDecimal.ZERO;
    private BigDecimal netcost = BigDecimal.ZERO;
    private List<RscRestrictionQuantitiesDTO> rscRestrictionQuantitiesDTOList;
    private RscExtractCommon rscExtractCommon;
    private Boolean staticText = false;
    private Integer type;
    private InstProjectGuideDTOConverter instProjectGuideDTOConverter;
    private List<InstProjectGuideDTO> instProjectGuideDtoList;
    private String uri;
    private BigDecimal totalDetailNet1 = BigDecimal.ZERO;
    private Boolean idLoaded;
    private RscContractExtractionWorkunitSearchBean extractionWorkunitSearchBean;

    @EJB
    private RscExtractRegisterService rscExtractRegisterService;
    @EJB
    private RscExtractRegisterDetailService rscExtractRegisterDetailService;
    @EJB
    private RscContractRegisterService contractRegisterService;
    @EJB
    private RscRestrictionQuantitiesService restrictionQuantitiesService;
    @EJB
    private RscRestrictionQuantitiesDetailService rscrestrictionQuantitiesDetailService;
    @EJB
    private InstProjectGuideService instProjectGuideService;

    @PostConstruct
    @Override
    public void init() {
        load();
    }

    @Override
    public void load() {
        uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        setScreenMode((String) context.getSessionMap().get("ScreenMode"));
        getUserData().getUser().setBranchId(getUserData().getUserBranch());
        fillLists();
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("AddrscExtractRegisterDTOSelected")) {
            reset();
        } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditrscExtractRegisterDTOSelected")) {
            // get tenderiD From Session
            extractRegisterId = (Integer) context.getSessionMap().get("rscExtractRegisterDTOSelected");
            //    extractRegisterDTO = rscExtractRegisterService.findRscExtractRegister(extractRegisterId);
            extractRegisterDTO = getRscExtractCommon().returnObject(extractRegisterId, uri);
//            extractRegisterDTO = rscExtractRegisterService.updateSummary(extractRegisterDTO, false);
            extractRegisterDTO.setUri(uri);
            fillList();

        }
    }

    public void fillLists() {
        getRscWorkUnitsGuideDTOList();
        getProfessionalBusinessConsultantList();
        getProjectManagerList();
        getGeneralConsultantList();
        getRscAccrualDTOList();
        getRscDeductionDTOList();
        if (uri.contains("rscExtractSummary") || uri.contains("rscExtractInv")) {
            getInstProjectGuideDtoList();
            getContractorList();
            getCustomerList();
        }
    }

    public void print() {
        if (extractRegisterDTO != null && extractRegisterDTO.getId() != null
                && extractRegisterDTO.getRscExtractRegisterDetailDTOList() != null
                && !extractRegisterDTO.getRscExtractRegisterDetailDTOList().isEmpty()) {
            getRscExtractCommon().print(extractRegisterDTO, uri);
        }
    }

    @Override
    public String save() {
        try {
            if (!validate()) {
                updatehi();
                extractRegisterDTO = rscExtractRegisterService.addRscExtractRegister(extractRegisterDTO, getRscExtractRegisterDetailWorkUnitDeletedList(), getRscExtractAccrualDeletedList(), getRscExtractDeductionDeletedList(), getUserData().getUser());
                if (extractRegisterDTO.getMsg() != null && !extractRegisterDTO.getMsg().isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), extractRegisterDTO.getMsg()));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), "تم الحفظ بنجاح"));
                }
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "حدث خطأ!"));
            e.printStackTrace();
        }
        CloseDlg("dlg3");
        return "";

    }

    public Boolean validate() {
        Boolean validate = Boolean.FALSE;
        if (uri.contains("rscExtractSummary")) {
            if (extractRegisterDTO.getSiteId() == null || extractRegisterDTO.getSiteId().getId() <= 0) {
                validate = Boolean.TRUE;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "من فضلك اختار مقاول باطن"));
            }
            if (extractRegisterDTO.getInstProjectGuideDTO() == null || extractRegisterDTO.getInstProjectGuideDTO().getId() <= 0) {
                validate = Boolean.TRUE;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "من فضلك اختار مشروع "));

            }
        }
        return validate;
    }

    public void reset() {
        RscExtractRegisterDetailDTOSelected = null;
        RscExtractAccrualDTOSelected = null;
        RscExtractDeductionDTOSelected = null;
        extractRegisterDTO = new RscExtractRegisterDTO();
        extractRegisterDTO.setDate(new Date());
        extractRegisterDTO.setRscExtractRegisterDetailDTOList(new ArrayList<>());
        extractRegisterDTO.setRscExtractAccuralList(new ArrayList<>());
        extractRegisterDTO.setRscExtractDeductionList(new ArrayList<>());
        extractRegisterDTO.setUri(uri);

    }

    public String exit() {
        if (uri.contains("rscExtractSummary")) {
            exit("../rscExtractSummary/rscExtractSummaryList.xhtml");
        } else if (uri.contains("rscExtractInv")) {
            exit("../rscExtractInv/rscExtractList.xhtml");
        } else {
            exit("../rscExtract/rscExtractList.xhtml");
        }

        return "";
    }

    public void addChildDetail() {

        if (getExtractRegisterDTO().getRscExtractRegisterDetailDTOList() == null) {
            getExtractRegisterDTO().setRscExtractRegisterDetailDTOList(new ArrayList<>());
        }
        RscExtractRegisterDetailDTO detailWorkUnitDTO = new RscExtractRegisterDetailDTO();
        setIndex(getIndex() + 1);
        detailWorkUnitDTO.setIndex(getIndex());
        getExtractRegisterDTO().getRscExtractRegisterDetailDTOList().add(detailWorkUnitDTO);
    }
//---------------------------------------------------------------------------------------

    public void addChildAccrual() {

        if (extractRegisterDTO.getRscExtractAccuralList() == null) {
            extractRegisterDTO.setRscExtractAccuralList(new ArrayList<>());
        }
        RscExtractAccuralDeductionDTO accuralDeductionDTO = new RscExtractAccuralDeductionDTO();
        setIndex(getIndex() + 1);
        accuralDeductionDTO.setIndex(getIndex());
        extractRegisterDTO.getRscExtractAccuralList().add(accuralDeductionDTO);
        RscExtractAccrualDTOSelected = accuralDeductionDTO;
        calculateAccrualPercentage();
        calculateAccrualValue();
        allUpdateSummition();

    }

    public void updateAccrualSummition() {
        totalAccrualNet = BigDecimal.ZERO;
        for (RscExtractAccuralDeductionDTO accuralDetail : extractRegisterDTO.getRscExtractAccuralList()) {
            totalAccrualNet = totalAccrualNet.add(accuralDetail.getRscAccuralDeductionValue() != null ? accuralDetail.getRscAccuralDeductionValue() : BigDecimal.ZERO);
            if (accuralDetail.getRscAccrualDeductionDTO() == null) {
                accuralDetail.setRscAccrualDeductionDTO(accuralDetail.getRscAccrualDeductionDTObak());
            } else {
                accuralDetail.setRscAccrualDeductionDTObak(accuralDetail.getRscAccrualDeductionDTO());
            }
        }
        extractRegisterDTO.setTotalAccrualNetvalue(totalAccrualNet);
    }

    public void calculateAccrualPercentage() {

        if (getRscExtractAccrualDTOSelected().getRscAccuralDeductionValue() != null && getRscExtractAccrualDTOSelected().getRscAccuralDeductionValue().compareTo(BigDecimal.ZERO) == 1) {

            getRscExtractAccrualDTOSelected().setRscAccuralDeductionPercentage(((getRscExtractAccrualDTOSelected().getRscAccuralDeductionValue() != null ? getRscExtractAccrualDTOSelected().getRscAccuralDeductionValue() : BigDecimal.ZERO)
                    .divide((extractRegisterDTO.getTotalDetailNet() != null && extractRegisterDTO.getTotalDetailNet().compareTo(BigDecimal.ZERO) == 1 ? extractRegisterDTO.getTotalDetailNet() : BigDecimal.ONE), 3, RoundingMode.UP))
                    .multiply(new BigDecimal(100)));

        }
        allUpdateSummition();
    }

    public void calculateAccrualValue() {

        if (getRscExtractAccrualDTOSelected().getRscAccuralDeductionPercentage() != null && getRscExtractAccrualDTOSelected().getRscAccuralDeductionPercentage().compareTo(BigDecimal.ZERO) == 1) {

            getRscExtractAccrualDTOSelected().setRscAccuralDeductionValue(
                    ((extractRegisterDTO.getTotalDetailNet() != null ? extractRegisterDTO.getTotalDetailNet() : BigDecimal.ZERO)
                            .multiply(getRscExtractAccrualDTOSelected().getRscAccuralDeductionPercentage() != null ? getRscExtractAccrualDTOSelected().getRscAccuralDeductionPercentage() : BigDecimal.ZERO)).divide(new BigDecimal(100), 3, RoundingMode.UP).setScale(3, BigDecimal.ROUND_HALF_UP));
        }
        allUpdateSummition();

    }

    public void updatehi() {
        for (RscExtractRegisterDetailDTO CalculateDetail : extractRegisterDTO.getRscExtractRegisterDetailDTOList()) {
            if (CalculateDetail.getGuideDTO() == null) {
                CalculateDetail.setGuideDTO(CalculateDetail.getGuideDTOBak());
            }
        }
        for (RscExtractAccuralDeductionDTO accuralDetail : extractRegisterDTO.getRscExtractAccuralList()) {
            if (accuralDetail.getRscAccrualDeductionDTO() == null) {
                accuralDetail.setRscAccrualDeductionDTO(accuralDetail.getRscAccrualDeductionDTObak());
            }
        }
        for (RscExtractAccuralDeductionDTO deductionDetail : extractRegisterDTO.getRscExtractDeductionList()) {
            if (deductionDetail.getRscAccrualDeductionDTO() == null) {
                deductionDetail.setRscAccrualDeductionDTO(deductionDetail.getRscAccrualDeductionDTObak());
            }
        }

    }
//-----------------------------------------------------------------------------------------------

    public void addChildDeduction() {

        if (extractRegisterDTO.getRscExtractDeductionList() == null) {
            extractRegisterDTO.setRscExtractDeductionList(new ArrayList<>());
        }
        RscExtractAccuralDeductionDTO extractDeductionDTO = new RscExtractAccuralDeductionDTO();
        setIndex(getIndex() + 1);
        extractDeductionDTO.setIndex(getIndex());

        extractRegisterDTO.getRscExtractDeductionList().add(extractDeductionDTO);
        RscExtractDeductionDTOSelected = extractDeductionDTO;
        calculateDeductionPercentage();
        calculateDeductionValue();
        allUpdateSummition();

    }

    public void updateDeductionSummition() {
        totalDeductionNet = BigDecimal.ZERO;
        for (RscExtractAccuralDeductionDTO deductionDetail : extractRegisterDTO.getRscExtractDeductionList()) {
            totalDeductionNet = totalDeductionNet.add(deductionDetail.getRscAccuralDeductionValue() != null ? deductionDetail.getRscAccuralDeductionValue() : BigDecimal.ZERO);
            if (deductionDetail.getRscAccrualDeductionDTO() == null) {
                deductionDetail.setRscAccrualDeductionDTO(deductionDetail.getRscAccrualDeductionDTObak());
            } else {
                deductionDetail.setRscAccrualDeductionDTObak(deductionDetail.getRscAccrualDeductionDTO());
            }
        }
        extractRegisterDTO.setTotalDeductionNetvalue(totalDeductionNet);
    }

    public void calculateDeductionPercentage() {
        if (RscExtractDeductionDTOSelected.getRscAccuralDeductionValue() != null && RscExtractDeductionDTOSelected.getRscAccuralDeductionValue().compareTo(BigDecimal.ZERO) == 1) {
            RscExtractDeductionDTOSelected.setRscAccuralDeductionPercentage(((RscExtractDeductionDTOSelected.getRscAccuralDeductionValue() != null ? RscExtractDeductionDTOSelected.getRscAccuralDeductionValue() : BigDecimal.ZERO)
                    .divide((extractRegisterDTO.getTotalDetailNet() != null && extractRegisterDTO.getTotalDetailNet().compareTo(BigDecimal.ZERO) == 1 ? extractRegisterDTO.getTotalDetailNet() : BigDecimal.ONE), 3, RoundingMode.UP))
                    .multiply(new BigDecimal(100)));
        }
        allUpdateSummition();
    }

    public void allUpdateSummition() {
        updateDetailSummition();
        updateAccrualSummition();
        updateDeductionSummition();
        extractRegisterDTO.setTotalFinal((extractRegisterDTO.getTotalAccrualNetvalue() != null ? extractRegisterDTO.getTotalAccrualNetvalue() : BigDecimal.ZERO).add(extractRegisterDTO.getTotalDetailNet() != null ? extractRegisterDTO.getTotalDetailNet() : BigDecimal.ZERO).subtract(extractRegisterDTO.getTotalDeductionNetvalue() != null ? extractRegisterDTO.getTotalDeductionNetvalue() : BigDecimal.ZERO));

    }

    public void calculateDeductionValue() {

        if (RscExtractDeductionDTOSelected.getRscAccuralDeductionPercentage() != null && RscExtractDeductionDTOSelected.getRscAccuralDeductionPercentage().compareTo(BigDecimal.ZERO) == 1) {

            RscExtractDeductionDTOSelected.setRscAccuralDeductionValue((extractRegisterDTO.getTotalDetailNet() != null ? extractRegisterDTO.getTotalDetailNet() : BigDecimal.ZERO)
                    .multiply((RscExtractDeductionDTOSelected.getRscAccuralDeductionPercentage() != null ? RscExtractDeductionDTOSelected.getRscAccuralDeductionPercentage() : BigDecimal.ZERO)).divide(new BigDecimal(100), 3, RoundingMode.UP).setScale(3, BigDecimal.ROUND_HALF_UP));

        }
        allUpdateSummition();

    }
    //----------------------------------------------------------------------------

    public void validateContract() {
        if (extractRegisterDTO != null) {
            if (extractRegisterDTO.getContractId() == null) {
                errorMessage("يجب ادخال بيانات العقد");
            }
        }

    }

    public void validateUnitGuide() {
        if (RscExtractRegisterDetailDTOSelected != null) {
            if (RscExtractRegisterDetailDTOSelected.getGuideDTO() != null) {
                RscExtractRegisterDetailDTOSelected.setGuideDTOBak(RscExtractRegisterDetailDTOSelected.getGuideDTO());
            }
        }
        allUpdateSummition();

    }

    public void validateAccrual() {
        if (RscExtractAccrualDTOSelected != null) {
            if (RscExtractAccrualDTOSelected.getRscAccrualDeductionDTO() != null) {
                RscExtractAccrualDTOSelected.setRscAccrualDeductionDTObak(RscExtractAccrualDTOSelected.getRscAccrualDeductionDTO());
            }
        }
        allUpdateSummition();

    }

    public void validateAccrualDeduction() {
        if (RscExtractDeductionDTOSelected != null) {
            if (RscExtractDeductionDTOSelected.getRscAccrualDeductionDTO() != null) {
                RscExtractDeductionDTOSelected.setRscAccrualDeductionDTObak(RscExtractDeductionDTOSelected.getRscAccrualDeductionDTO());
            }
        }
        allUpdateSummition();

    }

    public void searchQuantity() {
        rscRestrictionQuantitiesDTOList = restrictionQuantitiesService.getBranchRscRestrictionQuantitiesActiveByBranch(getUserData().getUserBranch().getId(),getExtractionWorkunitSearchBean());
    }

    //--------------------- Calculate Methods ---------------------------------
    public void loadDetailValues(RscExtractRegisterDetailDTO detailDTO) {

        detailDTO.setOldQty(rscExtractRegisterDetailService.getOldQty(extractRegisterDTO.getRestrictionId().getContractId().getId(), detailDTO.getGuideDTO().getId()));
        if (detailDTO.getContractQty().compareTo(detailDTO.getOldQty()) == 0) {
            setStaticText((Boolean) true);
            detailDTO.setCurrentQty(new BigDecimal(0));
            calculateExtractDetailValues(detailDTO);
        }
        detailDTO.setOldRamp(rscExtractRegisterDetailService.getRampValue(extractRegisterDTO.getRestrictionId().getContractId().getId(), detailDTO.getGuideDTO().getId()));
        if (detailDTO.getOldRamp().compareTo(BigDecimal.ZERO) == 0 || detailDTO.getOldRamp() == null) {
            detailDTO.setReturnRamp(new BigDecimal(0));
        }
    }

    BigDecimal valueAfterDiscount = BigDecimal.ZERO;

    public void calculateExtractValues() {
        if (uri.contains("rscExtractSummary")) {
            extractRegisterDTO = rscExtractRegisterService.updateExtractSummary(extractRegisterDTO, false);
        }
        /*else {
            extractRegisterDTO = rscExtractRegisterService.updateSummary(extractRegisterDTO, false);
        }*/
    }

    public void calculateExtractDetailValues(RscExtractRegisterDetailDTO detailDTO) {
        if (uri.contains("rscExtractSummary")) {
            extractRegisterDTO = rscExtractRegisterService.updateExtractSummary(extractRegisterDTO, false);
        } else {
            extractRegisterDTO = rscExtractRegisterService.updateSummary(extractRegisterDTO, false);
        }
    }

    public void calculateExtractDetailPercentage(RscExtractRegisterDetailDTO detailDTO) {
        if (uri.contains("rscExtractSummary")) {
            extractRegisterDTO = rscExtractRegisterService.updateExtractSummary(extractRegisterDTO, true);
        } else {
            extractRegisterDTO = rscExtractRegisterService.updateSummary(extractRegisterDTO, true);
        }
    }

    public void updateDetailSummition() {
        totalDetailNet = BigDecimal.ZERO;

        for (RscExtractRegisterDetailDTO CalculateDetail : extractRegisterDTO.getRscExtractRegisterDetailDTOList()) {
            if (extractRegisterDTO.getRscExtractDeductionList() != null && extractRegisterDTO.getRscExtractDeductionList().isEmpty()
                    && extractRegisterDTO.getRscExtractAccuralList() != null && extractRegisterDTO.getRscExtractAccuralList().isEmpty()) {
                totalDetailNet = (totalDetailNet.add(netcost != null ? netcost : BigDecimal.ZERO));

            }

            if (extractRegisterDTO.getRscExtractAccuralList() != null && !extractRegisterDTO.getRscExtractAccuralList().isEmpty()
                    || extractRegisterDTO.getRscExtractDeductionList() != null && !extractRegisterDTO.getRscExtractDeductionList().isEmpty()) {
                totalDetailNet = ((totalDetailNet.add(netcost != null ? netcost : BigDecimal.ZERO)).add(totalAccrualNet != null ? totalAccrualNet : BigDecimal.ZERO).subtract(totalDeductionNet != null ? totalDeductionNet : BigDecimal.ZERO));
            }

            if (CalculateDetail.getGuideDTO() == null) {
                CalculateDetail.setGuideDTO(CalculateDetail.getGuideDTOBak());
            }
        }
        totalDetailNet1 = totalDetailNet;
    }

    public void hsdfjksd() {

    }

    public void calculateRampPercentage() {
        for (RscExtractRegisterDetailDTO detailDTO : extractRegisterDTO.getRscExtractRegisterDetailDTOList()) {
            if (detailDTO.getRampValue() != null && detailDTO.getRampValue().compareTo(BigDecimal.ZERO) == 1) {

                detailDTO.setRampPercentage(((detailDTO.getRampValue() != null ? detailDTO.getRampValue() : BigDecimal.ZERO)
                        .divide(detailDTO.getCurrentValue().compareTo(BigDecimal.ZERO) == 0 || detailDTO.getCurrentValue() == null ? BigDecimal.ONE : detailDTO.getCurrentValue(), 4, BigDecimal.ROUND_CEILING))
                        .multiply(new BigDecimal(100)));

            }
            calculateTotalRamp(detailDTO);
            calculateExtractDetailValues(detailDTO);
        }
    }

    public void calculateRampValue() {
        for (RscExtractRegisterDetailDTO detailDTO : extractRegisterDTO.getRscExtractRegisterDetailDTOList()) {
            if (detailDTO.getRampPercentage() != null && detailDTO.getRampPercentage().compareTo(BigDecimal.ZERO) == 1) {

                detailDTO.setRampValue((detailDTO.getCurrentValue() != null ? detailDTO.getCurrentValue() : BigDecimal.ZERO)
                        .divide(detailDTO.getRampPercentage() != null ? detailDTO.getRampPercentage() : BigDecimal.ZERO, BigDecimal.ROUND_HALF_UP));
            }
            calculateTotalRamp(detailDTO);
            calculateExtractDetailValues(detailDTO);
        }

    }

    public void calculateOldRampPercentage() {
        for (RscExtractRegisterDetailDTO detailDTO : extractRegisterDTO.getRscExtractRegisterDetailDTOList()) {
            if (detailDTO.getReturnRamp() != null && detailDTO.getReturnRamp().compareTo(BigDecimal.ZERO) == 1) {

                detailDTO.setReturnRampPercentage(((detailDTO.getReturnRamp() != null ? detailDTO.getReturnRamp() : BigDecimal.ZERO)
                        .divide(detailDTO.getOldRamp().compareTo(BigDecimal.ZERO) == 0 || detailDTO.getOldRamp() == null ? BigDecimal.ONE : detailDTO.getOldRamp(), 4, BigDecimal.ROUND_CEILING))
                        .multiply(new BigDecimal(100)));

            }
            calculateTotalRamp(detailDTO);
            calculateExtractDetailValues(detailDTO);
        }
    }

    public void calculateIOldRampValue() {
        for (RscExtractRegisterDetailDTO detailDTO : extractRegisterDTO.getRscExtractRegisterDetailDTOList()) {
            if (detailDTO.getReturnRampPercentage() != null && detailDTO.getReturnRampPercentage().compareTo(BigDecimal.ZERO) == 1) {
                detailDTO.setReturnRamp((detailDTO.getOldRamp() != null ? detailDTO.getOldRamp() : BigDecimal.ZERO)
                        .divide(detailDTO.getReturnRampPercentage() != null ? detailDTO.getReturnRampPercentage() : BigDecimal.ZERO, BigDecimal.ROUND_HALF_UP));
            }
            calculateTotalRamp(detailDTO);
            calculateExtractDetailValues(detailDTO);
        }

    }

    public void calculateTotalRamp(RscExtractRegisterDetailDTO detailDTO) {
        detailDTO.setNetTotalReturnRamp((detailDTO.getOldRamp() != null ? detailDTO.getOldRamp() : BigDecimal.ZERO)
                .subtract(detailDTO.getReturnRamp() != null ? detailDTO.getReturnRamp() : BigDecimal.ZERO)
                .add(detailDTO.getRampValue() != null ? detailDTO.getRampValue() : BigDecimal.ZERO));
        calculateExtractDetailValues(detailDTO);
    }

    public void calculateDiscountValue() {
        for (RscExtractRegisterDetailDTO detailDTO : extractRegisterDTO.getRscExtractRegisterDetailDTOList()) {
            if (detailDTO.getDiscountPercentage() != null && detailDTO.getDiscountPercentage().compareTo(BigDecimal.ZERO) == 1) {
                detailDTO.setDiscountValue((detailDTO.getCurrentValue() != null ? detailDTO.getCurrentValue() : BigDecimal.ZERO)
                        .divide(detailDTO.getDiscountPercentage() != null ? detailDTO.getDiscountPercentage() : BigDecimal.ZERO, BigDecimal.ROUND_HALF_UP));
            }

            calculateExtractDetailValues(detailDTO);
        }

    }

    public void calculateDiscountPercentage() {
        for (RscExtractRegisterDetailDTO detailDTO : extractRegisterDTO.getRscExtractRegisterDetailDTOList()) {
            if (detailDTO.getDiscountValue() != null && detailDTO.getDiscountValue().compareTo(BigDecimal.ZERO) == 1) {

                detailDTO.setDiscountPercentage(((detailDTO.getDiscountValue() != null ? detailDTO.getDiscountValue() : BigDecimal.ZERO)
                        .divide(detailDTO.getCurrentValue().compareTo(BigDecimal.ZERO) == 0 || detailDTO.getCurrentValue() == null ? BigDecimal.ONE : detailDTO.getCurrentValue(), 4, BigDecimal.ROUND_CEILING))
                        .multiply(new BigDecimal(100)));

            }

            calculateExtractDetailValues(detailDTO);
        }

    }

    //-------------------------------------------------------------------------------------
    public void deleteRscExtractDetail() {

        if (RscExtractRegisterDetailDTOSelected != null) {
            extractRegisterDTO.getRscExtractRegisterDetailDTOList().remove(RscExtractRegisterDetailDTOSelected);
            if (RscExtractRegisterDetailDTOSelected.getId() != null) {
                getRscExtractRegisterDetailWorkUnitDeletedList().add(RscExtractRegisterDetailDTOSelected);
            }
        }
        updateDetailSummition();
    }

    //-------------------------------------------------------------------------------------
    public void deleteRscAccrual() {

        if (RscExtractAccrualDTOSelected != null) {
            extractRegisterDTO.getRscExtractAccuralList().remove(RscExtractAccrualDTOSelected);
            if (RscExtractAccrualDTOSelected.getId() != null) {
                rscExtractAccrualDeletedList.add(RscExtractAccrualDTOSelected);
            }
        }
        allUpdateSummition();
    }

    //-------------------------------------------------------------------------------------
    public void deleteRscDeduction() {

        if (RscExtractDeductionDTOSelected != null) {
            extractRegisterDTO.getRscExtractDeductionList().remove(RscExtractDeductionDTOSelected);
            if (RscExtractDeductionDTOSelected.getId() != null) {
                rscExtractDeductionDeletedList.add(RscExtractDeductionDTOSelected);
            }
        }
        allUpdateSummition();
    }
    //--------------------------------- Load Contract-------------------------------------------------

    public void loadcontract() {
        if (extractRegisterDTO.getRestrictionId() != null && extractRegisterDTO.getRestrictionId().getId() != null) {
            extractRegisterDTO.setBranchId(getUserlogin().getBranchId().getId());
            extractRegisterDTO = rscExtractRegisterService.loadRestriction(extractRegisterDTO);
        }
        CloseDlg("dlg2");
    }
    //--------------------------------- Load Contract List-------------------------------------------------

    public void loadcontractList() {
        if (extractRegisterDTO.getRestrictionIdList() != null && !extractRegisterDTO.getRestrictionIdList().isEmpty()) {
            extractRegisterDTO = rscExtractRegisterService.loadRestrictionList(extractRegisterDTO.getRestrictionIdList());
            setIdLoaded(Boolean.TRUE);
        }
        CloseDlg("dlg4");
    }

//-------------------------------------------------------------------------------------------- 
    @Override
    public String getScreenName() {
        return "costcenter";
    }

    public List<InstProjectGuideDTO> completeInstProject(String query) {
        return AutoComplete.completeInstProjectDTO(query, this.instProjectGuideDtoList, instProjectGuideDTOConverter);
    }

    /**
     * @return the tenderDTO
     */
    public RscExtractRegisterDTO getExtractRegisterDTO() {
        if (extractRegisterDTO == null) {
            extractRegisterDTO = new RscExtractRegisterDTO();
        }
        return extractRegisterDTO;
    }

    /**
     * @param tenderDTO the tenderDTO to set
     */
    public void setExtractRegisterDTO(RscExtractRegisterDTO ExtractRegisterDTO) {
        this.extractRegisterDTO = ExtractRegisterDTO;
    }

    /**
     * @return the rscTenderDetailWorkUnitDTOSelected
     */
    public RscExtractRegisterDetailDTO getRscExtractRegisterDetailDTOSelected() {
        if (RscExtractRegisterDetailDTOSelected == null) {
            RscExtractRegisterDetailDTOSelected = new RscExtractRegisterDetailDTO();
        }
        return RscExtractRegisterDetailDTOSelected;
    }

    /**
     * @param rscTenderDetailWorkUnitDTOSelected the
     * rscTenderDetailWorkUnitDTOSelected to set
     */
    public void setRscExtractRegisterDetailDTOSelected(RscExtractRegisterDetailDTO RscExtractRegisterDetailDTOSelected) {
        this.RscExtractRegisterDetailDTOSelected = RscExtractRegisterDetailDTOSelected;
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

    /**
     * @return the ExtractRegisterId
     */
    public Integer getExtractRegisterId() {
        return extractRegisterId;
    }

    /**
     * @param ExtractRegisterId the ExtractRegisterId to set
     */
    public void setExtractRegisterId(Integer ExtractRegisterId) {
        this.extractRegisterId = ExtractRegisterId;
    }

    /**
     * @return the rscExtractRegisterDetailWorkUnitDeletedList
     */
    public List<RscExtractRegisterDetailDTO> getRscExtractRegisterDetailWorkUnitDeletedList() {
        if (rscExtractRegisterDetailWorkUnitDeletedList == null) {
            rscExtractRegisterDetailWorkUnitDeletedList = new ArrayList<>();
        }
        return rscExtractRegisterDetailWorkUnitDeletedList;
    }

    /**
     * @param rscExtractRegisterDetailWorkUnitDeletedList the
     * rscExtractRegisterDetailWorkUnitDeletedList to set
     */
    public void setRscExtractRegisterDetailWorkUnitDeletedList(List<RscExtractRegisterDetailDTO> rscExtractRegisterDetailWorkUnitDeletedList) {
        this.rscExtractRegisterDetailWorkUnitDeletedList = rscExtractRegisterDetailWorkUnitDeletedList;
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
     * @return the rscRestrictionQuantitiesDTOList
     */
    public List<RscRestrictionQuantitiesDTO> getRscRestrictionQuantitiesDTOList() {
        if (rscRestrictionQuantitiesDTOList == null || rscRestrictionQuantitiesDTOList.isEmpty()) {
            rscRestrictionQuantitiesDTOList = restrictionQuantitiesService.getBranchRscRestrictionQuantitiesActiveByBranch(getUserData().getUserBranch().getId());
        }

        return rscRestrictionQuantitiesDTOList;
    }

    /**
     * @param rscRestrictionQuantitiesDTOList the
     * rscRestrictionQuantitiesDTOList to set
     */
    public void setRscRestrictionQuantitiesDTOList(List<RscRestrictionQuantitiesDTO> rscRestrictionQuantitiesDTOList) {
        this.rscRestrictionQuantitiesDTOList = rscRestrictionQuantitiesDTOList;
    }

    /**
     * @return the quantitiesDTO
     */
    public RscRestrictionQuantitiesDTO getQuantitiesDTO() {
        if (quantitiesDTO == null) {
            quantitiesDTO = new RscRestrictionQuantitiesDTO();
        }
        return quantitiesDTO;
    }

    /**
     * @param quantitiesDTO the quantitiesDTO to set
     */
    public void setQuantitiesDTO(RscRestrictionQuantitiesDTO quantitiesDTO) {
        this.quantitiesDTO = quantitiesDTO;
    }

    /**
     * @return the rscExtractCommon
     */
    public RscExtractCommon getRscExtractCommon() {
        if (rscExtractCommon == null) {
            rscExtractCommon = new RscExtractCommon(rscExtractRegisterService, getUserData());
        }
        return rscExtractCommon;
    }

    /**
     * @param rscExtractCommon the rscExtractCommon to set
     */
    public void setRscExtractCommon(RscExtractCommon rscExtractCommon) {
        this.rscExtractCommon = rscExtractCommon;
    }

    /**
     * @return the RscExtractAccrualDTOSelected
     */
    public RscExtractAccuralDeductionDTO getRscExtractAccrualDTOSelected() {
        if (RscExtractAccrualDTOSelected == null) {
            RscExtractAccrualDTOSelected = new RscExtractAccuralDeductionDTO();
        }
        return RscExtractAccrualDTOSelected;
    }

    /**
     * @param RscExtractAccrualDTOSelected the RscExtractAccrualDTOSelected to
     * set
     */
    public void setRscExtractAccrualDTOSelected(RscExtractAccuralDeductionDTO RscExtractAccrualDTOSelected) {
        this.RscExtractAccrualDTOSelected = RscExtractAccrualDTOSelected;
    }

    /**
     * @return the RscExtractDeductionDTOSelected
     */
    public RscExtractAccuralDeductionDTO getRscExtractDeductionDTOSelected() {
        if (RscExtractDeductionDTOSelected == null) {
            RscExtractDeductionDTOSelected = new RscExtractAccuralDeductionDTO();
        }
        return RscExtractDeductionDTOSelected;
    }

    /**
     * @param RscExtractDeductionDTOSelected the RscExtractDeductionDTOSelected
     * to set
     */
    public void setRscExtractDeductionDTOSelected(RscExtractAccuralDeductionDTO RscExtractDeductionDTOSelected) {
        this.RscExtractDeductionDTOSelected = RscExtractDeductionDTOSelected;
    }

    /**
     * @return the rscExtractAccrualDeletedList
     */
    public List<RscExtractAccuralDeductionDTO> getRscExtractAccrualDeletedList() {
        if (rscExtractAccrualDeletedList == null) {
            rscExtractAccrualDeletedList = new ArrayList<>();
        }

        return rscExtractAccrualDeletedList;
    }

    /**
     * @param rscExtractAccrualDeletedList the rscExtractAccrualDeletedList to
     * set
     */
    public void setRscExtractAccrualDeletedList(List<RscExtractAccuralDeductionDTO> rscExtractAccrualDeletedList) {
        this.rscExtractAccrualDeletedList = rscExtractAccrualDeletedList;
    }

    /**
     * @return the rscExtractDeductionDeletedList
     */
    public List<RscExtractAccuralDeductionDTO> getRscExtractDeductionDeletedList() {
        if (rscExtractDeductionDeletedList == null) {
            rscExtractDeductionDeletedList = new ArrayList<>();
        }

        return rscExtractDeductionDeletedList;
    }

    /**
     * @param rscExtractDeductionDeletedList the rscExtractDeductionDeletedList
     * to set
     */
    public void setRscExtractDeductionDeletedList(List<RscExtractAccuralDeductionDTO> rscExtractDeductionDeletedList) {
        this.rscExtractDeductionDeletedList = rscExtractDeductionDeletedList;
    }

    /**
     * @return the totalDetailNet
     */
    public BigDecimal getTotalDetailNet() {
        return totalDetailNet;
    }

    /**
     * @param totalDetailNet the totalDetailNet to set
     */
    public void setTotalDetailNet(BigDecimal totalDetailNet) {
        this.totalDetailNet = totalDetailNet;
    }

    /**
     * @return the totalAccrualNet
     */
    public BigDecimal getTotalAccrualNet() {
        return totalAccrualNet;
    }

    /**
     * @param totalAccrualNet the totalAccrualNet to set
     */
    public void setTotalAccrualNet(BigDecimal totalAccrualNet) {
        this.totalAccrualNet = totalAccrualNet;
    }

    /**
     * @return the totalDeductionNet
     */
    public BigDecimal getTotalDeductionNet() {
        return totalDeductionNet;
    }

    /**
     * @param totalDeductionNet the totalDeductionNet to set
     */
    public void setTotalDeductionNet(BigDecimal totalDeductionNet) {
        this.totalDeductionNet = totalDeductionNet;
    }

    /**
     * @return the netcost
     */
    public BigDecimal getNetcost() {
        return netcost;
    }

    /**
     * @param netcost the netcost to set
     */
    public void setNetcost(BigDecimal netcost) {
        this.netcost = netcost;
    }

    /**
     * @return the staticText
     */
    public Boolean getStaticText() {
        return staticText;
    }

    /**
     * @param staticText the staticText to set
     */
    public void setStaticText(Boolean staticText) {
        this.staticText = staticText;
    }

    /**
     * @return the instProjectGuideDTOConverter
     */
    public InstProjectGuideDTOConverter getInstProjectGuideDTOConverter() {
        return instProjectGuideDTOConverter;
    }

    /**
     * @param instProjectGuideDTOConverter the instProjectGuideDTOConverter to
     * set
     */
    public void setInstProjectGuideDTOConverter(InstProjectGuideDTOConverter instProjectGuideDTOConverter) {
        this.instProjectGuideDTOConverter = instProjectGuideDTOConverter;
    }

    /**
     * @return the instProjectGuideDtoList
     */
    public List<InstProjectGuideDTO> getInstProjectGuideDtoList() {
        if (instProjectGuideDtoList == null || instProjectGuideDtoList.isEmpty()) {
            instProjectGuideDtoList = instProjectGuideService.getAllSubInstProjectGuideByBranchIdActiveAndLevelDTO(getUserData().getUserBranch().getId(), 2, 3);
            instProjectGuideDTOConverter = new InstProjectGuideDTOConverter(instProjectGuideDtoList);
        }
        return instProjectGuideDtoList;
    }

    /**
     * @param instProjectGuideDtoList the instProjectGuideDtoList to set
     */
    public void setInstProjectGuideDtoList(List<InstProjectGuideDTO> instProjectGuideDtoList) {
        this.instProjectGuideDtoList = instProjectGuideDtoList;
    }

    /**
     * @return the idLoaded
     */
    public Boolean getIdLoaded() {
        if (idLoaded == null) {
            idLoaded = Boolean.FALSE;
        }
        return idLoaded;
    }

    /**
     * @param idLoaded the idLoaded to set
     */
    public void setIdLoaded(Boolean idLoaded) {
        this.idLoaded = idLoaded;
    }

    /**
     * @return the extractionWorkunitSearchBean
     */
    public RscContractExtractionWorkunitSearchBean getExtractionWorkunitSearchBean() {
        if(extractionWorkunitSearchBean == null){
            extractionWorkunitSearchBean = new RscContractExtractionWorkunitSearchBean();
        }
        return extractionWorkunitSearchBean;
    }

    /**
     * @param extractionWorkunitSearchBean the extractionWorkunitSearchBean to
     * set
     */
    public void setExtractionWorkunitSearchBean(RscContractExtractionWorkunitSearchBean extractionWorkunitSearchBean) {
        this.extractionWorkunitSearchBean = extractionWorkunitSearchBean;
    }

}
