package com.bartendertoolkit.services;

import com.bartendertoolkit.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ToolServiceImpl implements ToolService{
    private final UserRepository userRepository;

    @Override
    public Map<String, Float> calculateTips(List<String> names, List<Float> hours, int totalTips) {
        if  (names.size() != hours.size()){
            throw new RuntimeException("name or tip is missing");
        }

        float totalHours = 0f;
        for (float hour : hours){
            totalHours = totalHours + hour;
        }

        float hourlyTip = totalTips / totalHours;
        Map<String, Float> calculatedTips = new HashMap<>();

        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            float hour = hours.get(i);
            float tip = (float) Math.ceil(hour * hourlyTip);
            calculatedTips.put(name, tip);
        }

        return calculatedTips;
    }
}
