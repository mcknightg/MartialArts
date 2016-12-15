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
@Table(name = "\"karateclass\"")
public class Karateclass implements CustomDomain<Karateclass> {

    private static final Map< Serializable, Integer > SAVED_HASHES = Collections.synchronizedMap(new WeakHashMap< Serializable, Integer >());
    private volatile Integer hashCode;
    private Integer id = null;
    private School school;
    private String weekDay;

    public Karateclass() { }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "karateclass_id_seq")
    @SequenceGenerator(name = "karateclass_id_seq", allocationSize = 1, sequenceName = "karateclass_id_seq", initialValue = 1)
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

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @JoinColumn(name = "\"school\"", nullable = true )
    public School getSchool() {
        return school;
    }
    public void setSchool(School school){
        this.school = school;
    }

    @Column(name = "\"weekDay\"", length = 255)
    public String getWeekDay() {
        return weekDay;
    }
    public void setWeekDay(String weekDay){
        this.weekDay = weekDay;
    }

    @Transient
    public Class<?> getClassType() {
        return Karateclass.class;
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

    public int compareTo(Karateclass karateclass) {
        return 0;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Karateclass entity = (Karateclass)super.clone();
        entity.setId(null);
        return entity;
    }
}