package com.bartendertoolkit.controllers;

import com.bartendertoolkit.entities.TipForm;
import com.bartendertoolkit.services.ToolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tools")
public class ToolsController {
    private final ToolService toolService;

    @GetMapping("/tipscalculator")
    public String tipsCalculatorForm(Model model){
        model.addAttribute("tipForm", new TipForm());

        return "calculatorForm";
    }

    @PostMapping("/tipscalculator")
    public String tipsCalculator(
            Model model,
            @ModelAttribute("tipForm") TipForm tipForm
            ){
        Map<String, Float> calculatedTips = toolService.calculateTips(
                tipForm.getNames(),
                tipForm.getHours(),
                tipForm.getTotalTips());
        List<Map.Entry<String, Float>> namesAndTips = new ArrayList<>(calculatedTips.entrySet());
        model.addAttribute("namesAndTips", namesAndTips);

        return "calculatorResults";
    }
}
