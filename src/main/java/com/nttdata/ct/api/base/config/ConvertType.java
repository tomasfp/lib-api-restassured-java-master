package com.nttdata.ct.api.base.config;

import com.nttdata.ct.api.service.constans.ErrorMessageService;
import com.nttdata.ct.api.service.constans.ParseTypeEnum;
import com.nttdata.ct.api.service.util.UtilApi;

import java.util.logging.Level;

public class ConvertType {

    private ConvertType(){
        throw new IllegalStateException("Utility class");
    }

    private static final Class<? extends ConvertType> THIS_CLASS = ConvertType.class;

    public static Object setTypeTo(String newValue) {
        final Object value;
        final String REGEX_TYPE = "::";
        if (newValue.contains(REGEX_TYPE)) {
            String newValueType = newValue.split(REGEX_TYPE)[0];
            String currentNewValue = newValue.split(REGEX_TYPE)[1];
            ParseTypeEnum parseTypeEnum;
            try {
                parseTypeEnum = ParseTypeEnum.valueOf(newValueType);
            } catch (IllegalArgumentException argumentException) {
                UtilApi.logger(THIS_CLASS).log(Level.SEVERE, ErrorMessageService.ERROR_MENSAJE_PARSE_TYPE,
                        new Object[]{newValueType, ParseTypeEnum.getAllParseType()});
                throw new IllegalArgumentException();
            }
            switch (parseTypeEnum) {
                case INT:
                    value = Integer.parseInt(currentNewValue);
                    break;
                case BOOLEAN:
                    value = Boolean.valueOf(currentNewValue);
                    break;
                case DOUBLE:
                    value = Double.valueOf(currentNewValue);
                    break;
                case ARRAY:
                    value = "STRING_ARRAY";
                    break;
                default:
                    value = newValue;
            }
        } else {
            value = newValue;
        }
        return value;
    }

}
