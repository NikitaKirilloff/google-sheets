package startApplication.model;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import startApplication.service.SpreadsheetService;
import startApplication.utils.Calculator;

import static org.junit.jupiter.api.Assertions.*;

class SpreadsheetServiceTest {
  private final Calculator calculator = new Calculator();
  private final SpreadsheetService spreadsheetService = new SpreadsheetService(calculator);

  @ParameterizedTest
  @CsvSource({
          "0, 0, 100+20+3, 123",
          "1, 1, 100-200-55, -155",
          "3, 2, -777777*1, -777777",
          "3, 3, =(1+2)*(3+4), 21"
  })
  public void testShouldSuccessCalcExpressionWithNumericValue(int row, int col, String expression, String result) {
    spreadsheetService.calcExpression(row, col, expression);
    assertEquals(result, spreadsheetService.getCells()[row][col].getValue());
  }
  @ParameterizedTest
  @CsvSource({
          "4, 0, 123", // Wrong row
          "0, 4, 426", // Wrong col
          "5, 5, 779",  // Wrong col and row
          "-1, -5, 228" // Negative col and row
  })
  public void testShouldThrowOutOfBoundsException(int row, int col, String value) {
    assertThrows(ArrayIndexOutOfBoundsException.class, () -> spreadsheetService.calcExpression(row, col, value));
  }

  @ParameterizedTest
  @CsvSource({
          "1, 0, 2*3, 6",
          "1, 2, =a2+5, 11",
          "2, 1, =a2+c2 , 17",
          "0, 3, =1+2*3, 7",
          "0, 2, =2+2*3, 8",
          "1, 2, =3+2*3 , 9",
          "0, 3, =d1, 7",
          "0, 2, =c1, 8",
          "1, 2, =c2 , 9"

  })
  public void testWithReferenceOnCell() {
    spreadsheetService.calcExpression(1, 0, "2*3");
    spreadsheetService.calcExpression(1, 2, "=a2+1");
    spreadsheetService.calcExpression(2, 1, "=a2+c2");
    assertEquals("6", spreadsheetService.getCells()[1][0].getValue());
    assertEquals("7", spreadsheetService.getCells()[1][2].getValue());
    assertEquals("13", spreadsheetService.getCells()[2][1].getValue());
    spreadsheetService.calcExpression(0, 3, "=1+2*3"); //d1 7
    spreadsheetService.calcExpression(0, 2, "=2+2*3"); //c1 8
    spreadsheetService.calcExpression(1, 2, "=3+2*3"); //c2 9
    assertEquals("7", spreadsheetService.getCells()[0][3].getValue());
    assertEquals("8", spreadsheetService.getCells()[0][2].getValue());
    assertEquals("9", spreadsheetService.getCells()[1][2].getValue());
    spreadsheetService.calcExpression(0, 0, "=d1"); //a1 7
    spreadsheetService.calcExpression(0, 1, "=c1"); //a2 8
    spreadsheetService.calcExpression(0, 2, "=c2"); //a3 9
  }

  @Test
  public void testAddAttributeWithError() {
    spreadsheetService.calcExpression(2, 2, "abc");
    assertEquals("#ERROR!", spreadsheetService.getCells()[2][2].getValue());
    spreadsheetService.calcExpression(2, 2, "");
    assertEquals("#ERROR!", spreadsheetService.getCells()[2][2].getValue());
  }
}