package com.company.test1.web.screens.incrementos;

import com.company.test1.entity.TreeItem;
import com.company.test1.entity.coeficientes.TipoCoeficiente;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.enums.IncrementoGeneralObrasModoReparticion;
import com.company.test1.entity.enums.TipoContratoInquilinoEnum;
import com.company.test1.entity.enums.recibos.ConceptoReciboVigenciaEnum;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.incrementos.Incremento;
import com.company.test1.entity.recibos.Concepto;
import com.company.test1.entity.recibos.ConceptoAdicionalConceptoRecibo;
import com.company.test1.entity.recibos.ConceptoRecibo;
import com.company.test1.service.ContratosService;
import com.company.test1.service.IncrementosService;
import com.company.test1.service.JasperReportService;
import com.company.test1.web.GuiUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.Date;
import java.util.*;

@UiController("test1_IncrementosGenerales")
@UiDescriptor("incrementos-generales.xml")
public class IncrementosGenerales extends Screen {

    @Inject
    private TextField<Double> txtImporteAumento;
    @Inject
    private TextArea<String> txaDescripcion;
    @Inject
    private Tree<TreeItem> treePropietariosUbicaciones;
    @Inject
    private CheckBox chk12Perc;


    @Inject
    private Button btnProcesarIncrementosRecibos;
    @Inject
    private Button btnPrevisualizarContratosAfectados;


    @Inject
    private IncrementosService incrementosService;

    @Inject
    private Button btnCerrar;
    @Inject
    private CheckBox chkAplicacionIPC;
    @Inject
    private DateField<Date> dteFechaAplicacion;
    @Inject
    private DateField<Date> dteFechaAplicacionOcupacionHasta;
    @Inject
    private LookupField<TipoCoeficiente> lkpCoeficienteReparto;
    @Inject
    private LookupField<Concepto> lkpConcepto;
    @Inject
    private LookupField<Concepto> lkpConceptoAtrasos;
    @Inject
    private LookupField<TipoContratoInquilinoEnum> lkpDiscrimLau;
    @Inject
    private LookupField<IncrementoGeneralObrasModoReparticion> lkpModoImputacion;
    @Inject
    private CollectionLoader<TreeItem> treeItemsDl;
    @Inject
    private LookupField<Integer> lkpNumMesesAtrasos;
    @Inject
    private CollectionLoader<TipoCoeficiente> tipoCoeficientesDl;
    @Inject
    private CollectionLoader<Departamento> departamentosDl;
    @Inject
    private LookupField<ConceptoReciboVigenciaEnum> lkpVigencia;
    @Inject
    private Table tableDepartamentos;

    @Inject
    private CollectionLoader<Concepto> conceptosDl;
    @Inject
    private DataManager dataManager;
    @Inject
    private Notifications notifications;
    @Inject
    private ContratosService contratosService;
    @Inject
    private HBoxLayout hBoxVigencia;
    @Inject
    private UiComponents uiComponents;

    DateField<Date> dteDesde = null;
    DateField<Date> dteHasta = null;
    TextField<Integer> txfNumEmisiones = null;
    Object[] infoVigencia = new Object[5];
    @Inject
    private JasperReportService jasperReportService;
    @Inject
    private ExportDisplay exportDisplay;

    @Subscribe
    private void onAfterShow(AfterShowEvent event) {
        treeItemsDl.load();
        departamentosDl.load();
        conceptosDl.load();
        tipoCoeficientesDl.load();

        lkpNumMesesAtrasos.setOptionsList(Arrays.asList(0, 1,2,3,4,5,6,7,8,9));
        
        

    }

    @Subscribe("lkpVigencia")
    private void onLkpVigenciaValueChange(HasValue.ValueChangeEvent<ConceptoReciboVigenciaEnum> event) {
        hBoxVigencia.removeAll();
        hBoxVigencia.setVisible(false);
        if (event.getValue()==ConceptoReciboVigenciaEnum.PERMANENTE){
            //no se hace nada
        }
        if (event.getValue()==ConceptoReciboVigenciaEnum.ACTIVACION){
            CheckBox chbActivacion = uiComponents.create(CheckBox.NAME);
            chbActivacion.setCaption("Activado/Desactivado");
            chbActivacion.addValueChangeListener(e->{infoVigencia[1] = e.getValue();});
            hBoxVigencia.add(chbActivacion);
        }
        if (event.getValue()==ConceptoReciboVigenciaEnum.NUMERO_EMISIONES){
            txfNumEmisiones = uiComponents.create(TextField.NAME);
            txfNumEmisiones.setCaption("Numero de Emisiones");
            txfNumEmisiones.addValueChangeListener(e->{infoVigencia[4]=e.getValue();});
            hBoxVigencia.add(txfNumEmisiones);
            hBoxVigencia.setVisible(true);
        }
        if (event.getValue()==ConceptoReciboVigenciaEnum.ENTRE_FECHAS){
            dteDesde = uiComponents.create(DateField.NAME);
            dteDesde.setCaption("Desde");
            dteDesde.addValueChangeListener(e->{infoVigencia[2]=e.getValue();});
            dteHasta = uiComponents.create(DateField.NAME);
            dteHasta.setCaption("Hasta");
            dteHasta.addValueChangeListener(e->{infoVigencia[3]=e.getValue();});
            hBoxVigencia.add(dteDesde,dteHasta);
            hBoxVigencia.setVisible(true);
        }

        
    }
    
    



    @Install(to = "departamentosDl", target = Target.DATA_LOADER)
    private List<Departamento> departamentosDlLoadDelegate(LoadContext<Departamento> loadContext) {
        TreeItem ti = treePropietariosUbicaciones.getSingleSelected();
        if (ti!=null){
            if (ti.getUserObject() instanceof Ubicacion){
                Ubicacion u = (Ubicacion) ti.getUserObject();
                u = dataManager.reload(u, "ubicacion-with-direcciones");
                List<Departamento> deptos = new ArrayList(u.getDepartamentos());
                Collections.sort(deptos, Departamento.rm2idComparator);
                return deptos;
            }else{
                return new ArrayList();
            }

        }
        return new ArrayList();
    }
    
    

    @Install(to = "treeItemsDl", target = Target.DATA_LOADER)
    private List<TreeItem> treeItemsDlLoadDelegate(LoadContext<TreeItem> loadContext) {
        List<Propietario> props2 = dataManager.loadValue("select p from test1_Persona pers JOIN pers.propietario p", Propietario.class).list();
        List<Propietario> props = new ArrayList<>();
        for (int i = 0; i < props2.size(); i++) {
            Propietario propietario =  props2.get(i);
            propietario = dataManager.reload(propietario, "propietario-view");
            props.add(propietario);
        }

        HashMap hm_ = new HashMap();
        for (int i = 0; i < props.size(); i++) {
            ArrayList<Ubicacion> uu = new ArrayList<Ubicacion>();
            Propietario prop =  props.get(i);
            HashMap hm = new HashMap();
            hm.put("pid", prop.getId());
            List<Ubicacion> ubs = dataManager.loadValue("select u from test1_Ubicacion u JOIN u.propietario p where p.id = :pid", Ubicacion.class)
                    .setParameters(hm).list();
            uu.addAll(ubs);

            ubs = dataManager.loadValue("select u from test1_Departamento d JOIN d.propietario p JOIN d.ubicacion u WHERE p.id = :pid", Ubicacion.class)
                    .setParameters(hm).list();
            uu.addAll(ubs);
            hm_.put(prop, uu);
        }





        ArrayList<TreeItem> items = new ArrayList<TreeItem>();


        for (int i = 0; i < props.size(); i++) {
            Propietario p = props.get(i);
            List<Ubicacion> uu = (List<Ubicacion>) hm_.get(p);
            TreeItem ti = new TreeItem();
            ti.setUserObject(p);
            items.add(ti);
            for (int j = 0; j < uu.size(); j++) {
                Ubicacion ubicacion =  uu.get(j);
                TreeItem ti_ = new TreeItem();
                ti_.setUserObject(ubicacion);
                ti_.setParentItem(ti);
                items.add(ti_);
            }
        }

        Collections.sort(items, new Comparator<TreeItem>() {
            @Override
            public int compare(TreeItem o1, TreeItem o2) {
                if ((o1.getUserObject() instanceof Propietario)&&(o2.getUserObject()instanceof Propietario)){
                    return (((Propietario)o1.getUserObject()).getPersona().getNombreCompleto().compareTo(((Propietario)o2.getUserObject()).getPersona().getNombreCompleto()));
                }
                if ((o1.getUserObject() instanceof Ubicacion)&&(o2.getUserObject()instanceof Ubicacion)){
                    return (((Ubicacion)o1.getUserObject()).getNombre().compareTo(((Ubicacion)o2.getUserObject()).getNombre()));
                }
                if ((o1.getUserObject() instanceof Propietario)&&(o2.getUserObject()instanceof Ubicacion)){
                    return 1;
                }
                if ((o1.getUserObject() instanceof Ubicacion)&&(o2.getUserObject()instanceof Propietario)){
                    return -1;
                }
                return 1;
            }
        });

        return items;



    }

    @Subscribe("treePropietariosUbicaciones")
    private void onTreePropietariosUbicacionesSelection(Tree.SelectionEvent<TreeItem> event) {
        if (event.getSelected().size()>0){
            TreeItem ti = (TreeItem) event.getSelected().toArray()[0];
            if (ti.getUserObject() instanceof Ubicacion){
                departamentosDl.load();
            }
        }
    }


    public void onBtnSeleccionarTodosDeptosClick() {
        GuiUtils.selectAll(tableDepartamentos);
    }

    public void onBtnInvertirDeptosClick() {
        GuiUtils.invertSelection(tableDepartamentos);
    }

    private boolean datosPaginaListosParaEjecutarRealizacionOPrevisualizacion(){
        String mensajeas = "";
        if (tableDepartamentos.getSelected().size()==0){
            mensajeas += "Seleccione los Departamentos Afectados\n";
        }
        if(lkpConcepto.getValue()==null){
            mensajeas += "Seleccionar un ConceptoBean en que aplicar los incrementos\n";
        }
        if(lkpConceptoAtrasos.getValue()==null){
            mensajeas += "Seleccionar un ConceptoBean en que aplicar los incrementos atrasados y pendientes de aplicacion\n";
        }
        if (lkpNumMesesAtrasos.getValue()==null){
            mensajeas += "Indicar el numerod e meses de atrasos\n";
        }
        if (lkpCoeficienteReparto.getValue()==null){
            mensajeas += "Indicar Coeficiente de Reparto\n";
        }
        if (dteFechaAplicacion.getValue()==null){
            mensajeas += "Aportar Fecha de Aplicacion\n";
        }
        if (dteFechaAplicacionOcupacionHasta.getValue()==null){
            mensajeas += "Aportar Fecha Umbral de Ocupacion para la aplicacion de incrementos\n";
        }
        if (txtImporteAumento.getValue()==null){
            mensajeas += "Aportar importe de Incremento\n";
        }
        if (lkpModoImputacion.getValue()==null){
            mensajeas += "Asignar un Modo de Imputacion\n";
        }
        if (lkpVigencia.getValue()==null){
            mensajeas += "Seleccionar un tipo de vigencia para los conceptos de recibo a crear\n";
        }

        if (mensajeas.trim().length()==0){
            return true;
        }

        notifications.create().withCaption("Datos Insuficientes").withDescription(mensajeas).show();
        return false;
    }

    private void completaInfoVigencia(){
        infoVigencia = new Object[]{null, null, null, null, null};
        infoVigencia[0] = lkpVigencia.getValue();
        if (lkpVigencia.getValue() == ConceptoReciboVigenciaEnum.ENTRE_FECHAS){
            infoVigencia[2] = dteDesde.getValue();
            infoVigencia[3] = dteHasta.getValue();
        }
        if (lkpVigencia.getValue()==ConceptoReciboVigenciaEnum.NUMERO_EMISIONES){
            infoVigencia[4] = txfNumEmisiones.getValue();
        }

    }

    public void onBtnProcesarIncrementosRecibosClick() {
        if (!datosPaginaListosParaEjecutarRealizacionOPrevisualizacion()){
            return;
        }

        List<Entity> entitiesToPersist = new ArrayList<Entity>();

        int contadorRecibos = 0;
        List<Departamento> deptos = new ArrayList(tableDepartamentos.getSelected());
        Hashtable<Departamento, ConceptoRecibo[]> htDeptosCCRR = new Hashtable<Departamento, ConceptoRecibo[]>();

        try {
            for (int i = 0; i < deptos.size(); i++) {
                Departamento departamento = deptos.get(i);
                ContratoInquilino contratoInquilino = contratosService.devuelveContratoVigenteParaDepartamento(departamento);
                if (contratoInquilino == null) continue;
                if (lkpDiscrimLau.getValue()!=null) {
                    if (contratoInquilino.getTipoContrato() != lkpDiscrimLau.getValue()) {
                        continue;
                    }

                }

                if (dteFechaAplicacionOcupacionHasta.getValue().getTime() < contratoInquilino.getFechaOcupacion().getTime()) {
                    continue;
                }
                completaInfoVigencia();
                ConceptoRecibo[] arr_ccrr = incrementosService.creaConceptosReciboParaIncrementosGeneralesYObras(
                        lkpConcepto.getValue(),
                        contratoInquilino,
                        deptos,
                        lkpCoeficienteReparto.getValue(),
                        dteFechaAplicacion.getValue(),
                        txtImporteAumento.getValue(),
                        lkpDiscrimLau.getValue(),
                        chk12Perc.isChecked(),
                        chkAplicacionIPC.isChecked(),
                        lkpModoImputacion.getValue(),
                        lkpConceptoAtrasos.getValue(),
                        lkpNumMesesAtrasos.getValue(),
                        txaDescripcion.getValue(),
                        infoVigencia
                );
                if (arr_ccrr == null) {
                    //cualquier contrato que devuelve como coeficiente 0.0 devolvera null.

                    continue;
                }
                contadorRecibos += arr_ccrr.length;
                htDeptosCCRR.put(departamento, arr_ccrr);

                if (contadorRecibos==0){
                    notifications.create().withCaption("Sin datos").withDescription("No se han hallado incrementos aplicables. Revise su selección y los coeficientes asociados a la ubicación").show();
                    return;
                }

                //persistiendo datos
                ConceptoRecibo[] arr = htDeptosCCRR.get(departamento);
                for (int j = 0; j < arr.length; j++) {
                    ConceptoRecibo cr = arr[j];
                    entitiesToPersist.add(cr);
                    if (cr.getConceptosAdicionalesConceptoRecibo().size()>0){
                        for (int k = 0; k < cr.getConceptosAdicionalesConceptoRecibo().size(); k++) {
                            ConceptoAdicionalConceptoRecibo cacr = cr.getConceptosAdicionalesConceptoRecibo().get(k);
                            entitiesToPersist.add(cacr);

                        }
                    }
                    if (cr.getIncremento()!=null){
                        Incremento inc = cr.getIncremento();
                        entitiesToPersist.add(inc);

                    }
                }




            }
            dataManager.commit(new CommitContext(entitiesToPersist));
            notifications.create().withCaption("Incrementos generados y registrados exitósamente").show();
        } catch (Exception exception) {
            notifications.create().withCaption("Error").withDescription(exception.getMessage()).show();
            return;
        }
        if (contadorRecibos == 0){
            notifications.create().withCaption("Error").withDescription("No se han hallado incrementos aplicables. Revise su selección y los coeficientes asociados a la ubicación").show();
            return;
        }


    }

    public void onBtnPrevisualizarContratosAfectadosClick() {
        if (!datosPaginaListosParaEjecutarRealizacionOPrevisualizacion()){
            return;
        }

        int contadorRecibos = 0;
        List<Departamento> deptos = new ArrayList(tableDepartamentos.getSelected());
        Hashtable<Departamento, ConceptoRecibo[]> htDeptosCCRR = new Hashtable<Departamento, ConceptoRecibo[]>();

        try {
            for (int i = 0; i < deptos.size(); i++) {
                Departamento departamento = deptos.get(i);
                ContratoInquilino contratoInquilino = contratosService.devuelveContratoVigenteParaDepartamento(departamento);
                if (contratoInquilino == null) continue;
                if (lkpDiscrimLau.getValue()!=null) {
                    if (contratoInquilino.getTipoContrato() != lkpDiscrimLau.getValue()) {
                        continue;
                    }

                }

                if (dteFechaAplicacionOcupacionHasta.getValue().getTime() < contratoInquilino.getFechaOcupacion().getTime()) {
                    continue;
                }
                completaInfoVigencia();
                ConceptoRecibo[] arr_ccrr = incrementosService.creaConceptosReciboParaIncrementosGeneralesYObras(
                    lkpConcepto.getValue(),
                        contratoInquilino,
                        deptos,
                        lkpCoeficienteReparto.getValue(),
                        dteFechaAplicacion.getValue(),
                        txtImporteAumento.getValue(),
                        lkpDiscrimLau.getValue(),
                        chk12Perc.isChecked(),
                        chkAplicacionIPC.isChecked(),
                        lkpModoImputacion.getValue(),
                        lkpConceptoAtrasos.getValue(),
                        lkpNumMesesAtrasos.getValue(),
                        txaDescripcion.getValue(),
                        infoVigencia
                );
                if (arr_ccrr == null) {
                    //cualquier contrato que devuelve como coeficiente 0.0 devolvera null.

                    continue;
                }
                contadorRecibos += arr_ccrr.length;
                htDeptosCCRR.put(departamento, arr_ccrr);

                if (contadorRecibos==0){
                    notifications.create().withCaption("Sin datos").withDescription("No se han hallado incrementos aplicables. Revise su selección y los coeficientes asociados a la ubicación").show();
                    return;
                }




            }
            byte[] bb = jasperReportService.reportIncrementosGenerales(
                    (Ubicacion) treePropietariosUbicaciones.getSingleSelected().getUserObject(),
                    htDeptosCCRR,
                    lkpConcepto.getValue(),
                    lkpCoeficienteReparto.getValue(),
                    lkpNumMesesAtrasos.getValue(),
                    dteFechaAplicacion.getValue(),
                    txtImporteAumento.getValue());

            exportDisplay.show(new ByteArrayDataProvider(bb), "Incrementos.pdf", ExportFormat.getByExtension("pdf"));
        } catch (Exception exception) {
            notifications.create().withCaption("Error").withDescription(exception.getMessage()).show();
            return;
        }
        if (contadorRecibos == 0){
            notifications.create().withCaption("Error").withDescription("No se han hallado incrementos aplicables. Revise su selección y los coeficientes asociados a la ubicación").show();
            return;
        }

    }

    public void onBtnCerrarClick() {
        this.closeWithDefaultAction();
    }
}