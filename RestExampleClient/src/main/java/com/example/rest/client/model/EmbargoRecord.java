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
    private Integer id;
    @XmlElement
    private Integer firmId;
    @XmlElement
    private String emailDomain;
    @XmlElement
    private Integer fullAnonDays;
    @XmlElement
    private Integer firmVisibleDays;
    @XmlElement
    private Integer fullVisibleDays;
    @XmlElement
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date activeFrom;
    @XmlElement
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date activeTo;

    public EmbargoRecord() { }

    public EmbargoRecord(Integer id, Integer firmId, String emailDomain, Integer fullAnonDays, Integer firmVisibleDays, Integer fullVisibleDays, Date activeFrom, Date activeTo) {
        this.id = id;
        this.firmId = firmId;
        this.emailDomain = emailDomain;
        this.fullAnonDays = fullAnonDays;
        this.firmVisibleDays = firmVisibleDays;
        this.fullVisibleDays = fullVisibleDays;
        this.activeFrom = activeFrom;
        this.activeTo = activeTo;
    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public Date getActiveTo() { return activeTo; }

    public void setActiveTo(Date activeTo) { this.activeTo = activeTo; }

    public Date getActiveFrom() {
        return activeFrom;
    }

    public void setActiveFrom(Date activeFrom) {
        this.activeFrom = activeFrom;
    }

    public Integer getFullVisibleDays() {
        return fullVisibleDays;
    }

    public void setFullVisibleDays(Integer fullVisibleDays) {
        this.fullVisibleDays = fullVisibleDays;
    }

    public Integer getFirmVisibleDays() {
        return firmVisibleDays;
    }

    public void setFirmVisibleDays(Integer firmVisibleDays) {
        this.firmVisibleDays = firmVisibleDays;
    }

    public Integer getFullAnonDays() {
        return fullAnonDays;
    }

    public void setFullAnonDays(Integer fullAnonDays) {
        this.fullAnonDays = fullAnonDays;
    }

    public String getEmailDomain() {
        return emailDomain;
    }

    public void setEmailDomain(String emailDomain) {
        this.emailDomain = emailDomain;
    }

    public Integer getFirmId() { return firmId; }

    public void setFirmId(Integer firmId) {
        this.firmId = firmId;
    }
}
