package org.example.Assignment;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class FilterInvoiceTest {
    @Test
    void filterInvoiceTest() {
        // Mocking the database and DAO
        Database dbMock = mock(Database.class);
        QueryInvoicesDAO daoMock = mock(QueryInvoicesDAO.class);

        // Setting up the mock to return specific data
        when(daoMock.all()).thenReturn(Arrays.asList(new Invoice("Customer1", 50), new Invoice("Customer2", 30)));

        // Injecting mocked database and DAO into FilterInvoice
        FilterInvoice filter = new FilterInvoice(dbMock, daoMock);
        List<Invoice> result = filter.lowValueInvoices();

        // Assertions to validate the correct filtering of invoices
        assertNotNull(result, "The result should not be null.");
        assertFalse(result.isEmpty(), "The result should not be empty.");
        assertEquals(2, result.size(), "There should be exactly two invoices with value less than 100.");
        assertTrue(result.stream().allMatch(invoice -> invoice.getValue() < 100), "All invoices should have a value less than 100.");
    }
}


