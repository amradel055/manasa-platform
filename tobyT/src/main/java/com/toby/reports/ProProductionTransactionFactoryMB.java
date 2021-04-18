package com.toby.reports;

import com.toby.businessservice.ProProductionItemsTransactionService;
import com.toby.dto.ProProductionItemsTransactionDTO;
import com.toby.toby.Basic;
import com.toby.views.ProProductionTransactionFactoryView;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import net.sf.jasperreports.engine.JRException;

@Named(value = "proProductionTransactionFactoryMB")
@ViewScoped
public class ProProductionTransactionFactoryMB extends Basic {

    private List<ProProductionItemsTransactionDTO> proProductionItemsTransactionDTOList;
    private ProProductionTransactionFactoryView proProductionTransactionFactoryView;

    @EJB
    private ProProductionItemsTransactionService proProductionItemsTransactionService;

    public void search() {
        setProProductionItemsTransactionDTOList(proProductionItemsTransactionService.getALLProProductionItemsTransactionDTO(proProductionTransactionFactoryView, getUserData().getUserBranch().getId()));
    }

    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        String reportName = "ProProductionTransactionFactoryReport.jasper";
        fillReport(prepareReport(), getUserData().getReportPath() + reportName, getProProductionItemsTransactionDTOList(), "pdf");
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

        hashMap.put("galleryListText", "المعرض");
        hashMap.put("galleryListValue", proProductionTransactionFactoryView.getGallerySelected() == null ? null : proProductionTransactionFactoryView.getGallerySelected().getName());
        hashMap.put("statusText", "المرحلة");
        hashMap.put("statusValue", proProductionTransactionFactoryView.getStatus() == null ? null : proProductionTransactionFactoryView.getStatus().getName());
        hashMap.put("userText", "الموظف");
        hashMap.put("userValue", proProductionTransactionFactoryView.getUser() == null ? null : proProductionTransactionFactoryView.getUser().getUserCode());
        hashMap.put("dateText", "التاريخ");
        hashMap.put("dateValue", proProductionTransactionFactoryView.getDate());
        hashMap.put("invoiceNumberText", "رقم الفاتورة");
        hashMap.put("invoiceNumberValue", proProductionTransactionFactoryView.getInvoiceNumber());
        hashMap.put("serialText", "رقم الفاتورة");
        hashMap.put("createdByNameText", "اسم العامل ");
        hashMap.put("createdByCodeText", " رقم العامل");
        hashMap.put("numberText", "عدد الثياب");
        hashMap.put("nameText", "المرحله");
        hashMap.put("dateText", "التاريخ");

        return hashMap;
    }

    public List<ProProductionItemsTransactionDTO> getProProductionItemsTransactionDTOList() {
        return proProductionItemsTransactionDTOList;
    }

    /**
     * @param proProductionItemsTransactionDTOList the
     * proProductionItemsTransactionDTOList to set
     */
    public void setProProductionItemsTransactionDTOList(List<ProProductionItemsTransactionDTO> proProductionItemsTransactionDTOList) {
        this.proProductionItemsTransactionDTOList = proProductionItemsTransactionDTOList;
    }

    /**
     * @return the proProductionTransactionFactoryView
     */
    public ProProductionTransactionFactoryView getProProductionTransactionFactoryView() {

        if (proProductionTransactionFactoryView == null) {
            proProductionTransactionFactoryView = new ProProductionTransactionFactoryView();
        }
        return proProductionTransactionFactoryView;
    }

    /**
     * @param proProductionTransactionFactoryView the
     * proProductionTransactionFactoryView to set
     */
    public void setProProductionTransactionFactoryView(ProProductionTransactionFactoryView proProductionTransactionFactoryView) {
        this.proProductionTransactionFactoryView = proProductionTransactionFactoryView;
    }

    /**
     * @return the invoiceNumber
     */
}
