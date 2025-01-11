package com.company.test1.web.screens.direccion;

import com.company.test1.entity.enums.NombreTipoDireccion;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.Direccion;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@UiController("test1_Direccion.edit")
@UiDescriptor("direccion-edit.xml")
@EditedEntityContainer("direccionDc")
@LoadDataBeforeShow
public class DireccionEdit extends StandardEditor<Direccion> {
    @Inject
    private LookupField<String> paisField;
    @Inject
    private LookupField<String> nombreField;
    @Inject
    private TextField<String> nombrePersonalizadoField;
    @Inject
    private InstanceContainer<Direccion> direccionDc;

    List<String> nombres = Arrays.asList(new String[]{NombreTipoDireccion.DOMICILIO_CONTRACTUAL.getId(), NombreTipoDireccion.DOMICILIO_INQUILINO.getId(),
            NombreTipoDireccion.DOMICILIO_SOCIAL.getId(), NombreTipoDireccion.DOMICILIO_ADMINISTRADOR.getId(), "Personalizada"});
    @Inject
    private DataContext dataContext;

    @Subscribe
    private void onInit(InitEvent event) {
        List<String> l = Arrays.asList("AFGANISTAN",
                "ALBANIA",
                "ANTARTIDA",
                "ARGELIA",
                "SAMOA NORTEAMERICANA",
                "ANDORRA",
                "ANGOLA",
                "ANTIGUA Y BARBUDA",
                "AZERBAYAN",
                "ARGENTINA",
                "AUSTRALIA",
                "AUSTRIA",
                "BAHAMAS",
                "BAHREIN",
                "BANGLADESH",
                "ARMENIA",
                "BARBADOS",
                "BELGICA",
                "BERMUDAS",
                "BHUTAN",
                "BOLIVIA",
                "BOSNIA HERZEGOVINA",
                "BOTSWANA",
                "BOUVET, ISLA",
                "BRASIL",
                "BELIZE",
                "INDICO, OCEANO (TERITORIO BRITANICO DEL)",
                "SALOMON, ISLAS",
                "VIRGENES, ISLAS (REINO UNIDO)",
                "BRUNEI",
                "BULGARIA",
                "MYANMAR",
                "BURUNDI",
                "BIELORRUSIA",
                "CAMBOYA",
                "CAMERUN",
                "CANADA",
                "CANTON Y ENDERBURY, ISLAS",
                "CABO VERDE",
                "CAIMAN, ISLAS",
                "REPUBLICA CENTROAFRICANA",
                "SRILANKA",
                "CHAD",
                "CHILE",
                "CHINA",
                "TAIWAN",
                "CHRISTMAS, ISLA",
                "COCOS, ISLAS",
                "COLOMBIA",
                "COMORES",
                "MAYOTTE",
                "CONGO",
                "REPUBLICA DEMOCRATICA DEL CONGO",
                "COOK, ISLAS",
                "COSTA RICA",
                "CROACIA",
                "CUBA",
                "CHIPRE",
                "REPUBLICA CHECA",
                "BENIN",
                "DINAMARCA",
                "DOMINICA",
                "REPUBLICA DOMINICANA",
                "REINA MAUD, TIERRA DE LA",
                "ECUADOR",
                "SALVADOR, EL",
                "GUINEA ECUATORIAL",
                "ETIOPIA",
                "ERITREA",
                "ESTONIA",
                "FAEROES, ISLAS",
                "MALVINAS, ISLAS",
                "FIDJI, ISLAS",
                "FINLANDIA",
                "FRANCE, METROPOLITAN",
                "FRANCIA",
                "GUAYANA FRANCESA",
                "POLINESIA FRANCESA",
                "FRENCH SOUTHERN TERRITORIES",
                "DJIBUTI",
                "GABON",
                "GEORGIA",
                "GAMBIA",
                "PALESTINA",
                "ALEMANIA",
                "GHANA",
                "GIBRALTAR",
                "KIRIBATI",
                "GRECIA",
                "GROENLANDIA",
                "GRANADA",
                "GUADALUPE",
                "GUAM",
                "GUATEMALA",
                "GUINEA",
                "GUYANA",
                "HAITI",
                "HEARD Y MCDONALD, ISLAS",
                "VATICANO",
                "HONDURAS",
                "HONG KONG",
                "HUNGRIA",
                "ISLANDIA",
                "INDIA",
                "INDONESIA",
                "IRAN",
                "IRAQ",
                "IRLANDA",
                "ISRAEL",
                "ITALIA",
                "COSTA DE MARFIL",
                "JAMAICA",
                "JAPON",
                "JOHNSTON, ISLA",
                "KAZAJSTAN",
                "JORDANIA",
                "KENIA",
                "COREA DEL NORTE",
                "COREA DEL SUR",
                "KUWAIT",
                "KIRGUIZISTAN",
                "LAOS",
                "LIBANO",
                "LESOTO",
                "LETONIA",
                "LIBERIA",
                "LIBIA",
                "LIECHTENSTEIN",
                "LITUANIA",
                "LUXEMBURGO",
                "MACAO",
                "MADAGASCAR",
                "MALAWI",
                "MALASIA",
                "MALDIVAS, ISLAS",
                "MALI",
                "MALTA",
                "MARTINICA",
                "MAURITANIA",
                "MAURICIO",
                "MEJICO",
                "MIDWAY, ISLAS",
                "MONACO",
                "MONGOLIA",
                "REPUBLICA DE MOLDAVIA",
                "MONTENEGRO",
                "MONTSERRAT",
                "MARRUECOS",
                "MOZAMBIQUE",
                "OMAN",
                "NAMIBIA",
                "NAURU",
                "NEPAL",
                "PAISES BAJOS",
                "ANTILLAS HOLANDESAS",
                "ARUBA",
                "NUEVA CALEDONIA",
                "VANUATU",
                "NUEVA ZELANDA",
                "NICARAGUA",
                "NIGER, REPUBLICA DE",
                "NIGERIA",
                "NIUE",
                "NORFOLK, ISLA",
                "NORUEGA",
                "NORTHERN MARIANA ISLANDS",
                "UNITED STATES MINOR OUTLING ISLANDS",
                "PACIFICO, ISLAS",
                "ESTADOS FEDERADOS DE MICRONESIA",
                "MARSHALL, ISLAS",
                "PALAU",
                "PAKISTAN",
                "PANAMA",
                "PAPUA NUEVA GUINEA",
                "PARAGUAY",
                "PERU",
                "FILIPINAS",
                "PITCAIRN, ISLA",
                "POLONIA",
                "PORTUGAL",
                "GUINEA-BISSAU",
                "TIMOR",
                "PUERTO RICO",
                "QATAR",
                "REUNION",
                "RUMANIA",
                "FEDERACION RUSA",
                "RUANDA",
                "SANTA ELENA",
                "SAINT-CHISTOPHER Y NIEVES",
                "ANGUILLA",
                "SANTA LUCIA",
                "SAN PEDRO Y MIQUELON",
                "SAN VICENTE Y LAS GRANADINAS",
                "SAN MARINO",
                "SANTO TOME Y PRINCIPE",
                "ARABIA SAUDITA",
                "SENEGAL",
                "SERBIA",
                "SEYCHELLES",
                "SIERRA LEONA",
                "SIKKIM",
                "SINGAPUR",
                "ESLOVAQUIA (REPUBLICA DE ESLOVAQUIA)",
                "VIETNAM",
                "ESLOVENIA",
                "SOMALIA",
                "REPUBLICA SUDAFRICANA",
                "ZIMBABWE",
                "ESPAÑA",
                "SAHARA OCCIDENTAL",
                "SUDAN",
                "SURINAM",
                "SVALBARD Y JAN MAYEN, ISLA",
                "SWAZILANDIA",
                "SUECIA",
                "SUIZA",
                "SIRIA",
                "TADJIKISTAN",
                "TAILANDIA",
                "TOGO",
                "TOKELAU",
                "TONGA",
                "TRINIDAD Y TOBAGO",
                "EMIRATOS ARABES UNIDOS",
                "TUNEZ",
                "TURQUIA",
                "TURKMENISTAN",
                "TURKS Y CAICOS, ISLAS",
                "TUVALU",
                "UGANDA",
                "UCRANIA",
                "EX-REPUBLICA YUGOSLAVA DE MACEDONIA",
                "EGIPTO",
                "REINO UNIDO",
                "TANZANIA",
                "ESTADOS UNIDOS DE AMERICA (USA)",
                "PACIFICO, ISLAS DEL (ESTADOS UNIDOS)",
                "VIRGENES, ISLAS (ESTADOS UNIDOS)",
                "BURKINA FASO",
                "URUGUAY",
                "UZBEKISTAN",
                "VENEZUELA",
                "WAKE, ISLA DE",
                "WALLIS Y FUTUNA, ISLAS",
                "SAMOA",
                "YEMEN",
                "ZAMBIA",
                "JERUSALEM",
                "FRANJA DE GAZA",
                "CISJORDANIA",
                "ALTOS DEL GOLAN",
                "EXTRANJERO",
                "APATRIDA",
                "NACION DESCONOCIDA");
        paisField.setOptionsList(l);

        
        nombreField.setOptionsList(nombres);





        
        
    }

    @Subscribe("nombreField")
    public void onNombreFieldValueChange1(HasValue.ValueChangeEvent event) {
        if (nombreField.getValue().compareTo("Personalizada")==0){
            if (nombres.indexOf(direccionDc.getItem().getNombre())!=-1) {
                nombrePersonalizadoField.setValue("");
            }else{
                nombrePersonalizadoField.setValue(direccionDc.getItem().getNombre());
            }
        }else{
            direccionDc.getItem().setNombre(nombreField.getValue());
            nombrePersonalizadoField.setVisible(false);
        }
    }

    
    
    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        if (nombres.indexOf(direccionDc.getItem().getNombre())!=-1){
            nombreField.setValue(direccionDc.getItem().getNombre());
        }else{
            nombreField.setValue("Personalizada");
            nombrePersonalizadoField.setVisible(true);
            nombrePersonalizadoField.setValue(direccionDc.getItem().getNombre());
        }
    }
    
    

    @Subscribe("nombrePersonalizadoField")
    public void onNombrePersonalizadoFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        
        direccionDc.getItem().setNombre(event.getValue());
    }
    

    @Subscribe("nombreField")
    public void onNombreFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        if (event.getValue().compareTo("Personalizada")==0){
            nombrePersonalizadoField.setValue(direccionDc.getItem().getNombre());
            nombrePersonalizadoField.setVisible(true);
        }
    }
    
    
    


}