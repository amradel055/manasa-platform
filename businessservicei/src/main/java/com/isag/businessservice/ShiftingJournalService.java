/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.entity.GeneralJournal;
import com.isag.entity.GeneralJournalDetails;
import com.isag.entity.GlYear;
import com.isag.views.PostRestrictionsView;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author ahmed
 */
@Remote
public interface ShiftingJournalService {
    
    public String saveHeadJournalAndDetails(List<GeneralJournal> generalJournalList,Map<Integer, List<GeneralJournalDetails>> generalJournalDetailsMap ,Map<Integer, PostRestrictionsView> postRestricitonViewMap, GlYear glYearSelection,Integer fillGeneralJournalDetailsFlow, Integer companyId,Boolean cancelInstallment);
}