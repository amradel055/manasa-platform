/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.entity.InstContract;
import com.isag.entity.InstContractDetail;
import com.isag.views.InstPurchaseInvoiceContractDifrranceView;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author AhmedEssam
 */
@Stateless
public class InstContractServicenewImpl implements InstContractServicenew {

    @EJB
    private GenericDAO dao;
    @EJB
    InstContractDetailsService instContractDetailsService;
    @EJB
    InstPurchaseInvoiceContractDifrranceViewService instPurchaseInvoiceContractDifrranceViewService;

    @Override
    public synchronized InstContract addInstContract(InstContract instContract, List<InstContractDetail> instContractDetailList, List<InstContractDetail> instContractDetailListDeleted, Map<String, String> userDDs) {
        boolean validate = true;
        if (instContract.getProjectId() != null) {
            InstPurchaseInvoiceContractDifrranceView instPurchaseInvoiceContractDifrranceView = instPurchaseInvoiceContractDifrranceViewService.getAllInstPurchaseInvoiceContractDifrranceByType(instContract.getBranchId().getId(), instContract.getProjectId().getId(), 1);
            if (instPurchaseInvoiceContractDifrranceView != null && instPurchaseInvoiceContractDifrranceView.getQuantity().compareTo(instContract.getAmount()) == -1) {
                validate = false;
            } else if (instPurchaseInvoiceContractDifrranceView == null && getOldAmount(instContract.getId()).compareTo(instContract.getAmount()) == -1) {
                validate = false;
            }

        }
        if (instContract.getInvItemId() != null) {
            InstPurchaseInvoiceContractDifrranceView instPurchaseInvoiceContractDifrranceView = instPurchaseInvoiceContractDifrranceViewService.getAllInstPurchaseInvoiceContractDifrranceByType(instContract.getBranchId().getId(), instContract.getInvItemId().getId(), 0);
            if (instPurchaseInvoiceContractDifrranceView != null && instPurchaseInvoiceContractDifrranceView.getQuantity().compareTo(instContract.getAmount()) == -1) {
                validate = false;
            } else if (instPurchaseInvoiceContractDifrranceView == null && getOldAmount(instContract.getId()).compareTo(instContract.getAmount()) == -1) {
                validate = false;
            }

        }
        if (validate) {
            if (instContract.getId() != null) {
                dao.updateEntity(instContract);
            } else {
                if (instContract.getSerial() == null) {
                    instContract.setSerial(getMaxSerail(instContract) != null ? getMaxSerail(instContract) + 1 : 1);
                } else {
                    if (checkContractSerial(instContract)) {
                        return instContract;
                    }
                }
//                instContract.setSerial(getMaxSerail(instContract) != null ? getMaxSerail(instContract) + 1 : 1);
                dao.saveEntity(instContract);
            }

            if (instContractDetailListDeleted != null && !instContractDetailListDeleted.isEmpty()) {
                for (InstContractDetail deleted : instContractDetailListDeleted) {
                    instContractDetailsService.deleteInstContractDetails(deleted);
                }
            }

            Integer maxSerial = 0;
            if (instContract != null && instContract.getId() != null) {
                maxSerial = instContractDetailsService.finMaxSerial(instContract.getId());
            } else {
                maxSerial++;
            }
            for (InstContractDetail instContractDetail : instContractDetailList) {
                instContractDetail.setInstContractId(instContract);

                if (instContractDetail.getId() != null) {
                    instContractDetailsService.updateInstContractDetails(instContractDetail);
                } else {
                    instContractDetail.setSerial(maxSerial);
                    instContractDetailsService.addInstContractDetails(instContractDetail);
                    maxSerial++;
                }
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب وضع عدد اقل من الكميه او تساويها"));
        }
        return instContract;
    }

    @Override
    public boolean checkContractSerial(InstContract instContract) {
        if (instContract.getSerial() != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("branchId", instContract.getBranchId() != null ? instContract.getBranchId().getId() : null);
            params.put("serial", instContract.getSerial());
            Integer s = dao.findEntityByQuery(" SELECT e.serial FROM InstContract e WHERE e.branchId.id = :branchId and e.serial = :serial ", params);
            if (s != null) {
                instContract.setMsg("هذا السيريال موجود مسبقا برجاء ادخال سيريال اخر");
                return true;
            }
        }
        return false;
    }

    @Override
    public Integer getMaxSerail(InstContract instContract) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", instContract.getBranchId() != null ? instContract.getBranchId().getId() : null);
        return dao.findEntityByQuery(" SELECT MAX(e.serial) FROM InstContract e WHERE e.branchId.id = :branchId", params);
    }

    public BigDecimal getOldAmount(Integer id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return dao.findEntityByQuery(" SELECT e.amount FROM InstContract e WHERE e.id = :id", params);
    }
}
