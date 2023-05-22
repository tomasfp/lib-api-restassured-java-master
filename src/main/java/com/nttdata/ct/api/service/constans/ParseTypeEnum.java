package com.nttdata.ct.api.service.constans;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ParseTypeEnum {

    INT, BOOLEAN, DOUBLE, ARRAY;

    public static List<String> getAllParseType() {
        return Stream.of(ParseTypeEnum.values())
                .map(ParseTypeEnum::name)
                .collect(Collectors.toList());
    }

}
