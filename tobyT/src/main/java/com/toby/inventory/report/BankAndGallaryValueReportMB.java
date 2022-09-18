/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.inventory.report;

import com.toby.businessservice.GlBankTransactionService;
import com.toby.businessservice.InvInventoryService;
import com.toby.converter.InvInventoryDTOConverter;
import com.toby.dto.CommisionReport;
import com.toby.dto.InvInventoryDTO;
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
@Named("bankAndGallaryValueReportMB")
@ViewScoped
public class BankAndGallaryValueReportMB extends BaseReportBean {
    
    private Date datefrom;
    private Date dateto;
    private List<InvInventoryDTO> invInventoryDTOList;
    private List<InvInventoryDTO> invInventoryDTOSelected;
    private InvInventoryDTOConverter inventoryDTOConverters;
    private List<CommisionReport> CommisionReport;
    
    @EJB
    InvInventoryService invInventoryService;
    @EJB
    GlBankTransactionService glBankTransactionService;
    
    @Override
    @PostConstruct
    
    public void load() {
        
    }
    
    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void search() {
        for (InvInventoryDTO invInventoryDTO : invInventoryDTOSelected) {
            List<CommisionReport> commisionReports1 = glBankTransactionService.FindGallarValue(datefrom, dateto, invInventoryDTO.getId(),invInventoryDTO.getName());
            CommisionReport.addAll(commisionReports1);
            List<CommisionReport> commisionReports2 = glBankTransactionService.FindBankValues(datefrom, dateto, invInventoryDTO.getId() , invInventoryDTO.getName());
            CommisionReport.addAll(commisionReports2) ;
        }
        
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
    
    @Override
    public HashMap prepareReport() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the CommisionReport
     */
    public List<CommisionReport> getCommisionReport() {
        if (CommisionReport == null) {
            setCommisionReport(new ArrayList<>());
        }
        return CommisionReport;
    }

    /**
     * @param CommisionReport the CommisionReport to set
     */
    public void setCommisionReport(List<CommisionReport> CommisionReport) {
        this.CommisionReport = CommisionReport;
    }
    
}
