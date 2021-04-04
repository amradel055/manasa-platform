package com.toby.reports;

import com.toby.businessservice.InvPurchaseInvoiceService_1;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvPurchaseInvoiceDTO;
import com.toby.toby.Basic;
import com.toby.views.ClientsNoMovment;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import net.sf.jasperreports.engine.JRException;

@Named(value = "clientsNoMovmentMB")
@ViewScoped
public class ClientsNoMovmentMB extends Basic {

    private List<InvPurchaseInvoiceDTO> invPurchaseInvoiceDTOList;
    private ClientsNoMovment clientsNoMovment;
    private InvInventoryDTO galleryList;
    private Date date;

    @EJB
    private InvPurchaseInvoiceService_1 invPurchaseInvoiceService;

    public void search() {

        setInvPurchaseInvoiceDTOList(invPurchaseInvoiceDTOList);
    }

    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        String reportName = "ProProductionTransactionFactoryReport.jasper";
        fillReport(prepareReport(), getUserData().getReportPath() + reportName, getInvPurchaseInvoiceDTOList(), "pdf");
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

        hashMap.put("clientNumberText", "رقم العميل");
        hashMap.put("clientNameText", "اسم العميل ");
        hashMap.put("phoneNumberText", " الهاتف");
        hashMap.put("lastInvoiceDateText", "تاريخ اخر فاتورة");
        hashMap.put("totalInvoicesText", " اجمالى التعامل");

        return hashMap;
    }

    /**
     * @return the invPurchaseInvoiceDTOList
     */
    public List<InvPurchaseInvoiceDTO> getInvPurchaseInvoiceDTOList() {
        return invPurchaseInvoiceDTOList;
    }

    /**
     * @param invPurchaseInvoiceDTOList the invPurchaseInvoiceDTOList to set
     */
    public void setInvPurchaseInvoiceDTOList(List<InvPurchaseInvoiceDTO> invPurchaseInvoiceDTOList) {
        this.invPurchaseInvoiceDTOList = invPurchaseInvoiceDTOList;
    }

    public InvInventoryDTO getGalleryList() {
        return galleryList;
    }

    /**
     * @param galleryList the galleryList to set
     */
    public void setGalleryList(InvInventoryDTO galleryList) {
        this.galleryList = galleryList;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the clientsNoMovment
     */
    public ClientsNoMovment getClientsNoMovment() {

        if (clientsNoMovment == null) {
            clientsNoMovment = new ClientsNoMovment();
        }
        return clientsNoMovment;
    }

    /**
     * @param clientsNoMovment the clientsNoMovment to set
     */
    public void setClientsNoMovment(ClientsNoMovment clientsNoMovment) {
        this.clientsNoMovment = clientsNoMovment;
    }

    /**
     * @return the clientsNoMovment
     */
}
