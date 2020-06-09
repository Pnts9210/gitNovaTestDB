import com.github.javafaker.Faker;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class Init {
    private final String url = "jdbc:postgresql:nova_test_db";
    private final String user = "postgres";
    private final String password = "myPassword";
    ArrayList<User> userArr = new ArrayList<>();

    public void createSchema(Boolean makeFakeData, int users) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");

        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement("DROP SCHEMA IF EXISTS nova_test_schema CASCADE");
            stmt.executeUpdate();
            connection.commit();
            stmt = connection.prepareStatement("CREATE SCHEMA nova_test_schema");
            stmt.executeUpdate();
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
            connection.prepareStatement("CREATE UNIQUE INDEX applicant_hashed_ssn ON nova_test_schema.applicants (hashed_ssn)");
            statement.executeUpdate();
            // statement.executeUpdate();
            statement = connection.prepareStatement("CREATE TABLE nova_test_schema.applications\n" +
                    "    (\n" +
                    "      application_id serial PRIMARY KEY,\n" +
                    "      applicant_id serial references nova_test_schema.applicants(applicant_id),\n" +
                    "      timestamp timestamp NOT NULL,\n" +
                    "      amount integer NOT NULL,\n" +
                    "      desired_monthly_payment integer,\n" +
                    "      term integer\n" +
                    "    )");
            statement.executeUpdate();
            statement = connection.prepareStatement("CREATE TABLE nova_test_schema.offers\n" +
                    "    (\n" +
                    "      offer_id serial PRIMARY KEY,\n" +
                    "      application_id serial references nova_test_schema.applications(application_id),\n" +
                    "      offered_credit_amount integer NOT NULL,\n" +
                    "      offered_interest_rate real NOT NULL\n" +
                    "    )");
            statement.executeUpdate();
            statement = connection.prepareStatement("CREATE TABLE nova_test_schema.bookings\n" +
                    "    (\n" +
                    "      booking_id serial PRIMARY KEY,\n" +
                    "      offer_id serial references nova_test_schema.offers(offer_id),\n" +
                    "      booked_credit_amount integer NOT NULL,\n" +
                    "      booked_interest_rate real NOT NULL\n" +
                    "    )");
            statement.executeUpdate();
            statement = connection.prepareStatement("CREATE TABLE nova_test_schema.customers\n" +
                    "    (\n" +
                    "      customer_id serial PRIMARY KEY,\n" +
                    "      start_date timestamp NOT NULL,\n" +
                    "      applicant_id serial references nova_test_schema.applicants(applicant_id)\n" +
                    "    )");
            statement.executeUpdate();
            statement = connection.prepareStatement("CREATE TYPE nova_test_schema.transaction_types AS ENUM\n" +
                    "    (\n" +
                    "      'mortgage',\n" +
                    "      'interest',\n" +
                    "      'refinance',\n" +
                    "      'outgoing'\n" +
                    "    )");
            statement.executeUpdate();
            statement = connection.prepareStatement("CREATE TABLE nova_test_schema.credit_information\n" +
                    "    (\n" +
                    "      information_id serial PRIMARY KEY,\n" +
                    "      application_id serial references nova_test_schema.applications(application_id),\n" +
                    "      response varchar(80) NOT NULL\n" +
                    "    )");
            statement.executeUpdate();
            statement = connection.prepareStatement("CREATE TABLE nova_test_schema.invoices\n" +
                    "    (\n" +
                    "      invoice_id serial PRIMARY KEY,\n" +
                    "      booking_id serial references nova_test_schema.bookings(booking_id),\n" +
                    "      invoice_date timestamp NOT NULL,\n" +
                    "      ocr varchar(80) NOT NULL,\n" +
                    "      total_amount real NOT NULL,\n" +
                    "      interest_amount real NOT NULL,\n" +
                    "      amortization_amount real NOT NULL,\n" +
                    "      late_charge real,\n" +
                    "      late_interest real,\n" +
                    "      previous_invoice serial references nova_test_schema.invoices(invoice_id),\n" +
                    "      invoice_fee real,\n" +
                    "      due_date timestamp NOT NULL,\n" +
                    "      pdf_path varchar(80) NOT NULL\n" +
                    "    )");
            statement.executeUpdate();
            statement = connection.prepareStatement("CREATE TABLE nova_test_schema.transactions\n" +
                    "    (\n" +
                    "      transaction_id serial PRIMARY KEY,\n" +
                    "      customer_id serial references nova_test_schema.customers(customer_id),\n" +
                    "      booking_id serial references nova_test_schema.bookings(booking_id),\n" +
                    "      invoice_id serial references nova_test_schema.invoices(invoice_id),\n" +
                    "      transaction_amount integer NOT NULL,\n" +
                    "      transaction_type_id nova_test_schema.transaction_types NOT NULL\n" +
                    "    )");
            statement.executeUpdate();

            connection.commit();

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            connection.rollback();
            System.out.println("Rollback!!");
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
        if(makeFakeData) {
            createUserObjects(users);
            setApplicantsValues();
            setApplicationsValues();
            setCreditInformationValues();
            setOffersValues();
            setBookingsValues();
            setCustomersValues();
            setInvoiceValues();
            setTransactionValues();
        }
    }

    public void createUserObjects(int customerAmount){
        for(int i = 0; i < customerAmount; i++)
            userArr.add(new User());
    }
    @Deprecated
    public void createSchemaOld(boolean makeFakeData, int howMany) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");

        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement("DROP SCHEMA IF EXISTS nova_test_schema CASCADE");
            stmt.executeUpdate();
            connection.commit();
            stmt = connection.prepareStatement("CREATE SCHEMA nova_test_schema");
            stmt.executeUpdate();
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
            connection.prepareStatement("CREATE UNIQUE INDEX applicant_hashed_ssn ON nova_test_schema.applicants (hashed_ssn)");
            statement.executeUpdate();
           // statement.executeUpdate();
            statement = connection.prepareStatement("CREATE TABLE nova_test_schema.applications\n" +
                    "    (\n" +
                    "      application_id serial PRIMARY KEY,\n" +
                    "      applicant_id serial references nova_test_schema.applicants(applicant_id),\n" +
                    "      timestamp timestamp NOT NULL,\n" +
                    "      amount integer NOT NULL,\n" +
                    "      desired_monthly_payment integer,\n" +
                    "      term integer\n" +
                    "    )");
            statement.executeUpdate();
            statement = connection.prepareStatement("CREATE TABLE nova_test_schema.offers\n" +
                    "    (\n" +
                    "      offer_id serial PRIMARY KEY,\n" +
                    "      application_id serial references nova_test_schema.applications(application_id),\n" +
                    "      offered_credit_amount integer NOT NULL,\n" +
                    "      offered_interest_rate real NOT NULL\n" +
                    "    )");
            statement.executeUpdate();
            statement = connection.prepareStatement("CREATE TABLE nova_test_schema.bookings\n" +
                    "    (\n" +
                    "      booking_id serial PRIMARY KEY,\n" +
                    "      offer_id serial references nova_test_schema.offers(offer_id),\n" +
                    "      booked_credit_amount integer NOT NULL,\n" +
                    "      booked_interest_rate real NOT NULL\n" +
                    "    )");
            statement.executeUpdate();
            statement = connection.prepareStatement("CREATE TABLE nova_test_schema.customers\n" +
                    "    (\n" +
                    "      customer_id serial PRIMARY KEY,\n" +
                    "      start_date timestamp NOT NULL,\n" +
                    "      applicant_id serial references nova_test_schema.applicants(applicant_id)\n" +
                    "    )");
            statement.executeUpdate();
            statement = connection.prepareStatement("CREATE TYPE nova_test_schema.transaction_types AS ENUM\n" +
                    "    (\n" +
                    "      'mortgage',\n" +
                    "      'interest',\n" +
                    "      'refinance',\n" +
                    "      'outgoing'\n" +
                    "    )");
            statement.executeUpdate();
            statement = connection.prepareStatement("CREATE TABLE nova_test_schema.transactions\n" +
                    "    (\n" +
                    "      transaction_id serial PRIMARY KEY,\n" +
                    "      customer_id serial references nova_test_schema.customers(customer_id),\n" +
                    "      booking_id serial references nova_test_schema.bookings(booking_id),\n" +
                    "      transaction_amount integer NOT NULL,\n" +
                    "      transaction_type_id nova_test_schema.transaction_types NOT NULL\n" +
                    "    )");
            statement.executeUpdate();
            statement = connection.prepareStatement("CREATE TABLE nova_test_schema.credit_information\n" +
                    "    (\n" +
                    "      information_id serial PRIMARY KEY,\n" +
                    "      application_id serial references nova_test_schema.applications(application_id),\n" +
                    "      response varchar(80) NOT NULL\n" +
                    "    )");
            statement.executeUpdate();
            statement = connection.prepareStatement("CREATE TABLE nova_test_schema.invoices\n" +
                    "    (\n" +
                    "      invoice_id serial PRIMARY KEY,\n" +
                    "      booking_id serial references nova_test_schema.bookings(booking_id),\n" +
                    "      invoice_date timestamp NOT NULL,\n" +
                    "      ocr varchar(80) NOT NULL,\n" +
                    "      total_amount real NOT NULL,\n" +
                    "      interest_amount real NOT NULL,\n" +
                    "      amortization_amount real NOT NULL,\n" +
                    "      late_charge real,\n" +
                    "      late_interest real,\n" +
                    "      previous_invoice serial references nova_test_schema.invoices(invoice_id),\n" +
                    "      invoice_fee real,\n" +
                    "      due_date timestamp NOT NULL,\n" +
                    "      pdf_path varchar(80) NOT NULL\n" +
                    "    )");
            statement.executeUpdate();

            connection.commit();


        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            connection.rollback();
            System.out.println("Rollback!!");
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
        if(makeFakeData) {
            for (int i = 0; i < howMany; i++) {
                Faker faker = new Faker(new Locale("sv-SE"));
                String firstName = faker.name().firstName();
                String lastName = faker.name().lastName();
                String applicantGroupId = firstName + " " + lastName;
                String email = lastName + "@fakemail.com";
                String address = faker.address().streetAddress();
                String phoneNr = faker.phoneNumber().phoneNumber();
                String ssn = faker.idNumber().validSvSeSsn();
                String dek = "secureHashNumber";
                String response = "yesyes!";
                //String ocr = faker.number().toString();
                int term = faker.number().numberBetween(1, 50);
                int amount = faker.number().numberBetween(10000, 1000000);
                int desierdMonthyPayment = faker.number().numberBetween(100, 10000);
                int offeredCreditAmount = faker.number().numberBetween(10000, 1000000);
                int bookedCreditAmount = faker.number().numberBetween(10000, 1000000);
                int transactionAmount = faker.number().numberBetween(10000, 1000000);
                double offeredInterestRate = faker.number().numberBetween(1, 30) / 100.0;
                double bookedInterestRate = faker.number().numberBetween(1, 30) / 100.0;
                int randomInt = faker.number().numberBetween(1, 4);
                String transactioTypeId = "";
                long ocrInt = faker.number().numberBetween(10000000, 100000000);
                String ocr = "" + ocrInt;
                Double totalAmount = faker.number().numberBetween(100, 10000)/1.0;
                Double interest_amount = faker.number().numberBetween(1, 30) / 100.0;
                Double amortization_amount = faker.number().numberBetween(1, 30) / 100.0;
                Double late_charge = faker.number().numberBetween(50, 150) / 1.0;
                Double late_interest = faker.number().numberBetween(1, 30) / 100.0;
                Double invoice_fee = faker.number().numberBetween(50, 150) / 1.0;
                String pdf_path = "pathiPath";

                if(randomInt == 1)
                    transactioTypeId = "mortgage";
                if(randomInt == 2)
                    transactioTypeId = "interest";
                if(randomInt == 3)
                    transactioTypeId = "refinance";
                if(randomInt == 4)
                    transactioTypeId = "outgoing";

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

                    stmt = con.prepareStatement("INSERT INTO nova_test_schema.applications" +
                            "(" +
                            "timestamp, " +
                            "amount, " +
                            "desired_monthly_payment, " +
                            "term" +
                            ")" +
                            "VALUES(?,?,?,?)");
                    stmt.setTimestamp(1, randomDate(2018,2020));
                    stmt.setInt(2, amount);
                    stmt.setInt(3, desierdMonthyPayment);
                    stmt.setInt(4, term);
                    stmt.executeUpdate();

                    stmt = con.prepareStatement("INSERT INTO nova_test_schema.customers(start_date) VALUES(?)");
                    stmt.setTimestamp(1, randomDate(2018,2020));
                    stmt.executeUpdate();

                    stmt = con.prepareStatement("INSERT INTO nova_test_schema.offers" +
                            "(" +
                            "offered_credit_amount, " +
                            "offered_interest_rate " +
                            ")" +
                            "VALUES(?,?)");
                    stmt.setInt(1, offeredCreditAmount);
                    stmt.setDouble(2, offeredInterestRate);
                    stmt.executeUpdate();

                    stmt = con.prepareStatement("INSERT INTO nova_test_schema.bookings" +
                            "(" +
                            "booked_credit_amount, " +
                            "booked_interest_rate" +
                            ")" +
                            "VALUES(?,?)");
                    stmt.setInt(1, bookedCreditAmount);
                    stmt.setDouble(2, bookedInterestRate);
                    stmt.executeUpdate();

                    stmt = con.prepareStatement("INSERT INTO nova_test_schema.credit_information" +
                            "(" +
                            "response" +
                            ")" +
                            "VALUES(?)");
                    stmt.setString(1, response);
                    stmt.executeUpdate();

                    stmt = con.prepareStatement("INSERT INTO nova_test_schema.transactions(transaction_amount, transaction_type_id) VALUES(?,CAST(? AS nova_test_schema.transaction_types))");
                    stmt.setInt(1, transactionAmount);
                    stmt.setString(2, transactioTypeId);
                    stmt.executeUpdate();

                    stmt = con.prepareStatement("INSERT INTO nova_test_schema.invoices" +
                            "(" +
                            "invoice_date, " +
                            "ocr," +
                            "total_amount," +
                            "interest_amount," +
                            "amortization_amount," +
                            "late_charge," +
                            "late_interest," +
                            "invoice_fee," +
                            "due_date," +
                            "pdf_path" +
                            ")" +
                            "VALUES(?,?,?,?,?,?,?,?,?,?)");
                    stmt.setTimestamp(1, randomDate(2018, 2020));
                    stmt.setString(2, ocr);
                    stmt.setDouble(3, totalAmount);
                    stmt.setDouble(4, interest_amount);
                    stmt.setDouble(5, amortization_amount);
                    stmt.setDouble(6, late_charge);
                    stmt.setDouble(7, late_interest);
                    stmt.setDouble(8, invoice_fee);
                    stmt.setTimestamp(9, randomDate(2018,2021));
                    stmt.setString(10, "pathiPath!");
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

    private void setApplicantsValues() throws SQLException{

        Connection con = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");

            try {
                for (User user : userArr) {

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
                    stmt.setString(1, user.getPersonNr());
                    stmt.setString(2, "0");
                    stmt.setString(3, user.getFirstName());
                    stmt.setString(4, user.getLastName());
                    stmt.setString(5, user.getPersonNr());
                    stmt.setString(6, user.getEmail());
                    stmt.setString(7, user.getPhoneNr());
                    stmt.setString(8, user.getAddress());
                    stmt.setString(9, "secureHashNumber");
                    stmt.executeUpdate();
                }
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

    private void setApplicationsValues() throws SQLException{

        Faker faker = new Faker(new Locale("sv-SE"));
        Connection con = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");

        try {
            con.setAutoCommit(false);
            ResultSet resultSet = con.createStatement().executeQuery("select applicant_id from nova_test_schema.applicants;");
            while (resultSet.next()) {
                int applicantId = resultSet.getInt("applicant_id");
                int loopLimit = 0;
                do {
                    PreparedStatement stmt = con.prepareStatement("INSERT INTO nova_test_schema.applications" +
                            "(" +
                            "applicant_id, " +
                            "timestamp, " +
                            "amount, " +
                            "desired_monthly_payment, " +
                            "term" +
                            ")" +
                            "VALUES(?,?,?,?,?)");
                    stmt.setInt(1, applicantId);
                    stmt.setTimestamp(2, generateTimestamp(false,2016,2021, "random"));
                    stmt.setInt(3, faker.number().numberBetween(10000, 1000000));
                    stmt.setInt(4, faker.number().numberBetween(100, 10000));
                    stmt.setInt(5, faker.number().numberBetween(1, 50));
                    stmt.executeUpdate();
                    loopLimit++;
                } while (randomInt(1, 3) != 1 && loopLimit < 5);
            }
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

    private void setCustomersValues() throws SQLException{

        Connection con = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");
        con.setAutoCommit(false);
        try {
            ResultSet result = con.createStatement().executeQuery("SELECT offer_id from nova_test_schema.bookings");
            List<Integer> offerIds = new ArrayList<>();
            while(result.next()) {
                offerIds.add(result.getInt("offer_id"));
            }
            List <Integer> applicationIds = new ArrayList<>();
            for (Integer offerId : offerIds) {
                PreparedStatement statement = con.prepareStatement("select application_id from nova_test_schema.offers WHERE offer_id = ?");
                statement.setInt(1, offerId);
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                applicationIds.add(resultSet.getInt("application_id"));
            }

            // Set innehåller bara unika värden
            Set<Integer> applicantIds = new HashSet<>();
            for (Integer applicantionId: applicationIds) {
                PreparedStatement statement = con.prepareStatement("select applicant_id from nova_test_schema.applications WHERE application_id = ?");
                statement.setInt(1, applicantionId);
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                applicantIds.add(resultSet.getInt("applicant_id"));
            }

            for (Integer applicantId: applicantIds) {
                PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO nova_test_schema.customers(start_date, applicant_id) VALUES(?,?)");
                preparedStatement.setTimestamp(1, generateTimestamp(false,2017,2021, "random"));
                preparedStatement.setInt(2, applicantId);
                preparedStatement.executeUpdate();
            }
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

    private void setOffersValues() throws SQLException{

        Faker faker = new Faker();

        Connection con = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");


        try {
            ResultSet resultSet = con.createStatement().executeQuery("select * from nova_test_schema.credit_information;");
            con.setAutoCommit(false);
            while (resultSet.next()) {
                int offeredCreditAmount = faker.number().numberBetween(10000, 1000000);
                double offeredInterestRate = faker.number().numberBetween(1, 30) / 100.0;
                int appId = resultSet.getInt("application_id");
                if (resultSet.getString("response").equals("yes")) {
                    PreparedStatement stmt = con.prepareStatement("INSERT INTO nova_test_schema.offers" +
                            "(" +
                            "application_id, " +
                            "offered_credit_amount, " +
                            "offered_interest_rate " +
                            ")" +
                            "VALUES(?,?,?)");
                    stmt.setInt(1, appId);
                    stmt.setInt(2, offeredCreditAmount);
                    stmt.setDouble(3, offeredInterestRate);
                    stmt.executeUpdate();
                }
            }
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

    private void setBookingsValues() throws SQLException{
        Faker faker = new Faker();
        Connection con = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");

        int offerId = 0;
        int creditAmount =0;
        double interestRate = 0.0;
        int randomNum = 0;

        try {
            ResultSet resultSet = con.createStatement().executeQuery("select * from nova_test_schema.offers;");
            con.setAutoCommit(false);
            while (resultSet.next()) {
                offerId = resultSet.getInt("offer_id");
                creditAmount = resultSet.getInt("offered_credit_amount");
                interestRate = resultSet.getDouble("offered_interest_rate");

                randomNum = randomInt(1, 4);
                if (randomNum != 1) {
                    PreparedStatement stmt = con.prepareStatement("INSERT INTO nova_test_schema.bookings" +
                            "(" +
                            "offer_id, " +
                            "booked_credit_amount, " +
                            "booked_interest_rate" +
                            ")" +
                            "VALUES(?,?,?)");
                    stmt.setInt(1, offerId);
                    stmt.setInt(2, creditAmount);
                    stmt.setDouble(3, interestRate);
                    stmt.executeUpdate();

                }
                con.commit();
            }

            } catch(Exception e){
                System.out.println("Error:" + e.getMessage());
                con.rollback();
                System.out.println("Rollback!!");
            } finally{
                con.setAutoCommit(true);
                con.close();

            }

    }

    private void setCreditInformationValues() throws SQLException{
        String response = "";
        Connection con = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");

        try {
            ResultSet resultSet = con.createStatement().executeQuery("select application_id from nova_test_schema.applications;");
            while (resultSet.next()) {
                int applicationId = resultSet.getInt("application_id");

                con.setAutoCommit(false);
                    int randomNum = randomInt(1, 4);
                    if (randomNum == 1)
                        response = "no";
                    else
                        response = "yes";

                    PreparedStatement stmt = con.prepareStatement("INSERT INTO nova_test_schema.credit_information" +
                            "(" +
                            "application_id," +
                            "response" +
                            ")" +
                            "VALUES(?, ?)");
                    stmt.setInt(1, applicationId );
                    stmt.setString(2, response);
                    stmt.executeUpdate();

            }
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

    private void setInvoiceValues() throws SQLException{

        Faker faker = new Faker(new Locale("sv-SE"));
        Connection con = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");

        int creditAmount = 0;
        double interestRate = 0;
        int applicationID = 0;
        int term = 0;


        try {
            con.setAutoCommit(false);
            ResultSet resultSet = con.createStatement().executeQuery("select * from nova_test_schema.bookings;");
            while (resultSet.next()) {
                int bookingId = resultSet.getInt("booking_id");
                int offerID = resultSet.getInt("offer_id");
                ResultSet resultSet2 = con.createStatement().executeQuery("select * from nova_test_schema.offers WHERE offer_id = " + offerID + ";");
                while (resultSet2.next()) {
                    creditAmount = resultSet2.getInt("offered_credit_amount");
                    interestRate = resultSet2.getDouble("offered_interest_rate");
                    applicationID = resultSet2.getInt("application_id");
                }
                resultSet2 = con.createStatement().executeQuery("select term from nova_test_schema.applications WHERE application_id = " + applicationID + ";");
                while (resultSet2.next()) {
                    term = resultSet2.getInt("term");
                }

                Double amortization_amount = (double)creditAmount/term;
                Double late_charge = 0.0;
                Double late_interest = 0.0;
                Double invoice_fee = 25.0;
                long ocrInt = faker.number().numberBetween(10000000, 100000000);
                String ocr = "" + ocrInt;
                int loops = 0;
                int year = randomInt(2016, 2020);
                int month = randomInt(1, 12);

                while (loops < term) {
                    PreparedStatement stmt = con.prepareStatement("INSERT INTO nova_test_schema.invoices" +
                            "(" +
                            "booking_id, " +
                            "invoice_date, " +
                            "ocr," +
                            "total_amount," +
                            "interest_amount," +
                            "amortization_amount," +
                            "late_charge," +
                            "late_interest," +
                            "previous_invoice," +
                            "invoice_fee," +
                            "due_date," +
                            "pdf_path" +
                            ")" +
                            "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");

                    int prevInvoices = 1;
                    int day = randomInt(14, 25);

                    long generatedDate = Timestamp.valueOf(year + "-" + month + "-" + day + " 0:0:0").getTime();
                    Timestamp date = new Timestamp(generatedDate);

                    long generateDueDate = Timestamp.valueOf(year + "-" + month + "-25 0:0:0").getTime();
                    Timestamp dueDate = new Timestamp(generateDueDate);



                    stmt.setInt(1,bookingId);
                    stmt.setTimestamp(2, date);
                    stmt.setString(3, ocr);
                    stmt.setDouble(4, creditAmount);
                    stmt.setDouble(5, interestRate);
                    stmt.setDouble(6, amortization_amount);
                    stmt.setDouble(7, late_charge);
                    stmt.setDouble(8, late_interest);
                    stmt.setInt(9, prevInvoices);
                    stmt.setDouble(10, invoice_fee);
                    stmt.setTimestamp(11, dueDate);
                    stmt.setString(12, "pathiPath!");
                    stmt.executeUpdate();
                    prevInvoices++;
                    loops++;
                    if(month==12){
                        month = 0;
                        year++;
                    }
                    month++;
                }
            }
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

    private void setTransactionValues() throws SQLException{

        Connection con = DriverManager.getConnection("jdbc:postgresql:nova_test_db", "postgres", "myPassword");
        Faker faker = new Faker();

        try {
            con.setAutoCommit(false);
            //int bookingId = 0;
            int customerId = 0;
            int valueHolder = 0;
            String transactionTypeId = "";


            ResultSet resultSet = con.createStatement().executeQuery("SELECT * FROM nova_test_schema.invoices");
            while (resultSet.next()) {
                int hasPayed = randomInt(1, 20);
                if (hasPayed != 1){

                int randomNum = randomInt(1, 5);
                if (randomNum == 1)
                    transactionTypeId = "mortgage";
                if (randomNum == 2)
                    transactionTypeId = "interest";
                if (randomNum == 3)
                    transactionTypeId = "refinance";
                if (randomNum == 4)
                    transactionTypeId = "outgoing";


                Timestamp dueDate = resultSet.getTimestamp("due_date");
                Date date = new Date();
                if (dueDate.before(new Timestamp(date.getTime()))) {

                    int transactionAmount = resultSet.getInt("amortization_amount") + resultSet.getInt("invoice_fee");
                    int bookingId = resultSet.getInt("booking_id");
                    int invoiceId = resultSet.getInt("invoice_id");

                    ResultSet resultSet2 = con.createStatement().executeQuery("SELECT * FROM nova_test_schema.bookings WHERE booking_id = " + bookingId);
                    while (resultSet2.next())
                        valueHolder = resultSet2.getInt("offer_id");
                    resultSet2 = con.createStatement().executeQuery("SELECT * FROM nova_test_schema.offers WHERE offer_id = " + valueHolder);
                    while (resultSet2.next())
                        valueHolder = resultSet2.getInt("application_id");
                    resultSet2 = con.createStatement().executeQuery("SELECT * FROM nova_test_schema.applications WHERE application_id = " + valueHolder);
                    while (resultSet2.next())
                        valueHolder = resultSet2.getInt("applicant_id");
                    resultSet2 = con.createStatement().executeQuery("SELECT * FROM nova_test_schema.customers WHERE applicant_id = " + valueHolder);
                    while (resultSet2.next())
                        customerId = resultSet2.getInt("customer_id");

                    PreparedStatement stmt = con.prepareStatement("INSERT INTO nova_test_schema.transactions(customer_id, booking_id, invoice_id, transaction_amount, transaction_type_id) VALUES(?,?,?,?,CAST(? AS nova_test_schema.transaction_types))");
                    stmt.setInt(1, customerId);
                    stmt.setInt(2, bookingId);
                    stmt.setInt(3, invoiceId);
                    stmt.setInt(4, transactionAmount);
                    stmt.setString(5, transactionTypeId);
                    stmt.executeUpdate();
                }
                }
            }
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

    public Timestamp randomDate(int startYear, int endYear) {

        long start = Timestamp.valueOf(startYear+1+"-1-1 0:0:0").getTime();
        long end = Timestamp.valueOf(endYear+"-1-1 0:0:0").getTime();
        long ms=(long) ((end-start)*Math.random()+start);	//The qualified number of 13-bit milliseconds is obtained.
        //Date date = new Date(ms);
        return new Timestamp(ms);

    }

    public Timestamp generateTimestamp(boolean includeFuture, int startYear, int endYear, String month) {
        boolean loop = true;
        long generatedDate = 0;
        do{
            int year = randomInt(startYear, endYear + 1);
            String day = Integer.toString(randomInt(1, 28));
            if(month.equals("random"))
                month = Integer.toString(randomInt(1, 12));

            generatedDate = Timestamp.valueOf(year + "-" + month + "-" + day + " 0:0:0").getTime();
            Timestamp genDate = new Timestamp(generatedDate);


            Date date = new Date();
            if (genDate.before(new Timestamp(date.getTime()))) {
                return genDate;

            }

        }while(!includeFuture);
        return new Timestamp(generatedDate);
    }

    public void showUserArr(){
        for (User u : userArr) {
            System.out.println(u.toString());
        }
    }

    public int randomInt(int minInt, int maxInt){
        Faker faker = new Faker();
        return faker.number().numberBetween(minInt, maxInt);
    }

}

