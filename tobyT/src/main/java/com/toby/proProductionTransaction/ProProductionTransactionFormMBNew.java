package com.toby.proProductionTransaction;

import com.toby.businessservice.InvPurchaseInvoiceDetailService;
import com.toby.businessservice.ProProductionItemsTransactionService;
import com.toby.businessservice.ProProductionStagesService;
import com.toby.businessservice.ProProductionTransactionOfInvoiceService;
import com.toby.businessservice.TobyUserProproductionService;
import com.toby.converter.ProProductionStagesDTOConverter;
import com.toby.converter.TobyUserDTOConverter;
import com.toby.converter.TobyUserProproductionDTOConverter;
import com.toby.dto.ProProductionStagesDTO;
import com.toby.dto.ProProductionTransactionDTO;
import com.toby.dto.ProProductionTransactionDetailDetailViewDTO;
import com.toby.dto.TobyUserDTO;
import com.toby.dto.TobyUserProproductionDTO;
import com.toby.entity.TobyUser;
import com.toby.toby.AutoComplete;
import com.toby.toby.BaseListBean;
import com.toby.views.ProProductionTransactionOfInvoice;
import java.math.BigDecimal;
import java.util.ArrayList;
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
//    private TobyUserProproductionDTO tobyUserProproductionDTOSelected;

    private ProProductionStagesDTO proProductionStagesDTOSelected;
    private List<ProProductionStagesDTO> proProductionStagesDTOList;
    private ProProductionStagesDTOConverter proProductionStagesDTOConverter;

    private String invInvoiceSerial;

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
    @EJB
    private ProProductionStagesService proProductionStagesService;

    public ProProductionTransactionFormMBNew() {

    }

    public void changeUser(TobyUserDTO tobyUserDTO) {
        if (tobyUserDTO != null) {
            setTobyUserDTOSelected(tobyUserDTO);
            tobyUserProproductionDTOList = getTobyUserDTOSelected().getTobyUserProproductionDTOList();
            setTobyUserProproductionDTOConverter(new TobyUserProproductionDTOConverter(tobyUserProproductionDTOList));
        }
    }

    public void changeUser() {
        if (getTobyUserDTOSelected() != null) {
            tobyUserProproductionDTOList = getTobyUserDTOSelected().getTobyUserProproductionDTOList();
            if (tobyUserProproductionDTOList != null && !tobyUserProproductionDTOList.isEmpty()) {
                setTobyUserProproductionDTOConverter(new TobyUserProproductionDTOConverter(tobyUserProproductionDTOList));
//                setTobyUserProproductionDTOSelected(tobyUserProproductionDTOList.get(0));
            }
        }

        if (getTobyUserDTOSelected() != null) {
            proProductionStagesDTOList = getTobyUserDTOSelected().getProductionStagesDTOList();
            if (proProductionStagesDTOList != null && !proProductionStagesDTOList.isEmpty()) {
                setProProductionStagesDTOConverter(new ProProductionStagesDTOConverter(proProductionStagesDTOList));
                setProProductionStagesDTOSelected(proProductionStagesDTOList.get(0));
            }
        }

    }

    public void ccc() {
        proProductionStagesDTOList = proProductionStagesService.getAllProProductionStagesByBranchId(getUserData().getUserBranch().getId(), getUserData().getUser());
        setProProductionStagesDTOConverter(new ProProductionStagesDTOConverter(proProductionStagesDTOList));
    }

    public void updateDetailItems() {
        if (getProProductionStagesDTOSelected() != null) {
            setDetailDetailDetailViewDTOList(invPurchaseInvoiceDetailService.getInvPurchaseInvoiceDetailItems(Integer.parseInt(getInvInvoiceSerial() == null ? "0" : getInvInvoiceSerial()), getProProductionStagesDTOSelected().getId()));

            for (ProProductionTransactionDetailDetailViewDTO detailViewDTO : getDetailDetailDetailViewDTOList()) {
                ProProductionTransactionOfInvoice proProductionTransactionOfInvoice = proProductionTransactionOfInvoiceService.getRenaimNumberListOfproduction(Integer.parseInt(getInvInvoiceSerial()), detailViewDTO.getInvItemCode(), getProProductionStagesDTOSelected().getId(), getUserData().getUser());
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
        proProductionTransactionDetailDetailViewDTOSelected.setCost(getProProductionStagesDTOSelected().getPrice());
        ProProductionTransactionDTO proProductionTransactionDTO = new ProProductionTransactionDTO();
        ProProductionStagesDTO proProductionId = new ProProductionStagesDTO();
        proProductionId.setId(getProProductionStagesDTOSelected().getId());
        proProductionTransactionDTO.setProProductionId(proProductionId);
        TobyUser tobyUser = new TobyUser();
        tobyUser.setId(getTobyUserDTOSelected().getId());
        tobyUser.setCompanyId(getUserData().getCompany());
        tobyUser.setBranchId(getUserData().getUserBranch());
        proProductionItemsTransactionService.saveProProductionItemsTransaction(proProductionTransactionDetailDetailViewDTOSelected, proProductionTransactionDTO, tobyUser);
    }

    public void saveAll() {
        for (ProProductionTransactionDetailDetailViewDTO detailViewDTO : detailDetailDetailViewDTOList) {
            if (detailViewDTO.getNumberRemain().compareTo(BigDecimal.ZERO) == 1) {
                proProductionTransactionDetailDetailViewDTOSelected = detailViewDTO;
                proProductionTransactionDetailDetailViewDTOSelected.setNumberExcute(proProductionTransactionDetailDetailViewDTOSelected.getNumberRemain());
                saveDetailDetail();
            }
        }
        detailDetailDetailViewDTOList = new ArrayList<>();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "success!", "success!"));
    }

    public List<TobyUserDTO> completeTobyUserDTO(String query) {
        return AutoComplete.completeTobyUserDTO(query, getTobyUserDTOList(), getTobyUserDTOConverter());
    }

    public List<TobyUserProproductionDTO> completeTobyUserProproductionDTO(String query) {
        return AutoComplete.completeTobyUserProproductionDTO(query, getTobyUserProproductionDTOList(), getTobyUserProproductionDTOConverter());
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
//            setTobyUserDTOSelected(tobyUserDTOList.get(0));
//            if (getTobyUserDTOSelected().getTobyUserProproductionDTOList() != null && !getTobyUserDTOSelected().getTobyUserProproductionDTOList().isEmpty()) {
//                tobyUserProproductionDTOList = getTobyUserDTOSelected().getTobyUserProproductionDTOList();
//                setTobyUserProproductionDTOConverter(new TobyUserProproductionDTOConverter(tobyUserProproductionDTOList));
//            }
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
        if (getTobyUserDTOSelected() != null) {
            tobyUserProproductionDTOList = getTobyUserDTOSelected().getTobyUserProproductionDTOList();
            tobyUserProproductionDTOConverter = new TobyUserProproductionDTOConverter(tobyUserProproductionDTOList);
        }
        return tobyUserProproductionDTOList;
    }

    /**
     * @param tobyUserProproductionDTOList the tobyUserProproductionDTOList to
     * set
     */
    public void setTobyUserProproductionDTOList(List<TobyUserProproductionDTO> tobyUserProproductionDTOList) {
        this.tobyUserProproductionDTOList = tobyUserProproductionDTOList;
    }

//    /**
//     * @return the tobyUserProproductionDTOSelected
//     */
//    public TobyUserProproductionDTO getTobyUserProproductionDTOSelected() {
//        if (tobyUserProproductionDTOSelected == null) {
//            tobyUserProproductionDTOSelected = new TobyUserProproductionDTO();
//        }
//        return tobyUserProproductionDTOSelected;
//    }
//
//    /**
//     * @param tobyUserProproductionDTOSelected the
//     * tobyUserProproductionDTOSelected to set
//     */
//    public void setTobyUserProproductionDTOSelected(TobyUserProproductionDTO tobyUserProproductionDTOSelected) {
//        this.tobyUserProproductionDTOSelected = tobyUserProproductionDTOSelected;
//    }
    /**
     * @return the detailDetailDetailViewDTOList
     */
    public List<ProProductionTransactionDetailDetailViewDTO> getDetailDetailDetailViewDTOList() {
        if (detailDetailDetailViewDTOList == null) {
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
        if (proProductionTransactionDetailDetailViewDTOSelected == null) {
            proProductionTransactionDetailDetailViewDTOSelected = new ProProductionTransactionDetailDetailViewDTO();
        }
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
     * @return the invInvoiceSerial
     */
    public String getInvInvoiceSerial() {
        return invInvoiceSerial;
    }

    /**
     * @param invInvoiceSerial the invInvoiceSerial to set
     */
    public void setInvInvoiceSerial(String invInvoiceSerial) {
        this.invInvoiceSerial = invInvoiceSerial;
    }

    /**
     * @return the proProductionStagesDTOList
     */
    public List<ProProductionStagesDTO> getProProductionStagesDTOList() {
        return proProductionStagesDTOList;
    }

    /**
     * @param proProductionStagesDTOList the proProductionStagesDTOList to set
     */
    public void setProProductionStagesDTOList(List<ProProductionStagesDTO> proProductionStagesDTOList) {
        this.proProductionStagesDTOList = proProductionStagesDTOList;
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
     * @return the proProductionStagesDTOSelected
     */
    public ProProductionStagesDTO getProProductionStagesDTOSelected() {
        return proProductionStagesDTOSelected;
    }

    /**
     * @param proProductionStagesDTOSelected the proProductionStagesDTOSelected
     * to set
     */
    public void setProProductionStagesDTOSelected(ProProductionStagesDTO proProductionStagesDTOSelected) {
        this.proProductionStagesDTOSelected = proProductionStagesDTOSelected;
    }
}
