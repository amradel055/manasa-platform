/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.searchBean.GroupDataReportSearchBean;

import com.isag.entity.InvGroup;

import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hhhh
 */
@Remote
public interface GroupDataReportService {

   public List<InvGroup> getAllGroupsSearchBean(GroupDataReportSearchBean groupDataReportSearchBean);
  


}
