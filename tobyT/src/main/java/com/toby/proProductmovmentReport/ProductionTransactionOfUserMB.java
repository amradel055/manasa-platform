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
import com.toby.converter.ProProductionStagesDTOConverter;
import com.toby.converter.TobyUserDTOConverter;
import com.toby.dto.PrintProductionDTO;
import com.toby.dto.ProProductionStagesDTO;
import com.toby.dto.TobyUserDTO;
import com.toby.toby.AutoComplete;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import com.toby.views.ProductionTransactionOfUser;
import java.io.IOException;
import java.math.BigDecimal;
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
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

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
    private ProProductionStagesDTOConverter proProductionStagesDTOConverter;
    private BigDecimal totalQuantity;
    private BigDecimal Total;

    @EJB
    private TobyUserService tobyUserService;
    @EJB
    private ProProductionStagesService proProductionStagesService;
    @EJB
    private ProductionTransactionOfUserService productionTransactionOfUserService;
    @EJB
    private ProProductionItemsTransactionService proProductionItemsTransactionService;

    public List<ProProductionStagesDTO> completeProProductionId(String query) {

        return AutoComplete.completeProProductionId(query, proProductionStagesDTOList, proProductionStagesDTOConverter);

    }

    public List<TobyUserDTO> completusercode(String query) {

        return AutoComplete.completusercode(query, tobyUserDTOList, tobyUserDTOConverter);
    }

    public void search() {
        // if((printProductionDTO.getFromUser()!=null&&printProductionDTO.getToUser()!=null)
        // ||(printProductionDTO.getFromDate()!=null&&printProductionDTO.getToDate()!=null)
        //  ||(printProductionDTO.getFromStatus()!=null)&&(printProductionDTO.getToStatus()!=null)){
        totalQuantity = BigDecimal.ZERO;
        Total = BigDecimal.ZERO;
        productionTransactionOfUserList = proProductionItemsTransactionService.getProProductionTransactionDTOForAllUsersList(printProductionDTO, getUserData().getUserBranch().getId());
        for (ProductionTransactionOfUser productionTransactionOfUser : productionTransactionOfUserList) {
            setTotal(productionTransactionOfUser.getTotalNumber().add(getTotal()));
            setTotalQuantity(productionTransactionOfUser.getNumberExcute().add(getTotalQuantity()));
        }
        // }else{
        //  printProductionDTO.setMsg("ادخل من عامل  الي عامل او من تاريخ الي تاريخ او من مرحله الي مرحله  " );
        //    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", printProductionDTO.getMsg()));

        //  }
    }

    public void convertDateFrom() {
        getPrintProductionDTO().setFromDate(findDateFromHijriFrom());
    }

    public void convertDateTo() {
        getPrintProductionDTO().setToDate(findDateFromHijriTo());
    }

    public void postProcessXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow header = sheet.getRow(0);

        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            HSSFCell cell = header.getCell(i);

            cell.setCellStyle(cellStyle);
        }
    }

    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (productionTransactionOfUserList != null && !productionTransactionOfUserList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "productionTransactionOfUser.jasper", productionTransactionOfUserList, "pdf");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا توجد نتائج للطباعة"));
        }
    }

    public HashMap prepareReport() {
        HashMap hashMap = new HashMap();

        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        hashMap.put("PrintedBy", getUserData().getUser().getName());
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }

        hashMap.put("fromStatusText", "من فئة");
        hashMap.put("fromStatusValue", getPrintProductionDTO().getFromStatus() == null ? null : getPrintProductionDTO().getFromStatus().getName());
        hashMap.put("toStatusText", "الى فئة");
        hashMap.put("toStatusValue", getPrintProductionDTO().getToStatus() == null ? null : getPrintProductionDTO().getToStatus().getName());
        hashMap.put("fromUserText", " من عامل");
        hashMap.put("fromUserValue", getPrintProductionDTO().getFromUser() == null ? null : getPrintProductionDTO().getFromUser().getName());
        hashMap.put("toUserText", " الى عامل");
        hashMap.put("toUserValue", getPrintProductionDTO().getToUser() == null ? null : getPrintProductionDTO().getToUser().getName());
        hashMap.put("fromDateText", "  من تاريخ");
        hashMap.put("fromDateValue", printProductionDTO.getFromDate());
        hashMap.put("toDateText", "  الى تاريخ");
        hashMap.put("toDateValue", printProductionDTO.getToDate());
        hashMap.put("productionStagesIdText", " الرقم ");
        hashMap.put("stageNameText", " الفئة ");
        hashMap.put("userCodeText", " رقم العامل ");
        hashMap.put("userNameText", " الموظف ");
        hashMap.put("priceText", " السعر ");
        hashMap.put("numberExcuteText", " الكمية ");
        hashMap.put("totalNumberText", " الاجمالي ");
        hashMap.put("avarageText", " الكمية ");

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
            proProductionStagesDTOList = proProductionStagesService.getAllProproductionStages(getUserData().getUser(), getUserData().getUserBranch().getId());
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

    /**
     * @return the proProductionStagesDTOConverter
     */
    public ProProductionStagesDTOConverter getProProductionStagesDTOConverter() {
        return proProductionStagesDTOConverter;
    }

    /**
     * @param proProductionStagesDTOConverter the
     * proProductionStagesDTOConverter to set
     */
    public void setProProductionStagesDTOConverter(ProProductionStagesDTOConverter proProductionStagesDTOConverter) {
        this.proProductionStagesDTOConverter = proProductionStagesDTOConverter;
    }

    /**
     * @return the totalQuantity
     */
    public BigDecimal getTotalQuantity() {
        if (totalQuantity == null) {

            return BigDecimal.ZERO;
        }
        return totalQuantity;
    }

    /**
     * @param totalQuantity the totalQuantity to set
     */
    public void setTotalQuantity(BigDecimal totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    /**
     * @return the totalNumber
     */
    /**
     * @return the Total
     */
    public BigDecimal getTotal() {
        if (Total == null) {

            return BigDecimal.ZERO;
        }

        return Total;
    }

    /**
     * @param Total the Total to set
     */
    public void setTotal(BigDecimal Total) {
        this.Total = Total;
    }

}
