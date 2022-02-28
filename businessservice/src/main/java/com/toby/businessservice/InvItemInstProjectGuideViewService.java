/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.views.InvItemInstProjectGuideView;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author AhmedEssam
 */
@Remote
public interface InvItemInstProjectGuideViewService {
    
     public List<InvItemInstProjectGuideView> findInvItemInstProjectGuideView(Integer branchId) ;
    
     public InvItemInstProjectGuideView findInvItemInstProjectGuideViewBytableId(Integer branchId ,Integer tableType,Integer tableId);
}
