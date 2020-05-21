import java.sql.*;

import static org.postgresql.jdbc.EscapedFunctions.NOW;

public class Init {
    private final String url = "jdbc:postgresql:nova_test_db";
    private final String user = "postgres";
    private final String password = "myPassword";



    public void createSchema() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");

        try {
            con.setAutoCommit(false);
            PreparedStatement stmt = con.prepareStatement("DROP SCHEMA nova_test_schema CASCADE");
            stmt.executeUpdate();
            con.commit();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("failed dropping schema, probably because it didn't exist");
        }

        try {
            con.setAutoCommit(false);
            PreparedStatement stmt = con.prepareStatement("CREATE SCHEMA nova_test_schema");
            stmt.executeUpdate();
            con.commit();

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            con.rollback();
            System.out.println("Rollback!!");
        } finally {
            con.setAutoCommit(true);
            con.close();
        }
    }



    public void createTableApplicants() throws SQLException{
        try (
                Connection connection = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");
        ) {

            PreparedStatement statement = connection.prepareStatement("CREATE TABLE nova_test_schema.applicants (\n" +
                    "      applicant_id serial PRIMARY KEY," +
                    "      hashed_ssn text NOT NULL," +
                    "      applicant_group_id varchar(80),\n" +
                    "      first_name text NOT NULL,\n" +
                    "      last_name text NOT NULL,\n" +
                    "      ssn varchar(80) NOT NULL,\n" +
                    "      email text NOT NULL,\n" +
                    "      phone_number varchar(80) NOT NULL,\n" +
                    "      address text NOT NULL,\n" +
                    "      dek text NOT NULL\n" +
                    "    )");
            statement.executeUpdate();
            connection.prepareStatement("CREATE UNIQUE INDEX applicant_hashed_ssn ON nova_test_schema.applicants (hashed_ssn)");
            statement.executeUpdate();

            System.out.println("Table successfully created");

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }

    }

    public void createTableApplications(){
        try (
                Connection connection = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");
        ) {

            PreparedStatement statement = connection.prepareStatement("CREATE TABLE nova_test_schema.applications\n" +
                    "    (\n" +
                    "      application_id serial PRIMARY KEY,\n" +
                    "      applicant_id serial references nova_test_schema.applicants(applicant_id),\n" +
                    "      timestamp timestamp NOT NULL,\n" +
                    "      amount integer NOT NULL,\n" +
                    "      desired_monthly_payment integer,\n" +
                    "      term integer\n" +
                    "    )");
            statement.executeUpdate();

            System.out.println("Table successfully created");

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }

    }

    public void createTableOffers(){
        try (
                Connection connection = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");
        ) {

            PreparedStatement statement = connection.prepareStatement("CREATE TABLE nova_test_schema.offers\n" +
                    "    (\n" +
                    "      offer_id serial PRIMARY KEY,\n" +
                    "      application_id serial references nova_test_schema.applications(application_id),\n" +
                    "      offered_credit_amount integer NOT NULL,\n" +
                    "      offered_interest_rate real NOT NULL\n" +
                    "    )");
            statement.executeUpdate();

            System.out.println("Table successfully created");

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    public void createTableBookings(){
        try (
                Connection connection = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");
        ) {

            PreparedStatement statement = connection.prepareStatement("CREATE TABLE nova_test_schema.bookings\n" +
                    "    (\n" +
                    "      booking_id serial PRIMARY KEY,\n" +
                    "      offer_id serial references nova_test_schema.offers(offer_id),\n" +
                    "      booked_credit_amount integer NOT NULL,\n" +
                    "      booked_interest_rate real NOT NULL\n" +
                    "    )");
            statement.executeUpdate();

            System.out.println("Table successfully created");

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    public void createTableCustomers(){
        try (
                Connection connection = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");
        ) {

            PreparedStatement statement = connection.prepareStatement("CREATE TABLE nova_test_schema.customers\n" +
                    "    (\n" +
                    "      customer_id serial PRIMARY KEY,\n" +
                    "      start_date timestamp NOT NULL,\n" +
                    "      applicant_id serial references nova_test_schema.applicants(applicant_id)\n" +
                    "    )");
            statement.executeUpdate();

            System.out.println("Table successfully created");

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    public void createTableTransactionEnum(){
        try (
                Connection connection = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");
        ) {

            PreparedStatement statement = connection.prepareStatement("CREATE TYPE nova_test_schema.transaction_types AS ENUM\n" +
                    "    (\n" +
                    "      'mortgage',\n" +
                    "      'interest',\n" +
                    "      'refinance',\n" +
                    "      'outgoing'\n" +
                    "    )");
            statement.executeUpdate();

            System.out.println("Enum successfully created");

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    public void createTableTransactions(){
        try (
                Connection connection = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");
        ) {

            PreparedStatement statement = connection.prepareStatement("CREATE TABLE nova_test_schema.transactions\n" +
                    "    (\n" +
                    "      transaction_id serial PRIMARY KEY,\n" +
                    "      customer_id serial references nova_test_schema.customers(customer_id),\n" +
                    "      booking_id serial references nova_test_schema.bookings(booking_id),\n" +
                    "      transaction_amount integer NOT NULL,\n" +
                    "      transaction_type_id nova_test_schema.transaction_types NOT NULL\n" +
                    "    )");
            statement.executeUpdate();

            System.out.println("Table successfully created");

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    public void createTableCreditInformation(){
        try (
                Connection connection = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");
        ) {

            PreparedStatement statement = connection.prepareStatement("CREATE TABLE nova_test_schema.credit_information\n" +
                    "    (\n" +
                    "      information_id serial PRIMARY KEY,\n" +
                    "      application_id serial references nova_test_schema.applications(application_id),\n" +
                    "      response varchar(80) NOT NULL\n" +
                    "    )");
            statement.executeUpdate();

            System.out.println("Table successfully created");

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    public void deleteTable() throws SQLException{
        Connection connection = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement("DROP TABLE nova_test_schema.transactions, nova_test_schema.credit_information, nova_test_schema.bookings, nova_test_schema.offers, nova_test_schema.customers, nova_test_schema.applications ");
            statement.execute();

            System.out.println("Table successfully deleted");

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            connection.rollback();
            System.out.println("Rollback!!");
        }finally {
            connection.setAutoCommit(true);
            connection.close();

        }
    }


    public void setApplicantsValues() throws SQLException{
        String ssn = "19920411-0482";
        String applicantGroupId = "1";
        String firstName = "Bert";
        String lastName = "Andersson";
        String email = "b.andersson@email.com";
        String phoneNr = "0767028815";
        String address = "Fakegatan 27";
        String dek = "a";

        Connection con = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");
        try {
            con.setAutoCommit(false);
            PreparedStatement stmt = con.prepareStatement("INSERT INTO nova_test_schema.applicants" +
                    "(" +
                    "hashed_ssn, " +
                    "applicant_group_id, " +
                    "first_name, " +
                    "last_name, " +
                    "ssn, " +
                    "email, " +
                    "phone_number, " +
                    "address, " +
                    "dek" +
                    ")" +
                    "VALUES(?,?,?,?,?,?,?,?,?)");
            stmt.setString(1, ssn);
            stmt.setString(2, applicantGroupId);
            stmt.setString(3, firstName);
            stmt.setString(4, lastName);
            stmt.setString(5, ssn);
            stmt.setString(6, email);
            stmt.setString(7, phoneNr);
            stmt.setString(8, address);
            stmt.setString(9, dek);
            stmt.executeUpdate();
            con.commit();

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            con.rollback();
            System.out.println("Rollback!!");
        } finally {
            con.setAutoCommit(true);
            con.close();

        }



    }

    public void setApplicationsValues() throws SQLException{
        int amount = 10000;
        int desiredMonthlyPayment = 25;
        int term = 1;

        Connection con = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");
        try {
            con.setAutoCommit(false);
            PreparedStatement stmt = con.prepareStatement("INSERT INTO nova_test_schema.applications" +
                    "(" +
                    "timestamp, " +
                    "amount, " +
                    "desired_monthly_payment, " +
                    "term" +
                    ")" +
                    "VALUES(?,?,?,?)");
            stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            stmt.setInt(2, amount);
            stmt.setInt(3, desiredMonthlyPayment);
            stmt.setInt(4, term);
            stmt.executeUpdate();
            con.commit();

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            con.rollback();
            System.out.println("Rollback!!");
        } finally {
            con.setAutoCommit(true);
            con.close();

        }
    }

    public void setCustomersValues() throws SQLException{
        //Timestamp value;
        int amount = 10000;
        int desiredMonthlyPayment = 25;
        int term = 1;

        Connection con = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");
        try {
            con.setAutoCommit(false);
            PreparedStatement stmt = con.prepareStatement("INSERT INTO nova_test_schema.customers(start_date) VALUES(?)");
            stmt.setTimestamp(1,new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();
            con.commit();

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            con.rollback();
            System.out.println("Rollback!!");
        } finally {
            con.setAutoCommit(true);
            con.close();

        }
    }

    public void setOffersValues() throws SQLException{
        int offeredCreditAmount = 90000;
        double offeredInterestRate = 0.15;

        Connection con = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");
        try {
            con.setAutoCommit(false);
            PreparedStatement stmt = con.prepareStatement("INSERT INTO nova_test_schema.offers" +
                    "(" +
                    "offered_credit_amount, " +
                    "offered_interest_rate " +
                    ")" +
                    "VALUES(?,?)");
            stmt.setInt(1, offeredCreditAmount);
            stmt.setDouble(2, offeredInterestRate);
            stmt.executeUpdate();
            con.commit();

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            con.rollback();
            System.out.println("Rollback!!");
        } finally {
            con.setAutoCommit(true);
            con.close();

        }
    }

    public void setBookingsValues() throws SQLException{
        int bookedCreditAmount = 532000;
        double bookedInterestRate = 0.20;

        Connection con = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");
        try {
            con.setAutoCommit(false);
            PreparedStatement stmt = con.prepareStatement("INSERT INTO nova_test_schema.bookings" +
                    "(" +
                    "booked_credit_amount, " +
                    "booked_interest_rate" +
                    ")" +
                    "VALUES(?,?)");
            stmt.setInt(1, bookedCreditAmount);
            stmt.setDouble(2, bookedInterestRate);
            stmt.executeUpdate();
            con.commit();

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            con.rollback();
            System.out.println("Rollback!!");
        } finally {
            con.setAutoCommit(true);
            con.close();

        }
    }

    public void setCreditInformationValues() throws SQLException{

        String response = "yesyes";

        Connection con = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");
        try {
            con.setAutoCommit(false);
            PreparedStatement stmt = con.prepareStatement("INSERT INTO nova_test_schema.credit_information" +
                    "(" +
                    "response" +
                    ")" +
                    "VALUES(?)");
            stmt.setString(1, response);
            stmt.executeUpdate();
            con.commit();

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            con.rollback();
            System.out.println("Rollback!!");
        } finally {
            con.setAutoCommit(true);
            con.close();

        }
    }

    public void setTransactionValues() throws SQLException{
       int transationAmount = 22000;

        Connection con = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");
        try {
            con.setAutoCommit(false);
            PreparedStatement stmt = con.prepareStatement("INSERT INTO nova_test_schema.transactions(transaction_amount, transaction_type_id) VALUES(?,CAST(? AS nova_test_schema.transaction_types))");
            stmt.setInt(1, transationAmount);
            stmt.setString(2, "mortgage");
            stmt.executeUpdate();
            con.commit();

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            con.rollback();
            System.out.println("Rollback!!");
        } finally {
            con.setAutoCommit(true);
            con.close();

        }
    }


/*
    public static void deleteCustomerInDB() throws SQLException {
        String name = JOptionPane.showInputDialog("Enter name to delete", "Delete");
        Connection con = DriverManager.getConnection(url, user, password);
        try {
            con.setAutoCommit(false);
            PreparedStatement stmt = con.prepareStatement("DELETE FROM customers WHERE name=?");
            stmt.setString(1, name);
            stmt.executeUpdate();

            stmt = con.prepareStatement("UPDATE rooms SET available=true, guest =NULL WHERE guest = ?");
            stmt.setString(1, name);
            stmt.executeUpdate();
            con.commit();

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            con.rollback();
            System.out.println("Rollback!!");
        } finally {
            con.setAutoCommit(true);
            con.close();

        }

    }
*/

}

