/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.toby;

import com.toby.businessservice.CurrencyOperationService;
import com.toby.bussinessservice.global.AutoCompleteService;
import com.toby.converter.InvOrganizationSiteConverter;
import com.toby.converter.InvOrganizationSiteDTOConverter;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.entity.InvOrganizationSite;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author m_els
 */
public abstract class GlBankBasicDataForm extends BaseFormBean {

    @EJB
    CurrencyOperationService currencyOperationService;
    @EJB
    AutoCompleteService autoCompleteService;

    private List<InvOrganizationSite> organizationList;
    private List<InvOrganizationSiteDTO> organizationSiteDTOList;

    private InvOrganizationSiteConverter invOrganizationSiteConverter;
    private InvOrganizationSiteDTOConverter invOrganizationSiteDTOConverter;

//    public BigDecimal updateRate(CurrencyDTO currency, Date date) {
//
//        if (currency != null) {
//            if (date != null) {
//                CurrencyOperation currencyOperation = currencyOperationService.getRatesByDates(currency.getId(), date, getUserData().getCompany().getId());
//
//                return currencyOperation == null ? BigDecimal.ONE : currencyOperation.getRate();
//            } else {
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("ERROR"), getUserData().getUserDDs().get("MUST_SELECT_DATE")));
//            }
//        } else {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("ERROR"), getUserData().getUserDDs().get("NO_CURRENCY_ERROR")));
//        }
//        return null;
//    }
    public abstract void checkDate(Boolean dateType);

    public abstract void resetDateFrom();

    public abstract void resetDateTo();

    public void resetFinancailYearDate() {
        resetDateFrom();
        resetDateTo();
    }

    public void pushGlYearSelection() {
        resetFinancailYearDate();
        getUserData().setGlYear(getGlYearSelection());
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.getSessionMap().put("userLogInData", getUserData());
    }

    public void redirectFinancailYearPage() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            fc.getExternalContext().redirect("../base/financailyear.xhtml");
        } catch (IOException ex) {
//            Logger.getLogger(DeletedAndUpdatedGeneralJournalMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Boolean checkFinancailYear(Date date) {
        if ((date.before(getGlYearSelection().getDateFrom()) || date.after(getGlYearSelection().getDateTo()))) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار تاريخ يقع بين الفترة الماليه"));
            return true;
        }
        return false;
    }

    public abstract void calculateLocalValue();

    public <T> void fillReport(HashMap hashMap, String reportPath, List<T> listBean, String exportType) throws JRException, IOException {
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listBean);
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, hashMap, beanCollectionDataSource);
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        if ("pdf".equals(exportType)) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
        } else if ("excel".equals(exportType)) {

        } else if ("html".equals(exportType)) {

        }
        httpServletResponse.getOutputStream().flush();
        httpServletResponse.getOutputStream().close();
    }

//    public List<InvOrganizationSite> completeCustomerFrom(String query) {
//        organizationList = autoCompleteService.autoCompleteOrganizationSite(getUserData().getUserBranch().getId(), Boolean.TRUE, 0, query);
//        invOrganizationSiteConverter = new InvOrganizationSiteConverter(organizationList);
//        return organizationList;
//
//    }
    public List<InvOrganizationSiteDTO> completeCustomerFrom(String query) {
        organizationSiteDTOList = autoCompleteService.autoCompleteOrganizationSiteDTO(getUserData().getUserBranch().getId(), Boolean.TRUE, 0, query,((getUserData().getGallaryDTOList() != null && !getUserData().getGallaryDTOList().isEmpty()) ? getUserData().getGallaryDTOList().get(0).getId() : null));
        invOrganizationSiteDTOConverter = new InvOrganizationSiteDTOConverter(organizationSiteDTOList);
        return organizationSiteDTOList;

    }

    /**
     * @return the organizationList
     */
    public List<InvOrganizationSite> getOrganizationList() {
        return organizationList;
    }

    /**
     * @param organizationList the organizationList to set
     */
    public void setOrganizationList(List<InvOrganizationSite> organizationList) {
        this.organizationList = organizationList;
    }

    /**
     * @return the invOrganizationSiteConverter
     */
    public InvOrganizationSiteConverter getInvOrganizationSiteConverter() {
        return invOrganizationSiteConverter;
    }

    /**
     * @param invOrganizationSiteConverter the invOrganizationSiteConverter to
     * set
     */
    public void setInvOrganizationSiteConverter(InvOrganizationSiteConverter invOrganizationSiteConverter) {
        this.invOrganizationSiteConverter = invOrganizationSiteConverter;
    }

    /**
     * @return the invOrganizationSiteDTOConverter
     */
    public InvOrganizationSiteDTOConverter getInvOrganizationSiteDTOConverter() {
        return invOrganizationSiteDTOConverter;
    }

    /**
     * @param invOrganizationSiteDTOConverter the
     * invOrganizationSiteDTOConverter to set
     */
    public void setInvOrganizationSiteDTOConverter(InvOrganizationSiteDTOConverter invOrganizationSiteDTOConverter) {
        this.invOrganizationSiteDTOConverter = invOrganizationSiteDTOConverter;
    }

    /**
     * @return the organizationSiteDTOList
     */
    public List<InvOrganizationSiteDTO> getOrganizationSiteDTOList() {
        return organizationSiteDTOList;
    }

    /**
     * @param organizationSiteDTOList the organizationSiteDTOList to set
     */
    public void setOrganizationSiteDTOList(List<InvOrganizationSiteDTO> organizationSiteDTOList) {
        this.organizationSiteDTOList = organizationSiteDTOList;
    }

}
