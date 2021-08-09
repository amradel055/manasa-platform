/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.JournalDocumentsReportSearchBean;
import com.isag.core.GenericDAO;
import com.isag.entity.GeneralJournal;
import com.isag.entity.GeneralJournalDetails;
import com.isag.entity.HistoryGeneralJournal;
import com.isag.entity.HistoryGeneralJournalDetails;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq004
 */
@Stateless
public class HistoryGeneraljournalServiceImpl implements HistoryGeneraljournalService {

    @EJB
    private GenericDAO dao;


}
