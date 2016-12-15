package com.bluntsoftware.app;

//import com.bluntsoftware.app.config.WebConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import java.io.File;
/**
* Created by Alexander on 7/21/2014.
*/
public class SpringWebXml extends SpringBootServletInitializer {
    private final Logger log = LoggerFactory.getLogger(SpringWebXml.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

        //Add A properties override folder to user Home
        File userHomeAppPropertiesFolder = new File(System.getProperty("java.io.tmpdir"),"MartialArts");
        userHomeAppPropertiesFolder.mkdir();


                    return application
                    .showBanner(false)
                    .sources(SpringApp.class);
    }

}