
import com.github.javafaker.Faker;

import java.util.Locale;

public class User {

    private String firstName;
    private String lastName;
    private String personNr;
    private String email;
    private String address;
    private String phoneNr;

    public User() {
        Faker faker = new Faker(new Locale("sv-SE"));
        firstName = faker.name().firstName();
        lastName = faker.name().lastName();
        personNr = faker.idNumber().validSvSeSsn();
        email = lastName + "@fakemail.com";
        address = faker.address().streetAddress();
        phoneNr = faker.phoneNumber().phoneNumber();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPersonNr() {
        return personNr;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNr() {
        return phoneNr;
    }


    @Override
    public String toString() {
        return "User  " +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", personNr='" + personNr + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phoneNr='" + phoneNr + '\'' +
                '}';
    }
}
