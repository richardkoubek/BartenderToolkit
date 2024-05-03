package com.bartendertoolkit.controllers;

import com.bartendertoolkit.services.ToolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tools")
public class ToolsController {
    private final ToolService toolService;

    @GetMapping("/tipscalculator/{userId}")
    public String tipsCalculator(
            Model model,
            @PathVariable Long userId,
            @RequestParam List<String> names,
            @RequestParam List<Float> hours,
            @RequestParam int totalTips
            ){
        Map<String, Float> calculatedTips = toolService.calculateTips(userId, names, hours, totalTips);

        return "tipsCalculator";
    }
}
