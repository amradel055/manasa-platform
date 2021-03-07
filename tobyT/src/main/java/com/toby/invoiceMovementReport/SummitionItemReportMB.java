package com.toby.invoiceMovementReport;

import com.toby.businessservice.InvPurchaseInvoiceService_1;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.ProGroupDTO;
import com.toby.toby.Basic;
import com.toby.views.SummitionItemsView;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "summitionItemReportMB")
@ViewScoped
public class SummitionItemReportMB extends Basic{

    private InvInventoryDTO gallaryDTOSelected;
    private Date dateFrom;
    private Date dateTo;
    private ProGroupDTO proGroupDTOSelected;
    
    private List<SummitionItemsView> itemsViewList;
    
    @EJB
    InvPurchaseInvoiceService_1 invPurchaseInvoiceService_1;
    
    
    
    public void search(){
        invPurchaseInvoiceService_1.findSummitionQuantity(getGallaryDTOSelected() == null ? null : getGallaryDTOSelected().getId(), getUserData().getUserBranch().getId(), getDateFrom(), getDateTo(), getProGroupDTOSelected() == null ? null : getProGroupDTOSelected().getId());
    }

    /**
     * @return the gallaryDTOSelected
     */
    public InvInventoryDTO getGallaryDTOSelected() {
        return gallaryDTOSelected;
    }

    /**
     * @param gallaryDTOSelected the gallaryDTOSelected to set
     */
    public void setGallaryDTOSelected(InvInventoryDTO gallaryDTOSelected) {
        this.gallaryDTOSelected = gallaryDTOSelected;
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
     * @return the proGroupDTOSelected
     */
    public ProGroupDTO getProGroupDTOSelected() {
        return proGroupDTOSelected;
    }

    /**
     * @param proGroupDTOSelected the proGroupDTOSelected to set
     */
    public void setProGroupDTOSelected(ProGroupDTO proGroupDTOSelected) {
        this.proGroupDTOSelected = proGroupDTOSelected;
    }

    /**
     * @return the itemsViewList
     */
    public List<SummitionItemsView> getItemsViewList() {
        return itemsViewList;
    }

    /**
     * @param itemsViewList the itemsViewList to set
     */
    public void setItemsViewList(List<SummitionItemsView> itemsViewList) {
        this.itemsViewList = itemsViewList;
    }

    
}
