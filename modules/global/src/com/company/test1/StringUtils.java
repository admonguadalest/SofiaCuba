package com.company.test1;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author Carlos Conti
 */
public class StringUtils {

    public static String[] split(String source,String pattern){
        String[] s = source.split(pattern);
        return s;
    }

    public static String[] splitWithoutBlanks(String source,String pattern){
        String[] s = split(source,pattern);

        List<String> ss = new ArrayList();

        for (int i = 0; i < s.length; i++) {
            String string = s[i];
            if (string.trim().length() > 0){
                ss.add(string);
            }
        }

        String[] sNoBlanks = new String[ss.size()];
        for (int i = 0; i < ss.size(); i++) {
            String string = ss.get(i);
            sNoBlanks[i] = string;
        }

        return s;
    }

    public static String right(String s, int len){
        if (s!=null){
            if (s.length()>=len){
                String ss = s.substring(s.length()-len);
                return ss;
            }else return s;
        }else{
            return "";
        }
    }

    public static String left(String s, int len){
        if (s!=null){
            if (s.length()>=len){
                String ss = s.substring(0,len);
                return ss;
            }else return s;
        }else{
            return "";
        }
    }

    public static boolean isNumber(String s){
        try{
            Long l = new Long(s);
            return true;
        }catch(Exception exc){
            NumberFormat nf = NumberFormat.getInstance();
            try{
                Object o = nf.parse(s);
                return o != null;
            }catch(Exception sexc){
                return false;
            }

        }
    }

    public static String[] getUniqueValues(String[][] arr){
        ArrayList<String> al = new ArrayList<String>();
        for (int i = 0; i < arr.length; i++) {
            String[] strings = arr[i];
            for (int j = 0; j < strings.length; j++) {
                String string = strings[j];
                if (al.indexOf(string)==-1){
                    al.add(string);
                }
            }
        }
        return al.toArray(new String[0]);
    }

    public static String replace(String base, String search, String replace){
        String s = base.replace(search, replace);
        return s;
    }

    public static void main(String[] args){
        String s="0123456789";
        String l = left(s,4);
        String r = right(s,4);
        int y = 2;
    }

    public static String repeat(String s,int n){
        String ss = "";
        for (int i = 0; i < n; i++) {
            ss += s;

        }
        return ss;
    }

    /**
     * Generates a random string based on the char domain and of a given length. The random function is Math.random();
     * @param charDomain
     * @param len
     * @return
     */
    public static String generateRandomString (String charDomain, int len){
        String generatedString = "";
        int lenCharDomain = charDomain.length();
        while(generatedString.length() < len){
            double r = Math.random();
            double pos = ((double)lenCharDomain) * r;
            double remainder = Math.IEEEremainder(pos, 2);
            remainder = Math.abs(remainder);
            if (remainder < 0.5){
                pos += remainder;
            }else{
                pos -= (1 - remainder);
            }
            int pos_int = (int) pos;
            if (pos_int<lenCharDomain){
                generatedString += charDomain.charAt(pos_int);
            }else{
                generatedString += charDomain.charAt(charDomain.length()-1);
            }

        }
        return generatedString;
    }


    public static String getMesDeNumero(int n){

        String[] meses = new String[]{"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
        return meses[n-1];
    }

    public static String dateToString(Date d){

        if (d==null) return "";

        String dia = new SimpleDateFormat("dd").format(d);
        String mes = new SimpleDateFormat("MM").format(d);
        String anno = new SimpleDateFormat("yyyy").format(d);

        Integer diaNumero = Integer.valueOf(dia);
        if (diaNumero<10){
            dia = diaNumero.toString();
        }

        Integer mesNumero = Integer.valueOf(mes);

        return dia + " de " + getMesDeNumero(mesNumero) + " de " + anno;
    }

    public static char reemplazarCaracteresIncorrectos(char c){

        List<Character> caracteresInvalidos = Arrays.asList(',',';','¡','!','¿','?','º','ª');
        if (caracteresInvalidos.contains(c)){
            return ' ';
        }

        return c;
    }

    public static String obtenerStringSinCaracteresIncorrectos(String s){

        String nuevaCadena = "";

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            nuevaCadena += reemplazarCaracteresIncorrectos(c);
        }

        return nuevaCadena;
    }

    public static boolean comprobarSiExistenCaracteresIncorrectos(String s){

        List<Character> caracteresProhibidos = Arrays.asList('.',',',';','¡','!','¿','?','"','·','$','%','&','(',')','=','^','*');

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (caracteresProhibidos.contains(c)){
                return true;
            }

        }

        return false;
    }

    public static String obtenerStringSeparadoPorBarras(List<String> ss){

        if (ss.isEmpty())  return "";

        String nuevaCadena = "";

        for (int i = 0; i < ss.size(); i++) {
            String s = ss.get(i);
            nuevaCadena += s + " / ";
        }

        nuevaCadena = nuevaCadena.substring(0, nuevaCadena.length() - 3);
        return nuevaCadena;
    }

    public static String getStringSinEspacios(String cadena){
        cadena = cadena.trim();

        String nuevaCadena = "";

        for (int i = 0; i < cadena.length(); i++) {
            char c = cadena.charAt(i);

            if (c != ' '){
                nuevaCadena += c;
            }
        }

        return nuevaCadena;
    }

    public static String getStringSoloCaracteresNumericos(String cadena){

        String cadenaNumerica = "";

        for (int i = 0; i < cadena.length(); i++) {
            char c = cadena.charAt(i);
            if (Character.isDigit(c)){
                cadenaNumerica += c;
            }
        }

        return cadenaNumerica;
    }

    public static UUID toUUID(String s){
        if (s.indexOf("-")==-1){
            String s1 = s.substring(0,8);
            String s2 = s.substring(8,12);
            String s3 = s.substring(12,16);
            String s4 = s.substring(16,20);
            String s5 = s.substring(20,32);
            String res = s1 + "-" + s2 + "-" +s3 + "-" +s4 + "-" +s5;
            return UUID.fromString(res);
        }else{
            return UUID.fromString(s);
        }
    }
}