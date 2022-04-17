/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.inventory.report;

import com.toby.businessservice.GlBankTransactionDetailsService;
import com.toby.businessservice.InvInventoryService;
import com.toby.businessservice.InvPurchaseInvoiceDetailsService;
import com.toby.converter.InvInventoryDTOConverter;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.balanceGalary;
import com.toby.entity.GlBankTransactionDetail;
import com.toby.toby.BaseReportBean;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author hhhh
 */
@Named("unpaidAmountReport")
@ViewScoped
public class UnpaidAmountReport extends BaseReportBean {

    private Date datefrom;
    private Date dateto;
    private List<InvInventoryDTO> invInventoryDTOList;
    private List<InvInventoryDTO> invInventoryDTOSelected;
    private InvInventoryDTOConverter inventoryDTOConverters;
    private String stringBuilder;
    private List<balanceGalary> balanceGalaryList;

    @EJB
    InvInventoryService invInventoryService;
    @EJB
    GlBankTransactionDetailsService glBankTransactionDetailsService;
    @EJB
    InvPurchaseInvoiceDetailsService purchaseInvoiceDetailsService;

    @Override
    @PostConstruct

    public void load() {

    }

    public String fillStringBuilder() {
        StringBuilder stringBuilder = new StringBuilder();
        if (invInventoryDTOSelected != null && !invInventoryDTOSelected.isEmpty()) {
            for (InvInventoryDTO invInventoryDTO : invInventoryDTOSelected) {
                if (stringBuilder != null && stringBuilder.length() == 0) {
                    stringBuilder.append(invInventoryDTO.getId());
                } else {
                    stringBuilder.append(",").append(invInventoryDTO.getId());
                }
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void search() {
        setBalanceGalaryList(purchaseInvoiceDetailsService.UnPaidAmountAfterSearchNative(getUserData().getUserBranch().getId(), getDatefrom(), getDateto(), getStringBuilder(), getUserData().getUser()));
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        String reportName = "invInventortycollect.jasper";
        fillReport(prepareReport(), getUserData().getReportPath() + reportName, getBalanceGalaryList(), "pdf");
    }

    @Override
    public HashMap prepareReport() {
        HashMap hashMap = new HashMap();

        hashMap.put("dateFromText", "من تاريخ");
        hashMap.put("dateToText", "الى تاريخ");
        hashMap.put("nameText", "اسم المعرض");
        hashMap.put("valueText", "اجمالي التحصيلات الغير مسددة ");
        hashMap.put("nameReport", "تقرير عن اجمالي التحصيلات الغير المسددة للفروع");

        hashMap.put("dateFromValue", getDatefrom());
        hashMap.put("dateToValue", getDateto());

        return hashMap;
    }

    @Override
    public void exportExcel(ActionEvent actionEvent) throws JRException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportHtml(ActionEvent actionEvent) throws JRException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the datefrom
     */
    public Date getDatefrom() {
        return datefrom;
    }

    /**
     * @param datefrom the datefrom to set
     */
    public void setDatefrom(Date datefrom) {
        this.datefrom = datefrom;
    }

    /**
     * @return the dateto
     */
    public Date getDateto() {
        return dateto;
    }

    /**
     * @param dateto the dateto to set
     */
    public void setDateto(Date dateto) {
        this.dateto = dateto;
    }

    /**
     * @return the invInventoryDTOList
     */
    public List<InvInventoryDTO> getInvInventoryDTOList() {
        if (invInventoryDTOList == null) {
            setInvInventoryDTOList(invInventoryService.findInventoryByBranch(getUserData().getUserBranch().getId(), getUserData().getUser()));
            setInventoryDTOConverters(new InvInventoryDTOConverter(invInventoryDTOList));
        }
        return invInventoryDTOList;
    }

    /**
     * @param invInventoryDTOList the invInventoryDTOList to set
     */
    public void setInvInventoryDTOList(List<InvInventoryDTO> invInventoryDTOList) {
        this.invInventoryDTOList = invInventoryDTOList;
    }

    /**
     * @return the inventoryDTOConverters
     */
    public InvInventoryDTOConverter getInventoryDTOConverters() {
        return inventoryDTOConverters;
    }

    /**
     * @param inventoryDTOConverters the inventoryDTOConverters to set
     */
    public void setInventoryDTOConverters(InvInventoryDTOConverter inventoryDTOConverters) {
        this.inventoryDTOConverters = inventoryDTOConverters;
    }

    /**
     * @return the invInventoryDTOSelected
     */
    public List<InvInventoryDTO> getInvInventoryDTOSelected() {
        if (invInventoryDTOSelected == null) {
            setInvInventoryDTOSelected(new ArrayList<>());
        }
        return invInventoryDTOSelected;
    }

    /**
     * @param invInventoryDTOSelected the invInventoryDTOSelected to set
     */
    public void setInvInventoryDTOSelected(List<InvInventoryDTO> invInventoryDTOSelected) {
        this.invInventoryDTOSelected = invInventoryDTOSelected;
    }

    /**
     * @return the stringBuilder
     */
    public String getStringBuilder() {
        if (stringBuilder == null) {
            setStringBuilder(fillStringBuilder());
        }
        return stringBuilder;
    }

    /**
     * @param stringBuilder the stringBuilder to set
     */
    public void setStringBuilder(String stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    /**
     * @return the balanceGalaryList
     */
    public List<balanceGalary> getBalanceGalaryList() {
        if (balanceGalaryList == null) {
            setBalanceGalaryList(new ArrayList<>());
        }
        return balanceGalaryList;
    }

    /**
     * @param balanceGalaryList the balanceGalaryList to set
     */
    public void setBalanceGalaryList(List<balanceGalary> balanceGalaryList) {
        this.balanceGalaryList = balanceGalaryList;
    }

}
