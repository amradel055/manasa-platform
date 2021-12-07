/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.invPurchaseInvoiceDTO;

import com.isag.businessservice.InvPurchaseInvoiceService_1;
import com.isag.define.ScreenNameClassEnum;
import com.isag.dto.InvDetailDTO;
import com.isag.isag.InventoryBasicDataForm;
import java.math.BigDecimal;
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
@Named(value = "invPurchaseInvoiceFormDTOMB")
@ViewScoped
public class InvPurchaseInvoiceFormDTOMB extends InventoryBasicDataForm {
    
    private Boolean checkDiscountValue = false ;
    private Boolean checkDiscountRate = false ;
    // EJBs
    @EJB
    private InvPurchaseInvoiceService_1 invPurchaseInvoiceService;
    private BigDecimal remain;

    @Override
    @PostConstruct
    public void init() {
        try {
            setFirstLoad(Boolean.FALSE);
            String error = validateloginDistributedScreens(getInvInvoiceDTO().getScreenName());
            if (error.isEmpty()) {
                load();
                posSetting();
                if (getUri().contains("invposinvoiceform")) {
                    changeFocus("form:invPurchaseInvoiceDetailsTable:0:invItemNameIn");
                }
            } else {
                setErrorGlobal(error);
                exit("../base/error.xhtml");
            }
            

            //   getItemsBarcodesDetailsViewList();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "init");
        }
    }

    @Override
    public void load() {
        try {

            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("addInvoice")) {
                reset();
                fillDefaultList();
                putCurrency();
                Integer typeActivity = (Integer) getContext().getSessionMap().get("typeActivity");
                if (typeActivity == 1) {
                    getInvInvoiceDTO().setTypeActivity(1);
                }
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("editInvoice")) {
                reset();
                getInvInvoiceDTO().setIsEdit(Boolean.TRUE);
                fillInvInvoiceDTO();
                fillDefaultList();
                putCurrency();
                getInvInvoiceDTO().setIsEdit(Boolean.FALSE);
            } else {
                exit("../invpurchaseinvoice_1/invpurchaseinvoicelist.xhtml");
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "load");
        }
    }
    
   

    @Override
    public String save() {
        if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getInvPurchaseInvoiceDetailList() != null && !getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().isEmpty()) {
            getInvInvoiceDTO().setMsg(null);
            setInvInvoiceDTO(invPurchaseInvoiceService.save(getInvInvoiceDTO(), getUserData().getUser()));
            msgSave(getInvInvoiceDTO().getMsg());
//            reset();
//            exit("../invsalesinvoice_1/invsalesinvoiceformCeramic.xhtml");
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد تفاصيل للفاتورة"));
        }
        return "";
    }

    public void calcAfterPayCash() {
        try {
            if (getFirstLoad()) {
                calculateRemainValue();
                savePOS();
                setFirstLoad(Boolean.FALSE);
            }
        } catch (Exception e) {
            saveError(e, "SalesInvoiceFormMB", "calculateRemainValue");
        }
    }

    public String savePOS() {
        OpenDlg("remainconfirm");
        setRemain(getInvInvoiceDTO().getRemain());
        save();
        changeFocus("sure");
        return "";
    }

    public void closePOS() {
        CloseDlg("remainconfirm");
        reset();
        addInvDetail();
        changeFocus("form:invPurchaseInvoiceDetailsTable:0:invItemNameIn");
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String exit() {
        if (ScreenNameClassEnum.INVPURCHASEINVOICEFORM.equals(getInvInvoiceDTO().getScreenName())) {
            exit("../invpurchaseinvoice_1/invpurchaseinvoicelist.xhtml");
        } else if (ScreenNameClassEnum.INVRETURNPURCHASEFORM.equals(getInvInvoiceDTO().getScreenName())) {
            exit("../invreturnpurchase_1/invreturnpurchaselist.xhtml");
        } else if (ScreenNameClassEnum.INVSALESINVOICEFORM.equals(getInvInvoiceDTO().getScreenName())) {
            exit("../invsalesinvoice_1/invsalesinvoicelist.xhtml");
        } else if (ScreenNameClassEnum.INVRETURNSALESFORM.equals(getInvInvoiceDTO().getScreenName())) {
            exit("../invreturnsales_1/invreturnsaleslist.xhtml");
        } else if (ScreenNameClassEnum.INVPOSINVOICEFORM.equals(getInvInvoiceDTO().getScreenName())) {
            exit("../invsalesinvoice_1/invsalesinvoicelist.xhtml");
        }
        return "";
    }

    /**
     * @return the remain
     */
    public BigDecimal getRemain() {
        if (remain == null) {
            remain = BigDecimal.ZERO;
        }
        return remain;
    }

    /**
     * @param remain the remain to set
     */
    public void setRemain(BigDecimal remain) {
        this.remain = remain;
    }
    
    
    /**
     * @return the checkDiscountValue
     */
    public Boolean getCheckDiscountValue() {
        return checkDiscountValue;
    }

    /**
     * @param checkDiscountValue the checkDiscountValue to set
     */
    public void setCheckDiscountValue(Boolean checkDiscountValue) {
        this.checkDiscountValue = checkDiscountValue;
    }

    /**
     * @return the checkDiscountRate
     */
    public Boolean getCheckDiscountRate() {
        return checkDiscountRate;
    }

    /**
     * @param checkDiscountRate the checkDiscountRate to set
     */
    public void setCheckDiscountRate(Boolean checkDiscountRate) {
        this.checkDiscountRate = checkDiscountRate;
    }

}
