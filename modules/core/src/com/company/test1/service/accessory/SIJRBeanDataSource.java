package com.company.test1.service.accessory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;
public class SIJRBeanDataSource implements JRRewindableDataSource {
    List list = null;
    Iterator iterator = null;
    Object currentItem = null;

    public SIJRBeanDataSource(List l) {
        this.list = l;
        this.iterator = list.iterator();
    }

    public SIJRBeanDataSource(List l, int beginIndex, int endIndex){
        if (l.size() <= beginIndex) {
            this.list = new ArrayList();

        } else {

            List listAbreviada = new ArrayList();
            for (int i = beginIndex; (i < l.size()) && (i < endIndex); i++) {
                Object object = l.get(i);
                listAbreviada.add(object);
            }

            this.list = listAbreviada;
        }

        this.iterator = list.iterator();
    }


    @Override
    public void moveFirst() throws JRException {

        iterator = list.iterator();
        if (iterator.hasNext()) {
            currentItem = iterator.next();

        }
        return;
    }


    @Override
    public boolean next() throws JRException {
        if (iterator == null) return false;
        if (!iterator.hasNext()) return false;

        if (iterator.hasNext()) {
            currentItem = iterator.next();
            return true;
        }
        return false;
    }

    @Override
    public Object getFieldValue(JRField name) throws JRException{
        try{
            Object o = getFieldValue_(name, true);
            return o;
        }catch(JRException exc){
            Object o = getFieldValue_(name, false);
            return o;
        }
    }

    public Object getFieldValue_(JRField name, boolean nameDescription) throws JRException {
        String s;
        if (nameDescription){
            s = name.getName();
        }else{
            s = name.getDescription();
        }
        if (s == null){
            return "NULL";
        }
        String[] fields = this.strSplit(s, ".");

        Object currentObject = currentItem;
        if (currentObject == null){
            return "(N/D)";
        }
        for (int i=0; i<fields.length; i++) {
            String getterName = "get" + fields[i].substring(0, 1).toUpperCase() + fields[i].substring(1);
            try {

                Method getter = currentObject.getClass().getMethod(getterName, null);

                currentObject = getter.invoke(currentObject, null);
                if (currentObject == null) return "(N/D)";

            } catch (NoSuchMethodException e ) {
                throw new JRException("could not find getter for " + name.getName(), e);
            } catch (Exception e) {
                throw new JRException(e);

            }
        }
        if (name.getValueClass().equals(java.lang.String.class))
            return currentObject.toString();

        return currentObject;
    }

    private String[] strSplit(String text,String pattern){
        String[] tokens;
        Vector tokensv = new Vector();
        int start_pos = 0;
        int end_pos = 0;
        while (end_pos != -1){
            end_pos = text.indexOf(pattern,start_pos);
            if (end_pos != -1){
                tokensv.add(text.substring(start_pos, end_pos));
                start_pos = ++end_pos;

            }
        }
        if (end_pos == -1)
            tokensv.add(text.substring(start_pos));
        //transformando el vector en un array de strings
        tokens = new String[tokensv.size()];
        for (int a = 0;a < tokensv.size();a++){
            tokens[a] = (String) tokensv.get(a);
        }
        return tokens;
    }

    public List getList() {
        return list;
    }



}
