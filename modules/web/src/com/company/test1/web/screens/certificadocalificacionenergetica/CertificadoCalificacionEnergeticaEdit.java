package com.company.test1.web.screens.certificadocalificacionenergetica;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.departamentos.CertificadoCalificacionEnergetica;

@UiController("test1_CertificadoCalificacionEnergetica.edit")
@UiDescriptor("certificado-calificacion-energetica-edit.xml")
@EditedEntityContainer("certificadoCalificacionEnergeticaDc")
@LoadDataBeforeShow
public class CertificadoCalificacionEnergeticaEdit extends StandardEditor<CertificadoCalificacionEnergetica> {
}