package com.company.test1.jmx;

import com.haulmont.cuba.core.sys.jmx.JmxBean;
import org.springframework.jmx.export.annotation.ManagedResource;
@JmxBean(module = "data", alias = "AjustesMBean")
@ManagedResource(description = "JMX bean for some settings")
public interface AjustesMBean {

    public void realizaAjustePoblarTitularDocumentosProveedor() throws Exception;
}