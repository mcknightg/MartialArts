package com.bluntsoftware.app.config;

import com.bluntsoftware.lib.properties.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
* Created by Alex Mcknight on 12/16/2016.
*/


@Configuration
@ComponentScan({"com.bluntsoftware.lib.properties"})
public class PropertyConfiguration implements EnvironmentAware {

    @Autowired
    PropertyService propertyService;


    @Override
    public void setEnvironment(Environment environment) {
        propertyService.setApplicationName("MartialArts");
    }
}