/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.proFactoryDelivery;

import com.toby.businessservice.ProFactoryDeliveryService;
import com.toby.define.ScreenNameClassEnum;
import com.toby.dto.InvInvoiceDTO;
import com.toby.toby.ProductionBasicDataForm;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author WIN7
 */
@Named(value = "proFactoryDeliveryFormDTOMB")
@ViewScoped
public class ProFactoryDeliveryFormDTOMB extends ProductionBasicDataForm {

    // EJBs
    @EJB
    private ProFactoryDeliveryService proFactoryDeliveryService;

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "ProFactoryDeliveryFormDTOMB", "init");

        }
    }

    public void loadInvoiceSerial() {
        loadDlg();
        findAccountStatement();
    }

    @Override
    public void load() {
        try {
            if (getScreenMode() != null && "AddproFactoryDelivery".equals(getScreenMode())) {
                reset();
                fillDefaultList();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditproFactoryDelivery")) {
                fillInvInvoiceDTO();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("proSalesFormAdd")) {
                reset();
                fillDefaultList();
                getInvInvoiceDTO().setLoadedSerial((Integer) getContext().getSessionMap().get("proSalesFormSelected"));
                loadDlg();
                findAccountStatement();
            } else {
                exit("../profactoryDelivery/profactorydeviveryList.xhtml");
            }
        } catch (Exception e) {
            saveError(e, "ProFactoryDeliveryFormDTOMB", "load");

        }
    }

    @Override
    public String save() {
        if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getInvPurchaseInvoiceDetailList() != null && !getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().isEmpty()) {
            getInvInvoiceDTO().setMsg(null);
            findScreenName();
            setInvInvoiceDTO(proFactoryDeliveryService.preSave(getInvInvoiceDTO(), getUserData().getUser()));
            msgSave(getInvInvoiceDTO().getMsg());
            sendSMS();
            reset();
            changeFocus("form:invoiceNumber");
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد تفاصيل للفاتورة"));
        }
        return "";
    }

    public void sendSMS() {
        if (getUri().contains("proRecieveGallary")) {
            StringBuilder message = new StringBuilder();
            if (getInvInvoiceDTO().getInvnameType() != null && getInvInvoiceDTO().getInvnameType() == 0) {
                message.append("برنس للخياطة يبلغكم بأن الثياب جاهزة للتسليم  للاستفسار الاتصال على الرقم");
                message.append("920027070");
                message.append(" اكمل اناقتك مع شماغ برنس ");
                sendSms(getInvInvoiceDTO().getCustomerMobile(), "التوبى العالمى", message.toString(), "20099319", "0564933202");
            } else if (getInvInvoiceDTO().getInvnameType() != null && getInvInvoiceDTO().getInvnameType() == 1) {
                message.append("لي رويال للخياطة الرجالية يبلغكم بان الثياب جاهزة للاستلام ت ");
                message.append("920027711");
                message.append("\n");
                message.append("نسعد بتلقي تقييمكم عن الخدمة المقدمة لكم من لي رويال  \n");
                message.append("https://forms.gle/ru5v8evNy1nJ6QbK7");
                sendSms(getInvInvoiceDTO().getCustomerMobile(), "التوبى العالمى", message.toString(), "20099320", "Royal@e12z");
            }
        }
    }

    public String saveAll() {
        if (getInvInvoiceDTO().getInvDailogeDataListSelected() != null) {
            for (InvInvoiceDTO dTO : getInvInvoiceDTOListSelected()) {
                setInvInvoiceDTO(dTO);
                save();
                getInvInvoiceDTOListAfterSave().add(getInvInvoiceDTO());
            }
            setInvInvoiceDTOListSelected(getInvInvoiceDTOListAfterSave());
            setInvInvoiceDTO(getInvInvoiceDTOListSelected().get(0));
            setInvoiceNumber(0);
        }
        return "";
    }

    public String saveSelection() {
        int indexOfList = getInvInvoiceDTOListSelected().indexOf(getInvInvoiceDTO());

        save();
        getInvInvoiceDTOListSelected().remove(indexOfList);
//        forward();
        if (indexOfList != -1) {
            getInvInvoiceDTOListSelected().add(indexOfList, getInvInvoiceDTO());
            if (indexOfList < getInvInvoiceDTOListSelected().size() - 1) {
                setInvInvoiceDTO(getInvInvoiceDTOListSelected().get(indexOfList + 1));
            } else {
                setInvInvoiceDTO(getInvInvoiceDTOListSelected().get(0));
            }
        }
        return "";
    }

    public String exit() {
        try {
            if (ScreenNameClassEnum.INVPERMISSIONORDERFORM.equals(getInvInvoiceDTO().getScreenName())) {
                exit("../invpermissionorder_1/invpermissionorderlist.xhtml");
            } else if (ScreenNameClassEnum.INVADDINGORDERFORM.equals(getInvInvoiceDTO().getScreenName())) {
                exit("../invaddingorder/invAddingOrderList1.xhtml");
            }
            return "";
        } catch (Exception e) {
            saveError(e, "ProFactoryDeliveryFormDTOMB", "exit");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
