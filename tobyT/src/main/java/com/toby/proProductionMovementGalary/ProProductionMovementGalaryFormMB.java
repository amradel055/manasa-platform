package com.toby.proProductionMovementGalary;

import com.toby.businessservice.InvInventoryService;
import com.toby.businessservice.OrganizationSiteService;
import com.toby.businessservice.ProProductionMovementDetailService;
import com.toby.businessservice.ProProductionMovementService;
import com.toby.businessservice.ProProductionTransactionService;
import com.toby.converter.InvInventoryDTOConverter;
import com.toby.converter.InvOrganizationSiteDTOConverter;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.ProProductMovementDTO;
import com.toby.dto.ProProductMovementDetailDTO;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "proProductionMovementGalaryFormMB")
@ViewScoped
public class ProProductionMovementGalaryFormMB extends BaseFormBean {

    private ProProductMovementDTO proProductMovementDTO;
    private List<InvInventoryDTO> invInventoryDTOs;
    private List<InvOrganizationSiteDTO> invOrganizationSiteDTOs;
    private ProProductMovementDetailDTO proProductMovementDetailDTOSelected;

    private InvInventoryDTOConverter invInventoryDTOConverter;
    private InvOrganizationSiteDTOConverter invOrganizationSiteDTOConverter;

    @EJB
    private ProProductionMovementService proProductionMovementService;
    @EJB
    private ProProductionMovementDetailService proProductionMovementDetailService;
    @EJB
    private ProProductionTransactionService proProductionTransactionService;
    @EJB
    private InvInventoryService invInventoryService;

    @EJB
    private OrganizationSiteService organizationSiteService;

    public ProProductionMovementGalaryFormMB() {
    }
    
    @Override
    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        setScreenMode((String) context.getSessionMap().get("ScreenMode"));
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

        getInvOrganizationSiteDTOs();
        getInvInventoryDTOs();
    }

    public boolean validateHead() {
        if (proProductMovementDTO != null && proProductMovementDTO.getInventory() != null && proProductMovementDTO.getInventory().getId() != null
                && proProductMovementDTO.getInvOrganizationSiteId() != null && proProductMovementDTO.getInvOrganizationSiteId().getId() != null
                && proProductMovementDTO.getTime() != null && proProductMovementDTO.getDate() != null) {
            return true;
        } else {
            proProductMovementDTO.setMsg(" خطااملأ المعرض والطيار والتاريخ والوقت");
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
        if (validateHead() && validateDetails()) {
            proProductMovementDTO = proProductionMovementService.addProProductMovement(proProductMovementDTO, 2, getUserData().getUser());

            try {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("ScreenMode", "save");
                exit("../proProductionMovementGalary/proProductionMovementList.xhtml");
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

    public void findBySelected() {
        getProProductMovementDTO().setProProductMovementDetailDTOList(proProductionMovementDetailService.getDetailsByDelivery(proProductMovementDTO.getInvOrganizationSiteId().getId()));
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<InvInventoryDTO> complete(String query) {
        try {
            List<InvInventoryDTO> supplierList = invInventoryDTOs;
            if (query == null || query.trim().equals("")) {

                invInventoryDTOConverter = new InvInventoryDTOConverter(invInventoryDTOs);
                return supplierList;
            }
            List<InvInventoryDTO> filteredSuppliers = new ArrayList<>();

            String nameAr;
            String code;
            InvInventoryDTO supplierFilter;
            for (int i = 0; i < invInventoryDTOs.size(); i++) {
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

            invInventoryDTOConverter = new InvInventoryDTOConverter(filteredSuppliers);
            return filteredSuppliers;
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "completeSuppllier");
            return null;
        }
    }

    public List<InvOrganizationSiteDTO> complete2(String query) {
        try {
            List<InvOrganizationSiteDTO> supplierList = invOrganizationSiteDTOs;
            if (query == null || query.trim().equals("")) {

                invOrganizationSiteDTOConverter = new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOs);
                return supplierList;
            }
            List<InvOrganizationSiteDTO> filteredSuppliers = new ArrayList<>();

            String nameAr;
            String code;
            InvOrganizationSiteDTO supplierFilter;
            for (int i = 0; i < invOrganizationSiteDTOs.size(); i++) {
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

    public InvInventoryDTOConverter getInvInventoryDTOConverter() {
        return invInventoryDTOConverter;
    }

    public void setInvInventoryDTOConverter(InvInventoryDTOConverter invInventoryDTOConverter) {
        this.invInventoryDTOConverter = invInventoryDTOConverter;
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

    public List<InvInventoryDTO> getInvInventoryDTOs() {
        if (invInventoryDTOs == null || invInventoryDTOs.isEmpty()) {
            invInventoryDTOs = invInventoryService.getInventoryByInvType(getUserData().getUser(), 2);
            setInvInventoryDTOConverter(new InvInventoryDTOConverter(invInventoryDTOs));
        }
        return invInventoryDTOs;
    }

    public void setInvInventoryDTOs(List<InvInventoryDTO> invInventoryDTOs) {
        this.invInventoryDTOs = invInventoryDTOs;
    }

    public InvOrganizationSiteDTOConverter getInvOrganizationSiteDTOConverter() {
        return invOrganizationSiteDTOConverter;
    }

    public void setInvOrganizationSiteDTOConverter(InvOrganizationSiteDTOConverter invOrganizationSiteDTOConverter) {
        this.invOrganizationSiteDTOConverter = invOrganizationSiteDTOConverter;
    }

    public List<InvOrganizationSiteDTO> getInvOrganizationSiteDTOs() {
        if (invOrganizationSiteDTOs == null || invOrganizationSiteDTOs.isEmpty()) {
            invOrganizationSiteDTOs = organizationSiteService.findInvOrganizeSiteID(getUserData().getUser(),5,1);
            setInvOrganizationSiteDTOConverter(new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOs));

        }
        return invOrganizationSiteDTOs;
    }

    public void setInvOrganizationSiteDTOs(List<InvOrganizationSiteDTO> invOrganizationSiteDTOs) {
        this.invOrganizationSiteDTOs = invOrganizationSiteDTOs;
    }

    public ProProductMovementDetailDTO getProProductMovementDetailDTOSelected() {
        return proProductMovementDetailDTOSelected;
    }

    public void setProProductMovementDetailDTOSelected(ProProductMovementDetailDTO proProductMovementDetailDTOSelected) {
        this.proProductMovementDetailDTOSelected = proProductMovementDetailDTOSelected;
    }

}
