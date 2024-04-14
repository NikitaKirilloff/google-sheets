package startApplication.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import startApplication.model.Cell;
import startApplication.utils.Calculator;

@Service
public class SpreadsheetService {

  private int rows;
  private int columns;
  @Getter
  private Cell[][] cells;
  private final Calculator calculator;

  public SpreadsheetService(Calculator calculator) {
    this.calculator = calculator;
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

  public void calcExpression(int row, int col, String expression) {
    if (expression.startsWith("=") && expression.matches("^=[()+\\-*/a-zA-Z0-9]+")) {
      expression = resolveCellLink(expression.substring(1).toLowerCase());
    }
    if (!expression.isEmpty() && expression.matches("[()+\\-*/0-9.]+")) {
      String result = calculator.calc(expression);
      cells[row][col].setValue(result);
    } else if (!expression.isEmpty()) {
      cells[row][col].setValue("#ERROR!");
    }
  }

  private String resolveCellLink(String expression) {
    String[] array = expression.split("[+\\-*/()]");
    String regex = "^[a-d][1-4]$";

    for (String element : array) {
      if (element.matches(regex)) {
        expression = expression.replace(element, findElementOnArray(element));
      }
    }

    return expression;
  }

  private String findElementOnArray(String arrayElement) {
    if (cells[arrayElement.charAt(1) - 49][arrayElement.charAt(0) - 'a'].getValue() != null) {
      int row = arrayElement.charAt(0) - 'a';
      int col = arrayElement.charAt(1) - 49;
      return cells[col][row].getValue();
    }
    return arrayElement;
  }
}