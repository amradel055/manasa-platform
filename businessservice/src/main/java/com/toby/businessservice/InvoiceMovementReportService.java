package com.toby.businessservice;

import com.toby.dto.PrintProductionDTO;
import com.toby.views.InvoiceMovementReport;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface InvoiceMovementReportService {

    public List<InvoiceMovementReport> searchReport(PrintProductionDTO printProductionDTO, Integer branchId, Integer invoiceType);

    public BigDecimal summitionTob(Integer id);

    public BigDecimal summitionTob(Integer id, Integer type);

    public List<InvoiceMovementReport> searchReportAPI(Integer serial, Integer branchId);

}
