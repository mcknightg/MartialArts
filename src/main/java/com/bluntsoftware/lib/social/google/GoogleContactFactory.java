package com.bluntsoftware.lib.social.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.People;




/**
 * Created by Alex Mcknight on 9/6/2016.
 */
public class GoogleContactFactory {

    public GoogleContact getApi(String accessToken,String clientId,String clientSecret){
        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();
        GoogleCredential credentials =   new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setClientSecrets(clientId, clientSecret)
                .build()
                .setAccessToken(accessToken);

        People people = new People.Builder(httpTransport, jsonFactory, credentials).build();
        return new GoogleContactTemplate(people);
    }
}
