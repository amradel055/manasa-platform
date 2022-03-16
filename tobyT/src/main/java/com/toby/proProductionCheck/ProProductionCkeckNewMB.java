package com.toby.proProductionCheck;

import com.toby.businessservice.ProProductionStagesService;
import com.toby.businessservice.ProProductionTransactionService;
import com.toby.converter.ProProductionTransactionDTOConverter;
import com.toby.dto.InvPurchaseInvoiceDTO1;
import com.toby.dto.ProProductionStagesDTO;
import com.toby.dto.ProProductionTransactionDTO;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "proProductionCkeckNewMB")
@ViewScoped
public class ProProductionCkeckNewMB extends BaseFormBean {

    private List<ProProductionTransactionDTO> proProductionTransactionDTOs;
    private List<InvPurchaseInvoiceDTO1> invoices;
    private ProProductionTransactionDTO proProductionTransactionDTO;
    private ProProductionTransactionDTO proProductionTransactionDTOSelected;
    private InvPurchaseInvoiceDTO1 invPurchaseInvoiceDTOSelected;

    private ProProductionTransactionDTOConverter proProductionTransactionDTOConverter;

    @EJB
    private ProProductionTransactionService proProductionTransactionService;

    @EJB
    private ProProductionStagesService proProductionStagesService;

    public ProProductionCkeckNewMB() {
    }

    @Override
    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        setScreenMode((String) context.getSessionMap().get("ScreenMode"));
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("Edit")) {
            try {
                Integer proProductionTransactionDTOId = (Integer) context.getSessionMap().get("proProductionTransactionDTOSelected");
                if (proProductionTransactionDTOSelected == null) {
                    proProductionTransactionDTOSelected = proProductionTransactionService.getProductionTransactionById(proProductionTransactionDTOId, getUserData().getUser());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public String save() {
        //المفروض اني اعمل ست لمرحلة الانتاج في الدي تي او  بمرحلة التشييك الاول
        ProProductionStagesDTO productionStagesDTO = new ProProductionStagesDTO();
        productionStagesDTO.setId(proProductionStagesService.getCheckStage(getUserData().getUser()).getId());
        getProProductionTransactionDTOSelected().setProProductionId(productionStagesDTO);
        getProProductionTransactionDTOSelected().setInvPurchaseInvoiceId(invPurchaseInvoiceDTOSelected.getId());
        getProProductionTransactionDTOSelected().setProFactoryDeliveryId(invPurchaseInvoiceDTOSelected.getProFactoryDeliveryId());

        proProductionTransactionService.save(proProductionTransactionDTOSelected, 1, getUserData().getUser());
        invoices.remove(invPurchaseInvoiceDTOSelected);
        
        return "";

    }

    public String goToDetail() {
        try {
            if (invPurchaseInvoiceDTOSelected != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("ProFactoryDeliveryIdSelected", invPurchaseInvoiceDTOSelected.getProFactoryDeliveryId());
                exit("../proProductionCheck/proProductionTransactionDetail.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "proProductionCkeckMB", "goToEdit");
            return null;
        }
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ProProductionTransactionDTO getProProductionTransactionDTO() {
        if (proProductionTransactionDTO == null) {
            proProductionTransactionDTO = new ProProductionTransactionDTO();
        }
        return proProductionTransactionDTO;
    }

//    public void setProProductionTransactionDTO(ProProductionTransactionDTO proProductionTransactionDTO) {
//        this.proProductionTransactionDTO = proProductionTransactionDTO;
//    }
//
//    public List<ProProductionTransactionDTO> getProProductionTransactionDTOs() {
//        if (proProductionTransactionDTOs == null || proProductionTransactionDTOs.isEmpty()) {
//            proProductionTransactionDTOs = proProductionTransactionService.getAllInvoiceForFinalCheck(getUserData().getUser());
//        }
//        return proProductionTransactionDTOs;
//    }
    public void setProProductionTransactionDTOs(List<ProProductionTransactionDTO> proProductionTransactionDTOs) {
        this.proProductionTransactionDTOs = proProductionTransactionDTOs;
    }

    public List<InvPurchaseInvoiceDTO1> getInvoices() {
        if (invoices == null || invoices.isEmpty()) {
            invoices = proProductionTransactionService.getAllInvoiceForFinalCheckNew(getUserData().getUser());
        }
        return invoices;
    }

    public void setInvoices(List<InvPurchaseInvoiceDTO1> invoices) {
        this.invoices = invoices;
    }

    public ProProductionTransactionDTOConverter getProProductionTransactionDTOConverter() {
        return proProductionTransactionDTOConverter;
    }

    public void setProProductionTransactionDTOConverter(ProProductionTransactionDTOConverter proProductionTransactionDTOConverter) {
        this.proProductionTransactionDTOConverter = proProductionTransactionDTOConverter;
    }

    public ProProductionTransactionDTO getProProductionTransactionDTOSelected() {
        if (proProductionTransactionDTOSelected == null) {
            proProductionTransactionDTOSelected = new ProProductionTransactionDTO();
        }
        return proProductionTransactionDTOSelected;
    }

    public void setProProductionTransactionDTOSelected(ProProductionTransactionDTO proProductionTransactionDTOSelected) {
        this.proProductionTransactionDTOSelected = proProductionTransactionDTOSelected;
    }

    public InvPurchaseInvoiceDTO1 getInvPurchaseInvoiceDTOSelected() {
        return invPurchaseInvoiceDTOSelected;
    }

    public void setInvPurchaseInvoiceDTOSelected(InvPurchaseInvoiceDTO1 invPurchaseInvoiceDTOSelected) {
        this.invPurchaseInvoiceDTOSelected = invPurchaseInvoiceDTOSelected;
    }

}
