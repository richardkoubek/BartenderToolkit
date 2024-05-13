package com.bartendertoolkit.controllers;

import com.bartendertoolkit.dtos.TipForm;
import com.bartendertoolkit.dtos.TipResult;
import com.bartendertoolkit.services.ToolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/tools")
public class ToolsRestController {
    private final ToolService toolService;

    @PostMapping("/tipscalculator")
    public ResponseEntity<?> tipsCalculator(@RequestBody TipForm tipForm) {
        TipResult calculatedTips = toolService.calculateTips(tipForm);
        return ResponseEntity.ok(calculatedTips);
    }
}
