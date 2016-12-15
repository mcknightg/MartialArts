package com.bluntsoftware.app.config;

import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.MultipartConfigElement;

/**
 * Created by Alexander on 6/20/2015.
 */
@Configuration
public class MultiPartConfiguration {

    public static final String MAX_FILE_SIZE = "100MB";
    public static final String MAX_REQUEST_SIZE = "100MB";
    public static final String FILE_SIZE_THRESHOLD = "2MB";


    @Bean
    MultipartConfigElement multipartConfigElement() {
        //  String absTempPath = new File(createdDir).getAbsolutePath();
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(MAX_FILE_SIZE);
        factory.setMaxRequestSize(MAX_REQUEST_SIZE);
        factory.setFileSizeThreshold(FILE_SIZE_THRESHOLD);
        // factory.setLocation(absTempPath);
        return factory.createMultipartConfig();
    }
    @Bean
    public MultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }
}
