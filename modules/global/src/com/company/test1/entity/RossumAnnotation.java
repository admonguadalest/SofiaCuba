package com.company.test1.entity;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

@MetaClass(name = "test1_RossumAnnotation")
public class RossumAnnotation extends BaseUuidEntity {
    private static final long serialVersionUID = 3531652001822465792L;

    @MetaProperty
    private String basicInformation_documentId;

    @MetaProperty
    private String basicInformation_purchaseOrderNumber;

    @MetaProperty
    private String basicInformationIssueDate;

    @MetaProperty
    private String basicInformation_dueDate;

    @MetaProperty
    private String paymentInstructions_accountNumber;

    @MetaProperty
    private String paymentInstructions_bankCode;

    @MetaProperty
    private String paymentInstructions_iban;

    @MetaProperty
    private String totals_subtotal;

    @MetaProperty
    private String totals_amountDue;

    @MetaProperty
    private String totals_totalTax;

    @MetaProperty
    private String totals_currency;

    @MetaProperty
    private String totals_taxDetails;

    @MetaProperty
    private String vendorCustomer_vendorName;

    @MetaProperty
    private String vendorCustomer_vendorAddress;

    @MetaProperty
    private String vendorCustomer_customerName;

    @MetaProperty
    private String vendorName_customerAddress;

    @MetaProperty
    private String otherNotes;

    @MetaProperty
    private Integer documentId;

    @MetaProperty
    private String originalDocumentUrl;

    public void setTotals_subtotal(String totals_subtotal) {
        this.totals_subtotal = totals_subtotal;
    }

    public String getTotals_subtotal() {
        return totals_subtotal;
    }

    public void setTotals_amountDue(String totals_amountDue) {
        this.totals_amountDue = totals_amountDue;
    }

    public String getTotals_amountDue() {
        return totals_amountDue;
    }

    public void setTotals_totalTax(String totals_totalTax) {
        this.totals_totalTax = totals_totalTax;
    }

    public String getTotals_totalTax() {
        return totals_totalTax;
    }

    public String getOriginalDocumentUrl() {
        return originalDocumentUrl;
    }

    public void setOriginalDocumentUrl(String originalDocumentUrl) {
        this.originalDocumentUrl = originalDocumentUrl;
    }

    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }

    public String getOtherNotes() {
        return otherNotes;
    }

    public void setOtherNotes(String otherNotes) {
        this.otherNotes = otherNotes;
    }

    public String getVendorName_customerAddress() {
        return vendorName_customerAddress;
    }

    public void setVendorName_customerAddress(String vendorName_customerAddress) {
        this.vendorName_customerAddress = vendorName_customerAddress;
    }

    public String getVendorCustomer_customerName() {
        return vendorCustomer_customerName;
    }

    public void setVendorCustomer_customerName(String vendorCustomer_customerName) {
        this.vendorCustomer_customerName = vendorCustomer_customerName;
    }

    public String getVendorCustomer_vendorAddress() {
        return vendorCustomer_vendorAddress;
    }

    public void setVendorCustomer_vendorAddress(String vendorCustomer_vendorAddress) {
        this.vendorCustomer_vendorAddress = vendorCustomer_vendorAddress;
    }

    public String getVendorCustomer_vendorName() {
        return vendorCustomer_vendorName;
    }

    public void setVendorCustomer_vendorName(String vendorCustomer_vendorName) {
        this.vendorCustomer_vendorName = vendorCustomer_vendorName;
    }

    public String getTotals_taxDetails() {
        return totals_taxDetails;
    }

    public void setTotals_taxDetails(String totals_taxDetails) {
        this.totals_taxDetails = totals_taxDetails;
    }

    public String getTotals_currency() {
        return totals_currency;
    }

    public void setTotals_currency(String totals_currency) {
        this.totals_currency = totals_currency;
    }

    public String getPaymentInstructions_iban() {
        return paymentInstructions_iban;
    }

    public void setPaymentInstructions_iban(String paymentInstructions_iban) {
        this.paymentInstructions_iban = paymentInstructions_iban;
    }

    public String getPaymentInstructions_bankCode() {
        return paymentInstructions_bankCode;
    }

    public void setPaymentInstructions_bankCode(String paymentInstructions_bankCode) {
        this.paymentInstructions_bankCode = paymentInstructions_bankCode;
    }

    public String getPaymentInstructions_accountNumber() {
        return paymentInstructions_accountNumber;
    }

    public void setPaymentInstructions_accountNumber(String paymentInstructions_accountNumber) {
        this.paymentInstructions_accountNumber = paymentInstructions_accountNumber;
    }

    public String getBasicInformation_dueDate() {
        return basicInformation_dueDate;
    }

    public void setBasicInformation_dueDate(String basicInformation_dueDate) {
        this.basicInformation_dueDate = basicInformation_dueDate;
    }

    public String getBasicInformationIssueDate() {
        return basicInformationIssueDate;
    }

    public void setBasicInformationIssueDate(String basicInformationIssueDate) {
        this.basicInformationIssueDate = basicInformationIssueDate;
    }

    public String getBasicInformation_purchaseOrderNumber() {
        return basicInformation_purchaseOrderNumber;
    }

    public void setBasicInformation_purchaseOrderNumber(String basicInformation_purchaseOrderNumber) {
        this.basicInformation_purchaseOrderNumber = basicInformation_purchaseOrderNumber;
    }

    public String getBasicInformation_documentId() {
        return basicInformation_documentId;
    }

    public void setBasicInformation_documentId(String basicInformation_documentId) {
        this.basicInformation_documentId = basicInformation_documentId;
    }
}