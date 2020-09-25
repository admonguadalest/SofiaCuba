package com.company.test1.web.screens.modelocontrato;

import com.company.test1.entity.modeloscontratosinquilinos.Clausula;
import com.company.test1.entity.modeloscontratosinquilinos.ModeloContrato;
import com.company.test1.service.ModeloContratoInquilinoService;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.RichTextArea;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.InstanceLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.modeloscontratosinquilinos.Seccion;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Set;

@UiController("test1_Seccion.edit")
@UiDescriptor("seccion-edit.xml")
@EditedEntityContainer("seccionDc")
@LoadDataBeforeShow
public class SeccionEdit extends StandardEditor<Seccion> {

    @Inject
    private Table<Clausula> tableClausulas;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private DataContext dataContext;
    @Inject
    private UiComponents uiComponents;
    @Inject
    private InstanceContainer<Seccion> seccionDc;
    @Inject
    private ModeloContratoInquilinoService modeloContratoInquilinoService;
    @Inject
    private Notifications notifications;
    @Inject
    private CollectionPropertyContainer<Clausula> clausulasDc;
    @Inject
    private InstanceLoader<Seccion> seccionDl;

    @Subscribe
    private void onAfterShow(AfterShowEvent event) {
        Seccion s = seccionDc.getItem();
        if (s.getOrdenacion()==null){
            Integer i = modeloContratoInquilinoService.getValorOrdenacionNuevaSeccion(s.getModeloContrato());
            s.setOrdenacion(i);
        }
        
    }
    
    

    @Subscribe("tableClausulas.create")
    private void onTableClausulasCreate(Action.ActionPerformedEvent event) {
//        ScreenLaunchUtil.launchNewEntityStreen(Clausula.class, null, tableClausulas, screenBuilders, this, OpenMode.DIALOG, dataContext, null);
        Screen s = screenBuilders.editor(Clausula.class, this)
                .withLaunchMode(OpenMode.DIALOG)
                .newEntity()
                .withParentDataContext(dataContext)
                .withListComponent(tableClausulas)
                .withInitializer(clausula->{clausula.setSeccion(seccionDc.getItem());})
                .build();
        s.show();
    }

    @Subscribe("tableClausulas.edit")
    private void onTableClausulasEdit(Action.ActionPerformedEvent event) {
        ScreenLaunchUtil.launchEditEntityScreen(tableClausulas.getSingleSelected(), null, tableClausulas, screenBuilders, this, OpenMode.DIALOG, dataContext, null);
    }

    public RichTextArea getDescripcionColumn(Clausula c){
        RichTextArea rta = uiComponents.create(RichTextArea.NAME);
        rta.setValue(c.getDescripcion());
        rta.setEditable(false);
        rta.setWidth("100%");
        rta.setStyleName("textAreaInTableCell");
        return rta;
    }

    public void onBtnSubirClick() {
        Set<Clausula> s = tableClausulas.getSelected();

        if (s.size()==0){
            notifications.create().withCaption("Seleccionar una cláusula").show();
            return;
        }
        Clausula c = (Clausula) new ArrayList(s).get(0);
        c = clausulasDc.getItem(c.getId());
        c = clausulasDc.getMutableItems().get(clausulasDc.getItems().indexOf(c));
        Integer i = c.getOrdenacion();
        if (i==1){
            notifications.create().withCaption("La cláusula seleccionada ya es la mas alta de la lista").show();
            return;
        }else{
            Clausula c2 = null;
            try {
                c2 = getClausulaDesdeOrdenacion(i-1);
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

        tableClausulas.sort("ordenacion", Table.SortDirection.ASCENDING);

    }

    public void onBtnBajarClick() {
        Set<Clausula> s = tableClausulas.getSelected();
        if (s.size()==0){
            notifications.create().withCaption("Seleccionar una cláusula").show();
            return;
        }
        Clausula c = (Clausula) new ArrayList(s).get(0);
        Integer i = c.getOrdenacion();
        Clausula c2 = null;
        try {
            c2 = getClausulaDesdeOrdenacion(i+1);
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


        tableClausulas.sort("ordenacion", Table.SortDirection.ASCENDING);


    }

    public void onBtnRehacerOrdenacionClick() {
        ArrayList al = new ArrayList(tableClausulas.getItems().getItems());
        for (int i = 0; i < al.size(); i++) {
            Clausula s = (Clausula) al.get(i);
            s.setOrdenacion(i+1);
        }


    }

    public Clausula getClausulaDesdeOrdenacion(Integer ordenacion){
        ArrayList<Clausula> al = new ArrayList(clausulasDc.getMutableItems());
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