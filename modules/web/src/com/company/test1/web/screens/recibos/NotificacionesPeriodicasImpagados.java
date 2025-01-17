package com.company.test1.web.screens.recibos;

import com.company.test1.entity.Persona;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.notificaciones.Notificacion;
import com.company.test1.entity.recibos.Recibo;
import com.company.test1.entity.recibos.Remesa;
import com.company.test1.entity.recibos.Serie;
import com.company.test1.service.ContratosService;
import com.company.test1.service.DepartamentoService;
import com.company.test1.service.RecibosService;
import com.company.test1.web.screens.notificaciones.Recibospendientesnotificacionperiodica;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.screen.*;
import freemarker.template.SimpleDate;


import javax.inject.Inject;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@UiController("test1_NotificacionesPeriodicasImpagados")
@UiDescriptor("notificaciones-periodicas-impagados.xml")
public class NotificacionesPeriodicasImpagados extends Screen {
    @Inject
    private DataManager dataManager;
    @Inject
    private DepartamentoService departamentoService;
    @Inject
    private ContratosService contratosService;
    @Inject
    private Notifications notifications;
    @Inject
    private RecibosService recibosService;



    List<Recibo> recibos = null;
    List<Persona> inquilinos = null;
    HashMap<ContratoInquilino, List<Recibo>> hmContratosRecibos = new HashMap();
    @Inject
    private UiComponents uiComponents;
    @Inject
    private VBoxLayout vbmain;
    @Inject
    private ScreenBuilders screenBuilders;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        try {
            Propietario prop = dataManager.load(Persona.class).query("select p from test1_Persona p where p.nifDni = :nif")
                    .view("persona-view").parameter("nif", "B75537878").one().getPropietario();
            List<Propietario> props = Arrays.asList(new Propietario[]{prop});
            List<Departamento> dd = departamentoService.devuelveDepartamentosAsociadosAPropietarios(props);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date fechaDesde = sdf.parse("01/01/2000");
            Date fechaHasta = new Date();
            Serie s = dataManager.load(Serie.class).query("select s from test1_Serie s where s.nombreSerie = :s").parameter("s","ALQUILERES")
                    .one();

            recibos = recibosService.devuelveImpagadosAsociados(props, dd, fechaDesde, fechaHasta, "VIGENTE", "ORDINARIO", s);

            populateVbMain();
        }catch(Exception exc){
            notifications.create().withDescription("ERROR:" + exc.getMessage()).show();
        }
    }


    void populateVbMain(){

        try {
            HashMap<ContratoInquilino, Object[]> hm = new HashMap();
            for (int i = 0; i < recibos.size(); i++) {
                Recibo r = recibos.get(i);
                r = dataManager.reload(r, "recibo-view");
                ContratoInquilino ci = r.getUtilitarioContratoInquilino();
                if (!hm.containsKey(ci)) {
                    hm.put(ci, new Object[]{new ArrayList(), Integer.valueOf(0), Double.valueOf(0.0)});
                }
                Object[] oo = hm.get(ci);
                if (oo[0]==null) oo[0] = ci.getDepartamento().getNombreDescriptivoCompleto();

                Integer in = (Integer) oo[1];
                oo[1] = (Integer) (in + 1);
                Double ip = (Double) oo[2];
                ip += r.getTotalPendiente();
                oo[2] = ip;
                if (!hmContratosRecibos.containsKey(ci)){
                    hmContratosRecibos.put(ci, new ArrayList<Recibo>());
                }
                hmContratosRecibos.get(ci).add(r);
            }
            Iterator it = hm.keySet().iterator();
            Accordion acc = uiComponents.create(Accordion.NAME);
            acc.setCaption("GRUPO DOMUS VCS");
            while (it.hasNext()) {
                ContratoInquilino ci = (ContratoInquilino) it.next();

                Integer numRbos = (Integer) hm.get(ci)[1];
                Double totalPte = (Double) hm.get(ci)[2];
                DecimalFormat df = new DecimalFormat("#,##0.00");
                Persona p = ci.getInquilino();
                String abrevDepto = ci.getDepartamento().getUbicacion().getAbreviacionUbicacion() + ci.getDepartamento().getAbreviacionPisoPuerta();
                String accC = p.getNombreCompleto() + " " + abrevDepto + " (" + numRbos.toString() + " Rbos. Pendientes) " + df.format(totalPte);
                //acc.addLazyTab(accC, null, null );
                Accordion.Tab t = acc.addTab(accC, getUiRecibosImpagados(ci));
                t.setCaption(accC);

            }
            vbmain.add(acc);
        }catch(Exception exc){
            notifications.create().withDescription("ERROR:" + exc.getMessage()).show();
        }
    }

    public Component getUiRecibosImpagados(ContratoInquilino ci) throws Exception{

        VBoxLayout vb = uiComponents.create(VBoxLayout.NAME);
        vb.setSpacing(true);
        TextArea ta = uiComponents.create(TextArea.NAME);
        ta.setCaption("Observaciones Gestiones de Cobro");
        ta.setWidthFull();
        ta.setValue(ci.getObservacionesNotificacionesPeriodicasGestionCobro());
        ta.addValueChangeListener(e->{
            String s = (String)ta.getValue();
            ContratoInquilino ci_ = dataManager.load(ContratoInquilino.class).id(ci.getId()).view("contratoInquilino-view").one();
            ci_.setObservacionesNotificacionesPeriodicasGestionCobro(s);
            dataManager.commit(ci_);
            notifications.create().withDescription("Cambios en observaciones de Gestiones de Cobro de Contrato guardadas").show();
        });

        VBoxLayout svb = uiComponents.create(VBoxLayout.NAME);
        svb.setSpacing(true);
        vb.add(ta, svb);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DecimalFormat df = new DecimalFormat("#,##0.00");
        List<Recibo> rr = hmContratosRecibos.get(ci);
        GridLayout glh = uiComponents.create(GridLayout.NAME);
        glh.setColumns(9);
        glh.setWidth("100%");
        glh.setRows(1);

        float[] expandRatios = new float[]{0.02f,0.08f,0.20f,0.1f,0.1f,0.1f,0.1f,0.1f,0.2f};
        String[] headers = new String[]{"#","Depto.","Remesa","F.Emisión", "Total Rbo. ", "Total Pte. ", "Acc. Notif.", "Enviada", "Observaciones"};
        for (int j = 0; j < expandRatios.length; j++) {
            Label l = uiComponents.create(Label.NAME);

            l.setValue(headers[j]);
            glh.add(l, j, 0);
            glh.setColumnExpandRatio(j, expandRatios[j]);
        }
        svb.add(glh);
        for (int i = 0; i < rr.size(); i++) {

            GridLayout gl = uiComponents.create(GridLayout.NAME);
            gl.setColumns(9);
            gl.setRows(1);
            gl.setSpacing(true);
            gl.setWidth("100%");
            Recibo r = rr.get(i);
            Label lCounter = uiComponents.create(Label.NAME);
            lCounter.setValue(Integer.valueOf(i).toString());
            Label lDepto = uiComponents.create(Label.NAME);
            String nd = r.getUtilitarioContratoInquilino().getDepartamento().getNombreDescriptivoCompleto();
            lDepto.setValue(nd);
            Label lRemesa = uiComponents.create(Label.NAME);
            String rm = r.getOrdenanteRemesa().getRemesa().getIdentificadorRemesa();
            lRemesa.setValue(rm);
            Label lFecha = uiComponents.create(Label.NAME);
            lFecha.setValue(sdf.format(r.getFechaEmision()));
            Label lTotalRbo = uiComponents.create(Label.NAME);
            lTotalRbo.setValue(df.format(r.getTotalReciboPostCCAA()));
            Label lPte = uiComponents.create(Label.NAME);
            lPte.setValue(df.format(r.getTotalPendiente()));
            Button btnNotif = uiComponents.create(Button.NAME);
            btnNotif.setCaption("Notificar");
            btnNotif.addClickListener(e->{
                realizaNotificacion(r);
            });
            TextField tf = uiComponents.create(TextField.NAME);
            TextArea tf2 = uiComponents.create(TextArea.NAME);
            tf2.setWordWrap(true);
            tf2.setValue(r.getObservacionesNotificacionesPeriodicasGestionCobro());
            tf2.addValueChangeListener(e->{
                Recibo r_ = dataManager.load(Recibo.class).id(r.getId()).one();
                String s = tf2.getRawValue();
                r_.setObservacionesNotificacionesPeriodicasGestionCobro(s);
                dataManager.commit(r_);
                notifications.create().withDescription("Cambios en recibo guardados").show();
            });
            gl.add(lCounter, lDepto, lRemesa, lFecha, lTotalRbo, lPte, btnNotif, tf, tf2);
            for (int j = 0; j < expandRatios.length; j++) {
                gl.setColumnExpandRatio(j, expandRatios[j]);
            }
            Boolean b = r.getSilenciarAdvertenciasNotificacionesPeriodicasaGestionCobro();
            if (b==null) b = false;
            if ((r.getNotificacionPeriodicaImpagados()!=null)||(b)){
                btnNotif.setEnabled(false);
                if (r.getNotificacionPeriodicaImpagados()!=null){
                    tf.setValue("ENVIADA");
                }else{
                    tf.setValue("ENVIADA RBO POSTERIOR");
                }

            }else{
                tf.setValue("NO ENVIADA");
            }
            tf.setEnabled(false);

            svb.add(gl);
        }
        return vb;
    }

    private void realizaNotificacion(Recibo r){
        Recibospendientesnotificacionperiodica rpnp = (Recibospendientesnotificacionperiodica)
                screenBuilders.screen(this).withScreenId("test1_Recibospendientesnotificacionperiodica")
                .withOpenMode(OpenMode.DIALOG).build();
        rpnp.setContratoNotificacion(r.getUtilitarioContratoInquilino());
        rpnp.addAfterCloseListener(e->{
            Notificacion n = rpnp.getNotificacion();
            if (n!=null) {
                Recibo r_ = dataManager.load(Recibo.class).id(r.getId()).view("recibo-view").one();
                r_.setNotificacionPeriodicaImpagados(n);
                dataManager.commit(r_);
                //ACTULIZAANDO RECIBOS ANTERIORES DE EXISTIR
                List<Recibo> rr = hmContratosRecibos.get(r_.getUtilitarioContratoInquilino());

                for (int i = 0; i < rr.size() ; i++) {
                    Recibo r3 = rr.get(i);
                    if (r3.getId().compareTo(r_.getId())!=0){
                        r3.setSilenciarAdvertenciasNotificacionesPeriodicasaGestionCobro(true);
                        dataManager.commit(r3);
                    }

                }

                notifications.create().withDescription("Notificacion asignada en contrato y recibo").show();
            }

        });
        rpnp.show();
    }

}