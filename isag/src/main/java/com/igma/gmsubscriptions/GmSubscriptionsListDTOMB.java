/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.gmsubscriptions;

import com.igma.basic.GMBasicData;
import com.isag.businessservice.GmSubscriptionsService;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.converter.InvOrganizationSiteDTOConverter;
import com.isag.dto.GmSubscriptionsDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.isag.UserData;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author WIN7
 */
@Named(value = "gmSubscriptionsListDTOMB")
@ViewScoped
public class GmSubscriptionsListDTOMB extends GMBasicData {

    private String nameClient;
    private LazyDataModel<GmSubscriptionsDTO> dataModel;

    private List<GmSubscriptionsDTO> subscribtionList;
    private List<InvOrganizationSiteDTO> invOrganizationSiteDTOList;
    private InvOrganizationSiteDTOConverter organizationSiteDTOConvertor;

    private String filename;
    private String file;
    private String host;
    private String access;
    @EJB
    private GmSubscriptionsService gmSubscriptionsService;

    @EJB
    private OrganizationSiteService organizationSiteService;

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "init");
        }
    }

    public void reset() {
        getGmFreezeDto().setNumber(null);
    }

    public void calcDate() {
        if (getGmFreezeDto().getNumber() != null && getGmFreezeDto().getNumber().compareTo(getGmFreezeDto().getRemainDays()) != 1) {
            increaseDate();
        } else {
            getGmFreezeDto().setNumber(getGmFreezeDto().getRemainDays());
            increaseDate();
        }
    }

    public void show() {
        setInvOrganizationSiteDTOSelected(organizationSiteService.findOrganizationSiteDTOById(getInvOrganizationSiteDTOSelected().getId()));
        setGmSubscriptionsDTOSelected(gmSubscriptionsService.getValidGmSubscriptionsDTOByCustomerId(getInvOrganizationSiteDTOSelected().getId()));

    }

    private void increaseDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(getGmFreezeDto().getDatefrom());
        c.add(Calendar.DAY_OF_MONTH, getGmFreezeDto().getNumber());
        getGmFreezeDto().setDateto(c.getTime());
    }

   

    public List<GmSubscriptionsDTO> search() {

        try {

            setSubscribtionList(gmSubscriptionsService.getALLGmSubscriptionsDTODTOByCustomerId(getInvOrganizationSiteDTOSelected().getId()));

        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "load");
        }

        return getSubscribtionList();
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            this.setDataModel(new LazyDataModel<GmSubscriptionsDTO>() {
                private static final long serialVersionUID = 1L;

                @Override
                public List<GmSubscriptionsDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                    Integer type = 0;
                    type = 0;
                    setRowCount(gmSubscriptionsService.getTotalRegistors(type, getUserData().getUserBranch().getId(), filters).intValue());
                    return gmSubscriptionsService.getALLGmSubscriptionsDTODTOByTypeAndBranchId(type, first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), getUserData().getUserBranch().getId(), filters);
                }
            });
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "load");
        }
    }

    public void loadInventoryList() {
        try {
            if (getInvInventorySelection() != null && getInvInventorySelection().getId() != null && getInvInventorySelection().getId() > 0) {
                load();
            } else {
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "loadInventoryList");
        }

    }

    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "addGmSubscriptions");
            goToScreen();

            return "";
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "goToAdd");
            return null;
        }
    }

    public String goToMovment() {
        try {
            if (getGmSubscriptionsDTOSelected() != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("ScreenMode", "addGmMovement");
                context.getSessionMap().put("typeActivity", 0);
                context.getSessionMap().put("gmCustomerIdSelected", getGmSubscriptionsDTOSelected().getOrganizationSiteId().getId());
                exit("../gmsubscriptions/gmsubscriptionsformmovment.xhtml");

                return "";
            } else {

                setGmSubscriptionsDTOSelected(gmSubscriptionsService.getValidGmSubscriptionsDTOByCustomerId(getInvOrganizationSiteDTOSelected().getId()));
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("ScreenMode", "addGmMovement");
                context.getSessionMap().put("typeActivity", 0);
                context.getSessionMap().put("gmCustomerIdSelected", getGmSubscriptionsDTOSelected().getOrganizationSiteId().getId());
                exit("../gmsubscriptions/gmsubscriptionsformmovment.xhtml");

                return "";
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "goToAdd");
            return null;
        }
    }

    public void goTOAddForm() {
        exit("../gmregistration/gmregistrationForm.xhtml");
    }

    public String goToEdit() {
        try {
            if (getGmSubscriptionsDTOSelected() != null && getGmSubscriptionsDTOSelected().getId() > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("gmSubscriptionsIdSelected", getGmSubscriptionsDTOSelected().getId());
                context.getSessionMap().put("ScreenMode", "editGmSubscriptions");
                goToScreen();
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "goToEdit");
            return null;
        }
    }

    public void EditClientData() {
        try {
            OpenDlg("dlg3New");
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "EditClientData");
        }
    }

    public String goToresume() {
        try {
            if (getGmSubscriptionsDTOSelected() != null && getGmSubscriptionsDTOSelected().getOrganizationSiteId() != null && getGmSubscriptionsDTOSelected().getOrganizationSiteId().getId() > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("TamdedId", getGmSubscriptionsDTOSelected().getId());
                context.getSessionMap().put("ScreenMode", "Tamded");
                goToScreen();
                return "";
            } else if (getGmSubscriptionsDTOSelected() == null && getInvOrganizationSiteDTOSelected() != null) {
                setGmSubscriptionsDTOSelected(gmSubscriptionsService.getValidGmSubscriptionsDTOByCustomerId(getInvOrganizationSiteDTOSelected().getId()));
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("TamdedId", getGmSubscriptionsDTOSelected().getId());
                context.getSessionMap().put("ScreenMode", "Tamded");
                goToScreen();
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "goToresume");
            return null;
        }
    }

    public String goToEditClient() {
        try {
            if (getInvOrganizationSiteDTOSelected() != null && getInvOrganizationSiteDTOSelected().getId() > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("invOrganizationSiteDTOSelected", getInvOrganizationSiteDTOSelected().getId());
                context.getSessionMap().put("ScreenMode", "editorganization");
                goToScreen();
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "goToEdit");
            return null;
        }
    }

    public void goToScreen() {
        exit("../gmsubscriptions/gmsubscriptionsform.xhtml");
    }

    public void goToScreenCustomerData() {
        exit("../gmregistration/gmregistrationForm.xhtml");
    }

    public void goToClientEditScreen() {
        exit("../gmregistration/gmregistrationForm.xhtml");
    }

    public void delete() {
        try {
            if (getGmSubscriptionsDTOSelected() != null && getGmSubscriptionsDTOSelected().getId() != null) {
                gmSubscriptionsService.updateDeleteGmSubscriptionsDTODTO(getGmSubscriptionsDTOSelected(), getUserlogin());
                load();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "تم الجذذف بنجاح", "تم الجذذف بنجاح"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "خطأ", "خطأ فى الحف"));
            saveError(e, "InvPurchaseInvoiceListMB", "delete");
        }
    }

    public void deleteClient() {
        try {
            if (getInvOrganizationSiteDTOSelected() != null && getInvOrganizationSiteDTOSelected().getId() != null) {
                organizationSiteService.deleteOrganizationSiteDTO(getInvOrganizationSiteDTOSelected(), getUserlogin());
                load();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "تم الجذذف بنجاح", "تم الجذذف بنجاح"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "خطأ", "خطأ فى الحف"));
            saveError(e, "InvPurchaseInvoiceListMB", "delete");
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the dataModel
     */
    public LazyDataModel<GmSubscriptionsDTO> getDataModel() {
        return dataModel;
    }

    /**
     * @param dataModel the dataModel to set
     */
    public void setDataModel(LazyDataModel<GmSubscriptionsDTO> dataModel) {
        this.dataModel = dataModel;
    }

    /**
     * @return the subscribtionList
     */
    public List<GmSubscriptionsDTO> getSubscribtionList() {
        return subscribtionList;
    }

    /**
     * @param subscribtionList the subscribtionList to set
     */
    public void setSubscribtionList(List<GmSubscriptionsDTO> subscribtionList) {
        this.subscribtionList = subscribtionList;
    }

    /**
     * @return the invOrganizationSiteDTOList
     */
    public List<InvOrganizationSiteDTO> getInvOrganizationSiteDTOList() {
        invOrganizationSiteDTOList = organizationSiteService.getCustomerDTOByBranchId(getUserData().getUserBranch().getId());
        return invOrganizationSiteDTOList;
    }

    /**
     * @param invOrganizationSiteDTOList the invOrganizationSiteDTOList to set
     */
    public void setInvOrganizationSiteDTOList(List<InvOrganizationSiteDTO> invOrganizationSiteDTOList) {
        this.invOrganizationSiteDTOList = invOrganizationSiteDTOList;
    }

    /**
     * @return the organizationSiteDTOConvertor
     */
    public InvOrganizationSiteDTOConverter getOrganizationSiteDTOConvertor() {
        return organizationSiteDTOConvertor;
    }

    /**
     * @param organizationSiteDTOConvertor the organizationSiteDTOConvertor to
     * set
     */
    public void setOrganizationSiteDTOConvertor(InvOrganizationSiteDTOConverter organizationSiteDTOConvertor) {
        this.organizationSiteDTOConvertor = organizationSiteDTOConvertor;
    }

    /**
     * @return the nameClient
     */
    public String getNameClient() {
        return nameClient;
    }

    /**
     * @param nameClient the nameClient to set
     */
    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return the file
     */
    public String getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(String file) {
        this.file = file;
    }

    /**
     * @return the host
     */
    public String getHost() {
        HttpServletRequest request = (HttpServletRequest) getContext().getRequest();
        setHost(request.getRequestURL().substring(0, request.getRequestURL().indexOf("faces/")).concat("images/"));
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the access
     */
    public String getAccess() {
        if (getGmSubscriptionsDTOSelected() != null && getGmSubscriptionsDTOSelected().getStatus() != true && getGmSubscriptionsDTOSelected().isFreezeStatus() != true) {
            access = "يسمح له بدخول الجيم ";
        } else {
            access = "لا يسمح له بدخول الجيم";
        }
        return access;
    }

    /**
     * @param access the access to set
     */
    public void setAccess(String access) {
        this.access = access;
    }

}
