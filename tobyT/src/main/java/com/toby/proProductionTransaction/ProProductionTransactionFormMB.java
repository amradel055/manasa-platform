package com.toby.proProductionTransaction;

import com.toby.businessservice.InvPurchaseInvoiceDetailService;
import com.toby.businessservice.ProProductionItemsTransactionService;
import com.toby.businessservice.ProProductionTransactionOfInvoiceService;
import com.toby.businessservice.ProProductionTransactionService;
import com.toby.converter.ProProductionStagesDTOConverter;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.ProProductionItemsTransactionDTO;
import com.toby.dto.ProProductionProductNumberDTO;
import com.toby.dto.ProProductionStagesDTO;
import com.toby.dto.ProProductionTransactionDTO;
import com.toby.dto.ProProductionTransactionDetailDetailViewDTO;
import com.toby.dto.ProProductionTransactionDetailViewDTO;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import com.toby.views.ProProductionTransactionOfInvoice;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;

@Named(value = "proProductionTransactionListMB")
@ViewScoped
public class ProProductionTransactionFormMB extends BaseListBean {

    private List<ProProductionTransactionDTO> proProductionTransactionDTOList;
    private ProProductionItemsTransactionDTO proProductionItemsTransactionDTO;
    private List<ProProductionStagesDTO> proProductionStagesDTOs;
    private ProProductionTransactionDetailDetailViewDTO proProductionTransactionDetailDetailViewDTOSelected;
    private ProProductionStagesDTOConverter proProductionStagesDTOConverter;

    private ProProductionStagesDTO proProductionStagesDTO;
    private ProProductionTransactionDTO proProductionTransactionDTO;
    private ProProductionProductNumberDTO proProductionProductNumberDTO;
    private ProProductionTransactionDetailViewDTO proProductionTransactionDetailViewDTO;
    private ProProductionTransactionDetailDetailViewDTO productionTransactionDetailDetailViewDTO;

    private LazyDataModel<InvInvoiceDTO> dataModel;
    @EJB
    private InvPurchaseInvoiceDetailService invPurchaseInvoiceDetailService;
    @EJB
    private ProProductionTransactionService proProductionTransactionService;
    @EJB
    private ProProductionTransactionOfInvoiceService proProductionTransactionOfInvoiceService;
    @EJB
    private ProProductionItemsTransactionService proProductionItemsTransactionService;

    private List<ProProductionProductNumberDTO> productNumberDTOList;
    private List<ProProductionTransactionDetailViewDTO> detailDetailViewDTOList;
    private List<ProProductionTransactionDetailDetailViewDTO> detailDetailDetailViewDTOList;

    private ProProductionStagesDTO proProductionId;

    private Integer fromNumber;
    private Integer toNumber;

    public ProProductionTransactionFormMB() {
    }

    @Override
    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));

        if (getUserData().getProProductionStagesDTOList() == null | getUserData().getProProductionStagesDTOList().isEmpty()) {
            HttpServletRequest origRequest = (HttpServletRequest) context.getRequest();
            String contextPath = origRequest.getContextPath();
            try {
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect(contextPath + "/base/error.xhtml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (getProProductionStagesDTOs() != null && !getProProductionStagesDTOs().isEmpty()) {
                setProProductionId(getProProductionStagesDTOs().get(0));
                check();
            }            
        }
    }

    public String goToDetail() {
        try {
            if (proProductionTransactionDTO != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
//                context.getSessionMap().put("invPurchaseInvoiceIdSeclected", proProductionTransactionDTO.getInvPurchaseInvoiceId());
                context.getSessionMap().put("ProFactoryDeliveryIdSelected", proProductionTransactionDTO.getProFactoryDeliveryId());
                context.getSessionMap().put("ScreenMode", "editInvoice");
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

    public void saveAll() {
        for (ProProductionProductNumberDTO numberDTO : productNumberDTOList) {
            if (numberDTO.getProductNumber().compareTo(getFromNumber()) != -1 && numberDTO.getProductNumber().compareTo(getToNumber()) != 1) {
                proProductionProductNumberDTO = numberDTO;
                updateDetailItems();
                for (ProProductionTransactionDetailDetailViewDTO dTO : getDetailDetailDetailViewDTOList()) {
                    proProductionTransactionDetailDetailViewDTOSelected = dTO;
                    proProductionTransactionDetailDetailViewDTOSelected.setNumberExcute(dTO.getNumberRemain());
                    saveDetailDetail();
                }
            }
        }

        setFromNumber(0);
        setToNumber(0);
        reload();
    }

    public void reload() {
        check();
        if (getProProductionTransactionDTOList() == null && !getProProductionTransactionDTOList().isEmpty()) {
            setProProductionTransactionDTO(getProProductionTransactionDTOList().get(0));
            selectUserRole1(null);
            if (getProductNumberDTOList() == null && !getProductNumberDTOList().isEmpty()) {
                setProProductionProductNumberDTO(getProductNumberDTOList().get(0));
                selectUserRole3(null);
            }
        }
    }

    public String save() {

        if (proProductionTransactionDetailDetailViewDTOSelected.getNumberExcute().compareTo(proProductionTransactionDetailDetailViewDTOSelected.getNumberRemain()) > 0 || proProductionTransactionDetailDetailViewDTOSelected.getNumberExcute().compareTo(BigDecimal.ZERO) < 0) {
            proProductionTransactionDetailDetailViewDTOSelected.setMsg("الكميه هذا الصنف غير موجوده" + proProductionTransactionDetailDetailViewDTOSelected.getInvItemName());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR!", proProductionTransactionDetailDetailViewDTOSelected.getMsg()));

        } else {
            saveDetailDetail();

            updateDetailItems();

            proProductionTransactionDetailDetailViewDTOSelected.setMsg("تم الحفظ بنجاح");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "success!", proProductionTransactionDetailDetailViewDTOSelected.getMsg()));

        }
        return "";

    }

    private void saveDetailDetail() {
        proProductionTransactionDetailDetailViewDTOSelected.setCost(getProProductionId().getPrice());
        proProductionItemsTransactionService.saveProProductionItemsTransaction(proProductionTransactionDetailDetailViewDTOSelected, proProductionTransactionDTO, getUserData().getUser());
    }

    public void selectUserRole1(SelectEvent event) {
        try {
            if (event != null) {
                proProductionTransactionDTO = (ProProductionTransactionDTO) event.getObject();
            }
            if (proProductionTransactionDTO != null) {
                productNumberDTOList = new ArrayList<>();
                detailDetailViewDTOList = new ArrayList<>();
                detailDetailDetailViewDTOList = new ArrayList<>();

                proProductionProductNumberDTO = null;
                proProductionTransactionDetailViewDTO = null;
                proProductionTransactionDetailDetailViewDTOSelected = null;

                setProductNumberDTOList(invPurchaseInvoiceDetailService.getInvPurchaseInvoiceDetailClothNumber(getProProductionTransactionDTO().getInvPurchaseInvoiceId(), getProProductionId().getId()));

                for (ProProductionProductNumberDTO dTO : getProductNumberDTOList()) {
                    if (dTO.getProductNumber().compareTo(getFromNumber()) == -1) {
                        setFromNumber(dTO.getProductNumber());
                    }
                    if (dTO.getProductNumber().compareTo(getToNumber()) == 1) {
                        setToNumber(dTO.getProductNumber());
                    }
                }
            }
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "selectUserRole");
        }
    }

    public void selectUserRole2(SelectEvent event) {
        try {
            proProductionProductNumberDTO = (ProProductionProductNumberDTO) event.getObject();
            if (proProductionProductNumberDTO != null && proProductionTransactionDTO != null) {
                detailDetailViewDTOList = new ArrayList<>();
                detailDetailDetailViewDTOList = new ArrayList<>();

                proProductionTransactionDetailViewDTO = null;
                proProductionTransactionDetailDetailViewDTOSelected = null;

                setDetailDetailViewDTOList(invPurchaseInvoiceDetailService.getInvPurchaseInvoiceDetailItemCompleted(getProProductionTransactionDTO().getInvPurchaseInvoiceId(), getProProductionProductNumberDTO().getProductNumber(), getProProductionId().getId()));
            }
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "selectUserRole");
        }
    }

    public void selectUserRole3(SelectEvent event) {
        try {
            if (event != null) {
                proProductionProductNumberDTO = (ProProductionProductNumberDTO) event.getObject();
            }

            detailDetailDetailViewDTOList = new ArrayList<>();

            proProductionTransactionDetailDetailViewDTOSelected = null;

//            proProductionTransactionDetailViewDTO = (ProProductionTransactionDetailViewDTO) event.getObject();
            updateDetailItems();
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "selectUserRole");
        }
    }

    private void updateDetailItems() {
        if (proProductionProductNumberDTO != null) {
            setDetailDetailDetailViewDTOList(invPurchaseInvoiceDetailService.getInvPurchaseInvoiceDetailItems(proProductionTransactionDTO.getInvPurchaseInvoiceId(),
                    proProductionProductNumberDTO.getProductNumber(), getProProductionId().getId()));

            for (ProProductionTransactionDetailDetailViewDTO detailViewDTO : getDetailDetailDetailViewDTOList()) {
                ProProductionTransactionOfInvoice proProductionTransactionOfInvoice = proProductionTransactionOfInvoiceService.getRenaimNumberListOfproduction(proProductionTransactionDTO.getInvPurchaseInvoiceId(), proProductionProductNumberDTO.getProductNumber(), detailViewDTO.getInvItemCode(), getProProductionId().getId(), getUserData().getUser());
                if (proProductionTransactionOfInvoice == null) {
                    detailViewDTO.setNumberRemain(detailViewDTO.getQuantity());
                } else {
                    detailViewDTO.setNumberRemain(proProductionTransactionOfInvoice.getNumberRemain());
                }
                detailViewDTO.setNumberExcute(detailViewDTO.getNumberRemain());
            }

        }
    }

    public void check() {
        proProductionTransactionDTOList = new ArrayList<>();
        productNumberDTOList = new ArrayList<>();
        detailDetailViewDTOList = new ArrayList<>();
        detailDetailDetailViewDTOList = new ArrayList<>();

        proProductionTransactionDTO = null;
        proProductionProductNumberDTO = null;
        proProductionTransactionDetailViewDTO = null;
        proProductionTransactionDetailDetailViewDTOSelected = null;
        if (getProProductionId() != null) {
            proProductionTransactionDTOList = proProductionTransactionService.getProProductionTransactionDTOofViewList(getProProductionId(), getUserData().getUser());
        }
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToEdit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the proProductionTransactionDTOList
     */
    public List<ProProductionTransactionDTO> getProProductionTransactionDTOList() {
        return proProductionTransactionDTOList;
    }

    /**
     * @param proProductionTransactionDTOList the
     * proProductionTransactionDTOList to set
     */
    public void setProProductionTransactionDTOList(List<ProProductionTransactionDTO> proProductionTransactionDTOList) {
        this.proProductionTransactionDTOList = proProductionTransactionDTOList;
    }

    /**
     * @return the proProductionTransactionDTO
     */
    public ProProductionTransactionDTO getProProductionTransactionDTO() {
        if (proProductionTransactionDTO == null) {
            proProductionTransactionDTO = new ProProductionTransactionDTO();
        }
        return proProductionTransactionDTO;
    }

    /**
     * @param proProductionTransactionDTO the proProductionTransactionDTO to set
     */
    public void setProProductionTransactionDTO(ProProductionTransactionDTO proProductionTransactionDTO) {
        this.proProductionTransactionDTO = proProductionTransactionDTO;
    }

    /**
     * @return the proProductionStagesDTOConverter
     */
    public ProProductionStagesDTOConverter getProProductionStagesDTOConverter() {
        return proProductionStagesDTOConverter;
    }

    /**
     * @param proProductionStagesDTOConverter the
     * proProductionStagesDTOConverter to set
     */
    public void setProProductionStagesDTOConverter(ProProductionStagesDTOConverter proProductionStagesDTOConverter) {
        this.proProductionStagesDTOConverter = proProductionStagesDTOConverter;
    }

    /**
     * @return the proProductionStagesDTOs
     */
    public List<ProProductionStagesDTO> getProProductionStagesDTOs() {
        if (proProductionStagesDTOs == null || proProductionStagesDTOs.isEmpty()) {
            proProductionStagesDTOs = getUserData().getProProductionStagesDTOList();
            proProductionStagesDTOConverter = new ProProductionStagesDTOConverter(proProductionStagesDTOs);
        }
        return proProductionStagesDTOs;
    }

    /**
     * @param proProductionStagesDTOs the proProductionStagesDTOs to set
     */
    public void setProProductionStagesDTOs(List<ProProductionStagesDTO> proProductionStagesDTOs) {
        this.proProductionStagesDTOs = proProductionStagesDTOs;
    }

    /**
     * @return the proProductionProductNumberDTO
     */
    public ProProductionProductNumberDTO getProProductionProductNumberDTO() {
        if (proProductionProductNumberDTO == null) {
            proProductionProductNumberDTO = new ProProductionProductNumberDTO();
        }
        return proProductionProductNumberDTO;
    }

    /**
     * @param proProductionProductNumberDTO the proProductionProductNumberDTO to
     * set
     */
    public void setProProductionProductNumberDTO(ProProductionProductNumberDTO proProductionProductNumberDTO) {
        this.proProductionProductNumberDTO = proProductionProductNumberDTO;
    }

    /**
     * @return the proProductionTransactionDetailViewDTO
     */
    public ProProductionTransactionDetailViewDTO getProProductionTransactionDetailViewDTO() {
        if (proProductionTransactionDetailViewDTO == null) {
            proProductionTransactionDetailViewDTO = new ProProductionTransactionDetailViewDTO();
        }
        return proProductionTransactionDetailViewDTO;
    }

    /**
     * @param proProductionTransactionDetailViewDTO the
     * proProductionTransactionDetailViewDTO to set
     */
    public void setProProductionTransactionDetailViewDTO(ProProductionTransactionDetailViewDTO proProductionTransactionDetailViewDTO) {
        this.proProductionTransactionDetailViewDTO = proProductionTransactionDetailViewDTO;
    }

    /**
     * @return the productionTransactionDetailDetailViewDTO
     */
    public ProProductionTransactionDetailDetailViewDTO getProductionTransactionDetailDetailViewDTO() {
        if (productionTransactionDetailDetailViewDTO == null) {
            productionTransactionDetailDetailViewDTO = new ProProductionTransactionDetailDetailViewDTO();
        }
        return productionTransactionDetailDetailViewDTO;
    }

    /**
     * @param productionTransactionDetailDetailViewDTO the
     * productionTransactionDetailDetailViewDTO to set
     */
    public void setProductionTransactionDetailDetailViewDTO(ProProductionTransactionDetailDetailViewDTO productionTransactionDetailDetailViewDTO) {
        this.productionTransactionDetailDetailViewDTO = productionTransactionDetailDetailViewDTO;
    }

    /**
     * @return the proProductionStagesDTO
     */
    public ProProductionStagesDTO getProProductionStagesDTO() {
        if (proProductionStagesDTO == null) {
            proProductionStagesDTO = new ProProductionStagesDTO();
        }
        return proProductionStagesDTO;
    }

    /**
     * @param proProductionStagesDTO the proProductionStagesDTO to set
     */
    public void setProProductionStagesDTO(ProProductionStagesDTO proProductionStagesDTO) {
        this.proProductionStagesDTO = proProductionStagesDTO;
    }

    /**
     * @return the proProductionTransactionDetailDetailViewDTOSelected
     */
    public ProProductionTransactionDetailDetailViewDTO getProProductionTransactionDetailDetailViewDTOSelected() {
        return proProductionTransactionDetailDetailViewDTOSelected;
    }

    /**
     * @param proProductionTransactionDetailDetailViewDTOSelected the
     * proProductionTransactionDetailDetailViewDTOSelected to set
     */
    public void setProProductionTransactionDetailDetailViewDTOSelected(ProProductionTransactionDetailDetailViewDTO proProductionTransactionDetailDetailViewDTOSelected) {
        this.proProductionTransactionDetailDetailViewDTOSelected = proProductionTransactionDetailDetailViewDTOSelected;
    }

    /**
     * @return the proProductionItemsTransactionDTO
     */
    public ProProductionItemsTransactionDTO getProProductionItemsTransactionDTO() {
        return proProductionItemsTransactionDTO;
    }

    /**
     * @param proProductionItemsTransactionDTO the
     * proProductionItemsTransactionDTO to set
     */
    public void setProProductionItemsTransactionDTO(ProProductionItemsTransactionDTO proProductionItemsTransactionDTO) {
        this.proProductionItemsTransactionDTO = proProductionItemsTransactionDTO;
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
     * @return the productNumberDTOList
     */
    public List<ProProductionProductNumberDTO> getProductNumberDTOList() {
        if (productNumberDTOList == null || productNumberDTOList.isEmpty()) {
            productNumberDTOList = new ArrayList<>();
        }
        return productNumberDTOList;
    }

    /**
     * @param productNumberDTOList the productNumberDTOList to set
     */
    public void setProductNumberDTOList(List<ProProductionProductNumberDTO> productNumberDTOList) {
        this.productNumberDTOList = productNumberDTOList;
    }

    /**
     * @return the proProductionId
     */
    public ProProductionStagesDTO getProProductionId() {
        return proProductionId;
    }

    /**
     * @param proProductionId the proProductionId to set
     */
    public void setProProductionId(ProProductionStagesDTO proProductionId) {
        this.proProductionId = proProductionId;
    }

    /**
     * @return the detailDetailViewDTOList
     */
    public List<ProProductionTransactionDetailViewDTO> getDetailDetailViewDTOList() {
        if (detailDetailViewDTOList == null || detailDetailViewDTOList.isEmpty()) {
            detailDetailViewDTOList = new ArrayList<>();
        }
        return detailDetailViewDTOList;
    }

    /**
     * @param detailDetailViewDTOList the detailDetailViewDTOList to set
     */
    public void setDetailDetailViewDTOList(List<ProProductionTransactionDetailViewDTO> detailDetailViewDTOList) {
        this.detailDetailViewDTOList = detailDetailViewDTOList;
    }

    /**
     * @return the detailDetailDetailViewDTOList
     */
    public List<ProProductionTransactionDetailDetailViewDTO> getDetailDetailDetailViewDTOList() {
        if (detailDetailDetailViewDTOList == null || detailDetailDetailViewDTOList.isEmpty()) {
            detailDetailDetailViewDTOList = new ArrayList<>();
        }
        return detailDetailDetailViewDTOList;
    }

    /**
     * @param detailDetailDetailViewDTOList the detailDetailDetailViewDTOList to
     * set
     */
    public void setDetailDetailDetailViewDTOList(List<ProProductionTransactionDetailDetailViewDTO> detailDetailDetailViewDTOList) {
        this.detailDetailDetailViewDTOList = detailDetailDetailViewDTOList;
    }

    /**
     * @return the fromNumber
     */
    public Integer getFromNumber() {
        if (fromNumber == null) {
            fromNumber = 0;
        }
        return fromNumber;
    }

    /**
     * @param fromNumber the fromNumber to set
     */
    public void setFromNumber(Integer fromNumber) {
        this.fromNumber = fromNumber;
    }

    /**
     * @return the toNumber
     */
    public Integer getToNumber() {
        if (toNumber == null) {
            toNumber = 0;
        }
        return toNumber;
    }

    /**
     * @param toNumber the toNumber to set
     */
    public void setToNumber(Integer toNumber) {
        this.toNumber = toNumber;
    }

    /**
     * @return the proProductionStagesDTOList
     */
}
