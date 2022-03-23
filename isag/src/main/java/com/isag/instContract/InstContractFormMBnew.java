/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.instContract;

import com.isag.businessservice.GlBankTransactionService;
import com.isag.businessservice.InstContractDetailsService;
import com.isag.businessservice.InstContractService;
import com.isag.businessservice.InstContractServicenew;
import com.isag.businessservice.InstProfitRateService;
import com.isag.businessservice.InstProjectGuideService;
import com.isag.businessservice.InstPurchaseInvoiceContractDifrranceViewService;
import com.isag.businessservice.InvNoticeService;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.instcontractdetailwithbanktransactionviewService;
import com.isag.comparators.InstContractDetailsEntityComparator;
import com.isag.converter.InstProjectGuideConverter;
import com.isag.converter.InvOrganizationSiteDTOConverter;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.SymbolDTO;
import com.isag.entity.InstContract;
import com.isag.entity.InstContractDetail;
import com.isag.entity.InstProjectGuide;
import com.isag.entity.InvItem;
import com.isag.entity.InvOrganizationSite;
import com.isag.entiy.InstContractDetailsEntity;
import com.isag.entiy.InstContractEntity;
import com.isag.isag.AutoComplete;
import com.isag.isag.InstContractDataForm;
import com.isag.isag.UserData;
import com.isag.views.InstPurchaseInvoiceContractDifrranceView;
import com.isag.views.InstcontractdetailwithbanktransactionView;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.event.SelectEvent;
import tafqeet.Tafqeet;

/**
 *
 * @author AhmedEssam
 */
@ViewScoped
@Named(value = "instContractFormMBnew")
public class InstContractFormMBnew extends InstContractDataForm {

    private List<InvOrganizationSiteDTO> invOrgSiteList;
    private InvOrganizationSiteDTOConverter invOrgSiteConverter;
    private List<InstProjectGuide> instProjectGuideList;
    private InstProjectGuideConverter instProjectGuideConverter;
    private InstContractEntity instContractEntity;
    private InstContract instContract;
    private List<InstContractDetail> instContractDetailsList;
    private List<InstContractDetailsEntity> instContractDetailsEntityList;
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
    private InstPurchaseInvoiceContractDifrranceViewService instPurchaseInvoiceContractDifrranceViewService;
    @EJB
    private InstContractServicenew instContractServicenew;
    @EJB
    private InstProfitRateService instProfitRateService;
    @EJB
    private instcontractdetailwithbanktransactionviewService instcontractdetail_banktransactionviewService;

    @EJB
    private GlBankTransactionService bankTransactionService;
    @EJB
    private InvNoticeService invNoticeService;

    @Override
    @PostConstruct
    public void init() {
        try {

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));

            load();
            reset();
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
                Integer instContractId = (Integer) context.getSessionMap().get("instContractEntityIdSelected");
                findHeadAndDetailsOfInstContract(instContractId);
                getInstProjectGuideList().add(0, instContract.getProjectId());
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
        if (!instcontractdetail_banktransactionview.isEmpty()) {
            setDisabled(Boolean.TRUE);
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
            if (instContract.getOrganizationSiteId() != null) {
                InvOrganizationSiteDTO siteDTO = new InvOrganizationSiteDTO();
                siteDTO.setId(instContract.getOrganizationSiteId().getId());
                siteDTO.setCode(instContract.getOrganizationSiteId().getCode());
                siteDTO.setName(instContract.getOrganizationSiteId().getName());
                siteDTO.setMobile(instContract.getOrganizationSiteId().getMobile());
                siteDTO.setNationId(instContract.getOrganizationSiteId().getNationId());
                siteDTO.setAddress(instContract.getOrganizationSiteId().getAddress());
                siteDTO.setLegalAffairsRemark(instContract.getOrganizationSiteId().getLegalAffairsRemark());
                if (instContract.getOrganizationSiteId().getWorkTypeId() != null) {
                    SymbolDTO symbolDTO = new SymbolDTO();
                    symbolDTO.setName(instContract.getOrganizationSiteId().getWorkTypeId().getName());
                    siteDTO.setWorkTypeId(symbolDTO);
                }
                siteDTO.setSponsorName1(instContract.getOrganizationSiteId().getSponsorName1());
                siteDTO.setSponsorName2(instContract.getOrganizationSiteId().getSponsorName2());
                siteDTO.setSponsorPhone1(instContract.getOrganizationSiteId().getSponsorPhone1());
                siteDTO.setSponsorPhone2(instContract.getOrganizationSiteId().getSponsorPhone2());
                instContractEntity.setInvOrganizationSite(siteDTO);
            }

            instContractEntity.setStartInstallment(instContract.getStartInstallment());
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
            instContractEntity.setSerial(instContract.getSerial());

            InstPurchaseInvoiceContractDifrranceView invoiceContractDifrranceView = null;
            if (instContract.getProjectId() != null) {
                instContract.setInvItemId(null);
                InstPurchaseInvoiceContractDifrranceView contractDifrranceView = instPurchaseInvoiceContractDifrranceViewService.getAllInstPurchaseInvoiceContractDifrranceByType(getUserlogin().getBranchId().getId(), instContract.getProjectId().getId(), 1);
                if (contractDifrranceView != null) {
                    getInstPurchaseInvoiceContractDifrranceViewList().remove(contractDifrranceView);
                    contractDifrranceView.setQuantity(contractDifrranceView.getQuantity().add(instContract.getAmount()));
                    invoiceContractDifrranceView = contractDifrranceView;

                } else {
                    contractDifrranceView = new InstPurchaseInvoiceContractDifrranceView();
                    contractDifrranceView.setCode(instContract.getProjectId().getCode());
                    contractDifrranceView.setName(instContract.getProjectId().getName());
                    contractDifrranceView.setId(instContract.getProjectId().getId());
                    contractDifrranceView.setBranchId(instContract.getProjectId().getBranchId().getId());
                    contractDifrranceView.setPrice(instContract.getProjectId().getPrice());
                    contractDifrranceView.setQuantity(instContract.getAmount());
                    contractDifrranceView.setTableType(1);
                    invoiceContractDifrranceView = contractDifrranceView;
                }

            } else if (instContract.getInvItemId() != null) {
                instContract.setProjectId(null);
                InstPurchaseInvoiceContractDifrranceView contractDifrranceView = instPurchaseInvoiceContractDifrranceViewService.getAllInstPurchaseInvoiceContractDifrranceByType(getUserlogin().getBranchId().getId(), instContract.getInvItemId().getId(), 0);
                if (contractDifrranceView != null) {
                    getInstPurchaseInvoiceContractDifrranceViewList().remove(contractDifrranceView);
                    contractDifrranceView.setQuantity(contractDifrranceView.getQuantity().add(instContract.getAmount()));
                    invoiceContractDifrranceView = contractDifrranceView;
                } else {
                    contractDifrranceView = new InstPurchaseInvoiceContractDifrranceView();
                    contractDifrranceView.setCode(instContract.getInvItemId().getCode());
                    contractDifrranceView.setName(instContract.getInvItemId().getName());
                    contractDifrranceView.setId(instContract.getInvItemId().getId());
                    contractDifrranceView.setBranchId(instContract.getInvItemId().getBranchId().getId());
                    contractDifrranceView.setPrice(instContract.getInvItemId().getSellPrice());
                    contractDifrranceView.setQuantity(instContract.getAmount());
                    contractDifrranceView.setTableType(0);
                    invoiceContractDifrranceView = contractDifrranceView;
                }
            }

            instContractEntity.setInvItemId(invoiceContractDifrranceView);
            getInstPurchaseInvoiceContractDifrranceViewList().add(invoiceContractDifrranceView);

            for (InstContractDetail instContractDetail : instContractDetailsList) {
                ++index;
                InstContractDetailsEntity instContractDetailsEntity = new InstContractDetailsEntity();
                instContractDetailsEntity.setIndex(instContractDetail.getSerial());
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
                saveInstContract.setModificationDate(new Date());
                saveInstContract.setModifiedBy(getUserData().getUser().getModifiedBy());
                saveInstContract.setSerial(instContractEntity.getSerial());
            }

            if (instContractEntity.getInvItemId() != null && instContractEntity.getInvItemId().getTableType() == 0) {
                InvItem invItem = new InvItem();
                invItem.setId(instContractEntity.getInvItemId().getId());
                saveInstContract.setInvItemId(invItem);
            } else if (instContractEntity.getInvItemId() != null && instContractEntity.getInvItemId().getTableType() == 1) {
                InstProjectGuide instProjectGuide = new InstProjectGuide();
                instProjectGuide.setId(instContractEntity.getInvItemId().getId());
                saveInstContract.setProjectId(instProjectGuide);
            }

            saveInstContract.setId(instContractEntity.getId());
            saveInstContract.setBranchId(getUserData().getUserBranch());
            saveInstContract.setCompanyId(getUserData().getUser().getCompanyId());
            saveInstContract.setAdvancePayment(instContractEntity.getAdvancePayment());
            saveInstContract.setInstallmentNumber(instContractEntity.getInstallmentNumber());
            saveInstContract.setDate(instContractEntity.getDate());
            saveInstContract.setDaysNumber(instContractEntity.getDaysNumber());
            saveInstContract.setInstallmentValue(instContractEntity.getInstallmentValue());
            saveInstContract.setTotalValue(instContractEntity.getTotalValue());
            saveInstContract.setSerial(instContractEntity.getSerial());
            if (instContractEntity.getInvOrganizationSite() != null) {
                InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
                invOrganizationSite.setId(instContractEntity.getInvOrganizationSite().getId());
                saveInstContract.setOrganizationSiteId(invOrganizationSite);
            }
            saveInstContract.setStartInstallment(instContractEntity.getStartInstallment());
//            saveInstContract.setProjectId(instContractEntity.getInstProjectGuide());
            saveInstContract.setDeliveryPayment(instContractEntity.getDeliveryPayment());
            saveInstContract.setRemark(instContractEntity.getRemark());
            saveInstContract.setMurabahaRate(instContractEntity.getMurabahaRate());
            saveInstContract.setAmount(instContractEntity.getAmount());
            saveInstContract.setSerial(instContractEntity.getSerial());
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
                instContract = instContractServicenew.addInstContract(saveInstContract, saveInstContractDetailList, instContractDetailWillDeletedList, getUserData().getUserDDs());
                if (instContract.getMsg() != null && !instContract.getMsg().isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), instContract.getMsg()));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("SAVED")));
                    exit("../instContract/instContractList.xhtml");
                }

                //findHeadAndDetailsOfInstContract(instContract.getId());
            } catch (Exception e) {

            }
        } catch (Exception e) {
            saveError(e, "InstContractFormMB", "save");
        }
        return "";
    }

    public void checkContractSerial() {
        if (instContractServicenew.checkContractSerial(instContract)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserDDs().get("ERROR"), instContract.getMsg()));
        };
    }

    public void monthRate() {
        try {
            if (instContractEntity.getDaysNumber() == null) {
                instContractEntity.setDaysNumber(1);
            }
            if (instContractEntity.getInstallmentNumber() == null) {
                instContractEntity.setInstallmentNumber(1);
            }
            Integer monthNumber = instContractEntity.getDaysNumber() * instContractEntity.getInstallmentNumber();
            instContractEntity.setMurabahaRate(instProfitRateService.getInstProfitRatesByMonthAndBranchId(getUserData().getUserBranch().getId(), monthNumber));
            updateTotalValue();
        } catch (Exception e) {
            saveError(e, "InstContractFormMB", "save");
        }
    }

    public void validateSave() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            if (validateCalculation()) {
                if (instContractEntity.getInvItemId() == null || (instContractEntity.getInvItemId().getPrice() != null && instContractEntity.getInvItemId().getQuantity().compareTo(instContractEntity.getAmount()) == -1)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب وضع عدد اقل من الكميه او تساويها"));
                    validateSave = false;
                } else if (instContractEntity.getAmount() == null || instContractEntity.getAmount().compareTo(BigDecimal.ZERO) != 1) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب وضع عدد أكبر من الصفر"));
                    validateSave = false;
                } else if (instContractEntity.getInvOrganizationSite() == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب اختيار جهة تعامل"));
                    validateSave = false;
                } else if (instContractDetailsEntityList == null || (instContractDetailsEntityList != null && instContractDetailsEntityList.isEmpty())) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "لا توجد اقساط"));
                    validateSave = false;
                } else if (instContractEntity != null && instContractEntity.getSerial() != null) {
                    validateSave = checkSerial();
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
                validateSave = false;
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
                totalDetailValue = df.format(totalValue);
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

    public void updateTotalValue2() {
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

    public void updateTotalValue() {
        try {
            instContractEntity.getTotalValue();
            Map<String, String> userDDs = getUserData().getUserDDs();
            if (instContractEntity.getAmount() != null && instContractEntity.getAmount().compareTo(BigDecimal.ZERO) == 1 && instContractEntity.getInvItemId() != null && ((instContractEntity.getInvItemId().getQuantity().compareTo(instContractEntity.getAmount()) != -1))) {
//                instContractEntity.setAmount(BigDecimal.ONE);
                BigDecimal murabahaRateValue = instContractEntity.getMurabahaRate() != null ? instContractEntity.getMurabahaRate() : BigDecimal.ZERO;
                BigDecimal totalPrice = instContractEntity.getInvItemId().getPrice().multiply(instContractEntity.getAmount());
                BigDecimal murabahaValue = totalPrice.multiply(murabahaRateValue).divide(BigDecimal.valueOf(100));
                BigDecimal totlValue = totalPrice.add(murabahaValue);
                instContractEntity.setOrignelAmount(totalPrice);
                instContractEntity.setTotalValue(totlValue);
                calcInstallmentValue();
            } else if (instContractEntity.getAmount() == null || instContractEntity.getAmount().compareTo(BigDecimal.ZERO) != 1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب وضع عدد أكبر من الصفر"));
            } else if (instContractEntity.getInvItemId() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب اختيار مشروع"));
            } else if (((instContractEntity.getInvItemId().getQuantity().compareTo(instContractEntity.getAmount()) == -1))) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب وضع عدد اقل من الكميه او تساويها"));
            }
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
            hashMap.put("projectName", getInstContractEntity().getInvItemId().getName());
            //hashMap.put("projectName", getInstContractEntity().getInstProjectGuide().getName());
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
            hashMap.put("amountText", "القيمه");
            hashMap.put("murabahaRateText", "نسبة الربح");
            hashMap.put("installmentValueText", "مبلغ التقسيط");
            hashMap.put("installmentNumberText", "عدد الاقساط");
            hashMap.put("totalValueText", "اجمالي المبلغ");
            hashMap.put("daysNumberText", "عدد الشهور");
            hashMap.put("remarkText", "ملاحظات");
            hashMap.put("orignelAmountText", "المبلغ الاصلي");
            hashMap.put("totalAmountText", "الإجمالى");
            hashMap.put("totalText", "الإجمالي");

//            fillReport(hashMap, getUserData().getReportPath() + "emptyinitialsalecontract.jasper", instContractDetailsEntityList, "pdf");
            fillReport(hashMap, getUserData().getReportPath() + "installment.jasper", instContractDetailsEntityList, "pdf");
        } catch (Exception e) {
            saveError(e, "InstContractFormMB", "exportPDF");
        }
    }

    public void exportPDFCustomerAccountStatment(ActionEvent actionEvent) throws JRException, IOException {
        try {
            List<InstContractDetailsEntity> contractDetailsEntitys = new ArrayList<>();
            contractDetailsEntitys.addAll(instContractDetailsEntityList);
            HashMap hashMap = new HashMap();
            hashMap.put("CompanyName", getUserData().getCompany().getName());
            hashMap.put("centerTxt", "مركز تساهيل");
            hashMap.put("centerInfoText", "هذا المركز يخص التسديدات المالية حسب المسجل بالبنوك حتي تاريخه");
            hashMap.put("customerInfo", "بيانات العميل");
            hashMap.put("demandText", "المطالبة");
            hashMap.put("fileNumberText", "رقم الملف");
            hashMap.put("subsidyText", "مبلغ الدعم");
            hashMap.put("installmentPeriodText", "فتره التقسيط");
            hashMap.put("phoneText", "الجوال");
            hashMap.put("branchText", "الفرع / الشركة");
            hashMap.put("contractPriceText", "مبلغ العقد");
            hashMap.put("loanDateText", "تاريخ الاقراض");
            hashMap.put("installmentNumberText", "رقم العقد");
            hashMap.put("customerNameText", "اسم العميل");
            hashMap.put("installmentValueText", "قيمه القسط");
            hashMap.put("dueDateText", "تاريخ الاستحقاق");
            hashMap.put("nationalIdText", "رقم الهويه");
            hashMap.put("guarantorCustomerText", "اسماء المعرفين");
            hashMap.put("IBANText", "الايبان / الحساب");
            hashMap.put("LegalPositionText", "الموقف القانوني");
            hashMap.put("totalArrearsText", "إجمالي المتاخر");
            hashMap.put("jobText", "نطاق العمل");
            hashMap.put("amountAfterInstallmentDateText", "المبلغ الذي سدده خارج تواريخ العقد");
            hashMap.put("financialCenterText", "المركز المالي");
            hashMap.put("wronglyTransferredText", "مبالغ حولت بالخطاء");
            hashMap.put("serialText", "مسلسل");
            hashMap.put("installmentDateText", "تاريخ القسط");
            hashMap.put("installmentText", "القسط");
            hashMap.put("payedText", "المسدد");
            hashMap.put("netText", "الصافي");
            hashMap.put("paymentDateText", "تاريخ السداد");
            hashMap.put("paymentAccountText", "حساب السداد");
            hashMap.put("guarantorNameText", "الاسم");
            hashMap.put("centerDateValue", null);
            hashMap.put("reportInfoText", "سدادات العميل الموجوده بالبنوك و المدرجه بالمركز المالي حتى تاريخه هي (             ) ريال وعليه تم التوقيع");
            hashMap.put("totalText", "الإجمالي");
            hashMap.put("demandValue", BigDecimal.ZERO);
            hashMap.put("fileNumberValue", getInstContractEntity().getInvOrganizationSite().getCode());
            hashMap.put("installmentPeriodValue", getInstContractEntity().getInstallmentNumber());
            hashMap.put("phoneValue", instContract.getOrganizationSiteId().getMobile());
            hashMap.put("branchValue", getUserData().getUserBranch().getNameAr());
            hashMap.put("contractPriceValue", getInstContractEntity().getTotalValue());
            hashMap.put("loanDateValue", getInstContractEntity().getDate());
            hashMap.put("installmentNumberValue", getInstContractEntity().getSerial());
            hashMap.put("customerNameValue", getInstContractEntity().getInvOrganizationSite().getName());
            hashMap.put("installmentValue", getInstContractEntity().getTotalValue().divide(new BigDecimal(getInstContractEntity().getInstallmentNumber()), 0, RoundingMode.HALF_UP));
            hashMap.put("dueDateValue", getInstContractEntity().getStartInstallment());
            hashMap.put("nationalIdValue", instContract.getOrganizationSiteId().getNationId());
            hashMap.put("guarantorName1Value", instContract.getOrganizationSiteId().getSponsorName1());
            hashMap.put("guarantorName1NationalId", instContract.getOrganizationSiteId().getSponsorPhone1());
            hashMap.put("guarantorName2Value", instContract.getOrganizationSiteId().getSponsorName2());
            hashMap.put("guarantorName2NationalId", instContract.getOrganizationSiteId().getSponsorPhone2());
            hashMap.put("IBANValue", instContract.getOrganizationSiteId().getAccountBankNumber());
            hashMap.put("LegalPositionValue", instContract.getOrganizationSiteId().getLegalAffairsRemark() != null && !instContract.getOrganizationSiteId().getLegalAffairsRemark().isEmpty() ? instContract.getOrganizationSiteId().getLegalAffairsRemark() : "لايوجد");
            hashMap.put("totalArrearsValue", BigDecimal.ZERO);
            hashMap.put("jobValue", instContract.getOrganizationSiteId().getWorkTypeId() != null ? instContract.getOrganizationSiteId().getWorkTypeId().getName() : null);
            hashMap.put("amountAfterInstallmentDateValue", getInstContractEntity().getAdvancePayment());
            hashMap.put("wronglyTransferredValue", BigDecimal.ZERO);
            hashMap.put("PrintedBy", getUserData().getUser().getName());
            HashMap hashMap2 = new HashMap();
            hashMap2.put("column1", BigDecimal.ZERO);
            hashMap2.put("column2", BigDecimal.ZERO);
            hashMap2.put("column3", BigDecimal.ZERO);
            hashMap2.put("column4", BigDecimal.ZERO);
            hashMap2.put("column5", BigDecimal.ZERO);
            hashMap2.put("column6", BigDecimal.ZERO);
            hashMap2.put("column7", BigDecimal.ZERO);
            hashMap2.put("column8", BigDecimal.ZERO);
            hashMap.put("param", hashMap2);
            List<Integer> installmentIdList = new ArrayList<>();
            contractDetailsEntitys.forEach(obj -> installmentIdList.add(obj.getId()));
            Map<Integer, Vector<Object>> map = bankTransactionService.getAllinstallmentIdAndPayedValue(getUserData().getSelectedBranch(), installmentIdList);
            Map<Integer, Vector<Object>> noticeMap = invNoticeService.getAllinstallmentIdAndPayedValue(getUserData().getSelectedBranch(), instContract.getId());
            BigDecimal balance = BigDecimal.ZERO;
            for (InstContractDetailsEntity obj : contractDetailsEntitys) {
                if (map.containsKey(obj.getId())) {
                    obj.setPayedValue((BigDecimal) map.get(obj.getId()).firstElement());
                    obj.setPayedDate((Date) map.get(obj.getId()).get(1));
                    obj.setPayedAccountName((String) map.get(obj.getId()).get(2));
                }
                obj.setBalance((obj.getPayedValue().subtract(obj.getValue().setScale(0, RoundingMode.HALF_UP)).add(balance)));
                balance = obj.getBalance();
            }
            for (Map.Entry<Integer, Vector<Object>> entry : noticeMap.entrySet()) {
                Integer key = entry.getKey();
                Vector<Object> value = entry.getValue();
                InstContractDetailsEntity contractDetailsEntity = new InstContractDetailsEntity();
                contractDetailsEntity.setSerial(contractDetailsEntitys != null ? contractDetailsEntitys.get(contractDetailsEntitys.size() - 1).getSerial() + 1 : 1);
                contractDetailsEntity.setPayedValue((BigDecimal) value.firstElement());
                contractDetailsEntity.setPayedDate((Date) value.get(1));
                contractDetailsEntity.setPayedAccountName((String) value.get(2));
                contractDetailsEntity.setBalance((contractDetailsEntity.getPayedValue().add(balance)));
                balance = contractDetailsEntity.getBalance();
                contractDetailsEntitys.add(contractDetailsEntity);
            }
            InstContractEntity obj2 = new InstContractEntity(instContractEntity);
            obj2.setProfitStatus(Boolean.FALSE);
            salesReport(obj2);
            hashMap.put("subsidyValue", obj2.getPriceWithoutTaxValue());
            fillReport(hashMap, getUserData().getReportPath() + "customerAccountStatement.jasper", contractDetailsEntitys, "pdf");
            contractDetailsEntitys = null;
        } catch (Exception e) {
            saveError(e, "InstContractFormMB", "exportPDF");
        }
    }

    public void exportPDF3(ActionEvent actionEvent) throws JRException, IOException {
        try {
            HashMap hashMap = new HashMap();

            hashMap.put("titleText", "فاتوره ميبعات اجله");
            hashMap.put("taxNumberText", "الرقم الضريبي");
            hashMap.put("paymentMethodText", "طريقه الدفع");
            hashMap.put("reciptDateText", "تاريخ الفاتورة");
            hashMap.put("reciptNumberText", "رقم الفاتورة");
            hashMap.put("InstallmentPeriodText", "فترة تقسيط");
            hashMap.put("paymentDateText", "تاريخ الاستحقاق");
            hashMap.put("InstallmentMonthText", "القسط الشهري");
            hashMap.put("customerDataText", "بيانات العميل");
            hashMap.put("nameText", "الاسم");
            hashMap.put("phoneText", "الهاتف");
            hashMap.put("addressText", "العنوان");
            hashMap.put("nationalityText", "الهويه");
            hashMap.put("priceText", "المبلغ");
            hashMap.put("taxText", "الضريبه");
            hashMap.put("pricePerPieceText", "السعر الفردي");
            hashMap.put("cartonNumberText", "عدد الكرتون");
            hashMap.put("detailsText", "البيان");
            hashMap.put("numberText", "رقم");
            hashMap.put("priceWithoutTaxText", "صافي المبلغ");
            hashMap.put("discountText", "خصم");
            hashMap.put("addedValueText", "القيمة المضافة");
            hashMap.put("totalValueText", "المجموع الكلي");
            hashMap.put("signatureText", "توقيع المستلم");
            hashMap.put("priceWithTaxText", "المبلغ خالي الضريبه");

            hashMap.put("taxNumberValue", getUserlogin().getBranchId().getTaxCode());
            hashMap.put("paymentMethodValue", getInstContractEntity().getPaymentMethod());
            hashMap.put("reciptDateValue", getInstContractEntity().getDate());
            hashMap.put("reciptNumberValue", getInstContractEntity().getSerial());
            hashMap.put("InstallmentPeriodValue", getInstContractEntity().getInstallmentNumber());
            hashMap.put("paymentDateValue", getInstContractEntity().getStartInstallment());
            hashMap.put("InstallmentMonthValue", getInstContractEntity().getTotalValue().divide(new BigDecimal(getInstContractEntity().getInstallmentNumber()), 2, RoundingMode.HALF_UP));
            hashMap.put("nameValue", getInstContractEntity().getInvOrganizationSite().getName());
            hashMap.put("phoneValue", instContract.getOrganizationSiteId().getMobile());
            hashMap.put("addressValue", instContract.getOrganizationSiteId().getAddress());
            hashMap.put("nationalityValue", instContract.getOrganizationSiteId().getNationId());
            hashMap.put("priceValue", getInstContractEntity().getTotalValue());
            hashMap.put("companyImage", getUserData().getImageReportPath());
            hashMap.put("netValue", getInstContractEntity().getInstallmentValue());
            List<InstContractEntity> instContractEntityList = new ArrayList<>();
            InstContractEntity obj1 = new InstContractEntity(instContractEntity);
            InstContractEntity obj2 = new InstContractEntity(instContractEntity);
            obj1.setProfitStatus(Boolean.TRUE);
            salesReport(obj1);
            obj2.setProfitStatus(Boolean.FALSE);
            salesReport(obj2);
            instContractEntityList.add(obj1);
            instContractEntityList.add(obj2);
            hashMap.put("amountValueText", Tafqeet.numberToText(Double.parseDouble(getInstContractEntity().getTotalValue().toString()), instContract.getOrganizationSiteId().getCurrencyId().getName(), instContract.getOrganizationSiteId().getCurrencyId().getSubName()));
            hashMap.put("totalValue2", obj1.getTotalValue2());
            StringBuilder builder = new StringBuilder();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            builder.append(getUserData().getUserBranch().getNameAr()).append("\n")
                    .append("العميل: ").append(getInstContractEntity().getInvOrganizationSite().getName()).append("\n")
                    .append("فاتوره رقم: ").append(getInstContractEntity().getSerial()).append("\n")
                    .append("تاريخ الفاتوره: ").append(dateFormat.format(getInstContractEntity().getDate())).append("\n")
                    .append("صافي المبلغ: ").append(obj1.getPriceWithTaxValue().add(obj2.getAmountValue())).append("\n")
                    .append("الضريبه vat: ").append(obj1.getTaxValue()).append("\n")
                    .append("قيمه الفاتورة: ").append(obj1.getPriceWithTaxValue().add(obj2.getAmountValue()).add(obj1.getTaxValue()));
            reportQRCode(hashMap, builder.toString());
            fillReport(hashMap, getUserData().getReportPath() + "salesInvoice.jasper", instContractEntityList, "pdf");
        } catch (Exception e) {
            saveError(e, "InstContractFormMB", "exportPDF");
        }
    }

    private void salesReport(InstContractEntity contractEntity) {
        BigDecimal amountValue2 = contractEntity.getInstallmentValue().subtract(instContractEntity.getInvItemId().getPrice().multiply(instContractEntity.getAmount()));
        contractEntity.setCartonNumberValue(getInstContractEntity().getAmount().divide(new BigDecimal(250), 2, RoundingMode.HALF_UP));
        if (contractEntity.getProfitStatus()) {
            contractEntity.setAmountValue(amountValue2);
            contractEntity.setPriceWithTaxValue(contractEntity.getAmountValue().divide(new BigDecimal(1.15), 2, RoundingMode.HALF_UP));
            BigDecimal priceWithoutTaxValueCalc = (contractEntity.getPriceWithTaxValue().divide(getInstContractEntity().getAmount().divide(new BigDecimal(250), 2, RoundingMode.HALF_UP)));
            BigDecimal taxValueCalc = (contractEntity.getAmountValue().subtract(contractEntity.getPriceWithTaxValue()));
            contractEntity.setTaxValue(taxValueCalc);
            contractEntity.setPriceWithoutTaxValue(priceWithoutTaxValueCalc);
            contractEntity.setTotalValue2(contractEntity.getInstallmentValue().subtract(taxValueCalc));
            contractEntity.setPricePerPieceValue(contractEntity.getPriceWithTaxValue().divide(contractEntity.getCartonNumberValue()));
        } else {

            BigDecimal ammountValueCalc = getTotalValue().divide(new BigDecimal(1.60), 2, RoundingMode.HALF_UP);
            BigDecimal secondAmountValue = instContractEntity.getInvItemId().getPrice().multiply(instContractEntity.getAmount());
            contractEntity.setPriceWithTaxValue(amountValue2.divide(new BigDecimal(1.15), 2, RoundingMode.HALF_UP));
            BigDecimal priceWithoutTaxValueCalc = (contractEntity.getPriceWithTaxValue().divide(getInstContractEntity().getAmount().divide(new BigDecimal(250), 2, RoundingMode.HALF_UP)));
            BigDecimal taxValueCalc = (getTotalValue().subtract(ammountValueCalc)).subtract(priceWithoutTaxValueCalc);
            BigDecimal netvalueCalc = getTotalValue().subtract(taxValueCalc);
            contractEntity.setPriceWithoutTaxValue(ammountValueCalc);
            contractEntity.setAmountValue(secondAmountValue);
            contractEntity.setTaxValue(BigDecimal.ZERO);
            contractEntity.setNetValue(netvalueCalc);
            contractEntity.setPriceWithTaxValue(BigDecimal.ZERO);
            contractEntity.setPricePerPieceValue(secondAmountValue.divide(contractEntity.getCartonNumberValue()));
        }
    }

    public void exportPDFFromList(Integer instContractId) {
        try {
            findHeadAndDetailsOfInstContract(instContractId);

            exportPDF(null);

//            HashMap hashMap = new HashMap();
//            hashMap.put("PrintedBy", getUserData().getUser().getName());
//            hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
//            hashMap.put("companyName", getUserData().getCompany().getName());
//            fillReport(hashMap, getUserData().getReportPath() + "emptyinitialsalecontract.jasper", instContractDetailsEntityList, "pdf");
        } catch (Exception e) {
            saveError(e, "InstContractFormMB", "exportPDFFromList");
        }
    }

//    public <T> void fillReport(HashMap hashMap, String reportPath, List<T> listBean, String exportType) throws JRException, IOException {
//        try {
//            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listBean);
//            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, hashMap, beanCollectionDataSource);
//            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
//            if ("pdf".equals(exportType)) {
//                JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
//            } else if ("excel".equals(exportType)) {
//
//            } else if ("html".equals(exportType)) {
//
//            }
//        } catch (Exception e) {
//            saveError(e, "InstContractFormMB", "fillReport");
//        }
//
//    }
    @Override
    public String getScreenName() {

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<InvOrganizationSiteDTO> completOrgSite(String query) {

        return AutoComplete.completOrgSiteDTO(query, invOrgSiteList, invOrgSiteConverter);

//            List<InvOrganizationSite> invOrgSiteList = this.invOrgSiteList;
//        if (query == null || query.trim().equals("")) {
//
//            invOrgSiteConverter = new InvOrganizationSiteConverter(invOrgSiteList);
//            return invOrgSiteList;
//        }
//        List<InvOrganizationSite> filteredInvs = new ArrayList<>();
//
//        String nameAr;
//        String code;
//        InvOrganizationSite invOrgSiteFilter;
//
//        for (int i = 0; i < this.invOrgSiteList.size(); i++) {
//            invOrgSiteFilter = invOrgSiteList.get(i);
//            nameAr = invOrgSiteFilter.getName();
//            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
//                if (!filteredInvs.contains(invOrgSiteFilter)) {
//                    filteredInvs.add(invOrgSiteFilter);
//                }
//            }
//
//            code = invOrgSiteFilter.getCode().toString();
//            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
//                if (!filteredInvs.contains(invOrgSiteFilter)) {
//                    filteredInvs.add(invOrgSiteFilter);
//                }
//            }
//        }
//
//        invOrgSiteConverter = new InvOrganizationSiteConverter(filteredInvs);
//        return filteredInvs;
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

    public Boolean checkSerial() {
        if (instContractEntity != null && instContractEntity.getSerial() != null) {
            Boolean check = instContractServicenew.checkSerail(getUserData().getSelectedBranch(), instContractEntity.getSerial(), instContractEntity.getId());
            if (check) {
                errorMessage("رقم العقد موجود مسبقا");
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public List<InstProjectGuide> completeInstProject(String query) {
        List<InstProjectGuide> instProjectGuides = this.instProjectGuideList;
        if (query == null || query.trim().equals("")) {

            instProjectGuideConverter = new InstProjectGuideConverter(instProjectGuides);
            return instProjectGuides;
        }
        List<InstProjectGuide> filteredInstProject = new ArrayList<>();

        String nameAr;
        String code;
        InstProjectGuide instProjectGuideFilter;

        for (int i = 0; i < this.instProjectGuideList.size(); i++) {
            instProjectGuideFilter = instProjectGuides.get(i);
            nameAr = instProjectGuideFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInstProject.contains(instProjectGuideFilter)) {
                    filteredInstProject.add(instProjectGuideFilter);
                }
            }

            code = instProjectGuideFilter.getCode().toString();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInstProject.contains(instProjectGuideFilter)) {
                    filteredInstProject.add(instProjectGuideFilter);
                }
            }
        }

        instProjectGuideConverter = new InstProjectGuideConverter(filteredInstProject);
        return filteredInstProject;
    }

    /**
     * @return the invOrgSiteList
     */
    public List<InvOrganizationSiteDTO> getInvOrgSiteList() {
        if (invOrgSiteList == null || invOrgSiteList.isEmpty()) {
            invOrgSiteList = organizationSiteService.getorganizationSiteByBranchIdDTOSmall(getUserData().getUserBranch() != null ? getUserData().getUserBranch().getId() : null, true, 0);
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

}
