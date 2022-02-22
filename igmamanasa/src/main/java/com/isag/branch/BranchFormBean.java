/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.branch;

import com.isag.businessservice.BranchService;
import com.isag.businessservice.CompanyService;
import com.isag.define.CompanyActivityClassEnum;
import com.isag.entity.Branch;
import com.isag.entity.IgmaCompany;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
import com.isag.test.Message;
import com.isag.uploadfile.FileUploadController;
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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

//@Named(value = "branchFormBean")
//@ManagedBean(name = "branchFormBean", eager = true)
@Named(value = "branchFormBean")
@ViewScoped
public class BranchFormBean extends BaseFormBean {

    private Integer serial;
    private Integer branchId;
    private String nameAr;
    private String nameEn;
    private String address1;
    private String address2;
    private String address3;
    private String phone;
    private String mobile;
    private String fax;
    private String email;
    private String taxCode;
    private String iconPath;
    private UserData user;
    private String ScreenMode;
    private byte[] contents;
    private List<IgmaCompany> userCompany;
    private Integer selectedCompanyId;
    private String fileName;
    private String imageName;
    private StreamedContent image;
    private boolean showMessage = Boolean.FALSE;
    private String uploadedImage;
    private String showImage;
    private List<Branch> branches;
    private boolean validateSerial;
    private int limitMessages = 0;
    private FileUploadController fileUploadController = new FileUploadController();
    private FileUploadEvent fileUploadEvent;
    private StreamedContent file;
    private CompanyActivityClassEnum companyActivity;

    @EJB
    private BranchService brancheService;
    @EJB
    private CompanyService companyService;
    private Message messageBean;

    public BranchFormBean() {

    }

    @Override
    @PostConstruct
    public void init() {
        try {
            branches = new ArrayList<>();
            load();
        } catch (Exception e) {
            saveError(e, "BranchFormBean", "init");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            user = (UserData) context.getSessionMap().get("userLogInData");
            ScreenMode = ((String) context.getSessionMap().get("ScreenMode"));

            if (user.getCompany() != null && user.getCompany().getId() != null) {
                branches = brancheService.getAllBranchByCompanyId(user.getCompany().getId());
            } else {
                branches = brancheService.getAllBranch();
            }

            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("add")) {
                setBranchDataEmpty();

            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
                try {
                    branchId = (Integer) context.getSessionMap().get("brancheselected");
                    setEditedBranchValues(branchId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            saveError(e, "BranchFormBean", "load");
        }
    }
    
    public CompanyActivityClassEnum[] getCompanyActivityClassEnum() {
        return CompanyActivityClassEnum.values();
    }

    public void upload(FileUploadEvent event) {
        try {
            fileUploadEvent = event;
            setUploadedImage(fileUploadController.upload(event));
            setShowImage((getUploadedImage() != null) ? fileUploadController.getDestination().concat(getUploadedImage()) : fileUploadController.getDestination().concat("1.png"));
            setFileName(getUploadedImage());
            setImageName(fileName);
            setImage(getMessageBean().findGraphicImage(fileName));
        } catch (Exception e) {
            saveError(e, "BranchFormBean", "upload");
        }
    }

    public void copyfile() {
        try {

            fileUploadController.copyFile(fileUploadEvent.getFile().getFileName(), fileUploadEvent.getFile().getInputstream(),
                    fileUploadController.getDestination());
            setUploadedImage(fileUploadEvent.getFile().getFileName());
        } catch (Exception e) {
            saveError(e, "BranchFormBean", "copyfile");
        }

    }

    public void CloseDlg(String dlgvar) {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('" + dlgvar + "').hide();");
        } catch (Exception e) {
            saveError(e, "BranchFormBean", "CloseDlg");
        }
    }

    public void OpenDlg(String dlgvar) {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('" + dlgvar + "').show();");
        } catch (Exception e) {
            saveError(e, "BranchFormBean", "OpenDlg");
        }
    }

    @Override
    public String save() {
        try {
            showMessage = Boolean.TRUE;
//        validateSerial = true;
            limitMessages = 0;
//        validateSerial();

            if (nameAr != null && selectedCompanyId != null && !nameAr.isEmpty() && serial != null) {

                Branch branch = new Branch();
                branch.setNameAr(getNameAr());
                branch.setNameEn(getNameEn());
                branch.setAddress1(getAddress1());
                branch.setAddress2(getAddress2());
                branch.setAddress3(getAddress3());
                branch.setPhone(getPhone());
                branch.setMobile(getMobile());
                branch.setTaxCode(taxCode);
                branch.setFax(getFax());
                branch.setEmail(getEmail());
                branch.setSerial(getSerial());
                // branch.setIconPath(getIconPath());
                if (user.getCompany() != null) {
                    branch.setCompanyId(user.getCompany());
                } else if (selectedCompanyId != null) {
                    IgmaCompany companySelected = new IgmaCompany();
                    companySelected.setId(selectedCompanyId);
                    branch.setCompanyId(companySelected);
                }

                branch.setModifiedBy(user.getUser());
                branch.setModificationDate(new Date());

                branch.setImage(getImageName());

                if (getScreenMode().equalsIgnoreCase("add")) {
                    try {
                        Branch savedBranch = new Branch();
                        savedBranch = brancheService.addBranch(branch, user.getUser());

                        if (savedBranch != null) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, user.getUserDDs().get("INFO"), user.getUserDDs().get("SAVED")));
                            exit("../branch/branchList.xhtml");
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, user.getUserDDs().get("ERROR"), user.getUserDDs().get("UNIQE_SERIAL")));
                            return "";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, user.getUserDDs().get("ERROR"), user.getUserDDs().get("UNIQE_SERIAL")));
                        return "";
                    }

//                Integer branchid = branch.getId();
//                if (contents != null) {
//                    InputStream is = new ByteArrayInputStream(contents);
//
//                    BufferedImage bi;
//                    try {
//                        bi = ImageIO.read(is);
//                        File outputfile = new File("C:\\image\\" + branchid + ".jpg");
//                        ImageIO.write(bi, "jpg", outputfile);
//                    } catch (IOException ex) {
//                        Logger.getLogger(BranchFormBean.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//
//                    iconPath = branch.getId() + ".jpg";
//                    branch.setIconPath(iconPath);
//                }
                } else if (getScreenMode().equalsIgnoreCase("edit")) {
//                if (contents != null) {
//                    InputStream is = new ByteArrayInputStream(contents);
//                    BufferedImage bi;
//                    try {
//                        bi = ImageIO.read(is);
//                        File outputfile = new File("C:\\image\\" + branchId + ".jpg");
//                        ImageIO.write(bi, "jpg", outputfile);
//
//                    } catch (IOException ex) {
//                        Logger.getLogger(BranchFormBean.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
                    iconPath = branchId + ".jpg";

                    branch.setIconPath(iconPath);
                    branch.setId(branchId);
                    branch.setModifiedBy(user.getUser());
                    branch.setModificationDate(new Date());
                    try {
                        Branch updatedBranch = new Branch();
                        updatedBranch = brancheService.addBranch(branch, user.getUser());
                        if (updatedBranch != null) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, user.getUserDDs().get("INFO"), user.getUserDDs().get("SAVED")));
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, user.getUserDDs().get("ERROR"), user.getUserDDs().get("UNIQE_SERIAL")));
                            return "";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, user.getUserDDs().get("ERROR"), user.getUserDDs().get("UNIQE_SERIAL")));
                        return "";
                    }
                    exit("../branch/branchList.xhtml");
                }

            }
            return "";
        } catch (Exception e) {
            saveError(e, "BranchFormBean", "save");
            return null;
        }
    }

    public void setEditedBranchValues(Integer bId) {
        try {
            Branch branch = getBrancheService().findBranch(bId);
            setFileName((branch.getImage() != null && !branch.getImage().isEmpty()) ? branch.getImage() : "1.png");
            setShowImage((branch.getImage() != null && !branch.getImage().isEmpty()) ? fileUploadController.getDestination().concat(branch.getImage()) : fileUploadController.getDestination() + "1.png");

            setUploadedImage(branch.getImage() != null ? branch.getImage() : null);

            selectedCompanyId = branch.getCompanyId().getId();
            setAddress1(branch.getAddress1());
            setAddress2(branch.getAddress2());
            setAddress3(branch.getAddress3());
            setEmail(branch.getEmail());
            setFax(branch.getFax());
            setIconPath(branch.getIconPath());
            setMobile(branch.getMobile());
            setTaxCode(branch.getTaxCode());
            setNameAr(branch.getNameAr());
            setNameEn(branch.getNameEn());
            setPhone(branch.getPhone());
            setSerial(branch.getSerial());
            userCompany = new ArrayList<>();
            setImage(getMessageBean().findGraphicImage(fileName));
            if (user.getCompany() != null) {
                userCompany.add(user.getCompany());

            } else {
                try {
                    userCompany = companyService.getAllCompanies();
                } catch (Exception e) {
                }

            }
        } catch (Exception e) {
            saveError(e, "BranchFormBean", "setEditedBranchValues");
        }
    }

    public void setBranchDataEmpty() {
        try {
            setNameEn("");
            setAddress1("");
            setAddress2("");
            setAddress3("");
            setPhone(getPhone());
            setMobile("");
            setTaxCode("");
            setFax("");
            setEmail("");
            setIconPath("");
            setSerial(null);
            setFileName("1.png");
            setShowImage("1.png");
            setUploadedImage("");

            userCompany = new ArrayList<>();
            if (user.getCompany() != null) {
                userCompany.add(user.getCompany());
                selectedCompanyId = user.getCompany().getId();
            } else {
                try {
                    userCompany = companyService.getAllCompanies();
                } catch (Exception e) {
                }

            }
        } catch (Exception e) {
            saveError(e, "BranchFormBean", "setBranchDataEmpty");
        }
    }

    public String cancel() {
        try {
            exit("../branch/branchList.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "BranchFormBean", "cancel");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        return "branch";

    }

 
    public String getNameAr() {
        return nameAr;
    }

    /**
     * @param nameAr the nameAr to set
     */
    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    /**
     * @return the nameEn
     */
    public String getNameEn() {
        return nameEn;
    }

    /**
     * @param nameEn the nameEn to set
     */
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    /**
     * @return the address1
     */
    public String getAddress1() {
        return address1;
    }

    /**
     * @param address1 the address1 to set
     */
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    /**
     * @return the address2
     */
    public String getAddress2() {
        return address2;
    }

    /**
     * @param address2 the address2 to set
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    /**
     * @return the address3
     */
    public String getAddress3() {
        return address3;
    }

    /**
     * @param address3 the address3 to set
     */
    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the fax
     */
    public String getFax() {
        return fax;
    }

    /**
     * @param fax the fax to set
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the iconPath
     */
    public String getIconPath() {
        return iconPath;
    }

    /**
     * @param iconPath the iconPath to set
     */
    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    /**
     * @return the user
     */
    public UserData getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(UserData user) {
        this.user = user;
    }

    /**
     * @return the screenMode
     */
    public String getScreenMode() {
        return ScreenMode;
    }

    /**
     * @param screenMode the screenMode to set
     */
    public void setScreenMode(String ScreenMode) {
        this.ScreenMode = ScreenMode;
    }

    public BranchService getBrancheService() {
        return brancheService;
    }

    public void setBrancheService(BranchService brancheService) {
        this.brancheService = brancheService;
    }

    /**
     * @return the branchId
     */
    public Integer getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getContents() {
        return contents;
    }

    public void setContents(byte[] contents) {
        this.contents = contents;
    }

    public List<IgmaCompany> getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(List<IgmaCompany> userCompany) {
        this.userCompany = userCompany;
    }

    public Integer getSelectedCompanyId() {
        return selectedCompanyId;
    }

    public void setSelectedCompanyId(Integer selectedCompanyId) {
        this.selectedCompanyId = selectedCompanyId;
    }

    public StreamedContent getImage() {
        return image;
    }

    public void setImage(StreamedContent image) {
        this.image = image;
    }

    /**
     * @return the serial
     */
    public Integer getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public boolean isShowMessage() {
        return showMessage;
    }

    public void setShowMessage(boolean showMessage) {
        this.showMessage = showMessage;
    }

    public String getShowImage() {
        return showImage;
    }

    public void setShowImage(String showImage) {
        this.showImage = showImage;
    }

    public FileUploadController getFileUploadController() {
        return fileUploadController;
    }

    public void setFileUploadController(FileUploadController fileUploadController) {
        this.fileUploadController = fileUploadController;
    }

    public String getUploadedImage() {
        return uploadedImage;
    }

    public void setUploadedImage(String uploadedImage) {
        this.uploadedImage = uploadedImage;
    }

    public FileUploadEvent getFileUploadEvent() {
        return fileUploadEvent;
    }

    public void setFileUploadEvent(FileUploadEvent fileUploadEvent) {
        this.fileUploadEvent = fileUploadEvent;
    }

    /**
     * @return the branches
     */
    public List<Branch> getBranches() {
        return branches;
    }

    /**
     * @param branches the branches to set
     */
    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

    /**
     * @return the validateSerial
     */
    public boolean isValidateSerial() {
        return validateSerial;
    }

    /**
     * @param validateSerial the validateSerial to set
     */
    public void setValidateSerial(boolean validateSerial) {
        this.validateSerial = validateSerial;
    }

    /**
     * @return the limitMessages
     */
    public int getLimitMessages() {
        return limitMessages;
    }

    /**
     * @param limitMessages the limitMessages to set
     */
    public void setLimitMessages(int limitMessages) {
        this.limitMessages = limitMessages;
    }

    /**
     * @return the taxCode
     */
    public String getTaxCode() {
        return taxCode;
    }

    /**
     * @param taxCode the taxCode to set
     */
    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    /**
     * @return the imageName
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * @param imageName the imageName to set
     */
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    /**
     * @return the messageBean
     */
    public Message getMessageBean() {
        if (messageBean == null) {
            messageBean = new Message();
        }
        return messageBean;
    }

    /**
     * @param messageBean the messageBean to set
     */
    public void setMessageBean(Message messageBean) {
        this.messageBean = messageBean;
    }

    /**
     * @return the companyActivity
     */
    public CompanyActivityClassEnum getCompanyActivity() {
        return companyActivity;
    }

    /**
     * @param companyActivity the companyActivity to set
     */
    public void setCompanyActivity(CompanyActivityClassEnum companyActivity) {
        this.companyActivity = companyActivity;
    }
    
    /**
     * @return the file
     */
    public StreamedContent getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(StreamedContent file) {
        this.file = file;
    }

}
