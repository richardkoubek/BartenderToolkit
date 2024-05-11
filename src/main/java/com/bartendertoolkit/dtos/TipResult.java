package com.bartendertoolkit.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class TipResult {
    private Map<String, Float> calculatedTips;
}
