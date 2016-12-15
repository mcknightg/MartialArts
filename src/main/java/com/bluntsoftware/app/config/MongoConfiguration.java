package com.bluntsoftware.app.config;

import com.bluntsoftware.lib.nosql.mongo.MongoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
* Created by Alex Mcknight on 10/12/2016.
*/
@Configuration
@ComponentScan({"com.bluntsoftware.lib.nosql.mongo"})
public class MongoConfiguration implements EnvironmentAware {


    @Autowired
    MongoServiceImpl mongoService;


    @Override
    public void setEnvironment(Environment environment) {
        String server = environment.getProperty("mongo.host");
        String port =  environment.getProperty("mongo.port");
        if(server != null){
            mongoService.setServer(server);
        }
        if(port != null){
            mongoService.setPort(Integer.parseInt(port));
        }
    }

}