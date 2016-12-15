package com.bluntsoftware.lib.jpa.util;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.beans.Introspector;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class DomainSchema {
    private Class clazz;
    private boolean caseInsensitive = false;
    private Integer recursionLimit = 4;

    public DomainSchema(Class clazz) {
        this.clazz = clazz;
    }

    public static void main(String[] args) {
        DomainSchema domainSchema = new DomainSchema(DomainSchema.class);
        domainSchema.setRecursionLimit(4);
        if(!domainSchema.isCaseInsensitive()){
            domainSchema.setCaseInsensitive(true);
        }
    }

    private void setRecursionLimit(Integer recursionLimit) {
        this.recursionLimit = recursionLimit;
    }

    private boolean isCaseInsensitive() {
        return caseInsensitive;
    }

    private void setCaseInsensitive(boolean caseInsensitive) {
        this.caseInsensitive = caseInsensitive;
    }

    public Set< String > getNestedColumnNames() {
        Set< String > columns = new HashSet< String >();
        if (clazz != null) {
            getNestedColumnNames("", columns, clazz, 0);
        }
        return columns;
    }

    private void getNestedColumnNames(String prefix, Set<String> columns, Class clazz, int level) {
        for (Method method : clazz.getDeclaredMethods()) {
            boolean transientM = method.isAnnotationPresent(Transient.class);
            boolean o2m = method.isAnnotationPresent(OneToMany.class);
            boolean m2o = method.isAnnotationPresent(ManyToOne.class);
            String methodName = method.getName();
            String columnName = null;
            if (!transientM && !o2m && !method.isSynthetic()) {
                if (methodName.startsWith("is")) {
                    columnName = Introspector.decapitalize(methodName.substring(2));
                } else if (methodName.startsWith("get")) {
                    columnName = Introspector.decapitalize(methodName.substring(3));
                }

                if (columnName != null) {
                    boolean addColumnName = true;
                    boolean stepIntoReference = false;
                    if (m2o) {
                        stepIntoReference = true;
                        if (level >= recursionLimit || (clazz == method.getReturnType() && (prefix.endsWith(columnName + ".") && (prefix.length() == columnName.length() + 1 || prefix.charAt(prefix.length() - columnName.length() - 2) == '.')))) {
                            addColumnName = false;
                            stepIntoReference = false;
                        }
                    }
                    if (addColumnName) {
                        if (caseInsensitive) {
                            columns.add((prefix + columnName).toLowerCase());
                        } else {
                            columns.add(prefix + columnName);
                        }
                    }
                    if (stepIntoReference) {
                        getNestedColumnNames(prefix + columnName + ".", columns, method.getReturnType(), level + 1);
                    }
                }
            }
        }
    }

    public Map<String, Object> getSchema()  {
        Map<String, Object> root = new HashMap<String, Object>();
        Set< String > nestedColumnNames = getNestedColumnNames();
        for(String columnName:nestedColumnNames){
             Map<String, Object> elm = root;
             for(String name:columnName.split("\\.")){
                 if(columnName.endsWith(name)){
                     Object text = elm.get(name);
                     if(text == null) {
                         elm.put(name, "");
                     }
                 } else{
                     Object map = elm.get(name);
                     if(map == null){
                         elm.put(name,new HashMap<String, Object>());
                         map = elm.get(name);
                     }

                     if(map instanceof Map){
                         elm = (Map)map;
                     }
                 }
             }
        }

        return root;
    }
}
