package startApplication.controlllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import startApplication.model.Spreadsheet;

@RequiredArgsConstructor
@Controller
public class CellController {

  Spreadsheet spreadsheet = new Spreadsheet();

  @GetMapping("/cells")
  public String spreadsheet(Model model) {
    model.addAttribute("cells", spreadsheet.getCells());
    return "spreadsheet";
  }

  @PostMapping("/update")
  public String updateCellValue(@RequestParam("rowTable") int rowTable, @RequestParam("colTable") int colTable, @RequestParam("valueTable") String valueTable) {
    spreadsheet.addAttribute(rowTable, colTable, valueTable);
    return "redirect:/cells";
  }
}
