package com.company.test1.service;

import org.springframework.stereotype.Service;

@Service(NumberUtilsService.NAME)
public class NumberUtilsServiceBean implements NumberUtilsService {

    public Double roundTo2Decimals(Double number){
        return roundToNDecimals(number, 2.0);
    }


    public Double roundToNDecimals(Double number, double n_decimals){

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

}