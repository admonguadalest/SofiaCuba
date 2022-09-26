package com.company.test1.service;

import com.company.test1.entity.Persona;

public interface RossumIngegrationService {
    String NAME = "test1_RossumIngegrationService";

    public Persona getBestMatchPersonaForString(String s) throws Exception;

}