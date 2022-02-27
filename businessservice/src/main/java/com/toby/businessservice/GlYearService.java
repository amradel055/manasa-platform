/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.GlYear;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface GlYearService {

    public void deleteYear(GlYear glYear);

    public List<GlYear> getAllYear();

    public GlYear addYear(GlYear glYear);

    public GlYear findYear(Integer yearId);

    public GlYear updateYear(GlYear glYear);

    public List<GlYear> getALLGlyearByCompanyId(Integer companyId);

    public List<GlYear> getALLGlyearByBranchId(Integer branchId);

    public GlYear getDefaultYearsByBranchId(Integer branchId, GlYear newGLYear);

    public GlYear getDefaultYearsByBranchIdByUser(Integer userId, Integer branchId);

    public List<GlYear> getSimilarYearsByBranchId(Integer branchId, GlYear newGLYear);

    public List<GlYear> checkDateWithinDate(Integer branchId, GlYear newGLYear);

    public List<GlYear> findNextGlYear(Integer branchId, GlYear gLYear);

    public List<GlYear> findGlYearWithCode(Integer branchId, Integer year);

    public List<GlYear> findGlYearByDateBetween(Integer branchId, Date date);

}
