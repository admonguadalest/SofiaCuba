package com.company.test1.service.accessory.serialization;


import org.apache.commons.lang.StringUtils;
import com.company.test1.service.accessory.serialization.Util.TipoValor;

/**
 *
 * @author Carlos Conti
 */
public class Registro347_TR1 {

    public String tipoRegistro = "1";
    public String modeloDeclaracion = "347";
    public String ejercicio = "2017";
    public String nifDeclarante = "";
    public String razonSocialDeclarante = "";
    public String tipoDeSoporte = "";
    public String personaConQuienRelacionarse_Telefono = "";
    public String personaConQuienRelacionarse_NombreApellidos = "";
    public String noIdentificativoDeclaracion = "";
    public String declComplementariaSustitutiva_Complementaria = "";
    public String declComplementariaSustitutiva_Sustitutiva = "";
    public String noSustitutivoDeclAnterior = "";
    public String numTotalPersonasYEntidades = "";
    public String importeTotalAnualOperaciones_Signo = "";
    public String importeTotalAnualOperaciones_Importe = "";
    public String noTotalInmuebles = "";
    public String importeTotalOpsArrendamientoLocalesNegocio_Signo = "";
    public String importeTotalOpsArrendamientoLocalesNegocio_Importe = "";
    public String nifRepresentanteLegal = "";
    public String selloElectronico = "";




    public static String escribeRegistro(
            Object ejercicio,
            Object nifDeclarante,
            Object razonSocialDeclarante,
            Object tipoDeSoporte,
            Object personaConQuienRelacionarse_Telefono,
            Object personaConQuienRelacionarse_NombreApellidos,
            Object noIdentificativoDeclaracion,
            Object declComplementariaSustitutiva_Complementaria,
            Object declComplementariaSustitutiva_Sustitutiva,
            Object noSustitutivoDeclAnterior,
            Object numTotalPersonasYEntidades,
            Object importeTotalAnualOperaciones,
            Object noTotalInmuebles,
            Object importeTotalOpsArrendamientoLocalesNegocio,
            Object nifRepresentanteLegal,
            Object selloElectronico
    ) throws Exception{
        String registro = "";
        registro += "1";
        registro+= Util.escribeCampo(4, 1, "347", TipoValor.ALFANUMERICO_TEXTO);
        registro+= Util.escribeCampo(8, 4, ejercicio, TipoValor.ALFANUMERICO_TEXTO);
        registro+= Util.escribeCampo(17, 8, nifDeclarante, TipoValor.ALFANUMERICO_TEXTO);
        registro+= Util.escribeCampo(57, 17, razonSocialDeclarante, TipoValor.ALFANUMERICO_TEXTO);
        registro+= Util.escribeCampo(58, 57, tipoDeSoporte, TipoValor.ALFANUMERICO_TEXTO);
        registro+= Util.escribeCampo(67, 58, personaConQuienRelacionarse_Telefono, TipoValor.ALFANUMERICO_TEXTO);
        registro+= Util.escribeCampo(107, 67, personaConQuienRelacionarse_NombreApellidos, TipoValor.ALFANUMERICO_TEXTO);
        registro+= Util.escribeCampo(120, 107, noIdentificativoDeclaracion, TipoValor.NUMERICO_UNSIGNED);
        registro+= Util.escribeCampo(121, 120, declComplementariaSustitutiva_Complementaria, TipoValor.ALFANUMERICO_TEXTO);
        registro+= Util.escribeCampo(122, 121, declComplementariaSustitutiva_Sustitutiva, TipoValor.ALFANUMERICO_TEXTO);
        registro+= Util.escribeCampo(135, 122, noSustitutivoDeclAnterior, TipoValor.NUMERICO_UNSIGNED);
        registro+= Util.escribeCampo(144, 135, numTotalPersonasYEntidades, TipoValor.NUMERICO_UNSIGNED);
        registro+= Util.escribeCampo(160, 144, importeTotalAnualOperaciones, TipoValor.ALFANUMERICO_IMPORTE);
        registro+= Util.escribeCampo(169, 160, noTotalInmuebles, TipoValor.NUMERICO_UNSIGNED);
        registro+= Util.escribeCampo(185, 169, importeTotalOpsArrendamientoLocalesNegocio, TipoValor.ALFANUMERICO_IMPORTE);
        registro+= StringUtils.repeat(" ", 390-185);
        registro+= Util.escribeCampo(399, 390, nifRepresentanteLegal, TipoValor.ALFANUMERICO_TEXTO);
        registro+= StringUtils.repeat(" ", 487-399);
        registro+= Util.escribeCampo(500, 487, selloElectronico, TipoValor.ALFANUMERICO_TEXTO);
        registro += System.getProperty("line.separator");
        return registro;

    }






}
