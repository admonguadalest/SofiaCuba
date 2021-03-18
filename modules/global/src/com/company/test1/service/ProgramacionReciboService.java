package com.company.test1.service;

import com.company.test1.entity.recibos.ConceptoRecibo;
import com.company.test1.entity.recibos.NonPersistentConceptoRecibo;
import com.company.test1.entity.recibos.ProgramacionRecibo;

import java.util.List;

public interface ProgramacionReciboService {
    String NAME = "test1_ProgramacionReciboService";

    List<NonPersistentConceptoRecibo> resumeConceptosRecibo(ProgramacionRecibo pr) throws Exception;

    int getNumRecibosEnQueConceptoReciboHaSidoAplicado(ConceptoRecibo cr) throws Exception;

}