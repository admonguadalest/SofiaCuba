package com.company.test1.jmx;

import com.company.test1.jmx.importadores.DocumentacionesInquilinos;
import com.company.test1.jmx.importadores.ciclos.Ciclos;
import com.company.test1.jmx.importadores.coeficientes.Coeficientes;
import com.company.test1.jmx.importadores.conceptosadicionales.ConceptosAdicionales;
import com.company.test1.jmx.importadores.contratosinquilinos.Conceptos;
import com.company.test1.jmx.importadores.contratosinquilinos.ContratosInquilinos;
import com.company.test1.jmx.importadores.contratosinquilinos.Modelos;
import com.company.test1.jmx.importadores.definicionesremesas.DefinicionesRemesas;
import com.company.test1.jmx.importadores.definicionesremesas.Series;
import com.company.test1.jmx.importadores.notificaciones.Notificaciones;
import com.company.test1.jmx.importadores.personasyroles.Personas;
import com.company.test1.jmx.importadores.plantillas.Plantillas;
import com.company.test1.jmx.importadores.recibos.Cobros;
import com.company.test1.jmx.importadores.recibos.IPCs;
import com.company.test1.jmx.importadores.recibos.Recibos;
import com.company.test1.jmx.importadores.reports.FlexReports;
import com.company.test1.jmx.importadores.ubicacionesydepartamentos.Departamentos;
import com.company.test1.jmx.importadores.ubicacionesydepartamentos.Ubicaciones;
import com.company.test1.jmx.importadores.validaciones.OrdenesPagos;
import com.company.test1.jmx.importadores.validaciones.RealizacionesPagos;
import com.company.test1.listeners.ArchivoAdjuntoEntityListener;
import com.company.test1.listeners.ReciboEntityListener;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.security.app.Authenticated;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component("test1_DataMigration")
public class DataMigration implements DataMigrationMBean {

    Connection conn = null;

    @Inject
    Persistence persistence;
    @Inject
    DataManager dataManager;

    @Authenticated
    @Override
    public void doImportParteIpcs() throws Exception{
        new IPCs().realizaImportaciones(persistence);
    }

    public void doImportParteDocumentacionesInquilinos() throws Exception{
        new DocumentacionesInquilinos().realizaImportacion(dataManager, persistence);
    }


    @Authenticated
    @Override
    public void doImportRentamaster2() throws Exception{

        ArchivoAdjuntoEntityListener  .activated = false;
        ReciboEntityListener.activated = false;

        printTime("START ConceptosAdicionales");
        new ConceptosAdicionales().realizaImportacion(persistence);

        printTime("START Conceptos");
        new Conceptos().realizaImportacion(persistence);

//        printTime("START Modelos");
        new Modelos().realizaImportacionModelos(dataManager, persistence);
//
        printTime("START Series");
        new Series().realizaImportacion(persistence);
//
//        printTime("START Plantillas");
        new Plantillas().realizaImportaciones(persistence);
//
//        printTime("START FlexReports");
        new FlexReports().realizaImportaciones(persistence);

        printTime("START Personas");
        new Personas().realizaImportaciones(dataManager, persistence);

        printTime("START Representaciones Legales");
        new Personas().realizaImportacionRepresentacionesLegales(dataManager, persistence);

        printTime("START DefinicionesRemesas");
        new DefinicionesRemesas().realizaImportaciones(persistence);

        printTime("START Ubicaciones");
        new Ubicaciones().realizaImportacion(dataManager, persistence);

        printTime("START Departamentos");
        new Departamentos().realizaImportacion(dataManager, persistence);

//        //tipos coeficientes y coeficientes
        printTime("START TiposCoeficientes+Coeficientes");
        new Coeficientes().realizaImportacion(persistence);

//        printTime("START Ciclos");
        new Ciclos().realizaImportaciones(dataManager, persistence);

        printTime("START ContratosInquilinos");
        ContratosInquilinos cciiss = new ContratosInquilinos();
        printTime("START ContratosInquilinos->RealizaImportaciones");
        cciiss.realizaImportaciones(dataManager, persistence);
        printTime("START ContratosInquilinos->AjusteConceptoReciboAtrasos");
        cciiss.realizaAjusteConceptoReciboAtrasos(persistence);

//        eliminando los conceptos no maestros: aquellos conceptos que estan dupli- y hasta cuatriplicados segun entornos para los conceptos de los
//        recibos de alquiler. Una vez lograada una importacion exitosa se habra de eliminar la columna masterConcepto de la tabla CONCEPTO
        printTime("START ContratosInquilinos->EliminaConceptosNoMaestros");
        new Conceptos().eliminaConceptosNoMaestros(persistence);

//        //documentacioens inquilinos: puede depender de contratoInqulino
        new DocumentacionesInquilinos().realizaImportacion(dataManager, persistence);


        //importar los contratos inquilnos a parte y una vez importados realizar la asociacion
        //con los ciclos PENDIENTE



        Recibos rr = new Recibos();

        rr.createTableZHelperIva();
        printTime("START Recibos con Remesa");
        rr.realizaImportacionRemesas(dataManager, persistence);

        printTime("START RecibosIndividualizadosConOSinRemesa");
        rr.realizaImportacionRecibosIndividualizadosSinRemesa(dataManager, persistence);

//        eliminando las series duplicadas despues de importar recibos (series no maestros)
        printTime("START Remesas->Eliminacion Series");
        new Series().eliminaSeriesNoMaestros(persistence);

//        printTime("START IPCs");
        new IPCs().realizaImportaciones(persistence);

        printTime("START RealizacionesCobros");
        new Cobros().realizaImportacionesRealizacionesCobro(persistence);
        printTime("START OrdenesCobroRecibos");
        new Cobros().realizaImportacionesOrdenesCobroRecibo(persistence);

        //pagos
//        printTime("START RealizacionesPagos");
        new RealizacionesPagos().realizaImportaciones(persistence);
//        printTime("START OrdenesPagoAbono");
        new OrdenesPagos().realizaImportacionesOrdenPagoAbono(persistence);
//        printTime("START OrdenesPagoProveedor");
        new OrdenesPagos().realizaImportacionesOrdenPagoProveedor(persistence);
//        printTime("START OrdenesPagoFacturaProveedor");
        new OrdenesPagos().realizaImportacionesOrdenPagoFacturaProveedor(dataManager, persistence);

        //notificaciones
//        printTime("START Notificaciones");
        new Notificaciones().realizaImportacionesNotificacionesContratosInquilinos(persistence);



        printTime("END Migration");

        ArchivoAdjuntoEntityListener.activated = true;
        ReciboEntityListener.activated = true;

    }

    private void printTime(String message){
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy");
        System.out.println(message + " " + sdf.format(new Date()));
    }




}