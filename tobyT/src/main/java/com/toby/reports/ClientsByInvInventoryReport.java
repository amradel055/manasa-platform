package com.toby.reports;

import com.toby.businessservice.OfferCompanyService;
import com.toby.businessservice.ReportsService;
import com.toby.converter.OfferCompanyDTOConverter;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.OfferCompanyDTO;
import com.toby.toby.Basic;
import com.toby.views.ClientsByInvInventory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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

@Named(value = "clientsByInvInventoryReport")
@ViewScoped
public class ClientsByInvInventoryReport extends Basic {

    private Map<String, String> userDDs;

    private List<ClientsByInvInventory> List;
    private ClientsByInvInventory invInventory;
    private List<OfferCompanyDTO> offerCompanyList;
    private OfferCompanyDTOConverter offerCompanyDTOConverter;

    @EJB
    private ReportsService reportsService;
    @EJB
    private OfferCompanyService companyService;

    @PostConstruct
    public void init() {
        InvInventoryDTO inventoryDTO = getGallaryLists().get(0);
        getInvInventory().getGallaryListSelected().add(inventoryDTO);
        setUserDDs(getUserData().getUserDDs());

    }

    public void search() {
        if (validateSearch() == true) {
            List = new ArrayList<>();
            getList().addAll(reportsService.getClientsByInvInventory(getInvInventory(), getUserData().getUserBranch().getId()));
        }
    }

    public boolean validateSearch() {
        if (getInvInventory().getGallaryListSelected().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserDDs().get("ERROR"), "يجب اختيار المعرض "));
        } //        else if (getInvInventory().getOfferCompanyListSelected().isEmpty()) {
        //            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserDDs().get("ERROR"), "يجب اختيار الشركه "));
        //        } 
        else {
            return true;
        }
        return false;
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
        String reportName = "ClientsByInvInventoryReport.jasper";
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

        hashMap.put("titleText", " العملاء حسب المعرض");
        hashMap.put("gallaryListSelectedText", " المعرض");
        hashMap.put("gallaryListSelectedValue", getInvInventory() == null ? null : getInvInventory().getInventoryDTO().getName());
        hashMap.put("clientMobileText", " الموبايل");
        hashMap.put("clientNameText", " الاسم");
        hashMap.put("clientCodeText", " الكود");

        return hashMap;
    }

    /**
     * @return the List
     */
    public List<ClientsByInvInventory> getList() {
        if (List == null) {
            List = new ArrayList<>();
        }
        return List;
    }

    /**
     * @param List the List to set
     */
    public void setList(List<ClientsByInvInventory> List) {
        this.List = List;
    }

    /**
     * @return the invInventory
     */
    public ClientsByInvInventory getInvInventory() {
        if (invInventory == null) {
            invInventory = new ClientsByInvInventory();
        }
        return invInventory;
    }

    /**
     * @param invInventory the invInventory to set
     */
    public void setInvInventory(ClientsByInvInventory invInventory) {
        this.invInventory = invInventory;
    }

    /**
     * @return the offerCompanyList
     */
    public List<OfferCompanyDTO> getOfferCompanyList() {
        if (offerCompanyList == null || offerCompanyList.isEmpty()) {
            offerCompanyList = companyService.getALLOfferCompanyByBranchId(getUserData().getUser());
            setOfferCompanyDTOConverter(new OfferCompanyDTOConverter(offerCompanyList));
        }
        return offerCompanyList;
    }

    /**
     * @param offerCompanyList the offerCompanyList to set
     */
    public void setOfferCompanyList(List<OfferCompanyDTO> offerCompanyList) {
        this.offerCompanyList = offerCompanyList;
    }

    /**
     * @return the offerCompanyDTOConverter
     */
    public OfferCompanyDTOConverter getOfferCompanyDTOConverter() {
        return offerCompanyDTOConverter;
    }

    /**
     * @param offerCompanyDTOConverter the offerCompanyDTOConverter to set
     */
    public void setOfferCompanyDTOConverter(OfferCompanyDTOConverter offerCompanyDTOConverter) {
        this.offerCompanyDTOConverter = offerCompanyDTOConverter;
    }

    /**
     * @return the userDDs
     */
    public Map<String, String> getUserDDs() {
        return userDDs;
    }

    /**
     * @param userDDs the userDDs to set
     */
    public void setUserDDs(Map<String, String> userDDs) {
        this.userDDs = userDDs;
    }

}
