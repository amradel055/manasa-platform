package com.toby.reports;

import com.toby.businessservice.ReportsService;
import com.toby.dto.InvInventoryDTO;
import com.toby.toby.Basic;
import com.toby.views.ProfitOfItemsSold;
import java.io.IOException;
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

    @EJB
    private ReportsService reportsService;

    @PostConstruct
    public void init() {
        getProfitOfItemsSold().getGallarySelected().addAll(getGallaryLists());
    }

    public void search() {
        if (getProfitOfItemsSold() != null && getProfitOfItemsSold().getGallarySelected() != null && !getProfitOfItemsSold().getGallarySelected().isEmpty()) {
            profitOfItemsSoldList = new ArrayList<>();
            for (InvInventoryDTO invInventoryDTO : getProfitOfItemsSold().getGallarySelected()) {
                getProfitOfItemsSold().setGallaryFrom(invInventoryDTO);
                getProfitOfItemsSoldList().addAll(reportsService.getProfitOfItemsSold(getProfitOfItemsSold(), getUserData().getUserBranch().getId(), type));
            }
        }
    }

    public void convertDateFrom() {
        getProfitOfItemsSold().setDateFrom(findDateFromHijriFrom());
    }

    public void convertDateTo() {
        getProfitOfItemsSold().setDateTo(findDateFromHijriTo());
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

    public HashMap prepareReport() {
        HashMap hashMap = new HashMap();

        hashMap.put("PrintedBy", getUserData().getUser().getName());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());

        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }

        hashMap.put("gallarySelectedText", " المعرض");
        hashMap.put("gallarySelectedValue", getProfitOfItemsSold() == null ? null : getProfitOfItemsSold().getGallaryFrom().getName());
        hashMap.put("typeText", " الحالة");
        hashMap.put("typeValue", getType());
        hashMap.put("dateFromText", " من تاريخ");
        hashMap.put("dateFromValue", getProfitOfItemsSold().getDateFrom());
        hashMap.put("dateToText", " الى تاريخ");
        hashMap.put("dateToValue", getProfitOfItemsSold().getDateTo());
        hashMap.put("codeText", " الكود");
        hashMap.put("nameText", " الاسم");
        hashMap.put("costAverageText", " متوسط التكلفة");
        hashMap.put("totallSellText", " اجمالى المبيعات");
        hashMap.put("totallDiscountText", " الخصم الكلى");
        hashMap.put("totallNetText", " اجمالى سعر البيع");

        return hashMap;
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
     * @return the salesReportByValueAdedDetailsList
     */
}
