package com.bluntsoftware.lib.jpa.domain;



import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Alexander Mcknight
 * Date: 6/29/15
 * Time: 9:53 PM
 */
public interface Domain {
    Class<?> getClassType();
    Serializable getId();

    Object clone() throws CloneNotSupportedException;
}
