/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.cashing;

import com.toby.businessservice.InvInventoryTransactionService;
import com.toby.dto.InvInventoryTransactionDTO;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author Toby
 */

@Named(value = "cashingListMB")
@javax.faces.view.ViewScoped
public class cashingListMB extends BaseFormBean{
    
    private List<InvInventoryTransactionDTO> invInventoryTransactionDTOList ;
    private InvInventoryTransactionDTO invInventoryTransactionDTOSelected;
    
    @EJB
    private InvInventoryTransactionService invInventoryTransactionService;
    

    @Override
         
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @PostConstruct
    public void init() {
           ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
    getInvInventoryTransactionDTOList();

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
     * @return the invInventoryTransactionDTOList
     */
    public List<InvInventoryTransactionDTO> getInvInventoryTransactionDTOList() {
       if(invInventoryTransactionDTOList==null||invInventoryTransactionDTOList.isEmpty()){
       invInventoryTransactionDTOList=new ArrayList<>(); 
           invInventoryTransactionDTOList=invInventoryTransactionService.getInventoryTransactionListTocash(getUserData().getUser());
       
       }
        
        
        return invInventoryTransactionDTOList;
    }

    /**
     * @param invInventoryTransactionDTOList the invInventoryTransactionDTOList to set
     */
    public void setInvInventoryTransactionDTOList(List<InvInventoryTransactionDTO> invInventoryTransactionDTOList) {
        this.invInventoryTransactionDTOList = invInventoryTransactionDTOList;
    }

    /**
     * @return the invInventoryTransactionDTOSelected
     */
    public InvInventoryTransactionDTO getInvInventoryTransactionDTOSelected() {
        return invInventoryTransactionDTOSelected;
    }

    /**
     * @param invInventoryTransactionDTOSelected the invInventoryTransactionDTOSelected to set
     */
    public void setInvInventoryTransactionDTOSelected(InvInventoryTransactionDTO invInventoryTransactionDTOSelected) {
        this.invInventoryTransactionDTOSelected = invInventoryTransactionDTOSelected;
    }
    
}
