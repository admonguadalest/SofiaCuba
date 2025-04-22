package com.company.test1.web.screens.recibos;

import com.company.test1.entity.DatoDeContacto;
import com.company.test1.entity.Persona;
import com.company.test1.entity.PersonaFisica;
import com.company.test1.entity.PersonaJuridica;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.enums.TipoDeDatoDeContactoEnum;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.recibos.ReciboReport;
import com.company.test1.service.JasperReportService;
import com.company.test1.service.PdfService;
import com.company.test1.web.screens.DynamicReportHelper;
import com.company.test1.web.screens.EnvioRecibosMail;
import com.haulmont.cuba.core.app.EmailService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.EmailAttachment;
import com.haulmont.cuba.core.global.EmailInfo;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.recibos.Recibo;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.reports.entity.Report;
import com.haulmont.reports.gui.ReportGuiManager;
import com.haulmont.yarg.reporting.ReportOutputDocument;

import javax.inject.Inject;
import java.util.*;

@UiController("test1_Recibo.browse")
@UiDescriptor("gestionar-cobros.xml")
@LookupComponent("reciboesTable")
@LoadDataBeforeShow
public class GestionarCobros extends StandardLookup<Recibo> {
    @Inject
    private Table<Recibo> reciboesTable;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private JasperReportService jasperReportService;
    @Inject
    private Notifications notifications;
    @Inject
    private PdfService pdfService;
    @Inject
    private EmailService emailService;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private DataManager dataManager;
    @Inject
    private UserSession userSession;
    @Inject
    private ReportGuiManager reportGuiManager;
    @Inject
    private Filter filter;

    public void onBtnVerReportClick() {
        byte[] bb = DynamicReportHelper.getReportDinamico("Recibos", Recibo.class, reciboesTable);
        exportDisplay.show(new ByteArrayDataProvider(bb), "Recibos.pdf");
    }

    public void onBtnPdfClick() {
        Recibo r = reciboesTable.getSingleSelected();
        if (r==null){
            notifications.create().withCaption("Seleccionar un Recibo");
            return;
        }
        try {
            byte[] bb = jasperReportService.getReportRecibo(r);
            exportDisplay.show(new ByteArrayDataProvider(bb), "Recibo " + r.getNumRecibo() + ".pdf");
        }catch(Exception exc){
            notifications.create().withCaption("Error al generar el report:" + exc.getMessage()).show();
            return;
        }

    }


    boolean abrirAccesoTotal = false;
    @Subscribe("reciboesTable.edit")
    public void onReciboesTableEdit(Action.ActionPerformedEvent event) {
        User us = userSession.getUser();
        Recibo r = reciboesTable.getSingleSelected();
        if (r == null) {
            notifications.create().withDescription("Seleccione un recibo").show();
            return;
        }
        Propietario p = r.getUtilitarioContratoInquilino().getDepartamento().getPropietarioEfectivo();
        if (us.getLogin().compareTo("carlosconti") != 0) {
            String nifsPermitidos = "B75537886 ";
            if (nifsPermitidos.indexOf(p.getPersona().getNifDni())!=-1) {
                screenBuilders.editor(Recibo.class, this).editEntity(r).withOpenMode(OpenMode.NEW_TAB).build().show();
            } else {
                notifications.create().withCaption("Acceso denegado").show();
                return;
            }
            return;
        }else{
            String nifsPermitidos = "B75537878 B75537860 37234554S";
            if (abrirAccesoTotal){
                nifsPermitidos = "B75537878 B75537860 B75537886 37234554S";
            }



            if (nifsPermitidos.indexOf(p.getPersona().getNifDni())!=-1) {
                screenBuilders.editor(Recibo.class, this).editEntity(r).withOpenMode(OpenMode.NEW_TAB).build().show();
            } else {
                notifications.create().withCaption("Acceso denegado").show();
                return;
            }
        }

    }

    public void onBtnExcel(){
        ArrayList<Recibo> rbos = new ArrayList(reciboesTable.getSelected());
        if (rbos.size()==0){
            notifications.create().withCaption("Seleccionar al menos un recibo").show();
        }
        ArrayList<ReciboReport> rrs = new ArrayList();
        for (int i = 0; i < rbos.size(); i++) {
            Recibo r = rbos.get(i);
            r = dataManager.reload(r, "recibo-report-view");
            ReciboReport rr = dataManager.create(ReciboReport.class);
            rr.setUbicacion(r.getUtilitarioContratoInquilino().getDepartamento().getUbicacion().getNombre());
            rr.setPiso(r.getUtilitarioContratoInquilino().getDepartamento().getPiso());
            rr.setPuerta(r.getUtilitarioContratoInquilino().getDepartamento().getPuerta());
            rr.setFechaEmision(r.getFechaEmision());
            rr.setNumRecibo(r.getNumRecibo());
            rr.setNombreInquilino(r.getUtilitarioContratoInquilino().getInquilino().getNombreCompleto());
            rr.setImporte(r.getTotalRecibo());
            rr.setCuotaIrpf(r.getTotalIRPF());
            rr.setCuotaIva(r.getTotalIVA());
            rr.setImporteFinal(r.getTotalReciboPostCCAA());
            rr.setAmpliacion(r.getAmpliacion());
            rr.setReciboId(r.getId().toString().replace("-",""));
            rr.setRemesa(r.getInfoRemesa());
            rr.setPropietario(r.getUtilitarioContratoInquilino().getDepartamento().getPropietarioEfectivo().getPersona().getNombreCompleto());
            rr.setFechaRegistro(r.getCreateTs());
            //rr.setPropietario("pruebas");
            rrs.add(rr);
        }

        Collections.sort(rrs, ReciboReport.getNaturalComparator());

        Report r = dataManager.load(Report.class).query("select r from report$Report r " +
                "where r.name = :rn").parameter("rn", "RecibosEmitidos")
                .one();
        HashMap pams = new HashMap();
        /*pams.put("entities", asignacionTareasProgramadasDc.getItems());
        pams.put("TITULO", "TAREAS PROGRAMADAS");
        pams.put("FECHA_REPORT", new Date());*/
        pams.put("recibos",rrs);
        pams.put("nombreCompleto", "[NOMBRE EMPRESA]");
        pams.put("titulo", "Recibos Emitidos Periodo");
        pams.put("fechaRealizacion", new Date());

        pams.put("periodoFechaDesde", new Date());
        pams.put("periodoFechaHasta", new Date());
        ReportOutputDocument rod = reportGuiManager.getReportResult(r, pams, null);
        exportDisplay.show(new ByteArrayDataProvider(rod.getContent()), "Emitidas.xlsx");
        int y = 2;
    }

    public void onBtnEmail(){
        //primero verifico que todas las selecciones corresponden al mismo contrato, sino veto la accion
        ArrayList<Recibo> rbos = new ArrayList(reciboesTable.getSelected());
        if (rbos.size()==0){
            notifications.create().withCaption("Seleccionar al menos un recibo").show();
        }
        ArrayList<ContratoInquilino> ccii = new ArrayList();
        for (int i = 0; i < rbos.size(); i++) {
            ContratoInquilino ci = rbos.get(i).getUtilitarioContratoInquilino();
            if (ccii.indexOf(ci)==-1){
                ccii.add(ci);
            }
        }
        if (ccii.size()>1){
            notifications.create().withCaption("Seleccionar recibos asociados a un único contrato").show();
            return;
        }
        //fin
        Screen s = screenBuilders.screen(this).withScreenId("test1_EnvioRecibosMail")
                .withOpenMode(OpenMode.DIALOG).build();
        s.addAfterCloseListener(e->{
           if (e.getCloseAction() == WINDOW_COMMIT_AND_CLOSE_ACTION){
               String mails = ((EnvioRecibosMail)s).getEmails();
               doSend(mails);
           }
        });
        s.show();
        //precargando valores de email
        String direcciones = "";
        ContratoInquilino ci = ccii.get(0);
        Persona p = ci.getInquilino();
        if (p instanceof PersonaFisica){
            p = dataManager.reload(p, "personaFisica-view");
        }else if(p instanceof PersonaJuridica){
            p = dataManager.reload(p, "personaJuridica-view");
        }

        List<DatoDeContacto> ddcc = p.getDatosDeContacto();
        for (int i = 0; i < ddcc.size(); i++) {
            DatoDeContacto ddc = ddcc.get(i);
            if (ddc.getTipoDeDato()== TipoDeDatoDeContactoEnum.CORREO_ELECTRONICO){
                if (direcciones.trim().length()==0){
                    direcciones += ddc.getDato();
                }else{
                    direcciones += ";" + ddc.getDato();
                }
            }
        }
        ((EnvioRecibosMail)s).preDefineMails(direcciones);

    }

    public void doSend(String emails){
        String emailsCommaSeparated = emails;
        ArrayList<EmailAttachment> attachments = new ArrayList();
        try {
            Set<Recibo> rr = reciboesTable.getSelected();
            ArrayList al = new ArrayList(rr);
            for (int i = 0; i < al.size(); i++) {
                Recibo r = (Recibo) al.get(i);
                byte[] bb = jasperReportService.getReportRecibo(r);
                EmailAttachment eatt = new EmailAttachment(bb, "_" + r.getNumRecibo() + ".pdf");
                attachments.add(eatt);
            }
            EmailInfo einfo = new EmailInfo(emailsCommaSeparated, "Envío Periódico de Recibos", "Adjuntos los recibos solicitados. \nLa Administración.\n");
            einfo.setAttachments(attachments.toArray(new EmailAttachment[0]));
            emailService.sendEmailAsync(einfo);
            notifications.create().withCaption("Correo enviado!").show();
        }catch(Exception exc){

        }
    }
}