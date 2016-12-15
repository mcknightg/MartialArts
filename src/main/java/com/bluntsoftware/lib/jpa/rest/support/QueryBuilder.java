package com.bluntsoftware.lib.jpa.rest.support;


import com.bluntsoftware.lib.jpa.repository.support.HqlBuilder;
import com.bluntsoftware.lib.jpa.rest.editor.HttpRequestedColumns;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * Created by Alexander Mcknight on 7/8/2015.
 *
 */
public class QueryBuilder<T> extends HqlBuilder {
    public enum Type {
        ICONTAINS("icn"),CONTAINS("cn"),BEGINS_WITH("bw"),ENDS_WITH("ew"),EQUAL_TO("eq"),
        NOT_EQUAL_TO("ne"),DOES_NOT_CONTAIN("nc"),DOES_NOT_END_WITH("en"),
        DOES_NOT_BEGIN_WITH("bn"),GREATER_THAN("gt"),GREATER_THAN_OR_EQUAL_TO("ge"),
        LESS_THAN("lt"),LESS_THAN_OR_EQUAL_TO("le"),IS_NULL("in"),IS_NOT_NULL("nn");
        private String name;
        Type(String name){this.name = name;}
        public String getName() { return name;}
        static public Type fromName(String name) throws Exception {
            for (Type type : Type.values()) {if (type.getName().equals(name)) {return type;}}
            throw new Exception(name + " is not supported");
        }
    }
    public static HashMap<String,Object> result( HttpServletRequest request,Page items){
        return result(
                HttpRequestedColumns.list(request, items.getContent()),
                items.getTotalElements(),
                items.getTotalPages(),
                items.getNumber()+1);
    }
    @SuppressWarnings("unchecked")
    public static HashMap<String,Object> result(List list,long totalRecords,long totalPages,long currentPage){
        HashMap<String,Object> result = new HashMap<String, Object>();
        List<Map<String,Object>> rows = new ArrayList<Map<String, Object>>();
        ObjectMapper mapper = new ObjectMapper();
        for(Object obj: list){
            Map<String, Object> objectAsMap = mapper.convertValue(obj, Map.class);
            rows.add(objectAsMap);
        }

        result.put("rows",rows );
        result.put("totalrecords",totalRecords);
        result.put("totalpages",totalPages);
        result.put("currpage",currentPage);
        return result;
    }
    private Boolean hasNoComplexTypes(List<String> listColumns){
        return !listColumns.contains(".");
    }

    public QueryBuilder(Class<T> clazz) {
        super(clazz);
    }

    public String getHql(ServletRequest servletRequest) {

        String filterByFields =  validString(servletRequest.getParameter("filterByFields"), "{}");
        String searchOperator = validString(servletRequest.getParameter("defaultsearchoper"),"eq");
        String sord = validString(servletRequest.getParameter("sord"),"ASC");
        String sidx = validString(servletRequest.getParameter("sidx"),"id");

        //Default and criteria
        String or = servletRequest.getParameter("or");
        if(or != null && or.equalsIgnoreCase("true")){
             setOr(true);
        }

        ObjectMapper mapper = new ObjectMapper();
        // Mongo DB Syntax IE - The compound query below selects all records where the
        // `status` equals "A" and either age is less than 30 or type equals 1:
        // {status: "A", $or: [ { age: { $lt: 30 } }, { type: 1 } ]}
        if(filterByFields != null && !filterByFields.equalsIgnoreCase("")){
            try {
                Map data = mapper.readValue(filterByFields,Map.class);
                buildCriteria(data,searchOperator);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        List<String> listColumns = HttpRequestedColumns.listColumns(servletRequest);
        if(!listColumns.isEmpty() && hasNoComplexTypes(listColumns)){
               this.setColumns(listColumns);
        }

        return this.getHqlQuery(sidx,sord);
    }

    private  void buildCriteria(Map map,String defaultSearchOperator){
        for(Object key:map.keySet()){
            Object value = map.get(key);
            if(value instanceof Map){
                buildCriteria(key.toString(),(Map)value);
            }else{
                criteriaExpression( key.toString(),defaultSearchOperator, value.toString());
            }
        }
    }
    private  HqlBuilder buildCriteria(String field,Map map){
        if(map.keySet().size() < 1){
            throw new RuntimeException(field + " value is empty");
        }
        for(Object obj:map.keySet()){
            String key = obj.toString();
            if(key.startsWith("$")){
                Object value = map.get(key);
                criteriaExpression(field, key.replace("$", ""), value.toString());
            }
        }
        return this;
    }

    private HqlBuilder criteriaExpression(String path, String searchOperator, String value) {
        try {
            switch(Type.fromName(searchOperator)){
                case EQUAL_TO: return eq(path,value);
                case NOT_EQUAL_TO: return not().eq(path,value);
                case CONTAINS: return cn(path,value);
                case ICONTAINS: return icn(path,value);
                case DOES_NOT_CONTAIN: return not().cn(path,value);
                case BEGINS_WITH: return bw(path,value);
                case DOES_NOT_BEGIN_WITH: return not().bw(path,value);
                case ENDS_WITH:  return cn(path,value);
                case DOES_NOT_END_WITH: return not().ew(path, value);
                case GREATER_THAN: return  gt(path, value);
                case GREATER_THAN_OR_EQUAL_TO: return ge(path,value);
                case LESS_THAN: return lt(path,value);
                case LESS_THAN_OR_EQUAL_TO: return le(path, value);
                case IS_NULL: return isNull(path);
                case IS_NOT_NULL: return notNull(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return this;
    }

    public PageRequest getPageRequest(ServletRequest servletRequest) {
        Integer page = validInteger(servletRequest.getParameter("page"),1);
        Integer rows = validInteger(servletRequest.getParameter("rows"),500);
        String  sord = validString(servletRequest.getParameter("sord"),"asc");
        String  sidx = validString(servletRequest.getParameter("sidx"),"id");
        Sort sort = new Sort(Sort.Direction.valueOf(sord.toUpperCase(Locale.US)),sidx);
        return new PageRequest(page-1,rows,sort);
    }
    private static String validString(String value, String defaultValue){
        if (isValidParameter(value)) {
            return value;
        }
        return defaultValue;
    }

    private static Integer validInteger(String value, Integer defaultValue){
        if (isValidParameter(value)) {
            try{
                return Integer.parseInt(value);
            }catch(Exception e){
                //
            }
        }
        return defaultValue;
    }
    private static boolean isValidParameter(String param) {
        return param != null && !param.isEmpty() && !param.equalsIgnoreCase("_empty") && !param.equalsIgnoreCase("undefined") && !param.equalsIgnoreCase("null");
    }

}
