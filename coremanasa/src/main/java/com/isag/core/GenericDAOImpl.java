/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.core;

import com.isag.entity.BaseEntity;
import javax.ejb.Stateless;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.SessionContext;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author hq004
 */
@Stateless
public class GenericDAOImpl implements GenericDAO {

    @PersistenceContext(name = "ISAG_PU")
    private EntityManager em;
    @Resource
    private SessionContext ctx;

    @PreDestroy
    public void destruct() {
        em.close();
    }

    @Override
    public <T> List<T> findAll(Class<T> entityClass) {
        return executeJPQLQuery("SELECT entity FROM " + entityClass.getSimpleName() + " entity");
    }

    @Override
    public <T> T findEntityById(Class<T> entityClass, Serializable id) {
        return em.find(entityClass, id);
    }

    @Override
    public <T> T findEntityByNamedQuery(String namedQueryName) {
        return findEntityByNamedQuery(namedQueryName, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T findEntityByNamedQuery(String namedQueryName,
            Map<String, Object> params) {
        Query query = em.createNamedQuery(namedQueryName);

        if (params != null) {
            for (Entry<String, Object> entry : params.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }

        if (query.getResultList() != null && !query.getResultList().isEmpty()) {
            return (T) query.getResultList().get(0);
        } else {
            return null;
        }

    }

    @Override
    public <T> List<T> findListByNamedQuery(String namedQueryName) {
        return findListByNamedQuery(namedQueryName, null, -1, -1);
    }

    @Override
    public <T> List<T> findListByNamedQuery(String namedQueryName,
            Map<String, Object> params) {
        return findListByNamedQuery(namedQueryName, params, -1, -1);
    }

    @Override
    public <T> List<T> findListByNamedQuery(String namedQueryName, int start,
            int maxSize) {
        return findListByNamedQuery(namedQueryName, null, start, maxSize);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findListByNamedQuery(String namedQueryName,
            Map<String, Object> params, int start, int maxSize) {
        Query query = em.createNamedQuery(namedQueryName);
        if (start > -1) {
            query.setFirstResult(start);
        }
        if (maxSize > -1) {
            query.setMaxResults(maxSize);
        }

        if (params != null) {
            for (Entry<String, Object> entry : params.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }

        return query.getResultList();
    }

    @Override
    public <T> T findEntityByQuery(String QueryName) {
        return findEntityByQuery(QueryName, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T findEntityByQuery(String QueryName, Map<String, Object> params) {
        Query query = em.createQuery(QueryName);

        if (params != null) {
            for (Entry<String, Object> entry : params.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }

        if (query.getResultList() != null && !query.getResultList().isEmpty()) {
            return (T) query.getResultList().get(0);
        } else {
            return null;
        }

    }

    @Override
    public <T> List<T> findListByQuery(String QueryName) {
        return findListByQuery(QueryName, null, -1, -1);
    }

    @Override
    public <T> List<T> findListByQuery(String QueryName,
            Map<String, Object> params) {
        return findListByQuery(QueryName, params, -1, -1);
    }

    @Override
    public <T> List<T> findListByQuery(String QueryName, int start, int maxSize) {
        return findListByQuery(QueryName, null, start, maxSize);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findListByQuery(String QueryName,
            Map<String, Object> params, int start, int maxSize) {
        Query query = em.createQuery(QueryName);
        if (start > -1) {
            query.setFirstResult(start);
        }
        if (maxSize > -1) {
            query.setMaxResults(maxSize);
        }

        if (params != null) {
            for (Entry<String, Object> entry : params.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }

        return query.getResultList();
    }

    @Override
    public <T> void saveEntity(T entity) {
        em.persist(entity);
        em.flush();
    }

    @Override
    public <T> T updateEntity(T entity) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(entity);
        if (constraintViolations.size() > 0) {
            Iterator<ConstraintViolation<T>> iterator = constraintViolations.iterator();
            while (iterator.hasNext()) {
                ConstraintViolation<T> cv = iterator.next();
                System.err.println(cv.getRootBeanClass ().getName() + "." + cv.getPropertyPath() + " " + cv.getMessage());
            }
        } else {
            T e = em.merge(entity);
            em.flush();

            return e;
        }
        return null;
    }

    @Override
    public <T> void deleteEntity(T entity) {
        if (entity instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) entity;
            Object managedEntity = em.find(entity.getClass(), baseEntity.getId());
            em.remove(managedEntity);
        } else {
            em.remove(entity);
        }
    }

    @Override
    public void executeDeleteQuery(String jpqlQuery) {
        Query query = em.createQuery(jpqlQuery);
        query.executeUpdate();
    }

    @Override
    public void executeUpdateQuery(String jpqlQuery) {
        Query query = em.createQuery(jpqlQuery);
        query.executeUpdate();
    }

    @Override
    public void executeUpdateQuery(String jpqlQuery, Map<String, Object> params) {
        Query query = em.createQuery(jpqlQuery);
        if (params != null) {
            for (Entry<String, Object> entry : params.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        query.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> executeNativeQuery(String jpqlQuery) {
        try {
            return em.createNativeQuery(jpqlQuery).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> executeJPQLQuery(String jpqlQuery) {
        return em.createQuery(jpqlQuery).getResultList();
    }

    @Override
    public <T> T findEntityByCompany(Class<T> entityClass, Serializable id, Serializable companyId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> List<T> findEntityListByCompanyId(Class<T> entityClass, Serializable companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        return findListByQuery("SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e.companyId.id=:companyId", params);
    }

    @Override
    public <T> T findEntityByCompany(Class<T> entityClass, Serializable companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        return findEntityByQuery("SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e.companyId.id=:companyId", params);
    }

    @Override
    public void checkAndQuery(StringBuilder stringBuilder) {
        if (stringBuilder.toString().equals("")) {
            stringBuilder.append(" where ");
        } else {
            stringBuilder.append(" and ");
        }
    }

    @Override
    public void rollbackQuery() {
        ctx.setRollbackOnly();
    }

    public void StoredProcedure(String storedName, List<storedProcedureParameter> procedureParameters, Map<String, Object> parm) {
        StoredProcedureQuery query = em
                .createStoredProcedureQuery(storedName);
        if (procedureParameters != null || !procedureParameters.isEmpty()) {
            for (storedProcedureParameter parameter : procedureParameters) {

                if (parameter.getParameterType().equals('i')) {
                    query.registerStoredProcedureParameter(
                            parameter.getParameterName(),
                            parameter.getParameterClass(),
                            ParameterMode.IN
                    );
                } else if (parameter.getParameterType().equals('o')) {
                    query.registerStoredProcedureParameter(
                            parameter.getParameterName(),
                            parameter.getParameterClass(),
                            ParameterMode.OUT
                    );
                }
            }

        }
//                query.registerStoredProcedureParameter(
//                       "branchId",
//                       Integer.class,
//                        ParameterMode.IN
//                );
//                query.registerStoredProcedureParameter(
//                       "orgNameFrom",
//                       Integer.class,
//                        ParameterMode.IN
//                );
//                query.registerStoredProcedureParameter(
//                       "orgNameTo",
//                       Integer.class,
//                        ParameterMode.IN
//                );
//                query.registerStoredProcedureParameter(
//                       "fromDate",
//                        Date.class,
//                        ParameterMode.IN
//                );
//                query.registerStoredProcedureParameter(
//                       "toDate",
//                       Date.class,
//                        ParameterMode.IN
//                );
//                query.registerStoredProcedureParameter(
//                       "status",
//                       Integer.class,
//                        ParameterMode.IN
//                );
//                query.registerStoredProcedureParameter(
//                       "projectGuideFrom",
//                       Integer.class,
//                        ParameterMode.IN
//                );
//                query.registerStoredProcedureParameter(
//                       "projectGuideTo",
//                       Integer.class,
//                        ParameterMode.IN
//                ); 
//                query.registerStoredProcedureParameter(
//                       "userID",
//                       Integer.class,
//                        ParameterMode.IN
//                );  
        for (Entry<String, Object> entry : parm.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());

        }
        query.execute();
    }

}
