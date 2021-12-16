/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.gmsubscriptions;

import com.isag.businessservice.ReportService;
import com.isag.dto.resualtDTO;
import com.isag.dto.searchDTO;
import com.isag.entity.search;
import com.isag.isag.BaseFormBean;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Lenovo
 */
@Named(value = "reportMB")
@ViewScoped
public class ReportMB extends BaseFormBean{
    @EJB
    ReportService reportservice; 
   private search search;
   private resualtDTO resualtDTO ;
   private searchDTO searchdto;
   private List<resualtDTO> resultList ;
   
   
   public void search (){
    resultList = reportservice.getReporySearch(searchdto);
   }
   
   

    
    
   

    /**
     * @return the search
     */
    public search getSearch() {
        return search;
    }

    /**
     * @param search the search to set
     */
    public void setSearch(search search) {
        this.search = search;
    }

    /**
     * @return the resualtDTO
     */
    public resualtDTO getResualtDTO() {
        return resualtDTO;
    }

    /**
     * @param resualtDTO the resualtDTO to set
     */
    public void setResualtDTO(resualtDTO resualtDTO) {
        this.resualtDTO = resualtDTO;
    }

    /**
     * @return the searchdto
     */
    public searchDTO getSearchdto() {
        if(searchdto==null){
            setSearchdto(new searchDTO());
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
     * @return the resultList
     */
    public List<resualtDTO> getResultList() {
        return resultList;
    }

    /**
     * @param resultList the resultList to set
     */
    public void setResultList(List<resualtDTO> resultList) {
        this.resultList = resultList;
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
