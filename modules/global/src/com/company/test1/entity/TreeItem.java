package com.company.test1.entity;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.StandardEntity;


@MetaClass(name = "test1_TreeItem")
public class TreeItem extends BaseUuidEntity {
    private static final long serialVersionUID = 703904945621579358L;

    public TreeItem(){
        super();
    }

    @MetaProperty
    protected StandardEntity userObject;

    @MetaProperty
    protected TreeItem parentItem;

    @MetaProperty
    public String getUserObjectText() {
        return ((AsTreeItem)userObject).getTextAsTreeItem();
    }

    public TreeItem getParentItem() {
        return parentItem;
    }

    public void setParentItem(TreeItem parentItem) {
        this.parentItem = parentItem;
    }

    public StandardEntity getUserObject() {
        return userObject;
    }

    public void setUserObject(StandardEntity userObject) {
        this.userObject = userObject;
    }


}