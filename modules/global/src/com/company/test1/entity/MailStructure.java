package com.company.test1.entity;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;

@MetaClass(name = "test1_MailStructure")
public class MailStructure extends BaseUuidEntity {
    private static final long serialVersionUID = -5387211206061994227L;

    @MetaProperty
    private String from;

    @MetaProperty
    private String subject;

    @MetaProperty
    private String contentDisplay;

    @MetaProperty
    private Date sentDate;

    private Hashtable<String, Object> bodyParts = new Hashtable<String, Object>();

    @MetaProperty
    public Integer getNumOfAttachments() {
        try {
            return this.bodyParts.keySet().size() - 2;
        }catch(Exception exc){
            return 0;
        }
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public String getContentDisplay() {
        return contentDisplay;
    }

    public void setContentDisplay(String contentDisplay) {
        this.contentDisplay = contentDisplay;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Hashtable<String, Object> getBodyParts(){
        return bodyParts;
    }

    public void setBodyParts(Hashtable<String, Object> bbpp){
        this.bodyParts = bbpp;
    }

    public Object resolveDisplayableBodyPart(){
        String htmlBp = null;
        String plain = null;
        Iterator<String> keys = bodyParts.keySet().iterator();
        while(keys.hasNext()){
            String k = keys.next();
            Object o = bodyParts.get(k);
            if (k.indexOf("text/html")!=-1){
                htmlBp = (String) o;
            }
            if (k.indexOf("text/plain")!=-1){
                plain = (String) o;
            }
        }
        if (htmlBp!=null){
            return htmlBp;
        }else{
            return plain;
        }

    }

    public Hashtable<String, Object> getOnlyAttachments(){
        Hashtable<String, Object> attachments = new Hashtable<String, Object>();
        Iterator<String> it = bodyParts.keySet().iterator();
        while(it.hasNext()){
            String k = it.next();
            if ((k.indexOf("text/plain")==-1) && (k.indexOf("text/html")==-1)) {
                Object o = bodyParts.get(k);
                attachments.put(k, o);
            }
        }
        return attachments;
    }
}