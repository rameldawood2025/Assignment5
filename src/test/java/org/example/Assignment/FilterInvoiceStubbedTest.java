// Improved testability using stubs - Ramel Dawood

package org.example.Assignment;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Arrays;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class FilterInvoiceStubbedTest {
    @Test
    void filterInvoiceStubbedTest() {
        Database dbStub = mock(Database.class);
        QueryInvoicesDAO daoStub = mock(QueryInvoicesDAO.class);

        // Mocking the all() method to return a predictable result
        when(daoStub.all()).thenReturn(Arrays.asList(new Invoice("Customer1", 50), new Invoice("Customer2", 80)));

        FilterInvoice filter = new FilterInvoice(dbStub, daoStub);
        List<Invoice> result = filter.lowValueInvoices();
        assertNotNull(result);
        assertEquals(2, result.size()); // Expecting two invoices under 100 in value
    }
}
