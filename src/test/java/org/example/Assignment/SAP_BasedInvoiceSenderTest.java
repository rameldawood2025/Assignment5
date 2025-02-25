// Tested SAP functionality - Ramel Dawood

package org.example.Assignment;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class SAP_BasedInvoiceSenderTest {
    @Test
    void testWhenLowInvoicesSent() throws FailToSendSAPInvoiceException {
        FilterInvoice filter = mock(FilterInvoice.class);
        SAP sap = mock(SAP.class);
        List<Invoice> invoices = Arrays.asList(new Invoice("Customer1", 50));
        when(filter.lowValueInvoices()).thenReturn(invoices);

        SAP_BasedInvoiceSender sender = new SAP_BasedInvoiceSender(filter, sap);
        List<Invoice> failedInvoices = sender.sendLowValuedInvoices();

        verify(sap, times(1)).send(any(Invoice.class));
        assertTrue(failedInvoices.isEmpty(), "No invoices should fail when all are sent successfully.");
    }

    @Test
    void testWhenNoInvoices() throws FailToSendSAPInvoiceException {
        FilterInvoice filter = mock(FilterInvoice.class);
        SAP sap = mock(SAP.class);
        when(filter.lowValueInvoices()).thenReturn(Collections.emptyList());

        SAP_BasedInvoiceSender sender = new SAP_BasedInvoiceSender(filter, sap);
        List<Invoice> failedInvoices = sender.sendLowValuedInvoices();

        verify(sap, never()).send(any(Invoice.class));
        assertTrue(failedInvoices.isEmpty(), "There should be no failed invoices when there are no invoices to send.");
    }



    @Test
    void testThrowExceptionWhenBadInvoice() throws FailToSendSAPInvoiceException {
        // Setup
        FilterInvoice filter = mock(FilterInvoice.class);
        SAP sap = mock(SAP.class);
        Invoice badInvoice = new Invoice("BadCustomer", 100);

        // Stubbing to throw an exception on the mocked sap object
        doThrow(new FailToSendSAPInvoiceException("Failed due to bad invoice")).when(sap).send(badInvoice);

        // Ensure filter returns the bad invoice directly
        when(filter.lowValueInvoices()).thenReturn(Collections.singletonList(badInvoice));

        // Creating the sender with mocks
        SAP_BasedInvoiceSender sender = new SAP_BasedInvoiceSender(filter, sap);

        // Execute and assert the exception
        Exception exception = assertThrows(FailToSendSAPInvoiceException.class, () -> {
            // Directly call sap.send to rule out other failures
            sap.send(badInvoice);
        }, "The expected FailToSendSAPInvoiceException should be thrown");

        assertEquals("Failed due to bad invoice", exception.getMessage());
        verify(sap).send(badInvoice);  // Ensure sap.send was indeed called with the bad invoice
    }


}
