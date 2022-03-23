/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rscPriceTender;

import com.isag.businessservice.RscPriceTenderDetailWorkUnitService;
import com.isag.businessservice.RscPriceTenderService;
import com.isag.businessservice.RscTenderDetailWorkUnitService;
import com.isag.businessservice.RscTenderService;
import com.isag.converter.InvOrganizationSiteConverter;
import com.isag.converter.SymbolConverter;
import com.isag.dto.RscPriceTenderDTO;
import com.isag.dto.RscPriceTenderDetailWorkUnitDTO;
import com.isag.dto.RscTenderDTO;
import com.isag.dto.RscTenderDetailWorkUnitDTO;
import com.isag.isag.RSCFormBasicData;
import com.isag.isag.UserData;
import com.isag.rscTender.RscTenderCommon;
import java.math.BigDecimal;
import java.math.MathContext;
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
import org.primefaces.context.RequestContext;

/**
 *
 * @author hq003
 */
@Named(value = "rscPriceTenderFormMB")
@ViewScoped
public class rscPriceTenderFormMB extends RSCFormBasicData {

    private List<RscPriceTenderDetailWorkUnitDTO> rscPriceTenderDetailWorkUnitDeletedList;
    private RscPriceTenderDTO priceTenderDTO;
    private RscPriceTenderDetailWorkUnitDTO rscPriceTenderDetailWorkUnitDTOSelected;
    private List<RscPriceTenderDetailWorkUnitDTO> priceTenderDetail;
    private List<RscPriceTenderDetailWorkUnitDTO> selectedUnit;
    private SymbolConverter symbolConverter;
    private Integer priceTenderId;
    private InvOrganizationSiteConverter invOrganizationSiteConverter;
    Integer serialDetail = 0;
    private boolean add;

    private List<RscTenderDTO> tenderList;

    private RscPriceTenderCommon rscPriceTenderCommon;

    @EJB
    private RscPriceTenderService rscPriceTenderService;
    @EJB
    private RscPriceTenderDetailWorkUnitService rscPriceTenderDetailWorkUnitService;
    @EJB
    private RscTenderService rscTenderService;
    @EJB
    private RscTenderDetailWorkUnitService rscTenderDetailWorkUnitService;

    @PostConstruct
    @Override
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "rscPriceTenderFormMB", "init");
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
                priceTenderDTO = getRscPriceTenderCommon().returnObject(priceTenderId);
                add = Boolean.TRUE;

            }
        } catch (Exception e) {
            saveError(e, "rscPriceTenderFormMB", "load");
        }
    }

    public void fillLists() {
        try {
            getRscWorkUnitsGuideDTOList();
        } catch (Exception e) {
            saveError(e, "rscPriceTenderFormMB", "fillLists");
        }

    }

    @Override
    public String save() {
        try {
            if (validation()) {
                priceTenderDTO = rscPriceTenderService.addRscPriceTender(priceTenderDTO, priceTenderDTO.getRscPriceTenderDetailWorkUnitDTOList(), getRscPriceTenderDetailWorkUnitDeletedList(), getUserData().getUser());

                if (priceTenderDTO.getMsg() != null && !priceTenderDTO.getMsg().isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), priceTenderDTO.getMsg()));

                } else {

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), "تم الحفظ بنجاح"));
                }
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "حدث خطأ!"));
            saveError(e, "rscPriceTenderFormMB", "save");

            e.printStackTrace();
        }
        priceTenderDTO.setMsg(null);

        return "";

    }

    public void reset() {
        try {
            rscPriceTenderDetailWorkUnitDTOSelected = null;
            priceTenderDTO = new RscPriceTenderDTO();
            priceTenderDTO.setRscPriceTenderDetailWorkUnitDTOList(new ArrayList<>());
            priceTenderDetail = new ArrayList<>();
            rscPriceTenderDetailWorkUnitDeletedList = new ArrayList<>();
            getTenderList();
            priceTenderDTO.setRscTenderDTO(new RscTenderDTO());
            priceTenderDTO.setDate(new Date());
            add = Boolean.FALSE;
            /*    if (getTenderList() != null && !getTenderList().isEmpty()) {
             priceTenderDTO.setRscTenderDTO(getTenderList().get(0));
             //   loadTender();
             }else{
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد تسعير"));
             priceTenderDTO.setRscTenderDTO(new RscTenderDTO());
             } */
        } catch (Exception e) {
            saveError(e, "rscPriceTenderFormMB", "reset");
        }
    }

    private Boolean validation() {
        Boolean validation = Boolean.TRUE;
        String ErrorMsg = "";
        if (priceTenderDTO.getDate() == null) {
            ErrorMsg += "يجب اختيار تاريخ التحرير \n";

            validation = Boolean.FALSE;
        }
        if (priceTenderDTO.getStatus() == null) {
            ErrorMsg += "يجب ادخال خالة التسعير \n";

            validation = Boolean.FALSE;
        }
        if (priceTenderDTO.getRscPriceTenderDetailWorkUnitDTOList() != null && !priceTenderDTO.getRscPriceTenderDetailWorkUnitDTOList().isEmpty()) {
            for (RscPriceTenderDetailWorkUnitDTO rscPriceTenderDetailWorkUnitDTO : priceTenderDTO.getRscPriceTenderDetailWorkUnitDTOList()) {
                if (rscPriceTenderDetailWorkUnitDTO.getGuideDTO() != null) {
                    if (rscPriceTenderDetailWorkUnitDTO.getGuideDTO().getValue() == null) {
                        ErrorMsg += "يجب اختيار القيمه الاساسية\n ";

                        validation = Boolean.FALSE;
                    }
                    if (rscPriceTenderDetailWorkUnitDTO.getGuideDTO().getValue() != null && rscPriceTenderDetailWorkUnitDTO.getGuideDTO().getValue().compareTo(BigDecimal.ZERO) < 0) {
                        ErrorMsg += "يجب اختيار القيمه الاساسية اكبر من او تساوي صفر\n ";

                        validation = Boolean.FALSE;
                    }
                    if (rscPriceTenderDetailWorkUnitDTO.getQuantityIn() == null) {
                        ErrorMsg += "يجب اختيار كميه بند المناقصة\n ";

                        validation = Boolean.FALSE;
                    }
                    if (rscPriceTenderDetailWorkUnitDTO.getQuantityIn().compareTo(BigDecimal.ZERO) < 0) {
                        ErrorMsg += "يجب اختيار كميه بند المناقصة اكبر من صفر \n ";

                        validation = Boolean.FALSE;
                    }
                    if (rscPriceTenderDetailWorkUnitDTO.getProfitValue() == null || rscPriceTenderDetailWorkUnitDTO.getProfitValue().compareTo(BigDecimal.ZERO) < 0) {
                        ErrorMsg += "يجب اختيار قيمةالربح\n ";

                        validation = Boolean.FALSE;
                    }
                    if (rscPriceTenderDetailWorkUnitDTO.getProfitPercentage() == null || rscPriceTenderDetailWorkUnitDTO.getProfitPercentage().compareTo(BigDecimal.ZERO) < 0) {
                        ErrorMsg += "يجب اختيار نسبة الربح\n ";

                        validation = Boolean.FALSE;
                    }
                }
            }

        }
      if(!ErrorMsg.isEmpty()){
        for (String error : ErrorMsg.subSequence(0, ErrorMsg.lastIndexOf("\n")).toString().split("\n")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), error));
        }
    }
        return validation;
    }

    public String exit() {
        try {

            exit("../rscTenderPrice/rscTenderPriceList.xhtml");

            return "";
        } catch (Exception e) {
            saveError(e, "rscPriceTenderFormMB", "exit");
            return null;
        }
    }

    public void addChild() {
        try {
            if (priceTenderDTO.getRscPriceTenderDetailWorkUnitDTOList() == null) {
                priceTenderDTO.setRscPriceTenderDetailWorkUnitDTOList(new ArrayList<>());
            }
            RscPriceTenderDetailWorkUnitDTO detailWorkUnitDTO = new RscPriceTenderDetailWorkUnitDTO();
            setIndex(getIndex() + 1);
            detailWorkUnitDTO.setIndex(getIndex());
            priceTenderDTO.getRscPriceTenderDetailWorkUnitDTOList().add(detailWorkUnitDTO);
        } catch (Exception e) {
            saveError(e, "rscPriceTenderFormMB", "addChild");
        }
    }

    public void validateUnitGuide() {
        try {
            if (rscPriceTenderDetailWorkUnitDTOSelected != null) {
                if (rscPriceTenderDetailWorkUnitDTOSelected.getGuideDTO() != null) {
                    rscPriceTenderDetailWorkUnitDTOSelected.setGuideDTOBak(rscPriceTenderDetailWorkUnitDTOSelected.getGuideDTO());
                }
            }
        } catch (Exception e) {
            saveError(e, "rscPriceTenderFormMB", "validateUnitGuide");
        }
    }

    //---------------------Calculate Methods---------------------------------
    public void calculateTotalUnits() {
        try {

            for (RscPriceTenderDetailWorkUnitDTO tenderDetail : priceTenderDTO.getRscPriceTenderDetailWorkUnitDTOList()) {
                if (tenderDetail.getGuideDTO() != null) {
                    tenderDetail.setQuantityUnits((tenderDetail.getGuideDTO().getQuantity() == null ? BigDecimal.ZERO : tenderDetail.getGuideDTO().getQuantity()).round(new MathContext(4, RoundingMode.HALF_UP)));
                    tenderDetail.setPriceUnit((tenderDetail.getGuideDTO().getValue() != null ? (tenderDetail.getGuideDTO().getValue()) : BigDecimal.ZERO).round(new MathContext(4, RoundingMode.HALF_UP)));
                }
                tenderDetail.setTotalUnits(((tenderDetail.getQuantityUnits() != null ? tenderDetail.getQuantityUnits() : BigDecimal.ZERO).multiply(tenderDetail.getPriceUnit() != null ? tenderDetail.getPriceUnit() : BigDecimal.ZERO)).round(new MathContext(4, RoundingMode.HALF_UP)));
            }
        } catch (Exception e) {
            saveError(e, "rscPriceTenderFormMB", "calculateTotalUnits");
        }
    }

    public void calprofitValue() {
        try {
            BigDecimal total = new BigDecimal(0);
            for (RscPriceTenderDetailWorkUnitDTO tenderDetail : priceTenderDTO.getRscPriceTenderDetailWorkUnitDTOList()) {
                if (tenderDetail.getProfitValue() != null && tenderDetail.getProfitValue().compareTo(BigDecimal.ZERO) == 1) {
                    BigDecimal quantity = tenderDetail.getGuideDTO() != null ? tenderDetail.getGuideDTO().getValue() : BigDecimal.ZERO;
                    BigDecimal P_Value = (tenderDetail.getProfitValue() != null ? tenderDetail.getProfitValue() : BigDecimal.ZERO);
                   tenderDetail.setProfitPercentage((P_Value.divide(quantity, 3, BigDecimal.ROUND_UP)).multiply(new BigDecimal(100)).setScale(3, RoundingMode.HALF_UP));
                    //tenderDetail.setProfitPercentage((P_Value.divide(quantity, 3, BigDecimal.ROUND_UP)).multiply(new BigDecimal(100)).round(new MathContext(4, RoundingMode.HALF_UP)));
//                    tenderDetail.setProfitPercentage((((tenderDetail.getProfitValue().compareTo(BigDecimal.ZERO) == 0 || tenderDetail.getProfitValue() == null ? BigDecimal.ONE : tenderDetail.getProfitValue()).divide(tenderDetail.getTotalUnits() == null || tenderDetail.getTotalUnits().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ONE : tenderDetail.getTotalUnits())).multiply(new BigDecimal(100))).round(new MathContext(4, RoundingMode.HALF_UP)));
                }
                calculateTotalProfitAndSell(tenderDetail, total);
            }
        } catch (Exception e) {
            saveError(e, "rscPriceTenderFormMB", "calprofitValue");
        }

    }

    public void calpercentage() {
        try {
            BigDecimal total = new BigDecimal(0);
            for (RscPriceTenderDetailWorkUnitDTO tenderDetail : priceTenderDTO.getRscPriceTenderDetailWorkUnitDTOList()) {
                if (tenderDetail.getProfitPercentage() != null && tenderDetail.getProfitPercentage().compareTo(BigDecimal.ZERO) == 1) {
                    tenderDetail.setProfitValue(((((tenderDetail.getGuideDTO() != null ? tenderDetail.getGuideDTO().getValue() : BigDecimal.ZERO))).multiply(tenderDetail.getProfitPercentage() != null ? tenderDetail.getProfitPercentage() : BigDecimal.ZERO)).divide(new BigDecimal(100)).round(new MathContext(4, RoundingMode.HALF_UP)));

//                    tenderDetail.setProfitValue((((tenderDetail.getTotalUnits() != null ? tenderDetail.getTotalUnits() : BigDecimal.ZERO).multiply(tenderDetail.getProfitPercentage()))).divide(new BigDecimal(100)).round(new MathContext(4, RoundingMode.HALF_UP)));
                }
                calculateTotalProfitAndSell(tenderDetail, total);
            }
        } catch (Exception e) {
            saveError(e, "rscPriceTenderFormMB", "calpercentage");
        }

    }

    public void calculateTotalProfitAndSell(RscPriceTenderDetailWorkUnitDTO tenderDetail, BigDecimal total) {
        try {
            if (tenderDetail.getQuantityIn() != null && tenderDetail.getGuideDTO() != null && tenderDetail.getGuideDTO().getValue() != null) {
                tenderDetail.setTotalQuantityUnit(((tenderDetail.getQuantityIn() != null ? tenderDetail.getQuantityIn() : BigDecimal.ZERO).multiply(tenderDetail.getQuantityUnits() != null ? tenderDetail.getQuantityUnits() : BigDecimal.ZERO)).round(new MathContext(3, RoundingMode.HALF_UP)));
                tenderDetail.setTotalCost(((tenderDetail.getQuantityIn() != null ? tenderDetail.getQuantityIn() : BigDecimal.ZERO).multiply(tenderDetail.getTotalUnits() != null ? tenderDetail.getTotalUnits() : BigDecimal.ZERO)).round(new MathContext(3, RoundingMode.HALF_UP)));
                
                tenderDetail.setPriceUnitSell((((tenderDetail.getProfitPercentage()!= null ? tenderDetail.getProfitPercentage() : BigDecimal.ZERO).divide(new BigDecimal(100),3,RoundingMode.HALF_UP)).multiply(tenderDetail.getGuideDTO() != null ? tenderDetail.getGuideDTO().getValue() : BigDecimal.ZERO)).add(tenderDetail.getGuideDTO() != null ? tenderDetail.getGuideDTO().getValue() : BigDecimal.ZERO));
                
            tenderDetail.setTotalProfit((((tenderDetail.getProfitPercentage() != null ? tenderDetail.getProfitPercentage() : BigDecimal.ZERO).divide(new BigDecimal(100) , 3 ,RoundingMode.HALF_UP)).multiply(tenderDetail.getGuideDTO()!= null ? tenderDetail.getGuideDTO().getValue() : BigDecimal.ZERO)).setScale(3, RoundingMode.HALF_UP));

               
                total = (((tenderDetail.getTotalUnits() != null ? tenderDetail.getTotalUnits() : BigDecimal.ZERO)
                        .add(tenderDetail.getProfitValue() != null ? tenderDetail.getProfitValue() : BigDecimal.ZERO)).round(new MathContext(4, RoundingMode.HALF_UP)));
            tenderDetail.setTotalSell(((tenderDetail.getPriceUnitSell()!= null ? tenderDetail.getPriceUnitSell() : BigDecimal.ZERO).multiply(tenderDetail.getQuantityIn() != null ? tenderDetail.getQuantityIn() : BigDecimal.ZERO)).setScale(3,RoundingMode.HALF_UP));

            }
        } catch (Exception e) {
            saveError(e, "rscPriceTenderFormMB", "calculateTotalProfitAndSell");
        }
    }

    //-------------------------------------------------------------------------------------
    public void deleteRscTenderDetail() {
        try {

            if (rscPriceTenderDetailWorkUnitDTOSelected != null) {
                priceTenderDTO.getRscPriceTenderDetailWorkUnitDTOList().remove(rscPriceTenderDetailWorkUnitDTOSelected);
                if (rscPriceTenderDetailWorkUnitDTOSelected.getId() != null) {
                    rscPriceTenderDetailWorkUnitDeletedList.add(rscPriceTenderDetailWorkUnitDTOSelected);
                }
            }
        } catch (Exception e) {
            saveError(e, "rscPriceTenderFormMB", "deleteRscTenderDetail");
        }
    }

    public void print() {
        try {
            if (priceTenderDTO != null && priceTenderDTO.getId() != null
                    && priceTenderDTO.getRscPriceTenderDetailWorkUnitDTOList() != null
                    && !priceTenderDTO.getRscPriceTenderDetailWorkUnitDTOList().isEmpty()) {
                rscPriceTenderCommon.print(priceTenderDTO);
            }
        } catch (Exception e) {
            saveError(e, "rscPriceTenderFormMB", "print");
        }
    }

    public void loadTender() {
        try {
            if (priceTenderDTO.getRscTenderDTO() != null && priceTenderDTO.getRscTenderDTO().getId() != null) {
                List<RscTenderDetailWorkUnitDTO> rscTenderDetailWorkUnitDTOList = rscTenderDetailWorkUnitService.getTenderDetailWorkUnitByTenderId(priceTenderDTO.getRscTenderDTO().getId());
                for (RscTenderDetailWorkUnitDTO detailWorkUnitDTO : rscTenderDetailWorkUnitDTOList) {
                    RscPriceTenderDetailWorkUnitDTO detail = new RscPriceTenderDetailWorkUnitDTO();
                    detail.setGuideDTO(detailWorkUnitDTO.getGuideDTO());
                    detail.setGuideDTOBak(detailWorkUnitDTO.getGuideDTO());
                    detail.setQuantityUnits(((detailWorkUnitDTO.getGuideDTO() != null && detailWorkUnitDTO.getGuideDTO().getQuantity() != null) ? detailWorkUnitDTO.getGuideDTO().getQuantity() : BigDecimal.ZERO).round(new MathContext(4, RoundingMode.HALF_UP)));
                    detail.setPriceUnit(((detailWorkUnitDTO.getGuideDTO() != null && detailWorkUnitDTO.getGuideDTO().getValue() != null) ? detailWorkUnitDTO.getGuideDTO().getValue() : BigDecimal.ZERO).round(new MathContext(4, RoundingMode.HALF_UP)));
                    detail.setQuantityIn(detailWorkUnitDTO.getQuantity().round(new MathContext(4, RoundingMode.HALF_UP)));
                    detail.setTotalUnits(((detailWorkUnitDTO.getGuideDTO().getQuantity() == null ? BigDecimal.ZERO : detailWorkUnitDTO.getGuideDTO().getQuantity()).multiply(detailWorkUnitDTO.getGuideDTO().getValue() == null ? BigDecimal.ZERO : detailWorkUnitDTO.getGuideDTO().getValue())).round(new MathContext(4, RoundingMode.HALF_UP)));
                    detail.setTotalQuantityUnit(((detailWorkUnitDTO.getQuantity() == null ? BigDecimal.ZERO : detailWorkUnitDTO.getQuantity()).multiply(detail.getQuantityUnits())).round(new MathContext(4, RoundingMode.HALF_UP)));
                    detail.setTotalCost(((detailWorkUnitDTO.getQuantity() == null ? BigDecimal.ZERO : detailWorkUnitDTO.getQuantity()).multiply(detail.getTotalUnits())).round(new MathContext(4, RoundingMode.HALF_UP)));
                    setIndex(getIndex() + 1);
                    detail.setProfitValue(BigDecimal.ZERO);
                    detail.setProfitPercentage(BigDecimal.ZERO);
                    calprofitValue();
                    calpercentage();
                    detailWorkUnitDTO.setIndex(getIndex());
                    priceTenderDTO.getRscPriceTenderDetailWorkUnitDTOList().add(detail);
                }
                add = Boolean.TRUE;
                CloseDlg("dlg2");
            }
        } catch (Exception e) {
            saveError(e, "rscPriceTenderFormMB", "loadTender");
        }
    }

    @Override
    public String getScreenName() {
        return "costcenter";
    }

    /**
     * @return the tenderDTO
     */
    public RscPriceTenderDTO getPriceTenderDTO() {
        if (priceTenderDTO == null) {
            priceTenderDTO = new RscPriceTenderDTO();
        }
        return priceTenderDTO;
    }

    /**
     * @param tenderDTO the tenderDTO to set
     */
    public void setPriceTenderDTO(RscPriceTenderDTO priceTenderDTO) {
        this.priceTenderDTO = priceTenderDTO;
    }

    /**
     * @return the rscTenderDetailWorkUnitDTOSelected
     */
    public RscPriceTenderDetailWorkUnitDTO getRscPriceTenderDetailWorkUnitDTOSelected() {
        return rscPriceTenderDetailWorkUnitDTOSelected;
    }

    /**
     * @param rscTenderDetailWorkUnitDTOSelected the
     * rscTenderDetailWorkUnitDTOSelected to set
     */
    public void setRscPriceTenderDetailWorkUnitDTOSelected(RscPriceTenderDetailWorkUnitDTO rscPriceTenderDetailWorkUnitDTOSelected) {
        this.rscPriceTenderDetailWorkUnitDTOSelected = rscPriceTenderDetailWorkUnitDTOSelected;
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
     * @return the priceTenderDetail
     */
    public List<RscPriceTenderDetailWorkUnitDTO> getPriceTenderDetail() {
        if (priceTenderDetail == null) {
            setPriceTenderDetail(new ArrayList<>());
        }
        return priceTenderDetail;
    }

    /**
     * @param priceTenderDetail the priceTenderDetail to set
     */
    public void setPriceTenderDetail(List<RscPriceTenderDetailWorkUnitDTO> priceTenderDetail) {
        this.priceTenderDetail = priceTenderDetail;
    }

    /**
     * @return the selectedUnit
     */
    public List<RscPriceTenderDetailWorkUnitDTO> getSelectedUnit() {
        return selectedUnit;
    }

    /**
     * @param selectedUnit the selectedUnit to set
     */
    public void setSelectedUnit(List<RscPriceTenderDetailWorkUnitDTO> selectedUnit) {
        this.selectedUnit = selectedUnit;
    }

    /**
     * @return the rscPriceTenderDetailWorkUnitDeletedList
     */
    public List<RscPriceTenderDetailWorkUnitDTO> getRscPriceTenderDetailWorkUnitDeletedList() {
        if (rscPriceTenderDetailWorkUnitDeletedList == null) {
            rscPriceTenderDetailWorkUnitDeletedList = new ArrayList<>();
        }
        return rscPriceTenderDetailWorkUnitDeletedList;
    }

    /**
     * @param rscPriceTenderDetailWorkUnitDeletedList the
     * rscPriceTenderDetailWorkUnitDeletedList to set
     */
    public void setRscPriceTenderDetailWorkUnitDeletedList(List<RscPriceTenderDetailWorkUnitDTO> rscPriceTenderDetailWorkUnitDeletedList) {
        this.rscPriceTenderDetailWorkUnitDeletedList = rscPriceTenderDetailWorkUnitDeletedList;
    }

    /**
     * @return the tenderList
     */
    public List<RscTenderDTO> getTenderList() {
        if (tenderList == null || tenderList.isEmpty()) {
//            tenderList = rscTenderService.getBranchRscTenderActiveByBranch(getUserData().getUserBranch().getId(),getUserData().getUser());
            tenderList = rscTenderService.getRscTenderActiveByBranch(getUserData().getUserBranch().getId(), getUserData().getUser());

        }
        return tenderList;
    }

    /**
     * @param tenderList the tenderList to set
     */
    public void setTenderList(List<RscTenderDTO> tenderList) {
        this.tenderList = tenderList;
    }

    /**
     * @return the rscPriceTenderCommon
     */
    public RscPriceTenderCommon getRscPriceTenderCommon() {
        if (rscPriceTenderCommon == null) {
            rscPriceTenderCommon = new RscPriceTenderCommon(rscPriceTenderService, getUserData());
        }
        return rscPriceTenderCommon;
    }

    /**
     * @param rscPriceTenderCommon the rscPriceTenderCommon to set
     */
    public void setRscPriceTenderCommon(RscPriceTenderCommon rscPriceTenderCommon) {
        this.rscPriceTenderCommon = rscPriceTenderCommon;
    }

    /**
     * @return the add
     */
    public boolean isAdd() {
        return add;
    }

    /**
     * @param add the add to set
     */
    public void setAdd(boolean add) {
        this.add = add;
    }

}
