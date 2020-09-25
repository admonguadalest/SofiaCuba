package com.company.test1.web.components.archivoadjunto;

import com.company.test1.entity.ArchivoAdjunto;
import com.company.test1.entity.ArchivoAdjuntoExt;
import com.company.test1.service.ColeccionArchivosAdjuntosService;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.FileLoader;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.app.core.file.FileUploadDialog;

import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.components.SecuredActionsHolder;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.OpenMode;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.StandardEditor;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.gui.components.WebPickerField;
import com.haulmont.cuba.web.gui.components.WebV8AbstractField;
import com.haulmont.cuba.web.sys.WebScreens;
import com.haulmont.cuba.web.widgets.CubaPickerField;
import com.haulmont.cuba.web.widgets.CubaTextField;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import com.vaadin.ui.Button;

import java.io.File;
import java.io.InputStream;
import java.util.Base64;
import java.util.UUID;

public class WebArchivoAdjuntoField extends WebV8AbstractField<CubaArchivoAdjuntoField<ArchivoAdjunto>, ArchivoAdjunto, ArchivoAdjunto> implements InitializingBean {

    public static final String NAME = "WebArchivoAdjuntoField";

    FileUploadingAPI fileUploadingAPI;
    DataManager dataManager;
    FileLoader fileLoader;

    ArchivoAdjunto archivoAdjunto = null;

    public WebArchivoAdjuntoField(){

        component = createComponent();
        Button b = new Button("Upload", e->{onShowUploadDialogButtonClick();});
        b.setCaption("Upload");
        b.setIcon(VaadinIcons.UPLOAD);

        component.addButton(b,0);
        Button b2 = new Button("Ver", VaadinIcons.PLAY);
        b2.addClickListener(e->{view();});
        component.addButton(b2,1);

        attachValueChangeListener(this.component);
    }

    protected CubaArchivoAdjuntoField<ArchivoAdjunto> createComponent() {
        return new CubaArchivoAdjuntoField<>();
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public void onShowUploadDialogButtonClick() {
//        StandardEditor s = (StandardEditor) this.getFrame().getFrameOwner();
        fileUploadingAPI = AppBeans.get(FileUploadingAPI.class);
        dataManager = AppBeans.get(DataManager.class);
        fileLoader = AppBeans.get(FileLoader.class);
        Screens screens;
        ExportDisplay exportDisplay;



//        screens = AppBeans.get((Screens.class));
        exportDisplay = AppBeans.get(ExportDisplay.class);

        AppUI appui = App.getInstance().getAppUIs().get(0);
        screens = appui.getScreens();
        DataContext dc = UiControllerUtils.getScreenData(getFrame().getFrameOwner()).getDataContext();

//        if (this.archivoAdjunto==null){
            FileUploadDialog dialog = (FileUploadDialog) screens.create("fileUploadDialog", OpenMode.DIALOG);

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
                    bb = Base64.getMimeEncoder().encode(bb);
                    bb = Base64.getMimeEncoder().encode(bb);
                    ArchivoAdjunto aa = dc.create(ArchivoAdjunto.class);
                    aa.setDescripcion(fileDescriptor.getName());
                    aa.setExtension(fileDescriptor.getExtension());
                    aa.setMimeType("");
                    aa.setNombreArchivo(fileDescriptor.getName());
                    aa.setNombreArchivoOriginal(fileDescriptor.getName());
                    aa.setRepresentacionSerial(bb);
                    aa.setTamano(bb.length);
                    this.archivoAdjunto = aa;

                    this.setValue(this.archivoAdjunto);

//                    cvs.setValue(aa);


//                    btnAccionArchivo.setCaption(aa.getNombreArchivo());

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            screens.show(dialog);
//        }else{
//            byte[] bytes;
//            ArchivoAdjunto aa = this.archivoAdjunto;
//
//            try {
//                bytes = aa.getRepresentacionSerial();
//                bytes = Base64.getMimeDecoder().decode(bytes);
//                bytes = Base64.getMimeDecoder().decode(bytes);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            exportDisplay.show(new ByteArrayDataProvider(bytes), aa.getNombreArchivo(), ExportFormat.getByExtension(aa.getExtension()));
//        }


    }

    private void view(){
        byte[] bytes;
        ArchivoAdjunto aa = this.getValue();
        if (aa==null){
            AppBeans.get(Notifications.class).create().withCaption("Valor es nulo").show();
            return;
        }
        try {
            if (aa.getExtId()!=null) {
                Integer extId = aa.getExtId();
                ArchivoAdjuntoExt aaext = AppBeans.get(ColeccionArchivosAdjuntosService.class).getArchivoAdjuntoExt(aa);
                bytes = aaext.getRepresentacionSerial();

            }else{
                bytes = aa.getRepresentacionSerial();
            }
            bytes = Base64.getMimeDecoder().decode(bytes);
            bytes = Base64.getMimeDecoder().decode(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        AppBeans.get(ExportDisplay.class).show(new ByteArrayDataProvider(bytes), aa.getNombreArchivo(), ExportFormat.getByExtension(aa.getExtension()));
    }
}
