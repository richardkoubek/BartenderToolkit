package com.bartendertoolkit.controllers;

import com.bartendertoolkit.entities.TipForm;
import com.bartendertoolkit.services.ToolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tools")
public class ToolsController {
    private final ToolService toolService;

    @GetMapping("/tipscalculator/{userId}")
    public String tipsCalculatorForm(Model model){
        model.addAttribute("tipForm", new TipForm());

        return "calculatorForm";
    }

    @PostMapping("/tipscalculator/{userId}")
    public String tipsCalculator(
            Model model,
            @PathVariable Long userId,
            @ModelAttribute("tipForm") TipForm tipForm
            ){
        Map<String, Float> calculatedTips = toolService.calculateTips(
                userId,
                tipForm.getNames(),
                tipForm.getHours(),
                tipForm.getTotalTips());
        model.addAttribute("names", calculatedTips.keySet());
        model.addAttribute("tips", calculatedTips.values());

        return "calculatorResults";
    }
}
