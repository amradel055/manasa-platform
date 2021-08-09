package com.isag.invstrip;

import com.isag.businessservice.InvItemService;
import com.isag.businessservice.InvStripService;
import com.isag.bussinessservice.global.AutoCompleteService;
import com.isag.dto.InvDetailDTO;
import com.isag.isag.InventoryBasicDataForm;
import com.isag.views.ItemsBarcodesDetailsView;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.primefaces.model.UploadedFile;

/**
 * @author khaled
 */
@Named(value = "invStripFormMB")
@ViewScoped
public class InvStripFormMB extends InventoryBasicDataForm {

    private UploadedFile invItemFile;

    private InputStream inputStreamUploadInvItem;
    
    StringBuilder error = new StringBuilder();

    @EJB
    private InvStripService invStripService;
    @EJB
    InvItemService invItemService;
    @EJB
    AutoCompleteService autoCompleteService;

    @Override
    @PostConstruct
    public void init() {
        try {
            String error = validateloginDistributedScreens(getInvInvoiceDTO().getScreenName());
            if (error.isEmpty()) {
                load();
            } else {
                setErrorGlobal(error);
                exit("../base/error.xhtml");
            }
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "init");

        }
    }

    @Override
    public void load() {
        try {
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("AddInvStrip")) {
                reset();
                fillDefaultList();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditInvStrip")) {
                fillInvInvoiceDTO();
            } else {
                exit("../invstrip/invStripList.xhtml");
            }
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "load");

        }
    }

    @Override
    public String save() {
        if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getInvPurchaseInvoiceDetailList() != null && !getInvInvoiceDTO().getInvPurchaseInvoiceDetailList().isEmpty()) {
            getInvInvoiceDTO().setMsg(null);
            setInvInvoiceDTO(invStripService.save(getInvInvoiceDTO(), getUserData().getUser()));
            msgSave(getInvInvoiceDTO().getMsg());
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد تفاصيل للفاتورة"));
        }
        return "";
    }

    public String exit() {
        try {
            exit("../invstrip/invStripList.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderFormMB", "exit");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // CostCenter Code
    public void readInvItem() throws BiffException, IOException, Exception {
        setInputStreamUploadInvItem(getInvItemFile().getInputstream());
        Workbook workbook = null;
        error = new StringBuilder();
        try {
            workbook = Workbook.getWorkbook(getInputStreamUploadInvItem());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        Sheet sheet = workbook.getSheet(0);
        List<InvDetailDTO> List = importInvItem(sheet);
        workbook.close();
        getInvInvoiceDTO().setInvPurchaseInvoiceDetailList(List);
        if(error != null && !error.toString().isEmpty()){
            errorMsg(error.toString());
        }
//        setInvInvoiceDTO(invStripService.save(getInvInvoiceDTO(), getUserData().getUser()));
//        msgSave(getInvInvoiceDTO().getMsg());
    }

    public List<InvDetailDTO> importInvItem(Sheet sheet) throws NumberFormatException {
        List<InvDetailDTO> invPurchaseInvoiceDetailList = new ArrayList<>();
        
        for (int i = 1; i < sheet.getRows(); i++) {
            if (!sheet.getCell(0, i).getContents().isEmpty()) {
                String code = sheet.getCell(0, i).getContents();
                String amount = sheet.getCell(1, i).getContents();
                List<ItemsBarcodesDetailsView> barcodesDetailsViewList = autoCompleteService.autoCompleteItemsBarcodesDetailsViewBranchId(getUserData().getUserBranch().getId(), code);
                if (barcodesDetailsViewList != null && !barcodesDetailsViewList.isEmpty()) {
                    InvDetailDTO dTO = new InvDetailDTO();
                    dTO.setActualQuantity(new BigDecimal(amount));
                    dTO.setItemsBarcodesDetail(barcodesDetailsViewList.get(0));
                    dTO.setItemBarcode(code);
                    dTO.setIsEdit(Boolean.TRUE);
                    dTO.setIndex(i);
                    invPurchaseInvoiceDetailList.add(dTO);
                } else {
                    System.out.println(i + " -> " + code + " -> " + amount);
                    error.append(i+1).append(" -> ").append(code).append(" -> ").append(amount).append("\n");
                }
            }
        }
        return invPurchaseInvoiceDetailList;
    }

    /**
     * @return the invItemFile
     */
    public UploadedFile getInvItemFile() {
        return invItemFile;
    }

    /**
     * @param invItemFile the invItemFile to set
     */
    public void setInvItemFile(UploadedFile invItemFile) {
        this.invItemFile = invItemFile;
    }

    /**
     * @return the inputStreamUploadInvItem
     */
    public InputStream getInputStreamUploadInvItem() {
        return inputStreamUploadInvItem;
    }

    /**
     * @param inputStreamUploadInvItem the inputStreamUploadInvItem to set
     */
    public void setInputStreamUploadInvItem(InputStream inputStreamUploadInvItem) {
        this.inputStreamUploadInvItem = inputStreamUploadInvItem;
    }

}
