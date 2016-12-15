package com.bluntsoftware.lib.jpa.repository.Impl;


import com.bluntsoftware.lib.jpa.domain.Domain;
import com.bluntsoftware.lib.jpa.repository.GenericRepository;
import com.bluntsoftware.lib.jpa.repository.support.HqlBuilder;
import com.bluntsoftware.lib.jpa.rest.editor.GenericPropertyEditor;
import com.bluntsoftware.lib.jpa.util.DomainSchema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.io.Serializable;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Alexander Mcknight
 * Date: 6/29/15
 * Time: 6:27 PM
 */
@NoRepositoryBean
public class GenericRepositoryImpl<T extends Domain,ID extends Serializable> extends SimpleJpaRepository<T,ID> implements GenericRepository<T,ID> {
    private Class<T> domainClass;


    private EntityManager entityManager;


    GenericPropertyEditor<T,ID> propertyEditor;

    public final static Map<Class, GenericPropertyEditor> propertyEditors =  Collections.synchronizedMap(new HashMap<Class,GenericPropertyEditor>());
    public static void addPropertyEditor(Class domainClass,GenericPropertyEditor propertyEditor) {
        propertyEditors.put(domainClass,propertyEditor);
    }

    public GenericRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.domainClass = domainClass;
        this.entityManager = entityManager;
        this.propertyEditor = new GenericPropertyEditor<T,ID>(this);
        GenericRepositoryImpl.addPropertyEditor(domainClass,propertyEditor);
    }

    public Class<T> getDomainClass() {
        return domainClass;
    }

    @Override
    public Map<String, Set<String>> listColumns() {
        DomainSchema columnNamesFromObject = new DomainSchema(getDomainClass());
        return Collections.singletonMap("columns", columnNamesFromObject.getNestedColumnNames());
    }

    @Override
    public Map<String, Object> getSchema() {
        return new DomainSchema(getDomainClass()).getSchema();
    }

    @Override
    public Page<T> findAll(String hql, Object val, Pageable pageable) {
        return findAll(hql,listOfOne(val),pageable);
    }
    List<Object> listOfOne(Object obj){
        List<Object> ret = new ArrayList<Object>();
        ret.add(obj);
        return ret;
    }
    @Override
    public Page<T> findAll(String hql, List<Object> hqlParameters, Pageable pageable) {

        if(hqlParameters == null){
            hqlParameters = new ArrayList<Object>();
        }

        EntityManager em = this.getEntityManager();
        TypedQuery<T> query = em.createQuery(hql,getDomainClass());
        //Are we Paging
        if(pageable != null){
            query.setFirstResult(pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }

        int parameterCount = 1;
        for (Object sqlParameter : hqlParameters) {
            query.setParameter(parameterCount++, sqlParameter);
        }

        if(pageable != null){
            String originalQuery = query.unwrap(org.hibernate.Query.class).getQueryString();
            String countQuery = QueryUtils.createCountQueryFor(originalQuery);
            if(!countQuery.contains("count")){
                countQuery = "select count(t) " + countQuery + " as t";
            }
            TypedQuery<Long> count = em.createQuery(countQuery,Long.class);
            parameterCount = 1;
            for (Object sqlParameter : hqlParameters) {
                count.setParameter(parameterCount++, sqlParameter);
            }
            //List<T> contentData = query.getResultList();
            Long total = QueryUtils.executeCountQuery(count);
            List<T> content = total > pageable.getOffset() ? query.getResultList() : Collections.<T> emptyList();
            return new PageImpl<T>(content, pageable,total ); //content.size()
        } else{

            List<T> results = query.getResultList();

            return new PageImpl<T>(results);
        }
    }

    @Override
    public Page<T> findAll(HqlBuilder hqlBuilder, Pageable pageable) {
        return findAll(hqlBuilder.getHqlQuery(),hqlBuilder.getParameters(),pageable);
    }

    @Override
    public List<T> findAll(String hql, List<Object> hqlParameters) {
        return findAll(hql,hqlParameters,null).getContent();
    }

    @Override
    public List<T> findAll(String hql, Object val) {
        return findAll(hql,listOfOne(val));
    }

    @Override
    public List<T> findAll(HqlBuilder hqlBuilder) {
        return findAll(hqlBuilder.getHqlQuery(),hqlBuilder.getParameters());
    }

    @Override
    public T findOne(HqlBuilder hqlBuilder) {
        return findOne(hqlBuilder.getHqlQuery(), hqlBuilder.getParameters());
    }

    @Override
    public T findOne(String hql, Object val) {
        return findOne(hql,listOfOne(val));
    }

    @Override
    public T findOne(String hql, List<Object> hqlParameters) {
        List<T> list = findAll(hql,hqlParameters);
        return (list.size() == 0 ? null : list.get(0));
    }

    @Override
    @Transactional
    public T saveAndUpdate(T entity) {
         if(((Domain)entity).getId() == null){
            entity =  entityManager.merge(entity);
        }
        return  save(entity);
    }

    @Override
    public GenericPropertyEditor<T, ID> getPropertyEditor() {
        return  this.propertyEditor;
    }


    public EntityManager getEntityManager() {
        return entityManager;
    }

}
