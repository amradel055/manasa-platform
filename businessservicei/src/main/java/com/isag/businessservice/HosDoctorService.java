/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.HosDoctorSearchBean;
import com.isag.entity.HosDoctor;
import com.isag.entity.HosDoctor;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface HosDoctorService {

    public List<HosDoctor> getAllHosDoctorRoots();

    public List<HosDoctor> getCompanyHosDoctorRoots(Integer companyId);

    public List<HosDoctor> getAllHosDoctors(Integer branchId);

    public List<HosDoctor> getCompanyHosDoctors(Integer companyId);

    public void deleteHosDoctor(HosDoctor selectedCS);

    public HosDoctor addHosDoctor(HosDoctor newHosDoctor);

    public void saveHosDoctor(HosDoctor newHosDoctor);

    public HosDoctor findHosDoctor(Integer costCenterId);

    public HosDoctor findHosDoctorByCode(Integer parentCode, Integer branchId);

    public List<HosDoctor> getBranchHosDoctorsActive(Integer id);

    public List<HosDoctor> getAllSubHosDoctorByBranchIdActive(Integer id);

    public List<HosDoctor> getActiveSubHosDoctorsByBranch(Integer branchId);

    public List<HosDoctor> getBranchWithoutParentHosDoctors(Integer branchId);

    public List<HosDoctor> getBranchHosDoctorRoots(Integer id);

    public List<HosDoctor> getAllHosDoctorsIndex(HosDoctorSearchBean costCenterSearchBean);

    public List<HosDoctor> getAllHosDoctorByBranchIdAndAccNumOrShotCode(Integer selectedBranchId, String HosDoctorCode);

    public List<HosDoctor> getAllHosDoctorByBranchIdAndAccNumOrShotCode(Integer selectedBranchId, String HosDoctorCode, Integer HosDoctorId);

    public List<HosDoctor> getAllSubHosDoctorByBranchIdActiveAndHosDoctorId(Integer costCenterId);

    public List<HosDoctor> getAllSubHosDoctorByBranchIdActiveAndLevel(Integer branchId, Integer level);
    
    public List<HosDoctor> getAllSubHosDoctorByBranchIdActiveAndLevelAndClinicId(Integer branchId, Integer level , Integer clinicId);
    
    public List<HosDoctor> getAllServicesByDoctorIdAndBranchId(Integer branchId, Integer doctorId);
}
