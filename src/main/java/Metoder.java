import java.io.IOException;
import java.sql.*;
import java.util.Date;
import java.util.*;

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

    public ArrayList<Customer> createCustomersObjects() throws SQLException {

        ArrayList<Customer> allCustomers = new ArrayList<>();

        Connection con = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");

        try {

            ResultSet resultSet1 = con.createStatement().executeQuery("SELECT * FROM nova_test_schema.applicants");
            while (resultSet1.next()) {

                //APPLICANT
                int applicantID;
                String firstName;
                String lastName;
                String email;

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
                HashMap<Integer, Integer> transactionAndInvoiceIDs = new HashMap<>();

                    firstName = resultSet1.getString("first_name");
                    lastName = resultSet1.getString("last_name");
                    applicantID = resultSet1.getInt("applicant_id");
                    email = resultSet1.getString("email");



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
                                int invoiceID = resultSetTransactions.getInt("invoice_id");
                                transactionAndBookingIDs.put(transactionID, bookingID);
                                transactionAndCustomerIDs.put(transactionID, customerID);
                                transactionAndInvoiceIDs.put(transactionID, invoiceID);
                            }
                        }
                        ResultSet resultSetCustomer = con.createStatement().executeQuery("SELECT * FROM nova_test_schema.customers WHERE applicant_id =" + applicantID+";");
                        while(resultSetCustomer.next()){
                            customerID = resultSetCustomer.getInt("customer_id");
                        }
                    }
                    allCustomers.add(new Customer(firstName,lastName,applicantID,email,applicationsIDs,informationAndApplicationIDs,offerAndApplicationIDs,bookingAndOfferIDs, customerID,invoiceAndBookingId,transactionAndCustomerIDs,transactionAndBookingIDs,transactionAndInvoiceIDs));
            }
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        } finally {
            con.close();
        }
        return allCustomers;
    }

    public Customer createCustomerObject(int customerId) throws SQLException {
        String firstName = "";
        String lastName = "";
        int applicantID = 0;
        String email = "";

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
        HashMap<Integer,Integer> transactionAndInvoiceIDs = new HashMap<>();


        Connection con = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");

        try {

            PreparedStatement statement = con.prepareStatement("SELECT * FROM nova_test_schema.customers WHERE customer_id = ?");
            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            applicantID = resultSet.getInt("applicant_id");


            statement = con.prepareStatement("SELECT * FROM nova_test_schema.applicants WHERE applicant_id = ?");
            statement.setInt(1, applicantID);
            ResultSet resultSet1 = statement.executeQuery();
            resultSet1.next();
            firstName = resultSet1.getString("first_name");
            lastName = resultSet1.getString("last_name");
            applicantID = resultSet1.getInt("applicant_id");
            email = resultSet1.getString("email");

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
                            int invoiceID = resultSetTransactions.getInt("invoice_id");
                            transactionAndBookingIDs.put(transactionID, bookingID);
                            transactionAndCustomerIDs.put(transactionID, customerID);
                            transactionAndInvoiceIDs.put(transactionID, invoiceID);
                        }
                    }
                    ResultSet resultSetCustomer = con.createStatement().executeQuery("SELECT * FROM nova_test_schema.customers WHERE applicant_id =" + applicantID+";");
                    while(resultSetCustomer.next()){
                        customerID = resultSetCustomer.getInt("customer_id");
                    }
                }
                        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        } finally {
            con.close();
        }
        return new Customer(firstName,lastName,applicantID,email,applicationsIDs,informationAndApplicationIDs,offerAndApplicationIDs,bookingAndOfferIDs, customerID,invoiceAndBookingId,transactionAndCustomerIDs,transactionAndBookingIDs,transactionAndInvoiceIDs);
    }

    @Deprecated
    public List<Customer> makeLateInvoiceOLD() throws SQLException{

        Connection con = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");

        List<Integer> overdueCustomerIDs = getOverdueCustomerId();
        List<Integer> invoiceList = new ArrayList<>();
        List<Integer> payedInvoicesList = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();


        try {
            con.setAutoCommit(false);

            for (Integer customerId : overdueCustomerIDs) {
                Customer customer = createCustomerObject(customerId);
                double totalOverdueAmount = 0;

                // hämta alla invoiceIDs.
                HashMap<Integer,Integer>  invoiceAndBookingId = customer.getInvoiceAndBookingId();
                for(Map.Entry<Integer, Integer> entry : invoiceAndBookingId.entrySet()) {
                  invoiceList.add(entry.getKey());
                }
                //hämta alla transaction. invoice ids.
                HashMap<Integer, Integer> transactionAndInvoiceId = customer.getTransactionAndInvoiceIDs();
                for(Map.Entry<Integer, Integer> entry : transactionAndInvoiceId.entrySet()) {
                    payedInvoicesList.add(entry.getValue());
                }

                //invoicelist innehåller nu all overdue invoiceID som inte betalats
                invoiceList.removeAll(payedInvoicesList);



                for(int i = 0; i < invoiceList.size(); i++) {
                    PreparedStatement statement = con.prepareStatement("SELECT * FROM nova_test_schema.invoices WHERE invoice_id = ?");
                    statement.setInt(1, invoiceList.get(i));
                    ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()) {
                    /*
                    System.out.println(resultSet.getInt("amortization_amount"));
                    System.out.println(resultSet.getDouble("interest_amount"));
                    System.out.println(resultSet.getDouble("late_charge"));
                    System.out.println(resultSet.getDouble("invoice_fee"));
                    //transactionsList.add(resultSet.getInt("invoice_id"));
                    */
                        totalOverdueAmount += resultSet.getDouble("amortization_amount");
                    }
                }
                //customer.setLatePayAmount(totalOverdueAmount);
                customers.add(customer);
            }
            con.commit();

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            con.rollback();
            System.out.println("Rollback!!1");
        } finally {
            con.setAutoCommit(true);
            con.close();

        }
        return customers;
    }

    public List<Invoice> makeLateInvoiceList() throws SQLException{

        Connection con = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");

        List<Integer> overdueCustomerIDs = getOverdueCustomerId();
        List<Integer> invoiceList = new ArrayList<>();
        List<Integer> payedInvoicesList = new ArrayList<>();
        List<Invoice> newInvoices = new ArrayList<>();

        try {
            con.setAutoCommit(false);

            for (Integer customerId : overdueCustomerIDs) {
                Customer customer = createCustomerObject(customerId);
                double totalOverdueAmount = 0;

                // hämta alla invoiceIDs.
                HashMap<Integer,Integer>  invoiceAndBookingId = customer.getInvoiceAndBookingId();
                for(Map.Entry<Integer, Integer> entry : invoiceAndBookingId.entrySet()) {
                    invoiceList.add(entry.getKey());
                }
                //hämta alla transaction. invoice ids.
                HashMap<Integer, Integer> transactionAndInvoiceId = customer.getTransactionAndInvoiceIDs();
                for(Map.Entry<Integer, Integer> entry : transactionAndInvoiceId.entrySet()) {
                    payedInvoicesList.add(entry.getValue());
                }

                //invoicelist innehåller nu all overdue invoiceID som inte betalats
                //invoiceList.removeAll(payedInvoicesList);

                for(int i = 0; i < invoiceList.size(); i++) {
                    PreparedStatement statement = con.prepareStatement("SELECT * FROM nova_test_schema.invoices WHERE invoice_id = ?");
                    statement.setInt(1, invoiceList.get(i));
                    ResultSet resultSet = statement.executeQuery();
                    resultSet.next();

                    int invoiceAmount = resultSet.getInt("amortization_amount");
                    double interestAmount = resultSet.getDouble("interest_amount");
                    double totalAmount = resultSet.getDouble("total_amount");
                    double amortizationAmount = resultSet.getDouble("amortization_amount");
                    invoiceAmount += resultSet.getDouble("invoice_fee");
                    int invoiceID = resultSet.getInt("invoice_id");


                    statement = con.prepareStatement("SELECT * FROM nova_test_schema.transactions WHERE invoice_id = ?");
                    statement.setInt(1, invoiceList.get(i));
                    ResultSet resultSet2 = statement.executeQuery();

                    int shortage;

                    if(resultSet2.next()) {
                        int transactionAmount = resultSet2.getInt("transaction_amount");

                        //För lite batalt
                        if(transactionAmount != invoiceAmount) {
                            //Invoice objekt
                            shortage = invoiceAmount - transactionAmount;
                            Customer cus = createCustomerObject(customerId);
                            String firstName = cus.getFirstName();
                            String lastName = cus.getLastName();
                            String email = cus.getEmail();
                            int previousInvoice = invoiceList.get(i);
                            int bookingID = cus.getInvoiceAndBookingId().get(previousInvoice);
                            Invoice newInvoice = new Invoice(firstName, lastName, email, customerId, bookingID, previousInvoice, shortage, totalAmount, amortizationAmount);
                            newInvoices.add(newInvoice);


                            //Sql databasen
                            /*
                            Date date = new Date();
                            Timestamp timeNow = new Timestamp(date.getTime());
                            Faker faker = new Faker(new Locale("sv-SE"));
                            long intocr = faker.number().numberBetween(10000000, 100000000);
                            String ocr = intocr + "";

                            try {
                                con.setAutoCommit(false);
                                PreparedStatement stmt = con.prepareStatement("INSERT INTO nova_test_schema.invoices" +
                                        "(" +
                                        "booking_id, " +
                                        "invoice_date, " +
                                        "ocr," +
                                        "total_amount," +
                                        "interest_amount," +
                                        "amortization_amount," +
                                        "late_charge," + //7
                                        "late_interest," + //8
                                        "previous_invoice," + //9
                                        "invoice_fee," + //10
                                        "due_date," +  //11
                                        "pdf_path" +//12
                                        ")" +
                                        "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");


                                stmt.setInt(1,bookingID);
                                stmt.setTimestamp(2, timeNow);
                                stmt.setString(3, ocr);
                                stmt.setDouble(4, totalAmount + shortage);
                                stmt.setDouble(5, interestAmount);
                                stmt.setDouble(6, amortizationAmount);
                                stmt.setDouble(7, newInvoice.getLateCharge());
                                stmt.setDouble(8, newInvoice.getLateInterest());
                                stmt.setInt(9, newInvoice.getPreviousInvoice());
                                stmt.setDouble(10, newInvoice.getInvoiceFee());
                                stmt.setTimestamp(11, newInvoice.getDueDate());
                                stmt.setString(12, "pathiPath!");
                                stmt.executeUpdate();

                                con.commit();

                            } catch (Exception e) {
                                System.out.println("Error:" + e.getMessage());
                                con.rollback();
                                System.out.println("Rollback!!2");
                            }
*/
                        }


                    }
                    else {   //Fakturan inte batld.

                        //Invoice objekt
                        shortage = invoiceAmount;
                        Customer cus = createCustomerObject(customerId);
                        String firstName = cus.getFirstName();
                        String lastName = cus.getLastName();
                        String email = cus.getEmail();
                        int previousInvoice = invoiceList.get(i);
                        int bookingID = cus.getInvoiceAndBookingId().get(previousInvoice);
                        Invoice newInvoice = new Invoice(firstName, lastName, email, customerId, bookingID, previousInvoice, shortage, totalAmount, amortizationAmount);
                        newInvoices.add(newInvoice);

                        //Sql databasen
                        /*
                        Date date = new Date();
                        Timestamp timeNow = new Timestamp(date.getTime());
                        Faker faker = new Faker(new Locale("sv-SE"));
                        long intocr = faker.number().numberBetween(10000000, 100000000);
                        String ocr = intocr + "";

                        try {
                            con.setAutoCommit(false);
                            PreparedStatement stmt = con.prepareStatement("INSERT INTO nova_test_schema.invoices" +
                                    "(" +
                                    "booking_id, " +
                                    "invoice_date, " +
                                    "ocr," +
                                    "total_amount," +
                                    "interest_amount," +
                                    "amortization_amount," +
                                    "late_charge," + //7
                                    "late_interest," + //8
                                    "previous_invoice," + //9
                                    "invoice_fee," + //10
                                    "due_date," +  //11
                                    "pdf_path" +//12
                                    ")" +
                                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");


                            stmt.setInt(1,bookingID);
                            stmt.setTimestamp(2, timeNow);
                            stmt.setString(3, ocr);
                            stmt.setDouble(4, totalAmount + shortage);
                            stmt.setDouble(5, interestAmount);
                            stmt.setDouble(6, amortizationAmount);
                            stmt.setDouble(7, newInvoice.getLateCharge());
                            stmt.setDouble(8, newInvoice.getLateInterest());
                            stmt.setInt(9, newInvoice.getPreviousInvoice());
                            stmt.setDouble(10, newInvoice.getInvoiceFee());
                            stmt.setTimestamp(11, newInvoice.getDueDate());
                            stmt.setString(12, "pathiPath!");
                            stmt.executeUpdate();

                            con.commit();

                        } catch (Exception e) {
                            System.out.println("Error:" + e.getMessage());
                            con.rollback();
                            System.out.println("Rollback!!3");
                        }
*/
                    }
                }
            }
            con.commit();

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            con.rollback();
            System.out.println("Rollback!!4");
        } finally {
            con.setAutoCommit(true);
            con.close();
        }
        return compileInvoiceListToOneInvoice(newInvoices);
    }

    public void sendLateInvoices() throws SQLException, IOException {

        List<Invoice> invoices = makeLateInvoiceList();
        System.out.println("invoice sent: " + invoices);

        for(int i = 0; i < invoices.size(); i++){
            //FACTORY PATTERN
            EmailClientFactory emailClientFactory = new EmailClientFactory();
            EmailClient emailClient = emailClientFactory.createEmailClient();
            emailClient.sendInvoice(invoices.get(i));
        }


    }

    public List<Invoice> compileInvoiceListToOneInvoice(List<Invoice> invoiceList){

        List<Invoice> compiledInvoiceList = new ArrayList<>();

        for (int i = 0; i < invoiceList.size(); i++) {
            String name =  invoiceList.get(i).getFirtsName() + " " + invoiceList.get(i).getLastaName();
            double latePayAmount = invoiceList.get(i).getLatePayAmount();
            boolean found = false;
            for (int j = 0; j < compiledInvoiceList.size(); j++) {
                if ((compiledInvoiceList.get(j).getFirtsName() + " " + compiledInvoiceList.get(j).getLastaName()).equals(name)) {
                    found = true;
                }
                if(found){
                    double alredyExictsingLatePayment = compiledInvoiceList.get(j).getLatePayAmount();
                    latePayAmount += alredyExictsingLatePayment;
                    compiledInvoiceList.get(j).setLatePayAmount(latePayAmount);
                    break;
                }
            }
            if(!found) {
                compiledInvoiceList.add(invoiceList.get(i));
            }
        }

        return compiledInvoiceList;

    }

    public void insertLateInvoiceInDB(List<Invoice> invoiceList){

    }








}




