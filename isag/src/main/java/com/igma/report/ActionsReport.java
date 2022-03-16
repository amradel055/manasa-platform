/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.report;

import com.isag.businessservice.CreateActionService;
import com.isag.dto.ActionsDTO;
import com.isag.dto.searchDTO;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author Mostafa
 */
@Named("actionsReport")
@ViewScoped
public class ActionsReport extends BaseFormBean {

    @EJB
    private CreateActionService actionService;
    private List<ActionsDTO> actionsDTOs;
    private searchDTO searchdto;
    private ActionsDTO actionsDTO;

    public void search() {
        actionsDTOs = actionService.getAllValidSubsByDate(searchdto, getUserData().getUser(),actionsDTO);
    }

    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {

        String reportName = "ActionsReport.jasper";
        fillReport(prepareReport(), getUserData().getReportPath() + reportName, getActionsDTOs(), "pdf");

    }

    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();
        hashMap.put("datefrom", searchdto.getDateFrom());
        hashMap.put("dateto", searchdto.getDateTo());

        return hashMap;
    }

    /**
     * @return the actionsDTOs
     */
    public List<ActionsDTO> getActionsDTOs() {
        return actionsDTOs;
    }

    /**
     * @param actionsDTOs the actionsDTOs to set
     */
    public void setActionsDTOs(List<ActionsDTO> actionsDTOs) {
        this.actionsDTOs = actionsDTOs;
    }

    /**
     * @return the searchdto
     */
    public searchDTO getSearchdto() {
        if (searchdto == null) {
            searchdto = new searchDTO();
        }
        return searchdto;
    }

    /**
     * @param searchdto the searchdto to set
     */
    public void setSearchdto(searchDTO searchdto) {
        this.searchdto = searchdto;
    }

    /**
     * @return the actionsDTO
     */
    public ActionsDTO getActionsDTO() {
        if (actionsDTO == null) {
            actionsDTO = new ActionsDTO();
        }
        return actionsDTO;
    }

    /**
     * @param actionsDTO the actionsDTO to set
     */
    public void setActionsDTO(ActionsDTO actionsDTO) {
        this.actionsDTO = actionsDTO;
    }

    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void init() {
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

}
