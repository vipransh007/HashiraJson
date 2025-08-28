import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        // 1. READ AND PARSE THE JSON FILE
        Gson gson = new Gson();

        try (InputStream is = Main.class.getClassLoader().getResourceAsStream("problem.json");
             Reader reader = new InputStreamReader(is)) {

            // Define the type for our complex JSON structure: a map of strings to objects
            Type type = new TypeToken<Map<String, Object>>(){}.getType();
            Map<String, Object> data = gson.fromJson(reader, type);

            // Use a TreeMap to process the test cases in numerical order ("1", "2", "3", ...)
            Map<String, TestCase> testCases = new TreeMap<>();

            // 2. EXTRACT THE TEST CASES FROM THE PARSED DATA
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                // We only care about the numbered keys ("1", "2", etc.)
                if (entry.getKey().matches("\\d+")) { // Check if the key is a number
                    // Gson parses nested objects into a generic map, so we convert it back to a TestCase object
                    TestCase tc = gson.fromJson(gson.toJson(entry.getValue()), TestCase.class);
                    testCases.put(entry.getKey(), tc);
                }
            }

            // 3. PROCESS EACH TEST CASE AND PRINT THE RESULT
            System.out.println("--- Base Conversion Results ---");
            for (Map.Entry<String, TestCase> entry : testCases.entrySet()) {
                String caseNumber = entry.getKey();
                TestCase tc = entry.getValue();

                int base = Integer.parseInt(tc.base);
                String valueStr = tc.value;

                // Use BigInteger to convert the value from the given base to decimal (base 10)
                // This is necessary because the numbers are too large for standard 'long'
                BigInteger decimalValue = new BigInteger(valueStr, base);

                System.out.println("Case #" + caseNumber + ": " + decimalValue);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
