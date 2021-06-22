package com.toby.reports;

import com.toby.businessservice.InvItemService;
import com.toby.dto.InvItemDTO;
import com.toby.toby.Basic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

@Named(value = "invItemMainDataReport")
@ViewScoped
public class InvItemMainDataReport extends Basic {

    private List<InvItemDTO> list;
    private InvItemDTO invItemDTO;

  
    @EJB
    private InvItemService invItemService;

    public void search() {
        setList(invItemService.invItemsReport(getInvItemDTO(),getUserData().getUserBranch().getId()));
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
        String reportName = "InvItemMainDataReport.jasper";
        fillReport(prepareReport(), getUserData().getReportPath() + reportName, getList(), "pdf");
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

       
        hashMap.put("codeText", " رقم الصنف");
        hashMap.put("nameText", "اسم الصنف ");
        hashMap.put("dateText", " تاريخ اخر فاتورة");
        hashMap.put("sallaryText", " سعر البيع");
        hashMap.put("costAverageText", " متوسط التكلفة");
        hashMap.put("costAverageText", " متوسط التكلفة");
        hashMap.put("costAverageText", " متوسط التكلفة");
        hashMap.put("costAverageText", " متوسط التكلفة");
        hashMap.put("costAverageText", " متوسط التكلفة");
        hashMap.put("costAverageText", " متوسط التكلفة");
        hashMap.put("costAverageText", " متوسط التكلفة");
        hashMap.put("costAverageText", " متوسط التكلفة");

        return hashMap;
    }

    /**
     * @return the list
     */
    public List<InvItemDTO> getList() {
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(List<InvItemDTO> list) {
        this.list = list;
    }

    /**
     * @return the invItemDTO
     */
    public InvItemDTO getInvItemDTO() {
        if (invItemDTO == null) {
            invItemDTO = new InvItemDTO();
        }
        return invItemDTO;
    }

    /**
     * @param invItemDTO the invItemDTO to set
     */
    public void setInvItemDTO(InvItemDTO invItemDTO) {
        this.invItemDTO = invItemDTO;
    }

  

   
}
