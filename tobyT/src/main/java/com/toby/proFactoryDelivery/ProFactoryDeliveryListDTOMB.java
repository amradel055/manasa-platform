/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.proFactoryDelivery;

import com.toby.businessservice.ProFactoryDeliveryService;
import com.toby.dto.InvInvoiceDTO;
import com.toby.toby.InventoryBasicDataForm;
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
@Named(value = "proFactoryDeliveryListDTOMB")
@ViewScoped
public class ProFactoryDeliveryListDTOMB extends InventoryBasicDataForm {

    private InvInvoiceDTO invInvoiceDTOSelected;
    private Integer serialSelected ;
    private List<InvInvoiceDTO> invInvoiceDTOList ;
    @EJB
    private ProFactoryDeliveryService factoryDeliveryService;

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "invAddingOrderListMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            this.setDataModel(new LazyDataModel<InvInvoiceDTO>() {
                private static final long serialVersionUID = 1L;

                @Override
                public List<InvInvoiceDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                    Integer type = 0;
                    if (getUri().contains("proSendToGallary")) {
                        type = 1;
                    } else if (getUri().contains("proRecieveGallary")) {
                        type = 2;
                    } else if (getUri().contains("proSales")) {
                        type = 3;
                    }
                    setRowCount(factoryDeliveryService.getTotalRegistors(type, getUserData().getUserBranch().getId(), filters).intValue());
                    return factoryDeliveryService.getALLProFactoryDeliveryDTOByBranchId(type, first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), getUserData().getUserBranch().getId(), filters);
                }
            });
        } catch (Exception e) {
            saveError(e, "invAddingOrderListMB", "load");
        }
    }
    
    public void search(){
      if (serialSelected != null){
      invInvoiceDTOList = factoryDeliveryService.getALLProFactoryDeliveryDTOByBranchIdAndSerial(getUserData().getUserBranch().getId(), serialSelected);
      }else {
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR !", "يرجي ادخال رقم الفاتورة"));
      }
    }

    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "AddproFactoryDelivery");
            if (getUri().contains("proSendToGallary")) {
                exit("../proSendToGallary/proSendToGallaryForm.xhtml");
            } else if (getUri().contains("proRecieveGallary")) {
                exit("../proRecieveGallary/proRecieveGallaryForm.xhtml");
            } else if (getUri().contains("proSales")) {
                exit("../proSales/proSalesForm.xhtml");
            } else {
                exit("../profactoryDelivery/profactorydeviveryForm.xhtml");
            }
            return "";
        } catch (Exception e) {
            saveError(e, "invAddingOrderListMB", "goToAdd");
            return null;
        }
    }

    public String goToEdit() {
        try {
            if (getInvInvoiceDTOSelected() != null && getInvInvoiceDTOSelected().getId() > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("proFactoryDeliverySelected", getInvInvoiceDTOSelected().getId());
                context.getSessionMap().put("ScreenMode", "EditproFactoryDelivery");
                if (getUri().contains("proSendToGallary")) {
                    exit("../proSendToGallary/proSendToGallaryForm.xhtml");
                } else if (getUri().contains("proRecieveGallary")) {
                    exit("../proRecieveGallary/proRecieveGallaryForm.xhtml");
                } else if (getUri().contains("proSales")) {
                    exit("../proSales/proSalesForm.xhtml");
                } else {
                    exit("../profactoryDelivery/profactorydeviveryForm.xhtml");
                }
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "invAddingOrderListMB", "goToEdit");
            return null;
        }
    }

    public void delete() {
        try {
            if (getInvInvoiceDTOSelected() != null) {
                try {
                    factoryDeliveryService.deleteInvAddingOrder(invInvoiceDTOSelected);
                    load();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserDDs().get("INFO"), getUserDDs().get("DELETED")));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserDDs().get("ERROR"), getUserDDs().get("UNIQE_KEY_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "invAddingOrderListMB", "delete");
        }
    }

    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String exit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the invInvoiceDTOSelected
     */
    public InvInvoiceDTO getInvInvoiceDTOSelected() {
        if (invInvoiceDTOSelected == null) {
            invInvoiceDTOSelected = new InvInvoiceDTO();
        }
        return invInvoiceDTOSelected;
    }

    /**
     * @param invInvoiceDTOSelected the invInvoiceDTOSelected to set
     */
    public void setInvInvoiceDTOSelected(InvInvoiceDTO invInvoiceDTOSelected) {
        this.invInvoiceDTOSelected = invInvoiceDTOSelected;
    }

    /**
     * @return the serialSelected
     */
    public Integer getSerialSelected() {
        return serialSelected;
    }

    /**
     * @param serialSelected the serialSelected to set
     */
    public void setSerialSelected(Integer serialSelected) {
        this.serialSelected = serialSelected;
    }

    /**
     * @return the invInvoiceDTOList
     */
    public List<InvInvoiceDTO> getInvInvoiceDTOList() {
        return invInvoiceDTOList;
    }

    /**
     * @param invInvoiceDTOList the invInvoiceDTOList to set
     */
    public void setInvInvoiceDTOList(List<InvInvoiceDTO> invInvoiceDTOList) {
        this.invInvoiceDTOList = invInvoiceDTOList;
    }

  
}
