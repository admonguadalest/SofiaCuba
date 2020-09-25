package com.company.test1.web.screens.entrada;

import com.company.test1.entity.ciclos.Evento;
import com.company.test1.entity.ciclos.OrdenTrabajo;
import com.company.test1.web.screens.ordentrabajo.OrdenTrabajoFragment;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.Fragments;
import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.components.Fragment;
import com.haulmont.cuba.gui.components.GroupBoxLayout;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.InstanceLoader;
import com.haulmont.cuba.gui.model.InstancePropertyContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.ciclos.Entrada;

import javax.inject.Inject;
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
        return entradaDc.getItem().getCiclo().getEventos();
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
    
    
    
    
    
}