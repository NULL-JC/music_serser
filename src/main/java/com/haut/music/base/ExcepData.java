package com.haut.music.base;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.formula.functions.T;

@Getter
@Setter
public class ExcepData {

    private Integer code;

    public ExcepData(Integer code) {
        this.code = code;
    }

}
