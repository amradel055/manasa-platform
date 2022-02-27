/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.PrintProProductDTO;
import com.toby.entity.TobyUser;
import com.toby.views.ProProductionMovementReport;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author H
 */
@Remote
public interface ProProductionMovementReportService {
    public  List<ProProductionMovementReport> getProProductionMovementReportList(PrintProProductDTO printProProductDTO,TobyUser tobyUser);
}
