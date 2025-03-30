import java.util.Date;
import java.util.Map;

public class PaymentProcessor {

    private Map<String, String> config;

    public PaymentProcessor(Map<String, String> config) {
        this.config = config;
    }

    public Map<String, String> processPayment(PaymentType paymentType, double amount, String currency,
                                              Map<String, String> customerInfo, Map<String, String> paymentDetails) {

        Payment payment = PaymentFactory.createPayment(paymentType, amount, currency, paymentDetails);

        if (payment == null) {
            return Map.of("status", "failed", "message", "Unknown payment type");
        }

        if (!payment.validatePayment()) {
            return Map.of("status", "failed", "message", "Validation error");
        }

        PaymentGateway gateway = new PaymentGateway(config);
        Map<String, String> result = gateway.process(paymentType, payment, customerInfo);

        logTransaction(paymentType, amount, currency, customerInfo, paymentDetails, result);

        return result;
    }

    private void logTransaction(PaymentType paymentType, double amount, String currency,
                                Map<String, String> customerInfo, Map<String, String> paymentDetails,
                                Map<String, String> result) {
        String logEntry = String.format("%s - %s payment of %.2f %s for %s: %s",
                new Date(), paymentType, amount, currency, customerInfo.get("name"), result);
        System.out.println("LOG: " + logEntry);
    }

    public static void main(String[] args) {
        Map<String, String> config = Map.of(
                "credit_card_endpoint", "https://api.creditcard.com/process",
                "digital_wallet_endpoint", "https://api.digitalwallet.com/process",
                "bank_transfer_endpoint", "https://api.banktransfer.com/process"
        );

        PaymentProcessor processor = new PaymentProcessor(config);
        Map<String, String> customer = Map.of("name", "John Doe", "email", "john@example.com");
        Map<String, String> paymentDetails = Map.of("card_number", "123456789012", "expiry", "12/25", "cvv", "123");

        Map<String, String> result = processor.processPayment(PaymentType.CREDIT_CARD, 100, "USD", customer, paymentDetails);
        System.out.println("Final Result: " + result);
    }
}
