/**
 * Concrete Payment subclass for credit card payments.
 */
public class CreditCardPayment extends Payment {

    private String cardNumber;
    private String expiry;
    private String cvv;

    /**
     * Constructs a CreditCardPayment instance.
     *
     * @param amount     Payment amount
     * @param currency   Payment currency (USD, EUR, GBP)
     * @param cardNumber Credit card number
     * @param expiry     Card expiry date (e.g. "12/25")
     * @param cvv        Card CVV code
     */
    public CreditCardPayment(double amount, String currency,
                             String cardNumber, String expiry, String cvv) {
        super(amount, currency);
        this.type = PaymentType.CREDIT_CARD;
        this.cardNumber = cardNumber;
        this.expiry = expiry;
        this.cvv = cvv;
    }

    @Override
    public boolean validatePayment() {
        if (amount <= 0) return false;
        if (!("USD".equals(currency) || "EUR".equals(currency) || "GBP".equals(currency))) {
            return false;
        }
        if (cardNumber == null || cardNumber.length() < 12) {
            return false;
        }
        return true;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getExpiry() {
        return expiry;
    }

    public String getCvv() {
        return cvv;
    }
}
