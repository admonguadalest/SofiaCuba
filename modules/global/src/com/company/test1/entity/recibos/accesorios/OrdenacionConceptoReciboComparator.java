package com.company.test1.entity.recibos.accesorios;

import com.company.test1.entity.recibos.ConceptoRecibo;

import java.util.Comparator;

public class OrdenacionConceptoReciboComparator implements Comparator<ConceptoRecibo> {
    @Override
    public int compare(ConceptoRecibo o1, ConceptoRecibo o2) {
        ConceptoRecibo cr1 = o1;
        ConceptoRecibo cr2 = o2;
        Integer i1 = cr1.getConcepto().getOrdenacion();
        Integer i2 = cr2.getConcepto().getOrdenacion();

        if ((i1 == null) && (i2==null)) return 0;

        if (i1 == null) return 1;

        if (i2 == null) return -1;

        return i1.compareTo(i2);
    }
}
