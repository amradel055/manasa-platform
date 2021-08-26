/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.PeriodsDTO;
import com.isag.entity.IgmaUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface PeriodsService {

    public List<PeriodsDTO> findPeriodsByTeacherId(Integer teacherId);

    public List<PeriodsDTO> findPeriods(Integer branchId);

    public PeriodsDTO save(PeriodsDTO periodsDTO, IgmaUser isagUser);

    public void deletePeriods(PeriodsDTO periodsDTO);

    public void deletePeriods(Integer periodsDTO);
}
