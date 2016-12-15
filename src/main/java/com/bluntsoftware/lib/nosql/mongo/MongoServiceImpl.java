package com.bluntsoftware.lib.nosql.mongo;


import com.mongodb.*;
import com.mongodb.client.*;



import org.bson.Document;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.*;

import static com.mongodb.client.model.Filters.*;



/**
 * Created by Alex Mcknight on 10/12/2016.
 */
@Controller("MongoService")
@RequestMapping(value = "/mongo")
public class MongoServiceImpl {
    private Integer port = 27017;
    private String server = "localhost";
    MongoClient mongoClient = null;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    private  MongoClient getClient(){
        if(mongoClient == null){
            mongoClient = new MongoClient(server, port);
        }
        return mongoClient;
    }


    private  void createCollection(String database, String collection){
        MongoDatabase db = getClient().getDatabase(database);
        db.createCollection(collection);
    }
    private  MongoCollection<Document>  getCollection(String database, String collection){
        MongoDatabase db = getClient().getDatabase(database);
        return db.getCollection(collection);
    }

    private  MongoCollection<Document>  getCreateCollection(String database, String collection){
        MongoCollection<Document> ret = getCollection(database,collection);
        if(ret == null){createCollection(database,collection);}
        return getCollection(database,collection);
    }
    private Document getById(String id,String databaseName,String collectionName){
        MongoCollection<Document> collection = getCreateCollection(databaseName,collectionName);
        return collection.find(eq("_id", id)).first();
    }
    private Document save( String databaseName,String collectionName,Map<String,Object> data){
        MongoCollection<Document> collection = getCreateCollection(databaseName,collectionName);
        Document document = null;


        if(data.containsKey("_id") && data.get("_id") != null && !data.get("_id").equals("") ){
            document = this.getById(data.get("_id").toString(),databaseName,collectionName);
            if(document != null){
                document.putAll(data);
                collection.replaceOne(eq("_id", data.get("_id").toString()),document);
            }else{
                document = new Document();
                document.putAll(data);
                collection.insertOne(document);
            }
             /*
            for(String key:data.keySet()){
                collection.updateOne(eq("_id", data.get("_id").toString()), set(key, data.get(key)));
            }
            document = getById(data.get("_id").toString(),databaseName,collectionName);   */
        }else{
            document = new Document();
            data.put("_id",UUID.randomUUID().toString());
            document.putAll(data);
            collection.insertOne(document);
        }
        return document;
    }
    public static void main(String[] args) {

        try{
            MongoServiceImpl mongoService = new MongoServiceImpl();

            MongoCollection<Document> collection = mongoService.getCreateCollection("TestDb","testCol");
            Document doc = new Document("name", "MongoDB")
                    .append("_id",UUID.randomUUID().toString())
                    .append("type", "database")
                    .append("count", 1)
                    .append("info", new Document("x", 203).append("y", 102));

            collection.insertOne(doc);
            Block<Document> printBlock = new Block<Document>() {
                @Override
                public void apply(final Document document) {
                    System.out.println(document.toJson());
                }
            };
            collection.find().forEach(printBlock);
        }catch(Exception e){
           System.out.print(e.getMessage());
        }
    }



    @RequestMapping(
            value = "{databaseName}/{collectionName}/doc/save",
            method = { RequestMethod.GET,RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public  Object saveUpdateParams(
            @PathVariable("databaseName") String databaseName,
            @PathVariable("collectionName") String collectionName,
            HttpServletRequest request) throws Exception{

        Map<String,Object> object = new HashMap<String, Object>();
        Enumeration enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String parameterName = (String) enumeration.nextElement();
            object.put(parameterName,request.getParameter(parameterName));
        }
        return save(databaseName,collectionName,object);
    }



    @RequestMapping(
            value = "{databaseName}/{collectionName}",
            method = {RequestMethod.POST,RequestMethod.PUT},
            produces = "application/json",
            consumes = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public  Object saveUpdate(
            @PathVariable("databaseName") String databaseName,
            @PathVariable("collectionName") String collectionName,
            HttpServletRequest request,
            @RequestBody Map<String,Object> object) throws Exception{

        return save(databaseName,collectionName,object);
    }

    //Save
    @RequestMapping(
            value="{databaseName}/{collectionName}/{id}",
            method = { RequestMethod.POST,RequestMethod.PUT},
            produces = "application/json",
            consumes = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    protected   Object save(
            @PathVariable("databaseName") String databaseName,
            @PathVariable("collectionName") String collectionName,
            @PathVariable("id") String id,
            HttpServletRequest request,
            @RequestBody Map<String,Object> object) throws Exception{


        Document myDoc = getById(id,databaseName,collectionName);
        if(myDoc != null){
            myDoc.putAll(object);
        }
        return save(databaseName,collectionName,myDoc);
    }
    private static String validString(String value, String defaultValue){
        if (isValidParameter(value)) {
            return value;
        }
        return defaultValue;
    }
    private static boolean isValidParameter(String param) {
        return param != null && !param.isEmpty() && !param.equalsIgnoreCase("_empty") && !param.equalsIgnoreCase("undefined") && !param.equalsIgnoreCase("null");
    }

    //List
    @RequestMapping(
            value = {"{databaseName}/{collectionName}","{databaseName}/{collectionName}/data"},
            method = {RequestMethod.GET},
            produces = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public  Object findAll(
            @PathVariable("databaseName") String databaseName,
            @PathVariable("collectionName") String collectionName,
            HttpServletRequest request) throws Exception{

        String filterByFields =  validString(request.getParameter("filterByFields"), "{}");
        String searchOperator = validString(request.getParameter("defaultsearchoper"),"eq");
        String sord = validString(request.getParameter("sord"),"ASC");
        String sidx = validString(request.getParameter("sidx"),"id");
        String rows = validString(request.getParameter("rows"),"25");
        //Default and criteria
        String or = request.getParameter("or");
        if(or != null && or.equalsIgnoreCase("true")){
           // setOr(true);
        }

        // Mongo DB Syntax IE - The compound query below selects all records where the
        // `status` equals "A" and either age is less than 30 or type equals 1:
        // {status: "A", $or: [ { age: { $lt: 30 } }, { type: 1 } ]}
        BasicDBObject query = new BasicDBObject();
        if(filterByFields != null && !filterByFields.equalsIgnoreCase("")){
            query = BasicDBObject.parse(filterByFields);
        }
        MongoCollection<Document> collection = getCreateCollection(databaseName,collectionName);
        FindIterable<Document> result  = collection.find(query).limit(Integer.parseInt(rows));
        Map<String,Object> ret = new HashMap<String, Object>();
        ret.put("currpage",1);
        ret.put("totalpages",1);
        ret.put("totalrecords",collection.count());
        ret.put("rows",result);
        return ret;
    };

    //Get
    @RequestMapping(
            value = "{databaseName}/{collectionName}/{id}",
            method = {RequestMethod.GET},
            produces = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Object findOne(@PathVariable("databaseName") String databaseName,
                          @PathVariable("collectionName") String collectionName,
                          @PathVariable("id") String id,
                          HttpServletRequest request) throws Exception{
        return getById(id,databaseName,collectionName);
    };

    //Remove
    @RequestMapping(
            value = "{databaseName}/{collectionName}/{id}",
            method = {RequestMethod.DELETE},
            produces = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public   Object delete(
            @PathVariable("databaseName") String databaseName,
            @PathVariable("collectionName") String collectionName,
            @PathVariable("id") String id,
            HttpServletRequest request) throws Exception{

        MongoCollection<Document> collection = getCreateCollection(databaseName,collectionName);
        collection.deleteOne(eq("_id", id));

        Map ret = new HashMap();
        ret.put("status","success");
        ret.put("id",id);
        ret.put("action","remove");

        return ret;
    }

    //Show Schema
    @RequestMapping(
            value = "{databaseName}/{collectionName}/schema",
            method = {RequestMethod.GET, RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public  Map<String, Object> showSchema(
            @PathVariable("databaseName") String databaseName,
            @PathVariable("collectionName") String collectionName){

        MongoCollection<Document> collection = getCreateCollection(databaseName,collectionName);
        return collection.find().first();
    };

    //List Columns
    @RequestMapping(
            value = "{databaseName}/{collectionName}/columns",
            method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Set<String> columns(
            @PathVariable("databaseName") String databaseName,
            @PathVariable("collectionName") String collectionName){
        MongoCollection<Document> collection = getCreateCollection(databaseName,collectionName);
        Document document =  collection.find().first();
        return document.keySet();
    };



    @RequestMapping( value = "")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ListDatabasesIterable<Document> listDatabases(HttpServletRequest request){
        MongoClient client = getClient();
        return client.listDatabases();
    };

    //get api
    @RequestMapping( value = {"{databaseName}/{collectionName}/api","{database}/api"})
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> getApi(
            @PathVariable("databaseName") String databaseName,
            @PathVariable("collectionName") String collectionName,
            HttpServletRequest request){

        MongoCollection<Document> collection = getCreateCollection(databaseName,collectionName);

        return null;
    };
    @RequestMapping( value = "api")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> api(
            HttpServletRequest request){
        Map<String,Object> ret =  getServerInfo(request);


        MongoClient client = getClient();
        Map<String,Map> databaseMap = new HashMap<String, Map>();
        for(Document db:client.listDatabases()){
            String databaseName = (String)db.get("name");
            MongoDatabase database =  client.getDatabase(databaseName);

            Map<String,Map> collectionMap = new HashMap<String,Map>();
            for(Document collection:database.listCollections()){
                String collectionName = (String)collection.get("name");

                collectionMap.put(collectionName,config(databaseName,collectionName,request));
            }
            databaseMap.put(databaseName,collectionMap);

        }
        ret.put("mods",databaseMap);
        ret.put("serverConfig",getServerInfo(request));
        return ret;
    };

    @RequestMapping( value = "{databaseName}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Object listCollections(
            @PathVariable("databaseName") String databaseName,
            HttpServletRequest request){
        MongoClient client = getClient();
        MongoDatabase database =  client.getDatabase(databaseName);
        return database.listCollections();

    };
    public  Map<String,Object> getServerInfo(HttpServletRequest request){

        String serverPath = "http://" + request.getServerName();
        Integer port = request.getLocalPort();
        String contextPath = request.getContextPath();
        if(!port.toString().equalsIgnoreCase("")){
            serverPath += ":" + port;
        }
        if(contextPath != null ){
            serverPath += "/" + contextPath;
        }

        serverPath += "/mongo/";

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
    public    Map<String,Object> config(String qualifier,String name,HttpServletRequest request) {
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
}
