package startApplication.utils;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Stack;

@Component
public class Calculator {
  public String calc(String expression) {
    Stack<BigDecimal> numbers = new Stack<>();
    Stack<Character> operators = new Stack<>();
    boolean isNegative = false;

    for (int i = 0; i < expression.length(); i++) {
      char c = expression.charAt(i);
      if (c == '-') {
        if (i == 0 || expression.charAt(i - 1) == '(') {
          isNegative = true;
          continue;
        }
      }
      if (Character.isDigit(c) || c == '.') {
        StringBuilder numStr = new StringBuilder();
        if (isNegative) {
          numStr.append('-');
          isNegative = false;
        }
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
    return numbers.pop().toString();
  }

  private BigDecimal executeOp(Stack<BigDecimal> numbers, Stack<Character> operators) {
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

  private boolean checkOp(char c) {
    return (c == '+' || c == '-' || c == '*' || c == '/');
  }

  private int getOp(char operator) {
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
