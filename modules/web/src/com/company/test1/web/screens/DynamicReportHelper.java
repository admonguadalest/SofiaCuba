package com.company.test1.web.screens;

import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.service.JasperReportService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;

import java.util.ArrayList;
import java.util.List;

public class DynamicReportHelper {

    public static byte[] getReportDinamico(String title, Class itemsClass, Table table){

        List<String> ids = new ArrayList<String>();
        List<Table.Column> cols = table.getColumns();
        ArrayList<String> colnames = new ArrayList<String>();
        for (int i = 0; i < cols.size(); i++) {
            Table.Column dc =  cols.get(i);
            String id = dc.getIdString();
            ids.add(id);
            if (dc.getCaption()!=null){
                colnames.add(dc.getCaption());

            }else{
                colnames.add(id);
            }
        }

        List<Entity> entities = new ArrayList(table.getItems().getItems());
        try {
            byte[] bb = AppBeans.get(JasperReportService.class).getReportDinamico(title, itemsClass, entities,
                    ids, colnames);
            return bb;

        }catch(Exception exc){
            AppBeans.get(Notifications.class).create().withCaption("Error").withDescription(exc.getMessage()).show();
        }
        return null;
    }

}
