/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.print;

import com.isag.businessservice.reports.entityBean.completionRateReportEntity;
import com.isag.businessservice.reports.entityBean.rscExtractJournalReportPrintEntity;
import com.isag.businessservice.reports.searchBean.RscContractExtractionWorkunitSearchBean;
import com.isag.dto.InvInvoiceDTO;
import com.isag.dto.RscContractRegisterDTO;
import com.isag.dto.RscExtractRegisterDTO;
import com.isag.dto.RscPriceTenderDTO;
import com.isag.dto.RscQotationPriceTenderDTO;
import com.isag.dto.RscRestrictionQuantitiesDTO;
import com.isag.dto.RscTenderDTO;
import com.isag.isag.UserData;
import com.isag.report.entity.RscPriceTenderBean;
import com.isag.report.entity.RscRestrictionQuantitiesBean;
import com.isag.report.entity.RscTenderBean;
import com.isag.report.entity.RscContractRegisterBean;
import com.isag.report.entity.RscExtractRegisteringBean;
import com.isag.report.entity.RscQuotationPriceTenderBean;
import java.util.List;

/**
 *
 * @author hq004
 */
public interface IReportService {

    public void printReportInvoice(InvInvoiceDTO invPurchaseInvoiceDTO, UserData userData);

    public void printRscTender(RscTenderDTO rscTenderDTO, List<RscTenderBean> rscTenderBeanList, UserData userData);

    public void printRscPriceTender(RscPriceTenderDTO rscPriceTenderDTO, List<RscPriceTenderBean> rscPriceTenderBeanList, UserData userData);

    public void printRscContractRegister(RscContractRegisterDTO rscContractRegisterDTO, List<RscContractRegisterBean> rscContractRegisterBeanList, UserData userData);

    public void printRscRestrictionQuantities(RscRestrictionQuantitiesDTO rscRestrictionQuantitiesDTO, List<RscRestrictionQuantitiesBean> rscRestrictionQuantitiesBeanList, UserData userData);

    public void printRscExtractRegister(RscExtractRegisterDTO rscExtractRegisterDTO, List<RscExtractRegisteringBean> rscExtractionDTOBeanList, UserData userData, String url);

    public void printRscQuotationPriceTender(RscQotationPriceTenderDTO rscQuotationPriceTenderDTO, List<RscQuotationPriceTenderBean> RscQuotationPriceTenderBeanList, UserData userData);

    public void printRscExtractJournalReport(RscContractExtractionWorkunitSearchBean contractExtractionWorkunitSearchBean, List<rscExtractJournalReportPrintEntity> printEntityList, UserData userData);

    public void printCompletionRateReportMB(RscContractExtractionWorkunitSearchBean contractExtractionWorkunitSearchBean, completionRateReportEntity entity, List<completionRateReportEntity> printEntityList, UserData userData);
}
