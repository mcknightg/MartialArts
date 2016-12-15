package com.bluntsoftware.lib.jpa.rest.impl;


import com.bluntsoftware.lib.jpa.domain.Domain;
import com.bluntsoftware.lib.jpa.repository.GenericRepository;
import com.bluntsoftware.lib.jpa.repository.Impl.GenericRepositoryImpl;
import com.bluntsoftware.lib.jpa.rest.editor.GenericPropertyEditor;
import com.bluntsoftware.lib.jpa.rest.support.QueryBuilder;
import com.bluntsoftware.lib.jpa.rest.GenericRestController;
import com.bluntsoftware.lib.web.filter.module.ModuleControllerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditorSupport;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Alexander Mcknight
 * Date: 6/28/15
 * Time: 5:10 AM
 */

public abstract class JpaCRUDRestControllerImpl<T extends Domain,ID extends Serializable, X extends GenericRepository<T,ID>> extends GenericRestController<T,ID> {

    private final Logger log = LoggerFactory.getLogger(JpaCRUDRestControllerImpl.class);
    @Autowired
    protected GenericRepository<T,ID> repository;

    public void setRepository(X repository) {
        this.repository = repository;
    }
    public GenericRepository<T,ID> getRepository() {
        return repository;
    }

    @Transactional(readOnly = false)
    @Override
    public T saveUpdate(HttpServletRequest request,@RequestBody  Map<String,Object> object) throws Exception {

        if(!object.containsKey("id")){
            object.put("id",null);
        }
        T incomingObject = transformPayload(object);
        ID id = (ID)incomingObject.getId();
        if(id != null){
            T oldObject = repository.findOne(id);
            copyBeanProperties(incomingObject,oldObject,object.keySet());
            incomingObject = oldObject;
        }
        return this.save((ID)incomingObject.getId(),request,incomingObject);
    }
    private Object getPojo(Domain pojo){
        ID id = (ID)pojo.getId();
        if(id != null){
            GenericPropertyEditor pe =  GenericRepositoryImpl.propertyEditors.get(pojo.getClassType());
            Object ret = pe.getGenericRepository().findOne(id);
            if(ret != null ){
                return ret;
            }
        }
        return pojo;
    }

    private  void copyBeanProperties(final Object source, final Object target, final Iterable<String> properties){
        final BeanWrapper src = new BeanWrapperImpl(source);
        final BeanWrapper trg = new BeanWrapperImpl(target);
        for(PropertyDescriptor pd:src.getPropertyDescriptors()){
            if(!pd.getPropertyType().isMemberClass()){
                Object val =  src.getPropertyValue(pd.getName());
                if(val != null && val instanceof Domain){
                    trg.setPropertyValue(pd.getName(),getPojo((Domain)val));
                }
            }
        }
        trg.setAutoGrowNestedPaths(true);
        for(final String propertyName : properties){
            trg.setPropertyValue(propertyName,src.getPropertyValue(propertyName));
        }
    }

    @Transactional
    private T transformPayload(Map<String, ?> payload) throws Exception {
        DataBinder binder = new DataBinder(BeanUtils.instantiate(repository.getDomainClass()));
        registerPropertyEditors(binder);
        MutablePropertyValues values = new MutablePropertyValues();
        values.addPropertyValues(payload);
        binder.bind(values);
        T target = (T)BeanUtils.instantiate(repository.getDomainClass());
        copyBeanProperties((T)binder.getTarget(),target,payload.keySet());
        return target;
    }

    @Override
    public T remove(T item, HttpServletRequest request) throws Exception {
        repository.delete(item);
        return item;
    }

    @Override
    public T create(T object, HttpServletRequest request) throws Exception {
        return repository.saveAndUpdate(object);
    }

    @Override
    public T update(T object, HttpServletRequest request) throws Exception {
        return repository.save(object);
    }

    @Override
    public T get(T object, HttpServletRequest request) throws Exception {
        return object;
    }

    @Override
    protected T save(@PathVariable("id") ID id, HttpServletRequest request, @RequestBody T object) throws Exception {
          if(id == null){
              return create(object,request);
          }else{
              return update(object,request);
          }
    }

    @Override
    public HashMap<String,Object> findAll(HttpServletRequest request) throws Exception {
        QueryBuilder<T> sp = new QueryBuilder<T>(repository.getDomainClass());
        String hql = sp.getHql(request);


        PageRequest pageRequest = sp.getPageRequest(request);
        List<Object> parameters = sp.getParameters();
        Page<T> items = repository.findAll(hql,parameters,pageRequest);
        return QueryBuilder.result(request, items);
    }

    @Override
    public T findOne(@PathVariable("id") ID id,HttpServletRequest request) throws Exception {
        return  get(repository.findOne(id),request);
    }

    @Override
    public Object delete(@PathVariable("id") ID id,HttpServletRequest request) throws Exception {
        T item = repository.findOne(id);
        return remove(item,request);
    }

    @Override
    public Map<String, Object> showSchema() {
        return repository.getSchema();
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        registerPropertyEditors(binder);
    }

    private void registerPropertyEditors(DataBinder binder){

      for(Class klass: GenericRepositoryImpl.propertyEditors.keySet()){
            binder.registerCustomEditor(klass, GenericRepositoryImpl.propertyEditors.get(klass));
      }

     //   binder.registerCustomEditor(repository.getDomainClass(), repository.getPropertyEditor());
       // binder.registerCustomEditor(Boolean.class, new CustomBooleanEditor(false));
        binder.registerCustomEditor(Integer.class, new MyCustomNumberEditor(Integer.class, true));
        binder.registerCustomEditor(Long.class, new MyCustomNumberEditor(Long.class, true));
        binder.registerCustomEditor(BigDecimal.class, new MyCustomNumberEditor(BigDecimal.class, true));
        binder.registerCustomEditor(java.sql.Time.class,new PropertyEditorSupport() {
            public void setAsText(String text) throws IllegalArgumentException {
                if (isValidParameter(text)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    try {
                        setValue(new java.sql.Time(sdf.parse(text).getTime()));
                    } catch (ParseException e) {
                        sdf = new SimpleDateFormat("HH:mm");
                        try {
                            setValue(new java.sql.Time(sdf.parse(text).getTime()));
                        } catch (ParseException e1) {
                            throw new IllegalArgumentException("Could not parse date: " + e1.getMessage(), e1);
                        }
                    }
                } else {
                    setValue(null);
                }
            }

            public String getAsText() {
                java.sql.Time value = (java.sql.Time) getValue();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                return (value != null ? sdf.format(new Date(value.getTime())) : "");
            }
        });
      //  binder.registerCustomEditor(Date.class,new LocaleDateTimeEditor("yyyy-MM-dd",true));
        binder.registerCustomEditor(Date.class,new PropertyEditorSupport() {
            public void setAsText(String text) throws IllegalArgumentException {
                if (isValidParameter(text)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        setValue(sdf.parse(text));
                    } catch (ParseException e) {
                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            setValue(sdf.parse(text));
                        } catch (ParseException e1) {
                            throw new IllegalArgumentException("Could not parse date: " + e1.getMessage(), e1);
                        }
                    }
                } else {
                    setValue(null);
                }
            }

            public String getAsText() {
                Date value = (Date) getValue();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                return (value != null ? sdf.format(value) : "");
            }
        });
    }

    protected Map<String, String> validationMessages(Set<ConstraintViolation<T>> failures) {
        Map<String, String> failureMessages = new HashMap<String, String>();
        for (ConstraintViolation<T> failure : failures) {
            failureMessages.put(failure.getPropertyPath().toString(), failure.getMessage());
        }
        return failureMessages;
    }
    private static boolean isValidParameter(String param) {
        return param != null && !param.isEmpty() && !param.equalsIgnoreCase("_empty") && !param.equalsIgnoreCase("undefined") && !param.equalsIgnoreCase("null");
    }


    private static class MyCustomNumberEditor extends CustomNumberEditor {
        MyCustomNumberEditor(Class clazz, boolean bool) throws IllegalArgumentException {
            super(clazz, bool);
        }
        public void setAsText(String text) {
            if (isValidParameter(text)) {
                super.setAsText(text);
            } else {
                super.setAsText(null);
            }
        }
    }
    //List Columns
    @Override
    public Map<String, Set<String>> columns(){
        return repository.listColumns();
    }

    /* Begin Restful Navigation */
    private String name = "";
    private String qualifier= "";
    public String getName() {return name;}
    public void setName(String name) { this.name = name;}
    public static List<String> getControllerName(String qualifier) { return new ArrayList<String>(controllerNames.get(qualifier));}
    public final static Map<String,List<String>> controllerNames=  Collections.synchronizedMap(new HashMap<String, List<String>>());

    public JpaCRUDRestControllerImpl() {
        if (getClass().isAnnotationPresent(RequestMapping.class)) {
            RequestMapping mapping = getClass().getAnnotation(RequestMapping.class);
            String mapVal = mapping.value()[0];
            qualifier = mapVal.substring(0, mapVal.lastIndexOf("/")).replace("/", "");
            name = mapVal.substring(mapVal.lastIndexOf("/"), mapVal.length()).replace("/", "");
            if (!name.contains("{")) {
                addControllerEntity(qualifier, name);
            }
        }
    }
    private static void addControllerEntity(String qualifier, String name) {
        List<String> names = controllerNames.get(qualifier);
        if(names == null){
            names = new ArrayList<String>();
            controllerNames.put(qualifier,names);
        }
        names.add(name);
    }

    public Map<String, Object> getApi(HttpServletRequest request) {
        return ModuleControllerImpl.config(qualifier,name,request);
    }
    /* End Restful Navigation */
}
