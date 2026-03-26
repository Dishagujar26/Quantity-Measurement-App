package com.app.quantitymeasurement.controller;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.app.quantitymeasurement.config.SecurityConfig;
import com.app.quantitymeasurement.dto.QuantityDTO;
import com.app.quantitymeasurement.dto.QuantityInputDTO;
import com.app.quantitymeasurement.dto.QuantityMeasurementDTO;
import com.app.quantitymeasurement.exception.GlobalExceptionHandler;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unit tests for QuantityMeasurementController.
 *
 * Uses @WebMvcTest to load only the web layer — no JPA, no real DB.
 * The service is mocked with @MockBean so controller logic is tested in isolation.
 *
 * Key Mockito concepts:
 *  @MockBean  — injects mock IQuantityMeasurementService into Spring context
 *  Mockito.when() — stubs service method return values
 *  andExpect() — asserts HTTP status codes, JSON content, and headers
 */
@WebMvcTest(QuantityMeasurementController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class QuantityMeasurementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IQuantityMeasurementService service;

    // ── Shared test fixtures ──────────────────────────────────────────────
    private QuantityDTO feetDto;
    private QuantityDTO inchesDto;
    private QuantityDTO kilogramDto;
    private QuantityInputDTO lengthAddInput;

    private QuantityMeasurementDTO addResult;
    private QuantityMeasurementDTO compareResult;
    private QuantityMeasurementDTO convertResult;
    private QuantityMeasurementDTO divideResult;
    private QuantityMeasurementDTO subtractResult;

    @BeforeEach
    void setUp() {
        feetDto    = new QuantityDTO(1.0,  "FEET",     "LengthUnit");
        inchesDto  = new QuantityDTO(12.0, "INCHES",   "LengthUnit");
        kilogramDto = new QuantityDTO(1.0, "KILOGRAM", "WeightUnit");

        lengthAddInput = new QuantityInputDTO(feetDto, inchesDto);

        // ADD result: 1 FEET + 12 INCHES = 2 FEET
        addResult = new QuantityMeasurementDTO();
        addResult.setThisValue(1.0);   addResult.setThisUnit("FEET");    addResult.setThisMeasurementType("LengthUnit");
        addResult.setThatValue(12.0);  addResult.setThatUnit("INCHES");  addResult.setThatMeasurementType("LengthUnit");
        addResult.setOperation("add");
        addResult.setResultValue(2.0); addResult.setResultUnit("FEET");  addResult.setResultMeasurementType("LengthUnit");
        addResult.setError(false);

        // COMPARE result: 1 FEET == 12 INCHES → true
        compareResult = new QuantityMeasurementDTO();
        compareResult.setThisValue(1.0);  compareResult.setThisUnit("FEET");   compareResult.setThisMeasurementType("LengthUnit");
        compareResult.setThatValue(12.0); compareResult.setThatUnit("INCHES"); compareResult.setThatMeasurementType("LengthUnit");
        compareResult.setOperation("compare");
        compareResult.setResultString("true");
        compareResult.setError(false);

        // CONVERT result: 1 FEET → 12.0 INCHES
        convertResult = new QuantityMeasurementDTO();
        convertResult.setThisValue(1.0);  convertResult.setThisUnit("FEET");   convertResult.setThisMeasurementType("LengthUnit");
        convertResult.setOperation("convert");
        convertResult.setResultValue(12.0); convertResult.setResultUnit("INCHES");
        convertResult.setError(false);

        // DIVIDE result: 10 FEET / 2 FEET = 5.0
        divideResult = new QuantityMeasurementDTO();
        divideResult.setThisValue(10.0); divideResult.setThisUnit("FEET");   divideResult.setThisMeasurementType("LengthUnit");
        divideResult.setThatValue(2.0);  divideResult.setThatUnit("FEET");   divideResult.setThatMeasurementType("LengthUnit");
        divideResult.setOperation("divide");
        divideResult.setResultValue(5.0); divideResult.setResultUnit("RATIO");
        divideResult.setError(false);

        // SUBTRACT result: 4 FEET - 24 INCHES = 2 FEET
        subtractResult = new QuantityMeasurementDTO();
        subtractResult.setThisValue(4.0);  subtractResult.setThisUnit("FEET");   subtractResult.setThisMeasurementType("LengthUnit");
        subtractResult.setThatValue(24.0); subtractResult.setThatUnit("INCHES"); subtractResult.setThatMeasurementType("LengthUnit");
        subtractResult.setOperation("subtract");
        subtractResult.setResultValue(2.0); subtractResult.setResultUnit("FEET");
        subtractResult.setError(false);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POST /add
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("POST /add — 1 FEET + 12 INCHES = 2 FEET  →  200 OK")
    void testAdd_LengthUnits_ReturnsCorrectResult() throws Exception {

    	Mockito.when(service.add(
    		    Mockito.any(QuantityDTO.class),
    		    Mockito.any(QuantityDTO.class)
    		))
               .thenReturn(addResult);

        mockMvc.perform(post("/api/v1/quantities/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lengthAddInput)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.operation", is("add")))
            .andExpect(jsonPath("$.resultValue", is(2.0)))
            .andExpect(jsonPath("$.resultUnit", is("FEET")))
            .andExpect(jsonPath("$.error", is(false)));
    }

    @Test
    @DisplayName("POST /add — missing request body  →  400 Bad Request")
    void testAdd_MissingBody_Returns400() throws Exception {

        mockMvc.perform(post("/api/v1/quantities/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /add — incompatible units (LENGTH + WEIGHT)  →  400")
    void testAdd_IncompatibleUnits_Returns400() throws Exception {

        QuantityInputDTO incompatible = new QuantityInputDTO(feetDto, kilogramDto);

        Mockito.when(service.add(any(), any()))
               .thenThrow(new QuantityMeasurementException(
                   "add Error: Cannot perform arithmetic between different measurement categories: LengthUnit and WeightUnit"));

        mockMvc.perform(post("/api/v1/quantities/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(incompatible)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status", is(400)))
            .andExpect(jsonPath("$.message", containsString("Cannot perform arithmetic")));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POST /subtract
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("POST /subtract — 4 FEET - 24 INCHES = 2 FEET  →  200 OK")
    void testSubtract_ReturnsCorrectResult() throws Exception {

        QuantityInputDTO input = new QuantityInputDTO(
            new QuantityDTO(4.0, "FEET", "LengthUnit"),
            new QuantityDTO(24.0, "INCHES", "LengthUnit")
        );

        Mockito.when(service.subtract(any(), any())).thenReturn(subtractResult);

        mockMvc.perform(post("/api/v1/quantities/subtract")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.resultValue", is(2.0)))
            .andExpect(jsonPath("$.resultUnit", is("FEET")));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POST /divide
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("POST /divide — 10 FEET / 2 FEET = 5.0  →  200 OK")
    void testDivide_ReturnsRatio() throws Exception {

        QuantityInputDTO input = new QuantityInputDTO(
            new QuantityDTO(10.0, "FEET", "LengthUnit"),
            new QuantityDTO(2.0, "FEET", "LengthUnit")
        );

        Mockito.when(service.divide(any(), any())).thenReturn(divideResult);

        mockMvc.perform(post("/api/v1/quantities/divide")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.resultValue", is(5.0)))
            .andExpect(jsonPath("$.resultUnit", is("RATIO")));
    }

    @Test
    @DisplayName("POST /divide — divide by zero  →  500 Internal Server Error")
    void testDivide_ByZero_Returns500() throws Exception {

        QuantityInputDTO input = new QuantityInputDTO(
            new QuantityDTO(1.0, "FEET", "LengthUnit"),
            new QuantityDTO(0.0, "INCHES", "LengthUnit")
        );

        Mockito.when(service.divide(any(), any()))
               .thenThrow(new ArithmeticException("Divide by zero"));

        mockMvc.perform(post("/api/v1/quantities/divide")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.status", is(500)));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POST /compare
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("POST /compare — 1 FEET == 12 INCHES  →  resultString 'true'")
    void testCompare_EqualQuantities_ReturnsTrue() throws Exception {

        Mockito.when(service.compare(any(), any())).thenReturn(compareResult);

        mockMvc.perform(post("/api/v1/quantities/compare")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lengthAddInput)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.resultString", is("true")))
            .andExpect(jsonPath("$.operation", is("compare")));
    }

    @Test
    @DisplayName("POST /compare — invalid unit name  →  400 Bad Request")
    void testCompare_InvalidUnit_Returns400() throws Exception {

        QuantityInputDTO bad = new QuantityInputDTO(
            new QuantityDTO(1.0, "FEET", "LengthUnit"),
            new QuantityDTO(12.0, "INCHE", "LengthUnit")  // typo
        );

        Mockito.when(service.compare(any(), any()))
               .thenThrow(new QuantityMeasurementException(
                   "Unit must be valid for the specified measurement type"));

        mockMvc.perform(post("/api/v1/quantities/compare")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bad)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error", is("Quantity Measurement Error")));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POST /convert
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("POST /convert — 1 FEET → 12.0 INCHES  →  200 OK")
    void testConvert_FeetToInches_ReturnsCorrectValue() throws Exception {

        QuantityInputDTO input = new QuantityInputDTO(
            new QuantityDTO(1.0, "FEET",   "LengthUnit"),
            new QuantityDTO(0.0, "INCHES", "LengthUnit")
        );

        Mockito.when(service.convert(any(), anyString())).thenReturn(convertResult);

        mockMvc.perform(post("/api/v1/quantities/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.resultValue", is(12.0)))
            .andExpect(jsonPath("$.resultUnit", is("INCHES")));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // GET  /history/operation/{operation}
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("GET /history/operation/add — returns list of add records")
    void testGetHistoryByOperation_ReturnsMatchingRecords() throws Exception {

        Mockito.when(service.getHistoryByOperation("add"))
               .thenReturn(List.of(addResult));

        mockMvc.perform(get("/api/v1/quantities/history/operation/add"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].operation", is("add")));
    }

    @Test
    @DisplayName("GET /history/operation/compare — empty list when no records")
    void testGetHistoryByOperation_EmptyList() throws Exception {

        Mockito.when(service.getHistoryByOperation("compare"))
               .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/quantities/history/operation/compare"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // GET  /history/type/{measurementType}
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("GET /history/type/LengthUnit — returns length measurement records")
    void testGetHistoryByMeasurementType_ReturnsMatchingRecords() throws Exception {

        Mockito.when(service.getHistoryByMeasurementType("LengthUnit"))
               .thenReturn(List.of(addResult, compareResult));

        mockMvc.perform(get("/api/v1/quantities/history/type/LengthUnit"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // GET  /count/{operation}
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("GET /count/COMPARE — returns operation count as long")
    void testGetOperationCount_ReturnsCount() throws Exception {

        Mockito.when(service.getOperationCount("COMPARE")).thenReturn(3L);

        mockMvc.perform(get("/api/v1/quantities/count/COMPARE"))
            .andExpect(status().isOk())
            .andExpect(content().string("3"));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // GET  /history/errored
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("GET /history/errored — returns error records")
    void testGetErrorHistory_ReturnsErrorRecords() throws Exception {

        QuantityMeasurementDTO errDto = new QuantityMeasurementDTO();
        errDto.setOperation("add");
        errDto.setError(true);
        errDto.setErrorMessage("Cannot perform arithmetic between different measurement categories");

        Mockito.when(service.getErrorHistory()).thenReturn(List.of(errDto));

        mockMvc.perform(get("/api/v1/quantities/history/errored"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].error", is(true)))
            .andExpect(jsonPath("$[0].errorMessage", containsString("Cannot perform arithmetic")));
    }

    @Test
    @DisplayName("GET /history/errored — empty list when no errors")
    void testGetErrorHistory_Empty() throws Exception {

        Mockito.when(service.getErrorHistory()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/quantities/history/errored"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Content-Type negotiation
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("POST /add — response Content-Type is application/json")
    void testContentNegotiation_ResponseIsJSON() throws Exception {

        Mockito.when(service.add(any(), any())).thenReturn(addResult);

        mockMvc.perform(post("/api/v1/quantities/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lengthAddInput)))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Response serialisation
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("POST /add — response body contains all expected fields")
    void testResponseSerialization_AllFieldsPresent() throws Exception {

        Mockito.when(service.add(any(), any())).thenReturn(addResult);

        mockMvc.perform(post("/api/v1/quantities/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lengthAddInput)))
            .andExpect(jsonPath("$.thisValue").exists())
            .andExpect(jsonPath("$.thisUnit").exists())
            .andExpect(jsonPath("$.thisMeasurementType").exists())
            .andExpect(jsonPath("$.operation").exists())
            .andExpect(jsonPath("$.resultValue").exists())
            .andExpect(jsonPath("$.resultUnit").exists())
            .andExpect(jsonPath("$.error").exists());
    }
}