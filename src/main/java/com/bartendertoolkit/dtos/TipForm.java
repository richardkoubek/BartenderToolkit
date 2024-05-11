package com.bartendertoolkit.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TipForm {
    private List<String> names;
    private List<Float> hours;
    private int totalTips;

    public TipForm() {
    }
}
