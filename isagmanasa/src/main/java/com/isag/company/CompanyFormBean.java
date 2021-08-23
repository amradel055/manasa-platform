/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.company;

import com.isag.businessservice.CompanyService;
import com.isag.businessservice.SymbolService;
import com.isag.entity.CompanyLanguage;
import com.isag.entity.IsagCompany;
import com.isag.entity.Symbol;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
import com.isag.uploadfile.FileUploadController;
import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.model.UploadedFile;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author hq004
 */
@Named(value = "companyFormBean")
@ViewScoped
public class CompanyFormBean extends BaseFormBean {

    private String companyName;
    private String companyNameEn;

    private String companyCode;
    private String screenMode;
    private Integer companyId;
    private String code_bussiness;
    private String resposibility;
    private String phone;
    private String fax;
    private UploadedFile file;
    private String address;
    private String addressEn;
    private UserData userData;
    private IsagCompany companySave;
    private String fileName;
    private String uploadedImage;

    @EJB
    private CompanyService companyService;
    @EJB
    private SymbolService symbolService;

    private List<String> selectedLanguageIds;

    private List<Symbol> allSupportedLanguages;
    private List<String> selectedLanguagesBackup;
    private List<Symbol> selectedLanguages;

    private String showImage;
    private String showDialog;

    private FileUploadEvent fileUploadEvent;

    private FileUploadController fileUploadController;

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
            companySave = new IsagCompany();
        } catch (Exception e) {
            saveError(e, "CompanyFormBean", "init");
        }
    }

    @Override
    public void load() {
        try {

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");
            screenMode = ((String) context.getSessionMap().get("companyScreenMode"));
            allSupportedLanguages = symbolService.getAllSupportedLanguages();

            if (getScreenMode().equalsIgnoreCase("add")) {
                setCompanyValuesEmpty();
            } else if (getScreenMode().equalsIgnoreCase("edit")) {
                try {
                    setCompanyId((Integer) context.getSessionMap().get("selectedCompany"));
                    selectedLanguages = symbolService.getAllLanguagesListByCompanyId(companyId);
                    selectedLanguagesBackup = new ArrayList<>();
                    selectedLanguageIds = new ArrayList<>();
                    for (Symbol selectedLanguage : selectedLanguages) {
                        selectedLanguagesBackup.add(selectedLanguage.getId().toString());
                        selectedLanguageIds.add(selectedLanguage.getId().toString());
                    }
                    IsagCompany company = companyService.findCompany(getCompanyId());
                    setCompanyName(company.getName());
                    setCompanyNameEn(company.getNameEn());
                    setCompanyCode(company.getCode());
                    setCode_bussiness(company.getCore_business());
                    setFax(company.getFax());
                    setPhone(company.getPhone());
                    setResposibility(company.getResponsible());
                    setAddress(company.getAddress());
                    setAddressEn(company.getAddressEn());
                    setFileName(company.getImage() != null ? company.getImage() : "1.png");
                    setShowImage(company.getImage() != null ? fileUploadController.getDestination().concat(company.getImage()) : fileUploadController.getDestination() + "1.png");
                    setUploadedImage(company.getImage() != null ? company.getImage() : null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            saveError(e, "CompanyFormBean", "load");
        }
    }

    public void upload(FileUploadEvent event) {
        try {
//        UploadedFile uploadedFile = event.getFile();
//        fileName = uploadedFile.getFileName();
//        String contentType = uploadedFile.getContentType();
//        byte[] contents = uploadedFile.getContents();
//
//        companySave.setLogo(contents);

            fileUploadEvent = event;
            fileUploadController.upload(event);
//        setUploadedImage((uploadedFile == null) ? getUploadedImage() : uploadedFile);
//
//        setShowImage((uploadedFile != null) ? fileUploadController.getDestination().concat(uploadedFile) : fileUploadController.getDestination().concat("1.png"));
//        setFileName((uploadedFile == null) ? getUploadedImage() : uploadedFile);
//        if (uploadedFile == null) {
//            OpenDlg("dlg1");
//        }
        } catch (Exception e) {
            saveError(e, "CompanyFormBean", "upload");
        }

    }

    public void copyfile() throws IOException {
        try {

            fileUploadController.copyFile(fileUploadEvent.getFile().getFileName(), fileUploadEvent.getFile().getInputstream(),
                    fileUploadController.getDestination());
            setUploadedImage(fileUploadEvent.getFile().getFileName());
        } catch (Exception e) {
            saveError(e, "CompanyFormBean", "copyfile");
        }
    }

    public void CloseDlg(String dlgvar) {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('" + dlgvar + "').hide();");
        } catch (Exception e) {
            saveError(e, "CompanyFormBean", "CloseDlg");
        }
    }

    public void OpenDlg(String dlgvar) {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('" + dlgvar + "').show();");
        } catch (Exception e) {
            saveError(e, "CompanyFormBean", "OpenDlg");
        }
    }

    @Override
    public String save() {
        try {

            if (companyCode != null && companyName != null) {
                companySave.setCode(getCompanyCode());
                companySave.setName(getCompanyName());
                companySave.setNameEn(getCompanyNameEn());
                companySave.setAddress(getAddress());
                companySave.setAddressEn(getAddressEn());
                companySave.setCore_business(getCode_bussiness());
                companySave.setPhone(getPhone());
                companySave.setFax(getFax());
                companySave.setResponsible(getResposibility());
                companySave.setModifiedBy(userData.getUser());
                companySave.setModificationDate(new Date());

                companySave.setImage(getUploadedImage());

                if (getScreenMode().equalsIgnoreCase("add")) {
                    companySave.setCreatedBy(userData.getUser());
                    try {
                        companySave = companyService.addCompany(companySave);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userData.getUserDDs().get("INFO"), userData.getUserDDs().get("SAVED")));

                    } catch (Exception e) {
                        e.printStackTrace();
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userData.getUserDDs().get("ERROR"), userData.getUserDDs().get("DUBLICATED")));
                        return null;
                    }
                    for (int i = 0; i < selectedLanguageIds.size(); i++) {
                        Symbol selectedLanguage = findLanguageById(Integer.decode(selectedLanguageIds.get(i)));
                        CompanyLanguage companyLanguage = new CompanyLanguage();
                        companyLanguage.setCompanyId(companySave);
                        companyLanguage.setSymbol(selectedLanguage);
                        companyLanguage.setCreatedBy(userData.getUser());
                        companyLanguage.setCreationDate(new Date());
                        companyService.addCompanyLanguage(companyLanguage);
                    }
                }
                if (getScreenMode().equalsIgnoreCase("edit")) {
                    companySave.setId(getCompanyId());
                    companySave.setAddress(getAddress());
                    companySave.setCode(getCompanyCode());
                    companySave.setCore_business(getCode_bussiness());
                    companySave.setFax(getFax());
                    companySave.setName(getCompanyName());
                    companySave.setResponsible(getResposibility());
                    companySave.setPhone(getPhone());
                    try {
                        companyService.updateCompany(companySave);
                        for (String newLang : selectedLanguageIds) {
                            if (!selectedLanguagesBackup.contains(newLang)) {
                                Symbol selectedLanguage = findLanguageById(Integer.decode(newLang));
                                CompanyLanguage companyLanguage = new CompanyLanguage();
                                companyLanguage.setCompanyId(companySave);
                                companyLanguage.setSymbol(selectedLanguage);
                                companyLanguage.setCreatedBy(userData.getUser());
                                companyLanguage.setCreationDate(new Date());
                                companyService.addCompanyLanguage(companyLanguage);
                            }
                        }
                        for (String oldLang : selectedLanguagesBackup) {
                            if (!selectedLanguageIds.contains(oldLang)) {
                                companyService.removeCompanyLanguage(companySave.getId(), Integer.decode(oldLang));
                            }
                        }
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userData.getUserDDs().get("INFO"), userData.getUserDDs().get("SAVED")));
                    } catch (Exception e) {
                        e.printStackTrace();
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userData.getUserDDs().get("ERROR"), userData.getUserDDs().get("DUBLICATED")));
                        return null;
                    }
                }
                exit("../company/companyList.xhtml");
            }
            return "";
        } catch (Exception e) {
            saveError(e, "CompanyFormBean", "save");
            return null;
        }
    }

    public void validateSave() {
        try {
            if (getScreenMode().equalsIgnoreCase("add")) {

            }
            if (getScreenMode().equalsIgnoreCase("edit")) {

            }
        } catch (Exception e) {
            saveError(e, "CompanyFormBean", "validateSave");
        }
    }

    private Symbol findLanguageById(Integer languageId) {
        try {
            for (Symbol allSupportedLanguage : allSupportedLanguages) {
                if (allSupportedLanguage.getId().intValue() == languageId.intValue()) {
                    return allSupportedLanguage;
                }
            }
            return null;
        } catch (Exception e) {
            saveError(e, "CompanyFormBean", "findLanguageById");
            return null;
        }
    }

    private void setCompanyValuesEmpty() {
        try {
            companyName = "";
            companyCode = "";
            code_bussiness = "";
            phone = "";
            fax = "";
            file = null;
            address = "";
            setFileName("1.png");
            setShowImage("");
            setUploadedImage("");
        } catch (Exception e) {
            saveError(e, "CompanyFormBean", "setCompanyValuesEmpty");
        }
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public IsagCompany getCompanySave() {
        return companySave;
    }

    public void setCompanySave(IsagCompany companySave) {
        this.companySave = companySave;
    }

    /**
     * @return the selectedLanguages
     */
    public List<Symbol> getSelectedLanguages() {
        return selectedLanguages;
    }

    /**
     * @param selectedLanguages the selectedLanguages to set
     */
    public void setSelectedLanguages(List<Symbol> selectedLanguages) {
        this.selectedLanguages = selectedLanguages;
    }

    /**
     * @return the allSupportedLanguages
     */
    public List<Symbol> getAllSupportedLanguages() {
        return allSupportedLanguages;
    }

    /**
     * @param allSupportedLanguages the allSupportedLanguages to set
     */
    public void setAllSupportedLanguages(List<Symbol> allSupportedLanguages) {
        this.allSupportedLanguages = allSupportedLanguages;
    }

    /**
     * @return the selectedLanguageIds
     */
    public List<String> getSelectedLanguageIds() {
        return selectedLanguageIds;
    }

    /**
     * @param selectedLanguageIds the selectedLanguageIds to set
     */
    public void setSelectedLanguageIds(List<String> selectedLanguageIds) {
        this.selectedLanguageIds = selectedLanguageIds;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public FileUploadController getFileUploadController() {
        return fileUploadController;
    }

    public void setFileUploadController(FileUploadController fileUploadController) {
        this.fileUploadController = fileUploadController;
    }

    public String getShowImage() {
        return showImage;
    }

    public void setShowImage(String showImage) {
        this.showImage = showImage;
    }

    public String getUploadedImage() {
        return uploadedImage;
    }

    public void setUploadedImage(String uploadedImage) {
        this.uploadedImage = uploadedImage;
    }

    public String getShowDialog() {
        return showDialog;
    }

    public void setShowDialog(String showDialog) {
        this.showDialog = showDialog;
    }

    public FileUploadEvent getFileUploadEvent() {
        return fileUploadEvent;
    }

    public void setFileUploadEvent(FileUploadEvent fileUploadEvent) {
        this.fileUploadEvent = fileUploadEvent;
    }

    public String cancel() {
        exit("../company/companyList.xhtml");
        return "";
    }

    public String cancelDialog() {
        exit("../company/companyForm.xhtml");
        return "";
    }

    @Override
    public String getScreenName() {
        return "Company";
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getScreenMode() {
        return screenMode;
    }

    public void setScreenMode(String screenMode) {
        this.screenMode = screenMode;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCode_bussiness() {
        return code_bussiness;
    }

    public void setCode_bussiness(String code_bussiness) {
        this.code_bussiness = code_bussiness;
    }

    public String getResposibility() {
        return resposibility;
    }

    public void setResposibility(String resposibility) {
        this.resposibility = resposibility;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the companyNameEn
     */
    public String getCompanyNameEn() {
        return companyNameEn;
    }

    /**
     * @param companyNameEn the companyNameEn to set
     */
    public void setCompanyNameEn(String companyNameEn) {
        this.companyNameEn = companyNameEn;
    }

    /**
     * @return the addressEn
     */
    public String getAddressEn() {
        return addressEn;
    }

    /**
     * @param addressEn the addressEn to set
     */
    public void setAddressEn(String addressEn) {
        this.addressEn = addressEn;
    }
}
