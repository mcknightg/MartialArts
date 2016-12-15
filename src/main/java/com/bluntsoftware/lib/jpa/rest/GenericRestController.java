package com.bluntsoftware.lib.jpa.rest;


import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;


/**
 * Created with IntelliJ IDEA.
 * User: Alexander Mcknight
 * Date: 6/28/15
 * Time: 6:35 PM
 */
public abstract class GenericRestController<T,ID> {
    //Save Update
    @Transactional(readOnly = false)
    @RequestMapping(
            method = {RequestMethod.POST,RequestMethod.PUT},
            produces = "application/json",
            consumes = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public abstract Object saveUpdate( HttpServletRequest request,@RequestBody Map<String,Object> object) throws Exception;



    //Save
    @Transactional(readOnly = false)
    @RequestMapping(
            value="{id}",
            method = { RequestMethod.POST,RequestMethod.PUT},
            produces = "application/json",
            consumes = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    protected abstract Object save(@PathVariable("id") ID id, HttpServletRequest request, @RequestBody T object) throws Exception;



    public abstract T create(T object,HttpServletRequest request)throws Exception;
    public abstract T update(T object,HttpServletRequest request)throws Exception;
    public abstract T get(T object,HttpServletRequest request)throws Exception;
    public abstract T remove(T object,HttpServletRequest request)throws Exception;



    //List
    @RequestMapping(
            value = {"","/data"},
            method = {RequestMethod.GET},
            produces = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public abstract Object findAll(HttpServletRequest request) throws Exception;

    //Get
    @RequestMapping(
            value = "{id}",
            method = {RequestMethod.GET},
            produces = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public abstract Object findOne(@PathVariable("id") ID id,HttpServletRequest request) throws Exception;

    //Remove
    @RequestMapping(
            value = "{id}",
            method = {RequestMethod.DELETE},
            produces = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public abstract Object delete(@PathVariable("id") ID id,HttpServletRequest request) throws Exception;

    //Show Schema
    @RequestMapping( value = "/schema", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public abstract Map<String, Object> showSchema();

    //List Columns
    @RequestMapping(value = "/columns", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public abstract Map<String, Set<String>> columns();

    //get api
    @RequestMapping(value = "/api")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public abstract Map<String, Object> getApi(HttpServletRequest request);

}
