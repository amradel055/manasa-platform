package com.toby.proProductionMovementFactory;

import com.toby.businessservice.InvInventoryService;
import com.toby.businessservice.OrganizationSiteService;
import com.toby.businessservice.ProProductionMovementDetailService;
import com.toby.businessservice.ProProductionMovementService;
import com.toby.businessservice.ProProductionTransactionService;
import com.toby.converter.InvInventoryDTOConverter;
import com.toby.converter.ProProductMovementDetailDTOConvertor;
import com.toby.dto.InvInventoryDTO;
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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "proProductionMovementFactoryFormMB")
@ViewScoped
public class ProProductionMovementFactoryFormMB extends BaseFormBean {

    private ProProductMovementDTO proProductMovementDTO;
    private List<InvInventoryDTO> invFactoryDTOs;
    private List<InvInventoryDTO> invGalaryDTOs;
    private ProProductMovementDetailDTO proProductMovementDetailDTOSelected;
    private List<ProProductMovementDetailDTO> proProductMovementDetailDTOs1;
    private HashSet<Integer> hs;

    private InvInventoryDTOConverter invInventoryDTOConverter;
    private InvInventoryDTOConverter invInventoryDTOConverter1;
    private ProProductMovementDetailDTOConvertor proProductMovementDetailDTOConvertor;

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

    public ProProductionMovementFactoryFormMB() {
    }

    @Override
    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        setScreenMode((String) context.getSessionMap().get("ScreenMode"));
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("Edit")) {
            try {
                ProProductMovementDTO proProductMovementId = (ProProductMovementDTO) context.getSessionMap().get("proProductMovementDTOSelected");
                if (proProductMovementDTO == null) {
                    proProductMovementDTO = proProductionMovementService.getById(proProductMovementId.getId());
                    proProductMovementDTO.setProProductMovementDetailDTOList(proProductionMovementDetailService.getDetailsBySerial(proProductMovementId.getId()));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        proProductMovementDetailDTOs1 = new ArrayList<>();
        getInvFactoryDTOs();
        getInvGalaryDTOs();
    }

    public boolean validateHead() {
        if (proProductMovementDTO != null && proProductMovementDTO.getInvGalary() != null
                && proProductMovementDTO.getInvGalary().getId() != null
                && proProductMovementDTO.getInventory() != null
                && proProductMovementDTO.getInventory().getId() != null
                && proProductMovementDTO.getTime() != null && proProductMovementDTO.getDate() != null) {
            return true;
        } else {
            proProductMovementDTO.setMsg(" خطااملأ  المعرض والمشغل والتاريخ الوقت");
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
                    b = false;
                    proProductMovementDTO.setMsg(" خطا يوجد فاتورة فارغة");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", proProductMovementDTO.getMsg()));
                }
            }

        } else {
            b = false;
            proProductMovementDTO.setMsg(" من فضلك ادخل فاتورة على الاقل");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", proProductMovementDTO.getMsg()));

        }
        return b;
    }

    @Override
    public String save() {
        backup();
        if (validateHead() && validateDetails()) {
            proProductMovementDTO = proProductionMovementService.addProProductMovement(proProductMovementDTO, 3, getUserData().getUser());
            try {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("ScreenMode", "save");
                exit("../proProductionMovementFactory/proProductionMovementList.xhtml");
                return "";
            } catch (Exception e) {
                saveError(e, "proProductMovementMB", "save");

            }
        }
        return "";
    }

    public boolean validateDetailsWhenAddRow() {
        boolean b = true;
        for (ProProductMovementDetailDTO detailDTO : proProductMovementDTO.getProProductMovementDetailDTOList()) {
            if (detailDTO.getInvPurchesInvoiceId() == null) {
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
            for (ProProductMovementDetailDTO detail : proProductMovementDTO.getProProductMovementDetailDTOList()) {
                detail.setInvPurchesInvoiceId(detail.getInvPurchesInvoiceId());

            }
            proProductMovementDTO.getProProductMovementDetailDTOList().add(proProductMovementDetailDTO);
            setProProductMovementDetailDTOSelected(proProductMovementDetailDTO);
        }
    }

    public void delete() {
        proProductMovementDetailDTOSelected.setIndex(getIndex());
        proProductMovementDTO.getProProductMovementDetailDTOList().remove(proProductMovementDetailDTOSelected);
        getHs().remove(proProductMovementDetailDTOSelected.getInvPurchesInvoiceId());
        if(proProductMovementDetailDTOSelected.getId() != null){
            proProductionMovementDetailService.deleteBySelected(proProductMovementDetailDTOSelected);
        
        }
    }

    public void changeGalary() {
        if (!proProductMovementDTO.getProProductMovementDetailDTOList().isEmpty()) {
            proProductMovementDTO.setMsg("هل تريد تغيير المخزن مع العلم ان سيتم مسح جميع الفواتير");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", proProductMovementDTO.getMsg()));
            getProProductMovementDTO().setProProductMovementDetailDTOList(new ArrayList<>());
            proProductMovementDetailDTOs1 = new ArrayList<>();

        }
    }

    public void selectValue() {
        if (proProductMovementDetailDTOSelected != null) {
            if (!getHs().add(proProductMovementDetailDTOSelected.getInvPurchesInvoiceId())) {
                proProductMovementDTO.setMsg("لقد تم اختيار هذه الفاتورة من قبل");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", proProductMovementDTO.getMsg()));
                proProductMovementDTO.getProProductMovementDetailDTOList().remove(proProductMovementDetailDTOSelected);
            } else {
                proProductMovementDetailDTOSelected.setInvPurchesInvoiceIdBackUp(proProductMovementDetailDTOSelected.getInvPurchesInvoiceId());

            }
        }
    }

    public void backup() {
        for (ProProductMovementDetailDTO detailDTO : proProductMovementDTO.getProProductMovementDetailDTOList()) {
            if (detailDTO.getInvPurchesInvoiceId() == null && detailDTO.getInvPurchesInvoiceIdBackUp() !=null ) {
                detailDTO.setInvPurchesInvoiceId(detailDTO.getInvPurchesInvoiceIdBackUp());
            } else if (detailDTO.getInvPurchesInvoiceId() != null && detailDTO.getInvPurchesInvoiceIdBackUp() == null) {
                
                detailDTO.setInvPurchesInvoiceIdBackUp(detailDTO.getInvPurchesInvoiceId());
            }
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

    public List<InvInventoryDTO> complete(String query) {
        try {
            List<InvInventoryDTO> supplierList = invGalaryDTOs;
            if (query == null || query.trim().equals("")) {

                invInventoryDTOConverter = new InvInventoryDTOConverter(invGalaryDTOs);
                return supplierList;
            }
            List<InvInventoryDTO> filteredSuppliers = new ArrayList<>();

            String nameAr;
            String code;
            InvInventoryDTO supplierFilter;
            for (int i = 0; i < invGalaryDTOs.size(); i++) {
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

    public List<InvInventoryDTO> complete1(String query) {
        try {
            List<InvInventoryDTO> supplierList = invFactoryDTOs;
            if (query == null || query.trim().equals("")) {

                invInventoryDTOConverter1 = new InvInventoryDTOConverter(invFactoryDTOs);
                return supplierList;
            }
            List<InvInventoryDTO> filteredSuppliers = new ArrayList<>();

            String nameAr;
            String code;
            InvInventoryDTO supplierFilter;
            for (int i = 0; i < invFactoryDTOs.size(); i++) {
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

            invInventoryDTOConverter1 = new InvInventoryDTOConverter(filteredSuppliers);
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

    public List<ProProductMovementDetailDTO> getProProductMovementDetailDTOs1() {
        if (proProductMovementDetailDTOs1 == null || proProductMovementDetailDTOs1.isEmpty()) {
            proProductMovementDetailDTOs1 = proProductionMovementDetailService.getAllInvPurchaseInvoice(getUserData().getUser(), proProductMovementDTO);
            //setProProductMovementDetailDTOConvertor(new ProProductMovementDetailDTOConvertor(proProductMovementDetailDTOs1));
        }
        return proProductMovementDetailDTOs1;
    }

    public void setProProductMovementDetailDTOs1(List<ProProductMovementDetailDTO> proProductMovementDetailDTOs1) {
        this.proProductMovementDetailDTOs1 = proProductMovementDetailDTOs1;
    }

    public List<InvInventoryDTO> getInvGalaryDTOs() {
        if (invGalaryDTOs == null || invGalaryDTOs.isEmpty()) {
            invGalaryDTOs = invInventoryService.getInventoryByInvType(getUserData().getUser(), 2);
            setInvInventoryDTOConverter(new InvInventoryDTOConverter(invGalaryDTOs));
        }
        return invGalaryDTOs;
    }

    public void setInvGalaryDTOs(List<InvInventoryDTO> invGalaryDTOs) {
        this.invGalaryDTOs = invGalaryDTOs;
    }

    public List<InvInventoryDTO> getInvFactoryDTOs() {
        if (invFactoryDTOs == null || invFactoryDTOs.isEmpty()) {
            invFactoryDTOs = invInventoryService.getInventoryByInvType(getUserData().getUser(), 1);
            setInvInventoryDTOConverter1(new InvInventoryDTOConverter(invFactoryDTOs));
        }
        return invFactoryDTOs;
    }

    public void setInvFactoryDTOs(List<InvInventoryDTO> invFactoryDTOs) {
        this.invFactoryDTOs = invFactoryDTOs;
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

    public InvInventoryDTOConverter getInvInventoryDTOConverter1() {
        return invInventoryDTOConverter1;
    }

    public void setInvInventoryDTOConverter1(InvInventoryDTOConverter invInventoryDTOConverter1) {
        this.invInventoryDTOConverter1 = invInventoryDTOConverter1;
    }

}
