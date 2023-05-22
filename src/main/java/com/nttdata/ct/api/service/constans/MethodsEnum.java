package com.nttdata.ct.api.service.constans;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum MethodsEnum {

    GET,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTIONS,
    HEAD;

    public static List<String> getAllMethods() {
        return Stream.of(MethodsEnum.values())
                .map(MethodsEnum::name)
                .collect(Collectors.toList());
    }

}
