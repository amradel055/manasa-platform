package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.define.ScreenNameClassEnum;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.InvTransferDTO;
import com.toby.dto.InvTransferDetailDTO;
import com.toby.dto.SymbolDTO;
import com.toby.entity.Branch;
import com.toby.entity.InvItem;
import com.toby.entity.InvTransfer;
import com.toby.entity.InvTransferDetail;
import com.toby.entity.Symbol;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import com.toby.views.ItemsBarcodesDetailsView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author khaled
 */
@Stateless
public class InvTransferDetailServiceImp implements InvTransferDetailService {

    @EJB
    private GenericDAO dao;
    @EJB
    private UnitsItemsService unitsItemsService;
    @EJB
    ItemsBarcodesDetailsViewService itemsBarcodesDetailsViewService;
    @EJB
    InvGlobalService invGlobalService;
    @EJB
    GenericService genericService;

    @Override
    public void addInvTransferDetails(InvTransferDetail details) {
        dao.saveEntity(details);
    }

    @Override
    public void deleteInvTransferDetails(InvTransferDetail details) {
        dao.deleteEntity(details);
    }

    @Override
    public InvTransferDetail updateInvTransferDetails(InvTransferDetail details) {
        return dao.updateEntity(details);
    }

    @Override
    public List<InvTransferDetail> getAllInvTransferDetails() {
        return dao.findAll(InvTransferDetail.class);
    }

    @Override
    public List<InvTransferDetail> getAllInvTransferDetails(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("transferId", id);
        return dao.findListByQuery("SELECT d FROM InvTransferDetail d WHERE d.invTransferId.id =:transferId", params);
    }

    @Override
    public InvTransferDetail findInvTransferDetailsById(Integer invTransferDetailsId) {
        return dao.findEntityById(InvTransferDetail.class, invTransferDetailsId);
    }

    @Override
    public List<InvTransferDetail> getAllInvTransferDetailsByInvTransferId(Integer invTransferId) {
        Map<String, Object> params = new HashMap<>();
        params.put("invTransferId", invTransferId);
        List<InvTransferDetail> details = dao.findListByQuery("SELECT e FROM InvTransferDetail e WHERE e.invTransferId.id = :invTransferId", params);
        return details;
    }

    @Override
    public List<InvTransferDetail> getAllInvTransferDetailsByInvTransferIdWithStatus(Integer invTransferId) {
        Map<String, Object> params = new HashMap<>();
        params.put("invTransferId", invTransferId);
        List<InvTransferDetail> details = dao.findListByQuery("SELECT e FROM InvTransferDetail e WHERE e.invTransferId.id = :invTransferId and (e.status != 2  OR e.status is null) ", params);
        return details;
    }

    public InvTransferDetail mapToEntity(InvTransferDetailDTO invTransferDetailDTO, TobyUser tobyUser) {
        InvTransferDetail invTransferDetail = new InvTransferDetail();
        invTransferDetail.setId(invTransferDetailDTO.getId());
        invTransferDetail.setCreationDate(invTransferDetailDTO.getCreatedDate());
        invTransferDetail.setAmount(invTransferDetailDTO.getAmount());
        invTransferDetail.setBalance(invTransferDetailDTO.getBalance());
        invTransferDetail.setFinalQuantity(invTransferDetailDTO.getFinalQuantity());
        invTransferDetail.setItemBarcode(invTransferDetailDTO.getItemBarcode());
        invTransferDetail.setStatus(invTransferDetailDTO.getStatus());
        invTransferDetail.setScrewing(invTransferDetailDTO.getScrewing());
        if (invTransferDetailDTO.getInvItemId() != null) {
            InvItem invItem = new InvItem();
            invItem.setId(invTransferDetailDTO.getInvItemId().getId());
            invItem.setName(invTransferDetailDTO.getInvItemId().getName());
            invItem.setCode(invTransferDetailDTO.getInvItemId().getCode());

            if (invTransferDetailDTO.getInvItemId().getUnitId() != null) {
                Symbol symbol = new Symbol();
                symbol.setId(invTransferDetailDTO.getInvItemId().getUnitId().getId());
                symbol.setName(invTransferDetailDTO.getInvItemId().getUnitId().getName());
                invTransferDetail.setUnitId(symbol);
            }

            invTransferDetail.setInvItemId(invItem);
        }
        if (invTransferDetailDTO.getUnitsItemsSelected() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invTransferDetailDTO.getUnitsItemsSelected());
            invTransferDetail.setUnitId(symbol);
        }
        if (invTransferDetailDTO.getInvTransferId() != null) {
            InvTransfer invTransfer = new InvTransfer();
            invTransfer.setId(invTransferDetailDTO.getInvTransferId().getId());
            invTransferDetail.setInvTransferId(invTransfer);
        }
        if (invTransferDetailDTO.getSelectedId() != null) {
            InvTransferDetail transferDetail = new InvTransferDetail();
            transferDetail.setId(invTransferDetailDTO.getSelectedId().getId());
            invTransferDetail.setSelectedId(transferDetail);
        }
        if (invTransferDetailDTO.getUnitId() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invTransferDetailDTO.getUnitId().getId());
            symbol.setName(invTransferDetailDTO.getUnitId().getName());
            invTransferDetail.setUnitId(symbol);
        }

        if (invTransferDetailDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(invTransferDetailDTO.getCreatedBy());
            invTransferDetail.setCreatedBy(user);
            invTransferDetail.setCreationDate(invTransferDetailDTO.getCreatedDate());
            invTransferDetail.setModifiedBy(tobyUser);
            invTransferDetail.setModificationDate(new Date());
            if (invTransferDetailDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(invTransferDetailDTO.getCompanyId());
                invTransferDetail.setCompanyId(company);
            }
            if (invTransferDetailDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(invTransferDetailDTO.getBranchId());
//                invTransferDetail.setBranchId(branch);
            }
        } else {
            invTransferDetail.setCreatedBy(tobyUser);
            invTransferDetail.setCreationDate(new Date());
            invTransferDetail.setCompanyId(tobyUser.getCompanyId());
//            invTransferDetail.setBranchId(tobyUser.getBranchId());

        }
        return invTransferDetail;

    }

    public InvTransferDetailDTO mapToDTO(InvTransferDetail invTransferDetail, TobyUser tobyUser) {

        InvTransferDetailDTO invTransferDetailDTO = new InvTransferDetailDTO();

        invTransferDetailDTO.setId(invTransferDetail.getId());
        invTransferDetailDTO.setCreatedDate(invTransferDetail.getCreationDate());
        invTransferDetailDTO.setAmount(invTransferDetail.getAmount());
        invTransferDetailDTO.setBalance(invTransferDetail.getBalance());
        invTransferDetailDTO.setFinalQuantity(invTransferDetail.getFinalQuantity());
        invTransferDetailDTO.setItemBarcode(invTransferDetail.getItemBarcode());
        invTransferDetailDTO.setStatus(invTransferDetail.getStatus());
        invTransferDetailDTO.setScrewing(invTransferDetail.getScrewing());
        if (invTransferDetail.getInvItemId() != null && invTransferDetail.getUnitId() != null) {
            invTransferDetailDTO.setUnitsItemseList(unitsItemsService.getUnitsByItemId(invTransferDetail.getInvItemId().getId()));
            invTransferDetailDTO.setUnitsItemsSelected(invTransferDetail.getUnitId().getId());
        }
        if (invTransferDetail.getInvItemId() != null) {
            InvItemDTO invItemDTO = new InvItemDTO();
            invItemDTO.setId(invTransferDetail.getInvItemId().getId());
            invItemDTO.setName(invTransferDetail.getInvItemId().getName());
            invItemDTO.setCode(invTransferDetail.getInvItemId().getCode());
            invTransferDetailDTO.setInvItemId(invItemDTO);
        }
        if (invTransferDetail.getInvTransferId() != null) {
            InvTransferDTO invTransferDTO = new InvTransferDTO();
            invTransferDTO.setId(invTransferDetail.getInvTransferId().getId());
            invTransferDetailDTO.setInvTransferId(invTransferDTO);
        }
        if (invTransferDetail.getSelectedId() != null) {
            InvTransferDetailDTO transferDetailDTO = new InvTransferDetailDTO();
            transferDetailDTO.setId(invTransferDetail.getSelectedId().getId());
            invTransferDetailDTO.setSelectedId(transferDetailDTO);
        }
        if (invTransferDetail.getUnitId() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(invTransferDetail.getUnitId().getId());
            symbolDTO.setName(invTransferDetail.getUnitId().getName());
            invTransferDetailDTO.setUnitId(symbolDTO);
        }

        invTransferDetailDTO.setCreatedBy(invTransferDetail.getCreatedBy() != null ? invTransferDetail.getCreatedBy().getId() : null);
        invTransferDetailDTO.setCreatedDate(invTransferDetail.getCreationDate());
//        invTransferDetailDTO.setBranchId(invTransferDetail.getBranchId().getId() != null ? invTransferDetail.getBranchId().getId() : null);
        invTransferDetailDTO.setCompanyId(invTransferDetail.getCompanyId() != null ? invTransferDetail.getCompanyId().getId() : null);

        return invTransferDetailDTO;
    }

    public List<InvTransferDetailDTO> mapToDTOList(List<InvTransferDetail> invTransferDetailList, TobyUser tobyUser) {
        List<InvTransferDetailDTO> invTransferDetailDTOList = new ArrayList<>();
        for (InvTransferDetail invTransferDetail : invTransferDetailList) {
            invTransferDetailDTOList.add(mapToDTO(invTransferDetail, tobyUser));
        }
        return invTransferDetailDTOList;
    }

    @Override
    public List<InvTransferDetailDTO> getAllInvTransferDetaiDTOlList(Integer id, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap();
        params.put("transferId", id);
        List<InvTransferDetail> invTransferDetailList = dao.findListByQuery("SELECT d FROM InvTransferDetail d WHERE d.invTransferId.id =:transferId", params);
        return mapToDTOList(invTransferDetailList, tobyUser);
    }

    @Override
    public List<InvTransferDetailDTO> addInvTransferDetailsDTO(List<InvTransferDetailDTO> invTransferDetailDTOList, Integer invTransferDTOId, TobyUser tobyUser) {
        List<InvTransferDetailDTO> list = new ArrayList<>();
        for (InvTransferDetailDTO detailDTO : invTransferDetailDTOList) {
            InvTransferDTO invTransferDTO = new InvTransferDTO();
            invTransferDTO.setId(invTransferDTOId);
            detailDTO.setInvTransferId(invTransferDTO);
            InvTransferDetail invTransferDetail = mapToEntity(detailDTO, tobyUser);
            invTransferDetail = dao.updateEntity(invTransferDetail);
            InvTransferDetailDTO invTransferDetailDTO = mapToDTO(invTransferDetail, tobyUser);
            list.add(invTransferDetailDTO);
        }
        return list;
    }

    @Override
    public List<InvDetailDTO> getAllInvTransferDetailsByInvPurchaseInvoiceIdDTO(InvInvoiceDTO invPurchaseInvoiceDTO, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("invTransferId", invPurchaseInvoiceDTO.getId());
        List<InvTransferDetail> details = dao.findListByQuery("SELECT e FROM InvTransferDetail e WHERE e.invTransferId.id = :invTransferId", params);
        return returnListDTO(details, branchId, invPurchaseInvoiceDTO);
    }

    private List<InvDetailDTO> returnListDTO(List<InvTransferDetail> list, Integer branchId, InvInvoiceDTO invPurchaseInvoiceDTO) {
        List<InvDetailDTO> dTOList = new ArrayList<>();
        List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList = itemsBarcodesDetailsViewService.findItemsBarcodesDetailsViewBranchId(branchId);
        Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap = new HashMap<>();
        for (ItemsBarcodesDetailsView itemsBarcodesDetailsView : itemsBarcodesDetailsViewList) {
            itemsBarcodeMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
        }
        for (InvTransferDetail entity : list) {
            dTOList.add(mapTODTO(entity, itemsBarcodeMap, invPurchaseInvoiceDTO));
        }
        return dTOList;
    }

    private InvDetailDTO mapTODTO(InvTransferDetail entity, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap, InvInvoiceDTO invPurchaseInvoiceDTO) {
        InvDetailDTO dTO = initMapToDTO(entity, itemsBarcodeMap, invPurchaseInvoiceDTO);
        return dTO;
    }

    private InvDetailDTO initMapToDTO(InvTransferDetail entity, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap, InvInvoiceDTO invInvoiceDTO) {

        InvDetailDTO dTO = new InvDetailDTO();
        ItemsBarcodesDetailsView barcodesDetailsView = itemsBarcodeMap.get(entity.getItemBarcode());
        dTO = invGlobalService.fillItemDetail(dTO, itemsBarcodeMap,
                entity.getId(), entity.getSerial(), (entity.getBranchId() != null ? entity.getBranchId().getId() : null), barcodesDetailsView.getMaxPriceMen(),
                null, entity.getAmount(), null, null,
                BigDecimal.ZERO, null, entity.getId(), entity.getCreationDate(), entity.getCreatedBy().getId(),
                null, null,
                null,
                null, null,
                entity.getItemBarcode(), entity.getScrewing(), entity.getInvItemId().getId(), null,
                entity.getUnitId().getId(), entity.getUnitId().getSerial(), entity.getUnitId().getName(),
                entity.getInvItemId().getIsInventoryItem(),
                null, null, null, null,
                invInvoiceDTO.getCompanyActivity(), invInvoiceDTO.getScreenName(), null);
        dTO = invGlobalService.calculateDetailTotalsRow(dTO, invInvoiceDTO, true);
        dTO.setRealQuantity(dTO.getQuantity().multiply(dTO.getScrewing()));
        return dTO;
    }

    @Override
    public void deleteInvTransferDetailListDTO(List<InvDetailDTO> invDetailDTOList) {
        if (invDetailDTOList != null && !invDetailDTOList.isEmpty()) {
            for (InvDetailDTO detailDTO : invDetailDTOList) {
                if (detailDTO.getId() != null) {
                    InvTransferDetail detail = new InvTransferDetail();
                    detail.setId(detailDTO.getId());
                    dao.deleteEntity(detail);
                }
            }
        }

    }

    @Override
    public String addInvTransferDetailsDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList, InvInvoiceDTO invInvoiceDTO,
            ScreenNameClassEnum screenNameClassEnum, TobyUser isagUser) {
        String error = null;
        if (invPurchaseInvoiceDetailList != null && !invPurchaseInvoiceDetailList.isEmpty()) {
            error = invGlobalService.validateSave(invPurchaseInvoiceDetailList, invInvoiceDTO.getInvInventoryId().getId(),
                    screenNameClassEnum, invInvoiceDTO.getInventorySetupDTO().getNegativeSell(), (invInvoiceDTO.getLoadedSerial() != null),
                    invInvoiceDTO.getInventorySetupDTO().getSellBuy(), invInvoiceDTO.getCompanyActivity());
            if (error != null && !error.isEmpty()) {
                return error;
            }
            InvTransferDetail detail;
            for (InvDetailDTO detailDTO : invPurchaseInvoiceDetailList) {

                detail = mapToEntity(detailDTO, invInvoiceDTO.getId(), isagUser);
                dao.updateEntity(detail);
            }
        }

        return error;
    }

    private InvTransferDetail mapToEntity(InvDetailDTO dTO, Integer invTrasferId, TobyUser isagUser) {
        InvTransferDetail entity = new InvTransferDetail();

        entity.setSerial(dTO.getSerial());
        entity.setFinalQuantity(dTO.getFinalQuantity());
        entity.setItemBarcode(dTO.getItemBarcode());
        entity.setAmount(dTO.getQuantity());
        entity.setScrewing(dTO.getScrewing());
        entity.setStatus(dTO.getStatus());

        InvTransfer invTransfer = new InvTransfer();
        invTransfer.setId(invTrasferId);
        entity.setInvTransferId(invTransfer);

        if (dTO.getItemsBarcodesDetail() == null && dTO.getItemsBarcodesDetailTrans() != null) {
            dTO.setItemsBarcodesDetail(dTO.getItemsBarcodesDetailTrans());
        }

        if (dTO.getItemsBarcodesDetail() != null || dTO.getItemsBarcodesDetailTrans() != null) {
            InvItem invItem = new InvItem();
            invItem.setId(dTO.getItemsBarcodesDetail() != null ? dTO.getItemsBarcodesDetail().getItemId() : dTO.getItemsBarcodesDetailTrans().getItemId());
            entity.setInvItemId(invItem);
            entity.setItemBarcode(dTO.getItemsBarcodesDetail() != null ? dTO.getItemsBarcodesDetail().getBarcode() : dTO.getItemsBarcodesDetailTrans().getBarcode());
        }

        if (dTO.getItemsBarcodesDetail().getUnitId() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(dTO.getItemsBarcodesDetail().getUnitId());
            entity.setUnitId(symbol);
        }

        if (dTO.getId() == null) {
            if (entity.getSerial() == null) {
                Map<String, Object> params = new HashMap<>();
                params.put("invTransferId", invTrasferId);
                Integer max = genericService.getMaxGenericSerialForDetail(InvTransferDetail.class, "invTransferId", invTrasferId);
                entity.setSerial(max);
            }
            entity.setCreatedBy(isagUser);
            entity.setCreationDate(new Date());
        } else {
            TobyUser user = new TobyUser();
            user.setId(dTO.getCreatedBy());
            entity.setCreatedBy(user);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);
        }

        entity.setCompanyId(isagUser.getCompanyId());
        entity.setBranchId(isagUser.getBranchId());
        return entity;
    }

}
