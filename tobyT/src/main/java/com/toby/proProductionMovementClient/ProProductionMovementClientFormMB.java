
package com.toby.proProductionMovementClient;

import com.toby.businessservice.OrganizationSiteService;
import com.toby.businessservice.ProProductionMovementDetailService;
import com.toby.businessservice.ProProductionMovementService;
import com.toby.bussinessservice.global.Utility;
import com.toby.converter.InvOrganizationSiteDTOConverter;
import com.toby.converter.ProProductMovementDetailDTOConvertor;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.ProProductMovementDTO;
import com.toby.dto.ProProductMovementDetailDTO;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;


@Named(value = "proProductionMovementClientFormMB")
@ViewScoped
public class ProProductionMovementClientFormMB extends BaseFormBean{
  
    private int type;
    private ProProductMovementDTO proProductMovementDTO;
    private ProProductMovementDetailDTO proProductMovementDetailDTOSelected;
    private List<InvOrganizationSiteDTO> invOrganizationSiteDTOsClient;


    private InvOrganizationSiteDTOConverter invOrganizationSiteDTOConverter;
    private ProProductMovementDetailDTOConvertor proProductMovementDetailDTOConvertor;

    @EJB
    private ProProductionMovementService proProductionMovementService;
    @EJB
    private ProProductionMovementDetailService proProductionMovementDetailService;


    @EJB
    private OrganizationSiteService organizationSiteService;

    
    public ProProductionMovementClientFormMB() {
    }
    
    @Override
    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        setScreenMode((String) context.getSessionMap().get("ScreenMode"));
        getProProductMovementDTO().setDate(Utility.hourNow());
        getProProductMovementDTO().setTime(Utility.hourNow());
         if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("Edit")) {
            try {
                Integer proProductMovementId = (Integer) context.getSessionMap().get("proProductMovementDTOSelected");
                if (proProductMovementDTO == null) {
                    proProductMovementDTO = proProductionMovementService.getById(proProductMovementId);
                    proProductMovementDTO.setProProductMovementDetailDTOList(proProductionMovementDetailService.getDetailsBySerial(proProductMovementId));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
         
         if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("proProductionMovementClientFormAdd")) {
            try {
                InvInvoiceDTO invInvoiceDTO = (InvInvoiceDTO) context.getSessionMap().get("proProductionMovementClientFormSelected");
                if (invInvoiceDTO != null) {
                    proProductMovementDTO.setInvOrganizationSiteId(invInvoiceDTO.getOrganizationSiteId());
                    findBYClient();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
       
//        getInvOrganizationSiteDTOsClient();
        
    }
    public boolean validateHead() {
        if (proProductMovementDTO != null &&  proProductMovementDTO.getInvOrganizationSiteId() != null
                && proProductMovementDTO.getInvOrganizationSiteId().getId() != null
                && proProductMovementDTO.getTime()!= null && proProductMovementDTO.getDate() != null) {
            return true;
        } else {
            proProductMovementDTO.setMsg(" خطااملأ  العميل والتاريخ الوقت");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", proProductMovementDTO.getMsg()));
            return false;
        }
    }
    public boolean validateDetails() {
        boolean b = false;
        if (proProductMovementDTO != null && proProductMovementDTO.getProProductMovementDetailDTOList() != null
                && !proProductMovementDTO.getProProductMovementDetailDTOList().isEmpty()) {
            for (ProProductMovementDetailDTO detailDTO : proProductMovementDTO.getProProductMovementDetailDTOList()) {
                if (detailDTO.getInvPurchesInvoiceId() != null) {
                    b = true;
                } else {
                    proProductMovementDTO.setMsg(" خطا يوجد فاتورة فارغة");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", proProductMovementDTO.getMsg()));
                }
            }
            
        } else {
            b = false;
            proProductMovementDTO.setMsg(" خطا يوجد فاتورة فارغة");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", proProductMovementDTO.getMsg()));

        }
        return b;
    }
    @Override
    public String save() {
        if(validateHead() && validateDetails()){
        
               if (proProductMovementDTO.getProProductMovementDetailDTOList().isEmpty() || proProductMovementDTO.getProProductMovementDetailDTOList() ==null) {
             proProductMovementDTO.setMsg("لم يتم الحفظ" );
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", proProductMovementDTO.getMsg()));
               }
               else{
        proProductMovementDTO = proProductionMovementService.addProProductMovement(proProductMovementDTO, 4, getUserData().getUser());
              proProductMovementDTO.setMsg(" تم الحفظ" );
                           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", proProductMovementDTO.getMsg()));

               }
               try {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("ScreenMode", "save");
                exit("../proProductionMovementClient/proProductionMovementList.xhtml");
                return "";
            } catch (Exception e) {
                saveError(e, "proProductMovementMB", "save");
                }
            }
       return "";
    }



    public void addRow() {
        ProProductMovementDetailDTO proProductMovementDetailDTO = new ProProductMovementDetailDTO();
        proProductMovementDetailDTO.setIndex(getIndex());
        proProductMovementDTO.getProProductMovementDetailDTOList().add(proProductMovementDetailDTO);

    }

    public void delete() {
        proProductMovementDetailDTOSelected.setIndex(getIndex());
        proProductMovementDTO.getProProductMovementDetailDTOList().remove(proProductMovementDetailDTOSelected);
        proProductionMovementDetailService.deleteByInvoice(proProductMovementDetailDTOSelected);
    }


    public void findBYClient() {
        getProProductMovementDTO().setProProductMovementDetailDTOList(proProductionMovementDetailService.getAllInvoiceByClient(getUserData().getUser(), proProductMovementDTO.getInvOrganizationSiteId()));

    }
    
    

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public List<InvOrganizationSiteDTO> complete4(String query) {
        try {
            List<InvOrganizationSiteDTO> supplierList = getInvOrganizationSiteDTOsClient();
            if (query == null || query.trim().equals("")) {

                invOrganizationSiteDTOConverter = new InvOrganizationSiteDTOConverter(getInvOrganizationSiteDTOsClient());
                return supplierList;
            }
            List<InvOrganizationSiteDTO> filteredSuppliers = new ArrayList<>();

            String nameAr;
            String code;
            InvOrganizationSiteDTO supplierFilter;
            for (int i = 0; i < getInvOrganizationSiteDTOsClient().size(); i++) {
                supplierFilter = supplierList.get(i);
                nameAr = Integer.toString(supplierFilter.getId());
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredSuppliers.contains(supplierFilter)) {
                        filteredSuppliers.add(supplierFilter);
                    }
                }

                code = Integer.toString(supplierFilter.getId());
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredSuppliers.contains(supplierFilter)) {
                        filteredSuppliers.add(supplierFilter);
                    }
                }
            }

            invOrganizationSiteDTOConverter = new InvOrganizationSiteDTOConverter(filteredSuppliers);
            return filteredSuppliers;
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "completeSuppllier");
            return null;
        }
    }
    
    public ProProductMovementDetailDTO getProProductMovementDetailDTOSelected() {
        return proProductMovementDetailDTOSelected;
    }

    public void setProProductMovementDetailDTOSelected(ProProductMovementDetailDTO proProductMovementDetailDTOSelected) {
        this.proProductMovementDetailDTOSelected = proProductMovementDetailDTOSelected;
    }
    
    public ProProductMovementDTO getProProductMovementDTO() {

        if (proProductMovementDTO == null) {
            proProductMovementDTO = new ProProductMovementDTO();
            proProductMovementDTO.setProProductMovementDetailDTOList(new ArrayList<>());
        }
        return proProductMovementDTO;
    }

    public void setProProductMovementDTO(ProProductMovementDTO proProductMovementDTO) {
        this.proProductMovementDTO = proProductMovementDTO;
    }

    public List<InvOrganizationSiteDTO> getInvOrganizationSiteDTOsClient() {
        if (invOrganizationSiteDTOsClient == null || invOrganizationSiteDTOsClient.isEmpty()) {
            invOrganizationSiteDTOsClient = organizationSiteService.findInvOrganizeSiteClient(getUserData().getUser());
            setInvOrganizationSiteDTOConverter(new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOsClient));
        }
        return invOrganizationSiteDTOsClient;
    }

    public void setInvOrganizationSiteDTOsClient(List<InvOrganizationSiteDTO> invOrganizationSiteDTOsClient) {
        this.invOrganizationSiteDTOsClient = invOrganizationSiteDTOsClient;
    }

    public InvOrganizationSiteDTOConverter getInvOrganizationSiteDTOConverter() {
        return invOrganizationSiteDTOConverter;
    }

    public void setInvOrganizationSiteDTOConverter(InvOrganizationSiteDTOConverter invOrganizationSiteDTOConverter) {
        this.invOrganizationSiteDTOConverter = invOrganizationSiteDTOConverter;
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
