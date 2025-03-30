import java.util.Date;

/**
 * An abstract class that holds common payment properties and enforces validation.
 */
public abstract class Payment {

    PaymentType type;
    protected double amount;
    protected String currency;
    protected Date timeOfEntry;

    /**
     * Constructs a Payment with an amount and currency.
     *
     * @param amount   Payment amount
     * @param currency Payment currency (USD, EUR, GBP)
     */
    public Payment(double amount, String currency) {
        this.amount = amount;
        this.currency = currency;
        this.timeOfEntry = new Date();
    }

    /**
     * Validates the payment's details.
     *
     * @return true if valid, false otherwise
     */
    public abstract boolean validatePayment();

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Date getTimeOfEntry() {
        return timeOfEntry;
    }
}
