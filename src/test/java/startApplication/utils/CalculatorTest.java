package startApplication.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import startApplication.service.SpreadsheetService;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
  private final Calculator calculator = new Calculator();
  @ParameterizedTest
  @CsvSource({
          "16+5+9, 30",
          "1000-78*11, 142",
          "2*3, 6",
          "10/2, 5",
          "(10-3)*5, 35",
          "15+15*(12-12/4), 150",
          "(1+3)*(1+2), 12",
          "1000*1000/2, 500000",
          "(111*121/11)+16*2, 1253",
          "(-117+27)*100/45, -200",
          "2147483648/8/16 , 16777216",
          "3.5*13/7, 6.5",
          "-21.1*21.1, -445.21",
          "-2147483648*(-214), 459561500672"
  })
  public void testCalc(String expression, String expected) {
    assertEquals(expected, calculator.calc(expression));
  }
}