package com.company.test1.web;

import com.company.test1.entity.TreeItem;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.components.data.TreeItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class GuiUtils {

    public static void selectAll(Table table){
        Collection c = table.getItems().getItems();
        table.setSelected(c);

    }

    public static void invertSelection(Table table){
        ArrayList al = new ArrayList(table.getSelected());
        ArrayList all = new ArrayList(table.getItems().getItems());
        ArrayList newSelection = new ArrayList();
        for (int i = 0; i < all.size(); i++) {
            Object o =  all.get(i);
            if (al.indexOf(o)==-1){
                newSelection.add(o);
            }
        }
        table.setSelected(newSelection);

    }

    public static void selectAll(Tree<TreeItem> tree){
        TreeItems<TreeItem> c = tree.getItems();
        List<TreeItem> ttii = new ArrayList<TreeItem>();
        Stream<TreeItem> stream = c.getItems();
        Object[] oo =  stream.toArray();
        List l = Arrays.asList(oo);
        tree.setSelected(l);

    }

    public static void invertSelection(Tree tree){
        TreeItems<TreeItem> c = tree.getItems();
        List<TreeItem> ttii = new ArrayList<TreeItem>();
        Stream<TreeItem> stream = c.getItems();
        Object[] oo =  stream.toArray();

        List<TreeItem> selected = new ArrayList(tree.getSelected());

        List<TreeItem> newselection = new ArrayList<>();

        List l = Arrays.asList(oo);
        for (int i = 0; i < l.size(); i++) {
            TreeItem ti =  (TreeItem) l.get(i);
            if (selected.indexOf(ti)==-1){
                newselection.add(ti);
            }

        }
        tree.setSelected(newselection);



    }

    public static boolean isAnyValueNull(Component... components){
        for (int i = 0; i < components.length; i++) {
            Component c = components[i];
            if (c instanceof Field){
                Field f = (Field)c;
                if (f.getValue()==null){
                    return true;
                }
            }
        }
        return false;
    }
}
