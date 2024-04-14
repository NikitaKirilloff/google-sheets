package startApplication.controlllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import startApplication.service.SpreadsheetService;

@RequiredArgsConstructor
@Controller
public class CellController {

  private final SpreadsheetService spreadsheetService;

  @GetMapping("/cells")
  public String spreadsheet(Model model) {
    model.addAttribute("cells", spreadsheetService.getCells());
    return "spreadsheet";
  }

  @PostMapping("/update")
  public String updateCellValue(@RequestParam("rowTable") int rowTable, @RequestParam("colTable") int colTable, @RequestParam("expression") String expression) {
    if (expression == null || expression.isEmpty()) {
      return "redirect:/cells";
    }
    spreadsheetService.calcExpression(rowTable, colTable, expression);
    return "redirect:/cells";
  }
}
