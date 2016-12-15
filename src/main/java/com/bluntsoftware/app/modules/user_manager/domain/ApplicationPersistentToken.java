package com.bluntsoftware.app.modules.user_manager.domain;


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
@Table(name = "\"APPLICATION_PERSISTENT_TOKEN\"")
public class ApplicationPersistentToken implements CustomDomain<ApplicationPersistentToken> {

    private static final Map< Serializable, Integer > SAVED_HASHES = Collections.synchronizedMap(new WeakHashMap< Serializable, Integer >());
    private volatile Integer hashCode;
    private Integer id = null;
    private String ipAddress;
    private String series;
    private LocalDate tokenDate;
    private String tokenValue;
    private String userAgent;
    private ApplicationUser appuser;

    public ApplicationPersistentToken() { }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @Column(name = "\"ip_address\"", length = 39)
    public String getIpAddress() {
        return ipAddress;
    }
    public void setIpAddress(String ipAddress){
        this.ipAddress = ipAddress;
    }

    @Column(name = "\"series\"", length = 50)
    public String getSeries() {
        return series;
    }
    public void setSeries(String series){
        this.series = series;
    }

    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @Column(name = "\"token_date\"")
    public LocalDate getTokenDate() {
        return tokenDate;
    }
    public void setTokenDate(LocalDate tokenDate){
        this.tokenDate = tokenDate;
    }

    @Column(name = "\"token_value\"", length = 50)
    public String getTokenValue() {
        return tokenValue;
    }
    public void setTokenValue(String tokenValue){
        this.tokenValue = tokenValue;
    }

    @Column(name = "\"user_agent\"", length = 255)
    public String getUserAgent() {
        return userAgent;
    }
    public void setUserAgent(String userAgent){
        this.userAgent = userAgent;
    }

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @JoinColumn(name = "\"appuser\"", nullable = false )
    public ApplicationUser getAppuser() {
        return appuser;
    }
    public void setAppuser(ApplicationUser appuser){
        this.appuser = appuser;
    }

    @Transient
    public Class<?> getClassType() {
        return ApplicationPersistentToken.class;
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

    public int compareTo(ApplicationPersistentToken applicationPersistentToken) {
        return 0;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ApplicationPersistentToken entity = (ApplicationPersistentToken)super.clone();
        entity.setId(null);
        return entity;
    }
}