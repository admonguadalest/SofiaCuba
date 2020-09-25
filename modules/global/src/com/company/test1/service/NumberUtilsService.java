package com.company.test1.service;

public interface NumberUtilsService {
    String NAME = "test1_NumberUtilsService";

    Double roundTo2Decimals(Double number);
    Double roundToNDecimals(Double number, double n_decimals);
}