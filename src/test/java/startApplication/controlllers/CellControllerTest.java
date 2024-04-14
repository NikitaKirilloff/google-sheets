package startApplication.controlllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import startApplication.model.Cell;
import startApplication.service.SpreadsheetService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class CellControllerTest {

  @Mock
  private SpreadsheetService spreadsheetService;

  @InjectMocks
  private CellController cellController;
  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    mockMvc = standaloneSetup(cellController).build();
  }

  @Test
  public void testSpreadsheetPage() throws Exception {
    Cell[][] expectedCells = new Cell[4][4];
    expectedCells[0][0] = new Cell("100");
    when(spreadsheetService.getCells()).thenReturn(expectedCells);

    mockMvc.perform(get("/cells"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("cells", expectedCells))
            .andExpect(view().name("spreadsheet"));

    verify(spreadsheetService).getCells();
  }

  @Test
  public void testUpdateCellValue() throws Exception {
    mockMvc.perform(post("/update")
                    .param("rowTable", "3")
                    .param("colTable", "3")
                    .param("expression", "A1+B1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/cells"));

    verify(spreadsheetService).calcExpression(3, 3, "A1+B1");
  }
}