package com.bluntsoftware.lib.jpa.repository;


import com.bluntsoftware.lib.jpa.domain.Domain;
import com.bluntsoftware.lib.jpa.repository.support.HqlBuilder;
import com.bluntsoftware.lib.jpa.rest.editor.GenericPropertyEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Alexander Mcknight
 * Date: 6/29/15
 * Time: 1:38 AM.
 */
@NoRepositoryBean
public interface GenericRepository<T extends Domain, ID extends Serializable> extends JpaRepository<T, ID>,JpaSpecificationExecutor<T> {

    Class getDomainClass();
    EntityManager getEntityManager();
    Map<String, Set<String>> listColumns();
    Map<String, Object> getSchema();

    //Pageable Find All
    Page<T> findAll(String hql, Object val, Pageable pageable);
    Page<T> findAll(String hql, List<Object> hqlParameters, Pageable pageable);
    Page<T> findAll(HqlBuilder hqlBuilder, Pageable pageable);

    //Find All
    List<T> findAll(String hql, List<Object> hqlParameters);
    List<T> findAll(String hql, Object val);
    List<T> findAll(HqlBuilder  hqlBuilder);

    //Find One
    T findOne(HqlBuilder  hqlBuilder);
    T findOne(String hql, Object val);
    T findOne(String hql, List<Object> hqlParameters);

    T saveAndUpdate(T entity);

    GenericPropertyEditor<T,ID> getPropertyEditor();



}
