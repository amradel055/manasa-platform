/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.gmsubscriptions;
import com.isag.businessservice.GmSubscriptionsServiceImpl;
import com.isag.businessservice.ReportService;
import com.isag.dto.GmSubscriptionsDTO;
import com.isag.dto.resualtDTO;
import com.isag.dto.searchDTO;
import com.isag.entity.GmSubscriptions;
import com.isag.entity.search;
import com.isag.isag.BaseFormBean;
import com.isag.print.IReportService;
import com.isag.print.IReportServiceImpl;
import java.io.IOException;
import java.math.MathContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author Lenovo
 */
@Named(value = "reportMB")
@ViewScoped
public class ReportMB extends BaseFormBean {

    @EJB
    ReportService reportservice;
 
    private search search;
    private resualtDTO resualtDTO;
    private searchDTO searchdto;
    private List<resualtDTO> resultList;
    private resualtDTO resultDtoSelected ;
    IReportService iReportService = new IReportServiceImpl();
    GmSubscriptions gmSubscriptions;

//       public void printList() {
//    
//        if (getUri().contains("gmreports") || getUri().contains("gmsubscriptionsreport")) {
//            getResualtDTO().setId((Integer) getContext().getSessionMap().get("resultDtoSelected"));
//            setResualtDTO(reportservice.findElresultById(getResualtDTO(), getUserData().getUser()));
//        print();
//    }
//       }
  public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
      if(getUri().contains("gmdeletedsubscriptionsreport")){
         String reportName = "DeletedSubscribtionReport.jasper";
        fillReport(prepareReport(), getUserData().getReportPath() + reportName, getResultList(), "pdf");
      }else{
        String reportName = "salesReceiptCashGMReport.jasper";
        fillReport(prepareReport(), getUserData().getReportPath() + reportName, getResultList(), "pdf");
        }
    }

    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();
        hashMap.put("datefrom", searchdto.getDateFrom());
        hashMap.put("dateto", searchdto.getDateTo());
        if(!getUri().contains("gmdeletedsubscriptionsreport")){
          hashMap.put("sumValue", resualtDTO.getSumValue());
        }

   

        return hashMap;
    }
    
    
    public void delete() {
        try {
            if (getResultDtoSelected()!= null && getResultDtoSelected().getId() != null) {
                reportservice.deleteGmSubscribtionDeleted(getResultDtoSelected().getId());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "تم الجذذف بنجاح", "تم الجذذف بنجاح"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "خطأ", "خطأ فى الحف"));
            saveError(e, "InvPurchaseInvoiceListMB", "delete");
        }
    }
    public void search() {
        searchdto.setBranchId(getUserData().getUserBranch().getId());
        resultList = reportservice.getAllValidSubsByDate(searchdto);
        resualtDTO.setSumValue(reportservice.getAllValidSubsFinalNet(searchdto));
    }
    
     public void searchDeletedSubscribtion() {
        searchdto.setBranchId(getUserData().getUserBranch().getId());
        resultList = reportservice.getAllDeletedSubsByDate(searchdto);
    }

    public void searchFreeze() {
        searchdto.setBranchId(getUserData().getUserBranch().getId());
        resultList = reportservice.getAllFreezeByDate(searchdto);
        for (resualtDTO dTO : resultList) {
            if (dTO.getSumValue() != null) {
                resualtDTO.setSumValue(resualtDTO.getSumValue().add(dTO.getValue(), MathContext.UNLIMITED));
            }

        }
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
        if (resualtDTO == null) {
            resualtDTO = new resualtDTO();
        }
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

    /**
     * @return the resultDtoSelected
     */
    public resualtDTO getResultDtoSelected() {
         if (resultDtoSelected == null) {
             setResultDtoSelected(reportservice.findElresultById(getResualtDTO()));
        }
        return resultDtoSelected;
    }

    /**
     * @param resultDtoSelected the resultDtoSelected to set
     */
    public void setResultDtoSelected(resualtDTO resultDtoSelected) {
        this.resultDtoSelected = resultDtoSelected;
    }


 

}
