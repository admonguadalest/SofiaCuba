package com.company.test1.web.screens.reasignaciones;

import com.company.test1.entity.ciclos.Ciclo;
import com.company.test1.entity.ciclos.Entrada;
import com.company.test1.entity.ciclos.Evento;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.DataGrid;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@UiController("test1_ReasignacionEntradas")
@UiDescriptor("reasignacion-entradas.xml")
public class ReasignacionEntradas extends Screen {


    @Inject
    private DataGrid<Entrada> entradasTable;
    @Inject
    private PickerField<Ciclo> pkrCiclo;
    @Inject
    private Notifications notifications;
    @Inject
    private DataManager dataManager;


    
    



    public void reasignar(){
        List<Entrada> entradas = new ArrayList(entradasTable.getSelected());
        Ciclo ciclo = pkrCiclo.getValue();
        ciclo = dataManager.reload(ciclo, "ciclo-view");
        if (ciclo==null){
            notifications.create().withCaption("Seleccionar un ciclo para reasignación").show();
            return;
        }
        for (int i = 0; i < entradas.size(); i++) {
            Entrada e = entradas.get(i);
            Evento ev = e.getEvento();
            if (ev == null){
                Evento ev_ = dataManager.create(Evento.class);
                ev_.setArchivado(false);
                ev_.setCiclo(ciclo);
                ev_.setFecha(new Date());
                ev_.setNombre("NOMBRE EVENTO NO ASIGNADO");
                ev_.setTipoEvento(null);
                ev_ = dataManager.commit(ev_);
                ev = ev_;
                e.setEvento(ev_);

            }else if (!cicloContainsEventoWithName(ev.getNombre(), ciclo)){
                Evento ev_ = dataManager.create(Evento.class);
                ev_.setArchivado(false);
                ev_.setCiclo(ciclo);
                ev_.setFecha(ev.getFecha());
                ev_.setNombre(ev.getNombre());
                ev_.setTipoEvento(ev.getTipoEvento());
                ev_ = dataManager.commit(ev_);
                ev = ev_;
                e.setEvento(ev_);
            }else{
                for (int j = 0; j < ciclo.getEventos().size(); j++) {
                    Evento ev_ = ciclo.getEventos().get(i);
                    if (ev_.getNombre().compareTo(ev.getNombre())==0) {
                        e.setEvento(ev_);
                        break;
                    }
                }

            }
            e.setCiclo(ciclo);
            dataManager.commit(e);
            ciclo = dataManager.reload(ciclo, "ciclo-view");

        }
    }

    private boolean cicloContainsEventoWithName(String name, Ciclo ciclo){
        List<Evento> eventos = ciclo.getEventos();
        for (int i = 0; i < eventos.size(); i++) {
            Evento ev = eventos.get(i);
            if (ev.getNombre().compareTo(name)==0){
                return true;
            }
        }
        return false;
    }

    private boolean eventoContainsEntradas(Evento ev){
        String hql = "select e from test1_Entrada e where e.evento.id = :evid ";
        List<Entrada> l = dataManager.load(Entrada.class).query(hql).parameter("evid", ev.getId()).list();
        if (l.size()>0){
            return true;
        }else{
            return false;
        }
    }
}