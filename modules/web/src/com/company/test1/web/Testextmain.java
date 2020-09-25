package com.company.test1.web;

import com.company.test1.entity.recibos.Remesa;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.components.TextArea;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import freemarker.template.SimpleDate;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@UiController("test1_Testextmain")
@UiDescriptor("testExtMain.xml")
public class Testextmain extends Screen {

    @Inject
    private CollectionLoader<Remesa> remesasDl;
    @Inject
    private DataManager dataManager;

    @Subscribe
    private void onAfterInit(AfterInitEvent event) {
        int y = 2;
        remesasDl.load();
        
    }

    @Install(to = "remesasDl", target = Target.DATA_LOADER)
    private List<Remesa> remesasDlLoadDelegate(LoadContext<Remesa> loadContext) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date d = null;
        try{
            d = sdf.parse("03/06/2005");
        }catch(Exception e){

        }


        String hql = "SELECT r FROM test1_Remesa r JOIN r.definicionRemesa dr WHERE dr.nombreRemesa like :nr AND r.fechaRealizacion > :fr";
        List<Remesa> rr = dataManager.loadValue(hql, Remesa.class).parameter("nr", "%CGCGBANCARIA%").parameter("fr", d).list();
        return rr;
    }
    
    




    
    
    
    
}