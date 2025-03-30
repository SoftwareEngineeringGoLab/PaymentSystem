import java.util.Map;

/**
 * Interface defining the contract for payment gateway implementations.
 * This allows for polymorphic behavior when processing different payment types.
 */
public interface PaymentGateway {
    
    /**
     * Processes the payment using the specified gateway.
     *
     * @param paymentType  The type of payment being processed
     * @param payment      The validated Payment object
     * @param customerInfo Basic customer data (e.g., name, email)
     * @return A result map containing status and transaction_id, or error info
     */
    Map<String, String> process(PaymentType paymentType, Payment payment, Map<String, String> customerInfo);
    
    /**
     * Refunds a payment transaction.
     *
     * @param transactionId The ID of the transaction to refund
     * @param amount The amount to refund (can be partial)
     * @return A result map containing status and refund information
     */
    Map<String, String> refundPayment(String transactionId, double amount);
    
    /**
     * Gets the status of a transaction.
     *
     * @param transactionId The ID of the transaction to check
     * @return A result map containing the transaction status
     */
    Map<String, String> getTransactionStatus(String transactionId);

    GatewayType getType();
}