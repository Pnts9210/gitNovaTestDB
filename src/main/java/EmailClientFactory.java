public class EmailClientFactory {

    public EmailClient createEmailClient() {
        return new SMTPEmailClient();
    }
}
