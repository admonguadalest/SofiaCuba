package com.company.test1.service;

import com.company.test1.entity.Persona;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.DataManager;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@Service(RossumIngegrationService.NAME)
public class RossumIngegrationServiceBean implements RossumIngegrationService {

    @Inject
    Persistence persistence;
    @Inject
    DataManager dataManager;

    @Override
    public Persona getBestMatchPersonaForString(String s) throws Exception {
        if (s.trim().length()==0){
            return null;
        }
        Transaction t = persistence.createTransaction();
        try{

            String sql = "select id, nombre_completo, length(longest_common_substring('"+s+"',concat('00000000000000000',p.nombre_completo,'00000000000000000'))) as l from persona p order by l desc ";
            List l = persistence.getEntityManager()
                    .createNativeQuery(sql).getResultList();
            String personaId = (String) ((Object[])l.get(0))[0];
            String personaIdCorrected = personaId.substring(0,8)+"-"+
                                personaId.substring(8,12)+"-"+
                                personaId.substring(12,16)+"-"+
                                personaId.substring(16,20)+"-"+
                                personaId.substring(20);
            Persona p = dataManager.load(Persona.class).id(UUID.fromString(personaIdCorrected)).one();

            t.close();
            return p;
        }catch(Exception exc){
            t.close();
        }
        return null;
    }
}