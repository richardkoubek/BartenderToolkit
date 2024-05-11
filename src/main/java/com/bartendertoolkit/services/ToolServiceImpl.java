package com.bartendertoolkit.services;

import com.bartendertoolkit.dtos.TipForm;
import com.bartendertoolkit.dtos.TipResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ToolServiceImpl implements ToolService{
    @Override
    public TipResult calculateTips(TipForm tipForm) {
        if  (tipForm.getNames().size() != tipForm.getHours().size()){
            throw new RuntimeException("name or tip is missing");
        }

        float totalHours = 0f;
        for (float hour : tipForm.getHours()){
            totalHours = totalHours + hour;
        }

        float hourlyTip = tipForm.getTotalTips() / totalHours;
        Map<String, Float> calculatedTips = new HashMap<>();

        for (int i = 0; i < tipForm.getNames().size(); i++) {
            String name = tipForm.getNames().get(i);
            float hour = tipForm.getHours().get(i);
            float tip = (float) Math.ceil(hour * hourlyTip);
            calculatedTips.put(name, tip);
        }

        return new TipResult(calculatedTips);
    }
}
