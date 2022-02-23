/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.searchBean.HosDoctorSearchBean;
import com.isag.views.HosClinicView;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author ahmed
 */
@Remote
public interface HosClinicViewService {

    public List<HosClinicView> getHosClinicViewBySearchBean(HosDoctorSearchBean hosDoctorSearchBean);
    
    public List<HosClinicView> getHosClinicViewBySearchBeanOrderedBy(HosDoctorSearchBean hosDoctorSearchBean,String orderBy);
    
    public List<HosClinicView> getTotalsBySearchBeanGroupedBy(HosDoctorSearchBean hosDoctorSearchBean,String groupBy);
}
