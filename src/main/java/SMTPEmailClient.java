public class SMTPEmailClient implements EmailClient {
    @Override
    public void sendInvoice(Invoice invoice) {
        System.out.println("Sending SMTP");
    }

    @Override
    public void sendWelcomeEmail(String emailAddress) {

    }
}
