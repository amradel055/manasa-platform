/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.gmsubscriptions;

import com.isag.businessservice.GmFreezeService;
import com.isag.businessservice.GmSubscriptionsService;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.bussinessservice.global.Utility;
import com.isag.converter.InvOrganizationSiteDTOConverter;
import com.isag.dto.GmFreezeDto;
import com.isag.dto.GmItemDTO;
import com.isag.dto.GmSubscriptionsDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.isag.BaseListBean;
import com.isag.isag.UserData;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
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
import static javax.ws.rs.client.Entity.entity;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author WIN7
 */
@Named(value = "gmSubscriptionsListDTOMB")
@ViewScoped
public class GmSubscriptionsListDTOMB extends BaseListBean {

    private String nameClient;
    private LazyDataModel<GmSubscriptionsDTO> dataModel;
    private GmSubscriptionsDTO gmSubscriptionsDTOSelected;
    private List<GmSubscriptionsDTO> subscribtionList;
    private InvOrganizationSiteDTO invOrganizationSiteDTO;
    private List<InvOrganizationSiteDTO> invOrganizationSiteDTOList;
    private InvOrganizationSiteDTO invOrganizationSiteDTOSelected;
    private InvOrganizationSiteDTO siteDTO;
    private InvOrganizationSiteDTOConverter organizationSiteDTOConvertor;
    private GmFreezeDto gmFreezeDto;
    private String filename;
    private String file;
    private String host;
    @EJB
    private GmSubscriptionsService gmSubscriptionsService;

    @EJB
    private OrganizationSiteService organizationSiteService;

    @EJB
    private GmFreezeService gmFreezeService;

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

        gmFreezeDto.setNumber(null);
    }

    public void findCustomerData() {
        setSiteDTO(organizationSiteService.findOrganizationSiteDTOByIdSmall(getGmSubscriptionsDTOSelected().getOrganizationSiteId().getId()));
        getGmFreezeDto().setCustomerId(getSiteDTO());
        getGmFreezeDto().setSubscriptionId(getGmSubscriptionsDTOSelected());
        getGmFreezeDto().setDatefrom(new Date());

        calcDays();
        OpenDlg("dlg3");
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
        setInvOrganizationSiteDTOSelected(organizationSiteService.findOrganizationSiteDTOById(invOrganizationSiteDTO.getId()));
        setGmSubscriptionsDTOSelected(gmSubscriptionsService.getValidGmSubscriptionsDTOByCustomerId(invOrganizationSiteDTO.getId()));
       

       
    }

    private void increaseDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(getGmFreezeDto().getDatefrom());
        c.add(Calendar.DAY_OF_MONTH, getGmFreezeDto().getNumber());
        getGmFreezeDto().setDateto(c.getTime());
    }

    public void calcDays() {
      
        getGmFreezeDto().setMustDays(Utility.calcDays(getGmSubscriptionsDTOSelected().getStartDate(), getGmSubscriptionsDTOSelected().getEndDate(), 5));
        getGmFreezeDto().setTakeDays(gmFreezeService.calcTakenDaysByCustomerIdAndSubscribtionId(getGmSubscriptionsDTOSelected().getId()));
        getGmFreezeDto().setRemainDays(getGmFreezeDto().getMustDays() - getGmFreezeDto().getTakeDays());
    }

    public String saveFreeze() {
        if (gmFreezeDto.getNumber().compareTo(gmFreezeDto.getRemainDays()) != 1) {
            gmFreezeService.save(getGmFreezeDto(), getUserData().getUser());
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "خطأ", "يجب ان يكون عدد ايام التجميد اقل من او يساوي عدد الايام المتاحه"));
        }
        gmFreezeDto.setNumber(null);

        return "";
    }

    public List<GmSubscriptionsDTO> search() {

        try {

            setSubscribtionList(gmSubscriptionsService.getALLGmSubscriptionsDTODTOByCustomerId(invOrganizationSiteDTO.getId()));

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
            context.getSessionMap().put("typeActivity", 0);
            goToScreen();

            return "";
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "goToAdd");
            return null;
        }
    }
    
     public String goToMovment() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "addGmMovement");
            context.getSessionMap().put("typeActivity", 0);
            context.getSessionMap().put("gmCustomerIdSelected", getGmSubscriptionsDTOSelected().getOrganizationSiteId().getId());
             exit("../gmsubscriptions/gmsubscriptionsformmovment.xhtml");

            return "";
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
      public String goToresume() {
        try {
            if (getGmSubscriptionsDTOSelected().getOrganizationSiteId()!= null && getGmSubscriptionsDTOSelected().getOrganizationSiteId().getId() > 0) {
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
  
    public void goToClientEditScreen() {
        exit("../gmregistration/gmregistrationForm.xhtml");
    }

    public void delete() {
        try {
            if (getGmSubscriptionsDTOSelected() != null && getGmSubscriptionsDTOSelected().getId() != null) {
                gmSubscriptionsService.deleteGmSubscriptionsDTODTO(getGmSubscriptionsDTOSelected().getId());
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

    @Override
    public void filter() {
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
     * @return the gmSubscriptionsDTOSelected
     */
    public GmSubscriptionsDTO getGmSubscriptionsDTOSelected() {
        return gmSubscriptionsDTOSelected;
    }

    /**
     * @param gmSubscriptionsDTOSelected the gmSubscriptionsDTOSelected to set
     */
    public void setGmSubscriptionsDTOSelected(GmSubscriptionsDTO gmSubscriptionsDTOSelected) {
        this.gmSubscriptionsDTOSelected = gmSubscriptionsDTOSelected;
    }

    /**
     * @return the invOrganizationSiteDTO
     */
    public InvOrganizationSiteDTO getInvOrganizationSiteDTO() {
        return invOrganizationSiteDTO;
    }

    /**
     * @param invOrganizationSiteDTO the invOrganizationSiteDTO to set
     */
    public void setInvOrganizationSiteDTO(InvOrganizationSiteDTO invOrganizationSiteDTO) {
        this.invOrganizationSiteDTO = invOrganizationSiteDTO;
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
     * @return the invOrganizationSiteDTOSelected
     */
    public InvOrganizationSiteDTO getInvOrganizationSiteDTOSelected() {
        return invOrganizationSiteDTOSelected;
    }

    /**
     * @param invOrganizationSiteDTOSelected the invOrganizationSiteDTOSelected
     * to set
     */
    public void setInvOrganizationSiteDTOSelected(InvOrganizationSiteDTO invOrganizationSiteDTOSelected) {
        this.invOrganizationSiteDTOSelected = invOrganizationSiteDTOSelected;
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
     * @return the siteDTO
     */
    public InvOrganizationSiteDTO getSiteDTO() {
        if (siteDTO == null) {
            siteDTO = new InvOrganizationSiteDTO();
        }
        return siteDTO;
    }

    /**
     * @param siteDTO the siteDTO to set
     */
    public void setSiteDTO(InvOrganizationSiteDTO siteDTO) {
        this.siteDTO = siteDTO;
    }

    /**
     * @return the gmFreezeDto
     */
    public GmFreezeDto getGmFreezeDto() {
        if (gmFreezeDto == null) {
            gmFreezeDto = new GmFreezeDto();
        }
        return gmFreezeDto;
    }

    /**
     * @param gmFreezeDto the gmFreezeDto to set
     */
    public void setGmFreezeDto(GmFreezeDto gmFreezeDto) {
        this.gmFreezeDto = gmFreezeDto;
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

}
