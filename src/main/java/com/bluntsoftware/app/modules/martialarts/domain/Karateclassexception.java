package com.bluntsoftware.app.modules.martialarts.domain;


import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Date;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.WeakHashMap;
import java.sql.Time;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.proxy.HibernateProxy;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.annotation.*;
                    
@Entity
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Table(name = "\"karateclassexception\"")
public class Karateclassexception implements CustomDomain<Karateclassexception> {

    private static final Map< Serializable, Integer > SAVED_HASHES = Collections.synchronizedMap(new WeakHashMap< Serializable, Integer >());
    private volatile Integer hashCode;
    private Integer id = null;
    private String date;
    private String description;
    private String owner;

    public Karateclassexception() { }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "karateclassexception_id_seq")
    @SequenceGenerator(name = "karateclassexception_id_seq", allocationSize = 1, sequenceName = "karateclassexception_id_seq", initialValue = 1)
    @Column(name = "\"id\"")
    public Integer getId() {
        return id;
    }
    public void setId(Integer id){
            if ((this.id == null || this.id == 0) && id != null && hashCode != null) {
        SAVED_HASHES.put(id, hashCode);
        }
        this.id = id;
    }

    @Column(name = "\"date\"", length = 255)
    public String getDate() {
        return date;
    }
    public void setDate(String date){
        this.date = date;
    }

    @Column(name = "\"description\"", length = 255)
    public String getDescription() {
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }

    @Column(name = "\"owner\"", length = 255)
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner){
        this.owner = owner;
    }

    @Transient
    public Class<?> getClassType() {
        return Karateclassexception.class;
    }

    @Override
    public int hashCode() {
          if (hashCode == null) {
            synchronized (this) {
                if (hashCode == null) {
                    if (getId() != null) {
                        hashCode = SAVED_HASHES.get(getId());
                    }
                    if (hashCode == null) {
                        if ( getId() != null && getId() != 0) {
                            hashCode = new Integer(getId().hashCode());
                        } else {
                            hashCode = new Integer(super.hashCode());
                        }
                    }
                }
            }
        }
        return hashCode;
    }

    public int compareTo(Karateclassexception karateclassexception) {
        return 0;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Karateclassexception entity = (Karateclassexception)super.clone();
        entity.setId(null);
        return entity;
    }
}