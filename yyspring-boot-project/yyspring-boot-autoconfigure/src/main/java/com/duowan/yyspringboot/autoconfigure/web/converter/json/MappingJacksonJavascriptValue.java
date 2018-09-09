package com.duowan.yyspringboot.autoconfigure.web.converter.json;

import org.springframework.http.converter.json.MappingJacksonValue;

/**
 * @author Arvin
 */
public class MappingJacksonJavascriptValue extends MappingJacksonValue {

    /** JavaScript 参数变量名称 */
    private String javascriptVar;

    /**
     * Create a new instance wrapping the given POJO to be serialized.
     *
     * @param value the Object to be serialized
     */
    public MappingJacksonJavascriptValue(Object value) {
        super(getRealValue(value));
        if (value instanceof MappingJacksonValue) {
            MappingJacksonValue val = (MappingJacksonValue) value;
            this.setValue(val.getValue());
            this.setFilters(val.getFilters());
            this.setSerializationView(val.getSerializationView());
        }
    }

    private static Object getRealValue(Object value) {
        if (value instanceof MappingJacksonValue) {
            return ((MappingJacksonValue) value).getValue();
        }
        return value;
    }

    public String getJavascriptVar() {
        return javascriptVar;
    }

    public void setJavascriptVar(String javascriptVar) {
        this.javascriptVar = javascriptVar;
    }

    @Override
    public String getJsonpFunction() {
        return null;
    }
}
