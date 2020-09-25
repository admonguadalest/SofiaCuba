package com.company.test1.web;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.actions.list.CreateAction;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.data.meta.EntityDataUnit;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.Screen;

import javax.inject.Inject;

public class CustomCreateAction extends CreateAction {

    DataContext parentDataContext = null;



    public CustomCreateAction(DataContext parentDataContext, Security security, ScreenBuilders screenBuilders){
        this.parentDataContext = parentDataContext;
        this.security = security;
        this.screenBuilders = screenBuilders;
    }

    @Override
    protected boolean isPermitted() {
        return super.isPermitted();
    }

    @Override
    public void actionPerform(Component component) {
        // if standard behaviour
        if (!hasSubscriptions(ActionPerformedEvent.class)) {
            if (target == null) {
                throw new IllegalStateException("CreateAction target is not set");
            }

            if (!(target.getItems() instanceof EntityDataUnit)) {
                throw new IllegalStateException("CreateAction target items is null or does not implement EntityDataUnit");
            }

            MetaClass metaClass = ((EntityDataUnit) target.getItems()).getEntityMetaClass();
            if (metaClass == null) {
                throw new IllegalStateException("Target is not bound to entity");
            }

            Screen editor = null;
            if (parentDataContext!=null) {
                editor = screenBuilders.editor(target)
                        .newEntity()
                        .withParentDataContext(this.parentDataContext)
                        .build();
            }else{
                editor = screenBuilders.editor(target)
                        .newEntity()
                        .build();
            }
            editor.show();
        } else {
            super.actionPerform(component);
        }
    }
}
