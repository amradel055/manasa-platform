package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvInventoryTransactionDetailDTO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.InvPurchaseInvoiceDTO1;
import com.toby.dto.InvPurchaseInvoiceDetailDTO;
import com.toby.dto.InvoiceProductionStadesDTO;
import com.toby.dto.PrintProductionDTO;
import com.toby.dto.ProProductMovementDetailDTO;
import com.toby.dto.ProProductionProductNumberDTO;
import com.toby.dto.ProProductionStagesDTO;
import com.toby.dto.ProProductionTransactionDTO;
import com.toby.dto.ProProductionTransactionDetailViewDTO;
import com.toby.entity.Branch;
import com.toby.entity.InvInventoryTransaction;
import com.toby.entity.InvInventoryTransactionDetail;
import com.toby.entity.InvPurchaseInvoice;
import com.toby.entity.InvPurchaseInvoiceDetail;
import com.toby.entity.ProFactoryDelivery;
import com.toby.entity.ProItemProductionStages;
import com.toby.entity.ProProductionStages;
import com.toby.entity.ProProductionTransaction;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import com.toby.views.FinalRemainDataView;
import com.toby.views.ProProductionTransactionOfInvoice;
import com.toby.views.ProductionTransactionStagesInvoice;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class ProProductionTransactionServiceIMPL implements ProProductionTransactionService {

    @EJB
    private GenericDAO dao;
    @EJB
    InvPurchaseInvoiceService invPurchaseInvoiceService;

    ProProductionStagesServiceImpl productionStagesService = new ProProductionStagesServiceImpl();
    InvPurchaseInvoiceImpl invPurchaseInvoiceImpl = new InvPurchaseInvoiceImpl();
    public InvInventoryTransactionDetailServiceImpl inventoryTransactionDetailServiceImpl = new InvInventoryTransactionDetailServiceImpl();
    public InvPurchaseInvoiceDetailsImpl invPurchaseInvoiceDetailsImpl = new InvPurchaseInvoiceDetailsImpl();
    public InvPurchaseInvoiceImpl invoiceService = new InvPurchaseInvoiceImpl();

    @Override
    public List<ProProductionTransactionDTO> findBy(PrintProductionDTO productionDTO, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder query = new StringBuilder();
        query.append("SELECT p FROM ProProductionTransaction p WHERE p.branchId.id = :branchId ");
        appendProProductionStages(productionDTO, params, query);
        appendProProductionInvoice(productionDTO, params, query);
        appendProProductionDate(productionDTO, params, query);
        List<ProProductionTransaction> productionTransactions = dao.findListByQuery(query.toString(), params);
        return mapToTransactionDTOList(productionTransactions);
    }

    @Override
    public ProProductionStagesDTO findByProProduction(ProProductionTransactionDTO dTO) {
        Map<String, Object> params = new HashMap<>();
        params.put("productionId", dTO.getProProductionId());
        //ProProductionStages productionStages=dao.findEntityByNamedQuery("SELECT p FROM ProProductionStages p WHERE p.id=5");
        ProProductionStages productionStages = dao.findEntityById(ProProductionStages.class, dTO.getProProductionId());
        return productionStagesService.mapToDTO(productionStages);
    }

    private void appendProProductionStages(PrintProductionDTO productionDTO, Map<String, Object> params, StringBuilder query) {
        if (productionDTO.getFromStatus() != null) {
            params.put("fromStatus", productionDTO.getFromStatus());
            query.append("and p.proProductionId.id >= :fromStatus ");
        }

        if (productionDTO.getToStatus() != null) {
            params.put("toStatus", productionDTO.getToStatus());
            query.append("and p.proProductionId.id <= :toStatus ");
        }
    }

    private void appendProProductionInvoice(PrintProductionDTO productionDTO, Map<String, Object> params, StringBuilder query) {
        if (productionDTO.getFromInvoice() != null) {
            params.put("fromInvoice", productionDTO.getFromInvoice().getSerial());
            query.append("and p.invPurchaseInvoiceId.serial >= :fromInvoice ");
        }

        if (productionDTO.getToInvoice() != null) {
            params.put("toInvoice", productionDTO.getToInvoice().getSerial());
            query.append("and p.invPurchaseInvoiceId.serial <= :toInvoice ");
        }
    }

    private void appendProProductionDate(PrintProductionDTO productionDTO, Map<String, Object> params, StringBuilder query) {
        if (productionDTO.getFromDate() != null) {
            params.put("fromDate", productionDTO.getFromDate());
            query.append("and p.creationDate >= :fromDate ");
        }

        if (productionDTO.getToDate() != null) {
            params.put("toDate", productionDTO.getToDate());
            query.append("and p.creationDate <= :toDate ");
        }
    }

    @Override
    public List<ProProductionTransactionDTO> getAll(TobyUser tobyUser) {
        List<ProProductionTransaction> productionTransactions = dao.findListByQuery("SELECT p FROM ProProductionTransaction p");
        return mapToTransactionDTOList(productionTransactions);
    }

    @Override
    public List<ProProductionTransactionDTO> getInvPurchaseInvoiceById(TobyUser tobyUser, int proProductionId) {
        Map<String, Object> params = new HashMap<>();
        params.put("proProductionId", proProductionId);
        List<InvInventoryTransaction> invInventoryTransactions = dao.findListByQuery("SELECT i FROM InvInventoryTransaction i WHERE i.type=1 and i.invpurchaseinvoiceId.id not in (select p.invPurchaseInvoiceId.id from ProProductionTransaction p where  p.status = 1 and p.proProductionId.id=:proProductionId) and i.invpurchaseinvoiceId.id in (select d.invPurchaseInvoiceId.id from InvPurchaseInvoiceDetail d where d.itemId.id in (select p.invItemId.id from ProItemProductionStages p where p.proProductionStagesId.id=:proProductionId)) ", params);
        return mapInvTransactionToProTransactionDTOList(invInventoryTransactions, tobyUser);
    }

    public List<InvoiceProductionStadesDTO> getInvPurchaseInvoiceByStages(TobyUser tobyUser, int proProductionId) {
        Map<String, Object> params = new HashMap<>();
        Map<Integer, ProProductionTransactionDetailViewDTO> ProProductionTransactionDetailViewMap = new HashMap<>();
        params.put("proProductionId", proProductionId);
        List<FinalRemainDataView> finalRemainDataViewList = dao.findListByQuery(" select f from FinalRemainDataView f where  f.productionStagesId = :proProductionId ", params);
//        ProProductionTransactionDetailViewDTO
        for (FinalRemainDataView dataView : finalRemainDataViewList) {
            ProProductionTransactionDTO productionTransactionDTO = new ProProductionTransactionDTO();
            productionTransactionDTO.setInvPurchaseInvoiceId(dataView.getInvPurchaseInvoiceserial());
            productionTransactionDTO.setProductNumberDTOList(new ArrayList<ProProductionProductNumberDTO>());
            ProProductionProductNumberDTO proProductionProductNumberDTO = new ProProductionProductNumberDTO();
            proProductionProductNumberDTO.setProductNumber(dataView.getTest());
            productionTransactionDTO.getProductNumberDTOList().add(proProductionProductNumberDTO);
            productionTransactionDTO.getProductNumberDTOList().get(0).setDetailDetailViewDTOList(new ArrayList<ProProductionTransactionDetailViewDTO>());
            ProProductionTransactionDetailViewDTO proProductionTransactionDetailViewDTO = new ProProductionTransactionDetailViewDTO();
            proProductionTransactionDetailViewDTO.setInvItemName(dataView.getProductionLine());
            productionTransactionDTO.getProductNumberDTOList().get(0).getDetailDetailViewDTOList().add(proProductionTransactionDetailViewDTO);

        }

        return null;
    }

    @Override
    public void save(ProProductionTransactionDTO proProductionTransactionDTOSelected, int status, TobyUser tobyUser) {
        ProProductionTransaction proProductionTransaction = mapToEntity(proProductionTransactionDTOSelected, tobyUser);
        proProductionTransaction.setStatus(status);
        dao.updateEntity(proProductionTransaction);

    }

    @Override
    public void finished(ProProductionTransactionDTO proProductionTransactionDTOSelected, TobyUser tobyUser) {
        save(proProductionTransactionDTOSelected, 1, tobyUser);
        int temp = proProductionTransactionDTOSelected.getInvPurchaseInvoiceId();
        dao.executeDeleteQuery("delete from ProProductionTransaction p where p.status=0 and p.invPurchaseInvoiceId.id  = " + temp);

    }

    @Override
    public void findProProductionTransaction(int id, ProProductionTransactionDTO proProductionTransactionDTOSelected, TobyUser tobyUser) {
        ProProductionTransaction proProductionTransaction = dao.findEntityById(ProProductionTransaction.class, id);
        if ((proProductionTransaction.getInvPurchaseInvoiceId() != null ? proProductionTransaction.getInvPurchaseInvoiceId().getId() : null) == id) {
            proProductionTransaction.setStatus(1);
            dao.updateEntity(proProductionTransaction);

        } else {

            save(proProductionTransactionDTOSelected, 1, tobyUser);
        }

    }

    @Override
    public void deleteProProductionTransaction(ProProductionTransactionDTO productionTransactionDTO, TobyUser tobyUser) {
        ProProductionTransaction productionTransaction = mapToEntity(productionTransactionDTO, tobyUser);
        dao.deleteEntity(productionTransaction);
    }

    public ProProductionTransaction mapToEntity(ProProductionTransactionDTO proProductionTransactionDTO, TobyUser tobyUser) {
        ProProductionTransaction proProductionTransaction = new ProProductionTransaction();
        proProductionTransaction.setId(proProductionTransactionDTO.getId());
        proProductionTransaction.setClothNumber(proProductionTransactionDTO.getClothNumber());
        if (proProductionTransactionDTO.getInvPurchaseInvoiceId() != null) {
            InvPurchaseInvoice invPurchaseInvoice = new InvPurchaseInvoice();
            invPurchaseInvoice.setId(proProductionTransactionDTO.getInvPurchaseInvoiceId());
            proProductionTransaction.setInvPurchaseInvoiceId(invPurchaseInvoice);
        }
        if (proProductionTransactionDTO.getProFactoryDeliveryId() != null) {
            ProFactoryDelivery proFactoryDelivery = new ProFactoryDelivery();
            proFactoryDelivery.setId(proProductionTransactionDTO.getProFactoryDeliveryId());
            proProductionTransaction.setProFactoryDelivery(proFactoryDelivery);
        }
        if (proProductionTransactionDTO.getProProductionId() != null) {
            ProProductionStages productionStages = new ProProductionStages();
            productionStages.setId(proProductionTransactionDTO.getProProductionId().getId());
            proProductionTransaction.setProProductionId(productionStages);
        }

        proProductionTransaction.setInOrganizationSiteName(proProductionTransactionDTO.getInOrganizationSiteName());

        if (proProductionTransactionDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(proProductionTransactionDTO.getCreatedBy());
            proProductionTransaction.setCreatedBy(user);
            proProductionTransaction.setCreationDate(proProductionTransactionDTO.getCreatedDate());
            proProductionTransaction.setModifiedBy(tobyUser);
            proProductionTransaction.setModificationDate(new Date());
            if (proProductionTransactionDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(proProductionTransactionDTO.getCompanyId());
                proProductionTransaction.setCompanyId(company);
            }

            if (proProductionTransactionDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(proProductionTransactionDTO.getBranchId());
                proProductionTransaction.setBranchId(branch);
            }
        } else {
            proProductionTransaction.setCreatedBy(tobyUser);
            proProductionTransaction.setCreationDate(new Date());
            proProductionTransaction.setCompanyId(tobyUser.getCompanyId());
            proProductionTransaction.setBranchId(tobyUser.getBranchId());
        }

        return proProductionTransaction;
    }

    public ProProductionTransactionDTO mapInvTransactionToProTransactionDTO(InvInventoryTransaction inventoryTransaction) {
        ProProductionTransactionDTO proProductionTransactionDTO = new ProProductionTransactionDTO();
        //proProductionTransactionDTO.setId(invPurchaseInvoice.getId());
        if (inventoryTransaction != null) {

            proProductionTransactionDTO.setInvPurchaseInvoiceId(inventoryTransaction.getInvpurchaseinvoiceId() != null ? inventoryTransaction.getInvpurchaseinvoiceId().getId() : null);
            proProductionTransactionDTO.setInOrganizationSiteName(inventoryTransaction.getOrganizationSiteId() != null ? inventoryTransaction.getOrganizationSiteId().getName() : null);
            // proProductionTransactionDTO.setStatus(2);

        }

        return proProductionTransactionDTO;
    }

    public ProProductionTransactionDTO mapToTransactionDTO(ProProductionTransaction proProductionTransaction) {
        ProProductionTransactionDTO proProductionTransactionDTO = new ProProductionTransactionDTO();
        proProductionTransactionDTO.setId(proProductionTransaction.getId());
        proProductionTransactionDTO.setStatus(proProductionTransaction.getStatus());
        proProductionTransactionDTO.setInvPurchaseInvoiceId(proProductionTransaction.getInvPurchaseInvoiceId() != null ? proProductionTransaction.getInvPurchaseInvoiceId().getId() : null);
        proProductionTransactionDTO.setInvPurchaseInvoiceSerial(proProductionTransaction.getInvPurchaseInvoiceId() != null ? proProductionTransaction.getInvPurchaseInvoiceId().getSerial() : null);
        if (proProductionTransaction.getStatus() == 0) {
            proProductionTransactionDTO.setStatusName("received");
        } else if (proProductionTransaction.getStatus() == 1) {
            proProductionTransactionDTO.setStatusName("finished");
        }
        proProductionTransactionDTO.setInOrganizationSiteName(proProductionTransaction.getInOrganizationSiteName());
        proProductionTransactionDTO.setCreatedDate(proProductionTransaction.getCreationDate());

        if (proProductionTransaction.getProProductionId() != null) {
            ProProductionStagesDTO proProductionStagesDTO = new ProProductionStagesDTO();
            proProductionStagesDTO.setId(proProductionTransaction.getProProductionId().getId());
            proProductionStagesDTO.setName(proProductionTransaction.getProProductionId().getName());
            proProductionStagesDTO.setPrice(proProductionTransaction.getProProductionId().getPrice());
            proProductionStagesDTO.setSerial(proProductionTransaction.getProProductionId().getSerial());
            proProductionStagesDTO.setTypeStage(proProductionTransaction.getProProductionId().getTypeStage());
            proProductionTransactionDTO.setProProductionId(proProductionStagesDTO);
        }
        proProductionTransactionDTO.setStageName(proProductionTransaction.getProProductionId() != null ? proProductionTransaction.getProProductionId().getName() : null);

        return proProductionTransactionDTO;

    }

    public List<ProProductionTransactionDTO> mapToTransactionDTOList(List<ProProductionTransaction> proProductionTransactionList) {

        List<ProProductionTransactionDTO> proProductionTransactionDTOs = new ArrayList<>();
        for (ProProductionTransaction proProductionTransaction : proProductionTransactionList) {
            proProductionTransactionDTOs.add(mapToTransactionDTO(proProductionTransaction));
        }
        return proProductionTransactionDTOs;
    }

    public ProProductMovementDetailDTO mapTransactionToMovementDetailDTO(ProProductionTransaction proProductionTransaction) {
        ProProductMovementDetailDTO proProductMovementDetailDTO = new ProProductMovementDetailDTO();
        proProductMovementDetailDTO.setId(proProductionTransaction.getId());
        proProductMovementDetailDTO.setInvPurchesInvoiceId(proProductionTransaction.getInvPurchaseInvoiceId() != null ? proProductionTransaction.getInvPurchaseInvoiceId().getId() : null);
        proProductMovementDetailDTO.setInvPurchesInvoiceSerial(proProductionTransaction.getInvPurchaseInvoiceId() != null ? proProductionTransaction.getInvPurchaseInvoiceId().getSerial() : null);
        return proProductMovementDetailDTO;
    }

    public List<ProProductMovementDetailDTO> mapToMovementDetailDTOList(List<ProProductionTransaction> proProductionTransactionList) {
        List<ProProductMovementDetailDTO> proProductMovementDetailDTOList = new ArrayList<>();
        for (ProProductionTransaction proProductionTransaction : proProductionTransactionList) {
            proProductMovementDetailDTOList.add(mapTransactionToMovementDetailDTO(proProductionTransaction));
        }
        return proProductMovementDetailDTOList;
    }

    public List<ProProductionTransactionDTO> mapInvTransactionToProTransactionDTOList(List<InvInventoryTransaction> invInventoryTransaction, TobyUser tobyUser) {
        List<ProProductionTransactionDTO> proProductionTransactionDTOList = new ArrayList<>();
        for (InvInventoryTransaction inventoryTransaction : invInventoryTransaction) {
            proProductionTransactionDTOList.add(mapInvTransactionToProTransactionDTO(inventoryTransaction));
        }
        return proProductionTransactionDTOList;
    }

    @Override
    public List<ProProductMovementDetailDTO> getInvPurchesInvoiceID(TobyUser tobyUser) {
        List<ProProductionTransaction> proProductionTransactionList = dao.findListByQuery("select p from ProProductionTransaction p where p.status = 1 and p.invPurchaseInvoiceId.id not in (select m.invPurchesInvoiceId.id from ProProductMovementDetail m where m.proProductionDeliveryId.type in(1,2,4) ) ");
        return mapToMovementDetailDTOList(proProductionTransactionList);
    }

//    @Override
//    public List<InvInventoryTransactionDetailDTO> getInventoryDetailById(TobyUser tobyUser, int id) {
////        Map<String, Object> params = new HashMap<>();
////        params.put("id", id);
////        List<InvInventoryTransactionDetail> details=dao.findListByQuery("select d from InvInventoryTransactionDetail d where d.inventoryTransactionId.invpurchaseinvoiceId.id=:id",params);
////        
//        
//    }
    @Override
    public List<InvPurchaseInvoiceDetailDTO> getInvoiceDetailById(TobyUser tobyUser, int id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        List<InvPurchaseInvoiceDetail> details = dao.findListByQuery("select i from InvPurchaseInvoiceDetail i where i.invPurchaseInvoiceId.id=:id", params);
        return invPurchaseInvoiceDetailsImpl.mapToDTOList(details, tobyUser);
    }

    @Override
    public List<InvPurchaseInvoiceDTO1> getAllInvoiceForFinalCheck(TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        StringBuilder invpurchaseInvoiceIdList = new StringBuilder();
        List<Integer> invoices = dao.findListByQuery("select DISTINCT(p.invPurchaseInvoiceId) from ProProductionTransactionOfInvoice p where p.branchId=:branchId and p.numberRemain <= 0 and p.invPurchaseInvoiceId not in(select ppt.invPurchaseInvoiceId.id from ProProductionTransaction ppt where ppt.proProductionId.typeStage=1 and ppt.branchId.id=:branchId)", params);
        for (Integer invoice : invoices) {
            Integer numberOfItemByINvoice = getAllItemsByInvoice(invoice, tobyUser);
            Integer numberOfITemByInvoiceFromView = getAllItemsByInvoiceFromView(invoice, tobyUser);
            if (numberOfItemByINvoice.equals(numberOfITemByInvoiceFromView)) {
                Integer numberOfStagesByInvoice = getAllStagesByInvoice(invoice, tobyUser);//  
                Integer numberOfStagesByInvoiceFromView = getAllStagesByInvoiceFromView(invoice, tobyUser);//
                if (numberOfStagesByInvoice.equals(numberOfStagesByInvoiceFromView)) {
                    if (invpurchaseInvoiceIdList != null && !invpurchaseInvoiceIdList.toString().isEmpty()) {
                        invpurchaseInvoiceIdList.append(" , ").append(invoice);
                    } else {
                        invpurchaseInvoiceIdList.append(invoice);
                    }

                } else {
                    continue;
                }
            } else {
                continue;
            }
        }
        List<InvPurchaseInvoice> invPurchaseInvoices = new ArrayList<>();
        if (invpurchaseInvoiceIdList != null && !invpurchaseInvoiceIdList.toString().isEmpty()) {
            invPurchaseInvoices = dao.findListByQuery("select i from InvPurchaseInvoice i where i.id in ( " + invpurchaseInvoiceIdList.toString() + ")");
        }
        return invPurchaseInvoiceImpl.mapToDTOList(invPurchaseInvoices, tobyUser);
    }

    @Override
    public List<InvPurchaseInvoiceDTO1> getAllInvoiceForFinalCheckNew(TobyUser tobyUser) {
//        Map<String, Object> params = new HashMap<>();
        List<InvPurchaseInvoiceDTO1> invPurchaseInvoiceDTOList = new ArrayList<>();
//        params.put("branchId", tobyUser.getBranchId().getId());;
//        String query = "select i from InvPurchaseInvoice i where i.id in ( SELECT p.invPurchaseInvoiceId.id FROM ProFactoryDelivery p where p.branchId.id = :branchId AND p.id not in (select r.proFactoryDelivery.id from ProProductionTransaction r) )";
//        String query = "SELECT DISTINCT(p) FROM ProFactoryDelivery p where  p.type = 0 AND  p.branchId.id = :branchId AND p.id not in (select r.proFactoryDelivery.id from ProProductionTransaction r)";

//        List<ProFactoryDelivery> invPurchaseInvoices = dao.findListByQuery(query,params);
        Integer index = 1;
//        for(ProFactoryDelivery factoryDelivery : invPurchaseInvoices){
//            InvPurchaseInvoiceDTO1 invPurchaseInvoiceDTO1 = new InvPurchaseInvoiceDTO1();
//            invPurchaseInvoiceDTO1.setIndex(index++);
//            invPurchaseInvoiceDTO1.setId(factoryDelivery.getInvPurchaseInvoiceId().getId());
//            invPurchaseInvoiceDTO1.setSerial(factoryDelivery.getInvPurchaseInvoiceId().getSerial());
//            invPurchaseInvoiceDTO1.setDueDate(factoryDelivery.getInvPurchaseInvoiceId().getDueDate());
//            invPurchaseInvoiceDTO1.setProFactoryDeliveryId(factoryDelivery.getId());
//            invPurchaseInvoiceDTO1.setProFactoryDeliverySerial(factoryDelivery.getSerial());
//            invPurchaseInvoiceDTOList.add(invPurchaseInvoiceDTO1);
//        }

        String sql = "select distinct(serial) , pro_factory_delivery_id from  (select ipi.serial as serial , ipi.date as date , "
                + "pfdd.cloth_number as cloth_number , 1 as q , ipi.branch_id as branch_id ,pfd.id as pro_factory_delivery_id  from \n"
                + "pro_factory_delivery_detail pfdd \n"
                + "left join pro_factory_delivery pfd on pfd.id = pfdd.pro_factory_delivery_id\n"
                + "left join inv_purchase_invoice ipi on ipi.id = pfd.inv_purchase_invoice_id\n"
                + "group by ipi.serial , pfdd.cloth_number\n"
                + "union all\n"
                + "SELECT ipi.serial , ipi.date as date , ppt.cloth_number , -1 as q , ipi.branch_id as branch_id  ,ppt.pro_factory_delivery_id as pro_factory_delivery_id  \n"
                + "FROM toby.pro_production_transaction ppt \n"
                + "left join inv_purchase_invoice ipi on ipi.id = ppt.inv_purchase_invoice_id \n"
                + "where cloth_number is not null)  as s  where branch_id = "+tobyUser.getBranchId().getId()+"  group by  serial , pro_factory_delivery_id having sum(s.q) > 0";

        List<Object[]> res = dao.executeNativeQuery(sql);
        for (Object[] object : res) {
            InvPurchaseInvoiceDTO1 invPurchaseInvoiceDTO1 = new InvPurchaseInvoiceDTO1();
            invPurchaseInvoiceDTO1.setIndex(index++);
            invPurchaseInvoiceDTO1.setSerial((Integer) object[0]);
            invPurchaseInvoiceDTO1.setProFactoryDeliveryId((Integer) object[1]);
            invPurchaseInvoiceDTOList.add(invPurchaseInvoiceDTO1);
        }

        return invPurchaseInvoiceDTOList;
    }

    @Override
    public Integer getAllInvoiceByItem(Integer item, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        params.put("itemId", item);
        List<ProProductionTransactionOfInvoice> list1 = dao.findListByQuery("select p from ProProductionTransactionOfInvoice p where p.branchId=:branchId and p.itemId=:itemId and p.numberRemain = 0", params);
        return list1.size();
    }

    @Override
    public Integer getAllStagesByInvoice(Integer invoice, TobyUser tobyUser) {
        // List<Integer> list = dao.executeNativeQuery("select ipid.id from inv_purchase_invoice_detail ipid left join pro_item_production_stages pips on  ipid.item_id = pips.inv_item_id where inv_purchase_invoice_id = " + invoice+" group by pips.pro_production_stages_id");
        List<Integer> list = dao.executeNativeQuery("select ipid.id from inv_purchase_invoice_detail ipid , pro_item_production_stages pips where  ipid.item_id = pips.inv_item_id and inv_purchase_invoice_id = " + invoice + " group by pips.pro_production_stages_id");
        return list.size();
    }

    @Override
    public Integer getAllStagesByInvoiceFromView(Integer invoice, TobyUser tobyUser) {
        List<Integer> list = dao.executeNativeQuery("SELECT * FROM toby.pro_production_transaction_of_invoice where invPurchaseInvoiceId = " + invoice + " and numberRemain <= 0 group by productionStagesId");
        return list.size();
    }

    @Override
    public Integer getAllStagesByItem(Integer item, TobyUser tobyUser
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        params.put("itemId", item);
        List<ProItemProductionStages> list2 = dao.findListByQuery("select p from ProItemProductionStages p where p.branchId.id=:branchId and p.invItemId.id=:itemId", params);
        return list2.size();
    }

    public List<ProProductionTransactionDTO> mapViewtoTransactionDTOList(List<ProProductionTransactionOfInvoice> proProductionTransactionOfInvoices, TobyUser tobyUser) {
        List<ProProductionTransactionDTO> proProductionTransactionDTOs = new ArrayList<>();
        for (ProProductionTransactionOfInvoice invoice : proProductionTransactionOfInvoices) {
            proProductionTransactionDTOs.add(mapViewToTransactionDTO(invoice));
        }
        return proProductionTransactionDTOs;

    }

    public ProProductionTransactionDTO mapViewToTransactionDTO(ProProductionTransactionOfInvoice invoice) {
        ProProductionTransactionDTO productionTransactionDTO = new ProProductionTransactionDTO();
        productionTransactionDTO.setInvPurchaseInvoiceId(invoice.getInvPurchaseInvoiceId());
        ProProductionStagesDTO proProductionStagesDTO = new ProProductionStagesDTO();
        proProductionStagesDTO.setId(invoice.getProductionStagesId());
        productionTransactionDTO.setProProductionId(proProductionStagesDTO);
        productionTransactionDTO.setBranchId(invoice.getBranchId());

        return productionTransactionDTO;
    }

    @Override
    public Integer getAllItemsByInvoice(Integer invoice, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        params.put("invoice", invoice);
        List<InvPurchaseInvoiceDetail> list2 = dao.findListByQuery("select i from ProFactoryDeliveryDetail i where  i.proFactoryDeliveryId.type = 0 AND  i.branchId.id=:branchId and i.proFactoryDeliveryId.invPurchaseInvoiceId.id=:invoice ", params);
        return list2.size();
    }

    @Override
    public Integer getAllItemsByInvoiceFromView(Integer invoice, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        params.put("invoice", invoice);
        List<ProProductionTransactionOfInvoice> list1 = dao.findListByQuery("select p from ProProductionTransactionOfInvoice p where p.branchId=:branchId and p.invPurchaseInvoiceId=:invoice and p.numberRemain=0 group by p.itemId", params);
        return list1.size();
    }

    @Override
    public List<InvInventoryTransactionDetailDTO> getInventoryDetailById(TobyUser tobyUser, int id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        List<InvInventoryTransactionDetail> details = dao.findListByQuery("select d from InvInventoryTransactionDetail d where d.inventoryTransactionId.invpurchaseinvoiceId.id=:id", params);
        return inventoryTransactionDetailServiceImpl.mapToDTOList(details, tobyUser);
    }

    @Override
    public List<ProProductionTransactionDTO> getInvPurchaseInvoiceByIdByView(TobyUser tobyUser, int proProductionId) {
        Map<String, Object> params = new HashMap<>();
        params.put("proProductionId", proProductionId);
        List<ProductionTransactionStagesInvoice> itemses = dao.findListByQuery("SELECT e FROM ProductionTransactionStagesInvoice e WHERE e.proProductionStagesId =:proProductionStagesId", params);
        return mapToProProductionTransactionDTOList(itemses, tobyUser);
    }

    public List<ProProductionTransactionDTO> mapToProProductionTransactionDTOList(List<ProductionTransactionStagesInvoice> productionTransactionStagesInvoicesList, TobyUser tobyUser) {
        List<ProProductionTransactionDTO> proProductionTransactionDTOs = new ArrayList<>();
        for (ProductionTransactionStagesInvoice productionTransactionStagesInvoice : productionTransactionStagesInvoicesList) {
            proProductionTransactionDTOs.add(mapToProProductionTransactionDTO(productionTransactionStagesInvoice));
        }
        return proProductionTransactionDTOs;
    }

    public ProProductionTransactionDTO mapToProProductionTransactionDTO(ProductionTransactionStagesInvoice productionTransactionStagesInvoice) {
        ProProductionTransactionDTO proProductionTransactionDTO = new ProProductionTransactionDTO();
        proProductionTransactionDTO.setInvPurchaseInvoiceId(productionTransactionStagesInvoice.getInvoiceId());
        return proProductionTransactionDTO;
    }

    @Override
    public List<InvPurchaseInvoiceDetailDTO> getInvPurchaseInvoiceDetailsByPurchaseId(Integer invPurchaseInvoiceId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("invPurchaseInvoiceId", invPurchaseInvoiceId);
        List<InvPurchaseInvoiceDetail> detailses = dao.findListByQuery("SELECT e FROM InvPurchaseInvoiceDetail e WHERE e.invPurchaseInvoiceId.id=:invPurchaseInvoiceId ", params);
        return mapPurchaseToDTOList(detailses, tobyUser);
    }

    public List<InvPurchaseInvoiceDetailDTO> mapPurchaseToDTOList(List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailList, TobyUser tobyUser) {
        List<InvPurchaseInvoiceDetailDTO> invPurchaseInvoiceDTOList = new ArrayList<>();
        for (InvPurchaseInvoiceDetail invPurchaseInvoiceDetail : invPurchaseInvoiceDetailList) {
            invPurchaseInvoiceDTOList.add(mapPurchaseToDTO(invPurchaseInvoiceDetail, tobyUser));
        }
        return invPurchaseInvoiceDTOList;
    }

    public InvPurchaseInvoiceDetailDTO mapPurchaseToDTO(InvPurchaseInvoiceDetail invPurchaseInvoiceDetail, TobyUser tobyUser) {

        InvPurchaseInvoiceDetailDTO invPurchaseInvoiceDetailDTO = new InvPurchaseInvoiceDetailDTO();
        invPurchaseInvoiceDetailDTO.setCreatedBy(invPurchaseInvoiceDetail.getCreatedBy() != null ? invPurchaseInvoiceDetail.getCreatedBy().getId() : null);
        invPurchaseInvoiceDetailDTO.setCreatedDate(invPurchaseInvoiceDetail.getCreationDate());
        invPurchaseInvoiceDetailDTO.setBranchId(invPurchaseInvoiceDetail.getBranchId() != null ? invPurchaseInvoiceDetail.getBranchId().getId() : null);
        invPurchaseInvoiceDetailDTO.setExtraCost(invPurchaseInvoiceDetail.getExtraCost());
        invPurchaseInvoiceDetailDTO.setDiscount(invPurchaseInvoiceDetail.getDiscount());
        invPurchaseInvoiceDetailDTO.setDiscountType(invPurchaseInvoiceDetail.getDiscountType());
        invPurchaseInvoiceDetailDTO.setId(invPurchaseInvoiceDetail.getId());
        invPurchaseInvoiceDetailDTO.setCost(invPurchaseInvoiceDetail.getCost());
        invPurchaseInvoiceDetailDTO.setCostAvarage(invPurchaseInvoiceDetail.getCostAvarage());
        invPurchaseInvoiceDetailDTO.setFinalQuantity(invPurchaseInvoiceDetail.getFinalQuantity());
        invPurchaseInvoiceDetailDTO.setNet(invPurchaseInvoiceDetail.getNet());
        invPurchaseInvoiceDetailDTO.setQuantity(invPurchaseInvoiceDetail.getQuantity());
        invPurchaseInvoiceDetailDTO.setScrewing(invPurchaseInvoiceDetail.getScrewing());
        invPurchaseInvoiceDetailDTO.setSelectedId(invPurchaseInvoiceDetail.getSelectedId());
        invPurchaseInvoiceDetailDTO.setStatus(invPurchaseInvoiceDetail.getStatus());
        invPurchaseInvoiceDetailDTO.setTaxValue(invPurchaseInvoiceDetail.getTaxValue());
        invPurchaseInvoiceDetailDTO.setTransferFrom(invPurchaseInvoiceDetail.getTransferFrom());
        invPurchaseInvoiceDetailDTO.setSerial(invPurchaseInvoiceDetail.getSerial());
        invPurchaseInvoiceDetailDTO.setIndex(invPurchaseInvoiceDetail.getSerial());
        invPurchaseInvoiceDetailDTO.setIsdeleted(invPurchaseInvoiceDetail.getIsdeleted());
        invPurchaseInvoiceDetailDTO.setClothNumber(invPurchaseInvoiceDetail.getClothNumber());
        invPurchaseInvoiceDetailDTO.setUnitId(invPurchaseInvoiceDetail.getUnitId().getId());

        invPurchaseInvoiceDetailDTO.setWeight(invPurchaseInvoiceDetail.getWeight());
        invPurchaseInvoiceDetailDTO.setBounse(invPurchaseInvoiceDetail.getBounse());
        invPurchaseInvoiceDetailDTO.setNumber(invPurchaseInvoiceDetail.getNumber());
        if (invPurchaseInvoiceDetail.getInvItemParentId() != null) {
            InvItemDTO invItemDTO = new InvItemDTO();
            invItemDTO.setId(invPurchaseInvoiceDetail.getInvItemParentId().getId());
            invItemDTO.setName(invPurchaseInvoiceDetail.getInvItemParentId().getName());
            invPurchaseInvoiceDetailDTO.setInvItemParentId(invItemDTO);
        }

        invPurchaseInvoiceDetailDTO.setCompanyId(invPurchaseInvoiceDetail.getCompanyId() != null ? invPurchaseInvoiceDetail.getCompanyId().getId() : null);

        if (invPurchaseInvoiceDetail.getInvInventoryId() != null) {
            InvInventoryDTO invInventoryDTO = new InvInventoryDTO();
            invInventoryDTO.setId(invPurchaseInvoiceDetail.getInvInventoryId().getId());
            invInventoryDTO.setName(invPurchaseInvoiceDetail.getItemId().getName());
            invPurchaseInvoiceDetailDTO.setInvInventoryDTO(invInventoryDTO);
        }
        if (invPurchaseInvoiceDetail.getInvPurchaseInvoiceId() != null) {
            InvPurchaseInvoiceDTO1 invPurchaseInvoiceDTO = new InvPurchaseInvoiceDTO1();
            invPurchaseInvoiceDTO.setId(invPurchaseInvoiceDetail.getInvPurchaseInvoiceId().getId());
            invPurchaseInvoiceDetailDTO.setInvPurchaseInvoiceId(invPurchaseInvoiceDTO);
        }
        if (invPurchaseInvoiceDetail.getItemId() != null) {
            InvItemDTO invItemDTO = new InvItemDTO();
            invItemDTO.setId(invPurchaseInvoiceDetail.getItemId().getId());
            invItemDTO.setName(invPurchaseInvoiceDetail.getItemId().getName());
            invItemDTO.setCode(invPurchaseInvoiceDetail.getItemId().getCode());
//            if (invPurchaseInvoiceDetail.getItemId().getUnitId() != null) {
//                SymbolDTO symbolDTO = new SymbolDTO();
//                symbolDTO.setId(invPurchaseInvoiceDetail.getItemId().getUnitId().getId());
//                symbolDTO.setName(invPurchaseInvoiceDetail.getItemId().getUnitId().getName());
//                invItemDTO.setUnitId(symbolDTO);
//            }
            invPurchaseInvoiceDetailDTO.setItemId(invItemDTO);
        }

        return invPurchaseInvoiceDetailDTO;
    }

    @Override
    public List<ProProductionTransactionDTO> getProProductionTransactionDTOofViewList(ProProductionStagesDTO productionStages, TobyUser tobyUser) {

        List<ProProductionTransactionDTO> productionTransactionDTOList = new ArrayList<>();

        String sql = "select  distinct(ipi.id) ,  ipi.serial  from \n"
                + "                pro_factory_delivery_detail pfdd left join pro_factory_delivery pfd on pfd.id = pfdd.pro_factory_delivery_id\n"
                + "                left join inv_purchase_invoice ipi on ipi.id = pfd.inv_purchase_invoice_id\n"
                + "                left join pro_item_production_stages pips on pips.inv_item_id = pfdd.item_id\n"
                + "                where pips.pro_production_stages_id = " + productionStages.getId() + "  and ipi.branch_id = " + tobyUser.getBranchId().getId() + " AND pfd.type = 0\n"
                + "               and  (pfdd.id not in ( SELECT pro_factory_delivery_detail_id  FROM  `pro_production_items_transaction` where  pro_production_stages_id = " + productionStages.getId() + " and pro_factory_delivery_detail_id is not null ) \n"
                + "                or\n"
                + "                pfdd.id in (select s.invoiceDetailId from ( SELECT `pfdd`.`id` AS `invoiceDetailId`,`ppit`.`branch_id` AS `branchId`,(`pfdd`.`number` - SUM(`ppit`.`number`)) AS `numberRemain`\n"
                + "                    FROM\n"
                + "                        ((`pro_production_items_transaction` `ppit`\n"
                + "                        LEFT JOIN `pro_factory_delivery_detail` `pfdd` ON ((`ppit`.`pro_factory_delivery_detail_id` = `pfdd`.`id`)))\n"
                + "                        LEFT JOIN `pro_factory_delivery` `pfd` ON ((`pfd`.`id` = `pfdd`.`pro_factory_delivery_id`)))\n"
                + "                	where pfd.branch_id = " + tobyUser.getBranchId().getId() + " and  pro_production_stages_id = " + productionStages.getId() + " AND pfd.type = 0 \n"
                + "                    GROUP BY `pfdd`.`id` , `pfd`.`id` , `ppit`.`pro_production_stages_id`,`pfd`.`id`,`ppit`.`branch_id`,pfdd.cloth_number ) s where s.branchid = " + tobyUser.getBranchId().getId() + " and numberRemain > 0) \n"
                + "                )";

        List<Object[]> res = dao.executeNativeQuery(sql);

        for (Object[] object : res) {
            ProProductionTransactionDTO productionTransactionDTO = new ProProductionTransactionDTO();
            productionTransactionDTO.setInvPurchaseInvoiceId((Integer) object[0]);
            productionTransactionDTO.setSerial((Integer) object[1]);
//            productionTransactionDTO.setEmployeename((String) object[2]);
            productionTransactionDTO.setProProductionId(productionStages);

            productionTransactionDTOList.add(productionTransactionDTO);
        }
        return productionTransactionDTOList;
    }

    public List<ProProductionTransactionDTO> maptoDTOListOfView(List<InvPurchaseInvoice> invPurchaseInvoiceList) {
        List<ProProductionTransactionDTO> proProductionTransactionDTOList = new ArrayList<>();
        for (InvPurchaseInvoice invPurchaseInvoice : invPurchaseInvoiceList) {
            proProductionTransactionDTOList.add(mapInvTransactionToProTransactionDTOofview(invPurchaseInvoice));
        }
        return proProductionTransactionDTOList;
    }

    public ProProductionTransactionDTO mapInvTransactionToProTransactionDTOofview(InvPurchaseInvoice invPurchaseInvoice) {
        ProProductionTransactionDTO proProductionTransactionDTO = new ProProductionTransactionDTO();
        proProductionTransactionDTO.setInvPurchaseInvoiceId(invPurchaseInvoice.getId());
        proProductionTransactionDTO.setSerial(invPurchaseInvoice.getSerial());
        return proProductionTransactionDTO;

    }

    @Override
    public List<ProProductionTransactionDTO> getAllCheckedInvoicePurchase(TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        List<ProProductionTransaction> productionTransactions = dao.findListByQuery("SELECT p FROM ProProductionTransaction p where p.branchId.id=:branchId and p.proProductionId.serial=0", params);
        return mapToTransactionDTOList(productionTransactions);
    }

    @Override
    public ProProductionTransactionDTO getProductionTransactionById(Integer id, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("branchId", tobyUser.getBranchId().getId());
        ProProductionTransaction proProductionTransaction = dao.findEntityByQuery("SELECT p FROM ProProductionTransaction p where p.branchId.id=:branchId and p.id=:id", params);
        return mapToTransactionDTO(proProductionTransaction);
    }

    @Override
    public Boolean checkFinish(Integer invPurchaseInvoiceId, Integer proFactoryDeliveryId, Integer clothNumber) {
        Map<String, Object> params = new HashMap<>();
        params.put("invPurchaseInvoiceId", invPurchaseInvoiceId);
        params.put("proFactoryDeliveryId", proFactoryDeliveryId);
        params.put("clothNumber", clothNumber);
        ProProductionTransaction proProductionTransaction = dao.findEntityByQuery("SELECT p FROM ProProductionTransaction p where p.invPurchaseInvoiceId.id=:invPurchaseInvoiceId and p.proFactoryDelivery.id=:proFactoryDeliveryId AND p.clothNumber=:clothNumber", params);
        return proProductionTransaction != null;
    }

    @Override
    public Long getTotalRegistors(Integer branchId, Map<String, Object> filters) {
        Long details = null;
        StringBuilder builder = new StringBuilder();
        filters.put("branchId", branchId);
        builder.append("SELECT count(s) FROM ProProductionTransaction s WHERE   s.branchId.id = :branchId AND s.proFactoryDelivery IS NOT NULL ");
        appendFilter(filters, builder);
        details = dao.findEntityByQuery(builder.toString(), filters);

        return details == null ? 0 : details;
    }

    private void appendFilter(Map<String, Object> filters, StringBuilder builder) {
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and ( FUNCTION('DATE_FORMAT',s.creationDate, '%d-%m-%Y')  like CONCAT('%',:globalFilter ,'%') OR  s.proFactoryDelivery.invPurchaseInvoiceId.serial like CONCAT('%',:globalFilter ,'%'))";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
    }

    @Override
    public List<ProProductionTransactionDTO> getALLInvPurchaseInvoiceDTOByTypeAndBranchId(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters) {
        filters.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT s FROM ProProductionTransaction s WHERE  s.branchId.id = :branchId AND s.proFactoryDelivery is not null");
        appendFilter(filters, builder);
        builder.append(" order by s.id DESC");
        List<ProProductionTransaction> details = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        return mapToTransactionDTOList(details);
    }

}
