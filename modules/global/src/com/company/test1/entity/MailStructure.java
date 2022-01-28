package com.company.test1.entity;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.sun.mail.util.BASE64DecoderStream;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
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

    public Message originalJavaMailMessage = null;

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

    public Message getOriginalJavaMailMessage(){
        return this.originalJavaMailMessage;
    }

    public void setOriginalJavaMailMessage(Message m){
        this.originalJavaMailMessage = m;
    }

    public Object resolveDisplayableBodyPart(){
        String htmlBp = null;
        String plain = null;
        Iterator<String> keys = bodyParts.keySet().iterator();
        while(keys.hasNext()){
            String k = keys.next();
            Object o = bodyParts.get(k);
            if (k.toLowerCase().indexOf("text/html")!=-1){
                htmlBp = (String) o;
            }
            if (k.toLowerCase().indexOf("text/plain")!=-1){
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

    public void loadBodyParts()throws Exception{

        Hashtable bbpp = getAllBodyParts((MimeMultipart)this.originalJavaMailMessage.getContent());
        this.setBodyParts(bbpp);
    }

    private Hashtable<String,Object> getAllBodyParts(MimeMultipart mm) throws Exception{
        Hashtable<String,Object> ht = new Hashtable<>();
        ArrayList<MimeMultipart> parentBodyParts = new ArrayList();
        ArrayList<Integer> positions = new ArrayList();
        int currPos = 0;

        int size = mm.getCount();
        int bodyPartCounter = 0;
        int i = 0;
        while(true){
            for (; i < size; i++) {
                BodyPart bp = mm.getBodyPart(i);
                if (bp.getContent() instanceof MimeMultipart){
                    parentBodyParts.add(mm);
                    positions.add(i);
                    mm = (MimeMultipart) bp.getContent();
                    i = -1;
                    size = mm.getCount();
                }
                else if (bp.getContent() instanceof String){
                    ht.put(bp.getContentType(), bp.getContent());
                }
                else if (bp.getContent() instanceof BASE64DecoderStream){
                    BASE64DecoderStream b64ds = (BASE64DecoderStream) bp.getContent();
                    ByteArrayOutputStream bais = new ByteArrayOutputStream();
                    int b;
                    while((b = b64ds.read())!=-1){
                        bais.write(b);
                    }
                    String contentType = bp.getContentType();
                    ht.put(contentType, bais);
                }else{
                    int y = 2;
                }


            }
            if (parentBodyParts.size()>0){
                try{
                    mm = (MimeMultipart) parentBodyParts.get(parentBodyParts.size()-1);
                    parentBodyParts.remove(mm);
                    i = positions.get(positions.size()-1);
                    positions.remove(positions.size()-1);
                    i++;
                    size = mm.getCount();
                }catch(Exception exc){
                    int y = 2;
                    return null;
                }

            }else{
                break;
            }

        }

        return ht;
    }


}