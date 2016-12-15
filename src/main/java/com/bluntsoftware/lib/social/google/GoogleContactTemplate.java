package com.bluntsoftware.lib.social.google;

import com.google.api.services.people.v1.People;
import com.google.api.services.people.v1.model.Person;
import java.io.IOException;

/**
 * Created by Alex Mcknight on 9/5/2016.
 *
 */
public class GoogleContactTemplate implements GoogleContact {

    private People people;
    GoogleContactTemplate(People people){
          this.people = people;
    }

    public People getPeople() {
        return people;
    }
    public void setPeople(People people) {
        this.people = people;
    }

    @Override
    public boolean isAuthorized() {
        return true;
    }

    @Override
    public People.PeopleOperations peopleOperations() {
          return people.people();
    }

    @Override
    public Person getProfile() {
        try {
            return people.people().get("people/me").execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
