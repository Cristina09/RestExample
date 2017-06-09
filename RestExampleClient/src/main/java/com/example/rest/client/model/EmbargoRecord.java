package com.example.rest.client.model;

import com.example.rest.client.util.DateAdapter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

/**
 * Created by Cristina on 5/30/2017.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmbargoRecord {

    @XmlElement
    private Integer firmId;
    @XmlElement
    private String emailDomain;
    @XmlElement
    private Integer anonDays;
    @XmlElement
    private Integer fullDays;
    @XmlElement
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date activeFrom;

    public EmbargoRecord() { }

    public EmbargoRecord(Integer firmId, String emailDomain, Integer anonDays, Integer fullDays, Date activeFrom) {
        this.firmId = firmId;
        this.emailDomain = emailDomain;
        this.anonDays = anonDays;
        this.fullDays = fullDays;
        this.activeFrom = activeFrom;
    }

    public Date getActiveFrom() {
        return activeFrom;
    }

    public void setActiveFrom(Date activeFrom) {
        this.activeFrom = activeFrom;
    }

    public Integer getFullDays() {
        return fullDays;
    }

    public void setFullDays(Integer fullDays) {
        this.fullDays = fullDays;
    }

    public Integer getAnonDays() {
        return anonDays;
    }

    public void setAnonDays(Integer anonDays) {
        this.anonDays = anonDays;
    }

    public String getEmailDomain() {
        return emailDomain;
    }

    public void setEmailDomain(String emailDomain) {
        this.emailDomain = emailDomain;
    }

    public Integer getFirmId() {
        return firmId;
    }

    public void setFirmId(Integer firmId) {
        this.firmId = firmId;
    }
}
