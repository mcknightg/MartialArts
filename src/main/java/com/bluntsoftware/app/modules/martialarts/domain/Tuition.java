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
@Table(name = "\"tuition\"")
public class Tuition implements CustomDomain<Tuition> {

    private static final Map< Serializable, Integer > SAVED_HASHES = Collections.synchronizedMap(new WeakHashMap< Serializable, Integer >());
    private volatile Integer hashCode;
    private Integer id = null;
    private Double amount;
    private Student student;
    private Boolean paid;
    private String owner;

    public Tuition() { }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tuition_id_seq")
    @SequenceGenerator(name = "tuition_id_seq", allocationSize = 1, sequenceName = "tuition_id_seq", initialValue = 1)
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

    @Column(name = "\"amount\"")
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount){
        this.amount = amount;
    }

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @JoinColumn(name = "\"student\"", nullable = true )
    public Student getStudent() {
        return student;
    }
    public void setStudent(Student student){
        this.student = student;
    }

    @JsonSerialize(using = com.bluntsoftware.lib.jpa.serializers.CustomBooleanSerializer.class, include=JsonSerialize.Inclusion.NON_NULL)
    @Column(name = "\"paid\"")
    public Boolean getPaid() {
        return paid;
    }
    public void setPaid(Boolean paid){
        this.paid = paid;
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
        return Tuition.class;
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

    public int compareTo(Tuition tuition) {
        return 0;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Tuition entity = (Tuition)super.clone();
        entity.setId(null);
        return entity;
    }
}