package com.bluntsoftware.lib.web.filter.module;


import com.bluntsoftware.lib.jpa.rest.impl.JpaCRUDRestControllerImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexander on 2/19/2016.
 *
 */
@Controller
@RequestMapping(value = "/api")
public class ModuleControllerImpl {

    @RequestMapping(value = "")
    @ResponseBody
    Map getRootConfig(HttpServletRequest request) {
        HashMap<String,Object> api = new HashMap<String, Object>();
        api.put("serverInfo",getServerInfo(request));
        HashMap<String,Object> mods = new HashMap<String, Object>();

        for(String module: JpaCRUDRestControllerImpl.controllerNames.keySet()  ) {
            mods.put(module,getEntities(module,request));
        }
        api.put("mods",mods);
        return api;
    }

    @RequestMapping(value = "{qualifier}")
    @ResponseBody
    HashMap<String,Object> getConfig(@PathVariable("qualifier") String qualifier,HttpServletRequest request) {
             return getEntities(qualifier,request);
    }
    public static  HashMap<String,Object> getEntities(String module,HttpServletRequest request) {
        HashMap<String,Object> entities = new HashMap<String, Object>();
        for(String entity:JpaCRUDRestControllerImpl.getControllerName(module)){
            entities.put(entity,config(module,entity,request));
        }
        return entities;
    }
    public static   Map<String,Object> config(String qualifier,String name,HttpServletRequest request) {
        Map<String,Object> serverInfo = getServerInfo(request);
        String address =  serverInfo.get("serverPath") + qualifier + "/" +  name + "/" ;
        Map<String,Object>  map = new HashMap<String, Object>();
            map.put("mod",qualifier);
            map.put("name",name);
            map.put("schema",address + "schema");
            map.put("columns",address +"columns");
            map.put("api",address +  "api");
            map.put("data",address + "data");
        return map;
    }

    public static Map<String,Object> getServerInfo(HttpServletRequest request){

        String serverPath = "http://" + request.getServerName();
        Integer port = request.getLocalPort();
        String contextPath = request.getContextPath();
        if(!port.toString().equalsIgnoreCase("")){
            serverPath += ":" + port;
        }
        if(contextPath != null ){
            serverPath += "/" + contextPath;
        }

        Map<String,Object>  map = new HashMap<String, Object>();
        map.put("localAddress",request.getLocalAddr()); //- the server's IP address as a string
        map.put("localName",request.getLocalName()); //- the name of the server recieving the request
        map.put("serverName",request.getServerName()); //- the name of the server that the request was sent to
        map.put("port",request.getLocalPort()); //- the port the server recieved the request on
        map.put("serverPort",request.getServerPort()); //- the port the request was sent to
        map.put("contextPath",request.getContextPath()); //- the part of the path that identifies the application
        map.put("serverPath",serverPath);
        return map;
    }


}
