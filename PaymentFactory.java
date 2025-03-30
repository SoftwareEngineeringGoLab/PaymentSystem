import java.util.Map;

/**
 * A factory class that creates the appropriate Payment object
 * based on the given paymentType.
 */
public class PaymentFactory {

    /**
     * Creates a Payment instance for the given paymentType.
     *
     * @param paymentType    The type of payment (credit_card, digital_wallet, bank_transfer)
     * @param amount         Payment amount
     * @param currency       Payment currency
     * @param paymentDetails Extra payment details (cardNumber, walletId, accountNumber, etc.)
     * @return A concrete Payment subclass instance or null if the type is unknown
     */
    public static Payment createPayment(String paymentType, double amount,
                                        String currency, Map<String, String> paymentDetails) {
        switch (paymentType) {
            case "credit_card":
                return new CreditCardPayment(
                        amount,
                        currency,
                        paymentDetails.getOrDefault("card_number", ""),
                        paymentDetails.getOrDefault("expiry", ""),
                        paymentDetails.getOrDefault("cvv", "")
                );
            case "digital_wallet":
                return new DigitalWalletPayment(
                        amount,
                        currency,
                        paymentDetails.getOrDefault("wallet_id", "")
                );
            case "bank_transfer":
                return new BankTransferPayment(
                        amount,
                        currency,
                        paymentDetails.getOrDefault("account_number", "")
                );
            default:
                return null;
        }
    }
}
