/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.proProductmovmentReport;

import com.toby.businessservice.ProProductionItemsTransactionService;
import com.toby.businessservice.ProProductionStagesService;
import com.toby.businessservice.ProductionTransactionOfUserService;
import com.toby.businessservice.TobyUserService;
import com.toby.converter.TobyUserDTOConverter;
import com.toby.dto.PrintProductionDTO;
import com.toby.dto.ProProductionStagesDTO;
import com.toby.dto.TobyUserDTO;
import com.toby.toby.AutoComplete;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import com.toby.views.ProductionTransactionOfUser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author H
 */
@Named(value = "productionTransactionOfUserMB")
@ViewScoped
public class ProductionTransactionOfUserMB extends BaseFormBean {

    private PrintProductionDTO printProductionDTO;
    private List<TobyUserDTO> tobyUserDTOList;
    private List<ProProductionStagesDTO> proProductionStagesDTOList;
    private List<ProductionTransactionOfUser> productionTransactionOfUserList;
    private TobyUserDTOConverter tobyUserDTOConverter;
    @EJB
    private TobyUserService tobyUserService;
    @EJB
    private ProProductionStagesService proProductionStagesService;
    @EJB
    private ProductionTransactionOfUserService productionTransactionOfUserService;
    @EJB
    private ProProductionItemsTransactionService proProductionItemsTransactionService;

    
     public  List<ProProductionStagesDTO> completeProProductionId(String query){
     
         return AutoComplete.completeProProductionId(query, proProductionStagesDTOList, null);
     
     }
      public List<TobyUserDTO> completusercode(String query){
      
       return AutoComplete.completusercode(query, tobyUserDTOList, tobyUserDTOConverter);
      }
    
    public void search() {
        if((printProductionDTO.getFromUser()!=null&&printProductionDTO.getToUser()!=null)
                ||(printProductionDTO.getFromDate()!=null&&printProductionDTO.getToDate()!=null)
                 ||(printProductionDTO.getFromStatus()!=null)&&(printProductionDTO.getToStatus()!=null)){
        productionTransactionOfUserList = proProductionItemsTransactionService.getProProductionTransactionDTOForAllUsersList(printProductionDTO, getUserData().getUserBranch().getId());
        }else{
         printProductionDTO.setMsg("ادخل من عامل  الي عامل او من تاريخ الي تاريخ او من مرحله الي مرحله  " );
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", printProductionDTO.getMsg()));
        
        }
    }
    
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (productionTransactionOfUserList != null && !productionTransactionOfUserList.isEmpty()) {
                fillReport(prepareReport(), getUserData().getReportPath() + "omar.jasper", productionTransactionOfUserList, "pdf");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا توجد نتائج للطباعة"));
        }
    }
    

    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();
        
        hashMap.put("fromStage", printProductionDTO.getFromStatus());
        hashMap.put("toStage", printProductionDTO.getFromStatus());
        hashMap.put("toDate", printProductionDTO.getToDate());
        hashMap.put("fromDate", printProductionDTO.getFromDate());
        hashMap.put("fromUser", printProductionDTO.getFromUser().getUserCode());
        hashMap.put("toUser", printProductionDTO.getToUser().getUserCode());
        hashMap.put("fromStageText", "من مرحلة");
        hashMap.put("toStageText", "إلى مرحلة");
        hashMap.put("toDateText", "من تاريخ");
        hashMap.put("fromDateText", "إلى تاريخ");
        hashMap.put("fromUserText", " من عميل");
        hashMap.put("toUserText", "إلى عميل");
        hashMap.put("productionStagesIdText", "المرحلة");
        hashMap.put("dateText", "التاريخ");
        hashMap.put("itemIdText", "الصنف");
        hashMap.put("itemNameText", "اسم الصنف");
        hashMap.put("userNameText", " الموظف");
        hashMap.put("numberExcuteText", "الكمية");
        hashMap.put("totalNumberText", "الاجمالى");
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        hashMap.put("PrintedBy", getUserData().getUser().getName());
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        return hashMap;
    }
    
    

    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @PostConstruct
    @Override
    public void init() {

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        getProProductionStagesDTOList();
        getTobyUserDTOList();
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
     * @return the printProductionDTO
     */
    public PrintProductionDTO getPrintProductionDTO() {
        if (printProductionDTO == null) {
            printProductionDTO = new PrintProductionDTO();

        }
        return printProductionDTO;
    }

    /**
     * @param printProductionDTO the printProductionDTO to set
     */
    public void setPrintProductionDTO(PrintProductionDTO printProductionDTO) {
        this.printProductionDTO = printProductionDTO;
    }

    /**
     * @return the tobyUserDTOList
     */
    public List<TobyUserDTO> getTobyUserDTOList() {
        if (tobyUserDTOList == null || tobyUserDTOList.isEmpty()) {
            tobyUserDTOList = tobyUserService.getAllUserOfproduction(getUserData().getUser());
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
     * @return the proProductionStagesDTOList
     */
    public List<ProProductionStagesDTO> getProProductionStagesDTOList() {

        if (proProductionStagesDTOList == null || proProductionStagesDTOList.isEmpty()) {
            proProductionStagesDTOList = new ArrayList<>();
            proProductionStagesDTOList = proProductionStagesService.getAllProproductionStages(getUserData().getUser());
        }
        return proProductionStagesDTOList;
    }

    public void setProProductionStagesDTOList(List<ProProductionStagesDTO> proProductionStagesDTOList) {
        this.proProductionStagesDTOList = proProductionStagesDTOList;
    }

    /**
     * @return the productionTransactionOfUserList
     */
    public List<ProductionTransactionOfUser> getProductionTransactionOfUserList() {

        return productionTransactionOfUserList;
    }

    /**
     * @param productionTransactionOfUserList the
     * productionTransactionOfUserList to set
     */
    public void setProductionTransactionOfUserList(List<ProductionTransactionOfUser> productionTransactionOfUserList) {
        this.productionTransactionOfUserList = productionTransactionOfUserList;
    }

}
