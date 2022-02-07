package com.company.test1.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.*;
import org.apache.fop.pdf.PDFDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service(PdfService.NAME)
public class PdfServiceBean implements PdfService {

    public List<byte[]> pdfToImageList(byte[] bb) throws Exception{
        List<byte[]> images = new ArrayList();
        PDDocument pdf = PDDocument.load(bb);
        PDFRenderer pdfRenderer = new PDFRenderer(pdf);
        for (int page = 0; page < pdf.getNumberOfPages(); ++page) {
            BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 150, ImageType.RGB);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bim, "jpg",baos);
            images.add(baos.toByteArray());
        }
        pdf.close();
        return images;
    }

    public byte[] imagesToPdf(List<byte[]> images) throws Exception{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 20.0f, 20.0f, 20.0f, 20.0f);
        PdfWriter pdfWriter = PdfWriter.getInstance(document, baos);
        document.open();
        for (int i = 0; i < images.size(); i++) {
            byte[] im = images.get(i);
            com.itextpdf.text.Image itxtim = com.itextpdf.text.Image.getInstance(im);
            document.setPageSize(itxtim);
            document.newPage();
            itxtim.setAbsolutePosition(0, 0);
            document.add(itxtim);
        }
        document.close();

        return baos.toByteArray();

    }

    public void concatPdfs(Map m, OutputStream os, boolean paginate) {
        ArrayList al = new ArrayList();
        Iterator it = m.keySet().iterator();
        while(it.hasNext()){
            Object o = it.next();
            Integer i = (Integer) o;
            al.add(m.get(i));
        }
        concatPdfs(al, os, paginate);
    }

    public void concatPdfs(List<InputStream> streamOfPDFFiles, OutputStream outputStream, boolean paginate) {

        Document document = new Document();
        try {
            List<InputStream> pdfs = streamOfPDFFiles;
            List<PdfReader> readers = new ArrayList<PdfReader>();
            int totalPages = 0;
            Iterator<InputStream> iteratorPDFs = pdfs.iterator();

            // Create Readers for the pdfs.
            while (iteratorPDFs.hasNext()) {
                InputStream pdf = iteratorPDFs.next();
                PdfReader pdfReader = new PdfReader(pdf);
                readers.add(pdfReader);
                totalPages += pdfReader.getNumberOfPages();
            }
            // Create a writer for the outputstream
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            document.open();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            PdfContentByte cb = writer.getDirectContent(); // Holds the PDF
            // data

            PdfImportedPage page;
            int currentPageNumber = 0;
            int pageOfCurrentReaderPDF = 0;
            Iterator<PdfReader> iteratorPDFReader = readers.iterator();

            // Loop through the PDF files and add to the output.
            while (iteratorPDFReader.hasNext()) {
                PdfReader pdfReader = iteratorPDFReader.next();

                // Create a new page in the target for each source page.
                while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
                    document.newPage();
                    pageOfCurrentReaderPDF++;
                    currentPageNumber++;
                    page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
                    cb.addTemplate(page, 0, 0);
                }
                pageOfCurrentReaderPDF = 0;
            }
            outputStream.flush();
            document.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (document.isOpen())
                document.close();
            try {
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    /*Bridge for it to be callable from the gui side, since inputstreams are not serializable*/
    public byte[] concatPdfs(List<byte[]> inputStreams, boolean paginate){
        List<InputStream> lis = new ArrayList<InputStream>();
        for (int i = 0; i < inputStreams.size(); i++) {
            byte[] bb = inputStreams.get(i);
            lis.add(new ByteArrayInputStream(bb));
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        concatPdfs(lis, baos, paginate);
        return baos.toByteArray();
    }

}