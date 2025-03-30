import java.util.*;

/**
 * The main payment processor that handles payment validation and processing
 * through selected payment gateways. Demonstrates polymorphism using the
 * PaymentGateway interface.
 */
public class PaymentProcessor {

    private List<PaymentGateway> paymentGateways;

    /**
     * Constructs a PaymentProcessor with configured gateways.
     *
     * @param paymentGateways list of payment gateways
     */
    public PaymentProcessor(List<PaymentGateway> paymentGateways) {
        this.paymentGateways = paymentGateways;
    }

    /**
     * Processes a payment using the specified payment details and gateway.
     *
     * @param paymentType The type of payment to process
     * @param amount The payment amount
     * @param currency The payment currency
     * @param customerInfo Customer information
     * @param paymentDetails Specific payment details
     * @param selectedGateway The name of the gateway to use ("stripe" or "paypal")
     * @return A result map containing status and transaction information
     */
    public Map<String, String> processPayment(PaymentType paymentType, double amount, String currency,
                                             Map<String, String> customerInfo, Map<String, String> paymentDetails,
                                              GatewayType selectedGateway) {

        // Create and validate the payment object
        Payment payment = PaymentFactory.createPayment(paymentType, amount, currency, paymentDetails);

        if (payment == null) {
            return Map.of("status", "failed", "message", "Unknown payment type");
        }

        if (!payment.validatePayment()) {
            return Map.of("status", "failed", "message", "Validation error");
        }

        // Select the appropriate gateway (demonstrating polymorphism)
        PaymentGateway gateway = selectGateway(selectedGateway);
        if (gateway == null) {
            return Map.of("status", "failed", "message", "Invalid gateway selection");
        }

        // Process the payment using the selected gateway
        Map<String, String> result = gateway.process(paymentType, payment, customerInfo);
        
        // Log the transaction
        logTransaction(paymentType, amount, currency, customerInfo, selectedGateway, result);
        
        return result;
    }

    /**
     * Demonstrates runtime polymorphism by selecting a gateway based on the name.
     *
     * @param gatewayType The name of the gateway to use
     * @return The selected PaymentGateway implementation
     */
    private PaymentGateway selectGateway(GatewayType gatewayType) {
        return paymentGateways.stream()
                .filter(g -> g.getType() == gatewayType)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "No gateway found matching type: " + gatewayType
                ));
    }

    /**
     * Refunds a payment through the appropriate gateway.
     *
     * @param transactionId The transaction ID to refund
     * @param amount The amount to refund
     * @param gatewayType The type of the gateway to use
     * @return A result map containing refund information
     */
    public Map<String, String> refundPayment(String transactionId, double amount, GatewayType gatewayType) {
        PaymentGateway gateway = selectGateway(gatewayType);
        if (gateway == null) {
            return Map.of("status", "failed", "message", "Invalid gateway selection");
        }
        
        return gateway.refundPayment(transactionId, amount);
    }

    /**
     * Logs transaction details.
     */
    private void logTransaction(PaymentType paymentType, double amount, String currency,
                               Map<String, String> customerInfo, GatewayType gateway,
                               Map<String, String> result) {
        String logEntry = String.format("%s - %s payment of %.2f %s for %s via %s: %s",
                new Date(), paymentType, amount, currency, customerInfo.get("name"), gateway.name(), result);
        System.out.println("LOG: " + logEntry);
    }

    /**
     * Example usage of the payment processor.
     */
    public static void main(String[] args) {
        // Initialize the payment processor with configured gateways
        ArrayList<PaymentGateway> paymentGateways = new ArrayList<>();
        paymentGateways.add(new PayPalGateway("https://api.paypal.com/v1", "paypal_client_id", "paypal_client_secret"));
        paymentGateways.add(new StripeGateway("https://api.stripe.com/v1", "sk_test_stripe_key"));

        PaymentProcessor processor = new PaymentProcessor(paymentGateways);

        // Customer and payment information
        Map<String, String> customer = Map.of("name", "John Doe", "email", "john@example.com");
        Map<String, String> paymentDetails = Map.of("card_number", "4242424242424242", 
                                                   "expiry", "12/25", "cvv", "123");

        // Process a payment using the Stripe gateway
        System.out.println("\n--- Processing payment through Stripe ---");
        Map<String, String> stripeResult = processor.processPayment(
            PaymentType.CREDIT_CARD, 100, "USD", customer, paymentDetails, GatewayType.STRIPE
        );
        System.out.println("Stripe Result: " + stripeResult);
        
        // Process a payment using the PayPal gateway (demonstrating polymorphism)
        System.out.println("\n--- Processing same payment through PayPal ---");
        Map<String, String> paypalResult = processor.processPayment(
            PaymentType.CREDIT_CARD, 100, "USD", customer, paymentDetails, GatewayType.PAYPAL
        );
        System.out.println("PayPal Result: " + paypalResult);
        
        // Demonstrate refund functionality
        System.out.println("\n--- Processing refund through Stripe ---");
        String transactionId = stripeResult.get("transaction_id");
        Map<String, String> refundResult = processor.refundPayment(transactionId, 50, GatewayType.STRIPE);
        System.out.println("Refund Result: " + refundResult);
    }
}
