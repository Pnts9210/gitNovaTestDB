import java.sql.Timestamp;
import java.util.Date;

public class Invoice {

    private String firtsName;
    private String lastaName;
    private String email;
    private int customerID;
    private int bookingID;
    private int previousInvoice;
    private double latePayAmount;
    private double totalAmount;
    private double amortzation_amount;
    private double lateCharge = 100.0;
    private double lateInterest = 0.15;
    private double invoiceFee = 25.0;
    private Timestamp dueDate = setDuedate();

    public Invoice(String firtsName, String lastaName, String email, int customerID, int bookingID, int previousInvoice, double latePayAmount, double totalAmount,double amortzation_amount) {
        this.firtsName = firtsName;
        this.lastaName = lastaName;
        this.email = email;
        this.customerID = customerID;
        this.bookingID = bookingID;
        this.previousInvoice = previousInvoice;
        this.latePayAmount = latePayAmount;
        this.totalAmount = totalAmount;
        this.amortzation_amount = amortzation_amount;
    }

    private Timestamp setDuedate(){

        Date date = new Date();
        if(date.getDay() > 10) {
            date.setMonth(date.getMonth() + 1);
            date.setDate(25);
        }else
            date.setDate(30);
        Timestamp dueDate = new Timestamp(date.getTime());
        return dueDate;

    }


    public String getFirtsName() {
        return firtsName;
    }

    public void setFirtsName(String firtsName) {
        this.firtsName = firtsName;
    }

    public String getLastaName() {
        return lastaName;
    }

    public void setLastaName(String lastaName) {
        this.lastaName = lastaName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public int getPreviousInvoice() {
        return previousInvoice;
    }

    public void setPreviousInvoice(int previousInvoice) {
        this.previousInvoice = previousInvoice;
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

    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "firtsName='" + firtsName + '\'' +
                ", lastaName='" + lastaName + '\'' +
                ", email='" + email + '\'' +
                ", customerID=" + customerID +
                ", bookingID=" + bookingID +
                ", previousInvoice=" + previousInvoice +
                ", latePayAmount=" + latePayAmount +
                ", totalAmount=" + totalAmount +
                ", amortzation_amount=" + amortzation_amount +
                ", lateCharge=" + lateCharge +
                ", lateInterest=" + lateInterest +
                ", invoiceFee=" + invoiceFee +
                ", dueDate=" + dueDate +
                "\n";
    }
}


