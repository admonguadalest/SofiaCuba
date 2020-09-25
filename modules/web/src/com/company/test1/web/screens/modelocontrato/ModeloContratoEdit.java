package com.company.test1.web.screens.modelocontrato;

import com.company.test1.entity.modeloscontratosinquilinos.Seccion;
import com.company.test1.entity.recibos.Concepto;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.RichTextArea;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.InstanceLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.modeloscontratosinquilinos.ModeloContrato;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@UiController("test1_ModeloContrato.edit")
@UiDescriptor("modelo-contrato-edit.xml")
@EditedEntityContainer("modeloContratoDc")
@LoadDataBeforeShow
public class ModeloContratoEdit extends StandardEditor<ModeloContrato> {

    @Inject
    private Table<Seccion> tableSecciones;

    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private DataContext dataContext;
    @Inject
    private InstanceContainer<ModeloContrato> modeloContratoDc;
    @Inject
    private UiComponents uiComponents;
    @Inject
    private Notifications notifications;
    @Inject
    private DataManager dataManager;
    @Inject
    private InstanceLoader<ModeloContrato> modeloContratoDl;
    @Inject
    private CollectionPropertyContainer<Seccion> seccionesDc;


    @Subscribe
    private void onAfterInit(AfterInitEvent event) {
        if (event.getOptions() instanceof MapScreenOptions){
            MapScreenOptions mso = (MapScreenOptions) event.getOptions();
            Map m = mso.getParams();
            if (m.containsKey("newEntity")){

                this.setEntityToEdit(new ModeloContrato());
            }
        }

    }


    
    
    
    

    @Subscribe("tableSecciones.create")
    private void onTableSeccionesCreate(Action.ActionPerformedEvent event) {
//        ScreenLaunchUtil.launchNewEntityStreen(Seccion.class, null, tableSecciones, screenBuilders, this, OpenMode.DIALOG, dataContext, null);
        Screen s = screenBuilders.editor(Seccion.class, this)
                .withLaunchMode(OpenMode.DIALOG)
                .newEntity()
                .withParentDataContext(dataContext)
                .withListComponent(tableSecciones)
                .withInitializer(seccion->{seccion.setModeloContrato(modeloContratoDc.getItem());})
                .build();
        s.show();
    }

    @Subscribe("tableSecciones.edit")
    private void onTableSeccionesEdit(Action.ActionPerformedEvent event) {
        ScreenLaunchUtil.launchEditEntityScreen(tableSecciones.getSingleSelected(), null, tableSecciones, screenBuilders, this, OpenMode.DIALOG, dataContext, null);
    }

    public RichTextArea getDescripcionColumn(Seccion s){
        RichTextArea rta = uiComponents.create(RichTextArea.NAME);
        rta.setValue(s.getDescripcion());
        rta.setEditable(false);
        rta.setWidth("100%");
        rta.setStyleName("textAreaInTableCell");
        return rta;
    }


    public void onBtnSubirClick() {
        Set<Seccion> s = tableSecciones.getSelected();

        if (s.size()==0){
            notifications.create().withCaption("Seleccionar una seccion").show();
            return;
        }
        Seccion c = (Seccion) new ArrayList(s).get(0);
        c = seccionesDc.getItem(c.getId());
        c = seccionesDc.getMutableItems().get(seccionesDc.getItems().indexOf(c));
        Integer i = c.getOrdenacion();
        if (i==1){
            notifications.create().withCaption("La seccion seleccionada ya es la mas alta de la lista").show();
            return;
        }else{
            Seccion c2 = null;
            try {
                c2 = getSeccionDesdeOrdenacion(i-1);
            }catch(Exception exc){

            }
            if (c2 == null){
                notifications.create().withCaption("No se hallo seccion con ordenacion superior. Si es preciso corregir manualmente.").show();
                return;
            }
            Integer nord = c2.getOrdenacion();
            c2.setOrdenacion(i);
            c.setOrdenacion(nord);


        }

        tableSecciones.sort("ordenacion", Table.SortDirection.ASCENDING);


    }

    public void onBtnBajarClick() {
        Set<Seccion> s = tableSecciones.getSelected();
        if (s.size()==0){
            notifications.create().withCaption("Seleccionar una seccion").show();
            return;
        }
        Seccion c = (Seccion) new ArrayList(s).get(0);
        Integer i = c.getOrdenacion();
        Seccion c2 = null;
        try {
            c2 = getSeccionDesdeOrdenacion(i+1);
        }catch(Exception exc){
            int y = 2;
        }
        if (c2 == null){
            notifications.create().withCaption("No se hallo seccion con ordenacion inferior. Si es preciso corregir manualmente.").show();
            return;
        }
        Integer nord = c2.getOrdenacion();
        c2.setOrdenacion(i);
        c.setOrdenacion(nord);



        tableSecciones.sort("ordenacion", Table.SortDirection.ASCENDING);



    }

    public void onBtnRehacerOrdenacionClick() {
        ArrayList al = new ArrayList(tableSecciones.getItems().getItems());
        for (int i = 0; i < al.size(); i++) {
            Seccion s = (Seccion) al.get(i);
            s.setOrdenacion(i+1);
        }


    }

    public Seccion getSeccionDesdeOrdenacion(Integer ordenacion){
        ArrayList<Seccion> al = new ArrayList(seccionesDc.getMutableItems());
        try{
            for (int i = 0; i < al.size(); i++) {
                if (al.get(i).getOrdenacion().intValue()==ordenacion.intValue()){
                    return al.get(i);
                }
            }
        }catch(Exception exc){
            return null;
        }
        return null;
    }
}