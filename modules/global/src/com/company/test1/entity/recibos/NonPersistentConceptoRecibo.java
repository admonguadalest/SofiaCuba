package com.company.test1.entity.recibos;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

@MetaClass(name = "test1_NonPersistentConceptoRecibo")
public class NonPersistentConceptoRecibo extends BaseUuidEntity implements Serializable {
    private static final long serialVersionUID = -4147450551300960054L;

    @MetaProperty
    protected Double importe;

    @MetaProperty
    protected Concepto concepto;

    @MetaProperty
    protected List<ConceptoRecibo> conceptosRecibo;


//    public Comparator<NonPersistentConceptoRecibo> conceptoOrdenacionComparator = new Comparator<NonPersistentConceptoRecibo>() {
//        @Override
//        public int compare(NonPersistentConceptoRecibo o1, NonPersistentConceptoRecibo o2) {
//            return o1.getConcepto().getOrdenacion().compareTo(o2.getConcepto().getOrdenacion());
//        }
//    };

    public Concepto getConcepto() {
        return concepto;
    }

    public void setConcepto(Concepto concepto) {
        this.concepto = concepto;
    }


    public void setConceptosRecibo(List<ConceptoRecibo> conceptosRecibo) {
        this.conceptosRecibo = conceptosRecibo;
    }

    public List<ConceptoRecibo> getConceptosRecibo() {
        return conceptosRecibo;
    }



    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

}