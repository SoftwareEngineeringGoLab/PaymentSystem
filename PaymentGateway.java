import java.util.Date;
import java.util.Map;


public class PaymentGateway {

    private String endpoint;

    /**
     * Constructs a PaymentGateway with specified API endpoints.
     *
     * @param endpoint endpoint address
     */
    public PaymentGateway(String endpoint) {
        this.endpoint = endpoint;
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
        try {
            System.out.println("Connecting to " + paymentType.name() + " API at " + endpoint);
            String transactionId = "CC" + new Date().getTime();
            System.out.println("Processing " + paymentType.name() + " payment for " + customerInfo.get("name"));
            return Map.of("status", "success", "transaction_id", transactionId);
        } catch (Exception e) {
            return Map.of("status", "failed", "message", "Unsupported payment type");
        }
    }
}
