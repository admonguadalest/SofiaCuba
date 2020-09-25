package com.company.test1.service;


import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public interface PdfService {
    String NAME = "test1_PdfService";

    void concatPdfs(Map m, OutputStream os, boolean paginate);
    void concatPdfs(List<InputStream> streamOfPDFFiles, OutputStream outputStream, boolean paginate);
    byte[] concatPdfs(List<byte[]> inputStreams, boolean paginate);


}