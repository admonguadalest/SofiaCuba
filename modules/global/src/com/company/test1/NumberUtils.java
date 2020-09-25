package com.company.test1;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.text.DecimalFormat;

/**
 *
 * @author Xavier
 */
public class NumberUtils {



    public static Double roundTo2Decimals(Double number){
        return roundToNDecimals(number, 2.0);
    }


    public static Double roundToNDecimals(Double number, double n_decimals){

        if (number==null) return null;

        double powd = Math.pow(10.0, (n_decimals + 1));
        double number_res = number * powd;
        number_res = Math.floor(number_res);
        //dividir por 10 y tomar resto
        double n_10 = number_res / 10.0;
        double n_10_c = Math.ceil(n_10) - n_10;
        double n10_f = n_10 - Math.floor(n_10);
        double res;
        if (n_10_c <= n10_f){
            //redondeo para arriba
            res = Math.ceil(n_10);
            res = res / Math.pow(10.0,n_decimals);
            return res;
        }else{
            //redondeo para abajo
            res = Math.floor(n_10);
            res = res / Math.pow(10.0,n_decimals);
            return res;
        }

    }

    public static Number asNumber(Object s) throws Exception {
        if (s instanceof String){
            s = ((String)s).replace(",", "");
            try {
                Double d = new Double((String)s);
                return d;
            } catch (Exception exc) {
                throw exc;
            }
        }else if(s instanceof Number){
            return (Number)s;
        }else{
            throw new Exception("Object is not a number reperesentation");
        }
    }

    public static void main(String[] args){

        double n = 7.5;
        double res = roundToNDecimals(n,0);

        System.out.println(res);
    }

    public static String formatToImporte(Double d){
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(d);
    }
}