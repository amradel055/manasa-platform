package com.toby.printProductionTransaction;

import com.toby.businessservice.InvPurchaseInvoiceService;
import com.toby.businessservice.ProProductionStagesService;
import com.toby.businessservice.ProProductionTransactionService;
import com.toby.converter.InvPurchaseInvoiceDTOConverter;
import com.toby.converter.ProProductionStagesDTOConverter;
import com.toby.converter.ProProductionTransactionDTOConverter;
import com.toby.dto.InvInventoryTransactionDetailDTO;
import com.toby.dto.InvPurchaseInvoiceDTO1;
import com.toby.dto.PrintProductionDTO;
import com.toby.dto.ProProductionStagesDTO;
import com.toby.dto.ProProductionTransactionDTO;
import com.toby.toby.AutoComplete;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import com.toby.views.InvoiceMovementReport;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "printProductionTransaction")
@ViewScoped
public class PrintProductionTransactionMB extends BaseListBean {

    private List<ProProductionStagesDTO> proProductionStagesDTOList;
    private List<ProProductionTransactionDTO> proProductionTransactionDTOList;
    private List<InvPurchaseInvoiceDTO1> invPurchaseInvoiceDTOList;
    private List<ProProductionTransactionDTO> proProductionTransactionDTOs;
    private PrintProductionDTO printProductionDTO;

    private ProProductionStagesDTOConverter proProductionStagesDTOConverter;
    private ProProductionTransactionDTOConverter proProductionTransactionDTOConverter;
    private InvPurchaseInvoiceDTOConverter invPurchaseInvoiceDTOConverter;
    @EJB
    private InvPurchaseInvoiceService invPurchaseInvoiceService;
    @EJB
    private ProProductionStagesService proProductionStagesService;
    @EJB
    private ProProductionTransactionService proProductionTransactionService;

    public PrintProductionTransactionMB() {
    }

    @Override
    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        proProductionStagesDTOList = new ArrayList<>();
        proProductionTransactionDTOList = new ArrayList<>();
        proProductionTransactionDTOs = new ArrayList<>();
        getProProductionStagesDTOList();
        getProProductionTransactionDTOList();
        getProProductionTransactionDTOs();
        getInvPurchaseInvoiceDTOList();
    }

    public List<ProProductionStagesDTO> completeProProductionId(String query) {

        return AutoComplete.completeProProductionId(query, proProductionStagesDTOList, proProductionStagesDTOConverter);
    }

    @Override
    public void delete() {
    }

    @Override
    public void filter() {
    }

    @Override
    public String goToAdd() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToEdit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void load() {
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<ProProductionTransactionDTO> completeInvpurchaseInvoicefromproductionTrans(String query) {
        return AutoComplete.completeInvpurchaseInvoicefromproductionTrans(query, proProductionTransactionDTOList, proProductionTransactionDTOConverter);

    }

    public void search() {
        if ((printProductionDTO.getFromInvoice() != null && printProductionDTO.getToInvoice() != null)
                || (printProductionDTO.getFromDate() != null && printProductionDTO.getToDate() != null)
                || (printProductionDTO.getFromStatus() != null) && (printProductionDTO.getToStatus() != null)) {

            setProProductionTransactionDTOs(proProductionTransactionService.findBy(printProductionDTO, getUserData().getUserBranch().getId()));
            for (ProProductionTransactionDTO x : getProProductionTransactionDTOs()) {
                ProProductionStagesDTO stagesDTO = proProductionTransactionService.findByProProduction(x);
                x.setStagePrice(stagesDTO.getPrice());
                x.setQuantity(BigDecimal.ZERO);
                List<InvInventoryTransactionDetailDTO> y = proProductionTransactionService.getInventoryDetailById(getUserData().getUser(), x.getInvPurchaseInvoiceId());
                for (InvInventoryTransactionDetailDTO dTO : y) {
                    x.setQuantity(x.getQuantity().add(dTO.getQuantity()));
                }

                x.setTotal(x.getStagePrice().multiply(x.getQuantity()));
            }
            setProProductionTransactionDTOs(getProProductionTransactionDTOs());
        } else {
            printProductionDTO.setMsg("ادخل من فاتوره الي فاتوره او من تاريخ الي تاريخ او من مرحله الي مرحله  ");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", printProductionDTO.getMsg()));

        }
    }

    public List<InvPurchaseInvoiceDTO1> completeCostCenter(String query) {
        return AutoComplete.completeInvpurchaseInvoice(query, invPurchaseInvoiceDTOList, invPurchaseInvoiceDTOConverter);
    }

    public List<InvPurchaseInvoiceDTO1> getInvPurchaseInvoiceDTOList() {
        if (invPurchaseInvoiceDTOList == null || invPurchaseInvoiceDTOList.isEmpty()) {
            invPurchaseInvoiceDTOList = invPurchaseInvoiceService.findInvPurchaseInvoiceDTOList(getUserData().getUser());
//            setInvPurchaseInvoiceDTOConverter(new InvPurchaseInvoiceDTOConverter(invPurchaseInvoiceDTOList));
        }
        return invPurchaseInvoiceDTOList;
    }

    /**
     * @param invPurchaseInvoiceDTOList the invPurchaseInvoiceDTOList to set
     */
    public void setInvPurchaseInvoiceDTOList(List<InvPurchaseInvoiceDTO1> invPurchaseInvoiceDTOList) {
        this.invPurchaseInvoiceDTOList = invPurchaseInvoiceDTOList;
    }

    public List<ProProductionStagesDTO> getProProductionStagesDTOList() {
        if (proProductionStagesDTOList == null | proProductionStagesDTOList.isEmpty()) {
            proProductionStagesDTOList = proProductionStagesService.getAllProproductionStages(getUserData().getUser());
        }
        return proProductionStagesDTOList;
    }

    public void setProProductionStagesDTOList(List<ProProductionStagesDTO> proProductionStagesDTOList) {
        this.proProductionStagesDTOList = proProductionStagesDTOList;
    }

    public List<ProProductionTransactionDTO> getProProductionTransactionDTOList() {
        if (proProductionTransactionDTOList == null | proProductionTransactionDTOList.isEmpty()) {
            proProductionTransactionDTOList = proProductionTransactionService.getAll(getUserData().getUser());
            //setProProductionTransactionDTOConverter(new ProProductionTransactionDTOConverter(proProductionTransactionDTOList));
        }
        return proProductionTransactionDTOList;
    }

    public void setProProductionTransactionDTOList(List<ProProductionTransactionDTO> proProductionTransactionDTOList) {
        this.proProductionTransactionDTOList = proProductionTransactionDTOList;
    }

    public ProProductionStagesDTOConverter getProProductionStagesDTOConverter() {
        return proProductionStagesDTOConverter;
    }

    public void setProProductionStagesDTOConverter(ProProductionStagesDTOConverter proProductionStagesDTOConverter) {
        this.proProductionStagesDTOConverter = proProductionStagesDTOConverter;
    }

    public ProProductionTransactionDTOConverter getProProductionTransactionDTOConverter() {
        return proProductionTransactionDTOConverter;
    }

    public void setProProductionTransactionDTOConverter(ProProductionTransactionDTOConverter proProductionTransactionDTOConverter) {
        this.proProductionTransactionDTOConverter = proProductionTransactionDTOConverter;
    }

    public PrintProductionDTO getPrintProductionDTO() {
        if (printProductionDTO == null) {
            printProductionDTO = new PrintProductionDTO();
        }
        return printProductionDTO;
    }

    public void setPrintProductionDTO(PrintProductionDTO printProductionDTO) {
        this.printProductionDTO = printProductionDTO;
    }

    public List<ProProductionTransactionDTO> getProProductionTransactionDTOs() {
//        if (proProductionTransactionDTOs == null | proProductionTransactionDTOs.isEmpty()) {
//            proProductionTransactionDTOs = proProductionTransactionService.findBy(printProductionDTO.getFromStatus(), printProductionDTO.getToStatus(), printProductionDTO.getFromInvoice(), printProductionDTO.getToInvoice());
//        }
        return proProductionTransactionDTOs;
    }

    public void setProProductionTransactionDTOs(List<ProProductionTransactionDTO> proProductionTransactionDTOs) {
        this.proProductionTransactionDTOs = proProductionTransactionDTOs;
    }

    public InvPurchaseInvoiceDTOConverter getInvPurchaseInvoiceDTOConverter() {
        return invPurchaseInvoiceDTOConverter;
    }

    public void setInvPurchaseInvoiceDTOConverter(InvPurchaseInvoiceDTOConverter invPurchaseInvoiceDTOConverter) {
        this.invPurchaseInvoiceDTOConverter = invPurchaseInvoiceDTOConverter;
    }

}
