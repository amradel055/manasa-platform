package com.isag.businessservice;

import com.isag.define.ScreenNameClassEnum;
import com.isag.dto.InvDetailDTO;
import com.isag.dto.InvInvoiceDTO;
import com.isag.entity.InvStripDetail;
import com.isag.entity.IsagUser;

import javax.ejb.Remote;
import java.util.List;

/**
 * @author khaled
 */
@Remote
public interface InvStripDetailService {

    void addInvStripDetails(InvStripDetail details);

    void deleteInvStripDetails(InvStripDetail details);

    InvStripDetail updateInvStripDetails(InvStripDetail details);

    List<InvStripDetail> getAllInvStripDetails();

    List<InvStripDetail> getAllInvStripDetails(Integer id);

    public List<InvDetailDTO> getAllInvTransferDetailsByInvPurchaseInvoiceIdDTO(InvInvoiceDTO invInvoiceDTO, Integer branchId);

    public void deleteInvTransferDetailListDTO(List<InvDetailDTO> invDetailDTOList);

    public String addInvStripDetailsDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList, InvInvoiceDTO invInvoiceDTO, ScreenNameClassEnum screenNameClassEnum, IsagUser isagUser);
}
