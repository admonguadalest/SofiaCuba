package com.company.test1.web.screens.coleccionarchivosadjuntos;

import com.company.test1.entity.ArchivoAdjunto;
import com.company.test1.entity.ArchivoAdjuntoExt;
import com.company.test1.entity.ColeccionArchivosAdjuntos;
import com.company.test1.service.ColeccionArchivosAdjuntosService;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.FileLoader;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.app.core.file.FileUploadDialog;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import org.apache.poi.util.IOUtils;

import javax.inject.Inject;
import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@UiController("test1_ColeccionArchivosAdjuntosFragment")
@UiDescriptor("coleccion-archivos-adjuntos-fragment.xml")
public class ColeccionArchivosAdjuntosFragment extends ScreenFragment {

    @Inject
    private Table<ArchivoAdjunto> tableArchivos;
    @Inject
    private Tree<ColeccionArchivosAdjuntos> treeColecciones;
    @Inject
    private CollectionContainer<ArchivoAdjunto> archivosAdjuntosDc;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private InstanceContainer<ColeccionArchivosAdjuntos> coleccionArchivosAdjuntosDc;
    @Inject
    private DataContext dataContext;
    @Inject
    private ColeccionArchivosAdjuntosService coleccionArchivosAdjuntosService;
    @Inject
    private CollectionContainer<ColeccionArchivosAdjuntos> coleccionesDc;
    @Inject
    private CollectionLoader<ColeccionArchivosAdjuntos> coleccionesDl;
    @Inject
    private Screens screens;
    @Inject
    private FileUploadingAPI fileUploadingAPI;
    @Inject
    private DataManager dataManager;
    @Inject
    private FileLoader fileLoader;
    @Inject
    private ExportDisplay exportDisplay;


    @Subscribe(target = Target.PARENT_CONTROLLER)
    private void onAfterShowHost(Screen.AfterShowEvent event) {

        coleccionesDl.load();
    }

    @Install(to = "coleccionesDl", target = Target.DATA_LOADER)
    private List<ColeccionArchivosAdjuntos> coleccionesDlLoadDelegate(LoadContext<ColeccionArchivosAdjuntos> loadContext) {
        if (coleccionArchivosAdjuntosDc.getItemOrNull()==null){
            return new ArrayList();
        }

        ColeccionArchivosAdjuntos caa = coleccionArchivosAdjuntosDc.getItem();
        List<ColeccionArchivosAdjuntos> l =coleccionArchivosAdjuntosService.devuelveListColeccionesIntegrantes(caa);
        return l;
    }
    
    

    @Subscribe("treeColecciones")
    private void onTreeColeccionesSelection(Tree.SelectionEvent<ColeccionArchivosAdjuntos> event) {
        HashSet<ColeccionArchivosAdjuntos> s = (HashSet<ColeccionArchivosAdjuntos>)event.getSelected();
        if (s.size()==1){
            ColeccionArchivosAdjuntos caa = event.getSource().getSingleSelected();
            archivosAdjuntosDc.getMutableItems().clear();
            archivosAdjuntosDc.getMutableItems().addAll(caa.getArchivos());
        }
    }

    public void onRenombrarColeccionButtonClick() {

        ColeccionArchivosAdjuntos caa = coleccionArchivosAdjuntosDc.getItem();
        ScreenLaunchUtil.launchEditEntityScreen(caa, null, null, screenBuilders, this, OpenMode.DIALOG,
                dataContext, null);


    }

    @Inject
    private Notifications notifications;

    public void onBtnCrearClick() {
        ColeccionArchivosAdjuntos caaParent = treeColecciones.getSingleSelected();
        if (caaParent==null){
            notifications.create().withCaption("Seleccionar Coleccion Padre").withDescription("Debe seleccionar una coleccion donde incluir la nueva coleccion creada")
                    .withType(Notifications.NotificationType.HUMANIZED).show();
            return;
        }
        ColeccionArchivosAdjuntosEdit s = (ColeccionArchivosAdjuntosEdit) screenBuilders.editor(ColeccionArchivosAdjuntos.class, this)
                .withLaunchMode(OpenMode.DIALOG)
                .newEntity()
                .withParentDataContext(dataContext)
                .build().show();
        s.addAfterCloseListener(event->{
            ColeccionArchivosAdjuntos d = s.getEditedEntity();
            d.setColeccionParent(caaParent);
            List<ColeccionArchivosAdjuntos> lcaa = caaParent.getColecciones();
            lcaa.add(d);
            coleccionesDl.load();
        });
    }


    public void onShowUploadDialogButtonClick() {
        ColeccionArchivosAdjuntos caaParent = treeColecciones.getSingleSelected();
        if (caaParent==null){
            notifications.create().withCaption("Seleccionar Coleccion Padre").withDescription("Debe seleccionar una coleccion donde incluir la nueva coleccion creada")
                    .withType(Notifications.NotificationType.HUMANIZED).show();
            return;
        }

        FileUploadDialog dialog = (FileUploadDialog) screens.create("fileUploadDialog", OpenMode.DIALOG);
        DataContext dc = UiControllerUtils.getScreenData(getHostController()).getDataContext();

        dialog.addCloseWithCommitListener(() -> {
            UUID fileId = dialog.getFileId();
            String fileName = dialog.getFileName();

            File file = fileUploadingAPI.getFile(fileId);
            FileDescriptor fileDescriptor = fileUploadingAPI.getFileDescriptor(fileId, fileName);
            try {
                fileUploadingAPI.putFileIntoStorage(fileId, fileDescriptor);
                dataManager.commit(fileDescriptor);
                InputStream is = fileLoader.openStream(fileDescriptor);
                byte[] bb = IOUtils.toByteArray(is);
                ArchivoAdjunto aa = dc.create(ArchivoAdjunto.class);
                aa.setColeccionArchivosAdjuntos(treeColecciones.getSingleSelected());
                aa.setDescripcion(fileDescriptor.getName());
                aa.setExtension(fileDescriptor.getExtension());
                aa.setMimeType("");
                aa.setNombreArchivo(fileDescriptor.getName());
                aa.setNombreArchivoOriginal(fileDescriptor.getName());
                bb = Base64.getMimeEncoder().encode(bb);
                bb = Base64.getMimeEncoder().encode(bb);
                aa.setRepresentacionSerial(bb);
                aa.setTamano(bb.length);
                aa.getColeccionArchivosAdjuntos().getArchivos().add(aa);
                archivosAdjuntosDc.getMutableItems().clear();
                archivosAdjuntosDc.getMutableItems().addAll(treeColecciones.getSingleSelected().getArchivos());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        screens.show(dialog);

    }

    public void OnBtnDescargarColeccion(){
        try {
            String nombre = coleccionArchivosAdjuntosDc.getItem().getNombre();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zipOut = new ZipOutputStream(baos);
            for (int i = 0; i < coleccionArchivosAdjuntosDc.getItem().getArchivos().size(); i++) {
                ArchivoAdjunto aa = coleccionArchivosAdjuntosDc.getItem().getArchivos().get(i);
                ArchivoAdjuntoExt aaext = coleccionArchivosAdjuntosService.getArchivoAdjuntoExt(aa);
                byte[] bb = null;
                if (aaext==null){
                    bb = aa.getRepresentacionSerial();
                    bb = Base64.getMimeDecoder().decode(bb);
//                    bb = Base64.getMimeDecoder().decode(bb);
                }else{
                    bb = aaext.getRepresentacionSerial();
                    bb = Base64.getMimeDecoder().decode(bb);
//                    bb = Base64.getMimeDecoder().decode(bb);
                }

                ByteArrayInputStream bais = new ByteArrayInputStream(bb);
                ZipEntry zipEntry = new ZipEntry(aa.getNombreArchivo());
                zipOut.putNextEntry(zipEntry);
                byte[] bytes = new byte[1024];
                int length;
                while((length = bais.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
                bais.close();
            }
            zipOut.close();
            baos.close();
            byte[] bb = baos.toByteArray();
            exportDisplay.show(new ByteArrayDataProvider(bb), nombre + ".zip");

        }catch(Exception exc){
            notifications.create().withCaption("Error").withDescription(exc.getMessage()).show();
        }
    }


    public void onBtnDescargarClick() {
        ArchivoAdjunto aa = tableArchivos.getSingleSelected();
        ArchivoAdjuntoExt aaext = coleccionArchivosAdjuntosService.getArchivoAdjuntoExt(aa);
        byte[] bytes;
        if (aa==null){
            notifications.create().withCaption("Seleccionar Archivo").withDescription("Seleccionar archivo a descargar")
                    .withType(Notifications.NotificationType.HUMANIZED).show();
            return;
        }
        if (aaext==null){
            if (aa!=null){
                bytes = aa.getRepresentacionSerial();
                if (bytes==null){
                    notifications.create().withCaption("Error de Datos").withDescription("El archivo seleccionado tiene representacion serial vacía")
                            .withType(Notifications.NotificationType.HUMANIZED).show();
                    return;
                }
            }else {
                notifications.create().withCaption("Error de Datos").withDescription("El archivo seleccionado no tiene asociado un archivo adjunto en el almacen de datos externo")
                        .withType(Notifications.NotificationType.HUMANIZED).show();
                return;
            }

        }else{
            bytes = aaext.getRepresentacionSerial();
        }

        try {

            bytes = Base64.getMimeDecoder().decode(bytes);
            bytes = Base64.getMimeDecoder().decode(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        exportDisplay.show(new ByteArrayDataProvider(bytes), aa.getNombreArchivo(), ExportFormat.getByExtension(aa.getExtension()));
    }
}