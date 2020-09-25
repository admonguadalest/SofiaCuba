package com.company.test1.service.accessory;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Carlos Conti
 */
public class MyBeanUtils {

    public static String transformFromPropertyNameToCallerMethod(String property){
        String s = "get" + property.substring(0,1).toUpperCase() + property.substring(1);
        return s;
    }

    public static String transformFromPropertyNameToSetterMethod(String property){
        String s = "set" + property.substring(0,1).toUpperCase() + property.substring(1);
        return s;
    }

    public static final Object readBeanPath(Object base, String expression){

        String[] path = expression.split("\\.");
        Object currObj = base;
        Class currClass = base.getClass();


        for (int a = 0;a < path.length;a++){
            try{
                String property = path[a];
                String method = transformFromPropertyNameToCallerMethod(property);
                Method m = null;
                while (m==null){
                    try{
                        m = currClass.getDeclaredMethod(method);
                    }catch(Exception exc){
                        int x = 2;
                    }
                    //it can be that it is an ancestor that holds the method
                    if (m!=null) break;
                    if (currClass.getSuperclass()!=null){
                        currClass = currClass.getSuperclass();
                    }else{
                        break;
                    }
                }
                currObj = m.invoke(currObj);
                if (currObj==null) return null;
                currClass = currObj.getClass();
                if (currObj==null) return null;
            }catch(Exception exc){
                return null;
            }

        }
        return currObj;
    }

    public static final boolean writeBeanPath(Object base, String expression, Object value){


        String[] path = expression.split("\\.");
        Object currObj = base;
        Class currClass = base.getClass();


        for (int a = 0;a < path.length-1;a++){
            try{
                String property = path[a];
                String method = transformFromPropertyNameToCallerMethod(property);
                Method m = null;
                if (m==null){
                    m = currClass.getDeclaredMethod(method);
                }
                currObj = m.invoke(base);
            }catch(Exception exc){
                return false;
            }

        }

        String property = path[path.length-1];
        String method = transformFromPropertyNameToSetterMethod(property);
        Class type = currObj.getClass();
        try{
            Method m = currClass.getDeclaredMethod(method, value.getClass());
            m.invoke(currObj, value);
        }catch(Exception exc){
            return false;
        }
        return true;
    }

    /**
     * Usado para los casos en que la clase de value es un ancestor, y por reflexion no haya el metodo
     * @param base
     * @param expression
     * @param value
     * @param overrideValueClass
     * @return
     */
    public static final boolean writeBeanPath(Object base, String expression, Object value, Class overrideValueClass){


        String[] path = expression.split("\\.");
        Object currObj = base;
        Class currClass = base.getClass();


        for (int a = 0;a < path.length-1;a++){
            try{
                String property = path[a];
                String method = transformFromPropertyNameToCallerMethod(property);
                Method m = null;
                if (m==null){
                    m = currClass.getDeclaredMethod(method);
                }
                currObj = m.invoke(base);
            }catch(Exception exc){
                return false;
            }

        }

        String property = path[path.length-1];
        String method = transformFromPropertyNameToSetterMethod(property);
        Class type = currObj.getClass();
        try{
            Method m = currClass.getDeclaredMethod(method, overrideValueClass);
            m.invoke(currObj, value);
        }catch(Exception exc){
            return false;
        }
        return true;
    }

    public static void setDescendantFromAncestor(Class[] classes, Object source, Object dest)
            throws Exception
    {
        Field[] ff;
        List<Field> fields = new ArrayList<Field>();
        for (int i = 0; i < classes.length; i++) {
            Class class1 = classes[i];
            Field[] ffc = class1.getDeclaredFields();
            List l = Arrays.asList(ffc);
            fields.addAll(l);
        }
        Class ancestorClass = classes[classes.length-1];
        ff  = fields.toArray(new Field[0]);
        List l = new ArrayList();
        for (int i = 0; i < ff.length; i++) {
            Field field = ff[i];
            String name = field.getName();
            String methodNameGetter = MyBeanUtils.transformFromPropertyNameToCallerMethod(name);
            String methodNameSetter = MyBeanUtils.transformFromPropertyNameToSetterMethod(name);

            try{
                Method m = ancestorClass.getMethod(methodNameGetter);
                Class c = m.getReturnType();
                Method m2 = ancestorClass.getMethod(methodNameSetter, c);

                //m must be a getter method
                Object[] o = new Object[4];
                o[0] = name;
                o[1] = c;
                o[2] = methodNameSetter;
                o[3] = methodNameGetter;
                l.add(o);
            }catch(Exception exc){
                //jump to next
            }
        }

        for (int i = 0; i < l.size(); i++) {
            Object[] object = (Object[]) l.get(i);
            try{
                Method mgetter = ancestorClass.getMethod((String)object[3]);
                Method msetter = ancestorClass.getMethod((String)object[2], (Class)object[1]);
                Object value = mgetter.invoke(source);
                msetter.invoke(dest, value);

                //end checking
            }catch(Exception exc){
                throw exc;
            }
        }

    }

    public static Field getField(Class c, String fieldName){
        Field f = null;
        Class _c = c;
        while(f==null){
            try{
                f = _c.getDeclaredField(fieldName);
            }catch(Exception exc){
                if (_c.getSuperclass()!=null){
                    _c = _c.getSuperclass();
                }
            }
        }
        return f;
    }


    public static Method getDeclaredMethodExt(Class type, String method, Class[] pams)
            throws Exception{
        try{
            Method m = type.getDeclaredMethod(method, pams);
            return m;
        }catch(Exception exc){
            Class superType = type.getSuperclass();
            if (superType == null){
                throw new NoSuchMethodException();
            }
            return getDeclaredMethodExt(superType, method, pams);
        }
    }

    public static Field[] discoverFields(Class c){
        ArrayList al = new ArrayList();
        Class currClass = c;
        while(true){
            Field[] ff = currClass.getDeclaredFields();
            for (int i = 0; i < ff.length; i++) {
                Field field = ff[i];
                al.add(field);
            }
            currClass = currClass.getSuperclass();
            if (currClass==null){
                break;
            }
        }
        return (Field[]) al.toArray(new Field[0]);
    }



}
