/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.checkUp;

import com.isag.businessservice.CheckUpDetailsService;
import com.isag.businessservice.CheckUpService;
import com.isag.businessservice.HosDoctorService;
import com.isag.businessservice.IsagUserBankService;
import com.isag.businessservice.PatientRegistrationService;
import com.isag.businessservice.RscWorkUnitsGuideService;
import com.isag.converter.GlBankConverter;
import com.isag.converter.HosDoctorConverter;
import com.isag.converter.HosPatientRegistrationConvertor;
import com.isag.converter.RscWorkUnitsGuideDTOConverter;
import com.isag.dto.RscWorkUnitsGuideDTO;
import com.isag.entity.GlBank;
import com.isag.entity.HosCheckUp;
import com.isag.entity.HosCheckUpDetail;
import com.isag.entity.HosDoctor;
import com.isag.entity.HosPatientRegistration;
import com.isag.entiy.HosCheckingUpDetailEntity;
import com.isag.entiy.HosCheckingUpEntity;
import com.isag.isag.AutoComplete;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author ahmed
 */
@ManagedBean(name = "checkingUpFormMB")
@ViewScoped
public class CheckingUpFromMB extends BaseFormBean {

    /**
     * @param rscWorkUnitsGuideDTOList the rscWorkUnitsGuideDTOList to set
     */
    public void setRscWorkUnitsGuideDTOList(List<RscWorkUnitsGuideDTO> rscWorkUnitsGuideDTOList) {
        this.rscWorkUnitsGuideDTOList = rscWorkUnitsGuideDTOList;
    }

    private GlBankConverter glBankConverter;
    private List<GlBank> glBankList;
    private List<HosDoctor> hosDoctorsList;
    private List<HosDoctor> hosClinicsList;
    private List<HosDoctor> hosDoctorServiceList;
    private HosDoctor hosDoctorUpdated;
    private HosDoctor hosClinicUpdated;
    private HosDoctorConverter hosDoctorConverter;
    private HosDoctorConverter hosClinicConverter;
    private HosDoctorConverter hosDoctorServiceConverter;
    private List<HosPatientRegistration> hosPatientRegistrationList;
    private HosPatientRegistrationConvertor hosPatientRegistrationConvertor;
    private HosCheckingUpEntity hosCheckingUpEntity;
    private List<HosCheckingUpDetailEntity> hosCheckingUpDetailEntityList;
    private List<HosCheckingUpDetailEntity> hosCheckingUpDetailEntityReport;
    private List<HosCheckingUpDetailEntity> hosCheckingUpDetailDeletedEntityList;
    private HosCheckingUpDetailEntity hosCheckingUpDetailEntitySelected;
    private HosCheckingUpDetailEntity hosCheckingUpDetailEntityMemory;
    private Integer hosCheckUpId;
    private HosCheckUp hosCheckUp;
    private List<HosCheckUpDetail> hosCheckUpDetailsList;
    private String totalCach;
    private boolean validateSave;
    private boolean showPatientDetails;
    
    private List<RscWorkUnitsGuideDTO> rscWorkUnitsGuideDTOList;
    private RscWorkUnitsGuideDTOConverter rscWorkUnitsGuideDTOConverter;
    
    @EJB
    private HosDoctorService hosDoctorService;
    @EJB
    private IsagUserBankService isagUserBankService;
    @EJB
    private CheckUpService hosCheckUpService;
    @EJB
    private CheckUpDetailsService hosCheckUpDetailsService;
    @EJB
    private PatientRegistrationService hosPatientRegistrationService;
    @EJB
    private RscWorkUnitsGuideService rscWorkUnitsGuideService;

    ;

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
                hosCheckUpId = (Integer) context.getSessionMap().get("hosCheckUpIdSeclected");
                findHeadAndDetailsOfHosCheckUp(hosCheckUpId);
            }
            hosCheckingUpDetailDeletedEntityList = new ArrayList<>();
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "init");
        }
    }
    
    public List<RscWorkUnitsGuideDTO> completeWorkUnitsGuideDTO(String query) {
        return AutoComplete.completeWorkUnitsGuideDTO(query, getRscWorkUnitsGuideDTOList(), getRscWorkUnitsGuideDTOConverter());
    }
    
    public List<RscWorkUnitsGuideDTO> getRscWorkUnitsGuideDTOList() {
        if (rscWorkUnitsGuideDTOList == null || rscWorkUnitsGuideDTOList.isEmpty()) {
            setRscWorkUnitsGuideDTOList(rscWorkUnitsGuideService.getBranchUnitAndType(getUserData().getUserBranch().getId()));
            setRscWorkUnitsGuideDTOConverter(new RscWorkUnitsGuideDTOConverter(rscWorkUnitsGuideDTOList));
        }
        return rscWorkUnitsGuideDTOList;
    }
    

    public void validateUnitGuide() {
        try {
            if (hosCheckingUpDetailEntitySelected != null) {
                if (hosCheckingUpDetailEntitySelected.getInstProjectGuideId() != null) {
                    hosCheckingUpDetailEntitySelected.setInstProjectGuideIdBak(hosCheckingUpDetailEntitySelected.getInstProjectGuideId());
                }
            }
        } catch (Exception e) {
            saveError(e, "RscTenderFormMB", "validateUnitGuide");
        }
    }

    public void reset() {
        try {
            hosCheckingUpEntity = new HosCheckingUpEntity();
            hosCheckingUpDetailEntityList = new ArrayList<>();
            hosCheckUp = new HosCheckUp();
            hosCheckingUpEntity.setDate(new Date());
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "reset");
        }
    }

    public void findHeadAndDetailsOfHosCheckUp(Integer hosCheckUpId) {
        try {
            hosCheckUp = hosCheckUpService.findHosCheckUpById(hosCheckUpId);
            hosCheckUpDetailsList = hosCheckUpDetailsService.getAllHosCheckUpDetailsByHosCheckUpId(hosCheckUp.getId());
            hosCheckingUpDetailEntityList = new ArrayList<>();
            mapHosCheckUpToHosCheckUpEntity();
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "findHeadAndDetailsOfHosCheckUp");
        }
    }

    public void mapHosCheckUpToHosCheckUpEntity() {
        try {
            hosCheckingUpEntity = new HosCheckingUpEntity();
            hosCheckingUpEntity.setId(hosCheckUp.getId());
            hosCheckingUpEntity.setDate(hosCheckUp.getDate());
            hosCheckingUpEntity.setGlBank(hosCheckUp.getGlBankId());
            hosCheckingUpEntity.setRemark(hosCheckUp.getRemark());
            hosCheckingUpEntity.setSerial(hosCheckUp.getSerial());
            hosCheckingUpEntity.setPaymentType(hosCheckUp.getPaymentType());
            hosCheckingUpEntity.setBranchId(hosCheckUp.getBranchId() != null ? hosCheckUp.getBranchId().getId() : null);
            hosCheckingUpEntity.setCompanyId(hosCheckUp.getCompanyId() != null ? hosCheckUp.getCompanyId().getId() : null);
            hosCheckingUpEntity.setCreatedBy(hosCheckUp.getCreatedBy() != null ? hosCheckUp.getCreatedBy().getId() : null);
            hosCheckingUpEntity.setModifiedBy(hosCheckUp.getModifiedBy() != null ? hosCheckUp.getModifiedBy().getId() : null);
            hosCheckingUpEntity.setCreationDate(hosCheckUp.getCreationDate() != null ? hosCheckUp.getCreationDate() : null);
            hosCheckingUpEntity.setModificationDate(hosCheckUp.getModificationDate() != null ? hosCheckUp.getModificationDate() : null);

            hosCheckingUpEntity.setHosDoctor(hosCheckUp.getHosDoctorId());
            hosCheckingUpEntity.setHosClinic(hosCheckUp.getHosClinicId());
            hosCheckingUpEntity.setValue(hosCheckUp.getHosDoctorId() != null ? hosCheckUp.getHosDoctorId().getServicePrice() : BigDecimal.ZERO);

            hosCheckingUpEntity.setHosPatientRegistration(hosCheckUp.getHosPatientRegisterationId());

            if (hosCheckUp.getHosPatientRegisterationId() != null && hosCheckUp.getHosPatientRegisterationId().getGender() != null) {
                if (hosCheckUp.getHosPatientRegisterationId().getGender() == 1) {
                    hosCheckingUpEntity.setLiteralGender("ذكر");
                } else {
                    hosCheckingUpEntity.setLiteralGender("انثى");
                }
            }
            loadDoctorServices();
            for (HosCheckUpDetail entity : hosCheckUpDetailsList) {
                HosCheckingUpDetailEntity dto = new HosCheckingUpDetailEntity();
                dto.setValue(entity.getValue());
                dto.setDiscountRatio(entity.getDiscountRatio());
                dto.setBearReatio(entity.getBearRatio());
                dto.setTotal(entity.getTotal());
                dto.setId(entity.getId());
                dto.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);
                dto.setCompanyId(entity.getCompanyId() != null ? entity.getCompanyId().getId() : null);
                dto.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
                dto.setModifiedBy(entity.getModifiedBy() != null ? entity.getModifiedBy().getId() : null);
                dto.setCreationDate(entity.getCreationDate() != null ? entity.getCreationDate() : null);
                dto.setModificationDate(entity.getModificationDate() != null ? entity.getModificationDate() : null);
                dto.setHosDoctor(entity.getHosDoctorServiceId());
                dto.setInstProjectGuideId(entity.getInstProjectGuideId());
                dto.setMarkEdit(Boolean.FALSE);
                hosCheckingUpDetailEntityList.add(dto);
            }
            calculateTotalCash();
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "mapHosCheckUpToHosCheckUpEntity");
        }
    }

    @Override
    public String save() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            validateSave = true;
//            validateSave();
//            if (!validateSave) {
//                return "";
//            }
            HosCheckUp saveHosCheckUp = new HosCheckUp();
            saveHosCheckUp.setId(hosCheckingUpEntity.getId());
            saveHosCheckUp.setBranchId(getUserData().getUserBranch());
            saveHosCheckUp.setCompanyId(getUserData().getUser().getCompanyId());
            if (hosCheckingUpEntity.getId() == null) {
                saveHosCheckUp.setCreationDate(new Date());
                saveHosCheckUp.setCreatedBy(getUserData().getUser().getCreatedBy());
            } else {
                saveHosCheckUp.setModificationDate(new Date());
                saveHosCheckUp.setModifiedBy(getUserData().getUser().getModifiedBy());
            }
            saveHosCheckUp.setDate(hosCheckingUpEntity.getDate());
            saveHosCheckUp.setSerial(hosCheckingUpEntity.getSerial());
            if (glBankList != null && !glBankList.isEmpty()) {
                saveHosCheckUp.setGlBankId(glBankList.get(0));
            }
            saveHosCheckUp.setRemark(hosCheckingUpEntity.getRemark());
            saveHosCheckUp.setValue(hosCheckingUpEntity.getValue());
            saveHosCheckUp.setValueLocal(hosCheckingUpEntity.getValue());
            saveHosCheckUp.setHosDoctorId(hosCheckingUpEntity.getHosDoctor());
            saveHosCheckUp.setHosClinicId(hosCheckingUpEntity.getHosClinic());
            saveHosCheckUp.setHosPatientRegisterationId(hosCheckingUpEntity.getHosPatientRegistration());
            saveHosCheckUp.setHosInsuranceRegisterationId(hosCheckingUpEntity.getHosPatientRegistration().getParent());
            saveHosCheckUp.setPaymentType(hosCheckingUpEntity.getPaymentType());

            List<HosCheckUpDetail> saveCheckUpDetailList = new ArrayList<>();

            if (hosCheckingUpDetailEntityList != null && !hosCheckingUpDetailEntityList.isEmpty()) {
                for (HosCheckingUpDetailEntity dto : hosCheckingUpDetailEntityList) {
                    HosCheckUpDetail entity = new HosCheckUpDetail();
                    entity.setId(dto.getId());
                    entity.setBranchId(getUserData().getUser().getBranchId());
                    entity.setCompanyId(getUserData().getUser().getCompanyId());
                    if (dto.getId() == null) {
                        entity.setCreationDate(new Date());
                        entity.setCreatedBy(getUserData().getUser().getCreatedBy());
                    } else {
                        entity.setModificationDate(new Date());
                        entity.setModifiedBy(getUserData().getUser().getModifiedBy());
                    }
                    entity.setBearRatio(dto.getBearReatio());
                    entity.setDiscountRatio(dto.getDiscountRatio());
                    entity.setValue(dto.getValue());
                    entity.setTotal(dto.getTotal());
                    entity.setHosDoctorServiceId(dto.getHosDoctor());
                    entity.setInstProjectGuideId(dto.getInstProjectGuideId());
                    saveCheckUpDetailList.add(entity);
                }
            }

            List<HosCheckUpDetail> hosCheckUpDetailWillDeletedList = new ArrayList<>();
            if (hosCheckingUpDetailDeletedEntityList != null && !hosCheckingUpDetailDeletedEntityList.isEmpty()) {
                for (HosCheckingUpDetailEntity upDetailEntity : hosCheckingUpDetailDeletedEntityList) {
                    HosCheckUpDetail checkUpDetail = new HosCheckUpDetail();
                    checkUpDetail.setId(upDetailEntity.getId());
                    hosCheckUpDetailWillDeletedList.add(checkUpDetail);
                }
            }

            try {
                hosCheckUp = hosCheckUpService.addHosCheckUp(saveHosCheckUp, saveCheckUpDetailList, hosCheckUpDetailWillDeletedList);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("SAVED")));
            } catch (Exception e) {

            }
            findHeadAndDetailsOfHosCheckUp(hosCheckUp.getId());
            hosCheckingUpDetailDeletedEntityList = new ArrayList<>();
            return "";
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "save");
            return null;
        }
    }

    public void validateSave() {
        try {
            if (hosCheckingUpEntity.getDate() == null) {
                validateSave = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "يجب اختيار التاريخ"));
            }
            if (hosCheckingUpEntity.getHosDoctor() == null) {
                validateSave = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "يجب اختيار الطبيب المعالج "));
            }
            if (hosCheckingUpEntity.getHosClinic() == null) {
                validateSave = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "يجب اختيار العيادة"));
            }

            if (hosCheckingUpEntity.getGlBank() == null && hosCheckingUpEntity.getGlBank().getAccountId() == null) {
                validateSave = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار الخزنة"));

            }
            if (hosCheckingUpEntity.getHosPatientRegistration() == null) {
                validateSave = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "يجب اختيار المريض"));
            }
            if (hosCheckingUpDetailEntityList == null || hosCheckingUpDetailEntityList.isEmpty()) {
                validateSave = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "يجب اضافة خدمة"));
            }
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "validateSave");

        }
    }

    @Override
    public void load() {
        try {
            //glBankList = isagUserBankService.getAllGlBankForUserByTypeAndBranchId(getUserData().getUser().getId(), getUserData().getUserBranch().getId(), 0);
            glBankConverter = new GlBankConverter(glBankList);
//        hosDoctorsList = hosDoctorService.getAllSubHosDoctorByBranchIdActiveAndLevel(getUserData().getUserBranch().getId(), 2);
//        hosDoctorConverter = new HosDoctorConverter(hosDoctorsList);
            hosClinicsList = hosDoctorService.getAllSubHosDoctorByBranchIdActiveAndLevel(getUserData().getUserBranch().getId(), 1);
            hosClinicConverter = new HosDoctorConverter(hosClinicsList);

            hosPatientRegistrationList = hosPatientRegistrationService.getAllHosPatientRegistrationByBranchId(getUserData().getUserBranch().getId(), 0);
            hosPatientRegistrationConvertor = new HosPatientRegistrationConvertor(hosPatientRegistrationList);
            glBankList = isagUserBankService.getAllBankListByUserAndBranch(getUserData().getUser().getId(), getUserData().getUserBranch().getId());
            hosCheckingUpEntity = new HosCheckingUpEntity();
            hosCheckingUpEntity.setDate(new Date());
            showPatientDetails = false;
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "load");
        }
    }

    public void loadDoctorServices() {
        try {
            if (hosCheckingUpEntity.getHosDoctor() != null) {
                hosDoctorServiceList = hosDoctorService.getAllSubHosDoctorByBranchIdActiveAndHosDoctorId(hosCheckingUpEntity.getHosDoctor().getId());
                hosDoctorServiceConverter = new HosDoctorConverter(hosDoctorServiceList);
                // hosCheckingUpEntity = new HosCheckingUpEntity();

            }
            hosDoctorUpdated = new HosDoctor();
            hosDoctorUpdated = hosCheckingUpEntity.getHosDoctor();
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "loadDoctorServices");
        }
    }

    public void loadDoctors() {
        try {
            if (hosCheckingUpEntity.getHosClinic() != null) {
//                hosDoctorsList = hosDoctorService.getAllSubHosDoctorByBranchIdActiveAndLevelAndClinicId(getUserData().getUserBranch().getId(), 2, hosCheckingUpEntity.getHosClinic().getId());
//                hosDoctorConverter = new HosDoctorConverter(hosDoctorsList);
                // hosCheckingUpEntity = new HosCheckingUpEntity();
                getHosDoctorsList();
            }
            hosClinicUpdated = new HosDoctor();
            hosClinicUpdated = hosCheckingUpEntity.getHosClinic();
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "loadDoctors");
        }
    }

    public void updateServicePrice(SelectEvent event) {
        try {

            HosCheckingUpDetailEntity hosCheckingUpDetailEntity = (HosCheckingUpDetailEntity) event.getComponent().getAttributes().get("varValue");
            hosCheckingUpDetailEntitySelected = hosCheckingUpDetailEntity;
            if (hosCheckingUpDetailEntity.getHosDoctor() != null && hosCheckingUpDetailEntity.getHosDoctor().getServicePrice() != null) {
                hosCheckingUpDetailEntity.setValue(hosCheckingUpDetailEntity.getHosDoctor().getServicePrice());
            }
            if (hosCheckingUpEntity.getHosPatientRegistration() != null && hosCheckingUpEntity.getHosPatientRegistration().getParent() != null) {
                hosCheckingUpDetailEntity.setDiscountRatio(hosCheckingUpEntity.getHosPatientRegistration().getParent() != null ? hosCheckingUpEntity.getHosPatientRegistration().getParent().getDiscountRatio() : BigDecimal.ZERO);
            }
            if (hosCheckingUpEntity.getHosPatientRegistration() != null) {
                hosCheckingUpDetailEntity.setBearReatio(hosCheckingUpEntity.getHosPatientRegistration().getBearRatio() != null ? hosCheckingUpEntity.getHosPatientRegistration().getBearRatio() : BigDecimal.ZERO);
            }
            calculateTotalAfterDiscountsPerService();
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "updateServicePrice");
        }
    }

    public void calculateTotalAfterDiscountsPerService() {
        try {
            BigDecimal total = BigDecimal.ZERO;
            BigDecimal hundred = BigDecimal.valueOf(100);
            hosCheckingUpDetailEntitySelected.setDiscountRatio(hosCheckingUpDetailEntitySelected.getDiscountRatio() != null ? hosCheckingUpDetailEntitySelected.getDiscountRatio().setScale(2, BigDecimal.ROUND_UP) : BigDecimal.ZERO);
            hosCheckingUpDetailEntitySelected.setBearReatio(hosCheckingUpDetailEntitySelected.getBearReatio() != null ? hosCheckingUpDetailEntitySelected.getBearReatio().setScale(2, BigDecimal.ROUND_UP) : BigDecimal.ZERO);
            total = hosCheckingUpDetailEntitySelected.getValue() != null ? hosCheckingUpDetailEntitySelected.getValue().setScale(2, BigDecimal.ROUND_UP) : BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_UP);
            total = total.subtract((total.multiply(hosCheckingUpDetailEntitySelected.getDiscountRatio().divide(hundred.abs(), 4, 4))));
            total = total.multiply(hosCheckingUpDetailEntitySelected.getBearReatio().divide(hundred.abs(), 4, 4));
            total = total.setScale(2, BigDecimal.ROUND_UP);
            hosCheckingUpDetailEntitySelected.setTotal(total);
            calculateTotalCash();
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "calculateTotalAfterDiscountsPerService");
        }
    }

    public void addClosingAccounts() {
        try {
//            if ( hosCheckingUpEntity.getHosPatientRegistration() != null) {

                if (hosCheckingUpDetailEntityList != null && !hosCheckingUpDetailEntityList.isEmpty()) {
                    hosCheckingUpDetailEntityMemory = new HosCheckingUpDetailEntity();
                    hosCheckingUpDetailEntityMemory = hosCheckingUpDetailEntityList.get(hosCheckingUpDetailEntityList.size() - 1);
                }
                if ((hosCheckingUpDetailEntityList == null || hosCheckingUpDetailEntityList.isEmpty()) || (hosCheckingUpDetailEntityMemory.getHosDoctor() != null)) {
                    HosCheckingUpDetailEntity detailEntity = new HosCheckingUpDetailEntity();
                    detailEntity.setHosDoctor(null);
                    detailEntity.setValue(BigDecimal.ZERO);

                    for (HosCheckingUpDetailEntity detailEntity1 : hosCheckingUpDetailEntityList) {
                        detailEntity1.setMarkEdit(Boolean.FALSE);
                    }
                    detailEntity.setMarkEdit(Boolean.TRUE);
                    hosCheckingUpDetailEntityList.add(detailEntity);
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "يجب استكمال البيانات"));
                }
//            } 
//            else {
//                
//                if (hosCheckingUpEntity.getHosPatientRegistration() == null) {
//                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "يجب اختيار المريض"));
//                }
//            }
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "addClosingAccounts");
        }
    }

    public void calculateTotalCash() {
        try {
            if (hosCheckingUpDetailEntityList != null && !hosCheckingUpDetailEntityList.isEmpty()) {
                BigDecimal totalCashVal = BigDecimal.ZERO;
                for (HosCheckingUpDetailEntity upDetailEntity : hosCheckingUpDetailEntityList) {
                    totalCashVal = totalCashVal.add(upDetailEntity.getTotal());
                }
                DecimalFormat df = new DecimalFormat("#,##0.00");
                totalCach = df.format(totalCashVal);
            }
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "calculateTotalCash");
        }
    }

    public void deleteCheckingUpDetail() {
        try {

            if (hosCheckingUpDetailEntitySelected.getId() != null) {
                hosCheckingUpDetailDeletedEntityList.add(hosCheckingUpDetailEntitySelected);
            }

            hosCheckingUpDetailEntityList.remove(hosCheckingUpDetailEntitySelected);
            calculateTotalCash();
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "deleteCheckingUpDetail");
        }
    }

    public void calculateTotalAfterDiscountsPerServiceForEach() {
        try {
            List<HosCheckingUpDetailEntity> hosCheckingUpDetailTempEntityList = new ArrayList<>();
            BigDecimal hundred = BigDecimal.valueOf(100);
            if (hosCheckingUpDetailEntityList != null && !hosCheckingUpDetailEntityList.isEmpty()) {
                for (HosCheckingUpDetailEntity hcude : hosCheckingUpDetailEntityList) {
                    if (hosCheckingUpEntity.getHosPatientRegistration() != null && hosCheckingUpEntity.getHosPatientRegistration().getParent() != null) {
                        hcude.setDiscountRatio(hosCheckingUpEntity.getHosPatientRegistration().getParent() != null ? hosCheckingUpEntity.getHosPatientRegistration().getParent().getDiscountRatio() : BigDecimal.ZERO);
                    }
                    if (hosCheckingUpEntity.getHosPatientRegistration() != null) {
                        hcude.setBearReatio(hosCheckingUpEntity.getHosPatientRegistration().getBearRatio() != null ? hosCheckingUpEntity.getHosPatientRegistration().getBearRatio() : BigDecimal.ZERO);
                    }
                    BigDecimal total = BigDecimal.ZERO;
                    hcude.setDiscountRatio(hcude.getDiscountRatio() != null ? hcude.getDiscountRatio().setScale(2, BigDecimal.ROUND_UP) : BigDecimal.ZERO);
                    hcude.setBearReatio(hcude.getBearReatio() != null ? hcude.getBearReatio().setScale(2, BigDecimal.ROUND_UP) : BigDecimal.ZERO);
                    total = hcude.getValue() != null ? hcude.getValue().setScale(2, BigDecimal.ROUND_UP) : BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_UP);
                    total = total.subtract((total.multiply(hcude.getDiscountRatio().divide(hundred.abs(), 4, 4))));
                    total = total.multiply(hcude.getBearReatio().divide(hundred.abs(), 4, 4));
                    total = total.setScale(2, BigDecimal.ROUND_UP);
                    hcude.setTotal(total);
                    hosCheckingUpDetailTempEntityList.add(hcude);
                }
            }
            hosCheckingUpDetailEntityList = new ArrayList<>(hosCheckingUpDetailTempEntityList);
            calculateTotalCash();
            if (hosCheckingUpEntity.getHosPatientRegistration() != null) {
                showPatientDetails = true;
            } else {
                showPatientDetails = false;
            }

            if (hosCheckingUpEntity.getHosPatientRegistration() != null && hosCheckingUpEntity.getHosPatientRegistration().getGender() != null) {
                if (hosCheckingUpEntity.getHosPatientRegistration().getGender() == 1) {
                    hosCheckingUpEntity.setLiteralGender("ذكر");
                } else {
                    hosCheckingUpEntity.setLiteralGender("انثى");
                }
            } else if (hosCheckingUpEntity.getHosPatientRegistration() != null && hosCheckingUpEntity.getHosPatientRegistration().getGender() == null) {
                hosCheckingUpEntity.setLiteralGender(" ");
            }
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "calculateTotalAfterDiscountsPerServiceForEach");
        }
    }

    public String exit() {

        exit("../MovementClinics/CheckUp.xhtml");

        return "";
    }

    public void showOrHideDialogForClinics() {
        try {
            if (hosCheckingUpDetailEntityList != null && !hosCheckingUpDetailEntityList.isEmpty()) {
                OpenDlg("dlg2");
            } else {
                loadDoctors();
            }
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "showOrHideDialogForClinics");
        }
    }

    public void showOrHideDialog() {
        try {
            if (hosCheckingUpDetailEntityList != null && !hosCheckingUpDetailEntityList.isEmpty()) {
                OpenDlg("dlg1");
            } else {
                loadDoctorServices();
            }
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "showOrHideDialog");
        }
    }

    public void changeHosDoctorOk() {
        try {
            if (hosCheckingUpEntity.getId() != null && !hosCheckingUpDetailEntityList.isEmpty()) {
                for (HosCheckingUpDetailEntity hcude : hosCheckingUpDetailEntityList) {
                    hosCheckingUpDetailDeletedEntityList.add(hcude);
                }
            }
            hosCheckingUpDetailEntityList = new ArrayList<>();
            totalCach = new String();
            loadDoctorServices();
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "changeHosDoctorOk");
        }
    }

    public void changeHosClinicOk() {
        try {
            if (hosCheckingUpEntity.getId() != null && !hosCheckingUpDetailEntityList.isEmpty()) {
                for (HosCheckingUpDetailEntity hcude : hosCheckingUpDetailEntityList) {
                    hosCheckingUpDetailDeletedEntityList.add(hcude);
                }
            }
            hosCheckingUpDetailEntityList = new ArrayList<>();
            totalCach = new String();
            hosCheckingUpEntity = new HosCheckingUpEntity();
            loadDoctors();
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "changeHosClinicOk");
        }
    }

    public void changeHosDoctorCancel() {
        try {
            if (hosDoctorUpdated != null) {
                hosCheckingUpEntity.setHosDoctor(hosDoctorUpdated);
            }
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "changeHosDoctorCancel");
        }
    }

    public void changeHosClinicCancel() {
        try {
            if (hosDoctorUpdated != null) {
                hosCheckingUpEntity.setHosClinic(hosClinicUpdated);
            }
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "changeHosClinicCancel");
        }
    }

    public void CloseDlg(String dlgvar) {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('" + dlgvar + "').hide();");
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "CloseDlg");
        }
    }

    public void OpenDlg(String dlgvar) {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('" + dlgvar + "').show();");
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "OpenDlg");
        }
    }

    public String cancel() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");
            exit("../PatientRegistration/PatientRegistrationForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "cancel");
            return null;
        }
    }

    public void onCellEdit(CellEditEvent event) {
        try {

            for (HosCheckingUpDetailEntity detailEntity1 : hosCheckingUpDetailEntityList) {
                detailEntity1.setMarkEdit(Boolean.FALSE);
            }

            Object oldValue = event.getOldValue() != null ? event.getOldValue() : null;
            Object newValue = event.getNewValue() != null ? event.getNewValue() : null;
            String column_name = event.getColumn().getClientId();
            DataTable dataTable = (DataTable) event.getSource();
            hosCheckingUpDetailEntitySelected = (HosCheckingUpDetailEntity) dataTable.getRowData();
            hosCheckingUpDetailEntitySelected.setMarkEdit(Boolean.TRUE);
            if (column_name.contains("BEAR_VALUE")) {
                if (!validateValue(newValue != null ? (BigDecimal) newValue : null)) {
                    hosCheckingUpDetailEntitySelected.setValue((BigDecimal) oldValue);
                }
            }
            // calculateAmountByRatioForList();
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "onCellEdit");
        }
    }

    public Boolean validateValue(BigDecimal newValue) {
        try {
            return newValue != null && newValue.compareTo(BigDecimal.ZERO) == 1;
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "validateValue");
            return false;
        }

    }

    //----------------------------------------------
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
//        search();
        if (hosCheckingUpDetailEntityList != null && !hosCheckingUpDetailEntityList.isEmpty()) {

            fillReport(prepareReport(), getUserData().getReportPath() + "checkUpReport.jasper", getHosCheckingUpDetailEntityList(), "pdf");

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
                servletOutputStream.flush();
                servletOutputStream.close();
                httpServletResponse.getOutputStream().flush();
                httpServletResponse.getOutputStream().close();
            } else if ("excel".equals(exportType)) {

            } else if ("html".equals(exportType)) {

            }
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "fillReport");
        }

    }

    public HashMap prepareReport() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            HashMap hashMap = new HashMap();

            if (isFileExist(getUserData().getImageReportPath())) {
                hashMap.put("companyImage", getUserData().getImageReportPath());
            } else {
                hashMap.put("companyImage", null);
            }
            hashMap.put("PrintedBy", getUserData().getUser().getName());
            hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
            hashMap.put("companyName", getUserData().getCompany().getName());
            hashMap.put("phone", getUserData().getUserBranch().getPhone());
            hashMap.put("phoneText", "تليفون/فاكس");
            hashMap.put("address", getUserData().getUserBranch().getAddress1());
            hashMap.put("branchNameEn", getUserData().getUserBranch().getNameEn());
            hashMap.put("companyNameEn", getUserData().getCompany().getNameEn());
            hashMap.put("addressEn", getUserData().getUserBranch().getAddress2());

            hashMap.put("checkUpReportText", "تقرير الكشف");
            hashMap.put("serialText", "رقم الكشف");
            hashMap.put("serialValue", hosCheckingUpEntity.getSerial());
            hashMap.put("glBankNameText", "الخزنة");
            hashMap.put("glBankNameValue", hosCheckingUpEntity.getGlBank().getName());
            hashMap.put("checkUpDateText", "التاريخ");
            hashMap.put("checkUpDateValue", hosCheckingUpEntity.getDate());
            hashMap.put("clinicNameText", "العيادة");
            hashMap.put("clinicNameValue", hosCheckingUpEntity.getHosClinic().getName());
            hashMap.put("doctorNameText", "الطبيب المعالج");
            hashMap.put("doctorNameValue", hosCheckingUpEntity.getHosDoctor().getName());
            hashMap.put("patientNameText", "المريض");
            hashMap.put("patientNameValue", hosCheckingUpEntity.getHosPatientRegistration().getName());
            hashMap.put("PaymentTypeText", "نوع الدفع");
            if (hosCheckingUpEntity.getPaymentType() == 0) {
                hosCheckingUpEntity.setPaymentTypeText("نقدى");
                hashMap.put("paymentTypeValue", hosCheckingUpEntity.getPaymentTypeText());
            } else {
                hosCheckingUpEntity.setPaymentTypeText("بطاقة");
                hashMap.put("paymentTypeValue", hosCheckingUpEntity.getPaymentTypeText());
            }

            hashMap.put("doctorDetailNameText", "اسم الخدمة");
            hashMap.put("valueText", "القيمة");
            hashMap.put("discountRatioText", "نسبة الخصم");
            hashMap.put("bearReatioText", "نسبة التحمل");
            hashMap.put("totalText", "الاجمالى");

            return hashMap;
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "prepareReport");
            return null;
        }
    }

    //-----------------------------------------------
    public List<HosDoctor> completeClinicHosDoctor(String query) {
        try {
            List<HosDoctor> doctorsList = hosClinicsList;
            if (query == null || query.trim().equals("")) {

                hosClinicConverter = new HosDoctorConverter(doctorsList);
                return doctorsList;
            }
            List<HosDoctor> filteredHosDoctors = new ArrayList<>();

            String nameAr;
            String code;
            HosDoctor hosDoctorFilter;
            for (int i = 0; i < hosClinicsList.size(); i++) {
                hosDoctorFilter = doctorsList.get(i);
                nameAr = hosDoctorFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredHosDoctors.contains(hosDoctorFilter)) {
                        filteredHosDoctors.add(hosDoctorFilter);
                    }
                }

                code = hosDoctorFilter.getCode();
                if (code != null && String.valueOf(code).contains(query.toLowerCase())) {
                    if (!filteredHosDoctors.contains(hosDoctorFilter)) {
                        filteredHosDoctors.add(hosDoctorFilter);
                    }
                }
            }

            hosClinicConverter = new HosDoctorConverter(filteredHosDoctors);
            return filteredHosDoctors;
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "completeClinicHosDoctor");
            return null;
        }
    }

    public List<HosDoctor> completeDoctorHosDoctor(String query) {
        try {
            List<HosDoctor> doctorsList = hosDoctorsList;
            if (query == null || query.trim().equals("")) {

                hosDoctorConverter = new HosDoctorConverter(doctorsList);
                return doctorsList;
            }
            List<HosDoctor> filteredHosDoctors = new ArrayList<>();

            String nameAr;
            String code;
            HosDoctor hosDoctorFilter;
            for (int i = 0; i < hosDoctorsList.size(); i++) {
                hosDoctorFilter = doctorsList.get(i);
                nameAr = hosDoctorFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredHosDoctors.contains(hosDoctorFilter)) {
                        filteredHosDoctors.add(hosDoctorFilter);
                    }
                }

                code = hosDoctorFilter.getCode();
                if (code != null && String.valueOf(code).contains(query.toLowerCase())) {
                    if (!filteredHosDoctors.contains(hosDoctorFilter)) {
                        filteredHosDoctors.add(hosDoctorFilter);
                    }
                }
            }

            hosDoctorConverter = new HosDoctorConverter(filteredHosDoctors);
            return filteredHosDoctors;
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "completeDoctorHosDoctor");
            return null;
        }
    }

    public List<HosDoctor> completeHosDoctorService(String query) {
        try {
            List<HosDoctor> patientList = hosDoctorServiceList;
            if (query == null || query.trim().equals("")) {

                hosDoctorServiceConverter = new HosDoctorConverter(patientList);
                return patientList;
            }
            List<HosDoctor> filteredHosDoctors = new ArrayList<>();

            String nameAr;
            String code;
            HosDoctor hosDoctorFilter;
            for (int i = 0; i < hosDoctorServiceList.size(); i++) {
                hosDoctorFilter = patientList.get(i);
                nameAr = hosDoctorFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredHosDoctors.contains(hosDoctorFilter)) {
                        filteredHosDoctors.add(hosDoctorFilter);
                    }
                }

                code = hosDoctorFilter.getCode();
                if (code != null && String.valueOf(code).contains(query.toLowerCase())) {
                    if (!filteredHosDoctors.contains(hosDoctorFilter)) {
                        filteredHosDoctors.add(hosDoctorFilter);
                    }
                }
            }

            hosDoctorServiceConverter = new HosDoctorConverter(filteredHosDoctors);
            return filteredHosDoctors;
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "completeHosDoctorService");
            return null;
        }
    }

    public List<HosPatientRegistration> completeHosPatientRegistration(String query) {
        try {
            List<HosPatientRegistration> hosPatientRegList = hosPatientRegistrationList;
            if (query == null || query.trim().equals("")) {

                hosPatientRegistrationConvertor = new HosPatientRegistrationConvertor(hosPatientRegList);
                return hosPatientRegList;
            }
            List<HosPatientRegistration> filteredHosPatientRegistration = new ArrayList<>();

            String nameAr;
            String code;
            String nationalId;
            HosPatientRegistration hosPatientRegFilter;
            for (int i = 0; i < hosPatientRegistrationList.size(); i++) {
                hosPatientRegFilter = hosPatientRegList.get(i);
                nameAr = hosPatientRegFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredHosPatientRegistration.contains(hosPatientRegFilter)) {
                        filteredHosPatientRegistration.add(hosPatientRegFilter);
                    }
                }

                code = hosPatientRegFilter.getCode();
                if (code != null && String.valueOf(code).contains(query.toLowerCase())) {
                    if (!filteredHosPatientRegistration.contains(hosPatientRegFilter)) {
                        filteredHosPatientRegistration.add(hosPatientRegFilter);
                    }
                }

                nationalId = hosPatientRegFilter.getNationalId();
                if (nationalId != null && String.valueOf(nationalId).contains(query.toLowerCase())) {
                    if (!filteredHosPatientRegistration.contains(hosPatientRegFilter)) {
                        filteredHosPatientRegistration.add(hosPatientRegFilter);
                    }
                }
            }

            hosPatientRegistrationConvertor = new HosPatientRegistrationConvertor(filteredHosPatientRegistration);
            return filteredHosPatientRegistration;
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "completeHosPatientRegistration");
            return null;
        }
    }

    public List<GlBank> completeGlBank(String query) {
        try {
            List<GlBank> glBanksList = glBankList;
            if (query == null || query.trim().equals("")) {

                glBankConverter = new GlBankConverter(glBanksList);
                return glBanksList;
            }
            List<GlBank> filteredGlBanks = new ArrayList<>();

            String nameAr;
            String code;
            GlBank glBankFilter;
            for (int i = 0; i < glBankList.size(); i++) {
                glBankFilter = glBanksList.get(i);
                nameAr = glBankFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredGlBanks.contains(glBankFilter)) {
                        filteredGlBanks.add(glBankFilter);
                    }
                }

                code = glBankFilter.getCode();
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredGlBanks.contains(glBankFilter)) {
                        filteredGlBanks.add(glBankFilter);
                    }
                }
            }

            glBankConverter = new GlBankConverter(filteredGlBanks);
            return filteredGlBanks;
        } catch (Exception e) {
            saveError(e, "CheckingUpFromMB", "completeGlBank");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the glBankConverter
     */
    public GlBankConverter getGlBankConverter() {
        return glBankConverter;
    }

    /**
     * @param glBankConverter the glBankConverter to set
     */
    public void setGlBankConverter(GlBankConverter glBankConverter) {
        this.glBankConverter = glBankConverter;
    }

    /**
     * @return the glBankList
     */
    public List<GlBank> getGlBankList() {
        return glBankList;
    }

    /**
     * @param glBankList the glBankList to set
     */
    public void setGlBankList(List<GlBank> glBankList) {
        this.glBankList = glBankList;
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
     * @return the hosCheckingUpDetailEntityList
     */
    public List<HosCheckingUpDetailEntity> getHosCheckingUpDetailEntityList() {
        return hosCheckingUpDetailEntityList;
    }

    /**
     * @param hosCheckingUpDetailEntityList the hosCheckingUpDetailEntityList to
     * set
     */
    public void setHosCheckingUpDetailEntityList(List<HosCheckingUpDetailEntity> hosCheckingUpDetailEntityList) {
        this.hosCheckingUpDetailEntityList = hosCheckingUpDetailEntityList;
    }

    /**
     * @return the hosCheckUpId
     */
    public Integer getHosCheckUpId() {
        return hosCheckUpId;
    }

    /**
     * @param hosCheckUpId the hosCheckUpId to set
     */
    public void setHosCheckUpId(Integer hosCheckUpId) {
        this.hosCheckUpId = hosCheckUpId;
    }

    /**
     * @return the hosCheckUp
     */
    public HosCheckUp getHosCheckUp() {
        return hosCheckUp;
    }

    /**
     * @param hosCheckUp the hosCheckUp to set
     */
    public void setHosCheckUp(HosCheckUp hosCheckUp) {
        this.hosCheckUp = hosCheckUp;
    }

    /**
     * @return the hosCheckUpDetailsList
     */
    public List<HosCheckUpDetail> getHosCheckUpDetailsList() {
        return hosCheckUpDetailsList;
    }

    /**
     * @param hosCheckUpDetailsList the hosCheckUpDetailsList to set
     */
    public void setHosCheckUpDetailsList(List<HosCheckUpDetail> hosCheckUpDetailsList) {
        this.hosCheckUpDetailsList = hosCheckUpDetailsList;
    }

    /**
     * @return the hosDoctorConverter
     */
    public HosDoctorConverter getHosDoctorConverter() {
        return hosDoctorConverter;
    }

    /**
     * @param hosDoctorConverter the hosDoctorConverter to set
     */
    public void setHosDoctorConverter(HosDoctorConverter hosDoctorConverter) {
        this.hosDoctorConverter = hosDoctorConverter;
    }

    /**
     * @return the hosDoctorsList
     */
    public List<HosDoctor> getHosDoctorsList() {
        if (hosCheckingUpEntity.getHosClinic() != null) {
            hosDoctorsList = hosDoctorService.getAllSubHosDoctorByBranchIdActiveAndLevelAndClinicId(getUserData().getUserBranch().getId(), 2, hosCheckingUpEntity.getHosClinic().getId());
            hosDoctorConverter = new HosDoctorConverter(hosDoctorsList);
        }   
        return hosDoctorsList;
    }

    /**
     * @param hosDoctorsList the hosDoctorsList to set
     */
    public void setHosDoctorsList(List<HosDoctor> hosDoctorsList) {
        this.hosDoctorsList = hosDoctorsList;
    }

    /**
     * @return the hosPatientRegistrationList
     */
    public List<HosPatientRegistration> getHosPatientRegistrationList() {
        return hosPatientRegistrationList;
    }

    /**
     * @param hosPatientRegistrationList the hosPatientRegistrationList to set
     */
    public void setHosPatientRegistrationList(List<HosPatientRegistration> hosPatientRegistrationList) {
        this.hosPatientRegistrationList = hosPatientRegistrationList;
    }

    /**
     * @return the hosPatientRegistrationConvertor
     */
    public HosPatientRegistrationConvertor getHosPatientRegistrationConvertor() {
        return hosPatientRegistrationConvertor;
    }

    /**
     * @param hosPatientRegistrationConvertor the
     * hosPatientRegistrationConvertor to set
     */
    public void setHosPatientRegistrationConvertor(HosPatientRegistrationConvertor hosPatientRegistrationConvertor) {
        this.hosPatientRegistrationConvertor = hosPatientRegistrationConvertor;
    }

    /**
     * @return the hosCheckingUpDetailEntitySelected
     */
    public HosCheckingUpDetailEntity getHosCheckingUpDetailEntitySelected() {
        return hosCheckingUpDetailEntitySelected;
    }

    /**
     * @param hosCheckingUpDetailEntitySelected the
     * hosCheckingUpDetailEntitySelected to set
     */
    public void setHosCheckingUpDetailEntitySelected(HosCheckingUpDetailEntity hosCheckingUpDetailEntitySelected) {
        this.hosCheckingUpDetailEntitySelected = hosCheckingUpDetailEntitySelected;
    }

    /**
     * @return the hosCheckingUpDetailEntityMemory
     */
    public HosCheckingUpDetailEntity getHosCheckingUpDetailEntityMemory() {
        return hosCheckingUpDetailEntityMemory;
    }

    /**
     * @param hosCheckingUpDetailEntityMemory the
     * hosCheckingUpDetailEntityMemory to set
     */
    public void setHosCheckingUpDetailEntityMemory(HosCheckingUpDetailEntity hosCheckingUpDetailEntityMemory) {
        this.hosCheckingUpDetailEntityMemory = hosCheckingUpDetailEntityMemory;
    }

    /**
     * @return the hosDoctorServiceList
     */
    public List<HosDoctor> getHosDoctorServiceList() {
        return hosDoctorServiceList;
    }

    /**
     * @param hosDoctorServiceList the hosDoctorServiceList to set
     */
    public void setHosDoctorServiceList(List<HosDoctor> hosDoctorServiceList) {
        this.hosDoctorServiceList = hosDoctorServiceList;
    }

    /**
     * @return the hosDoctorServiceConverter
     */
    public HosDoctorConverter getHosDoctorServiceConverter() {
        return hosDoctorServiceConverter;
    }

    /**
     * @param hosDoctorServiceConverter the hosDoctorServiceConverter to set
     */
    public void setHosDoctorServiceConverter(HosDoctorConverter hosDoctorServiceConverter) {
        this.hosDoctorServiceConverter = hosDoctorServiceConverter;
    }

    /**
     * @return the hosCheckingUpDetailDeletedEntityList
     */
    public List<HosCheckingUpDetailEntity> getHosCheckingUpDetailDeletedEntityList() {
        return hosCheckingUpDetailDeletedEntityList;
    }

    /**
     * @param hosCheckingUpDetailDeletedEntityList the
     * hosCheckingUpDetailDeletedEntityList to set
     */
    public void setHosCheckingUpDetailDeletedEntityList(List<HosCheckingUpDetailEntity> hosCheckingUpDetailDeletedEntityList) {
        this.hosCheckingUpDetailDeletedEntityList = hosCheckingUpDetailDeletedEntityList;
    }

    /**
     * @return the totalCach
     */
    public String getTotalCach() {
        return totalCach;
    }

    /**
     * @param totalCach the totalCach to set
     */
    public void setTotalCach(String totalCach) {
        this.totalCach = totalCach;
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
     * @return the hosDoctorUpdated
     */
    public HosDoctor getHosDoctorUpdated() {
        return hosDoctorUpdated;
    }

    /**
     * @param hosDoctorUpdated the hosDoctorUpdated to set
     */
    public void setHosDoctorUpdated(HosDoctor hosDoctorUpdated) {
        this.hosDoctorUpdated = hosDoctorUpdated;
    }

    /**
     * @return the hosClinicsList
     */
    public List<HosDoctor> getHosClinicsList() {
        return hosClinicsList;
    }

    /**
     * @param hosClinicsList the hosClinicsList to set
     */
    public void setHosClinicsList(List<HosDoctor> hosClinicsList) {
        this.hosClinicsList = hosClinicsList;
    }

    /**
     * @return the hosClinicConverter
     */
    public HosDoctorConverter getHosClinicConverter() {
        return hosClinicConverter;
    }

    /**
     * @param hosClinicConverter the hosClinicConverter to set
     */
    public void setHosClinicConverter(HosDoctorConverter hosClinicConverter) {
        this.hosClinicConverter = hosClinicConverter;
    }

    /**
     * @return the hosClinicUpdated
     */
    public HosDoctor getHosClinicUpdated() {
        return hosClinicUpdated;
    }

    /**
     * @param hosClinicUpdated the hosClinicUpdated to set
     */
    public void setHosClinicUpdated(HosDoctor hosClinicUpdated) {
        this.hosClinicUpdated = hosClinicUpdated;
    }

    /**
     * @return the showPatientDetails
     */
    public boolean isShowPatientDetails() {
        return showPatientDetails;
    }

    /**
     * @param showPatientDetails the showPatientDetails to set
     */
    public void setShowPatientDetails(boolean showPatientDetails) {
        this.showPatientDetails = showPatientDetails;
    }

    /**
     * @return the hosCheckingUpDetailEntityReport
     */
    public List<HosCheckingUpDetailEntity> getHosCheckingUpDetailEntityReport() {
        return hosCheckingUpDetailEntityReport;
    }

    /**
     * @param hosCheckingUpDetailEntityReport the
     * hosCheckingUpDetailEntityReport to set
     */
    public void setHosCheckingUpDetailEntityReport(List<HosCheckingUpDetailEntity> hosCheckingUpDetailEntityReport) {
        this.hosCheckingUpDetailEntityReport = hosCheckingUpDetailEntityReport;
    }

    /**
     * @return the rscWorkUnitsGuideDTOConverter
     */
    public RscWorkUnitsGuideDTOConverter getRscWorkUnitsGuideDTOConverter() {
        return rscWorkUnitsGuideDTOConverter;
    }

    /**
     * @param rscWorkUnitsGuideDTOConverter the rscWorkUnitsGuideDTOConverter to set
     */
    public void setRscWorkUnitsGuideDTOConverter(RscWorkUnitsGuideDTOConverter rscWorkUnitsGuideDTOConverter) {
        this.rscWorkUnitsGuideDTOConverter = rscWorkUnitsGuideDTOConverter;
    }

}
