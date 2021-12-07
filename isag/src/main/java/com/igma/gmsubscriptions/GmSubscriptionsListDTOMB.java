/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.gmsubscriptions;

import com.isag.businessservice.GmSubscriptionsService;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.dto.GmSubscriptionsDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.isag.BaseListBean;
import com.isag.isag.UserData;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author WIN7
 */
@Named(value = "gmSubscriptionsListDTOMB")
@ViewScoped
public class GmSubscriptionsListDTOMB extends BaseListBean {

    private LazyDataModel<GmSubscriptionsDTO> dataModel;
    private GmSubscriptionsDTO gmSubscriptionsDTOSelected;
    private List<GmSubscriptionsDTO> subscribtionList ;
    private InvOrganizationSiteDTO invOrganizationSiteDTO ;
    private List<InvOrganizationSiteDTO> invOrganizationSiteDTOList ;
    private InvOrganizationSiteDTO invOrganizationSiteDTOSelected ;
    
    @EJB
    private GmSubscriptionsService gmSubscriptionsService;
    
    @EJB 
    private OrganizationSiteService organizationSiteService ;
    
    
    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "init");
        }
    } 
    
    
    
 
    
    
    
     public List<GmSubscriptionsDTO> search(){
      
        try {
            
            setSubscribtionList(gmSubscriptionsService.getALLGmSubscriptionsDTODTOByCustomerId(invOrganizationSiteDTO.getId()));
         
        }
        catch (Exception e){
        saveError(e, "InvPurchaseInvoiceFormMB", "load");
        }
        
        return getSubscribtionList() ;
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
    
    public void goTOAddForm(){
        exit("../gmregistration/gmregistrationForm.xhtml");
    }

    public String goToEdit() {
        try {
            if (getGmSubscriptionsDTOSelected() != null && getGmSubscriptionsDTOSelected().getId() > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("invPurchaseInvoiceIdSeclected", getGmSubscriptionsDTOSelected().getId());
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
    
    public String goToEditClient() {
        try {
            if (getInvOrganizationSiteDTOSelected() != null && getInvOrganizationSiteDTOSelected().getId() > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("invOrganizationSiteDTOSelected", getInvOrganizationSiteDTOSelected().getId());
                context.getSessionMap().put("ScreenMode", "editorganization");
                goToClientEditScreen();
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
            if (getInvOrganizationSiteDTOSelected()!= null && getInvOrganizationSiteDTOSelected().getId() != null) {
                organizationSiteService.deleteOrganizationSiteDTO(getInvOrganizationSiteDTOSelected() , getUserlogin());
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
        invOrganizationSiteDTOList = organizationSiteService.getCustomerDTOByBranchId(getUserData().getUserBranch().getId()) ;
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
     * @param invOrganizationSiteDTOSelected the invOrganizationSiteDTOSelected to set
     */
    public void setInvOrganizationSiteDTOSelected(InvOrganizationSiteDTO invOrganizationSiteDTOSelected) {
        this.invOrganizationSiteDTOSelected = invOrganizationSiteDTOSelected;
    }
}
