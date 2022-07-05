/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.report.entity;

import com.toby.businessservice.InvDelegatorService;
import com.toby.businessservice.InvInventoryService;
import com.toby.businessservice.InvPurchaseInvoiceService;
import com.toby.businessservice.TobyUserService;
import com.toby.converter.InvDelegatorDTOConvertor;
import com.toby.converter.InvInventoryDTOConverter;
import com.toby.converter.TobyUserDTOConverter;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvPurchaseInvoiceDTO1;
import com.toby.dto.InventoryDelegatorDTO;
import com.toby.dto.TobyUserDTO;
import com.toby.toby.BaseGlAccountReportBean;
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

@Named(value = "usersAndDelegatesReport")
@ViewScoped
public class UsersAndDelegatesReport extends BaseGlAccountReportBean {

    @EJB
    InvPurchaseInvoiceService invPurchaseInvoiceService;
    @EJB
    InvInventoryService invInventoryService;
    @EJB
    InvDelegatorService invDelegatorService;
    @EJB
    TobyUserService tobyUserService;

    private Date dateFrom;
    private Date dateTo;
    private List<InvInventoryDTO> invInventoryDTOList;
    private List<InventoryDelegatorDTO> inventoryDelegatorDTOList;
    private List<TobyUserDTO> tobyUserDTOList;
    private List<InvInventoryDTO> invInventoryList;
    private List<InventoryDelegatorDTO> inventoryDelegatorList;
    private List<TobyUserDTO> tobyUserList;
    private List<InvPurchaseInvoiceDTO1> invPurchaseInvoiceDTOList;
    
    private String inventory ;
    private String delegator ;
    private String users  ;
    
    private InvInventoryDTOConverter invInventoryDTOConverter ;
    private InvDelegatorDTOConvertor invDelegatorDTOConvertor ;
    private TobyUserDTOConverter tobyUserDTOConverter ;

    public String fillInventoryList() {
        StringBuilder stringBuilder = new StringBuilder();
        if (invInventoryList != null && !invInventoryList.isEmpty()) {
            for (InvInventoryDTO invInventoryDTO : invInventoryList) {
                if (stringBuilder != null && stringBuilder.toString().length() == 0) {
                    stringBuilder.append(invInventoryDTO.getId());
                } else {
                    stringBuilder.append(",").append(invInventoryDTO.getId());
                }
            }
        }
        return stringBuilder.toString();
    }

    public String fillInvDelegatorList() {
        StringBuilder stringBuilder = new StringBuilder();
        if (inventoryDelegatorList != null && !inventoryDelegatorList.isEmpty()) {
            for (InventoryDelegatorDTO delegatorDTO : inventoryDelegatorList) {
                if (stringBuilder != null && stringBuilder.toString().length() == 0) {
                    stringBuilder.append(delegatorDTO.getId());
                } else {
                    stringBuilder.append(",").append(delegatorDTO.getId());
                }
            }
        }
        return stringBuilder.toString();
    }

    public String fillTobyUserList() {
        StringBuilder stringBuilder = new StringBuilder();
        if (tobyUserList != null && !tobyUserList.isEmpty()) {
            for (TobyUserDTO tobyUserDTO : tobyUserList) {
                if (stringBuilder != null && stringBuilder.toString().length() == 0) {
                    stringBuilder.append(tobyUserDTO.getId());
                } else {
                    stringBuilder.append(",").append(tobyUserDTO.getId());
                }
            }
        }
        return stringBuilder.toString();
    }

    public UsersAndDelegatesReport() {
    }

    @PostConstruct
    public void init() {
      

    }

    @Override
    public void checkDate(Boolean dateType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetDateFrom() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetDateTo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void search() {
        if(getInvInventoryList() != null && getInventoryDelegatorList() !=null && getTobyUserList() != null  ){
        invPurchaseInvoiceDTOList  = invPurchaseInvoiceService.getAllClients(182, dateFrom , dateTo, getInventory(), getDelegator(), getUsers(), getUserData().getUser());
        
        }
        
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
     * @return the dateFrom
     */
    public Date getDateFrom() {
        return dateFrom;
    }

    /**
     * @param dateFrom the dateFrom to set
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * @return the dateTo
     */
    public Date getDateTo() {
        return dateTo;
    }

    /**
     * @param dateTo the dateTo to set
     */
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    /**
     * @return the invInventoryDTOList
     */
    public List<InvInventoryDTO> getInvInventoryDTOList() {
        if (invInventoryDTOList == null) {
            setInvInventoryDTOList(invInventoryService.findInventoryByBranch(getUserData().getUserBranch().getId(), getUserData().getUser()));
            setInvInventoryDTOConverter(new InvInventoryDTOConverter(invInventoryDTOList));
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
     * @return the inventoryDelegatorDTOList
     */
    public List<InventoryDelegatorDTO> getInventoryDelegatorDTOList() {
        if (inventoryDelegatorDTOList == null) {
            setInventoryDelegatorDTOList(invDelegatorService.getSalesDTOByBranch(getUserData().getUserBranch().getId()));
            setInvDelegatorDTOConvertor(new InvDelegatorDTOConvertor(inventoryDelegatorDTOList));
        }
        return inventoryDelegatorDTOList;
    }

    /**
     * @param inventoryDelegatorDTOList the inventoryDelegatorDTOList to set
     */
    public void setInventoryDelegatorDTOList(List<InventoryDelegatorDTO> inventoryDelegatorDTOList) {
        this.inventoryDelegatorDTOList = inventoryDelegatorDTOList;
    }

    /**
     * @return the tobyUserDTOList
     */
    public List<TobyUserDTO> getTobyUserDTOList() {
        if (tobyUserDTOList == null) {
            setTobyUserDTOList(tobyUserService.getUsersByCompanyId(getUserData().getCompany().getId()));
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
     * @return the invInventoryList
     */
    public List<InvInventoryDTO> getInvInventoryList() {
        if (invInventoryList == null) {
            setInvInventoryList(new ArrayList<>());
        }
        return invInventoryList;
    }

    /**
     * @param invInventoryList the invInventoryList to set
     */
    public void setInvInventoryList(List<InvInventoryDTO> invInventoryList) {
        this.invInventoryList = invInventoryList;
    }

    /**
     * @return the inventoryDelegatorList
     */
    public List<InventoryDelegatorDTO> getInventoryDelegatorList() {
        if (inventoryDelegatorList == null) {
            setInventoryDelegatorList(new ArrayList<>());
        }
        return inventoryDelegatorList;
    }

    /**
     * @param inventoryDelegatorList the inventoryDelegatorList to set
     */
    public void setInventoryDelegatorList(List<InventoryDelegatorDTO> inventoryDelegatorList) {
        this.inventoryDelegatorList = inventoryDelegatorList;
    }

    /**
     * @return the tobyUserList
     */
    public List<TobyUserDTO> getTobyUserList() {
        if (tobyUserList == null) {
            setTobyUserList(new ArrayList<>());
        }
        return tobyUserList;
    }

    /**
     * @param tobyUserList the tobyUserList to set
     */
    public void setTobyUserList(List<TobyUserDTO> tobyUserList) {
        this.tobyUserList = tobyUserList;
    }

    /**
     * @return the inventory
     */
    public String getInventory() {
          if(inventory == null){
            setInventory(fillInventoryList());
        }
        return inventory;
    }

    /**
     * @param inventory the inventory to set
     */
    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    /**
     * @return the delegator
     */
    public String getDelegator() {
          if(delegator == null){
            setDelegator(fillInvDelegatorList());
        }
        return delegator;
    }

    /**
     * @param delegator the delegator to set
     */
    public void setDelegator(String delegator) {
        this.delegator = delegator;
    }

    /**
     * @return the users
     */
    public String getUsers() {
        if(users == null){
            setUsers(fillTobyUserList());
        }
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(String users) {
        this.users = users;
    }

    /**
     * @return the invPurchaseInvoiceDTOList
     */
    public List<InvPurchaseInvoiceDTO1> getInvPurchaseInvoiceDTOList() {
        return invPurchaseInvoiceDTOList;
    }

    /**
     * @param invPurchaseInvoiceDTOList the invPurchaseInvoiceDTOList to set
     */
    public void setInvPurchaseInvoiceDTOList(List<InvPurchaseInvoiceDTO1> invPurchaseInvoiceDTOList) {
        this.invPurchaseInvoiceDTOList = invPurchaseInvoiceDTOList;
    }

    /**
     * @return the invInventoryDTOConverter
     */
    public InvInventoryDTOConverter getInvInventoryDTOConverter() {
        return invInventoryDTOConverter;
    }

    /**
     * @param invInventoryDTOConverter the invInventoryDTOConverter to set
     */
    public void setInvInventoryDTOConverter(InvInventoryDTOConverter invInventoryDTOConverter) {
        this.invInventoryDTOConverter = invInventoryDTOConverter;
    }

    /**
     * @return the invDelegatorDTOConvertor
     */
    public InvDelegatorDTOConvertor getInvDelegatorDTOConvertor() {
        return invDelegatorDTOConvertor;
    }

    /**
     * @param invDelegatorDTOConvertor the invDelegatorDTOConvertor to set
     */
    public void setInvDelegatorDTOConvertor(InvDelegatorDTOConvertor invDelegatorDTOConvertor) {
        this.invDelegatorDTOConvertor = invDelegatorDTOConvertor;
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

}
