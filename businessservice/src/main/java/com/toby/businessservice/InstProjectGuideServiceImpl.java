/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.ProjectGuideSearchBean;
import com.toby.core.GenericDAO;
import com.toby.dto.InstProjectGuideDTO;
import com.toby.dto.InstPurchaseInvoiceDTO;
import com.toby.dto.InstPurchaseInvoiceDetailDTO;
import com.toby.entity.InstProjectGuide;
import com.toby.entity.TobyUser;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author ahmed
 */
@Stateless
public class InstProjectGuideServiceImpl implements InstProjectGuideService {

    @EJB
    private GenericDAO dao;
    @EJB
    private instPurchaseInvoiceService invoiceService;

    @Override
    public List<InstProjectGuide> getAllInstProjectGuideRoots() {
        return dao.findListByQuery("SELECT cs FROM InstProjectGuide cs WHERE cs.parent IS NULL");
    }

    @Override
    public List<InstProjectGuide> getCompanyInstProjectGuideRoots(Integer companyId) {
        Map<String, Object> params = new HashMap();
        params.put("id", companyId);
        return dao.findListByQuery("SELECT cs FROM InstProjectGuide cs WHERE cs.parent IS NULL AND cs.companyId.id=:id", params);
    }

    @Override
    public List<InstProjectGuide> getAllInstProjectGuides(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        return dao.findListByQuery("SELECT cs FROM InstProjectGuide cs WHERE cs.branchId.id=:branchId AND cs.active = 1 order by cs.code", params);
    }

    @Override
    public List<InstProjectGuideDTO> getAllInstProjectGuidesDTO(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<InstProjectGuide> list = dao.findListByQuery("SELECT cs FROM InstProjectGuide cs WHERE cs.branchId.id=:branchId AND cs.active = 1", params);
        List<InstProjectGuideDTO> dtoList = new ArrayList<>();
        returnListDTO(list, dtoList);
        return dtoList;
    }

    @Override
    public List<InstProjectGuide> getCompanyInstProjectGuides(Integer companyId) {
        Map<String, Object> params = new HashMap();
        params.put("id", companyId);
        return dao.findListByQuery("SELECT cs FROM InstProjectGuide cs WHERE cs.companyId.id=:id ORDER BY cs.level ASC", params);
    }

    @Override
    public void deleteInstProjectGuide(InstProjectGuide selectedCS) {
        dao.deleteEntity(selectedCS);
    }

    @Override
    public InstProjectGuide addInstProjectGuide(InstProjectGuide newInstProjectGuide, TobyUser tobyUser) {
        InstProjectGuide instProjectGuide = dao.updateEntity(newInstProjectGuide);
        /* if (newInstProjectGuide.getLevel() == 3 && instProjectGuide.getId() != null) {
            InstPurchaseInvoiceDTO dto = new InstPurchaseInvoiceDTO();
            dto.setDate(new Date());
            List<InstPurchaseInvoiceDetailDTO> instPurchaseInvoiceDetailDTOList = new ArrayList();
            InstPurchaseInvoiceDetailDTO instPurchaseInvoiceDetailDTO = new InstPurchaseInvoiceDetailDTO();
            InvItemInstProjectGuideView instProjectGuideView = new InvItemInstProjectGuideView();
            instProjectGuideView.setId(instProjectGuide.getId());
            instProjectGuideView.setTableType(1);
            instProjectGuideView.setName(newInstProjectGuide.getName());
            instPurchaseInvoiceDetailDTO.setItemId(instProjectGuideView);
            instPurchaseInvoiceDetailDTO.setQuantity(BigDecimal.ONE);
            instPurchaseInvoiceDetailDTO.setUnitPrice(newInstProjectGuide.getPrice());
            instPurchaseInvoiceDetailDTOList.add(instPurchaseInvoiceDetailDTO);
            addInstPurchaseInvoiceDTO(dto, instPurchaseInvoiceDetailDTOList, null, tobyUser);
        }*/
        return instProjectGuide;
    }

    private InstPurchaseInvoiceDTO addInstPurchaseInvoiceDTO(InstPurchaseInvoiceDTO instPurchaseInvoiceDTO, List<InstPurchaseInvoiceDetailDTO> instPurchaseInvoiceDetailDTOList, List<InstPurchaseInvoiceDetailDTO> instPurchaseInvoiceDetailDTOListDelete, TobyUser tobyUser) {
        return invoiceService.addInstPurchaseInvoice(instPurchaseInvoiceDTO, instPurchaseInvoiceDetailDTOList, instPurchaseInvoiceDetailDTOListDelete, tobyUser);
    }

    @Override
    public void saveInstProjectGuide(InstProjectGuide newInstProjectGuide) {
        dao.saveEntity(newInstProjectGuide);
    }

    @Override
    public InstProjectGuide findInstProjectGuide(Integer instProjectId) {
        if (instProjectId != null) {
            return dao.findEntityById(InstProjectGuide.class, instProjectId);
        } else {
            return null;
        }
    }

    @Override
    public InstProjectGuide findInstProjectGuideByCode(String parentCode, Integer branchId) {
        if (parentCode != null) {
            List<InstProjectGuide> instProjectGuideList = null;
            Map<String, Object> params = new HashMap();
            params.put("parentCode", parentCode);
            params.put("branchId", branchId);
            instProjectGuideList = dao.findListByQuery("SELECT cs FROM InstProjectGuide cs WHERE cs.code = :parentCode AND cs.branchId.id=:branchId", params);
            InstProjectGuide center = instProjectGuideList != null ? instProjectGuideList.get(0) : null;
            return center;
        } else {
            return null;
        }
    }

    @Override
    public List<InstProjectGuide> getBranchInstProjectGuidesActive(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        String sql = "SELECT cs FROM InstProjectGuide cs WHERE cs.branchId.id=:id AND cs.active=1 AND cs.id not in (select i.parent.id from InstProjectGuide i where i.branchId.id=:id AND i.parent is not null) AND cs.id NOT IN (select ic.projectId.id from InstContract ic where ic.projectId IS NOT NULL and ic.cancel != true)";
        return dao.findListByQuery(sql, params);
    }

    @Override
    public List<InstProjectGuide> getBranchInstProjectGuidesActiveNotAvailable(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        String sql = "SELECT cs FROM InstProjectGuide cs WHERE cs.branchId.id=:id AND cs.active=1 AND cs.level = 3 AND cs.id  IN (select ic.projectId.id from InstContract ic where ic.projectId IS NOT NULL)";
        return dao.findListByQuery(sql, params);
    }

    @Override
    public List<InstProjectGuideDTO> getBranchInstProjectGuidesActiveNotAvailableDTO(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        String sql = "SELECT cs FROM InstProjectGuide cs WHERE cs.branchId.id=:id AND cs.active=1 AND cs.level = 3 AND cs.id  IN (select ic.projectId.id from InstContract ic where ic.projectId IS NOT NULL)";
        List<InstProjectGuide> instProjectGuideList = dao.findListByQuery(sql, params);
        List<InstProjectGuideDTO> instProjectGuideDTOList = new ArrayList<>();
        returnListDTO(instProjectGuideList, instProjectGuideDTOList);
        return instProjectGuideDTOList;
    }

    @Override
    public List<InstProjectGuideDTO> findInstProjectGuidesByBranch(Integer id, Integer level) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT cs FROM InstProjectGuide cs WHERE cs.branchId.id=:id  ");
        if (level != null) {
            params.put("level", level);
            sql.append(" and cs.level = :level ");
        }
        sql.append(" order by cs.code");
        List<InstProjectGuide> instProjectGuideList = dao.findListByQuery(sql.toString(), params);
        List<InstProjectGuideDTO> instProjectGuideDTOList = new ArrayList<>();
        returnListDTO(instProjectGuideList, instProjectGuideDTOList);
        return instProjectGuideDTOList;
    }

    @Override
    public List<InstProjectGuide> getAllSubInstProjectGuideByBranchIdActive(Integer id) {
        List<InstProjectGuide> allInstProjectGuideUnits = getBranchInstProjectGuidesActive(id);
//        List<InstProjectGuide> childrens = new ArrayList<>();
//        Map<Integer, InstProjectGuide> map = new HashMap<>();
//
//        for (InstProjectGuide gau : allInstProjectGuideUnits) {
//            if (gau.getParent() != null) {
//                map.put(gau.getParent().getId(), gau.getParent());
//            }
//        }
//
//        for (InstProjectGuide gau : allInstProjectGuideUnits) {
//            if (!map.containsKey(gau.getId())) {
//                if (gau.getParent() == null) {
//                    childrens.add(gau);
//                    continue;
//                }
//                childrens.add(gau);
//            }
//        }

        return allInstProjectGuideUnits;
    }

    @Override
    public List<InstProjectGuide> getActiveSubInstProjectGuidesByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        return dao.findListByQuery("SELECT cs FROM InstProjectGuide cs WHERE cs.parent IS NOT NULL AND cs.branchId.id=:id AND cs.active = 1", params);
    }

    @Override
    public List<InstProjectGuide> getBranchWithoutParentInstProjectGuides(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        return dao.findListByQuery("SELECT cs FROM InstProjectGuide cs WHERE cs.branchId.id=:id", params);
    }

    @Override
    public List<InstProjectGuide> getBranchInstProjectGuideRoots(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        return dao.findListByQuery("SELECT cs FROM InstProjectGuide cs WHERE cs.parent IS NULL AND cs.branchId.id=:id", params);
    }
    
    @Override
    public void updateReserved(Boolean reserved,Integer id,Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        params.put("branchId", branchId);
        params.put("reserved", reserved);
         dao.executeUpdateQuery("Update  InstProjectGuide cs set cs.reserved = :reserved WHERE cs.id =:id AND cs.branchId.id= :branchId",params);
    }

    @Override
    public List<InstProjectGuide> getAllprojectGuideIndex(ProjectGuideSearchBean projectGuideSearchBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", projectGuideSearchBean.getBranchId());
        StringBuilder queryBuilder = new StringBuilder();
        //String query = "SELECT e FROM CostCenter e WHERE e.parent IS NOT NULL AND e.branchId.id = :branchId " + queryBuilder.toString();
        queryBuilder.append("SELECT e FROM InstProjectGuide e WHERE e.branchId.id = :branchId ");
        appendProjectGuidForQueryBuilder(queryBuilder, params, projectGuideSearchBean);
        queryBuilder.append(" ORDER BY e.code");
        List<InstProjectGuide> projectGuide = dao.findListByQuery(queryBuilder.toString(), params);
        return projectGuide;
    }
    
    @Override
    public List<InstProjectGuide> getAllprojectGuideSellAndNotSell(ProjectGuideSearchBean projectGuideSearchBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", projectGuideSearchBean.getBranchId());
        StringBuilder queryBuilder = new StringBuilder();
        //String query = "SELECT e FROM CostCenter e WHERE e.parent IS NOT NULL AND e.branchId.id = :branchId " + queryBuilder.toString();
        queryBuilder.append("SELECT e FROM InstProjectGuide e WHERE e.branchId.id = :branchId AND e.id not in(  SELECT e.projectId.id FROM InstContract e WHERE e.branchId.id = :branchId and e.projectId is not null )");
        appendProjectGuidForQueryBuilder(queryBuilder, params, projectGuideSearchBean); 
        List<InstProjectGuide> notSellProjectGuide = dao.findListByQuery(queryBuilder.toString(), params);
         
        StringBuilder queryBuilderTwo = new StringBuilder();
        queryBuilderTwo.append("SELECT i FROM InstProjectGuide i WHERE i.branchId.id = :branchId AND i.id  in(  SELECT e.projectId.id FROM InstContract e WHERE e.branchId.id = :branchId and e.projectId is not null )");
        appendProjectGuidForQueryBuilderNotSell(queryBuilderTwo, params, projectGuideSearchBean);
      List<InstProjectGuide> sellProjectGuide = dao.findListByQuery(queryBuilderTwo.toString(), params);
        ArrayList <InstProjectGuide> finalProjectGuide = new ArrayList<InstProjectGuide>();
        finalProjectGuide.addAll(notSellProjectGuide);
        sellProjectGuide.forEach(instProjectGuide -> { instProjectGuide.setSell(Boolean.TRUE);});
      finalProjectGuide.addAll(sellProjectGuide);
        finalProjectGuide.stream().sorted(Comparator.comparing(InstProjectGuide::getCode))
            .collect(Collectors.toList()); 
        return finalProjectGuide;
    }

    private void appendProjectGuidForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, ProjectGuideSearchBean projectGuideSearchBean) {
        if (projectGuideSearchBean.getProjectGuideIdFrom() != null && projectGuideSearchBean.getProjectGuideIdFrom().getId() != null) {
            params.put("projectGuideIdFrom", projectGuideSearchBean.getProjectGuideIdFrom().getCode());
            queryBuilder.append(" and e.code >= :projectGuideIdFrom");
        }

        if (projectGuideSearchBean.getProjectGuideIdTo() != null && projectGuideSearchBean.getProjectGuideIdTo().getId() != null) {
            params.put("projectGuideIdTo", projectGuideSearchBean.getProjectGuideIdTo().getCode());
            queryBuilder.append(" and e.code <= :projectGuideIdTo");
        }
    }
    
    private void appendProjectGuidForQueryBuilderNotSell(StringBuilder queryBuilder, Map<String, Object> params, ProjectGuideSearchBean projectGuideSearchBean) {
        if (projectGuideSearchBean.getProjectGuideIdFrom() != null && projectGuideSearchBean.getProjectGuideIdFrom().getId() != null) {
            params.put("projectGuideIdFrom", projectGuideSearchBean.getProjectGuideIdFrom().getCode());
            queryBuilder.append(" and i.code >= :projectGuideIdFrom");
        }

        if (projectGuideSearchBean.getProjectGuideIdTo() != null && projectGuideSearchBean.getProjectGuideIdTo().getId() != null) {
            params.put("projectGuideIdTo", projectGuideSearchBean.getProjectGuideIdTo().getCode());
            queryBuilder.append(" and i.code <= :projectGuideIdTo");
        }
    }

    @Override
    public List<InstProjectGuide> getAllInstProjectGuideByBranchIdAndAccNumOrShotCode(Integer selectedBranchId, String instProjectGuideCode) {
        Map<String, Object> params = new HashMap();
        params.put("id", selectedBranchId);
        params.put("instProjectGuideCode", instProjectGuideCode);
        return dao.findListByQuery("SELECT cc FROM InstProjectGuide cc WHERE cc.branchId.id = :id AND cc.code = :instProjectGuideCode", params);
    }

    @Override
    public List<InstProjectGuide> getAllInstProjectGuideByBranchIdAndAccNumOrShotCode(Integer selectedBranchId, String instProjectGuideCode, Integer instProjectGuideId) {
        Map<String, Object> params = new HashMap();
        params.put("id", selectedBranchId);
        params.put("instProjectGuideCode", instProjectGuideCode);
        params.put("instProjectGuideId", instProjectGuideId);
        return dao.findListByQuery("SELECT cc FROM InstProjectGuide cc WHERE cc.id != :instProjectGuideId AND cc.branchId.id = :id AND cc.code = :instProjectGuideCode", params);
    }

    @Override
    public List<InstProjectGuide> getAllSubInstProjectGuideByBranchIdActiveAndInstProjectGuideId(Integer instProjectId) {
        List<InstProjectGuide> childrens = new ArrayList<>();
        InstProjectGuide InstProjectGuideUnit = findInstProjectGuide(instProjectId);
        if (InstProjectGuideUnit != null && InstProjectGuideUnit.getId() != null) {
            findTheLastChilds(InstProjectGuideUnit, childrens);
        }

        return childrens;
    }

    public void findTheLastChilds(InstProjectGuide InstProjectGuideUnit, List<InstProjectGuide> childrens) {
        if (InstProjectGuideUnit.getChildNodes() != null && !InstProjectGuideUnit.getChildNodes().isEmpty()) {
            for (InstProjectGuide childInstProjectGuide : InstProjectGuideUnit.getChildNodes()) {
                InstProjectGuide InstProjectGuideValue = findInstProjectGuide(childInstProjectGuide.getId());
                findTheLastChilds(InstProjectGuideValue, childrens);
            }
        } else {
            childrens.add(InstProjectGuideUnit);
        }
    }

    @Override
    public List<InstProjectGuide> getAllSubInstProjectGuideByBranchIdActiveAndLevel(Integer branchId, Integer level) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("level", level);
        return dao.findListByQuery("SELECT cs FROM InstProjectGuide cs WHERE cs.branchId.id=:branchId AND cs.active=1 AND cs.level =:level ", params);
    }

    @Override
    public List<InstProjectGuideDTO> getAllSubInstProjectGuideByBranchIdActiveAndLevelDTO(Integer branchId, Integer level, Integer level2) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
//        params.put("level", level);
//        params.put("level2", level2);
//        String sql = "SELECT cs FROM InstProjectGuide cs WHERE cs.branchId.id=:branchId AND cs.active=1 AND cs.level =:level AND cs.level =:level2";
        String sql = "SELECT cs FROM InstProjectGuide cs WHERE cs.branchId.id=:branchId AND cs.active=1 ";
        List<InstProjectGuide> instProjectGuideList = dao.findListByQuery(sql, params);
        List<InstProjectGuideDTO> instProjectGuideDTOList = new ArrayList<>();
        returnListDTO(instProjectGuideList, instProjectGuideDTOList);
        return instProjectGuideDTOList;
    }

    private void mapToDTO(InstProjectGuideDTO dTO, InstProjectGuide entity) {
        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
        dTO.setLevel(entity.getLevel());
        dTO.setIndex(entity.getId());
        dTO.setId(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate() != null ? entity.getCreationDate() : new Date());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
    }

    public InstProjectGuide mapFromDTO(InstProjectGuideDTO dTO, TobyUser tobyUser) {
        InstProjectGuide entity = new InstProjectGuide();
        entity.setName(dTO.getName());
        entity.setId(dTO.getId());
        entity.setLevel(dTO.getLevel());

        if (dTO.getId() == null) {
            entity.setCreatedBy(tobyUser);
            entity.setCreationDate(new Date());
        } else {
            TobyUser tobyUser1 = new TobyUser();
            tobyUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(tobyUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(tobyUser);
        }

        entity.setBranchId(tobyUser.getBranchId());
        entity.setCompanyId(tobyUser.getCompanyId());
        return entity;
    }

    private void returnListDTO(List<InstProjectGuide> list, List<InstProjectGuideDTO> dTOList) {
        for (InstProjectGuide entity : list) {
            InstProjectGuideDTO dTO = new InstProjectGuideDTO();
            mapToDTO(dTO, entity);
            dTOList.add(dTO);
        }
    }

}
