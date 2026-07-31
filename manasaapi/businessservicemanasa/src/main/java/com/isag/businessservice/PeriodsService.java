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

    public List<PeriodsDTO> findPeriodsV2(Integer branchId, Integer teacherId);

    public PeriodsDTO save(PeriodsDTO periodsDTO, IgmaUser isagUser);

    public void savePeriods(PeriodsDTO periodsDTO, IgmaUser igmaUser);

    public void deletePeriods(Integer periodsDTO);

    public List<PeriodsDTO> findPeriodsByMaterialId(Integer materialId);

    public PeriodsDTO findPeriodsById(Integer periodsId);

    public List<Integer> findPeriodAndChildIds(List<Integer> periodParentIds);
}
