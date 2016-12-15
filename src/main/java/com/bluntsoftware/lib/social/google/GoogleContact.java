package com.bluntsoftware.lib.social.google;

import com.google.api.services.people.v1.People;
import com.google.api.services.people.v1.model.Person;
import org.springframework.social.ApiBinding;


/**
 * Created by Alex Mcknight on 9/5/2016.
 */
public interface GoogleContact extends ApiBinding {

    People.PeopleOperations peopleOperations();
    Person getProfile();
}
