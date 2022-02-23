/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.core;

import com.isag.entity.InvItem;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;

/**
 *
 * @author hq004
 */
@Local
public interface GenericDAO {

    <T> List<T> findAll(Class<T> entityClass);

    <T> T findEntityById(Class<T> entityClass, Serializable id);

    <T> T findEntityByCompany(Class<T> entityClass, Serializable id, Serializable companyId);

    <T> T findEntityByCompany(Class<T> entityClass, Serializable companyId);

    <T> List<T> findEntityListByCompanyId(Class<T> entityClass, Serializable companyId);

    <T> T findEntityByNamedQuery(String namedQueryName);

    <T> T findEntityByNamedQuery(String namedQueryName,
            Map<String, Object> params);

    <T> List<T> findListByNamedQuery(String namedQueryName);

    <T> List<T> findListByNamedQuery(String namedQueryName,
            Map<String, Object> params);

    <T> List<T> findListByNamedQuery(String namedQueryName, int start,
            int maxSize);

    <T> List<T> findListByNamedQuery(String namedQueryName,
            Map<String, Object> params, int start, int maxSize);

    <T> T findEntityByQuery(String namedQueryName);

    <T> T findEntityByQuery(String namedQueryName, Map<String, Object> params);

    <T> List<T> findListByQuery(String namedQueryString);

    <T> List<T> findListByQuery(String namedQueryString,
            Map<String, Object> params);

    <T> List<T> findListByQuery(String namedQueryString, int start, int maxSize);

    <T> List<T> findListByQuery(String namedQueryString,
            Map<String, Object> params, int start, int maxSize);

    <T> void saveEntity(T entity);

    <T> T updateEntity(T entity);

    <T> void deleteEntity(T entity);

    void executeDeleteQuery(String query);

    void executeUpdateQuery(String jpqlQuery);

    public void executeUpdateQuery(String jpqlQuery, Map<String, Object> params);

    <T> List<T> executeNativeQuery(String sqlQuery);

    <T> List<T> executeJPQLQuery(String jpqlQuery);

    void checkAndQuery(StringBuilder stringBuilder);

    public void rollbackQuery();

    public List<InvItem> creteQueryInvItemView(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters);

    public void StoredProcedure(String storedName, List<storedProcedureParameter> procedureParameters, Map<String, Object> parm);
}
