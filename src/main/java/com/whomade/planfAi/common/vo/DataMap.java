package com.whomade.planfAi.common.vo;

import org.apache.ibatis.type.Alias;
import java.util.LinkedHashMap;

@Alias("dataMap")
public class DataMap extends LinkedHashMap<String, Object> {
    private static final long serialVersionUID = 1L;
}
