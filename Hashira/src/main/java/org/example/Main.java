import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

public class Main {

    // These will hold our problem's data once loaded
    private static List<Double> coefficients;
    private static double constant; // The 'c' we need to find

    /**
     * This is our f(x) function. It calculates the value of the polynomial for a given x.
     * It assumes 'coefficients' and 'constant' have already been set.
     */
    public static double f(double x) {
        double result = 0.0;
        int degree = coefficients.size();

        // Calculate the value for terms with coefficients (e.g., a*x^2, b*x)
        for (int i = 0; i < degree; i++) {
            // The power is degree - i. E.g., for [2, -3], the first power is 2, then 1.
            double power = degree - i;
            result += coefficients.get(i) * Math.pow(x, power);
        }

        // Add the constant term 'c'
        result += constant;
        return result;
    }

    public static void main(String[] args) {
        // 1. READ AND PARSE THE JSON FILE
        Gson gson = new Gson();
        ProblemData problemData = null;

        try (InputStream is = Main.class.getClassLoader().getResourceAsStream("polynomial_problem.json");
             Reader reader = new InputStreamReader(is)) {
            problemData = gson.fromJson(reader, ProblemData.class);
        } catch (Exception e) {
            e.printStackTrace();
            return; // Exit if file can't be read
        }

        coefficients = problemData.coefficients;
        List<TestCase> testCases = problemData.test_cases;

        if (testCases == null || testCases.isEmpty()) {
            System.out.println("No test cases found in JSON file.");
            return;
        }

        // 2. FIND THE CONSTANT ('c') USING THE FIRST TEST CASE
        TestCase firstCase = testCases.get(0);
        double x0 = firstCase.x;
        double y0 = firstCase.y;

        // Calculate f(x0) *without* the constant
        double valueWithoutConstant = 0.0;
        int degree = coefficients.size();
        for (int i = 0; i < degree; i++) {
            double power = degree - i;
            valueWithoutConstant += coefficients.get(i) * Math.pow(x0, power);
        }

        // The equation is: y0 = valueWithoutConstant + constant
        // So, constant = y0 - valueWithoutConstant
        constant = y0 - valueWithoutConstant;

        System.out.println("Using test case (x=" + x0 + ", y=" + y0 + "), the calculated constant is: " + constant);
        System.out.println("----------------------------------------------------");
        System.out.println("The full polynomial function is f(x) = 2x^2 - 3x + " + constant);
        System.out.println("----------------------------------------------------");

        // 3. CONFIGURE f(x) AND PROCESS ALL TEST CASES
        System.out.println("Processing all test cases with the configured f(x):");
        for (TestCase tc : testCases) {
            double calculatedY = f(tc.x);
            System.out.printf("For test case x = %.1f, f(x) = %.4f (Expected y = %.1f)\n", tc.x, calculatedY, tc.y);
        }
    }
}