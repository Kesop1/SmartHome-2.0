package com.piotrak.service.action;

import java.util.Map;

public interface Readable {
    
    Map<String, String> getValues();
    
    void setValues(Map<String, String> values);
    
}
