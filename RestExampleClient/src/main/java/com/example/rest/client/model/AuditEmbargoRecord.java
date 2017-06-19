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
public class AuditEmbargoRecord {

    @XmlElement
    private Integer id;
    @XmlElement
    private Integer firmId;
    @XmlElement
    private String emailDomain;
    @XmlElement
    private String source;
    @XmlElement
    private String userEmail;
    @XmlElement
    private Integer fullAnonDays;
    @XmlElement
    private Integer firmVisibleDays;
    @XmlElement
    private Integer fullVisibleDays;
    @XmlElement
    private String action;
    @XmlElement
    private Integer recordId;
    @XmlElement
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date date;

    public AuditEmbargoRecord() { }

    public AuditEmbargoRecord(Integer id, Integer firmId, String emailDomain, String source, String userEmail, Integer fullAnonDays, Integer firmVisibleDays, Integer fullVisibleDays, String action, Integer recordId, Date date) {
        this.id = id;
        this.firmId = firmId;
        this.emailDomain = emailDomain;
        this.source = source;
        this.userEmail = userEmail;
        this.fullAnonDays = fullAnonDays;
        this.firmVisibleDays = firmVisibleDays;
        this.fullVisibleDays = fullVisibleDays;
        this.action = action;
        this.recordId = recordId;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFirmId() {
        return firmId;
    }

    public void setFirmId(Integer firmId) {
        this.firmId = firmId;
    }

    public String getEmailDomain() {
        return emailDomain;
    }

    public void setEmailDomain(String emailDomain) {
        this.emailDomain = emailDomain;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Integer getFullAnonDays() {
        return fullAnonDays;
    }

    public void setFullAnonDays(Integer fullAnonDays) {
        this.fullAnonDays = fullAnonDays;
    }

    public Integer getFirmVisibleDays() {
        return firmVisibleDays;
    }

    public void setFirmVisibleDays(Integer firmVisibleDays) {
        this.firmVisibleDays = firmVisibleDays;
    }

    public Integer getFullVisibleDays() {
        return fullVisibleDays;
    }

    public void setFullVisibleDays(Integer fullVisibleDays) {
        this.fullVisibleDays = fullVisibleDays;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
