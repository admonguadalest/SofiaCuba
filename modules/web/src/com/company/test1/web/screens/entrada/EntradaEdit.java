package com.company.test1.web.screens.entrada;

import com.company.test1.entity.ciclos.Evento;
import com.company.test1.entity.ciclos.OrdenTrabajo;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.company.test1.web.screens.evento.EventoEdit;
import com.company.test1.web.screens.ordentrabajo.OrdenTrabajoFragment;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.Fragments;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.*;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.ciclos.Entrada;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@UiController("test1_Entrada.edit")
@UiDescriptor("entrada-edit.xml")
@EditedEntityContainer("entradaDc")
@LoadDataBeforeShow
public class EntradaEdit extends StandardEditor<Entrada> {

    @Inject
    private InstanceContainer<Entrada> entradaDc;
    @Inject
    private InstancePropertyContainer<OrdenTrabajo> ordenTrabajoDc;
    @Inject
    private InstanceLoader<Entrada> entradaDl;
    @Inject
    private CheckBox chkActivarOrdenTrabajo;
    @Inject
    private DataContext dataContext;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private CollectionLoader<Evento> eventoesLc;
    @Inject
    private LookupField<Evento> eventoField;

    @Subscribe
    private void onBeforeShow(BeforeShowEvent event) {
        entradaDl.load();
    }

    @Inject
    private Fragments fragments;

    @Inject
    private GroupBoxLayout gbxOrdenTrabajo;

    OrdenTrabajoFragment ordenTrabajoFragment = null;

    @Install(to = "eventoesLc", target = Target.DATA_LOADER)
    private List<Evento> eventoesLcLoadDelegate(LoadContext<Evento> loadContext) {
//        return entradaDc.getItem().getCiclo().getEventos();
        ArrayList al = new ArrayList(entradaDc.getItem().getCiclo().getEventos());
        try {
            Collections.sort(al, new Comparator<Evento>() {

                public int compare(Evento ev1, Evento ev2) {
                    try {
                        /**
                         * Existe un error de datos en la tabla evento. Hay 206 registros sin fecha asignada.
                         * Se debería corregir
                         * PENDIENTE
                         */
                        if ((ev1.getFecha() == null) || (ev2.getFecha() == null)) {
                            return -1;
                        }
                        return -1 * ev1.getFecha().compareTo(ev2.getFecha());
                    } catch (Exception exc) {
                        int y = 2;
                    }
                    return -1;
                }

            });
        }catch(Exception exc){
            int y = 2;
        }
        return al;
    }

    @Subscribe
    private void onBeforeShow1(BeforeShowEvent event) {
        if (!PersistenceHelper.isNew(entradaDc.getItem())){
            if (entradaDc.getItem().getOrdenTrabajo()!=null){
                chkActivarOrdenTrabajo.setValue(true);
                chkActivarOrdenTrabajo.setEditable(false);
                gbxOrdenTrabajo.setVisible(true);
            }
        }else{
            if (entradaDc.getItem().getOrdenTrabajo()!=null){
                chkActivarOrdenTrabajo.setValue(true);
                chkActivarOrdenTrabajo.setEditable(false);
                gbxOrdenTrabajo.setVisible(true);
            }
        }
    }
    
    

    @Subscribe("chkActivarOrdenTrabajo")
    private void onChkActivarOrdenTrabajoValueChange(HasValue.ValueChangeEvent<Boolean> event) {
        if (event.getValue()){
            OrdenTrabajo ot = entradaDc.getItem().getOrdenTrabajo();
            if (ot == null){
                ot = dataContext.create(OrdenTrabajo.class);
                ot.setEntrada(entradaDc.getItem());
                entradaDc.getItem().setOrdenTrabajo(ot);


            }
            ordenTrabajoDc.setItem(ot);
            if (ordenTrabajoFragment==null){
                ordenTrabajoFragment = fragments.create(this, OrdenTrabajoFragment.class);
            }
            gbxOrdenTrabajo.setVisible(true);
            gbxOrdenTrabajo.add(ordenTrabajoFragment.getFragment());
        }else{
            ordenTrabajoDc.setItem(null);
            entradaDc.getItem().setOrdenTrabajo(null);
            gbxOrdenTrabajo.remove(ordenTrabajoFragment.getFragment());
        }
    }
    
    
    public void OnBtnNuevoEventoClick(){

            ScreenLaunchUtil.launchNewEntityStreen(Evento.class, screenBuilders, this, OpenMode.DIALOG, dataContext,
                    (s)->{
                        Evento ev = ((EventoEdit)s).getEditedEntity();
                        entradaDc.getItem().getCiclo().getEventos().add(ev);
                        ev.setCiclo(entradaDc.getItem().getCiclo());
                        entradaDc.getItem().setEvento(ev);
                        eventoesLc.load();
                        eventoField.setValue(ev);
                        dataContext.merge(ev);
                    });

    }
    
    
}