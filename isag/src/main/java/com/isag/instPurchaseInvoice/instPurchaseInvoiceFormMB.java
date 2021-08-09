/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.instPurchaseInvoice;

import com.isag.businessservice.InstPurchaseInvoiceDetailService;
import com.isag.businessservice.instPurchaseInvoiceService;
import com.isag.dto.InstPurchaseInvoiceDTO;
import com.isag.dto.InstPurchaseInvoiceDetailDTO;
import com.isag.isag.UserData;
import com.isag.isag.instPurchaseInvoiceDataForm;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent; 
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException; 
import org.primefaces.event.SelectEvent;
 

/**
 *
 * @author AhmedEssam
 */
@Named(value = "instPurchaseInvoiceFormMB")
@ViewScoped
public class instPurchaseInvoiceFormMB extends instPurchaseInvoiceDataForm {

    @EJB
    instPurchaseInvoiceService invoiceService;

    @EJB
    InstPurchaseInvoiceDetailService instPurchaseInvoiceDetailService;

    private InstPurchaseInvoiceDTO instPurchaseInvoiceDTOSelected;
    private InstPurchaseInvoiceDTO instPurchaseInvoiceDTO;
    private InstPurchaseInvoiceDetailDTO instPurchaseInvoiceDetailDTOSelected;
    private InstPurchaseInvoiceDetailDTO instPurchaseInvoiceDetailDTOUnSelected;
    private InstPurchaseInvoiceDetailDTO instPurchaseInvoiceDetailDTO;
    private List<InstPurchaseInvoiceDetailDTO> instPurchaseInvoiceDetailDTOList;
    private List<InstPurchaseInvoiceDetailDTO> instPurchaseInvoiceDetailDTOeleteListDelete;
    private BigDecimal totalValue;
    private BigDecimal quantity;
    private Integer index = 0;
    private String totalDetailValue;
    private String quantityDetailValue;
    private SelectEvent ev;

    @PostConstruct
    @Override
    public void init() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            load();
            reset();
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("Edit")) {
                Integer PurchaseInvoiceId = (Integer) context.getSessionMap().get("PurchaseInvoiceDTOSelected");
                findHeadAndDetailsOfPurchaseInvoice(PurchaseInvoiceId);
                getItemInstProjectGuideViewList();
            }

        } catch (Exception e) {
            saveError(e, "instPurchaseInvoiceFormMB", "init");
        }

    }

    public void findHeadAndDetailsOfPurchaseInvoice(Integer PurchaseInvoiceId) {
        instPurchaseInvoiceDTO = invoiceService.findbyInstPurchaseInvoiceId(PurchaseInvoiceId);
        getInstPurchaseInvoiceDetailDTOList();
        setInstPurchaseInvoiceDetailDTOList(instPurchaseInvoiceDetailService.findbyInstPurchaseInvoiceDetailList(PurchaseInvoiceId, getUserlogin().getBranchId().getId()));
        updateValueTotal();
    }

    public void reset() {
        try {
            instPurchaseInvoiceDTO = new InstPurchaseInvoiceDTO();
            instPurchaseInvoiceDetailDTOList = new ArrayList<>();
            instPurchaseInvoiceDetailDTOeleteListDelete = new ArrayList<>();
            setTotalValue(BigDecimal.ZERO);
            setQuantity(BigDecimal.ZERO);
            setTotalDetailValue("0");
            setQuantityDetailValue("0");
        } catch (Exception e) {
            saveError(e, "instPurchaseInvoiceFormMB", "reset");
        }

    }

    public void addNewDetail() {
        try {

            if (instPurchaseInvoiceDetailDTOList != null && !instPurchaseInvoiceDetailDTOList.isEmpty()) {
                for (InstPurchaseInvoiceDetailDTO dto : instPurchaseInvoiceDetailDTOList) {
                    dto.setMarkEdit(Boolean.FALSE);
                }
            }
            InstPurchaseInvoiceDetailDTO detailsDTO = new InstPurchaseInvoiceDetailDTO();
            detailsDTO.setMarkEdit(Boolean.TRUE);
            setIndex((Integer) getIndex() + 1);
            detailsDTO.setIndex(getIndex());
            instPurchaseInvoiceDetailDTOSelected = detailsDTO;
            instPurchaseInvoiceDetailDTOList.add(detailsDTO);
        } catch (Exception e) {
            saveError(e, "instPurchaseInvoiceFormMB", "addNewDetail");
        }

    }

    public void deleteDetail() {
        try {

            if (instPurchaseInvoiceDetailDTOSelected.getId() != null) {
                instPurchaseInvoiceDetailDTOeleteListDelete.add(instPurchaseInvoiceDetailDTOSelected);
            }
            instPurchaseInvoiceDetailDTOList.remove(instPurchaseInvoiceDetailDTOSelected);
            updateValueTotal();
        } catch (Exception e) {
            saveError(e, "instPurchaseInvoiceFormMB", "deleteDetail");
        }
    }

    public void updateValueTotal() {
        try {

            if (instPurchaseInvoiceDetailDTOList != null && !instPurchaseInvoiceDetailDTOList.isEmpty()) {
                totalValue = BigDecimal.ZERO;
                quantity = BigDecimal.ZERO;
                for (InstPurchaseInvoiceDetailDTO dto : instPurchaseInvoiceDetailDTOList) {

                    BigDecimal total = dto.getUnitPrice() != null ? dto.getUnitPrice().multiply(dto.getQuantity() != null ? dto.getQuantity() : BigDecimal.ZERO) : BigDecimal.ZERO;
                    dto.setTotal(total);
                    totalValue = totalValue.add(total != null ? total : BigDecimal.ZERO);
                    quantity = quantity.add(dto.getQuantity() != null ? dto.getQuantity() : BigDecimal.ZERO);
                }
                DecimalFormat df = new DecimalFormat("#,##0.00");
                getTotalDetailValue();
                getQuantityDetailValue();
                setTotalDetailValue(df.format(totalValue));
                setQuantityDetailValue(df.format(quantity));
            }

        } catch (Exception e) {
            saveError(e, "instPurchaseInvoiceFormMB", "updateValueTotal");
        }
    }

    public void onRowSelect(SelectEvent e) throws CloneNotSupportedException {

        try {
            if (checkIfTheSameRowChoosen(e)) {
                for (InstPurchaseInvoiceDetailDTO dto : instPurchaseInvoiceDetailDTOList) {
                    dto.setMarkEdit(Boolean.FALSE);
                }
                if (instPurchaseInvoiceDetailDTOSelected != null) {
                    instPurchaseInvoiceDetailDTOSelected.setMarkEdit(Boolean.TRUE);
                    ev = e;
                    //  updateValueTotal();
                }

            }

        } catch (Exception ex) {
            saveError(ex, "instPurchaseInvoiceFormMB", "onRowSelect");
        }
    }
 
    public boolean checkIfTheSameRowChoosen(SelectEvent e) {
        if (getEv() != null && e.getObject().equals(getEv().getObject())) {
            return false;
        } else {
            return true;
        }
    }

    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("PrintedBy", getUserData().getUser().getName());
            hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
            hashMap.put("companyName", getUserData().getCompany().getName());
            // fillReport(hashMap, getUserData().getReportPath() + "emptyinitialsalecontract.jasper", instContractDetailsEntityList, "pdf");
        } catch (Exception e) {
            saveError(e, "instPurchaseInvoiceFormMB", "exportPDF");
        }
    }

    @Override
    public void load() {
        try {

        } catch (Exception e) {
            saveError(e, "instPurchaseInvoiceFormMB", "load");
        }
    }

    @Override
    public String save() {
        try {
            instPurchaseInvoiceDTO = invoiceService.addInstPurchaseInvoice(instPurchaseInvoiceDTO, getInstPurchaseInvoiceDetailDTOList(), getInstPurchaseInvoiceDetailDTOeleteListDelete(), getUserData().getUser());
            if (instPurchaseInvoiceDTO.getMsg() != null && !instPurchaseInvoiceDTO.getMsg().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), instPurchaseInvoiceDTO.getMsg()));
            } else {

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), "تم الحفظ بنجاح"));
            }
        } catch (Exception e) {
          
            saveError(e, "instPurchaseInvoiceFormMB", "save");
        }
        return "";
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the instPurchaseInvoiceDTOSelected
     */
    public InstPurchaseInvoiceDTO getInstPurchaseInvoiceDTOSelected() {
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
        return instPurchaseInvoiceDTO;
    }

    /**
     * @param instPurchaseInvoiceDTO the instPurchaseInvoiceDTO to set
     */
    public void setInstPurchaseInvoiceDTO(InstPurchaseInvoiceDTO instPurchaseInvoiceDTO) {
        this.instPurchaseInvoiceDTO = instPurchaseInvoiceDTO;
    }

    /**
     * @return the totalValue
     */
    public BigDecimal getTotalValue() {
        return totalValue;
    }

    /**
     * @param totalValue the totalValue to set
     */
    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    /**
     * @return the quantity
     */
    public BigDecimal getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the instPurchaseInvoiceDetailDTOList
     */
    public List<InstPurchaseInvoiceDetailDTO> getInstPurchaseInvoiceDetailDTOList() {
        if (instPurchaseInvoiceDetailDTOList == null || instPurchaseInvoiceDetailDTOList.isEmpty()) {
            instPurchaseInvoiceDetailDTOList = new ArrayList<>();
        }
        return instPurchaseInvoiceDetailDTOList;
    }

    /**
     * @param instPurchaseInvoiceDetailDTOList the
     * instPurchaseInvoiceDetailDTOList to set
     */
    public void setInstPurchaseInvoiceDetailDTOList(List<InstPurchaseInvoiceDetailDTO> instPurchaseInvoiceDetailDTOList) {
        this.instPurchaseInvoiceDetailDTOList = instPurchaseInvoiceDetailDTOList;
    }

    /**
     * @return the instPurchaseInvoiceDetailDTOSelected
     */
    public InstPurchaseInvoiceDetailDTO getInstPurchaseInvoiceDetailDTOSelected() {
        return instPurchaseInvoiceDetailDTOSelected;
    }

    /**
     * @param instPurchaseInvoiceDetailDTOSelected the
     * instPurchaseInvoiceDetailDTOSelected to set
     */
    public void setInstPurchaseInvoiceDetailDTOSelected(InstPurchaseInvoiceDetailDTO instPurchaseInvoiceDetailDTOSelected) {
        this.instPurchaseInvoiceDetailDTOSelected = instPurchaseInvoiceDetailDTOSelected;
    }

    /**
     * @return the instPurchaseInvoiceDetailDTO
     */
    public InstPurchaseInvoiceDetailDTO getInstPurchaseInvoiceDetailDTO() {
        return instPurchaseInvoiceDetailDTO;
    }

    /**
     * @param instPurchaseInvoiceDetailDTO the instPurchaseInvoiceDetailDTO to
     * set
     */
    public void setInstPurchaseInvoiceDetailDTO(InstPurchaseInvoiceDetailDTO instPurchaseInvoiceDetailDTO) {
        this.instPurchaseInvoiceDetailDTO = instPurchaseInvoiceDetailDTO;
    }

    /**
     * @return the totalDetailValue
     */
    public String getTotalDetailValue() {
        if (totalDetailValue == null) {
            totalDetailValue = new String();
        }
        return totalDetailValue;
    }

    /**
     * @param totalDetailValue the totalDetailValue to set
     */
    public void setTotalDetailValue(String totalDetailValue) {
        this.totalDetailValue = totalDetailValue;
    }

    /**
     * @return the quantityDetailValue
     */
    public String getQuantityDetailValue() {
        if (quantityDetailValue == null) {
            quantityDetailValue = new String();
        }
        return quantityDetailValue;
    }

    /**
     * @param quantityDetailValue the quantityDetailValue to set
     */
    public void setQuantityDetailValue(String quantityDetailValue) {
        this.quantityDetailValue = quantityDetailValue;
    }

    /**
     * @return the index
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * @return the instPurchaseInvoiceDetailDTOeleteListDelete
     */
    public List<InstPurchaseInvoiceDetailDTO> getInstPurchaseInvoiceDetailDTOeleteListDelete() {
        if (instPurchaseInvoiceDetailDTOeleteListDelete == null || instPurchaseInvoiceDetailDTOeleteListDelete.isEmpty()) {
            instPurchaseInvoiceDetailDTOeleteListDelete = new ArrayList<>();
        }
        return instPurchaseInvoiceDetailDTOeleteListDelete;
    }

    /**
     * @param instPurchaseInvoiceDetailDTOeleteListDelete the
     * instPurchaseInvoiceDetailDTOeleteListDelete to set
     */
    public void setInstPurchaseInvoiceDetailDTOeleteListDelete(List<InstPurchaseInvoiceDetailDTO> instPurchaseInvoiceDetailDTOeleteListDelete) {
        this.instPurchaseInvoiceDetailDTOeleteListDelete = instPurchaseInvoiceDetailDTOeleteListDelete;
    }

    /**
     * @return the instPurchaseInvoiceDetailDTOUnSelected
     */
    public InstPurchaseInvoiceDetailDTO getInstPurchaseInvoiceDetailDTOUnSelected() {
        return instPurchaseInvoiceDetailDTOUnSelected;
    }

    /**
     * @param instPurchaseInvoiceDetailDTOUnSelected the
     * instPurchaseInvoiceDetailDTOUnSelected to set
     */
    public void setInstPurchaseInvoiceDetailDTOUnSelected(InstPurchaseInvoiceDetailDTO instPurchaseInvoiceDetailDTOUnSelected) {
        this.instPurchaseInvoiceDetailDTOUnSelected = instPurchaseInvoiceDetailDTOUnSelected;
    }

    /**
     * @return the ev
     */
    public SelectEvent getEv() {
        return ev;
    }

    /**
     * @param ev the ev to set
     */
    public void setEv(SelectEvent ev) {
        this.ev = ev;
    }

}
