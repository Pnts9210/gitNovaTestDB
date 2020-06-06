import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Metoder {

    //ArrayList<Customer> customers = new ArrayList<>();
    Map<Integer, Integer> allTransactionsMap = new HashMap<>();
    Map<Integer, Integer> oldInvoicesSum = new HashMap<>();

    private void setHashMaps() throws SQLException {
        /**Metoden summerar alla transaktioner och alla utgågna invoices och sätter dom i varsin HashMap med customer id som key*/

        Connection con = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");

        try {
            ResultSet resultSet = con.createStatement().executeQuery("SELECT * FROM nova_test_schema.transactions");
            while (resultSet.next()) {
                int transaction = resultSet.getInt("transaction_amount");
                int customerId = resultSet.getInt("customer_id");

                if (allTransactionsMap.get(customerId) == null)
                    allTransactionsMap.put(customerId, transaction);
                else {
                    int allTransactionAmount = allTransactionsMap.get(customerId);
                    allTransactionAmount += transaction;
                    allTransactionsMap.put(customerId, allTransactionAmount);
                }
            }


            resultSet = con.createStatement().executeQuery("SELECT * FROM nova_test_schema.invoices");
            int customerId = 1;
            while (resultSet.next()) {
                Timestamp dueDate = resultSet.getTimestamp("due_date");
                Date date = new Date();
                int valueHolder = resultSet.getInt("booking_id");
                int invoice = 0;

                if(dueDate.before(new Timestamp(date.getTime()))) {
                    invoice += resultSet.getInt("amortization_amount") + resultSet.getInt("invoice_fee");
                    ResultSet resultSet2 = con.createStatement().executeQuery("SELECT offer_id FROM nova_test_schema.bookings WHERE booking_id =" + valueHolder);
                    while (resultSet2.next())
                        valueHolder = resultSet2.getInt("offer_id");
                    resultSet2 = con.createStatement().executeQuery("SELECT application_id FROM nova_test_schema.offers WHERE offer_id = " + valueHolder);
                    while (resultSet2.next())
                        valueHolder = resultSet2.getInt("application_id");
                    resultSet2 = con.createStatement().executeQuery("SELECT applicant_id FROM nova_test_schema.applications WHERE application_id = " + valueHolder);
                    while (resultSet2.next())
                        valueHolder = resultSet2.getInt("applicant_id");
                    resultSet2 = con.createStatement().executeQuery("SELECT customer_id FROM nova_test_schema.customers WHERE applicant_id = " + valueHolder);
                    while (resultSet2.next())
                        customerId = resultSet2.getInt("customer_id");

                    if (oldInvoicesSum.get(customerId) == null)
                        oldInvoicesSum.put(customerId, invoice);
                    else {
                        int allInvoiceAmount = oldInvoicesSum.get(customerId);
                        allInvoiceAmount += invoice;
                        oldInvoicesSum.put(customerId, allInvoiceAmount);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        } finally {
            con.close();
            System.out.println("Transactions: " + allTransactionsMap);
            System.out.println("Old Invoice: " + oldInvoicesSum);
        }
    }

    /**
     Metoden retunerar en ArrayList men alla customer id där summan av utgångna invoices är mindre än deras transactions.
     * @return ArrayList<Integer>
     * @throws SQLException
     */
    public ArrayList<Integer> getOverdueCustomerId() throws SQLException{
        setHashMaps();
        ArrayList<Integer> lateCustomer_id = new ArrayList<>();

        for (Map.Entry overDueMap : oldInvoicesSum.entrySet()) {
            int customerId = (int) overDueMap.getKey();
            int invoiceAmount = (int) overDueMap.getValue();
            int transAmount = allTransactionsMap.get(customerId);
            if(invoiceAmount > transAmount) {
                lateCustomer_id.add(customerId);
            }
        }
        return lateCustomer_id;
    }

    public ArrayList<Customer> createCustomers() throws SQLException {

        ArrayList<Customer> allCustomers = new ArrayList<>();

        Connection con = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");

        try {

            ResultSet resultSet1 = con.createStatement().executeQuery("SELECT * FROM nova_test_schema.applicants");
            while (resultSet1.next()) {

                String firstName;
                String lastName;
                //APPLICANT
                int applicantID;

                //APPLICATIONS
                int applicationID = 0;
                ArrayList<Integer> applicationsIDs = new ArrayList<>();

                //CREDIT_INFORMATION
                HashMap<Integer, Integer> informationAndApplicationIDs = new HashMap<>();

                //OFFERS
                int offerId = 0;
                HashMap<Integer,Integer> offerAndApplicationIDs = new HashMap<>();

                //BOOKINGS
                int bookingID = 0;
                HashMap<Integer,Integer> bookingAndOfferIDs = new HashMap<>();

                //CUSTOMERS
                int customerID = 0;

                //invoices
                HashMap<Integer,Integer> invoiceAndBookingId = new HashMap<>();

                //TRANSACTIONS
                HashMap<Integer,Integer> transactionAndCustomerIDs = new HashMap<>();
                HashMap<Integer,Integer> transactionAndBookingIDs = new HashMap<>();

                    firstName = resultSet1.getString("first_name");
                    lastName = resultSet1.getString("last_name");
                    applicantID = resultSet1.getInt("applicant_id");



                    ResultSet resultSetApplication = con.createStatement().executeQuery("SELECT * FROM nova_test_schema.applications WHERE applicant_id =" + applicantID +";");
                    while(resultSetApplication.next()){
                        applicationID = resultSetApplication.getInt("application_id");
                        applicationsIDs.add(applicationID);
                        ResultSet resultSetInformation = con.createStatement().executeQuery("SELECT * FROM nova_test_schema.credit_information WHERE application_id =" + applicationID+";");
                        while(resultSetInformation.next()){
                            int informationID = resultSetInformation.getInt("information_id");
                            informationAndApplicationIDs.put(informationID,applicationID);
                        }
                        ResultSet resultSetOffers = con.createStatement().executeQuery("SELECT * FROM nova_test_schema.offers WHERE application_id =" + applicationID+";");
                        while(resultSetOffers.next()){
                            offerId = resultSetOffers.getInt("offer_id");
                            offerAndApplicationIDs.put(offerId, applicantID);
                        }
                        ResultSet resultSetBookings = con.createStatement().executeQuery("SELECT * FROM nova_test_schema.bookings WHERE offer_id =" + offerId+";");
                        while(resultSetBookings.next()){
                            bookingID = resultSetBookings.getInt("booking_id");
                            int offerID = resultSetBookings.getInt("offer_id");
                            bookingAndOfferIDs.put(bookingID, offerID);
                            ResultSet resultSetInvoices = con.createStatement().executeQuery("SELECT * FROM nova_test_schema.invoices WHERE booking_id =" + bookingID+";");
                            while(resultSetInvoices.next()){
                                int invoiceID = resultSetInvoices.getInt("invoice_id");
                                invoiceAndBookingId.put(invoiceID, bookingID);
                            }
                            ResultSet resultSetTransactions = con.createStatement().executeQuery("SELECT * FROM nova_test_schema.transactions WHERE booking_id =" + bookingID+";");
                            while(resultSetTransactions.next()){
                                int transactionID = resultSetTransactions.getInt("transaction_id");
                                customerID = resultSetTransactions.getInt("customer_id");
                                transactionAndBookingIDs.put(transactionID, bookingID);
                                transactionAndCustomerIDs.put(transactionID, customerID);
                            }
                        }
                        ResultSet resultSetCustomer = con.createStatement().executeQuery("SELECT * FROM nova_test_schema.customers WHERE applicant_id =" + applicantID+";");
                        while(resultSetCustomer.next()){
                            customerID = resultSetCustomer.getInt("customer_id");
                        }
                    }
                    allCustomers.add(new Customer(firstName,lastName,applicantID,applicationsIDs,informationAndApplicationIDs,offerAndApplicationIDs,bookingAndOfferIDs, customerID,invoiceAndBookingId,transactionAndCustomerIDs,transactionAndBookingIDs));
            }
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        } finally {
            con.close();
        }
        return allCustomers;
    }








}




