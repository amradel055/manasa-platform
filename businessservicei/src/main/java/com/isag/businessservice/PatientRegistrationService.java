/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.entity.HosPatientRegistration;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface PatientRegistrationService {

    public HosPatientRegistration addPatientRegistration(HosPatientRegistration hosPatientRegistration);

    public HosPatientRegistration updatePatientRegistration(HosPatientRegistration hosPatientRegistration);

    public void deletePatientRegistration(HosPatientRegistration hosPatientRegistration);

    public HosPatientRegistration findPatientRegistrationById(Integer hosPatientRegistrationId);

    public List<HosPatientRegistration> getPatientRegistrationByCompanyId(Integer companyId);

    public List<HosPatientRegistration> getPatientRegistrationByBranchId(Integer BranchId);

    public List<HosPatientRegistration> getPatientByBranchId(Integer BranchId);

    public List<HosPatientRegistration> getCompanyByBranchId(Integer BranchId);

    public Boolean checkCodeExistence(HosPatientRegistration hosPatientRegistration);

    public List<HosPatientRegistration> getAllHosPatientRegistrationByBranchId(Integer branchId, Integer type);

}
