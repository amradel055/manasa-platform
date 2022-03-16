/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.InvItemService;
import com.isag.dto.MissingReport;
import com.isag.entity.InvItem;
import com.isag.isag.BaseReportBean;
import java.io.IOException;
import java.util.ArrayList;
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
@Named("shortageReport")
@ViewScoped
public class ShortageReport extends BaseReportBean {

    private List<InvItem> invItemsList;
    private List<MissingReport> reportsList;

    @EJB
    private InvItemService invItemService;

    @Override
    @PostConstruct
    public void load() {
        search();

    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void search() {
        setReportsList(invItemService.FindinvItemsList(getInvItemsList(), getUserData().getUserBranch().getId()));
    }

    @Override
    public HashMap prepareReport() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
     * @return the invItemsList
     */
    public List<InvItem> getInvItemsList() {
        if (invItemsList == null) {
            setInvItemsList(invItemService.getInvItemByBranchId(getUserData().getUserBranch().getId()));
        }
        return invItemsList;
    }

    /**
     * @param invItemsList the invItemsList to set
     */
    public void setInvItemsList(List<InvItem> invItemsList) {
        this.invItemsList = invItemsList;
    }

    /**
     * @return the reportsList
     */
    public List<MissingReport> getReportsList() {
        if (reportsList == null) {
            reportsList = new ArrayList<>();
        }
        return reportsList;
    }

    /**
     * @param reportsList the reportsList to set
     */
    public void setReportsList(List<MissingReport> reportsList) {
        this.reportsList = reportsList;
    }

}
