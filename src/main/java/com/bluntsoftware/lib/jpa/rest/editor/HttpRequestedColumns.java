package com.bluntsoftware.lib.jpa.rest.editor;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 *  Created by Alexander Mcknight on 7/29/2015.
 */
public class HttpRequestedColumns {

    public static List<String> listColumns(ServletRequest request){
        List<String> columnList = new ArrayList<String>();
        String columns =  request.getParameter("columns");
        if (columns != null && !columns.isEmpty()) {
            StringTokenizer st = new StringTokenizer(columns.trim(), ",;");
            while (st.hasMoreTokens()) {
                columnList.add(st.nextToken());
            }
        }
        return columnList;
    }


    public static List list(HttpServletRequest request,List list){
        List<String> columnList =  listColumns(request);
        if (!columnList.isEmpty() ) {
            ArrayList<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            for (Object obj : list) {
                Map<String, Object> map = new HashMap<String, Object>();
                BeanWrapper wrapper = new BeanWrapperImpl(obj);
                for (String column : columnList) {
                    try {
                        map.put(column, wrapper.getPropertyValue(column));
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                result.add(map);
            }
            return result;
        }
        return list;
    }
}
