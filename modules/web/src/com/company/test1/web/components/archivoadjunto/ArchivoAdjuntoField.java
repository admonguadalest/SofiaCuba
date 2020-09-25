package com.company.test1.web.components.archivoadjunto;

import com.company.test1.entity.ArchivoAdjunto;
import com.company.test1.entity.ArchivoAdjuntoExt;
import com.company.test1.service.ColeccionArchivosAdjuntosService;
import com.haulmont.bali.events.EventHub;
import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.FileLoader;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.app.core.file.FileUploadDialog;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.data.BindingState;
import com.haulmont.cuba.gui.components.data.DataUnit;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.meta.ValueBinding;
import com.haulmont.cuba.gui.components.data.value.ContainerValueSource;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.impl.DataContextImpl;
import com.haulmont.cuba.gui.screen.OpenMode;

import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.ScreenContext;

import com.haulmont.cuba.gui.sys.ScreenContextImpl;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.gui.components.*;
import com.haulmont.cuba.web.sys.WebEvents;
import com.haulmont.cuba.web.sys.WebScreens;
import org.apache.poi.util.IOUtils;
import org.eclipse.persistence.internal.oxm.ContainerValue;
import org.springframework.context.ApplicationEvent;

import javax.inject.Inject;
import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Consumer;

@CompositeDescriptor("archivoadjunto-component.xml")
public class ArchivoAdjuntoField
        extends CompositeComponent<HBoxLayout>
        implements Field<ArchivoAdjunto>,
        CompositeWithCaption,
        CompositeWithHtmlCaption,
        CompositeWithHtmlDescription,
        CompositeWithIcon,
        CompositeWithContextHelp {

    public static final String NAME = "ArchivoAdjuntoField";


    DataContext dataContext;

    private Button btnAccionArchivo;
    private Button btnClear;


    private boolean required = false;
    private String requiredMessage = "";
    ArrayList<Validator<Consumer<? super ArchivoAdjunto>>> validators = new ArrayList<Validator<Consumer<? super ArchivoAdjunto>>>();
    private boolean editable = true;
    ArrayList<Consumer<ValueChangeEvent<ArchivoAdjunto>>> valueChangeListeners = new ArrayList<Consumer<ValueChangeEvent<ArchivoAdjunto>>>();
    private boolean valid = true;

    FileUploadingAPI fileUploadingAPI;
    DataManager dataManager;
    FileLoader fileLoader;

    Screens screens;
    ExportDisplay exportDisplay;



    private ArchivoAdjunto archivoAdjunto;


    public ArchivoAdjuntoField() {

        addCreateListener(this::onCreate);
    }

    private void onCreate(CreateEvent createEvent) {

        btnAccionArchivo = getInnerComponent("btnAccionArchivo");
        btnClear = getInnerComponent("btnClear");
        btnClear.addClickListener(e->{
            cvs.setValue(null);
            archivoAdjunto = null;
            btnAccionArchivo.setCaption("(Seleccionar)");
        });
        btnAccionArchivo.addClickListener(e->{onBtnArchivoAdjuntoClick();});
        int y = 2;

    }

    public void onShowUploadDialogButtonClick() {
//        StandardEditor s = (StandardEditor) this.getFrame().getFrameOwner();
        fileUploadingAPI = AppBeans.get(FileUploadingAPI.class);
        dataManager = AppBeans.get(DataManager.class);
        fileLoader = AppBeans.get(FileLoader.class);

//        screens = AppBeans.get((Screens.class));
        exportDisplay = AppBeans.get(ExportDisplay.class);

        AppUI appui = App.getInstance().getAppUIs().get(0);
        screens = appui.getScreens();
        if (this.archivoAdjunto==null){
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
                    ArchivoAdjunto aa = new ArchivoAdjunto();
                    aa.setDescripcion(fileDescriptor.getName());
                    aa.setExtension(fileDescriptor.getExtension());
                    aa.setMimeType("");
                    aa.setNombreArchivo(fileDescriptor.getName());
                    aa.setNombreArchivoOriginal(fileDescriptor.getName());
                    aa.setRepresentacionSerial(bb);
                    aa.setTamano(bb.length);
                    this.archivoAdjunto = aa;

                    this.setValue(this.archivoAdjunto);

                    cvs.setValue(aa);

                    btnAccionArchivo.setCaption(aa.getNombreArchivo());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            screens.show(dialog);
        }else{
            byte[] bytes;
            ArchivoAdjunto aa = this.archivoAdjunto;

            try {
                bytes = aa.getRepresentacionSerial();
                bytes = Base64.getMimeDecoder().decode(bytes);
                bytes = Base64.getMimeDecoder().decode(bytes);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            exportDisplay.show(new ByteArrayDataProvider(bytes), aa.getNombreArchivo(), ExportFormat.getByExtension(aa.getExtension()));
        }


    }

    public void onBtnArchivoAdjuntoClick() {
        onShowUploadDialogButtonClick();
    }



    private void updateValue(ArchivoAdjunto delta) {
        ArchivoAdjunto value = getValue();
        setValue(delta);
    }



    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public void setRequired(boolean required) {
        this.required = required;
        getComposition().setRequiredIndicatorVisible(required);
    }

    @Override
    public String getRequiredMessage() {
        return this.requiredMessage;
    }

    @Override
    public void setRequiredMessage(String msg) {
        this.requiredMessage = msg;
    }

    @Override
    public void addValidator(Consumer<? super ArchivoAdjunto> validator) {
//        validators.add(validator);
    }

    @Override
    public void removeValidator(Consumer<ArchivoAdjunto> validator) {
        validators.remove(validator);
    }

    @Override
    public Collection<Consumer<ArchivoAdjunto>> getValidators() {
//        return validators;
        return null;
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;

    }

    @Override
    public ArchivoAdjunto getValue() {
        return archivoAdjunto;
    }

    @Override
    public void setValue(ArchivoAdjunto value) {

        ValueSource.ValueChangeEvent<ArchivoAdjunto> vche = new ValueSource.ValueChangeEvent<>(cvs, null, value);


    }

    @Override
    public Subscription addValueChangeListener(Consumer<ValueChangeEvent<ArchivoAdjunto>> listener) {
        return null;
    }

    @Override
    public void removeValueChangeListener(Consumer<ValueChangeEvent<ArchivoAdjunto>> listener) {
        valueChangeListeners.remove(listener);
    }

    @Override
    public boolean isValid() {
        return this.valid;
    }

    @Override
    public void validate() throws ValidationException {
        validateMe();
    }

    private void validateMe(){

    }

    ContainerValueSource cvs = null;
    ValueBinding vb = null;

        @Override
    public void setValueSource(ValueSource<ArchivoAdjunto> valueSource) {
        cvs = (ContainerValueSource)valueSource;
        BeanLocator bl = AppBeans.get(BeanLocator.class);
        cvs.setBeanLocator(bl);
        getComposition().setRequiredIndicatorVisible(this.isRequired());

        cvs.addStateChangeListener(new Consumer<DataUnit.StateChangeEvent>() {
            @Override
            public void accept(DataUnit.StateChangeEvent stateChangeEvent) {

                ContainerValueSource s = (ContainerValueSource) stateChangeEvent.getSource();
                ArchivoAdjunto aa = (ArchivoAdjunto) s.getValue();
                if (aa==null) return;
                archivoAdjunto = aa;
                btnAccionArchivo.setCaption(archivoAdjunto.getNombreArchivo());

            }
        });
    }

    @Override
    public ValueSource<ArchivoAdjunto> getValueSource() {
//        return valueField.getValueSource();
        return cvs;
    }

}