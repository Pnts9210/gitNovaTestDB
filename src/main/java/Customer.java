import java.util.ArrayList;
import java.util.HashMap;

public class Customer {

    private String firstName;
    private String lastName;
    //applicants
    private int applicantID;
    private String email;
    //applications
    private ArrayList<Integer> applicationsIDs = new ArrayList<>();
    //credit_information
    private HashMap<Integer, Integer> informationAndApplicationIDs = new HashMap<>();
    //offers
    private HashMap<Integer,Integer> offerAndApplicationIDs = new HashMap<>();
    //bookings
    private HashMap<Integer,Integer> bookingAndOfferIDs = new HashMap<>();
    //customer
    private int customerID;
    //invoices
    private HashMap<Integer,Integer> invoiceAndBookingId = new HashMap<>();
    //TRANSACTIONS
    private HashMap<Integer,Integer> transactionAndCustomerIDs = new HashMap<>();
    private HashMap<Integer,Integer> transactionAndBookingIDs = new HashMap<>();
    private HashMap<Integer,Integer> transactionAndInvoiceIDs = new HashMap<>();
    //extra
    double latePayAmount;
    double lateCharge;
    double lateInterest;
    double invoiceFee;

    public Customer(String firstName, String lastName, int applicantID, String email, ArrayList<Integer> applicationsIDs, HashMap<Integer, Integer> informationAndApplicationIDs, HashMap<Integer, Integer> offerAndApplicationIDs, HashMap<Integer, Integer> bookingAndOfferIDs, int customerID, HashMap<Integer, Integer> invoiceAndBookingId, HashMap<Integer, Integer> transactionAndCustomerIDs, HashMap<Integer, Integer> transactionAndBookingIDs, HashMap<Integer, Integer> transactionAndInvoiceIDs) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.applicantID = applicantID;
        this.email = email.toLowerCase();
        this.applicationsIDs = applicationsIDs;
        this.informationAndApplicationIDs = informationAndApplicationIDs;
        this.offerAndApplicationIDs = offerAndApplicationIDs;
        this.bookingAndOfferIDs = bookingAndOfferIDs;
        this.customerID = customerID;
        this.invoiceAndBookingId = invoiceAndBookingId;
        this.transactionAndCustomerIDs = transactionAndCustomerIDs;
        this.transactionAndBookingIDs = transactionAndBookingIDs;
        this.transactionAndInvoiceIDs = transactionAndInvoiceIDs;
    }

    public double getLatePayAmount() {
        return latePayAmount;
    }

    public void setLatePayAmount(double latePayAmount) {
        this.latePayAmount = latePayAmount;
    }

    public double getLateCharge() {
        return lateCharge;
    }

    public void setLateCharge(double lateCharge) {
        this.lateCharge = lateCharge;
    }

    public double getLateInterest() {
        return lateInterest;
    }

    public void setLateInterest(double lateInterest) {
        this.lateInterest = lateInterest;
    }

    public double getInvoiceFee() {
        return invoiceFee;
    }

    public void setInvoiceFee(double invoiceFee) {
        this.invoiceFee = invoiceFee;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getApplicantID() {
        return applicantID;
    }

    public ArrayList<Integer> getApplicationsIDs() {
        return applicationsIDs;
    }

    public HashMap<Integer, Integer> getInformationAndApplicationIDs() {
        return informationAndApplicationIDs;
    }

    public HashMap<Integer, Integer> getOfferAndApplicationIDs() {
        return offerAndApplicationIDs;
    }

    public HashMap<Integer, Integer> getBookingAndOfferIDs() {
        return bookingAndOfferIDs;
    }

    public int getCustomerID() {
        return customerID;
    }

    public HashMap<Integer, Integer> getInvoiceAndBookingId() {
        return invoiceAndBookingId;
    }

    public HashMap<Integer, Integer> getTransactionAndCustomerIDs() {
        return transactionAndCustomerIDs;
    }

    public HashMap<Integer, Integer> getTransactionAndBookingIDs() {
        return transactionAndBookingIDs;
    }

    public HashMap<Integer, Integer> getTransactionAndInvoiceIDs() {
        return transactionAndInvoiceIDs;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", applicantID=" + applicantID +
                ", email='" + email + '\'' +
                ", applicationsIDs=" + applicationsIDs +
                ", informationAndApplicationIDs=" + informationAndApplicationIDs +
                ", offerAndApplicationIDs=" + offerAndApplicationIDs +
                ", bookingAndOfferIDs=" + bookingAndOfferIDs +
                ", customerID=" + customerID +
                ", invoiceAndBookingId=" + invoiceAndBookingId +
                ", transactionAndCustomerIDs=" + transactionAndCustomerIDs +
                ", transactionAndBookingIDs=" + transactionAndBookingIDs +
                ", transactionAndInvoiceIDs=" + transactionAndInvoiceIDs +
                ", latePayAmount=" + latePayAmount +
                ", lateCharge=" + lateCharge +
                ", lateInterest=" + lateInterest +
                ", invoiceFee=" + invoiceFee +
                "\n";
    }
}
