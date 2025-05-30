package com.company.test1.web.screens.validaciones;

import com.company.test1.entity.ArchivoAdjunto;
import com.company.test1.entity.ArchivoAdjuntoExt;
import com.company.test1.entity.Persona;
import com.company.test1.entity.ciclos.Ciclo;
import com.company.test1.entity.ciclos.ImputacionDocumentoImputable;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.documentosImputables.DocumentoImputable;
import com.company.test1.entity.documentosImputables.DocumentoProveedor;
import com.company.test1.entity.documentosImputables.FacturaProveedor;
import com.company.test1.entity.documentosImputables.Presupuesto;
import com.company.test1.entity.enums.*;
import com.company.test1.entity.extroles.Proveedor;
import com.company.test1.entity.ordenespago.OrdenPago;
import com.company.test1.entity.ordenespago.OrdenPagoAbono;
import com.company.test1.entity.ordenespago.OrdenPagoFacturaProveedor;
import com.company.test1.entity.validaciones.ValidacionImputacionDocumentoImputable;
import com.company.test1.service.*;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.*;
import com.haulmont.cuba.gui.builders.ScreenBuilder;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.data.ValueSourceProvider;
import com.haulmont.cuba.gui.components.data.value.ContainerValueSource;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.impl.InstanceContainerImpl;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.security.global.UserSession;


import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.text.DecimalFormat;
import java.util.List;

@UiController("test1_GestionarValidaciones")
@UiDescriptor("gestionar-validaciones.xml")
public class GestionarValidaciones extends Screen {
    @Inject
    private CheckBox btnAmpliarEscaneosTipoFactura;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private DataContext dataContext;
    @Inject
    private Button btnBuscar;
    @Inject
    private DateField<Date> datFechaDesde;
    @Inject
    private DateField<Date> datFechaHasta;
    @Inject
    private LookupField<ValidacionEstado> lkpEstadoValidacion;
    @Inject
    private LookupField<TipoCiclo> lkpTipoCiclo;
    @Inject
    private LookupField<DepartamentoTipoEnum> lkpTipoDepartamento;
    @Inject
    private LookupField<DocumentoImputableTipoEnum> lkpTipoValidacion;
    @Inject
    private LookupField<DepartamentoEstadoEnum> lkpVaciosOcupados;
    @Inject
    private TextField<String> txtDireccion;
    @Inject
    private TextField<String> txtProveedor;

    @Inject
    private ValidacionesService validacionesService;
    @Inject
    private DataManager dataManager;
    @Inject
    private CollectionContainer<ValidacionImputacionDocumentoImputable> validacionesDc;
    @Inject
    private UiComponents uiComponents;
    @Inject
    private UiComponentsGenerator uiComponentsGenerator;
    @Inject
    private Table<ValidacionImputacionDocumentoImputable> tableValidaciones;
    @Inject
    private Metadata metadata;
    @Inject
    private Notifications notifications;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private OrdenPagoService ordenPagoService;
    @Inject
    private Dialogs dialogs;
    @Inject
    private Button btnCerrar;
    private HashMap<DocumentoImputable, List<Component>> documentosToListColumnsEstadoValidacion = new HashMap<DocumentoImputable, List<Component>>();
    @Inject
    private ColeccionArchivosAdjuntosService coleccionArchivosAdjuntosService;
    @Inject
    private JasperReportService jasperReportService;
    @Inject
    private Label<String> lblNumRows;
    @Inject
    private PdfService pdfService;
    @Inject
    private UserSession userSession;
    @Inject
    private TextField<String> txtNombreTitular;

    @Subscribe
    public void onAfterInit(AfterInitEvent event) {

    }



    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        addEnterKeyListeners();
    }



    private void addEnterKeyListeners(){
        txtProveedor.addEnterPressListener(e->{onBtnBuscarClick();});
        txtDireccion.addEnterPressListener(e->{onBtnBuscarClick();});
        datFechaDesde.addValueChangeListener(e->{onBtnBuscarClick();});
        datFechaHasta.addValueChangeListener(e->{onBtnBuscarClick();});
        lkpVaciosOcupados.addValueChangeListener(e->{onBtnBuscarClick();});
        lkpTipoDepartamento.addValueChangeListener(e->{onBtnBuscarClick();});
        lkpTipoCiclo.addValueChangeListener(e->{onBtnBuscarClick();});

    }


    public void onBtnCerrarClick() {
        this.closeWithDefaultAction();
    }

    public void onBtnBuscarClick() {

        try{
            String userlogin = userSession.getUser().getLogin();
            String allowed = "carlosconti pilarconti bellamateos";
            if (allowed.indexOf(userlogin)==-1){
                notifications.create().withDescription("No tiene privilegios para acceder a esta funcionalidad")
                        .show();
                return;
            }
            HashMap<String, String> userLoginNifPropietarios = new HashMap();
            userLoginNifPropietarios.put("pilarconti", "B75537886");
            userLoginNifPropietarios.put("bellamateos", "B75537886");
            userLoginNifPropietarios.put("carlosconti", "B75537878;B75537860;B75537886;B08377079;B65687766");
            //userLoginNifPropietarios.put("carlosconti", "B75537878");
            List<ValidacionImputacionDocumentoImputable> lvidis =
                    validacionesService.devuelveValidacionesAcordeADatos(
                            lkpTipoValidacion.getValue(),
                            lkpEstadoValidacion.getValue(),
                            datFechaDesde.getValue(),
                            datFechaHasta.getValue(),
                            txtDireccion.getValue(),
                            txtProveedor.getValue(),
                            lkpVaciosOcupados.getValue(),
                            lkpTipoDepartamento.getValue(),
                            lkpTipoCiclo.getValue(),
                            txtNombreTitular.getValue()
                    );
            ArrayList<ValidacionImputacionDocumentoImputable> recargadas = new ArrayList<ValidacionImputacionDocumentoImputable>();
            for (int i = 0; i < lvidis.size(); i++) {
                ValidacionImputacionDocumentoImputable validacionImputacionDocumentoImputable =  lvidis.get(i);
                validacionImputacionDocumentoImputable = dataManager.reload(validacionImputacionDocumentoImputable, "validacionImputacionDocumentoImputable-view");
                ImputacionDocumentoImputable idi = validacionImputacionDocumentoImputable.getImputacionDocumentoImputable();
                Departamento d = idi.getCiclo().getDepartamento();
                Persona p = d.getPropietarioEfectivo().getPersona();
                String nifProp = p.getNifDni();
                String nifsPermitidos = userLoginNifPropietarios.get(userlogin);
                if (nifsPermitidos.indexOf(nifProp)==-1){
                    continue;
                }
                DocumentoProveedor dp = (DocumentoProveedor) validacionImputacionDocumentoImputable.getImputacionDocumentoImputable().getDocumentoImputable();
                if (dp instanceof FacturaProveedor){
                    dp = dataManager.reload((FacturaProveedor) dp, "facturaProveedor-reasignaciones-imputaciones-view");
                    validacionImputacionDocumentoImputable.getImputacionDocumentoImputable().setDocumentoImputable(dp);
                }
                recargadas.add(validacionImputacionDocumentoImputable);
            }
            validacionesDc.getMutableItems().clear();
            validacionesDc.getMutableItems().addAll(recargadas);

            lblNumRows.setValue( "(" + Integer.valueOf(recargadas.size()) + " registros)");


        }catch(Exception exc){
            int y = 2;
        }


    }

    public Component getColumnNombreProveedor(ValidacionImputacionDocumentoImputable vidi){
        DocumentoImputable di = vidi.getImputacionDocumentoImputable().getDocumentoImputable();
        if (di instanceof FacturaProveedor){
            FacturaProveedor dp = (FacturaProveedor) di;
            dp = dataManager.reload(dp, "facturaProveedor-view");
            Label l = uiComponents.create(Label.NAME);
            l.setValue(dp.getProveedor().getPersona().getNombreCompleto());
            return l;
        }else if (di instanceof Presupuesto){
            Presupuesto dp = (Presupuesto) di;
            dp = dataManager.reload(dp, "presupuesto-view");
            Label l = uiComponents.create(Label.NAME);
            l.setValue(dp.getProveedor().getPersona().getNombreCompleto());
            return l;
        }else{
            Label l = uiComponents.create(Label.NAME);
            l.setValue("N/D");
            return l;
        }

    }

    public Component getColumnNombreTitular(ValidacionImputacionDocumentoImputable vidi){
        DocumentoImputable di = vidi.getImputacionDocumentoImputable().getDocumentoImputable();
        if (di instanceof FacturaProveedor){
            FacturaProveedor dp = (FacturaProveedor) di;
            dp = dataManager.reload(dp, "facturaProveedor-view");
            Label l = uiComponents.create(Label.NAME);
            l.setValue(dp.getTitular().getNombreCompleto());
            return l;
        }else if (di instanceof Presupuesto){
            Presupuesto dp = (Presupuesto) di;
            dp = dataManager.reload(dp, "presupuesto-view");
            Label l = uiComponents.create(Label.NAME);
            l.setValue(dp.getTitular().getNombreCompleto());
            return l;
        }else{
            Label l = uiComponents.create(Label.NAME);
            l.setValue("N/D");
            return l;
        }

    }

    public Component getColumnConsideraciones(ValidacionImputacionDocumentoImputable vidi){
        DocumentoImputable di = vidi.getImputacionDocumentoImputable().getDocumentoImputable();
        Label l = uiComponents.create(Label.NAME);
        if (di instanceof FacturaProveedor){
            FacturaProveedor dp = (FacturaProveedor) di;
            dp = dataManager.reload(dp, "facturaProveedor-view");

            l.setValue(dp.getConsideracionesDocumentoImputable());
            return l;
        }else{
            l.setValue("");
        }
        return l;
    }

    public Component getColumnEstadoValidacion(ValidacionImputacionDocumentoImputable vidi){
        HBoxLayout hbx = uiComponents.create(HBoxLayout.NAME);

        //guardando las diferentes columnas de las diferentes validaciones de imputacioens
        //para un documento imputable para que cuando sean todas validadas poder
        //modificar su contenido
        List<Component> l = this.documentosToListColumnsEstadoValidacion.get(vidi.getImputacionDocumentoImputable().getDocumentoImputable());
        if (l==null){
            l = new ArrayList<Component>();
            this.documentosToListColumnsEstadoValidacion.put(vidi.getImputacionDocumentoImputable().getDocumentoImputable(), l);
        }
        l.add(hbx);
        //final registro de columnas


        boolean imprimirLabel = true;
        ValidacionEstado ve = null;
        try{
            ve = validacionesService.devuelveEstadoValidacionDocumentoImputable(vidi.getImputacionDocumentoImputable().getDocumentoImputable());
            if (ve == ValidacionEstado.VALIDADO){
                Label linfo = uiComponents.create(Label.NAME);

                if (vidi.getImputacionDocumentoImputable().getDocumentoImputable() instanceof FacturaProveedor){
                    OrdenPagoFacturaProveedor opfp = ordenPagoService.devuelveOrdenPagoFacturaProveedor((FacturaProveedor) vidi.getImputacionDocumentoImputable().getDocumentoImputable());
                    OrdenPagoAbono opa = ordenPagoService.devuelveOrdenPagoAbono((FacturaProveedor) vidi.getImputacionDocumentoImputable().getDocumentoImputable());
                    if ((opfp!=null) && (opa==null)) {
                        if (opfp != null) {
                            linfo.setValue("Validado - Existe Orden de Pago");

                        } else {
                            //no hay orden de pago
                            imprimirLabel = false;
                        }
                    }else if ((opfp==null) && (opa!=null)) {
                        if (opa != null) {
                            linfo.setValue("Validado - Existe Orden de Abono");
                            imprimirLabel = true;
                        } else {
                            //no hay orden de abono
                            imprimirLabel = false;
                        }
                    }else{
                        imprimirLabel = false;
                    }
                }else{
                    linfo.setValue("Validado");
                }
                if (imprimirLabel){
                    hbx.add(linfo);
                    return hbx;
                }

            }

        }catch(Exception exc){
            Label lerror = uiComponents.create(Label.NAME);
            lerror.setValue("No se pudo comprobar el estado de Validacion del documento asociado");
            hbx.add(lerror);
            return hbx;
        }



        LookupField<ValidacionEstado> lkpValidacion = uiComponents.create(LookupField.NAME);
        hbx.add(lkpValidacion);
        lkpValidacion.setOptionsEnum(ValidacionEstado.class);
        lkpValidacion.setValue(vidi.getEstadoValidacion());


        lkpValidacion.addValueChangeListener(e->{

            lkpValidacion_ValueChanged(e, vidi);

        });

        return hbx;
    }


    public Component getColumnTipoDocumento(ValidacionImputacionDocumentoImputable vidi){
        Label label = uiComponents.create(Label.NAME);
        if (vidi.getImputacionDocumentoImputable().getDocumentoImputable() instanceof FacturaProveedor){
            label.setValue("Factura");
        }
        if (vidi.getImputacionDocumentoImputable().getDocumentoImputable() instanceof Presupuesto){
            label.setValue("Presupuesto");
        }
        return label;

    }




    public void onBtnVerRegistroDocumentoClick() {
        ValidacionImputacionDocumentoImputable vidi = tableValidaciones.getSingleSelected();
        if (vidi == null){
            notifications.create().withCaption("Seleccionar un registro de Validacion").show();
            return;
        }
        DocumentoProveedor dp = (DocumentoProveedor) vidi.getImputacionDocumentoImputable().getDocumentoImputable();
        ScreenLaunchUtil.launchEditEntityScreen(dp, null, null, screenBuilders, this, OpenMode.NEW_TAB, null, null);

    }

    public void onBtnVerEscaneoDocumentoClick() {
        ValidacionImputacionDocumentoImputable vidi = tableValidaciones.getSingleSelected();
        if (vidi == null){
            notifications.create().withCaption("Seleccionar un registro de Validacion").show();
            return;
        }
        try {
            DocumentoImputable di = vidi.getImputacionDocumentoImputable().getDocumentoImputable();
            if (di instanceof FacturaProveedor){
                di = dataManager.reload(di, "facturaProveedor-view");
            }
            if (di instanceof Presupuesto){
                di = dataManager.reload(di, "presupuesto-view");
            }

            ArchivoAdjunto aa  = di.getColeccionArchivosAdjuntos().getArchivos().get(0);
            byte[] bytes = devuelveDescargableSegunTipoDocumentoYOpcion(di, btnAmpliarEscaneosTipoFactura.getValue());
            exportDisplay.show(new ByteArrayDataProvider(bytes), aa.getNombreArchivo(), ExportFormat.getByExtension(aa.getExtension()));
        }catch(Exception exc){
            notifications.create().withCaption("Error").withDescription("No se pudo descargar el archivo").show();
        }
    }

    private byte[] devuelveDescargableSegunTipoDocumentoYOpcion(DocumentoImputable di, Boolean ampliarEscaneosParaFacturas) throws Exception{
        if (ampliarEscaneosParaFacturas==null){
            ampliarEscaneosParaFacturas = false;
        }
        ArchivoAdjunto aa =di.getColeccionArchivosAdjuntos().getArchivos().get(0);

        ArchivoAdjuntoExt aaext = coleccionArchivosAdjuntosService.getArchivoAdjuntoExt(aa);
        byte[] bytes = aaext.getRepresentacionSerial();
        bytes = Base64.getMimeDecoder().decode(bytes);
        bytes = Base64.getMimeDecoder().decode(bytes);

        if ((ampliarEscaneosParaFacturas)&&(di instanceof FacturaProveedor)){
            FacturaProveedor fprov = (FacturaProveedor) di;
            List<byte[]> imagesFra = new ArrayList();
            if (aa.getExtension().toLowerCase().compareTo("pdf")==0){
                imagesFra = pdfService.pdfToImageList(bytes);
            }else{
                imagesFra.add(bytes);
            }
            if (fprov.getPresupuesto()!=null){
                Presupuesto p = fprov.getPresupuesto();
                p = dataManager.reload(p, "presupuesto-view");
                ArchivoAdjunto aap = p.getColeccionArchivosAdjuntos().getArchivos().get(0);
                ArchivoAdjuntoExt aaextp = coleccionArchivosAdjuntosService.getArchivoAdjuntoExt(aap);
                byte[] bytesp = aaextp.getRepresentacionSerial();
                bytesp = Base64.getMimeDecoder().decode(bytesp);
                bytesp = Base64.getMimeDecoder().decode(bytesp);
                List<byte[]> imagesPpto = new ArrayList();
                if (aap.getExtension().toLowerCase().compareTo("pdf")==0){
                    imagesPpto = pdfService.pdfToImageList(bytesp);
                }else{
                    imagesPpto.add(bytesp);
                }
                //overlay de importeDefinitivo y observaciones
                if ((p.getImporteDefinitivo()!=null) && (p.getConsideracionesPresupuesto()!=null)){
                    String importeDefinitivo = new DecimalFormat("#,##0.00").format(p.getImporteDefinitivo());
                    for (int i = 0; i < imagesPpto.size(); i++) {
                        Image bim = ImageIO.read(new ByteArrayInputStream(imagesPpto.get(i)));
                        int h = bim.getHeight(null);
                        int w = bim.getWidth(null);
                        Graphics g = bim.getGraphics();
                        g.setFont(new Font("TimesRoman", Font.PLAIN, 24));

                        g.setColor(Color.red);
                        g.drawString(importeDefinitivo, 20, h - 140);
                        g.drawString(p.getConsideracionesPresupuesto(), 20, h - 100);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write((RenderedImage)bim, "jpg",baos);
                        imagesPpto.set(i, baos.toByteArray());

                    }
                }

                imagesFra.addAll(imagesPpto);
            }
            bytes = pdfService.imagesToPdf(imagesFra);
            return bytes;
        }else{
            return bytes;
        }
    }

    public void onBtnConsultarCicloClick() {
        ValidacionImputacionDocumentoImputable vidi = tableValidaciones.getSingleSelected();
        if (vidi == null){
            notifications.create().withCaption("Seleccionar un registro de Validacion").show();
            return;
        }
        ScreenLaunchUtil.launchEditEntityScreen(vidi.getImputacionDocumentoImputable().getCiclo(), null, null, screenBuilders, this, OpenMode.NEW_TAB,
                    null, null);

    }


    private void    lkpValidacion_ValueChanged(HasValue.ValueChangeEvent<ValidacionEstado> e, ValidacionImputacionDocumentoImputable vidi){
        vidi.setEstadoValidacion(e.getValue());
        vidi.setFechaAprobacionRechazo(new Date());
        vidi = dataManager.commit(vidi);
        vidi = dataManager.reload(vidi, "validacionImputacionDocumentoImputable-view");

        //al reemplazar la instancia en el dataContainer puedo cambiar el estado de la validacion a través de varias transacciones
        //SOLUCION al error que me obligaba a recargar la tabla para cambiar mas de una vez el estado de una validacion
        int iofvidi = validacionesDc.getMutableItems().indexOf(vidi);
        if (iofvidi!=-1){
            validacionesDc.getMutableItems().set(iofvidi, vidi);
        }

        if (e.getValue() == ValidacionEstado.VALIDADO){

            DocumentoImputable di = vidi.getImputacionDocumentoImputable().getDocumentoImputable();
            if (di instanceof FacturaProveedor){
                FacturaProveedor fp = (FacturaProveedor) vidi.getImputacionDocumentoImputable().getDocumentoImputable();
                fp = dataManager.reload(fp, "facturaProveedor-view");
                //comprobar si existen mas imputaciones pendientes de validacion para el FacturaProveedor
                //SI NO EXISTEN MAS entonces lanzamos el proceso de creacion de orden de pago
                ArrayList<ImputacionDocumentoImputable> al = new ArrayList(fp.getImputacionesDocumentoImputable());
                al.remove(vidi.getImputacionDocumentoImputable());
                boolean existenImputacionesPendientesDeValidacion = false;
                for (int i = 0; i < al.size(); i++) {
                    if (al.get(i).getValidacionImputacion().getEstadoValidacion()!=ValidacionEstado.VALIDADO){
                        existenImputacionesPendientesDeValidacion = true;
                        break;
                    }
                }

                if (existenImputacionesPendientesDeValidacion){

                    return;
                }

                Proveedor prov = fp.getProveedor();
                List<OrdenPago> oopppc = ordenPagoService.devuelveOrdenesPagoPendientesDeCompensacion(prov, false);


                final FacturaProveedor fp2 = fp;
                final ValidacionImputacionDocumentoImputable vidi2 = vidi;
                ScreenBuilder sb = screenBuilders.screen(GestionarValidaciones.this);

                //primero verificamos si es factura con importes negativos, pues si es un abono, el que
                //existan compensaciones pendientes o no es irrelevante
                if (fp.getImportePostCCAA()<0.0) {
                    //orden de pago abono
                    String importe = new DecimalFormat("#,##0.00").format(fp.getImportePostCCAA());
                    dialogs.createOptionDialog().withCaption("Confirmar la accion").withMessage("Desea crear una Orden Pago Abono por importe de " + importe + "?")
                            .withActions(
                                    new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withHandler(ev -> {
                                        OrdenPagoAbono opa = new OrdenPagoAbono();
                                        opa.setFacturaProveedor(fp2);
                                        opa.setProveedor(fp2.getProveedor());
                                        opa.setEmisor(fp2.getTitular());
                                        opa.setBeneficiario(fp2.getProveedor().getPersona());
                                        opa.setDescripcion(fp2.getDescripcionDocumento());
                                        opa.setFechaValor(new Date());
                                        opa.setImporte(-fp2.getImportePostCCAA());//
                                        opa.setImporteEfectivo(0.0);
                                        dataManager.commit(opa);
                                        tableValidaciones.refresh();


                                    }),
                                    new DialogAction(DialogAction.Type.NO)
                            ).show();
                    return;
                }

                if (oopppc.size()>0){
                    Screen s = sb.withScreenId("test1_GeneracionOrdenPagoFacturaProveedor").withOpenMode(OpenMode.DIALOG).build();

                    GeneracionOrdenPagoFacturaProveedor gopfp = (GeneracionOrdenPagoFacturaProveedor)s;
                    gopfp.setFacturaProveedor(fp);
                    s.addAfterCloseListener(ace->{

                        OrdenPagoFacturaProveedor opfp = gopfp.getOrdenPagoFacturaProveedor();
                        if (opfp!=null) {
                            opfp.setEmisor(opfp.getFacturaProveedor().getTitular());
                            opfp.setBeneficiario(opfp.getFacturaProveedor().getProveedor().getPersona());
                            ordenPagoService.guardaOrdenPagoFacturaProveedor(opfp);

                            tableValidaciones.refresh();

//                            vidi.setEstadoValidacion(ValidacionEstado.VALIDADO);
//                            vidi.setFechaAprobacionRechazo(new Date());
//
//                            dataManager.commit(vidi);

                        }
                    });
                    s.show();
                }else if(oopppc.size()==0){
                    if (fp.getImportePostCCAA()>=0) {
                        String importe = new DecimalFormat("#,##0.00").format(fp.getImportePostCCAA());
                        dialogs.createOptionDialog().withCaption("Confirmar la accion").withMessage("Desea crear una Orden Pago FP por importe de " + importe + "?")
                                .withActions(
                                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withHandler(ev -> {
                                            OrdenPagoFacturaProveedor opfp = new OrdenPagoFacturaProveedor();
                                            opfp.setFacturaProveedor(fp2);
                                            opfp.setFechaValor(new Date());
                                            opfp.setImporte(fp2.getImportePostCCAA());
                                            opfp.setImporteEfectivo(fp2.getImportePostCCAA());
                                            opfp.setDescripcion("");
                                            opfp.setEmisor(fp2.getTitular());
                                            opfp.setBeneficiario(fp2.getProveedor().getPersona());
                                            dataManager.commit(opfp);

                                            tableValidaciones.refresh();
//                                        vidi.setEstadoValidacion(ValidacionEstado.VALIDADO);
//                                        vidi.setFechaAprobacionRechazo(new Date());
//                                        dataManager.commit(vidi);


                                        }),
                                        new DialogAction(DialogAction.Type.NO)
                                ).show();
                    }
                }



            }

        }

        tableValidaciones.refresh();
    }

    public void onBtnHacerReportClick(){
        List<ValidacionImputacionDocumentoImputable> validaciones = validacionesDc.getItems();
        try{
            byte[] bb = jasperReportService.generaReportValidacionesIdis(validaciones, lkpTipoValidacion.getValue(), datFechaDesde.getValue(), datFechaHasta.getValue());
            exportDisplay.show(new ByteArrayDataProvider(bb), "report_validaciones.pdf");
        }catch(Exception exc){

        }
    }

    public void buscaValidacionesPendientesParaDatos(DocumentoImputableTipoEnum dite, Date fechaDesde, Proveedor p){
        lkpTipoValidacion.setValue(dite);
        datFechaDesde.setValue(fechaDesde);
        lkpEstadoValidacion.setValue(ValidacionEstado.PENDIENTE);
        txtProveedor.setValue(p.getNombreComercial());
        onBtnBuscarClick();
    }






}