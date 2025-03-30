import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * Loads gateway configuration from a .properties file and 
 * creates the appropriate PaymentGateway.
 */

public class ConfigManager {

    private static final String PROPERTIES_FILE = "app.properties";

    public static Map<String, String> getPaypalConfig() {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream(PROPERTIES_FILE)) {
            props.load(input);

            String paypalUrl = props.getProperty("paypal.api.url");
            String clientSecret = props.getProperty("paypal.client.secret" );
            String clientId = props.getProperty("paypal.client.id");

            return Map.of("endpoint", paypalUrl, "clientSecret", clientSecret, "clientid", clientId);
        } catch (IOException e) {
            throw new RuntimeException("Could not load " + PROPERTIES_FILE, e);
        }
    }

    public static Map<String, String> getStripeConfig() {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream(PROPERTIES_FILE)) {
            props.load(input);

            String stripeUrl = props.getProperty("stripe.api.url");
            String apikey = props.getProperty("stripe.api.key");

            return Map.of("endpoint", stripeUrl, "apikey", apikey);
        } catch (IOException e) {
            throw new RuntimeException("Could not load " + PROPERTIES_FILE, e);
        }
    }

}
