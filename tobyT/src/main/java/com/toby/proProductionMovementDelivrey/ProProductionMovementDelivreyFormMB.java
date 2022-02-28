package com.toby.proProductionMovementDelivrey;

import com.toby.businessservice.OrganizationSiteService;
import com.toby.businessservice.ProProductionMovementDetailService;
import com.toby.businessservice.ProProductionMovementService;
import com.toby.businessservice.ProProductionTransactionService;
import com.toby.converter.InvOrganizationSiteDTOConverter;
import com.toby.converter.ProProductMovementDetailDTOConvertor;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.ProProductMovementDTO;
import com.toby.dto.ProProductMovementDetailDTO;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

@Named(value = "proProductionMovementDelivreyFormMB")
@ViewScoped
public class ProProductionMovementDelivreyFormMB extends BaseFormBean {

    private ProProductMovementDTO proProductMovementDTO;
    private List<InvOrganizationSiteDTO> invOrganizationSiteDTOs;
    private ProProductMovementDetailDTO proProductMovementDetailDTOSelected;
    private List<ProProductMovementDetailDTO> proProductMovementDetailDTOs;
    private HashSet<Integer> hs;

    private InvOrganizationSiteDTOConverter invOrganizationSiteDTOConverter;
    private ProProductMovementDetailDTOConvertor proProductMovementDetailDTOConvertor;

    @EJB
    private ProProductionMovementService proProductionMovementService;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private ProProductionTransactionService proProductionTransactionService;
    @EJB
    private ProProductionMovementDetailService proProductionMovementDetailService;

    public ProProductionMovementDelivreyFormMB() {
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
    }

    public boolean validateHead() {
        if (proProductMovementDTO != null && proProductMovementDTO.getInvOrganizationSiteId() != null
                && proProductMovementDTO.getInvOrganizationSiteId().getId() != null
                && proProductMovementDTO.getRemark() != null && proProductMovementDTO.getDate() != null) {
            return true;
        } else {
            proProductMovementDTO.setMsg(" خطااملأ  الطيار والتاريخ والبيان");
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
            proProductMovementDTO.setMsg("  من فضلك ادخل فاتورة على الاقل");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", proProductMovementDTO.getMsg()));

        }
        return b;
    }

    @Override
    public String save() {
        backup();
        if (validateHead() && validateDetails()) {
            if (proProductMovementDTO != null) {
                proProductMovementDTO = proProductionMovementService.addProProductMovement(proProductMovementDTO, 1, getUserData().getUser());
                try {
                    ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                    context.getSessionMap().put("ScreenMode", "save");
                    exit("../proProductionMovementDelivrey/proProductionMovementList.xhtml");
                    return "";
                } catch (Exception e) {
                    saveError(e, "proProductMovementMB", "save");
                    return "";
                }
            }
        }
        return "";
    }

    public boolean validateDetailsWhenAddRow() {
        boolean b = true;

        for (ProProductMovementDetailDTO detailDTO : proProductMovementDTO.getProProductMovementDetailDTOList()) {
            if (detailDTO.getInvPurchesInvoiceId() != null) {
                b = true;
            } else {
                b = false;
                proProductMovementDTO.setMsg(" خطا يوجد فاتورة فارغة");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", proProductMovementDTO.getMsg()));
            }
        }

        return b;
    }

    public void addRow() {
        backup();
        if (validateDetailsWhenAddRow()) {
            ProProductMovementDetailDTO proProductMovementDetailDTO = new ProProductMovementDetailDTO();
            proProductMovementDetailDTO.setIndex(getIndex());
            proProductMovementDTO.getProProductMovementDetailDTOList().add(proProductMovementDetailDTO);
            setProProductMovementDetailDTOSelected(proProductMovementDetailDTO);

        }
    }

    public void delete() {
        proProductMovementDetailDTOSelected.setIndex(getIndex());
        proProductMovementDTO.getProProductMovementDetailDTOList().remove(proProductMovementDetailDTOSelected);
        getHs().remove(proProductMovementDetailDTOSelected.getInvPurchesInvoiceId());
        if (proProductMovementDetailDTOSelected.getId() != null) {
           proProductionMovementDetailService.deleteBySelected(proProductMovementDetailDTOSelected);
        
        }
    }

    public void selectValue() {
        if (proProductMovementDetailDTOSelected != null) {
            if (!getHs().add(proProductMovementDetailDTOSelected.getInvPurchesInvoiceId())) {
                proProductMovementDTO.setMsg("لقد تم اختيار هذه الفاتورة من قبل");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", proProductMovementDTO.getMsg()));
            } else {
                proProductMovementDetailDTOSelected.setInvPurchesInvoiceIdBackUp(proProductMovementDetailDTOSelected.getInvPurchesInvoiceId());

            }
        }
    }

    public void backup() {
        for (ProProductMovementDetailDTO detailDTO : proProductMovementDTO.getProProductMovementDetailDTOList()) {
            if (detailDTO.getInvPurchesInvoiceId() == null && detailDTO.getInvPurchesInvoiceIdBackUp() != null) {
                detailDTO.setInvPurchesInvoiceId(detailDTO.getInvPurchesInvoiceIdBackUp());
            } else if (detailDTO.getInvPurchesInvoiceId() != null && detailDTO.getInvPurchesInvoiceIdBackUp() == null) {

                detailDTO.setInvPurchesInvoiceIdBackUp(detailDTO.getInvPurchesInvoiceId());
            }
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

    public InvOrganizationSiteDTOConverter getInvOrganizationSiteDTOConverter() {
        return invOrganizationSiteDTOConverter;
    }

    public void setInvOrganizationSiteDTOConverter(InvOrganizationSiteDTOConverter invOrganizationSiteDTOConverter) {
        this.invOrganizationSiteDTOConverter = invOrganizationSiteDTOConverter;
    }

    public List<InvOrganizationSiteDTO> getInvOrganizationSiteDTOs() {
        if (invOrganizationSiteDTOs == null || invOrganizationSiteDTOs.isEmpty()) {
            invOrganizationSiteDTOs = organizationSiteService.findInvOrganizeSiteID(getUserData().getUser(), 5, 1);
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

    public ProProductMovementDetailDTOConvertor getProProductMovementDetailDTOConvertor() {
        return proProductMovementDetailDTOConvertor;
    }

    public void setProProductMovementDetailDTOConvertor(ProProductMovementDetailDTOConvertor proProductMovementDetailDTOConvertor) {
        this.proProductMovementDetailDTOConvertor = proProductMovementDetailDTOConvertor;
    }

    public List<ProProductMovementDetailDTO> getProProductMovementDetailDTOs() {
        if (proProductMovementDetailDTOs == null || proProductMovementDetailDTOs.isEmpty()) {
            proProductMovementDetailDTOs = proProductionTransactionService.getInvPurchesInvoiceID(getUserData().getUser());
        }
        return proProductMovementDetailDTOs;
    }

    public void setProProductMovementDetailDTOs(List<ProProductMovementDetailDTO> proProductMovementDetailDTOs) {
        this.proProductMovementDetailDTOs = proProductMovementDetailDTOs;
    }

    public HashSet<Integer> getHs() {
        if (hs == null || hs.isEmpty()) {
            hs = new HashSet();
        }
        return hs;
    }

    public void setHs(HashSet<Integer> hs) {
        this.hs = hs;
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
