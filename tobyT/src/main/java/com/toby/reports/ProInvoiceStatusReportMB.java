package com.toby.reports;

import com.toby.businessservice.InvPurchaseInvoiceService_1;
import com.toby.businessservice.ProFactoryDeliveryService;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.PrintProductionDTO;
import com.toby.toby.Basic;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "proInvoiceStatusReportMB")
@ViewScoped
public class ProInvoiceStatusReportMB extends Basic {

    private PrintProductionDTO printProductionDTO;

    private List<InvInvoiceDTO> list;

    private Integer number;

    @EJB
    private InvPurchaseInvoiceService_1 invPurchaseInvoiceService;
    @EJB
    private ProFactoryDeliveryService proFactoryDeliveryService;

    public void search() {
        if (printProductionDTO.getInvoiceStatus() == 4) {
            setList(invPurchaseInvoiceService.getALLInvPurchaseInvoiceDTOByStatus(getPrintProductionDTO(), getUserData().getUserBranch().getId()));
        }
        if (printProductionDTO.getInvoiceStatus() == 10) {
            setList(proFactoryDeliveryService.getALLProFactoryDeliveryDTOByStatusRemain(getPrintProductionDTO(), getUserData().getUserBranch().getId()));
        } else {
            setList(proFactoryDeliveryService.getALLProFactoryDeliveryDTOByStatus(getPrintProductionDTO(), getUserData().getUserBranch().getId()));
        }
        setNumber(getList() == null ? 0 : getList().size());
    }

    /**
     * @return the printProductionDTO
     */
    public PrintProductionDTO getPrintProductionDTO() {
        if (printProductionDTO == null) {
            printProductionDTO = new PrintProductionDTO();
        }
        return printProductionDTO;
    }

    /**
     * @param printProductionDTO the printProductionDTO to set
     */
    public void setPrintProductionDTO(PrintProductionDTO printProductionDTO) {
        this.printProductionDTO = printProductionDTO;
    }

    /**
     * @return the list
     */
    public List<InvInvoiceDTO> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(List<InvInvoiceDTO> list) {
        this.list = list;
    }

    /**
     * @return the number
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(Integer number) {
        this.number = number;
    }
}
