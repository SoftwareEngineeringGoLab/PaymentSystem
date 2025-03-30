/**
 * Concrete Payment subclass for bank transfer payments.
 */
public class BankTransferPayment extends Payment {

    private String accountNumber;

    /**
     * Constructs a BankTransferPayment instance.
     *
     * @param amount        Payment amount
     * @param currency      Payment currency
     * @param accountNumber The bank account number
     */
    public BankTransferPayment(double amount, String currency, String accountNumber) {
        super(amount, currency);
        this.type = PaymentType.BANK_TRANSFER;
        this.accountNumber = accountNumber;
    }

    @Override
    public boolean validatePayment() {
        if (amount <= 0) return false;
        if (!("USD".equals(currency) || "EUR".equals(currency) || "GBP".equals(currency))) {
            return false;
        }
        if (accountNumber == null || accountNumber.isBlank()) {
            return false;
        }
        return true;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
