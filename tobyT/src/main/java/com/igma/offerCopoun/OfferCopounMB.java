/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.offerCopoun;

import com.toby.businessservice.OfferCopounService;
import com.toby.dto.OfferCopounDTO;
import com.toby.toby.Basic;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author hq004
 */
@Named(value = "offerCopounMB")
@ViewScoped
public class OfferCopounMB extends Basic implements Serializable {

    private UploadedFile OfferCompanyFile;

    private InputStream inputStreamUploadOfferCompany;
    
    private OfferCopounDTO offerCopounDTOSearch;
    
    private List<OfferCopounDTO> offerCopounDTOList;

    public OfferCopounMB() {
    }

    @EJB
    private OfferCopounService offerCopounService;

    @PostConstruct
    public void init() {

    }
    
    public void raghiGenerateCode (){
        offerCopounService.addOfferCopoun(offerCopounDTOSearch.getNumber(), offerCopounDTOSearch.getDiscountType(), offerCopounDTOSearch.getDiscountValue(), offerCopounDTOSearch.getGallaryType(), getUserData().getUser());
        offerCopounService.getALLOfferCopounByOfferSearch(offerCopounDTOSearch);
    }

    public void OpenDlg(String dlgvar) {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('" + dlgvar + "').show();");
    }

    // Add New Offer Company Code
    public void readOfferCopoun() throws BiffException, IOException, Exception {
        setInputStreamUploadOfferCompany(getOfferCompanyFile().getInputstream());
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(getInputStreamUploadOfferCompany());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        Sheet sheet = workbook.getSheet(0);
        importOfferCopoun(sheet);
        workbook.close();
    }

    public void importOfferCopoun(Sheet sheet) throws NumberFormatException {
        List<OfferCopounDTO> offerCopounDTOList = new ArrayList<>();
        for (int i = 1; i < sheet.getRows(); i++) {
            if (!sheet.getCell(0, i).getContents().isEmpty()) {
                String code = sheet.getCell(0, i).getContents();
                OfferCopounDTO offerCopounDTO = new OfferCopounDTO();
                offerCopounDTO.setCustomerCode(code);
                offerCopounDTOList.add(offerCopounDTO);
            }
        }
        offerCopounService.addOfferCopoun(offerCopounDTOList, getUserData().getUserlogin());
        OpenDlg("sucesses");
        System.out.println("تم الحفظ بنجاح");
    }

    /**
     * @return the inputStreamUploadOfferCompany
     */
    public InputStream getInputStreamUploadOfferCompany() {
        return inputStreamUploadOfferCompany;
    }

    /**
     * @param inputStreamUploadOfferCompany the inputStreamUploadOfferCompany to
     * set
     */
    public void setInputStreamUploadOfferCompany(InputStream inputStreamUploadOfferCompany) {
        this.inputStreamUploadOfferCompany = inputStreamUploadOfferCompany;
    }

    /**
     * @return the OfferCompanyFile
     */
    public UploadedFile getOfferCompanyFile() {
        return OfferCompanyFile;
    }

    /**
     * @param OfferCompanyFile the OfferCompanyFile to set
     */
    public void setOfferCompanyFile(UploadedFile OfferCompanyFile) {
        this.OfferCompanyFile = OfferCompanyFile;
    }

    /**
     * @return the offerCopounDTOSearch
     */
    public OfferCopounDTO getOfferCopounDTOSearch() {
        if(offerCopounDTOSearch == null){
            offerCopounDTOSearch = new OfferCopounDTO();
        }
        return offerCopounDTOSearch;
    }

    /**
     * @param offerCopounDTOSearch the offerCopounDTOSearch to set
     */
    public void setOfferCopounDTOSearch(OfferCopounDTO offerCopounDTOSearch) {
        this.offerCopounDTOSearch = offerCopounDTOSearch;
    }

    /**
     * @return the offerCopounDTOList
     */
    public List<OfferCopounDTO> getOfferCopounDTOList() {
        return offerCopounDTOList;
    }

    /**
     * @param offerCopounDTOList the offerCopounDTOList to set
     */
    public void setOfferCopounDTOList(List<OfferCopounDTO> offerCopounDTOList) {
        this.offerCopounDTOList = offerCopounDTOList;
    }

}
