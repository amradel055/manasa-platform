/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.GlAnnualClosing;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author ahmed
 */
@Remote
public interface GlAnnualClosingService {

    public List<GlAnnualClosing> findGlAnnualClosingListByBranch(Integer branchId, Integer glYearId);

    public void updateGlAnuualList(List<GlAnnualClosing> glAnnualClosingsUpdated, List<GlAnnualClosing> glAnnualClosingsDeleted);
}
