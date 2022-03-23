/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.IsagUserBankService;
import com.isag.core.GenericDAO;
import com.isag.entity.GlBankTransactionDetail;
import com.isag.views.GlTransactionBankListView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author ahmed
 */
@Stateless
public class GlTransactionBankListViewImpl implements GlTransactionBankListViewService {

    @EJB
    private GenericDAO dao;
    @EJB
    private IsagUserBankService isagUserBankService;

    @Override
    public List<GlTransactionBankListView> getALLGlBankTransactionRecievableByBranchId(Integer branchId, Integer transactionType) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("transactionType", transactionType);
        return dao.findListByQuery("SELECT e FROM GlTransactionBankListView e WHERE e.branchId.id = :branchId AND e.transactionType =:transactionType order by e.serial DESC ", params);
    }

    @Override
    public List<GlTransactionBankListView> getALLGlBankTransactionRecievableByBranchId(Integer transactionType, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters) {
        filters.put("transactionType", transactionType);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT s FROM GlTransactionBankListView s WHERE s.branchId= :branchId AND s.transactionType =:transactionType  ");
        appendFilter(filters, branchId, builder);
        builder.append(" order by s.serial DESC");
        List<GlTransactionBankListView> details = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        return details;
    }

    @Override
    public List<GlTransactionBankListView> getALLGlTransactionBankListViewLazy(Integer transactionType, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters, Integer selectGlBankToId,Integer userId ) {
        filters.put("transactionType", transactionType);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT s , SUM(s.valueLocal) FROM GlBankTransactionDetail s  WHERE s.glBankTransactionId.branchId.id = :branchId AND s.glBankTransactionId.transactionType = :transactionType  ");
        appendFilterToLazy(filters, branchId,userId , builder, selectGlBankToId);
        builder.append(" GROUP BY s.bankIdFrom.id , s.bankIdFrom.name , s.bankIdFrom.code , s.bankIdTo.id ,s.bankIdTo.name , s.bankIdTo.code , s.glBankTransactionId.date , s.glBankTransactionId.remark , s.glBankTransactionId.branchId.id , s.glBankTransactionId.transactionType , s.glBankTransactionId.serial ,  s.glBankTransactionId.postFlag ,  s.glBankTransactionId.id ,  s.glBankTransactionId.rate , s.glBankTransactionId.paymentType order by s.glBankTransactionId.serial DESC");
        List<Object[]> result = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        List<GlTransactionBankListView> glTransactionBankListViews = new ArrayList<>();
        returnListView(result, glTransactionBankListViews);
        return glTransactionBankListViews;

    }

    public void appendFilter(Map<String, Object> filters, Integer branchId,Integer userId , StringBuilder builder, Integer selectGlBankToId) {
        filters.put("branchId", branchId);
        if (selectGlBankToId != null) {
            filters.put("selectGlBankToId", selectGlBankToId);
            builder.append(" and s.bankIdTo.id = :selectGlBankToId  ");
        }
        List<Integer> glBankIdList = isagUserBankService.getAllGlBankIdForUserByTypeAndBranchId(userId, branchId, 2);
        filters.put("glBankIdList", glBankIdList);
        builder.append(" AND s.bankIdTo.id in :glBankIdList ");

        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and (s.serial like CONCAT('%',:globalFilter ,'%')  OR s.date  like CONCAT('%',:globalFilter ,'%') OR s.bankFromName  like CONCAT('%',:globalFilter ,'%') OR s.bankToName like CONCAT('%',:globalFilter ,'%')  OR s.valueLocal like CONCAT('%',:globalFilter ,'%') OR s.remark like CONCAT('%',:globalFilter ,'%') ) ";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
    }
    
    public void appendFilterToLazy(Map<String, Object> filters, Integer branchId,Integer userId , StringBuilder builder, Integer selectGlBankToId) {
        filters.put("branchId", branchId);
        if (selectGlBankToId != null) {
            filters.put("selectGlBankToId", selectGlBankToId);
            builder.append(" and s.bankIdTo.id = :selectGlBankToId  ");
        }
        List<Integer> glBankIdList = isagUserBankService.getAllGlBankIdForUserByTypeAndBranchId(userId, branchId, 2);
        filters.put("glBankIdList", glBankIdList);
        builder.append(" AND s.bankIdFrom.id in :glBankIdList ");

        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and (s.serial like CONCAT('%',:globalFilter ,'%')  OR s.date  like CONCAT('%',:globalFilter ,'%') OR s.bankFromName  like CONCAT('%',:globalFilter ,'%') OR s.bankToName like CONCAT('%',:globalFilter ,'%')  OR s.valueLocal like CONCAT('%',:globalFilter ,'%') OR s.remark like CONCAT('%',:globalFilter ,'%') ) ";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
    }

    public void appendFilter(Map<String, Object> filters, Integer branchId, StringBuilder builder) {
        filters.put("branchId", branchId);
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and (s.serial like CONCAT('%',:globalFilter ,'%')  OR s.date  like CONCAT('%',:globalFilter ,'%') OR s.bankFromName  like CONCAT('%',:globalFilter ,'%') OR s.bankToName like CONCAT('%',:globalFilter ,'%')  OR s.valueLocal like CONCAT('%',:globalFilter ,'%') OR s.remark like CONCAT('%',:globalFilter ,'%') ) ";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
    }

    @Override
    public Long getTotalRegistors(Integer transactionType, Integer branchId, Map<String, Object> filters) {
        StringBuilder builder = new StringBuilder();
        filters.put("transactionType", transactionType);
        builder.append("SELECT count(s.rowNum) FROM GlTransactionBankListView s WHERE s.branchId = :branchId AND s.transactionType =:transactionType ");
        appendFilter(filters, branchId, builder);
        Long details = dao.findEntityByQuery(builder.toString(), filters);

        return details;
    }

    @Override
    public Long getTotalRegistorslazy(Integer transactionType, Integer branchId,Integer userId , Map<String, Object> filters, Integer selectGlBankToId) {
        StringBuilder builder = new StringBuilder();
        filters.put("transactionType", transactionType);
        builder.append("SELECT  coalesce(count(s), 0)  FROM GlBankTransactionDetail s  WHERE s.glBankTransactionId.branchId.id = :branchId AND s.glBankTransactionId.transactionType = :transactionType  ");
        appendFilter(filters, branchId,userId,  builder, selectGlBankToId);
        // builder.append(" GROUP BY s.bankIdFrom.id , s.bankIdFrom.name , s.bankIdFrom.code , s.bankIdTo.id ,s.bankIdTo.name , s.bankIdTo.code , s.glBankTransactionId.date , s.glBankTransactionId.remark  , s.glBankTransactionId.transactionType , s.glBankTransactionId.serial ,  s.glBankTransactionId.postFlag ,  s.glBankTransactionId.id ,  s.glBankTransactionId.rate , s.glBankTransactionId.paymentType ");
        Long details = dao.findEntityByQuery(builder.toString(), filters);
        return (details != null ? details : 0L);
    }

    private void returnListView(List<Object[]> list, List<GlTransactionBankListView> viewList) {

        for (Object[] entity : list) {
            viewList.add(mapTOView((GlBankTransactionDetail) entity[0], (BigDecimal) entity[1], null));
        }
    }

    private GlTransactionBankListView mapTOView(GlBankTransactionDetail entity, BigDecimal sumValueLocal, GlTransactionBankListView view) {
        if (view == null) {
            view = new GlTransactionBankListView();
        }
        initMapToView(view, sumValueLocal, entity);
        return view;
    }

    private void initMapToView(GlTransactionBankListView view, BigDecimal sumValueLocal, GlBankTransactionDetail entity) {

        if (entity.getBankIdFrom() != null) {
            view.setBankFromId(entity.getBankIdFrom().getId());
            view.setBankFromName(entity.getBankIdFrom().getName());
            view.setBankFromCode(entity.getBankIdFrom().getCode());
        }
        if (entity.getBankIdTo() != null) {
            view.setBankToId(entity.getBankIdTo().getId());
            view.setBankToName(entity.getBankIdTo().getName());
            view.setBankToCode(entity.getBankIdTo().getCode());
        }
        if (entity.getGlBankTransactionId() != null) {
            view.setRowNum(entity.getGlBankTransactionId().getId());
            view.setDate(entity.getGlBankTransactionId().getDate());
            view.setRemark(entity.getGlBankTransactionId().getRemark());
            view.setBranchId(entity.getGlBankTransactionId().getBranchId().getId());
            view.setTransactionType(entity.getGlBankTransactionId().getTransactionType());
            view.setSerial(entity.getGlBankTransactionId().getSerial());
            view.setPostFlag(entity.getGlBankTransactionId().getPostFlag());
            view.setId(entity.getGlBankTransactionId().getId());
            view.setRate(entity.getGlBankTransactionId().getRate());
            view.setPaymentType(entity.getGlBankTransactionId().getPaymentType());
        }
        view.setValueLocal(sumValueLocal);

    }

}
