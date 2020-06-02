import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.sql.Timestamp;

public class Metoder {

    //ArrayList<User> lateTransactionArr = new ArrayList<>();
    Map<Integer, Integer> transactionsMap = new HashMap<>();
    Map<Integer, Integer> overdueInvoicesMap = new HashMap<>();

    private void setHashMaps() throws SQLException {

        Connection con = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");

        try {
            ResultSet resultSet = con.createStatement().executeQuery("SELECT * FROM nova_test_schema.transactions");
            while (resultSet.next()) {
                int transactionAmount = resultSet.getInt("transaction_amount");
                int customerId = resultSet.getInt("customer_id");

                if (transactionsMap.get(customerId) == null)
                    transactionsMap.put(customerId, transactionAmount);
                else {
                    int allTransactionAmount = transactionsMap.get(customerId);
                    allTransactionAmount += transactionAmount;
                    transactionsMap.put(customerId, allTransactionAmount);
                }
            }
            //*************Går igenom alla invoices och sammanfattar summan under samma customer_id i en hashMao************************
            resultSet = con.createStatement().executeQuery("SELECT * FROM nova_test_schema.invoices");
            int customerId = 0;
            while (resultSet.next()) {
                Timestamp dueDate = resultSet.getTimestamp("due_date");
                Date date = new Date();
                int valueHolder = resultSet.getInt("booking_id");
                int invoiceAmount = resultSet.getInt("total_amount");
                if(dueDate.before(new Timestamp(date.getTime()))) {
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

                    if (overdueInvoicesMap.get(customerId) == null)
                        overdueInvoicesMap.put(customerId, invoiceAmount);
                    else {
                        int allInvoiceAmount = overdueInvoicesMap.get(customerId);
                        allInvoiceAmount += invoiceAmount;
                        overdueInvoicesMap.put(customerId, allInvoiceAmount);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        } finally {
            con.close();
            System.out.println("Transactions: " + transactionsMap);
            System.out.println("Overdue Invoice: " + overdueInvoicesMap);
        }
    }

    public void getOverduePay() throws SQLException{

        setHashMaps();

        for (Map.Entry overDueMap : overdueInvoicesMap.entrySet()) {
            int customerId = (int) overDueMap.getKey();
            int invoiceAmount = (int) overDueMap.getValue();
            int transAmount = transactionsMap.get(customerId);
            if(invoiceAmount > transAmount)
                System.out.println(customerId);
        }


    }



}




