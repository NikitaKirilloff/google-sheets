package startApplication.model;

import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Stack;

@Repository
public class Spreadsheet {

  private int rows;
  private int columns;
  @Getter
  private Cell[][] cells;


  public Spreadsheet() {
    this.rows = 4;
    this.columns = 4;
    initializeCells(rows, columns);
  }

  private void initializeCells(int rows, int columns) {
    cells = new Cell[rows][columns];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        cells[i][j] = new Cell();
      }
    }
  }

  public void addAttribute(int row, int col, String attributeValue) {
    if (attributeValue.startsWith("=") && attributeValue.matches("^=[()+\\-*/a-zA-Z0-9]+")) {
      attributeValue = replaceCellLink(attributeValue.substring(1).toLowerCase());
    }
    if (!attributeValue.isEmpty() && attributeValue.matches("[()+\\-*/0-9.]+")) {
      String result = String.valueOf(calc(attributeValue));
      cells[row][col].setValue(result);
    } else if (!attributeValue.isEmpty()) {
      cells[row][col].setValue("#ERROR!");
    }
  }

  private String replaceCellLink(String attributeValue) {
    String[] array = attributeValue.split("[+\\-*/()]");
    String regex = "^[a-d][1-4]$";

    for (String element : array) {
      if (element.matches(regex)) {
        attributeValue = attributeValue.replace(element, findElementOnArray(element));
      }
    }

    return attributeValue;
  }

  private String findElementOnArray(String element) {
    if (!cells[element.charAt(1) - 49][element.charAt(0) - 'a'].getValue().isBlank()) {
      return cells[element.charAt(1) - 49][element.charAt(0) - 'a'].getValue();
    }
    return element;
  }

  public static BigDecimal calc(String expression) {
    Stack<BigDecimal> numbers = new Stack<>();
    Stack<Character> operators = new Stack<>();

    for (int i = 0; i < expression.length(); i++) {
      char c = expression.charAt(i);
      if (Character.isDigit(c) || c == '.') {
        StringBuilder numStr = new StringBuilder();
        while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
          numStr.append(expression.charAt(i));
          i++;
        }
        i--;
        numbers.push(new BigDecimal(numStr.toString()));
      } else if (c == '(') {
        operators.push(c);
      } else if (c == ')') {
        while (operators.peek() != '(') {
          BigDecimal result = executeOp(numbers, operators);
          numbers.push(result);
        }
        operators.pop();
      } else if (checkOp(c)) {
        while (!operators.isEmpty() && getOp(operators.peek()) >= getOp(c)) {
          BigDecimal result = executeOp(numbers, operators);
          numbers.push(result);
        }
        operators.push(c);
      }
    }
    while (!operators.isEmpty()) {
      BigDecimal result = executeOp(numbers, operators);
      numbers.push(result);
    }
    return numbers.pop();
  }

  public static BigDecimal executeOp(Stack<BigDecimal> numbers, Stack<Character> operators) {
    BigDecimal operand2 = numbers.pop();
    BigDecimal operand1 = numbers.pop();
    char operator = operators.pop();
    BigDecimal result = BigDecimal.ZERO;

    switch (operator) {
      case '+':
        result = operand1.add(operand2);
        break;
      case '-':
        result = operand1.subtract(operand2);
        break;
      case '*':
        result = operand1.multiply(operand2);
        break;
      case '/':
        result = operand1.divide(operand2, BigDecimal.ROUND_HALF_UP);
        break;
    }

    return result;
  }

  public static boolean checkOp(char c) {
    return (c == '+' || c == '-' || c == '*' || c == '/');
  }

  public static int getOp(char operator) {
    switch (operator) {
      case '+':
      case '-':
        return 1;
      case '*':
      case '/':
        return 2;
      default:
        return 0;
    }
  }
}