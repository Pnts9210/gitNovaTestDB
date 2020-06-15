public class MailgunEmailClient implements EmailClient {
    @Override
    public void sendInvoice(Invoice invoice) {
        System.out.println("Sending invoice to " + invoice.getEmail());
    }

    @Override
    public void sendWelcomeEmail(String emailAddress) {

    }
}
