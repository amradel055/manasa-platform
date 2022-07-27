package com.toby.reports;

import com.toby.businessservice.InvItemService;
import com.toby.businessservice.ProGroupService;
import com.toby.converter.ProGroupDTOConverter;
import com.toby.dto.InvItemDTO;
import com.toby.dto.ProGroupDTO;
import com.toby.toby.Basic;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

@Named(value = "invItemMainDataReport")
@ViewScoped
public class InvItemMainDataReport extends Basic {

    private List<InvItemDTO> list;
    private InvItemDTO invItemDTO;

    private List<ProGroupDTO> groupDTOSelected;
    private List<ProGroupDTO> groupDTOList;
    private ProGroupDTOConverter proGroupDTOConverter;

    private Boolean isUsed;
    private BigDecimal lastCost;
    private Date dateFrom;
    private Integer itemNatural;

    @EJB
    private InvItemService invItemService;
    @EJB
    ProGroupService proGroupService;

    @PostConstruct
    public void init() {
        getGroupDTOSelected().addAll(getGroupDTOList());
    }

    public void search() {
        if (getGroupDTOSelected() != null && !getGroupDTOSelected().isEmpty()) {
            list = new ArrayList<>();
            for (ProGroupDTO proGroupDTO : getGroupDTOSelected()) {
                getInvItemDTO().setProGroupId(proGroupDTO);
                getList().addAll(invItemService.invItemsReport(getInvItemDTO(), getUserData().getUserBranch().getId(), isUsed, lastCost, dateFrom,itemNatural));
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

        hashMap.put("groupDTOSelectedText", " الفئة");
        hashMap.put("groupDTOSelectedValue", getInvItemDTO().getProGroupId() == null ? null : getInvItemDTO().getProGroupId().getName());
        hashMap.put("idText", "رقم الصنف");
        hashMap.put("nameText", " اسم الصنف");
        hashMap.put("sellPriceText", "التكلفة");
        hashMap.put("maxpricemenText", "اعلى سعر للرجال");
        hashMap.put("maxpriceyoungText", "اعلى سعر للشباب");
        hashMap.put("minpricemenText", "اقل سعر للرجال");
        hashMap.put("minpriceyoungText", " اقل سعر للشباب");
        hashMap.put("numbermetersfreemenText", "عدد الامتار المجانية للرجال");
        hashMap.put("numbermetersfreeyoungText", "عدد الامتار المجانية للشباب");
        hashMap.put("numbermetersmenText", "عدد الامتار  للرجال");
        hashMap.put("numbermetersyoungText", " عدد الامتار  للشباب");
        hashMap.put("itemNaturalText", "طبيعة الصنف");

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

    /**
     * @return the groupDTOSelected
     */
    public List<ProGroupDTO> getGroupDTOSelected() {
        if (groupDTOSelected == null) {
            groupDTOSelected = new ArrayList<>();
        }
        return groupDTOSelected;
    }

    /**
     * @param groupDTOSelected the groupDTOSelected to set
     */
    public void setGroupDTOSelected(List<ProGroupDTO> groupDTOSelected) {
        this.groupDTOSelected = groupDTOSelected;
    }

    /**
     * @return the groupDTOList
     */
    public List<ProGroupDTO> getGroupDTOList() {
        if (groupDTOList == null || groupDTOList.isEmpty()) {
            groupDTOList = proGroupService.getallProGroupByBranchIdDTO(getUserData().getUser());
            setProGroupDTOConverter(new ProGroupDTOConverter(groupDTOList));
        }
        return groupDTOList;
    }

    /**
     * @param groupDTOList the groupDTOList to set
     */
    public void setGroupDTOList(List<ProGroupDTO> groupDTOList) {
        this.groupDTOList = groupDTOList;
    }

    /**
     * @return the proGroupDTOConverter
     */
    public ProGroupDTOConverter getProGroupDTOConverter() {
        return proGroupDTOConverter;
    }

    /**
     * @param proGroupDTOConverter the proGroupDTOConverter to set
     */
    public void setProGroupDTOConverter(ProGroupDTOConverter proGroupDTOConverter) {
        this.proGroupDTOConverter = proGroupDTOConverter;
    }

    /**
     * @return the isUsed
     */
    public Boolean getIsUsed() {
        if (isUsed == null) {
            isUsed = Boolean.TRUE;
        }
        return isUsed;
    }

    /**
     * @param isUsed the isUsed to set
     */
    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    /**
     * @return the lastCost
     */
    public BigDecimal getLastCost() {
        return lastCost;
    }

    /**
     * @param lastCost the lastCost to set
     */
    public void setLastCost(BigDecimal lastCost) {
        this.lastCost = lastCost;
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
     * @return the itemNatural
     */
    public Integer getItemNatural() {
        return itemNatural;
    }

    /**
     * @param itemNatural the itemNatural to set
     */
    public void setItemNatural(Integer itemNatural) {
        this.itemNatural = itemNatural;
    }

}
