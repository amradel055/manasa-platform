package com.isag.report;

import com.isag.businessservice.IsagUserInventoryService;
import com.isag.businessservice.ReportsService;
import com.isag.converter.InvInventoryDTOConverter;
import com.isag.dto.InvInventoryDTO;
import com.isag.isag.Basic;
import com.isag.views.ProfitOfItemsSold;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import net.sf.jasperreports.engine.JRException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

@Named(value = "profitOfItemsSoldReport")
@ViewScoped
public class ProfitOfItemsSoldReport extends Basic {

    private List<ProfitOfItemsSold> profitOfItemsSoldList;
    private ProfitOfItemsSold profitOfItemsSold;
    private Integer type;

    private InvInventoryDTOConverter gallaryDTOConverter;
    private List<InvInventoryDTO> gallaryList;

    private BigDecimal sumtotalprofit;
    private BigDecimal sumnetAfterDiscountHead;
    private BigDecimal sumdiscountValue;
    private BigDecimal sumtotalCost;
    private BigDecimal sumtotalAddTotal;

    @EJB
    private ReportsService reportsService;
    @EJB
    private IsagUserInventoryService isagUserInventoryService;

    @PostConstruct
    public void init() {
        getProfitOfItemsSold().getGallarySelected().addAll(getGallaryList());
    }

    public void search() {
        if (getProfitOfItemsSold() != null && getProfitOfItemsSold().getGallarySelected() != null && !getProfitOfItemsSold().getGallarySelected().isEmpty()) {
            profitOfItemsSoldList = new ArrayList<>();
            for (InvInventoryDTO invInventoryDTO : getProfitOfItemsSold().getGallarySelected()) {
                getProfitOfItemsSold().setGallaryFrom(invInventoryDTO);
                getProfitOfItemsSoldList().addAll(reportsService.getProfitOfItemsSold(getProfitOfItemsSold(), getUserData().getUserBranch().getId(), type));
                ProfitOfItemsSold profitOfItemsSold1 = getProfitOfItemsSoldList().get(getProfitOfItemsSoldList().size()-1);
                setSumtotalprofit(profitOfItemsSold1.getSumtotalprofit());
                setSumnetAfterDiscountHead(profitOfItemsSold1.getSumnetAfterDiscountHead());
                setSumdiscountValue(profitOfItemsSold1.getSumdiscountValue());
                setSumtotalCost(profitOfItemsSold1.getSumtotalCost());
                setSumtotalAddTotal(profitOfItemsSold1.getSumtotalAddTotal());
            }
        }
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
        String reportName = "ProfitOfItemsSoldReport.jasper";
        fillReport(prepareReport(), getUserData().getReportPath() + reportName, getProfitOfItemsSoldList(), "pdf");
    }

    public void exportProfitReceipt(ActionEvent actionEvent) throws JRException, IOException {
        String reportName = "InvReceipProfit.jasper";
        fillReport(prepareReport(), getUserData().getReportPath() + reportName, getProfitOfItemsSoldList(), "pdf");
    }

    public HashMap prepareReport() {
        HashMap hashMap = new HashMap();

        hashMap.put("PrintedBy", getUserData().getUser().getName());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());

        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }

        hashMap.put("codeText", " الكود");
        hashMap.put("nameText", " الاسم");
        hashMap.put("costAverageText", " متوسط التكلفة");
        hashMap.put("totallSellText", " اجمالى المبيعات");
        hashMap.put("totallDiscountText", " الخصم الكلى");
        hashMap.put("totallNetText", " اجمالى سعر البيع");

        return hashMap;
    }

    /**
     * @return the gallaryDTOConverter
     */
    public InvInventoryDTOConverter getGallaryDTOConverter() {
        return gallaryDTOConverter;
    }

    /**
     * @param gallaryDTOConverter the gallaryDTOConverter to set
     */
    public void setGallaryDTOConverter(InvInventoryDTOConverter gallaryDTOConverter) {
        this.gallaryDTOConverter = gallaryDTOConverter;
    }

    /**
     * @return the gallaryList
     */
    public List<InvInventoryDTO> getGallaryList() {

        if (gallaryList == null || gallaryList.isEmpty()) {
            gallaryList = isagUserInventoryService.getAllInventroisByUserAndBranchPerDTO(getUserData().getUser().getId(), getUserlogin().getBranchId().getId());
            setGallaryDTOConverter(new InvInventoryDTOConverter(gallaryList));
        }
        return gallaryList;
    }

    /**
     * @param gallaryList the gallaryList to set
     */
    public void setGallaryList(List<InvInventoryDTO> gallaryList) {
        this.gallaryList = gallaryList;
    }

    /**
     * @return the profitOfItemsSoldList
     */
    public List<ProfitOfItemsSold> getProfitOfItemsSoldList() {
        if (profitOfItemsSoldList == null) {
            profitOfItemsSoldList = new ArrayList<>();
        }
        return profitOfItemsSoldList;
    }

    /**
     * @param profitOfItemsSoldList the profitOfItemsSoldList to set
     */
    public void setProfitOfItemsSoldList(List<ProfitOfItemsSold> profitOfItemsSoldList) {
        this.profitOfItemsSoldList = profitOfItemsSoldList;
    }

    /**
     * @return the profitOfItemsSold
     */
    public ProfitOfItemsSold getProfitOfItemsSold() {
        if (profitOfItemsSold == null) {
            profitOfItemsSold = new ProfitOfItemsSold();
        }
        return profitOfItemsSold;
    }

    /**
     * @param profitOfItemsSold the profitOfItemsSold to set
     */
    public void setProfitOfItemsSold(ProfitOfItemsSold profitOfItemsSold) {
        this.profitOfItemsSold = profitOfItemsSold;
    }

    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return the sumtotalprofit
     */
    public BigDecimal getSumtotalprofit() {
        return sumtotalprofit;
    }

    /**
     * @param sumtotalprofit the sumtotalprofit to set
     */
    public void setSumtotalprofit(BigDecimal sumtotalprofit) {
        this.sumtotalprofit = sumtotalprofit;
    }

    /**
     * @return the sumnetAfterDiscountHead
     */
    public BigDecimal getSumnetAfterDiscountHead() {
        return sumnetAfterDiscountHead;
    }

    /**
     * @param sumnetAfterDiscountHead the sumnetAfterDiscountHead to set
     */
    public void setSumnetAfterDiscountHead(BigDecimal sumnetAfterDiscountHead) {
        this.sumnetAfterDiscountHead = sumnetAfterDiscountHead;
    }

    /**
     * @return the sumdiscountValue
     */
    public BigDecimal getSumdiscountValue() {
        return sumdiscountValue;
    }

    /**
     * @param sumdiscountValue the sumdiscountValue to set
     */
    public void setSumdiscountValue(BigDecimal sumdiscountValue) {
        this.sumdiscountValue = sumdiscountValue;
    }

    /**
     * @return the sumtotalCost
     */
    public BigDecimal getSumtotalCost() {
        return sumtotalCost;
    }

    /**
     * @param sumtotalCost the sumtotalCost to set
     */
    public void setSumtotalCost(BigDecimal sumtotalCost) {
        this.sumtotalCost = sumtotalCost;
    }

    /**
     * @return the sumtotalAddTotal
     */
    public BigDecimal getSumtotalAddTotal() {
        return sumtotalAddTotal;
    }

    /**
     * @param sumtotalAddTotal the sumtotalAddTotal to set
     */
    public void setSumtotalAddTotal(BigDecimal sumtotalAddTotal) {
        this.sumtotalAddTotal = sumtotalAddTotal;
    }

    /**
     * @return the salesReportByValueAdedDetailsList
     */
}
