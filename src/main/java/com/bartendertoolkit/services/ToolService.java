package com.bartendertoolkit.services;

import java.util.List;

public interface ToolService {
    void calculateTips(Long userId, List<String> names, List<Float> hours, int totalTips);
}
