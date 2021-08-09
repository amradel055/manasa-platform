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
 * @author ahmed
 */
@Remote
public interface HosPatientRegistrationService {

    public List<HosPatientRegistration> getAllHosPatientRegistration();

    public void deleteHosPatientRegistration(HosPatientRegistration hosPatientRegistration);

    public HosPatientRegistration updateHosPatientRegistration(HosPatientRegistration hosPatientRegistration);

    public HosPatientRegistration addHosPatientRegistration(HosPatientRegistration hosPatientRegistration);

    public HosPatientRegistration findHosPatientRegistration(Integer id);

    public List<HosPatientRegistration> getAllHosPatientRegistrationByCompanyId(Integer companyId);

    public List<HosPatientRegistration> getAllHosPatientRegistrationByBranchId(Integer branchId, Integer type);

}
