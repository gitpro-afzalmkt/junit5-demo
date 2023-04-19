import com.example.junitdemo.Calculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    public void init() { calculator = new Calculator(); }

    @Test
    public void addTest1() {
        assertEquals(10, calculator.add(5, 5));
    }

    @Test
    public void addTest2() {
        assertEquals(2, calculator.add(1, 1));
    }

}
