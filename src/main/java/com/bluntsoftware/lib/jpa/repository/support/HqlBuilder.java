package com.bluntsoftware.lib.jpa.repository.support;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapperImpl;


public class HqlBuilder {
    private Boolean or = false;
    private boolean not = false;
    static String dateFormat ="yyyy/MM/dd";
    String alias = "t";

    BeanWrapperImpl beanWrapper = null;
    protected List<Object> parameters = new ArrayList<Object>();
    private String selectFrom;
    protected StringBuilder sb;
    private List<String> columns = new ArrayList<String>();
    private Logger logger = LoggerFactory.getLogger(getClass());
    protected Class  clazz;
    public HqlBuilder(Class clazz) {

        this.clazz =  clazz;
        sb = new StringBuilder();
        this.selectFrom = "from " + clazz.getSimpleName();
        if(alias != null){
            this.selectFrom +=  " AS " + alias;
        }
        try {
            beanWrapper = new BeanWrapperImpl(clazz.newInstance());
        } catch (InstantiationException exception) {
            exception.printStackTrace();
            throw new IllegalStateException("cannot create instance of class");
        } catch (IllegalAccessException exception) {
            exception.printStackTrace();
            throw new IllegalStateException("not allowed to create instance of class");
        }
        beanWrapper.setAutoGrowNestedPaths(true);
    }

    public Boolean isOr() {
        return or;
    }

    public void setOr(Boolean or) {
        this.or = or;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public HqlBuilder(String selectFrom, String where) {
        this.selectFrom = selectFrom;
        sb = new StringBuilder(where);
    }

    enum Operator {
        ieq("upper(cast(", " as string)) =", ""),
        lt("<"),le("<="),eq("="),ge(">="),gt(">"),
        cn("cast(", " as string) LIKE CONCAT('%',", ",'%')"),
        icn("upper(cast(", " as string)) LIKE CONCAT('%',",",'%')"),
        ew("cast(", " as string) LIKE CONCAT('%',", ")"),
        iew("upper(cast(", " as string)) LIKE CONCAT('%',",")"),
        bw("cast(", " as string) LIKE CONCAT(", ",'%')"),
        ibw("upper(cast(", " as string)) LIKE CONCAT(",",'%')"),
        in(""," in (", ")"),or("("," or ", ")");

        String sqlOperator,sqlPropertyPrefix,sqlValueSuffix;
        Operator(String sqlOperator) {
            this("", sqlOperator, "");
        }
        Operator(String sqlPropertyPrefix, String sqlOperator, String sqlValueSuffix) {
            this.sqlPropertyPrefix = sqlPropertyPrefix;
            this.sqlValueSuffix = sqlValueSuffix;
            this.sqlOperator = sqlOperator;
        }
        Boolean isStringCompare(){
           return  new HashSet< Enum >(Arrays.asList( ieq,cn,icn,ibw,bw,iew,ew)).contains(this);
        }

    }
    public List<Object> getParameters() {
        return parameters;
    }

    public class SubQueryBuilder extends HqlBuilder {
        public SubQueryBuilder(){
            this("");
        }
        public SubQueryBuilder(String where) {
            super(null,where);
            parameters = HqlBuilder.this.parameters;
        }
    }
    public HqlBuilder not(){
        not = true;
        return this;
    }

    public HqlBuilder eq(String propertyName, Object value) {
        return addCriteria(propertyName, Operator.eq, value);
    }

    public HqlBuilder ge(String propertyName, Object value) {
        return addCriteria(propertyName, Operator.ge, value);
    }

    public HqlBuilder gt(String propertyName, Object value) {
        return addCriteria(propertyName, Operator.gt, value);
    }

    public HqlBuilder le(String propertyName, Object value) {
        return addCriteria(propertyName, Operator.le, value);
    }

    public HqlBuilder lt(String propertyName, Object value) {
        return addCriteria(propertyName, Operator.lt, value);
    }

    public HqlBuilder cn(String propertyName, Object value) {
        return addCriteria(propertyName, Operator.cn,value);
    }

    public HqlBuilder bw(String propertyName, Object value) {
        return addCriteria(propertyName, Operator.bw,value);
    }

    public HqlBuilder ew(String propertyName, Object value) {
        return addCriteria(propertyName, Operator.ew,value);
    }

    public HqlBuilder icn(String propertyName, Object value) {
        return addCriteria(propertyName, Operator.icn,value,true);
    }

    public HqlBuilder ibw(String propertyName, Object value) {
        return addCriteria(propertyName, Operator.ibw,value,true);
    }

    public HqlBuilder iew(String propertyName, Object value) {
        return addCriteria(propertyName, Operator.iew,value,true);
    }
    public HqlBuilder ieq(String propertyName, Object value) {
        return addCriteria(propertyName, Operator.ieq,value,true);
    }

    public HqlBuilder in(String propertyName, Collection value) {
        //return addCriteria(propertyName, Operator.in, value);
        return this;
    }


    public HqlBuilder or(HqlBuilder[] subQueries) {
        boolean needBracket = false;
        StringBuilder query = new StringBuilder();
        for (HqlBuilder b: subQueries){
            if (!StringUtils.isBlank(b.getQueryString())){
                if (query.length() > 0){
                    query.append(" or ");
                    needBracket = true;
                }
                query.append(b.getQueryString());
            }
        }
        if (query.length()>0){
            appendAndToQuery();
            sb.append(needBracket?"(":"").append(query).append(needBracket?")":"");
        }
        return this;
    }

    // For 2 states criteria, on the page, it's a checkbox,
    // if not checked, no to check against this criteria
    public HqlBuilder yesNo(String propertyName, boolean checked) {
        if (checked)
            return this;//eq(propertyName, true);
        return this;
    }

    public HqlBuilder isNull(String propertyName) {
        appendAndToQuery();
        sb.append(propertyName).append(" is null");
        return this;
    }

    private void appendAndToQuery() {
        if (sb.length() > 0)
            sb.append(" and ");
    }

    private void appendOrToQuery() {
        if (sb.length() > 0)
            sb.append(" or ");
    }

    public HqlBuilder notNull(String propertyName) {
        appendAndToQuery();
        sb.append(propertyName).append(" is not null");
        return this;
    }
    private HqlBuilder addCriteria(String propertyName, Operator op, Object value) {
        return addCriteria(propertyName,op,value,false);
    }

    private String getString(Object obj,Boolean caseInsensitive){
        String value = StringUtils.trimToNull(obj.toString());
        if(caseInsensitive) {
            value = StringUtils.upperCase(value);
        }
        return value;
    }


    private HqlBuilder addCriteria(String propertyName, Operator op, Object value,Boolean caseInsensitive ) {

        if (value != null) {
            if(!op.isStringCompare() && beanWrapper != null && beanWrapper.getPropertyType(propertyName) != String.class){
                Class<?> propertyType = beanWrapper.getPropertyType(propertyName);
                if(value.getClass().equals(propertyType) ){ parameters.add(value);
                } else{ parameters.add(stringToObject(getString(value,caseInsensitive), beanWrapper.getPropertyType(propertyName)));}
            }else{
                parameters.add(getString(value,caseInsensitive));
            }
            if(or != null && or){
                appendOrToQuery();
            }else{
                appendAndToQuery();
            }


            if(alias != null ){
                propertyName = alias + "." + propertyName;
            }
            String inverse = "";
            if(not){
                inverse = "not ";
                not = false;
            }
            sb.append(inverse).append(op.sqlPropertyPrefix).append(propertyName).append(op.sqlOperator).append("?").append(op.sqlValueSuffix);
        }
        return this;
    }

    protected String getQueryString(){
        return sb.toString();
    }


    public String getHqlQuery() {
           return  getHqlQuery("","");
    }
    public String getHqlQuery(String orderBy,String direction) {

        String select = "";
        if(!columns.isEmpty()){
            select =  "select ";
            String comma = "";
            for(String column:columns){
                select += comma + alias + "." + column + " ";
                comma = ",";
            }
        }
        //select +
        StringBuilder sql = new StringBuilder( selectFrom);
        if (sb.length()>0)
            sql.append(" WHERE ").append(sb);
        if (!StringUtils.isBlank(orderBy)){
            if (!StringUtils.isBlank(alias)){
                sql.append(" ORDER BY ").append(alias).append(".").append(orderBy).append(" ").append(direction.toUpperCase());
            } else{
                sql.append(" ORDER BY ").append(orderBy).append(" ").append(direction.toUpperCase());
            }
        }

        return sql.toString();
    }

    public static Object stringToObject(String searchString, Class clazz) {
        if (Date.class.equals(clazz)) {
            try {
               return new SimpleDateFormat(dateFormat).parse(searchString);
            } catch (ParseException e) {
                if (!searchString.isEmpty()) {
                    e.printStackTrace();
                }
            }
        } else if (Integer.class.equals(clazz) || int.class.equals(clazz)) {
            if (!searchString.isEmpty()) {
                return Integer.parseInt(searchString);
            }
        } else if (Long.class.equals(clazz) || long.class.equals(clazz)) {
            if (!searchString.isEmpty()) {
                return Long.parseLong(searchString);
            }
        } else if (Double.class.equals(clazz) || double.class.equals(clazz)) {
            if (!searchString.isEmpty()) {
                return Double.parseDouble(searchString);
            }
        } else if (Float.class.equals(clazz) || float.class.equals(clazz)) {
            if (!searchString.isEmpty()) {
                return Float.parseFloat(searchString) ;
            }
        } else if (BigDecimal.class.equals(clazz)) {
            if (!searchString.isEmpty()) {
                return new BigDecimal(searchString);
            }
        } else if (BigInteger.class.equals(clazz)) {
            if (!searchString.isEmpty()) {
                return new BigInteger(searchString);
            }
        } else if (Boolean.class.equals(clazz) || boolean.class.equals(clazz)) {
            return Boolean.valueOf(searchString);
        }  else if(searchString.startsWith("{")){ //Maybe a Json Object
            try{
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue( searchString, clazz);
            }catch(Exception e){
                e.printStackTrace();
            }
        } else{
            //Lets just Try and de serialize
            try {
                byte b[] = searchString.getBytes("utf-8");
                ByteArrayInputStream bi = new ByteArrayInputStream(b);
                ObjectInputStream si = new ObjectInputStream(bi);
                Object obj =  clazz.cast(si.readObject());
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return searchString;
    }

}
