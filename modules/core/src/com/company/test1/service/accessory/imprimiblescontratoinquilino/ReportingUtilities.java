package com.company.test1.service.accessory.imprimiblescontratoinquilino;

import com.company.test1.service.accessory.SIJRBeanDataSource;

//PENDIENTE REAJUSTAR
/*
ReportingUtilities es una clase descartada: solo me he visto en la obligacion de mantener este metodo pues la clase se pasa
a la produccion de un report. Sin embargo deberia probar si puedo ejecutar esa lina de codigo desde el script groovy del report
y de esta manera ahorrarme esta clase.

Puede anyadir confusion, pues en la antigua aplicacion hay un monton de referencias a ReportingUtilities que en Cuba se eliminan completamente
 */
class ReportingUtilities{

    public static SIJRBeanDataSource getSIJRBeanDataSourceDeList(java.util.List l){
        return new SIJRBeanDataSource(l);
    }
}