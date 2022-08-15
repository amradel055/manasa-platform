package com.toby.reports;

import com.toby.businessservice.OrganizationSiteService;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.toby.Basic;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

@Named(value = "clientsSalesReport")
@ViewScoped
public class ClientsSalesReport extends Basic {

    @EJB
    OrganizationSiteService organizationSiteService;

    private List<InvOrganizationSiteDTO> invOrganizationSiteDTOList;

    private List<InvInventoryDTO> invInventoryDTOListSelected;
    private Date dateFrom;
    private Date dateTo;
    private String value;

    @PostConstruct
    public void init() {
    }

    public void search() {
        setInvOrganizationSiteDTOList(organizationSiteService.returnClientSales(getInvInventoryDTOListSelected(), getDateFrom(), getDateTo(), getValue()));

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

    /**
     * @return the invInventoryDTOListSelected
     */
    public List<InvInventoryDTO> getInvInventoryDTOListSelected() {
        if (invInventoryDTOListSelected == null) {

            setInvInventoryDTOListSelected(new ArrayList<>());
        }
        return invInventoryDTOListSelected;
    }

    /**
     * @param invInventoryDTOListSelected the invInventoryDTOListSelected to set
     */
    public void setInvInventoryDTOListSelected(List<InvInventoryDTO> invInventoryDTOListSelected) {
        this.invInventoryDTOListSelected = invInventoryDTOListSelected;
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
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the invOrganizationSiteDTOList
     */
    public List<InvOrganizationSiteDTO> getInvOrganizationSiteDTOList() {
        if (invOrganizationSiteDTOList == null) {

            setInvOrganizationSiteDTOList(new ArrayList<>());
        }
        return invOrganizationSiteDTOList;
    }

    /**
     * @param invOrganizationSiteDTOList the invOrganizationSiteDTOList to set
     */
    public void setInvOrganizationSiteDTOList(List<InvOrganizationSiteDTO> invOrganizationSiteDTOList) {
        this.invOrganizationSiteDTOList = invOrganizationSiteDTOList;
    }

}
