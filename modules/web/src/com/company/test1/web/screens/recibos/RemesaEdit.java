package com.company.test1.web.screens.recibos;

import com.company.test1.entity.recibos.OrdenanteRemesa;
import com.company.test1.entity.recibos.Recibo;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.recibos.Remesa;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@UiController("test1_Remesa.edit")
@UiDescriptor("remesa-edit.xml")
@EditedEntityContainer("remesaDc")
@LoadDataBeforeShow
public class RemesaEdit extends StandardEditor<Remesa> {

    @Inject
    private DataManager dataManager;
    @Inject
    private InstanceContainer<Remesa> remesaDc;

    @Install(to = "recibosDl", target = Target.DATA_LOADER)
    private List<Recibo> recibosDlLoadDelegate(LoadContext<Recibo> loadContext) {
        ArrayList<Recibo> al = new ArrayList<Recibo>();
        try {
            if (remesaDc.getItem().getOrdenantesRemesa() == null) return null;
            for (int i = 0; i < remesaDc.getItem().getOrdenantesRemesa().size(); i++) {
                OrdenanteRemesa orem = remesaDc.getItem().getOrdenantesRemesa().get(i);
                List<Recibo> l = orem.getRecibos();
                for (int j = 0; j < l.size(); j++) {
                    Recibo recibo = l.get(j);
                    recibo = dataManager.reload(recibo, "recibo-view");
                    al.add(recibo);
                }

            }
        }catch(Exception exc){
            int y = 2;
        }

        return al;
    }
    
    
}