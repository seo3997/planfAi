package com.whomade.planfAi.common.util;

import org.springframework.stereotype.Component;

@Component("coMessageSource")
public class CoMessageSource {
    public String getMessage(String code) {
        return code;
    }

    public String getMessage(String code, Object[] args) {
        return code;
    }
}
