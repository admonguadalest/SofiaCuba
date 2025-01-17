package com.company.test1.web.screens.notificaciones;

import com.company.test1.entity.ArchivoAdjunto;
import com.company.test1.entity.DatoDeContacto;
import com.company.test1.entity.Persona;
import com.company.test1.entity.PersonaFisica;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.enums.TipoDeDatoDeContactoEnum;
import com.company.test1.entity.notificaciones.NotificacionContratoInquilino;
import com.company.test1.entity.recibos.Recibo;
import com.company.test1.service.NotificacionService;
import com.company.test1.service.PdfService;
import com.company.test1.web.screens.DynamicReportHelper;
import com.haulmont.cuba.core.app.EmailService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.EmailAttachment;
import com.haulmont.cuba.core.global.EmailException;
import com.haulmont.cuba.core.global.EmailInfo;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.DialogAction;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.notificaciones.Notificacion;

import javax.inject.Inject;
import javax.mail.internet.InternetAddress;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@UiController("test1_Notificacion.browse")
@UiDescriptor("notificacion-browse.xml")
@LookupComponent("notificacionsTable")
@LoadDataBeforeShow
public class NotificacionBrowse extends StandardLookup<Notificacion> {

    @Inject
    private Table<NotificacionContratoInquilino> notificacionsTable;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private PdfService pdfService;
    @Inject
    private NotificacionService notificacionService;
    @Inject
    private Dialogs dialogs;
    @Inject
    private EmailService emailService;
    @Inject
    private Notifications notifications;
    @Inject
    private DataManager dataManager;

    public void onBtnVerReportClick() {
        byte[] bb = DynamicReportHelper.getReportDinamico("Notificaciones", NotificacionContratoInquilino.class, notificacionsTable);
        exportDisplay.show(new ByteArrayDataProvider(bb), "Notificaciones.pdf");
    }

    public void onBtnEnvioMailClick(){
        dialogs.createOptionDialog(Dialogs.MessageType.CONFIRMATION).withCaption("Confirmar Acción")
                .withMessage("¿Está seguro que desea enviar esta notificación a su destinatario?")
                .withActions(
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withHandler(e -> {
                            try{
                                doSendNotification();
                            }catch(Exception exc){
                                notifications.create().withCaption(exc.getMessage()).show();
                            }

                        }),
                        new DialogAction(DialogAction.Type.NO)
                )
                .show();
    }

    private void doSendNotification() throws Exception{
        List<Notificacion> nn = new ArrayList(notificacionsTable.getSelected());
        for (int j = 0; j < nn.size(); j++) {
            Notificacion n = nn.get(j);
            if (n==null){
                notifications.create().withCaption("Seleccionar una notificación a enviar.").show();
                return;
            }
            String emails = null;
            String ampliacionVivienda = "";
            if (n instanceof NotificacionContratoInquilino){
                NotificacionContratoInquilino nci = (NotificacionContratoInquilino)n;
                ampliacionVivienda = nci.getContratoInquilino().getDepartamento().getNombreDescriptivoCompleto();
                Departamento dTO = nci.getContratoInquilino().getDepartamento();
                if (dTO.getViviendaLocal()){
                    ampliacionVivienda = " VIVIENDA " + ampliacionVivienda;
                }else{
                    ampliacionVivienda = " ALQUILER " + ampliacionVivienda;
                }
                Persona inquilino = nci.getContratoInquilino().getInquilino();
                if (inquilino instanceof PersonaFisica){
                    inquilino = dataManager.reload(inquilino, "personaFisica-view");
                }else{
                    inquilino = dataManager.reload(inquilino, "personaJuridica-view");
                }

                List<DatoDeContacto> ddc = inquilino.getDatosDeContacto();

                for (int i = 0; i < ddc.size(); i++) {
                    DatoDeContacto d = ddc.get(i);
                    if (d.getTipoDeDato()== TipoDeDatoDeContactoEnum.CORREO_ELECTRONICO){

                        if (emails == null){
                            emails = d.getDato().replace(";","").toLowerCase().trim();
                        }else{
                            emails += "," + d.getDato().replace(";","").toLowerCase().trim();
                        }
                    }
                }
            }
            EmailInfo emailInfo = new EmailInfo("","","");

            emailInfo.setAddresses(emails);

            emailInfo.setCaption("NOTIFICACION IMPORTANTE SOBRE SU " + ampliacionVivienda);
            emailInfo.setBody("Ponemos a su disposición la notificación adjunta.\nAténtamente,\nLa Administración.");
            emailInfo.setBodyContentType(EmailInfo.HTML_CONTENT_TYPE);
            emailInfo.setFrom("NoReply Grupo Domus <noreply@domusvcs.com>");




            //email attachments
            List<EmailAttachment> attachments = new ArrayList<EmailAttachment>();
            List<ArchivoAdjunto> aas = null;
            EmailAttachment ea = new EmailAttachment(n.getVersionPdf(), "NOTIFICACION.pdf");

            emailInfo.setAttachments(new EmailAttachment[]{ea});
            try {

                emailService.sendEmail(emailInfo);

            } catch (Exception e) {
                e.printStackTrace();
                String ampliacion = emails;

                notifications.create().withCaption(e.getMessage())
                        .withDescription("La notificacion dirigida a '" + emails + "' falló. Las anteriores han sido exitosas y las posteriores no.").show();
            }
        }

        notifications.create().withCaption("Mailing programado exitósamente").show();




    }

    public void onBtnPdfClick() {


        byte[] bb = notificacionService.getVersionPdfConcatenada(new ArrayList(notificacionsTable.getSelected()));
        exportDisplay.show(new ByteArrayDataProvider(bb), "Notificaciones.pdf");
    }

    @Subscribe("removeBtn")
    public void onRemoveBtnClick(Button.ClickEvent event) {
        List<Notificacion> nn = new ArrayList(notificacionsTable.getSelected());
        if (nn.size()==0){
            notifications.create().withDescription("Seleccionar notificaciones a borrar").show();
            return;
        }
        boolean allTrue = true;
        try {

            for (int i = 0; i < nn.size(); i++) {
                Notificacion n = nn.get(i);
                if (!borrarNotificacion(n)) {
                    allTrue = false;
                }
            }
        }catch(Exception exc){
            allTrue = false;
        }
        if (!allTrue){
            notifications.create().withDescription("Una o mas notificaciones no pudieron ser borradas. Por favor revise la ultima accion.").show();
            return;
        }else{
            notifications.create().withDescription("Todas las notificaciones fueron eliminadas exitosamente").show();

        }


    }

    private boolean borrarNotificacion(Notificacion n){

            String hql = "select r from test1_Recibo r where r.notificacionPeriodicaImpagados.id = :nid";
            List<Recibo> rr = dataManager.load(Recibo.class).query(hql).parameter("nid",n.getId()).list();
            for (int i = 0; i < rr.size(); i++) {
                Recibo r = rr.get(i);
                r.setNotificacionPeriodicaImpagados(null);
                dataManager.commit(r);
            }
            dataManager.remove(n);
            return true;

    }


}