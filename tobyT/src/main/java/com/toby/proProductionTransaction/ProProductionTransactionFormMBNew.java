package com.toby.proProductionTransaction;

import com.toby.businessservice.InvPurchaseInvoiceDetailService;
import com.toby.businessservice.ProProductionItemsTransactionService;
import com.toby.businessservice.ProProductionTransactionOfInvoiceService;
import com.toby.businessservice.TobyUserProproductionService;
import com.toby.converter.TobyUserDTOConverter;
import com.toby.converter.TobyUserProproductionDTOConverter;
import com.toby.dto.ProProductionTransactionDTO;
import com.toby.dto.ProProductionTransactionDetailDetailViewDTO;
import com.toby.dto.TobyUserDTO;
import com.toby.dto.TobyUserProproductionDTO;
import com.toby.toby.BaseListBean;
import com.toby.views.ProProductionTransactionOfInvoice;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "proProductionTransactionFormMBNew")
@ViewScoped
public class ProProductionTransactionFormMBNew extends BaseListBean {

    private List<TobyUserDTO> tobyUserDTOList;
    private TobyUserDTOConverter tobyUserDTOConverter;
    private TobyUserDTO tobyUserDTOSelected;

    private List<TobyUserProproductionDTO> tobyUserProproductionDTOList;
    private TobyUserProproductionDTOConverter tobyUserProproductionDTOConverter;
    private TobyUserProproductionDTO tobyUserProproductionDTOSelected;

    private Integer invInvoiceSerial;

    private List<ProProductionTransactionDetailDetailViewDTO> detailDetailDetailViewDTOList;
    ;
    
    private ProProductionTransactionDetailDetailViewDTO proProductionTransactionDetailDetailViewDTOSelected;

    @EJB
    private TobyUserProproductionService tobyUserProproductionService;
    @EJB
    private InvPurchaseInvoiceDetailService invPurchaseInvoiceDetailService;
    @EJB
    private ProProductionTransactionOfInvoiceService proProductionTransactionOfInvoiceService;
    @EJB
    private ProProductionItemsTransactionService proProductionItemsTransactionService;

    public ProProductionTransactionFormMBNew() {

    }

    public void changeUser(TobyUserDTO tobyUserDTO) {
        if (tobyUserDTO != null) {
            setTobyUserDTOSelected(tobyUserDTO);
            tobyUserProproductionDTOList = getTobyUserDTOSelected().getTobyUserProproductionDTOList();
            setTobyUserProproductionDTOConverter(new TobyUserProproductionDTOConverter(tobyUserProproductionDTOList));
        }
    }

    public void updateDetailItems() {
        if (tobyUserProproductionDTOSelected != null) {
            setDetailDetailDetailViewDTOList(invPurchaseInvoiceDetailService.getInvPurchaseInvoiceDetailItems(invInvoiceSerial, getTobyUserProproductionDTOSelected().getProproductionId()));

            for (ProProductionTransactionDetailDetailViewDTO detailViewDTO : getDetailDetailDetailViewDTOList()) {
                ProProductionTransactionOfInvoice proProductionTransactionOfInvoice = proProductionTransactionOfInvoiceService.getRenaimNumberListOfproduction(invInvoiceSerial, detailViewDTO.getInvItemCode(), getTobyUserProproductionDTOSelected().getProproductionId(), getUserData().getUser());
                if (proProductionTransactionOfInvoice == null) {
                    detailViewDTO.setNumberRemain(detailViewDTO.getQuantity());
                } else {
                    detailViewDTO.setNumberRemain(proProductionTransactionOfInvoice.getNumberRemain());
                }
                detailViewDTO.setNumberExcute(detailViewDTO.getNumberRemain());
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
        proProductionTransactionDetailDetailViewDTOSelected.setCost(getTobyUserProproductionDTOSelected().getProproductionPrice());
        ProProductionTransactionDTO proProductionTransactionDTO = new ProProductionTransactionDTO();
        proProductionTransactionDTO.setId(tobyUserProproductionDTOSelected.getProproductionId());
        proProductionItemsTransactionService.saveProProductionItemsTransaction(proProductionTransactionDetailDetailViewDTOSelected, proProductionTransactionDTO, getUserData().getUser());
    }

    @Override
    @PostConstruct
    public void init() {

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
     * @return the tobyUserDTOList
     */
    public List<TobyUserDTO> getTobyUserDTOList() {
        if (tobyUserDTOList == null) {
            tobyUserDTOList = tobyUserProproductionService.getUsersWithProductionStagesForBranch(getUserData().getUserBranch().getId());
            setTobyUserDTOConverter(new TobyUserDTOConverter(tobyUserDTOList));
        }
        return tobyUserDTOList;
    }

    /**
     * @param tobyUserDTOList the tobyUserDTOList to set
     */
    public void setTobyUserDTOList(List<TobyUserDTO> tobyUserDTOList) {
        this.tobyUserDTOList = tobyUserDTOList;
    }

    /**
     * @return the tobyUserDTOConverter
     */
    public TobyUserDTOConverter getTobyUserDTOConverter() {
        return tobyUserDTOConverter;
    }

    /**
     * @param tobyUserDTOConverter the tobyUserDTOConverter to set
     */
    public void setTobyUserDTOConverter(TobyUserDTOConverter tobyUserDTOConverter) {
        this.tobyUserDTOConverter = tobyUserDTOConverter;
    }

    /**
     * @return the tobyUserProproductionDTOList
     */
    public List<TobyUserProproductionDTO> getTobyUserProproductionDTOList() {
        return tobyUserProproductionDTOList;
    }

    /**
     * @param tobyUserProproductionDTOList the tobyUserProproductionDTOList to
     * set
     */
    public void setTobyUserProproductionDTOList(List<TobyUserProproductionDTO> tobyUserProproductionDTOList) {
        this.tobyUserProproductionDTOList = tobyUserProproductionDTOList;
    }

    /**
     * @return the invInvoiceSerial
     */
    public Integer getInvInvoiceSerial() {
        return invInvoiceSerial;
    }

    /**
     * @param invInvoiceSerial the invInvoiceSerial to set
     */
    public void setInvInvoiceSerial(Integer invInvoiceSerial) {
        this.invInvoiceSerial = invInvoiceSerial;
    }

    /**
     * @return the tobyUserProproductionDTOSelected
     */
    public TobyUserProproductionDTO getTobyUserProproductionDTOSelected() {
        return tobyUserProproductionDTOSelected;
    }

    /**
     * @param tobyUserProproductionDTOSelected the
     * tobyUserProproductionDTOSelected to set
     */
    public void setTobyUserProproductionDTOSelected(TobyUserProproductionDTO tobyUserProproductionDTOSelected) {
        this.tobyUserProproductionDTOSelected = tobyUserProproductionDTOSelected;
    }

    /**
     * @return the detailDetailDetailViewDTOList
     */
    public List<ProProductionTransactionDetailDetailViewDTO> getDetailDetailDetailViewDTOList() {
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
     * @return the tobyUserDTOSelected
     */
    public TobyUserDTO getTobyUserDTOSelected() {
        return tobyUserDTOSelected;
    }

    /**
     * @param tobyUserDTOSelected the tobyUserDTOSelected to set
     */
    public void setTobyUserDTOSelected(TobyUserDTO tobyUserDTOSelected) {
        this.tobyUserDTOSelected = tobyUserDTOSelected;
    }

    /**
     * @return the tobyUserProproductionDTOConverter
     */
    public TobyUserProproductionDTOConverter getTobyUserProproductionDTOConverter() {
        return tobyUserProproductionDTOConverter;
    }

    /**
     * @param tobyUserProproductionDTOConverter the
     * tobyUserProproductionDTOConverter to set
     */
    public void setTobyUserProproductionDTOConverter(TobyUserProproductionDTOConverter tobyUserProproductionDTOConverter) {
        this.tobyUserProproductionDTOConverter = tobyUserProproductionDTOConverter;
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
}
