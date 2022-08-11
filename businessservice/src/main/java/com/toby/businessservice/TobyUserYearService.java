/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.GlYearDTO;
import com.toby.entity.Branch;
import com.toby.entity.GlYear;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUserRole;
import com.toby.entity.TobyUserYear;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface TobyUserYearService {

    public void deleteTobyUserYear(TobyUserYear tobyUserYear);

    public List<TobyUserYear> getAllTobyUserYear();

    public TobyUserYear addTobyUserYear(TobyUserYear tobyUserYear);

    public TobyUserYear findTobyUserYear(Integer tobyUserYearId);

    public TobyUserYear updateTobyUserYear(TobyUserYear tobyUserYear);

    public List<TobyUserYear> findTobyUserYearByUserId(Integer userid);

    public TobyUserYear findTobyUserYearByUserIdAndCompany(Integer userid, Integer companyId);

    public List<TobyUserYear> findTobyUserYearByCompany(Integer companyId);

    public List<GlYear> findYearByUserId(Integer userId);

    public List<GlYear> findYearByUserId(Integer userId, Branch branchId);

    public List<GlYear> findYearByUserId(Integer userId, TobyCompany companyId);

    public void DeleteYear(TobyUserRole userRole, GlYear year);

    public List<TobyUserYear> findTobyUserYearByUserIdAndBranchId(Integer userId, Branch branch);

    public void deleteTobyUserYear(List<TobyUserYear> tobyUserYearList);

    public List<GlYearDTO> findYearDTOByUserId(Integer userId, Integer branchId);

}
