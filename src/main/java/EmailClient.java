public interface EmailClient {
    void sendInvoice(Invoice invoice);
    void sendWelcomeEmail(String emailAddress);
}
