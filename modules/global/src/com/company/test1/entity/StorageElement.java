package com.company.test1.entity;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

import java.util.Hashtable;

@MetaClass(name = "test1_StorageElement")
public class StorageElement extends BaseUuidEntity {
    private static final long serialVersionUID = -255105427750837861L;

    static Hashtable<String, String> mimeTypes = new Hashtable<String, String>();

    static {
        mimeTypes.put("pdf", "application/pdf");
        mimeTypes.put("jpg", "image/jpeg");
        mimeTypes.put("jpeg", "image/jpeg");
        mimeTypes.put("png", "image/png");
        mimeTypes.put("gif", "image/gif");
    }

    @MetaProperty
    private String elementName;

    @MetaProperty
    private Boolean isFolder;

    @MetaProperty
    private String elementPath;

    @MetaProperty
    private String elementParentFolder;

    @MetaProperty
    private byte[] representacionSerial;

    public byte[] getRepresentacionSerial() {
        return representacionSerial;
    }

    public void setRepresentacionSerial(byte[] representacionSerial) {
        this.representacionSerial = representacionSerial;
    }

    @MetaProperty
    public String getMimeType() {
        String extension = this.elementName.substring(elementName.lastIndexOf(".")+1);
        return getMimeTypeFromExtension(extension.toLowerCase());
    }

    private String getMimeTypeFromExtension(String extension){
        if (mimeTypes.containsKey(extension)){
            return mimeTypes.get(extension);
        }else{
            return "application/octet-stream";
        }
    }

    public Boolean getIsFolder() {
        return isFolder;
    }

    public void setIsFolder(Boolean isFolder) {
        this.isFolder = isFolder;
    }

    public String getElementParentFolder() {
        return elementParentFolder;
    }

    public void setElementParentFolder(String elementParentFolder) {
        this.elementParentFolder = elementParentFolder;
    }

    public String getElementPath() {
        return elementPath;
    }

    public void setElementPath(String elementPath) {
        this.elementPath = elementPath;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }
}