/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.HrSponsorDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;
/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
@Remote
public interface HrSponsorService {

    public HrSponsorDTO findHrSponsorByEmployeeId(Integer employeeId);

    public void deleteHrSponsor(HrSponsorDTO sponsorDTO, IsagUser isagUser);

    public HrSponsorDTO addHrSponsor(HrSponsorDTO hrSponsorDTO, IsagUser isagUser);

    public List<HrSponsorDTO> getSponsorsByCompanyId(Integer companyId);

}
