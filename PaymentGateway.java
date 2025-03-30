import java.util.Date;
import java.util.Map;


public class PaymentGateway {

    private Map<String, String> config;

    /**
     * Constructs a PaymentGateway with specified API endpoints.
     *
     * @param config A map of endpoints or other configuration
     */
    public PaymentGateway(Map<String, String> config) {
        this.config = config;
    }

    /**
     * Processes the payment using the given paymentType.
     *
     * @param paymentType  The type of payment (credit_card, digital_wallet, bank_transfer)
     * @param payment      The Payment object (already validated)
     * @param customerInfo Basic customer data (e.g., name, email)
     * @return A result map containing status and transaction_id, or error info
     */
    public Map<String, String> process(PaymentType paymentType, Payment payment, Map<String, String> customerInfo) {
        switch (paymentType) {
            case PaymentType.CREDIT_CARD:
                return processCreditCard(payment, customerInfo);
            case PaymentType.DIGITAL_WALLET:
                return processDigitalWallet(payment, customerInfo);
            case PaymentType.BANK_TRANSFER:
                return processBankTransfer(payment, customerInfo);
            default:
                return Map.of("status", "failed", "message", "Unsupported payment type");
        }
    }

    private Map<String, String> processCreditCard(Payment payment, Map<String, String> customerInfo) {
        System.out.println("Connecting to Credit Card API at " + config.get("credit_card_endpoint"));
        String transactionId = "CC" + new Date().getTime();
        System.out.println("Processing credit card payment for " + customerInfo.get("name"));
        return Map.of("status", "success", "transaction_id", transactionId);
    }

    private Map<String, String> processDigitalWallet(Payment payment, Map<String, String> customerInfo) {
        System.out.println("Connecting to Digital Wallet API at " + config.get("digital_wallet_endpoint"));
        String transactionId = "DW" + new Date().getTime();
        System.out.println("Processing digital wallet payment for " + customerInfo.get("name"));
        return Map.of("status", "success", "transaction_id", transactionId);
    }

    private Map<String, String> processBankTransfer(Payment payment, Map<String, String> customerInfo) {
        System.out.println("Connecting to Bank Transfer API at " + config.get("bank_transfer_endpoint"));
        String transactionId = "BT" + new Date().getTime();
        System.out.println("Processing bank transfer payment for " + customerInfo.get("name"));
        return Map.of("status", "success", "transaction_id", transactionId);
    }
}
