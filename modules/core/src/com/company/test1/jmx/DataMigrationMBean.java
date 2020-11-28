package com.company.test1.jmx;

import com.haulmont.cuba.core.sys.jmx.JmxBean;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

@JmxBean(module = "data", alias = "DataMigrationMBean")

@ManagedResource(description = "JMX bean for data migration")
public interface DataMigrationMBean {

    @ManagedOperation(description = "performs data migrtion")
    void doImportRentamaster2() throws Exception;

    @ManagedOperation(description = "data migration for ipcs")
    void doImportParteIpcs() throws Exception;

    @ManagedOperation(description="data migration for DocumentacionInquilino")
    public void doImportParteDocumentacionesInquilinos() throws Exception;


}