/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.report;

import com.toby.businessservice.GeneralSymbolService;
import com.toby.businessservice.reports.searchBean.GeneralSymbolSearchBean;
import com.toby.entity.GeneralSymbol;
import com.toby.entity.TobyCompany;
import com.toby.toby.BaseGlAccountReportBean;
import com.toby.toby.UserData;
import com.toby.report.entity.GeneralSymbolBean;
import com.toby.report.entity.GeneralSymbolDetailReportBean;
import com.toby.report.entity.GeneralSymbolForReportBean;
import java.io.IOException;
import java.util.HashMap;
import javax.faces.event.ActionEvent;
import com.toby.views.GeneralSymbolView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author hq002
 */
@Named(value = "generalSymbolReportMB")
@ViewScoped
public class GeneralSymbolReportMB extends BaseGlAccountReportBean {

    private UserData userData;
    private List<GeneralSymbolView> generalSymbolView;
    private GeneralSymbolSearchBean generalSymbolSearchBean;
    private List<GeneralSymbol> generalSymbolList;
    private Map<Integer, GeneralSymbol> generalSymbolMap = new HashMap<>();
    private List<GeneralSymbolBean> generalSymbolBeanList;
    private List<GeneralSymbolBean> generalSymbolBeanListForShow;
    private List<GeneralSymbolForReportBean> symbolForReportBeanList;
    private GeneralSymbolDetailReportBean generalSymbolDetailReportBean;
    private GeneralSymbolBean newSymbolItem;
    private JasperPrint jasperPrint;

    @EJB
    private GeneralSymbolService GeneralsymbolService;

    public GeneralSymbolReportMB() {

    }

    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        userData = (UserData) context.getSessionMap().get("userLogInData");
        reset();
    }

    @Override
    public void reset() {
        generalSymbolMap = new HashMap<>();
        generalSymbolSearchBean = new GeneralSymbolSearchBean();
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        TobyCompany companyId = getUserData().getCompany();
        generalSymbolSearchBean.setCompanyId(companyId.getId());
        generalSymbolList = GeneralsymbolService.getAllGSymbolAscended();
        if (generalSymbolList != null && !generalSymbolList.isEmpty()) {
            for (GeneralSymbol saSymbol : generalSymbolList) {
                generalSymbolMap.put(saSymbol.getSerial(), saSymbol);
            }
        }
    }

    @Override
    public void search() {

        generalSymbolView = GeneralsymbolService.getAllGSymbol(generalSymbolSearchBean);
        generalSymbolBeanList = new ArrayList<>();
        generalSymbolBeanListForShow = new ArrayList<>();
        symbolForReportBeanList = new ArrayList<>();
        Integer nextNumber = -1;
        Map<String, String> userDDs = userData.getUserDDs();
        for (GeneralSymbolView generalSymbolViews : generalSymbolView) {
            if (!nextNumber.equals(Integer.parseInt(generalSymbolViews.getGeneralSerial()))) {
                nextNumber = Integer.parseInt(generalSymbolViews.getGeneralSerial());

                newSymbolItem = new GeneralSymbolBean();
                newSymbolItem.setGeneralSerial(Integer.parseInt(generalSymbolViews.getGeneralSerial()));
                newSymbolItem.setGeneralName(userDDs.get(generalSymbolViews.getGeneralName()));

                getNewSymbolItem().setGeneralSymbolDetailReportBeanList(new ArrayList<GeneralSymbolDetailReportBean>());
                for (GeneralSymbolView generalSymbolViewForDetail : generalSymbolView) {
                    if (generalSymbolViewForDetail.getGeneralSerial().equals(generalSymbolViews.getGeneralSerial())) {
                        generalSymbolDetailReportBean = new GeneralSymbolDetailReportBean();
                        GeneralSymbolForReportBean symbolForReportBean = new GeneralSymbolForReportBean();
                        generalSymbolDetailReportBean.setSymbolName(generalSymbolViewForDetail.getSymbolName());
                        generalSymbolDetailReportBean.setSymbolSerial(Integer.parseInt(generalSymbolViewForDetail.getSymbolSerial()));
                        symbolForReportBean.setGeneralSerial(Integer.parseInt(generalSymbolViews.getGeneralSerial()));
                        symbolForReportBean.setGeneralName(userDDs.get(generalSymbolViews.getGeneralName()));
                        symbolForReportBean.setSymbolName(generalSymbolViewForDetail.getSymbolName());
                        symbolForReportBean.setSymbolSerial(Integer.parseInt(generalSymbolViewForDetail.getSymbolSerial()));
                        newSymbolItem.getGeneralSymbolDetailReportBeanList().add(generalSymbolDetailReportBean);
                        symbolForReportBeanList.add(symbolForReportBean);

                    }
                }
                generalSymbolBeanListForShow.add(newSymbolItem);
            }
        }
    }

    @Override
    public HashMap prepareReport() {

        Map<String, String> userDDs = userData.getUserDDs();
        String symbolIndexText = userDDs.get("SYMBOLS_INDEX");
        String symbolFromText = userDDs.get("SYMBOL_FROM");
        String symbolToText = userDDs.get("SYMBOL_TO");
        String serialText = userDDs.get("SERIAL");
        String generalSymbolText = userDDs.get("GENERAL_SYMBOLS");
        String generalSymbolNumberText = userDDs.get("GENERAL_SYMBOL_NUMBER");
        String symbolNumberText = userDDs.get("SYMBOL_NUMBER");
        String symbolNameText = userDDs.get("SYMBOL_NAME");
        String PrintedBy = userData.getUser().getName();
        String companyName = userData.getCompany().getName();
        String companyImage = getUserData().getImageReportPath();
        String branchName = getUserData().getUserBranch().getNameAr();

        HashMap hashMap = new HashMap();

       
        hashMap.put("serialFrom", generalSymbolSearchBean.getSerialFrom());
        hashMap.put("serialTo", generalSymbolSearchBean.getSerialTo());
        hashMap.put("symbolIndexText", symbolIndexText);
        hashMap.put("symbolFromText", symbolFromText);
        hashMap.put("symbolToText", symbolToText);
        hashMap.put("serialText", serialText);
        hashMap.put("generalSymbolText", generalSymbolText);
        hashMap.put("generalSymbolNumberText", generalSymbolNumberText);
        hashMap.put("symbolNumberText", symbolNumberText);
        hashMap.put("symbolNameText", symbolNameText);

        hashMap.put("PrintedBy", PrintedBy);
        //hashMap.put("companyImage", companyImage);
        hashMap.put("branchName", branchName);

        if (generalSymbolMap.containsKey(generalSymbolSearchBean.getSerialFrom())) {
            String from = generalSymbolMap.get(generalSymbolSearchBean.getSerialFrom()) != null ? generalSymbolMap.get(generalSymbolSearchBean.getSerialFrom()).getName() : null;
            hashMap.put("symbolNameFrom", userDDs.get(from));
        }
        if (generalSymbolMap.containsKey(generalSymbolSearchBean.getSerialTo())) {
            String to = generalSymbolMap.get(generalSymbolSearchBean.getSerialTo()) != null ? generalSymbolMap.get(generalSymbolSearchBean.getSerialTo()).getName() : null;
            hashMap.put("symbolNameTo", userDDs.get(to));
        }
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        search();
        if (symbolForReportBeanList != null && !symbolForReportBeanList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "generalSymbolReport.jasper", symbolForReportBeanList, "pdf");
        }
    }

    @Override
    public void exportExcel(ActionEvent actionEvent) throws JRException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportHtml(ActionEvent actionEvent) throws JRException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<GeneralSymbolView> getGeneralSymbolView() {
        return generalSymbolView;
    }

    public void setGeneralSymbolView(List<GeneralSymbolView> generalSymbolView) {
        this.generalSymbolView = generalSymbolView;
    }

    public GeneralSymbolSearchBean getGeneralSymbolSearchBean() {
        return generalSymbolSearchBean;
    }

    public void setGeneralSymbolSearchBean(GeneralSymbolSearchBean generalSymbolSearchBean) {
        this.generalSymbolSearchBean = generalSymbolSearchBean;
    }

    public JasperPrint getJasperPrint() {
        return jasperPrint;
    }

    public void setJasperPrint(JasperPrint jasperPrint) {
        this.jasperPrint = jasperPrint;
    }

    public GeneralSymbolService getGeneralsymbolService() {
        return GeneralsymbolService;
    }

    public void setGeneralsymbolService(GeneralSymbolService GeneralsymbolService) {
        this.GeneralsymbolService = GeneralsymbolService;
    }

    /**
     * @return the generalSymbolList
     */
    public List<GeneralSymbol> getGeneralSymbolList() {
        return generalSymbolList;
    }

    /**
     * @param generalSymbolList the generalSymbolList to set
     */
    public void setGeneralSymbolList(List<GeneralSymbol> generalSymbolList) {
        this.generalSymbolList = generalSymbolList;
    }

    /**
     * @return the generalSymbolBeanList
     */
    public List<GeneralSymbolBean> getGeneralSymbolBeanList() {
        return generalSymbolBeanList;
    }

    /**
     * @param generalSymbolBeanList the generalSymbolBeanList to set
     */
    public void setGeneralSymbolBeanList(List<GeneralSymbolBean> generalSymbolBeanList) {
        this.generalSymbolBeanList = generalSymbolBeanList;
    }

    @Override
    public void checkDate(Boolean dateType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetDateFrom() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetDateTo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the generalSymbolBeanListForShow
     */
    public List<GeneralSymbolBean> getGeneralSymbolBeanListForShow() {
        return generalSymbolBeanListForShow;
    }

    /**
     * @param generalSymbolBeanListForShow the generalSymbolBeanListForShow to
     * set
     */
    public void setGeneralSymbolBeanListForShow(List<GeneralSymbolBean> generalSymbolBeanListForShow) {
        this.generalSymbolBeanListForShow = generalSymbolBeanListForShow;
    }

    /**
     * @return the generalSymbolDetailReportBean
     */
    public GeneralSymbolDetailReportBean getGeneralSymbolDetailReportBean() {
        return generalSymbolDetailReportBean;
    }

    /**
     * @param generalSymbolDetailReportBean the generalSymbolDetailReportBean to
     * set
     */
    public void setGeneralSymbolDetailReportBean(GeneralSymbolDetailReportBean generalSymbolDetailReportBean) {
        this.generalSymbolDetailReportBean = generalSymbolDetailReportBean;
    }

    /**
     * @return the newSymbolItem
     */
    public GeneralSymbolBean getNewSymbolItem() {
        return newSymbolItem;
    }

    /**
     * @param newSymbolItem the newSymbolItem to set
     */
    public void setNewSymbolItem(GeneralSymbolBean newSymbolItem) {
        this.newSymbolItem = newSymbolItem;
    }

    /**
     * @return the symbolForReportBeanList
     */
    public List<GeneralSymbolForReportBean> getSymbolForReportBeanList() {
        return symbolForReportBeanList;
    }

    /**
     * @param symbolForReportBeanList the symbolForReportBeanList to set
     */
    public void setSymbolForReportBeanList(List<GeneralSymbolForReportBean> symbolForReportBeanList) {
        this.symbolForReportBeanList = symbolForReportBeanList;
    }

}
