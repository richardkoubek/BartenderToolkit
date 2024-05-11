package com.bartendertoolkit.services;

import com.bartendertoolkit.dtos.TipForm;
import com.bartendertoolkit.dtos.TipResult;

import java.util.List;
import java.util.Map;

public interface ToolService {
    TipResult calculateTips(TipForm tipForm);
}
