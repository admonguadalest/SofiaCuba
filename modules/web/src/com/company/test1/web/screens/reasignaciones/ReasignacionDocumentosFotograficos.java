package com.company.test1.web.screens.reasignaciones;

import com.company.test1.entity.ciclos.Ciclo;
import com.company.test1.entity.ciclos.Entrada;
import com.company.test1.entity.ciclos.Evento;
import com.company.test1.entity.ciclos.ImputacionDocumentoImputable;
import com.company.test1.entity.documentosfotograficos.CarpetaDocumentosFotograficos;
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

@UiController("test1_ReasignacionDocumentosFotograficos")
@UiDescriptor("reasignacion-documentos-fotograficos.xml")
public class ReasignacionDocumentosFotograficos extends Screen {

    @Inject
    private DataGrid<CarpetaDocumentosFotograficos> cdfsTable;
    @Inject
    private PickerField<Ciclo> pkrCiclo;
    @Inject
    private Notifications notifications;
    @Inject
    private DataManager dataManager;







    public void reasignar(){
        List<CarpetaDocumentosFotograficos> cdfs = new ArrayList(cdfsTable.getSelected());
        Ciclo ciclo = pkrCiclo.getValue();
        ciclo = dataManager.reload(ciclo, "ciclo-view");
        if (ciclo==null){
            notifications.create().withCaption("Seleccionar un ciclo para reasignación").show();
            return;
        }
        for (int i = 0; i < cdfs.size(); i++) {
            CarpetaDocumentosFotograficos cdf = cdfs.get(i);
            Evento ev = cdf.getEvento();
            if (ev == null){
                Evento ev_ = dataManager.create(Evento.class);
                ev_.setArchivado(false);
                ev_.setCiclo(ciclo);
                ev_.setFecha(new Date());
                ev_.setNombre("NOMBRE EVENTO NO ASIGNADO");
                ev_.setTipoEvento(null);
                ev_ = dataManager.commit(ev_);
                ev = ev_;
                cdf.setEvento(ev_);

            }else if (!cicloContainsEventoWithName(ev.getNombre(), ciclo)){
                Evento ev_ = dataManager.create(Evento.class);
                ev_.setArchivado(false);
                ev_.setCiclo(ciclo);
                ev_.setFecha(ev.getFecha());
                ev_.setNombre(ev.getNombre());
                ev_.setTipoEvento(ev.getTipoEvento());
                ev_ = dataManager.commit(ev_);
                ev = ev_;
                cdf.setEvento(ev_);
            }else{
                for (int j = 0; j < ciclo.getEventos().size(); j++) {
                    Evento ev_ = ciclo.getEventos().get(i);
                    if (ev_.getNombre().compareTo(ev.getNombre())==0) {
                        cdf.setEvento(ev_);
                        break;
                    }
                }

            }
            cdf.setCiclo(ciclo);
            dataManager.commit(cdf);
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

    private boolean eventoContainsImputacion(Evento ev){
        String hql = "select e from test1_CarpetaDocumentosFotograficos e where e.evento.id = :evid ";
        List<ImputacionDocumentoImputable> l = dataManager.load(ImputacionDocumentoImputable.class).query(hql).parameter("evid", ev.getId()).list();
        if (l.size()>0){
            return true;
        }else{
            return false;
        }
    }
}
