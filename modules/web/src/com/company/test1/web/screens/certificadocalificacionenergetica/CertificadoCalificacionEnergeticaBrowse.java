package com.company.test1.web.screens.certificadocalificacionenergetica;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.departamentos.CertificadoCalificacionEnergetica;

@UiController("test1_CertificadoCalificacionEnergetica.browse")
@UiDescriptor("certificado-calificacion-energetica-browse.xml")
@LookupComponent("certificadoCalificacionEnergeticasTable")
@LoadDataBeforeShow
public class CertificadoCalificacionEnergeticaBrowse extends StandardLookup<CertificadoCalificacionEnergetica> {
}