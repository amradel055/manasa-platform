/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rscContractRegister;

import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.RscContractRegisterDetailWorkUnitService;
import com.isag.businessservice.RscContractRegisterService;
import com.isag.businessservice.RscQotationPriceTenderDetailWorkUnitService;
import com.isag.businessservice.RscQotationPriceTenderService;
import com.isag.businessservice.RscWorkUnitsGuideService;
import com.isag.converter.InvOrganizationSiteConverter;
import com.isag.dto.RscContractRegisterDTO;
import com.isag.dto.RscContractRegisterDetailWorkUnitDTO;
import com.isag.dto.RscQotationPriceTenderDTO;
import com.isag.dto.RscQotationPriceTenderDetailWorkUnitDTO;
import com.isag.dto.RscTenderDTO;
import com.isag.isag.RSCFormBasicData;
import com.isag.isag.UserData;
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
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @author hq003
 */
@Named(value = "rscContractRegisterFormMB")
@ViewScoped
public class RscContractRegisterFormMB extends RSCFormBasicData {

    private RscContractRegisterDetailWorkUnitDTO RscContractRegisterDetailWorkUnitDTOSelected;
    private List<RscContractRegisterDetailWorkUnitDTO> contractTenderDetail;

    private RscContractRegisterDTO rscContractRegisterDTO;
    private RscContractRegisterDTO rscContractRegisterDTOtest;
    private List<RscQotationPriceTenderDTO> rscQotationPriceTenderDTOList;
    private List<RscContractRegisterDetailWorkUnitDTO> selectedUnit;
    private Integer contractId;
    private String uri;
    private InvOrganizationSiteConverter invOrganizationSiteConverter;

    private BigDecimal contractTotal;
    private BigDecimal initialValue;
    private BigDecimal initialPercentage;
    private Boolean selectTender;
    private Boolean subcontractor;
    Integer serialDetail = 0;

    private RscContractRegisterCommon rscContractRegisterCommon;

    @EJB
    private RscContractRegisterService rscContractRegisterService;
    @EJB
    private RscContractRegisterDetailWorkUnitService rscContractRegisterDetailWorkUnitService;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private RscWorkUnitsGuideService rscWorkUnitsGuideService;
    @EJB
    private RscQotationPriceTenderService qotationPriceTenderService;
    @EJB
    private RscQotationPriceTenderDetailWorkUnitService qotationPriceTenderDetailWorkUnitService;

    @PostConstruct
    @Override
    public void init() {
        try {
            load();
            if (getUri().contains("newrscContractregisterform")) {
                getRscTenderDTOList();

            }
        } catch (Exception e) {
            saveError(e, "RscContractRegisterFormMB", "init");
        }
    }

    @Override
    public void load() {
        try {

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            getUserData().getUser().setBranchId(getUserData().getUserBranch());
            setUri(((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI());

            fillLists();
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("AddrscContractRegisterDTOSelected")) {
                reset();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditrscContractRegisterDTOSelected")) {
                // get tenderiD From Session
                contractId = (Integer) context.getSessionMap().get("rscContractRegisterDTOSelected");

                rscContractRegisterDTO = getRscContractRegisterCommon().returnObject(contractId);
                if (rscContractRegisterDTO.getTenderId() != null && rscContractRegisterDTO.getTenderId().getId() != null) {
                    getRscQotationPriceTenderList(rscContractRegisterDTO.getTenderId().getId());
                    selectTender = Boolean.TRUE;
                    if (rscContractRegisterDTO.getTenderId().getType() == 1) {
                        subcontractor = Boolean.TRUE;
                    }
                }
            }
        } catch (Exception e) {
            saveError(e, "RscContractRegisterFormMB", "load");
        }
    }

    public void hishis() {
        //Hello HisHis
        System.out.println("yarab");
    }

    public void valitdatetotal() {
        try {
            if (rscContractRegisterDTO != null) {
                if ((rscContractRegisterDTO.getNettotal() != null ? rscContractRegisterDTO.getNettotal() : BigDecimal.ZERO)
                        .compareTo(rscContractRegisterDTO.getContractTotal() != null ? rscContractRegisterDTO.getContractTotal() : BigDecimal.ZERO) == 0) {
                    save();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "حدث خطأ يجب أن يكون الاجمالى مساوى لقيمة اجمالى العقد"));

                }

            }
        } catch (Exception e) {
            saveError(e, "RscContractRegisterFormMB", "valitdatetotal");
        }
    }

    public void print() {
        try {
            if (rscContractRegisterDTO != null && rscContractRegisterDTO.getId() != null
                    && rscContractRegisterDTO.getContractRegisterDetailList() != null
                    && !rscContractRegisterDTO.getContractRegisterDetailList().isEmpty()) {
                rscContractRegisterCommon.print(rscContractRegisterDTO);
            }
        } catch (Exception e) {
            saveError(e, "RscContractRegisterFormMB", "print");
        }
    }

    @Override
    public String save() {
        try {
            try {
                setRscContractRegisterDTO(rscContractRegisterService.addRscContractRegister(getRscContractRegisterDTO(), getUserData().getUser()));

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), "تم الحفظ بنجاح"));

            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "حدث خطأ!"));
                e.printStackTrace();
            }
            return "";
        } catch (Exception e) {
            saveError(e, "RscContractRegisterFormMB", "save");
            return null;
        }

    }

    public void fillLists() {
        try {
            getRscWorkUnitsGuideDTOList();
            getContractorList();
        } catch (Exception e) {
            saveError(e, "RscContractRegisterFormMB", "fillLists");
        }
    }

    public void reset() {
        try {
            RscContractRegisterDetailWorkUnitDTOSelected = null;
            setRscContractRegisterDTO(new RscContractRegisterDTO());
            rscContractRegisterDTO.setContractRegisterDetailList(new ArrayList<>());
            contractTenderDetail = new ArrayList<>();
            rscContractRegisterDTO.setDate(new Date());
            setRscQotationPriceTenderList(new ArrayList<>());
            selectTender = Boolean.FALSE;
        } catch (Exception e) {
            saveError(e, "RscContractRegisterFormMB", "reset");
        }
    }

    public String exit() {
        try {

            exit("../rscContractRegistering/rscContractregisterlist.xhtml");

            return "";
        } catch (Exception e) {
            saveError(e, "RscContractRegisterFormMB", "exit");
            return null;
        }
    }

    public void selectTender() {
        setRscQotationPriceTenderList(new ArrayList<>());
        rscContractRegisterDTO.setRscQotationPriceTenderId(null);
        rscContractRegisterDTO.setSiteId(null);
        getRscQotationPriceTenderList(rscContractRegisterDTO.getTenderId() != null ? rscContractRegisterDTO.getTenderId().getId() : null);
        selectTender = Boolean.TRUE;
        if (rscContractRegisterDTO.getTenderId().getType() != null && rscContractRegisterDTO.getTenderId().getType() == 1) {
            setSubcontractor(Boolean.TRUE);
        } else {
            setSubcontractor(Boolean.FALSE);
        }
    }

    public void selectQotation() {
        rscContractRegisterDTO.setSiteId(rscContractRegisterDTO.getRscQotationPriceTenderId().getSubContractor());
    }

    public void addChild() {
        try {
            if (rscContractRegisterDTO.getContractRegisterDetailList() == null) {
                rscContractRegisterDTO.setContractRegisterDetailList(new ArrayList<>());
            }
            RscContractRegisterDetailWorkUnitDTO detailWorkUnitDTO = new RscContractRegisterDetailWorkUnitDTO();
            setIndex(getIndex() + 1);
            detailWorkUnitDTO.setIndex(getIndex());
            rscContractRegisterDTO.getContractRegisterDetailList().add(detailWorkUnitDTO);
        } catch (Exception e) {
            saveError(e, "RscContractRegisterFormMB", "addChild");
        }
    }

    public void validateUnitGuide() {
        try {
            if (RscContractRegisterDetailWorkUnitDTOSelected != null) {
                if (RscContractRegisterDetailWorkUnitDTOSelected.getGuideDTO() != null) {
                    RscContractRegisterDetailWorkUnitDTOSelected.setGuideDTOBak(RscContractRegisterDetailWorkUnitDTOSelected.getGuideDTO());

                }
            }
        } catch (Exception e) {
            saveError(e, "RscContractRegisterFormMB", "validateUnitGuide");
        }
    }

    public void calprofitValue() {
        try {
            if (getRscContractRegisterDTO().getContractTotal() != null && getRscContractRegisterDTO().getContractTotal().compareTo(BigDecimal.ZERO) == 1
                    && getRscContractRegisterDTO().getInitialValue() != null && getRscContractRegisterDTO().getInitialValue().compareTo(BigDecimal.ZERO) == 1) {
                BigDecimal quantity = (getRscContractRegisterDTO().getContractTotal() != null ? getRscContractRegisterDTO().getContractTotal() : BigDecimal.ZERO);
                BigDecimal P_Value = (getRscContractRegisterDTO().getInitialValue() != null ? getRscContractRegisterDTO().getInitialValue() : BigDecimal.ZERO);
                getRscContractRegisterDTO().setInitialPercentage((P_Value.divide(quantity, 3, BigDecimal.ROUND_UP)).multiply(new BigDecimal(100)).round(new MathContext(4, RoundingMode.HALF_UP)));
            }
        } catch (Exception e) {
            saveError(e, "RscContractRegisterFormMB", "calprofitValue");
        }
    }

    public void calpercentage() {
        try {
            if (getRscContractRegisterDTO().getContractTotal() != null && getRscContractRegisterDTO().getContractTotal().compareTo(BigDecimal.ZERO) == 1
                    && getRscContractRegisterDTO().getInitialValue() != null && getRscContractRegisterDTO().getInitialValue().compareTo(BigDecimal.ZERO) == 1) {
                BigDecimal quantity = (getRscContractRegisterDTO().getContractTotal() != null ? getRscContractRegisterDTO().getContractTotal() : BigDecimal.ZERO);
                BigDecimal P_Value = (getRscContractRegisterDTO().getInitialPercentage() != null ? getRscContractRegisterDTO().getInitialPercentage() : BigDecimal.ZERO);
                getRscContractRegisterDTO().setInitialValue((P_Value.divide(new BigDecimal(100), 3, BigDecimal.ROUND_UP)).multiply(quantity).round(new MathContext(4, RoundingMode.HALF_UP)));
            }
        } catch (Exception e) {
            saveError(e, "RscContractRegisterFormMB", "calpercentage");
        }

    }

    //---------------------Calculate Methods---------------------------------
    public void calculateTotalUnits() {
        try {
            rscContractRegisterDTO.setTotalprice(BigDecimal.ZERO);
            rscContractRegisterDTO.setTotalquantity(BigDecimal.ZERO);
            rscContractRegisterDTO.setNettotal(BigDecimal.ZERO);
            for (RscContractRegisterDetailWorkUnitDTO contractDetail : rscContractRegisterDTO.getContractRegisterDetailList()) {
                if (contractDetail.getGuideDTO() != null) {
                    contractDetail.setQuantity(contractDetail.getQuantity() != null ? contractDetail.getQuantity() : BigDecimal.ZERO);
                    contractDetail.setPrice(contractDetail.getPrice() != null ? contractDetail.getPrice() : BigDecimal.ZERO);
                }
                contractDetail.setTotal((contractDetail.getQuantity() != null ? contractDetail.getQuantity() : BigDecimal.ZERO).multiply(contractDetail.getPrice() != null ? contractDetail.getPrice() : BigDecimal.ZERO));

                rscContractRegisterDTO.setTotalquantity((rscContractRegisterDTO.getTotalquantity() != null ? rscContractRegisterDTO.getTotalquantity() : BigDecimal.ZERO).add(contractDetail.getQuantity() != null ? contractDetail.getQuantity() : BigDecimal.ZERO));
                rscContractRegisterDTO.setTotalprice((rscContractRegisterDTO.getTotalprice() != null ? rscContractRegisterDTO.getTotalprice() : BigDecimal.ZERO).add(contractDetail.getPrice() != null ? contractDetail.getPrice() : BigDecimal.ZERO));
                rscContractRegisterDTO.setNettotal((rscContractRegisterDTO.getNettotal() != null ? rscContractRegisterDTO.getNettotal() : BigDecimal.ZERO).add(contractDetail.getTotal() != null ? contractDetail.getTotal() : BigDecimal.ZERO));

            }
        } catch (Exception e) {
            saveError(e, "RscContractRegisterFormMB", "calculateTotalUnits");
        }
    }

    //-------------------------------------------------------------------------------------
    public void deleteRscTenderDetail() {
        try {

            if (RscContractRegisterDetailWorkUnitDTOSelected != null) {
                rscContractRegisterDTO.getContractRegisterDetailList().remove(RscContractRegisterDetailWorkUnitDTOSelected);
                if (RscContractRegisterDetailWorkUnitDTOSelected.getId() != null) {
                    getRscContractRegisterDTO().getRscContractRegisterDetailWorkUnitDeletedList().add(RscContractRegisterDetailWorkUnitDTOSelected);
                }
            }
        } catch (Exception e) {
            saveError(e, "RscContractRegisterFormMB", "deleteRscTenderDetail");
        }
    }

    public void loadOrder() {
        try {
            if (rscContractRegisterDTO.getRscQotationPriceTenderId() != null && rscContractRegisterDTO.getRscQotationPriceTenderId().getId() != null) {
                rscContractRegisterDTO.setContractRegisterDetailList(new ArrayList<>());
                RscQotationPriceTenderDTO qotationPriceTenderDTO = new RscQotationPriceTenderDTO();
                qotationPriceTenderDTO.setSerial(rscContractRegisterDTO.getRscQotationPriceTenderId().getSerial());
                qotationPriceTenderDTO.setStatus(rscContractRegisterDTO.getRscQotationPriceTenderId().getStatus());
                if (qotationPriceTenderDTO.getStatus() == 0 || qotationPriceTenderDTO.getStatus() == null) {
                    qotationPriceTenderDTO.setQotationStatus("لم يتم");
                } else if (qotationPriceTenderDTO.getStatus() == 1) {
                    qotationPriceTenderDTO.setQotationStatus("تم");
                }
                qotationPriceTenderDTO.setDate(rscContractRegisterDTO.getRscQotationPriceTenderId().getDate());
                qotationPriceTenderDTO.setRscPriceTenderId(rscContractRegisterDTO.getRscQotationPriceTenderId().getRscPriceTenderId());
                if (rscContractRegisterDTO.getRscQotationPriceTenderId().getRscPriceTenderId() != null && rscContractRegisterDTO.getRscQotationPriceTenderId().getRscPriceTenderId().getId() != null) {
                    if (rscContractRegisterDTO.getRscQotationPriceTenderId().getRscPriceTenderId().getRscTenderDTO() != null && rscContractRegisterDTO.getRscQotationPriceTenderId().getRscPriceTenderId().getRscTenderDTO().getId() != null) {
                        RscTenderDTO tenderDTO = new RscTenderDTO();
                        tenderDTO.setCustomerId(rscContractRegisterDTO.getRscQotationPriceTenderId().getRscPriceTenderId().getRscTenderDTO().getCustomerId());
                        tenderDTO.setSerial(rscContractRegisterDTO.getRscQotationPriceTenderId().getRscPriceTenderId().getRscTenderDTO().getSerial());
                        tenderDTO.setRemark(rscContractRegisterDTO.getRscQotationPriceTenderId().getRscPriceTenderId().getRscTenderDTO().getRemark());
                    }
                }
                List<RscQotationPriceTenderDetailWorkUnitDTO> detailList = qotationPriceTenderDetailWorkUnitService.getRscQotationPriceTenderDetailWorkUnitByTenderId(rscContractRegisterDTO.getRscQotationPriceTenderId().getId());
                BigDecimal totalqty = BigDecimal.ZERO;
                BigDecimal pricetotal = BigDecimal.ZERO;
                BigDecimal total = BigDecimal.ZERO;

                for (RscQotationPriceTenderDetailWorkUnitDTO detailLoop : detailList) {
                    RscContractRegisterDetailWorkUnitDTO detail = new RscContractRegisterDetailWorkUnitDTO();
                    detail.setGuideDTO(detailLoop.getGuideDTO());
                    detail.setGuideDTOBak(detailLoop.getGuideDTO());
                    detail.setPrice(detailLoop.getPrice());
                    detail.setQuantity(detailLoop.getQuantity());
                    detail.setTotal(((detailLoop.getPrice() == null ? BigDecimal.ZERO : detailLoop.getPrice()).multiply(detailLoop.getQuantity() == null ? BigDecimal.ZERO : detailLoop.getQuantity())));
                    setIndex(getIndex() + 1);
                    detailLoop.setIndex(getIndex());
                    rscContractRegisterDTO.setTotalquantity(totalqty.add(detail.getQuantity()));
                    rscContractRegisterDTO.setTotalprice(pricetotal.add(detail.getPrice()));
                    rscContractRegisterDTO.setNettotal(total.add(detail.getTotal()));
                    rscContractRegisterDTO.getContractRegisterDetailList().add(detail);
                }

                CloseDlg("dlg2");
            }
        } catch (Exception e) {
            saveError(e, "RscContractRegisterFormMB", "loadOrder");
        }
    }

    public void OpenDlg(String dlgvar) {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('" + dlgvar + "').show();");
        } catch (Exception e) {
            saveError(e, "RscContractRegisterFormMB", "OpenDlg");
        }
    }

    public void CloseDlg(String dlgvar) {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('" + dlgvar + "').hide();");
        } catch (Exception e) {
            saveError(e, "RscContractRegisterFormMB", "CloseDlg");
        }
    }

    @Override
    public String getScreenName() {
        return "costcenter";
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
     * @return the selectedUnit
     */
    public List<RscContractRegisterDetailWorkUnitDTO> getSelectedUnit() {
        return selectedUnit;
    }

    /**
     * @param selectedUnit the selectedUnit to set
     */
    public void setSelectedUnit(List<RscContractRegisterDetailWorkUnitDTO> selectedUnit) {
        this.selectedUnit = selectedUnit;
    }

    /**
     * @return the RscContractRegisterDetailWorkUnitDTOSelected
     */
    public RscContractRegisterDetailWorkUnitDTO getRscContractRegisterDetailWorkUnitDTOSelected() {
        return RscContractRegisterDetailWorkUnitDTOSelected;
    }

    /**
     * @param RscContractRegisterDetailWorkUnitDTOSelected the
     * RscContractRegisterDetailWorkUnitDTOSelected to set
     */
    public void setRscContractRegisterDetailWorkUnitDTOSelected(RscContractRegisterDetailWorkUnitDTO RscContractRegisterDetailWorkUnitDTOSelected) {
        this.RscContractRegisterDetailWorkUnitDTOSelected = RscContractRegisterDetailWorkUnitDTOSelected;
    }

    /**
     * @return the contractTenderDetail
     */
    public List<RscContractRegisterDetailWorkUnitDTO> getContractTenderDetail() {
        return contractTenderDetail;
    }

    /**
     * @param contractTenderDetail the contractTenderDetail to set
     */
    public void setContractTenderDetail(List<RscContractRegisterDetailWorkUnitDTO> contractTenderDetail) {
        this.contractTenderDetail = contractTenderDetail;
    }

    /**
     * @return the rscContractRegisterDetailWorkUnitService
     */
    public RscContractRegisterDetailWorkUnitService getRscContractRegisterDetailWorkUnitService() {
        return rscContractRegisterDetailWorkUnitService;
    }

    /**
     * @param rscContractRegisterDetailWorkUnitService the
     * rscContractRegisterDetailWorkUnitService to set
     */
    public void setRscContractRegisterDetailWorkUnitService(RscContractRegisterDetailWorkUnitService rscContractRegisterDetailWorkUnitService) {
        this.rscContractRegisterDetailWorkUnitService = rscContractRegisterDetailWorkUnitService;
    }

    /**
     * @return the organizationSiteService
     */
    public OrganizationSiteService getOrganizationSiteService() {
        return organizationSiteService;
    }

    /**
     * @param organizationSiteService the organizationSiteService to set
     */
    public void setOrganizationSiteService(OrganizationSiteService organizationSiteService) {
        this.organizationSiteService = organizationSiteService;
    }

    /**
     * @return the rscContractRegisterDTO
     */
    public RscContractRegisterDTO getRscContractRegisterDTO() {
        if (rscContractRegisterDTO == null) {
            rscContractRegisterDTO = new RscContractRegisterDTO();
        }
        return rscContractRegisterDTO;
    }

    /**
     * @param rscContractRegisterDTO the rscContractRegisterDTO to set
     */
    public void setRscContractRegisterDTO(RscContractRegisterDTO rscContractRegisterDTO) {
        this.rscContractRegisterDTO = rscContractRegisterDTO;
    }

    /**
     * @return the rscQotationPriceTenderDTOList
     */
    public List<RscQotationPriceTenderDTO> getRscQotationPriceTenderDTOList() {

        if (rscQotationPriceTenderDTOList == null || rscQotationPriceTenderDTOList.isEmpty()) {
            rscQotationPriceTenderDTOList = qotationPriceTenderService.getBranchRscQotationPriceTenderActiveByBranch(getUserData().getUserBranch().getId());
        }

        return rscQotationPriceTenderDTOList;
    }

    /**
     * @param rscQotationPriceTenderDTOList the rscQotationPriceTenderDTOList to
     * set
     */
    public void setRscQotationPriceTenderDTOList(List<RscQotationPriceTenderDTO> rscQotationPriceTenderDTOList) {
        this.rscQotationPriceTenderDTOList = rscQotationPriceTenderDTOList;
    }

    /**
     * @return the qotationPriceTenderService
     */
    public RscQotationPriceTenderService getQotationPriceTenderService() {
        return qotationPriceTenderService;
    }

    /**
     * @param qotationPriceTenderService the qotationPriceTenderService to set
     */
    public void setQotationPriceTenderService(RscQotationPriceTenderService qotationPriceTenderService) {
        this.qotationPriceTenderService = qotationPriceTenderService;
    }

    /**
     * @return the rscContractRegisterDTOtest
     */
    public RscContractRegisterDTO getRscContractRegisterDTOtest() {
        if (rscContractRegisterDTOtest == null) {
            rscContractRegisterDTOtest = new RscContractRegisterDTO();
        }
        return rscContractRegisterDTOtest;
    }

    /**
     * @param rscContractRegisterDTOtest the rscContractRegisterDTOtest to set
     */
    public void setRscContractRegisterDTOtest(RscContractRegisterDTO rscContractRegisterDTOtest) {
        this.rscContractRegisterDTOtest = rscContractRegisterDTOtest;
    }

    /**
     * @return the contractTotal
     */
    public BigDecimal getContractTotal() {
        return contractTotal;
    }

    /**
     * @param contractTotal the contractTotal to set
     */
    public void setContractTotal(BigDecimal contractTotal) {
        this.contractTotal = contractTotal;
    }

    /**
     * @return the initialValue
     */
    public BigDecimal getInitialValue() {
        return initialValue;
    }

    /**
     * @param initialValue the initialValue to set
     */
    public void setInitialValue(BigDecimal initialValue) {
        this.initialValue = initialValue;
    }

    /**
     * @return the initialPercentage
     */
    public BigDecimal getInitialPercentage() {
        return initialPercentage;
    }

    /**
     * @param initialPercentage the initialPercentage to set
     */
    public void setInitialPercentage(BigDecimal initialPercentage) {
        this.initialPercentage = initialPercentage;
    }

    /**
     * @return the rscContractRegisterCommon
     */
    public RscContractRegisterCommon getRscContractRegisterCommon() {
        if (rscContractRegisterCommon == null) {
            rscContractRegisterCommon = new RscContractRegisterCommon(rscContractRegisterService, getUserData());
        }
        return rscContractRegisterCommon;
    }

    /**
     * @param rscContractRegisterCommon the rscContractRegisterCommon to set
     */
    public void setRscContractRegisterCommon(RscContractRegisterCommon rscContractRegisterCommon) {
        this.rscContractRegisterCommon = rscContractRegisterCommon;
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

    /**
     * @return the selectTender
     */
    public Boolean getSelectTender() {
        return selectTender;
    }

    /**
     * @param selectTender the selectTender to set
     */
    public void setSelectTender(Boolean selectTender) {
        this.selectTender = selectTender;
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
