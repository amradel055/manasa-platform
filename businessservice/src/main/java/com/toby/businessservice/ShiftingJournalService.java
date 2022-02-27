/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.GeneralJournal;
import com.toby.entity.GeneralJournalDetails;
import com.toby.entity.GlYear;
import com.toby.views.PostRestrictionsView;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author ahmed
 */
@Remote
public interface ShiftingJournalService {
    
    public void saveHeadJournalAndDetails(List<GeneralJournal> generalJournalList,Map<Integer, List<GeneralJournalDetails>> generalJournalDetailsMap ,Map<Integer, PostRestrictionsView> postRestricitonViewMap, GlYear glYearSelection,Integer fillGeneralJournalDetailsFlow, Integer companyId,Boolean cancelInstallment);
}