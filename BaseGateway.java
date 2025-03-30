import java.util.Date;
import java.util.Map;

/**
 * Base implementation of the PaymentGateway interface that provides
 * common functionality for all concrete gateway implementations.
 */
public abstract class BaseGateway implements PaymentGateway {
    
    protected String endpoint;
    
    /**
     * Constructs a BaseGateway with the specified API endpoint.
     *
     * @param endpoint The API endpoint URL
     */
    public BaseGateway(String endpoint) {
        this.endpoint = endpoint;
    }
    
    /**
     * Gets the endpoint for this gateway.
     *
     * @return The API endpoint URL
     */
    public String getEndpoint() {
        return endpoint;
    }
    
    @Override
    public Map<String, String> getTransactionStatus(String transactionId) {
        // Common implementation for checking transaction status
        System.out.println("Checking transaction status for " + transactionId + " at " + endpoint);
        // In a real implementation, this would make an API call to the payment provider
        return Map.of("status", "success", "transaction_status", "completed");
    }
    
    /**
     * Logs API interactions with the payment gateway.
     *
     * @param action The action being performed
     * @param details Additional details about the action
     */
    protected void logGatewayInteraction(String action, String details) {
        String timestamp = new Date().toString();
        System.out.println("GATEWAY LOG [" + timestamp + "]: " + action + " - " + details);
    }
}