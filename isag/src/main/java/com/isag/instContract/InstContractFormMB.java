/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.instContract;

import com.isag.businessservice.InstContractDetailsService;
import com.isag.businessservice.InstContractService;
import com.isag.businessservice.InstProjectGuideService;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.instcontractdetailwithbanktransactionviewService;
import com.isag.businessservice.reports.searchBean.ProjectGuideSearchBean;
import com.isag.comparators.InstContractDetailsEntityComparator;
import com.isag.converter.InstProjectGuideConverter;
import com.isag.converter.InstProjectGuideDTOConverter;
import com.isag.converter.InvOrganizationSiteDTOConverter;
import com.isag.dto.InstProjectGuideDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.entity.InstContract;
import com.isag.entity.InstContractDetail;
import com.isag.entity.InstProjectGuide;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.IsagUser;
import com.isag.entiy.InstContractDetailsEntity;
import com.isag.entiy.InstContractEntity;
import com.isag.isag.AutoComplete;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
import com.isag.views.InstcontractdetailwithbanktransactionView;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author ahmed
 */
@ViewScoped
@Named(value = "instContractFormMB")
public class InstContractFormMB extends BaseFormBean {

    private List<InvOrganizationSiteDTO> invOrgSiteList;
    private InvOrganizationSiteDTOConverter invOrgSiteConverter;
    private List<InstProjectGuide> instProjectGuideList;
    private InstProjectGuideConverter instProjectGuideConverter;

    private List<InstProjectGuideDTO> instProjectGuideListSelected;
    private InstProjectGuideDTO instProjectGuideSelected;
    private List<InstProjectGuideDTO> instProjectGuideDTOList;
    private InstProjectGuideDTOConverter instProjectGuideDTOConverter;

    private InstContractEntity instContractEntity;
    private InstContract instContract;
    private List<InstContractDetail> instContractDetailsList;
    private List<InstContractDetailsEntity> instContractDetailsEntityList;
    private List<InstProjectGuideDTO> instProjectGuideDTOListDeleted;
    private InstContractDetailsEntity instContractDetailsEntitySelected;
    private InstContractDetailsEntity instContractDetailsEntityUnSelected;
    private boolean validateSave;
    private List<InstContractDetailsEntity> isntContractDetailDeletedEntityList;
    private Integer index = 0;
    private List<InstContractDetailsEntity> rowsDeleted;
    private String totalDetailValue;
    private BigDecimal totalValue;
    private Boolean Disabled = Boolean.FALSE;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private InstContractService instContractService;
    @EJB
    private InstContractDetailsService instContractDetailsService;
    @EJB
    private InstProjectGuideService instProjectGuideService;
    @EJB
    private instcontractdetailwithbanktransactionviewService instcontractdetail_banktransactionviewService;

    @Override
    @PostConstruct
    public void init() {
        try {

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
//            addNewDetailProjectGuide();
            load();
            reset();
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
                Integer instContractId = (Integer) context.getSessionMap().get("instContractEntityIdSelected");
                findHeadAndDetailsOfInstContract(instContractId);
//                getInstProjectGuideList().add(0, instContract.getProjectId());
//                getInstProjectGuideDTOList();
                findInstProjectGuideDTOList();
                getInstProjectGuideListSelected().addAll(instProjectGuideService.getAllInstProjectGuidesDTOByContractId(instContract.getId()));
                getInstProjectGuideDTOList().addAll(0, getInstProjectGuideListSelected());
//                List<InstProjectGuideDTO> newList = Stream.concat(getInstProjectGuideDTOList().stream(), listTwo.stream())
//                             .collect(Collectors.toList());
            }

        } catch (Exception e) {
            saveError(e, "InstContractFormMB", "init");
        }
    }

    public void findHeadAndDetailsOfInstContract(Integer instContractId) {
        instContract = instContractService.findInstContractById(instContractId);
        instContractDetailsList = instContractDetailsService.getAllInstContractDetailsByInstContractId(instContract.getId());
        instContractDetailsEntityList = new ArrayList<>();
        List<InstcontractdetailwithbanktransactionView> instcontractdetail_banktransactionview = new ArrayList<>();
        instcontractdetail_banktransactionview = instcontractdetail_banktransactionviewService.findInstContractDetailByBankTransaction(instContractId);
        setInstProjectGuideListSelected(instProjectGuideService.getAllInstProjectGuidesDTOByContractId(instContract.getId()));
        setInstProjectGuideDTOConverter(new InstProjectGuideDTOConverter(getInstProjectGuideListSelected()));
        if (!instcontractdetail_banktransactionview.isEmpty()) {
//            setDisabled(Boolean.TRUE);
        }
        mapInstContractToInstContractEntity();
    }

    public void mapInstContractToInstContractEntity() {
        try {

            index = 0;
            instContractEntity = new InstContractEntity();
            instContractEntity.setId(instContract.getId());
            instContractEntity.setDate(instContract.getDate());
            instContractEntity.setDaysNumber(instContract.getDaysNumber());
            instContractEntity.setInstProjectGuide(instContract.getProjectId());
            instContractEntity.setInstallmentNumber(instContract.getInstallmentNumber());
            instContractEntity.setInstallmentValue(instContract.getInstallmentValue());
            instContractEntity.setSerial(instContract.getSerial());
            if (instContract.getOrganizationSiteId() != null) {
                InvOrganizationSiteDTO siteDTO = new InvOrganizationSiteDTO();
                siteDTO.setId(instContract.getOrganizationSiteId().getId());
                siteDTO.setCode(instContract.getOrganizationSiteId().getCode());
                siteDTO.setName(instContract.getOrganizationSiteId().getName());
                instContractEntity.setInvOrganizationSite(siteDTO);
            }
            instContractEntity.setStartInstallment(instContract.getStartInstallment());
            instContractEntity.setInstProjectGuide(instContract.getProjectId());
            instContractEntity.setAdvancePayment(instContract.getAdvancePayment());
            instContractEntity.setDeliveryPayment(instContract.getDeliveryPayment());
            instContractEntity.setTotalValue(instContract.getTotalValue());
            instContractEntity.setCreationDate(instContract.getCreationDate());
            instContractEntity.setModificationDate(instContract.getModificationDate());
            instContractEntity.setCreatedBy(instContract.getCreatedBy() != null ? instContract.getCreatedBy().getId() : null);
            instContractEntity.setModifiedBy(instContract.getModifiedBy() != null ? instContract.getModifiedBy().getId() : null);
            instContractEntity.setBranchId(instContract.getBranchId() != null ? instContract.getBranchId().getId() : null);
            instContractEntity.setCompanyId(instContract.getCompanyId() != null ? instContract.getCompanyId().getId() : null);
            instContractEntity.setGlBank(instContract.getBankId());
            instContractEntity.setRemark(instContract.getRemark());
            instContractEntity.setMurabahaRate(instContract.getMurabahaRate());
            instContractEntity.setAmount(instContract.getAmount());
            instContractEntity.setDisabled(Disabled);
            instContractEntity.setCancel(instContract.isCancel());
            instContractEntity.setPostFlag(instContract.getPostFlag());
            instContractEntity.setStartDate(instContract.getStartDate());
            instContractEntity.setEndDate(instContract.getEndDate());

            for (InstContractDetail instContractDetail : instContractDetailsList) {
                ++index;
                InstContractDetailsEntity instContractDetailsEntity = new InstContractDetailsEntity();
                instContractDetailsEntity.setIndex(index);
                instContractDetailsEntity.setId(instContractDetail.getId());
                instContractDetailsEntity.setDueDate(instContractDetail.getDueDate());
                instContractDetailsEntity.setStatus(instContractDetail.getStatus());
                instContractDetailsEntity.setValue(instContractDetail.getValue());
                instContractDetailsEntity.setCreationDate(instContractDetail.getCreationDate());
                instContractDetailsEntity.setModificationDate(instContractDetail.getModificationDate());
                instContractDetailsEntity.setCreatedBy(instContractDetail.getCreatedBy() != null ? instContractDetail.getCreatedBy().getId() : null);
                instContractDetailsEntity.setModifiedBy(instContractDetail.getModifiedBy() != null ? instContractDetail.getModifiedBy().getId() : null);
                instContractDetailsEntity.setBranchId(instContractDetail.getBranchId() != null ? instContractDetail.getBranchId().getId() : null);
                instContractDetailsEntity.setCompanyId(instContractDetail.getCompanyId() != null ? instContractDetail.getCompanyId().getId() : null);
                instContractDetailsEntity.setSerial(instContractDetail.getSerial());
                instContractDetailsEntity.setRemark(instContractDetail.getRemark());
                instContractDetailsEntityList.add(instContractDetailsEntity);
            }
        } catch (Exception e) {
            saveError(e, "InstContractFormMB", "mapInstContractToInstContractEntity");
        }
        updateValueTotal();
    }

    @Override
    public void load() {
        try {
            getInvOrgSiteList();
            getInstProjectGuideList();
        } catch (Exception e) {
            saveError(e, "InstContractFormMB", "load");
        }
    }

    public void reset() {
        try {
            instContractEntity = new InstContractEntity();
            instContractEntity.setAmount(BigDecimal.ONE);
            instContractEntity.setMurabahaRate(BigDecimal.ZERO);
            instContractDetailsEntityList = new ArrayList<>();
            rowsDeleted = new ArrayList<>();
            totalValue = BigDecimal.ZERO;
        } catch (Exception e) {
            saveError(e, "InstContractFormMB", "reset");
        }

    }

    @Override
    public String save() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            validateSave = true;
            validateSave();
            if (!validateSave) {
                return "";
            }
            InstContract saveInstContract = new InstContract();
            if (instContractEntity.getId() == null) {
                saveInstContract.setCreationDate(new Date());
                saveInstContract.setCreatedBy(getUserData().getUser().getCreatedBy());
            } else {
                IsagUser user = new IsagUser();
                user.setId(instContractEntity.getCreatedBy());
                saveInstContract.setCreatedBy(user);
                saveInstContract.setModificationDate(new Date());
                saveInstContract.setModifiedBy(getUserData().getUser().getModifiedBy());
            }
            saveInstContract.setId(instContractEntity.getId());
            saveInstContract.setSerial(instContractEntity.getSerial());
            saveInstContract.setBranchId(getUserData().getUserBranch());
            saveInstContract.setCompanyId(getUserData().getUser().getCompanyId());
            saveInstContract.setAdvancePayment(instContractEntity.getAdvancePayment());
            saveInstContract.setInstallmentNumber(instContractEntity.getInstallmentNumber());
            saveInstContract.setDate(instContractEntity.getDate());
            saveInstContract.setDaysNumber(instContractEntity.getDaysNumber());
            saveInstContract.setInstallmentValue(instContractEntity.getInstallmentValue());
            saveInstContract.setTotalValue(instContractEntity.getTotalValue());
            saveInstContract.setStartDate(instContractEntity.getStartDate());
            saveInstContract.setEndDate(instContractEntity.getEndDate());
            if (instContractEntity.getInvOrganizationSite() != null) {
                InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
                invOrganizationSite.setId(instContractEntity.getInvOrganizationSite().getId());
                saveInstContract.setOrganizationSiteId(invOrganizationSite);
            }
            saveInstContract.setStartInstallment(instContractEntity.getStartInstallment());
            saveInstContract.setProjectId(instContractEntity.getInstProjectGuide());
            saveInstContract.setDeliveryPayment(instContractEntity.getDeliveryPayment());
            saveInstContract.setRemark(instContractEntity.getRemark());
            saveInstContract.setMurabahaRate(instContractEntity.getMurabahaRate());
            saveInstContract.setAmount(instContractEntity.getAmount());
            if (getUserData().getIsagUserBankList() != null && !getUserData().getIsagUserBankList().isEmpty() && instContractEntity.getGlBank() == null) {
                saveInstContract.setBankId(getUserData().getIsagUserBankList().get(0).getBankId());
            }
            List<InstContractDetail> saveInstContractDetailList = new ArrayList<>();
            if (instContractDetailsEntityList != null && !instContractDetailsEntityList.isEmpty()) {
                for (InstContractDetailsEntity instContractDetailsEntity : instContractDetailsEntityList) {
                    InstContractDetail instContractDetail = new InstContractDetail();
                    instContractDetail.setId(instContractDetailsEntity.getId());
                    instContractDetail.setDueDate(instContractDetailsEntity.getDueDate());
                    instContractDetail.setStatus(instContractDetailsEntity.getStatus());
                    instContractDetail.setValue(instContractDetailsEntity.getValue());
                    instContractDetail.setBranchId(getUserData().getUserBranch());
                    instContractDetail.setCompanyId(getUserData().getUser().getCompanyId());
                    instContractDetail.setSerial(instContractDetailsEntity.getSerial());
                    instContractDetail.setRemark(instContractDetailsEntity.getRemark());
                    if (instContractDetailsEntity.getId() == null) {
                        instContractDetail.setCreationDate(new Date());
                        instContractDetail.setCreatedBy(getUserData().getUser().getCreatedBy());
                    } else {
                        instContractDetail.setCreationDate(instContractDetailsEntity.getCreationDate());
                        IsagUser user = new IsagUser();
                        user.setId(instContractDetailsEntity.getCreatedBy());
                        instContractDetail.setCreatedBy(user);
                        instContractDetail.setModificationDate(new Date());
                        instContractDetail.setModifiedBy(getUserData().getUser().getModifiedBy());
                    }
                    saveInstContractDetailList.add(instContractDetail);
                }

            }

            List<InstContractDetail> instContractDetailWillDeletedList = new ArrayList<>();
            if (isntContractDetailDeletedEntityList != null && !isntContractDetailDeletedEntityList.isEmpty()) {
                for (InstContractDetailsEntity upDetailEntity : isntContractDetailDeletedEntityList) {
                    InstContractDetail instContractDetail = new InstContractDetail();
                    instContractDetail.setId(upDetailEntity.getId());
                    instContractDetailWillDeletedList.add(instContractDetail);
                }
            }

            try {
//                instContract = instContractService.addInstContract(saveInstContract, saveInstContractDetailList, instContractDetailWillDeletedList);
                instContract = instContractService.addInstContract(saveInstContract, saveInstContractDetailList, instContractDetailWillDeletedList, getInstProjectGuideListSelected(), getUserlogin());

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("SAVED")));
                exit("../instContract/instContractList.xhtml");
                //findHeadAndDetailsOfInstContract(instContract.getId());
            } catch (Exception e) {

            }
        } catch (Exception e) {
            saveError(e, "InstContractFormMB", "save");
        }
        return "";
    }

    public void validateSave() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            if (validateCalculation()) {
//                if (instContractEntity.getInstProjectGuide() == null || (instContractEntity.getInstProjectGuide().getPrice() != null && instContractEntity.getInstProjectGuide().getPrice().compareTo(BigDecimal.ZERO) == 0)) {
//                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب اختيار سلعة او منتج لها قيمة"));
//                    validateSave = false;
//                } else 
                if (instContractEntity.getAmount() == null || instContractEntity.getAmount().compareTo(BigDecimal.ZERO) != 1) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب وضع عدد أكبر من الصفر"));
                    validateSave = false;
                } else if (instContractEntity.getInvOrganizationSite() == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب اختيار جهة تعامل"));
                    validateSave = false;
                } else if (instContractDetailsEntityList == null || (instContractDetailsEntityList != null && instContractDetailsEntityList.isEmpty())) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "لا توجد اقساط"));
                    validateSave = false;
                } else {
                    BigDecimal advancedPayment = instContractEntity.getAdvancePayment() != null ? instContractEntity.getAdvancePayment() : BigDecimal.ZERO;
                    BigDecimal installmentValue = instContractEntity.getInstallmentValue() != null ? instContractEntity.getInstallmentValue() : BigDecimal.ZERO;
                    BigDecimal delivaryPayment = instContractEntity.getDeliveryPayment() != null ? instContractEntity.getDeliveryPayment() : BigDecimal.ZERO;
//                BigDecimal totalAdvancedAndInstallment = advancedPayment.add(installmentValue).add(delivaryPayment);
                    if (installmentValue.compareTo(totalValue) != 0) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "اجمالي الاقساط لا يساوي اجمالي الدفعة المقدمة مضاف اليه دفعة عند الاستلام مضاف اليه مبلغ التقسيط"));
                        validateSave = false;
                    }
                    /* if (totalValue.compareTo(instContractEntity.getTotalValue()) != 0) {
                     FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "اجمالي الاقساط لا يساوي اجمالي المبلغ"));
                     validateSave = false;
                     }*/
                }
            }
        } catch (Exception e) {
            saveError(e, "InstContractFormMB", "validateSave");
        }
    }

    public void calculateInstallment() {
        try {
            isntContractDetailDeletedEntityList = instContractDetailsEntityList;
            instContractDetailsEntityList = new ArrayList<>();
            BigDecimal advancedPayment = instContractEntity.getAdvancePayment() != null ? instContractEntity.getAdvancePayment() : BigDecimal.ZERO;
            //BigDecimal installmentValue = instContractEntity.getInstallmentValue() != null ? instContractEntity.getInstallmentValue() : BigDecimal.ZERO;
            BigDecimal valuePerMonth = BigDecimal.ZERO;
            //BigDecimal daysNumber = instContractEntity.getDaysNumber();

            if (validateCalculation()) {
                BigDecimal installmentValue = instContractEntity.getInstallmentValue() != null ? instContractEntity.getInstallmentValue() : BigDecimal.ZERO;

                //instContractEntity.setTotalValue(advancedPayment.add(installmentValue));
                valuePerMonth = installmentValue.divide(BigDecimal.valueOf(instContractEntity.getInstallmentNumber()), 4, 4);
                valuePerMonth = valuePerMonth.setScale(2, BigDecimal.ROUND_UP);
                Calendar c = Calendar.getInstance();
                c.setTime(instContractEntity.getStartInstallment());
                Date dueDate = instContractEntity.getStartInstallment();
                index = 0;
                for (int i = 0; i < instContractEntity.getInstallmentNumber(); i++) {
                    InstContractDetailsEntity detailsEntity = new InstContractDetailsEntity();
                    detailsEntity.setValue(valuePerMonth);
                    detailsEntity.setDueDate(dueDate);
                    detailsEntity.setSerial(i + 1);
                    detailsEntity.setIndex(++index);
                    c.add(Calendar.MONTH, instContractEntity.getDaysNumber());
                    dueDate = c.getTime();
                    instContractDetailsEntityList.add(detailsEntity);
                }
            }
        } catch (Exception e) {
            saveError(e, "InstContractFormMB", "calculateInstallment");
        }
        updateValueTotal();
    }

    public boolean validateCalculation() {

        Map<String, String> userDDs = getUserData().getUserDDs();

//        if (instContractEntity.getInstallmentValue() == null || (instContractEntity.getInstallmentValue() != null && (instContractEntity.getInstallmentValue().compareTo(BigDecimal.ZERO) == 0 || instContractEntity.getInstallmentValue().compareTo(BigDecimal.ZERO) == -1))) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب ادخال قيم تقسيط صحيحة"));
//            validateSave = false;
//            return false;
//        } else 
        if ((instContractEntity.getDeliveryPayment() != null && instContractEntity.getDeliveryPayment().compareTo(BigDecimal.ZERO) == -1)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب ادخال قيم الدفعة عند الاستلام بشكل صحيح"));
            validateSave = false;
            return false;
        } else if (instContractEntity.getInstallmentNumber() == null || (instContractEntity.getInstallmentNumber() != null && (instContractEntity.getInstallmentNumber().compareTo(0) == 0 || instContractEntity.getInstallmentNumber().compareTo(0) == -1))) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب ادخال عدد الاقساط"));
            validateSave = false;
            return false;
        } else if (instContractEntity.getDaysNumber() == null || (instContractEntity.getDaysNumber() != null && (instContractEntity.getDaysNumber().compareTo(0) == 0 || instContractEntity.getDaysNumber().compareTo(0) == -1))) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب اختيار عدد الشهور"));
            validateSave = false;
            return false;
        } else if (instContractEntity.getStartInstallment() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب اختيار تاريخ بداية الاقساط"));
            validateSave = false;
            return false;
        } else if (instContractEntity != null) {
            BigDecimal instValue = calcInstallmentValue();
            if (instValue.compareTo(BigDecimal.ZERO) == 0 || instValue.compareTo(BigDecimal.ZERO) == -1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب ادخال قيم تقسيط صحيحة"));
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }

    }

    public BigDecimal calcInstallmentValue() {
        BigDecimal instValue = instContractEntity.getInstallmentValue() != null ? instContractEntity.getInstallmentValue() : BigDecimal.ZERO;
        BigDecimal twoPaymentsSum = (instContractEntity.getAdvancePayment() != null ? instContractEntity.getAdvancePayment() : BigDecimal.ZERO).add(instContractEntity.getDeliveryPayment() != null ? instContractEntity.getDeliveryPayment() : BigDecimal.ZERO);
        instValue = (instContractEntity.getTotalValue() != null ? instContractEntity.getTotalValue() : BigDecimal.ZERO).subtract(twoPaymentsSum);
        instContractEntity.setInstallmentValue(instValue);
        return instValue;
    }
    
    public void addNewDetailProjectGuide() {
        try {

            InstProjectGuideDTO detailsEntity = new InstProjectGuideDTO();
            detailsEntity.setMarkEdit(Boolean.TRUE);
            detailsEntity.setIndex(++index);
            instProjectGuideSelected = detailsEntity;
            getInstProjectGuideListSelected().add(detailsEntity);
        } catch (Exception e) {
            saveError(e, "InstContractFormMB", "addNewDetail");
        }

    }


    public void addNewDetail() {
        try {

            if (instContractDetailsEntityList != null && !instContractDetailsEntityList.isEmpty()) {
                for (InstContractDetailsEntity entity : instContractDetailsEntityList) {
                    entity.setMarkEdit(Boolean.FALSE);
                }
            }

            InstContractDetailsEntity detailsEntity = new InstContractDetailsEntity();
            detailsEntity.setMarkEdit(Boolean.TRUE);
            detailsEntity.setIndex(++index);
            instContractDetailsEntitySelected = detailsEntity;
            instContractDetailsEntityList.add(detailsEntity);
        } catch (Exception e) {
            saveError(e, "InstContractFormMB", "addNewDetail");
        }

    }

    public void onRowSelectDetails(SelectEvent e) {

        if (instContractDetailsEntityUnSelected != null && !instContractDetailsEntityUnSelected.equals(instContractDetailsEntitySelected)) {
            instContractDetailsEntityUnSelected.setMarkEdit(false);
            instContractDetailsEntitySelected.setMarkEdit(true);
        } else if (instContractDetailsEntityUnSelected != null && instContractDetailsEntityUnSelected.equals(instContractDetailsEntitySelected) && instContractDetailsEntitySelected.getMarkEdit() != null && instContractDetailsEntitySelected.getMarkEdit()) {
            instContractDetailsEntitySelected.setMarkEdit(false);
        } else if (instContractDetailsEntityUnSelected != null && instContractDetailsEntityUnSelected.equals(instContractDetailsEntitySelected) && instContractDetailsEntitySelected.getMarkEdit() != null && !instContractDetailsEntitySelected.getMarkEdit()) {
            instContractDetailsEntitySelected.setMarkEdit(true);
        } else {
            instContractDetailsEntitySelected.setMarkEdit(true);
        }
        instContractDetailsEntityUnSelected = instContractDetailsEntitySelected;

        e.getSource();

    }

    public void deleteDetailProjectGuide() {
        try {

            if (instProjectGuideSelected.getId() != null) {
                getInstProjectGuideDTOListDeleted().add(instProjectGuideSelected);
            }
            instProjectGuideListSelected.remove(instProjectGuideSelected);
        } catch (Exception e) {
            saveError(e, "InstContractFormMB", "deleteDetail");
        }
    }
    
    public void deleteDetail() {
        try {

            if (instContractDetailsEntitySelected.getId() != null) {
                getIsntContractDetailDeletedEntityList().add(instContractDetailsEntitySelected);
            }
            instContractDetailsEntityList.remove(instContractDetailsEntitySelected);
            updateValueTotal();
        } catch (Exception e) {
            saveError(e, "InstContractFormMB", "deleteDetail");
        }
    }

    public void updateValueTotal() {
        try {
            totalDetailValue = new String();
            if (instContractDetailsEntityList != null && !instContractDetailsEntityList.isEmpty()) {
                totalValue = BigDecimal.ZERO;
                for (InstContractDetailsEntity entity : instContractDetailsEntityList) {
                    totalValue = totalValue.add(entity.getValue() != null ? entity.getValue() : BigDecimal.ZERO);
                }
                DecimalFormat df = new DecimalFormat("#,##0.00");
                totalDetailValue = df.format(totalValue.setScale(2, RoundingMode.HALF_UP));
            }
        } catch (Exception e) {
            saveError(e, "InstContractFormMB", "updateValueTotal");
        }
    }

    public void updateListDetailsOrder() {
        try {
            index = 0;
            if (instContractDetailsEntityList != null && !instContractDetailsEntityList.isEmpty()) {
                instContractDetailsEntityList.sort(new InstContractDetailsEntityComparator());
                for (InstContractDetailsEntity entity : instContractDetailsEntityList) {
                    entity.setMarkEdit(Boolean.FALSE);
                    entity.setIndex(index++);
                    entity.setSerial(index);
                }

            }
            instContractDetailsEntitySelected.setMarkEdit(Boolean.TRUE);
        } catch (Exception e) {
            saveError(e, "InstContractFormMB", "updateListDetailsOrder");
        }
    }

    public void updateTotalValue() {
        try {
            instContractEntity.getTotalValue();
            if (instContractEntity.getAmount() != null && instContractEntity.getAmount().compareTo(BigDecimal.ZERO) == 1 && instContractEntity.getInstProjectGuide() != null && instContractEntity.getInstProjectGuide().getPrice() != null) {
                BigDecimal murabahaRateValue = instContractEntity.getMurabahaRate() != null ? instContractEntity.getMurabahaRate() : BigDecimal.ZERO;
                BigDecimal totalPrice = instContractEntity.getInstProjectGuide().getPrice().multiply(instContractEntity.getAmount());
                BigDecimal murabahaValue = totalPrice.multiply(murabahaRateValue).divide(BigDecimal.valueOf(100));
                BigDecimal totlValue = totalPrice.add(murabahaValue);
                instContractEntity.setTotalValue(totlValue);
                calcInstallmentValue();
            }
        } catch (Exception e) {
            saveError(e, "InstContractFormMB", "updateTotalValue");
        }
    }

    public void updateTotalValueMultiProject() {
        try {
            BigDecimal total = BigDecimal.ZERO;
            for (InstProjectGuideDTO guideDTO : getInstProjectGuideListSelected()) {
                total = total.add(guideDTO.getPrice());
            }
            instContractEntity.setTotalValue(total);
            calcInstallmentValue();
        } catch (Exception e) {
            saveError(e, "InstContractFormMB", "updateTotalValue");
        }
    }

    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("PrintedBy", getUserData().getUser().getName());
            hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
            hashMap.put("companyName", getUserData().getCompany().getName());
            hashMap.put("CreatedBy", getUserData().getUser().getName());

            hashMap.put("serial", getInstContractEntity().getSerial());
            hashMap.put("customerName", getInstContractEntity().getInvOrganizationSite().getName());
            hashMap.put("projectName", getInstContractEntity().getInstProjectGuide().getName());
            hashMap.put("startInstallment", getInstContractEntity().getStartInstallment());
            hashMap.put("date", getInstContractEntity().getDate());
            hashMap.put("advancePayment", getInstContractEntity().getAdvancePayment());
            hashMap.put("deliveryPayment", getInstContractEntity().getDeliveryPayment());
            hashMap.put("amount", getInstContractEntity().getAmount());
            hashMap.put("murabahaRate", getInstContractEntity().getMurabahaRate());
            hashMap.put("installmentValue", getInstContractEntity().getInstallmentValue());
            hashMap.put("installmentNumber", getInstContractEntity().getInstallmentNumber());
            hashMap.put("totalValue", getInstContractEntity().getTotalValue());
            hashMap.put("daysNumber", getInstContractEntity().getDaysNumber());
            hashMap.put("remark", getInstContractEntity().getRemark());
            hashMap.put("orignelAmount", getInstContractEntity().getOrignelAmount());
            hashMap.put("totalAmount", getInstContractEntity().getTotalValue());

            hashMap.put("serialText", "رقم العقد");
            hashMap.put("customerNameText", "اسم العميل");
            hashMap.put("projectNameText", "المشروع");
            hashMap.put("startInstallmentText", "تاريخ بداية الاقساط");
            hashMap.put("dateText", "تاريخ العقد");
            hashMap.put("advancePaymentText", "دفعة مقدمة");
            hashMap.put("deliveryPaymentText", "دفعة عند الاستلام");
            hashMap.put("amountText", "العدد");
            hashMap.put("murabahaRateText", "نسبة الربح");
            hashMap.put("installmentValueText", "مبلغ التقسيط");
            hashMap.put("installmentNumberText", "عدد الاقساط");
            hashMap.put("totalValueText", "اجمالي المبلغ");
            hashMap.put("daysNumberText", "عدد الشهور");
            hashMap.put("remarkText", "ملاحظات");
            hashMap.put("orignelAmountText", "المبلغ الاصلي");
            hashMap.put("totalAmountText", "الإجمالى");

//            fillReport(hashMap, getUserData().getReportPath() + "emptyinitialsalecontract.jasper", instContractDetailsEntityList, "pdf");
            fillReport(hashMap, getUserData().getReportPath() + "installment.jasper", instContractDetailsEntityList, "pdf");

        } catch (Exception e) {
            saveError(e, "InstContractFormMB", "exportPDF");
        }
    }

    public String exit() {
        try {
            exit("../instContract/instContractList.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "InstContractFormMB", "exit");
            return null;
        }
    }

    public <T> void fillReport(HashMap hashMap, String reportPath, List<T> listBean, String exportType) throws JRException, IOException {
        try {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listBean);
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, hashMap, beanCollectionDataSource);
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            if ("pdf".equals(exportType)) {
                JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            } else if ("excel".equals(exportType)) {

            } else if ("html".equals(exportType)) {

            }
        } catch (Exception e) {
            saveError(e, "InstContractFormMB", "fillReport");
        }

    }

    @Override
    public String getScreenName() {

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<InvOrganizationSiteDTO> completOrgSite(String query) {

        return AutoComplete.completOrgSiteDTO(query, this.invOrgSiteList, invOrgSiteConverter);

    }

    public List<InstProjectGuide> completeInstProject(String query) {

        return AutoComplete.completeInstProject(query, this.instProjectGuideList, instProjectGuideConverter);

    }

    /**
     * @return the invOrgSiteList
     */
    public List<InvOrganizationSiteDTO> getInvOrgSiteList() {
        if (invOrgSiteList == null || invOrgSiteList.isEmpty()) {
            invOrgSiteList = organizationSiteService.getorganizationSiteByBranchIdDTO(getUserData().getUserBranch() != null ? getUserData().getUserBranch().getId() : null, true, 0);
            invOrgSiteConverter = new InvOrganizationSiteDTOConverter(invOrgSiteList);
        }
        return invOrgSiteList;
    }

    /**
     * @param invOrgSiteList the invOrgSiteList to set
     */
    public void setInvOrgSiteList(List<InvOrganizationSiteDTO> invOrgSiteList) {
        this.invOrgSiteList = invOrgSiteList;
    }

    /**
     * @return the invOrgSiteConverter
     */
    public InvOrganizationSiteDTOConverter getInvOrgSiteConverter() {
        return invOrgSiteConverter;
    }

    /**
     * @param invOrgSiteConverter the invOrgSiteConverter to set
     */
    public void setInvOrgSiteConverter(InvOrganizationSiteDTOConverter invOrgSiteConverter) {
        this.invOrgSiteConverter = invOrgSiteConverter;
    }

    /**
     * @return the instContractEntity
     */
    public InstContractEntity getInstContractEntity() {
        return instContractEntity;
    }

    /**
     * @param instContractEntity the instContractEntity to set
     */
    public void setInstContractEntity(InstContractEntity instContractEntity) {
        this.instContractEntity = instContractEntity;
    }

    /**
     * @return the instContract
     */
    public InstContract getInstContract() {
        return instContract;
    }

    /**
     * @param instContract the instContract to set
     */
    public void setInstContract(InstContract instContract) {
        this.instContract = instContract;
    }

    /**
     * @return the instContractDetailsList
     */
    public List<InstContractDetail> getInstContractDetailsList() {
        return instContractDetailsList;
    }

    /**
     * @param instContractDetailsList the instContractDetailsList to set
     */
    public void setInstContractDetailsList(List<InstContractDetail> instContractDetailsList) {
        this.instContractDetailsList = instContractDetailsList;
    }

    /**
     * @return the instContractDetailsEntityList
     */
    public List<InstContractDetailsEntity> getInstContractDetailsEntityList() {
        return instContractDetailsEntityList;
    }

    /**
     * @param instContractDetailsEntityList the instContractDetailsEntityList to
     * set
     */
    public void setInstContractDetailsEntityList(List<InstContractDetailsEntity> instContractDetailsEntityList) {
        this.instContractDetailsEntityList = instContractDetailsEntityList;
    }

    /**
     * @return the validateSave
     */
    public boolean isValidateSave() {
        return validateSave;
    }

    /**
     * @param validateSave the validateSave to set
     */
    public void setValidateSave(boolean validateSave) {
        this.validateSave = validateSave;
    }

    /**
     * @return the isntContractDetailDeletedEntityList
     */
    public List<InstContractDetailsEntity> getIsntContractDetailDeletedEntityList() {
        if (isntContractDetailDeletedEntityList == null || isntContractDetailDeletedEntityList.isEmpty()) {
            isntContractDetailDeletedEntityList = new ArrayList<>();
        }
        return isntContractDetailDeletedEntityList;
    }

    /**
     * @param isntContractDetailDeletedEntityList the
     * isntContractDetailDeletedEntityList to set
     */
    public void setIsntContractDetailDeletedEntityList(List<InstContractDetailsEntity> isntContractDetailDeletedEntityList) {
        this.isntContractDetailDeletedEntityList = isntContractDetailDeletedEntityList;
    }

    /**
     * @return the instProjectGuideConverter
     */
    public InstProjectGuideConverter getInstProjectGuideConverter() {
        return instProjectGuideConverter;
    }

    /**
     * @param instProjectGuideConverter the instProjectGuideConverter to set
     */
    public void setInstProjectGuideConverter(InstProjectGuideConverter instProjectGuideConverter) {
        this.instProjectGuideConverter = instProjectGuideConverter;
    }

    /**
     * @return the instProjectGuideList
     */
    public List<InstProjectGuide> getInstProjectGuideList() {
        if (instProjectGuideList == null || instProjectGuideList.isEmpty()) {
            instProjectGuideList = instProjectGuideService.getAllSubInstProjectGuideByBranchIdActive(getUserData().getUserBranch().getId());
            instProjectGuideConverter = new InstProjectGuideConverter(instProjectGuideList);
        }
        return instProjectGuideList;
    }

    /**
     * @param instProjectGuideList the instProjectGuideList to set
     */
    public void setInstProjectGuideList(List<InstProjectGuide> instProjectGuideList) {
        this.instProjectGuideList = instProjectGuideList;
    }

    /**
     * @return the instContractDetailsEntitySelected
     */
    public InstContractDetailsEntity getInstContractDetailsEntitySelected() {
        return instContractDetailsEntitySelected;
    }

    /**
     * @param instContractDetailsEntitySelected the
     * instContractDetailsEntitySelected to set
     */
    public void setInstContractDetailsEntitySelected(InstContractDetailsEntity instContractDetailsEntitySelected) {
        this.instContractDetailsEntitySelected = instContractDetailsEntitySelected;
    }

    /**
     * @return the index
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * @return the instContractDetailsEntityUnSelected
     */
    public InstContractDetailsEntity getInstContractDetailsEntityUnSelected() {
        return instContractDetailsEntityUnSelected;
    }

    /**
     * @param instContractDetailsEntityUnSelected the
     * instContractDetailsEntityUnSelected to set
     */
    public void setInstContractDetailsEntityUnSelected(InstContractDetailsEntity instContractDetailsEntityUnSelected) {
        this.instContractDetailsEntityUnSelected = instContractDetailsEntityUnSelected;
    }

    /**
     * @return the rowsDeleted
     */
    public List<InstContractDetailsEntity> getRowsDeleted() {
        return rowsDeleted;
    }

    /**
     * @param rowsDeleted the rowsDeleted to set
     */
    public void setRowsDeleted(List<InstContractDetailsEntity> rowsDeleted) {
        this.rowsDeleted = rowsDeleted;
    }

    /**
     * @return the totalDetailValue
     */
    public String getTotalDetailValue() {
        return totalDetailValue;
    }

    /**
     * @param totalDetailValue the totalDetailValue to set
     */
    public void setTotalDetailValue(String totalDetailValue) {
        this.totalDetailValue = totalDetailValue;
    }

    /**
     * @return the totalValue
     */
    public BigDecimal getTotalValue() {
        return totalValue;
    }

    /**
     * @param totalValue the totalValue to set
     */
    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    /**
     * @return the Disabled
     */
    public Boolean getDisabled() {
        return Disabled;
    }

    /**
     * @param Disabled the Disabled to set
     */
    public void setDisabled(Boolean Disabled) {
        this.Disabled = Disabled;
    }

    /**
     * @return the instProjectGuideListSelected
     */
    public List<InstProjectGuideDTO> getInstProjectGuideListSelected() {
        if(instProjectGuideListSelected == null){
            instProjectGuideListSelected = new ArrayList<>();
        }
        return instProjectGuideListSelected;
    }

    /**
     * @param instProjectGuideListSelected the instProjectGuideListSelected to
     * set
     */
    public void setInstProjectGuideListSelected(List<InstProjectGuideDTO> instProjectGuideListSelected) {
        this.instProjectGuideListSelected = instProjectGuideListSelected;
    }

    /**
     * @return the instProjectGuideDTOList
     */
    public List<InstProjectGuideDTO> getInstProjectGuideDTOList() {
//        if (getInstContractEntity().getStartDate() != null && getInstContractEntity().getEndDate() != null) {
//            ProjectGuideSearchBean projectGuideSearchBean = new  ProjectGuideSearchBean();
//            projectGuideSearchBean.setBranchId(getUserData().getUserBranch().getId());
//            projectGuideSearchBean.setStartDate(instContractEntity.getStartDate());
//            projectGuideSearchBean.setEndDate(instContractEntity.getEndDate());
//            
//            instProjectGuideDTOList = instProjectGuideService.getBranchInstProjectGuidesActiveDTO(projectGuideSearchBean,Boolean.FALSE);
//            instProjectGuideDTOConverter = new InstProjectGuideDTOConverter(instProjectGuideDTOList);
//        }
        if (instProjectGuideDTOList == null) {
            instProjectGuideDTOList = new ArrayList<>();
        }
//        else {
//            if (getInstProjectGuideListSelected() != null) {
//                instProjectGuideDTOList.addAll(0, getInstProjectGuideListSelected());
//            }
//        }
        return instProjectGuideDTOList;
    }

    public List<InstProjectGuideDTO> findInstProjectGuideDTOList() {
        if (getInstContractEntity().getStartDate() != null && getInstContractEntity().getEndDate() != null) {
            ProjectGuideSearchBean projectGuideSearchBean = new  ProjectGuideSearchBean();
            projectGuideSearchBean.setBranchId(getUserData().getUserBranch().getId());
            projectGuideSearchBean.setStartDate(instContractEntity.getStartDate());
            projectGuideSearchBean.setEndDate(instContractEntity.getEndDate());
            
            instProjectGuideDTOList = instProjectGuideService.getBranchInstProjectGuidesActiveDTO(projectGuideSearchBean,Boolean.FALSE);
            instProjectGuideDTOConverter = new InstProjectGuideDTOConverter(instProjectGuideDTOList);
            setInstProjectGuideListSelected(null);
        }
        return instProjectGuideDTOList;
    }

    /**
     * @param instProjectGuideDTOList the instProjectGuideDTOList to set
     */
    public void setInstProjectGuideDTOList(List<InstProjectGuideDTO> instProjectGuideDTOList) {
        this.instProjectGuideDTOList = instProjectGuideDTOList;
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
     * @return the instProjectGuideSelected
     */
    public InstProjectGuideDTO getInstProjectGuideSelected() {
        return instProjectGuideSelected;
    }

    /**
     * @param instProjectGuideSelected the instProjectGuideSelected to set
     */
    public void setInstProjectGuideSelected(InstProjectGuideDTO instProjectGuideSelected) {
        this.instProjectGuideSelected = instProjectGuideSelected;
    }

    /**
     * @return the instProjectGuideDTOListDeleted
     */
    public List<InstProjectGuideDTO> getInstProjectGuideDTOListDeleted() {
        if(instProjectGuideDTOListDeleted == null){
            instProjectGuideDTOListDeleted = new ArrayList<>();
        }
        return instProjectGuideDTOListDeleted;
    }

    /**
     * @param instProjectGuideDTOListDeleted the instProjectGuideDTOListDeleted to set
     */
    public void setInstProjectGuideDTOListDeleted(List<InstProjectGuideDTO> instProjectGuideDTOListDeleted) {
        this.instProjectGuideDTOListDeleted = instProjectGuideDTOListDeleted;
    }

}
