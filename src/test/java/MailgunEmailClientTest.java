import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MailgunEmailClientTest {


    @Test
    public void testWelcomeEmail() throws IOException {
        MailgunEmailClient mailgunEmailClient = new MailgunEmailClient("7c2479561394403c9469a273f693a92a-baa55c84-3af23d3b");
        mailgunEmailClient.sendWelcomeEmail("p.norderheim@gmail.com");
    }

    @Test
    public void test1() throws IOException, SQLException {
        Metoder metoder = new Metoder();
        List<Invoice> invoices = metoder.makeLateInvoiceList();

        //FACTORY PATTERN
        EmailClientFactory emailClientFactory = new EmailClientFactory();
        EmailClient emailClient = emailClientFactory.createEmailClient();
        int code = emailClient.sendInvoice(invoices.get(0));
        assertEquals(200, code);
    }

}
