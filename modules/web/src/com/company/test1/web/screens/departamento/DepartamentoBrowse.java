package com.company.test1.web.screens.departamento;

import com.company.test1.service.JasperReportService;
import com.company.test1.web.screens.DynamicReportHelper;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Sort;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.Sorter;
import com.haulmont.cuba.gui.model.impl.CollectionContainerSorter;
import com.haulmont.cuba.gui.model.impl.EntityValuesComparator;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.departamentos.Departamento;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@UiController("test1_Departamento.browse")
@UiDescriptor("departamento-browse.xml")
@LookupComponent("departamentoesTable")
@LoadDataBeforeShow
public class DepartamentoBrowse extends StandardLookup<Departamento> {

    
    @Inject
    private JasperReportService jasperReportService;
    @Inject
    private GroupTable<Departamento> departamentoesTable;
    @Inject
    private CollectionContainer<Departamento> departamentoesDc;
    @Inject
    private Notifications notifications;
    @Inject
    private ExportDisplay exportDisplay;

    @Inject
    private CollectionLoader<Departamento> departamentoesDl;
    @Inject
    private DataManager dataManager;




    
    
    @Subscribe
    public void onInit(InitEvent event) {
        /*departamentoesDc.setSorter(new CollectionContainerSorter(departamentoesDc,departamentoesDl) {

            @Override
            protected Comparator<? extends Entity> createComparator(Sort sort, MetaClass metaClass) {
                MetaPropertyPath metaPropertyPath = Objects.requireNonNull(
                        metaClass.getPropertyPath(sort.getOrders().get(0).getProperty()));

                if (metaPropertyPath.getMetaClass().getJavaClass().equals(Departamento.class)
                        && "number".equals(metaPropertyPath.toPathString())) {
                    boolean isAsc = sort.getOrders().get(0).getDirection() == Sort.Direction.ASC;
                    return Comparator.comparing(
                            (Departamento e) -> e.getRm2id() == null ? null : Integer.valueOf(e.getRm2id()),
                            EntityValuesComparator.asc(isAsc));
                }
                return super.createComparator(sort, metaClass);
            }
        });*/
    }
    
    



    public void onBtnReportClick() {

        byte[] bb = DynamicReportHelper.getReportDinamico("Departamentos", Departamento.class, departamentoesTable);
        exportDisplay.show(new ByteArrayDataProvider(bb), "Departamentos.pdf", ExportFormat.getByExtension("pdf"));

    }



}