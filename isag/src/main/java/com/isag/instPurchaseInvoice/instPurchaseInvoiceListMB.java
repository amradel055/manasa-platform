/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.instPurchaseInvoice;

import com.isag.businessservice.InstPurchaseInvoiceDetailService;
import com.isag.businessservice.instPurchaseInvoiceService;
import com.isag.dto.InstPurchaseInvoiceDTO;
import com.isag.isag.BaseListBean;
import com.isag.isag.UserData;
import java.lang.reflect.Array;
import java.util.ArrayList;
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
 * @author AhmedEssam
 */
@Named(value = "instPurchaseInvoiceListMB")
@ViewScoped
public class instPurchaseInvoiceListMB extends BaseListBean {

    @EJB
    instPurchaseInvoiceService invoiceService;
    
    @EJB
     InstPurchaseInvoiceDetailService instPurchaseInvoiceDetailService;

    private List<InstPurchaseInvoiceDTO> instPurchaseInvoiceDTOList;
    private InstPurchaseInvoiceDTO instPurchaseInvoiceDTOSelected;
    private InstPurchaseInvoiceDTO instPurchaseInvoiceDTO;
    private LazyDataModel<InstPurchaseInvoiceDTO> dataModel;


    @Override
    @PostConstruct
    public void init() {
        try {
            this.load();
        } catch (Exception e) {
            saveError(e, "instPurchaseInvoiceListMB", "init");
        }
    }

    @Override
    public void delete() {
     try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            if (getInstPurchaseInvoiceDTOSelected() != null) {
                try {
                    instPurchaseInvoiceDetailService.deleteByInstPurchaseInvoiceId(getInstPurchaseInvoiceDTOSelected().getId());
                    invoiceService.deleteInstPurchaseInvoiceDTO(getInstPurchaseInvoiceDTOSelected(), getUserData().getUser());
                    getInstPurchaseInvoiceDTOList().remove(getInstPurchaseInvoiceDTOSelected());
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "instPurchaseInvoiceListMB", "delete");
        }  
    
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
    try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "AddinstPurchaseInvoiceDTOSelected");

            exit("../instPurchaseInvoice/instPurchaseInvoiceForm.xhtml");

            return "";
        } catch (Exception e) {
            saveError(e, "instPurchaseInvoiceListMB", "goToAdd");
            return null;
        }   
    }

    @Override
    public String goToEdit() {
    try {
            if (getInstPurchaseInvoiceDTOSelected() != null && getInstPurchaseInvoiceDTOSelected().getId() != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("PurchaseInvoiceDTOSelected", getInstPurchaseInvoiceDTOSelected().getId());
                context.getSessionMap().put("ScreenMode", "Edit");
                exit("../instPurchaseInvoice/instPurchaseInvoiceForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "instPurchaseInvoiceListMB", "goToEdit");
            return null;
        }   
    }

    @Override
    public void load() {
    try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
             setDataModel(new LazyDataModel<InstPurchaseInvoiceDTO>(){
                private static final long serialVersionUID = 1L; 
                @Override
                public List<InstPurchaseInvoiceDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                     setRowCount(invoiceService.getTotalRegistors(getUserData().getUserBranch().getId(), filters).intValue());
                     setInstPurchaseInvoiceDTOList(invoiceService.getAllInstPurchaseInvoiceByBranchId( first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), getUserData().getUserBranch().getId(), filters));
                    return getInstPurchaseInvoiceDTOList();
                } 
            });
          //  setInstPurchaseInvoiceDTOList(invoiceService.getAllInstPurchaseInvoiceByBranchId(getUserData().getUserBranch().getId()));
    

    } catch (Exception e) {
            saveError(e, "instPurchaseInvoiceListMB", "load");
        }    
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the instPurchaseInvoiceDTOList
     */
    public List<InstPurchaseInvoiceDTO> getInstPurchaseInvoiceDTOList() {
        if (instPurchaseInvoiceDTOList.isEmpty() || instPurchaseInvoiceDTOList == null) {
            instPurchaseInvoiceDTOList = new ArrayList<>();
        }
        return instPurchaseInvoiceDTOList;
    }

    /**
     * @param instPurchaseInvoiceDTOList the instPurchaseInvoiceDTOList to set
     */
    public void setInstPurchaseInvoiceDTOList(List<InstPurchaseInvoiceDTO> instPurchaseInvoiceDTOList) {
        this.instPurchaseInvoiceDTOList = instPurchaseInvoiceDTOList;
    }

    /**
     * @return the instPurchaseInvoiceDTOSelected
     */
    public InstPurchaseInvoiceDTO getInstPurchaseInvoiceDTOSelected() {
        if (instPurchaseInvoiceDTOSelected == null) {
            instPurchaseInvoiceDTOSelected = new InstPurchaseInvoiceDTO();
        }
        return instPurchaseInvoiceDTOSelected;
    }

    /**
     * @param instPurchaseInvoiceDTOSelected the instPurchaseInvoiceDTOSelected
     * to set
     */
    public void setInstPurchaseInvoiceDTOSelected(InstPurchaseInvoiceDTO instPurchaseInvoiceDTOSelected) {
        this.instPurchaseInvoiceDTOSelected = instPurchaseInvoiceDTOSelected;
    }

    /**
     * @return the instPurchaseInvoiceDTO
     */
    public InstPurchaseInvoiceDTO getInstPurchaseInvoiceDTO() {
        if (instPurchaseInvoiceDTO == null) {
            instPurchaseInvoiceDTO = new InstPurchaseInvoiceDTO();
        }
        return instPurchaseInvoiceDTO;
    }

    /**
     * @param instPurchaseInvoiceDTO the instPurchaseInvoiceDTO to set
     */
    public void setInstPurchaseInvoiceDTO(InstPurchaseInvoiceDTO instPurchaseInvoiceDTO) {
        this.instPurchaseInvoiceDTO = instPurchaseInvoiceDTO;
    }

    /**
     * @return the dataModel
     */
    public LazyDataModel<InstPurchaseInvoiceDTO> getDataModel() {
        return dataModel;
    }

    /**
     * @param dataModel the dataModel to set
     */
    public void setDataModel(LazyDataModel<InstPurchaseInvoiceDTO> dataModel) {
        this.dataModel = dataModel;
    }

}
