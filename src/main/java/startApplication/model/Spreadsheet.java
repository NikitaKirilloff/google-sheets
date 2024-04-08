package startApplication.model;

import lombok.Getter;
import org.springframework.stereotype.Repository;

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

  public String addAttribute(int row, int col, String attributeValue) {
    cells[row][col].setValue(attributeValue);
    return attributeValue;
    /*int value = 0;
    if (attributeValue.startsWith("=")) {
      try {
        value = calc(attributeValue.substring(1));
        //cells.get(Integer.parseInt(String.valueOf(row))).get(Integer.parseInt(String.valueOf(col))).setValue(String.valueOf(Double.valueOf(String.valueOf(value))));
      } catch (Exception e) {
        return -1;
      }
    } else if (attributeValue.length() == 1) {
      try {
        value = Integer.parseInt(attributeValue);
        //cells.get(Integer.parseInt(String.valueOf(row))).get(Integer.parseInt(String.valueOf(col))).setValue(String.valueOf(Double.valueOf(String.valueOf(value))));
      } catch (NumberFormatException e) {
        return -1;
      }
    } else {
      return -1;
    }
    return value;*/
  }

  private static int calc(String expression) {
    Stack<Integer> numbers = new Stack<>();
    Stack<Character> operators = new Stack<>();

    for (int i = 0; i < expression.length(); i++) {
      char c = expression.charAt(i);
      if (Character.isDigit(c)) {
        int num = 0;
        while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
          num = num * 10 + (expression.charAt(i) - '0');
          i++;
        }
        i--;
        numbers.push(num);
      } else if (c == '(') {
        operators.push(c);
      } else if (c == ')') {
        while (operators.peek() != '(') {
          int result = executeOp(numbers, operators);
          numbers.push(result);
        }
        operators.pop();
      } else if (checkOp(c)) {
        while (!operators.isEmpty() && getOp(operators.peek()) >= getOp(c)) {
          int result = executeOp(numbers, operators);
          numbers.push(result);
        }
        operators.push(c);
      }
    }
    while (!operators.isEmpty()) {
      int result = executeOp(numbers, operators);
      numbers.push(result);
    }
    return numbers.pop();
  }

  private static int executeOp(Stack<Integer> numbers, Stack<Character> operators) {
    int operand2 = numbers.pop();
    int operand1 = numbers.pop();
    char operator = operators.pop();
    int result = 0;

    switch (operator) {
      case '+':
        result = operand1 + operand2;
        break;
      case '-':
        result = operand1 - operand2;
        break;
      case '*':
        result = operand1 * operand2;
        break;
      case '/':
        result = operand1 / operand2;
        break;
    }

    return result;
  }

  private static boolean checkOp(char c) {
    return (c == '+' || c == '-' || c == '*' || c == '/');
  }

  private static int getOp(char operator) {
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