package com.company.test1.web.screens.fianzas;

import com.company.test1.entity.contratosinquilinos.Fianza;
import com.company.test1.entity.enums.EstadoFianzaEnum;
import com.company.test1.web.screens.DynamicReportHelper;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.text.DecimalFormat;
import java.util.*;

@UiController("test1_Fianzas")
@UiDescriptor("fianzas.xml")
public class Fianzas extends Screen {

    @Inject
    private Button btnInvertirSeleccion;
    @Inject
    private LookupField<String> lkpVigencia;
    @Inject
    private CheckBox chbSolicitadaDevolucion;
    @Inject
    private CheckBox chbNoIngresadaAdmon;
    @Inject
    private Button btnCerrar;
    @Inject
    private Button btnBuscar;
    @Inject
    private CollectionContainer<Fianza> fianzasDc;
    @Inject
    private CollectionLoader<Fianza> fianzasDl;
    @Inject
    private CheckBox chbFianzaDevuelta;
    @Inject
    private CheckBox chbFianzaEnAdmon;
    @Inject
    private CheckBox chbFianzaEnCamara;
    @Inject
    private CheckBox chbGarantiasAval;
    @Inject
    private CheckBox chbGarantiasEfectivo;
    @Inject
    private CheckBox chbGarantiasPoliza;
    @Inject
    private TextField<String> txtDireccion;
    @Inject
    private DataManager dataManager;
    @Inject
    private Table<Fianza> tblFianzas;
    @Inject
    private ExportDisplay exportDisplay;


    public void onBtnBuscarClick() {
        fianzasDl.load();
    }

    @Subscribe
    private void onAfterInit(AfterInitEvent event) {
        List<String> l = Arrays.asList("TODOS", "VIGENTES", "NO VIGENTES");
        lkpVigencia.setOptionsList(l);
    }
    
    

    @Install(to = "fianzasDl", target = Target.DATA_LOADER)
    private List<Fianza> fianzasDlLoadDelegate(LoadContext<Fianza> loadContext) {
        String hql = "select distinct f from test1_Fianza f JOIN f.contratoInquilino ci JOIN ci.departamento d JOIN d.ubicacion u WHERE " +
                "concat(u.nombre,' ', d.piso, ' ', d.puerta) like :direccion ";
        HashMap parameters = new HashMap();
        String vigencia = lkpVigencia.getValue();
        if (vigencia == null) vigencia = "TODOS";
        if (vigencia.compareTo("VIGENTES")==0){
            hql += "and ci.estadoContrato = 3 ";
        }else if(vigencia.compareTo("NO VIGENTES")==0){
            hql += "and ci.estadoContrato != 3 ";
        }
        hql += " and ci.estadoContrato >= 3 ";

        String direccion = "%";
        if (txtDireccion.getValue()!=null){
            direccion = "%" + txtDireccion.getValue().replace(" ", "%") + "%";
        }

        String hqlG = "";
        if (chbGarantiasEfectivo.getValue()){
            hqlG += "(f.esAvalBancario = false) ";
        }
        if (chbGarantiasAval.getValue()){
            if (hqlG.trim().length()>0) hqlG += " OR ";
            hqlG += " (f.esAvalBancario = true) ";
        }
        if (chbGarantiasPoliza.getValue()){
            if (hqlG.trim().length()>0) hqlG += " OR ";
            hqlG += " (f.tienePolizaAlquiler = true) ";
        }


        String hqlF = "";
        if(chbNoIngresadaAdmon.getValue()){
            if (hqlF.trim().length()>0) hqlF += " OR ";
            hqlF += "(f.estadoFianza = :efnoingresadaadmon ) ";
            parameters.put("efnoingresadaadmon", EstadoFianzaEnum._0_NO_INGRESADA_EN_ADMON);
        }else{
            parameters.remove("efnoingresadaadmon");
        }
        if(chbFianzaEnCamara.getValue()){
            if (hqlF.trim().length()>0) hqlF += " OR ";
            hqlF += "(f.estadoFianza = :efcamara ) ";
            parameters.put("efcamara", EstadoFianzaEnum._2_EN_CAMARA);
        }else{
            parameters.remove("efcamara");
        }
        if(chbFianzaDevuelta.getValue()){
            if (hqlF.trim().length()>0) hqlF += " OR ";
            hqlF += "(f.estadoFianza = :efdevuelta ) ";
            parameters.put("efdevuelta", EstadoFianzaEnum._4_DEVUELTA_ADMON);
        }else{
            parameters.remove("efdevuelta");
        }


        String hql3 = "";
        if (chbFianzaEnAdmon.getValue()){
            hql3 += "(f.estadoFianza = :efenadmon ) ";
            parameters.put("efenadmon", EstadoFianzaEnum._1_EN_ADMON);
        }else{
            parameters.remove("efenadmon");
        }
        if (chbSolicitadaDevolucion.getValue()){
            if (hql3.trim().length()>0) hql3 += " OR ";
            hql3 += "(f.estadoFianza = :efsolicdev ) ";
            parameters.put("efsolicdev", EstadoFianzaEnum._3_SOLICITADA_DEVOLUCION);
        }else{
            parameters.remove("efsolicdev");
        }




        parameters.put("direccion", direccion);

        if (hqlG.trim().length()>0)
            hql += " AND (" + hqlG + ") ";
        if (hql3.trim().length()>0)
            hql3 += "OR (" + hql3 + ")";
        if (hqlF.trim().length()>0)
            hql += " AND (" + hqlF + " or " + hql3 + ") ";


        List<Fianza> ff = dataManager.loadValue(hql, Fianza.class).setParameters(parameters).list();
        for (int i = 0; i < ff.size(); i++) {
            Fianza f = ff.get(i);
            f = dataManager.reload(f, "fianza-list");
            ff.set(i, f);
        }
        return ff;
    }

    public void onBtnCerrarClick() {
        this.closeWithDefaultAction();
    }

    public void onBtnSeleccionarTodosClick() {
        tblFianzas.setSelected(tblFianzas.getItems().getItems());
    }

    public void onBtnInvertirSeleccionClick() {
        List<Fianza> selected = new ArrayList(tblFianzas.getSelected());
        ArrayList newsel = new ArrayList();
        List<Fianza> all = new ArrayList(tblFianzas.getItems().getItems());
        for (int i = 0; i < all.size(); i++) {
            if (selected.indexOf(all.get(i))==-1){
                newsel.add(all.get(i));
            }
        }
        tblFianzas.setSelected(newsel);
    }



    public void onBtnVerReportClick() {
        //campos footer
        Hashtable<String, Object> camposFooter = new Hashtable();
        List<Fianza> ff = fianzasDc.getItems();
        String nombreFieldComplementarias = "GARANTÍAS COMPL.";

        for (int i = 0; i < ff.size(); i++) {
            Fianza f = ff.get(i);
            if (camposFooter.containsKey(f.getEstadoFianzaNombre())){
                Double d = (Double) camposFooter.get(f.getEstadoFianzaNombre());
                d += f.getFianzaLegal();
                camposFooter.put(f.getEstadoFianzaNombre(), d);
            }else{

                camposFooter.put(f.getEstadoFianzaNombre(), f.getFianzaLegal());
            }
            if (camposFooter.containsKey(nombreFieldComplementarias)){
                Double d = (Double) camposFooter.get(nombreFieldComplementarias);
                d += f.getFianzaComplementaria();
                camposFooter.put(nombreFieldComplementarias, d);
            }else{
                camposFooter.put(nombreFieldComplementarias, f.getFianzaComplementaria());
            }

        }
        Integer[] anchosDeColumnaIArr = new Integer[]{150,150,30,30,15,30,30,50};
        List<Integer> anchosDeColumna = Arrays.asList(anchosDeColumnaIArr);
        byte[] bb = DynamicReportHelper.getReportDinamico("Listado de Fianzas", Fianza.class, tblFianzas, camposFooter, anchosDeColumna);
        exportDisplay.show(new ByteArrayDataProvider(bb), "Listado Fianzas.pdf");
    }
}