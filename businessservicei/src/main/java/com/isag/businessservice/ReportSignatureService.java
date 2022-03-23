/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.ReportSignatureDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author ahmed
 */
@Remote
public interface ReportSignatureService {

    public ReportSignatureDTO addReportSignature(ReportSignatureDTO reportSignatureDTO, IsagUser isagUser);

    public void deleteReportSignatureDTO(ReportSignatureDTO reportSignatureDTO, IsagUser isagUser);

    public List<ReportSignatureDTO> getAllReportSignatureDTOByBranchId(Integer branchId);

    public ReportSignatureDTO getReportSignatureDTOByIdANDBranchId(Integer Id, Integer branchId);

    public Boolean ReportSignatureAleardyExist(ReportSignatureDTO reportSignatureDTO);

    public ReportSignatureDTO getReportSignatureDTOByScreenANDBranchId(Integer screen, Integer branchId);
}
