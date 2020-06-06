import java.util.ArrayList;
import java.util.HashMap;

public class Customer {

    private String firstName;
    private String lastName;
    //applicants
    private int applicantID;
    //applications
    private ArrayList<Integer> applicationsIDs = new ArrayList<>();
    //credit_information
    private HashMap<Integer, Integer> informationAndApplicationIDs = new HashMap<>();
    //offers
    private HashMap<Integer,Integer> offerAndApplicationIDs = new HashMap<>();
    //bookings
    private HashMap<Integer,Integer> bookingAndOfferIDs = new HashMap<>();
    //customer
    private ArrayList<Integer> customerID = new ArrayList<>();
    //invoices
    private HashMap<Integer,Integer> invoiceAndBookingId = new HashMap<>();
    //TRANSACTIONS
    private HashMap<Integer,Integer> transactionAndCustomerIDs = new HashMap<>();
    private HashMap<Integer,Integer> transactionAndBookingIDs = new HashMap<>();

    public Customer(String firstName, String lastName, int applicantID, ArrayList<Integer> applicationsIDs, HashMap<Integer, Integer> informationAndApplicationIDs, HashMap<Integer, Integer> offerAndApplicationIDs, HashMap<Integer, Integer> bookingAndOfferIDs, ArrayList<Integer> customerID, HashMap<Integer, Integer> invoiceAndBookingId, HashMap<Integer, Integer> transactionAndCustomerIDs, HashMap<Integer, Integer> transactionAndBookingIDs) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.applicantID = applicantID;
        this.applicationsIDs = applicationsIDs;
        this.informationAndApplicationIDs = informationAndApplicationIDs;
        this.offerAndApplicationIDs = offerAndApplicationIDs;
        this.bookingAndOfferIDs = bookingAndOfferIDs;
        this.customerID = customerID;
        this.invoiceAndBookingId = invoiceAndBookingId;
        this.transactionAndCustomerIDs = transactionAndCustomerIDs;
        this.transactionAndBookingIDs = transactionAndBookingIDs;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", applicantID=" + applicantID +
                ", applicationsIDs=" + applicationsIDs +
                ", informationAndApplicationIDs=" + informationAndApplicationIDs +
                ", offerAndApplicationIDs=" + offerAndApplicationIDs +
                ", bookingAndOfferIDs=" + bookingAndOfferIDs +
                ", customerID=" + customerID +
                ", invoiceAndBookingId=" + invoiceAndBookingId +
                ", transactionAndCustomerIDs=" + transactionAndCustomerIDs +
                ", transactionAndBookingIDs=" + transactionAndBookingIDs +
                "\n";
    }
}
