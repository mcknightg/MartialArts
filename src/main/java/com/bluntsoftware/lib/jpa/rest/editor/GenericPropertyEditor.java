package com.bluntsoftware.lib.jpa.rest.editor;

import com.bluntsoftware.lib.jpa.domain.Domain;
import com.bluntsoftware.lib.jpa.repository.GenericRepository;
import com.bluntsoftware.lib.jpa.repository.support.HqlBuilder;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ClassUtils;


import java.beans.PropertyEditorSupport;
import java.io.Serializable;
import java.util.Map;


/**
 * Created by Alexander Mcknight on 7/6/2015.
 *
 */
public class GenericPropertyEditor <T  extends Domain,ID extends Serializable > extends PropertyEditorSupport {
    private final Logger log = LoggerFactory.getLogger(com.bluntsoftware.lib.jpa.rest.editor.GenericPropertyEditor.class);
    private GenericRepository<T,ID> genericRepository;
    private Class idClass;
    public GenericPropertyEditor(GenericRepository<T,ID> genericRepository) {
        this.genericRepository = genericRepository;
        try {
            BeanWrapperImpl  beanWrapper = new BeanWrapperImpl(genericRepository.getDomainClass().newInstance());
            this.idClass = beanWrapper.getPropertyType("id");
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public GenericRepository<T, ID> getGenericRepository() {
        return genericRepository;
    }

    public void setGenericRepository(GenericRepository<T, ID> genericRepository) {
        this.genericRepository = genericRepository;
    }

    @Override
    public Object getValue() {

        return super.getValue();
    }

    private T convertMap(Map value){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return (T) objectMapper.convertValue(value, genericRepository.getDomainClass());
    }
    private ID convertId(Map value){
        if(value.containsKey("id")){
            return (ID) HqlBuilder.stringToObject(value.get("id").toString(), idClass);
        }
        return null;
    }

    @Override
    public void setValue(Object value) {

        if(value != null &&  value instanceof Map){
            Map mapValue = (Map)value;
            ID id = convertId(mapValue);
            if(id != null){
                T existingObject = genericRepository.findOne(id);
                if(existingObject != null){
                    value =  existingObject;
                }else{
                    value = genericRepository.saveAndUpdate(convertMap(mapValue));
                }
            }
        }else if(value != null && ClassUtils.isPrimitiveOrWrapper(value.getClass())){
            ID id = (ID) HqlBuilder.stringToObject(value.toString(), idClass);
            if(id != null) {
                T existingObject = genericRepository.findOne(id);
                if(existingObject != null){
                    value = existingObject;
                }
            }
        }
        super.setValue(value);
    }

    public String getAsText() {
        T obj = (T)getValue();
        if (obj == null) {
            return null;
        }
        BeanWrapperImpl  beanWrapper = new BeanWrapperImpl(obj);
        Object id = beanWrapper.getPropertyValue("id");
        if(id != null){
            return id.toString();
        }
        return null;
    }

    public void setAsText(String text) {

        if (text == null || text.length() == 0) {
            setValue(null);
            return;
        }
        Object idObject = HqlBuilder.stringToObject(text,idClass);
        if(idObject instanceof Serializable){
            Object obj = genericRepository.findOne((ID)idObject);
            setValue(genericRepository.getEntityManager().merge(obj));
        }

    }

}
