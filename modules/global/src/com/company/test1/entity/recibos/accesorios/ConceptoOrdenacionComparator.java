package com.company.test1.entity.recibos.accesorios;

import com.company.test1.entity.recibos.Concepto;

import java.util.Comparator;

public class ConceptoOrdenacionComparator implements Comparator<Concepto> {

    @Override
    public int compare(Concepto o1, Concepto o2) {

        Concepto c1 =  o1;
        Concepto c2 = o2;

        if ((c1 == null)&&(c2==null))  return 0;

        if (c1 == null) return -1;

        if (c2 == null) return 1;

        return c1.getOrdenacion() - c2.getOrdenacion();
    }
}
