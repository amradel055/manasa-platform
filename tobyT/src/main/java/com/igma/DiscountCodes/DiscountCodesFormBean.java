package com.igma.DiscountCodes;

import com.toby.businessservice.DiscountCodesService;
import com.toby.dto.DiscountCodesDTO;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author H
 */
@Named(value = "discountCodesFormBean")
@ViewScoped
public class DiscountCodesFormBean extends BaseFormBean {

    private DiscountCodesDTO discountCodesDTO;

    private UploadedFile discountCodeFile;
    private InputStream inputStreamUploadDiscountCode;

    @EJB
    DiscountCodesService discountCodesService;

    public DiscountCodesFormBean() {
    }

    @Override
    @PostConstruct
    public void init() {

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        setScreenMode((String) context.getSessionMap().get("ScreenMode"));
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("Edit")) {
            try {
                Integer discountCodesId = (Integer) context.getSessionMap().get("discountCodesDTOSelected");
                discountCodesDTO = discountCodesService.findDiscountCodes(discountCodesId, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void reset() {
        setDiscountCodesDTO(null);
    }

    public String exit() {
        try {
            exit("../DiscountCodes/discountCodesList.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "glBankFromMB", "exit");
            return null;
        }
    }

    public void readDiscountCode() throws BiffException, IOException, Exception {
        setInputStreamUploadDiscountCode(getDiscountCodeFile().getInputstream());
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(getInputStreamUploadDiscountCode());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        Sheet sheet = workbook.getSheet(0);
        importDiscountCode(sheet);
        workbook.close();
    }

    public void importDiscountCode(Sheet sheet) throws NumberFormatException {
        for (int i = 1; i < sheet.getRows(); i++) {
            if (!sheet.getCell(0, i).getContents().isEmpty()) {
                String code = sheet.getCell(0, i).getContents();
                //String bankName = sheet.getCell(1, i).getContents();
               // String disValue = sheet.getCell(2, i).getContents();
                //discountCodesDTO = new DiscountCodesDTO();
                discountCodesDTO.setCode(code);
                discountCodesDTO.setBankName(getDiscountCodesDTO().getBankName());
                discountCodesDTO.setDiscountValue(getDiscountCodesDTO().getDiscountValue());
                discountCodesService.addDiscountCodes(discountCodesDTO, getUserData().getUserlogin());

            }
        }
        OpenDlg("sucesses");
        System.out.println("تم الحفظ بنجاح");
    }

    @Override
    public void load() {

    }

    @Override
    public String save() {

        setDiscountCodesDTO(discountCodesService.addDiscountCodes(getDiscountCodesDTO(), getUserData().getUser()));

        return "";
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the discountCodesDTO
     */
    public DiscountCodesDTO getDiscountCodesDTO() {
        if (discountCodesDTO == null) {
            discountCodesDTO = new DiscountCodesDTO();
        }
        return discountCodesDTO;
    }

    /**
     * @param discountCodesDTO the discountCodesDTO to set
     */
    public void setDiscountCodesDTO(DiscountCodesDTO discountCodesDTO) {
        this.discountCodesDTO = discountCodesDTO;
    }

    /**
     * @return the discountCodeFile
     */
    public UploadedFile getDiscountCodeFile() {
        return discountCodeFile;
    }

    /**
     * @param discountCodeFile the discountCodeFile to set
     */
    public void setDiscountCodeFile(UploadedFile discountCodeFile) {
        this.discountCodeFile = discountCodeFile;
    }

    /**
     * @return the inputStreamUploadDiscountCode
     */
    public InputStream getInputStreamUploadDiscountCode() {
        return inputStreamUploadDiscountCode;
    }

    /**
     * @param inputStreamUploadDiscountCode the inputStreamUploadDiscountCode to
     * set
     */
    public void setInputStreamUploadDiscountCode(InputStream inputStreamUploadDiscountCode) {
        this.inputStreamUploadDiscountCode = inputStreamUploadDiscountCode;
    }

}
