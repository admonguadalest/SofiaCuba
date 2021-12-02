package com.company.test1.web.screens.reasignaciones;

import com.company.test1.entity.ciclos.Ciclo;
import com.company.test1.entity.ciclos.Entrada;
import com.company.test1.entity.ciclos.Evento;
import com.company.test1.entity.ciclos.ImputacionDocumentoImputable;
import com.company.test1.entity.documentosImputables.DocumentoImputable;
import com.company.test1.entity.documentosImputables.DocumentoProveedor;
import com.company.test1.entity.documentosImputables.FacturaProveedor;
import com.company.test1.entity.documentosImputables.Presupuesto;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.DataGrid;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@UiController("test1_ReasignacionImputaciones")
@UiDescriptor("reasignacion-imputaciones.xml")
public class ReasignacionImputaciones extends Screen {

    @Inject
    private DataGrid<ImputacionDocumentoImputable> imputacionesTable;
    @Inject
    private PickerField<Ciclo> pkrCiclo;
    @Inject
    private Notifications notifications;
    @Inject
    private DataManager dataManager;

    @Install(to = "imputacionesDl", target = Target.DATA_LOADER)
    private List<ImputacionDocumentoImputable> imputacionesDlLoadDelegate(LoadContext<ImputacionDocumentoImputable> loadContext) {
        List<ImputacionDocumentoImputable> l = dataManager.loadList(loadContext);
        for (int i = 0; i < l.size(); i++) {
            ImputacionDocumentoImputable idi = l.get(i);
            DocumentoImputable di = idi.getDocumentoImputable();
            if (di instanceof DocumentoProveedor){
                DocumentoProveedor dp = (DocumentoProveedor) di;
                if (dp instanceof FacturaProveedor){
                    dp = dataManager.reload(dp, "facturaProveedor-reasignaciones-imputaciones-view");
                }
                if (dp instanceof Presupuesto){
                    dp = dataManager.reload(dp, "presupuesto-reasignaciones-imputaciones-view");
                }

                idi.setDocumentoImputable(dp);
            }
        }
        return l;
    }




    public void reasignar(){
        List<ImputacionDocumentoImputable> imputaciones = new ArrayList(imputacionesTable.getSelected());
        Ciclo ciclo = pkrCiclo.getValue();
        ciclo = dataManager.reload(ciclo, "ciclo-view");
        if (ciclo==null){
            notifications.create().withCaption("Seleccionar un ciclo para reasignación").show();
            return;
        }
        for (int i = 0; i < imputaciones.size(); i++) {
            ImputacionDocumentoImputable idi = imputaciones.get(i);
            Evento ev = idi.getEvento();
            if (ev == null){
                Evento ev_ = dataManager.create(Evento.class);
                ev_.setArchivado(false);
                ev_.setCiclo(ciclo);
                ev_.setFecha(new Date());
                ev_.setNombre("NOMBRE EVENTO NO ASIGNADO");
                ev_.setTipoEvento(null);
                ev_ = dataManager.commit(ev_);
                ev = ev_;
                idi.setEvento(ev_);

            }else if (!cicloContainsEventoWithName(ev.getNombre(), ciclo)){
                Evento ev_ = dataManager.create(Evento.class);
                ev_.setArchivado(false);
                ev_.setCiclo(ciclo);
                ev_.setFecha(ev.getFecha());
                ev_.setNombre(ev.getNombre());
                ev_.setTipoEvento(ev.getTipoEvento());
                ev_ = dataManager.commit(ev_);
                ev = ev_;
                idi.setEvento(ev_);
            }else{
                for (int j = 0; j < ciclo.getEventos().size(); j++) {
                    Evento ev_ = ciclo.getEventos().get(i);
                    if (ev_.getNombre().compareTo(ev.getNombre())==0) {
                        idi.setEvento(ev_);
                        break;
                    }
                }

            }
            idi.setCiclo(ciclo);
            dataManager.commit(idi);
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
        String hql = "select e from test1_ImputacionDocumentoImputable e where e.evento.id = :evid ";
        List<ImputacionDocumentoImputable> l = dataManager.load(ImputacionDocumentoImputable.class).query(hql).parameter("evid", ev.getId()).list();
        if (l.size()>0){
            return true;
        }else{
            return false;
        }
    }
}
