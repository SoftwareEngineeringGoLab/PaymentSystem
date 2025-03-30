import java.util.Date;
import java.util.Map;

public class PaymentProcessor {
    private String creditCardEndpoint;
    private String digitalWalletEndpoint;
    private String bankTransferEndpoint;

    public PaymentProcessor(String creditCardEndpoint, String digitalWalletEndpoint, String bankTransferEndpoint) {
        this.creditCardEndpoint = creditCardEndpoint;
        this.digitalWalletEndpoint = digitalWalletEndpoint;
        this.bankTransferEndpoint = bankTransferEndpoint;
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

        Map<String, String> result;
        switch (payment.type) {
            case CREDIT_CARD -> {
                result = processCreditCard(payment, customerInfo);
            }
            case DIGITAL_WALLET -> {
                result = processDigitalWallet(payment, customerInfo);
            }
            case BANK_TRANSFER -> {
                result = processBankTransfer(payment, customerInfo);
            }
            default -> {
                return Map.of("status", "failed", "message", "Unsupported payment type");
            }
        }

        logTransaction(paymentType, amount, currency, customerInfo, paymentDetails, result);
        return result;
    }

    private Map<String, String> processCreditCard(Payment payment, Map<String, String> customerInfo) {
        System.out.println("Connecting to Credit Card API at " + creditCardEndpoint);
        String transactionId = "CC" + new Date().getTime();
        System.out.println("Processing credit card payment for " + customerInfo.get("name"));
        return Map.of("status", "success", "transaction_id", transactionId);
    }

    private Map<String, String> processDigitalWallet(Payment payment, Map<String, String> customerInfo) {
        System.out.println("Connecting to Digital Wallet API at " + digitalWalletEndpoint);
        String transactionId = "DW" + new Date().getTime();
        System.out.println("Processing digital wallet payment for " + customerInfo.get("name"));
        return Map.of("status", "success", "transaction_id", transactionId);
    }

    private Map<String, String> processBankTransfer(Payment payment, Map<String, String> customerInfo) {
        System.out.println("Connecting to Bank Transfer API at " + bankTransferEndpoint);
        String transactionId = "BT" + new Date().getTime();
        System.out.println("Processing bank transfer payment for " + customerInfo.get("name"));
        return Map.of("status", "success", "transaction_id", transactionId);
    }

    private void logTransaction(PaymentType paymentType, double amount, String currency,
                                Map<String, String> customerInfo, Map<String, String> paymentDetails,
                                Map<String, String> result) {
        String logEntry = String.format("%s - %s payment of %.2f %s for %s: %s",
                new Date(), paymentType, amount, currency, customerInfo.get("name"), result);
        System.out.println("LOG: " + logEntry);
    }

    public static void main(String[] args) {
        PaymentProcessor processor = new PaymentProcessor(
                "https://api.creditcard.com/process",
                "https://api.digitalwallet.com/process",
                "https://api.banktransfer.com/process"
        );
        Map<String, String> customer = Map.of("name", "John Doe", "email", "john@example.com");
        Map<String, String> paymentDetails = Map.of("card_number", "123456789012", "expiry", "12/25", "cvv", "123");

        Map<String, String> result = processor.processPayment(PaymentType.CREDIT_CARD, 100, "USD", customer, paymentDetails);
        System.out.println("Final Result: " + result);
    }
}
