import java.util.ArrayList;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Map<String, String> paypalConfig = ConfigManager.getPaypalConfig();
        Map<String, String> stripeConfig = ConfigManager.getStripeConfig();

        // Initialize the payment processor with configured gateways
        ArrayList<PaymentGateway> paymentGateways = new ArrayList<>();

        paymentGateways.add(new PayPalGateway(paypalConfig.get("endpoint"), paypalConfig.get("clientid"), paypalConfig.get("clientSecret")));
        paymentGateways.add(new StripeGateway(stripeConfig.get("endpoint"), stripeConfig.get("apikey")));

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
