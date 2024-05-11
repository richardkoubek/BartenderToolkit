package com.bartendertoolkit.services;

import com.bartendertoolkit.dtos.TipForm;
import com.bartendertoolkit.dtos.TipResult;


public interface ToolService {
    TipResult calculateTips(TipForm tipForm);
}
