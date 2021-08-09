/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.GlYearDTO;
import com.isag.entity.Branch;
import com.isag.entity.GlYear;
import com.isag.entity.IsagCompany;
import com.isag.entity.IsagUserRole;
import com.isag.entity.IsagUserYear;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface IsagUserYearService {

    public void deleteIsagUserYear(IsagUserYear isagUserYear);

    public List<IsagUserYear> getAllIsagUserYear();

    public IsagUserYear addIsagUserYear(IsagUserYear isagUserYear);

    public IsagUserYear findIsagUserYear(Integer isagUserYearId);

    public IsagUserYear updateIsagUserYear(IsagUserYear isagUserYear);
    
    public List<IsagUserYear> findIsagUserYearByUserId(Integer userid);
    
    public IsagUserYear findIsagUserYearByUserIdAndCompany(Integer userid, Integer companyId);
    
    public List<IsagUserYear> findIsagUserYearByCompany(Integer companyId);
    
    public List<GlYear> findYearByUserId(Integer userId);
    
    public List<GlYear> findYearByUserId(Integer userId , Branch branchId);
    
    public List<GlYear> findYearByUserId(Integer userId , IsagCompany companyId);
    
    public void DeleteYear(IsagUserRole userRole , GlYear year);
    
    public List<IsagUserYear> findIsagUserYearByUserIdAndBranchId(Integer userId , Branch branch);
    
    public void deleteIsagUserYear(List<IsagUserYear> isagUserYearList);
    
    public List<GlYearDTO> findYearDTOByUserId(Integer userId , Branch branchId);
    
    public GlYearDTO findYearDTOByUserIdAndDate(Integer userId , Branch branchId,Date date);
}
