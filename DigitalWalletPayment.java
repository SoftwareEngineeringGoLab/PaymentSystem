/**
 * Concrete Payment subclass for digital wallet payments.
 */
public class DigitalWalletPayment extends Payment {

    private String walletId;

    /**
     * Constructs a DigitalWalletPayment instance.
     *
     * @param amount   Payment amount
     * @param currency Payment currency
     * @param walletId The digital wallet ID
     */
    public DigitalWalletPayment(double amount, String currency, String walletId) {
        super(amount, currency);
        this.type = PaymentType.DIGITAL_WALLET;
        this.walletId = walletId;
    }

    @Override
    public boolean validatePayment() {
        if (amount <= 0) return false;
        if (!("USD".equals(currency) || "EUR".equals(currency) || "GBP".equals(currency))) {
            return false;
        }
        if (walletId == null || walletId.isBlank()) {
            return false;
        }
        return true;
    }

    public String getWalletId() {
        return walletId;
    }
}
