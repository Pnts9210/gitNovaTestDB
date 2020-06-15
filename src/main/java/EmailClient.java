public class EmailClient {

    public void sendInvoice(Invoice invoice) {
        // HTTP call goes here
        System.out.println("Sending invoice to: " + invoice.getEmail());
    }
    public void sendNewCustomerEmail() {
        
    }
}
