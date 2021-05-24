/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.toby;

import com.toby.businessservice.TobyUserInventoryService;
import com.toby.businessservice.InvDialogueService;
import com.toby.businessservice.ProFactoryDeliveryService;
import com.toby.businessservice.ReportManagementService;
import com.toby.bussinessservice.global.AutoCompleteService;
import com.toby.converter.InvInventoryDTOConverter;
import com.toby.define.ModuleClassEnum;
import com.toby.define.ScreenNameClassEnum;
import com.toby.dto.InvDailogueData;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.print.IReportService;
import com.toby.print.IReportServiceImpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author m_els
 */
public abstract class ProductionBasicDataForm extends BaseFormBean {
    
    private Integer siteType;
    
    private InvInvoiceDTO invInvoiceDTO;
    private InvInvoiceDTO invInvoiceDTOSelected;
    private List<InvInvoiceDTO> invInvoiceDTOListSelected;
    private List<InvInvoiceDTO> invInvoiceDTOListAfterSave;
    private Integer invoiceNumber;
    
    IReportService iReportService = new IReportServiceImpl();
    
    private LazyDataModel<InvInvoiceDTO> dataModel;
    private LazyDataModel<InvDailogueData> dataModelInvDailogueData;
    
    private InvInventoryDTOConverter gallaryDTOConverter;
    private List<InvInventoryDTO> gallaryList;
    
    @EJB
    AutoCompleteService autoCompleteService;
    @EJB
    private TobyUserInventoryService tobyUserInventoryService;
    @EJB
    private ProFactoryDeliveryService proFactoryDeliveryService;
    @EJB
    private ReportManagementService reportManagementService;
    @EJB
    private InvDialogueService invDialogueService;
    
    @Override
    public abstract String save();
    
    @Override
    public abstract void init();
    
    public abstract String exit();
    
    @Override
    public abstract void load();
    
    @Override
    public abstract String getScreenName();
    
    public void convertDateFrom() {
        getInvInvoiceDTO().setDate(findDateFromHijriFrom());
    }
    
    public void reset() {
        try {
            setInvInvoiceDTO(null);
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "resetInvPurchaseInvoiceForm");
        }
    }
    
    public void fillDefaultList() {
        //getGallaryList();
    }
    
    public void fillLists() {
        try {
            fillDefaultList();
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "fillLists");
        }
        
    }
    
    public void deleteInvDetail(InvDetailDTO invPurchaseInvoiceDetailsValidate) {
        try {
            if (invPurchaseInvoiceDetailsValidate.getId() != null) {
                getInvInvoiceDTO().getInvPurchaseInvoiceDetailsDeletedList().add(invPurchaseInvoiceDetailsValidate);
            }
            getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().remove(invPurchaseInvoiceDetailsValidate);
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "deleteInvPurchaseInvoiceDetail");
        }
    }
    
    public void deleteAllSelection() {
        if (getInvInvoiceDTO().getInvPurchaseInvoiceDetailListSelected() != null) {
            for (InvDetailDTO detailEntity : getInvInvoiceDTO().getInvPurchaseInvoiceDetailListSelected()) {
                if (detailEntity.getId() != null) {
                    getInvInvoiceDTO().getInvPurchaseInvoiceDetailsDeletedList().add(detailEntity);
                }
                getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().remove(detailEntity);
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد تفاصيل للفاتورة"));
        }
    }

    ////////// load dialogue //////////
    public void loadDailogue() {
        this.setDataModelInvDailogueData(new LazyDataModel<InvDailogueData>() {
            private static final long serialVersionUID = 1L;
            
            @Override
            public InvDailogueData getRowData(String rowKey) {
                for (InvDailogueData invDailogueData : getInvInvoiceDTO().getInvDailogueDataList()) {
                    if (invDailogueData.getIndex().toString().equals(rowKey)) {
                        return invDailogueData;
                    }
                }
                
                return null;
            }
            
            @Override
            public Integer getRowKey(InvDailogueData invDailogueData) {
                return invDailogueData.getIndex();
            }
            
            @Override
            public List<InvDailogueData> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                
                setRowCount(invDialogueService.getTotalRegistors(getUserData().getUserBranch().getId(), filters, null, getInvInvoiceDTO().getScreenName()).intValue());
                getInvInvoiceDTO().setInvDailogueDataList(invDialogueService.loadInvDailogueData(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), filters, getInvInvoiceDTO().getInventorySetupDTO(), getUserData().getUserBranch().getId(), getInvInvoiceDTO().getScreenName()));
                return getInvInvoiceDTO().getInvDailogueDataList();
            }
            
        });
        OpenDlg("loaddlg");
    }
    
    public void loadDlg() {
        if (getInvInvoiceDTO().getLoadedSerial() != null && getInvInvoiceDTO().getNeedLoadedSerial() == null) {
            getInvInvoiceDTO().setNeedLoadedSerial(getInvInvoiceDTO().getLoadedSerial());
        }
        if (getInvInvoiceDTO().getInvDailogeDataSelected() != null || getInvInvoiceDTO().getNeedLoadedSerial() != null) {
            setInvInvoiceDTO(invDialogueService.loadDlg(getInvInvoiceDTO()));
            fillLists();
            CloseDlg("loaddlg");
            errorMsg(getInvInvoiceDTO().getMsg());
        }
    }
    
    public void loadMultiSelection() throws CloneNotSupportedException {
        
        if (getInvInvoiceDTO().getInvDailogeDataListSelected() != null || getInvInvoiceDTO().getNeedLoadedSerial() != null) {
            setInvInvoiceDTOListSelected(new ArrayList<>());
            if (getInvInvoiceDTO().getInvDailogeDataListSelected() != null && !getInvInvoiceDTO().getInvDailogeDataListSelected().isEmpty()) {
                for (InvDailogueData dailogueData : getInvInvoiceDTO().getInvDailogeDataListSelected()) {
                    initDailogue(dailogueData);
                }
                setInvInvoiceDTO(getInvInvoiceDTOListSelected().get(0));
            } else {
                setInvInvoiceDTO(invDialogueService.loadDlg(getInvInvoiceDTO()));
            }
            
            CloseDlg("loaddlg");
            errorMsg(getInvInvoiceDTO().getMsg());
        }
    }
    
    private void initDailogue(InvDailogueData dailogueData) throws CloneNotSupportedException {
        InvInvoiceDTO invoiceDTO = (InvInvoiceDTO) getInvInvoiceDTO().clone();
        invoiceDTO.setInvDailogeDataSelected(dailogueData);
        invoiceDTO.setIsOld(Boolean.FALSE);
        InvInvoiceDTO dTO = invDialogueService.loadDlg(invoiceDTO);
        getInvInvoiceDTOListSelected().add(dTO);
    }
    
    public void forward() {
        if (getInvInvoiceDTOListSelected() != null && !getInvInvoiceDTOListSelected().isEmpty()) {
            int size = getInvInvoiceDTOListSelected().size();
            if (size > (getInvoiceNumber() + 1)) {
                setInvoiceNumber(getInvoiceNumber() + 1);
                setInvInvoiceDTO(getInvInvoiceDTOListSelected().get(getInvoiceNumber()));
            }
        }
    }
    
    public void backward() {
        if (getInvInvoiceDTOListSelected() != null && !getInvInvoiceDTOListSelected().isEmpty()) {
            if (getInvoiceNumber() > 0) {
                setInvoiceNumber(getInvoiceNumber() - 1);
                setInvInvoiceDTO(getInvInvoiceDTOListSelected().get(getInvoiceNumber()));
            }
        }
    }
    
    public void print() {
        iReportService.printReportInvoice(getInvInvoiceDTO(), getUserData());
    }
    
    public void printList() {
        
    }
    
    public void hishis() {
        System.out.println("Tamam");
    }
    
    public List<InvInventoryDTO> completeGallary(String query) {
        return AutoComplete.completeInventoryDTO(query, getGallaryList(), getGallaryDTOConverter());
    }

    /**
     * @return the siteType
     */
    public Integer getSiteType() {
        return siteType;
    }

    /**
     * @param siteType the siteType to set
     */
    public void setSiteType(Integer siteType) {
        this.siteType = siteType;
    }

    /**
     * @return the invInvoiceDTO
     */
    public InvInvoiceDTO getInvInvoiceDTO() {
        if (invInvoiceDTO == null) {
            invInvoiceDTO = new InvInvoiceDTO();
            invInvoiceDTO = settingInventory(invInvoiceDTO);
        }
        return invInvoiceDTO;
    }
    
    public InvInvoiceDTO settingInventory(InvInvoiceDTO invInvoiceDTO) {
        
        invInvoiceDTO.setCompanyActivity(getUserData().getUserBranch().getCompanyActivity());
        
        findScreenName();
        
        invInvoiceDTO.setDate(new Date());
        
        invInvoiceDTO.setReportManagement(reportManagementService.reportManagementByBranch(getUserData().getUserBranch().getId()));
        
        invInvoiceDTO.setBranchId(getUserData().getUserBranch().getId());
        
        return invInvoiceDTO;
    }
    
    public void findScreenName() {
        if (getUri().contains("profactorydeviveryForm")) {
            getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.PROFACTORYDELIVERY);
            getInvInvoiceDTO().setType(0);
        } else if (getUri().contains("proSendToGallary")) {
            getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.PROSENDTOGALLARY);
            getInvInvoiceDTO().setType(1);
        } else if (getUri().contains("proRecieveGallary")) {
            getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.PRORECIEVEGALLARY);
            getInvInvoiceDTO().setType(2);
        } else if (getUri().contains("proSales")) {
            getInvInvoiceDTO().setScreenName(ScreenNameClassEnum.PROSALES);
            getInvInvoiceDTO().setType(3);
        }
        getInvInvoiceDTO().setModuleClassEnum(ModuleClassEnum.PRODUCTION);
    }
    
    public void fillInvInvoiceDTO() {
        fillInvInvoiceDetailDTO();
        setInvInvoiceDTO(settingInventory(getInvInvoiceDTO()));
        fillLists();
    }
    
    private void fillInvInvoiceDetailDTO() {
        if (getUri().contains("profactorydeviveryForm") || getUri().contains("proSendToGallaryForm") || getUri().contains("proSales")) {
            getInvInvoiceDTO().setId((Integer) getContext().getSessionMap().get("proFactoryDeliverySelected"));
            setInvInvoiceDTO(proFactoryDeliveryService.findProFactoryDeliveryById(getInvInvoiceDTO()));
        }
    }

    /**
     * @param invPurchaseInvoiceDTO the invInvoiceDTO to set
     */
    public void setInvInvoiceDTO(InvInvoiceDTO invPurchaseInvoiceDTO) {
        this.invInvoiceDTO = invPurchaseInvoiceDTO;
    }

    /**
     * @return the dataModel
     */
    public LazyDataModel<InvInvoiceDTO> getDataModel() {
        return dataModel;
    }

    /**
     * @param dataModel the dataModel to set
     */
    public void setDataModel(LazyDataModel<InvInvoiceDTO> dataModel) {
        this.dataModel = dataModel;
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
     * @return the dataModelInvDailogueData
     */
    public LazyDataModel<InvDailogueData> getDataModelInvDailogueData() {
        return dataModelInvDailogueData;
    }

    /**
     * @param dataModelInvDailogueData the dataModelInvDailogueData to set
     */
    public void setDataModelInvDailogueData(LazyDataModel<InvDailogueData> dataModelInvDailogueData) {
        this.dataModelInvDailogueData = dataModelInvDailogueData;
    }

    /**
     * @return the gallaryDTOConverter
     */
    public InvInventoryDTOConverter getGallaryDTOConverter() {
        return gallaryDTOConverter;
    }

    /**
     * @param gallaryDTOConverter the gallaryDTOConverter to set
     */
    public void setGallaryDTOConverter(InvInventoryDTOConverter gallaryDTOConverter) {
        this.gallaryDTOConverter = gallaryDTOConverter;
    }

    /**
     * @return the gallaryList
     */
    public List<InvInventoryDTO> getGallaryList() {
        if (gallaryList == null || gallaryList.isEmpty()) {
            gallaryList = tobyUserInventoryService.getAllGallaryByUserAndBranch(getUserData().getUser());
            setGallaryDTOConverter(new InvInventoryDTOConverter(gallaryList));
        }
        return gallaryList;
    }

    /**
     * @param gallaryList the gallaryList to set
     */
    public void setGallaryList(List<InvInventoryDTO> gallaryList) {
        this.gallaryList = gallaryList;
    }

    /**
     * @return the invInvoiceDTOListSelected
     */
    public List<InvInvoiceDTO> getInvInvoiceDTOListSelected() {
        if (invInvoiceDTOListSelected == null) {
            invInvoiceDTOListSelected = new ArrayList<>();
        }
        return invInvoiceDTOListSelected;
    }

    /**
     * @param invInvoiceDTOListSelected the invInvoiceDTOListSelected to set
     */
    public void setInvInvoiceDTOListSelected(List<InvInvoiceDTO> invInvoiceDTOListSelected) {
        this.invInvoiceDTOListSelected = invInvoiceDTOListSelected;
    }

    /**
     * @return the invoiceNumber
     */
    public Integer getInvoiceNumber() {
        if (invoiceNumber == null) {
            invoiceNumber = 0;
        }
        return invoiceNumber;
    }

    /**
     * @param invoiceNumber the invoiceNumber to set
     */
    public void setInvoiceNumber(Integer invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    /**
     * @return the invInvoiceDTOListAfterSave
     */
    public List<InvInvoiceDTO> getInvInvoiceDTOListAfterSave() {
        if (invInvoiceDTOListAfterSave == null) {
            invInvoiceDTOListAfterSave = new ArrayList<>();
        }
        return invInvoiceDTOListAfterSave;
    }

    /**
     * @param invInvoiceDTOListAfterSave the invInvoiceDTOListAfterSave to set
     */
    public void setInvInvoiceDTOListAfterSave(List<InvInvoiceDTO> invInvoiceDTOListAfterSave) {
        this.invInvoiceDTOListAfterSave = invInvoiceDTOListAfterSave;
    }
}
