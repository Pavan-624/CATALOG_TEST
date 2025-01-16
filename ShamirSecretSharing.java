import java.io.FileReader;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.math.BigInteger;

public class ShamirSecretSharing {

    public static void main(String[] args) {
        try {
            // Load JSON file
            JSONParser parser = new JSONParser();
JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("C:\\Users\\Pavan\\OneDrive\\Desktop\\catalog\\input.json"));

            // Extract keys n and k
            JSONObject keys = (JSONObject) jsonObject.get("keys");
            int n = Integer.parseInt(keys.get("n").toString());
            int k = Integer.parseInt(keys.get("k").toString());

            // Parse and decode roots
            double[][] points = new double[k][2]; // Store first k points (x, y)
            int count = 0;

            for (Object key : jsonObject.keySet()) {
                if (key.equals("keys")) continue;

                int x = Integer.parseInt(key.toString());
                JSONObject valueObject = (JSONObject) jsonObject.get(key);
                int base = Integer.parseInt(valueObject.get("base").toString());
                String value = valueObject.get("value").toString();

                // Decode y value
                BigInteger yDecoded = new BigInteger(value, base);

                if (count < k) {
                    points[count][0] = x; // x value
                    points[count][1] = yDecoded.doubleValue(); // y value
                    count++;
                }
            }

            // Calculate constant term (c) using Lagrange Interpolation
            double secret = lagrangeInterpolation(points, k);

            // Output the result
            System.out.printf("Secret (constant term c): %.0f\n", secret);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static double lagrangeInterpolation(double[][] points, int k) {
        double result = 0;

        for (int i = 0; i < k; i++) {
            double term = points[i][1];
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    term *= (0 - points[j][0]) / (points[i][0] - points[j][0]);
                }
            }
            result += term;
        }

        return result;
    }
}
