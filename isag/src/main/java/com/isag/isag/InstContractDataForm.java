/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.isag;

import com.isag.businessservice.InstPurchaseInvoiceContractDifrranceViewService;
import com.isag.converter.InstPurchaseInvoiceContractDifrranceViewConverter;
import com.isag.views.InstPurchaseInvoiceContractDifrranceView;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author AhmedEssam
 */
public class InstContractDataForm extends BaseFormBean {

    private InstPurchaseInvoiceContractDifrranceViewConverter instPurchaseInvoiceContractDifrranceViewConverter;
    private List<InstPurchaseInvoiceContractDifrranceView> instPurchaseInvoiceContractDifrranceViewList;

    @EJB
    private InstPurchaseInvoiceContractDifrranceViewService instPurchaseInvoiceContractDifrranceViewService;

    public List<InstPurchaseInvoiceContractDifrranceView> completeInstPurchaseInvoiceContractDifrrance(String query) {
        List<InstPurchaseInvoiceContractDifrranceView> instPurchaseInvoiceContractDifrrance = getInstPurchaseInvoiceContractDifrranceViewList();
        if (query == null || query.trim().equals("")) {
            instPurchaseInvoiceContractDifrranceViewConverter = new InstPurchaseInvoiceContractDifrranceViewConverter(instPurchaseInvoiceContractDifrrance);
            return instPurchaseInvoiceContractDifrrance;
        }
        List<InstPurchaseInvoiceContractDifrranceView> filteredInstPurchaseInvoiceContractDifrranceView = new ArrayList<>();

        String nameAr;
        Integer code;
        InstPurchaseInvoiceContractDifrranceView instPurchaseInvoiceContractDifrranceView;
        for (int i = 0; i < getInstPurchaseInvoiceContractDifrranceViewList().size(); i++) {
            instPurchaseInvoiceContractDifrranceView = getInstPurchaseInvoiceContractDifrranceViewList().get(i);
            nameAr = instPurchaseInvoiceContractDifrranceView.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInstPurchaseInvoiceContractDifrranceView.contains(instPurchaseInvoiceContractDifrranceView)) {
                    filteredInstPurchaseInvoiceContractDifrranceView.add(instPurchaseInvoiceContractDifrranceView);
                }
            }

            code = Integer.valueOf(instPurchaseInvoiceContractDifrranceView.getCode());
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredInstPurchaseInvoiceContractDifrranceView.contains(instPurchaseInvoiceContractDifrranceView)) {
                    filteredInstPurchaseInvoiceContractDifrranceView.add(instPurchaseInvoiceContractDifrranceView);
                }
            }
        }

        instPurchaseInvoiceContractDifrranceViewConverter = new InstPurchaseInvoiceContractDifrranceViewConverter(filteredInstPurchaseInvoiceContractDifrranceView);
        return filteredInstPurchaseInvoiceContractDifrranceView;
    }

    /**
     * @return the instPurchaseInvoiceContractDifrranceViewConverter
     */
    public InstPurchaseInvoiceContractDifrranceViewConverter getInstPurchaseInvoiceContractDifrranceViewConverter() {
        return instPurchaseInvoiceContractDifrranceViewConverter;
    }

    /**
     * @param instPurchaseInvoiceContractDifrranceViewConverter the
     * instPurchaseInvoiceContractDifrranceViewConverter to set
     */
    public void setInstPurchaseInvoiceContractDifrranceViewConverter(InstPurchaseInvoiceContractDifrranceViewConverter instPurchaseInvoiceContractDifrranceViewConverter) {
        this.instPurchaseInvoiceContractDifrranceViewConverter = instPurchaseInvoiceContractDifrranceViewConverter;
    }

    /**
     * @return the instPurchaseInvoiceContractDifrranceViewList
     */
    public List<InstPurchaseInvoiceContractDifrranceView> getInstPurchaseInvoiceContractDifrranceViewList() {
        if (instPurchaseInvoiceContractDifrranceViewList == null || instPurchaseInvoiceContractDifrranceViewList.isEmpty()) {
            instPurchaseInvoiceContractDifrranceViewList = instPurchaseInvoiceContractDifrranceViewService.getAllInstPurchaseInvoiceContractDifrrance(getUserData().getUserBranch().getId());
            instPurchaseInvoiceContractDifrranceViewConverter = new InstPurchaseInvoiceContractDifrranceViewConverter(instPurchaseInvoiceContractDifrranceViewList);
        }
        return instPurchaseInvoiceContractDifrranceViewList;
    }

    /**
     * @param instPurchaseInvoiceContractDifrranceViewList the
     * instPurchaseInvoiceContractDifrranceViewList to set
     */
    public void setInstPurchaseInvoiceContractDifrranceViewList(List<InstPurchaseInvoiceContractDifrranceView> instPurchaseInvoiceContractDifrranceViewList) {
        this.instPurchaseInvoiceContractDifrranceViewList = instPurchaseInvoiceContractDifrranceViewList;
    }

    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void init() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
