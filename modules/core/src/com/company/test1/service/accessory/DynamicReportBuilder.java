package com.company.test1.service.accessory;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Xavier
 */

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;


/**
 * Uses the Jasper Report API to dynamically add columns to the Report
 */
public class DynamicReportBuilder {
    //The prefix used in defining the field name that is later used by the JasperFillManager
    public static final String COL_EXPR_PREFIX = "col";

    // The prefix used in defining the column header name that is later used by the JasperFillManager
    public static final String COL_HEADER_EXPR_PREFIX = "header";

    // The page width for a page in portrait mode with 10 pixel margins
    public final static int TOTAL_PAGE_WIDTH = 555;

    // The whitespace between columns in pixels
    public final static int SPACE_BETWEEN_COLS = 5;

    // The height in pixels of an element in a row and column
    private final static int DETAIL_COLUMN_HEIGHT = 10;

    // The height in pixels of an element in a row and column pf header
    private final static int HEADER_COLUMN_HEIGHT = 15;

    // The total height of the detail band
    private final static int DETAIL_BAND_HEIGHT = 12;

    // The total height of the column header or detail band
    private final static int HEADER_BAND_HEIGHT = 18;

    // The left and right margin in pixels
    public final static int MARGIN = 20;

    // The JasperDesign object is the internal representation of a report
    private JasperDesign jasperDesign;

    // The number of columns that are to be displayed
    private int numColumns;

    private int numeroDeColumnasDeCamposDeBusqueda = 2;

    List<Integer> anchoColumnas=null;

    Hashtable<String, Object> camposFooter = new Hashtable<String, Object>();


    public DynamicReportBuilder(JasperDesign jasperDesign, int numColumns,List<Integer> anchoColumnas) {
        this.jasperDesign = jasperDesign;
        this.numColumns = numColumns;
        this.anchoColumnas = anchoColumnas;
    }

    public void setCamposFooter(Hashtable<String, Object> camposFooter){
        this.camposFooter = camposFooter;
    }

    public void addDynamicColumns() throws JRException {

        JRDesignBand detailBand = new JRDesignBand();
        JRDesignBand headerBand = new JRDesignBand();

        JRDesignStyle normalStyle = getNormalStyle();
        JRDesignStyle columnHeaderStyle = getColumnHeaderStyle();

        int xPos = MARGIN;
        int columnWidth = (TOTAL_PAGE_WIDTH - MARGIN - (SPACE_BETWEEN_COLS * (numColumns - 1))) / numColumns;

        if (anchoColumnas==null){

            anchoColumnas = new ArrayList<Integer>();

            for (int i = 0; i < numColumns; i++) {
                anchoColumnas.add(columnWidth);
            }
        }


        for (int i = 0; i < numColumns; i++) {

            // Create a Column Field
            JRDesignField field = new JRDesignField();
            field.setName(COL_EXPR_PREFIX + i);
            field.setValueClass(java.lang.String.class);
            jasperDesign.addField(field);

            // Create a Header Field
            JRDesignField headerField = new JRDesignField();
            headerField.setName(COL_HEADER_EXPR_PREFIX + i);
            headerField.setValueClass(java.lang.String.class);
            jasperDesign.addField(headerField);

            // Add a Header Field to the headerBand
            //headerBand.setHeight(BAND_HEIGHT);
            headerBand.setHeight(HEADER_BAND_HEIGHT);
            JRDesignTextField colHeaderField = new JRDesignTextField();
            colHeaderField.setX(xPos);
            colHeaderField.setY(2);
            colHeaderField.setWidth(anchoColumnas.get(i));

            colHeaderField.setHeight(HEADER_COLUMN_HEIGHT);
            colHeaderField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            colHeaderField.setStyle(columnHeaderStyle);
            JRDesignExpression headerExpression = new JRDesignExpression();
            headerExpression.setValueClass(java.lang.String.class);
            headerExpression.setText("$F{" + COL_HEADER_EXPR_PREFIX + i + "}");
            colHeaderField.setExpression(headerExpression);
            headerBand.addElement(colHeaderField);

            // Add text field to the detailBand
            detailBand.setHeight(DETAIL_BAND_HEIGHT);
            JRDesignTextField textField = new JRDesignTextField();
            textField.setX(xPos);
            textField.setY(2);
            textField.setWidth(anchoColumnas.get(i));
            textField.setHeight(DETAIL_COLUMN_HEIGHT);
            textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textField.setStyle(normalStyle);
            JRDesignExpression expression = new JRDesignExpression();
            expression.setValueClass(java.lang.String.class);
            expression.setText("$F{" + COL_EXPR_PREFIX + i + "}");
            textField.setExpression(expression);
            detailBand.addElement(textField);

            xPos = xPos + anchoColumnas.get(i) + SPACE_BETWEEN_COLS;
        }

        JRDesignLine headerLine = new JRDesignLine();
        headerLine.setX(MARGIN);
        headerLine.setY(HEADER_BAND_HEIGHT-2);
        headerLine.setWidth(TOTAL_PAGE_WIDTH-MARGIN);

        headerBand.addElement(headerLine);

        jasperDesign.setColumnHeader(headerBand);
        ((JRDesignSection)jasperDesign.getDetailSection()).addBand(detailBand);
    }

    public void addTitleSection(String titulo,String nombreUsuario,String hora, String fecha,Hashtable<String,Object> camposDeBusqueda, Double anchoImagen,Double altoImagen) throws JRException{
        JRDesignBand titleBand = new JRDesignBand();

        JRDesignStyle normalStyle = getNormalStyle();
        JRDesignStyle columnHeaderStyle = getColumnHeaderStyle();
        jasperDesign.addStyle(normalStyle);
        jasperDesign.addStyle(columnHeaderStyle);

        JRDesignImage imagenLogo = new JRDesignImage(jasperDesign);

        JRDesignExpression jrde = new JRDesignExpression();
        jrde.setText("$P{P_LOGO}");
        jrde.setValueClass(net.sf.jasperreports.engine.JRRenderable.class);
        jrde.setValueClassName(net.sf.jasperreports.engine.JRRenderable.class.getName());

        imagenLogo.setExpression(jrde);

        int alturaImagen = altoImagen.intValue();

        imagenLogo.setHeight(alturaImagen);
        imagenLogo.setWidth(anchoImagen.intValue());
        imagenLogo.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);

        //Long margenImagen = Math.round(1.0f *(MARGIN + (TOTAL_PAGE_WIDTH-MARGIN-anchoImagen)/2.0f));

        imagenLogo.setX(TOTAL_PAGE_WIDTH - MARGIN - anchoImagen.intValue());
        imagenLogo.setY(0);
        imagenLogo.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
        titleBand.addElement(imagenLogo);


        JRDesignStyle titleStyle = getTitleStyle();
        jasperDesign.addStyle(titleStyle);


        JRDesignStaticText titleField = new JRDesignStaticText();
        titleField.setX(MARGIN);
        titleField.setY(alturaImagen+5);
        titleField.setWidth(TOTAL_PAGE_WIDTH - MARGIN);


        titleField.setHeight(HEADER_COLUMN_HEIGHT);
        titleField.setStyle(titleStyle);
        titleField.setText(titulo.toUpperCase());
        titleField.setHeight(25);
        titleBand.addElement(titleField);

        jasperDesign.addStyle(getMiniStyle());

        JRDesignStaticText fechaField = new JRDesignStaticText();
        fechaField.setX(MARGIN);
        fechaField.setY(alturaImagen+30);
        fechaField.setWidth(TOTAL_PAGE_WIDTH - MARGIN);
        fechaField.setHeight(15);
        fechaField.setStyle(getMiniStyle());
        fechaField.setText("Fecha: " + fecha + "  Hora: " + hora + "  Informe Realizado por: " + nombreUsuario);
        titleBand.addElement(fechaField);


       /* JRDesignStaticText horaField = new JRDesignStaticText();
        horaField.setX(MARGIN+100);
        horaField.setY(alturaImagen+40);
        horaField.setWidth(100);
        horaField.setHeight(15);
        horaField.setStyle(normalStyle);
        horaField.setText("Hora: " + hora);
        titleBand.addElement(horaField);


        JRDesignStaticText usuarioField = new JRDesignStaticText();
        usuarioField.setX(300);
        usuarioField.setY(alturaImagen+40);
        usuarioField.setWidth(255);
        usuarioField.setHeight(15);
        usuarioField.setStyle(normalStyle);
        usuarioField.setText("Informe Realizado por:" + nombreUsuario);
        usuarioField.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
        titleBand.addElement(usuarioField);*/


        List<String> keys = new ArrayList(camposDeBusqueda.keySet());


        if (!keys.isEmpty()) {
            titleBand.setHeight(alturaImagen + 70 + (int) Math.ceil(1.0 * keys.size() / numeroDeColumnasDeCamposDeBusqueda)  * DETAIL_BAND_HEIGHT);
            String textoListadoCamposDeBusqueda = "Campos de búsqueda:";

            JRDesignStaticText listadoBusquedaField = new JRDesignStaticText();
            listadoBusquedaField.setX(MARGIN);
            listadoBusquedaField.setY(alturaImagen+50);
            listadoBusquedaField.setWidth(TOTAL_PAGE_WIDTH - MARGIN);

            listadoBusquedaField.setHeight(HEADER_COLUMN_HEIGHT);
            listadoBusquedaField.setStyle(columnHeaderStyle);
            listadoBusquedaField.setText(textoListadoCamposDeBusqueda);
            titleBand.addElement(listadoBusquedaField);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            DecimalFormat df = new DecimalFormat("#,##0.00");

            int numeroColumna = 0;
            int anchuraColumnasCampoDeBusqueda = (TOTAL_PAGE_WIDTH - MARGIN - 20) / numeroDeColumnasDeCamposDeBusqueda;

            for (int i = 0; i < keys.size(); i++) {
                String clave = keys.get(i);
                Object val = camposDeBusqueda.get(clave);

                String valor;
                if (val == null) {
                    valor = "---";
                } else if (val instanceof Date) {
                    valor = sdf.format((Date) val);
                } else if (val instanceof Double) {
                    valor = df.format(val);
                } else {
                    valor = val.toString();
                }

                JRDesignStaticText campoBusquedaField = new JRDesignStaticText();
                campoBusquedaField.setX(anchuraColumnasCampoDeBusqueda *  numeroColumna + MARGIN + 20);
                campoBusquedaField.setY(alturaImagen + 65 + i/numeroDeColumnasDeCamposDeBusqueda * DETAIL_BAND_HEIGHT);
                campoBusquedaField.setWidth(anchuraColumnasCampoDeBusqueda);
                campoBusquedaField.setStyle(normalStyle);
                campoBusquedaField.setText(clave + ": " + valor);
                campoBusquedaField.setHeight(DETAIL_COLUMN_HEIGHT);
                titleBand.addElement(campoBusquedaField);

                numeroColumna = (numeroColumna + 1) % numeroDeColumnasDeCamposDeBusqueda; // Suma uno y llega al final lo reinicia
            }

        } else {
            titleBand.setHeight(alturaImagen+55);
        }

        jasperDesign.setTitle(titleBand);
    }



    public void addPageHeaderSection(Double anchoImagen,Double altoImagen) throws JRException{
        JRDesignBand pageHeader = new JRDesignBand();
        JRDesignImage imagenLogo = new JRDesignImage(jasperDesign);

        JRDesignExpression jrde = new JRDesignExpression();
        jrde.setText("$P{P_LOGO}");
        jrde.setValueClass(net.sf.jasperreports.engine.JRRenderable.class);
        jrde.setValueClassName(net.sf.jasperreports.engine.JRRenderable.class.getName());

        imagenLogo.setExpression(jrde);

        int alturaImagen = altoImagen.intValue();

        imagenLogo.setHeight(alturaImagen);
        imagenLogo.setWidth(anchoImagen.intValue());
        imagenLogo.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
        imagenLogo.setX(TOTAL_PAGE_WIDTH - MARGIN - anchoImagen.intValue());

        pageHeader.addElement(imagenLogo);
        //pageHeader.s


        jasperDesign.setPageHeader(pageHeader);
        pageHeader.setHeight(((Double)(altoImagen + 5)).intValue());

        JRDesignExpression jrdePrint = new JRDesignExpression();
        jrdePrint.setText("$V{PAGE_NUMBER} > 1");
        jrdePrint.setValueClass(java.lang.Boolean.class);
        jrdePrint.setValueClassName(java.lang.Boolean.class.getName());

        pageHeader.setPrintWhenExpression(jrdePrint);
    }




    public void addPageFooterSection(){
        JRDesignBand pageFooterBand = new JRDesignBand();
        pageFooterBand.setHeight(150);

        JRDesignStyle normalStyle = getNormalStyle();

        int posY = 2;
        int h = 20;
        int counter = 0;
        //campos footer
        if (camposFooter != null) {
            Enumeration<String> keys = camposFooter.keys();
            while (keys.hasMoreElements()) {
                String k = keys.nextElement();
                JRDesignStaticText staticText = new JRDesignStaticText();
                staticText.setX(TOTAL_PAGE_WIDTH - MARGIN - 200);
                staticText.setY(posY + (h * counter));
                staticText.setWidth(200);
                staticText.setHeight(20);
                staticText.setStyle(normalStyle);
                staticText.setText(k + ":" + camposFooter.get(k).toString());

                counter += 1;
                pageFooterBand.addElement(staticText);
            }
        }


        JRDesignTextField pageField = new JRDesignTextField();
        pageField.setX(TOTAL_PAGE_WIDTH - MARGIN - 20);
        pageField.setY(150-25);
        pageField.setWidth(20);

        pageField.setHeight(20);
        pageField.setStyle(normalStyle);


        JRDesignExpression expression = new JRDesignExpression();
        expression.setValueClass(java.lang.Integer.class);
        expression.setText("$V{PAGE_NUMBER}");
        pageField.setExpression(expression);

        pageFooterBand.addElement(pageField);
        jasperDesign.setPageFooter(pageFooterBand);
    }


    private JRDesignStyle getTitleStyle() {
        JRDesignStyle titleStyle = new JRDesignStyle();
        titleStyle.setName("Sans_Title");
        titleStyle.setDefault(true);
        titleStyle.setFontName("SansSerif");
        titleStyle.setFontSize(14);
        titleStyle.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
        titleStyle.setBold(true);
        titleStyle.setUnderline(true);
        titleStyle.setPdfFontName("Helvetica");
        titleStyle.setPdfEncoding("Cp1252");
        titleStyle.setPdfEmbedded(false);
        return titleStyle;
    }

    private JRDesignStyle getMiniStyle() {
        JRDesignStyle titleStyle = new JRDesignStyle();
        titleStyle.setName("Sans_Mini");
        titleStyle.setDefault(true);
        titleStyle.setFontName("SansSerif");
        titleStyle.setFontSize(6);
        titleStyle.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
        titleStyle.setPdfFontName("Helvetica");
        titleStyle.setPdfEncoding("Cp1252");
        titleStyle.setPdfEmbedded(false);
        titleStyle.setForecolor(Color.GRAY);
        return titleStyle;
    }

    private JRDesignStyle getNormalStyle() {
        JRDesignStyle normalStyle = new JRDesignStyle();
        normalStyle.setName("Sans_Normal");
        normalStyle.setDefault(true);
        normalStyle.setFontName("SansSerif");
        normalStyle.setFontSize(6);
        normalStyle.setPdfFontName("Helvetica");
        normalStyle.setPdfEncoding("Cp1252");
        normalStyle.setPdfEmbedded(false);
        return normalStyle;
    }

    private JRDesignStyle getColumnHeaderStyle() {
        JRDesignStyle columnHeaderStyle = new JRDesignStyle();
        columnHeaderStyle.setName("Sans_Header");
        columnHeaderStyle.setDefault(false);
        columnHeaderStyle.setFontName("SansSerif");
        columnHeaderStyle.setFontSize(8);
        columnHeaderStyle.setBold(true);
        columnHeaderStyle.setPdfFontName("Helvetica");
        columnHeaderStyle.setPdfEncoding("Cp1252");
        columnHeaderStyle.setPdfEmbedded(false);
        return columnHeaderStyle;
    }

    public void setNumColumns(int numColumns) {
        this.numeroDeColumnasDeCamposDeBusqueda = numColumns;
    }

}