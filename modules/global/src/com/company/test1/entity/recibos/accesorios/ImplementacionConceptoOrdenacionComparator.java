package com.company.test1.entity.recibos.accesorios;

import com.company.test1.entity.recibos.ImplementacionConcepto;

import java.util.Comparator;

public class ImplementacionConceptoOrdenacionComparator implements Comparator {

    /*
     * Este comparador compara en dos fases, primero por fecha y luego por concepto
     */

    @Override
    public int compare(Object o1, Object o2) {
        ImplementacionConcepto ic1 = (ImplementacionConcepto) o1;
        ImplementacionConcepto ic2 = (ImplementacionConcepto) o2;

        Integer orden1 = ic1.getConcepto().getOrdenacion();
        Integer orden2 = ic2.getConcepto().getOrdenacion();

        if ((orden1 == null) && (orden2 == null)) return 0;

        if (orden1 == null) return 1;

        if (orden2 == null)  return -1;

        return orden1.compareTo(orden2);
    }
}