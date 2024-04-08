package startApplication.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@AllArgsConstructor
@Setter
@Getter
public class Cell {

  private String value;

  public Cell() {
    value = "";
  }
}
