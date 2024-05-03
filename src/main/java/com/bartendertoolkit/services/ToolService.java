package com.bartendertoolkit.services;

import java.util.List;
import java.util.Map;

public interface ToolService {
    Map<String, Float> calculateTips(List<String> names, List<Float> hours, int totalTips);
}
