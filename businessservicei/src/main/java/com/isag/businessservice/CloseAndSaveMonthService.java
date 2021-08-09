/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.entity.CloseAndSaveMonth;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface CloseAndSaveMonthService {

    public void deleteCloseAndSaveMonth(CloseAndSaveMonth CloseAndSaveMonth);

    public List<CloseAndSaveMonth> getAllCloseAndSaveMonths();

    public CloseAndSaveMonth addCloseAndSaveMonth(CloseAndSaveMonth CloseAndSaveMonth);

    public CloseAndSaveMonth findCloseAndSaveMonth(Integer CloseAndSaveMonthId);

    public List<CloseAndSaveMonth> findCloseAndSaveMonth(Integer monthId, Integer monthNumber, Integer yearId, Integer branchId);

    public CloseAndSaveMonth updateCloseAndSaveMonth(CloseAndSaveMonth CloseAndSaveMonth);

    public List<CloseAndSaveMonth> getAllCloseAndSaveMonthsByBranchId(Integer branchI);

    public List<CloseAndSaveMonth> getCloseAndSaveMonthsByYearIdAndMounthNumberAndBranchId(Integer branchId, Integer yearId, Integer monthNumber);

     public boolean isCloseAndSaveMonthsByYearIdAndMounthNumberAndBranchIdExist(Integer branchId, Integer yearId, Integer monthNumber);
}
