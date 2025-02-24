package org.example.Assignment;

import java.util.ArrayList;
import java.util.List;

public class SAP_BasedInvoiceSender {
    private final FilterInvoice filter;
    private final SAP sap;

    public SAP_BasedInvoiceSender(FilterInvoice filter, SAP sap) {
        this.filter = filter;
        this.sap = sap;
    }

    public List<Invoice> sendLowValuedInvoices() {
        List<Invoice> lowValuedInvoices = filter.lowValueInvoices();
        List<Invoice> failedInvoices = new ArrayList<>();

        for (Invoice invoice : lowValuedInvoices) {
            try {
                sap.send(invoice);
            } catch (FailToSendSAPInvoiceException exception) {
                failedInvoices.add(invoice);
                System.out.println("SAP invoice failed: " + exception.getMessage());
            }
        }
        return failedInvoices;
    }
}


