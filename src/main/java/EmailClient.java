import java.io.IOException;

public interface EmailClient {
    int sendInvoice(Invoice invoice) throws IOException;
    void sendWelcomeEmail(String emailAddress) throws IOException;
}
